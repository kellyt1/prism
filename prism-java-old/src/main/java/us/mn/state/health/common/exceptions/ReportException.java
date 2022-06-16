package us.mn.state.health.common.exceptions;

public class ReportException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 163826927455244635L;

    public ReportException(String message) {
        super(message);
    }
    
    public ReportException(String message, Throwable cause) {
        super(message, cause);
    }
}