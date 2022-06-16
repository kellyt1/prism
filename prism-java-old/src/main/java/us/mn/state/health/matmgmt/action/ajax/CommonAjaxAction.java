package us.mn.state.health.matmgmt.action.ajax;

import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.actions.MappingDispatchAction;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ajaxtags.helpers.AjaxXmlBuilder;
import us.mn.state.health.dao.VendorDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class CommonAjaxAction extends MappingDispatchAction {

    private static Log log = LogFactory.getLog(CommonAjaxAction.class);

    public ActionForward getVendorPhooneList(ActionMapping mapping,
                                             ActionForm actionForm,
                                             HttpServletRequest request,
                                             HttpServletResponse response) throws Exception {
        String vendorId = request.getParameter("vendorId");
        VendorDAO vendorDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getVendorDAO();
        Vendor vendor = vendorDAO.getVendorById(new Long(vendorId), false);
        List phones = new ArrayList();
        Phone primaryPhone = vendor.getExternalOrgDetail().getPrimaryPhone();
        if (primaryPhone != null) {
            phones.add(primaryPhone);
        } else {
            Phone phone = new Phone();
            phone.setPhoneId(null);
            phone.setNumber("N/A");
            phones.add(phone);
        }
        String xml = null;

        try {
            xml = new AjaxXmlBuilder().addItems(phones, "number", "phoneId").toString();
        } catch (Exception ex) {
            // Send back a 500 error code.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not create response");
            return null;
        }
        processXml(xml, mapping, actionForm, request, response);
        return null;
        // Set content to xml
    }

    public ActionForward getVendorsNameLike(ActionMapping mapping,
                                       ActionForm actionForm,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        VendorDAO vendorDAO = DAOFactory.getDAOFactory(DAOFactory.HIBERNATE).getVendorDAO();
        String vendorLikeName = request.getParameter("potentialStockItemForm.itemVendorForm.vendorName");
        List vendors = (List) vendorDAO.findVendorByNameLike(vendorLikeName);
        String xml = null;

        try {
            xml = new AjaxXmlBuilder().addItems(vendors, "externalOrgDetail.orgName", "vendorId").toString();
        } catch (Exception ex) {
            // Send back a 500 error code.
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Can not create response");
            return null;
        }
        processXml(xml, mapping, actionForm, request, response);
        return null;
    }

    public void processXml(String xml, ActionMapping mapping,
                           ActionForm actionForm,
                           HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        response.setContentType("text/xml; charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        PrintWriter pw = response.getWriter();
        pw.write(xml);
        pw.close();
    }

}
