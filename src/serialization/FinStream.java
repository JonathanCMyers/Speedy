package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class FinStream extends ControlFrame{
	/**
	 * Holds the stream ID of the frame
	 */
	private int streamID;
	/**
	 * Holds the status of the frame
	 */
	private int statusCode;
	
	
	/**
	 * The constructor of the Fin stream frame
	 * @param streamID
	 */
	public FinStream(int streamID){
		setStreamID(streamID);
		
		setType(ConstUtility.FIN_STREAM_NUM);
		setLength();
	}
	
	
	/**
	 * The constructor of the Fin stream frame 
	 * @param streamID the stream ID for the frame
	 * @param statusCode  the status code for the frame
	 */
	public FinStream(int streamID,int statusCode){
		setStreamID(streamID);
		setVersion(ConstUtility.VERSION);
		setType(ConstUtility.FIN_STREAM_NUM);
		setLength();
		setStatusCode(statusCode);
	}
	
	/**
	 * Encodes the FINSTREAM frame into byte array
	 * 
	 * @return byte array
	 */
	public byte[] encode() {
		byte[] encodedBytes = super.encode();
		// Encodes the header block
		int lengthOfEncode = ConstUtility.FINSTREAM_LENGTH;
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
		
		// Encode status code
		ByteUtility.copyBytes(encodedBytes, index, ByteUtility.uint32ToLittleEndian(statusCode));

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
		
		// Decode status code
		int statusCode = ByteUtility
				.littleEndianToUINT32(ByteUtility.byteSubarray(encodedBytes, index, ConstUtility.FINSTREAM_STATUS_CODE_LENGTH));
		index += ConstUtility.FINSTREAM_STATUS_CODE_LENGTH;
		
		FinStream frame = new FinStream(streamId, statusCode);
		return frame;
	}
	
	/**
	 * Set the value of streamID in the frame
	 * @param streamID
	 */
	public void setStreamID(int streamID){
		this.streamID = streamID;
	}
	/**
	 * Gets the StreamId
	 */
	public int getStreamID(){
		return streamID;
	}
	
	
	/**
	 * Set the value of streamID in the frame
	 * @param streamID
	 */
	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}
	/**
	 * Gets the StatusCode
	 */
	public int getStatusCode(){
		return statusCode;
	}
	
	
	/**
	 * Gets the length of data in fin stream frame
	 */
	private void setLength(){
		this.length = ConstUtility.FIN_DATA_LENGTH;//8 bytes
	}
}
