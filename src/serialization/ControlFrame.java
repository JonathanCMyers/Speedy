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
		throw new UnsupportedOperationException("ControlFrame.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("ControlFrame.equals()");
	}
	
}
