<%@ page import="us.mn.state.health.security.*" %>
<%@ page import="us.mn.state.health.security.command.*" %>
<%@ page import="us.mn.state.health.security.view.*" %>

<script type="text/javascript">
	function checkBlank(element){
		if(element.value == ""){
			element.focus();
			return true;
		}
		return false;	
	}
	
	function doSetChallengeQuestion(){ 
        if(checkBlank(document.setChallengeQuestionForm.challengeQuestion)){
			alert("Please choose a question from the list.");
			return false;
		}
		if(checkBlank(document.setChallengeQuestionForm.challengeAnswer)){
			alert("Please enter your answer.");
			return false;
		}
		document.setChallengeQuestionForm.cmd.value = "<%= Commands.SET_CHALLENGE_QUESTION %>"; //cmd=SetChallengeQuestion
		document.setChallengeQuestionForm.submit();
        return true;
	}
    
     /* override the onKeyPress method to make the Enter key submit the form */
    function onKeyPress(event) {
        var keycode;
        if(window.event) {
            keycode = window.event.keyCode;
        }
        else if(event) {
            keycode = event.which;
        }
        else {
            return true;
        }
        if(keycode == 13) {
            doSetChallengeQuestion();
        }
        return true; 
    }
    document.onkeypress = onKeyPress;
</script>

<%--<%@ include file="../headers/header.jsp" %>--%>
<br />
<br />
<%
	/* Users should never come directly to this page. The viewHelper attribute must be
		on the session for this page to work correctly.  If its not, redirect to login page
	 ***************************************************************************************/
	ViewHelper viewHelper =  (ViewHelper)session.getAttribute(ApplicationResources.VIEW_HELPER);
	if(viewHelper == null){
		%><jsp:forward page="./login.jsp" /><%
	}
	
	//use the view helper to get any message to be displayed
	String message = viewHelper.getMessage();
	
	//use the view helper to get the username
	String username = viewHelper.getUser().getUsername();
	
	//use the view helper to get the fullName
	String fullName = viewHelper.getUser().getFirstAndLastName();
%>
  
<p align="center">Welcome, <%= fullName %>!</p>

<%
	//display the message if there is one... 
	if(message == null){
		message = "";
	}
%>
<form name="setChallengeQuestionForm" method="post" action="${pageContext.request.contextPath}/securityManager/Controller" AUTOCOMPLETE="OFF" >
	<table align="center" width="65%">
        <tr>
            <td colspan="2" class="small">
                <%=message %><br />
                A challenge question is a question that we will use to verify your identity 
                in case you forget your password. Also, in order to change your password later, 
                the system will prompt you with the challenge question and you will provide an answer.	
            </td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
		<tr>
			<td class="normal_text" align="right">Challenge Question:</td>
			<td class="normal_text">
				<select name="challengeQuestion">
                    <option value=""></option>
					<option value="What is your mother's maiden name?">What is your mother's maiden name?</option>
					<option value="What is your high school mascot?">What is your high school mascot?</option>
					<option value="What is your childhood pet's name?">What is your childhood pet's name?</option>
					<option value="What is your first child's middle name?">What is your first child's middle name?</option>
					<option value="What are the last 4 numbers of your social security number?">What are the last 4 numbers of your social security number?</option>
					<option value="Who is your favorite musical performer/group?">Who is your favorite musical performer/group?</option>
					<option value="What city were you born in?">What city were you born in?</option>
					<option value="What college did you graduate from?">What college did you graduate from?</option>
					<option value="What was your grade-school principle's name?">What was your grade-school principle's name?</option>
                    <option value="What is your favorite sports team?">What is your favorite sports team?</option>
                    <option value="Where was your first job?">Where was your first job?</option>
				</select>
			 </td>
		</tr>
		<tr>
			 <td class="normal_text" align="right">Challenge Answer:</td>
             <td class="normal_text"><input name="challengeAnswer" type="text" size="15" /></td>
		</tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td >
                <input type="button" onClick="doSetChallengeQuestion();" name="Submit" value="Submit" />
                &nbsp;&nbsp;
                <input type="button" onClick='document.setChallengeQuestionForm.action="login.jsp";document.setChallengeQuestionForm.submit();' name="cancel" value="Cancel" />
            </td>
        </tr>
	</table>

	<p>
	<input type="hidden" name="cmd" id="cmd" />
	<input type="hidden" name="username" id="username" value="<%= username %>" />
</form>