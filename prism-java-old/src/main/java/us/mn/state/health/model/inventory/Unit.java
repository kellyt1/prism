package us.mn.state.health.model.inventory;

import java.io.Serializable;

public class Unit implements Serializable, Comparable {
    private String name;
    private String code;
    private Long unitId;

    public static final String CODE_UNKNOWN = "UNKWN";
    public static final String CODE_EACH = "EACH";
    public static final String CODE_PACK = "PACK";
    public static final String CODE_BOTTLE = "BTTL";
    public static final String CODE_BUNDLE = "BNDL";
    public static final String CARTON_BUNDLE = "CRTN";
    public static final String CODE_PAD = "PAD";
    public static final String CODE_SHEET = "SHET";
    public static final String CODE_CASE = "CASE";
    public static final String CODE_DOZEN = "DOZN";
    public static final String CODE_BOX = "BOX";
    public static final String CODE_LOT = "LOT";
    public static final String CODE_REAM = "REAM";
    public static final String CODE_SET = "SET";


    public Unit() {
    }

    public Unit(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public int compareTo(Object o) {
        if (o instanceof Unit) {
            return this.getName().compareTo(((Unit) o).getName());
        }
        return 0;
    }


    public String toString() {
        return "Unit{" +
                "name='" + name + "'" +
                ", code='" + code + "'" +
                ", unitId=" + unitId +
                "}";
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unit unit = (Unit) o;

        if (!code.equals(unit.code)) return false;
        if (name != null ? !name.equals(unit.name) : unit.name != null) return false;
        if (unitId != null ? !unitId.equals(unit.unitId) : unit.unitId != null) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (name != null ? name.hashCode() : 0);
        result = 31 * result + code.hashCode();
        result = 31 * result + (unitId != null ? unitId.hashCode() : 0);
        return result;
    }
}
