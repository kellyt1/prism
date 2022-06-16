<% String additionalInstructions = request.getParameter("instructions"); %>

<html>
    <head>
        <title>Additional Instructions</title>
    </head>
    <body>
        <h1>Special Instructions</h1>
        <p><%=additionalInstructions%></p>
        <input type="button" value="Close" onclick="window.close()">
    </body>
</html>