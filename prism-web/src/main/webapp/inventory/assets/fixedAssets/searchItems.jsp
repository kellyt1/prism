<%@ include file="../../../include/tlds.jsp" %>

<html>
  <head>
    <title>Add New Fixed Asset - Step 1</title>
  </head>
  <body>
    <div align="center">
    <p>
        A Fixed Asset must be associated with an item in our database.  Use this screen to search for
        the appropriate item.  Enter any combination of search criteria, then click Search.
    </p>
    <nested:form action="searchAllItems.do" method="post">
        <input type="HIDDEN" name="forward" value="fixedAsset" />
        <table class="table" align="center" border="0" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC" width="50%">
            <tr>
                <td colspan="2" align="center">Search Items</td>
            </tr>
            <tr>
                <td align="right">Description:</td>
                <td><nested:text property="description" size="30" /></td>
            </tr>
            <tr>
                <td align="right">Category:</td>
                <td>
                    <nested:select property="categoryCode">
                        <option value="">All</option>
                        <nested:optionsCollection property="categories" label="name" value="categoryCode"/>
                    </nested:select>
                </td>
            </tr>
            <tr>
                <td align="right">Vendor:</td>
                <td><nested:text property="vendor"/></td>
            </tr>
            <tr>
                <td align="right">Manufacturer:</td>
                <td>
                  <nested:select property="manufacturer">
                    <option value="">Any</option>
                    <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                  </nested:select>
                </td>
            </tr>
            <tr>
                <td align="right">Hazardous:</td>
                <td>
                  <html:radio property="hazardous" value="">Either</html:radio><br>
                  <html:radio property="hazardous" value="true">Yes</html:radio><br>
                  <html:radio property="hazardous" value="false">No</html:radio><br>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center"><input type="SUBMIT" value="Search" /></td>                
            </tr>
        </table>
        <br />
        <nested:notEmpty property="results">
            <table class="table table-bordered table-striped">
                <tr>
                    <th><a href="?orderBy=description" style="color: White">Description</a></th>
                    <th><a href="?orderBy=categoryName" style="color: White">Category</a> </th>
                    <th><a href="?orderBy=categoryName" style="color: White">Type</a> </th>
                    <th><a href="?orderBy=manufacturerName" style="color: White">Manufacturer</a></th>
                    <th>&nbsp;</th>
                </tr>
                <% int i; %>
                <nested:iterate id="item" property="results" indexId="a" >
                    <%  i = a % 2;%>
                    <tr>
                        <td><nested:write property="description"/></td>
                        <td><nested:write property="category.name"/></td>
                        <td><nested:write property="itemType"/></td>
                        <td>
                          <nested:present property="manufacturer">
                            <nested:write property="manufacturer.externalOrgDetail.orgName"/>
                          </nested:present>
                        </td>
                        <td>
                            <input type="button" value="Select" onclick="this.form.action='viewAddNewFixedAsset.do?itemId=<nested:write property="itemId" />'; this.form.submit(); return false;" />
                        </td>
                    </tr>
                </nested:iterate>
            </table>
        </nested:notEmpty>
    </nested:form>
    </div>
  </body>