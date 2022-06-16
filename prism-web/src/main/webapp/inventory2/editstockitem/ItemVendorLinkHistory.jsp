<%@ include file="../../include/tlds.jsp" %>
<html>
    <head><title>History for xxx-xxxx</title></head>
    <body>
        <display:table export="false" name="requestScope.itemVendorLinkHistory" class="table table-bordered table-striped" id="parent">
            <display:column title="">
                <c:out value="${parent_rowNum}"/>
            </display:column>
            <display:column title="ICNBR" property="stockItem.icnbr" maxLength="20"/>
            <display:column title="Vendor" property="vendor.externalOrgDetail.orgName" maxLength="50"/>
            <display:column title="Vendor Contract" property="vendorContract.contractNumber" maxLength="100"/>
            <display:column title="Buy Unit" property="buyUnit.name" maxLength="20"/>
            <display:column title="Vendor Catalog Nbr" property="vendorCatalogNbr" maxLength="20"/>
            <display:column title="Buy Unit Cost" property="buyUnitCostHistoryAsString" maxLength="20"/>
            <display:column title="Primary Vendor" property="primaryVendor" maxLength="20"/>
            <display:column title="Discount" property="discountHistoryAsString" maxLength="20"/>
            <display:column title="Dispense Units/Buy Unit" property="dispenseUnitsPerBuyUnitHistoryAsString" maxLength="20"/>
            <display:column title="Updated By" property="updatedBy" maxLength="20"/>
            <display:column title="Updated Date" property="updateDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
            <display:column title="Create Date" property="createDate" maxLength="10" format="{0,date,MM/dd/yyyy}"/>
        </display:table>
    </body>
</html>