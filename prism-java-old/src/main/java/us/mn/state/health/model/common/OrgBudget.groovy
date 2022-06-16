package us.mn.state.health.model.common;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.dao.DAOFactory;
import us.mn.state.health.matmgmt.util.Constants;
import javax.persistence.Transient;


public class OrgBudget implements Serializable, Comparable {
    private Long orgBudgetId;
    private String name;
    private String orgBudgetCodeDisplay;
    private String orgBudgetCode;
    private String budgetAccountCode;
    private String reportCategory;
    private String appropriationCode;
    private String fundCode;
    private String provisions;
    private String comments;
    private Date effectiveDate;
    private String programCode;
    private Date insertionDate;
    private String insertedBy;
    private String realOrgIndicator;
    private Date endDate;
    private Date fedEndDate;
    private Group managerGroup;              //the group that includes people who are managers of this budget
    private int version;
    private Collection rptCategories = new ArrayList();
    private Collection notificationEmailBudgetLinks = new HashSet()

    public static String UKNOWN = "UNKN";
    public static String INDIRECT = "1305";     //the code for the 'indirect' org budget... subject to change, we'll refactor later

    private String deptId;
    private String projectId;
    private String sourceType;

    public OrgBudget() {
    }

    public int compareTo(Object o) {
        if (o instanceof OrgBudget)
            return this.orgBudgetCode.compareTo(((OrgBudget) o).getOrgBudgetCode());
        return 0;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrgBudget)) {
            return false;
        }
        final OrgBudget orgBdgt = (OrgBudget) o;
        if (getOrgBudgetId() != null ? !getOrgBudgetId().equals(orgBdgt.getOrgBudgetId()) : orgBdgt.getOrgBudgetId() != null)
        {
            return false;
        }

        return true;
    }

    @Transient
    public String getOrgBudgetCodeAndName() {
        return "${orgBudgetCodeDisplay ?: orgBudgetCode} ${name}"
    }

    private static String getFiscalYear(Date endDate) {
        if (endDate){
            int yy = endDate?.getAt(Calendar.YEAR)
            if (endDate.getAt(Calendar.MONTH) >= 6) ++yy

            return "FY" + ("" + yy).substring(2)
        }else{
            return ""
        }
    }

    @Transient
    public String getOrgBudgetCodeAndNameAndFY() {
        return "${deptId} ${(orgBudgetCodeDisplay || orgBudgetCodeDisplay.equals("N/A")) ? "(${orgBudgetCodeDisplay})" : ""}-${getFiscalYear(endDate)}-${name} - Ending:${fedEndDate?:endDate}"
    }

    @Transient
    public String getOrgBudgetCodeAndFY() {
        return "${deptId} ${orgBudgetCodeDisplay ? "(${orgBudgetCodeDisplay})" : ""}-${getFiscalYear(endDate)}-${name}"
    }

    public Group getManagerGroup() {
        return managerGroup;
    }

    public void setManagerGroup(Group managerGroup) {
        this.managerGroup = managerGroup;
    }

    public String getProvisions() {
        return provisions;
    }

    public void setProvisions(String provisions) {
        this.provisions = provisions;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getReportCategory() {
        return reportCategory;
    }

    public void setReportCategory(String reportCategory) {
        this.reportCategory = reportCategory;
    }

    public String getAppropriationCode() {
        return appropriationCode;
    }

    public void setAppropriationCode(String appropriationCode) {
        this.appropriationCode = appropriationCode;
    }

    public String getOrgBudgetCode() {
        return orgBudgetCode;
    }

    public void setOrgBudgetCode(String orgBudgetCode) {
        this.orgBudgetCode = orgBudgetCode;
    }

    public String getOrgBudgetCodeDisplay() {
        return orgBudgetCodeDisplay;
    }

    public void setOrgBudgetCodeDisplay(String orgBudgetCodeDisplay) {
        this.orgBudgetCodeDisplay = orgBudgetCodeDisplay;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public Date getInsertionDate() {
        return insertionDate;
    }

    public void setInsertionDate(Date insertionDate) {
        this.insertionDate = insertionDate;
    }

    public String getInsertedBy() {
        return insertedBy;
    }

    public void setInsertedBy(String insertedBy) {
        this.insertedBy = insertedBy;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    Date getFedEndDate() {
        return fedEndDate
    }

    void setFedEndDate(Date fedEndDate) {
        this.fedEndDate = fedEndDate
    }

    public String getBudgetAccountCode() {
        return budgetAccountCode;
    }

    public void setBudgetAccountCode(String budgetAccountCode) {
        this.budgetAccountCode = budgetAccountCode;
    }

    public String getProgramCode() {
        return programCode;
    }

    public void setProgramCode(String programCode) {
        this.programCode = programCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrgBudgetId() {
        return orgBudgetId;
    }

    public void setOrgBudgetId(Long orgBudgetId) {
        this.orgBudgetId = orgBudgetId;
    }


    public void setVersion(int version) {
        this.version = version;
    }


    public int getVersion() {
        return version;
    }

    public String getRealOrgIndicator() {
        return realOrgIndicator;
    }

    public void setRealOrgIndicator(String realOrgIndicator) {
        this.realOrgIndicator = realOrgIndicator;
    }

    @Transient
    public Collection getRptCategories() {
       Collection lRptCat = new ArrayList();
       try {
          lRptCat = rptCategories;

       } catch (Exception ignore) {
           lRptCat = new ArrayList();
       }
        return lRptCat;
    }

    public void setRptCategories(Collection rptCategories) {
        this.rptCategories = rptCategories;
    }

    @Transient
    public String getRptCategoriesList() {
        String reportCategories = "";
        String separator = "";
        if (appropriationCode != null) {
            try {
                List<ReportCategory>  rptCats = null;
                //Federal Reporting Category is tied to the Appropriation code unless the
                //Appropriation code is 202 in which case the Reporting Category is tied
                //to the Org Budget Code.
                if ((appropriationCode.equals(Constants.FEDERAL_REPORTING_CATEGORY_SPECIAL_APPROPRIATION_CODE))) {
                    rptCats = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getOrgBudgetDAO().findRptCategoriesByOrg(orgBudgetCode);
                    for (ReportCategory rptCat : rptCats) {
    //                    reportCategories += separator + rptCat.getRepCatApprCode() ;
                        reportCategories += separator + rptCat.getRepCatCode() + " " + rptCat.getRepCatBeginDate() + "-" + rptCat.getRepCatEndDate();
                        separator = "\n";
                    }
                }
                else {
                    rptCats = DAOFactory.getDAOFactory(DAOFactory.DEFAULT).getOrgBudgetDAO().findRptCategoriesByAppropriationCode(appropriationCode, orgBudgetCode);
                    for (ReportCategory rptCat : rptCats) {
                        reportCategories += separator + rptCat.getRepCatCode() + " " + rptCat.getRepCatBeginDate() + "-" + rptCat.getRepCatEndDate();
                        separator = "\n";
                    }
                }

            } catch (InfrastructureException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return reportCategories;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    Collection getNotificationEmailBudgetLinks() {
        return notificationEmailBudgetLinks
    }

    void setNotificationEmailBudgetLinks(Collection notificationEmailBudgetLinks) {
        this.notificationEmailBudgetLinks = notificationEmailBudgetLinks
    }

    public String toString() {
        return "OrgBudget{" +
                "orgBudgetId=" + orgBudgetId +
                ", name='" + name + "'" +
                ", orgBudgetCode='" + orgBudgetCode + "'" +
                ", budgetAccountCode='" + budgetAccountCode + "'" +
                ", reportCategory='" + reportCategory + "'" +
                ", appropriationCode='" + appropriationCode + "'" +
                ", fundCode='" + fundCode + "'" +
                ", effectiveDate=" + effectiveDate +
                ", programCode='" + programCode + "'" +
                ", endDate=" + endDate +
                ", fedEndDate=" + fedEndDate +
                "}";
    }

    @Transient
    public String getFundingString(){
        return "${fundCode}/${deptId}/${appropriationCode}/${projectId}/${orgBudgetCode}/${sourceType}-${getFiscalYear(endDate)}"
    }
}
