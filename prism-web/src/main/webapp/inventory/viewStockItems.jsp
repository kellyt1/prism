<%@ taglib prefix="nested" uri="http://struts.apache.org/tags-nested" %>
<html>
    <head>
        <title>Maintain Stock Items</title>
    </head>
    <body>
        <form action="searchStockItems.do" method="POST" accept-charset="utf-8">
            <div class="row">
                <div class="col-md-4 col-md-offset-4">
                    <label for="keywords">Keyword Search</label>
                    <div class="input-group">
                        <nested:text name="searchCatalogForm" property="query" size="50" styleClass="form-control" styleId="keywords"/>
                        <div class="input-group-btn">
                            <label for="search" style="display: none;">Search</label>
                            <button class="btn btn-default" type="submit" id="search"><i class="glyphicon glyphicon-search"></i></button>
                        </div>
                    </div>
                    <p class="text-center">
                        <label><input type="checkbox" name="ckShowInactive" value="true">Show all items regardless of status.</label><br/>
                        <a href="viewAdvancedSearchStockItems.do">Advanced Search</a>
                    </p>
                </div>
            </div>
        </form>
    </body>
</html>
