package us.mn.state.health.builder.purchasing;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.purchasing.OrderNote;
import us.mn.state.health.view.purchasing.OrderNoteForm;

/**
 * Class that builds a OrderNote bean from a OrderNote form
 * @author Jason Stull, Shawn Flahave, Lucian Ochian
 */
public class OrderNoteBuilder {

    /** OrderNote to be built */
    private OrderNote orderNote;

    /** OrderNoteForm used to build OrderNote */
    private OrderNoteForm orderNoteForm;

    /**
     * OrderNoteBuilder Constructor
     * @param orderNote OrderNote to be built
     * @param orderNoteForm OrderNoteForm from which OrderNote is built
     */
    public OrderNoteBuilder(OrderNote orderNote, OrderNoteForm orderNoteForm) {
        this.orderNote = orderNote;
        this.orderNoteForm = orderNoteForm;
    }

    /** Build default OrderNote properties */
    public void buildDefaultProperties() {
    
    }

    /**
     * Build simple OrderNote properties from the OrderNoteForm
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(orderNote, orderNoteForm);
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}
