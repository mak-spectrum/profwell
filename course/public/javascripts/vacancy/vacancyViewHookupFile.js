jQuery(document).ready(function() {

    var attachToRecord = jQuery("#attach-to-record");

    var hookupIdField = jQuery("#fileUploadHookupId");
    var fileField = jQuery("#fileUploadFileValueId");
    var submitField = jQuery("#fileUploadFormSubmitId");

    var progress = jQuery("#fileUploadProgress");
    var bar = jQuery("#fileUploadBar");
    var message = jQuery("#fileUploadMessage");
    var percent = jQuery("#fileUploadPercent");

    var percent = jQuery("#fileUploadPercent");

    var formContainer = jQuery("div#fileUploadModalContainer");

    var options = {
            beforeSend: function()
            {
                progress.show();
            },

            uploadProgress: function(event, position, total, percentComplete)
            {
                bar.width(percentComplete + "%");
                percent.html(percentComplete + "%");
            },

            success: function()
            {
                bar.width("100%");
                percent.html("100%");
            },

            complete: function(response)
            {
                var responseObject = jQuery.parseJSON(response.responseText);
                if (response.status == 200) {
                    message.html("<font color='gray'>" + responseObject.message + "</font>");
                    fileField.hide();
                    submitField.hide();
                    showStoredDocument(responseObject);
                    setTimeout(function() {
                        formContainer.dialog("close");
                    }, 2000);
                } else if (responseObject.message) {
                    message.html("<font color='red'>" + responseObject.message + "</font>");
                }
            },

            error: function()
            {
                message.html("<font color='red'> ERROR: unable to upload file</font>");
            }
         
    };

    function showStoredDocument(responseObject) {
        if ("resume" == responseObject.docType) {
            var resumeBlock = jQuery("div.resumeBlock", jQuery("div#hookup-item\\:" + hookupIdField.val()));
            resumeBlock.empty();
            resumeBlock.append(createDocumentBlock("resume: ", responseObject.docType, responseObject.fileName));
        } else if ("testtask" == responseObject.docType) {
            var testtaskBlock = jQuery("div.testtaskBlock", jQuery("div#hookup-item\\:" + hookupIdField.val()));
            testtaskBlock.empty();
            testtaskBlock.append(createDocumentBlock("test task: ", responseObject.docType, responseObject.fileName));
        } else if ("interview" == responseObject.docType) {
            var interviewBlock = jQuery("div.interviewBlock", jQuery("div#hookup-item\\:" + hookupIdField.val()));
            interviewBlock.append(createDocumentBlock("interview feedback: ", responseObject.docType, responseObject.fileName, responseObject.index));
        } else if ("probation" == responseObject.docType) {
            var probationBlock = jQuery("div.probationBlock", jQuery("div#hookup-item\\:" + hookupIdField.val()));
            probationBlock.append(createDocumentBlock("probation feedback: ", responseObject.docType, responseObject.fileName, responseObject.index));
        }
    }

    function createDocumentBlock(docCaption, docType, fileName, index) {
        var div = jQuery("<div />");
        if (index) {
            div.attr("id", "hookup-item:" + hookupId + ":" + docType + ":" + index);
        } else {
            div.attr("id", "hookup-item:" + hookupId + ":" + docType);
        }

        var span = jQuery("<span />", {class: "subline", text: docCaption});

        var hookupId = hookupIdField.val();

        var downloadUrl = "";
        var deleteUrl = "";
        if (index) {
            downloadUrl = "/vacancyHookupFile?hookupId=" + hookupId + "&type=" + docType + "&index=" + index;
            deleteUrl = "javascript:hookupDeleteFileAsync(" + hookupId + ", " + docType + ", " + index + ");";
        } else {
            downloadUrl = "/vacancyHookupFile?hookupId=" + hookupId + "&type=" + docType;
            deleteUrl = "javascript:hookupDeleteFileAsync(" + hookupId + ", " + docType + ");";
        }

        var downloadHref = jQuery("<a />", {href: downloadUrl, title: "Download file " + fileName});
        var image = jQuery("<img />", {src: document.downloadImageUrl, alt: "Download"});
        var strong = jQuery("<strong />", {text: fileName});
        downloadHref.append(image, strong);
        span.append(downloadHref);

        var deleteHref = jQuery("<a />", {href: deleteUrl, title: "Delete file " + fileName, class: "hookup-delete-file"});
        image = jQuery("<img />", {src: document.deleteImageUrl, alt: "Delete", class: "hookup-delete-file"});
        deleteHref.append(image);
        span.append(deleteHref);

        div.append(span);
        return div;
    }

    window.hookupDeleteFileAsync = function(hookupId, docType, index) {
        if (confirm("Are you sure, you want to delete selected document?")) {
            var requestData = {
                "hookupId": hookupId,
                "type": docType
            }
            if (index) {
                requestData.index = index;
            }

            jQuery.ajax(
                    {
                        url: "/vacancyHookupFileDeleteAsync",
                        data: requestData
                    }
            );
            if (index) {
                jQuery("div#hookup-item\\:" + hookupId + "\\:" + docType + "\\:" + index).remove();
                for (var i = (index + 1);; i++) {
                    var div = jQuery("div#hookup-item\\:" + hookupId + "\\:" + docType + "\\:" + i);
                    if (div.length > 0) {
                        div.attr("id", "hookup-item:" + hookupId + ":" + docType + ":" + (i - 1));
                        var span = jQuery(div.children()[0]);
                        jQuery(span.children()[0]).attr("href", "/vacancyHookupFile?hookupId=" + hookupId + "&type=" + docType + "&index=" + (i - 1));
                        jQuery(span.children()[1]).attr("href", "javascript:hookupDeleteFileAsync(" + hookupId + ", " + docType + ", " + (i - 1) + ");");
                    } else {
                        return;
                    }
                }
            } else {
                jQuery("div#hookup-item\\:" + hookupId + "\\:" + docType).remove();
            }
        }
    }

    jQuery("#fileUploadForm").ajaxForm(options);

    formContainer.dialog({
        autoOpen: false,
        height: "auto",
        width: 700,
        modal: true,
    });

    window.hookupAttachFile = function(hookupId, title) {
        clearForm();
        hookupIdField.val(hookupId);
        formContainer.dialog({title: title});
        formContainer.dialog("open");
    }

    function clearForm() {
        hookupIdField.val("");
        submitField.show();
        fileField.replaceWith(fileField = jQuery("<input/>",
            {
                id: fileField.attr("id"),
                type: "file",
                name: fileField.attr("name")
                
            }));
        bar.width("0%");
        message.html("");
        percent.html("0%");
        progress.hide();
    };

});