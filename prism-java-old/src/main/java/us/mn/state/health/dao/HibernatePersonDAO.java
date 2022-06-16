package us.mn.state.health.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.common.Person;
import us.mn.state.health.model.common.User;
import us.mn.state.health.persistence.HibernateUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class HibernatePersonDAO implements PersonDAO {

    public HibernatePersonDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    public Person getPersonById(Long personId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Person person = null;
        try {
            if (lock) {
                person = (Person) session.load(Person.class, personId, LockMode.UPGRADE);
            } else {
                person = (Person) session.load(Person.class, personId);
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return person;
    }

    public Collection findAll() throws InfrastructureException {
        Collection persons;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setCacheable(true);
            persons = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;
    }

    public Collection findAllMDHEmployees() throws InfrastructureException {
        Collection persons;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
            criteria.add(Restrictions.sqlRestriction("{alias}.person_id in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setCacheable(true);
            persons = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;
    }

    public Collection findAllMDHEmployeesAsDTO() throws InfrastructureException {
        Collection persons;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
            criteria.add(Restrictions.sqlRestriction("{alias}.person_id in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setProjection(Projections.projectionList()
                    .add(Projections.id().as("personId"))
                    .add(Projections.property("firstName").as("firstName"))
                    .add(Projections.property("middleName").as("middleName"))
                    .add(Projections.property("lastName").as("lastName"))
            ).setResultTransformer(new AliasToBeanResultTransformer(Person.class));
            persons = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;

    }

    public Collection findAllNonMDHEmployees() throws InfrastructureException {
        Collection persons;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
            criteria.add(Restrictions.sqlRestriction("{alias}.person_id not in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setCacheable(true);
            persons = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;
    }


    public Collection findAllNonMDHEmployeesAsDTO() throws InfrastructureException {
        Collection persons;
        try {
            Criteria criteria = HibernateUtil.getSession().createCriteria(Person.class);
            criteria.add(Restrictions.sqlRestriction("{alias}.person_id not in (select distinct person_id from employee_quick_tbl)"));
            criteria.addOrder(Order.asc("lastName"));
            criteria.addOrder(Order.asc("firstName"));
            criteria.setProjection(Projections.projectionList()
                    .add(Projections.id().as("personId"))
                    .add(Projections.property("firstName").as("firstName"))
                    .add(Projections.property("middleName").as("middleName"))
                    .add(Projections.property("lastName").as("lastName"))
            ).setResultTransformer(new AliasToBeanResultTransformer(Person.class));
            criteria.setCacheable(false);
            persons = criteria.list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;
    }


    public Collection findByExample(Person person) throws InfrastructureException {
        Collection persons;
        try {
            Criteria crit = HibernateUtil.getSession().createCriteria(Person.class);
            crit.setCacheable(true);
            persons = crit.add(Example.create(person).ignoreCase()).list();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return persons;
    }

    public void makePersistent(Person person) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(person);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public Collection findPersonsByPositionClassCode(String classCode) throws InfrastructureException {
        List result;
        Session session = HibernateUtil.getSession();
        try {
            result = session.getNamedQuery("findPersonsByPositionClassCode")
                    .setString("classCode", classCode)
                    .setCacheable(true)
                    .list();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return result;
    }

    public Collection findPersonsByGroupId(Long groupId) throws InfrastructureException {
        Collection groupMembers;
        Session session = HibernateUtil.getSession();
        try {
            groupMembers = session.getNamedQuery("findPersonsByGroupId")
                    .setLong("groupId", groupId.longValue())
                    .setCacheable(true)
                    .list();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return groupMembers;
    }

//Roll back to this version of method because we have decided to no longer use Microsoft AD to
//manage group membership, for now anyway
    public List findPersonsByGroupCode(String code) throws InfrastructureException {
        List groupMembers;
        Session session = HibernateUtil.getSession();
        try {
            groupMembers = session.getNamedQuery("findPersonsByGroupCode")
                    .setString("groupCode", code)
                    .list();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return groupMembers;
    }


/*
    public List<Person> findPersonsByGroupCode(String code) throws InfrastructureException {
// Even though it is a hibernate implemenataion we will get the group from the LDAPUserDAO
        List<Person> groupMembers = new ArrayList();
        DAOFactory ldapFactory = DAOFactory.getDAOFactory(DAOFactory.LDAP);
//        DAOFactory ldapFactory = DAOFactory.getDAOFactory(DAOFactory.ORACLE);
        UserDAO userDAO = ldapFactory.getUserDAO();
        String outString = userDAO.findAllByGroup(code);


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayInputStream bs = new ByteArrayInputStream(outString.getBytes());
            Document doc = db.parse(bs);
            doc.getDocumentElement().normalize();

            System.out.println("Root element " + doc.getDocumentElement().getNodeName());
            NodeList nodeLst = doc.getElementsByTagName("member");

            HibernateUserDAO hudao = new HibernateUserDAO();
            for (int s = 0; s < nodeLst.getLength(); s++) {

                Node fstNode = nodeLst.item(s);

                if (fstNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element fstElmnt = (Element) fstNode;
                    NodeList fstNmElmntLst = fstElmnt.getElementsByTagName("cn");
                    Element fstNmElmnt = (Element) fstNmElmntLst.item(0);
                    NodeList fstNm = fstNmElmnt.getChildNodes();
                    String sUser = fstNm.item(0).getNodeValue().toUpperCase();
                   // System.out.println("User : " + sUser);
                    User user = hudao.findUserByUsername(sUser);
                    if (!(user == null)) groupMembers.add(user);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
//        Session session = HibernateUtil.getSession();
//        try {
//            groupMembers = session.getNamedQuery("findPersonsByGroupCode")
//                    .setString("groupCode", code)
//                    .list();
//        }
//        catch (HibernateException e) {
//            throw new InfrastructureException(e);
//        }
        return groupMembers;
    }
*/

    public Collection findBudgetManagersByOrgBdgtCode(String orgBdgtCode) throws InfrastructureException {
        List result;
        Session session = HibernateUtil.getSession();
        try {
            result = session.getNamedQuery("findBudgetManagersByOrgBdgtCode")
                    .setString("orgBdgtCode", orgBdgtCode)
                    .setCacheable(true)
                    .list();
        }
        catch (HibernateException e) {
            throw new InfrastructureException(e);
        }
        return result;
    }
}
