jQuery(document).ready(function() {

    window.vacancyClose = function(vacancyId, passWithoutCheck) {
        if (passWithoutCheck || confirm("Are you sure, you want to close the vacancy? This action can't be undone.")) {
            window.location = "/vacancyClose?id=" + vacancyId;
        }
    }

});