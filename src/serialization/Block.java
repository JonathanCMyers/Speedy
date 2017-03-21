/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/
package serialization;

import utility.ByteUtility;

/**
 * 
 * @author detian
 *
 */
public class Block {
	/**
	 * Holds the length of the block name
	 */
	private static int lengthOfName;
	/**
	 * Holds the name of the block
	 */
	private static String name;
	/**
	 * Holds the length of the value in the block
	 */
	private static int lengthOfValue;
	/**
	 * Holds the value of the block
	 */
	private static String value;

	public Block(String name, String value) {

		this.name = name;
		this.lengthOfValue = (short) value.length();
		this.value = value;
	}

	/**
	 * encode the block
	 * 
	 * @return
	 */
	public static byte[] encode() {
		int lengthOfBlock = 0;
		lengthOfBlock += 2;// Add the length of lengthOfName
		lengthOfBlock += 2 * lengthOfName;// Add the length of the name
		lengthOfBlock += 2;// Add the length of lengthOfValue
		lengthOfBlock += 2 * lengthOfValue;// Add the length of the value
		byte[] encodeBlock = new byte[lengthOfBlock];
		// Encodes the lengthOfName
		ByteUtility.copyBytes(encodeBlock, 0, ByteUtility.uint32ToLittleEndian(lengthOfName));
		ByteUtility.copyBytes(encodeBlock, 4, name.getBytes());
		ByteUtility.copyBytes(encodeBlock, 4 + 2 * lengthOfName, ByteUtility.uint32ToLittleEndian(lengthOfValue));
		ByteUtility.copyBytes(encodeBlock, 4 + 2 * lengthOfName + 4, value.getBytes());
		// To compress the data.
		return encodeBlock;
	}

	/**
	 * decode the block
	 * @return
	 */
	public static Block decode(byte[] encodeBlock) {
		int position = 0;
		//ToDo
		//decompress the byte[]
		//Decode length of name
		int lengthOfName =  ByteUtility.littleEndianToUINT16(ByteUtility.byteSubarray(encodeBlock, position,Integer.SIZE/8));
		position += Integer.SIZE/8;
		String name = ByteUtility.littleEndianToString(ByteUtility.byteSubarray(encodeBlock, position, lengthOfName*2));
		position += lengthOfName*2;
		int lengthOfValue = ByteUtility.littleEndianToUINT16(ByteUtility.byteSubarray(encodeBlock, position,Integer.SIZE/8));
		position += Integer.SIZE/8;
		String value = ByteUtility.littleEndianToString(ByteUtility.byteSubarray(encodeBlock, position, lengthOfValue*2));
		Block block = new Block(name,value);
		return block;
	}

	/**
	 * Sets the name of block
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name of block
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of block
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of block
	 */
	public String getValue() {
		return value;
	}
}
