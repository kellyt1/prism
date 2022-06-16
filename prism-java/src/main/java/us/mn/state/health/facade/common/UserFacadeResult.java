package us.mn.state.health.facade.common;

import java.util.List;

public class UserFacadeResult {
    private List users;
    private int status;


    public UserFacadeResult() {
    }


    public List getUsers() {
        return users;
    }

    public void setUsers(List users) {
        this.users = users;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
