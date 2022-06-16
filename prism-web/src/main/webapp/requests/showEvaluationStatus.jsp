<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Request Approval Status</title>
</head>

<body>
<table class="table" align="center" border="0" bordercolor="#000000" cellspacing="0" cellpadding="5" bgcolor="#CCCCCC"
       width="95%">
    <tr class="tableheader">
        <td class="tabledetail" align="center">Request Number:</td>
        <td class="tabledetail" align="center">Evaluators<br>Evaluation Status:</td>
        <td class="tabledetail" align="center">Requestor:</td>
        <td class="tabledetail" align="center">Date Requested:</td>
        <td class="tabledetail" align="center">Need-by Date:</td>
        <td class="tabledetail" align="center">Priority:</td>
    </tr>

    <tr>
        <td class="tabledetail" height="30" align="center">
            <nested:write name="evaluateRequestForm" property="trackingNumber"/>
        </td>

        <td class="tabledetail" height="30" align="center">
            <nested:write name="evaluateRequestForm" property="evaluatorsEvaluationStatus"/>
        </td>
        <td class="tabledetail" height="30" align="center">
            <nested:present name="evaluateRequestForm" property="requestor">
                <nested:write name="evaluateRequestForm" property="requestor.lastAndFirstName"/>
            </nested:present>
        </td>
        <td class="tabledetail" align="center">
            <nested:write name="evaluateRequestForm" property="dateRequested" format="MM/dd/yyyy"/>
        </td>
        <td class="tabledetail" align="center">
            <nested:write name="evaluateRequestForm" property="needByDate" format="MM/dd/yyyy"/>
        </td>
        <td class="tabledetail" align="center">
            <nested:write name="evaluateRequestForm" property="priority.name"/>
        </td>
    </tr>

    <tr>
        <td class="tabledetail" align="center" colspan="6">
            <font color='red'>Here is the current Approval Summary for this Request</font><br>
            <b>****************************************************************************************</b><br/>
            <b>GENERAL Request Information:</b><br/>
            Tracking Number: <b> <nested:write name="evaluateRequestForm" property="trackingNumber"/></b><br>
        </td>
    </tr>
    <tr>
        <td class="tabledetail" align="center" colspan="6">
            <logic:equal name="evaluateRequestForm" property="RLIsNumber" value="1">
                There is <b><nested:write name="evaluateRequestForm"
                                          property="RLIsNumber"/></b> Line Item in this Request<br>
            </logic:equal>
            <logic:greaterThan name="evaluateRequestForm" property="RLIsNumber" value="1">
                There are <b><nested:write name="evaluateRequestForm"
                                           property="RLIsNumber"/></b> Line Items in this Request<br>
            </logic:greaterThan>
        </td>
    </tr>
    <tr>
        <td class="tabledetail" align="left" colspan="6">
            <nested:iterate id="materialsRequest" name="evaluateRequestForm" property="requestLineItemForms"
                            indexId="a">

                Request Line Item <b> ${a+1} </b> of <b> <nested:write name="evaluateRequestForm"
                                                                       property="RLIsNumber"/></b><br/>
                ReqquestLineItemId: <b><nested:write property="requestLineItemId"/></b><br/>
                Description: <b><nested:write property="itemDescription"/></b><br/>
                <nested:hidden property="requestLineItem2" value="requestLineItem"/>

                Status: <b><nested:write property="statusName"/></b><br/>
                <br/><br/>
                <b>****************************************************************************************</b><br/>
                &nbsp;&nbsp;&nbsp;&nbsp;<b>Approval Group Information:</b><br/>
                &nbsp;&nbsp;&nbsp;&nbsp;(Note that One or More Groups are listed below depending<br/>
                &nbsp;&nbsp;&nbsp;&nbsp;on how many Group Approvals are required for this request)<br/>
                <br/>

                <nested:iterate id="evaluations" property="requestEvaluations" indexId="a2">
                    <br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;----------------------------------------------------------------------------------<br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Approval Group # ${a2+1}:</b> <nested:write
                        property="evaluatorGroup.groupName"/>
                    &nbsp;&nbsp;&nbsp;<b>Level: <nested:write property="approvalLevel"/></b><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Decision:</b> <nested:write
                        property="evaluationDecision.name"/><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Date:</b> <nested:write
                        property="evaluationDate"/><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Person:</b>
                    <nested:notEmpty property="evaluator">
                        <nested:write property="evaluator.firstName"/> <nested:write
                            property="evaluator.lastName"/><br/>
                    </nested:notEmpty>
                    <br/>
                    <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>The following person(s) are able to grant approval for this Approval</b><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Group. Please contact one of them if you need to send a reminder</b><br/>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>to them to grant approval for your PRISM request.</b><br/>

                    <nested:iterate id="evaluators" property="groupMembers" indexId="a3">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<nested:write property="person.firstName"/>
                        <nested:write property="person.lastName"/><br/>
                    </nested:iterate>

                </nested:iterate>


            </nested:iterate>
        </td>
    </tr>
</table>
</body>
</html>