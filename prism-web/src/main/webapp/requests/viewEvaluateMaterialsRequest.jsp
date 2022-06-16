<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ page import="us.mn.state.health.view.inventory.Command" %>
<%@ page import="us.mn.state.health.view.materialsrequest.RequestLineItemForm" %>

<%
    int rliIndex = 0;
%>
<html>
    <head>
        <title>Evaluate Request</title>
        <script type="text/javascript">
            function popup(inVal) {
                var wpos = document.width / 2 + 100;
                var hpos = document.height / 2 + 125;
                //alert(document.top);
                var obj_calwindow = window.open("showItemDetailsPop.do?itemId=" + inVal, inVal
                        , "width=" + wpos + ",height=" + hpos + ",status=no,resizable=yes,top=200" + ",left=200,dependent=no,alwaysRaised=yes");
                obj_calwindow.opener = window;
                obj_calwindow.focus();
            }
        </script>
    </head>
    <body>
        <nested:form action="/evaluateMaterialsRequest" method="post">
            <nested:hidden property="cmd" value=""/>
            <nested:hidden property="rliFormIndex" value=""/>
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Request Information</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6">Request Number:</div>
                                <div class="col-xs-6"><nested:write property="trackingNumber"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Requestor:</div>
                                <div class="col-xs-6"><nested:write property="requestor.lastAndFirstName"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Date Requested:</div>
                                <div class="col-xs-6"><nested:write property="dateRequested" format="MM/dd/yyyy"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Need-By Date:</div>
                                <div class="col-xs-6"><nested:write property="needByDate" format="MM/dd/yyyy"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Priority:</div>
                                <div class="col-xs-6"><nested:write property="priority.name"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Additional Instructions:</div>
                                <div class="col-xs-6" id="addInstructions"><nested:write property="additionalInstructions"/></div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Deliver To</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6">Organization:</div>
                                <div class="col-xs-6">
                                    <nested:present property="deliveryDetail.organization">
                                        <nested:write property="deliveryDetail.organization.orgName" />
                                    </nested:present>
                                    <nested:notPresent property="deliveryDetail.organization">
                                        <nested:present property="deliveryDetail.facility">MDH</nested:present>
                                    </nested:notPresent>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Recipient:</div>
                                <div class="col-xs-6"><nested:write property="deliveryDetail.recipientName" /></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">Mailing Address:</div>
                                <div class="col-xs-6">
                                    <nested:present property="deliveryDetail.facility">
                                        <nested:write property="deliveryDetail.facility.facilityName" /><br />
                                    </nested:present>
                                    <nested:present property="deliveryDetail.mailingAddress">
                                        <nested:write property="deliveryDetail.mailingAddress.address1" /><br />
                                        <nested:present property="deliveryDetail.mailingAddress.address2">
                                            <nested:write property="deliveryDetail.mailingAddress.address2" /><br />
                                        </nested:present>
                                        <nested:write property="deliveryDetail.mailingAddress.city" />,&nbsp;
                                        <nested:write property="deliveryDetail.mailingAddress.state" />&nbsp;
                                        <nested:write property="deliveryDetail.mailingAddress.zip" />
                                    </nested:present>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <table class="table table-bordered table-striped">
                        <caption>Request Line Items</caption>
                        <thead>
                            <tr>
                                <th scope="col">&nbsp;</th>
                                <th scope="col">Item Description</th>
                                <th scope="col">Qty</th>
                                <th scope="col">Unit</th>
                                <th scope="col">Unit Cost</th>
                                <th scope="col">Estimated Cost*</th>
                                <th scope="col">Funding Sources</th>
                                <th scope="col">Approve/Deny</th>
                            </tr>
                        </thead>
                        <tbody>
                            <nested:iterate property="requestLineItemForms" id="requestLineItemForm" indexId="a">
                                <tr>
                                    <td>
                                        <button type="button"
                                                class="btn btn-default"
                                                onclick="toggleDetails(<nested:write property="requestLineItemId"/>)">
                                            <span class="sr-only">Toggle Details</span>
                                            <span class="glyphicon glyphicon-chevron-right detail-toggle<nested:write property="requestLineItemId"/>" aria-hidden="true"></span>
                                        </button>
                                    </td>
                                    <td>
                                        <nested:present property="item">
                                            <a href="#" onclick=popup("<nested:write property="item.itemId"/>");> <nested:write property="item.description"/></a>
                                            <nested:equal value="Stock Item" property="item.itemType">
                                                <br>(IC# - <nested:write property="item.fullIcnbr"/>)
                                            </nested:equal>
                                            <nested:notEmpty property="item.attachedFiles">
                                                <% java.util.Date dt = new java.util.Date(); %>
                                                <br/>
                                                <i class="attachment">attachment(s):
                                                    <nested:iterate property="item.attachedFiles" id="attachedFile">
                                                        &nbsp;<a href=${pageContext.request.contextPath}/inventory/downloadFileAction.do?attachedFileId=<nested:write
                                                            name="attachedFile" property='attachedFileId'/>&dt=<%=dt.getTime()%> > <nested:write
                                                            name="attachedFile" property='fileName'/></a>
                                                    </nested:iterate>
                                                </i>
                                            </nested:notEmpty>

                                        </nested:present>
                                        <nested:notPresent property="item">
                                            <nested:write property="itemDescription"/>
                                        </nested:notPresent>

                                        <nested:present property="requestLineItem">
                                            <nested:notEmpty property="requestLineItem.attachedFileNonCats">
                                                <% java.util.Date dt = new java.util.Date(); %>
                                                <br/>
                                                <i class="attachment">attachment(s):
                                                    <nested:iterate property="requestLineItem.attachedFileNonCats" id="attachedFile">
                                                        &nbsp;<a href=${pageContext.request.contextPath}/purchasing/downloadFileAction.do?attachedFileNonCatId=<nested:write
                                                            name="attachedFile" property='attachedFileNonCatId'/>&dt=<%=dt.getTime()%> > <nested:write
                                                            name="attachedFile" property='fileName'/> </a>
                                                    </nested:iterate> </i>
                                            </nested:notEmpty>
                                        </nested:present>
                                    </td>
                                    <td><nested:write property="quantity"/></td>
                                    <td>
                                        <nested:present property="item">
                                            <nested:write property="item.dispenseUnit.name"/>
                                        </nested:present>
                                        <nested:notPresent property="item">
                                            <nested:write property="unit.name"/>
                                        </nested:notPresent>
                                    </td>
                                    <td>
                                        <nested:present property="item">
                                            <nested:present property="item.dispenseUnitCost">
                                                <nested:write property="itemCost" format="#,##0.00"/>
                                            </nested:present>
                                            <nested:notPresent property="item.dispenseUnitCost">
                                                n/a
                                            </nested:notPresent>
                                        </nested:present>
                                        <nested:notPresent property="item">
                                            $<nested:write property="itemCostRND" format="#,##0.00"/>
                                        </nested:notPresent>
                                    </td>
                                    <td>
                                        <nested:present property="item">
                                            $<nested:write property="estimatedCostD" format="#,##0.00"/>
                                        </nested:present>
                                        <nested:notPresent property="item">
                                            $<nested:write property="estimatedCost" format="#,##0.00"/>
                                        </nested:notPresent>
                                    </td>
                                    <!-- funding source section -->
                                    <td style="padding: 0;">
                                        <table class="table" style="background: inherit;">
                                            <thead>
                                                <tr>
                                                    <th scope="col"><bean:message key="orgBudget"/></th>
                                                    <th scope="col">Amount</th>
                                                </tr>
                                            </thead>
                                            <tfoot>
                                                <tr>
                                                    <td colspan="2">
                                                        <% String onClickStr = "this.form.cmd.value='" + Command.ADD_FUNDING_SOURCE + "'; this.form.rliFormIndex.value='" + rliIndex + "'; this.form.submit(); return false; "; %>
                                                        <nested:equal value="true" property="atLeastOneFundingSourceEnabled">
                                                            <html:submit value="Add Another Funding Source" onclick="<%= onClickStr %>" styleClass="btn btn-default"/>
                                                        </nested:equal>
                                                        <nested:notEqual value="true" property="atLeastOneFundingSourceEnabled">
                                                            <html:submit value="Add Another Funding Source" onclick="<%= onClickStr %>" disabled="true" styleClass="btn btn-default"/>
                                                        </nested:notEqual>
                                                    </td>
                                                </tr>
                                            </tfoot>
                                            <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                                <tr>
                                                    <nested:equal property="enabled" value="true">
                                                        <td>
                                                            <nested:select property="orgBudgetId" style="width:600px" styleClass="chosen-select">
                                                                <option value=""></option>
                                                                <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                            </nested:select>
                                                        </td>
                                                        <td align="center"><nested:text property="amount" size="5"/></td>
                                                    </nested:equal>
                                                    <nested:notEqual property="enabled" value="true">
                                                        <td>
                                                            <nested:select property="orgBudgetId" style="width:600px" disabled="true" styleClass="chosen-select">
                                                                <option value=""></option>
                                                                <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                                            </nested:select>
                                                        </td>
                                                        <td>
                                                            <nested:text property="amount" size="5" disabled="true"/>
                                                        </td>
                                                    </nested:notEqual>
                                                </tr>
                                            </nested:iterate>
                                        </table>
                                    </td>
                                    <td>
                                        <logic:equal name="skin" value="PRISM2">
                                            <nested:equal property="itPurchase" value="true">
                                                On Hand:&nbsp;<nested:radio property="approved" value="2"/>
                                                <%--<br/><button onclick="this.form.action='attachQuotes.do';this.form.cmd.value='<%=Command.ADD_QUOTE%>';this.form.rliFormIndex.value=<%=rliIndex%>;this.form.submit(); return false;" class="btn btn-default">Send Back to Requestor with Quote</button>--%>
                                                <hr/>
                                            </nested:equal>
                                        </logic:equal>
                                        <label>Approve: <nested:radio property="approved" value="1"/></label>
                                        <label>Deny:&nbsp;<nested:radio property="approved" value="0"/></label>
                                        <p>
                                            <i><strong>Latest Note:</strong></i>
                                            <br/>
                                            <nested:iterate property="noteForms" id="requestLineItemNoteForm" length="1">
                                                <nested:write property="noteText"/>
                                            </nested:iterate>
                                        </p>
                                        <hr/>
                                        <i>Reason if Denied</i><br/>
                                        <nested:textarea property="rliDenialReason" cols="25" rows="2"/>
                                        <p>
                                            (More details and notes can be found by expanding this line item)
                                        </p>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8" style="padding: 0;">
                                        <div class="detail detail<nested:write property="requestLineItemId"/>">
                                            <table class="table table-bordered table-striped">
                                            <nested:notEqual property="suggestedVendorCatalogNumber" value="STAFFAUG">
                                            <thead>
                                                <tr>
                                                    <th scope="col">Vendor Name</th>
                                                    <th scope="col">Catalog #</th>
                                                    <th scope="col">URL</th>
                                                    <th scope="col">Buy Unit</th>
                                                    <th scope="col">Category</th>
                                                </tr>
                                            </thead>

                                            <tr>
                                                <td><nested:write property="suggestedVendorName"/></td>
                                                <td><nested:write property="suggestedVendorCatalogNumber"/></td>
                                                <td><nested:write property="suggestedVendorURL"/></td>
                                                <td><nested:notEmpty property="unit"><nested:write property="unit.name"/></nested:notEmpty></td>
                                                <td><nested:write property="requestLineItemCategory.name"/></td>
                                            </tr>
                                            </nested:notEqual>
                                            <nested:equal property="suggestedVendorCatalogNumber" value="MNITCONTRACT">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Type</th>
                                                    <th scope="col">PO#</th>
                                                    <th scope="col">AC2 Code</th>
                                                    <th scope="col">Notes</th>
                                                </tr>
                                                </thead>
                                            <tr>
                                                <td>Contract</td>
                                                <td>
                                                    <nested:write property="suggestedVendorName"/>
                                                </td>
                                                <td>
                                                    <nested:write property="swiftItemId"/>
                                                </td>
                                                <td>
                                                    <nested:notEmpty property="noteForms">
                                                        <nested:write property="noteForms[0].noteText"/>
                                                    </nested:notEmpty>
                                                </td>
                                            </tr>
                                            </nested:equal>
                                                <nested:equal property="suggestedVendorCatalogNumber" value="WAN/Computing Services">
                                                    <thead>
                                                    <tr>
                                                        <th scope="col">Type</th>
                                                        <th scope="col">PO#</th>
                                                        <th scope="col">AC2 Code/Account#</th>
                                                        <th scope="col">Notes</th>
                                                    </tr>
                                                    </thead>
                                                    <tr>
                                                        <td>WAN/Computing Services</td>
                                                        <td>
                                                            <nested:write property="suggestedVendorName"/>
                                                        </td>
                                                        <td>
                                                            <nested:write property="swiftItemId"/>
                                                        </td>
                                                        <td>
                                                            <nested:notEmpty property="noteForms">
                                                                <nested:write property="noteForms[0].noteText"/>
                                                            </nested:notEmpty>
                                                        </td>
                                                    </tr>
                                                </nested:equal>
                                            <nested:equal property="suggestedVendorCatalogNumber" value="STAFFAUG">
                                                <thead>
                                                <tr>
                                                    <th scope="col">Type</th>
                                                    <th scope="col">PO#</th>
                                                    <th scope="col">AC2 Code</th>
                                                    <th scope="col">Notes</th>
                                                </tr>
                                                </thead>
                                                <tr>
                                                    <td>Staffing</td>
                                                    <td>
                                                        <nested:write property="suggestedVendorName"/>
                                                    </td>
                                                    <td>
                                                        <nested:write property="swiftItemId"/>
                                                    </td>
                                                    <td>
                                                        <nested:notEmpty property="noteForms">
                                                            <nested:write property="noteForms[0].noteText"/>
                                                        </nested:notEmpty>
                                                    </td>
                                                </tr>
                                            </nested:equal>

                                            <nested:present property="itemJustification">
                                                <tr>
                                                    <th scope="row">Justification</th>
                                                    <td colspan="7"><nested:write property="itemJustification"/></td>
                                                </tr>
                                            </nested:present>
                                            <tr>
                                                <td colspan="5" style="padding: 0;">
                                                    <table class="table table-bordered table-striped">
                                                        <thead>
                                                            <tr>
                                                                <th scope="col">Note</th>
                                                                <th scope="col">Author</th>
                                                                <th scope="col">Date</th>
                                                            </tr>
                                                        </thead>
                                                        <nested:iterate property="noteForms" id="requestLineItemNoteForm">
                                                            <tr>
                                                                <td><nested:write property="noteText"/></td>
                                                                <td><nested:write property="authorName"/></td>
                                                                <td><nested:write property="insertionDate" format="MM/dd/yyyy"/></td>
                                                            </tr>
                                                        </nested:iterate>

                                                        <tr>
                                                            <td><textarea name="rliNote" cols="30" rows="2"></textarea></td>
                                                            <td>
                                                                <% String addNoteOnClick = "this.form.cmd.value='" + Command.ADD_NOTE + "'; this.form.rliFormIndex.value='" + rliIndex + "'; this.form.submit(); return false; "; %>
                                                                <html:submit value="Add Note" onclick="<%=addNoteOnClick%>" styleClass="btn btn-default"/>
                                                            </td>
                                                            <td>&nbsp;</td>
                                                        </tr>
                                                    </table>
                                                </td>
                                            </tr>
                                        </table>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="8">
                                        Status to place order in:
                                        <label>Waiting For Purchase:&nbsp;<nested:radio property="endRequestStatus" value="WFP"/></label>
                                        <label>Standing Lab Order:&nbsp;<nested:radio property="endRequestStatus" value="SLO"/></label>
                                        <label>MNIT Order:&nbsp;<nested:radio property="endRequestStatus" value="CPO"/></label>
                                    </td>
                                </tr>
                                <% ++rliIndex; %>
                            </nested:iterate>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12 text-center">
                    <p>* - Estimated Cost shown does not include potential shipping or other charges.</p>
                </div>
            </div>
            <br/>
            <div class="row">
                <div class="col-md-12 text-center">
                    <html:submit value="Submit" styleClass="btn btn-default"/>
                    <button onclick="this.form.action='viewMaterialsRequests.do';this.form.submit(); return false;" class="btn btn-default">Cancel</button>
                </div>
            </div>
        </nested:form>
    </body>
</html>