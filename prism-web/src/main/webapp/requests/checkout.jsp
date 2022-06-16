<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>Checkout</title>
        <script type="text/javascript">
            function hideshow() {
                if (!document.getElementById("highdiv"))
                    return;

                var form = document.forms[0];
                var objPriority = form.priorityCode.value;

                if (objPriority == "HIGH")  {
                    alert("Enter the Maximum amount you are willing to pay for shipping. (See ?Definitions)");
                    highdiv.style.display="block" ;
                    form.additionalInstructions.value = "I am willing to pay up to $__.00 extra for Shipping.";
                } else {
                    highdiv.style.display="none"
                }
            }
        </script>
    </head>

    <body onload="hideshow()">
        <nested:form action="/checkout" method="post"  enctype="multipart/form-data">
            <nested:hidden property="cmd" value=""  />
            <nested:hidden property="rliFormIndex" value=""  />
            <div class="row">
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Deliver To</div>
                        <div class="panel-body">
                            <nested:present property="deliveryDetail">
                                <div class="row" onload="console.print('Please double check Deliver To address.')">
                                    <div class="col-xs-6">Organization:</div>
                                    <div class="col-xs-6">
                                        <nested:present property="deliveryDetail.organization">
                                            <nested:write property="deliveryDetail.organization.orgName" />
                                        </nested:present>
                                        <nested:notPresent property="deliveryDetail.organization">
                                            <nested:present property="deliveryDetail.facility">MDH</nested:present>
                                        </nested:notPresent>
                                    </div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-6">Recipient:</div>
                                    <div class="col-xs-6"><nested:write property="deliveryDetail.recipientName" /></div>
                                </div>
                                <br/>
                                <div class="row">
                                    <div class="col-xs-6">Mailing Address:</div>
                                    <div class="col-xs-6">
                                        <nested:present property="deliveryDetail.facility">
                                            <nested:write property="deliveryDetail.facility.facilityName" /><br />
                                        </nested:present>
                                        <nested:present property="deliveryDetail.mailingAddress">
                                            <nested:write property="deliveryDetail.mailingAddress.address1" /><br />
                                            <nested:present property="deliveryDetail.mailingAddress.address2">
                                                <nested:write property="deliveryDetail.mailingAddress.address2" /><br />
                                            </nested:present>
                                            <nested:write property="deliveryDetail.mailingAddress.city" />,&nbsp;
                                            <nested:write property="deliveryDetail.mailingAddress.state" />&nbsp;
                                            <nested:write property="deliveryDetail.mailingAddress.zip" />
                                        </nested:present>
                                    </div>
                                </div>
                                <br/>
                                <nested:equal property="defaultItemDeliveryDetail" value="true">
                                    <div class="row">
                                        <div class="col-sm-12 text-center">
                                            Delivery Location cannot be changed.
                                            <br/>
                                            One or more items have a designated delivery location.
                                        </div>
                                    </div>
                                </nested:equal>
                                <nested:equal property="defaultItemDeliveryDetail" value="false">
                                    <div class="row">
                                        <div class="col-sm-12 text-center">
                                            <html:submit value="Change Delivery Info" onclick="this.form.cmd.value='changeDeliveryInfo'; this.form.submit(); return false;" styleClass="btn btn-default"/>
                                        </div>
                                    </div>
                                </nested:equal>
                            </nested:present>
                        </div>
                    </div>
                </div>
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">Additional Information</div>
                        <div class="panel-body">
                            <div class="row">
                                <div class="col-xs-6"><label for="dateNeeded">Need-By Date (MM/dd/yyyy):</label></div>
                                <div class="col-xs-6"><nested:text property="needByDate" size="12" styleClass="dateInput form-control" styleId="dateNeeded"/></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6"><label for="priority">Priority:</label></div>
                                <div class="col-xs-6">
                                    <nested:select property="priorityCode" onchange="hideshow(), $j(this).prioritySelected()" styleId="priority" styleClass="chosen-select">
                                        <nested:optionsCollection property="priorities" label="name" value="priorityCode"/>
                                    </nested:select>&nbsp;&nbsp;
                                    <a href="#" onclick="window.open('./requests/help/priorityDefinitions.jsp', 'priorityDef', 'width=500,height=300,status=no,resizable=no,top=200,left=200,dependent=yes,alwaysRaised=yes')"> <span class="smallwarning">?</span>Definitions </a>
                                </div>
                                <div class="col-xs-6"></div>
                                <div class="col-xs-6"><span id="shipping-rate"></span></div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-xs-6">
                                    <label for="additionalInstructions">Additional Instructions:</label><br />
                                    <span class="text-danger small">For computer orders enter:
                                        <p>
                                            Supervisors Name:
                                            <br>
                                            Division Purchasing Liaison Name:
                                        </p>
                                    </span>
                                </div>
                                <div class="col-xs-6" id="addInstructions">
                                    <nested:textarea property="additionalInstructions" cols="25" rows="2" styleId="additionalInstructions" styleClass="form-control"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <br />
            <%@include file="requestLineItemsInclude.jsp"%>
            <br />
            <hr width="75%" />
            <div class="row">
                <div class="col-md-12 text-center">
                    <p>* - Estimated Cost shown does not include potential shipping or other charges.</p>
                    <p>* - After submitting request, if you have made an entry error, or need to change something, <b>PLEASE DO NOT MAKE ANOTHER ENTRY.</b>
                        <logic:equal name="skin" value="PRISM2">
                            Instead submit a <a href="https://fyi.web.health.state.mn.us/open/helpdesk/" target="_blank">MN.iT service ticket</a> with the MRQ# and the change you are requesting and they will work with Purchasing to update the request for you.
                        </logic:equal>
                        <logic:notEqual name="skin" value="PRISM2">
                            Instead email any changes to your respective buyer with the MRQ# and they will edit it for you.
                        </logic:notEqual>
                    </p>
                    <div class="btn-group">
                        <html:submit value="Update/Refresh Cart" onclick="this.form.cmd.value='saveCart'; this.form.submit(); return false;" styleClass="btn btn-default"/>
                        <button id="submitButton" type="button" class="btn btn-default">Done - Submit Request</button>
                    </div>
                </div>
            </div>
            <div id="dialog-confirm" title="Correct Deliver-To location?" class="hidden">
                <p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>Is the correct Deliver-To location selected for this order?</p><br/>
                <p><strong>Yes</strong> - Complete checkout process.</p>
                <p><strong>No</strong> - Change Deliver-To location.</p>
            </div>
        </nested:form>
        <script type="text/javascript">
            $j(function() {
                var isPRISM = <%= !session.getAttribute("skin").toString().equals("PRISM2") %>;
                if(isPRISM) {
                    $j('#submitButton').click(function () {
                        $j("#dialog-confirm").dialog({
                            resizable: false,
                            width: 400,
                            modal: true,
                            buttons: {
                                "Yes": function () {
                                    $j(this).dialog("close");
                                    document.forms[0].cmd.value = 'checkout';
                                    document.forms[0].submit();
                                    return false;
                                },
                                "No": function () {
                                    $j(this).dialog("close");
                                    document.forms[0].cmd.value = 'changeDeliveryInfo';
                                    document.forms[0].submit();
                                    return false;
                                }
                            }
                        });
                        $j("#dialog-confirm").removeClass('hidden');
                    });
                } else {
                    $j('#submitButton').click(function () {
                        document.forms[0].cmd.value = 'checkout';
                        document.forms[0].submit();
                    });
                }
            });
            $j(document).ready(function() {
                $j(this).prioritySelected()
            });
        </script>
    </body>
</html>