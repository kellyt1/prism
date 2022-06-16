package us.mn.state.health.model.util.search.struts;

import java.util.List;
import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

public class LuceneIndexesForm extends ValidatorForm {
    private List indexForms = new ArrayList();

    public List getIndexForms() {
        return indexForms;
    }

    public void setIndexForms(List indexForms) {
        this.indexForms = indexForms;
    }
}
