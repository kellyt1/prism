package us.mn.state.health.test.legacySystem.convertFixedAssets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;
import us.mn.state.health.builder.conversionLegacy.fixedAssets.FixedAssetBuilder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.dao.HibernateDAO;
import us.mn.state.health.dao.HibernateDAOFactory;
import us.mn.state.health.dao.inventory.FixedAssetDAO;
import us.mn.state.health.matmgmt.director.conversionLegacy.fixedassets.FixedAssetDirector;
import us.mn.state.health.matmgmt.util.Constants;
import us.mn.state.health.model.inventory.FixedAsset;
import us.mn.state.health.model.legacySystem.fixedAssets.FixedAssetOracle;
import us.mn.state.health.persistence.HibernateUtil;

/**
 * Note: if we run the script for the second time we have to
 */

public class ConvertFixedAsset extends TestCase {
    private DAOFactory daoFactory;
    private String user;
    private String environment;


    protected void setUp() {
        //here is where we select the environment
        environment = Constants.DEVDB;
//        environment = Constants.TEST;
//        environment = Constants.PROD;
        System.setProperty(Constants.ENV_KEY, environment);

        daoFactory = new HibernateDAOFactory();
        user = "ochial1";
    }

    /**
     * This method does the conversion of the old fixed assets from the legacy table eqipfle_dbf
     * to the new tables SENSITIVE_ASSET_TBL and FIXED_ASSET_TBL
     * @throws InfrastructureException
     */
    public void convertFixedAssets() throws InfrastructureException {
        Collection legacyFixedAssets = getLegacyFixedAssets();
        FixedAssetBuilder builder;
        FixedAssetDirector director;
        FixedAsset fixedAsset;
        int i = 0;
        for (Iterator iterator = legacyFixedAssets.iterator(); iterator.hasNext();) {
            i++;
            FixedAssetOracle fixedAssetOracle = (FixedAssetOracle) iterator.next();
            fixedAsset = new FixedAsset();
            builder = new FixedAssetBuilder(fixedAssetOracle, fixedAsset, daoFactory, user);
            director = new FixedAssetDirector(builder);
            director.construct();
            new HibernateDAO().makePersistent(fixedAsset);
            System.out.println("i="+i);
            if(i%25==0){
                new HibernateDAOFactory().commitTransaction(true);
            }
        }

    }

    public void addDateReceivedToConvertedFixedAssets() throws InfrastructureException{
        Collection results = new ArrayList();
        String hql = "from FixedAssetOracle fa where fa.dateReceived is not null";
        results = new HibernateDAO().executeQuery(hql);
        for (Iterator iterator = results.iterator(); iterator.hasNext();) {
            FixedAssetOracle fixedAssetOracle = (FixedAssetOracle) iterator.next();
            FixedAssetDAO fixedAssetDAO = daoFactory.getFixedAssetDAO();
            FixedAsset fixedAsset = fixedAssetDAO.findFixedAssetByAssetNumber(fixedAssetOracle.getAssetNumber().trim());
            if(fixedAsset!=null){
                fixedAsset.setDateReceived(fixedAssetOracle.getDateReceived());
                fixedAsset.setChangeDate(new Date());
                fixedAsset.setChangedBy(user);
                fixedAssetDAO.makePersistent(fixedAsset);
            }
        }
    }

    private Collection getLegacyFixedAssets() throws InfrastructureException {
        Collection results = new ArrayList();
//        String hql = "from FixedAssetOracle fa where fa.cost>=5000";
        String hql = "from FixedAssetOracle fa";
        results = new HibernateDAO().executeQuery(hql);
        return results;
    }

    protected void tearDown() throws InfrastructureException {
        HibernateUtil.commitTransaction();
        HibernateUtil.closeSession();
    }
}