package us.mn.state.health.model.util.search.spring;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.CallbackException;
import org.hibernate.EntityMode;
import org.hibernate.Interceptor;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.model.util.search.Searchable;

public class LuceneInterceptor implements Interceptor, Serializable {
    private Log log = LogFactory.getLog(LuceneInterceptor.class);

    private ThreadLocal dirty = new ThreadLocal();
    private ThreadLocal deleted = new ThreadLocal();
    private ThreadLocal newO = new ThreadLocal();


    public LuceneInterceptor() {
        log.debug("===================Instantiate the Lucene Interceptor===================");
    }

    /**
     * Called just before an object is initialized. The interceptor may change the state,
     * which will be propagated to the persistent object.
     * Note that when this method is called, entity will be an empty uninitialized
     * instance of the class.
     *
     * @param entity
     * @param id
     * @param state
     * @param propertyNames
     * @param types
     * @return true if the user modified the state in any way.
     * @throws org.hibernate.CallbackException
     *
     */
    public boolean onLoad(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) throws CallbackException {
        /* 04/05/2005 - Shawn: I added the following if block to try and make the Lucene Index classes
         * execute the static blocks right away.  Just by calling getEntityIndex(), we're
         * instantiating the appropriate EntityIndex class which will cause the static block of
         * index initialization code to be called.

        if(entity instanceof Searchable) {
           try {
               Searchable searchable = (Searchable)entity;
               searchable.getEntityIndex();
            }
            catch (InfrastructureException e) {
               throw new CallbackException(e.getMessage());
            }
        }
        */
        return false;
    }

    /**
     * Called when an object is detected to be dirty, during a flush.
     * The interceptor may modify the detected currentState, which will be
     * propagated to both the database and the persistent object.
     * Note that not all flushes end in actual synchronization with the database,
     * in which case the new currentState will be propagated to the object, but not
     * necessarily (immediately) to the database. It is strongly recommended that the
     * interceptor not modify the previousState.
     * <p/>
     * 12/27/2005 - SF - I modified this class and this method so that it now simply adds the Searchable entity
     * to a HashSet (HashSet to make sure that the entity exists only once in the collection).  This is somewhat of a hack
     * fix for a problem where this gets called multiple times for the same object.  I don't know why this happens, but
     * the result was that we were searchable.getEntityIndex().dropAndAdd(searchable); more than once, which has an obvious
     * negative performance hit associated with it because it involves File I/O.  Now, we will put that off to the postFlush()
     * method, which will call dropAndAdd() for each Searchable in the dirtyObjects HashSet.   So far it seems that postFlush() is only
     * being called once, which is what we wanted in the first place.
     */
    public boolean onFlushDirty(Object entity,
                                Serializable id,
                                Object[] currentState,
                                Object[] previousState,
                                String[] propertyNames,
                                Type[] types) throws CallbackException {
        log.debug("in onFlushDirty");
        Set dirtyObjectsSet = getSet(dirty);
        if (entity instanceof Searchable) {
            Searchable searchable = (Searchable) entity;
            (dirtyObjectsSet).add(searchable);
        }
        return false;
    }

    private static Set getSet(ThreadLocal threadLocal) {
        Object set = threadLocal.get();
        if (set == null) {
            set = new HashSet();
            threadLocal.set(set);
        }
        return (Set) set;
    }

    /**
     * Called before an object is saved. The interceptor may modify the state,
     * which will be used for the SQL INSERT and propagated to the persistent object.
     */
    public boolean onSave(Object entity,
                          Serializable id,
                          Object[] state,
                          String[] propertyNames,
                          Type[] types) throws CallbackException {
        Set newObjectsSet = getSet(newO);
        if (entity instanceof Searchable) {
            Searchable searchable = (Searchable) entity;
            newObjectsSet.add(searchable);
        }
        return false;
    }

    /**
     * Called before an object is deleted. It is not recommended that the interceptor modify the state.
     */

    public void onDelete(Object entity,
                         Serializable id,
                         Object[] state,
                         String[] propertyNames,
                         Type[] types) throws CallbackException {
        Set deletedObjectsSet = getSet(deleted);
        if (entity instanceof Searchable) {
            Searchable searchable = (Searchable) entity;
            deletedObjectsSet.add(searchable);
        }
    }

    /**
     * Called before a flush
     *
     * @param entities
     * @throws org.hibernate.CallbackException
     *
     */
    public void preFlush(Iterator entities) throws CallbackException {
    }

    /**
     * Called after a flush that actually ends in execution of the SQL statements required
     * to synchronize in-memory state with the database.
     *
     * @param entities
     * @throws org.hibernate.CallbackException
     *
     */
    public void postFlush(Iterator entities) throws CallbackException {
    }

    public Boolean isTransient(Object entity) {
        return null;
    }

    /**
     * Called when a transient entity is passed to saveOrUpdate(). The return value determines:
     * - Boolean.TRUE - the entity is passed to save(), resulting in an INSERT
     * - Boolean.FALSE - the entity is passed to update(), resulting in an UPDATE
     * - null - Hibernate uses the unsaved-value mapping to determine if the object is unsaved
     *
     * @param entity
     * @return null - Hibernate uses the unsaved-value mapping to determine if the object is unsaved
     */
    public Boolean isUnsaved(Object entity) {
        return null;
    }

    /**
     * Called from flush(). The return value determines whether the entity is updated
     * - an array of property indices - the entity is dirty
     * - an empty array - the entity is not dirty
     * - null - use Hibernate's default dirty-checking algorithm
     *
     * @param entity
     * @param id
     * @param currentState
     * @param previousState
     * @param propertyNames
     * @param types
     * @return null - use Hibernate's default dirty-checking algorithm
     */
    public int[] findDirty(Object entity,
                           Serializable id,
                           Object[] currentState,
                           Object[] previousState,
                           String[] propertyNames,
                           Type[] types) {
        return null;
    }

    public Object instantiate(String entityName, EntityMode entityMode, Serializable id) throws CallbackException {
        return null;
    }

    public String getEntityName(Object object) throws CallbackException {
        return null;
    }

    public Object getEntity(String entityName, Serializable id) throws CallbackException {
        return null;
    }

    public void afterTransactionBegin(Transaction tx) {

    }

    public void beforeTransactionCompletion(Transaction tx) {

    }

    public void afterTransactionCompletion(Transaction tx) {
        if (tx.wasCommitted()) {
            log.debug("in LuceneInterceptor postFlush...");
            Set newObjectsSet = getSet(newO);
            Set deletedObjectsSet = getSet(deleted);
            Set dirtyObjectsSet = getSet(dirty);
            for (Iterator iterator = newObjectsSet.iterator(); iterator.hasNext();) {
                Searchable searchable = (Searchable) iterator.next();
                try {
                    searchable.getEntityIndex().add(searchable);
                    dirtyObjectsSet.remove(searchable);
                }
                catch (InfrastructureException e) {
                    log.error("error in afterTransactionCompletion(): " + e);
                    throw new CallbackException(e.getMessage());
                }
            }

            /*
            * Here we have to remove the deleted entities from the dirtyObjects collection
            * in order to avoid adding them back to the index (postFlush() is called after onDelete())
            * TODO In order to make that to work fine we have to revise the equals and the hashCode for
            * the searchable classes
            */
            for (Iterator iterator = deletedObjectsSet.iterator(); iterator.hasNext();) {
                Searchable searchable = (Searchable) iterator.next();
                dirtyObjectsSet.remove(searchable);
                try {
                    searchable.getEntityIndex().drop(searchable);
                }
                catch (InfrastructureException e) {
                    throw new CallbackException(e.getMessage());
                }
            }
            for (Iterator iter = dirtyObjectsSet.iterator(); iter.hasNext();) {
                log.debug("in postFlush...dirtyObjects has " + dirtyObjectsSet.size() + " objects");
                Searchable searchable = (Searchable) iter.next();
                try {
                    searchable.getEntityIndex().dropAndAdd(searchable);
                }
                catch (InfrastructureException e) {
                    log.error("error in afterTransactionCompletion(): " + e);
                    throw new CallbackException(e.getMessage());
                }
            }
            dirtyObjectsSet.clear();
            dirty.remove();
            deletedObjectsSet.clear();
            deleted.remove();
            newObjectsSet.clear();
            newO.remove();
        }
    }

    /**
     * Instantiate the entity class.
     * Return null to indicate that Hibernate should use the default constructor of the class.
     * The identifier property of the returned instance should be initialized with the given identifier.
     *
     * @param clazz
     * @param id
     * @return null to indicate that Hibernate should use the default constructor of the class.
     */
    public Object instantiate(Class clazz, Serializable id) {
        return null;
    }

    public String onPrepareStatement(String sql) {
        return sql;
    }

    public void onCollectionRecreate(Object collection, Serializable key) throws CallbackException {

    }

    public void onCollectionRemove(Object collection, Serializable key) throws CallbackException {

    }

    public void onCollectionUpdate(Object collection, Serializable key) throws CallbackException {

    }
}