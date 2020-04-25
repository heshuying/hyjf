var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	exportExcelAction : "exportReturncashExcelAction",
	// 查询的Action
	searchAction : "searchReturncashAction",
	// 详细画面的Action
	returncashAction : "returncashAction",
},
/* 事件动作处理 */
Events = {
	// 返手续费按钮的单击事件
	returncashClkAct : function(event) {
		var rewardTotal = 0;
		var idtemp;
		if(event) {
			Page.primaryKey.val($(this).data("id"));
			idtemp = $(this).data("id");
			rewardTotal = $(this).attr("data-rewardTotal")
		}
		Page.confirm("", "确定要执行本次返手续费操作吗？", function(isConfirm) {
			if (isConfirm) {
				setTimeout("Events.submitFunction('" + idtemp +"','" + rewardTotal + "')", 300);
			};

		})
	},
	
	submitFunction : function(ids, rewardTotalStr) {
		var param = {};
		param.ids = ids;
		param.rewardTotalStr = rewardTotalStr;
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
					Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
				}
			},
			error : function(err) {
				Page.coverLayer();
				Page.notice("返手续费发生错误,请重新操作!", "","error");
			}
		});
	
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#mainForm").attr("target", "");
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout: function() {
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 返手续费按钮的单击事件
		$(".fn-Returncash").click(Events.returncashClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 清空按钮的单击事件绑定
		$(".fn-ClearForm").click(Events.clearClkAct)
	},
	// 画面初始化
	initialize : function() {
	}
});
