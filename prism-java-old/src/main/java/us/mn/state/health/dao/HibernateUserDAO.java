package us.mn.state.health.dao;

import org.hibernate.*;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.lang.StringUtils;
import us.mn.state.health.model.common.*;
import us.mn.state.health.persistence.HibernateUtil;

import java.util.Collection;
import java.util.Iterator;

public class HibernateUserDAO extends HibernatePersonDAO implements UserDAO {

    public HibernateUserDAO() {
        try {
            HibernateUtil.beginTransaction();
        }
        catch (InfrastructureException ie) {
        }
    }

    /**
     * authenticate a user
     *
     * @param username
     * @param password
     * @return TRUE if valid credentials passed in, FALSE otherwise
     * @throws us.mn.state.health.common.exceptions.InfrastructureException
     *
     */
    public Boolean authenticate(String username, String password, String authentication) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        User user = null;
       // password = Utilities.encryptPassword(password.toLowerCase());
        try {
            user = findUserByUsername(username);

            if (user == null) {
                return Boolean.FALSE;
            }

//            if(password.equals(user.getPassword())){
            Iterator iter = user.getPersonGroupLinks().iterator();
            while (iter.hasNext()) {
                PersonGroupLink groupLink = (PersonGroupLink) iter.next();
                Hibernate.initialize(groupLink.getGroup());
            }
            return Boolean.TRUE;
//            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
//        return Boolean.FALSE;
    }

    public User findUserByUsername(String username) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        if (StringUtils.nullOrBlank(username)) {
            return null;
        }
        User user = null;
        Person person = null;
        try {
            person = (Person) session.getNamedQuery("findUserByUsername")
                    .setString("username", username.toLowerCase())
                    .uniqueResult();
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }

        if (person != null) {

            user = person.getUser();
            // Get and set the primary email address
            Collection<PersonEmailAddressLink> cEmailAddress = person.getPersonEmailAddressLinks();
            for (Iterator<PersonEmailAddressLink> emailAddressIterator = cEmailAddress.iterator(); emailAddressIterator.hasNext();) {
                PersonEmailAddressLink emailAddress =  emailAddressIterator.next();
                user.setEmailAddress(emailAddress.getEmailAddress().getEmailAddress());
            }


            String pPhone = "";

            SQLQuery cc = session.createSQLQuery("Select CONTACT_NBR_TEXT from ENTITY_TARGET_CONTACT_LINK where device_type = 'LAND_PHONE' and end_date is null and start_date <= sysdate and (termination_date is null or termination_date > sysdate) and entity_target_id = " + person.getPersonId());
            //Criteria cc = session.createCriteria(PersonPhone.class)
            //     .add(eq("person.personId",person.getPersonId()));

            Collection <PersonPhone> pp = cc.list();
            for (Iterator<PersonPhone> personPhoneIterator = pp.iterator(); personPhoneIterator.hasNext();) {
                Object personPhone = personPhoneIterator.next();
                if (personPhone != null) {
                    user.setWorkPhone(personPhone.toString());
                }
            }
        }


        return user;
    }

    public String findAllByGroup(String inGroup) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String groupAuthority(String username) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Collection findAll() throws InfrastructureException {
        String hql =
                "select new User(u.personId, u.firstName, u.middleName, u.lastName) from User u" +
                        " order by lower(u.lastName) asc, lower(u.firstName) asc";
        Query query = HibernateUtil.getSession().createQuery(hql);
        query.setCacheable(true);
        return query.list();
    }


    public Collection findByExample(User user) throws InfrastructureException {
        //TODO: implement this method
        return null;
    }

    public User getUserById(Long userId, boolean lock) throws InfrastructureException {
        Session session = HibernateUtil.getSession();
        Person person = null;
        User user = null;
        try {
            if (lock) {
                person = (Person) session.load(Person.class, userId, LockMode.UPGRADE);
                //user = (User)session.load(User.class, userId, LockMode.UPGRADE);
                user = person.getUser();
            } else {
                //user = (User)session.load(User.class, userId);
                person = (Person) session.load(Person.class, userId);
                user = person.getUser();
            }
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
        return user;
    }

    public void makePersistent(User user) throws InfrastructureException {
        try {
            HibernateUtil.getSession().saveOrUpdate(user);
        }
        catch (HibernateException ex) {
            throw new InfrastructureException(ex);
        }
    }

    public java.sql.Connection getConnection() {
        java.sql.Connection conn = null;
        try {
            conn = HibernateUtil.getSession().connection();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return conn;
    }

    public int changePassword(String username, String password, String oldpassword) throws InfrastructureException {
        return -1;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getErrorMSG() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getErrorType() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
