/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/
package serialization;

import java.util.ArrayList;
import java.util.List;

import serialization.exception.SpeedyException;
import utility.ByteUtility;

public class HeaderBlock {
	/**
	 * Holds the number of name/value pairs
	 */
	private int numOfPairs;
	/**
	 * Holds the blocks in the header block
	 */
	private List<Block> blocksList;

	public HeaderBlock() {
		blocksList = new ArrayList<>();
		numOfPairs = 0;
	}

	/**
	 * Add new block into the headerBlock
	 */

	public void addBlock(String name, String value) {
		Block block = new Block(name, value);
		numOfPairs++;
		blocksList.add(block);
	}
	
	/**
	 * Add new block into the headerBlock
	 */

	public void addBlock(Block block) {
		numOfPairs++;
		blocksList.add(block);
	}

	/**
	 * encode the block
	 * 
	 * @return
	 * @throws SpeedyException
	 */
	public byte[] encode() throws SpeedyException {
		if (numOfPairs == 0) {
			throw new SpeedyException("The blocksList is empty. ");
		}
		List<byte[]> encodedBlocks = new ArrayList<>();
		int lengthOfEncode = 0;
		for (Block block : blocksList) {
			byte[] encodedBlock = block.encode();
			lengthOfEncode += encodedBlock.length;
			encodedBlocks.add(encodedBlock);
		}

		byte[] encodedHeaderBlock = new byte[lengthOfEncode + Integer.SIZE / 8];
		ByteUtility.copyBytes(encodedHeaderBlock, 0, ByteUtility.uint32ToLittleEndian(this.numOfPairs));
		int position = Integer.SIZE / 8;
		for (byte[] eb : encodedBlocks) {
			ByteUtility.copyBytes(encodedHeaderBlock, position, eb);
			position += eb.length;
		}
		return encodedHeaderBlock;
	}

	/**
	 * Decodes the headerBlock
	 * @param encodedHeaderBlock
	 * @return
	 */
	public static HeaderBlock decode(byte[] encodedHeaderBlock ){
		HeaderBlock headerBlock = new HeaderBlock();
		int index = 0;
		//Decode numOfPairs
		int numOfPairs = ByteUtility.littleEndianToUINT32(
				ByteUtility.byteSubarray(encodedHeaderBlock, index, Integer.SIZE/8));
		index += 4;
		
		for(int i =0;i < numOfPairs;i++){
			int lengthOfName = ByteUtility.littleEndianToUINT32(
					ByteUtility.byteSubarray(encodedHeaderBlock, index, Integer.SIZE/8));
			int lengthOfValue = ByteUtility.littleEndianToUINT32(
					ByteUtility.byteSubarray(encodedHeaderBlock, index + lengthOfName*2 + Integer.SIZE/8, Integer.SIZE/8));
			int lengthOfBlock = Integer.SIZE/8 + lengthOfName + Integer.SIZE/8 + lengthOfValue;
			Block block = Block.decode(ByteUtility.byteSubarray(encodedHeaderBlock, index, lengthOfBlock));
			index += lengthOfBlock;
			headerBlock.addBlock(block);
		}
		return headerBlock;
	}
	/**
	 * Gets the number of name/value pairs
	 * @return
	 */
	public short getNumOfPairs(){
		return (short)blocksList.size();
	}
}