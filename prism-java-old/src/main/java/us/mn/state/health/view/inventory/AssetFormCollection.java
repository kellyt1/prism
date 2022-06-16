package us.mn.state.health.view.inventory;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class AssetFormCollection extends ValidatorForm {
    private Collection fixedAssetForms = new ArrayList();     //the type of collection has to be accessible via index (i.e., requests[0])
    private String fixedAssetFormIndex;
    private Collection sensitiveAssetForms = new ArrayList();
    private String sensitiveAssetFormIndex;
    private String cmd = "";
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }


    public void setFixedAssetForms(Collection fixedAssetForms) {
        this.fixedAssetForms = fixedAssetForms;
    }


    public Collection getFixedAssetForms() {
        return fixedAssetForms;
    }


    public void setFixedAssetFormIndex(String fixedAssetFormIndex) {
        this.fixedAssetFormIndex = fixedAssetFormIndex;
    }


    public String getFixedAssetFormIndex() {
        return fixedAssetFormIndex;
    }


    public void setSensitiveAssetForms(Collection sensitiveAssetForms) {
        this.sensitiveAssetForms = sensitiveAssetForms;
    }


    public Collection getSensitiveAssetForms() {
        return sensitiveAssetForms;
    }


    public void setSensitiveAssetFormIndex(String sensitiveAssetFormIndex) {
        this.sensitiveAssetFormIndex = sensitiveAssetFormIndex;
    }


    public String getSensitiveAssetFormIndex() {
        return sensitiveAssetFormIndex;
    }


    public void setCmd(String cmd) {
        this.cmd = cmd;
    }


    public String getCmd() {
        return cmd;
    }

   
}