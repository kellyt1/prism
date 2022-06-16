package us.mn.state.health.common.lang.test;
import junit.framework.TestCase;
import us.mn.state.health.common.lang.StringUtils;

public class StringUtilsTestCase extends TestCase  {
    public StringUtilsTestCase(String sTestName) {
        super(sTestName);
    }

    /**
     * String stripChar(String, String)
     */
    public void teststripChar() {
        String str = "1,000,000,000";
        str = str.replaceAll(",", "");
        TestCase.assertEquals("1000000000", str);
    }

    /**
     * boolean nullOrBlank(String)
     */
    public void testnullOrBlank() {
    }

    /**
     * String split(String, String, String)
     */
    public void testsplit() throws Exception {
        String s = "this that theother";
        System.out.println("Split: " + s.split(" "));
    }

    public void testGenerateCode() {
        String value = "AIR PRODUCTS & CHEMICALS";
        System.out.println(StringUtils.generateCodeFromName(value));
    }
    
    public void testFormatCurrency() {
        String currency = "2,000.00";
        //currency = StringUtils.formatCurrencySimple(currency);
        //super.assertEquals("Test Format Currency:", "2000.00", currency);
    }
    
    public void testAlphaNumericOnly() {
        String value = "N3t(*@!}]n4y4";
        value = StringUtils.alphaNumericOnly(value);
        assertEquals("N3tn4y4", value);
    }
    
}