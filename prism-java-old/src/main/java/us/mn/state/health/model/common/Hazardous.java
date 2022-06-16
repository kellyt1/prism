package us.mn.state.health.model.common;

import java.io.Serializable;

import us.mn.state.health.util.ProxyUtils;

public class Hazardous implements Serializable {
    private Long hazardousId;
    private String code;
    private String description;


    public Long getHazardousId() {
        return hazardousId;
    }

    public void setHazardousId(Long hazardousId) {
        this.hazardousId = hazardousId;
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
        if (!ProxyUtils.validateClassesForEquals(Hazardous.class, this, o)) return false;

        Hazardous hazardous = (Hazardous) o;

        if (code != null ? !code.equals(hazardous.getCode()) : hazardous.getCode() != null) return false;
        if (description != null ? !description.equals(hazardous.getDescription()) : hazardous.getDescription() != null)
            return false;
        if (!hazardousId.equals(hazardous.getHazardousId())) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = hazardousId.hashCode();
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
