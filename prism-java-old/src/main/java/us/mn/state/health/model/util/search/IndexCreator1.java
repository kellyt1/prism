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

import us.mn.state.health.common.io.IOUtils;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.util.ClassUtils;
import us.mn.state.health.matmgmt.util.Constants;

/**
 * Class that creates the indexes
 */
public class IndexCreator1 {

    public static Map indexes = new HashMap();

    public static void main(String[] args) {
        selectEnvironment();
        selectIndexes();
        Collection idxs = indexes.keySet();
        for (Iterator iterator = idxs.iterator(); iterator.hasNext();) {
            String indexClassName = (String) iterator.next();
            IndexCreatorThread t = new IndexCreatorThread(indexClassName);
            t.start();
        }
    }

    /**
     * Method that replaces the old index with the new index(the temp index)
     */
    public static void updateIndexes(EntityIndex entityIndex) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException {
        Method method = entityIndex.getClass().getMethod("getIndexDirectory", new Class[]{});
        File indexDirectory = (File) method.invoke(entityIndex, new Object[]{});
        method = entityIndex.getClass().getMethod("getTempIndexDirectory", new Class[]{});
        File tempIndexDirectory = (File) method.invoke(entityIndex, new Object[]{});

        System.out.println("Delete " + indexDirectory.toString() + ":" + IOUtils.deleteDir(indexDirectory));
        IOUtils.copyDirectory(tempIndexDirectory, indexDirectory);
        System.out.println("Delete " + tempIndexDirectory.toString() + ":" + IOUtils.deleteDir(tempIndexDirectory));
    }


    private static void selectIndexes() {
        String name;
        name = "us.mn.state.health.model.util.search.EntityIndex";
        System.out.println(name);
        List subClasses = ClassUtils.find("us.mn.state.health.model.util.search", name);
        for (Iterator iterator = subClasses.iterator(); iterator.hasNext();) {
            Object entityIndex = iterator.next();
//            System.out.println(entityIndex.getClass().getName());
            selectEachIndex(entityIndex);
        }
    }

    private static void selectEachIndex(Object o) {
        String className = o.getClass().getName();
        String shortClassName = className.substring(className.lastIndexOf(".") + 1);
        String yn = readKeyboardInput("Do you want to build the " + shortClassName + "?");
        if (Constants.YES.equalsIgnoreCase(yn)) {
            indexes.put(className, o);
        } else if (Constants.NO.equalsIgnoreCase(yn)) {

        } else {
            selectEachIndex(o);
        }
    }

    private static void selectEnvironment() {
        System.out.println("Choose your environment");
        System.out.println("DEVELPMENT: A");
        System.out.println("TEST: B");
        System.out.println("PRODUCTION: C");
        String env = readKeyboardInput("Press A,B or C");
        if ("a".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY, Constants.DEVDB);
        } else if ("b".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY, Constants.TEST);

        } else if ("c".equalsIgnoreCase(env)) {
            System.setProperty(Constants.ENV_KEY, Constants.PROD);
        } else {
            selectEnvironment();
        }
    }

    private static String readKeyboardInput(String message) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String str = "";
            while (str != null) {
                System.out.print(message);
                System.out.println();
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

class IndexCreatorThread extends Thread {
    private String indexClassName;

    public IndexCreatorThread(String indexClassName) {
        this.indexClassName = indexClassName;
    }

    public void run() {
        Class clazz = null;

        try {
            clazz = Class.forName(indexClassName);

            //o-is an EntityIndex object
            Object o = IndexCreator1.indexes.get(indexClassName);
            //invoke createIndex(true)
            Method method = clazz.getMethod("createIndex", new Class[]{boolean.class});
            method.invoke(o, new Object[]{new Boolean(true)});

            IndexCreator1.updateIndexes((EntityIndex) o);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}