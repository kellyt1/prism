package us.mn.state.health.common.util.test;
import junit.framework.Test;
import junit.framework.TestSuite;

public class UtilTestSuite  {
    public static Test suite() {
        TestSuite suite;
        suite = new TestSuite("UtilTestSuite");

        suite.addTestSuite(us.mn.state.health.common.util.test.CollectionUtilsTestCase.class);
        suite.addTestSuite(us.mn.state.health.common.util.test.DateUtilsTestCase.class);

        return suite;
    }

    public static void main(String args[]) {
        String args2[] = {"-noloading", "us.mn.state.health.common.util.test.UtilTestSuite"};

        junit.swingui.TestRunner.main(args2);
    }
}