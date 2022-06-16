package us.mn.state.health.builder.conversionLegacy.fixedAssets;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.CategoryDAO;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.inventory.UnitDAO;
import us.mn.state.health.model.common.Category;
import us.mn.state.health.model.common.Manufacturer;
import us.mn.state.health.model.common.Vendor;
import us.mn.state.health.model.inventory.ClassCode;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.inventory.Item;
import us.mn.state.health.model.inventory.PurchaseItem;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.model.legacySystem.fixedAssets.FixedAssetOracle;

/**
 * This
 */
public class FixedAssetBuilder {
    private static Log log = LogFactory.getLog(FixedAssetBuilder.class);
    private FixedAssetOracle fixedAssetOracle;
    private FixedAsset fixedAsset;
    private DAOFactory daoFactory;
    private String user;

    public FixedAssetBuilder(FixedAssetOracle fixedAssetOracle,
                             FixedAsset fixedAsset,
                             DAOFactory daoFactory,
                             String user) {
        this.fixedAssetOracle = fixedAssetOracle;
        this.fixedAsset = fixedAsset;
        this.daoFactory = daoFactory;
        this.user = user;
    }

    public void buildSimpleProperties() {

        String serialNumberTemp = fixedAssetOracle.getSerialNumber();
        String serialNumber = StringUtils.nullOrBlank(serialNumberTemp) ? "UNKNOWN" : serialNumberTemp;
        fixedAsset.setSerialNumber(serialNumber);
        fixedAsset.setCost(fixedAssetOracle.getCost());
        fixedAsset.setDateReceived(fixedAssetOracle.getDateReceived());
        fixedAsset.setDocument(fixedAssetOracle.getDocument());
        fixedAsset.setMaintAgreementPONumber(fixedAssetOracle.getMaintainancePONumber());
        fixedAsset.setInsertedBy(user);
        fixedAsset.setInsertionDate(new Date());
        fixedAsset.setFixedAssetNumber(fixedAssetOracle.getAssetNumber().toString());
    }

    public void buildPurchaseItem() throws InfrastructureException {
        // see if there is an purchase item with that description and model
        //if the item doesn't exists, add it to the DB, save it and then associate it with the fixed asset
        //else associated with the fixed asset
        //put a category for the item using type
        //associate the item with the vendor
        FixedAssetOracle exampleFixedAssetOracle = new FixedAssetOracle();
        Integer type = fixedAssetOracle.getType();
        String manufacturer = fixedAssetOracle.getManufacturer();
        String description = fixedAssetOracle.getDescription();
        String model = fixedAssetOracle.getModel();
        exampleFixedAssetOracle.setType(type);
        exampleFixedAssetOracle.setManufacturer(manufacturer);
        exampleFixedAssetOracle.setDescription(description);
        exampleFixedAssetOracle.setModel(model);

        Collection faos = new HibernateDAO().findByExample(FixedAssetOracle.class, exampleFixedAssetOracle);
        if (faos.size() > 1) {
            log.error("faos.size > 1");
        }
        Item item = null;
        for (Iterator iterator = faos.iterator(); iterator.hasNext();) {
            FixedAssetOracle faOracle = (FixedAssetOracle) iterator.next();
            String assetNumber = faOracle.getAssetNumber();
            FixedAsset existingAsset = daoFactory.getFixedAssetDAO().findFixedAssetByAssetNumber(assetNumber);
            if (existingAsset != null) {
                item = existingAsset.getItem();
                break;
            }
        }
        if (item == null) {
            item = new PurchaseItem();
            Manufacturer manuf = daoFactory.getManufacturerDAO().findManufacturerByCode(Manufacturer.CODE_UNKNOWN);
            item.setManufacturer(manuf);

            UnitDAO unitDAO = daoFactory.getUnitDAO();
            Unit unit = unitDAO.findUnitByCode(Unit.CODE_UNKNOWN);
            item.setDispenseUnit(unit);

            item.setDescription(description);
            item.setModel(model);
            item.setInsertedBy(user);
            item.setInsertionDate(new Date());
            Category category = null;
            CategoryDAO categoryDAO = daoFactory.getCategoryDAO();
            switch (type.intValue()) {
                case 1:
                    category = categoryDAO.findByCategoryCode(Category.MATERIALS_COMPUTER_EQUIPMENT);
                    break;
                case 3:
                    category = categoryDAO.findByCategoryCode(Category.MATERIALS_COMPUTER_EQUIPMENT);
                    break;
                default:
                    category = categoryDAO.findByCategoryCode(Category.MATERIALS_MISCELLANEOUS);
                    break;
            }
            item.setCategory(category);
            new HibernateDAO().makePersistent(item);
//            daoFactory.commitTransaction(true);
        }

        fixedAsset.setItem(item);

    }

    public void buildClassCode() throws InfrastructureException {
        String classCode = fixedAssetOracle.getClassCode();
        if (StringUtils.nullOrBlank(classCode)) {
            return;
        }
        classCode = classCode.toUpperCase();
        ClassCode cc = daoFactory.getClassCodeDAO().getClassCodeByCode(classCode);
        if (cc == null) {
            cc = new ClassCode();
            cc.setClassCodeValue(classCode);
            new HibernateDAO().makePersistent(cc);
            daoFactory.commitTransaction(true);
        }
        fixedAsset.setClassCode(cc);
    }

    public void buildVendor() throws InfrastructureException {
        String vendorId = fixedAssetOracle.getVidnbr();
        if (StringUtils.nullOrBlank(vendorId)) {
            return;
        }
        Vendor vendor = daoFactory.getVendorDAO().getVendorByLegacyId(vendorId);
        if (vendor == null) {
            return;
        }
        fixedAsset.setVendor(vendor);
    }

    public void buildNotes() {
        StringBuffer notes = new StringBuffer();

        String building = fixedAssetOracle.getBuilding();
        if (building != null) {
            notes.append("Building:").append(building).append("; ");
        }
        String room = fixedAssetOracle.getRoom();
        if (room != null) {
            notes.append("Room:").append(room).append("; ");
        }
        String workstation = fixedAssetOracle.getWorkstation();
        if (workstation != null) {
            notes.append("Workstation:").append(workstation).append("; ");
        }
        String bnbr = fixedAssetOracle.getBnbr();
        if (bnbr != null) {
            notes.append("BNBR:").append(bnbr).append("; ");
        }
        String org = fixedAssetOracle.getOrg();
        if (org != null) {
            notes.append("ORG:").append(org).append("; ");
        }
        String manufacturer = fixedAssetOracle.getManufacturer();
        if (manufacturer != null) {
            notes.append("MANUFACTURER:").append(manufacturer).append("; ");
        }
        fixedAsset.setNotes(notes.toString());
    }

}
