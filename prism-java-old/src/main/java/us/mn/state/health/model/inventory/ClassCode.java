package us.mn.state.health.model.inventory;

import java.io.Serializable;

/**
 * This is a class to represent the CLASS_CODE_REF table,
 * which holds allowable values for the FixedAsset.classCode
 * property.
 */
public class ClassCode implements Serializable {
    private Long classCodeId;
    private String classCodeValue;
    private String description;
    private Integer life; //this is the life in years


    public void setClassCodeId(Long classCodeId) {
        this.classCodeId = classCodeId;
    }


    public Long getClassCodeId() {
        return classCodeId;
    }


    public void setClassCodeValue(String classCodeValue) {
        this.classCodeValue = classCodeValue;
    }


    public String getClassCodeValue() {
        return classCodeValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getLife() {
        return life;
    }

    public void setLife(Integer life) {
        this.life = life;
    }


}