<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ page import="us.mn.state.health.web.struts.editstockitem.EditStockItemForm" %>

<html>
    <head>
        <title>Advanced Search</title>
    </head>
    <body>
        <nested:form action="advancedSearchStockItems.do" method="post">
        <table class="table" align="center" cellpadding="5" bgcolor="#CCCCCC" width="90%">
        <tr class="tableheader">
            <td align="center">Search Stock Items</td>
        </tr>
        <tr>
            <td>
                <table>
                    <tr >
                        <td>Description</td>
                        <td></td>
                        <td>ICNBR</td>
                        <td>Hazardous</td>
                        <td>Category</td>
                        <td></td>
                        <td>Dispense Unit</td>
                    </tr>
                    <tr class="tabledetail">
                        <td>
                            <nested:textarea name="advancedSearchStockItemsForm" property="description" rows="2"/>
                        </td>
                        <td>
                            <html:radio name="advancedSearchStockItemsForm" property="descriptionSearchOption" value="ANY">Any
                                word
                            </html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="descriptionSearchOption" value="ALL">All
                                the words
                            </html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="descriptionSearchOption" value="MATCH">
                                Exact match
                            </html:radio>
                            <br>
                        </td>
                        <td>
                            <nested:text name="advancedSearchStockItemsForm" property="icnbr" size="10"/>
                        </td>
                        <td>
                            <html:radio name="advancedSearchStockItemsForm" property="hazardous" value="true">Yes</html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="hazardous" value="false">No</html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="hazardous" value="">Both</html:radio>
                            <br>
                        </td>
                        <td>
                            <nested:select property="categoryCode" name="advancedSearchStockItemsForm">
                                <option value="">All</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="categories" label="name"
                                                          value="categoryCode"/>
                            </nested:select>
                        </td>
                        <td>
                            <html:radio name="advancedSearchStockItemsForm" property="categorySearchOption" value="SC">Strictly
                                the selected category
                            </html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="categorySearchOption" value="DC">All the
                                descendant categories
                            </html:radio>
                            <br>
                        </td>
                        <td>
                            <nested:select property="dispenseUnit" name="advancedSearchStockItemsForm">
                                <option value="">All</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="units" label="name" value="name"/>
                            </nested:select>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table>
                    <tr >
                        <td>Contact</td>
                        <td>
                            <bean:message key="orgBudget"/>
                        </td>
                        <td>Manufacturer</td>
                        <td>Vendor</td>
                    </tr>
                    <tr class="tabledetail">
                        <td>
                            <nested:select property="contact" name="advancedSearchStockItemsForm">
                                <option value="">Any</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="contacts"
                                                          label="lastAndFirstName" value="personId"/>
                            </nested:select>
                        </td>
                        <td align="center">
                            <nested:select property="orgBudget" name="advancedSearchStockItemsForm">
                                <option value="">Any</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="orgBudgets"
                                                          label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                            </nested:select>
                        </td>
                        <td>
                            <nested:select property="manufacturer" name="advancedSearchStockItemsForm">
                                <option value="">Any</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="manufacturers"
                                                          label="externalOrgDetail.orgName" value="manufacturerId"/>
                            </nested:select>
                        </td>
                        <td>
                            <nested:text property="vendor" name="advancedSearchStockItemsForm"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        <tr>
            <td>
                <table>
                    <tr >
                        <td>Seasonal</td>
                        <td align="center">Quantity On Hand</td>
                        <td>Status</td>
                        <td>Fill Until Depleted</td>
                        <td align="center">Hold Until Date</td>
                    </tr>
                    <tr class="tabledetail">
                        <td>
                            <html:radio name="advancedSearchStockItemsForm" property="seasonal" value="true">Yes</html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="seasonal" value="false">No</html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="seasonal" value="">Both</html:radio>
                            <br>
                        </td>
                        <td>
                            Between
                            <html:text name="advancedSearchStockItemsForm" property="qtyOnHandFrom" size="5"/>
                            and
                            <html:text name="advancedSearchStockItemsForm" property="qtyOnHandTo" size="5"/>
                        </td>
                        <td>
                            <nested:select property="status" name="advancedSearchStockItemsForm">
                                <option value="">Any</option>
                                <nested:optionsCollection name="advancedSearchStockItemsForm" property="statuses" label="name"
                                                          value="statusCode"/>
                            </nested:select>
                        </td>
                        <td>
                            <html:radio name="advancedSearchStockItemsForm" property="fillUntilDepleted" value="true">Yes
                            </html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="fillUntilDepleted" value="false">No
                            </html:radio>
                            <br>
                            <html:radio name="advancedSearchStockItemsForm" property="fillUntilDepleted" value="">Both
                            </html:radio>
                            <br>
                        </td>
                        <td>
                            Between
                            <html:text name="advancedSearchStockItemsForm" property="holdUntilDateFrom" size="8" styleClass="dateInput"/>
                            and
                            <html:text name="advancedSearchStockItemsForm" property="holdUntilDateTo" size="8" styleClass="dateInput"/>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
        </table>
        <table border="0" align="center">
            <tr>
                <td align="center" colspan="2">
                    <input type="SUBMIT" value="Search"/> <input type="reset" name="reset" value="Clear Form"/>
                </td>
            </tr>
        </table>
        </nested:form>
        <nested:form action="inventory/editStockItem.do">
            <nested:hidden property="stockItemId"/>
            <nested:hidden property="cmd"/>
            <nested:notEmpty name="advancedSearchStockItemsForm" property="results">
                <table align="center" cellspacing="0" cellpadding="5" width="90%">
                    <tr>
                        <td  colspan="7">You have
                            <nested:write name="advancedSearchStockItemsForm" property="resultsNumber"/>
                            results!
                            <br/>
                        </td>
                    </tr>
                    <tr class="tableheader">
                        <th>&nbsp;</th>
                        <th><a href="?orderBy=icnbr" style="color: White">ICNBR</a></th>
                        <th><a href="?orderBy=unit" style="color: White">Unit</a></th>
                        <th><a href="?orderBy=desc" style="color: White">Item Description</a></th>
                        <th><a href="?orderBy=org" style="color: White">
                            <bean:message key="orgBudget"/>
                        </a></th>
                        <th><a href="?orderBy=pcontact" style="color: White">Primary Contact</a></th>
                        <th><a href="?orderBy=scontact" style="color: White">Secondary Contact</a></th>
                        <th><a href="?orderBy=usage" style="color: White">Usage</a></th>
                    </tr>
                    <% int i; %>
                    <nested:iterate id="stockItem" name="advancedSearchStockItemsForm" property="results" indexId="a">
                        <% i = a % 2;%>
                        <tr class="tabledetail" bgcolor="<%out.write((i==0)?"#cccccc":"white");%>">
                            <td>
                                <input type="BUTTON" value="Edit Item"
                                       onclick="this.form.action = 'inventory/editStockItem.do';
                                   this.form.stockItemId.value = <nested:write property='itemId'/>;
                                   this.form.cmd.value='<%=EditStockItemForm.LOAD_ITEM%>';this.form.submit(); return false;"/>
                            </td>
                            <td><nested:write property="fullIcnbr"/></td>
                            <td><nested:write property="dispenseUnit.name"/></td>
                            <td><nested:write property="description"/></td>
                            <td><nested:write property="orgBudget.orgBudgetCodeAndNameAndFY"/></td>
                            <td><nested:write property="primaryContact.firstAndLastName"/></td>
                            <td><nested:write property="secondaryContact.firstAndLastName"/></td>
                            <td><nested:write property="annualUsage"/></td>
                        </tr>
                    </nested:iterate>
                </table>
            </nested:notEmpty>
        </nested:form>
    </body>
</html>
