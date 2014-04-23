jQuery(document).ready(function() {

    var skillsInplace = jQuery("#requiredSkillsInplace");
    var skillsHref = jQuery("#requiredSkillsHref");

    var skillsBlock = jQuery("div.skills-list");
    var mandatorySkillsList = jQuery("#mandatorySkills");
    var optionalSkillsList = jQuery("#optionalSkills");

    var wscrolltop;

    skillsInplace.hide();
    skillsInplace.blur(function() {
        consumeCurrentSkill();
        skillsHref.show();
        skillsInplace.hide();
    });
    skillsInplace.keypress(function(event) {
        if (event.which == 13) {
            consumeCurrentSkill();
            event.preventDefault();
        }
    });
    skillsInplace.autocomplete({
        source: "/autocompleteSkill",
        minLength: 1,
        delay: 500,
        select: function(event, widget) {
            skillsInplace.val(widget.item.value);
            consumeCurrentSkill();
            return false;
        }
    });

    function activateSkillInplace(event) {
        skillsHref.hide();
        skillsInplace.show();
        skillsInplace.focus();
        event.preventDefault();
    }

    skillsHref.click(activateSkillInplace);
    skillsHref.keypress(function(event) {
        if (event.which == 13) {
            activateSkillInplace(event);
        }
    });

    skillsInplace.tooltip(
            {
                disabled: true,
                content: "This value have been already specified, as a required skill."
            }
    );

    function consumeCurrentSkill() {
        var currentSkillValue = skillsInplace.val();
        if (!currentSkillValue || currentSkillValue == "") {
            return;
        }

        var unique = true;
        jQuery("ul.connectedSortable > li.skill-item > span").each(
                function(index, element) {
                    if (jQuery(element).text().toLowerCase() ==
                            currentSkillValue.toLowerCase()) {
                        unique = false;
                    }
                }
        );

        if (!unique) {
            skillsInplace.tooltip("open");

            window.setTimeout(
                    function () {
                        skillsInplace.tooltip("close");
                        skillsInplace.tooltip("disable");
                    },
                    3000);
            return;
        }

        var newLi = jQuery("<li/>",
                {
                    class: "skill-item"
                });

        var newSpan = jQuery("<span/>",
                {
                    text: currentSkillValue
                });

        newLi.append(newSpan);

        var editA = jQuery("<a/>",
                {
                    class: "list-item-control left",
                    href: "#"
                }
        ).click(function(event) {
            var innerValue = newSpan.text();
            newLi.remove();
            skillsHref.hide();
            skillsInplace.show();
            skillsInplace.val(innerValue);
            skillsInplace.focus();
            event.preventDefault();
        });
        editA.append(jQuery("<img/>",
                {
                    class: "image-mark",
                    src: document.skill.imageEditIcon,
                    alt: "Edit Record",
                    title: "Edit Record"
                }));
        newLi.append(editA);

        var deleteA = jQuery("<a/>",
                {
                    class: "list-item-control right",
                    href: "#"
                }
        ).click(function(event) {
            newLi.remove();
            event.preventDefault();
        });
        deleteA.append(jQuery("<img/>",
                {
                    class: "image-mark",
                    src: document.skill.imageDeleteIcon,
                    alt: "Delete Record",
                    title: "Delete Record"
                }));
        newLi.append(deleteA);

        newLi.append(jQuery("<input />",
                {
                    type: "hidden",
                    name: "skill:m",
                    value: currentSkillValue
                }));

        mandatorySkillsList.append(newLi);
        skillsInplace.val("");

        if (skillsBlock.is(":hidden")) {
            skillsBlock.show("blind", {}, 200);
        }
    }

    jQuery("#mandatorySkills, #optionalSkills").sortable(
            {
                connectWith: ".connectedSortable",
                placeholder: "skill-item-placeholder",
                items: "li.skill-item",
                "start": function (event, ui) {
                    //jQuery Ui-Sortable Overlay Offset Fix
                    wscrolltop = $(window).scrollTop();
                },
                "change": function (event, ui) {
                    //jQuery Ui-Sortable Overlay Offset Fix
                    if (!ui.item.parent().attr("enableMyScrollingFix")) {
                        ui.item.parent().attr("enableMyScrollingFix", true);
                    }
                    ui.helper.css({ "top": ui.position.top + wscrolltop + "px" });
                },
                "sort": function (event, ui) {
                    //jQuery Ui-Sortable Overlay Offset Fix
                    if (ui.item.parent().attr("enableMyScrollingFix")) {
                        ui.helper.css({ "top": ui.position.top + wscrolltop + "px" });
                    }
                },
                "receive": function (event, ui) {
                    mandatorySkillsList.children("li.skill-item").children("input").attr("name", "skill:m")
                    optionalSkillsList.children("li.skill-item").children("input").attr("name", "skill:o")
                },
            }
    ).disableSelection();

    jQuery("a.list-item-control.left")
    .click(function(event) {
        var self = jQuery(this);
        var innerValue = self.children("span").text();
        self.parent().remove();
        skillsHref.hide();
        skillsInplace.show();
        skillsInplace.val(innerValue);
        skillsInplace.focus();
        event.preventDefault();
    });

    jQuery("a.list-item-control.right")
    .click(function(event) {
        jQuery(this).parent().remove();
        event.preventDefault();
    });

    if (!mandatorySkillsList.children("li.skill-item").length > 0
            && !optionalSkillsList.children("li.skill-item").length > 0) {
        skillsBlock.hide();
    }

    jQuery("a.list-item-control.right")
    .click(function(event) {
        jQuery(this).parent().remove();
        event.preventDefault();
    });

});