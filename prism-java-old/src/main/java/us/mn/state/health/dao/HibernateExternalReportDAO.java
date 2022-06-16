package us.mn.state.health.dao;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.PrismExternalReports;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Example;

public class HibernateExternalReportDAO extends HibernateDAO implements ExternalReportDAO{
    public Collection findAll() throws InfrastructureException {
        return super.findAll(PrismExternalReports.class);
    }

    public Collection findByExample(PrismExternalReports phone) throws InfrastructureException {
        Collection externalReports;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(PrismExternalReports.class);
            externalReports = crit.add(Example.create(phone)).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return externalReports;
    }


    public void makePersistent(PrismExternalReports externalReport) throws InfrastructureException {
        super.makePersistent(externalReport);

    }


    public void makeTransient(PrismExternalReports externalReport) throws InfrastructureException {
        super.makeTransient(externalReport);
    }
}
