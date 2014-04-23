jQuery(document).ready(function() {

    /* POSITION:START */
    var positionCaptionField = jQuery("#positionCaptionId");

    positionCaptionField.autocomplete({
        source: "/autocompletePosition",
        minLength: 2,
        delay: 500
    });
    /* POSITION:END */

    /* COMPANY:START */
    var companyNameField = jQuery("#companyNameId");
    var companyDetailsField = jQuery("#companyDetailsId");
    var companySBenefsField = jQuery("#companySocialBenefitsId");

    companyNameField.autocomplete({
        source: "/autocompleteCompany",
        minLength: 3,
        delay: 500,
        select: function(event, widget) {
            companyNameField.val(widget.item.value);
            companyDetailsField.val(widget.item.details);
            companySBenefsField.val(widget.item.socialBenefits);
            return false;
        }
    });
    /* COMPANY:END */

    /* COUNTRY:START */
    var countryField = jQuery("#countryId");
    var cityField = jQuery("#cityId");

    cityField.autocomplete({
        minLength: 2,
        delay: 500,
        source: function(request, response) {
            jQuery.getJSON(
                    "/autocompleteCity",
                    {
                        country: countryField.val(),
                        term: request.term
                    },
                    response);
        },
        select: function(event, widget) {
            cityField.val(widget.item.value);
            countryField.val(widget.item.country);
            return false;
        }
    });
    /* COUNTRY:END */

    /* DUE DATE:START */
    jQuery("#dueDateId").datepicker({
            showAnim: 'blind'
    });
    /* DUE DATE:END */
    
});