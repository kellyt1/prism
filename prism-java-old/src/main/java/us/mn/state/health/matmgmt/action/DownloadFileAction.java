package us.mn.state.health.matmgmt.action;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.model.materialsrequest.AttachedFileNonCat;
import us.mn.state.health.dao.HibernateDAO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletOutputStream;
import java.sql.Blob;
import java.io.InputStream;

public final class DownloadFileAction extends Action {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        Long attachedFileNonCatId = new Long( request.getParameter("attachedFileNonCatId"));

        HibernateDAO hdao = new HibernateDAO();
        AttachedFileNonCat attachedFile  = (AttachedFileNonCat) hdao.getById(attachedFileNonCatId, AttachedFileNonCat.class);

        String fileName = attachedFile.getFileName();
        String fileType = fileName.substring(fileName.indexOf(".") + 1, fileName.length());

        if (fileType.trim().equalsIgnoreCase("txt")) {
            response.setContentType("text/plain");
        } else if (fileType.trim().equalsIgnoreCase("doc")) {
            response.setContentType("application/msword");
        } else if (fileType.trim().equalsIgnoreCase("xls")) {
            response.setContentType("application/vnd.ms-excel");
        } else if (fileType.trim().equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
        } else if (fileType.trim().equalsIgnoreCase("ppt")) {
            response.setContentType("application/ppt");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "max-age=600");

        ServletOutputStream outStream = response.getOutputStream();
        outStream.write(attachedFile.getFileContents());
        outStream.flush();
        outStream.close();

        return (null);
    }

}
