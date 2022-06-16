package us.mn.state.health.builder.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.purchasing.OrderLineItem;
import us.mn.state.health.view.inventory.FixedAssetForm;

public class FixedAssetFormBuilder extends SensitiveAssetFormBuilder {
    private FixedAsset fixedAsset;
    private FixedAssetForm fixedAssetForm;
   
    public FixedAssetFormBuilder(OrderLineItem oli, 
                                 FixedAsset fixedAsset,
                                 FixedAssetForm fixedAssetForm,
                                 DAOFactory daoFactory) {
        super(oli, fixedAsset, fixedAssetForm, daoFactory);
        this.fixedAsset = fixedAsset;
        this.fixedAssetForm = fixedAssetForm;
    }
    
    public FixedAssetFormBuilder(FixedAsset fixedAsset,
                                 FixedAssetForm fixedAssetForm,
                                 DAOFactory daoFactory) {
        super(fixedAsset, fixedAssetForm, daoFactory);
        this.fixedAssetForm = fixedAssetForm;
        this.fixedAsset = fixedAsset;
    }
    
    public void buildFixedAsset() {
        fixedAssetForm.setFixedAsset(fixedAsset);
    }
    
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(fixedAssetForm, fixedAsset);
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException(rpe);
        }
    }
}