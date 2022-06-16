package us.mn.state.health.test.struts;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static void main(String[] args) {
//		junit.swingui.TestRunner.run(AllTests.class);
        junit.textui.TestRunner.run(suite());
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(DeliveryAddressActionTest.class));
		return suite;
	}
}