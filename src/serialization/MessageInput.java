/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/29/2017          *
 ********************************/

package serialization;

import java.io.IOException;
import java.io.InputStream;

/**
 * MessageInput class that wraps around an InputStream to manage the buffer on the file being read for SPDy
 */
public class MessageInput {
	
	/**
	 * Holds the data we are trying to read
	 */
	private InputStream in;
	
	/**
	 * MessageInput constructor that manages an InputStream passed in
	 * @param in InputStream given to MessageInput for MessageInput to parse through
	 */
	public MessageInput(InputStream in) {
		this.in = in;
	}
	
	public byte getFlag() throws IOException {
		byte[] flag = new byte[1];
		in.read(flag);
		return flag[0];
	}
	
	public byte[] getNumberBytes(int length) throws IOException {
		byte[] returnBytes = new byte[length];
		in.read(returnBytes);
		return returnBytes;
	}
	
}
