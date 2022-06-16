<%@ taglib prefix="menu" uri="http://struts-menu.sf.net/tag-el" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<%@ page import="us.mn.state.health.common.exceptions.InfrastructureException" %>
<%@ page import="us.mn.state.health.dao.configuration.HibernateConfigurationItemDAO" %>
<%@ page import="us.mn.state.health.matmgmt.util.Constants" %>
<%@ page import="us.mn.state.health.model.common.OrgBudget" %>
<%@ page import="us.mn.state.health.model.inventory.Item" %>
<%@ page import="us.mn.state.health.model.materialsrequest.Request" %>
<%@ page import="us.mn.state.health.model.util.configuration.ConfigurationItem" %>
<%@ page import="us.mn.state.health.util.VersionProvider" %>

<!DOCTYPE html>
<html lang="en">
<%
    Request shoppingCart = (Request)request.getSession().getAttribute("shoppingCart");
    application.setAttribute("indirect", OrgBudget.INDIRECT);
    application.setAttribute("stockItem", Item.STOCK_ITEM);

    if (session.getAttribute("skin") == null) {
        session.setAttribute("skin","reset");
    }
    if (request.getParameter("skin") != null) {
        session.setAttribute("skin",request.getParameter("skin"));
    }

    if(shoppingCart == null) shoppingCart = new Request();
%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>${skin == "PRISM2" ? "PARIT" : "PRISM"} - <decorator:title default="Home"/></title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/bootstrap/3.3.5/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/jquery-ui/1.11.4/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.ui.theme.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/main.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/chosen/1.3.0/chosen.min.css"/>

    <script type="text/javascript" src="${pageContext.request.contextPath}/webjars/jquery/2.1.4/jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/webjars/prototype/1.7.1.0/prototype.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/ajaxtags-1.2-beta2.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/webjars/chosen/1.3.0/chosen.jquery.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.jquery.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/common.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/sortable_mod.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/webjars/jquery-ui/1.11.4/jquery-ui.min.js"></script>
    <script type="text/javascript">var $j = jQuery.noConflict();</script>
    <decorator:head />
</head>
<body>
<header class="hidden-print">
    <a href="#pagecontent" class="sr-only">Skip to main content</a>
    <div id="mdhHeaderDiv" class="row">
        <%--<div id="headerLeftSide" class="col-sm-5">--%>
            <%--<div id="mdhLogoDiv">--%>
                <%--<a href="http://www.health.state.mn.us/">--%>
                    <%--<img src="${pageContext.request.contextPath}/images/logo.gif" alt="MDH Logo"/>--%>
                    <%--<span class="mdhText">Minnesota<br/>Department<br/>of Health</span>--%>
                <%--</a>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div id="headerRightSide" class="col-sm-7 hidden-xs">--%>
            <%--<div id="appTitleDiv" class="text-center col-md-9 col-sm-10">${skin == "PRISM2" ? "PARIT" : "PRISM"}</div>--%>
        <%--</div>--%>
    <%--</div>--%>

            <div>
                <div class="mdhLogo">
                    <a href="http://www.health.state.mn.us/">
                <img src="${pageContext.request.contextPath}/images/logo-mdh-mn-h-whi_rgb.png" alt="MDH Logo"/>
                    </a>
                </div>
                <div class="appTitleDiv">${skin == "PRISM2" ? "PARIT" : "PRISM"}</div>
            </div>

    </div>
    <nav class="navbar navbar-default" role="navigation">
        <div class="navbar-header">
            <a href="${pageContext.request.contextPath}/index.jsp" class="navbar-brand">Home</a>
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <menu:useMenuDisplayer name="Velocity" bundle="org.apache.struts.action.MESSAGE" config="./templates/cssMenu.html" permissions="rolesAdapter">
                    <logic:equal name="skin" value="PRISM2">
                        <menu:displayMenu name="SCOMPAdmin"/>
                        <menu:displayMenu name="PurchasingMNIT"/>
                        <menu:displayMenu name="SCOMP"/>
                        <menu:displayMenu name="SCOMPView"/>
                        <menu:displayMenu name="SCOMP_TabbedHelp"/>
                    </logic:equal>
                    <logic:notEqual name="skin" value="PRISM2">
                        <menu:displayMenu name="Request"/>
                        <menu:displayMenu name="Purchasing"/>
                        <menu:displayMenu name="Editor"/>
                        <menu:displayMenu name="Finance"/>
                        <menu:displayMenu name="Inventory"/>
                        <menu:displayMenu name="Receiving"/>
                        <menu:displayMenu name="GeneralAdmin"/>
                        <%--<menu:displayMenu name="SystemAdmin"/>--%>
                        <menu:displayMenu name="Reports"/>
                        <menu:displayMenu name="Help"/>
                    </logic:notEqual>
                </menu:useMenuDisplayer>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="#" data-toggle="dropdown">
                        <i class="glyphicon glyphicon-shopping-cart">
                            <span class="badge pull-right"><%=shoppingCart.getRequestLineItems().size()%></span>
                        </i>
                        <span class="visible-xs-inline"> Shopping Cart</span>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="${pageContext.request.contextPath}/viewShoppingCart.do">View Cart</a></li>
                        <li><a href="${pageContext.request.contextPath}/viewCheckout.do">Checkout</a></li>
                    </ul>
                </li>
                <li class="dropdown">
                    <a href="#" data-toggle="dropdown">
                        <i class="glyphicon glyphicon-user"></i>
                        <span class="visible-xs-inline"> User Menu</span>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li><a href="${pageContext.request.contextPath}/viewMaintainShoppingLists.do">My Shopping Lists</a></li>
                        <li><a href="${pageContext.request.contextPath}/viewMyRequests.do">View My Requests</a></li>
                        <li><a href="${pageContext.request.contextPath}/j_acegi_logout">Logout</a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>

</header>
<div class="contentWrapper">
    <div class="mainContent container-fluid" id="pagecontent">
        <div class="row hidden-print">
            <div class="col-md-12 text-center" style="margin-bottom: 25px;">
                <% HibernateConfigurationItemDAO hcidao = new HibernateConfigurationItemDAO();
                    ConfigurationItem ci = null;
                    String pageName = (String) session.getAttribute("page_name");
                    if (pageName != null && pageName.equals("LOGIN_PAGE")) {
                        try {
                            ci = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_LOGIN);
                        } catch (InfrastructureException ignore) {
                        }
                    }
                    else {
                        try {
                            ci = hcidao.getConfigurationItem(Constants.MATERIALS_MANAGMENT_CODE, Constants.MESSAGE_NOTIFY_MAINMENU);
                        } catch (InfrastructureException ignore) {
                        }
                    }
                    if (!((ci == null) || (ci.getValue() == null) || (ci.getValue().trim().equals("")))) {
                        out.print(ci.getValue());
                    }
                    session.removeAttribute("page_name");
                %>
            </div>
            <div class="col-md-12">
                <html:errors/>
            </div>
        </div>
        <decorator:body />
    </div>
</div>
<footer class="hidden-print">
    <p class="small">
        To report problems with this application, please <a href="https://fyi.web.health.state.mn.us/open/helpdesk/index.cfm" target="_blank">contact the MN.iT service desk</a>.
    </p>
    <logic:notEqual name="skin" value="PRISM2">
        <p class="small">
            PRISM <a href='http://wiki.health.state.mn.us/bin/view/Know/PrismVersions' target='_blank'><%= VersionProvider.getVersion() %></a>
        </p>
        <p>
            <span class="text-danger">Please read revised policy 802.05:</span> <a href="https://mn365.sharepoint.com/teams/MDH/permanent/pp/SitePages/Home.aspx">Purchase of Non-Office Supplies, Equipment or Non-Professional Services</a>.<br>
            The policy has been completely rewritten to reflect the use of PRISM.<br>If you have concerns, please contact the Accounting Operations Unit Supervisor at 651-201-4643.
        </p>
    </logic:notEqual>
</footer>

<script type="text/javascript">
    <%--Prevent namespace collisions between Bootstrap and Prototype libraries--%>
    if (Prototype.BrowserFeatures.ElementExtensions) {
        var disablePrototypeJS = function (method, pluginsToDisable) {
                    var handler = function (event) {
                        event.target[method] = undefined;
                        setTimeout(function () {
                            delete event.target[method];
                        }, 0);
                    };
                    pluginsToDisable.each(function (plugin) {
                        $j(window).on(method + '.bs.' + plugin, handler);
                    });
                },
                pluginsToDisable = ['collapse', 'dropdown', 'modal', 'tooltip'];
        disablePrototypeJS('show', pluginsToDisable);
        disablePrototypeJS('hide', pluginsToDisable);
    }

    <%--This section is knowingly hacky.  It competes with the 'chosen' js library to apply styling to the budgtet expiration dates--%>
    $j("#bdgt option").each(function () {
        this.text = this.text.replace(/Ending:(\d{4}-\d{2}-\d{2})/, "Ending:<span style=\"color:red\">$1</span>");
    });
    $j(document).ready(function(){
        $j("#bdgt_chosen .chosen-single span").each(function () {
            this.outerHTML = this.outerHTML.replace(/&lt;(.*?)&gt;/g, "<$1>");
            $j("#bdgt_chosen .chosen-single span span").each(function () {
                this.outerHTML = this.outerHTML.replace(/(style="color:red)/, "$1; display:inline");
            });
        });
    });
    function styleSelectedExpirationDate() {
        $j("#bdgt_chosen .chosen-single span").each(function () {
            this.outerHTML = this.outerHTML.replace(/&lt;(.*?)&gt;/g, "<$1>");
            $j("#bdgt_chosen .chosen-single span span").each(function () {
                this.outerHTML = this.outerHTML.replace(/(style="color:red)/, "$1; display:inline");
            });
        });
    }
    <%--End of inline budget expiration date js styling hacks--%>

    <%--Call to chosen plugin. Adds search functionality to select inputs.--%>
    $j('.chosen-select').chosen({
        search_contains: true,
        allow_single_deselect: true,
        disable_search_threshold: 10,
        no_results_text: "Oops, nothing found!"
    });
    <%--Adds jquery ui dateinput on date fields.--%>
    $j('.dateInput').datepicker({
        dateFormat: 'mm/dd/yy',
        changeMonth: true,
        changeYear: true
    });
    <%--Adds jquery ui tooltips to any element using a title attribute.--%>
    $j(function() {
        $j(document).tooltip();
    });

    <%--Workaround to populate the fields on page load that are normally calculated after an onblur event.--%>
    $j('.blurOnLoad').each(function(){
        $j(this).trigger('blur');
    });

    <%--Workaround for Bootstrap dropdown sub-menus --%>
    $j('ul.dropdown-menu [data-toggle=dropdown]').on('click', function(event) {
        // Avoid following the href location when clicking
        event.preventDefault();
        // Avoid having the menu close when clicking
        event.stopPropagation();
        // Re-add .open to parent sub-menu item
        if($j(this).parent().hasClass('open')){
            $j(this).parent().removeClass('open');
            $j(this).parent().find("ul").parent().first("li.dropdown").removeClass('open');
        }
        else {
            $j(this).parent().addClass('open');
            $j(this).parent().find("ul").parent().first("li.dropdown").addClass('open');
        }
    });
</script>
</body>
</html>