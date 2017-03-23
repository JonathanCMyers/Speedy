/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package utility;

/**
 * Contains constants for use throughout the project
 * @author MyersJO
 * @version 1.0 Build 3/15/2017
 */
public class SpeedyUtility {
	
	public static final boolean USING_SSL = false;
	
	public static final short VERSION = 1;//
	public static final int TYPE_HELLO_MSG = 4;//
	public static final int CONTROL_HEADER_BYTE_LENGTH = 4;// The length of header of abstract control frame
	public static final short SYN_STREAM_NUM = 1;
	
	public static final short SYN_STREAM_DEFUALT_PRIORITY = 0;
	public static final int VERSION_BYTE_LENGTH = 2;
	public static final int TYPE_BYTE_LENGTH = 2;
	public static final int FLAGS_BYTE_LENGTH = 1;
	public static final int LENGTH_BYTE_LENGTH = 3;
	public static final int STREAMID_BYTE_LENGTH = 4;
	
	
	//ID numbers for hello message
	public static final int HELLO_BANDWIDTH_TO_YOU = 1;//
	public static final int HELLO_BANDWIDTH_FROM_YOU  = 2;//
	public static final int HELLO_ROUND_TRIP_TIME  = 3;//
	public static final int HELLO_MAX_CONCURRENT_STREAMS  = 4;//
}
