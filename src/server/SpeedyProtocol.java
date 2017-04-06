/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/19/2017          *
 ********************************/

package server;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

import serialization.*;
import serialization.exception.*;
import utility.ConstUtility;

/**
 * FoodNetworkProtocol class to be run in a separate Thread for each Client connection requested of the Server
 * @author Feng Yang, Jonathan Myers, Nathan Stickney
 * @version 1.0 Build 3/19/2017
 */
public class SpeedyProtocol implements Runnable {
	
	/**
	 * Socket that contains the connection to the client
	 */
	private Socket clientSock;
	
	/**
	 * Logger for the Server to write to when it has something to say
	 */
	private Logger logger;
	
	/**
	 * InputStream assigned to the client
	 */
	private InputStream in;
	
	/**
	 * MessageInput assigned to the Client
	 */
	private MessageInput min;
	
	/**
	 * OutputStream assigned to the client
	 */
	private OutputStream out;
	
	/**
	 * ID associated with this stream connection
	 */
	private int streamID;
	
	/**
	 * SpeedyProtocol constructor to hold the variables needed at each Client connection
	 * @param clientSock Socket that contains the connection to the client
	 * @param logger Logger for the Server to write to when it has something to say
	 */
	public SpeedyProtocol(Socket clientSock, Logger logger) {
		this.clientSock = clientSock;
		this.logger = logger;
	}
	
	/**
	 * Runnable function to run handleSpeedyClient for each Client connection
	 */
	public void run() {
		try {
			// Get the InputStream and OutputStream from the Client
			getClientInputStream();
			getClientOutputStream();
			
			// Decode the SynStream
			Frame fm = null;
			try {
				fm = receiveFrame();
			} catch (ServerContinueException e) {
				sendErrorMessageAndTerminate(e.getMessage());
			}
			
			// Grab the StreamID
			streamID = ((SynStream)fm).getStreamID();
			
			// Log the Client connection
			logger.info("Handling client " + clientSock.getInetAddress() + "-" + clientSock.getPort() + 
					" with StreamID " + streamID + " with thread id " + Thread.currentThread().getId() + "\n");
			
			while(true) {
				try {
					Frame f = receiveFrame();
					logger.info("Packet received from client " + clientSock.getInetAddress() + "-" + clientSock.getPort() + 
							" with StreamID " + streamID + " with thread id " + Thread.currentThread().getId() + f + "\n");
					
					if(f instanceof DataFrame) {
						
						// Generate Reply
						Frame sendFrame = processDataAndGenerateReply(((DataFrame) f).getData());
						byte[] encodedBytes = sendFrame.encode();
						try {
							out.write(encodedBytes);
						} catch(IOException e) {
							logger.severe("Unable to write to client " + clientSock.getInetAddress() + "-" + clientSock.getPort() + 
									" with StreamID " + streamID + " with thread id " + Thread.currentThread().getId() + "\n");
							closeClient();
							throw new ServerBreakException("Unable to write to client.");
						}
					} else if(f instanceof SynStream) {
						
						throw new ServerContinueException("Unexpected SynStream.");
					} else if(f instanceof FinStream) {
						logger.warning("FinStream received. Closing connection.");
						closeClient();
						throw new ServerBreakException("FinStream received. Closing connection.");
					} else {
						throw new ServerContinueException("Unknown packet type received.");
					}	
				} catch (ServerContinueException e) {
					logger.warning("Warning from client " + clientSock.getInetAddress() + "-" + clientSock.getPort() +
						" with StreamID " + streamID + " with thread id " + Thread.currentThread().getId() + ": " + e.getMessage());
				}
			}
		} catch(ServerBreakException e) {
			System.exit(1);
		}
	}
	
	/**
	 * Processes the InputStream from the Client connection
	 * @param clientSock Socket that contains our connection to the Client
	 * @param logger Logger to write our error if we can't get the InputStream
	 * @return in InputStream connected to our Client
	 */
	public InputStream getClientInputStream() throws ServerBreakException {
		in = null;
		try {
			// Get the InputStream from the client
			in = clientSock.getInputStream();
		} catch(IOException e) {
			// If unable to get the stream, log the issue, exit the connection and close the thread
			logger.severe("Unable to get InputStream from " + clientSock.getInetAddress() + ". Closing connection.");
			closeClient();
		}
		
		min = new MessageInput(in);
		
		return in;
	}
	
	/**
	 * Processes the OutputStream from the Client connection
	 * @return out OutputStream connected to our Client
	 */
	public OutputStream getClientOutputStream() throws ServerBreakException {
		out = null;
		try {
			// Get the OutputStream from the client
			out = clientSock.getOutputStream();
		} catch(IOException e) {
			// If unable to get the stream, log the issue, exit the connection and close the thread
			logger.severe("Unable to get OutputStream from " + clientSock.getInetAddress() + ". Closing connection.");
			closeClient();
		}
		return out;
	}
	
	/**
	 * Decodes and processes a Frame received from the client
	 * @throws ServerContinueException if an issue was detected and a Goaway was successfully sent to the client
	 * @throws ServerBreakException if a severe error that could not be resolved was encountered, and client connection was terminated
	 */
	public Frame receiveFrame() throws ServerBreakException, ServerContinueException {
		
		Frame fm = null;
		
		// Try to decode the frame
		try {
			fm = Frame.decodeFrame(min);
		} catch(SpeedyException e) {
			try {
				sendErrorMessage(e.getMessage());
			} catch (ServerContinueException e1) {
				logger.severe("Error sending GoAway to " + clientSock.getInetAddress() + ". Closing connection.");
				closeClient();
			}
			throw new ServerBreakException("Error decoding client's packet: " + e.getMessage(), e);
		}
		
		logger.info("Received from " + clientSock.getInetAddress() + "-" + clientSock.getPort() + " " + fm.toString() + "\n");
		
		return fm;
	}
	
	/**
	 * Reads the data from a DataFrame and generates the appropriate reply
	 * @param dataBytes - bytes of data read in from the client
	 * @return generated DataFrame containing the reply
	 * @throws ServerContinueException if there are parsing errors
	 */
	public Frame processDataAndGenerateReply(byte[] dataBytes) throws ServerContinueException {
		Frame f = null;
		Scanner scan = new Scanner(new ByteArrayInputStream(dataBytes));
		String fileName = null;
		if(scan.hasNext()) {
			if(scan.next().equalsIgnoreCase("GET")) {
				if(scan.hasNext()) {
					fileName = scan.next();
				} else {
					scan.close();
					throw new ServerContinueException("Invalid file name provided in GET request.");
				}
			} else {
				scan.close();
				throw new ServerContinueException("Unexpected HTTP Request in DataFrame");
			}
		} else {
			scan.close();
			throw new ServerContinueException("Unexpected ending of parse in DataFrame.");
		}
		
		if(fileName != null) {
			try {
				InputStream targetStream = new FileInputStream(new File(fileName));
				byte[] fileData = new byte[targetStream.available()];
				int readBytes = targetStream.read(fileData);
				targetStream.close();
				fileData = Arrays.copyOfRange(fileData, 0, readBytes);
				
				String returnDataString = "HTTP-Version: " + ConstUtility.HTTP_VERSION + " " + ConstUtility.HTTP_CORRECT_VALIDATION + "\n";
				returnDataString += "Content-Length: " + fileData.length + "\n";
				returnDataString += "Content-Type: " + "text/html" + "\n";
				returnDataString += "\n";
				byte[] returnDataHeader = returnDataString.getBytes();
				
				byte[] returnData = new byte[returnDataHeader.length + fileData.length];
				for(int i = 0; i < returnDataHeader.length; i++) {
					returnData[i] = returnDataHeader[i];
				}
				for(int i = 0; i < fileData.length; i++) {
					returnData[returnDataHeader.length + i] = fileData[i];
				}
				f = new DataFrame(streamID, returnData);
				
			} catch(Exception e) {
				scan.close();
				throw new ServerContinueException(e.getMessage(), e);
			}
		}
		scan.close();
		return f;
	}
	
	/**
	 * Whenever the Server comes across a situation in which it needs to throw an ErrorMessage, it calls this method
	 * which creates a Goaway and outputs it to the Client, and throws a ServerContinueException when successful
	 * @param message String to write to the ErrorMessage and to include in any exceptions thrown
	 * @throws ServerContinueException when sending the Goaway was successful
	 * @throws ServerBreakException if there was an error sending the Goaway
	 */
	public void sendErrorMessage(String message) throws ServerContinueException, ServerBreakException {
		logger.warning(message);
		Frame fm = null;
		try {
			fm = new GoAway(streamID);
		} catch(SpeedyException e) {
			throw new ServerBreakException("Could not create GoAway: " + e.getMessage(), e);
		}
		byte[] encodedGoaway = fm.encode();
		try {
			out.write(encodedGoaway);
			logger.info("[Send message] Sent to " + clientSock.getInetAddress() + "-" + clientSock.getPort() + " " + fm.toString() + "\n");
		} catch(IOException e) {
			throw new ServerBreakException("Could not send GoAway: " + e.getMessage(), e);
		}
		
		// Write to log and throw a ServerBreakException upon successful sending of the Goaway
		logger.warning(message);
		throw new ServerContinueException(message);
	}
	
	/**
	 * Whenever the Server comes across a situation in which it needs to throw an ErrorMessage and terminate the connection, 
	 * it calls this method which creates a Goaway and outputs it to the Client, and throws a ServerBreakException when successful
	 * @param message String to write to the ErrorMessage and to include in any exceptions thrown
	 * @throws ServerBreakException if there was an error sending the Goaway
	 */
	public void sendErrorMessageAndTerminate(String message) throws ServerBreakException {
		Frame fm = null;
		try {
			fm = new GoAway(streamID);
		} catch(SpeedyException e) {
			throw new ServerBreakException("Could not create GoAway: " + e.getMessage(), e);
		}
		byte[] encodedGoaway = fm.encode();
		try {
			out.write(encodedGoaway);
			logger.info("[Send message] Sent to " + clientSock.getInetAddress() + "-" + clientSock.getPort() + " " + fm.toString() + "\n");
		} catch(IOException e) {
			throw new ServerBreakException("Could not send GoAway: " + e.getMessage(), e);
		}
		
		// Write to log and throw a ServerBreakException upon successful sending of the Goaway
		logger.warning(message);
		closeClient();
		throw new ServerBreakException(message);
	}
	
	/**
	 * Closes the Client socket and interrupts the current thread
	 */
	public void closeClient() throws ServerBreakException {
		try {
			clientSock.close();
		} catch(IOException e2) {
			// Do nothing
		}
		throw new ServerBreakException("Client closed");
	}	

	

}
