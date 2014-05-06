jQuery(document).ready(function() {

    // disable requesting, while a request is in progress
    var requestAllowed = true;
    
    var idValueField = jQuery("#idValue");
    var emptyListItem = jQuery("li.partner-empty");
    var loadingListItem = jQuery("li.partner-loading");
    var errorListItem = jQuery("li.partner-error");
    var partnersList = jQuery("ul.partner-list");

    errorListItem.hide();

    function loadPartners() {

        if (requestAllowed) {
            requestAllowed = false;
        } else {
            return;
        }

        if (partnersList.hasClass("loaded")) {
            requestAllowed = true;
            return;
        }

        jQuery("li.partner-item", partnersList).remove();

        loadingListItem.show();
        errorListItem.hide();
        emptyListItem.hide();

        var requestData = {vacancyId: idValueField.val()};
        if (archCheckbox.is(":checked")) {
            requestData.archived = true;
        }

        jQuery.ajax(
                {
                    url: "/vacancyPartnersAsync",
                    data: requestData
                }
        ).done(
                function(responseMsg) {
                    var html = jQuery.parseHTML(responseMsg);
                    jQuery.each(html, function(i, el) {
                        if (el.nodeName.toLowerCase() == "li") {
                            jQuery(el).insertAfter(loadingListItem);
                        }
                    });

                    processEmptyPlaceholder();

                    loadingListItem.hide();
                    partnersList.addClass("loaded");
                    requestAllowed = true;
                }
        ).fail(
                function(response) {
                    loadingListItem.hide();
                    errorListItem.show();
                    errorListItem.text("Error on loading partners from the server.");
                    requestAllowed = true;
                }

        );
    }

    function processEmptyPlaceholder() {
        if (jQuery("ul.partners-list > li.partner-item").length > 0) {
            emptyListItem.hide();
        } else {
            emptyListItem.show();
        }
    }
    processEmptyPlaceholder();

    jQuery("#vacancySharingTabToggle")
    .click(loadPartners)
    .keypress(function(event) {
        if (event.which == 13) {
            loadPartners();
        }
    });

    function createLoadingImg() {
        return jQuery("<img/>",
                {
                    src: document.loadingImageUrl,
                    class: "loading",
                    alt: "Loading",
                    title: "Please wait, loading is in process."
                });
    };

});