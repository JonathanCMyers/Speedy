/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;

public abstract class Frame {
	
	public byte[] encode() {
		return null;
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
}
