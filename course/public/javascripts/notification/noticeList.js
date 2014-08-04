jQuery(document).ready(function() {
    
    window.readMessage = function(block, id) {

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
                    jQuery("img", block).removeClass("message-unread").addClass("message-read");
                }
        );
    }
    
    jQuery(".mark-all-as-read").on("click", function(){
        jQuery.ajax(
                {
                    url: "/noticeMarkAllAsRead"
                }
        ).done(
                function(responseMsg) {
                    jQuery("span[id^='messageText']").removeClass("notice-text-style");
                    jQuery("img.message-unread").removeClass("message-unread").addClass("message-read");
                }
        );
    });
});