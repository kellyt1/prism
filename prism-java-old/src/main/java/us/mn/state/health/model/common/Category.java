package us.mn.state.health.model.common;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Category implements Serializable, Comparable {
    private int version;
    private String categoryCode;
    private String name;
    private Long categoryId;
    private Date insertionDate;
    private Category parentCategory;
    private Collection childCategories = new HashSet();
    private String insertedBy;
    private String usedFor;

    public static final String UNKNOWN = "UNK";
    public static final String MATERIALS = "MAT";
    public static final String MATERIALS_FORMS = "140";
    public static final String MATERIALS_PRINTEDMATERIALS = "141";
    public static final String MATERIALS_WOLFMATERIALS = "364";
    public static final String MATERIALS_LABSUPPLIES = "365";
    public static final String MATERIALS_CHEMICALS = "366";
    public static final String MATERIALS_LABMEDIA = "368";
    public static final String MATERIALS_GASSES = "369";
    public static final String MATERIALS_OFFICESUPPLIES = "375";
    public static final String MATERIALS_CONTAINERS = "381";
    public static final String MATERIALS_MISCELLANEOUS = "399";
    public static final String MATERIALS_COMPUTER_EQUIPMENT = "COMP";
    public static final String MATERIALS_RADIO_EQUIPMENT = "RADI";
    public static final String MATERIALS_FOR_REQUEST = "REQUEST";
    public static final String MATERIALS_FOR_STOCK = "STOCK";
    public static final String MATERIALS_STANDARD_COMPUTERS = "SCOMP_C";
    public static final String MATERIALS_STANDARD_COMPUTER_ACCESSORY = "SCOMP_A";
    public static final String MATERIALS_STANDARD_SOFTWARE = "SCOMP_S";
    public static final String MATERIALS_IT_PURCHASE_OTHER = "ITPUR_OTH";
    public static final String MATERIALS_IT_SERVICES_AND_CONTRACTS = "SCOMP_SC";
    public static final String MATERIALS_CONFERENCES_AND_MEETINGS = "CONF/MEETING";
    public static final String MATERIALS_CATERING_AND_FOOD = "CATERING/FOOD";
    public static final String MATERIALS_BOOK = "BOK";

    /**
     * No-arg constructor for JavaBean tools.
     */
    public Category() {
    }

    public Category(String name) {
        this(name, "", null);
    }

    public Category(String name, String categoryCode) {
        this(name, categoryCode, null);
    }

    public Category(String name, String categoryCode, Category parentCategory) {
        this.name = name;
        this.categoryCode = categoryCode;
        this.parentCategory = parentCategory;
    }

    public void addChildCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Can't add a null Category as child.");
        }
        // Remove from old parent category
        if (category.getParentCategory() != null) {
            category.getParentCategory().getChildCategories().remove(category);
        }
        // Set parent in child
        category.setParentCategory(this);
        // Set child in parent
        this.getChildCategories().add(category);
    }

    // ********************** Common Methods ********************** //
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        final Category category = (Category) o;
        return getCategoryId().equals(category.getCategoryId());
    }

    public int hashCode() {
        int result;
        result = (getName() != null ? getName().hashCode() : 0);
        result = 29 * result + (insertionDate != null ? insertionDate.hashCode() : 0);
        return result;
    }

    public String toString() {
        return "\n Category ('" + getCategoryId() + "'), " + "Name: '" + getName() + "'";
    }

    public int compareTo(Object o) {
        if (o instanceof Category) {
            return this.getName().compareTo(((Category) o).getName());
        }
        return 0;
    }


    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Collection getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(Set childCategories) {
        this.childCategories = childCategories;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    
    public String getUsedFor() {
        return usedFor;
    }

    public void setUsedFor(String usedFor) {
        this.usedFor = usedFor;
    }

    @Transient
    public String getMATERIALS() {
        return MATERIALS;
    }

    @Transient
    public String getMATERIALS_FORMS() {
        return MATERIALS_FORMS;
    }

    @Transient
    public String getMATERIALS_PRINTEDMATERIALS() {
        return MATERIALS_PRINTEDMATERIALS;
    }

    @Transient
    public String getMATERIALS_WOLFMATERIALS() {
        return MATERIALS_WOLFMATERIALS;
    }

    @Transient
    public String getMATERIALS_LABSUPPLIES() {
        return MATERIALS_LABSUPPLIES;
    }

    @Transient
    public String getMATERIALS_CHEMICALS() {
        return MATERIALS_CHEMICALS;
    }

    @Transient
    public String getMATERIALS_LABMEDIA() {
        return MATERIALS_LABMEDIA;
    }

    @Transient
    public String getMATERIALS_GASSES() {
        return MATERIALS_GASSES;
    }

    @Transient
    public String getMATERIALS_OFFICESUPPLIES() {
        return MATERIALS_OFFICESUPPLIES;
    }

    @Transient
    public String getMATERIALS_CONTAINERS() {
        return MATERIALS_CONTAINERS;
    }

    @Transient
    public String getMATERIALS_MISCELLANEOUS() {
        return MATERIALS_MISCELLANEOUS;
    }

    @Transient
    public String getCodeAndName() {
        return getCategoryCode() + "-" + getName();
    }
}
