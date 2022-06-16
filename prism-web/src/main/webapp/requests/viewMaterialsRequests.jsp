<%@ include file="../include/tlds.jsp" %>
<html>
    <head>
        <title>Evaluate Materials Requests</title>
        <script type="text/javascript">
            function showhide(id) {
                var e = document.getElementById(id);
                e.style.display = (e.style.display == 'block') ? 'none' : 'block';
            }
        </script>
    </head>

    <body>
        <table class="table table-bordered table-striped">
            <thead>
                <tr>
                    <th scope="col">Request Number:<br>(Click for details)</th>
                    <th scope="col">Evaluators<br>Evaluation Status:</th>
                    <th scope="col">Requestor:</th>
                    <th scope="col">Date Requested:</th>
                    <th scope="col">Need-by Date:</th>
                    <th scope="col">Priority:</th>
                </tr>
            </thead>
            <tbody>

                <nested:iterate id="materialsRequest" name="requestFormCollection" property="requestForms" indexId="a">
                    <tr>
                        <td>
                            <a id="<%=a%>" href="${pageContext.request.contextPath}/viewEvaluateMaterialsRequest.do?requestId=<nested:write property='requestId'/>"><nested:write property="trackingNumber" /></a>
                        </td>
                        <td>
                            <a href="javascript:showhide('mrqDetails<%=a%>')" style="background-color: #ccc; padding: 5px 10px;">Show/Hide Detail</a>
                            <div id="mrqDetails<%=a%>" style="display:none;" >
                            <nested:iterate id="materialsRequest2" name="materialsRequest" property="requestLineItemForms"
                                            indexId="a">
                                <b>****************************************************************************************</b><br/>
                                <nested:notEmpty property="itemDescription">
                                   <b> Description: <nested:write property="itemDescription"/></b><br/>
                                </nested:notEmpty>
                                <nested:notEmpty property="item">
                                    <b> Description: <nested:write property="item.description" filter="false"/></b><br/>
                                </nested:notEmpty>


                                    <nested:iterate id="evaluations" property="requestEvaluations" indexId="a2">
                                        <br/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;----------------------------------------------------------------------------------<br/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Approval Group # ${a2+1}:</b> <nested:write
                                            property="evaluatorGroup.groupName"/>
                                        &nbsp;&nbsp;&nbsp;<b>Level: <nested:write property="approvalLevel"/></b><br/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Decision:</b> <nested:write
                                            property="evaluationDecision.name"/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Date:</b> <nested:write
                                            property="evaluationDate"/>
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Person:</b>
                                        <nested:notEmpty property="evaluator">
                                            <nested:write property="evaluator.firstName"/> <nested:write
                                                property="evaluator.lastName"/><br/>
                                        </nested:notEmpty>
                                        <br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>The following person(s) are able to grant approval for this group</b><br/>
                                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Group. Please contact one of them if you need to send a reminder</b><br/>--%>
                                        <%--&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>to them to grant approval for your PRISM request.</b><br/>--%>
                                        <nested:iterate id="evaluators" property="groupMembers" indexId="a3">
                                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<nested:write property="person.firstName"/>
                                            <nested:write property="person.lastName"/>,
                                        </nested:iterate>
                                        <br/>
                                </nested:iterate>
                            </nested:iterate>
                            </div>
                        </td>
                        <td><nested:present property="requestor"><nested:write property="requestor.lastAndFirstName"/></nested:present></td>
                        <td><nested:write property="dateRequested" format="MM/dd/yyyy" /></td>
                        <td><nested:write property="needByDate" format="MM/dd/yyyy" /></td>
                        <td><nested:write property="priority.name" /></td>
                  </tr>
                </nested:iterate>
            </tbody>
        </table>
    </body>
</html>