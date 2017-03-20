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

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;

import java.util.Scanner;

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
	 * InputStream from the socket to receive input from the server
	 */
	private static InputStream in;
	
	/**
	 * OutputStream from the socket to write output to the server
	 */
	private static OutputStream out;
	
	public static void main(String[] args) {
		
		handleArgs(args); // Store the port number and server from the arguments given
		createSocket(); // Create socket that is connected to server on specified port
		//Scanner reader = new Scanner(System.in); // Create the scanner to read in the input from the user
		
		// Request page
		// Receive multiplexed elements of page
		// Reconstruct page
		
		
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
	}
	
	/**
	 * Initializes the private Socket, InputStream, and OutputStream variables
	 */
	public static void createSocket() {
		try {
			socket = new Socket(serverName, serverPort);
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch(UnknownHostException e) {
			System.err.println("Unable to communicate: Unknown Host.");
			System.exit(1);
		} catch(IOException e) {
			System.err.println("Unable to communicate: IOException from socket initialization");
			System.exit(1);
		}
	}
}
