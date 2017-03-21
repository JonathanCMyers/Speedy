/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;

public abstract class Frame {
	/**
	 * Holds the type of Frame, 0:DATA FRAME, 1: CONTROL FRAME
	 */
	boolean CFlag;
	
	/**
	 * 8 bits containing the flags for the frame
	 */
	protected byte flags;
	
	/**
	 * Holds the length of data in the frame, only the last 24 bits are used
	 * @return
	 */
	protected int length;
	public byte[] encode() {
		byte[] encodeBytes = new byte[1];
		return encodeBytes;
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		return null;
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("Frame.toString()");
	}
	
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("Frame.equals()");
	}
	
	/**
	 * Sets the value of CFlag
	 * @param flag
	 */
	public void SetCFlags(boolean flag){
		CFlag = flag;
	}
	
	/**
	 * Get the value of CFlag
	 * @return
	 */
	public boolean GetFlags(){
		return CFlag;
	}
	
	/**
	 * Sets the 8 bits that denote the flags for this frame
	 * @param b byte containing the 8 bits for the flags
	 */
	public void setFlags(byte flags) {
		this.flags = flags;
	}
	
	
	/**
	 * Returns the flags of the frame
	 * @return flags
	 */
	public byte getFlags() {
		return flags;
	}
	
}
