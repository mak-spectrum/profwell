jQuery(document).ready(function() {

    var uuidField = jQuery("input#partnerUuidId");
    var formContainer = jQuery("div#modalContainer");
    var info = jQuery("p#infoMessage");
    var error = jQuery("p#errorMessage");

    if (jQuery(".partner-item").length == 0) {
        jQuery(".partner-item-empty").show();
    } else {
        jQuery(".partner-item-empty").hide();
    }

    if (jQuery(".myPR-item").length == 0) {
        jQuery(".myPR-item-empty").show();
    } else {
        jQuery(".myPR-item-empty").hide();
    }

    if (jQuery(".inPR-item").length == 0) {
        jQuery(".inPR-item-empty").show();
    } else {
        jQuery(".inPR-item-empty").hide();
    }

    formContainer.dialog({
        autoOpen: false,
        height: "auto",
        width: 700,
        modal: true,
        buttons: {
            "Send Request": function() {
                var self = this;

                var valid = true;
                uuidField.removeClass("ui-state-error");

                if (uuidField.val().length == 0) {
                    uuidField.addClass("ui-state-error");
                    uuidField.focus();
                    return;
                }

                var postData = {};
                postData.uuid = uuidField.val();
                postData.relationshipType = jQuery("input[name='partnershipType']:checked").val();

                jQuery.ajax(
                        {
                            type: "GET",
                            url: "/partnershipRequestSendAsync",
                            data: postData
                        }
                ).done(
                        function(responseMsg) {
                            clearForm();
                            info.show();
                            info.text(responseMsg.validationContext.message[0].message);

                            setTimeout(
                                    function() {
                                        info.hide();
                                        window.location.reload();
                                    },
                                    1000
                            );
                        }
                ).fail(
                        function(response) {
                            var responseMsg = jQuery.parseJSON(response.responseText);

                            error.show();
                            error.text(responseMsg.validationContext.message[0].message);
                        }

                );
            },

            Cancel: function() {
                jQuery(this).dialog("close");
            }
        }
    });

    window.requestPartnership = function() {
        clearForm();
        formContainer.dialog("open");
    }
    
    function clearForm() {
        uuidField.val("");
        info.hide();
        error.hide();
    };

    jQuery('.break-partnership').click(function (event) {
        var confirmResult = confirm("Are you sure you want to break partnership?");
        if (!confirmResult) {
            event.stopPropagation();
            event.preventDefault();
            return false;
        }
    });

    jQuery('.cancel-partnership-request').click(function (event) {
        var confirmResult = confirm("Are you sure you want to cancel partnership request?");
        if (!confirmResult) {
            event.stopPropagation();
            event.preventDefault();
            return false;
        }
    });

    jQuery('.decline-partnership-request').click(function (event) {
        var confirmResult = confirm("Are you sure you want to decline partnership?");
        if (!confirmResult) {
            event.preventDefault();
            return false;
        }
    });

    jQuery('.accept-partnership-request').click(function (event) {
        var confirmResult = confirm("Are you sure you want to accept partnership?");
        if (!confirmResult) {
            event.preventDefault();
            return false;
        }
    });

});