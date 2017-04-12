/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import java.util.Arrays;

import serialization.exception.DataFrameException;
import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class DataFrame extends Frame {

	/**
	 * Holds the ID of the Stream in which the frame is Only the last 31 bits
	 * are used.Including all non-negative integer.
	 */
	private int streamID;

	/**
	 * Holds the data in the payload.
	 */
	private byte[] data;

	/**
	 * Contructor for DataFrame
	 * 
	 * @param streamID
	 * @throws SpeedyException
	 */
	public DataFrame(int streamID) throws SpeedyException {
		setStreamID(streamID);
		setData();
		setLength(ConstUtility.DEFAULT_DATA_LENGTH);
	}

	/**
	 * Constructor for DataFrame
	 * 
	 * @param streamID
	 * @param data
	 * @throws SpeedyException
	 */
	public DataFrame(int streamID, byte[] data) throws SpeedyException {
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

		byte[] streamid = ByteUtility.uint32ToEndian(streamID);
		ByteUtility.copyBytes(encodedBytes, index, streamid);
		index += streamid.length;

		// Encode length
		// Encode Flags,Length
		byte[] fl = getBytesFL(flags, length);
		ByteUtility.copyBytes(encodedBytes, index, fl);
		index += fl.length;
		// Encode data
		ByteUtility.copyBytes(encodedBytes, index, data);

		return encodedBytes;
	}

	public static DataFrame decode(byte[] encodedBytes) throws SpeedyException {
		if (encodedBytes == null || encodedBytes.length < ConstUtility.DATA_STREAM_HEADER_LENGTH) {
			throw new SpeedyException("EncodedBytes is illigal.");
		}
		int index = 0;
		int streamID = ByteUtility
				.EndianToUINT32(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.STREAMID_BYTE_LENGTH));
		byte[] data = ByteUtility.byteSubarray(encodedBytes, ConstUtility.DATA_STREAM_HEADER_LENGTH,
				encodedBytes.length - ConstUtility.DATA_STREAM_HEADER_LENGTH);
		return new DataFrame(streamID, data);
	}

	@Override
	public String toString() {
		return "[DataFrame]: [ID = " + streamID + "] [Data: " + new String(data) + "]";
	}

	/**
	 * Check current object equals to the obj
	 */
	@Override
	public boolean equals(Object obj) {

		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DataFrame objf = (DataFrame) obj;
		// Check streamID
		if (this.streamID != objf.getStreamID()) {
			return false;
		}
		// Check data
		if (!Arrays.equals(this.data, objf.getData())) {
			return false;
		}
		return true;
	}

	/**
	 * Assigns the a value to streamID in the frame
	 * 
	 * @param id
	 * @throws SpeedyException
	 */
	public void setStreamID(int id) throws SpeedyException {
		if (id <= 0) {
			throw new SpeedyException("StreamID should be bigger than 0.");
		}
		if (id > (int) Math.pow(2, 31) - 1) {
			throw new SpeedyException("StreamID is too large. Cannot be greater than 2^31 - 1");
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

}
