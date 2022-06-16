package us.mn.state.health.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;


public class NotYetImplementedException extends RuntimeException {
    private static Log log = LogFactory.getLog(NotYetImplementedException.class);
    private Exception e;

    public NotYetImplementedException(Exception e) {
    	this.e = e;
    }

    public NotYetImplementedException() {
        super("Not Yet Implemented");
    }

    public NotYetImplementedException(String m) {
        super("Not Yet Implemented:" + m);
    }
	public void printStackTrace() {
		super.printStackTrace();
		if (e != null) {
			log.error("Nested exception -----");
			e.printStackTrace();
		}

	}

	public void printStackTrace(PrintStream arg0) {
		super.printStackTrace(arg0);
		if (e != null) {
			arg0.println("Nested exception -----");
			e.printStackTrace(arg0);
		}
	}

	public void printStackTrace(PrintWriter arg0) {
		super.printStackTrace(arg0);
		if (e != null) {
			arg0.println("Nested exception -----");
			e.printStackTrace(arg0);
		}
	}
}
