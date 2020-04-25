var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 标的撤销Action
	revokeAction : "revokeAction",
	// 导出Aciton
	exportExcelAction : "exportAction"
		
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
		
		$("#borrowNidSrch").val("");
		$("#borrowNameSrch").val("");
		$("#usernameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 删除按钮的单击动作事件
	deleteClkAct : function(selection, cds) {
		// 取得选择行
		Page.primaryKey.val($(this).data("id"))
		if (!Page.primaryKey.val()) {
			Page.alert("请选择要删除的数据！", "");
		} else {
			Page.confirm("", "该删除执行的是不可逆操作，确定要物理删除吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit(Action.deleteAction);
				}
			});
		}// Endif
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
	revokeClkAct : function(selection,cds){
		// 取得选择行
		Page.primaryKey.val($(this).data("id"))
		if(!Page.primaryKey.val()){
			Page.alert("请选择要撤销的标的!","");
		}else{
			Page.confirm("", "该标的的撤销执行的是不可逆操作，确定要撤销标的吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit(Action.revokeAction);
				}
			});		
		}
	},
	sortClkAct : function() {
		$("#col").val($(this).data("col"));
		$("#sort").val($(this).data("sort") == "asc" ? 'asc' : 'desc');
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#borrowNid"),
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
		$('#start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		
		// 日历选择器
		$('#recover-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#recover-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		
		
	    $("#recover-start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#recover-end-date-time').datepicker("setStartDate", selectedDate);
	        $('#recover-end-date-time').datepicker("setDate", selectedDate);
	    });
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 撤销按钮的单击事件绑定
		$(".fn-UpdateBy").click(Events.revokeClkAct),
		// 列表导出
		$(".fn-Export").click(Events.exportClkAct);
	}
});
