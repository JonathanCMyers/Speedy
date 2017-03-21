/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.DataFrameException;
import serialization.exception.SpeedyException;

public class DataFrame extends Frame {
	
	/**
	 * Holds the ID of the Stream in which the frame is
	 * Only the last 31 bits are used.Including all non-negative integer.
	 */
	protected int StreamID;
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// TODO
		return encodedBytes;
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		return null;
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("DataFrame.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("DataFrame.equals()");
	}
	
	/**
	 * Assigns the a value to streamID in the frame
	 * @param id
	 */
	public void setStreamID(int id){
		if(id < 0){
			System.err.println("Stream ID should be non-negative integer.");
		}
		StreamID = id;
	}

	/**
	 * Gets the streamID of the frame
	 * @param id
	 */
	public void getStreamID(int id){
		if(id < 0){
			System.err.println("Stream ID should be non-negative integer.");
		}
		StreamID = id;
	}
}
