/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class SynReply extends ControlFrame {

	/**
	 * Holds the streamID of the Frame
	 */
	private int streamID;

	/**
	 * Holds the Associated-To-Stream-ID
	 * 
	 * @param streamID
	 */
	private int associateStreamID;

	/**
	 * Holds the slot of the frame
	 */
	private byte slot;

	/**
	 * Holds the length of the header in the frame
	 */
	private final static int lengthOfHeader = 16;

	/**
	 * Holds the number of name/value pairs
	 */
	private short numOfPairs;

	/**
	 * Holds the HeaderBlock for the frame
	 * 
	 * @param streamID
	 */
	private HeaderBlock headerBlock;

	public SynReply(int streamID) {
		headerBlock = new HeaderBlock();
		setStreamID(streamID);
		setType(ConstUtility.SYN_REPLY_NUM);
		setVersion(ConstUtility.VERSION);
		setDefaultDataLength();
		setNumOfPairs(ConstUtility.NUMBER_OF_PAIRS_DEFAULT);
	}

	public SynReply(int streamID, HeaderBlock headerBlock) {
		setHeaderBlock(headerBlock);
		setStreamID(streamID);
		setType(ConstUtility.SYN_REPLY_NUM);
		setVersion(ConstUtility.VERSION);
		setNumOfPairs(headerBlock.getNumOfPairs());
		setLengthData(ConstUtility.SYNSTREAM_HEADER_LENGTH + headerBlock.getLength());
	}

	/**
	 * Encodes the SYNREPY into byte array
	 * 
	 * @return byte array
	 */
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		try {
			// Encodes the header block
			byte[] encodedHeader = headerBlock.encode();
			int lengthOfEncode = ConstUtility.SYNSTREAM_HEADER_LENGTH + encodedHeader.length;
			encodedBytes = ByteUtility.lengthenBytes(encodedBytes, lengthOfEncode);
			int index = 0;
			// Encode CFlag,Version,Type(4 bytes)
			byte[] cvt = getBytesCVT(CFlag, version, type);
			ByteUtility.copyBytes(encodedBytes, index, cvt);
			index += cvt.length;
			// Encode Flags,Length
			byte[] fl = getBytesFL(flags, length);
			ByteUtility.copyBytes(encodedBytes, index, fl);
			index += fl.length;
			// Endode StreamId
			byte[] streamid = ByteUtility.uint32ToLittleEndian(streamID);
			ByteUtility.copyBytes(encodedBytes, index, streamid);
			index += streamid.length;
			index += ConstUtility.REPLY_UNUSED_LENGTH;
			// Encode numOfPairs
			ByteUtility.copyBytes(encodedBytes, index, ByteUtility.uint16ToLittleEndian(numOfPairs));
			index += ConstUtility.NUM_OF_PAIRS_LENGTH;
			// Encode HeaderBlock
			ByteUtility.copyBytes(encodedBytes, index, encodedHeader);

		} catch (SpeedyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedBytes;
	}

	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// Decode CFlag
		int index = 0;
		boolean cFlag = decodeCFlag(encodedBytes[0]);
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
		// Decode streamID
		int streamId = ByteUtility.littleEndianToUINT32(
				ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.STREAMID_BYTE_LENGTH));
		index += ConstUtility.STREAMID_BYTE_LENGTH;
		index += ConstUtility.REPLY_UNUSED_LENGTH;
		// Decode number of pairs
		int numOfPairs = ByteUtility
				.littleEndianToUINT16(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.NUM_OF_PAIRS_LENGTH));
		index += ConstUtility.NUM_OF_PAIRS_LENGTH;
		// Decode header
		HeaderBlock headerBlock = HeaderBlock
				.decode(ByteUtility.byteSubarray(encodedBytes, index, encodedBytes.length - index));

		SynReply frame = new SynReply(streamId, headerBlock);
		return frame;

	}

	/**
	 * Sets the value of streamID in the frame
	 * 
	 * @param streamID
	 */
	public void setStreamID(int streamID) {
		this.streamID = streamID;
	}

	/**
	 * Gets the StreamID of the Frame
	 * 
	 * @return
	 */
	public int getStreamID() {
		return streamID;
	}

	/**
	 * Sets the value of associated streamID in the frame
	 * 
	 * @param associateStreamID
	 */
	public void setAssociatedStreamID(int associateStreamID) {
		this.associateStreamID = associateStreamID;
	}

	/**
	 * Gets the associated StreamID of the Frame
	 * 
	 * @return associateStreamID
	 */
	public int getAssociatedStreamID() {
		return associateStreamID;
	}

	/**
	 * Sets the value of slot in the frame
	 * 
	 * @param streamID
	 */
	public void setSlot(byte slot) {
		this.slot = slot;
	}

	/**
	 * Gets the slot of the Frame
	 * 
	 * @return
	 */
	public byte getSlot() {
		return slot;
	}

	@Override
	public String toString() {
		// TODO
		throw new UnsupportedOperationException("SynStream.toString()");
	}

	@Override
	public boolean equals(Object o) {
		// TODO
		throw new UnsupportedOperationException("SynStream.equals()");
	}

	/**
	 * Sets the headerBlock
	 */
	private void setHeaderBlock(HeaderBlock headerBlock) {
		this.headerBlock = headerBlock;
	}

	/**
	 * Gets the byte array of CFlag,Version and Type
	 */
	public byte[] getBytesCVT(boolean CFlag, short version, short type) {
		byte[] cvt = new byte[4];

		cvt[0] = (byte) (version & 0b11111111);
		if (CFlag) {// For control frame
			cvt[0] = (byte) (cvt[0] | 0b10000000);
		}
		cvt[1] = (byte) (version >> 8 & 0b11111111);
		cvt[2] = (byte) (type & 0b1111111);
		cvt[3] = (byte) (type >> 8 & 0b1111111);
		return cvt;
	}


	/**
	 * Sets the value of number of Name/Value pairs
	 * 
	 * @param numOfPairs
	 */
	private void setNumOfPairs(short numOfPairs) {
		this.numOfPairs = numOfPairs;
	}

	/**
	 * Gets the bytes array of unused and numOfPairs
	 * 
	 * @param priority
	 * @param numOfPairs
	 * @return
	 */
	private byte[] getBytesNOP(short numOfPairs) {

		byte[] nop = ByteUtility.uint16ToLittleEndian(numOfPairs);
		byte[] unop = new byte[4];
		unop[2] = nop[0];
		unop[3] = nop[1];
		return unop;
	}

	/**
	 * Sets the default length of data in the syn Stream frame without any block
	 */
	private void setDefaultDataLength() {
		this.length = ConstUtility.SYNSTREAM_DEFAULT_DATA_LENGTH;
	}

	/**
	 * Sets the length of data in Syn Stream frame
	 * 
	 * @param length
	 */
	private void setLengthData(int length) {
		this.length = length;
	}
	
	
	/**
	 * Gets the headerBlock
	 */
	public HeaderBlock getHeaderBlock() {
		return this.headerBlock;
	}
}
