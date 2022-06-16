package us.mn.state.health.common.lang.test;

import java.util.Date;

import junit.framework.TestCase;
import us.mn.state.health.common.lang.PropertyUtils;

public class PropertyUtilsTestCase extends TestCase {
  public PropertyUtilsTestCase(String sTestName) {
    super(sTestName);
  }
  
  public void testCreateReadMethodName() {
    String propertyName = "stringProp";
    String methodName = PropertyUtils.createReadMethodName(propertyName);
    
    System.out.println("testCreateReadMethodName() = " + methodName);
  }
  
  public void testCreateWriteMethodName() {
    String propertyName = "stringProp";
    String methodName = PropertyUtils.createWriteMethodName(propertyName);
    
    System.out.println("testCreateWriteMethodName() = " + methodName);
  }
  
  public void testInvokeReadMethodWithPropertyName() throws Exception {
    TestObject testObj = new TestObject();
    Object propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "stringProp");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 1: " + propertyValue);
    propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "integerProp");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 2: " + propertyValue);
    propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "String Prop");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 3: " + propertyValue);
    propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "Integer Prop");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 4: " + propertyValue);
    propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "dateProp");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 5: " + propertyValue);
    propertyValue = 
      PropertyUtils.invokeReadMethodWithPropertyName(testObj, "falseProp");
    System.out.println("testInvokeReadMethodWithPropertyName(), Test 6: " + propertyValue);

  }
  
  public void testInvokeWriteMethodWithPropertyName() throws Exception {
    TestObject testObj = new TestObject(null, null, null);
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "stringProp", null);
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 1: " + testObj.getStringProp());
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "integerProp", "65");
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 2: " + testObj.getIntegerProp());
    testObj = new TestObject("Lucy Lu", new Integer(65));
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "stringProp", "Lucy");
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 3: " + testObj.getStringProp());
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "integerProp", "65");
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 4: " + testObj.getIntegerProp());
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "dateProp", 
        "01/02/2004");
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 5: " + testObj.getDateProp());
    PropertyUtils.invokeWriteMethodWithPropertyName(testObj, "falseProp", "Lucy");
    System.out.println("testInvokeWriteMethodWithPropertyName(), Test 6: " + testObj.getStringProp());
  }
  
  public void testCopyProperties() throws Exception {
      TestChildObject fromObj = new TestChildObject("Lucy", new Integer(7), new Date());
      fromObj.setTestObject(new TestObject());
      TestChildObject toObj = new TestChildObject(null, null, null);
      PropertyUtils.copyProperties(toObj, fromObj);
  }

}