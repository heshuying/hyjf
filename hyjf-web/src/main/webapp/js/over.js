 $(document).ready(function(){
	    	var handIn = function(){
	    		var _this = $(this);
	    		_this.addClass("hover");
	    		_this.find(".overTitle").animate({"font-size":"36px"},500);//animate({top:"145px"},300);
	    		_this.find(".overlay").css("background","rgba(153, 108, 51, 0.7)");
	    	}
	    	var handOut = function(){
	    		var _this = $(this);
	    		_this.removeClass("hover");
	    		_this.find(".overTitle").animate({"font-size":"28px"},500);//animate({top:"12px"},500)
	    		_this.find(".overlay").css("background","rgba(0, 0, 0, 0.24)");
	    	}
	    	//点击展开handler
	    	var clickOpen = function(){
	    		var _this = $(this);
	    		_this.hide()
	    		_this.prev().css("height","auto");
	    	}
			$(".clearfix>div").hover(handIn,handOut);
			$(".hjjOpen").on("click",clickOpen);
	    });