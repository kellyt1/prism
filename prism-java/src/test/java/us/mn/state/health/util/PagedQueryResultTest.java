package us.mn.state.health.util;

import java.util.ArrayList;

import junit.framework.TestCase;

public class PagedQueryResultTest extends TestCase {

    @Override
    protected void setUp() {

    }

    public void testPagination() {
        ArrayList results = new ArrayList();
        PagedQueryResultEnhanced queryResultEnhanced = new PagedQueryResultEnhanced(results, new Pagination(0, 3), 11);
        assertEquals(0, queryResultEnhanced.getPageNo());
        queryResultEnhanced = new PagedQueryResultEnhanced(results, new Pagination(1, 1), 11);
        assertEquals(1, queryResultEnhanced.getPageNo());
        queryResultEnhanced = new PagedQueryResultEnhanced(results, new Pagination(2, 3), 11);
        assertEquals(2, queryResultEnhanced.getPageNo());

        queryResultEnhanced = new PagedQueryResultEnhanced(results, new Pagination(20, 3), 11);
        assertEquals(3, queryResultEnhanced.getPageNo());
    }

    @Override
    protected void tearDown() {

    }
}