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
import serialization.GoAway;
import serialization.GoAway;
import serialization.HeaderBlock;
import serialization.MessageInput;
import serialization.SynStream;
import serialization.exception.SpeedyException;
import utility.ConstUtility;

public class GoawayTest {
	@Test
	public void testEmptyEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		GoAway ga1 = new GoAway();
		assertEquals(ga, ga1);
	}
	
	@Test
	public void testEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		ga.setStatusCode(ConstUtility.GOAWAY_STATUS_OK);
		GoAway ga1 = new GoAway();
		ga.setStatusCode(ConstUtility.GOAWAY_STATUS_OK);
		assertEquals(ga, ga1);
	}
	
	@Test
	public void testLastStreamIDEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		ga.setLastStreamID(1);
		GoAway ga1 = new GoAway();
		ga1.setLastStreamID(1);
		assertEquals(ga, ga1);
	}
	
	@Test
	public void testNotEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		GoAway ga1 = new GoAway();
		ga.setStatusCode(ConstUtility.GOAWAY_STATUS_OK);
		assertEquals(ga, ga1);
	}
	
	@Test
	public void testNotNUllEquals() throws SpeedyException {
		GoAway ga = new GoAway();
		GoAway ga1 = null;
		assertNotEquals(ga, ga1);
	}
	
	@Test
	public void testEncodeDecodeReflexive() throws SpeedyException {
		GoAway ga1 = new GoAway();
		ga1.setLastStreamID(10);
		byte[] encodedBytes = ga1.encode();
		InputStream in = new ByteArrayInputStream(encodedBytes);
		MessageInput msgin = new MessageInput(in);
		GoAway ga2  = (GoAway)Frame.decodeFrame(msgin);
		try {
			byte[] b = msgin.getNumberBytes(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(ga1,ga2);
		
	}
	
	
	
}
