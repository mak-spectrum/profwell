jQuery(document).ready(function() {

    document.profwell = document.profwell || {};
    document.profwell.validation = document.profwell.validation || {};

    document.profwell.validation.updateMessages = function(field, texts) {
        var ul = field.parent().find("div.messages > ul");

        if (ul.length == 0) {
            var messages = field.parent().find("div.messages");
            messages.append($("<div />", {class: "indent20Percent", text: '\u00a0'}));
            var ul = $("<ul />", {});
            messages.append(ul);
        }

        ul.empty();
        if (texts.length > 0) {
            ul.parent().show();
            field.addClass("ui-state-error");
            for (var i = 0; i < texts.length; i++) {
                ul.append($("<li />", {class: texts[i].severity, text: texts[i].message}));
            }
        } else {
            ul.parent().hide();
        }
    }

    document.profwell.validation.checkLengthLimit = function(field, limit) {
        if (!limit) {
            limit = 128;
        }
        if (field.val().length > limit) {
            document.profwell.validation.updateMessages(field, [{severity: "error", message: "Field can't be greater than " + limit + " symbols in length."}]);
            return false;
        }
        return true;
    }

    document.profwell.validation.checkNotEmpty = function(field) {
        if (field.prop("tagName").toLowerCase() == "select") {
            if (field.val() == "NULL") {
                document.profwell.validation.updateMessages(field, [{severity: "error", message: "Please, select a value."}]);
                return false;
            }
        } else {
            if (field.val().length == 0) {
                document.profwell.validation.updateMessages(field, [{severity: "error", message: "Please, fill in the field."}]);
                return false;
            }
        }
        return true;
    }

});