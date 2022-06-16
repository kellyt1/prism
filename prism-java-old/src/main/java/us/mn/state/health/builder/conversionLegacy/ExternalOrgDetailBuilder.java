package us.mn.state.health.builder.conversionLegacy;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.model.common.ExtOrgDetailMailingAddress;
import us.mn.state.health.model.common.ExtOrgDetailPhone;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.model.common.MailingAddress;
import us.mn.state.health.model.common.Phone;
import us.mn.state.health.model.legacySystem.common.ExternalOrgASAP;

public class ExternalOrgDetailBuilder {
    private String externalOrgASAP;
    private ExternalOrgDetail externalOrgDetail;
    private String user;
    private HibernateDAO hibernateDAO;

    public ExternalOrgDetailBuilder(String externalOrgASAP,
                                    ExternalOrgDetail externalOrgDetail,
                                    String user,
                                    HibernateDAO hibernateDAO) {
        this.externalOrgASAP = externalOrgASAP.replaceAll("'","''").toUpperCase();
        this.externalOrgDetail = externalOrgDetail;
        this.user = user;
        this.hibernateDAO = hibernateDAO;
    }

    public void buildExternalOrgDetail(){
        externalOrgDetail.setOrgName(externalOrgASAP);
        externalOrgDetail.setInsertedBy(user);
        externalOrgDetail.setInsertionDate(new Date());
        externalOrgDetail.setOrgCode(StringUtils.generateCodeFromName(externalOrgASAP));
        externalOrgDetail.setOrgEffectiveDate(new Date());
    }

    public void buildMailingAddresses() throws InfrastructureException {
        Collection externalOrgs_ASAP;
        externalOrgs_ASAP = hibernateDAO
                .executeQuery("from ExternalOrgASAP org where upper(org.externalOrgASAPId.name) like \'"
                        + externalOrgASAP.toUpperCase() + "\'");
//        hibernateDAO.addCriterion(Expression.ilike("externalOrgASAPId.name",externalOrgASAP));
//        externalOrgs_ASAP = hibernateDAO.executeCriteriaQuery(us.mn.state.health.model.legacySystem.common.ExternalOrgASAP.class);

        //Now we are trying to get the distinct mailing addresses putting the results in a Set
        //  (look how the equals method is implemented in ExternalOrgASAP)
        Collection distinctExternalOrgs_ASAP = new HashSet(externalOrgs_ASAP);
        for (Iterator iterator = distinctExternalOrgs_ASAP.iterator(); iterator.hasNext();) {
            ExternalOrgASAP orgASAP = (ExternalOrgASAP) iterator.next();
            MailingAddress mailingAddress = new MailingAddress();
            mailingAddress.setAddress1(orgASAP.getAddress1());
            mailingAddress.setAddress2(orgASAP.getAddress2());
            mailingAddress.setState(orgASAP.getState());
            mailingAddress.setCity(orgASAP.getCity());
            mailingAddress.setZip(orgASAP.getZipCode().substring(0,5));
            mailingAddress.setInsertedBy(user);
            mailingAddress.setInsertionDate(new Date());
            ExtOrgDetailMailingAddress extOrgDetailMailingAddress
                    = ExtOrgDetailMailingAddress.create(externalOrgDetail, mailingAddress, user);
        }
        distinctExternalOrgs_ASAP.size();

    }

    public void buildTelephones() throws InfrastructureException, BusinessException {
        Collection externalOrgs_ASAP;
        externalOrgs_ASAP = hibernateDAO
                .executeQuery("select distinct org.telephone from ExternalOrgASAP org where lower(org.externalOrgASAPId.name) like \'"
                        + externalOrgASAP.toLowerCase() + "\'");
//        hibernateDAO.addCriterion(Expression.ilike("externalOrgASAPId.name",externalOrgASAP));
//                externalOrgs_ASAP = hibernateDAO.executeCriteriaQuery(us.mn.state.health.model.legacySystem.common.ExternalOrgASAP.class);

        for (Iterator iterator = externalOrgs_ASAP.iterator(); iterator.hasNext();) {
//            ExternalOrgASAP orgASAP = (ExternalOrgASAP) iterator.next();
//            String tele = orgASAP.getTelephone();
            String tele = (String) iterator.next();
            Phone phone = new Phone();
            Collection existingPhones = hibernateDAO.executeQuery("from Phone p where p.number like '" + tele +"'");
            if (existingPhones.size()>0){
                phone = (Phone) existingPhones.iterator().next();
            }
            else {
                phone.setInsertedBy(user);
                phone.setInsertionDate(new Date());
                phone.setNumber(tele);
                phone.setMdhOwned(Phone.DEFAULT_MDHOWNED);
                phone.setLocation(Phone.DEFAULT_LOCATION);
            }
            if(!StringUtils.nullOrBlank(tele)){
                ExtOrgDetailPhone.create(externalOrgDetail, phone, user, Phone.LAND_PHONE);
            }
        }

    }
}
