<%@ include file="../include/tlds.jsp" %>

<html>
    <head>
        <title>My Shopping Lists</title>
    </head>
    <body>
        <html:form action="addToShoppingList" method="post">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <div class="panel panel-default">
                        <div class="panel-heading">Which Shopping List would you like to add this item to?</div>
                        <div class="panel-body">
                            <nested:notEmpty property="shoppingLists">
                                <div class="row">
                                    <div class="col-md-6">
                                        <label><html:radio property="existingList" value="true" /> Select an existing Shopping List</label>
                                    </div>
                                    <div class="col-md-6">
                                        <label for="shoppingLists" class="hidden">Shopping Lists</label>
                                        <nested:select property="shoppingListId" styleClass="chosen-select" styleId="shoppingLists">
                                            <nested:optionsCollection property="shoppingLists" label="name" value="shoppingListId"/>
                                        </nested:select>
                                    </div>
                                </div>
                            </nested:notEmpty>
                            <div class="row">
                                <div class="col-md-12">
                                    <label>
                                        <nested:notEmpty property="shoppingLists">
                                            <html:radio property="existingList" value="false"/>
                                        </nested:notEmpty>
                                        <nested:empty property="shoppingLists">
                                            <html:hidden property="existingList" value="false"/>
                                        </nested:empty>
                                        Create a new Shopping List
                                    </label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="name">Shopping List Name</label>
                                </div>
                                <div class="col-md-6">
                                    <nested:text size="40" property="shoppingListForm.name" styleId="name" styleClass="form-control"/>
                                </div>
                            </div>
                            <br/>
                            <div class="row">
                                <div class="col-md-6">
                                    <label for="comments">Comments</label>
                                </div>
                                <div class="col-md-6">
                                    <nested:textarea cols="30" rows="2" property="shoppingListForm.comment" styleId="comments" styleClass="form-control"/>
                                </div>
                            </div>
                        </div>
                        <div class="panel-footer text-center">
                            <div class="btn-group">
                                <button type="submit" class="btn btn-default">Add To Shopping List</button>
                                <button onclick="history.go(-1);" class="btn btn-default">Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </html:form>
    </body>
</html>