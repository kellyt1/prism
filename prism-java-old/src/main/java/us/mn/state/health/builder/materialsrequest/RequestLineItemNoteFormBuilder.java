package us.mn.state.health.builder.materialsrequest;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.common.util.DateUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.materialsrequest.RequestLineItemNote;
import us.mn.state.health.view.materialsrequest.RequestLineItemNoteForm;

public class RequestLineItemNoteFormBuilder  {
    private RequestLineItemNote rliNote;
    private RequestLineItemNoteForm rliNoteForm;
    private User author;
    
    public RequestLineItemNoteFormBuilder(RequestLineItemNoteForm rliNoteForm, 
                                          DAOFactory daoFactory) {
        this.rliNoteForm = rliNoteForm;
    }
    
     public RequestLineItemNoteFormBuilder(RequestLineItemNoteForm rliNoteForm, 
                                           RequestLineItemNote rliNote,
                                           DAOFactory daoFactory) {
        this(rliNoteForm, daoFactory);
        this.rliNote = rliNote;
    }
    
    public RequestLineItemNoteFormBuilder(RequestLineItemNoteForm rliNoteForm,
                                          User author) {
        this.rliNoteForm = rliNoteForm;
        this.author = author;
    }
   
    /**
     * This method will handle setting the note text and the insertion date.
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     */
     public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(rliNoteForm, rliNote);
        } 
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building RequestLineItemNoteForm: ", rpe);
        }
    }
    
    public void buildAuthorName() {
        rliNoteForm.setAuthorName(author.getFirstAndLastName());
    }
    
    public void buildDate() {
        String now = DateUtils.toString(new Date(), DateUtils.DEFAULT_DATE_FORMAT);
        rliNoteForm.setInsertionDate(now);
    }
}