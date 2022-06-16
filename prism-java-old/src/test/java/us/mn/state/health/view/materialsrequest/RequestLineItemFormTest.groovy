package us.mn.state.health.view.materialsrequest

import junit.framework.TestCase
import org.apache.struts.upload.FormFile
import us.mn.state.health.model.materialsrequest.AttachedFileNonCat
import us.mn.state.health.model.materialsrequest.RequestLineItem

/**
 * Created by demita1 on 5/17/2016.
 */
class RequestLineItemFormTest extends TestCase {

    private static final String DUMMY = "woo"

    private RequestLineItemForm form

    @Override
    public void setUp() throws Exception {
        form = buildForm()
    }

    public void test_reset() throws Exception {
        // when
        form.reset()

        // then
        assertTrue(form.cmd.empty)
        assertTrue(form.itemDescription.empty)
        assertTrue(form.quantity.empty)
        assertTrue(form.itemCost.empty)
        assertTrue(form.itemJustification.empty)
        assertTrue(form.suggestedVendorURL.empty)
        assertTrue(form.suggestedVendorCatalogNumber.empty)
        assertTrue(form.categoryId.empty)

        assertTrue(form.amountInDollars)
        assertFalse(form.selected)
        assertFalse(form.remove)

        assertTrue(form.fundingSourceKey.empty)

        assertNull(form.purchasingInfoFile)
        assertNull(form.purchasingInfoFileAlternate)

        assertTrue(form.shoppingListAction.empty)
        assertTrue(form.textNote.empty)

        assertFalse(form.itPurchase)

        assertTrue(form.noteForms.empty)
        assertTrue(form.purchasingInfoFiles.empty)
        assertTrue(form.requestLineItem.attachedFileNonCats.empty)

        assertNull(form.unitId)
        assertTrue(form.fundingSourceForms.empty)
        assertNull(form.suggestedVendorName)
        assertNull(form.swiftItemId)
    }

    private static RequestLineItemForm buildForm() {
        RequestLineItemForm form = new RequestLineItemForm()
        form.with {
            cmd = DUMMY
            itemDescription = DUMMY
            quantity = DUMMY
            itemCost = DUMMY
            itemJustification = DUMMY
            suggestedVendorURL = DUMMY
            suggestedVendorCatalogNumber = DUMMY
            categoryId = DUMMY

            amountInDollars = false
            selected = true
            remove = true

            fundingSourceKey = DUMMY

            purchasingInfoFile = buildFormFile()
            purchasingInfoFileAlternate = buildFormFile()

            shoppingListAction = DUMMY
            textNote = DUMMY

            itPurchase = true

            noteForms.collect{buildRequestLineItemForm()}
            setPurchasingInfoFiles(collect{buildFormFile()})
            requestLineItem = buildRequestLineItem()

            unitId = DUMMY
            fundingSourceForms.collect{buildRequestLineItemFundingSourceForm()}
            suggestedVendorName = DUMMY
            swiftItemId = DUMMY
        }

        form
    }

    private static FormFile buildFormFile() {
        new FormFileStub()
    }


    private static RequestLineItemForm buildRequestLineItemForm() {
        new RequestLineItemForm()
    }

    private static RequestLineItem buildRequestLineItem() {
        RequestLineItem item = new RequestLineItem()
        item.attachedFileNonCats = Collections.singletonList(new AttachedFileNonCat())
        item
    }

    private static RequestLineItemFundingSourceForm buildRequestLineItemFundingSourceForm() {
        new RequestLineItemFundingSourceForm()
    }

}
