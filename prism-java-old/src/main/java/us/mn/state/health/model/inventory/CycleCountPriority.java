package us.mn.state.health.model.inventory;

import java.io.Serializable;

import us.mn.state.health.util.ProxyUtils;

public class CycleCountPriority implements Serializable {
    private Long cycleCountPriorityId;
    private String code;
    private String description;

    public Long getCycleCountPriorityId() {
        return cycleCountPriorityId;
    }

    public void setCycleCountPriorityId(Long cycleCountPriorityId) {
        this.cycleCountPriorityId = cycleCountPriorityId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public boolean equals(Object o) {
        if (this == o) return true;

        if (!ProxyUtils.validateClassesForEquals(CycleCountPriority.class, this, o)) return false;
        CycleCountPriority that = (CycleCountPriority) o;

        if (!getCode().equals(that.getCode())) return false;
        if (!getCycleCountPriorityId().equals(that.getCycleCountPriorityId())) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = getCycleCountPriorityId().hashCode();
        result = 31 * result + getCode().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
}
