package utility;

import java.net.Socket;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Provides the SSL related client services
 */
public final class ClientFactory {

	/**
	 * SSL algorithm
	 */
	private static final String TLSALG = "TLSv1.2";

	/**
	 * Gets a TLS SSL client socket
	 * 
	 * @param host
	 *            identity of host to connect to
	 * @param port
	 *            port of host to connect to
	 * 
	 * @return connected/initialized socket
	 * 
	 * @throws Exception
	 *             if connection/initialization fails
	 */
	public static Socket getClientSocket(final String host, final int port) throws Exception {
		if(ConstUtility.USING_SSL) {
			// Create and initialize SSL context as a factory for secure sockets
			SSLContext sslContext = SSLContext.getInstance(TLSALG);
			sslContext.init(new KeyManager[0], new TrustManager[] { new ClientFactory.MyTrustManager() },
					new SecureRandom());
			SSLContext.setDefault(sslContext);
			return sslContext.getSocketFactory().createSocket(host, port);
		} else {
			return new Socket(host, port);
		}
	}

	/**
	 * Default X509 trust manager
	 */
	private static class MyTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
}
