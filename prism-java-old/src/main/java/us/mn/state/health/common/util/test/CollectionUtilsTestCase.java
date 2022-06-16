package us.mn.state.health.common.util.test;
import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

import junit.framework.TestCase;
import us.mn.state.health.common.lang.test.TestObject;
import us.mn.state.health.common.util.CollectionUtils;

public class CollectionUtilsTestCase extends TestCase  {
    public CollectionUtilsTestCase(String sTestName) {
            super(sTestName);
    }

    /**
     * void synchronizeItems(Collection, Collection, String)
     */
    public void testsynchronizeItems() throws Exception {
        Collection selected = new ArrayList();
        selected.add(new TestObject("obj1", new Integer(1)));
        selected.add(new TestObject("obj2", new Integer(2)));
        selected.add(new TestObject("obj3", new Integer(3)));
        
        Collection available = new ArrayList();
        available.add(new TestObject("obj1", new Integer(1)));
        available.add(new TestObject("obj2", new Integer(2)));
        available.add(new TestObject("obj3", new Integer(3)));
        available.add(new TestObject("obj4", new Integer(4)));
        
        CollectionUtils.synchronizeItems(selected, available, "integerProp");
    }
    
    public void testsort() throws Exception {
        Collection c = new ArrayList();
        c.add(new TestObject("johnson", new Integer(4)));
        c.add(new TestObject("madison", new Integer(3)));
        c.add(new TestObject("madison", new Integer(2)));
        c.add(new TestObject("peterson", new Integer(1)));
        
        CollectionUtils.sort(c, "stringProp,integerProp", true);
        int i=0;
    }
    
    public void testGetObjectFromSetById() throws Exception {
        Collection c = new TreeSet();
        c.add(new TestObject("d1", new Integer(4)));
        c.add(new TestObject("c1", new Integer(3)));
        c.add(new TestObject("b1", new Integer(2)));
        c.add(new TestObject("a1", new Integer(1)));
        Object obj = CollectionUtils.getObjectFromCollectionById(c, new Integer(3), "integerProp");
        
        c = new ArrayList();
        c.add(new TestObject("d1", new Integer(4)));
        c.add(new TestObject("c1", new Integer(3)));
        c.add(new TestObject("b1", new Integer(2)));
        c.add(new TestObject("a1", new Integer(1)));
        obj = CollectionUtils.getObjectFromCollectionById(c, new Integer(3), "integerProp");
        int i = 0;
    }
}