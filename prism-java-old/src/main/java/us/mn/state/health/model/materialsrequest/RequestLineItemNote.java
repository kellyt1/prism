package us.mn.state.health.model.materialsrequest;

import java.io.Serializable;
import java.util.Date;

public class RequestLineItemNote implements Serializable {
    private Long requestLineItemNoteId;
    private RequestLineItem requestLineItem;
    private String noteText;
    private String authorName;
    private Date insertionDate;

    public RequestLineItem getRequestLineItem() {
        return requestLineItem;
    }

    public void setRequestLineItem(RequestLineItem requestLineItem) {
        this.requestLineItem = requestLineItem;
    }

    public Long getRequestLineItemNoteId() {
        return requestLineItemNoteId;
    }

    public void setRequestLineItemNoteId(Long requestLineItemNoteId) {
        this.requestLineItemNoteId = requestLineItemNoteId;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }
}