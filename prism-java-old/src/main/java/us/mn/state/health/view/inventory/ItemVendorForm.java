package us.mn.state.health.view.inventory;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.model.inventory.ItemVendor;
import us.mn.state.health.model.common.User;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.UserDAO;

public class ItemVendorForm extends ValidatorForm {
    private ItemVendor.ItemVendorId itemVendorId;
    private Long buyUnitId;
    private String buyUnitCost;
    private String vendorCatalogNbr;
    private Boolean primaryVendor = Boolean.FALSE;
    private String vendorName;
    private String discount; //discount is in percents 0-100
    private String lastUpdatedBy;
    private String lastUpdatedDate;
    public static Log log = LogFactory.getLog(ItemVendorForm.class);
    private String vendorId;

    public String getVendorId() {
        if (StringUtils.nullOrBlank(vendorId)||vendorId.trim().equals("0")) {
            return null;
        }
        else {
            return vendorId;
        }
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public Long getBuyUnitId() {
        return buyUnitId;
    }

    public void setBuyUnitId(Long buyUnitId) {
        this.buyUnitId = buyUnitId;
    }

    public String getBuyUnitCost() {
        return buyUnitCost;
    }

    public void setBuyUnitCost(String buyUnitCost) {
        if (StringUtils.nullOrBlank(buyUnitCost)) {
            return;
        }
        try {
            double cost = Double.parseDouble(buyUnitCost);
            if (cost < 0) return;
        } catch (NumberFormatException e) {
            return;
        }
        this.buyUnitCost = buyUnitCost;
    }

    public String getVendorCatalogNbr() {
        return vendorCatalogNbr;
    }

    public void setVendorCatalogNbr(String vendorCatalogNbr) {
        this.vendorCatalogNbr = vendorCatalogNbr;
    }

    public Boolean getPrimaryVendor() {
        return primaryVendor;
    }

    public void setPrimaryVendor(Boolean primaryVendor) {
        this.primaryVendor = primaryVendor;
    }

    public ItemVendor.ItemVendorId getItemVendorId() {
        return itemVendorId;
    }

    public void setItemVendorId(ItemVendor.ItemVendorId itemVendorId) {
        this.itemVendorId = itemVendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        if (StringUtils.nullOrBlank(discount)) {
            this.discount = "0.0";
            return;
        }
        try {
            double disc = Double.parseDouble(discount);
            if (disc < 0) return;
        } catch (NumberFormatException e) {
            return;
        }
        this.discount = discount;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getKey() {
        return "" + itemVendorId.hashCode();
    }

    public String getLastUpdatedByUser(){
       try {
            UserDAO userDAO = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getUserDAO();
            User userByUsername = userDAO.findUserByUsername(lastUpdatedBy);
            if (userByUsername!=null) {
                return userByUsername.getFirstAndLastName();
            } else {
                return "N/A";
            }
        } catch (InfrastructureException e) {
            log.error(e);
        }
        return "N/A";
    }
}