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
	 * Holds the HeaderBlock for the frame
	 * 
	 * @param streamID
	 */
	private HeaderBlock headerBlock;

	public SynReply(int streamID) throws SpeedyException {
		headerBlock = new HeaderBlock();
		setStreamID(streamID);
		setType(ConstUtility.SYN_REPLY_NUM);
		setVersion(ConstUtility.VERSION);
		setDefaultDataLength();
	}

	public SynReply(int streamID, HeaderBlock headerBlock) throws SpeedyException {
		setHeaderBlock(headerBlock);
		setStreamID(streamID);
		setType(ConstUtility.SYN_REPLY_NUM);
		setVersion(ConstUtility.VERSION);
		setLengthData(ConstUtility.SYNSTREAM_DEFAULT_DATA_LENGTH + headerBlock.getLength());
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
			throw new SpeedyException("Unexpected byte array");
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
		index += ConstUtility.REPLY_UNUSED_LENGTH;
		
		// Decode header
		HeaderBlock headerBlock = HeaderBlock
				.decode(ByteUtility.byteSubarray(encodedBytes, index, encodedBytes.length - index));

		SynReply frame = new SynReply(streamId, headerBlock);
		frame.setLength(length);
		frame.setFlags(flags);
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
			throw new SpeedyException("StreamID should be bigger than 0.");
		}
		if(id > (int)Math.pow(2, 31) - 1){
			throw new SpeedyException("StreamID is too large.");
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
		return new String("[StreamID,"+streamID +"]");
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null && this!=null){
			return false;
		}
		if(this == obj) {
			return true;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		SynReply ss = (SynReply) obj;
		
		if(this.length != ss.getLength()){
			return false;
		}
		if(this.getCFlag() != ss.getCFlag()){
			return false;
		}
		if(this.getStreamID() != ss.getStreamID()){
			return false;
		}
		if(!this.headerBlock.equals(ss.getHeaderBlock())){
			return false;
		}
		
		
		
		return true;
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
		cvt[3] = (byte) (type & 0b1111111);
		cvt[2] = (byte) (type >> 8 & 0b1111111);
		return cvt;
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
