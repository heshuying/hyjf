var Action = {
	bankCheckAction : "startAccountCheckAction",
	// 查询的Action
	searchAction : "accountmanage_list"
};

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			$("#mainForm").attr("target", "");
			Page.submit(Action.searchAction);
		},
		// 确认按键单击事件绑定
		confirmClkAct : function() {
			var startTime = getTimeByDateStr(Page.startTimeKey.val());
			var endTime = getTimeByDateStr(Page.endTimeKey.val());
			var nowTime = getTimeByDateStr(null);
			if (Page.startTimeKey.val()==""||Page.endTimeKey.val()=="") {
				Page.notice("请选择开始时间和结束时间!", "","error");
				return false;
			}
			if (startTime > endTime) {
				Page.notice("开始时间必须小于结束时间!", "","error");
				return false;
			}
			if (startTime > nowTime || endTime > nowTime) {
				Page.notice("开始时间或者结束时间不能大于当前时间!", "","error");
				return false;
			}
			Page.confirm("", "确定要执行本次对账操作吗？", function(isConfirm) {
				if (isConfirm) {
					var param = {};
					param.userId = Page.primaryKey.val();
					param.startTime = Page.startTimeKey.val();
					param.endTime = Page.endTimeKey.val();
					
					console.log(param);
					Page.coverLayer("正在对账,请稍候...");
					$.ajax({
						url : Action.bankCheckAction,
						type : "POST",
						async : true,
						data : JSON.stringify(param),
						dataType: "json",
						contentType : "application/json",
						success : function(data) {
							Page.coverLayer();
							Page.primaryKey.val("");
							if (data.status == "success") {
							    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.cancelClkAct()});
							} else {
								Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.cancelClkAct()});
								Page.notice(data.msg, "","error");
							}
						},
						error : function(err) {
							Page.coverLayer();
							Page.primaryKey.val("");
							Page.notice("对账发生错误,请重新操作!", "","error");
						}
					});
				};

			})
		
		},
		// 取消按键单击事件绑定
		cancelClkAct : function() {
			parent.$.colorbox.close();
		},
		//对账完成事件绑定
		checkEndClkAct : function(){
			Events.cancelClkAct();
			Events.refreshClkAct();
		}
		
};

function getTimeByDateStr(date){
	if (date==null) {
		return new Date().getTime();
	}
	var time = new Date(Date.parse(date.replace(/-/g,   "-"))).getTime();     
	return time;
}
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey : $("#userId"),
	startTimeKey : $("#statrTime"),
	endTimeKey : $("#endTime"),
	
	// 初始化画面事件处理
	initEvents : function() {
		//确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
//	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	}
});

Page.initialize();
