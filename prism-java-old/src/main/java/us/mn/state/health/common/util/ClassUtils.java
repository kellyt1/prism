package us.mn.state.health.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class ClassUtils {
    private static Log log = LogFactory.getLog(ClassUtils.class);
    /**
     * Display all the classes inheriting or implementing a given
     * class in the currently loaded packages.
     *
     * @param tosubclassname the name of the class to inherit from
     */
    public static List find(String tosubclassname) {
        List results = new ArrayList();
        try {
            Class tosubclass = Class.forName(tosubclassname);
            Package [] pcks = Package.getPackages();
            for (int i = 0; i < pcks.length; i++) {
                results = find(pcks[i].getName(), tosubclass);
            }
        } catch (ClassNotFoundException ex) {
            log.error("Class " + tosubclassname + " not found!");
        }
        return results;
    }

    /**
     * Display all the classes inheriting or implementing a given
     * class in a given package.
     *
     * @param pckname        the fully qualified name of the package
     * @param tosubclassname the name of the class to inherit from
     */
    public static List find(String pckname, String tosubclassname) {
        List results = new ArrayList();
        try {
            Class tosubclass = Class.forName(tosubclassname);
            results  = find(pckname, tosubclass);
        } catch (ClassNotFoundException ex) {
            log.error("Class " + tosubclassname + " not found!");
        }
        return results;
    }

    /**
     * Display all the classes inheriting or implementing a given
     * class in a given package.
     *
     * @param pckgname   the fully qualified name of the package
     * @param tosubclass the Class object to inherit from
     */
    public static List find(String pckgname, Class tosubclass) {
        // Code from JWhich
        // ======
        // Translate the package name into an absolute path
        List results = new ArrayList();
        String name = new String(pckgname);
        if (!name.startsWith("/")) {
            name = "/" + name;
        }
        name = name.replace('.', '/');

        // Get a File object for the package
        URL url = ClassUtils.class.getResource(name);
        // URL url = tosubclass.getResource(name);
        // URL url = ClassLoader.getSystemClassLoader().getResource(name);
//	System.out.println(name+"->"+url);

        if (url == null) return results;

        File directory = new File(url.getFile());

        // New code
        // ======
        if (directory.exists()) {
            // Get the list of the files contained in the package
            String [] files = directory.list();
            for (int i = 0; i < files.length; i++) {

                // we are only interested in .class files
                if (files[i].endsWith(".class")) {
                    // removes the .class extension
                    String classname = files[i].substring(0, files[i].length() - 6);
                    try {
                        // Try to create an instance of the object
                        Object o = Class.forName(pckgname + "." + classname).newInstance();
                        if (tosubclass.isInstance(o)) {
                            log.info(classname);
                            results.add(o);
                        }
                    } catch (ClassNotFoundException cnfex) {
                        log.error(cnfex);
                    } catch (InstantiationException iex) {
                        // We try to instanciate an interface
                        // or an object that does not have a
                        // default constructor
                    } catch (IllegalAccessException iaex) {
                        // The class is not public
                    }
                }
            }
        }
        else {
            try {
                // It does not work with the filesystem: we must
                // be in the case of a package contained in a jar file.
                JarURLConnection conn = (JarURLConnection) url.openConnection();
                String starts = conn.getEntryName();
                JarFile jfile = conn.getJarFile();
                Enumeration e = jfile.entries();
                while (e.hasMoreElements()) {
                    ZipEntry entry = (ZipEntry) e.nextElement();
                    String entryname = entry.getName();
                    if (entryname.startsWith(starts)
                            && (entryname.lastIndexOf('/') <= starts.length())
                            && entryname.endsWith(".class")) {
                        String classname = entryname.substring(0, entryname.length() - 6);
                        if (classname.startsWith("/"))
                            classname = classname.substring(1);
                        classname = classname.replace('/', '.');
                        try {
                            // Try to create an instance of the object
                            Object o = Class.forName(classname).newInstance();
                            if (tosubclass.isInstance(o)) {
                                log.info(classname.substring(classname.lastIndexOf('.') + 1));
                                results.add(o);
                            }
                        } catch (ClassNotFoundException cnfex) {
                            log.error(cnfex);
                        } catch (InstantiationException iex) {
                            // We try to instanciate an interface
                            // or an object that does not have a
                            // default constructor
                        } catch (IllegalAccessException iaex) {
                            // The class is not public
                        }
                    }
                }
            } catch (IOException ioex) {
                log.error(ioex);
            }
        }

        return results;
    }
}
