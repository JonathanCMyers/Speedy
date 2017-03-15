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
		
	}
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// TODO
		return encodedBytes;
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		return null;
	}
	
}
