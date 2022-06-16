<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html>
  <head>
    <title>Inventory Maintenance</title>
  </head>
  <body>
    <html:form action="viewInventoryMaintenanceItems" method="post">
        <table style="width: 100%; border-collapse: collapse; border: none;">
          <tr>
            <th >Inventory Maintenance Items</th>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <th style="text-align: left;">&nbsp;Item Categories</th>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>
                <table>
                    <tr>
                        <td>
                            <table>
                                <tr>
                                    <th>&nbsp;Current Category</th>
                                    <th>Category Code</th>
                                    <th>Parent Category</th>
                                    <th>Child Categories</th>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td style="text-align: center;">
                                        <a href="viewEditCategory.do?categoryId=<nested:write property='category.categoryId'/>"><nested:write property="category.name"/></a>
                                    </td>
                                    <td style="text-align: center;">
                                        <nested:write property="category.categoryCode"/>
                                    </td>
                                    <td style="text-align: center;">
                                        <nested:notEmpty property="category.parentCategory">
                                            <a href="selectCategory.do?categoryId=<nested:write property='category.parentCategory.categoryId'/>"><nested:write property="category.parentCategory.name"/></a>
                                        </nested:notEmpty>
                                    </td>
                                    <td style="text-align: center;">
                                        <nested:select property="categoryId" size = "5" onclick="selectFromListWithNavigation(this.form, 'categoryId', 'selectCategory.do')">
                                            <nested:optionsCollection property="category.childCategories" label="name" value="categoryId"/>
                                        </nested:select>
                                    </td>
                                    <td style="text-align: center;">
                                        <a href="viewNewCategory.do?categoryId=<nested:write property='category.categoryId'/>">New Child Category</a>
                                    </td> 
                                </tr>
                            </table>
                        </td>
                    </tr>       
                </table>
            </td>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <th style="text-align: left;"> &nbsp;Stock Item Locations</th>
          </tr>
          <tr>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>
                <table>
                    <tr>
                        <th>Facilities</th>
                        <th>Current Facility</th>
                        <th>Parent Facility</th>
                        <th>Locations</th>
                        <td></td>
                    </tr>
                    <tr>
                        <td>
                            <nested:select property="facilityId" size = "5" onclick="selectFromListWithNavigation(this.form, 'facilityId', 'selectStockItemFacility.do')">
                                <nested:optionsCollection property="facilities" label="facilityName" value="facilityId"/>
                            </nested:select>
                        </td>
                        <td style="text-align: center;">
                            <nested:notEmpty property="facility">
                                <nested:write property="facility.facilityName"/>
                            </nested:notEmpty>
                        </td>
                        <td style="text-align: center;">
                            <nested:notEmpty property="facility">
                                <nested:notEmpty property="facility.parent">
                                    <a href="selectStockItemFacility.do?facilityId=<nested:write property='facility.parent.facilityId'/>"><nested:write property="facility.parent.facilityName"/></a>
                                </nested:notEmpty>
                            </nested:notEmpty>
                        </td>
                        <td style="text-align: center;">
                            <nested:notEmpty property="facility">
                                <nested:select property="locationId" size = "5" onclick="selectFromListWithNavigation(this.form, 'itemId', 'viewEditStockItemLocation.do')">
                                    <nested:optionsCollection property="facility.stockItemLocations" label="locationCode" value="locationId"/>
                                </nested:select>
                            </nested:notEmpty>
                        </td>
                        <td style="text-align: center;">
                            <nested:notEmpty property="facility">
                                <a href="viewNewStockItemLocation.do?facilityId=<nested:write property='facility.facilityId'/>">New Stock Item Location</a>
                            </nested:notEmpty>
                        </td>
                    </tr>
                </table>
            </td>
          </tr>
        </table>
    </html:form>
  </body>
</html>
