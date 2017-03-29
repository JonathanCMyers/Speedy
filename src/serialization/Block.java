/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/
package serialization;

import utility.ByteUtility;
import utility.ConstUtility;

/**
 * 
 * @author detian
 *
 */
public class Block {
	/**
	 * Holds the length of the block name
	 */
	private short lengthOfName;
	/**
	 * Holds the name of the block
	 */
	private String name;
	/**
	 * Holds the length of the value in the block
	 */
	private short lengthOfValue;
	/**
	 * Holds the value of the block
	 */
	private String value;

	public Block(String name, String value) {

		this.name = name;
		this.lengthOfName = (short) name.length();
		this.value = value;
		this.lengthOfValue = (short) value.length();

	}

	/**
	 * encode the block
	 * 
	 * @return
	 */
	public byte[] encode() {
		int lengthOfBlock = 0;
		lengthOfBlock += ConstUtility.BLOCK_NAME_LENGTH_LENGTH;// Add the length of lengthOfName
		lengthOfBlock += 2 * lengthOfName;// Add the length of the name
		lengthOfBlock += ConstUtility.BLOCK_VALUE_LENGTH_LENGTH;// Add the length of lengthOfValue
		lengthOfBlock += 2 * lengthOfValue;// Add the length of the value

		byte[] encodeBlock = new byte[lengthOfBlock];
		// Encodes the lengthOfName
		ByteUtility.copyBytes(encodeBlock, 0, ByteUtility.uint16ToLittleEndian(lengthOfName));
		ByteUtility.copyBytes(encodeBlock, ConstUtility.BLOCK_NAME_LENGTH_LENGTH, name.getBytes());
		ByteUtility.copyBytes(encodeBlock, ConstUtility.BLOCK_NAME_LENGTH_LENGTH + 2 * lengthOfName,
				ByteUtility.uint16ToLittleEndian(lengthOfValue));
		ByteUtility.copyBytes(encodeBlock,
				ConstUtility.BLOCK_NAME_LENGTH_LENGTH + 2 * lengthOfName + ConstUtility.BLOCK_VALUE_LENGTH_LENGTH,
				value.getBytes());
		// To compress the data.
		return encodeBlock;
	}

	/**
	 * decode the block
	 * 
	 * @return
	 */
	public static Block decode(byte[] encodeBlock) {
		int position = 0;
		// decompress the byte[]
		// Decode length of name
		int lengthOfName = ByteUtility
				.littleEndianToUINT16(ByteUtility.byteSubarray(encodeBlock, position, ConstUtility.BLOCK_NAME_LENGTH_LENGTH));
		
		position += ConstUtility.BLOCK_NAME_LENGTH_LENGTH;
		String name = new String(ByteUtility.byteSubarray(encodeBlock, position, lengthOfName * 2));
		
		position += lengthOfName * 2;
		int lengthOfValue = ByteUtility.littleEndianToUINT16(
				ByteUtility.byteSubarray(encodeBlock, position, ConstUtility.BLOCK_VALUE_LENGTH_LENGTH));
		position += ConstUtility.BLOCK_VALUE_LENGTH_LENGTH;
		String value = new String(ByteUtility.byteSubarray(encodeBlock, position, lengthOfValue * 2));
		Block block = new Block(name, value);
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
