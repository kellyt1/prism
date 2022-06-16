package us.mn.state.health.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.matmgmt.util.Constants;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Environment {
    public static final String PROD_SERVER_NAME = "web";
    public static final String PROD_PRISM_URL = "prism.web.health.state.mn.us";
    public static final String NONPROD_SERVER_NAME = "nonprod";
    public static final String NONPROD_PRISM_URL = "prism.nonprod.health.state.mn.us";
    public static final String LOCALHOST_SERVER_NAME = "localhost";
    public static final String LOCALHOST_PRISM_URL = "localhost:7080";
    private static Log log = LogFactory.getLog(Environment.class);

    public static boolean isNotProduction() {
        return !isProduction();
    }

    public static boolean isNotTest() {
        return !isTest();
    }

    public static boolean isNotLocalhost() {
        return !isLocalhost();
    }

    public static boolean isProduction() {
            return Constants.ENVIRONMENT.toLowerCase().contains(PROD_SERVER_NAME);
    }

    public static boolean isTest() {
            return Constants.ENVIRONMENT.toLowerCase().contains(NONPROD_SERVER_NAME);
    }

    public static boolean isLocalhost() {
            return Constants.ENVIRONMENT.toLowerCase().toLowerCase().contains(LOCALHOST_SERVER_NAME);
    }


    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "";
    }
}