package serialization.test;

import static org.junit.Assert.*;

import org.junit.Test;

import serialization.Block;
import serialization.Frame;
import serialization.HeaderBlock;
import serialization.SynStream;
import serialization.exception.SpeedyException;

public class BlockTest {

	
	@Test(expected=SpeedyException.class)
	public void testNullDecode() throws SpeedyException {
		Block.decode(null);
	}
	
	@Test(expected=SpeedyException.class)
	public void testNameNull() throws SpeedyException {
		Block b1 = new Block(null,"Value");
	}
	
	@Test
	public void testNameNotEquals() throws SpeedyException {
		Block b1 = new Block("Name1","Value");
		Block b2 = new Block("Name2","Value");
		assertNotEquals(b1,b2);
	}
	
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		Block b1 = new Block("Name","Value");
		Block b2 = Block.decode(b1.encode());
		assertEquals(b1,b2);
	}
	
	@Test
	public void testEquals() throws SpeedyException {
		
		Block b1 = new Block("Name","Value");
		Block b2 = new Block("Name","Value");
		assertEquals(b1,b2);
	}
	
	@Test
	public void testNotEquals() throws SpeedyException {
		
		Block b1 = new Block("Name1","Value");
		Block b2 = new Block("Name2","Value");
		assertNotEquals(b1,b2);
	}
}
