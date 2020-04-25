var str;


var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
		// 导出的Action
	drowAction : "hongbaoAction",
				// 导出的Action
	pingguoAction :"pingguoAction"
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
		
		$("#referrerNameSrch").val("");
		$("#usernameSrch").val("");

	},
	// 导出按钮的单击动作事件
	exportClkAct: function(event) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	hongbaoClkAct : function(event) {
		Page.confirm("", "确定要执行本次抽奖么?请不要重复点击!", function(isConfirm) {
			//isConfirm && Page.submit(Action.drowAction);
			if(isConfirm){
				$.post(Action.drowAction, $("#mainForm").serialize(), function(
				data) {
					alert( "成功抽奖"+data.str+"个战队");
				});
			}
//			$.post(Action.drowAction, $("#mainForm").serialize(), function(
//					data) {
//				alert( "成功抽奖"+data.str+"个战队");
//			});
			
		});
		
	},
	pingguoClkAct : function(event) {
		Page.confirm("", "确定要执行本次抽奖么?请不要重复点击!", function(isConfirm) {
			if(isConfirm){
				$.post(Action.pingguoAction, $("#mainForm").serialize(), function(
						data) {
					alert( "成功抽奖"+data.str+"个战队");
					
				});
			}

			
		});
		
	},
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
		}),
		// 日历范围限制
		$('#start-date-time').on("show", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("show", function(evnet, d) {
			d = $('#start-date-time').datepicker("getDate"),
			d && $('#end-date-time').datepicker("setStartDate", d)
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-hongbao").click(Events.hongbaoClkAct);
		$(".fn-pingguo").click(Events.pingguoClkAct);
		
	}
});
