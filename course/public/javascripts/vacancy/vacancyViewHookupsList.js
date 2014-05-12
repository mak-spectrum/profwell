jQuery(document).ready(function() {

    /* HOOKUPS : START */
    var archCheckbox = jQuery("#show-archived-checkbox");

    // disable requesting, while a request is in progress
    var requestAllowed = true;

    var idValueField = jQuery("#idValue");
    var emptyListItem = jQuery("li.hookup-empty");
    var loadingListItem = jQuery("li.hookup-loading");
    var errorListItem = jQuery("li.hookup-error");
    var hookupsList = jQuery("ul.hookups-list");

    errorListItem.hide();

    function loadHookups() {

        if (requestAllowed) {
            requestAllowed = false;
        } else {
            return;
        }

        if (hookupsList.hasClass("loaded")) {
            requestAllowed = true;
            return;
        }

        jQuery("li.hookup-item", hookupsList).remove();

        loadingListItem.show();
        errorListItem.hide();
        emptyListItem.hide();

        var requestData = {vacancyId: idValueField.val()};
        if (archCheckbox.is(":checked")) {
            requestData.archived = true;
        }

        jQuery.ajax(
                {
                    url: "/vacancyHookupsAsync",
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
                    hookupsList.addClass("loaded");
                    requestAllowed = true;
                }
        ).fail(
                function(response) {
                    loadingListItem.hide();
                    errorListItem.show();
                    errorListItem.text("Error on loading hookups from the server.");
                    requestAllowed = true;
                }

        );
    }

    function processEmptyPlaceholder() {
        if (jQuery("ul.hookups-list > li.hookup-item").length > 0) {
            emptyListItem.hide();
        } else {
            emptyListItem.show();
        }
    }
    processEmptyPlaceholder();

    jQuery("#hookupsTabToggle")
    .click(loadHookups)
    .keypress(function(event) {
        if (event.which == 13) {
            loadHookups();
        }
    });

    archCheckbox.change(function() {
        hookupsList.removeClass("loaded")
        loadHookups();
    });




    window.moveHookupToStatus = function(hookupId, nextStatus) {
        var requestData = {
                "hookupId": hookupId,
                status: nextStatus
        };

        var holder = jQuery("div#hookup-item\\:" + hookupId);
        var statusesGroup = holder.children("div.hookup-control").children("div.statuses-group");
        statusesGroup.empty();

        var loadingGif = createLoadingImg();
        statusesGroup.prepend(loadingGif);

        jQuery.ajax(
                {
                    url: "/vacancyHookupToStatusAsync",
                    data: requestData
                }
        ).done(
                function(responseMsg) {
                    jQuery("strong.status", holder).text(responseMsg.newStatusCaption);
                    jQuery("strong.last-update", holder).text(responseMsg.lastActivityTime);

                    var attachButton = jQuery("a.attach-to-record", holder);
                    if (responseMsg.documentAttachable) {
                        attachButton.show();
                        attachButton.attr('href',
                                "javascript:hookupAttachFile(" + hookupId + ", '" + responseMsg.attachableDocumentTitle + "');");
                        attachButton.attr('title',
                                responseMsg.attachableDocumentTitle);
                    } else {
                        attachButton.hide();
                    }

                    var archiveButton = jQuery("a.archive-record", holder);
                    if (responseMsg.canBeArchived) {
                        archiveButton.show();
                    } else {
                        archiveButton.hide();
                    }

                    processStatusButtons(responseMsg, hookupId, statusesGroup);

                    loadingGif.remove();

                    if (responseMsg.newStatus == 'PROBATION_IN_PROGRESS') {
                        if (confirm("Probation started. Would you like to suspend talent acquisition on the vacancy?")) {
                            jQuery("#vacancySuspendLink").click();
                        }
                    } else if (responseMsg.newStatus == 'HIRED') {
                        if (confirm("Candidate has been hired. Would you like to close the vacancy? This action can't be undone.")) {
                            window.vacancyClose(jQuery("#idValue").val(), true);
                        }
                    }
                }
        ).fail(
                function(response) {
                    loadingGif.remove();
                    holder.append("Error on status update, please try again later or contact administration.");
                }

        );
    }

    window.hookupArchiveAsync = function(hookupId) {
        var holder = jQuery("div#hookup-item\\:" + hookupId);
        var statusesGroup = holder.children("div.hookup-control").children("div.statuses-group");
        statusesGroup.empty();

        var loadingGif = createLoadingImg();
        statusesGroup.append(loadingGif);

        jQuery.ajax(
                {
                    url: "/vacancyHookupArchiveAsync",
                    data: { "hookupId": hookupId }
                }
        ).done(
                function(responseMsg) {
                    jQuery("strong.last-update", holder).text(responseMsg.lastActivityTime);

                    jQuery("a.edit-record", holder).hide();
                    jQuery("a.archive-record", holder).hide();
                    jQuery("a.delete-record", holder).hide();
                    jQuery("a.attach-to-record", holder).hide();
                    jQuery("a.hookup-delete-file", holder).hide();
                    jQuery("a.unarchive-record", holder).show();

                    holder.addClass("archived");

                    loadingGif.remove();
                }
        ).fail(
                function(response) {
                    loadingGif.remove();
                    holder.append("Error on status update, please try again later or contact administration.");
                }

        );
    }

    window.hookupUnarchiveAsync = function(hookupId) {
        var holder = jQuery("div#hookup-item\\:" + hookupId);
        var statusesGroup = holder.children("div.hookup-control").children("div.statuses-group");

        var loadingGif = createLoadingImg();
        statusesGroup.append(loadingGif);

        jQuery.ajax(
                {
                    url: "/vacancyHookupUnarchiveAsync",
                    data: { "hookupId": hookupId }
                }
        ).done(
                function(responseMsg) {
                    jQuery("strong.last-update", holder).text(responseMsg.lastActivityTime);

                    jQuery("a.edit-record", holder).show();
                    jQuery("a.archive-record", holder).show();
                    jQuery("a.delete-record", holder).show();
                    jQuery("a.hookup-delete-file", holder).show();
                    jQuery("a.unarchive-record", holder).hide();

                    processStatusButtons(responseMsg, hookupId, statusesGroup);

                    holder.removeClass("archived");

                    loadingGif.remove();
                }
        ).fail(
                function(response) {
                    loadingGif.remove();
                    holder.append("Error on status update, please try again later or contact administration.");
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

    function processStatusButtons(responseMsg, hookupId, statusesGroup) {
        for (var i = 0; i < responseMsg.statusMoves.length; i++) {
            var step = responseMsg.statusMoves[i];
            var href = jQuery("<a/>",
                    {
                        href: "javascript:moveHookupToStatus(" + hookupId + ", '" + step.toStatus + "')",
                        title: step.actionDescription
                    });
            var img = jQuery("<img/>",
                    {
                        src: step.statusImage,
                        alt: step.statusImageAlt
                    });
            href.append(img);
            statusesGroup.append(href);
        }
    };

    window.hookupDeleteAsync = function(hookupId) {
        if (confirm("Are you sure, you want to delete selected hookup? This action can't be undone.")) {
            jQuery.ajax(
                    {
                        url: "/vacancyHookupDeleteAsync",
                        data: { "hookupId": hookupId }
                    }
            );
            jQuery("div#hookup-item\\:" + hookupId).parent().remove();
            processEmptyPlaceholder();
        }
    }
    /* HOOKUPS : END */

    window.vacancyClose = function(vacancyId, passWithoutCheck) {
        if (passWithoutCheck || confirm("Are you sure, you want to close the vacancy? This action can't be undone.")) {
            window.location = "/vacancyClose?id=" + vacancyId;
        }
    }

});