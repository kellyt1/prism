package us.mn.state.health.common.taglib.html;
import javax.servlet.jsp.JspException;

public class SortGroup extends BaseHtmlTag {

    //Attributes
    
    /**
     * The body content of this tag (if any).
     */
    protected String text = null;
    
    /**
     * The relative web app context of the "sort" action
     */
    private String context;
    
    /**
     * The page to return to
     */
    private String input;
    
    private String sessionKey;
    
    public int doStartTag() throws JspException {
        try {
            return 0;
        }
        catch(Exception e) {
            throw new JspException(e);
        }
    }
}