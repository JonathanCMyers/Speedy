/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import utility.ServerFactory;

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
		
		// Create the ServerSocket
		try(ServerSocket servSock = ServerFactory.getServerListeningSocket(serverPort, "src/keystore", "icecream")) {
			// Set SO_REUSEADDR
			servSock.setReuseAddress(true);
			// Executor to handle threadCount number of Threads
			Executor service = Executors.newFixedThreadPool(threadCount);
			
			// Run forever, accepting and spawning threads to service each connection
			while(true) {
				// Try to accept a new client
				Socket clientSock = null;
				try {
					clientSock = ServerFactory.getServerConnectedSocket(servSock);
					//clientSock.setSoTimeout(10000); // Set the Client socket timeout to 10 seconds
				} catch (IOException e) {
					logger.warning("Issue connecting to a client");
					throw new IOException(e.getMessage());
				}
				
				// Execute the FoodNetworkProtocol for each Client that connects
				service.execute(new SpeedyProtocol(clientSock, logger));
			}
		} catch(Exception e) {
			logger.severe(e.getMessage());
			throw new IOException(e.getMessage());
		}
		
		
	}
	
	public static void setupLogger() throws IOException {
		// Create a new FileHandler to log whatever is needed
		FileHandler fileHandler = new FileHandler("connections.log");
		
		// Create a Logger for logging whatever the Server needs to do
		logger = Logger.getLogger("practical");
		logger.addHandler(fileHandler);
		logger.setUseParentHandlers(true);
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
 * public class TestServer {
	public static void main(final String[] args) throws Exception {
		ServerSocket listSock = getServerListeningSocket(14999, "src/keystore", "icecream");

		Socket cSock = getServerConnectedSocket(listSock);
		if (cSock.getInputStream().read() == 1) {
			System.out.println("Success");
		} else {
			System.err.println("Failure");
		}
		cSock.getOutputStream().write(2);
		cSock.close();
		listSock.close();
	}
}

 */






/*
 * 
		
		
		
		
		
		
		
		
		
		try {

			
			ServerSocket s = SocketFactory.createServerSocket(serverPort);
			System.out.println("ServerSocket created.");
			SSLSocket c = (SSLSocket) s.accept();
			System.out.println("SSLSocket created.");
			BufferedWriter w =  new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String m = "Welcome to SSL Reverse Echo Server. Please type in some words.";
			w.write(m, 0, m.length());
			w.newLine();
			w.flush();
			while((m = r.readLine()) != null) {
				if(m.equals(".")) {
					break;
				}
				char[] a = m.toCharArray();
				int n = a.length;
				for(int i = 0; i < n/2; i++) {
					char t = a[i];
					a[i] = a[n-1-i];
					a[n-i-1] = t;
				}
				w.write(a, 0, n);
				w.newLine();
				w.flush();
			}
			w.close();
			r.close();
			c.close();
			s.close();
		} catch(Exception e) {
			System.err.println(e.toString());
		}
		
		//SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
		//ServerSocket sslserversocket = factory.createServerSocket(port);
		 * */
