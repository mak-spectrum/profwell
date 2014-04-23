jQuery(document).ready(function() {

    /* CALENDAR:START */
    var calendar = jQuery("#calendar");
    calendar.datepicker(
            {
                showOtherMonths: true,
                showWeek: true,
                onSelect: function() {
                    calendar.tooltip("open");

                    window.setTimeout(
                            function () {
                                calendar.tooltip("close");
                                calendar.tooltip("disable");
                            },
                            3000);
                }
            }
    );

    calendar.tooltip(
            {
                disabled: true,
                content: "Sorry, this functionality has not been implemented yet."
            }
    );
    /* CALENDAR:END */

    jQuery(".expand-hookups").click(
            function(event) {
                var self = jQuery(this);
                if (self.hasClass("more-icon")) {

                    jQuery("div.hookups:not(.collapsed)").each(function (index, element) {
                        var elementToHide = jQuery(element);
                        var button = elementToHide.children(".expand-hookups");
                        var options = {"height": "7em"};
                        elementToHide.animate(options, 500, function() {
                            elementToHide.addClass("collapsed");
                        });
                        button.addClass("more-icon");
                        button.removeClass("less-icon");
                    });

                    var totalHeight = 8;
                    self.parent().children().each(function(index, element) {
                        var jElem = jQuery(element);
                        if (jElem.css("position") != "absolute") {
                            totalHeight = totalHeight + jQuery(element).outerHeight();
                        }
                    });

                    var visibleHeight = self.parent().height();
                    self.parent().css("height", visibleHeight + "px")

                    self.parent().removeClass("collapsed");

                    var options = {height: totalHeight + "px"};
                    self.parent().animate(options, 500);

                    self.removeClass("more-icon");
                    self.addClass("less-icon");

                } else {
                    var options = {"height": "7em"};
                    self.parent().animate(options, 500, function() {
                        self.parent().addClass("collapsed");
                    });
                    self.addClass("more-icon");
                    self.removeClass("less-icon");
                }
                event.preventDefault();
                return false;
            }
    );
});