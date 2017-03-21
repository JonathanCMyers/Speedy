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

public class SynStream extends ControlFrame {
	/**
	 * Holds the streamID of the Frame
	 */
	private int streamID;
	
	/**
	 * Holds the  Associated-To-Stream-ID
	 * @param streamID
	 */
	private int associateStreamID;
	
	/**
	 * Holds the priority of the frame
	 * 0 represents the highest priority and 7 represents the lowest priority.
	 */
	private int priority;
	
	/**
	 * Holds the slot of the frame
	 */
	private byte slot;
	
	/**
	 * Holds the length of the header in the frame
	 */
	private final static int lengthOfHeader = 16;
	
	/**
	 * Holds the HeaderBlock for the frame
	 * @param streamID
	 */
	private HeaderBlock headerBlock;
	
	public SynStream(int streamID,HeaderBlock headerBlock) {
		setStreamID(streamID);
		setHeaderBlock(headerBlock);
		
	}
	
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		byte[] encodedHeader;
		try {
			encodedHeader = headerBlock.encode();
			int lengthOfEncode = lengthOfHeader + encodedHeader.length;
			encodedBytes = ByteUtility.lengthenBytes(encodedBytes, lengthOfEncode);
			int index = 0;
			//Encode CFlag,Version,Type(4 bytes)
			byte[] cvt = getBytesCVT(CFlag,version,type);
			ByteUtility.copyBytes(encodedBytes, index, cvt);
			//TODO Encode Flags,Length
			
			//TODO Encode HeaderBlock
			
		} catch (SpeedyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return encodedBytes;
	}
	
	public static Frame decode(byte[] encodedBytes) throws SpeedyException {
		// TODO
		throw new UnsupportedOperationException("SynStream.decode()");
	}
	/**
	 * Sets the value of streamID in the frame
	 * @param streamID
	 */
	public void setStreamID(int streamID) {
		this.streamID = streamID;
	}
	
	/**
	 * Gets the StreamID of the Frame
	 * @return
	 */
	public int getStreamID() {
		return streamID;
	}
	
	/**
	 * Sets the value of associated streamID in the frame
	 * @param associateStreamID
	 */
	public void setAssociatedStreamID(int associateStreamID) {
		this.associateStreamID = associateStreamID;
	}
	
	/**
	 * Gets the associated StreamID of the Frame
	 * @return associateStreamID
	 */
	public int getAssociatedStreamID() {
		return associateStreamID;
	}
	
	
	/**
	 * Sets the value of priority in the frame
	 * @param priority 0~7
	 * @throws SpeedyException 
	 */
	public void setPriority(int priority) throws SpeedyException {
		if(priority < 0 || priority >7){
			throw new SpeedyException("Wrong priority for SynStream.");
		}
		this.priority = priority;
	}
	
	/**
	 * Gets the priority of the Frame
	 * @return priority
	 */
	public int setPriority() {
		return priority;
	}
	
	/**
	 * Sets the value of slot in the frame
	 * @param streamID
	 */
	public void setSlot(byte slot) {
		this.slot = slot;
	}
	
	/**
	 * Gets the slot of the Frame
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
	private void setHeaderBlock(HeaderBlock headerBlock){
		this.headerBlock = headerBlock;
	}
	/**
	 * Gets the byte array of CFlag,Version and Type
	 */
	public byte[] getBytesCVT(boolean CFlag,short version,short type){
		byte[] cvt = new byte[4];
		//Get CFlag
		if(CFlag){//For control frame
			cvt[0] = (byte)0b10000000;
		}
		cvt[0] = (byte)(version&0b11111111);
		cvt[1] = (byte)(version >> 8 &0b11111111);
		cvt[2] = (byte)(type&0b1111111);
		cvt[3] = (byte)(type >> 8&0b1111111);
		return cvt;
	}

}
