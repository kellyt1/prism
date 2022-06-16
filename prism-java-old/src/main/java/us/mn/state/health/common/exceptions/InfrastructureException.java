package us.mn.state.health.common.exceptions;

public class InfrastructureException extends Exception {

  /**
     * 
     */
    private static final long serialVersionUID = -178504464246973627L;

public InfrastructureException(Exception e) {
    super(e);
  }
  
  public InfrastructureException(String message, Exception e) {
    super(message, e);
  }
}