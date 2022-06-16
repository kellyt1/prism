package us.mn.state.health.dao;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.transform.AliasToBeanResultTransformer;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.Employee;
import us.mn.state.health.model.common.EmployeeQuickTblId;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateEmployeeDAO implements EmployeeDAO {

    public HibernateEmployeeDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    public Employee getEmployeeByPersonId(Long personId, boolean lock) throws InfrastructureException {
        Collection employees;
        Employee employee = null;
        Session session = HibernateUtil.getSession();
        try {
            Criteria criteria = session.createCriteria(Employee.class);
//            criteria.add(Restrictions.sqlRestriction("{alias}.person_id in (select distinct person_id from employee_quick_tbl)"));
            criteria.add(Expression.eq("personId",personId));
            criteria.setCacheable(true);
            employees = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        for (Iterator iterator = employees.iterator(); iterator.hasNext();) {
            Object o =  iterator.next();
            employee = (Employee) o;
            break;
        }
        return employee;
    }

    public Employee getEmployeeById(EmployeeQuickTblId employeeQuickTblId, boolean lock) throws InfrastructureException {
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
        Session session = HibernateUtil.getSession();
        Employee employee = null;
        try {
            if (lock) {
                employee = (Employee) session.load(Employee.class, employeeQuickTblId, LockMode.UPGRADE);
            } else {
                employee = (Employee) session.load(Employee.class, employeeQuickTblId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return employee;
    }

    public Collection findAll() throws InfrastructureException {
        Collection employees;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Employee.class);
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setCacheable(true);
            employees = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return employees;
    }


    public Collection findAllMDHEmployees() throws InfrastructureException {
        Collection employees;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Employee.class);
//            criteria.add(Restrictions.sqlRestriction("{alias}.person_id in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setCacheable(true);
            employees = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return employees;
    }

    public Collection findAllMDHEmployeesAsDTO() throws InfrastructureException {
        Collection employees;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Employee.class);
            criteria.add(Restrictions.sqlRestriction("{alias}.person_id in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setProjection(Projections.projectionList()
                    .add(Projections.id().as("personId"))
                    .add(Projections.property("firstName").as("firstName"))
                    .add(Projections.property("middleName").as("middleName"))
                    .add(Projections.property("lastName").as("lastName"))
            ).setResultTransformer(new AliasToBeanResultTransformer(Person.class));
            employees = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return employees;

    }

}