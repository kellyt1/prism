<%@ include file="../include/tlds.jsp" %>
<html>
  <head>
    <title>Item Advanced Search</title>
  </head>
  <body>
    <html:form action="/advancedSearchItemsPurchasing.do" method="post">
      <input type="HIDDEN" name="itemId"/>
      <html:hidden property="input"/>
      <table class="table table-bordered">
        <tr>
          <th width="40%" scope="col">Common Properties</th>
          <th width="30%" scope="col">Stock Item Properties</th>
          <th width="30%" scope="col">Options</th>
        </tr>
        <tr>
          <td>
            <table>
              <tr>
                <td>Description</td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td>
                  <nested:textarea name="advancedSearchCatalogForm" property="description" rows="2"/>
                </td>
                <td colspan="2">
                  <label><html:radio name="advancedSearchCatalogForm" property="descriptionSearchOption" value="ANY"/> Any word</label><br>
                  <label><html:radio name="advancedSearchCatalogForm" property="descriptionSearchOption" value="ALL"/> All the words</label><br>
                  <label><html:radio name="advancedSearchCatalogForm" property="descriptionSearchOption" value="MATCH"/> Match exactly the phrase</label><br>
                </td>
              </tr>
              <tr>
                <td>Dispense Unit</td>
                <td>Category</td>
<logic:notEqual name="skin" value="PRISM2">
                <td>Hazardous</td>
</logic:notEqual>
              </tr>
              <tr >
                <td>
                  <nested:select property="dispenseUnit" name="advancedSearchCatalogForm" styleClass="chosen-select">
                    <option value="">All</option>
                    <nested:optionsCollection name="advancedSearchCatalogForm" property="units" label="name" value="name"/>
                  </nested:select>
                </td>
                <td>
                  <nested:select property="categoryCode" name="advancedSearchCatalogForm" styleClass="chosen-select">
<logic:notEqual name="skin" value="PRISM2">
                    <option value="">All</option>
                    <nested:optionsCollection name="advancedSearchCatalogForm" property="categories" label="name" value="categoryCode"/>
</logic:notEqual>
<logic:equal name="skin" value="PRISM2">
    <option value="SCOMP">Standard Computer Equipment</option>
    <%--<option value="SCOMP_C">Computers</option>--%>
    <%--<option value="SCOMP_A">Computer Accessories</option>--%>
    <option value="SCOMP_S">Software</option>
    <%--<nested:optionsCollection name="advancedSearchCatalogForm" property="categories" label="name" value="categoryCode"/>--%>
</logic:equal>

                  </nested:select>
                </td>
<logic:notEqual name="skin" value="PRISM2">
                <td>
                  <html:radio name="advancedSearchCatalogForm" property="hazardous" value="true">Yes</html:radio><br>
                  <html:radio name="advancedSearchCatalogForm" property="hazardous" value="false">No</html:radio><br>
                  <html:radio name="advancedSearchCatalogForm" property="hazardous" value="">Both</html:radio><br>
                </td>
</logic:notEqual>
              </tr>
              <tr>
                <td>Vendor</td>
                <td>Manufacturer</td>
              </tr>
              <tr>
                <td>
                  <nested:select property="vendor" name="advancedSearchCatalogForm" styleClass="chosen-select">
                    <option value="">Any</option>
                    <nested:optionsCollection name="advancedSearchCatalogForm" property="vendors" label="externalOrgDetail.orgName" value="vendorId"/>
                  </nested:select>
                </td>
                <td>
                  <nested:select property="manufacturer" name="advancedSearchCatalogForm" styleClass="chosen-select">
                    <option value="">Any</option>
                    <nested:optionsCollection name="advancedSearchCatalogForm" property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                  </nested:select>
                </td>
              </tr>
            </table>
          </td>
          <td width="20%">
            <table>
              <tr>
                <td>ICNBR:</td>
              </tr>
              <tr>
                <td><nested:text name="advancedSearchCatalogForm" property="icnbr" size="10" /></td>
              </tr>
              <tr>
                <td>Status:</td>
              </tr>
              <tr>
                <td>
                  <nested:select property="status" name="advancedSearchCatalogForm" styleClass="chosen-select">
                    <option value="">Any</option>
                    <nested:optionsCollection name="advancedSearchCatalogForm" property="statuses" label="name" value="name"/>
                  </nested:select>
                </td>
              </tr>
              <tr>
                <td><bean:message key="orgBudget"/>:</td>
              </tr>
              <tr>
                <td>
                    <nested:select property="orgBudget" name="advancedSearchCatalogForm" styleClass="chosen-select">
                        <option value="">Any</option>
                        <nested:optionsCollection name="advancedSearchCatalogForm" property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                    </nested:select>
                </td>
              </tr>
            </table>
          </td>
          <td width="20%" style="padding: 0;">
            <table class="table">
                <tr>
                    <th>Category Search Option:</th>
                </tr>
              <tr>
                <td>
                    <label><html:radio name="advancedSearchCatalogForm" property="categorySearchOption" value="SC"> Strictly the selected category</html:radio></label><br>
                    <label><html:radio name="advancedSearchCatalogForm" property="categorySearchOption" value="DC"> All the descendant categories</html:radio></label><br><br>
                </td>
              </tr>
                <tr>
                    <th>Item Type Search Option:</th>
                </tr>
              <tr>
                <td>
                  <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="OPI"> Only Purchase Items</html:radio></label><br>
                  <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="OSI"> Only Stock Items</html:radio></label><br>
                  <label><html:radio name="advancedSearchCatalogForm" property="itemTypeSearchOption" value="AI"> All Item Types</html:radio></label><br>
                </td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>&nbsp;</tr>
      </table>
      <table>
          <tr>
            <td align="center" colspan="2">
                <input type="SUBMIT" value="Search"/>&nbsp;
                <input type="SUBMIT" value="Create New Item" onclick="this.form.action='viewCreatePurchaseItem.do'"/>
                <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;">Cancel</button>
            </td>
          </tr>
      </table>
        <nested:notEmpty name="advancedSearchCatalogForm" property="results">
          <table width="90%" align="center">
            <tr>
                <td colspan="4"></td>
            </tr>
            <tr>
                <th width="20%">Item ID #</th>
                <th width="40%">Description</th>
                <th width="20%">Unit</th>
                <th width="20%">Item Type</th>
            </tr>
            <nested:iterate name="advancedSearchCatalogForm" property="results">
              <tr>
                <td>
                    <logic:equal name="advancedSearchCatalogForm" property="input" value="orderLineItem">
                        <input type="SUBMIT" value="<nested:write property='itemId'/>"
                            onclick="this.form.itemId.value='<nested:write property='itemId'/>'; this.form.action='associateItemWithOrderLineItem.do'"/>
                    </logic:equal>
                    <logic:equal name="advancedSearchCatalogForm" property="input" value="advancedSearchItems">
                        <input type="SUBMIT" value="<nested:write property='itemId'/>"
                            onclick="this.form.itemId.value='<nested:write property='itemId'/>'; this.form.action='viewEditPurchaseItem.do'"/>
                    </logic:equal>
                    <logic:equal name="advancedSearchCatalogForm" property="input" value="purchasingRequest">
                        <input type="SUBMIT" value="<nested:write property='itemId'/>"
                            onclick="this.form.itemId.value='<nested:write property='itemId'/>'; this.form.action='viewCreatePurchasingRequestLineItem.do'"/>
                    </logic:equal>
                </td>
                <td><nested:write property="description"/></td>
                <td><nested:write property="dispenseUnit.name"/></td>
                <td><nested:write property="itemType"/></td>
              </tr>
            </nested:iterate>
          </table>
        </nested:notEmpty>
    </html:form>
  </body>
</html>
