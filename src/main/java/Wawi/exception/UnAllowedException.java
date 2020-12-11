package Wawi.exception;

/**
 * @author Daniel Koersten
 *
 */
public class UnAllowedException extends Exception {

	private static final long serialVersionUID = 3775713859417721428L;

	/**
	 * Creates a {@link UnAllowedException} object.
	 * 
	 * @param message contains an optional message, which will be printed out.
	 */
	// Constructor that accepts a message
	public UnAllowedException(String message) {
		super(message);
	}
}