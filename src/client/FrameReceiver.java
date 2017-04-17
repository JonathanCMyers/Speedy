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

import serialization.DataFrame;
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
		
		byte[] replyBytes = new byte[3223300];
		int bytesRead = -1;
		try {
			bytesRead = in.read(replyBytes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(bytesRead > 0) {
			int i = 0;
			int oldCount = 0;
			for(int j = 0; j < 3; j++) {
				byte[] currentBytes = new byte[10000];
				System.out.println("\n\n[Data]\n\n");
				for(byte b : replyBytes) {
					if(b != 0) {
						currentBytes[i] = b;
						i++;
					}
					else {
						break;
					}
					System.out.println(new String(Arrays.copyOfRange(currentBytes, oldCount, i)));
					oldCount = i;
				}
			}
		}
		
		
		/*
		int counter = 0;
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
				counter++;
				System.out.println(counter);
				System.out.println(((DataFrame)f).getLength());
				System.out.println("Reply ID: " + ((DataFrame)f).getStreamID());
				frameQueue.add(f);
				byte[] dataBytes = ((DataFrame)f).getData();
				byte[] nonShortenedBytes = new byte[((DataFrame)f).getLength()];
				int i = 0;
				for(int j = 0; j < 3; j++) {
					System.out.println("\n[Data]\n");
					for(byte b : dataBytes) {
						if(b != 0) {
							//System.out.print(b);
							nonShortenedBytes[i] = b;
							i++;
						} else {
							break;
						}
					}
					System.out.println(new String(Arrays.copyOfRange(nonShortenedBytes, 0, i)));
				}
				//System.out.println("Frame added: " + f + "\n\n\n");
			}
		}
		*/
		//
	}
	
	public Frame getTopOfQueue() {
		if(frameQueue.size() == 0) {
			return null;
		} else {
			return frameQueue.get(0);
		}
	}
}