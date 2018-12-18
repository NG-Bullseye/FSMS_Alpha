package kickstart.exception;

public class UnAllowedException extends Exception { 

	private static final long serialVersionUID = 3775713859417721428L;

	// Constructor that accepts a message
      public UnAllowedException(String message)
      {
    	  super(message);
      }
 }