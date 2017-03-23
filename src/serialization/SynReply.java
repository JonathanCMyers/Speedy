/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;

public class SynReply extends ControlFrame {
	
	/**
	 * Holds the streamID of the Frame
	 */
	private int streamID;

	/**
	 * Holds the Associated-To-Stream-ID
	 * 
	 * @param streamID
	 */
	private int associateStreamID;

	/**
	 * Holds the slot of the frame
	 */
	private byte slot;

	/**
	 * Holds the length of the header in the frame
	 */
	private final static int lengthOfHeader = 16;

	/**
	 * Holds the number of name/value pairs
	 */
	private short numOfPairs;

	/**
	 * Holds the HeaderBlock for the frame
	 * 
	 * @param streamID
	 */
	private HeaderBlock headerBlock;
	
	public SynReply(long streamID) {
		setStreamID(streamID);
	}
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// TODO
		throw new UnsupportedOperationException("SynReply.encode");
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		throw new UnsupportedOperationException("SynReply.decode");
	}
	
	public void setStreamID(long streamID) {
		// TODO
		throw new UnsupportedOperationException("SynReply.setStreamID");
	}
	
	public long getStreamID() {
		return streamID;
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("SynReply.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("SynReply.equals()");
	}
	
	

}
