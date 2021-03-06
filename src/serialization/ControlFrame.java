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
import utility.ConstUtility;

public abstract class ControlFrame extends Frame {

	/**
	 * Holds the version of the protocol, currently 1.
	 */
	protected short version;

	/**
	 * Holds the type of ControlFrame
	 */
	protected short type;

	public ControlFrame() throws SpeedyException {
		// The flag of control frame
		setCFlag(1);
		setVersion(ConstUtility.VERSION);
		setLength(8);
	}

	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// Encode CFlag

		return encodedBytes;
	}

	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		Frame frame = null;
		// Decode CFlag
		int index = 0;
		boolean cFlag = decodeCFlag(encodedBytes[index]);
		// Decode version
		short version = decodeVersion(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.VERSION_BYTE_LENGTH));
		index += ConstUtility.VERSION_BYTE_LENGTH;
		
		// Decode type
		FrameType type = FrameType.getFrameType(ByteUtility
				.endianToUINT16(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.TYPE_BYTE_LENGTH)));
		switch(type){
		case SYN_STREAM:
			frame = SynStream.decode(encodedBytes);
			break;
		case SYN_REPLY:
			frame = SynReply.decode(encodedBytes);
			break;
		case FIN_STREAM:
			frame = SynReply.decode(encodedBytes);
			break;
		case GOAWAY:
			frame = GoAway.decode(encodedBytes);
			break;
		default:
			throw new SpeedyException("Unexpected type:"+type);
		}
		return frame;
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
	 * 
	 * @param v
	 */

	public void setVersion(short version) {
		this.version = version;
	}

	/**
	 * Gets the value of version
	 *
	 */
	public short getVersion() {
		return version;
	}

	/**
	 * Sets the value of type
	 * 
	 * @param type
	 */
	public void setType(short type) {
		this.type = type;
	}

	/**
	 * Gets the vlaue of type
	 * 
	 * @return
	 */
	public short getType() {
		return this.type;
	}

	/**
	 * Gets the byte array of CFlag,Version and Type
	 */
	protected byte[] getBytesCVT(boolean CFlag, short version, short type) {
		byte[] cvt = new byte[4];
		System.out.println(version);
		cvt[0] = (byte) (version >> 8 & 0b11111111);
		cvt[1] = (byte) (version & 0b11111111);

		if (CFlag) {// For control frame
			cvt[0] = (byte) (cvt[0] | 0b10000000);
		}

		byte[] typebytes = ByteUtility.uint16ToEndian(type);
		ByteUtility.copyBytes(cvt, 2, typebytes);

		return cvt;
	}

	

	/**
	 * Encode header of the control frame
	 */
	protected byte[] encodeHeader() {
		byte[] headerBytes = new byte[ConstUtility.CONTROL_HEADER_BYTE_LENGTH];
		int index = 0;
		// Encode CFlag,Version,Type(4 bytes)
		byte[] cvt = getBytesCVT(CFlag, version, type);
		ByteUtility.copyBytes(headerBytes, index, cvt);
		index += cvt.length;
		// Encode Flags,Length
		byte[] fl = getBytesFL(flags, length);
		ByteUtility.copyBytes(headerBytes, index, fl);

		return headerBytes;
	}

	
	
}
