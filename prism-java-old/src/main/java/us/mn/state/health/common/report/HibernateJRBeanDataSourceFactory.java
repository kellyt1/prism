package us.mn.state.health.common.report;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

import net.sf.jasperreports.engine.JRException;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import us.mn.state.health.persistence.HibernateUtil;

public class HibernateJRBeanDataSourceFactory  {
    
    private Stack iteratorStack = new Stack();
    
    public HibernateJRBeanDataSourceFactory(String hql) {
        try {
            Collection queryResults = executeQuery(hql);
            Iterator iterator = queryResults.iterator();
            iteratorStack.push(new IteratorInStack("", iterator)); //push on to stack
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private Collection executeQuery(String hql) throws Exception {
        Query query = HibernateUtil.getSession().createQuery(hql);
        return query.list();
    }
    
    public boolean next() throws JRException { 
        boolean next = getCurrentIterator().iterator.hasNext();
        if(next) {
            getCurrentIterator().currentItem = getCurrentIterator().iterator.next();
        }
        else {
            if(iteratorStack.size() > 1) {
                iteratorStack.pop();
            }
        }
        return next;
    }
    public Object getFieldValue(String propertyName) throws JRException {
        //Check if in current collection item
        //String propertyName = jrField.getName();
        Object value = null;
        value = getCurrentIterator().getProperty(propertyName);
        
        if(value == null) { //Item in a nested collection
            IteratorInStack iterator = getIterator(propertyName);
            if(iterator != null) {
                //Get value from Current Iterator item
                value = iterator.getProperty(propertyName);
                if(value != null) {
                    iteratorStack.push(iterator); //this is now the current iterator, push it on the stack
                }
            }
        }
        return value;  
    }
    
    private IteratorInStack getIterator(String propertyName) throws JRException {
        propertyName = propertyName.replace('.', '_');
        String[] fieldTokens = propertyName.split("_");
        propertyName = propertyName.replace('_', '.');
        Collection collection = null;
        for(int i = fieldTokens.length-1; i >= 0; i--) {
            try {
                propertyName = 
                    propertyName.substring(0, propertyName.length() - (fieldTokens[i].length() + 1));
                collection = (Collection)PropertyUtils.getNestedProperty(getCurrentIterator().currentItem, propertyName);
                if(collection != null) {
                    i = -1; //break out of the loop
                }
            }
            catch(Exception e) {
                //do nothing
            }
        }
        if(collection != null) {
            IteratorInStack iterator  = new IteratorInStack(propertyName, collection.iterator());
            if(iterator.iterator.hasNext()) {
                iterator.currentItem = iterator.iterator.next();
            }
            return iterator;
        }
        return null;
    }
    
    private IteratorInStack getCurrentIterator() {
        return (IteratorInStack)iteratorStack.peek();
    }
    
    private class IteratorInStack {
        private String name;
        private Iterator iterator;
        private Object currentItem;
        private HashMap fieldsPrinted = new HashMap();
        
        private IteratorInStack(String name, Iterator iterator) {
            this.name = name;
            this.iterator = iterator;
        }
        
        private Object getProperty(String propertyName) {
            if(!name.equals("")) {
                propertyName = propertyName.substring(name.length() + 1, propertyName.length());
            }
            Object value = null;
            try {
                value = PropertyUtils.getNestedProperty(currentItem, propertyName);
                fieldsPrinted.put(propertyName, propertyName);
            }
            catch(Exception e) {
                //do nothing
            }
            return value;
        }
        
        private boolean alreadyPrinted(String propertyName) {
            return (fieldsPrinted.get(propertyName) != null);
        }
    }
    
}