package us.mn.state.health.common.report;

import java.io.File;
import java.io.FileReader;
import java.util.Collection;

public class TestJRBeanDataSourceFactory extends JRBeanDataSourceFactory {

    public TestJRBeanDataSourceFactory() {
        try {
            Collection beans = super.executeQuery(getHQL("bean.hql"));
            super.setBeans(beans);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    protected String getHQL(String fileName) throws Exception {
        File inputFile = new File(fileName);
        FileReader in = new FileReader(inputFile);
        char[] buf = new char[1000];
        int c;
        while ((c = in.read(buf)) != -1) {
        }
        return new String(buf).trim();
    }
}