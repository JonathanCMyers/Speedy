/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/15/2017          *
 ********************************/

package serialization.exception;

/**
 * DataFrameException class to manage formatting errors from the Frame packets
 * @author Feng Yang, Jonathan Myers, Nathan Stickney
 * @version 1.0 Build 3/17/2017
 */
public class DataFrameException extends Exception {
	
	/**
	 * Serialization ID for DataFrameException
	 */
	private static final long serialVersionUID = -128384179262484160L;
	
	/**
	 * DataFrameException to throw an exception with a given message and cause by data frame
	 * @param message String that stores information on the exception being thrown
	 * @param cause Throwable that stores information on the exception being thrown
	 */
	public DataFrameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * DataFrameException to throw an exception with a given message
	 * @param message String that stores information on the exception being thrown
	 */
	public DataFrameException(String message) {
		super(message);
	}
	
}