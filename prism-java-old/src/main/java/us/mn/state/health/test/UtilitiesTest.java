package us.mn.state.health.test;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;

import junit.framework.TestCase;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.util.search.RequestIndex;

public class UtilitiesTest extends TestCase {

    protected void setUp() {

    }

    public void testThis() throws InfrastructureException {
        String reportFilesDirectory =
                new HibernateConfigurationItemDAO().getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE,
                        Constants.INVENTORY_REPORTS_PATH_CODE)
                        .getValue();

        String inFileName = reportFilesDirectory + File.separatorChar + "picklist.jasper";
        String outFileName = reportFilesDirectory + File.separatorChar + "picklist.pdf";
        System.out.println(inFileName);
        System.out.println(outFileName);
    }

    public void testRequestIndexPath() throws InfrastructureException {
        System.out.println(RequestIndex.getCurrentIndexDirectory());
    }

    public void testDate() {
        try {
            // Some examples
            DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = (Date) formatter.parse("05/16/1995");
            System.out.println(date);
        }
        catch (ParseException e) {
        }

        System.out.println(new Date().compareTo(null));
    }

    public void testIcnbr() {
        String icnbr = "111-0111";
        System.out.println(Integer.decode(icnbr.substring(4)));
        String categoryCode = icnbr.substring(0, icnbr.indexOf("-"));
        String itemCode = icnbr.substring(icnbr.indexOf("-") + 1);
        System.out.println(categoryCode);
        System.out.println(itemCode);
    }

    public void testFormatNumber() {
        NumberFormat formatter = new DecimalFormat("0000");
        String s = formatter.format(23);
        System.out.println(s);
    }

    public void testFormatNumberLibrary() {
        System.out.println(StringUtils.formatStringNumber("1222", 4));
    }

    public void testThisDate() throws ParseException {
        String stringDate = DateUtils.toString(new Date(), "yyyyMMdd");
        System.out.println(stringDate);
        Date date = DateUtils.createDate(stringDate, "yyyyMMdd");
        System.out.println(date);
    }

    public void testDisplayFilesInDir() {
        File dir = new File("C:\\projects\\java\\MaterialsManagement\\lib");

        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
            }
        }

        // It is also possible to filter the list of returned files.
        // This example does not return any files that start with `.'.
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return !name.startsWith(".");
            }
        };
        children = dir.list(filter);

        // The list of files can also be retrieved as File objects
        File[] files = dir.listFiles();

        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        };
        files = dir.listFiles(fileFilter);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.getName().toLowerCase().endsWith(".jar")) {
                System.out.print("../lib/" + file.getName() + ":");
//                System.out.print("../lib/"+file.getName()+";");
            }
        }
    }

    public void testRemoveLeadingNonAlphaNumericChars() {
        String value = "+";
        System.out.println(StringUtils.removeLeadingNonAlphaNumericChars(value));
        String[] result = "this \"is\" a test".split("[\\s\"]");
        for (int x = 0; x < result.length; x++)
            System.out.println("|" + result[x] + "|" + result[x].length());

    }

    public void testCheckIfStringContainsWord() {
        String string = "abc!@#@$#@$@#$  {123} 34$%*& QWE";
        String word = "123";
        String word1 = "12";
        assertTrue(StringUtils.checkIfStringContainsWord(string, word));
        assertFalse(StringUtils.checkIfStringContainsWord(string, word1));
    }

    public void testMachineName() {
        try {
            java.net.InetAddress i = java.net.InetAddress.getLocalHost();
            System.out.println(i);                  // name and IP address
            System.out.println(i.getHostName());    // name
            System.out.println(i.getHostAddress()); // IP address only
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("================");
        try {
         Enumeration e = NetworkInterface.getNetworkInterfaces();

         while(e.hasMoreElements()) {
            NetworkInterface netface = (NetworkInterface)
            e.nextElement();
            System.out.println("Net interface: "+netface.getName());

            Enumeration e2 = netface.getInetAddresses();

            while (e2.hasMoreElements()){
               InetAddress ip = (InetAddress) e2.nextElement();
               System.out.println("IP address: "+ip.toString());
            }
         }
      }
      catch (Exception e) {
         System.out.println ("e: " + e);
      }
    }

    protected void tearDown() {

    }
}