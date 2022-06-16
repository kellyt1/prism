package us.mn.state.health.util;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.inventory.Unit;

public class Utilities {
    private static Log log = LogFactory.getLog(Utilities.class);

    public static DAOFactory getDAOFactory(String dataStoreType) {
        printMessage("dataStoreType = " + dataStoreType);
        DAOFactory daoFactory;

        if (dataStoreType.equalsIgnoreCase("ORACLE")) {
            daoFactory = DAOFactory.getDAOFactory(DAOFactory.ORACLE);
        }
        else if (dataStoreType.equalsIgnoreCase("LDAP")) {
            daoFactory = DAOFactory.getDAOFactory(DAOFactory.LDAP);
        }
        else if (dataStoreType.equalsIgnoreCase("HIBERNATE")) {
            daoFactory = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE);
        }
        else {
            printMessage("Datasource not properly specified");
            daoFactory = null;
        }
        return daoFactory;
    }

    private static void printMessage(String message) {
        log.debug(message);
    }


    public static String decodeUnitCode(String code) {
        final String BOTTLE_CODES = "Bottle,BTL',BTL";
        final String BUNDLE_CODES = "Bundle,BNDL,BDL";
        final String CARTON_CODES = "CARTON,CRTN,CTN";
        final String CASE_CODES = "CASE,CS";
        final String EACH_CODES = "EA,EACH',EACH";
        final String PACK_CODES = "PACK,PK,PKG";
        final String PAD_CODES = "PAD,PD";
        final String UNKNOWN_CODES = "null, ,";
        final String SHEET_CODES = "SHEET,SHT";
        final String DOZEN_CODES = "DOZ,DOZN";
        final String BOX_CODES = "BOX,BX";
        final String LOT_CODES = "LOT";
        final String REAM_CODES = "REAM";
        final String SET_CODES = "SET,ST";

        if (stringContainsToken(DOZEN_CODES, code)) {
            return Unit.CODE_DOZEN;
        }
        if (stringContainsToken(BOX_CODES, code)) {
            return Unit.CODE_BOX;
        }
        if (stringContainsToken(LOT_CODES,code)) {
            return Unit.CODE_LOT;
        }
        if (stringContainsToken(REAM_CODES,code)) {
            return Unit.CODE_REAM;
        }
        if (stringContainsToken(SET_CODES,code)) {
            return Unit.CODE_SET;
        }
        if (stringContainsToken(BOTTLE_CODES, code)) {
            return Unit.CODE_BOTTLE;
        }
        if (stringContainsToken(BUNDLE_CODES, code)) {
            return Unit.CODE_BUNDLE;
        }
        if (stringContainsToken(CARTON_CODES, code)) {
            return Unit.CARTON_BUNDLE;
        }
        if (stringContainsToken(CASE_CODES, code)) {
            return Unit.CODE_CASE;
        }
        if (stringContainsToken(EACH_CODES, code)) {
            return Unit.CODE_EACH;
        }
        if (stringContainsToken(PACK_CODES, code)) {
            return Unit.CODE_PACK;
        }
        if (stringContainsToken(PAD_CODES, code)) {
            return Unit.CODE_PAD;
        }
        if (stringContainsToken(UNKNOWN_CODES, code) || code == null) {
            return Unit.CODE_UNKNOWN;
        }
        if (stringContainsToken(SHEET_CODES, code)) {
            return Unit.CODE_SHEET;
        }
        return code.toUpperCase();
    }

    private static boolean stringContainsToken(String string, String token) {
        if (token == null) {
            return false;
        }else{
            StringTokenizer stringTokenizer = new StringTokenizer(string, ",");
            while (stringTokenizer.hasMoreTokens()) {
                String nextToken = stringTokenizer.nextToken(",");
                if (token.equalsIgnoreCase(nextToken.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
    }
}