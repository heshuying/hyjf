setTimeout(function(){
	var banner = new Swiper('.landing-banner', {
        autoplay: 5000,
           paginationClickable: true,
           loop:"true"
    });
    
    $('.prev').on('click', function(e){
        e.preventDefault();
        banner.swipePrev();
    })
    $('.next').on('click', function(e){
        e.preventDefault();
        banner.swipeNext();
    })
    $(".landing-banner").mouseenter(function() {
        var ctrl = $(".landing-banner .prev,.landing-banner .next");
        ctrl.stop().fadeIn(500);
    }).mouseleave(function() {
        var ctrl = $(".landing-banner .prev,.landing-banner .next");
        ctrl.stop().fadeOut(500);
    })
},300)