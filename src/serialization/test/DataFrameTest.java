/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/27/2017          *
 ********************************/

package serialization.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import serialization.DataFrame;
import serialization.Frame;
import serialization.HeaderBlock;
import serialization.MessageInput;
import serialization.DataFrame;
import serialization.exception.SpeedyException;
import utility.ConstUtility;

public class DataFrameTest {
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		byte[] encodedBytes = d1.encode();
		InputStream in = new ByteArrayInputStream(encodedBytes);
		MessageInput msgin = new MessageInput(in);
		Frame d2 = Frame.decodeFrame(msgin);
		DataFrame df = (DataFrame) d2;
		
		assertEquals(d1, (DataFrame) d2);
	}
	
	@Test
	public void testHTTPData() throws SpeedyException {
		String pageRequest = "GET / " + ConstUtility.HTTP_VERSION + "\n";
		pageRequest += ConstUtility.REQUEST_SOURCE + "\n";
		pageRequest += ConstUtility.USER_AGENT + "\n";
		
		
		byte[] dataBytes = pageRequest.getBytes();

		Frame df = new DataFrame(221, dataBytes);
		byte[] encodedBytes = df.encode();
		
		assertEquals(df, (DataFrame)Frame.decodeFrame(new MessageInput(new ByteArrayInputStream(encodedBytes))));

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
		assertEquals(d1, d2);
	}
	
	@Test
	public void testNotEqualsDifferentID() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = new DataFrame(6, data.getBytes());
		assertNotEquals(d1, d2);
	}

	@Test
	public void testOneSideNullNotEqual() throws SpeedyException {
		String data = "data";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = null;
		assertNotEquals(d1, d2);
	}
	
	@Test
	public void testOneSideNullNotEqualStringLength() throws SpeedyException {
		String data = "data";
		String data2 = "dataa";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = new DataFrame(5, data2.getBytes());
		assertNotEquals(d1, d2);
	}
	
	@Test
	public void testOneSideNullNotEqualStringDifferent() throws SpeedyException {
		String data = "data";
		String data2 = "bbbb";
		DataFrame d1 = new DataFrame(5, data.getBytes());
		DataFrame d2 = new DataFrame(5, data2.getBytes());
		assertNotEquals(d1, d2);
	}
}
