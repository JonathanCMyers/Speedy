/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import serialization.SynStream;

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
	
	public static void main(String[] args) {
		
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
			
		}
		
		//handleArgs(args);
		
		SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
		try {
			Socket c = f.createSocket("localhost", 8888);
			((SSLSocket)c).startHandshake();
			printSocketInfo((SSLSocket)c);
			BufferedWriter w =  new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String m = null;
			while((m = r.readLine()) != null) {
				w.write(m, 0, m.length());
				w.newLine();
			}
			w.flush();
			w.close();
			r.close();
			c.close();
		} catch(IOException e) {
			System.err.println(e.toString());
		}
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
	
	
	//TODO: Temporary! Delete later.
	private static void printSocketInfo(SSLSocket s) {
		System.out.println("Socket class: " + s.getClass());
		System.out.println("   Remote address = " + s.getInetAddress().toString());
		System.out.println("   Remote port = " + s.getPort());
		System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
		System.out.println("   Local address = " + s.getLocalAddress().toString());
		System.out.println("   Local port = " + s.getLocalPort());
		System.out.println("   Need client authentication = " + s.getNeedClientAuth());
		SSLSession ss = s.getSession();
		System.out.println("   Cipher suite = " + ss.getCipherSuite());
		System.out.println("   Protocol = " + ss.getProtocol());
	}
}
