jQuery(document).ready(function() {
    
    window.messageRead = function(id) {

        var requestData = {
                noticeId: id
        };

        jQuery.ajax(
                {
                    url: "/noticeReadAsync",
                    data: requestData
                }
        ).done(
                function(responseMsg) {
                    jQuery("span#messageText" + id).removeClass("notice-text-style");
                }
        );
    }
});
