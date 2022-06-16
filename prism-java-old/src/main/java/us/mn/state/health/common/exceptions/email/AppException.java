package us.mn.state.health.common.exceptions.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AppException extends RuntimeException {
    private Throwable nestedException;
    private static Log log = LogFactory.getLog(AppException.class);

    /**
     * Creates a new <code>AppException</code> instance with null as
     * its detail message.
     */
    public AppException() {
        super();
        log.debug("The noarg AppException constructor was called.");
    }

    /**
     * Creates a new <code>AppException</code> instance with the
     * specified detail message.
     *
     * @param message a <code>String</code> value
     */
    public AppException(String message) {
        super(message);
        log.debug("AppException: " + message);
    }

    /**
     * Creates a new <code>AppException</code> instance with the
     * specified detail message and cause.
     *
     * @param message a <code>String</code> value
     * @param cause a <code>Throwable</code> value
     */
    public AppException(String message, Throwable cause) {
        super(message);
        log.debug("AppException: " + message);
        nestedException = cause;
    }

    /**
     * Creates a new <code>AppException</code> instance with
     * the specified cause and a detail message of
     * (cause==null ? null : cause.toString()) (which
     * typically contains the class and detail message of
     * cause).
     *
     * @param cause a <code>Throwable</code> value
     */
    public AppException(Throwable cause) {
        super();
        nestedException = cause;
        log.debug("AppException: " + cause.getMessage());
    }

    public Throwable getNestedException(){
        return nestedException;
    }
}
