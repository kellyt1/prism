<%@ include file="../include/tlds.jsp" %>

<html>
<head>
    <title>Checkout Error!</title>
</head>

<body>
    <nested:form action="/checkout" method="post">
        <p class="text-center text-warning">
            THE SYSTEM HAS ENCOUNTERED AN ERROR.  YOUR REQUEST WAS NOT SAVED. PLEASE LOG OUT, LOG IN AND 
            TRY AGAIN. IF YOU RECEIVE THIS ERROR MESSAGE A SECOND TIME FOR THE SAME 
            REQUEST, PLEASE IMMEDIATELY INFORM THE MN.iT SERVICE DESK.
        </p>
    </nested:form>
</body>
</html>