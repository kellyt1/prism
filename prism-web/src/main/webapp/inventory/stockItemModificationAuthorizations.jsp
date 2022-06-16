<%@ taglib uri="/WEB-INF/tld/securitytags.tld" prefix="security" %>

    <%String icnbrFlag = "false";%>
    <%String qtyOnHandFlag = "false";%>
    <%String estimatedAnnualUsageFlag = "false";%>
    <%String dispenseUnitFlag = "false";%>
    <%String vendorsFlag = "false";%>
    <%String EOQFlag = "false";%>    
    <%String hazardousFlag = "false";%>
    <%String descriptionFlag = "false";%>
    <%String modelFlag = "false";%>
    <%String manufacturerFlag = "false";%>
    <%String objectCodeFlag = "false";%>
    <%String categoryFlag = "false";%>
    <%String handlingInstructionsFlag = "false";%>
    <%String asstDivDirFlag = "false";%>
    <%String printSpecFileFlag = "false";%>
    <%String orgBudgetsFlag = "false";%>
    <%String statusFlag = "false";%>
    <%String seasonalFlag = "false";%>
    <%String primaryContactFlag = "false";%>
    <%String secondaryContactFlag = "false";%>
    <%String holdUntilDateFlag = "false";%>
    <%String ROPFlag = "false";%>
    <%String ROQFlag = "false";%>    
    <%String cycleCountPriorityFlag = "false";%> 
    <%String fillUntilDepletedFlag = "false";%>
    <%String locationsFlag = "false";%>
    <%String reorderDateFlag = "false";%>
    
    <security:authorize groupCode="STOCK_PICKER">
        <%
            locationsFlag = "true";
        %>
    </security:authorize>
    
    <security:authorize groupCode="STOCK_CONTROLLER">
        <%
            descriptionFlag = "true";
            ROPFlag = "true";
            ROQFlag = "true";
            dispenseUnitFlag = "true"; 
            icnbrFlag = "true";
            qtyOnHandFlag = "true";  
            estimatedAnnualUsageFlag = "true";
            dispenseUnitFlag = "true";
            vendorsFlag = "true";
            EOQFlag = "true";
            hazardousFlag = "true";
            descriptionFlag = "true";
            modelFlag = "true";
            manufacturerFlag = "true";
            objectCodeFlag = "true";
            categoryFlag = "true";
            handlingInstructionsFlag = "true";
            asstDivDirFlag = "true";
            printSpecFileFlag = "false";
            orgBudgetsFlag = "true";
            statusFlag = "true";
            seasonalFlag = "true";
            primaryContactFlag = "true";
            secondaryContactFlag = "true";
            holdUntilDateFlag = "true";
            ROPFlag = "true";
            ROQFlag = "true";
            cycleCountPriorityFlag = "true";
            fillUntilDepletedFlag = "true";
            locationsFlag = "true";
            reorderDateFlag = "true";
        %>
    </security:authorize>
    
    <security:authorize groupCode="BUYER">
        <%
            descriptionFlag = "true";
            ROPFlag = "true";
            ROQFlag = "true";
            dispenseUnitFlag = "true"; 
            icnbrFlag = "true";
            qtyOnHandFlag = "true";   
            estimatedAnnualUsageFlag = "true";
            dispenseUnitFlag = "true";
            vendorsFlag = "true";
            EOQFlag = "true";
            hazardousFlag = "true";
            descriptionFlag = "true";
            modelFlag = "true";
            manufacturerFlag = "true";
            objectCodeFlag = "true";
            categoryFlag = "true";
            handlingInstructionsFlag = "true";
            asstDivDirFlag = "true";
            printSpecFileFlag = "true";
            orgBudgetsFlag = "true";
            statusFlag = "true";
            seasonalFlag = "true";
            primaryContactFlag = "true";
            secondaryContactFlag = "true";
            holdUntilDateFlag = "true";
            ROPFlag = "true";
            ROQFlag = "true";
            cycleCountPriorityFlag = "true";
            fillUntilDepletedFlag = "true";
            locationsFlag = "true";
            reorderDateFlag = "true";
        %>
    </security:authorize>