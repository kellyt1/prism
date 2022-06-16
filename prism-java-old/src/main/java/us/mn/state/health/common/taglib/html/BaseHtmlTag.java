package us.mn.state.health.common.taglib.html;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.taglib.TagUtils;

public class BaseHtmlTag extends BodyTagSupport {


    private String name = null;
    
    private String property = null;
    
    private String value = null;

    /** Mouse click event. */
    private String onclick = null;

    /** Mouse double click event. */
    private String ondblclick = null;

    /** Mouse over component event. */
    private String onmouseover = null;

    /** Mouse exit component event. */
    private String onmouseout = null;

    /** Mouse moved over component event. */
    private String onmousemove = null;

    /** Mouse pressed on component event. */
    private String onmousedown = null;

    /** Mouse released on component event. */
    private String onmouseup = null;

    //  Keyboard Events

    /** Key down in component event. */
    private String onkeydown = null;

    /** Key released in component event. */
    private String onkeyup = null;

    /** Key down and up together in component event. */
    private String onkeypress = null;

    // Text Events

    /** Text selected in component event. */
    private String onselect = null;

    /** Content changed after component lost focus event. */
    private String onchange = null;

    // Focus Events and States

    /** Component lost focus event. */
    private String onblur = null;

    /** Component has received focus event. */
    private String onfocus = null;

    /** Component is disabled. */
    private boolean disabled = false;

    /** Component is readonly. */
    private boolean readonly = false;

    // CSS Style Support

    /** Style attribute associated with component. */
    private String style = null;

    /** Named Style class associated with component. */
    private String styleClass = null;

    /** Identifier associated with component.  */
    private String styleId = null;


    //Properties
    
    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }


    public String getOnclick() {
        return onclick;
    }


    public void setOndblclick(String ondblclick) {
        this.ondblclick = ondblclick;
    }


    public String getOndblclick() {
        return ondblclick;
    }


    public void setOnmouseover(String onmouseover) {
        this.onmouseover = onmouseover;
    }


    public String getOnmouseover() {
        return onmouseover;
    }


    public void setOnmouseout(String onmouseout) {
        this.onmouseout = onmouseout;
    }


    public String getOnmouseout() {
        return onmouseout;
    }


    public void setOnmousemove(String onmousemove) {
        this.onmousemove = onmousemove;
    }


    public String getOnmousemove() {
        return onmousemove;
    }


    public void setOnmousedown(String onmousedown) {
        this.onmousedown = onmousedown;
    }


    public String getOnmousedown() {
        return onmousedown;
    }


    public void setOnmouseup(String onmouseup) {
        this.onmouseup = onmouseup;
    }


    public String getOnmouseup() {
        return onmouseup;
    }


    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }


    public String getOnkeydown() {
        return onkeydown;
    }


    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }


    public String getOnkeyup() {
        return onkeyup;
    }


    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }


    public String getOnkeypress() {
        return onkeypress;
    }


    public void setOnselect(String onselect) {
        this.onselect = onselect;
    }


    public String getOnselect() {
        return onselect;
    }


    public void setOnchange(String onchange) {
        this.onchange = onchange;
    }


    public String getOnchange() {
        return onchange;
    }


    public void setOnblur(String onblur) {
        this.onblur = onblur;
    }


    public String getOnblur() {
        return onblur;
    }


    public void setOnfocus(String onfocus) {
        this.onfocus = onfocus;
    }


    public String getOnfocus() {
        return onfocus;
    }


    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }


    public boolean isDisabled() {
        return disabled;
    }


    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }


    public boolean isReadonly() {
        return readonly;
    }


    public void setStyle(String style) {
        this.style = style;
    }


    public String getStyle() {
        return style;
    }


    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }


    public String getStyleClass() {
        return styleClass;
    }


    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }


    public String getStyleId() {
        return styleId;
    }
    
    /**
     * Searches all scopes for the bean and calls BeanUtils.getProperty() with the 
     * given arguments and converts any exceptions into JspException.
     * 
     * @param beanName The name of the object to get the property from.
     * @param property The name of the property to get.
     * @return The value of the property.
     * @throws JspException
     * @since Struts 1.1
     */
    protected String lookupProperty(String beanName, String property)
        throws JspException {

        Object bean = TagUtils.getInstance().lookup(this.pageContext, beanName, null);
        try {
            return BeanUtils.getProperty(bean, property);
        } 
        catch (Exception e) {
            throw new JspException("Failed Looking Up Property for Bean: " + beanName, e);
        }
    }

    public void release() {
        super.release();
        onclick = null;
        ondblclick = null;
        onmouseover = null;
        onmouseout = null;
        onmousemove = null;
        onmousedown = null;
        onmouseup = null;
        onkeydown = null;
        onkeyup = null;
        onkeypress = null;
        onselect = null;
        onchange = null;
        onblur = null;
        onfocus = null;
        disabled = false;
        readonly = false;
        style = null;
        styleClass = null;
        styleId = null;
        name = null;
        property = null;
        value = null;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setProperty(String property) {
        this.property = property;
    }


    public String getProperty() {
        return property;
    }


    public void setValue(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
    
}