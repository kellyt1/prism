package us.mn.state.health.view.inventory;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.inventory.FixedAsset;

public class FixedAssetForm extends SensitiveAssetForm {
    private String fixedAssetNumber;
    private FixedAsset fixedAsset;

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        if (this.isReset()) {
            fixedAssetNumber = "";
        }
        super.reset(mapping, request);
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }

    public void setFixedAssetNumber(String fixedAssetNumber) {
        this.fixedAssetNumber = StringUtils.trim(fixedAssetNumber);
    }


    public String getFixedAssetNumber() {
        return fixedAssetNumber;
    }

    /**
     * another getter for fixedAssetNumber needed by validation 
     * @return fixedAssetNumber
     */
    public String getFixedAssetNumber1() {
        return fixedAssetNumber;
    }


    public void setFixedAsset(FixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }


    public FixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public boolean getFixedAssetNumberAlreadyAssigned() {
        if(StringUtils.nullOrBlank(fixedAssetNumber)){
            return false;
        }
        try {
            FixedAsset fixedAsset = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getFixedAssetDAO().findFixedAssetByAssetNumber(StringUtils.trim(fixedAssetNumber));
            Long currentId = StringUtils.nullOrBlank(getSensitiveAssetId())?null:Long.decode(getSensitiveAssetId());
            if (fixedAsset != null && (!fixedAsset.getSensitiveAssetId().equals(currentId))) {
               return true;
            }
        } catch (InfrastructureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean getFixedAssetNumberIsNull(){
        return StringUtils.nullOrBlank(fixedAssetNumber);
    }

}