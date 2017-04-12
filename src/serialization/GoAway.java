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
	//Holds the last stream id which was accepted by the sender
	private int lastStreamID;
	
	//Holds the reason for closing the session
	private int statusCode;

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
	 * Constructor for the goaway frame
	 * @param lastStreamID
	 * @param statusCode
	 * @throws SpeedyException
	 */
	public GoAway(int lastStreamID,int statusCode) throws SpeedyException {
		setType(ConstUtility.GOAWAY);
		setLength(ConstUtility.GOAWAY_DEFAULT_LENGTH);
		setLastStreamID(lastStreamID);
		setStatusCode(statusCode);
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
		return "[LastStreamID:"+this.lastStreamID+"],[Status Code:"+this.statusCode+"]";
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
		GoAway go = (GoAway) obj;
		//Check name
		if(this.lastStreamID != go.getLastStreamID()){
			return false;
		}
		
		return true;
	}

	/**
	 * Sets the value of the last stream id
	 * 
	 * @param lastStreamID
	 * @throws SpeedyException
	 */
	public void setLastStreamID(int lastStreamID) throws SpeedyException {
		if (lastStreamID <= 0) {
			System.err.println("Stream ID should be non-negative integer.");
			throw new SpeedyException("StreamID is smaller than or equals to 0.");
		}
		this.lastStreamID = lastStreamID;
	}

	/**
	 * Gets the value of the last stream id
	 * 
	 * @param lastStreamID
	 */
	public int getLastStreamID() {
		return this.lastStreamID;
	}
	
	/**
	 * Sets the status code for go away frame
	 * @param code
	 * @throws SpeedyException 
	 */
	public void setStatusCode(int code) throws SpeedyException{
		if(code <0 || code > 3){
			throw new SpeedyException("Wrong status code:" + code);
		}
		this.statusCode = code;
	}
	
	
	/**
	 * Gets the status code of the goaway frame
	 * @return
	 */
	public int getStatusCode(){
		return this.statusCode;
	}
}
