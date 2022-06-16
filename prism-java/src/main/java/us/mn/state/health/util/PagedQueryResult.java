package us.mn.state.health.util;

import java.util.List;

public class PagedQueryResult {
    private final boolean more;
    private final List results;

    public PagedQueryResult(List results, boolean more) {
        this.results = results;
        this.more = more;
    }

    public boolean isMore() {
        return more;
    }

    public List getResults() {
        return results;
    }
}