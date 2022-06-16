package us.mn.state.health.model.util.search.struts;

public class IndexForm {

    private boolean selected;
    private Object index;


    public IndexForm(Object index, boolean selected) {
        this.index = index;
        this.selected = selected;
    }

    public String getClassName() {
        return index.getClass().getName();
    }

    public String getShortClassName(){
        return getClassName().substring(getClassName().lastIndexOf(".")+1);
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }


    public Object getIndex() {
        return index;
    }

    public void setIndex(Object index) {
        this.index = index;
    }
}
