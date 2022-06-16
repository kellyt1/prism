package us.mn.state.health.util.hibernate.queryparameters;

import org.hibernate.LockMode;

public class BaseQueryParameters {

    protected String lockAlias;
    protected LockMode lockMode;
    protected boolean justOne = false;


    public BaseQueryParameters(LockMode lockMode) {
        this.lockMode = lockMode;
    }

    public BaseQueryParameters(String lockAlias, LockMode lockMode) {
        this.lockAlias = lockAlias;
        this.lockMode = lockMode;
    }

    public BaseQueryParameters(String lockAlias, LockMode lockMode, boolean justOne) {
        this.lockAlias = lockAlias;
        this.lockMode = lockMode;
        this.justOne = justOne;
    }

    public String getLockAlias() {
        return lockAlias;
    }

    public LockMode getLockMode() {
        return lockMode;
    }


    public boolean isJustOne() {
        return justOne;
    }

    public void setJustOne(boolean justOne) {
        this.justOne = justOne;
    }
}
