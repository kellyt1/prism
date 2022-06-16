<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>

<html>
    <head>
        <title>Reorder Stock Item</title>
    </head>
    <body>
        <nested:form action="/reorderStockItem" method="post">
            <nested:hidden property="cmd" value=""  />
            <nested:hidden property="rliFormIndex" value=""  />

            <table width="85%" align="center" cellspacing="5" cellpadding="5" bgcolor="#CCCCCC" >
                <tr>
                    <th colspan="3">
                        Re-Order Stock Item (replenish inventory)
                    </th>
                </tr>
                <tr>
                    <td>
                    <table align="center" cellspacing="5" cellpadding="5" >
                       <tr >
                           <td align="left" valign="bottom">Need-By Date<br />(MM/dd/yyyy):</td>
                           <td align="left" valign="bottom">Priority:</td>
                           <td align="left" valign="bottom">Additional Instructions:</td>
                        </tr>
                        <tr>
                           <td valign="top">
                             <nested:text property="needByDate" size="12" styleClass="dateInput"/>
                           </td>
                           <td valign="top">
                             <nested:select property="priorityCode">
                               <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                             </nested:select>
                           </td>
                           <td valign="top">
                             <nested:textarea property="additionalInstructions" cols="25" rows="2"/>
                           </td>
                        </tr>
                    </table>
                    </td>
                </tr>
            </table>
            <br />
            <table class="table table-bordered table-striped">
                <thead>
                    <tr>
                        <td align="left">I.C. NBR</td>
                        <td align="left">Item Description</td>
                        <td align="center">Qty</td>
                        <td align="center">Unit</td>
                        <td align="center">Unit Cost</td>
                        <td align="center">Estimated Cost*</td>
                    </tr>
                </thead>
                <nested:iterate property="requestLineItemForms" id="requestLineItemForm" indexId="a">
                    <tr>
                        <td align="left" valign="top">
                            <nested:present property="item" >
                                <nested:write property="item.fullIcnbr" />
                            </nested:present>
                        </td>
                        <td align="left" valign="top">
                            <nested:present property="item" >
                                <nested:write property="item.description" />
                            </nested:present>
                            <nested:notPresent property="item">
                                <nested:textarea property="itemDescription" rows="5" cols="20" />
                            </nested:notPresent>
                        </td>
                        <td align="center" valign="top">
                             <nested:text property="quantity" size="5" />
                        </td>
                        <td align="center" valign="top">
                            <nested:present property="item" >
                                <nested:write property="item.dispenseUnit.name" />
                            </nested:present>
                            <nested:notPresent property="item">
                                <nested:select property="unitId">
                                    <option value=""></option>
                                    <nested:optionsCollection property="units" label="name" value="unitId"/>
                                </nested:select>
                            </nested:notPresent>
                        </td>
                        <td align="center" valign="top" nowrap="nowrap">
                            <nested:present property="item" >
                                <nested:present property="item.dispenseUnitCost">
                                    $<nested:write property="item.dispenseUnitCost" format="#,##0.00" />
                                </nested:present>
                                <nested:notPresent property="item.dispenseUnitCost">
                                    n/a
                                </nested:notPresent>
                            </nested:present>
                            <nested:notPresent property="item">
                               $<nested:text property="itemCost" size="5" />
                            </nested:notPresent>
                        </td>
                        <td align="center" valign="top">
                             $<nested:write property="estimatedCost" format="#,##0.00" />
                        </td>
                    </tr>
                </nested:iterate>
            </table>
            <br/>
            <hr width="75%" />
            <div align="center">
                <p>* - Estimated Cost shown does not include potential shipping or other charges.</p>
                <html:submit value="Submit" onclick="this.form.cmd.value='checkout'; this.form.submit(); return false;"/>
                <button onclick="this.form.action='/viewMyStockItems.do';this.form.submit(); return false;">Cancel</button>
            </div>
        </nested:form>
    </body>
</html>