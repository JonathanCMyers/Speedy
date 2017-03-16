/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.KeyStore;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

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
		
		//setupLogger();	
		//testArgs(args);
		
		String ksName = "src/server/SSL.jks";
		char ksPass[] = "icecream".toCharArray();
		char ctPass[] = "icecream".toCharArray();
		try {
			KeyStore ks = KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(ksName), ksPass);
			System.out.println("Loaded key.");
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
			kmf.init(ks, ctPass);
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), null, null);
			SSLServerSocketFactory ssf = sc.getServerSocketFactory();
			SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(8888);
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

This will generate keystore:
keytool -genkey -keystore SSL.jks -keyalg RSA

The keystore must be in the server config.

Now export as a certificate:
keytool -export -keystore SSL.jks -file SSL.cer

Move the SSL.cer to the machine that will include the client. Since it is windows,
keytool.exe -importcert -file H:/csi\ 5321/SPDYworkspace-git/Speedy/src/client/SSL.cer SSL.cer -keystore SSL.jks -storepass icecream

Enter yourPASSWORD and than start your server with ssl debug information(put yourKEYSTORE into directory with SSLServer.class):
java -Djavax.net.ssl.keyStore=yourKEYSTORE -Djavax.net.ssl.keyStorePassword=yourPASSWORD -Djava.protocol.handler.pkgs=com.sun.net.ssl.internal.www.protocol -Djavax.net.debug=ssl SSLServer

Than start your client(put yourKEYSTORE into directory with SSLClient.class):
java -Djavax.net.ssl.trustStore=yourKEYSTORE -Djavax.net.ssl.trustStorePassword=yourPASSWORD SSLClient


keySTORE = SSL.key
keyStorePassword = icecream

*/
