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

	private long streamID;
	
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
