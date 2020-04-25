$(function(){
	$('.chart-tab a').each(function(i){
		$(this).click(function(){
			$('.chart-tab .select').removeClass('select');
			$(this).addClass('select')
			$('.chart-box .chart-box-child').hide();
			$('.chart-box .chart-box-child:eq('+i+')').show()	
		})
	})
})
