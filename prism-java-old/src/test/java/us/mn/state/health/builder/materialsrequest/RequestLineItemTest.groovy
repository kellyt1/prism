package us.mn.state.health.builder.materialsrequest

import junit.framework.TestCase
import us.mn.state.health.model.common.Category
import us.mn.state.health.model.common.Status
import us.mn.state.health.model.materialsrequest.RequestLineItem

/**
 * Created by demita1 on 5/16/2016.
 */
class RequestLineItemTest extends TestCase {

    private static String CATEGORY_BOOK = Category.MATERIALS_BOOK,
                          STATUS_WAITING_FOR_APPROVAL = Status.WAITING_FOR_APPROVAL

    private RequestLineItem lineItem

    public void setUp() throws Exception {
        lineItem = new RequestLineItem()
    }

    public void test_isBook_categorySetToBook_returnsTrue() throws Exception {
        lineItem.itemCategory = buildCategory()
        assertTrue(lineItem.book)
    }

    public void test_isBook_categorySetToNull_returnsFalse() throws Exception {
        assertFalse(lineItem.book)
    }

    public void test_isWaitingForApproval_statusSetToWaitingForApproval_returnsTrue() throws Exception {
        lineItem.status = buildStatus()
        assertTrue(lineItem.waitingForApproval)
    }

    public void test_isWaitingForApproval_statusSetToNull_returnsFalse() throws Exception {
        assertFalse(lineItem.waitingForApproval)
    }

    private static Category buildCategory() {
        Category category = new Category()
        category.categoryCode = CATEGORY_BOOK
        category
    }

    private static Status buildStatus() {
        Status status = new Status()
        status.statusCode = STATUS_WAITING_FOR_APPROVAL
        status
    }
}
