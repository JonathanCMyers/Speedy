package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

/**
 * Provides the TLS related server services
 */
public class ServerFactory {

	/**
	 * Certificate storage type
	 */
	private static final String STORETYPE = "JKS";
	/**
	 * Key management type
	 */
	private static final String KEYMGRTYPE = "SunX509";
	/**
	 * SSL algorithm
	 */
	private static final String TLSALG = "TLS";

	/**
	 * Creates and initializes SSL context for server
	 * 
	 * @return fully initialized SSL context
	 * 
	 * @throws Exception
	 *             if context creation fails
	 */
	private static SSLContext getSSLContext(final String keystorefile, final String keystorepassword) throws Exception {

		// Create keystore
		KeyStore ks = KeyStore.getInstance(STORETYPE);

		// Load keystore from file
		ks.load(new FileInputStream(keystorefile), keystorepassword.toCharArray());

		// Create key manager factory and init with keystore
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEYMGRTYPE);
		kmf.init(ks, keystorepassword.toCharArray());

		// Create SSL context and init with key factory
		SSLContext sslCtx = SSLContext.getInstance(TLSALG);
		sslCtx.init(kmf.getKeyManagers(), null, null);

		return sslCtx;
	}

	/**
	 * Create initialized listening socket
	 * 
	 * @param port
	 *            port to listen
	 * @param keystorefile
	 *            name of key store file
	 * @param keystorepassword
	 *            password for key store file
	 * 
	 * @return initialized server socket
	 * 
	 * @throws Exception
	 *             if unable to create socket
	 */
	public static ServerSocket getServerListeningSocket(final int port, final String keystorefile,
			final String keystorepassword) throws Exception {
		
		if(ConstUtility.USING_SSL) {
			// Create SSL context and get SSL socket factory
			SSLContext sslCtx = getSSLContext(keystorefile, keystorepassword);
			SSLServerSocketFactory servSocketFactory = sslCtx.getServerSocketFactory();
	
			// Generate server socket and set protocol
			SSLServerSocket servSocket = (SSLServerSocket) servSocketFactory.createServerSocket(port);
			servSocket.setEnabledProtocols(new String[] { "TLSv1" });
	
			return servSocket;
		} else {
			return new ServerSocket(port);
		}
	}

	/**
	 * Block until connection then return new, connected socket
	 * 
	 * @param servSocket
	 *            socket waiting on connections
	 * 
	 * @return connected socket
	 * 
	 * @throws IOException
	 *             if problem handling new connection
	 */
	public static Socket getServerConnectedSocket(final ServerSocket servSocket) throws IOException {
		return servSocket.accept();
	}
}
