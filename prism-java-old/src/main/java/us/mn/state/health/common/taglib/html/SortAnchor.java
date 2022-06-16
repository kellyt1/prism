package us.mn.state.health.common.taglib.html;

import java.util.HashMap;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;

public class SortAnchor extends BaseHtmlTag {

    //Attributes
    
    /**
     * The body content of this tag (if any).
     */
    protected String text = null;
    
    /**
     * The page to return to
     */
    private String input;
    
    private String sessionKey;
    

   /**
     * Render the beginning of the hyperlink.
     * <p>
     * Support for indexed property since Struts 1.1
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        //Create map to describe this sort and put in session
        HashMap sortDef = new HashMap();
        sortDef.put("name", getName());
        sortDef.put("property", getProperty());
        sortDef.put("value", getValue());
        sortDef.put("input", input);
        sessionKey = getName() + getProperty() + getValue();
        pageContext.getSession().setAttribute(sessionKey, sortDef);
        
        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a");
        results.append(" href=\"list.sort?sortKey=" + sessionKey + "\"");
        if(getStyle() != null) {
            results.append(" style=\"" + getStyle() + "\" ");
        }
        results.append(">");
        JspWriter writer = pageContext.getOut();
        try {
            writer.write(results.toString());
        }
        catch(Exception e) {
            throw new JspException(e);
        }
        
        return (EVAL_BODY_BUFFERED);

    }
    
    /**
     * Save the associated label from the body content.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0)
                text = value;
        }
        return (SKIP_BODY);

    }
    
    /**
     * Render the end of the hyperlink.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Prepare the textual content and ending element of this hyperlink
        StringBuffer results = new StringBuffer();
        if (text != null) {
            results.append(text);
        }
        results.append("</a>");
        results.append("<input type=\"hidden\" name=\"sortKey\" />");
        JspWriter writer = pageContext.getOut();
        try {
            writer.write(results.toString());
        }
        catch(Exception e) {
            throw new JspException(e);
        }

        return (EVAL_PAGE);
    }
    
    /**
     * Release any acquired resources.
     */
    public void release() {
        super.release();
        this.text = null;
        this.input = null;
        this.sessionKey = null;
    }


    public void setText(String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setInput(String input) {
        this.input = input;
    }


    public String getInput() {
        return input;
    }

}