package us.mn.state.health.builder.inventory;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.User;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.view.inventory.FixedAssetForm;

public class FixedAssetBuilder extends SensitiveAssetBuilder {
   private FixedAsset fixedAsset;
   private FixedAssetForm fixedAssetForm;
   public FixedAssetBuilder(FixedAsset fixedAsset,
                            FixedAssetForm fixedAssetForm,
                            DAOFactory daoFactory,
                            User user) {
        super(fixedAsset, fixedAssetForm, daoFactory, user);
        this.fixedAsset = fixedAsset;
        this.fixedAssetForm = fixedAssetForm;        
   }
   
    public void buildSimpleProperties() throws InfrastructureException {
        try {
            if(fixedAssetForm != null) {
                PropertyUtils.copyProperties(fixedAsset, fixedAssetForm);
            }            
        }
        catch (ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building FixedAsset: ", rpe);
        }
    }
   
}