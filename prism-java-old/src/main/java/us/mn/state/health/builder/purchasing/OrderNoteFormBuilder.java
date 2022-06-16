package us.mn.state.health.builder.purchasing;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.purchasing.OrderNote;
import us.mn.state.health.view.purchasing.OrderNoteForm;

/**
 * Class that builds a OrderNoteForm from a OrderNote
 * @author Jason Stull, Shawn Flahave, Lucian Ochian
 */
public class OrderNoteFormBuilder {

    /** OrderNoteForm to be built */
    private OrderNoteForm orderNoteForm;

    /** OrderNote used to build OrderNoteForm */
    private OrderNote orderNote;
    
    /** Person who authored this note */
    private String authorName;
    
    /**
     * OrderNoteFormBuilder Constructor
     * @param orderNoteForm the OrderNoteForm to be built
     * @param authorName person who authored this note
     */
    public OrderNoteFormBuilder(OrderNoteForm orderNoteForm, String authorName) {
        this.orderNoteForm = orderNoteForm;
        this.authorName = authorName;
    }

    /**
     * OrderNoteFormBuilder Constructor
     * @param orderNoteForm the OrderNoteForm to be built
     * @param orderNote the OrderNote used to build OrderNoteForm
     */
    public OrderNoteFormBuilder(OrderNoteForm orderNoteForm, OrderNote orderNote) {
        this.orderNoteForm = orderNoteForm;
        this.orderNote = orderNote;
    }

    /** Build default OrderNoteForm properties */
    public void buildDefaultProperties() {
        orderNoteForm.setAuthorName(authorName);
        orderNoteForm.setInsertionDate(new Date());
        orderNoteForm.setRemoved(Boolean.FALSE);
    }

    /**
     * Build simple orderNoteForm properties OrderNoteForm the OrderNote
     * @throws us.mn.state.health.common.exceptions.InfrastrucureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(orderNoteForm, orderNote);
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
