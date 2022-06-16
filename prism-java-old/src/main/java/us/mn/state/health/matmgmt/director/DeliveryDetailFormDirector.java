package us.mn.state.health.matmgmt.director;

import us.mn.state.health.builder.materialsrequest.DeliveryDetailFormBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;

public class DeliveryDetailFormDirector {
    private DeliveryDetailFormBuilder builder;
    
    public DeliveryDetailFormDirector(DeliveryDetailFormBuilder builder) {
        this.builder = builder;
    }
    
     public void constructForExternalOrgOption() throws InfrastructureException {
        builder.buildOrganizations();
        builder.buildMailingAddresses();
     }  
     
     public void constructForMdhEmployeeOption() throws InfrastructureException {
         builder.buildRecipients();
         builder.buildFacilities();
     }
     
     public void constructForPrivateCitizenOption() throws InfrastructureException {
         builder.buildPrivateCitizenRecipients();
         builder.buildPrivateCitizenMailingAddresses();
     }
}