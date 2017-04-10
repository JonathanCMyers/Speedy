/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

import serialization.*;
import serialization.exception.SpeedyException;
import utility.ClientFactory;
import utility.ConstUtility;

import java.util.ArrayList;
import java.util.Random;

/**
 * Client to connect and retrieve data from a server
 * @author Jonathan Myers
 * @version 1.6 Build 3/16/2017
 */
public class Client {
	
	/**
	 * Name of the server, passed in as the first argument by the user
	 */
	private static String serverName;
	
	/**
	 * Port number of the server, passed in as the second argument by the user
	 */
	private static int serverPort;
	
	/**
	 * Socket that manages the connection from the client to the server
	 */
	private static Socket socket;
	
	/**
	 * ID used to communicate with the server
	 */
	private static int streamID;
	
	/**
	 * InputStream from the socket to receive input from the server
	 */
	private static InputStream in;
	
	/**
	 * MessageInput from the socket to receive input from the server
	 */
	private static MessageInput min;
	
	/**
	 * OutputStream from the socket to write output to the server
	 */
	private static OutputStream out;
	
	/**
	 * Contains all frames that have been received by the client, but have not been processed yet
	 */
	private static ArrayList<Frame> frameQueue;
	
	/**
	 * Thread to receive all incoming frames
	 */
	private static Thread frameReceiver;
	
	
	/**
	 * Makes a simple HTTP GET Request to the server
	 * @param args
	 */
	public static void main(String[] args) {
		handleArgs(args); // Store the port number and server from the arguments given
		createSocket(); // Create socket that is connected to server on specified port
		sendSynStream(); // Send a SynStream to the server
		requestPage("/"); // Send an HTTP GET request
		receiveHTTPReply(); // Receive HTTP reply
	}
	
	/**
	 * Tests the arguments passed into Main
	 * @param args Arguments passed to the Client by the User
	 */
	public static void handleArgs(String[] args) {
		// Make sure the user has only input the server and port number
		if(args.length != 2) {
			System.err.println("Unable to communicate: Correct Parameter(s): <Server> <Port>");
			System.exit(1);
		}
		
		// Store the port number
		try {
			serverPort = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			System.err.println("Unable to communicate: Port number must be an integer");
			System.exit(1);
		}
		serverName = args[0];
		frameQueue = new ArrayList<Frame>();
	}
	
	/**
	 * Initializes the private Socket, InputStream, and OutputStream variables
	 */
	public static void createSocket() {
		try {
			socket = ClientFactory.getClientSocket(serverName, serverPort);
			in = socket.getInputStream();
			out = socket.getOutputStream();
			min = new MessageInput(in);
		} catch(UnknownHostException e) {
			System.err.println("Unable to communicate: Unknown Host.");
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Unable to communicate: IOException from socket initialization");
			System.exit(1);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Initializes the frame receiver, which stores each frame it receives
	 */
	public static void initializeFrameReceiver() {
		frameReceiver = new FrameReceiver(socket, in, out, frameQueue);
		frameReceiver.start();
	}
	
	/**
	 * Creates a SynStream with a random odd streamID, and sends it to the server
	 */
	public static void sendSynStream() {
		Random r = new Random(2584);
		streamID = 2 * r.nextInt(200) + 1; // Must be odd
		byte[] encodedBytes = null;
		try {
			SynStream s = new SynStream(streamID);
			encodedBytes = s.encode();
			out.write(encodedBytes);
		} catch(SpeedyException e) {
			System.err.println("Error initializing SynStream: " + e.getMessage());
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Error sending SynStream to server: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public static void requestPage(String fileRequest) {
		String pageRequest = "GET " + fileRequest + " " + ConstUtility.HTTP_VERSION + "\n";
		pageRequest += ConstUtility.REQUEST_SOURCE + "\n";
		pageRequest += ConstUtility.USER_AGENT + "\n";
		
		
		byte[] dataBytes = pageRequest.getBytes();
		try {
			System.out.println(streamID);
			Frame df = new DataFrame(streamID, dataBytes);
			byte[] encodedBytes = df.encode();
			System.out.println("Writing " + encodedBytes.length + " bytes.");
			out.write(encodedBytes);
		} catch (SpeedyException e) {
			System.err.println("Error creating DataFrame: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error sending DataFrame to server: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public static void receiveHTTPReply() {
		Frame f = null;
		try {
			f = Frame.decodeFrame(min);
		} catch(SpeedyException e) {
			System.err.println("Error decoding message: " + e.getMessage());
			System.exit(1);
		}
		System.out.println(f);
	}
}
