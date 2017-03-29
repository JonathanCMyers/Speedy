/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import serialization.Frame;
import serialization.HeaderBlock;
import serialization.SynStream;
import serialization.exception.SpeedyException;

public class SynStreamTest {
	
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		headerBlock.addBlock("Name1","value");
		headerBlock.addBlock("Name2","value");
		SynStream s1 = new SynStream(5,headerBlock);
		byte[] encodedBytes = s1.encode();
		Frame s2 = Frame.decode(encodedBytes);
		assertEquals(s1, (SynStream)s2);
	}
	
	@Test(expected=SpeedyException.class)
	public void testShortDecode() throws SpeedyException {
		SynStream.decode(new byte[] {0x00});
	}
	
	@Test(expected=SpeedyException.class)
	public void testNullDecode() throws SpeedyException {
		SynStream.decode(null);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDNegative() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s = new SynStream(1,headerBlock);
		s.setStreamID(-1);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDZero() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s = new SynStream(1,headerBlock);
		s.setStreamID(0);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDTooLarge() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s = new SynStream(1,headerBlock);
		s.setStreamID((int)Math.pow(2, 31));
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDNegative() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		new SynStream(-1,headerBlock);
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDZero() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		new SynStream(0,headerBlock);
	}
	
	
	@Test
	public void testEqualsReflexive() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s = new SynStream(1,headerBlock);
		assertEquals(s, s);
	}
	
	@Test
	public void testEquals() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s1 = new SynStream(1,headerBlock);
		SynStream s2 = new SynStream(1,headerBlock);
		assertEquals(s1, s2);
	}
	
	
	@Test
	public void testHeaderBlockNotEquals() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		HeaderBlock headerBlock1 = new HeaderBlock();
		headerBlock.addBlock("Name","value");
		headerBlock1.addBlock("Name","value");
		SynStream s1 = new SynStream(1,headerBlock);
		SynStream s2 = new SynStream(1,headerBlock1);
		s1.setPriority(1);
		s2.setPriority(2);
		assertNotEquals(s1, s2);
	}
	@Test
	public void testOneSideNullNotEqual() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynStream s1 = null;
		SynStream s2 = new SynStream(1,headerBlock);
		assertNotEquals(s1, s2);
	}
	
	@Test
	public void testDoubleDecode() throws SpeedyException {
		
		SynStream s1 = new SynStream(2);
		byte[] encodedBytes1 = s1.encode();
		assertEquals(s1, (SynStream) (Frame.decode(encodedBytes1)));
			
			
		SynStream s2 = new SynStream(4);
		byte[] encodedBytes2 = s2.encode();
		assertEquals(s2, (SynStream) Frame.decode(encodedBytes2));
		
	}
}
