jQuery(document).ready(function() {

    // disable requesting, while a request is in progress
    var requestAllowed = true;
    
    var idValueField = jQuery("#idValue");
    var emptyListItem = jQuery("li.partner-empty");
    var loadingListItem = jQuery("li.partner-loading");
    var errorListItem = jQuery("li.partner-error");
    var partnersList = jQuery("ul.partners-list");
    var saveSharing = jQuery("a.save-sharing");

    errorListItem.hide();

    jQuery("div#partner-save-success").tooltip()
    jQuery("div#partner-save-error").tooltip()

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
        saveSharing.hide();

        loadingListItem.show();
        errorListItem.hide();
        emptyListItem.hide();

        var requestData = {vacancyId: idValueField.val()};

        jQuery.ajax(
                {
                    url: "/vacancyPartnersAsync",
                    data: requestData
                }
        ).done(
                function(responseMsg) {
                    window.vacancy = {};
                    window.vacancy.sharingConfiguration = responseMsg;

                    jQuery.each(responseMsg, function(i, el) {
                        var li = jQuery("<li />", {class: "partner-item"});
                        li.insertAfter(loadingListItem);

                        var checkbox = jQuery("<input />",
                                {
                                    id: "partner-share:id:" + el.partnerId,
                                    name: "partner-share:" + el.partnerId,
                                    type: "checkbox"
                                });

                        li.append(checkbox);

                        var label = jQuery("<label />",
                                {
                                    for: "partner-share:id:" + el.partnerId,
                                    class: "gray-check-red",
                                    text: el.name
                                });
                        li.append(label);

                        var helpDiv = jQuery("<div />",
                                    {
                                        class: "tooltip-help",
                                        title: "This partner has added a candidate to this vacancy, so you can't disable sharing configuration. It is only possible, to switch the sharing configuration to 'readonly' mode, so the partner will be able to see the vacancy and his hookups, but won't be able to attach new candidates."
                                    });

                        li.append(helpDiv);
                        helpDiv.tooltip();
                        helpDiv.hide();


                        if (el.enabled) {
                            checkbox.attr("checked", "checked");
                        }

                        if (el.readonly || !el.canBeDisabled) {
                            label.addClass("frozen");
                            if (el.readonly) {
                                checkbox.removeAttr("checked");
                            }
                            if (!el.enabled) {
                                helpDiv.show();
                            }
                        }


                        checkbox.click(function() {
                            if (el.readonly && el.canBeDisabled) {
                                if (label.hasClass("frozen")) {
                                    label.removeClass("frozen");
                                    checkbox.removeAttr("checked");
                                }
                            }

                            saveSharing.show(400);
                            partnersList.removeClass("loaded");

                            if (!el.canBeDisabled) {
                                if (!checkbox.is(':checked')) {
                                    helpDiv.show();
                                } else {
                                    helpDiv.hide();
                                }
                            } else if (!checkbox.is(':checked')) {
                                helpDiv.hide();
                            }
                        });

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

    saveSharing
    .click(processSharingConfiguration)
    .keypress(function(event) {
        if (event.which == 13) {
            processSharingConfiguration();
        }
    });

    function processSharingConfiguration() {
        saveSharing.hide();

        var postData = {
                vacancyId: idValueField.val()
                };

        jQuery("input[id^='partner-share:id:'")
        .each(
                function(i, el) {
                    var element = jQuery(el);
                    postData[element.attr("name")] = element.is(":checked")
                });

        jQuery.ajax(
                {
                    type: "POST",
                    url: "/vacancyPartnersSubmitAsync",
                    data: postData
                }
        ).done(
                function(responseMsg) {
                    jQuery("div#partner-save-success").show();
                }
        ).fail(
                function(response) {
                    jQuery("div#partner-save-error").show();
                }
        );
    }

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