<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <% int rliIndex = 0; %>
    <title>Reorder Stock Item - Checkout</title>
    <script language="javascript" type="text/javascript">

        function calcTotalAmountStock() {
            var loops=document.forms[0].elements['loops'].value;
            var runningTotal = 0;
            for (var i=0; i < loops; i ++) {
               
                var span2 = document.getElementById("estimatedCostSPAN"+i) ;
                var lValue = span2.firstChild.nodeValue;
                lValue = lValue.replace("$","");
                if (!isNaN(lValue)) {
                    runningTotal += parseFloat(lValue);
                }
            }
            var span3 = document.getElementById("totalEstimatedCost") ;
            span3.firstChild.nodeValue = runningTotal;
        }

        function refreshAmountStock(inQuanity,arrayIndex) {
            var qty = inQuanity.value;
            var cost = document.forms[0].elements['requestLineItemForms[' + arrayIndex + '].itemCost'].value; //requestLineItemForms[0].itemCost.value;
            var amt = 0;

            if((qty != "" && qty != null) && (cost != "" && qty != null)) {
                   amt = (cost * qty).toFixed(2);
            }
            var span2 = document.getElementById("estimatedCostSPAN"+arrayIndex) ;
            span2.firstChild.nodeValue = amt;
        }
    </script>

</head>

<body>
    <nested:form action="/reorderStockItem" method="post">
        <nested:hidden property="cmd" value=""  />
        <nested:hidden property="rliFormIndex" value=""  />
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <fieldset class="text-center">
                    <legend class="text-left">Re-Order Stock Item (replenish inventory)</legend>
                    <label class="text-left">
                        Deliver To Facility:<br>
                        <nested:select name="stockItemReorderRequestForm" property="deliveryDetailForm.facilityId" styleClass="chosen-select">
                            <option value=""></option>
                            <nested:optionsCollection property="deliveryDetailForm.facilities" label="facilityName" value="facilityId"/>
                        </nested:select>
                    </label>&nbsp;
                    <label class="text-left">
                        Need-By Date:<br>
                        <nested:text name="stockItemReorderRequestForm" property="needByDate" size="12" styleClass="dateInput"/>
                    </label>&nbsp;
                    <label class="text-left">
                        Priority:<br>
                        <nested:select name="stockItemReorderRequestForm" property="priorityCode" styleClass="chosen-select" style="width: 100px;">
                            <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                        </nested:select>
                    </label><br><br>
                    <label class="text-left">Additional Instructions:<br><nested:textarea name="stockItemReorderRequestForm" property="additionalInstructions" cols="30" rows="3"/></label>
                </fieldset>
            </div>
        </div>
        <br />
        <div align="center">
            <p>* - Estimated Cost shown does not include potential shipping or other charges.</p>
            <button onclick="this.form.action='/viewReorderStockItems.do';this.form.submit(); return false;" class="btn btn-default">Add More Items</button>&nbsp;&nbsp;
            <button onclick="this.form.action='/cancelReorderStockItems.do';this.form.submit(); return false;" class="btn btn-default">Cancel Re-Order</button>&nbsp;&nbsp;
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
        <br />
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th scope="col">ICNBR</th>
                    <th scope="col">Item Description</th>
                    <th scope="col" class="text-right">Qty On Hand</th>
                    <th scope="col" class="text-right">Current Demand</th>
                    <th scope="col" class="text-right">ROP</th>
                    <th scope="col" class="text-right">ROQ</th>
                    <th scope="col" align="center">Qty</th>
                    <th scope="col" align="center">Unit</th>
                    <th scope="col" align="center">Unit Cost</th>
                    <th scope="col" align="center">Estimated Cost*</th>
                    <th scope="col"></th>
                </tr>
            </thead>

            <nested:iterate property="requestLineItemForms" id="requestLineItemForm" name="stockItemReorderRequestForm" indexId="a">
                <tr >
                    <td>
                        <nested:present property="item" >
                            <nested:write property="item.fullIcnbr" />
                        </nested:present>
                    </td>
                    <td><nested:write property="item.description" /></td>
                    <td class="text-right"><nested:write property="stockItem.qtyOnHand" format="#,###"/></td>
                    <td class="text-right"><nested:write property="stockItem.currentDemand" format="#,###"/></td>
                    <td class="text-right"><nested:write property="stockItem.reorderPoint" format="#,###" /></td>
                    <td class="text-right"><nested:write property="stockItem.reorderQty" format="#,###" /></td>
                    <td align="center"><nested:text property="quantity" size="5" onblur="refreshAmountStock(this,${a});calcTotalAmountStock();" /></td>
                    <td align="center">
                        <nested:present property="item">
                            <nested:write property="item.dispenseUnit.name" />
                        </nested:present>
                        <nested:notPresent property="item">
                            <nested:select property="unitId">
                                <option value=""></option>
                                <nested:optionsCollection property="units" label="name" value="unitId"/>
                            </nested:select>
                        </nested:notPresent>
                    </td>
                    <td class="text-center">
                        <nested:hidden property="itemCost"/>
                        <span id="unitcost">
                        <nested:present property="item" >
                            <nested:write property="itemCost"/>
                            <%--<nested:present property="item.dispenseUnitCost">--%>
                                <!--$<nested:write property="item.dispenseUnitCost" format="#,##0.00" />-->
                            <%--</nested:present>--%>
                            <nested:notPresent property="item.dispenseUnitCost">
                                n/a
                            </nested:notPresent>

                        </nested:present>
                        <nested:notPresent property="item">
                           $<nested:text property="itemCost" size="5" />
                        </nested:notPresent>
                         </span>
                    </td>
                    <td class="text-right">
                        <span id="estimatedCostSPAN${a}">
                        $<nested:write property="estimatedCostD" format="#,##0.00" />
                        <%--<nested:write property="itemCost" format="#,##0.00"/>--%>
                        </span>
                    </td>
                    <td class="text-center">
                        <a href="removeRLIFormFromReorderList.do?rliIndex=<%=rliIndex%>">remove</a>               
                    </td>
                </tr>
                <% rliIndex++; %>
            </nested:iterate>
            <tr>
                <td colspan="9" class="text-right">Total Estimated Cost:</td>
                <td class="text-right">
                    <span id="totalEstimatedCost">
                        $<nested:write property="estimatedCostTotal" format="#,##0.00" />
                    </span>
                    <input type="hidden" value="<%=rliIndex%>" name="loops">
                </td>    
                <td>&nbsp;</td>
            </tr>
        </table>
        <br />
        <hr width="75%" />
        <div align="center">
            <p>* - Estimated Cost shown does not include potential shipping or other charges.</p>
            <button onclick="this.form.action='/viewReorderStockItems.do';this.form.submit(); return false;" class="btn btn-default">Add More Items</button>&nbsp;&nbsp;
            <button onclick="this.form.action='/cancelReorderStockItems.do';this.form.submit(); return false;" class="btn btn-default">Cancel Re-Order</button>&nbsp;&nbsp;
            <button type="submit" class="btn btn-default">Submit</button>
        </div>
    </nested:form>
</body>
</html>