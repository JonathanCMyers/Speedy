/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/27/2017          *
 ********************************/

package serialization.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import serialization.DataFrame;
import serialization.Frame;
import serialization.HeaderBlock;
import serialization.DataFrame;
import serialization.exception.SpeedyException;

public class DataFrameTest {
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		byte[] encodedBytes = d1.encode();
		Frame d2 = Frame.decode(encodedBytes);
		DataFrame df = (DataFrame) d2;
		System.out.println(new String(df.getData()));
		assertEquals(d1, (DataFrame) d2);
	}

	@Test(expected = SpeedyException.class)
	public void testShortDecode() throws SpeedyException {
		DataFrame.decode(new byte[] { 0x00 });
	}

	@Test(expected = SpeedyException.class)
	public void testNullDecode() throws SpeedyException {
		DataFrame.decode(null);
	}

	@Test(expected = SpeedyException.class)
	public void testSetStreamIDNegative() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(5, data.getBytes());
		d.setStreamID(-1);
	}

	@Test(expected = SpeedyException.class)
	public void testSetStreamIDZero() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(5, data.getBytes());
		d.setStreamID(0);
	}

	@Test(expected = SpeedyException.class)
	public void testSetStreamIDTooLarge() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(5, data.getBytes());
		d.setStreamID((int) Math.pow(2, 31));
	}

	@Test(expected = SpeedyException.class)
	public void testConstructorStreamIDNegative() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(-1, data.getBytes());
	}

	@Test(expected = SpeedyException.class)
	public void testConstructorStreamIDZero() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(0, data.getBytes());
	}

	@Test
	public void testEqualsReflexive() throws SpeedyException {
		String data = "data";
		DataFrame d = new DataFrame(5, data.getBytes());
		assertEquals(d, d);
	}

	@Test
	public void testEquals() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = new DataFrame(5, data.getBytes());
		byte[] db1 = d1.encode();
		byte[] db2 = d2.encode();
		for (int i = 0;i < db1.length;i++) {
			    System.out.print(Integer.toBinaryString(db1[i] & 255 | 256).substring(1));
			    System.out.println("  "+Integer.toBinaryString(db2[i] & 255 | 256).substring(1));
			}
		assertEquals(new DataFrame(5, data.getBytes()),new DataFrame(5, data.getBytes()));
		//assertEquals(d1, d2);
	}

	@Test
	public void testOneSideNullNotEqual() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = null;
		assertNotEquals(d1, d2);
	}
}
