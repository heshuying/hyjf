	$(".fxcp-list-item").on("click", "a", function() {
		var _self = $(this);
		var checked =_self.parent().siblings().children(".checked");
		//var par = _self.parent("p");
		if(checked.length){
			//has checked ,remove checked
			checked.children("input[type=radio]").prop("checked",false);
			checked.removeClass("checked");
		}
		_self.children("input[type=radio]").prop("checked",true);
		_self.addClass("checked");
		var ipt = _self.children("input[type=radio]");
		$("#consultForm").validate().element(ipt);
		var behavior=ipt.val();
		
	});
	var validator = $("#consultForm").validate({
        submitHandler: function(form) {   //表单提交句柄,为一回调函数，带一个参数：form
            /* form.submit();   提交表单 */
        	var userAnswer="";
        	var i=0
        	$(".fxcp-list").find("li.checkedStyle").each(function(idx,ele){
            	var checked=$(ele).find(".checked");
            	if(checked.length==1){
            		if(i!=0){
                		userAnswer+=",";
                	}
            		userAnswer+=checked.find("input").val();
            		i++
            	}
            });
            $("#userAnswer").val(userAnswer);
            $("#submitForm").submit();
        },
        rules: {           //定义验证规则,其中属性名为表单的name属性
			"ask1":"required",
			"ask2":"required",
			"ask3":"required",
			"ask4":"required",
			"ask5":"required",
			"ask6":"required",
			"ask7":"required",
			"ask8":"required",
			"ask9":"required",
			"ask10":"required",
			"ask11":"required",
			"ask12":"required"
        },
        messages: {       //自定义验证消息
			"ask1":"未回答本题",
			"ask2":"未回答本题",
			"ask3":"未回答本题",
			"ask4":"未回答本题",
			"ask5":"未回答本题",
			"ask6":"未回答本题",
			"ask7":"未回答本题",
			"ask8":"未回答本题",
			"ask9":"未回答本题",
			"ask10":"未回答本题",
			"ask11":"未回答本题",
			"ask12":"未回答本题"
        },
        ignore: ".ignore",
        errorPlacement: function(error, element) {  //验证消息放置的地方
        	error.appendTo(element.parents('.fxcp-list-item').siblings('.fxcp-list-title'));
        }
	});
	$(".submit").click(function(){
		if($("#consultForm").valid() == false){
			utils.scrollTo($(".error:visible").eq(0));
		}else{
			$("#consultForm").submit();
		}
		
	})

