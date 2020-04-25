var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	
	//执行后台发提成操作
	switchAction : "switchAction",
	//修改以后页面
	jupmURLAction : webRoot +"/manager/hjhplan/planlist/init"
},
/* 事件动作处理 */
Events = {			
		// 重发按钮的单击动作事件
		confirmPushMoneyClkAct : function(event) {
					var param = {};
					param.debtPlanNid = $(this).data("debtplannid");
            		param.enableOrDisplayFlag = $(this).data("enableordisplayflag");

					$.ajax({
						url : Action.switchAction,
						type : "post",
						data : {
							debtPlanNid : param.debtPlanNid,
                            enableOrDisplayFlag : param.enableOrDisplayFlag
						},
						async: false,
						dataType : "json",
						success : function(data) {
							
							parent.$.colorbox.close();
							parent.Events.refreshClkAct();
							
						},
						error : function(err) {
							parent.$.colorbox.close();
							parent.Events.refreshClkAct();
						}
					});
					
//					parent.$.colorbox.close();
//					Page.submit(Action.jupmURLAction);
				},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 初始化画面事件处理
	initEvents : function() {
		// 修改状态按钮的单击事件
		$(".fn-Confirm").click(Events.confirmPushMoneyClkAct),
		// 导出Excel的单击事件绑定
		$(".fn-ExportAgreement").click(Events.exportAgreementClkAct);
	}
});