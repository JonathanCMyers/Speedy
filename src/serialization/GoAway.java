/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 * Update:   3/28/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class GoAway extends ControlFrame {

	private int lastStreamID;

	public GoAway() throws SpeedyException {
		setType(ConstUtility.GOAWAY);
		setLength(ConstUtility.GOAWAY_DEFAULT_LENGTH);
	}

	/**
	 * Constructor for the goaway frame
	 * 
	 * @param lastStreamID
	 * @throws SpeedyException
	 */
	public GoAway(int lastStreamID) throws SpeedyException {
		setType(ConstUtility.GOAWAY);
		setLength(ConstUtility.GOAWAY_DEFAULT_LENGTH);
		setLastStreamID(lastStreamID);

	}

	/**
	 * Encode goaway frame into byte array;
	 * 
	 * @return the byte array
	 */
	public byte[] encode() {
		byte[] encodedBytes = new byte[ConstUtility.GOAWAY_FRAME_LENGTH];
		byte[] header = super.encodeHeader();
		int index = 0;
		ByteUtility.copyBytes(encodedBytes, index, header);
		index += header.length;
		// Encode last stream id
		ByteUtility.copyBytes(encodedBytes, index, ByteUtility.uint32ToLittleEndian(lastStreamID));
		return encodedBytes;
	}

	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// Decode CFlag
		int index = 0;
		boolean cFlag = decodeCFlag(encodedBytes[index]);
		// Decode version
		short version = decodeVersion(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.VERSION_BYTE_LENGTH));
		index += ConstUtility.VERSION_BYTE_LENGTH;

		// Decode type
		short type = (short) ByteUtility
				.littleEndianToUINT16(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.TYPE_BYTE_LENGTH));
		index += ConstUtility.TYPE_BYTE_LENGTH;

		// Decode flags
		byte flags = encodedBytes[index];
		index += ConstUtility.FLAGS_BYTE_LENGTH;
		// Decode length
		int length = decodeLength(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.LENGTH_BYTE_LENGTH));
		index += ConstUtility.LENGTH_BYTE_LENGTH;
		// Decode last stream ID
		int lastStreamID = ByteUtility.littleEndianToUINT32(
				ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.LAST_STREAMID_BYTE_LENGTH));
		Frame goaway = new GoAway(lastStreamID);
		goaway.setLength(length);
		goaway.setFlags(flags);
		return goaway;

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

	/**
	 * Sets the value of the last stream id
	 * 
	 * @param lastStreamID
	 * @throws SpeedyException
	 */
	private void setLastStreamID(int lastStreamID) throws SpeedyException {
		if (lastStreamID <= 0) {
			System.err.println("Stream ID should be non-negative integer.");
			throw new SpeedyException("StreamID should be bigger than 0.");
		}
		this.lastStreamID = lastStreamID;
	}

	/**
	 * Gets the value of the last stream id
	 * 
	 * @param lastStreamID
	 */
	private int getLastStreamID() {
		return this.lastStreamID;
	}

}
