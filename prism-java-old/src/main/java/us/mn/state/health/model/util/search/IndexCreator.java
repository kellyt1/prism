package us.mn.state.health.model.util.search;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.ClassUtils;
import us.mn.state.health.matmgmt.util.Constants;

/**
 * Class that creates the indexes
 */
public class IndexCreator {

    private static Log log = LogFactory.getLog(IndexCreator.class);
    private static Map indexes = new HashMap();

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        selectEnvironment();
        selectIndexes();
        Collection idxs = indexes.keySet();
        for (Iterator iterator = idxs.iterator(); iterator.hasNext();) {
            String indexClassName = (String) iterator.next();
            Class clazz = Class.forName(indexClassName);
            //o-is an EntityIndex object
            Object o = indexes.get(indexClassName);
            //invoke createIndex(true)
            Method method = clazz.getMethod("createIndex", new Class[]{boolean.class});
            method.invoke(o, new Object[]{new Boolean(true)} );

            updateIndexes((EntityIndex) o);

        }
    }

    /**
     * Method that replaces the old index with the new index(the temp index)
     */
    private static void updateIndexes(EntityIndex entityIndex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        Method method = entityIndex.getClass().getMethod("getIndexDirectory",new Class[]{});
        File indexDirectory = (File) method.invoke(entityIndex, new Object[]{});
        method = entityIndex.getClass().getMethod("getTempIndexDirectory", new Class[]{});
        File tempIndexDirectory = (File) method.invoke(entityIndex, new Object[]{});

        log.info("Delete " + indexDirectory.toString()+":" + IOUtils.deleteDir(indexDirectory) );
        IOUtils.copyDirectory(tempIndexDirectory, indexDirectory);
        log.info("Delete " + tempIndexDirectory.toString()+":" + IOUtils.deleteDir(tempIndexDirectory));
    }



    private static void selectIndexes(){
        String name ;
        name = "us.mn.state.health.model.util.search.EntityIndex";
        log.info(name);
        List subClasses = ClassUtils.find("us.mn.state.health.model.util.search",name);
        for (Iterator iterator = subClasses.iterator(); iterator.hasNext();) {
            Object entityIndex =  iterator.next();
//            System.out.println(entityIndex.getClass().getName());
            selectEachIndex(entityIndex);
        }
    }

    private static void selectEachIndex(Object o){
        String className = o.getClass().getName();
        String shortClassName = className.substring(className.lastIndexOf(".")+1);
        String yn = readKeyboardInput("Do you want to build the " + shortClassName + "?");
        if(Constants.YES.equalsIgnoreCase(yn)){
            indexes.put(className,o);
        }
        else if (Constants.NO.equalsIgnoreCase(yn)){

        }
        else {
            selectEachIndex(o);
        }
    }

    private static void selectEnvironment() {
        log.info("Choose your environment");
        log.info("DEVELPMENT: A");
        log.info("TEST: B");
        log.info("STAGE: C");
        log.info("PRODUCTION: D");
        String env = readKeyboardInput("Press A,B,C or D");
        if ("a".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY, Constants.DEVDB);
        }
        else if ("b".equalsIgnoreCase(env)){
            System.setProperty(Constants.ENV_KEY,Constants.TEST);

        }
        else if ("c".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY,Constants.STAGE);
        }

        else if ("d".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY,Constants.PROD);
        }
        else {
            selectEnvironment();
        }
    }

    private static String readKeyboardInput(String message) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (str != null) {
                log.info(message);
                log.info("");
                str = in.readLine();
                if (!StringUtils.nullOrBlank(str)) {
                    str = str.trim();
//                    System.out.println(str);
                    return str;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}