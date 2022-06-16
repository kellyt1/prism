//Ensures that item is selected from list. Added to make
//selects in Nestcape navigator more robust.
function selectFromListWithNavigation(form, elementName, url) {
    var element = getFormElementByName(elementName);
    if (validNumericListValue(element.value)) {
        window.location = url + "?" + element.name + "=" + element.value;
    }
}


//Convenience method for changing action on a
//form and submitting the form
function setActionAndSubmit(formName, action) {
    var form = document.forms[formName];
    form.action = action;
    form.submit();
}

// Retrieves first element in a form by the element name 
function getFormElementByName(form, elementName) {
    for (var i = 0; i < form.elements.length; i++) {
        if (form.elements[i].name == elementName)
            return form.elements[i];
    }
    return null;
}

function validNumericListValue(value) {
    return !isNaN(value);
}

function selectAll(collection, property, collectionSize) {
    for (var i = 0; i < collectionSize; i++) {
        var name = collection + '[' + i + '].' + property;
        document.getElementsByName(name)[0].checked = document.getElementsByName('checkall')[0].checked;
    }
}

function submitParameter(formName, formNbr, action, parameterName, parameterValue) {
    document.getElementsByName(parameterName)[0].value = parameterValue;
    document.getElementsByName(formName)[formNbr].action = action;
    document.getElementsByName(formName)[formNbr].submit();
}

function setParameterInParentWindow(paramaterName, parameterValue) {
    window.opener.document.getElementsByName(paramaterName)[0].value = parameterValue;
}

function submitForm(formName) {
    document.getElementsByName(formName)[0].submit();
}

function setFormValue(variableName, value) {
    var element = document.getElementsByName("" + variableName)[0].value = value;
}

function getFormValue(variableName) {
    return document.getElementsByName("" + variableName)[0].value;
}

function toggleSorting(sortByPropertyName, sortByComparedValue, sortMethodPropertyName) {
    if (getFormValue(sortByPropertyName) == sortByComparedValue) {
        if (getFormValue(sortMethodPropertyName) == 'asc') {
            setFormValue(sortMethodPropertyName, 'desc')
        }
        else {
            setFormValue(sortMethodPropertyName, 'asc')
        }
    }
}


function refreshAmount() {
    var patt = /[^\d\.\-]+/g;
    var qty = parseInt(document.requestLineItemForm.quantity.value.replace(patt,''));
    var cost = parseFloat(document.requestLineItemForm.itemCost.value.replace(patt,''));
    var amt = document.getElementsByName("fundingSourceForms[0].amount");


    if(isNaN(qty)){ qty = 1;}
    if(isNaN(cost)){ cost = 0.00;}
    document.requestLineItemForm.quantity.value = qty;
    document.requestLineItemForm.itemCost.value = cost.toFixed(2);

    for (var i = 0; i < amt.length; i++) {
        amt[i].value = (cost * qty).toFixed(2);
    }
}

function toggleDetails(id) {
    $j('.detail' + id).slideToggle(200).css('display', 'block');
    $j('.detail-toggle' + id).toggleClass('glyphicon-chevron-right glyphicon-chevron-down');
}