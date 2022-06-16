package us.mn.state.health.builder.materialsrequest

import junit.framework.TestCase
import us.mn.state.health.dao.DAOFactoryStub
import us.mn.state.health.model.common.Priority
import us.mn.state.health.model.materialsrequest.Request
import us.mn.state.health.view.materialsrequest.RequestForm

import java.text.SimpleDateFormat

/**
 * Created by demita1 on 5/16/2016.
 */
class RequestFormBuilderTest extends TestCase {

    private RequestFormBuilder builder
    private RequestForm requestForm

    private static Date DATE_REQUESTED = new Date(),
                        NEED_BY_DATE = new Date()

    private static String ADDTL_INSTR = "woo",
                          MRQ = "MRQ-123456789",
                          PRIORITY_CODE = "euch"

    @Override
    protected void setUp() throws Exception {
        requestForm = new RequestForm()
        builder = new RequestFormBuilder(requestForm, new DAOFactoryStub(), buildRequest())
    }

    public void test_buildSimpleProperties_dateRequested() {
        // when
        builder.buildSimpleProperties()

        assertEquals(DATE_REQUESTED, requestForm.dateRequested)
    }

    public void test_buildSimpleProperties_needByDate() {
        // setup
        String date = new SimpleDateFormat("MM/dd/yyyy").format(NEED_BY_DATE)

        // when
        builder.buildSimpleProperties()

        assertEquals(date, requestForm.needByDate)
    }

    public void test_buildSimpleProperties_additionalInstructions() {
        // when
        builder.buildSimpleProperties()

        assertEquals(ADDTL_INSTR, requestForm.additionalInstructions)
    }

    public void test_buildSimpleProperties_priorityCode() {
        // when
        builder.buildSimpleProperties()

        assertEquals(PRIORITY_CODE, requestForm.priorityCode)
    }

    public void test_buildSimpleProperties_trackingNumber() {
        // when
        builder.buildSimpleProperties()

        assertEquals(MRQ, requestForm.trackingNumber)
    }

    private static Request buildRequest() {
        Request request = new Request()
        request.dateRequested = DATE_REQUESTED
        request.needByDate = NEED_BY_DATE
        request.additionalInstructions = ADDTL_INSTR
        request.priority = buildPriority()
        request.trackingNumber = MRQ
        request
    }

    private static Priority buildPriority() {
        Priority priority = new Priority()
        priority.priorityCode = PRIORITY_CODE
        priority
    }

}
