package us.mn.state.health.builder;
import us.mn.state.health.common.exceptions.InfrastructureException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.UnitForm;

public class UnitFormBuilder  {

    private UnitForm unitForm;
    private Unit unit;
    
    public UnitFormBuilder(UnitForm unitForm, Unit unit) {
        this.unitForm = unitForm;
        this.unit = unit;
    }
    
    public void buildUnitForm() throws InfrastructureException {
        try {
            PropertyUtils.copyProperties(unitForm, unit);
        }
        catch(ReflectivePropertyException rpe) {
            throw new  InfrastructureException("Failed building Unit Form: ", rpe);
        }
    }
}