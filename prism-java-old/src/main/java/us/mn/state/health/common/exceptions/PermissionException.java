package us.mn.state.health.common.exceptions;

/**
 * This exception is used to mark access violations.
 */
public class PermissionException
	extends RuntimeException {

	/**
     * 
     */
    private static final long serialVersionUID = 1320908307017986523L;

    public PermissionException() {}

	public PermissionException(String message) {
		super(message);
	}

	public PermissionException(String message, Throwable cause) {
		super(message, cause);
	}

	public PermissionException(Throwable cause) {
		super(cause);
	}
}
