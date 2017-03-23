/********************************
 * Authors:  Feng Yang        
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.SpeedyUtility;

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

	public SynStream(int streamID,HeaderBlock headerBlock) {
		setHeaderBlock(headerBlock);
		setStreamID(streamID);
		setType(SpeedyUtility.SYN_STREAM_NUM);
		setVersion(SpeedyUtility.VERSION);
		setPriority(SpeedyUtility.SYN_STREAM_DEFUALT_PRIORITY);
		setNumOfPairs(headerBlock.getNumOfPairs());
		
	}
	/**
	 * Encodes the SYN_STREAM into byte array
	 * @return byte array
	 */
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		byte[] encodedHeader;
		try {
			encodedHeader = headerBlock.encode();
			int lengthOfEncode = lengthOfHeader + encodedHeader.length;
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
			//Encode Priority and numOfPairs
			byte[] pn = getBytesPN(priority,numOfPairs);
			ByteUtility.copyBytes(encodedBytes, index, pn);
			index += pn.length;
			// Encode HeaderBlock
			byte[] header = headerBlock.encode();
			ByteUtility.copyBytes(encodedBytes, index, header);

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
		short version = decodeVersion(ByteUtility.byteSubarray(encodedBytes, index, SpeedyUtility.VERSION_BYTE_LENGTH));
		index += SpeedyUtility.VERSION_BYTE_LENGTH;
		// Decode type
		short type = (short) ByteUtility
				.littleEndianToUINT16(ByteUtility.byteSubarray(encodedBytes, index, SpeedyUtility.TYPE_BYTE_LENGTH));
		index += SpeedyUtility.TYPE_BYTE_LENGTH;
		// Decode flags
		byte flags = encodedBytes[index];
		index += SpeedyUtility.FLAGS_BYTE_LENGTH;
		// Decode length
		int length = decodeLength(ByteUtility.byteSubarray(encodedBytes, index, SpeedyUtility.LENGTH_BYTE_LENGTH));
		index += SpeedyUtility.LENGTH_BYTE_LENGTH;
		//Decode streamID
		int streamId = ByteUtility.littleEndianToUINT32(ByteUtility.byteSubarray(encodedBytes, index, SpeedyUtility.STREAMID_BYTE_LENGTH));
		index += SpeedyUtility.STREAMID_BYTE_LENGTH;
		//Decode Priority
		
		//Decode numOfPairs
		// Decode header
		HeaderBlock headerBlock = HeaderBlock
				.decode(ByteUtility.byteSubarray(encodedBytes, index, encodedBytes.length - index));

		SynStream frame = new SynStream(streamId,headerBlock);
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

	/**
	 * Gets the cFlag
	 * 
	 * @param cf
	 * @return
	 */
	private static boolean decodeCFlag(byte cf) {
		boolean cFlag = false;
		if ((cf & 0b10000000) != 0b0000000) {
			cFlag = true;
		}
		return cFlag;
	}

	/**
	 * Decodes the version
	 * 
	 * @param value
	 * @return
	 */
	private static short decodeVersion(byte[] value) {
		short version = 0;
		version = (short) ByteUtility.littleEndianToUINT16(value);
		version = (short) (version & 0b0111111);
		return version;
	}

	/**
	 * Decodes the length
	 * 
	 * @param value
	 * @return
	 */
	private static int decodeLength(byte[] value) {
		if (value.length != 3) {
			System.err.println("decodeLength error: the length should be 24 bits.");
		}
		int length = 0;
		length += value[2] << 16;
		length += value[1] << 8;
		length += value[0];
		return length;
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
	 * @param priority
	 * @param numOfPairs
	 * @return
	 */
	private byte[] getBytesPN(short priority, short numOfPairs){
		byte[] pn = new byte[4];
		pn[0] =(byte)( priority & 0b00000111 << 1);
		byte[] nop = ByteUtility.uint16ToLittleEndian(numOfPairs);
		pn[2] = nop[0];
		pn[3] = nop[1];
		return pn;
	}
}
