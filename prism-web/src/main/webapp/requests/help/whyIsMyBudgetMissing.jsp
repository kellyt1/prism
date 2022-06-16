<%@ include file="../../include/tlds.jsp" %>


<html>
    <head>
        <title>Setting the Deliver-To information</title>
    </head>
    <body>
        <div class="row text-center">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">Why is my budget missing?</div>
                    <div class="panel-body">
                        <p>
                            Your budget has not been activated yet. Please contact Financial Management at
                            <a href="mailto:Health.FM-Budget-Questions@state.mn.us?subject=Missing PRISM Budgets&body=I would like to request that the following budget be added to Prism: %0D%0A %0D%0A Fin Dept Id:%0D%0A Project: %0D%0A Activity Number:">Missing
                            PRISM Budgets.</a> Include SWIFT fin dept id, project and activity numbers, if available.
                        </p>
                    </div>
                </div>
            </div>
        </div>
        <div class="row text-center">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">Budgets without Approvers</div>
                    <div class="panel-body">
                        <p>Below is a list of budgets that do not have approvers setup in PRISM</p>
                        <br/>
                        <nested:iterate property="fundingSourceForms" id="requestLineItemFundingSourceForm" name="requestLineItemForm" indexId="a">
                            <nested:select property="orgBudgetId" styleClass="chosen-select"  onchange="$j(this).updateBudgetEmailBody()">
                                <option value=""></option>
                                <nested:optionsCollection property="orgApproverMissing" label="orgBudgetCodeAndNameAndFY" value="orgBudgetId"/>
                            </nested:select>
                        </nested:iterate>
                        <br/><br/>
                        <p>Please provide a list of approvers and the threshold amounts if < $500.00 and email it to
                            <a class="budgetEmail" href="mailto:Health.FM-Budget-Questions@state.mn.us?subject=PRISM approval information">Missing PRISM Approvers</a>
                        </p>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>