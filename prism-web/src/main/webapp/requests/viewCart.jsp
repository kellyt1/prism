<%@ taglib uri="http://struts.apache.org/tags-nested" prefix="nested" %>

<html>
    <head>
        <title>Shopping Cart</title>
    </head>
    <body>
        <nested:form action="/editShoppingCart" method="post" enctype="multipart/form-data">
            <nested:hidden property="cmd" value=""/>
            <nested:hidden property="rliFormIndex" value=""/>
            <input type="HIDDEN" name="shoppingListAction" value="addCart" />
            <%@ include file="requestLineItemsInclude.jsp"%>
            <br/>
            <div class="text-center">
                <button onclick="this.form.cmd.value='saveCart'; this.form.submit(); return false;" class="btn btn-default">Update Shopping Cart</button>
                <button onclick="this.form.cmd.value='addToShoppingList'" class="btn btn-default">Add Cart to Shopping List</button>
                <button onclick="this.form.cmd.value='viewCheckout';this.form.submit(); return false;" class="btn btn-default">Checkout</button>
            </div>
        </nested:form>
    </body>
</html>