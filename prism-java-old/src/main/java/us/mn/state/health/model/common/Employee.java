package us.mn.state.health.model.common;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import us.mn.state.health.common.util.CollectionUtils;

public class Employee implements Serializable {
    private EmployeeQuickTblId id;
    private Long personId;
    private String lastName;
    private String firstName;
    private String midName;
    private String classCode;
    private String positionControlNbr;
    private String workingTitle;
    private Long orgId;
    private String deptIdCode;
    private String deptIdName;
    private String phoneNbr;
    private String voipNbr;
    private String cellNbr;
    private String pagerNbr;
    private String email;
    private String supName;
    private String supPositionNbr;
    private Long supPositionId;
    private Long supPersonId;
    private String supEmpId;
    private String division;
    private String orgType;
    private String orgCode;
    private Long personIdIncharge;
    private String firstNameIncharge;
    private String lastNameIncharge;
    private String classificationTitle;
    private String moreContactInfo;

    public Long getPersonId() {
//        return id.getPersonId();
        return personId;
    }

//    public void setPersonId(Long personId) {
//        this.personId = id.setPersonId(personId);
//        this.getId().setPersonId(personId);
//    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }


// Constructors

   /** default constructor */
   public Employee() {
   }

   /** minimal constructor */
   public Employee(EmployeeQuickTblId id) {
       this.id = id;
   }
   /** full constructor */
   public Employee(EmployeeQuickTblId id, String lastName, String firstName, String midName, String classCode, String positionControlNbr, String workingTitle, Long orgId, String deptIdCode, String deptIdName, String phoneNbr, String voipNbr, String cellNbr, String pagerNbr, String email, String supName, String supPositionNbr, Long supPositionId, Long supPersonId, String supEmpId, String division, String orgType, String orgCode, Long personIdIncharge, String firstNameIncharge, String lastNameIncharge, String classificationTitle, String moreContactInfo) {
      this.id = id;
      this.personId = id.getPersonId();
      this.lastName = lastName;
      this.firstName = firstName;
      this.midName = midName;
      this.classCode = classCode;
      this.positionControlNbr = positionControlNbr;
      this.workingTitle = workingTitle;
      this.orgId = orgId;
      this.deptIdCode = deptIdCode;
      this.deptIdName = deptIdName;
      this.phoneNbr = phoneNbr;
      this.voipNbr = voipNbr;
      this.cellNbr = cellNbr;
      this.pagerNbr = pagerNbr;
      this.email = email;
      this.supName = supName;
      this.supPositionNbr = supPositionNbr;
      this.supPositionId = supPositionId;
      this.supPersonId = supPersonId;
      this.supEmpId = supEmpId;
      this.division = division;
      this.orgType = orgType;
      this.orgCode = orgCode;
      this.personIdIncharge = personIdIncharge;
      this.firstNameIncharge = firstNameIncharge;
      this.lastNameIncharge = lastNameIncharge;
      this.classificationTitle = classificationTitle;
      this.moreContactInfo = moreContactInfo;
   }

   // Property accessors
   public EmployeeQuickTblId getId() {
       return this.id;
   }

   public void setId(EmployeeQuickTblId id) {
       this.id = id;
   }
   public String getLastName() {
       return this.lastName;
   }

   public void setLastName(String lastName) {
       this.lastName = lastName;
   }
   public String getFirstName() {
       return this.firstName;
   }

   public void setFirstName(String firstName) {
       this.firstName = firstName;
   }
   public String getMidName() {
       return this.midName;
   }

   public void setMidName(String midName) {
       this.midName = midName;
   }
   public String getClassCode() {
       return this.classCode;
   }

   public void setClassCode(String classCode) {
       this.classCode = classCode;
   }
   public String getPositionControlNbr() {
       return this.positionControlNbr;
   }

   public void setPositionControlNbr(String positionControlNbr) {
       this.positionControlNbr = positionControlNbr;
   }
   public String getWorkingTitle() {
       return this.workingTitle;
   }

   public void setWorkingTitle(String workingTitle) {
       this.workingTitle = workingTitle;
   }
   public Long getOrgId() {
       return this.orgId;
   }

   public void setOrgId(Long orgId) {
       this.orgId = orgId;
   }
   public String getDeptIdCode() {
       return this.deptIdCode;
   }

   public void setDeptIdCode(String deptIdCode) {
       this.deptIdCode = deptIdCode;
   }
   public String getDeptIdName() {
       return this.deptIdName;
   }

   public void setDeptIdName(String deptIdName) {
       this.deptIdName = deptIdName;
   }
   public String getPhoneNbr() {
       return this.phoneNbr;
   }

   public void setPhoneNbr(String phoneNbr) {
       this.phoneNbr = phoneNbr;
   }
   public String getVoipNbr() {
       return this.voipNbr;
   }

   public void setVoipNbr(String voipNbr) {
       this.voipNbr = voipNbr;
   }
   public String getCellNbr() {
       return this.cellNbr;
   }

   public void setCellNbr(String cellNbr) {
       this.cellNbr = cellNbr;
   }
   public String getPagerNbr() {
       return this.pagerNbr;
   }

   public void setPagerNbr(String pagerNbr) {
       this.pagerNbr = pagerNbr;
   }
   public String getEmail() {
       return this.email;
   }

   public void setEmail(String email) {
       this.email = email;
   }
   public String getSupName() {
       return this.supName;
   }

   public void setSupName(String supName) {
       this.supName = supName;
   }
   public String getSupPositionNbr() {
       return this.supPositionNbr;
   }

   public void setSupPositionNbr(String supPositionNbr) {
       this.supPositionNbr = supPositionNbr;
   }
   public Long getSupPositionId() {
       return this.supPositionId;
   }

   public void setSupPositionId(Long supPositionId) {
       this.supPositionId = supPositionId;
   }
   public Long getSupPersonId() {
       return this.supPersonId;
   }

   public void setSupPersonId(Long supPersonId) {
       this.supPersonId = supPersonId;
   }
   public String getSupEmpId() {
       return this.supEmpId;
   }

   public void setSupEmpId(String supEmpId) {
       this.supEmpId = supEmpId;
   }
   public String getDivision() {
       return this.division;
   }

   public void setDivision(String division) {
       this.division = division;
   }
   public String getOrgType() {
       return this.orgType;
   }

   public void setOrgType(String orgType) {
       this.orgType = orgType;
   }
   public String getOrgCode() {
       return this.orgCode;
   }

   public void setOrgCode(String orgCode) {
       this.orgCode = orgCode;
   }
   public Long getPersonIdIncharge() {
       return this.personIdIncharge;
   }

   public void setPersonIdIncharge(Long personIdIncharge) {
       this.personIdIncharge = personIdIncharge;
   }
   public String getFirstNameIncharge() {
       return this.firstNameIncharge;
   }

   public void setFirstNameIncharge(String firstNameIncharge) {
       this.firstNameIncharge = firstNameIncharge;
   }
   public String getLastNameIncharge() {
       return this.lastNameIncharge;
   }

   public void setLastNameIncharge(String lastNameIncharge) {
       this.lastNameIncharge = lastNameIncharge;
   }
   public String getClassificationTitle() {
       return this.classificationTitle;
   }

   public void setClassificationTitle(String classificationTitle) {
       this.classificationTitle = classificationTitle;
   }
   public String getMoreContactInfo() {
       return this.moreContactInfo;
   }

   public void setMoreContactInfo(String moreContactInfo) {
       this.moreContactInfo = moreContactInfo;
   }

}


