/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import java.nio.ByteBuffer;

import serialization.exception.SpeedyException;
import utility.ByteUtility;

public abstract class Frame {
	/**
	 * Holds the type of Frame, 0:DATA FRAME, 1: CONTROL FRAME
	 */
	protected boolean CFlag;
	
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
		boolean cFlag = decodeCFlag(encodedBytes[0]);
		Frame frame = null;
		if(cFlag){//Control frame
			frame = ControlFrame.decode(encodedBytes);
		}else{
			frame = DataFrame.decode(encodedBytes);
		}
		return frame;
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
	public void setCFlag(int cflag){
		if(cflag == 0){
			CFlag = false;
		}else{
			CFlag = true;
		}
	}
	
	/**
	 * Get the value of CFlag
	 * @return
	 */
	public int getCFlag(){
		if(CFlag) return 1;
		return 0;
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
	
	/**
	 * Sets the length
	 * @param length
	 */
	public void setLength(int length){
		this.length = length;
	}
	
	/**
	 * Gets the length
	 * @return
	 */
	public int sgtLength(){
		return length;
	}
	
	/**
	 * Gets the cFlag
	 * 
	 * @param cf
	 * @return
	 */
	protected static boolean decodeCFlag(byte cf) {
		boolean cFlag = false;
		if ((cf & 0b10000000) != 0b0000000) {
			cFlag = true;
		}
		return cFlag;
	}
	
	
	
	
	/**
	 * Decodes the length
	 * 
	 * @param value
	 * @return
	 */
	protected static int decodeLength(byte[] value) {
		if (value.length != 3) {
			System.err.println("decodeLength error: the length should be 24 bits.");
		}
		int length = 0;
		length += value[0] << 16;
		length += value[1] << 8;
		length += value[2];
		return length;
	}
}
