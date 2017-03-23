/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;

public abstract class ControlFrame extends Frame {
	
	
	/**
	 * Holds the version of the protocol, currently 1.
	 */
	protected short version;
	
	/**
	 * Holds the type of ControlFrame
	 */
	protected short type;
	
	public ControlFrame(){
		//The flag of control frame
		setCFlag(true);
		setLength(8);
	}
	
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// Encode CFlag
		
		
		return encodedBytes;
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		return null;
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("ControlFrame.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("ControlFrame.equals()");
	}
	
	/**
	 * Sets the value of version
	 * @param v
	 */
	
	public void setVersion(short v){
		version = v;
	}
	
	/**
	 * Gets the value of version
	 *
	 */
	public short getVersion(){
		return version;
	}
	
	/**
	 * Sets the value of type
	 * @param type
	 */
	public void setType(short type){
		this.type = type;
	}
	/**
	 * Gets the vlaue of type
	 * @return
	 */
	public short getType(){
		return this.type;
	}
}
