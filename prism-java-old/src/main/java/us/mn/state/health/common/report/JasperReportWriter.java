package us.mn.state.health.common.report;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.fill.JRBaseFiller;
import net.sf.jasperreports.engine.fill.AsynchronousFillHandle;
import net.sf.jasperreports.engine.fill.AsynchronousFilllListener;
import us.mn.state.health.common.exceptions.ReportException;
import us.mn.state.health.persistence.HibernateUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.sql.Connection;

public class JasperReportWriter {
    private File reportFile;
    private File reportFilejrxml;
    private File outFile;
    private Collection reportData;
    private String format = "PDF_FORMAT";
    private boolean written = false;
    private boolean createUniqueOutFileName = true;
    private boolean autoDeleteOutFile = true;
    private long deleteWaitTime = 30000l; //Default to 30 seconds
    private String relativeOutFilePath;
    private HashMap hashMap;
    private HttpServletResponse response = null;
    private Connection conn = null;

    private final static String JASPERRUNTHREAD = "JASPERRUN";
    
    //Report Formats
    public static final String FORMAT_PDF = "PDF_FORMAT"; //Default
    public static final String FORMAT_CSV = "CSV_FORMAT";
    public static final String FORMAT_XLS = "XLS_FORMAT";
    
//    public JasperReportWriter(File reportFile,
//                              File outFile,
//                              Collection reportData,
//                              HashMap hashMap) {
//        this.reportFile = new File(reportFile + ".jasper");
//        this.reportFilejrxml = new File(reportFile + ".jrxml");
//
//        this.outFile = outFile;
//        this.relativeOutFilePath = outFile.getPath();
//        this.reportData = reportData;
//        this.hashMap = hashMap;
//    }
    
/*
    public JasperReportWriter(HttpServlet servlet, HttpServletResponse response,
                              String relativeReportPath,
                              String relativeOutFilePath,
                              Collection reportData,
                              HashMap hashMap)  {

        this.response = response;
        this.reportFile = new File(servlet.getServletContext().getRealPath(relativeReportPath) + ".jasper");
        this.reportFilejrxml = new File(servlet.getServletContext().getRealPath(relativeReportPath) + ".jrxml");
        this.outFile = new File(servlet.getServletContext().getRealPath(relativeOutFilePath));
        this.relativeOutFilePath = relativeOutFilePath;
        this.reportData = reportData;
        this.hashMap = hashMap;
    }
      public JasperReportWriter(HttpServlet servlet, 
                              String relativeReportPath,
                              String relativeOutFilePath,
                              Collection reportData,
                              HashMap hashMap)  {

        this.response = null;
        this.reportFile = new File(servlet.getServletContext().getRealPath(relativeReportPath) + ".jasper");
        this.reportFilejrxml = new File(servlet.getServletContext().getRealPath(relativeReportPath) + ".jrxml");
        this.outFile = new File(servlet.getServletContext().getRealPath(relativeOutFilePath));
        this.relativeOutFilePath = relativeOutFilePath;
        this.reportData = reportData;
        this.hashMap = hashMap;
    }
*/

    public JasperReportWriter(HttpServlet servlet, HttpServletResponse response,
//                              String relativeReportPath,
                              String absoluteReportPath,
                              String relativeOutFilePath,
                              Collection reportData,
                              HashMap hashMap)  {

        this.response = response;
        this.reportFile = new File(absoluteReportPath + ".jasper");
        this.reportFilejrxml = new File(absoluteReportPath + ".jrxml");
        this.outFile = new File(servlet.getServletContext().getRealPath(relativeOutFilePath));
        this.relativeOutFilePath = relativeOutFilePath;
        this.reportData = reportData;
        this.hashMap = hashMap;
    }
    public JasperReportWriter(HttpServlet servlet,
//                            String relativeReportPath,
                            String absoluteReportPath,
                            String relativeOutFilePath,
                            Collection reportData,
                            HashMap hashMap)  {

      this.response = null;
      this.reportFile = new File(absoluteReportPath + ".jasper");
      this.reportFilejrxml = new File(absoluteReportPath + ".jrxml");
      this.outFile = new File(servlet.getServletContext().getRealPath(relativeOutFilePath));
      this.relativeOutFilePath = relativeOutFilePath;
      this.reportData = reportData;
      this.hashMap = hashMap;
  }

    public JasperReportWriter(HttpServlet servlet,
//                            String relativeReportPath,
                            String absoluteReportPath,
                            String relativeOutFilePath,
                            Connection conn,
                            HashMap hashMap)  {

      this.response = null;
      this.reportFile = new File(absoluteReportPath + ".jasper");
      this.reportFilejrxml = new File(absoluteReportPath + ".jrxml");
      this.outFile = new File(servlet.getServletContext().getRealPath(relativeOutFilePath));
      this.relativeOutFilePath = relativeOutFilePath;
      this.conn = conn;
      this.hashMap = hashMap;
  }

    //Properties to be manually set for the Report Writer
    
    public void setAutoDeleteOutFile(boolean autoDeleteOutFile) {
        this.autoDeleteOutFile = autoDeleteOutFile;
    }
    
    /**
     * Tells report writer to create a unique name for the report. Default is "true".
     * @param createUniqueOutFileName
     */
    public void setCreateUniqueOutFileName(boolean createUniqueOutFileName) {
        this.createUniqueOutFileName = createUniqueOutFileName;
    }
    
    /**
     * Sets Delete wait time for report on server (in seconds). Default is 30 seconds
     * @param deleteWaitTime
     */
    public void setDeleteWaitTime(long deleteWaitTime) {
        this.deleteWaitTime = deleteWaitTime * 1000l;
    }
    
    /**
     * Sets Report format. Default is PDF.
     * @param format
     */
    public void setReportFormat(String format) {
        this.format = format;
    }
   
    /**
     * Executes report and writes it to file system. This method can only be called once per 
     * JasperReportWriter instance
     * @throws us.mn.state.health.common.exceptions.ReportException
     * @return name of report output file that was written to file system
     */
    public String write() throws ReportException {
        try {
            if(written) {
                throw new ReportException("Report has already been written. Please instanciate new ReportWriter.");
            }
            if ( response == null)  buildOutFiles(); //build relative and real out files

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportData);

              JasperPrint jPrint = new JasperPrint();
//            JasperPrint jPrint =  JasperFillManager.fillReport(reportFile.getPath(), hashMap, ds);
//            JRExporter exporter = null;
//            JRBaseFiller jr;
             JasperReport jr2 = JasperCompileManager.compileReport(reportFilejrxml.getPath());
            JRExporter exporter = new JRPdfExporter();

            AsynchronousFillHandle asynchronousFillHandle;

            if (reportData != null)  {
                asynchronousFillHandle = AsynchronousFillHandle.createHandle(jr2,hashMap,ds);
            }
            else {
                Connection conn = HibernateUtil.getSession().connection();

                asynchronousFillHandle = AsynchronousFillHandle.createHandle(jr2,hashMap,conn);
            }
            JasperListener listener= new JasperListener();
            try {

                asynchronousFillHandle.setThreadName(JASPERRUNTHREAD);
                asynchronousFillHandle.setPriority(Thread.MAX_PRIORITY);

                asynchronousFillHandle.addListener(listener);
                asynchronousFillHandle.startFill();

//                /*** For testing direct access of thread */
//                Thread mainThread = Thread.currentThread();
//                Thread lJasperThread = visit(mainThread.getThreadGroup(),1);
//                lJasperThread.interrupt();

////                /**********************************************************/
                long timeStarted= System.currentTimeMillis();
                long timeElapsed= 0;
                long timeBeforeCancel = 60 * 1000;

                boolean cancelThread = false;


                   while (listener.getJoutPrint() == null && !cancelThread)  {
                        // Give it a 10 second at most running time.
                        timeElapsed = Math.abs(timeStarted - System.currentTimeMillis());
                        if (Long.valueOf(timeElapsed).compareTo(Long.valueOf(timeBeforeCancel)) > 0 || listener.getReportCancelled()) {
                            cancelThread = true;
                        }
                    }
                    if (cancelThread) {
                        try {
                            asynchronousFillHandle.cancellFill();
                        } catch (JRException e) {
                            asynchronousFillHandle.removeListener(listener);
                        }
                        System.out.println("REPORT CANCELLED " + ds.toString());
                        if (response != null)  makeErrorMessage(response, "Report cancelled maximum run time exceeded. please notify Prism support with details such as what MRQ you were processing. ");
                        return "blank screen";
                    }

               jPrint = listener.getJoutPrint();
            } catch (Exception e) {
                asynchronousFillHandle.removeListener(listener);
                if (response != null)  makeErrorMessage(response,"Major Error in Jasper Report,  please notify Prism support with details such as what MRQ you were processing.");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return "blank screen";
            }                                                                             

            if ( response != null) {
               response.setContentType("application/pdf");
               //response.setHeader("Content-Disposition","attachment; filename = "+reportFile+".pdf");
               exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            } else exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFile.getPath());
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
            exporter.exportReport();
//            written = true;
            //asynchronousFillHandle.removeListener(listener);
            if ( response == null) {
                  if(autoDeleteOutFile) {
                    delete(); //clean up report out file automatically
                }
            } else {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

            return relativeOutFilePath;
        }
        //we replaced the catch of all the Exceptions only with JRException (we want not to catch Runtime Exceptions)
        //07-28-2006 Lucian Ochian
        catch (Exception e) {
            e.printStackTrace();
            throw new ReportException("Failed writing report: " + reportFile, e);
        }
    }



     public String write(String format) throws ReportException {
        try {
            if(written) {
                throw new ReportException("Report has already been written. Please instanciate new ReportWriter.");
            }
            if ( response == null)  buildOutFiles(format); //build relative and real out files

            JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(reportData);

              JasperPrint jPrint = new JasperPrint();
//            JasperPrint jPrint =  JasperFillManager.fillReport(reportFile.getPath(), hashMap, ds);
//            JRExporter exporter = null;
//            JRBaseFiller jr;
             JasperReport jr2 = JasperCompileManager.compileReport(reportFilejrxml.getPath());
            JRExporter exporter;

            if(format.equals("csv")){
                exporter= new JRCsvExporter();
            }  else{
                exporter = new JRPdfExporter();
            }

            AsynchronousFillHandle asynchronousFillHandle;

            if (reportData != null)  {
                asynchronousFillHandle = AsynchronousFillHandle.createHandle(jr2,hashMap,ds);
            }
            else {
                Connection conn = HibernateUtil.getSession().connection();

                asynchronousFillHandle = AsynchronousFillHandle.createHandle(jr2,hashMap,conn);
            }
            JasperListener listener= new JasperListener();
            try {

                asynchronousFillHandle.setThreadName(JASPERRUNTHREAD);
                asynchronousFillHandle.setPriority(Thread.MAX_PRIORITY);

                asynchronousFillHandle.addListener(listener);
                asynchronousFillHandle.startFill();

//                /*** For testing direct access of thread */
//                Thread mainThread = Thread.currentThread();
//                Thread lJasperThread = visit(mainThread.getThreadGroup(),1);
//                lJasperThread.interrupt();

////                /**********************************************************/
                long timeStarted= System.currentTimeMillis();
                long timeElapsed= 0;
                long timeBeforeCancel = 60 * 1000;

                boolean cancelThread = false;


                   while (listener.getJoutPrint() == null && !cancelThread)  {
                        // Give it a 10 second at most running time.
                        timeElapsed = Math.abs(timeStarted - System.currentTimeMillis());
                        if (Long.valueOf(timeElapsed).compareTo(Long.valueOf(timeBeforeCancel)) > 0 || listener.getReportCancelled()) {
                            cancelThread = true;
                        }
                    }
                    if (cancelThread) {
                        try {
                            asynchronousFillHandle.cancellFill();
                        } catch (JRException e) {
                            asynchronousFillHandle.removeListener(listener);
                        }
                        System.out.println("REPORT CANCELLED " + ds.toString());
                        if (response != null)  makeErrorMessage(response, "Report cancelled maximum run time exceeded. please notify Prism support with details such as what MRQ you were processing. ");
                        return "blank screen";
                    }

               jPrint = listener.getJoutPrint();
            } catch (Exception e) {
                asynchronousFillHandle.removeListener(listener);
                if (response != null)  makeErrorMessage(response,"Major Error in Jasper Report,  please notify Prism support with details such as what MRQ you were processing.");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                return "blank screen";
            }

            if ( response != null) {
               response.setContentType("application/"+format);
               //response.setHeader("Content-Disposition","attachment; filename = "+reportFile+".pdf");
               exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            } else exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFile.getPath());
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jPrint);
            exporter.exportReport();
//            written = true;
            //asynchronousFillHandle.removeListener(listener);
            if ( response == null) {
                  if(autoDeleteOutFile) {
                    delete(); //clean up report out file automatically
                }
            } else {
                response.getOutputStream().flush();
                response.getOutputStream().close();
            }

            return relativeOutFilePath;
        }
        //we replaced the catch of all the Exceptions only with JRException (we want not to catch Runtime Exceptions)
        //07-28-2006 Lucian Ochian
        catch (Exception e) {
            e.printStackTrace();
            throw new ReportException("Failed writing report: " + reportFile, e);
        }
    }
    
    /**
     * Deletes a report after the given "deleteWaitInterval" has been reaced.
     * This method acts asynchronously.
     * @throws us.mn.state.health.common.exceptions.ReportException
     */
    public void delete() throws ReportException {
        try {
            new ReportDeleter().start();
        }
        catch(Exception e) {
            throw new ReportException("Failed deleting report out file: " + outFile, e);
        }
    }
    
    /**
     * Performs physical delete of report output file
     */
    private synchronized void deleteFile() {
        if(outFile.exists()) {
            outFile.delete();
        }
    }
    
    //Utility Methods
    
    /**
     * Builds unique file name for report output
     */
    private String createUniqueOutfileName() {
        return "/" + Long.toString(this.hashCode() + System.currentTimeMillis());
    }
    
    /**
     * Worker thread that deletes report output file
     */
    private class ReportDeleter extends Thread {
        public void run() {
            //sleep for a little bit to give the server a chance to serve up the report
            try {sleep(deleteWaitTime);}catch(Exception e) {e.printStackTrace();}
            //delete report from file system
            deleteFile();
        }
    }
    
    private void buildOutFiles() {
        String uniqueFileName = "";
        String extension = "";
        
        //Build unique file name
        if(createUniqueOutFileName) {
            uniqueFileName = createUniqueOutfileName();
        }
        
        //Build extension
        if(format.equals(JasperReportWriter.FORMAT_PDF)) {
            extension = ".pdf";
        }

//        if(format.equals(JasperReportWriter.FORMAT_CSV)) {
//                   extension = ".csv";
//               }
//        if(format.equals(JasperReportWriter.FORMAT_XLS)) {
//                   extension = ".xls";
//               }

        
        String outFilePath = outFile.getPath() + uniqueFileName + extension;
        relativeOutFilePath = relativeOutFilePath + uniqueFileName + extension;
        outFile = new File(outFilePath);
    }

    private void buildOutFiles(String format) {
        String uniqueFileName = "";
        String extension = "";

        //Build unique file name
        if(createUniqueOutFileName) {
            uniqueFileName = createUniqueOutfileName();
        }

        //Build extension
       if(format.equals("csv")) {
                   extension = ".csv";
               }
        if(format.equals("pdf")) {
                   extension = ".xls";
               }


        String outFilePath = outFile.getPath() + uniqueFileName + extension;
        relativeOutFilePath = relativeOutFilePath + uniqueFileName + extension;
        outFile = new File(outFilePath);
    }
    public static Thread visit(ThreadGroup group, int level) {
           // Get threads in `group'
           int numThreads = group.activeCount();
           Thread[] threads = new Thread[numThreads*2];
           numThreads = group.enumerate(threads, false);
           Thread outThread = null;

           // Enumerate each thread in `group'
           for (int i=0; i<numThreads; i++) {
               // Get thread
               Thread thread = threads[i];
               if (thread.getName().equalsIgnoreCase(JASPERRUNTHREAD)) {
                   outThread = thread;
               }
           }
        return outThread;
    }

    private void makeErrorMessage(HttpServletResponse response, String inMessage) {
        try {
            response.getOutputStream().print(inMessage);
            response.setContentType("text/html");
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            System.out.println("Response problem");
        }

    }
           // Get thread subgroups of `group'
//           System.out.println("Now get subgroups");
//           int numGroups = group.activeGroupCount();
//           ThreadGroup[] groups = new ThreadGroup[numGroups*2];
//           numGroups = group.enumerate(groups, false);
//
//           // Recursively visit each subgroup
//           for (int i=0; i<numGroups; i++) {
//               visit(groups[i], level+1);
//           }
 }