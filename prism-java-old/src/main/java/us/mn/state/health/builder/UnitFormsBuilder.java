package us.mn.state.health.builder;
import java.util.Collection;
import java.util.Iterator;

import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.UnitForm;

public class UnitFormsBuilder  {

    private Collection unitForms;
    private Collection units;
    
    public UnitFormsBuilder(Collection unitForms, Collection units) {
        this.unitForms = unitForms;
        this.units = units;
    }
    
    public void buildUnitForms() throws InfrastructureException {
        try {
            for(Iterator i = units.iterator(); i.hasNext();) {
                Unit unit = (Unit)i.next();
                UnitForm unitForm = new UnitForm();
                PropertyUtils.copyProperties(unitForm, unit);
                unitForms.add(unitForm);
            }  
        }
        catch(ReflectivePropertyException rpe) {
            throw new InfrastructureException("Failed Building Unit Form: ", rpe);
        }
    }
}