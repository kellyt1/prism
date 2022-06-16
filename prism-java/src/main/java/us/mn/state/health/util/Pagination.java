package us.mn.state.health.util;

public class Pagination {
    public final static Pagination DEFAULT_PAGINATION = new Pagination(0, 10);
    public final static int UNPAGINATED = -1;

    private int pageNumber = UNPAGINATED;

    private int pageSize = UNPAGINATED;

    public static final Pagination NO_PAGINATION = new Pagination(UNPAGINATED, UNPAGINATED);

    public Pagination(int pageNumber, int pageSize) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

//    public void setPageNumber(int pageNumber) {
//        this.pageNumber = pageNumber;
//    }
//
//    public void setPageSize(int pageSize) {
//        this.pageSize = pageSize;
//    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pagination that = (Pagination) o;

        if (pageSize != that.pageSize) return false;
        if (pageNumber != that.pageNumber) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = pageNumber;
        result = 31 * result + pageSize;
        return result;
    }
}
