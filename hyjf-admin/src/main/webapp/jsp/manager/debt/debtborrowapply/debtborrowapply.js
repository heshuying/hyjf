var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 审核的Action
	infoAction : "infoAction",
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
		
		$("#nameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");

	},
	// 已交保证金按钮的单击动作事件
	updateClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("id"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 审核",
			width: "50%", height: "60%",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 导出按钮的单击动作事件
	exportClkAct: function(event) {
		Page.submit(Action.exportAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
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
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 审核按钮的单击事件绑定
		$(".fn-Modify").click(Events.updateClkAct);
	}
});
