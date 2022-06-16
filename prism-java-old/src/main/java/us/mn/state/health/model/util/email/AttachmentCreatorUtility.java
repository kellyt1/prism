package us.mn.state.health.model.util.email;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

public class AttachmentCreatorUtility {
    private static Log log = LogFactory.getLog(AttachmentCreatorUtility.class);

    /**
     * This method creates the byteBlob of the attachment using the InputStream of the AttachmentBean
     *
     * @param attachment
     * @return
     * @throws Exception
     */
    public static int create(AttachmentBean attachment) throws Exception {
        InputStream rawData = attachment.getFileBlob();
        int nextValue = -1;

        if (rawData == null) {
            log.error("rawdata is null");
            return nextValue;
        } else {
            try {
                attachment.setByteBlob(getByteArrayFromInputStream(rawData));
                return nextValue;
            } catch (Exception sqle) {
                throw sqle;
            } finally {

            }
        }
    }

    private static byte[] getByteArrayFromInputStream(InputStream is) throws Exception {
        byte[] raw_data = null;
        Vector buffers = new Vector();
        byte[] b2k, lastBuf;
        int bufCount = -1, bytesRead, lastCount;

        try {
            do {

                b2k = new byte[2048];
                bytesRead = is.read(b2k);
                buffers.addElement(b2k);
                bufCount++;
            } while (2048 == bytesRead);

            if (bytesRead == -1) bytesRead = 0;

            lastBuf = b2k;
            int totalBufferSize = (bufCount * 2048) + bytesRead;

            if (totalBufferSize > 0) {
                raw_data = new byte[totalBufferSize];
                for (int i = 0; i < bufCount; i++) {
                    b2k = (byte[]) buffers.elementAt(i);
                    System.arraycopy(b2k, 0, raw_data, i * 2048, 2048);
                }
                System.arraycopy(lastBuf, 0, raw_data, bufCount * 2048, bytesRead);
            } else {
                raw_data = new byte[0];
            }


        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


        return raw_data;
    }

}
