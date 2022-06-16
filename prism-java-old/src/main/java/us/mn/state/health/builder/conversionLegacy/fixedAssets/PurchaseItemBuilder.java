package us.mn.state.health.builder.conversionLegacy.fixedAssets;

import java.util.Date;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.legacySystem.fixedAssets.FixedAssetDbf;
import us.mn.state.health.model.legacySystem.fixedAssets.FixedAssetOracle;

/**
 * This builder builds the purchase item coresponding to the fixed assets
 * using the legacy table for the fixed assets
 */

public class PurchaseItemBuilder {
    private FixedAssetOracle fixedAssetOracle;
    private FixedAssetDbf fixedAssetDbf;
    private PurchaseItem purchaseItem;
    private String user;
    private DAOFactory daoFactory;

    public PurchaseItemBuilder(FixedAssetOracle fixedAssetOracle, PurchaseItem purchaseItem, String user, DAOFactory daoFactory) {
        this.fixedAssetOracle = fixedAssetOracle;
        this.purchaseItem = purchaseItem;
        this.user = user;
        this.daoFactory = daoFactory;
    }

    public PurchaseItemBuilder(FixedAssetDbf fixedAssetDbf, PurchaseItem purchaseItem, String user, DAOFactory daoFactory) {
        this.fixedAssetDbf = fixedAssetDbf;
        this.purchaseItem = purchaseItem;
        this.user = user;
        this.daoFactory = daoFactory;
    }

    public void buildSimpleProperties() {
        if (fixedAssetDbf != null) {
            purchaseItem.setDescription(fixedAssetDbf.getDescription());
            purchaseItem.setModel(fixedAssetDbf.getModel());
        }
        else {
            purchaseItem.setDescription(fixedAssetOracle.getDescription());
            purchaseItem.setModel(fixedAssetOracle.getModel());
        }
        purchaseItem.setInsertedBy(user);
        purchaseItem.setInsertionDate(new Date());
    }

    public void buildCategory() {

    }

    public void buildManufacturer() throws InfrastructureException {
        String manufacturerString =
                (fixedAssetDbf != null) ?
                fixedAssetDbf.getManufacturer() : fixedAssetOracle.getManufacturer();
        Manufacturer manufacturer = daoFactory
                .getManufacturerDAO()
                .findManufacturerByCode(StringUtils.generateCodeFromName(manufacturerString));
        purchaseItem.setManufacturer(manufacturer);

    }
}


