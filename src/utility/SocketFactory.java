package utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.*;

import serialization.exception.SpeedyException;

public class SocketFactory {
	
	public static Socket createClientSocket(String serverAddress, int serverPort) throws SpeedyException {
		Socket socket = null;		
		if(SpeedyUtility.USING_SSL) {
			TrustManager[] trustAllCerts = new TrustManager[] {
					new X509TrustManager() {
						public X509Certificate[] getAcceptedIssuers() {
							return new X509Certificate[0];
						}
						public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	
						}
						public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
	
						}
					}
				};
				
			// Install malicious TrustManager
			try {
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			} catch(GeneralSecurityException e) {
				throw new SpeedyException("Error installing TrustManager: " + e.getMessage(), e);
			}
			
			SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
			try {
				socket = f.createSocket(serverAddress, serverPort);
				((SSLSocket)socket).startHandshake();
			} catch (Exception e) {
				throw new SpeedyException("Error creating Socket: " + e.getMessage(), e);
			}
		} else {
			try {
				socket = new Socket(serverAddress, serverPort);
			} catch (Exception e) {
				throw new SpeedyException("Error creating Socket: " + e.getMessage(), e);
			}
		}
		return socket;
	}
	
	public static ServerSocket createServerSocket(int port) throws SpeedyException {
		ServerSocket serverSocket = null;
		if(SpeedyUtility.USING_SSL) {
			String ksName = "src/server/SSL.jks";
			char ksPass[] = "icecream".toCharArray();
			char ctPass[] = "icecream".toCharArray();
			try {
				KeyStore ks = KeyStore.getInstance("JKS");
				InputStream fis = new FileInputStream(ksName);
				ks.load(fis, ksPass);
				System.out.println("Loaded key.");
				KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
				kmf.init(ks, ctPass);
				SSLContext sc = SSLContext.getInstance("TLS");
				sc.init(kmf.getKeyManagers(), null, null);
				SSLServerSocketFactory ssf = sc.getServerSocketFactory();
				serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
				fis.close();
			} catch(Exception e) {
				throw new SpeedyException("Error initializing server socket: " + e.getMessage(), e);
			}
		} else {
			try {
				serverSocket = new ServerSocket(port);
			} catch (IOException e) {
				throw new SpeedyException("Error initializing server socket: " + e.getMessage(), e);
			}
		}
		return serverSocket;
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
