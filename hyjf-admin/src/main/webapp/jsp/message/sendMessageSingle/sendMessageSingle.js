var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction"
},
/* 事件动作处理 */
Events = {

	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	},
	
	// 初始化画面事件处理
	initEvents : function() {
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct)
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		Page.validate2=$("#sendMessageForm").Validform({
			ajaxPost:true,
			beforeSubmit:function(curform){
				if($.trim($("#message").val())==""){
					Page.alert("", "发送的消息内容不可为空");
					return false;
				}
				//如果user_phones有内容,则取user_phones长度来判断
				//否则用user_number的长度来判断
				var size=0;
				if(!$("#user_phones").val()==""){
					size=$("#user_phones").val().split(",").length;
				}
				if(size>1){
					Page.alert("", "只能发送一条短信");
					return false;
				}
				return true;
				//在验证成功后，表单提交前执行的函数，curform参数是当前表单对象。
				//这里明确return false的话表单将不会提交;	
			},
			callback:function(data){
				alert( data.msg);
				window.location.reload();
				//返回数据data是json格式，{"info":"demo info","status":"y"}
				//info: 输出提示信息;
				//status: 返回提交数据的状态,是否提交成功。如可以用"y"表示提交成功，"n"表示提交失败，在ajax_post.php文件返回数据里自定字符，主要用在callback函数里根据该值执行相应的回调操作;
				//你也可以在ajax_post.php文件返回更多信息在这里获取，进行相应操作；
				//ajax遇到服务端错误时也会执行回调，这时的data是{ status:**, statusText:**, readyState:**, responseText:** }；
				
				//这里执行回调操作;
				//注意：如果不是ajax方式提交表单，传入callback，这时data参数是当前表单对象，回调函数会在表单验证全部通过后执行，然后判断是否提交表单，如果callback里明确return false，则表单不会提交，如果return true或没有return，则会提交表单。
			}
		});
	},
}),

Page.initialize();