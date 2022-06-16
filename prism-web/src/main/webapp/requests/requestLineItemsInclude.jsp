<%@ include file="../include/tlds.jsp" %>

<script type="text/javascript">
    function popup(inVal) {
        var wpos = document.width / 2 + 100;
        var hpos = document.height / 2 + 125;
        var obj_calwindow = window.open("showItemDetailsPop.do?itemId=" + inVal, inVal
                , "width=" + wpos + ",height=" + hpos + ",status=no,resizable=yes,scrollbars=yes,top=200" + ",left=200,dependent=no,alwaysRaised=yes");
        obj_calwindow.opener = window;
        obj_calwindow.focus();
    }
</script>


<table class="table table-bordered table-striped">
    <thead>
        <tr>
            <th scope="col">Item Description</th>
            <th scope="col" style="width: 100px;">Qty</th>
            <th scope="col" style="width: 100px;">Unit</th>
            <th scope="col" style="width: 150px;">Unit Cost</th>
            <th scope="col" style="width: 150px;">Estimated Cost</th>
            <th scope="col">Funding Source</th>
            <th scope="col"></th>
            <th scope="col">Remove</th>
        </tr>
    </thead>
    <tbody>
        <nested:iterate property="requestLineItemForms" id="requestLineItemForm" indexId="a">
            <tr>
                <td>
                    <nested:present property="item">
                        <a href="#" onclick=popup("<nested:write property="item.itemId"/>");><nested:write property="item.description" filter="false"/></a>
                        <nested:equal value="Stock Item" property="item.itemType">
                            <br>(IC# - <nested:write property="item.fullIcnbr"/>)
                        </nested:equal>
                    </nested:present>
                    <nested:notPresent property="item">
                        <nested:textarea property="itemDescription" rows="5" style="width: 100%;"/>
                    </nested:notPresent>
                    <hr style="margin: 5px 0;">
                    <label for="addFile">Attach Files
                        <c:choose>
                        <c:when test="${requestLineItemForm.suggestedVendorCatalogNumber eq 'MNITCONTRACT'}">
                        <font size="-1" color="red"> -  Attach the encumbrance worksheet before submitting request</font>
                        </c:when>
                        </c:choose>
                        <br/>
                    <font size="1">Selecting will clear any existing attachments</font></label><br/>
                    <nested:file property="purchasingInfoFile" size="20" onchange="this.form.cmd.value='saveCart'; this.form.submit(); return false;" styleId="addFile">

                    </nested:file>
                    <nested:notEmpty property="requestLineItem.attachedFileNonCats">
                        <hr style="margin: 5px 0;">
                        <i class="attachment"><strong>Attachment:</strong>
                            <nested:iterate property="requestLineItem.attachedFileNonCats" id="attachedFile">
                                <nested:write name="attachedFile" property='fileName'/> <br/>
                            </nested:iterate></i>
                    </nested:notEmpty>
                    <br/>Add additional files<nested:file property="purchasingInfoFileAlternate" size="20" onchange="this.form.cmd.value='saveCart'; this.form.submit(); return false;" styleId="addFile"/>
                    <logic:equal name="requestType" value="SWIFT">
                        <br/>
                        <label>SWIFT Item ID or AC2: <nested:text property="swiftItemId"/></label>
                    </logic:equal>
                </td>
                <td colspan="4">
                    <table style="width: 100%;">
                        <tr>
                            <td style="width: 20%;"><nested:text property="quantity" size="4" onblur="$j(this).updateAmount()" styleClass="blurOnLoad"/></td>
                            <td style="width: 20%;">
                                <nested:present property="item">
                                    <nested:present parameter="item.dispenseUnit">
                                        <nested:write property="item.dispenseUnit.name"/>
                                    </nested:present>
                                </nested:present>
                                <nested:notPresent property="item">
                                    <nested:notEqual property="suggestedVendorCatalogNumber"  value="STAFFAUG" >
                                        <nested:select property="unitId" styleClass="chosen-select">
                                            <option value="">&nbsp;</option>
                                            <nested:optionsCollection property="units" label="name" value="unitId"/>
                                        </nested:select>
                                    </nested:notEqual>
                                </nested:notPresent>
                            </td>
                            <td style="width: 30%; white-space: nowrap;">
                                <nested:present property="item">
                                    <nested:present parameter="item.dispenseUnitCost">
                                        <%--$<nested:write property="item.dispenseUnitCost" format="#,##0.00" />--%>
                                        $<nested:text property="item.dispenseUnitCost" disabled="true" size="5" />
                                    </nested:present>
                                    <nested:notPresent property="item.dispenseUnitCost">n/a</nested:notPresent>
                                </nested:present>
                                <nested:notPresent property="item">
                                    $<nested:text property="itemCost" size="5" onblur="$j(this).updateAmount()" />
                                </nested:notPresent>
                            </td>
                            <td style="width: 30%; white-space: nowrap;">
                                <%--$<nested:write property="estimatedCost" format="#,##0.00"/>--%>
                                $<nested:text property="estimatedCost" size="5" disabled="true" />
                            </td>
                        </tr>
                    <nested:notPresent property="item">
                        <tr>
                            <td colspan="4"><br/></td>
                        </tr>
                        <tr>
                            <td colspan="4">
                                <logic:notEqual name="skin" value="PRISM2">
                                    <label>
                                        Category:&nbsp;
                                        <nested:select property="categoryId" styleClass="chosen-select">
                                            <option value=""></option>
                                            <nested:optionsCollection property="categories" label="name" value="categoryId"/>
                                        </nested:select>
                                    </label>
                                </logic:notEqual>
                                <logic:equal name="skin" value="PRISM2">
                                    <nested:hidden property="categoryId"/>
                                </logic:equal>
                            </td>
                        </tr>
                    </nested:notPresent>
                </table>
            </td>
            <!-- funding source section -->
                <c:choose>
                    <c:when test="${requestLineItemForm.item.itemType eq 'Stock Item' && requestLineItemForm.stockItem.orgBudget.orgBudgetCode eq indirect}">
                        <!-- this is a stock item funded by indirect, so do not collect the funding source information -->
                        <td class="text-center">Not Applicable - Funded by Indirect</td>
                    </c:when>
                    <c:otherwise>
                        <!-- this is either a purchase item or a stock item NOT funded by indirect, so we WILL collect the
                        funding source information -->
                        <td style="padding: 0;">
                            <table id="fundingSourcesTable_NonCatItem" class="table" style="background: inherit;">
                                <tr>
                                    <th scope="col" style="padding-right: 25px;"><label><bean:message key="orgBudget"/>:</label></th>
                                    <th scope="col" class="text-center"><label>Amount:</label></th>
                                </tr>
                                <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm">
                                    <tr>
                                        <td>
                                            <nested:select property="orgBudgetId" style="width:550px" styleClass="chosen-select" styleId="bdgt" onchange="styleSelectedExpirationDate()">
                                                <option value="">Select an OPTION - begin By Typing Your Budget Code</option>
                                                <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                                            </nested:select>
                                        </td>
                                        <td style="white-space: nowrap;">$<nested:text property="amount" size="10"/></td>
                                    </tr>
                                </nested:iterate>
                                <tr>
                                    <td colspan="2">
                                        <button type="submit" onclick="this.form.cmd.value='addFundingSource'; this.form.rliFormIndex.value=<%=a%>;
                                                this.form.submit(); return false;" class="btn btn-default">Add Another Funding Source</button>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </c:otherwise>
                </c:choose>
            <!-- END funding source section -->

            <!-- Vendor section only for non-stock items -->
            <%--<logic:notEqual name="skin" value="PRISM2">--%>
                <c:choose>
                    <c:when test="${requestLineItemForm.item.itemType eq 'Purchase Item'}">
                        <!-- this is a stock item, so we don't need the vendor info -->
                        <td class="text-center"></td>
                    </c:when>
                    <c:when test="${requestLineItemForm.suggestedVendorCatalogNumber eq 'STAFFAUG' || requestLineItemForm.suggestedVendorCatalogNumber eq 'MNITCONTRACT'|| requestLineItemForm.suggestedVendorCatalogNumber eq 'WAN/Computing Services'}">
                        <c:choose>
                        <c:when test="${requestLineItemForm.suggestedVendorCatalogNumber eq 'STAFFAUG'}">
                            <td><b>MNIT Salary Budget Builder:</b><br/>
                        </c:when>
                            <c:when test="${requestLineItemForm.suggestedVendorCatalogNumber eq 'WAN/Computing Services'}">
                                <td><b>WAN/Computing Services:</b><br/>
                            </c:when>
                        <c:otherwise>
                            <td><b>IT Contracts:</b><br/>
                        </c:otherwise>
                        </c:choose>

                            <label style="white-space: nowrap">
                                PO#:<br/>
                                <nested:text property="suggestedVendorName" size="25" maxlength="50" styleClass="form-control"/>
                            </label><br/>
                            <br/>
                            <label>AC2 Code: <nested:text property="swiftItemId"/></label>
                            <nested:notEmpty property="noteForms">
                            <br/>
                            <label style="white-space: nowrap">
                                Vendor Notes:<br/>
                                    <nested:textarea property="noteForms[0].noteText" cols="30" rows="6" styleClass="form-control"/>
                                    </nested:notEmpty>
                              </label>
                        </td>
                    </c:when>

                    <c:otherwise>
                        <td>
                            <label style="white-space: nowrap">
                                Vendor Name:<br/>
                                <nested:text property="suggestedVendorName" size="25" maxlength="50" styleClass="form-control"/>
                            </label><br/>
                            <label style="white-space: nowrap">
                                Vendor URL:<br/>
                                <nested:textarea property="suggestedVendorURL" cols="20" rows="1" styleClass="form-control"/>
                            </label><br/>
                            <label style="white-space: nowrap">
                                Vendor Catalog #:<br/>
                                <nested:text property="suggestedVendorCatalogNumber" size="18" styleClass="form-control"/>
                            </label>
                            <nested:notEmpty property="noteForms">
                                <br/><nested:textarea property="noteForms[0].noteText" cols="30" rows="6" styleClass="form-control"/>
                            </nested:notEmpty>
                        </td>
                    </c:otherwise>
                </c:choose>
                <td class="text-center" style="vertical-align: middle;"><nested:checkbox property="remove"/></td>
            </tr>
        </nested:iterate>
    </tbody>
</table>