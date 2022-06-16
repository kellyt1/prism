<%@ include file="../include/tlds.jsp" %>

<html>
    <head><title>Prism Admin Menu</title></head>
    <body>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font size="5" color="red">Prism Administrator Menu</font><br>

        <br><span size="3">
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="selectIndexes.do">Work with Lucene Indexes.</a><br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="selectCacheToEvict.do">Selectively Clear Cached Objects.</a><br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;View Web Server Logs:
            <a href="http://wstas01.health.state.mn.us/cgi-bin/istm/view_container.pl?site=v10&cnt=10000">TEST</a>&nbsp;&nbsp;&nbsp;
            <a href="http://wssas01.health.state.mn.us/cgi-bin/istm/view_container.pl?site=v10&cnt=10000">STAGE</a>&nbsp;&nbsp;&nbsp;
            <a href="http://wspas01.health.state.mn.us/cgi-bin/istm/view_container.pl?site=v10&cnt=10000">PROD</a>&nbsp;&nbsp;&nbsp;
            <a href="https://wslog02.health.state.mn.us:8000/">splunk</a>&nbsp;&nbsp;&nbsp;
            <br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;View PRISM Usage:
            <a href="http://fyi.health.state.mn.us/login/fusebox/extparty/index.cfm?fuseaction=AppUsage">Usage</a><br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="changePrismNotifications.do">Change Prism Notification Messages.</a><br><br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="changeBudgetBuilderLink.do">Change Budget Builder Link.</a><br><br>
        </span>
    </body>
</html>