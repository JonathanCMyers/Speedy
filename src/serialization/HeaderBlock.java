/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/
package serialization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import serialization.exception.SpeedyException;
import utility.ByteUtility;
import utility.ConstUtility;

public class HeaderBlock {
	/**
	 * Holds the number of name/value pairs
	 */
	private int numOfPairs;
	/**
	 * Holds the blocks in the header block
	 */
	private List<Block> blocksList;

	/**
	 * Holds the length of header block;
	 */
	int length;
	public HeaderBlock() {
		blocksList = new ArrayList<>();
		numOfPairs = 0;
	}

	/**
	 * Add new block into the headerBlock
	 * @throws SpeedyException 
	 */

	public void addBlock(String name, String value) throws SpeedyException {
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
			//throw new SpeedyException("The blocksList is empty. ");
		}
		List<byte[]> encodedBlocks = new ArrayList<>();
		int lengthOfEncode = 0;
		
		for (Block block : blocksList) {
			byte[] encodedBlock = block.encode();
			
			lengthOfEncode += encodedBlock.length;
			encodedBlocks.add(encodedBlock);
		}

		byte[] encodedHeaderBlock = new byte[lengthOfEncode];
		//ByteUtility.copyBytes(encodedHeaderBlock, 0, ByteUtility.uint32ToLittleEndian(this.numOfPairs));
		int position = 0;
		for (byte[] eb : encodedBlocks) {
			ByteUtility.copyBytes(encodedHeaderBlock, position, eb);
			position += eb.length;
		}
		//Compress the data
		
		return ByteUtility.compress(encodedHeaderBlock);
	}

	/**
	 * Decodes the headerBlock
	 * @param encodedHeaderBlock
	 * @return
	 * @throws SpeedyException 
	 */
	public static HeaderBlock decode(byte[] headerBlockData ) throws SpeedyException{
		if(headerBlockData.length > 0) {
			byte[] encodedHeaderBlock = ByteUtility.decompress(headerBlockData);
			HeaderBlock headerBlock = new HeaderBlock();
			int index = 0;
			//Decode numOfPairs
			int numOfPairs = 0;
			while(index < encodedHeaderBlock.length - 1){
				short lengthOfName = ByteUtility.littleEndianToUINT16(
						ByteUtility.byteSubarray(encodedHeaderBlock, index, ConstUtility.BLOCK_NAME_LENGTH_LENGTH));
				short lengthOfValue = ByteUtility.littleEndianToUINT16(
						ByteUtility.byteSubarray(encodedHeaderBlock, index + lengthOfName + ConstUtility.BLOCK_NAME_LENGTH_LENGTH, ConstUtility.BLOCK_VALUE_LENGTH_LENGTH));
				int lengthOfBlock = ConstUtility.BLOCK_NAME_LENGTH_LENGTH + lengthOfName + ConstUtility.BLOCK_VALUE_LENGTH_LENGTH + lengthOfValue;
				Block block = Block.decode(ByteUtility.byteSubarray(encodedHeaderBlock, index, lengthOfBlock));
				headerBlock.addBlock(block);
				numOfPairs++;
				index += lengthOfBlock;
			}
			headerBlock.setnumOfPairs(numOfPairs);
			return headerBlock;
		}
		return null;
	}
	/**
	 * Gets the number of name/value pairs
	 * @return
	 */
	public short getNumOfPairs(){
		return (short)blocksList.size();
	}
	
	/**
	 * Gets the blocksList;
	 * @return
	 */
	public List<Block> getBlocksList(){
		return blocksList;
	}
	
	/**
	 * Gets the length of Header block
	 * @return
	 * @throws SpeedyException 
	 */
	public int getLength() {
		int length = 0;
		try {
			length = this.encode().length;
		} catch (SpeedyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return length;
	}
	
	
	/**
	 * Check current object equals to the obj
	 */
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
		HeaderBlock hb = (HeaderBlock) obj;
		if(this.numOfPairs == 0 && hb.getNumOfPairs() == 0){
			return true;
		}
		
		if(this.numOfPairs!=hb.getNumOfPairs()){
			return false;
		}
		
		for(int i = 0; i < numOfPairs;i++){
			if(!this.blocksList.get(i).equals(hb.getBlocksList().get(i))){
				return false;
			}
		}
		return true;
	}
	/**
	 * Sets the value of numOfPairs
	 * @param num
	 */
	public void setnumOfPairs(int num){
		numOfPairs = num;
	}

	
}