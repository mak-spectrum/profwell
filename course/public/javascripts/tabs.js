jQuery(document).ready(function() {

    /* TABS : START */
    var tabContents = jQuery(".tab-content").hide();
    var tabs = jQuery("ul.tabs li");

    var activeTab = jQuery(".tab-content.active");
    if (activeTab.length > 0) {
        activeTab.show();
    } else {
        tabs.first().addClass("active").show();
        tabContents.first().addClass("active").show();
    }

    tabs.click(function() {
        var self = jQuery(this); 
        var activeTab = self.find("a").attr("href");
        
        if (!self.hasClass("active")) {
            self.addClass("active").siblings().removeClass("active");
            tabContents.hide().filter(activeTab).addClass("active").fadeIn().siblings().removeClass("active");
        }
        return false;
    });
    /* TABS : END */
    
});