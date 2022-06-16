<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html>
    <head>
        <title>Edit Order Formula</title>
    </head>

    <body>
        <form action="maintainOrderFormula.do" method="post"  >
            <table border="1" align="center" >
                <tr>
                    <th>Category</th>
                    <th>Order Formula Parameters</th>
                </tr>
                <tr>
                    <nested:hidden name="orderFormulaMaintainanceForm" property="cmd"/>
                    <td>
                        <nested:select name="orderFormulaMaintainanceForm" property="orderFormulaForm.categoryId" multiple="true" size="8"
                             onchange="this.form.cmd.value='SelectCategory';this.form.submit(); return false;" >
                            <nested:optionsCollection name="orderFormulaMaintainanceForm" property="categories" label="name" value="categoryId"/>
                        </nested:select>
                    </td>
                    <td>
                       <table>
                          <tr>
                            <td align="center"  colspan="2" >
                                <nested:notEmpty name="orderFormulaMaintainanceForm" property="category">
                                    <nested:write name="orderFormulaMaintainanceForm" property="category.name"/>
                                </nested:notEmpty>
                            </td>
                          </tr>
                          <tr>
                            <td>
                                <br>
                                <br>
                            </td>
                          </tr>
                          <tr>
                            <td>Order Cost</td>
                            <td>
                                <nested:text name="orderFormulaMaintainanceForm"  property="orderFormulaForm.orderCost"/>
                            </td>
                          </tr>
                          <tr>
                            <td>Lead Time Days</td>
                            <td><nested:text name="orderFormulaMaintainanceForm" property="orderFormulaForm.leadTimeDays"/></td>
                          </tr>
                       </table>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <button onclick="this.form.cmd.value='SaveOrderFormula';this.form.submit(); return false;">Save</button> &nbsp;
                        <input type="reset" name="reset" value="Reset"/>
                        <button onclick="this.form.action='/purchasing/index.jsp';this.form.submit(); return false;">Cancel</button>
                    </td>
                </tr>
            </table>
       </form>
    </body>
</html>