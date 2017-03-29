/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;

public class RstStream extends ControlFrame {
	
	public RstStream() throws SpeedyException {
		throw new UnsupportedOperationException("RstStream()");
	}
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		throw new UnsupportedOperationException("RstStream.encode()");
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		throw new UnsupportedOperationException("RstStream.decode()");
	}
	
	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("RstStream.toString()");
	}
	
	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("RstStream.equals()");
	}

}
