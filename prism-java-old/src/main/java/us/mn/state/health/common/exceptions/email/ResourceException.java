package us.mn.state.health.common.exceptions.email;

public class ResourceException extends RuntimeException{
    public ResourceException(){}
    public ResourceException(String message){
        super(message);
    }
    public ResourceException(String message, Throwable cause){
        super(message,cause);

    }
    public ResourceException(Throwable cause){
        super(cause);
    }
}
