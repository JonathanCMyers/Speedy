/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import serialization.Frame;
import serialization.MessageInput;
import serialization.exception.SpeedyException;

public class FrameReceiver extends Thread {
	
	/**
	 * Socket that manages the connection from the client to the server
	 */
	private Socket socket;
	
	/**
	 * InputStream from the socket to receive input from the server
	 */
	private InputStream in;
	
	/**
	 * MessageInput to parse input from the socket
	 */
	private MessageInput min;
	
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
		min = new MessageInput(in);
		this.out = out;
		this.frameQueue = frameQueue;
	}
	
	@Override
	public void run() {
		while(true) {
			Frame f = null;
			try {
				f = Frame.decodeFrame(min);
			} catch(SpeedyException e) {
				System.err.println("Error decoding frame: " + e.getMessage());
				System.exit(1);
			} catch(IllegalArgumentException e) {
				System.err.println(f);
			}
			if(f != null) {
				frameQueue.add(f);
				System.out.println("Frame added: " + f + "\n\n\n");
			}
		}
		
	}
	
	public Frame getTopOfQueue() {
		if(frameQueue.size() == 0) {
			return null;
		} else {
			return frameQueue.get(0);
		}
	}
	
<<<<<<< HEAD
}
=======
}
>>>>>>> refs/remotes/origin/master
