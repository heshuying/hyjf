var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "getActivityInfoAction",
	//返现的Action
	returncashAction : "returncashAction",
	
	// 详细画面的Action
	//infoAction : "infoAction",
	// 详细画面的Action
	//updateAction : "updateAction",
	// 删除的Action
	//deleteAction : "deleteAction",
},
/* 事件动作处理 */
Events = {
		// 返手续费按钮的单击事件
		returncashClkAct : function(event) {
			Page.primaryKey.val($(this).data("userid"))
			Page.primary2Key.val($(this).data("returnamount"))
			Page.confirm("", "确定要执行本次返现操作吗？", function(isConfirm) {
				if (isConfirm) {
					var param = {};
					param.userId = Page.primaryKey.val();
					param.returnAmount = Page.primary2Key.val();
					Page.coverLayer("正在处理,请稍候...");
					$.ajax({
						url : Action.returncashAction,
						type : "POST",
						async : true,
						data : JSON.stringify(param),
						dataType: "json",
						contentType : "application/json",
						success : function(data) {
							Page.coverLayer();
							if (data.status == "success") {
							    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
							} else {
								Page.coverLayer();
								Page.notice(data.result, "","error");
							}
						},
						error : function(err) {
							Page.coverLayer();
							Page.notice("返手续费发生错误,请重新操作!", "","error");
						}
					});
				};

			})
		},
		
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#mainForm").attr("target", "");
		Page.submit(Action.searchAction);
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
		
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
	primary2Key : $("#returnAmount"),
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		})

	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		//返现按钮的单机事件绑定
		$(".fn-Returncash").click(Events.returncashClkAct);
	}
	
});
