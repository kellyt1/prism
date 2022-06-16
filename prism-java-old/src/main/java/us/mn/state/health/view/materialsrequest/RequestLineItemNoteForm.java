package us.mn.state.health.view.materialsrequest;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class RequestLineItemNoteForm extends ValidatorForm {    
    private Long requestLineItemNoteId;
    private String noteText;
    private String authorName; 
    private String insertionDate;
    private Boolean removed = Boolean.FALSE;
    private String key = "";
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {        
    }
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }


    public String getNoteText() {
        return noteText;
    }


    public void setRequestLineItemNoteId(Long requestLineItemNoteId) {
        this.requestLineItemNoteId = requestLineItemNoteId;
    }


    public Long getRequestLineItemNoteId() {
        return requestLineItemNoteId;
    }


    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getAuthorName() {
        return authorName;
    }


    public void setInsertionDate(String insertionDate) {
        this.insertionDate = insertionDate;
    }


    public String getInsertionDate() {
        return insertionDate;
    }


    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }


    public Boolean getRemoved() {
        return removed;
    }

    public String getKey() {
        return this.toString();
    }
}