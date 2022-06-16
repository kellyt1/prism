package us.mn.state.health.view.purchasing;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class OrderNoteForm extends ValidatorForm {

    /** Attributes */
    private String orderNoteId;
    private Date insertionDate;
    private String authorName;
    private String note;
    private Boolean removed = Boolean.FALSE;

    /** Resets OrderNote form values */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

    }

    /** Validates OrderNote form values */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return super.validate(mapping, request);
    }
    
    
    /** Accessor Methods */
   


    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }


    public Date getInsertionDate() {
        return insertionDate;
    }


    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getAuthorName() {
        return authorName;
    }


    public void setNote(String note) {
        this.note = note;
    }


    public String getNote() {
        return note;
    }


    public void setRemoved(Boolean removed) {
        this.removed = removed;
    }


    public Boolean getRemoved() {
        return removed;
    }


    public void setOrderNoteId(String orderNoteId) {
        this.orderNoteId = orderNoteId;
    }


    public String getOrderNoteId() {
        return orderNoteId;
    }
    
    public String getKey() {
        return this.toString();
    }
}
