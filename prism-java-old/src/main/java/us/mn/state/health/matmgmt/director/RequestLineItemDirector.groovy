package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.RequestLineItemBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class RequestLineItemDirector  {
    RequestLineItemBuilder builder;
    
    public RequestLineItemDirector(RequestLineItemBuilder builder) {
        this.builder = builder;
    }
    
    public void createNewLineItem(String skin) throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildFundingSources();
        builder.buildStatus(skin);
        builder.buildItem();
        builder.buildUnit();
        builder.buildMetaProperties();
        builder.buildItemCategory();
        builder.buildPurchasingInfoFiles();
        builder.buildPurchasingInfoFilesAlternate();
        //todo fix up this exception handling t.r.

        // ?? uncomment after debugging
        if (skin?.equalsIgnoreCase("PRISM2")) {
            try {
                builder.addNote();
                builder.buildNotes();
            }
            catch (Exception e) {
                throw new InfrastructureException(e);
            }
        }
        builder.buildItPurchase();

    }

    public void constructEvaluatedLineItem(Boolean isApproved, String skin) throws Exception {
        builder.buildFundingSources();              
        builder.buildNotes();                           
        builder.buildRequestEvaluations(isApproved);
        builder.buildStatus(skin);
        builder.buildMetaProperties();
        builder.buildDenialReason();
    }
    
    public void constructEvaluatedLineItem(Integer isApproved, String skin,String inDesiredStatus) throws Exception {
        builder.buildFundingSources();
        builder.buildNotes();
        builder.buildRequestEvaluations(isApproved, inDesiredStatus);
        builder.buildStatus(skin, inDesiredStatus);
        builder.buildMetaProperties();
        builder.buildDenialReason();
        //builder.buildItPurchase();
        builder.buildItPurchase(inDesiredStatus);
    }

    public void constructEditPurchasingLineItem() throws Exception {
        builder.buildNotes();
        builder.buildItemCategory();
        builder.buildMetaProperties();
        builder.buildStatusFromForm();
    }
    public void constructEditLineItem() throws InfrastructureException {
        builder.buildSimpleProperties();
        //builder.buildItem();
        builder.buildItemCategory();
        builder.buildAttachFile();
        builder.buildMetaProperties();
    }

//  Not used in this version
//   used to attach multiple files to a request line item
//
    public void attachFile() throws InfrastructureException {
        builder.buildAttachFile();
    }
}