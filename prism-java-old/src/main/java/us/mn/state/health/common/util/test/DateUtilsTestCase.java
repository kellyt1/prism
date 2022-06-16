package us.mn.state.health.common.util.test;
import junit.framework.TestCase;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateUtilsTestCase extends TestCase  {
  public DateUtilsTestCase(String sTestName) {
    super(sTestName);
  }

  /**
   * Date createDate(String, String)
   */
  public void testcreateDate() throws Exception {
    String date = "01/01/1930";
    System.out.println("testcreateDate(): " + DateUtils.createDate(date).toString());
  }
  
  public void testToString()  throws Exception {
      String dateStr = DateUtils.toString(new Date(), "M/d/yyyy/H:m:s");
      System.out.println("testToString(): " + dateStr);
  }
  
  public void testFormatDate() throws Exception {
      Date date = DateUtils.createDate("2005-01-05", "yyyy-MM-dd");
      System.out.println("Formatted Date: " + date.toString());
  }

    public void testTwoDates() throws Exception {
        Date contractEndDate = StringUtils.formatDateFromString("01/02/2014");  // expected format "mm/dd/yyyy"
        long endDate = contractEndDate.getTime();
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        long presentDay = today.getTime();

        if (endDate < presentDay)
            System.out.println("endDate less than today");
        else
            System.out.println("endDate greater than today");
    }

    public void testTwoDatesAsDate() throws Exception {
        Date contractEndDate = DateUtils.createDate("2016-01-05", "yyyy-MM-dd");
        long endDate = contractEndDate.getTime();
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        long presentDay = today.getTime();

        if (endDate < presentDay)
            System.out.println("endDate less than today");
        else
            System.out.println("endDate greater than today");
    }

}