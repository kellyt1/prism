<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>

<div align="center">
    <html:messages id="msg" message="true">
        <bean:write name="msg"/><br>
    </html:messages>
</div>