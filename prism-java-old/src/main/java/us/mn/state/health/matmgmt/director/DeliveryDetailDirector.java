package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.DeliveryDetailBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class DeliveryDetailDirector {
    private DeliveryDetailBuilder builder;
    
    public DeliveryDetailDirector(DeliveryDetailBuilder builder) {
        this.builder = builder;
    }
    
     public void construct() throws InfrastructureException {
        builder.buildSimpleProperties();
        builder.buildOrganization();
        builder.buildFacility();
        builder.buildRecipient();
        builder.buildMailingAddress();
     }  
     
     public void constructDefault() throws InfrastructureException {
         builder.buildDefault();
     }

    public void constructStock() throws InfrastructureException{
        builder.buildStock();
    }
}