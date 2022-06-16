package us.mn.state.health.builder.conversionLegacy.fixedAssets;

import java.util.Date;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.Manufacturer;

/**
 * This builder is for building the manufacturer using the manufacturer's name
 * obtained from the MANUF colmun of the table FA_DBF
 */
public class ManufacturerBuilder {
    private String manufacturerName;
    private Manufacturer manufacturer;
    private String user;

    public ManufacturerBuilder(String manufacturerName, Manufacturer manufacturer, String user) {
        this.manufacturerName = manufacturerName;
        this.manufacturer = manufacturer;
        this.user = user;
    }

    public void buildExternalOrgDetail() throws BusinessException {
        ExternalOrgDetail externalOrgDetail = new ExternalOrgDetail();
        externalOrgDetail.setOrgName(manufacturerName);
        externalOrgDetail.setOrgCode(StringUtils.generateCodeFromName(manufacturerName));
        externalOrgDetail.setInsertionDate(new Date());
        externalOrgDetail.setInsertedBy(user);
        manufacturer.setExternalOrgDetail(externalOrgDetail);
    }

    public void buildSimpleProperties(){
        manufacturer.setInsertedBy(user);
        manufacturer.setInsertionDate(new Date());
        manufacturer.setManufacturerCode(StringUtils.generateCodeFromName(manufacturerName));
    }
}
