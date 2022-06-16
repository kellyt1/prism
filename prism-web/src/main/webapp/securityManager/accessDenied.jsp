<%@ page import="org.acegisecurity.context.SecurityContextHolder" %>
<%@ page import="org.acegisecurity.Authentication" %>
<%@ page import="org.acegisecurity.ui.AccessDeniedHandlerImpl" %>

<h1>Sorry, access is denied</h1>

<p><%= request.getAttribute(AccessDeniedHandlerImpl.ACEGI_SECURITY_ACCESS_DENIED_EXCEPTION_KEY)%></p>
<p>
    <%		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) { %>
                Authentication object as a String: <%= auth.toString() %><BR><BR>
    <%      } %>
</p>
<h5>Please forward the following information to Prism Support</h5>
<a href="mailto:health.istmappdev@state.mn.us; health.istmappdev_emails@state.mn.us">Prism support</a>
