/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/23/2017          *
 ********************************/

package utility;

/**
 * Contains constants for use throughout the project
 * @author Feng Yang
 * @version 1.0 Build 03/23/2017
 */
public class ConstUtility {
	
	public static final short VERSION = 1;//
	public static final int TYPE_HELLO_MSG = 4;//
	public static final int CONTROL_HEADER_BYTE_LENGTH = 8;// The length of header of abstract control frame
	public static final short SYN_STREAM_NUM = 1;//The number for the type of syn stream frame
	public static final short SYN_REPLY_NUM = 2;//The number for the type of syn reply frame
	public static final short FIN_STREAM_NUM = 3;//The number for the type of fin stream frame
	public static final short GOAWAY = 7;//The number for the type of go away frame
	
	
	public static final short SYN_STREAM_DEFUALT_PRIORITY = 0;
	public static final short NUMBER_OF_PAIRS_DEFAULT = 0;
	public static final int VERSION_BYTE_LENGTH = 2;
	public static final int TYPE_BYTE_LENGTH = 2;
	public static final int FLAGS_BYTE_LENGTH = 1;
	public static final int LENGTH_BYTE_LENGTH = 3;
	public static final int STREAMID_BYTE_LENGTH = 4;
	public static final int DATA_STREAM_HEADER_LENGTH = 8;
	public static final int  BLOCK_NAME_LENGTH_LENGTH = 2;//The length of the Length of Name
	public static final int  BLOCK_VALUE_LENGTH_LENGTH = 2;//The length of the Length of Value
	public static final int  SYNSTREAM_HEADER_LENGTH = 16;//The length of the Length of Value
	public static final int  SYNSTREAM_DEFAULT_DATA_LENGTH = 8;//The length of the Length of Value
	public static final int  SYNSTREAM_PRIORITY_LENGTH = 1;//The length of the Length of priority, 2 bits
	public static final int  SYNSTREAM_UNUSED_LENGTH = 1;//The length of the unused,14 bits
	public static final int  REPLY_UNUSED_LENGTH = 2;//The length of the unused,16 bits
	public static final int  NUM_OF_PAIRS_LENGTH = 2;//The length of numOfPairs
	
	public static final int  FIN_DATA_LENGTH = 8;//The value of the length field in the frame
	public static final int  FINSTREAM_LENGTH = 16;//The length of FINSTREAM frame, in bytes
	public static final int  FINSTREAM_STATUS_CODE_LENGTH = 4;//The length of status code in FINSTREAM frame, in bytes
	
	public static final int  GOAWAY_DEFAULT_LENGTH = 4;//The value of length in GoAway frame.
	public static final int  GOAWAY_FRAME_LENGTH = 12;//The length of the whole goaway frame.
	
	
	//ID numbers for hello message
	public static final int HELLO_BANDWIDTH_TO_YOU = 1;//
	public static final int HELLO_BANDWIDTH_FROM_YOU  = 2;//
	public static final int HELLO_ROUND_TRIP_TIME  = 3;//
	public static final int HELLO_MAX_CONCURRENT_STREAMS  = 4;//
}
