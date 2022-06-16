package us.mn.state.health.common.io;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import javax.servlet.http.HttpServlet;

/**
 * A class containing some common IO Utilities
 *
 * @author Jason Stull
 */
public class IOUtils {

    /**
     * Attempts to close a java.io Reader implementation
     *
     * @param reader Reader Implementation
     */
    public static void close(Reader reader) {
        try {
            reader.close();
        } catch (Exception e) {
            //do nothing
        }
    } // end close

    /**
     * Attempts to close a java.io OutputStream implementation
     *
     * @param stream OutputStream Implementation
     */
    public static void close(OutputStream stream) {
        try {
            stream.close();
        } catch (Exception e) {
            //do nothing
        }
    } // end close

    public static byte[] readFile(String fileName) throws IOException {
        FileReader fr = null;
        ByteArrayOutputStream baos = null;
        try {
            File file = new File(fileName);
            fr = new FileReader(file);
            baos = new ByteArrayOutputStream();
            int c;
            while ((c = fr.read()) != -1) {
                baos.write(c);
            }
            return baos.toByteArray();
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            close(fr);
            close(baos);
        }
    }

    public static void writeFile(String fileName, byte[] binaryValue,
                                 boolean append) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            fos.write(binaryValue);
        } catch (IOException ioe) {
            throw ioe;
        } finally {
            close(fos);
        }

    }

    /**
     * If the directory is not empty, it is necessary to first recursively delete all files
     * and subdirectories in the directory. Here is a method that will delete a non-empty directory.
     * Deletes all files and subdirectories under dir.
     * Returns true if all deletions were successful.
     * If a deletion fails, the method stops attempting to delete and returns false.
     *
     * @param dir
     * @return boolean value that represents the succes of the operation
     */

    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

    public static void copyDirectory(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) {
                dstDir.mkdir();
            }

            String[] children = srcDir.list();
            for (int i=0; i<children.length; i++) {
                copyDirectory(new File(srcDir, children[i]),
                                     new File(dstDir, children[i]));
            }
        } else {
            copyFile(srcDir, dstDir);
        }
    }

    public static void copyFile(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }
    
    public static File getRealPath(HttpServlet servlet, String relativePath) {
        return new File(servlet.getServletContext().getRealPath(relativePath));
    }

} // end IOUtils



