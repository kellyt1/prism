package us.mn.state.health.common.exceptions;

/**
 * This exception is used to mark business rule violations.
 */
public class BusinessException extends Exception {

	/**
     * 
     */
    private static final long serialVersionUID = 9059748918420490122L;

    public BusinessException() {}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
