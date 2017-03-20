/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;

public class Goaway extends ControlFrame {
	
	public Goaway() {
		// TODO
		throw new UnsupportedOperationException("new Goaway()");
	}
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// TODO
		throw new UnsupportedOperationException("Goaway.encode()");
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		throw new UnsupportedOperationException("Goaway.decode()");
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("Goaway.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("Goaway.equals()");
	}
	
}
