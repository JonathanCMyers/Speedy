/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 * Updated:  3/29/2017          *
 ********************************/
package serialization;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

/**
 * 
 * @author Feng Yang
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

	public Block(String name, String value) throws SpeedyException {
		setName(name);
		setNameLength((short) name.length());
		setValue(value);
		setValueLength((short) value.length());

	}

	/**
	 * encode the block
	 * 
	 * @return
	 */
	public byte[] encode() {
		int lengthOfBlock = 0;
		lengthOfBlock += ConstUtility.BLOCK_NAME_LENGTH_LENGTH;// Add the length of lengthOfName
		lengthOfBlock += lengthOfName;// Add the length of the name
		lengthOfBlock += ConstUtility.BLOCK_VALUE_LENGTH_LENGTH;// Add the length of lengthOfValue
		lengthOfBlock += lengthOfValue;// Add the length of the value

		byte[] encodeBlock = new byte[lengthOfBlock];
		// Encodes the lengthOfName
		ByteUtility.copyBytes(encodeBlock, 0, ByteUtility.uint16ToLittleEndian(lengthOfName));
		ByteUtility.copyBytes(encodeBlock, ConstUtility.BLOCK_NAME_LENGTH_LENGTH, name.getBytes());
		ByteUtility.copyBytes(encodeBlock, ConstUtility.BLOCK_NAME_LENGTH_LENGTH + lengthOfName,
				ByteUtility.uint16ToLittleEndian(lengthOfValue));
		ByteUtility.copyBytes(encodeBlock,
				ConstUtility.BLOCK_NAME_LENGTH_LENGTH + lengthOfName + ConstUtility.BLOCK_VALUE_LENGTH_LENGTH,
				value.getBytes());
		// To compress the data.
		return encodeBlock;
	}

	/**
	 * decode the block
	 * 
	 * @return
	 * @throws SpeedyException 
	 */
	public static Block decode(byte[] encodeBlock) throws SpeedyException {
		if(encodeBlock==null){
			throw new SpeedyException("The encoded array is null.");
		}
		if(encodeBlock.length <  ConstUtility.BLOCK_NAME_LENGTH_LENGTH +  ConstUtility.BLOCK_VALUE_LENGTH_LENGTH){
			throw new SpeedyException("The encoded array is too short.");
		}
		int position = 0;
		// decompress the byte[]
		// Decode length of name
		int lengthOfName = ByteUtility
				.littleEndianToUINT16(ByteUtility.byteSubarray(encodeBlock, position, ConstUtility.BLOCK_NAME_LENGTH_LENGTH));
		position += ConstUtility.BLOCK_NAME_LENGTH_LENGTH;
		String name = new String(ByteUtility.byteSubarray(encodeBlock, position, lengthOfName));
		
		position += lengthOfName;
		int lengthOfValue = ByteUtility.littleEndianToUINT16(
				ByteUtility.byteSubarray(encodeBlock, position, ConstUtility.BLOCK_VALUE_LENGTH_LENGTH));
		position += ConstUtility.BLOCK_VALUE_LENGTH_LENGTH;
		String value = new String(ByteUtility.byteSubarray(encodeBlock, position, lengthOfValue));
		Block block = new Block(name, value);
		return block;
	}

	/**
	 * Sets the name of block
	 * @throws SpeedyException 
	 */
	public void setName(String name) throws SpeedyException {
		if(name == null){
			throw new SpeedyException("Name is null.");
		}
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
	
	
	/**
	 * Sets the value of block
	 */
	public void setNameLength(short lengthOfName) {
		this.lengthOfName = lengthOfName;
	}

	/**
	 * Gets the value of block
	 */
	public short getNameLength() {
		return lengthOfName;
	}
	
	
	/**
	 * Sets the value of block
	 */
	public void setValueLength(short lengthOfValue) {
		this.lengthOfValue = lengthOfValue;
	}

	/**
	 * Gets the value of block
	 */
	public short getValueLength() {
		return lengthOfValue;
	}
	
	/**
	 * Check current object equals to the obj
	 */
	@Override
	public boolean equals(Object obj) {
		
		if(this == obj) {
			return true;
		}
		if(getClass() != obj.getClass()) {
			return false;
		}
		Block objb = (Block) obj;
		//Check name
		if(!this.name.equals(objb.getName())){
			return false;
		}
		//Check value
		if(!this.value.equals(objb.getValue())){
			return false;
		}
		return true;
	}
}
