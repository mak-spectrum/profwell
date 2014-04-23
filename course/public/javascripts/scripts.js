$(document).ready(function() {

// Smooth scrolling - css-tricks.com
    function filterPath(string){return string.replace(/^\//,'').replace(/(index|default).[a-zA-Z]{3,4}$/,'').replace(/\/$/,'');}var locationPath=filterPath(location.pathname);var scrollElem=scrollableElement('html','body');$('a[href*=#nav]').each(function(){var thisPath=filterPath(this.pathname)||locationPath;if(locationPath==thisPath&&(location.hostname==this.hostname||!this.hostname)&&this.hash.replace(/#/,'')){var $target=$(this.hash),target=this.hash;if(target){var targetOffset=$target.offset().top;$(this).click(function(event){event.preventDefault();$(scrollElem).animate({scrollTop:targetOffset},'slow',function(){location.hash=target;});});}}});function scrollableElement(els){for(var i=0,argLength=arguments.length;i<argLength;i++){var el=arguments[i],$scrollElement=$(el);if($scrollElement.scrollTop()>0){return el;}else{$scrollElement.scrollTop(1);var isScrollable=$scrollElement.scrollTop()>0;$scrollElement.scrollTop(0);if(isScrollable){return el;}}}return[];}

// TOGGLES
    $('.toggle-title').click(function () {
        var text = $(this).parent().children('.toggle');

        if (text.is(':hidden')) {
            text.slideDown('fast');
            $(this).parent().children('.toggle-title').addClass('tactive');
        } else {
            text.slideUp('fast');
            $(this).parent().children('.toggle-title').removeClass('tactive');
        }
    });

    $('#deleteButton').click(function (event) {
        var confirmResult = confirm("Are you sure you want to delete this object? This action can't be undone.");
        /* 
         * NOTE: don't remove 'confirmResult == true';
         * it is required because of strange browsers behavior
         */
        if (!confirmResult) {
            event.stopPropagation();
            event.preventDefault();
            return false;
        }
    });

    jQuery("div.tooltip-help").tooltip();
});