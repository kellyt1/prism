package us.mn.state.health.model.purchasing;

import java.io.Serializable;
import java.util.Date;

public class OrderNote implements Serializable {
    private Long orderNoteId;
    private Date insertionDate;
    private String authorName;
    private String note;
    private Order order;

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrderNoteId(Long orderNoteId) {
        this.orderNoteId = orderNoteId;
    }

    public Long getOrderNoteId() {
        return orderNoteId;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
