<%@ include file="../../../include/tlds.jsp" %>

<html>
  <head>
    <title>Edit Fixed Asset - Step 1 - Search</title>
  </head>
  <body>
    <div align="center">
    <nested:form action="searchFixedAssets.do" method="post">
        <table class="table" align="center" border="0" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC" width="50%">
            <tr>
                <td colspan="4" align="center">Search Fixed Assets</td>
            </tr>
            <tr>
                <td>Description:</td>
                <td>Model:</td>
                <td>Serial Number:</td>  
                <td>Asset Number:</td>
            </tr>
            <tr>
                <td><nested:text property="description" size="30" /></td>
                <td><nested:text property="model" size="15" /></td>
                <td><nested:text property="serialNumber" size="15" /></td>
                <td><nested:text property="assetNumber" size="15" /></td>
            </tr>
            <tr>
                <td>Category:</td>
                <td>Vendor:</td>
                <td>Manufacturer:</td>
                <td>Status:</td>                
            </tr>
            <tr>
                <td>
                    <nested:select property="categoryCode" style="width: 200px">
                        <option value="">All</option>
                        <nested:optionsCollection property="categories" label="name" value="categoryCode"/>
                    </nested:select>
                </td>
                <td><nested:text property="vendor"/></td>
                <td>
                  <nested:select property="manufacturer" style="width: 200px">
                    <option value="">Any</option>
                    <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                  </nested:select>
                </td>
                <td>
                  <nested:select property="statusCode" style="width: 200px">
                    <option value="">Any</option>
                    <nested:optionsCollection property="statuses" label="name" value="statusCode"/>
                  </nested:select>
                </td>
            </tr>
            <tr>
                <td>Class Code:</td>
                <td><bean:message key="orgBudget"/>:</td>
                <td>Contact Person:</td>
                <td>Facility:</td>                
            </tr>
            <tr>
                 <td>
                   <nested:select property="classCode" style="width: 200px">
                        <option value="">Any</option>
                        <nested:optionsCollection property="classCodes" label="classCodeValue" value="classCodeId"/>
                    </nested:select>
                </td>
                <td>
                  <nested:select property="orgBudget" style="width: 200px">
                    <option value="">Any</option>
                    <nested:optionsCollection property="orgBudgets" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                  </nested:select>
                </td>
                <td>
                  <nested:select property="contact" style="width: 200px">
                    <option value="">Any</option>
                    <nested:optionsCollection property="contacts" label="firstAndLastName" value="personId"/>
                  </nested:select>
                </td>
                <td>
                  <nested:select property="facility" style="width: 200px">
                    <option value="">Any</option>
                    <nested:optionsCollection property="facilities" label="facilityName" value="facilityId"/>
                  </nested:select>
                </td>
            </tr>
            <tr>
                <td colspan="2">Date Received:</td>
                <td></td>
                <td></td>                
            </tr>
            <tr>
                <td colspan="2">
                  From:&nbsp;<nested:text property="dateReceivedFrom" size="10" styleClass="dateInput"/>
                  &nbsp;&nbsp;
                  To:&nbsp;<nested:text property="dateReceivedTo" size="10" styleClass="dateInput"/>
                </td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td colspan="4" align="center"><input type="SUBMIT" value="Search" /></td>                
            </tr>
        </table>
        <br />
        <nested:notEmpty property="results">
            <table class="table table-bordered table-striped">
                <tr class="tableheader">
                    <th><a href="?orderBy=assetNbr" style="color: White">Asset Nbr</a> </th>
                    <th><a href="?orderBy=description" style="color: White">Description</a></th>
                    <th><a href="?orderBy=categoryName" style="color: White">Category</a> </th>                    
                    <th><a href="?orderBy=manufacturerName" style="color: White">Manufacturer</a></th>
                    <th><a href="?orderBy=model" style="color: White">Model</a></th>
                    <th><a href="?orderBy=contact" style="color: White">Contact</a></th>
                    <th>&nbsp;</th>
                </tr>
                <% int i; %>
                <nested:iterate id="asset" property="results" indexId="a" >
                    <%  i = a % 2;%>
                    <tr>
                        <td><nested:write property="fixedAssetNumber"/></td>
                        <td><nested:write property="item.description"/></td>
                        <td><nested:write property="item.category.name"/></td>
                        <td>
                          <nested:present property="item.manufacturer">
                            <nested:write property="item.manufacturer.externalOrgDetail.orgName"/>
                          </nested:present>
                        </td>
                        <td><nested:write property="item.model"/></td>
                        <td><nested:write property="contactPerson.firstAndLastName"/></td>
                        <td>
                            <input type="button" value="Select" onclick="this.form.action='viewEditFixedAsset.do?fixedAssetId=<nested:write property="sensitiveAssetId" />'; this.form.submit(); return false;" />
                        </td>
                    </tr>
                </nested:iterate>
            </table>
        </nested:notEmpty>
    </nested:form>
    </div>
  </body>
</html>