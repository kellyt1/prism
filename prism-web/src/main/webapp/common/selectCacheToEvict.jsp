<%@ include file="../include/tlds.jsp" %>

<html>
    <head><title>Select Cache Objects to Evict/Clear</title></head>
    <body>
        <nested:form action="evictCache">
            <fieldset>
                <legend>Select Cache Objects to Evict/Clear</legend>
                <nested:iterate  property="indexForms" id="indexForm">
                    <label>
                        <nested:checkbox property="selected"/>
                        <nested:write property="index"/>
                    </label>
                    <br/>
                </nested:iterate>
                <br/>
                <label>Enter a class name. Include the Package: <input name="CLASSTOEVICT" size="50"/></label>
            </fieldset>
            <br/>
            <br/>
            <html:submit value="Evict Selected Caches" styleClass="btn btn-default"/>
        </nested:form>
    </body>
</html>