/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     4/11/2017          *
 ********************************/

package serialization.exception;

/**
 * NetworkCloseException class to manage when the client and server disconnect
 * @author Feng Yang, Jonathan Myers, Nathan Stickney
 * @version 1.0 Build 4/11/2017
 */
public class NetworkCloseException extends SpeedyException {
	
	/**
	 * Serialization ID for DataFrameException
	 */
	private static final long serialVersionUID = 674179712841838622L;
	
	/**
	 * NetworkCloseException to throw an exception with a given message and cause by data frame
	 * @param message String that stores information on the exception being thrown
	 * @param cause Throwable that stores information on the exception being thrown
	 */
	public NetworkCloseException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * NetworkCloseException to throw an exception with a given message
	 * @param message String that stores information on the exception being thrown
	 */
	public NetworkCloseException(String message) {
		super(message);
	}
	
}