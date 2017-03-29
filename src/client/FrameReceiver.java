/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import serialization.Frame;
import serialization.SynStream;
import serialization.exception.SpeedyException;
import utility.SpeedyUtility;

public class FrameReceiver implements Runnable {
	
	/**
	 * Socket that manages the connection from the client to the server
	 */
	private Socket socket;
	
	/**
	 * InputStream from the socket to receive input from the server
	 */
	private InputStream in;
	
	/**
	 * OutputStream from the socket to write output to the server
	 */
	private OutputStream out;
	
	/**
	 * Contains all frames that have been received by the client, but have not been processed yet
	 */
	private ArrayList<Frame> frameQueue;
	
	public FrameReceiver(Socket socket, InputStream in, OutputStream out, ArrayList<Frame> frameQueue) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.frameQueue = frameQueue;
	}

	
	@Override
	public void run() {
		while(true) {
			Frame f = null;
			try {
				byte[] incomingBytes = new byte[SpeedyUtility.MAX_TCP_PAYLOAD_SIZE];
				int bytesRead = in.read(incomingBytes);
				f = Frame.decode(Arrays.copyOfRange(incomingBytes, 0, bytesRead));
			} catch(SpeedyException e) {
				System.err.println("Error decoding frame: " + e.getMessage());
			} catch (IOException e) {
				System.err.println("Error reading frame from server: " + e.getMessage());
			}
			if(f != null) {
				frameQueue.add((SynStream)f);
				System.out.println("Frame added!");
			}
		}
		
	}
	
}
