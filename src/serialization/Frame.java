/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import java.io.IOException;
import java.nio.ByteBuffer;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

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
	 * 
	 * @return
	 */
	protected int length;

	public byte[] encode() {
		byte[] encodeBytes = new byte[1];
		return encodeBytes;
	}

	public static Frame decodeFrame(MessageInput msgIn) throws SpeedyException {
		int index = 0;
		index += ConstUtility.FLAGS_BYTE_LENGTH;
		// Get the first 8 bytes for the header of the frame
		byte[] header = null;
		try {
			header = msgIn.getNumberBytes(ConstUtility.FRAME_HEADER_LENGTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int dataLength = decodeLength(ByteUtility.byteSubarray(header, header.length - ConstUtility.LENGTH_BYTE_LENGTH,
				ConstUtility.LENGTH_BYTE_LENGTH));
		System.out.println(dataLength);
		
		boolean cFlag = decodeCFlag(header[0]);
		Frame frame = null;

		int frame_length = ConstUtility.FRAME_HEADER_LENGTH + dataLength;
		byte[] encodedBytes = new byte[frame_length];
		index = 0;
		ByteUtility.copyBytes(encodedBytes, index, header);
		index += header.length;
		
		try {
			ByteUtility.copyBytes(encodedBytes, index, msgIn.getNumberBytes(dataLength));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (cFlag) {// Control frame
			frame = ControlFrame.decode(encodedBytes);
		} else {
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
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		return true;
	}

	/**
	 * Sets the value of CFlag
	 * 
	 * @param flag
	 */
	public void setCFlag(int cflag) {
		if (cflag == 0) {
			CFlag = false;
		} else {
			CFlag = true;
		}
	}

	/**
	 * Get the value of CFlag
	 * 
	 * @return
	 */
	public int getCFlag() {
		if (CFlag)
			return 1;
		return 0;
	}

	/**
	 * Sets the 8 bits that denote the flags for this frame
	 * 
	 * @param b
	 *            byte containing the 8 bits for the flags
	 */
	public void setFlags(byte flags) {
		this.flags = flags;
	}

	/**
	 * Returns the flags of the frame
	 * 
	 * @return flags
	 */
	public byte getFlags() {
		return flags;
	}

	/**
	 * Sets the length
	 * 
	 * @param length
	 * @throws SpeedyException
	 */
	public void setLength(int length) throws SpeedyException {
		if (length > Math.pow(2, 24) - 1) {
			throw new SpeedyException("The lenght is too large.");
		}
		this.length = length;
	}

	/**
	 * Gets the length
	 * 
	 * @param length
	 */
	public int getLength() {
		return this.length;
	}

	/**
	 * Gets the length
	 * 
	 * @return
	 */
	public int sgtLength() {
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
		length += ((int) value[0] << 16) & 0xFF;
		length += ((int) value[1] << 8) & 0xFF;
		length += ((int) value[2]) & 0xFF;

		return length;
	}

	/**
	 * Turn flags and length into byte array
	 * 
	 * @param flags
	 * @param length
	 * @return
	 */
	protected byte[] getBytesFL(byte flags, int length) {
		byte[] fl = new byte[4];
		fl[0] = flags;
		fl[3] = (byte) (length & 0b11111111);
		fl[2] = (byte) (length >> 8 & 0b11111111);
		fl[1] = (byte) (length >> 16 & 0b11111111);
		return fl;
	}

	/**
	 * Decodes the version
	 * 
	 * @param value
	 * @return
	 */
	protected static short decodeVersion(byte[] value) {

		short version = 0;
		value[0] &= 0b01111111;
		version = ByteBuffer.wrap(value).getShort();
		return version;
	}
}