/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/19/2017          *
 ********************************/

package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Logger;

import serialization.Frame;
import serialization.Goaway;
import serialization.exception.ServerBreakException;
import serialization.exception.ServerContinueException;
import serialization.exception.SpeedyException;

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
	 * OutputStream assigned to the client
	 */
	private OutputStream out;
	
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
			in = getClientInputStream();
			out = getClientOutputStream();
			
			// Log the Client connection
			logger.info("Handling client " + clientSock.getInetAddress() + "-" + clientSock.getPort() + 
					" with thread id " + Thread.currentThread().getId() + "\n");

			while(true) {
				try {
					Frame fm = receiveFrame();
				} catch (ServerContinueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		InputStream in = null;
		try {
			// Get the InputStream from the client
			in = clientSock.getInputStream();
		} catch(IOException e) {
			// If unable to get the stream, log the issue, exit the connection and close the thread
			logger.severe("Unable to get InputStream from " + clientSock.getInetAddress() + ". Closing connection.");
			closeClient();
		}
		return in;
	}
	
	/**
	 * Processes the OutputStream from the Client connection
	 * @return out OutputStream connected to our Client
	 */
	public OutputStream getClientOutputStream() throws ServerBreakException {
		OutputStream out = null;
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
		byte[] readBytes = null;
		
		// Try to read from the client
		try {
			in.read(readBytes);
		} catch(IOException e) {
			throw new ServerBreakException("Error reading from client's InputStream: " + e.getMessage(), e);
		}
		
		// Try to decode the frame
		try {
			fm = Frame.decode(readBytes);
		} catch(SpeedyException e) {
			sendErrorMessage(e.getMessage());
			throw new ServerBreakException("Error decoding client's packet: " + e.getMessage(), e);
		}
		
		return fm;
	}
	
	/**
	 * Whenever the Server comes across a situation in which it needs to throw an ErrorMessage, it calls this method
	 * which creates a Goaway and outputs it to the Client, and throws a ServerBreakException when successful
	 * @param message String to write to the ErrorMessage and to include in any exceptions thrown
	 * @throws ServerContinueException when sending the Goaway was successful
	 * @throws ServerBreakException if there was an error sending the Goaway
	 */
	public void sendErrorMessage(String message) throws ServerContinueException, ServerBreakException {
		
		// Try to send a Goaway to the client - if this doesn't work, terminate connection with the client.
		try {
			Frame fm = new Goaway();
			byte[] encodedGoaway = fm.encode();
			out.write(encodedGoaway);
			logger.info("[Send message] Sent to " + clientSock.getInetAddress() + "-" + clientSock.getPort() + " " + fm.toString() + "\n");
		} catch(IOException e) {
			logger.severe("Unable to write to client: " + clientSock.getInetAddress() + ". Closing connection.\n");
			closeClient();
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
