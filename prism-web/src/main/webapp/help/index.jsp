<%@ include file="../include/tlds.jsp" %>

<div class="row">
    <div class="col-xs-12">
        <article class="text-center">
            <p>
                <logic:notEqual name="skin" value="PRISM2">
                    <a href="http://wiki.health.state.mn.us/pub/Know/AppDev/PRISM_tutorial.doc">PRISM User Guide</a>.
                </logic:notEqual>
                <logic:equal name="skin" value="PRISM2">
                    <a href="http://wiki.health.state.mn.us/pub/Know/AppDev/parituserguide.doc" target="_blank">PARIT User Guide</a>.
                </logic:equal>
            </p>
            <p>For help with Attaching Documents, click <a href="AttachDoc.doc">here</a>.</p>
            <br/>
            <p>For technical support, please contact the MN.iT service desk:</p>
            <p>Online: <a href="https://fyi.web.health.state.mn.us/open/helpdesk/index.cfm" target="_blank">Click Here</a></p>
            <p>E-Mail: <a href="mailto:health.ISTMHELPDESK@state.mn.us">health.ISTMHELPDESK@state.mn.us</a></p>
            <p>Telephone: 651-201-5555</p>
        </article>
    </div>
</div>