/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package server;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.net.ssl.SSLServerSocketFactory;

/**
 * Server class to handle the separate threads when a Client tries to connect
 * @author Jonathan Myers
 * @version 1.6 Build 3/16/2017
 */
public class Server {
	
	/**
	 * Port to initialize the server connection
	 */
	private static int serverPort;
	
	/**
	 * Thread count for the server
	 */
	private static int threadCount;
	
	/**
	 * Logs any warnings or new connections
	 */
	private static Logger logger;
	
	/**
	 * Main function to handle assigning Threads to each Client that tries to connect
	 * @param args Port and ThreadCount
	 * @throws IOException if the Logger initialization does not succeed
	 */
	public static void main(String[] args) throws IOException {
		
		setupLogger();	
		testArgs(args);
		
		SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		//ServerSocket sslserversocket = factory.createServerSocket(port);
	}
	
	public static void setupLogger() throws IOException {
		// Create a new FileHandler to log whatever is needed
		FileHandler fileHandler = new FileHandler("connections.log");
		
		// Create a Logger for logging whatever the Server needs to do
		Logger logger = Logger.getLogger("practical");
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(false);
	}
	
	
	public static void testArgs(String[] args) {
		// Make sure the Port number and ThreadCount are passed in by the user starting the Server
		if(args.length != 2) {
			logger.severe("Parameter(s): <Port> <ThreadCount>");
			throw new IllegalArgumentException("Parameter(s): <Port> <ThreadCount>");
		}

		// Read in the server port and thread count
		serverPort = Integer.parseInt(args[0]);
		threadCount = Integer.parseInt(args[1]);
	}
	
}


/*

This will generate certificate:
keytool -genkey -keystore yourKEYSTORE -keyalg RSA

Enter yourPASSWORD and than start your server with ssl debug information(put yourKEYSTORE into directory with SSLServer.class):
java -Djavax.net.ssl.keyStore=yourKEYSTORE -Djavax.net.ssl.keyStorePassword=yourPASSWORD -Djava.protocol.handler.pkgs=com.sun.net.ssl.internal.www.protocol -Djavax.net.debug=ssl SSLServer

Than start your client(put yourKEYSTORE into directory with SSLClient.class):
java -Djavax.net.ssl.trustStore=yourKEYSTORE -Djavax.net.ssl.trustStorePassword=yourPASSWORD SSLClient


keySTORE = SSL.key
keyStorePassword = icecream

*/
