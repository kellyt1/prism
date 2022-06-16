package us.mn.state.health.builder;
import us.mn.state.health.common.exceptions.BusinessException;
import us.mn.state.health.common.exceptions.ReflectivePropertyException;
import us.mn.state.health.common.lang.PropertyUtils;
import us.mn.state.health.model.inventory.Unit;
import us.mn.state.health.view.inventory.UnitForm;

public class UnitBuilder  {
    
    private Unit unit;
    private UnitForm unitForm;
    
    public UnitBuilder(Unit unit, UnitForm unitForm) {
        this.unit = unit;
        this.unitForm = unitForm;
    }
    
    public void buildUnit() throws BusinessException {
        try {
            PropertyUtils.copyProperties(unit, unitForm);
        }
        catch(ReflectivePropertyException rpe) {
            throw new BusinessException(rpe);
        }
    }
}