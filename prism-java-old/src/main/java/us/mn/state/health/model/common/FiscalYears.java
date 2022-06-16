package us.mn.state.health.model.common;

import java.io.Serializable;
import java.io.InputStream;
import java.sql.Blob;

public class FiscalYears implements Serializable {
 
                                    
    private Long id;
    private Long fiscalyear;
    private Long yearsago;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFiscalyear() {
        return fiscalyear;
    }

    public void setFiscalyear(Long fiscalyear) {
        this.fiscalyear = fiscalyear;
    }

    public Long getYearsago() {
        return yearsago;
    }

    public void setYearsago(Long yearsago) {
        this.yearsago = yearsago;
    }
}