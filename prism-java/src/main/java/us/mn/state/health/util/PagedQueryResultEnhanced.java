package us.mn.state.health.util;

import java.util.List;

public class PagedQueryResultEnhanced {
    private final List results;
    private int totalResults = 0;
//    private int firstResult;

    public PagedQueryResultEnhanced(List results, Pagination pagination, int totalResults) {
        if (pagination == null || pagination.equals(Pagination.NO_PAGINATION)) {
            throw new IllegalArgumentException("Invalid pagination parameter!");
        }
        this.results = results;
        this.pageSize = pagination.getPageSize();
        this.totalResults = totalResults;
        setPageNo(pagination.getPageNumber());
    }

    public static final int DEFAULT_PAGE_SIZE = 10;

    public static final int DEFAULT_MAX_LINKED_PAGES = 20;

    private int pageSize = DEFAULT_PAGE_SIZE;

    private int pageNo = 0;

//    private boolean newPageSet;

    private int maxLinkedPages = DEFAULT_MAX_LINKED_PAGES;

    /**
     * Set the current page size.
     * <p>Default value is 10.
     */
    public void setPageSize(int pageSize) {
        if (pageSize != this.pageSize) {
            this.pageSize = pageSize;
        }
    }

    /**
     * Return the current page size.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Set the current page number.
     * Page numbering starts with 0.
     */
    private void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * Return the current page number.
     * Page numbering starts with 0.
     */
    public int getPageNo() {
        if (this.pageNo >= getPageCount()) {
            this.pageNo = getPageCount() - 1;
        }
        return this.pageNo;
    }

    /**
     * Set the maximum number of page links to a few pages around the current one.
     */
    public void setMaxLinkedPages(int maxLinkedPages) {
        this.maxLinkedPages = maxLinkedPages;
    }

    /**
     * Return the maximum number of page links to a few pages around the current one.
     */
    public int getMaxLinkedPages() {
        return maxLinkedPages;
    }


    /**
     * Return the number of pages for the current source list.
     */
    public int getPageCount() {
        float nrOfPages = (float) totalResults / getPageSize();
        return (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages);
    }

    /**
     * Return if the current page is the first one.
     */
    public boolean isFirstPage() {
        return getPageNo() == 0;
    }

    /**
     * Return if the current page is the last one.
     */
    public boolean isLastPage() {
        return getPageNo() == getPageCount() - 1;
    }

    /**
     * Switch to previous page.
     * Will stay on first page if already on first page.
     */
    public void previousPage() {
        if (!isFirstPage()) {
            this.pageNo--;
        }
    }

    /**
     * Switch to next page.
     * Will stay on last page if already on last page.
     */
    public void nextPage() {
        if (!isLastPage()) {
            this.pageNo++;
        }
    }

    /**
     * Return the total number of elements in the source list.
     */
    public int getNrOfElements() {
        return totalResults;
    }

    /**
     * Return the element index of the first element on the current page.
     * Element numbering starts with 0.
     */
    public int getFirstElementOnPage() {
        return (getPageSize() * getPageNo());
    }

    /**
     * Return the element index of the last element on the current page.
     * Element numbering starts with 0.
     */
    public int getLastElementOnPage() {
        int endIndex = getPageSize() * (getPageNo() + 1);
        return (endIndex > totalResults ? totalResults : endIndex) - 1;
    }

    /**
     * Return a sub-list representing the current page.
     */
    public List getPageList() {
        return results;
    }

    /**
     * Return the first page to which create a link around the current page.
     */
    public int getFirstLinkedPage() {
        return Math.max(0, getPageNo() - (getMaxLinkedPages() / 2));
    }

    /**
     * Return the last page to which create a link around the current page.
     */
    public int getLastLinkedPage() {
        return Math.min(getFirstLinkedPage() + getMaxLinkedPages() - 1, getPageCount() - 1);
    }


}
