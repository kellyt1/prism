package us.mn.state.health.common.exceptions;

public class UploadTextFileException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 6331992359696848921L;

    public UploadTextFileException() {
    }

    public UploadTextFileException(Throwable t) {
        super(t);
    }

    public UploadTextFileException(String message, Throwable t) {
        super(message, t);
    }
}