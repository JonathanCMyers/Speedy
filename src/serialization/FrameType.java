/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/
package serialization;

import serialization.exception.SpeedyException;

/**
 * The types of Frame
 * @author Feng Yang,Jonathan Myers,Nathaniel Stickney
 *
 */

public enum FrameType {
	
	DATA(0),
	SYN_STREAM(1),
	SYN_REPLY(2),
	FIN_STREAM(3),
	SETTINGS(4),
	GOAWAY(5),
	HEADERS(6);
	
	
	/**
	 * Integer representation of the frame type
	 */
	private int type;
	
	/**
	 * FrameType constructor
	 * @param type integer representation of the frame type
	 */
	private FrameType(int type) {
		this.type = type;
	}
	
	/**
	 * FrameType constructor
	 * @param type integer representation of the frame type
	 */
	private FrameType(short type) {
		this.type = type;
	}
	
	/**
	 * Returns the integer identifier for the given frame type
	 * @return type
	 */
	public int getType() {
		return type;
	}
	
	
	/**
	 * Returns the PacketTYpe for an equivalent integer
	 * @param type integer representation of the packet type
	 * @return PacketType equivalent for a given integer
	 * @throws QUICException if int provided is not within the supported PacketTypes
	 */
	public static FrameType getFrameType(int type) throws SpeedyException {
		switch(type) {
		case 0:
			return DATA;
		case 1:
			return SYN_STREAM;
		case 2:
			return SYN_REPLY;
		case 3:
			return FIN_STREAM;
		case 4:
			return SETTINGS;
		case 7:
			return GOAWAY;
		case 8:
			return HEADERS;
		default:
			throw new SpeedyException("Unknown Frame type provided.");
		}
		
	}
	
}
