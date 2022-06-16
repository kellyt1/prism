package us.mn.state.health.web.struts.admin

import org.apache.struts.validator.ValidatorForm
import us.mn.state.health.model.util.search.struts.IndexForm;

public class CachedObjectsForm  extends ValidatorForm {
    List<IndexForm> indexForms = new ArrayList()
    String prismLoginNotification
    String prismEveryPageNotification
}