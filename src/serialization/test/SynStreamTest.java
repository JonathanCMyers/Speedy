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
import serialization.SynStream;
import serialization.exception.SpeedyException;

public class SynStreamTest {
	
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		SynStream s1 = new SynStream(5);
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
		SynStream s = new SynStream(1);
		s.setStreamID(-1);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDZero() throws SpeedyException {
		SynStream s = new SynStream(1);
		s.setStreamID(0);
	}
	
	@Test(expected=SpeedyException.class)
	public void testSetStreamIDTooLarge() throws SpeedyException {
		SynStream s = new SynStream(1);
		s.setStreamID((long)Math.pow(2, 31));
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDNegative() throws SpeedyException {
		new SynStream(-1);
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDZero() throws SpeedyException {
		new SynStream(0);
	}
	
	@Test(expected=SpeedyException.class)
	public void testConstructorStreamIDTooLarge() throws SpeedyException {
		new SynStream((long)Math.pow(2, 31));
	}
	
	@Test
	public void testEqualsReflexive() {
		SynStream s = new SynStream(1);
		assertEquals(s, s);
	}
	
	@Test
	public void testEquals() {
		SynStream s1 = new SynStream(1);
		SynStream s2 = new SynStream(1);
		assertEquals(s1, s2);
	}
	
	@Test
	public void testOneSideNullNotEqual() {
		SynStream s1 = null;
		SynStream s2 = new SynStream(1);
		assertNotEquals(s1, s2);
	}
}
