package us.mn.state.health.model.common;

import java.io.Serializable;

public class Priority implements Serializable {
    private Long priorityId;
    private String priorityCode;
    private String name;
    private Integer codeNumber; // 1-High, 2-Normal, 3-Low

    public static final String HIGH = "HIGH";
    public static final String LOW = "LOW";
    public static final String NORMAL = "NORM";

    public Long getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Long priorityId) {
        this.priorityId = priorityId;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCodeNumber() {
        return codeNumber;
    }

}
