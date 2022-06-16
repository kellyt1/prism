package us.mn.state.health.model.util.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO;
import us.mn.state.health.persistence.HibernateUtil;

public class Config {
    private static Log log = LogFactory.getLog(Config.class); 
    private static Config config = new Config();
    private static Map configMap = Collections.synchronizedMap(new HashMap());

    private Config() { }

    public static String getConfig(final String appCode, final String configCode) throws InterruptedException {
        /**
         * start a thread here to use the dao
         */
        Foo t = new Foo(appCode, configCode);
        t.start();
        t.join();
        return (String) configMap.get("result");
    }

    public static Map getConfigMap() {
        return configMap;
    }

    public static void setConfig(String appCode, String configCode, String value) {
    }

    public static Config getInstance() {
        return config;
    }
}

class Foo extends Thread {
    private static Log log = LogFactory.getLog(Foo.class); 
    private String appCode;
    private String configCode;

    public Foo(String appCode, String configCode) {
        this.appCode = appCode;
        this.configCode = configCode;
    }

    public void run() {
        HibernateConfigurationItemDAO configurationItemDAO = new HibernateConfigurationItemDAO();
        try {
            ConfigurationItem configurationItem = configurationItemDAO.getConfigurationItem(appCode, configCode);
            log.debug("value=" + configurationItem.getValue());
            Config.getConfigMap().put("result", configurationItem.getValue());
        } 
        catch(InfrastructureException e) {
            log.error("Error in Config.run()",e);
        }
        try {
            HibernateUtil.commitTransaction();
            HibernateUtil.closeSession();
        } 
        catch(InfrastructureException e) {
            log.error("Error in Config.run2()",e);
        }

    }
}