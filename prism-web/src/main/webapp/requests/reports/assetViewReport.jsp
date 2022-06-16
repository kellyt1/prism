<%@ include file="/./include/tlds.jsp"%>

<jsp:useBean id="assetsViewReportForm"
             class="us.mn.state.health.view.purchasing.AssetsViewReportForm"
             scope="session"/>
<html>
    <head>
        <title>Assets Since Date Report</title>
    </head>
    <body>
    <html:form action="/displayAssetsViewReport">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-offset-5 col-md-2 form-group">
                    <label>From Order Date:</label>
                    <nested:text property="orderDate" size="10" maxlength="10" styleClass="dateInput form-control margin-bottom"/>
                </div>
            </div>
            <div class="row">
                <div class="col-md-offset-5 col-md-2 btn-group text-center">
                    <html:submit styleClass="btn btn-primary margin-bottom"/>
                    <input class="btn btn-default margin-bottom" type="button" onclick="this.form.action = '/readyAssetsViewReport.do';this.form.submit(); return false;" value="Cancel"/>
                </div>
            </div>
        </div>
    </html:form>
    <nested:notEmpty name="assetsViewReportForm" property="results">
        <ajax:displayTag id="displayTagFrame" ajaxFlag="displayAjax" pagelinksClass="pagelinks">
            <% request.setAttribute("results", assetsViewReportForm.getResults());%>
            <display:table name="results" class="displaytag table table-striped" export="true" id="row" excludedParams="ajax" pagesize="10">
                <display:column title="Order Line Item ID" property="orderLineItemId"/>
                <display:column title="PO" property="mdhPo"/>
                <display:column title="Asset Number" property="assetNumber"/>
                <display:column title="MRQ" property="mrq"/>
                <display:column title="Description" property="assetDescription"/>
                <display:column title="Type" property="assetsType"/>
                <display:column title="Cost" property="cost"/>
                <display:column title="Order Date" property="orderDate"/>
                <display:column title="Funding" property="fundingStreams"/>
                <display:column title="Quantity" property="quantity"/>
                <display:column title="Deliver To" property="deliverToName"/>
                <display:column title="Deliver Address" property="deliverToAddress"/>
                <display:column title="Updated By" property="updatedBy"/>
                <display:setProperty name="export.pdf" value="false"/>
                <display:setProperty name="export.rtf" value="false"/>
                <display:setProperty name="export.pdf.filename" value="example.pdf"/>
                <display:setProperty name="export.excel.filename" value="example.xls"/>
                <display:setProperty name="export.rtf.filename" value="example.rtf"/>
                <display:setProperty name="export.xml" value="false"/>
                <display:setProperty name="export.csv" value="false"/>
            </display:table>
        </ajax:displayTag>
    </nested:notEmpty>
    <nested:empty name="assetsViewReportForm" property="results">
        <h4>0 items found.</h4>
    </nested:empty>

    <script>
        jQuery(document).ready(function () {
            jQuery('#displayTagFrame').on("DOMSubtreeModified", function () {
                jQuery('.pagelinks').on('click', function () {
                    jQuery('[role="tooltip"]').each(function () {
                        jQuery(this).remove()
                    })
                })
            })
        })
    </script>
    </body>
</html>
