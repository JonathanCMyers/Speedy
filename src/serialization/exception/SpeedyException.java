/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization.exception;

/**
 * SpeedyException class to manage formatting errors from the Frame packets
 * @author Feng Yang, Jonathan Myers, Nathan Stickney
 * @version 1.0 Build 3/15/2017
 */
public class SpeedyException extends Exception {
	
	/**
	 * Serialization ID for SpeedyException
	 */
	private static final long serialVersionUID = -158269553419834096L;
	
	/**
	 * SpeedyException to throw an exception with a given message and cause
	 * @param message String that stores information on the exception being thrown
	 * @param cause Throwable that stores information on the exception being thrown
	 */
	public SpeedyException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * SpeedyException to throw an exception with a given message
	 * @param message String that stores information on the exception being thrown
	 */
	public SpeedyException(String message) {
		super(message);
	}
	
}