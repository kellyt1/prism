<%@ include file="../include/tlds.jsp" %>

<html>
<head><title>Index Selection Confirmation</title></head>
<body>
You requested to rebuild these indexes:<br/>
<ol>
    <nested:iterate name="luceneIndexesForm" property="indexForms" id="indexForm">
        <nested:equal property="selected" value="true">
            <li><nested:write property="shortClassName"/></li>
        </nested:equal>
    </nested:iterate>
</ol>
</body>
</html>