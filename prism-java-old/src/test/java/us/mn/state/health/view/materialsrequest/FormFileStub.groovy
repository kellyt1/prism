package us.mn.state.health.view.materialsrequest

import org.apache.struts.upload.FormFile

/**
 * Created by demita1 on 5/17/2016.
 */
class FormFileStub implements FormFile {
    @Override
    String getContentType() {
        return null
    }

    @Override
    void setContentType(String contentType) {

    }

    @Override
    int getFileSize() {
        return 0
    }

    @Override
    void setFileSize(int fileSize) {

    }

    @Override
    String getFileName() {
        return null
    }

    @Override
    void setFileName(String fileName) {

    }

    @Override
    byte[] getFileData() throws FileNotFoundException, IOException {
        return new byte[0]
    }

    @Override
    InputStream getInputStream() throws FileNotFoundException, IOException {
        return null
    }

    @Override
    void destroy() {

    }
}
