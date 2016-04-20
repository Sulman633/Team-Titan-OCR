package lexicon;

import java.io.Serializable;

/**
 * Exception to be thrown by {@link Lexicon} when an invalid String is passed to the useWord method.
 * @author Nathan Van Dyken
 */

public class InvalidWordException extends Exception {

	/**
	 * @see Exception#Exception(String)
	 * @param message - see reference below
	 */
	public InvalidWordException(String message) {
		super(message);
	}

	/**
	 * The serialVersionUID allows the exception to implement {@link Serializable} .
	 */
	private static final long serialVersionUID = 7889517420559189434L;

}