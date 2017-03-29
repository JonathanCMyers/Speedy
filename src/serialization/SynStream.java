/********************************
 * Authors:  Feng Yang        
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 * Update:   3/28/2017          *
 ********************************/

package serialization;

import java.nio.ByteBuffer;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class SynStream extends ControlFrame {
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
	 * Holds the priority of the frame 0 represents the highest priority and 7
	 * represents the lowest priority.
	 */
	private short priority;

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

	public SynStream(int streamID) throws SpeedyException {
		headerBlock = new HeaderBlock();
		setStreamID(streamID);
		setType(ConstUtility.SYN_STREAM_NUM);
		setVersion(ConstUtility.VERSION);
		setPriority(ConstUtility.SYN_STREAM_DEFUALT_PRIORITY);
		setDefaultDataLength();
		setNumOfPairs(ConstUtility.NUMBER_OF_PAIRS_DEFAULT);

	}

	public SynStream(int streamID, HeaderBlock headerBlock) throws SpeedyException {
		
		setHeaderBlock(headerBlock);
		setStreamID(streamID);
		setType(ConstUtility.SYN_STREAM_NUM);
		setVersion(ConstUtility.VERSION);
		setPriority(ConstUtility.SYN_STREAM_DEFUALT_PRIORITY);
		setNumOfPairs(headerBlock.getNumOfPairs());
		setLengthData(ConstUtility.SYNSTREAM_HEADER_LENGTH + headerBlock.getLength());
	}

	/**
	 * Encodes the SYN_STREAM into byte array
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
			// Encode Priority and numOfPairs
			byte[] pn = getBytesPN(priority, numOfPairs);
			ByteUtility.copyBytes(encodedBytes, index, pn);
			index += pn.length;
			// Encode HeaderBlock
			ByteUtility.copyBytes(encodedBytes, index, encodedHeader);

		} catch (SpeedyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return encodedBytes;
	}

	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		if(encodedBytes == null || encodedBytes.length < ConstUtility.SYNSTREAM_HEADER_LENGTH){
			throw new SpeedyException("The byte array is illegal.");
		}
		
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
		// Decode Priority
		int priority = decodePriority(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.SYNSTREAM_PRIORITY_LENGTH));
		index += ConstUtility.SYNSTREAM_PRIORITY_LENGTH;
		index += ConstUtility.SYNSTREAM_UNUSED_LENGTH;
		//Decode number of pairs
		int numOfPairs = ByteUtility.littleEndianToUINT16(ByteUtility.
				byteSubarray(encodedBytes, index,ConstUtility.NUM_OF_PAIRS_LENGTH));
		index += ConstUtility.NUM_OF_PAIRS_LENGTH;
		// Decode header
		HeaderBlock headerBlock = HeaderBlock
				.decode(ByteUtility.byteSubarray(encodedBytes, index, encodedBytes.length - index));

		SynStream frame = new SynStream(streamId, headerBlock);
		return frame;
	}

	/**
	 * Sets the value of streamID in the frame
	 * 
	 * @param streamID
	 * @throws SpeedyException 
	 */
	public void setStreamID(int id) throws SpeedyException {
		if (id <= 0) {
			System.err.println("Stream ID should be non-negative integer.");
			throw new SpeedyException("StreamID should be bigger than 0.");
		}
		streamID = id;
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
	 * Sets the value of priority in the frame
	 * 
	 * @param priority
	 *            0~7
	 * @throws SpeedyException
	 */
	public void setPriority(short priority) {
		if (priority < 0 || priority > 7) {
			System.err.println("setPriority error: the priority shoud be between 0 and 7.");
		}
		this.priority = priority;
	}

	public void setPriority(int priority) {
		if (priority < 0 || priority > 7) {
			System.err.println("setPriority error: the priority shoud be between 0 and 7.");
		}
		this.priority = (short) priority;
	}

	/**
	 * Gets the priority of the Frame
	 * 
	 * @return priority
	 */
	public short getPriority() {
		return priority;
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
	 * @throws SpeedyException 
	 */
	private void setHeaderBlock(HeaderBlock headerBlock) throws SpeedyException {
		
		this.headerBlock = headerBlock;
	}

	/**
	 * Gets the headerBlock
	 */
	public HeaderBlock getHeaderBlock() {
		return this.headerBlock;
	}


	

	/**
	 * Decodes the version
	 * 
	 * @param value
	 * @return
	 */
	private static short decodePriority(byte[] value) {

		short priority = 0;
		priority = (short) (value[0] >> 6 & 0b00000011);
		return priority;
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
	 * Gets the bytes array of priority, unused and numOfPairs
	 * 
	 * @param priority
	 * @param numOfPairs
	 * @return
	 */
	private byte[] getBytesPN(short priority, short numOfPairs) {
		byte[] pn = new byte[4];
		pn[0] = (byte) ((priority & 0b00000011) << 6);
		byte[] nop = ByteUtility.uint16ToLittleEndian(numOfPairs);// Number of
																	// pairs
		pn[2] = nop[0];
		pn[3] = nop[1];
		return pn;
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
}
