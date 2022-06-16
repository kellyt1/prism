package us.mn.state.health.builder;

import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.common.OrgBudget;
import us.mn.state.health.view.common.OrgBudgetForm;

public class OrgBudgetFormsBuilder {
    private Collection orgBudgetForms;
    private Collection orgBudget;

    public OrgBudgetFormsBuilder(Collection orgBudgetForms, Collection orgBudget) {
        this.orgBudgetForms = orgBudgetForms;
        this.orgBudget = orgBudget;
    }

    public void buildOrgBudgetForms() throws BusinessException {
        try {
            for (Iterator i = orgBudget.iterator(); i.hasNext();) {
                OrgBudget orgBudget = (OrgBudget) i.next();
                OrgBudgetForm orgBudgetForm = new OrgBudgetForm();
                PropertyUtils.copyProperties(orgBudgetForm, orgBudget);
                orgBudgetForms.add(orgBudgetForm);
            }
        } 
        catch (ReflectivePropertyException rpe) {
            throw new BusinessException("Failed Building OrgBudget Forms: ", rpe);
        }
    }
}
