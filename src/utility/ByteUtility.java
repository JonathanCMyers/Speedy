/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/17/2017          *
 ********************************/
package utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import serialization.exception.SpeedyException;

public class ByteUtility {

	/**
	 * Copies the second byte array passed in into the first byte array, starting at the first byte array's index given by startingIndex
	 * @param arrayToCopyInto the byte array we are copying the second byte array into
	 * @param startingIndex the index at the first byte array that we begin copying the second byte array into
	 * @param arrayToCopy the second byte array that we copy fully, starting at index 0, into the first byte array
	 * @return arrayToCopyInto the modified first byte array with the second byte array now contained within it
	 */
	public static void copyBytes(byte[] arrayToCopyInto, int startingIndex, byte[] arrayToCopy) {
		for(int i = 0; i < arrayToCopy.length; i++) {
			arrayToCopyInto[i+startingIndex] = arrayToCopy[i];
		}
	}
	
	
	/**
	 * Converts a uint16 into littleEndian notation
	 * @param value to be converted into little endian byte form
	 * @return bytes the converted short value
	 */
	public static byte[] uint16ToLittleEndian(short value) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) (value & 0b11111111);
		bytes[1] = (byte) (value >> 8 & 0b11111111);
		return bytes;
	}
	
	
	/**
	 * Converts a set of bytes into a 16-bit, unsigned integer
	 * @param bytes to be converted
	 * @return value the converted bytes
	 */
	public static short littleEndianToUINT16(byte[] bytes) {
		if(bytes.length != 2) {
			throw new IllegalArgumentException("Must be 2 bytes in length. Given: " + bytes.length + " bytes.");
		}
		short value = 0;
		value += makeUnsigned(bytes[1]) << 8;
		value += makeUnsigned(bytes[0]);
		return value;
	}
	/**
	 * Converts a uint32 into littleEndian notation
	 * @param value to be converted into little endian byte form
	 * @return bytes the converted long value
	 */
	public static byte[] uint32ToLittleEndian(int value) {
		if(value < 0) {
			throw new IllegalArgumentException("The given value should be a 32-bit, unsigned integer. Given: " + value);
		}
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (value & 0b11111111);
		bytes[1] = (byte) (value >> 8  & 0b11111111);
		bytes[2] = (byte) (value >> 16 & 0b11111111);
		bytes[3] = (byte) (value >> 24 & 0b11111111);
		return bytes;
	}
	
	
	
	
	/**
	 * Checks to see if bytes are negative, and if so, converts them
	 * @param b byte to check 
	 * @return b integer representation of the non-negative byte
	 */
	public static int makeUnsigned(byte b) {
		if((b & 0b10000000) != 0) {
			return (b & 0b01111111) + 0b10000000;
		} 
		return b;
	}
	
	
	/**
	 * Creates a subset of a byte array at the given starting index for the given length
	 * @param bytes bytes to create the subarray of
	 * @param startingIndex the first index to copy over into the subarray
	 * @param length of the subarray to be copied
	 * @return returnBytes the newly copied over bytes
	 */
	public static byte[] byteSubarray(byte[] bytes, int startingIndex, int length) {
		byte[] returnBytes = new byte[length];
		int count = 0;
		for(int i = startingIndex; i < startingIndex + length; i++, count++) {
			try {
				returnBytes[count] = bytes[i];
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new ArrayIndexOutOfBoundsException(e.getMessage() + "; i: " + i + "; length: " + length);
			}
		}
		return returnBytes;
	}
	
	
	
	/**
	 * Converts a set of bytes into a 32-bit, unsigned integer
	 * @param bytes to be converted
	 * @return value the converted bytes
	 */
	public static int littleEndianToUINT32(byte[] bytes) {
		if(bytes.length != 4) {
			throw new IllegalArgumentException("Must be 4 bytes in length. Given: " + bytes.length + " bytes.");
		}
		int value = 0;
		value += makeUnsigned(bytes[3]) << 24;
		value += makeUnsigned(bytes[2]) << 16;
		value += makeUnsigned(bytes[1]) << 8;
		value += makeUnsigned(bytes[0]);
		return value;
	}
	
	/**
	 * Converts a set of bytes in little endian notation to a long
	 * @param bytes to be converted into a long
	 * @return value the converted bytes
	 */
	public static long littleEndianToLong(byte[] bytes) {
		if(bytes.length != 8) {
			throw new IllegalArgumentException("Must be 8 bytes in length. Given: " + bytes.length + " bytes.");
		}
		ByteBuffer bb = ByteBuffer.wrap(bytes);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		return bb.getLong();
	}
	
	/**
	 * Converts a set of bytes in little endian notation to a string
	 * @param bytes to be converted into a string
	 * @return value the converted bytes
	 */
	public static String littleEndianToString(byte[] bytes) {
		byte[] stringBytes = new byte[bytes.length];
		for(int i = 0; i < bytes.length; i++) {
			stringBytes[i] = bytes[bytes.length - 1 - i];
		}
		return new String(stringBytes);
	}
	
	/**
	 * Converts a String into littleEndian notation
	 * @param value to be converted into little endian byte form
	 * @return bytes the converted long value
	 */
	public static byte[] stringToLittleEndian(String value) {
		byte[] bytes = new byte[value.length()];
		byte[] valueBytes = value.getBytes();
		for(int i = 0; i < value.length(); i++) {
			bytes[i] = valueBytes[value.length() - 1 - i];
		}
		return bytes;
	}
	
	/**
	 * Copies the given byte array into a new byte array of the given new size
	 * @param bytes byte array
	 * @param newSize the size the byte array should be lengthened into
	 * @return newBytes the newly lengthened byte array
	 */
	public static byte[] lengthenBytes(byte[] bytes, int newSize) {
		if(newSize < bytes.length) {
			throw new IllegalArgumentException("Cannot lengthen a byte array to a smaller size than it currently is. Current size: " + 
					bytes.length + ". Desired size: " + newSize);
		}
		byte[] newBytes = new byte[newSize];
		for(int i = 0; i < bytes.length; i++) {
			newBytes[i] = bytes[i];
		}
		return newBytes;
	}
	
	/**
	 * Compress the data using SPDY_dictionary_txt
	 * @param data
	 * @return
	 * @throws SpeedyException
	 */
	public static byte[] compress(byte[] data) throws SpeedyException{
		if(data.length > ConstUtility.MAX_COMPRESS_LENGTH){
			throw new SpeedyException("Compress data is too long.");
		}
		Deflater compress = new Deflater();
		compress.setInput(data);
		compress.setDictionary(ConstUtility.SPDY_dictionary_txt);
		compress.finish();
		byte[] compressedData = new byte[ConstUtility.MAX_COMPRESS_LENGTH];

		int compressLength = compress.deflate(compressedData, 0, compressedData.length);
		compress.finish();
		return ByteUtility.byteSubarray(compressedData, 0, compressLength);
	}
	
	/**
	 * Decompress the data by using SPDY_dictionary_txt
	 * @param data
	 * @return
	 */
	public static byte[] decompress(byte[] data){
		Inflater decompress = new Inflater();
		decompress.setInput(data);
		decompress.finished();
		byte[] decompressedData = new byte[ConstUtility.MAX_COMPRESS_LENGTH];

		int decompressLength = 0;
		try {
			decompress.inflate(decompressedData);
			decompress.setDictionary(ConstUtility.SPDY_dictionary_txt);
			decompressLength = decompress.inflate(decompressedData);
		} catch (DataFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		return ByteUtility.byteSubarray(decompressedData, 0, decompressLength);
	}
	
}
