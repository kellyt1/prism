<%@ include file="../../../include/tlds.jsp" %>

<html>
    <head>
        <title>Add New Sensitive Asset - Step 1</title>
    </head>
  <body>
    <div align="center">
    <p>
        A Sensitive Asset must be associated with an item in our database.  Use this screen to search for
        the appropriate item.  Enter any combination of search criteria, then click Search.
    </p>
    <nested:form action="searchAllItems.do" method="post">
        <input type="HIDDEN" name="forward" value="sensitiveAsset" />
        <fieldset>
            <legend>Search Items</legend>
            <label>Description: <nested:text property="description" size="30" /></label>
            <br/>
            <label>Category:
                <nested:select property="categoryCode" styleClass="chosen-select">
                    <option value="">All</option>
                    <nested:optionsCollection property="categories" label="name" value="categoryCode"/>
                </nested:select>
            </label>
            <br/>
            <label>Vendor: <nested:text property="vendor"/></label>
            <br/>
            <label>Manufacturer:
                <nested:select property="manufacturer" styleClass="chosen-select">
                    <option value="">Any</option>
                    <nested:optionsCollection property="manufacturers" label="externalOrgDetail.orgName" value="manufacturerId"/>
                </nested:select>
            </label>
            <br/>
            <strong>Hazardous: </strong>
            <label class="radio-inline"><html:radio property="hazardous" value="">Either</html:radio></label>
            <label class="radio-inline"><html:radio property="hazardous" value="true">Yes</html:radio></label>
            <label class="radio-inline"><html:radio property="hazardous" value="false">No</html:radio></label><br/>
            <input type="SUBMIT" value="Search" />
        </fieldset>
        <br />
        <nested:notEmpty property="results">
            <table class="table table-bordered table-striped">
                <tr>
                    <th><a href="?orderBy=description">Description</a></th>
                    <th><a href="?orderBy=categoryName">Category</a> </th>
                    <th><a href="?orderBy=categoryName">Type</a> </th>
                    <th><a href="?orderBy=manufacturerName">Manufacturer</a></th>
                    <th>&nbsp;</th>
                </tr>
                <nested:iterate id="item" property="results" indexId="a" >
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
                            <input type="button" value="Select" onclick="this.form.action='viewAddNewSensitiveAsset.do?itemId=<nested:write property="itemId" />'; this.form.submit(); return false;" />
                        </td>
                    </tr>
                </nested:iterate>
            </table>
        </nested:notEmpty>
    </nested:form>
    </div>
  </body>
