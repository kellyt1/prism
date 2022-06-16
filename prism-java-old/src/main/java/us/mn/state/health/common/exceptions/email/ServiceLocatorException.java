package us.mn.state.health.common.exceptions.email;

public class ServiceLocatorException extends RuntimeException {
    public ServiceLocatorException(){}
    public ServiceLocatorException(String message){
        super(message);
    }
    public ServiceLocatorException(String message, Throwable cause){
        super(message,cause);

    }
    public ServiceLocatorException(Throwable cause){
        super(cause);
    }

}
