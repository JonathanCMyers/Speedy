/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import serialization.Frame;
import serialization.HeaderBlock;
import serialization.MessageInput;
import serialization.SynReply;
import serialization.SynReply;
import serialization.exception.SpeedyException;

public class SynReplyTest {

	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		headerBlock.addBlock("Name1","value");
		headerBlock.addBlock("Name2","value");
		SynReply s1 = new SynReply(5,headerBlock);
		byte[] encodedBytes = s1.encode();
		InputStream in = new ByteArrayInputStream(encodedBytes);
		MessageInput msgin = new MessageInput(in);
		SynReply s2 =(SynReply) Frame.decodeFrame(msgin);
		//System.out.println(s2.getHeaderBlock().getBlocksList().get(1));
		assertEquals(s1, s2);
	}
	
	@Test(expected=SpeedyException.class)
	public void testShortDecode() throws SpeedyException {
		SynReply.decode(new byte[] {0x00});
	}
	
	@Test(expected=SpeedyException.class)
	public void testNullDecode() throws SpeedyException {
		SynReply.decode(null);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDNegative() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s = new SynReply(1,headerBlock);
		s.setStreamID(-1);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDZero() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s = new SynReply(1,headerBlock);
		s.setStreamID(0);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDTooLarge() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s = new SynReply(1,headerBlock);
		s.setStreamID((int)Math.pow(2, 31));
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDNegative() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		new SynReply(-1,headerBlock);
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDZero() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		new SynReply(0,headerBlock);
	}
	
	
	@Test
	public void testEqualsReflexive() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s = new SynReply(1,headerBlock);
		assertEquals(s, s);
	}
	
	@Test
	public void testEquals() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s1 = new SynReply(1,headerBlock);
		SynReply s2 = new SynReply(1,headerBlock);
		assertEquals(s1, s2);
	}
	
	
	@Test
	public void testHeaderBlockNotEquals() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		HeaderBlock headerBlock1 = new HeaderBlock();
		headerBlock.addBlock("Name","value");
		headerBlock1.addBlock("Name1","value");
		SynReply s1 = new SynReply(1,headerBlock);
		SynReply s2 = new SynReply(1,headerBlock1);
		assertNotEquals(s1, s2);
	}
	@Test
	public void testOneSideNullNotEqual() throws SpeedyException {
		HeaderBlock headerBlock = new HeaderBlock();
		SynReply s1 = null;
		SynReply s2 = new SynReply(1,headerBlock);
		assertNotEquals(s1, s2);
	}

}
