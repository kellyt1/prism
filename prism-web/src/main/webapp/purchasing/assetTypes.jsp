<%--
  Created by IntelliJ IDEA.
  User: kiminn1
  Date: 10/17/2017
  Time: 10:03 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="../include/tlds.jsp" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.5/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
</head>
<body>
<header>
    <div id="mdhHeaderDiv" class="row">
            <div>
                <div class="mdhLogo">
                    <a href="http://www.health.state.mn.us/">
                        <img src="${pageContext.request.contextPath}/images/logo-mdh-mn-h-whi_rgb.png" alt="MDH Logo"/>
                    </a>
                </div>
                <div class="appTitleDiv">${skin == "PRISM2" ? "PARIT" : "PRISM"}</div>
            </div>
    </div>
</header>

<div class="container-fluid">
    <h2>Asset Types</h2>
    <div class="panel panel-body">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                </tr>
            </thead>
            <tbody>
            <nested:iterate name="orderLineItemForm" property="assetTypes">
                <tr>
                    <td><nested:write property="label"/></td>
                    <td><nested:write property="description"/></td>
                </tr>
            </nested:iterate>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
