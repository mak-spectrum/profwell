jQuery(document).ready(function() {
    
    window.messageRead = function(a, id) {

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
                    jQuery(a)
                            .parent()
                            .parent()
                            .remove();
                }
        );
    }
}
);