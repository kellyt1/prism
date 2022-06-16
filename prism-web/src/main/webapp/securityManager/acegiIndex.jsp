<%@ include file="../include/tlds.jsp" %>
<%@ page import="org.acegisecurity.AuthenticationException" %>
<%@ page import="org.acegisecurity.ui.AbstractProcessingFilter" %>

<html>
    <head>
        <title id="prism">Login</title>
    </head>
    <body>
        <div class="row">
            <div class="col-md-12 text-center">
                <p class="small"></p>
                <br>
                <form action="<c:url value="/j_acegi_security_check"/>" method="POST">
                    <fieldset>
                        <legend>Please Sign In To Proceed</legend>
                        <span class="help-block">Use your Network Login/Password (The same as used upon initial daily login.)</span>
                        <span class="help-block">Your username is not case-sensitive, but your password IS case-sensitive.</span>
                        <label>Username<input type='text' name='j_username' class="form-control" placeholder="Username" autofocus="true"></label><br/>
                        <label>Password<input type='password' name='j_password' class="form-control" placeholder="Password"></label><br/>
                        <nested:hidden property="authentication" value="AD" />
                        <br/>
                        <div class="btn-group">
                            <input name="Login" type="submit" value="Login" class="btn btn-default">
                            <input name="reset" type="reset" class="btn btn-default">
                        </div>
                    </fieldset>
                </form>
                <br/>
                <c:if test="${not empty param.login_error}">
                    <div class="bg-danger">Your login attempt was not successful, try again.<br>Reason: <%= ((AuthenticationException) session.getAttribute(AbstractProcessingFilter.ACEGI_SECURITY_LAST_EXCEPTION_KEY)).getMessage() %></div>
                </c:if>
                <br/>
                <% if (request.getRequestURL().toString().toLowerCase().contains("-dev") || request.getRequestURL().toString().toLowerCase().contains("-stage") || request.getRequestURL().toString().toLowerCase().contains("-test") || request.getRequestURL().toString().toLowerCase().contains("wsdjv") || request.getRequestURL().toString().toLowerCase().contains("localhost")) { %>
                    <p class="bg-danger"><strong>This is a PRISM TEST system. For PRISM PRODUCTION system click <a href="https://prism.web.health.state.mn.us/">HERE!</a></strong></p>
                <% } %>
            </div>
         </div>
     </body>
</html>
