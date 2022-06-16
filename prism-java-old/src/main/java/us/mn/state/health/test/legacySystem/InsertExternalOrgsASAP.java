package us.mn.state.health.test.legacySystem;

import java.util.Collection;
import java.util.Iterator;

import junit.framework.TestCase;
import org.hibernate.Session;
import us.mn.state.health.builder.conversionLegacy.ExternalOrgDetailBuilder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.common.ExternalOrgDetail;
import us.mn.state.health.persistence.HibernateUtil;

public class InsertExternalOrgsASAP extends TestCase {
    private String user;
    private HibernateDAO hibernateDAO;
    private String environment;
    private String insertionDate ; //date used for deletion criteria

    protected void setUp() {
        //here is where we select the environment
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        user = "ochial1";
        hibernateDAO = new HibernateDAO();

        insertionDate = "17-Jan-2006";

    }

    /**
     * Method that converts the legacy External Orgs from the ASAP system to the new system
     * The legacy orgs are in the "ASAP_ORG_TBL"
     *
     * @throws InfrastructureException
     * @throws BusinessException
     */

    public void loadExternalOrgsASAP() throws InfrastructureException, BusinessException {
        Collection distinctExternalOrgASAPNames =
                hibernateDAO.executeQuery("select distinct upper(org.externalOrgASAPId.name) from ExternalOrgASAP org ");
        distinctExternalOrgASAPNames.size();
        int i = 1;
        for (Iterator iterator = distinctExternalOrgASAPNames.iterator(); iterator.hasNext();) {
            String externalOrgASAPName = (String) iterator.next();
            System.out.println("i=" + (i++) + " " + externalOrgASAPName);
            ExternalOrgDetail externalOrgDetail = new ExternalOrgDetail();
            ExternalOrgDetailBuilder builder =
                    new ExternalOrgDetailBuilder(externalOrgASAPName, externalOrgDetail, user, hibernateDAO);
            builder.buildExternalOrgDetail();
            hibernateDAO.makePersistent(externalOrgDetail);
            builder.buildMailingAddresses();
            hibernateDAO.makePersistent(externalOrgDetail);
            builder.buildTelephones();
            hibernateDAO.makePersistent(externalOrgDetail);
            externalOrgDetail.toString();
            if(i%25==0){
                new HibernateDAOFactory().commitTransaction(true);
            }
        }
    }

    /**
     * Deletes the external orgs inserted by a certain user at a certain date
     * @throws InfrastructureException
     */
    public void deleteExternalOrgsASAP() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        HibernateUtil.beginTransaction();

        /**
         * To run the following queries we have to set this property in the hibernate config file
         * <property name="hibernate.query.factory_class">org.hibernate.hql.ast.ASTQueryTranslatorFactory</property>
         */

        String hqlDelete = "delete ExternalOrgDetail where insertionDate = :insertionDate and insertedBy like :user";
        int deletedEntities = session.createQuery(hqlDelete)
                .setString("insertionDate", insertionDate)
                .setString("user", user)
                .executeUpdate();
        System.out.println(deletedEntities);
        hqlDelete = "delete MailingAddress where insertionDate = :insertionDate and insertedBy like :user";
        deletedEntities = session.createQuery(hqlDelete)
                .setString("insertionDate", insertionDate)
                .setString("user", user)
                .executeUpdate();
        System.out.println(deletedEntities);
        hqlDelete = "delete Phone where insertionDate = :insertionDate and insertedBy like :user";
        deletedEntities = session.createQuery(hqlDelete)
                .setString("insertionDate", insertionDate)
                .setString("user", user)
                .executeUpdate();
        System.out.println(deletedEntities);
    }

    protected void tearDown() throws InfrastructureException {
//        HibernateUtil.commitTransaction();
//        HibernateUtil.closeSession();
    }
}