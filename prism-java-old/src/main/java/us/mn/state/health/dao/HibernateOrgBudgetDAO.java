package us.mn.state.health.dao;

import org.hibernate.*;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.model.common.User;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;
import java.util.List;

public class HibernateOrgBudgetDAO implements OrgBudgetDAO {

    public HibernateOrgBudgetDAO() {
        try {
            HibernateUtil.beginTransaction();
        } 
        catch(InfrastructureException e) {
            e.printStackTrace();
        }
    }
    
    public OrgBudget getOrgBudgetById(Long orgBudgetId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        OrgBudget orgBudget = null;
        try {
            if(lock) {
                orgBudget = (OrgBudget)session.load(OrgBudget.class, orgBudgetId, LockMode.UPGRADE);
            } 
            else {
                orgBudget = (OrgBudget)session.load(OrgBudget.class, orgBudgetId);
            }
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return orgBudget;
    }

    public Collection findAll() throws InfrastructureException {
        Collection orgBudgets;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(OrgBudget.class);
            criteria.addOrder(Order.asc("orgBudgetCode"));
            criteria.setCacheable(true);
            orgBudgets = criteria.list();

            return orgBudgets;
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
    
    public Collection findAllByBudgetManager(User budgetManager) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection orgBudgets;
        try {
            orgBudgets = session.getNamedQuery("findAllByBudgetManager")
                                .setEntity("budgetManager", budgetManager).setCacheable(true)
                                .list();
            return orgBudgets;
        } 
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Collection findRptCategoriesByOrg(String orgBdgtCode) throws InfrastructureException {
            Session session = HibernateUtil.getSession();
            Collection rptCategories;
            try {
                rptCategories = session.getNamedQuery("findRptCategoriesByOrg")
                                    .setString("orgBdgtCode", orgBdgtCode).setCacheable(true)
                                    .list();
                return rptCategories;
            }
            catch(HibernateException ex) {
                throw new InfrastructureException(ex);
            }
        }


    public Collection findRptCategoriesByAppropriationCode(String appropriationCode,String orgCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection rptCategories;
        try {
            rptCategories = session.getNamedQuery("findRptCategoriesByAppropriationCode")
                                .setString("appropriationCode", appropriationCode).setCacheable(true)
                                .setString("orgCode", orgCode).setCacheable(true)
                                .list();
            return rptCategories;
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
}


    public OrgBudget findByOrgBudgetCode(String orgBdgtCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        OrgBudget orgBudget = null;
        try {
            orgBudget = (OrgBudget)session.getNamedQuery("findOrgBudgetByOrgBudgetCode")
                                          .setString("orgBdgtCode", orgBdgtCode).setCacheable(true)
                                          .uniqueResult();
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return orgBudget;
    }


    public OrgBudget findByOrgBudgetCodeAndLastFiscalYear(String orgBudgetCode) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        OrgBudget example = new OrgBudget();
        example.setOrgBudgetCode(orgBudgetCode);
        Collection results;
        results =  session.createCriteria(OrgBudget.class)
                          .add(Example.create(example))
                          .addOrder(Order.desc("effectiveDate"))
                          .setMaxResults(1)
                          .list();
        if(results == null || results.size() == 0){
            return null;
        }
        return (OrgBudget)results.iterator().next();
    }

    /**
     * Some org budgets should not be used for purchases - some are supposed to be 
     * used only for paying salaries for example.  So, we will 
     * use this method to filter out those non-purchasing org budgets.  An org budget 
     * that can be used for purchases will have a NULL value for the 'provisions' property.
     * This query also filters out all but current FY org budgets, so modify it 
     * when you need to include previous FY org budgets (usually only needed in June).
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     * @return 
     */
    public Collection findAllPurchaseOrgBudgets() throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Collection orgBudgets;
        try {
            orgBudgets = session.getNamedQuery("findAllPurchaseOrgBudgets").setCacheable(true).list();
            return orgBudgets;
        }
        catch(HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    // ??
//    public Collection findMissingOrgBudgets() throws InfrastructureException {
//        Session session = HibernateUtil.getSession();
//        Collection orgBudgets;
//        try {
//            orgBudgets = session.getNamedQuery("findMissingOrgBudgets").setCacheable(true).list();
//            return orgBudgets;
//        }
//        catch(HibernateException ex) {
//            throw new InfrastructureException(ex);
//        }
//    }

    //public Collection findMissingOrgBudgets() {
    public List<OrgBudget> findMissingOrgBudgets() {
        List<OrgBudget> missingBudgets;

        String sqlString =
                //"Select org.dept_id,org.APPR_CODE, ORG_BDGT_CODE,org.PROJECT_ID, org.ORG_BDGT_NAME, minimum_amount,person.LAST_NAME, person.FIRST_NAME, br.PRIMARY_EVALUATOR as \"Internal Name of Group\" " +
                        "select org.* " +
                        "from prism_owner.business_rules_tbl br inner join admin_db.org_bdgt_tbl_view org on " +
                        "org.org_bdgt_id = br.org_budget_id " +
                        "left join admin_db.group_tbl gr on gr.group_name = br.primary_evaluator " +
                        "left join admin_db.entity_target_group_link etgl on etgl.group_id = gr.group_id " +
                        "left join admin_db.person_tbl person on person.person_id = etgl.entity_target_id " +
                        "where (br.end_date is null or br.end_date > sysdate) " +
                        "and (etgl.end_date is null or etgl.end_date > sysdate) " +
                        "and etgl.group_id is null " +
                        "and org.end_date > sysdate " +
                        "order by org.dept_id,appr_code,org_bdgt_code,org.project_id ,org.org_bdgt_code ";

        try {
            SQLQuery sqlQuery = HibernateUtil.getSession().createSQLQuery(sqlString).addEntity("orgBdgt", OrgBudget.class);

            missingBudgets = sqlQuery.list();
            return missingBudgets;
        } catch (InfrastructureException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return null;
    }

    public void makePersistent(OrgBudget orgBudget) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(orgBudget);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public void makeTransient(OrgBudget orgBudget) throws InfrastructureException {
        try {
            HibernateUtil.getSession().delete(orgBudget);
        } 
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }
}