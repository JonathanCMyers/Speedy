/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.DataFrameException;
import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class DataFrame extends Frame {

	/**
	 * Holds the ID of the Stream in which the frame is Only the last 31 bits
	 * are used.Including all non-negative integer.
	 */
	protected int streamID;

	/**
	 * Holds the data in the payload.
	 */
	byte[] data;

	/**
	 * 
	 * @param streamID
	 */
	public DataFrame(int streamID) {
		setStreamID(streamID);
		setData();
		setLength(data.length);
	}

	public DataFrame(int streamID, byte[] data) {
		setStreamID(streamID);
		setData(data);
		setLength(data.length);
	}

	/**
	 * Sets the value of payload
	 * 
	 * @param streamID
	 */
	public void setData(byte[] data) {
		this.data = data;
	}

	/**
	 * Sets the value of payload
	 * 
	 * @param streamID
	 */
	public void setData() {
		this.data = new byte[0];
	}

	/**
	 * Gets the value of payload
	 * 
	 * @param streamID
	 */
	public byte[] getData() {
		return data;
	}

	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		encodedBytes = new byte[ConstUtility.DATA_STREAM_HEADER_LENGTH + length];
		int index = 0;
		// Encode CFlag, always 0
		byte[] streamid = ByteUtility.uint32ToLittleEndian(streamID);
		ByteUtility.copyBytes(encodedBytes, index, streamid);
		index += streamid.length;

		// Encode length
		// Encode Flags,Length
		byte[] fl = getBytesFL(flags, length);
		ByteUtility.copyBytes(encodedBytes, index, fl);
		index += fl.length;
		//Encode data
		ByteUtility.copyBytes(encodedBytes, index, data);
		return encodedBytes;
	}

	public static DataFrame decode(byte[] encodedBytes) throws SpeedyException {
		int index = 0;
		int streamID = ByteUtility.littleEndianToUINT32(ByteUtility.byteSubarray(encodedBytes, index, Integer.SIZE/8));
		byte[] data = ByteUtility.byteSubarray(encodedBytes, ConstUtility.DATA_STREAM_HEADER_LENGTH, encodedBytes.length - ConstUtility.DATA_STREAM_HEADER_LENGTH);
		return new DataFrame(streamID,data);
	}

	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("DataFrame.toString()");
	}

	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("DataFrame.equals()");
	}

	/**
	 * Assigns the a value to streamID in the frame
	 * 
	 * @param id
	 */
	public void setStreamID(int id) {
		if (id < 0) {
			System.err.println("Stream ID should be non-negative integer.");
		}
		streamID = id;
	}

	/**
	 * Gets the streamID of the frame
	 * 
	 * @param id
	 */
	public int getStreamID() {
		return streamID;
	}
	
	/**
	 * Turn flags and length into byte array
	 * 
	 * @param flags
	 * @param length
	 * @return
	 */
	private byte[] getBytesFL(byte flags, int length) {
		byte[] fl = new byte[4];
		fl[0] = flags;
		fl[1] = (byte) (length >> 8 & 0b11111111);
		fl[2] = (byte) (length >> 16 & 0b11111111);
		fl[3] = (byte) (length >> 24 & 0b11111111);
		return fl;
	}
}
