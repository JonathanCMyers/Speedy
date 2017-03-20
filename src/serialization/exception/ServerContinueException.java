/********************************
 * Authors:  Feng Yang          *
 * 	         Jonathan Myers     *
 *           Nathaniel Stickney *
 * Course:   CSI 5321           *
 * Date:     3/19/2017          *
 ********************************/

package serialization.exception;

/**
 * ServerBreakException to handle internal server logic when a server should close a connection with a client
 * @author Feng Yang, Jonathan Myers, Nathan Stickney
 * @version 1.0 Build 3/19/2017
 */
public class ServerContinueException extends Exception {
	
	/**
	 * Serialization ID for SpeedyException
	 */
	private static final long serialVersionUID = -1345390785827599884L;
	
	/**
	 * ServerContinueException to throw an exception with a given message and cause
	 * @param message String that stores information on the exception being thrown
	 * @param cause Throwable that stores information on the exception being thrown
	 */
	public ServerContinueException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * ServerContinueException to throw an exception with a given message
	 * @param message String that stores information on the exception being thrown
	 */
	public ServerContinueException(String message) {
		super(message);
	}
	
}