package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import us.mn.state.health.common.util.CollectionUtils;

public class EmployeeQuickTblId implements Serializable {


    // Fields

     private long personId;
     private long positionId;
     private String empId;

     // Constructors

    /** default constructor */
    public EmployeeQuickTblId() {
    }

    /** full constructor */
    public EmployeeQuickTblId(long personId, long positionId, String empId) {
       this.personId = personId;
       this.positionId = positionId;
       this.empId = empId;
    }

    // Property accessors
    public long getPersonId() {
        return this.personId;
    }

    public void setPersonId(long personId) {
        this.personId = personId;
    }
    public long getPositionId() {
        return this.positionId;
    }

    public void setPositionId(long positionId) {
        this.positionId = positionId;
    }
    public String getEmpId() {
        return this.empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof EmployeeQuickTblId) ) return false;
		 EmployeeQuickTblId castOther = ( EmployeeQuickTblId ) other;

		 return (this.getPersonId()==castOther.getPersonId())
 && (this.getPositionId()==castOther.getPositionId())
 && ( (this.getEmpId()==castOther.getEmpId()) || ( this.getEmpId()!=null && castOther.getEmpId()!=null && this.getEmpId().equals(castOther.getEmpId()) ) );
   }

   public int hashCode() {
         int result = 17;

         result = 37 * result + (int) this.getPersonId();
         result = 37 * result + (int) this.getPositionId();
         result = 37 * result + ( getEmpId() == null ? 0 : this.getEmpId().hashCode() );
         return result;
   }


}


