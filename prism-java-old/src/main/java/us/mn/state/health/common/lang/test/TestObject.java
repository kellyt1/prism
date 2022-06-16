package us.mn.state.health.common.lang.test;
import java.util.Date;

public class TestObject implements Comparable {
  private String stringProp;
  
  private Integer integerProp;
 
  private Date dateProp;
  
  private TestObject testObject;
  
  public TestObject() {
    this.stringProp = "Lucy";
    this.integerProp = new Integer(7);
    this.dateProp = new Date(System.currentTimeMillis());
  }
  
  public TestObject(String stringProp, Integer integerProp) {
    this.stringProp = stringProp;
    this.integerProp = integerProp;
  }
  
  public TestObject(String stringProp, Integer integerProp, Date dateProp) {
    this.stringProp = stringProp;
    this.integerProp = integerProp;
    this.dateProp = dateProp;
  }
  
  public String getStringProp() {
    return stringProp;
  }
  
  public void setStringProp(String stringProp) {
    this.stringProp = stringProp;
  }
  
  public Integer getIntegerProp(){
    return integerProp;
  }
  
  public void setIntegerProp(Integer integerProp) {
    this.integerProp = integerProp;
  }

    public void setDateProp(Date dateProp) {
        this.dateProp = dateProp;
    }


    public Date getDateProp() {
        return dateProp;
    }
    
    public int compareTo(Object comparable) {
        TestObject testObj = (TestObject)comparable;
        return this.getIntegerProp().compareTo(testObj.getIntegerProp());
    }


    public void setTestObject(TestObject testObject) {
        this.testObject = testObject;
    }


    public TestObject getTestObject() {
        return testObject;
    }
}