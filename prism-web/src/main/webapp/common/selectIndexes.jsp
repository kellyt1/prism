<%@ include file="../include/tlds.jsp" %>
<html>
    <head><title>Select Lucene Indexes</title></head>
    <body>
        <nested:form action="buildIndexes">
            <nested:hidden property="cmd" value=""/>
            <fieldset>
                <legend>Select Indexes</legend>
                <nested:iterate name="luceneIndexesForm" property="indexForms" id="indexForm">
                    <label>
                        <nested:checkbox property="selected"/>
                        <nested:write property="shortClassName"/>
                    </label>
                    <br/>
                </nested:iterate>
            </fieldset>
            <br/><br/>
            <div class="btn-group">
                <button type="submit" onclick="this.form.cmd.value='refreshIndexes'; this.form.submit(); return false;" class="btn btn-default">Build Selected Indexes</button>
                <button type="submit" onclick="this.form.cmd.value='createIndexes'; this.form.submit(); return false;" class="btn btn-danger" title="This can take a long time. If you're not sure, don't do it.">Delete and Recreate Selected Indexes</button>
            </div>
        </nested:form>
        <br/>
        <span class="bg-warning"><strong>Note: Do not select RequestLineItemIndex if you select RequestIndex</strong></span>
    </body>
</html>