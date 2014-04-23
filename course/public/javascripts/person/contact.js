jQuery(document).ready(function() {

    var emptyLine = jQuery("div.contact-line.empty");

    jQuery("input[id^='contact:type:']").autocomplete({
        source: "/autocompleteContactName",
        minLength: 1,
        delay: 300
    });

    jQuery("a[id^='contact:remove:']").click(removeContactLine);


    var template = prepareTemplate();

    jQuery("#addContactButton").click(function (event) {
        /* Depends on views.Person.contactLine.scala.html */
        var div = jQuery(template["div"].element, template["div"].conf);

        var maximalIndex = parseInt(findMaximalIndex()) + 1;
        if (!maximalIndex) {
            maximalIndex = 0;
            // hide -nothing to display- block
            emptyLine.hide();
        }

        /* Create line from predefined config */
        var elemDefs = template["elementDefinitions"];
        for (elementDefinition in elemDefs) {
            var element = jQuery(
                    elemDefs[elementDefinition].element,
                    modifyConfiguration(elemDefs[elementDefinition].conf, maximalIndex));
            div.append(element);
        }

        /* Specify autocompletion for contact type */
        div.children("input[id^='contact:type:']").autocomplete({
            source: "/autocompleteContactName",
            minLength: 1,
            delay: 300
        });

        /* Create and add removal control */
        var removeCA = jQuery(
                template["removeContactA"].element,
                modifyConfiguration(template["removeContactA"].conf, maximalIndex));
        div.append(removeCA);

        removeCA.append(
                jQuery(template["removeContactImage"].element, template["removeContactImage"].conf));

        removeCA.click(removeContactLine);

        /* Add line */
        div.insertBefore(jQuery(event.target).parent());
        return false;
    });

    function removeContactLine(event) {
        /* Depends on views.Person.contactLine.scala.html */

        var element = jQuery(event.target);

        var result = confirm("Remove Contact?");

        if (result) {
            var contactLine;
            if (element.prop("tagName") != "A") { // click to image
                contactLine = element.parent().parent();
            } else {
                contactLine = element.parent();
            }

            var contactBlock = contactLine.parent();
            contactLine.remove();

            if (contactBlock.children(".contact-item").length == 0) {
                emptyLine.show();
            }
        }

        return false;
    }

    function findMaximalIndex() {
        var result;

        var labels = jQuery(".contact-line");

        jQuery.each(labels, function(index, object) {
            var name = jQuery(object.children[0]).prop("name");

            if (name) {
                result = name.substring(name.lastIndexOf(":") + 1);
            }
        });

        return result;
    }

    function modifyConfiguration(configuration, index) {

        var newConf = {};

        for (a in configuration) {
            if (a == "id" || a == "for" || a == "name") {
                newConf[a] = configuration[a] + index;
            } else {
                newConf[a] = configuration[a];
            }

            if (configuration.id == "contact:index:") {
                newConf["value"] = index;
            }
        }

        return newConf;
    }

    function prepareTemplate() {
        var contactTemplate = {};

        contactTemplate["div"] = {
                element: "<div />",
                conf: {
                    class: "contact-line contact-item"
                }
        }

        contactTemplate["elementDefinitions"] = {};

        contactTemplate["elementDefinitions"]["inputId"]= {
                element: "<input />",
                conf: {
                    id: "contact:id:",
                    name: "contact:",
                    type: "hidden",
                    value: "0"
                }
        }

        contactTemplate["elementDefinitions"]["labelType"]= {
                element: "<label />",
                conf: {
                    for: "contact:type:",
                    text: "Type:"
                }
        }

        contactTemplate["elementDefinitions"]["inputType"]= {
                element: "<input />",
                conf: {
                    id: "contact:type:",
                    name: "contact:type:",
                    maxlenght: 15,
                    type: "text"
                }
        }

        contactTemplate["elementDefinitions"]["markType"]= {
                element: "<img />",
                conf: {
                    src: document.contact.imageMark,
                    class: "image-mark",
                    alt: "Field supports mark",
                    title: "Field supports mark"
                }
        }

        contactTemplate["elementDefinitions"]["requiredType"]= {
                element: "<img />",
                conf: {
                    src: document.contact.imageRequired,
                    class: "image-mark separator",
                    alt: "Required field",
                    title: "Required field"
                }
        }

        contactTemplate["elementDefinitions"]["labelValue"]= {
                element: "<label />",
                conf: {
                    for: "contact:value:",
                    text: "Value:"
                }
        }

        contactTemplate["elementDefinitions"]["inputValue"]= {
                element: "<input />",
                conf: {
                    id: "contact:value:",
                    class: "contact-value",
                    name: "contact:value:",
                    type: "text"
                }
        }

        contactTemplate["elementDefinitions"]["requiredValue"]= {
                element: "<img />",
                conf: {
                    src: document.contact.imageRequired,
                    class: "image-mark separator",
                    alt: "Required field",
                    title: "Required field"
                }
        }

        contactTemplate["elementDefinitions"]["inputPrimary"]= {
                element: "<input />",
                conf: {
                    id: "contact:primary:",
                    name: "contact:primary:",
                    type: "checkbox"
                }
        }

        contactTemplate["elementDefinitions"]["labelPrimary"]= {
                element: "<label />",
                conf: {
                    for: "contact:primary:",
                    class: "gray-check-red separator",
                    text: "primary"
                }
        }

        contactTemplate["removeContactA"]= {
                element: "<a />",
                conf: {
                    href: "#",
                    class: "image-mark"
                }
        }

        contactTemplate["removeContactImage"]= {
                element: "<img />",
                conf: {
                    src: document.contact.imageDelete,
                    alt: "Remove contact",
                    title: "Remove contact"
                }
        }

        return contactTemplate;
    }

});