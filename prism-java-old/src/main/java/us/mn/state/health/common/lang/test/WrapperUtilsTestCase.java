package us.mn.state.health.common.lang.test;
import junit.framework.TestCase;
import us.mn.state.health.common.lang.WrapperUtils;

public class WrapperUtilsTestCase extends TestCase  {
    public WrapperUtilsTestCase(String sTestName) {
        super(sTestName);
    }
    
    public void testincrement() throws Exception {
        assertEquals("1", WrapperUtils.increment("0"));
        //super.assertNull(null, WrapperUtils.increment(null));
    }
    
    public void testadd() throws Exception {
        assertEquals("1", WrapperUtils.add("1","0"));
        assertEquals("4", WrapperUtils.add("1","3"));
        assertNull(null, WrapperUtils.add(null, null));
    }
    
    public void testRound() throws Exception {
        String number = "12.0";
        System.out.println("Inputed Number: " + number);
        number = WrapperUtils.round(number, 2);
        System.out.println("Rounded Number: " + number);
        
        number = "12.0365";
        System.out.println("Inputed Number: " + number);
        number = WrapperUtils.round(number, 2);
        System.out.println("Rounded Number: " + number);
        
        number = "12";
        System.out.println("Inputed Number: " + number);
        number = WrapperUtils.round(number, 2);
        System.out.println("Rounded Number: " + number);
    }

}