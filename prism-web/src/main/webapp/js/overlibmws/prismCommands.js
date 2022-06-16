function displayStockItemIsOnOrderInformation() {
    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Item On Order Details',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            DRAGGABLE,
            STATUS, 'Example with AJAX content via responseText of XMLHttpResponse'
            , CGCLASS, 'popupheader'
            , CLOSESIZE, '100%'
            );
}
function displayStockItemHistory() {
    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Stock Item History ',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            DRAGGABLE,
            STATUS, 'Example with AJAX content via responseText of XMLHttpResponse'
            , CGCLASS, 'popupheader'
            , CLOSESIZE, '100%'
            );
}

function displayStockQtyAdjustmentHistory() {
    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Stock Qty Adjustment History ',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            DRAGGABLE,
            STATUS, 'Example with AJAX content via responseText of XMLHttpResponse'
            , CGCLASS, 'popupheader'
            , CLOSESIZE, '100%'
            );
}

function displayStockItemLocationHistory() {
    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Stock Item Location History ',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            DRAGGABLE,
            STATUS, 'Example with AJAX content via responseText of XMLHttpResponse'
            , CGCLASS, 'popupheader'
            , CLOSESIZE, '100%'
            );
}


function displayItemVendorLinkHistory() {
    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Item Vendor Link History ',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            DRAGGABLE,
            STATUS, 'Example with AJAX content via responseText of XMLHttpResponse'
            , CGCLASS, 'popupheader'
            , CLOSESIZE, '100%'
            );
}

function displayStockItemIsOnOrderInformation2() {
    return overlib2(OLresponseAJAX,
            HEIGHT, 150,
            TEXTPADDING, 0, CAPTIONPADDING, 4,
            CAPTION, 'Request Details',
            WRAP, BORDER, 2, STICKY, CLOSECLICK, SCROLL,
            BELOW,
            CGCLASS, 'popupheader2',
            CLOSESIZE, '100%',
            SHADOW, SHADOWCOLOR, '6699cc',
            DRAGGABLE,
            FGCOLOR, 'ffcccc',
            CGCOLOR, '5555bb',
            STATUS, 'Normal alignment'
            );
}
// Does not work properly, it does not wait for the re for some reason.
//function displayRequestLineItemDetail() {
//    return overlib(OLresponseAJAX, TEXTPADDING, 0, CAPTIONPADDING, 4,
//            CAPTION, 'Request Line Items'
//            , WRAP
//            , BORDER, 5
//            , STICKY
//            , CLOSECLICK
//            , SCROLL
//            ,ABOVE
//            ,DRAGGABLE
//            ,STATUS, 'View request Line item'
//            , CGCLASS, 'popupheader'
//            , BGCLASS, 'borderstyle'
//            , CLOSEFONTCLASS, 'popupheader'
//            ,PADY, 0 , 0
//            );
//}
