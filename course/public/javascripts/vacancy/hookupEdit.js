jQuery(document).ready(function() {

    /* CONTACT ON DATE : START */
    jQuery("#contactedOnId").datepicker({
            showAnim: 'blind',
            maxDate: "+0D"
        });
    /* CONTACT ON DATE : END */

    /* CANDIDATE : START */
    var candidateSelectInplace =    jQuery("#candidateSelectInplace");
    var candidateNameSpan =         jQuery("#candidateName");
    var candidateIdField =          jQuery("#candidateId");
    var candidateNameField =        jQuery("#candidateNameId");
    var candidateSelectControls =   jQuery("#candidateSelectControls");
    var candidateSelectHref =       jQuery("#candidateSelectHref");
    var candidateCreateHref =       jQuery("#candidateCreateHref");

    candidateSelectInplace.hide();
    candidateSelectInplace.blur(function() {
        candidateSelectControls.show();
        candidateSelectInplace.hide();
        candidateSelectInplace.val("");
    });
    candidateSelectInplace.autocomplete({
        source: "/autocompletePerson",
        minLength: 3,
        delay: 500,
        select: function(event, widget) {
            candidateNameSpan.text(widget.item.value);

            candidateIdField.val(widget.item.personId);
            candidateNameField.val(widget.item.value);
            candidateSelectInplace.hide();
            candidateSelectControls.show();
            return false;
        }
    });

    function activateCandidateInplace(event) {
        candidateSelectControls.hide();
        candidateSelectInplace.show();
        candidateSelectInplace.focus();
        event.preventDefault();
    }

    candidateSelectHref.click(activateCandidateInplace);
    candidateSelectHref.keypress(function(event) {
        if (event.which == 13) {
            activateCandidateInplace(event);
        }
    });

    /* CANDIDATE : END */

    /* POSITION:START */
    var positionCaptionField = jQuery("#candidateCurrentPositionId");

    positionCaptionField.autocomplete({
        source: "/autocompletePosition",
        minLength: 2,
        delay: 500
    });
    /* POSITION:END */

    /* COMPANY:START */
    var companyNameField = jQuery("#candidateCurrentCompanyId");

    companyNameField.autocomplete({
        source: "/autocompleteCompany",
        minLength: 3,
        delay: 500,
        select: function(event, widget) {
            companyNameField.val(widget.item.value);
            return false;
        }
    });
    /* COMPANY:END */

    /* RECOMMENDER : START */
    var recommenderSelectInplace =  jQuery("#recommenderSelectInplace");
    var recommenderNameSpan =       jQuery("#recommenderName");
    var recommenderIdField =        jQuery("#recommenderId");
    var recommenderNameField =      jQuery("#recommenderNameId");
    var recommenderSelectControls = jQuery("#recommenderSelectControls");
    var recommenderSelectHref =     jQuery("#recommenderSelectHref");
    var recommenderCreateHref =     jQuery("#recommenderCreateHref");

    recommenderSelectInplace.hide();
    recommenderSelectInplace.blur(function() {
        recommenderSelectControls.show();
        recommenderSelectInplace.hide();
        recommenderSelectInplace.val("");
    });
    recommenderSelectInplace.autocomplete({
        source: "/autocompletePerson",
        minLength: 3,
        delay: 500,
        select: function(event, widget) {
            recommenderNameSpan.text(widget.item.value);
            
            recommenderIdField.val(widget.item.personId);
            recommenderNameField.val(widget.item.value);
            recommenderSelectInplace.hide();
            recommenderSelectControls.show();
            return false;
        }
    });
    
    function activateRecommenderInplace(event) {
        recommenderSelectControls.hide();
        recommenderSelectInplace.show();
        recommenderSelectInplace.focus();
        event.preventDefault();
    }

    recommenderSelectHref.click(activateRecommenderInplace);
    recommenderSelectHref.keypress(function(event) {
        if (event.which == 13) {
            activateRecommenderInplace(event);
        }
    });

    /* RECOMMENDER : END */

    /* MODAL FORM : START */
    var firstNameField =    jQuery("#firstNameId");
    var secondNameField =   jQuery("#secondNameId");
    var lastNameField =     jQuery("#lastNameId");
    var shortNoteField =    jQuery("#shortNoteId");
    var professionField =   jQuery("#generalProfessionId");
    var profDetailsField =  jQuery("#detailsId");
    var generalContactMsg = jQuery("div.contact-line.control > div.messages");

    var PERSON_TYPE_CANDIDATE = "candidate",
    PERSON_TYPE_RECOMMENDER = "recommender",
    personTypeToCreate;

    jQuery("#personModalForm").dialog({
        autoOpen: false,
        height: 'auto',
        width: 800,
        modal: true,
        buttons: {
            "Save Person": function() {
                if (validateModalForm()) {
                    var postData = {
                            firstName:                  firstNameField.val(),
                            secondName:                 secondNameField.val(),
                            lastName:                   lastNameField.val(),
                            shortNote:                  shortNoteField.val(),
                            generalProfessionValue:     professionField.val(),
                            professionDetails:          profDetailsField.val()
                    }

                    var stopPosting = false;
                    for (var i = 0; !stopPosting; i++) {
                        (function(index) {
                            var contactTypeId =         jQuery("#contact:id:" + index);
                            var contactTypeField =      jQuery("#contact:type:" + index);
                            var contactValueField =     jQuery("#contact:value:" + index);
                            var contactPrimaryField =   jQuery("#contact:primary:" + index);

                            if (contactTypeField.length == 0) {
                                stopPosting = true;
                                return;
                            }

                            postData["contact:id:" + index] = contactTypeId.val();
                            postData["contact:type:" + index] = contactTypeField.val();
                            postData["contact:value:" + index] = contactValueField.val();
                            if (contactPrimaryField.is(":checked")) {
                                postData["contact:primary:" + index] = "on";
                            }
                        }(i));
                    }

                    jQuery("#personModalSaving #success").hide();
                    jQuery("#personModalSaving #loading").show();
                    jQuery("#personModalSaving").dialog("open");

                    jQuery.ajax(
                            {
                                type: "POST",
                                url: "/personEditSubmitAsync",
                                data: postData
                            }
                    ).done(
                            function(responseMsg) {
                                jQuery("#personModalSaving #success").show();
                                jQuery("#personModalSaving #loading").hide();
                                if (personTypeToCreate === PERSON_TYPE_CANDIDATE) {
                                    candidateIdField.val(responseMsg.personId);
                                    candidateNameSpan.text(responseMsg.personName);
                                } else if (personTypeToCreate === PERSON_TYPE_RECOMMENDER) {
                                    recommenderIdField.val(responseMsg.personId);
                                    recommenderNameSpan.text(responseMsg.personName);
                                }
                            }
                    ).fail(
                        function(response) {
                            jQuery("#personModalSaving").dialog("close");
                            var responseMsg = jQuery.parseJSON(response.responseText);
                            clearForm();
                            for (entry in responseMsg.validationContext) {
                                (function(entryName) {
                                    var field = jQuery("[name='" + entryName + "']");
                                    if (field.length == 1) {
                                        document.profwell.validation.updateMessages(field, validationContext[entryName]);
                                    } else if (entryName.startsWith("contact:")) {
                                        var number = entryName.substring(entryName.indexOf(":") + 1);
                                        field = jQuery("[name='contact:type" + number + "']");
                                        document.profwell.validation.updateMessages(field, validationContext[entryName]);
                                    } else if (entryName == "contact") {
                                        document.profwell.validation.updateMessages(generalContactMsg, validationContext[entryName]);
                                    }
                                }(entry));
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

    jQuery("#personModalSaving").dialog({
        dialogClass: "no-close",
        autoOpen: false,
        height: 'auto',
        width: 300,
        modal: true
    });

    jQuery("#completePersonSaving").click(function(event) {
        jQuery("#personModalSaving").dialog("close");
        jQuery("#personModalForm").dialog("close");
        event.preventDefault();
    });

    function validateModalForm() {
        valid = true;
        valid = valid & document.profwell.validation.checkNotEmpty(firstNameField)
                & document.profwell.validation.checkLengthLimit(firstNameField);
        valid = valid & document.profwell.validation.checkLengthLimit(secondNameField);
        valid = valid & document.profwell.validation.checkNotEmpty(lastNameField)
                & document.profwell.validation.checkLengthLimit(firstNameField);

        valid = valid & document.profwell.validation.checkNotEmpty(professionField);
        valid = valid & document.profwell.validation.checkLengthLimit(profDetailsField, 2000);

        return valid & validateContacts();
    }

    function validateContacts() {
        var valid = true;
        var stopValidation = false;
        var primaryCount = 0;
        for (var i = 0; !stopValidation; i++) {
            (function(index) {
                var contactTypeField = jQuery("#contact:type:" + index);
                var contactValueField = jQuery("#contact:value:" + index);
                var contactPrimaryField = jQuery("#contact:primary:" + index);

                if (contactTypeField.length == 0) {
                    if (primaryCount > 2) {
                        document.profwell.validation.updateMessages(
                                generalContactMsg,
                                [{severity: "error", message: "Only 2 contacts can be marked, as primary."}]);
                        valid = false;
                    }
                    stopValidation = true;
                    return;
                }

                var contactType = contactTypeField.val();
                if (contactType.length == 0) {
                    document.profwell.validation.updateMessages(contactType, [{severity: "error", message: "Please set Contact Type."}]);
                    valid = false;
                } else if (contactType.length > 16) {
                    document.profwell.validation.updateMessages(contactType, [{severity: "error", message: "Contact Type can't be greater than 16 symbols in length."}]);
                    valid = false;
                }

                var contactValue = contactValueField.val()
                if (contactValue.length == 0) {
                    document.profwell.validation.updateMessages(contactValueField, [{severity: "error", message: "Please set Contact Value."}]);
                    valid = false;
                } else if (contactValue.length > 128) {
                    document.profwell.validation.updateMessages(contactValueField, [{severity: "error", message: "Contact Value can't be greater than 128 symbols in length."}]);
                    valid = false;
                }

                if (contactPrimaryField.is(':checked')) {
                    primaryCount = primaryCount + 1;
                }

            }(i));
        }

        return valid;
    }

    function clearForm() {
        firstNameField.val("");
        secondNameField.val("");
        lastNameField.val("");
        shortNoteField.val("");
        professionField.val("NULL");
        profDetailsField.val("");
        jQuery(".contact-line.empty").show();
        jQuery(".contact-item").remove();
        jQuery("#personModalForm div.messages").hide();
    }

    candidateCreateHref.click(function(event) {
        jQuery("#personModalForm").dialog("open");
        personTypeToCreate = PERSON_TYPE_CANDIDATE;
        event.preventDefault();
    });

    recommenderCreateHref.click(function(event) {
        jQuery("#personModalForm").dialog("open");
        personTypeToCreate = PERSON_TYPE_RECOMMENDER;
        event.preventDefault();
    });
    /* MODAL FORM : END */

});