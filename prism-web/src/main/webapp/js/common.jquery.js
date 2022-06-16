/**
 * Dennis Rausch
 * 4/21/2014
 *
 * This is a generic container to hold jquery scripts that are MDH developed. Anything that isn't large or complex
 * enough to warrant it's own plugin should go in here.
 */
(function( $ ) {
    //This function is supposed to automatically recalculate estimated cost and amount on all cart pages.
    $.fn.updateAmount = function(){
        var cost = 0;
        var base = "[name='" + $(this).attr('name').split('.')[0];
        var qty  = parseInt($(base + ".quantity']").toNumeric().val());

        //This block checks which type of line item it is and uses the appropriate property.
        if(typeof($(base + ".itemCost']").val()) === 'undefined')
            cost = parseFloat($(base + ".item.dispenseUnitCost']").toNumeric().val());
        else
            cost = parseFloat($(base + ".itemCost']").toNumeric().val());

        //If we don't have numbers, default to a reasonable value.
        if(isNaN(qty)) qty = 1;
        if(isNaN(cost)) cost = 0;

        //Write back the normalized qty and cost.
        $(base + ".quantity']").val(qty);

        if(typeof($(base + ".itemCost']").val()) === 'undefined')
            $(base + ".item.dispenseUnitCost']").val(cost.toFixed(2));
        else
            $(base + ".itemCost']").val(cost.toFixed(2));

        //Update the funding source amount field and estimated cost. Currently only uses the first funding source
        //TODO Make this iterate through an array of funding sources and divide the total?
        if(typeof($(base + ".fundingSourceForms[1].amount']").val()) === 'undefined')
            $(base + ".fundingSourceForms[0].amount']").val((cost*qty).toFixed(2));
        $(base + ".estimatedCost']").val((cost*qty).toFixed(2));
        return this;
    };

    //This function takes any input and uses regex to remove any characters that are not digits or decimal points, then parses the result to a float.
    $.fn.toNumeric = function() {
        return this.each(function(){
            $(this).val(parseFloat($(this).val().replace(/[^\d\.\-]+/g,'')));
        });
    };

    $.fn.updateBudgetEmailBody = function() {
        var base = "[name='" + $(this).attr('name').split('.')[0];
        var budgetSelector = $(base + ".orgBudgetId']");
        var budgetText = budgetSelector.find(':selected').text();

        $('.budgetEmail').prop('href', function(i, prop) {
            return prop + '&body=' + encodeURIComponent("Please make the following assignments to budget: " + budgetText);
        });
    };

    $.fn.prioritySelected = function() {
        var priority = $('#priority').val();
        switch (priority) {
            case 'HIGH':
                $.fn.updateShippingRateInfo('Higher shipping rates will apply');
                break;
            case 'NORM':
                $.fn.updateShippingRateInfo('Normal shipping rates may/will apply');
                break;
            case 'LOW':
                $.fn.updateShippingRateInfo('Higher shipping rates will apply');
                break;
        }
    };

    $.fn.updateShippingRateInfo = function(text) {
        $('#shipping-rate').text(text);
    };
    
})(jQuery);