$(document).ready(function() {

    var TO_TOP = "TO_TOP";
    var PREV = "PREV";
    var GOTO = "GOTO";
    var NEXT = "NEXT";
    var TO_END = "TO_END";

    var actionElement = $("#filter:actionId");
    var form = $("#listForm");


    $("#search").click(function() {
        actionElement.val(GOTO);
        form.submit();
    });


    $("#toTop").click(function() {
        actionElement.val(TO_TOP);
        form.submit();
    });

    $("#prev").click(function() {
        actionElement.val(PREV);
        form.submit();
    });

    $("#goto").click(function() {
        actionElement.val(GOTO);
        form.submit();
    });

    $("#next").click(function() {
        actionElement.val(NEXT);
        form.submit();
    });

    $("#toEnd").click(function() {
        actionElement.val(TO_END);
        form.submit();
    });

    document.orderBy = function(orderBy) {
        var orderByValueElement = $("#filter:orderById");
        var ascValueElement = $("#filter:orderAscId");

        var orderByValue = orderByValueElement.val();
        var ascValue = ascValueElement.val();
        if (orderByValue == orderBy
                && ascValue == "true") {
            ascValueElement.val("false");
        } else {
            ascValueElement.val("true");
        }
        orderByValueElement.val(orderBy);

        form.submit();
    }

    var hideable = $("div.grid-table-entry.hideable");
    hideable.hide();

    $(".search-collapse").click(function() {
        if ($(".search-collapse").hasClass("more-icon")) {
            hideable.show('blind', {}, 200);
            $(".search-collapse").removeClass("more-icon");
            $(".search-collapse").addClass("less-icon");
        } else {
            hideable.hide('blind', {}, 200);
            $(".search-collapse").addClass("more-icon");
            $(".search-collapse").removeClass("less-icon");
        }
    });
});