/* 
 * drag 1.0
 * create by tony@jentian.com
 * date 2015-08-18
 * 拖动滑块
 */
(function($){
    $.fn.drag = function(options){
        var x, drag = this, isMove = false, defaults = {
        };
        var options = $.extend(defaults, options);
        //添加背景，文字，滑块
        var html = '<div class="drag_bg"></div>'+
                    '<div class="drag_text" onselectstart="return false;" unselectable="on">请按住滑块，拖动到最右边</div>'+
                    '<div class="handler handler_bg"></div>';
        this.append(html);
        
        var handler = drag.find('.handler');
        var drag_bg = drag.find('.drag_bg');
        var text = drag.find('.drag_text');
       // var maxWidth = drag.width() - handler.width();  //能滑动的最大间距
        //console.log(handler.width())
        //console.log(drag.width())
       // console.log(maxWidth)
        //鼠标按下时候的x轴的位置
        handler.mousedown(function(e){
            isMove = true;
            x = e.pageX - parseInt(handler.css('left'), 10);
        });
        
        //鼠标指针在上下文移动时，移动距离大于0小于最大间距，滑块x轴位置等于鼠标移动距离
        $(document).mousemove(function(e){
        	var maxWidth = drag.width() - handler.width();  //能滑动的最大间距
            var _x = e.pageX - x;
            if(isMove){
                if(_x > 0 && _x <= maxWidth){
                    handler.css({'left': _x});
                    drag_bg.css({'width': _x});
                }else if(_x > maxWidth){  //鼠标指针移动距离达到最大时清空事件
                	handler.css({'left': maxWidth});
                    drag_bg.css({'width': maxWidth});
                    dragOk();
                }
            }
        }).mouseup(function(e){
            isMove = false;
            maxWidth = drag.width() - handler.width();  //能滑动的最大间距
            var _x = e.pageX - x;
            if(_x < maxWidth){ //鼠标松开时，如果没有达到最大距离位置，滑块就返回初始位置
                handler.css({'left': 0});
                drag_bg.css({'width': 0});
            }
        });
        
        //清空事件
        function dragOk(){
            handler.removeClass('handler_bg').addClass('handler_ok_bg');
            text.text('验证通过');
            $(".drag_text").css("padding-left","22px")
            drag.css({'color': '#fff'});
            handler.unbind('mousedown');
            $(document).unbind('mousemove');
            $(document).unbind('mouseup');
            $("#dragInput").prop("checked","checked");
            if( $("#dragInput").prop("checked")==true){
            	$("#dragInput-error").hide()
            }
        }
    };
})(jQuery);
/* 
 输入框点击向上
 */
(function($){
	var labelUp = function(){
		$(this).parent().prev().animate({"top":"5px","font-size":"12px"},300).addClass("awsomeColor").delay(300).addClass("inputPseudo");
		$(this).parent().prev().addClass("awsomeColor");
		$(this).addClass("awsomeColor");
		$(this).parent().parent().find(".iconfont-mobile:before").css("font-size","15px");
		var xx = $(this).parent().parent().find(".iconfont-mobile:before")
	}
	var labelBlur = function(){
		var _this = $(this);
		if(_this.val().length===0){
			_this.parent().prev().animate({"top":"23px","font-size":"16px"},300).removeClass("awsomeColor").delay(300).removeClass("inputPseudo")
			_this.removeClass("awsomeColor");
			_this.parent().prev().removeClass("awsomeColor");
		}
	}
	$(".newRegInput").on("focus",labelUp);
	$(".newRegInput").on("blur",labelBlur);
	//自动填充密码时label向上
	
	//点击推荐人显示隐藏
	var $referree = $(".newRegReferree span");
	var referreeShowHide = function(){
		$(this).parent().next().slideDown(300);
	}
	$referree.on("click",referreeShowHide)
})(jQuery);
//获取验证码
var clock = '';
var nums = 10;
var btn;
function sendCode()
{	
	var btn = this;
	btn.disabled = true; //将按钮置为不可点击
	btn.value = nums+'秒后可重新获取';
	btn.style.color = "#d6d6d6";
	btn.style.borderColor = "#d6d6d6";
	btn.style.cursor = "not-allowed";
	clock = setInterval(doLoop, 1000); //一秒执行一次
}
window.onload=function(){
	setTimeout(function(){
		var $newRegInput = $(".newRegInput");
		for(var i=0;i<$newRegInput.length;i++){
			if($newRegInput.eq(i).val().length>0){
				$newRegInput.eq(i).parent().prev().animate({"top":"5px","font-size":"12px"},300).addClass("awsomeColor").delay(300).addClass("inputPseudo");
				$newRegInput.eq(i).parent().prev().addClass("awsomeColor");
				$newRegInput.eq(i).parent().addClass("awsomeColor");
				$newRegInput.eq(i).parent().parent().find(".iconfont-mobile:before").css("font-size","15px")
			}
		}
	},0)
}