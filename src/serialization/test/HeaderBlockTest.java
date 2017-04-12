package serialization.test;

import static org.junit.Assert.*;

import org.junit.Test;

import serialization.Block;
import serialization.GoAway;
import serialization.HeaderBlock;
import serialization.SynStream;
import serialization.exception.SpeedyException;

public class HeaderBlockTest {

	
	@Test
	public void testEmptyEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		
	}
	
	@Test
	public void testEquals() throws SpeedyException {
		HeaderBlock hb = new HeaderBlock();
		hb.addBlock("Name","Value");
		HeaderBlock hb1 = new HeaderBlock();
		hb1.addBlock("Name","Value");
		assertEquals(hb, hb1);
	}
	
	@Test
	public void testHeaderEquals() throws SpeedyException {
		HeaderBlock hb = new HeaderBlock();
		hb.addBlock("Name","Value");
		hb.addBlock("Name1","Value1");
		HeaderBlock hb1 = new HeaderBlock();
		hb1.addBlock("Name","Value");
		hb1.addBlock("Name1","Value1");
		assertEquals(hb, hb1);
	}
	
	@Test
	public void testNotEquals() throws SpeedyException {
		HeaderBlock hb = new HeaderBlock();
		HeaderBlock hb1 = new HeaderBlock();
		assertEquals(hb, hb1);
	}
	
	@Test
	public void testNotNUllEquals() throws SpeedyException {
		HeaderBlock hb = new HeaderBlock();
		HeaderBlock hb1 = null;
		assertNotEquals(hb, hb1);
	}
	
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		HeaderBlock hb1 = new HeaderBlock();
		hb1.addBlock("Name","Value");
		hb1.addBlock("Name1","Value1");
		HeaderBlock hb2 = HeaderBlock.decode(hb1.encode());
		assertEquals(hb1,hb2);
	}
	
	
	@Test
	public void testDecodeLengthEquals() throws SpeedyException {
		HeaderBlock hb1 = new HeaderBlock();
		hb1.addBlock("Name","Value");
		hb1.addBlock("Name1","Value1");
		System.out.println(hb1.getLength() + " " + hb1.encode().length);
		assertEquals(hb1.getLength(),hb1.encode().length);
	}
}
