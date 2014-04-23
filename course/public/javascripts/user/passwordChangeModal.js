jQuery(document).ready(function() {

    var idValue = jQuery("#idValue");
    var currentPassword = jQuery("#currentPasswordId");
    var password = jQuery("#passwordId");
    var confirmPassword = jQuery("#confirmPasswordId");

    var p = jQuery("#generalMessage");
    p.hide();

    function checkPasswordConfirmation(pass, passConfirm) {
        if (pass.val() != passConfirm.val()) {
            pass.val("").addClass("ui-state-error").focus();
            passConfirm.val("").addClass("ui-state-error");
            document.profwell.validation.updateMessages(
                    passConfirm,
                    [{severity: "error", message: "Password and confirmation should be equal."}]);
            return false;
        }
        return true;
    }

    jQuery("#userPasswordChangeDialogForm").dialog({
        autoOpen: false,
        height: 'auto',
        width: 750,
        modal: true,
        buttons: {
            "Change Password": function() {
                var self = this;

                var valid = true;
                currentPassword.removeClass("ui-state-error");
                password.removeClass("ui-state-error");
                confirmPassword.removeClass("ui-state-error");

                if (currentPassword.length > 0) {
                    valid &= document.profwell.validation.checkLengthLimit(currentPassword);
                    if (!valid) {
                        return;
                    }
                }

                valid &= document.profwell.validation.checkLengthLimit(password);
                if (!valid) {
                    password.val("")
                    password.focus();
                }

                if (valid) {
                    valid &= checkPasswordConfirmation(password, confirmPassword);
                }

                if (valid) {
                    var postData = {};
                    postData.idValue = idValue.val();
                    if (currentPassword.length > 0) {
                        postData.currentPassword = currentPassword.val();
                    }
                    postData.password = password.val(),
                    postData.confirmPassword = confirmPassword.val()

                    jQuery.ajax(
                            {
                                type: "POST",
                                url: "/userPasswordChangeSubmitAsync",
                                data: postData
                            }
                    ).done(
                            function(responseMsg) {
                                clearForm();
                                p.show();
                                p.text(responseMsg.validationContext.generalMessage[0].message);

                                setTimeout(
                                        function() {
                                            p.hide();
                                            jQuery(self).dialog("close");
                                        },
                                        2000
                                );
                            }
                    ).fail(
                            function(response) {
                                var responseMsg = jQuery.parseJSON(response.responseText);

                                clearForm();

                                if (currentPassword.length > 0 && responseMsg.validationContext.currentPassword) {
                                    document.profwell.validation.updateMessages(
                                            currentPassword, responseMsg.validationContext.currentPassword);
                                }
                                if (responseMsg.validationContext.password) {
                                    document.profwell.validation.updateMessages(
                                            password, responseMsg.validationContext.password);
                                }
                                if (responseMsg.validationContext.confirmPassword) {
                                    document.profwell.validation.updateMessages(
                                            confirmPassword, responseMsg.validationContext.confirmPassword);
                                }
                            }

                    );
                }
            },

            Cancel: function() {
                jQuery(this).dialog("close");
            }
        },

        close: function() {
            clearForm();
        }
    });

    function clearForm() {
        currentPassword.val("").removeClass("ui-state-error");
        password.val("").removeClass("ui-state-error");
        confirmPassword.val("").removeClass("ui-state-error");
        document.profwell.validation.updateMessages(currentPassword, []);
        document.profwell.validation.updateMessages(password, []);
        document.profwell.validation.updateMessages(confirmPassword, []);
    }

    jQuery("#userPasswordChange")
    .click(function() {
        jQuery("#userPasswordChangeDialogForm").dialog("open");
    });
});