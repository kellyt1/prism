package us.mn.state.health.common.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import us.mn.state.health.common.exceptions.ReportException;

import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;

public class JasperListener implements AsynchronousFilllListener  {
    private static Log log = LogFactory.getLog(JasperListener.class);

     public JasperListener() {
              reportCancelled = Boolean.FALSE;
     }
        JasperPrint joutPrint = null;
        Boolean reportCancelled = Boolean.FALSE;
        public void reportFinished(JasperPrint jprint) {
           // System.out.println("Called Finished ");
            this.joutPrint = jprint;
            reportCancelled = Boolean.FALSE;
        }
        public void reportFillError(Throwable t) {
            log.error("Report Fill error " + t );
            reportCancelled = Boolean.TRUE;

        }
        public void reportCancelled() {
            log.error("Report Cancelled ");
            reportCancelled = Boolean.TRUE;

        }
        public JasperPrint getJoutPrint() {
            return joutPrint;
        }
        public void setJoutPrint(JasperPrint joutPrint) {
            this.joutPrint = joutPrint;
        }

    public Boolean getReportCancelled() {
        return reportCancelled;
    }

    public void setReportCancelled(Boolean reportCancelled) {
        this.reportCancelled = reportCancelled;
    }
}
