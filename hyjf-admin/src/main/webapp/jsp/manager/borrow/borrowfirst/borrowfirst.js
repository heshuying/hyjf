var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 已交保证金的Action
	bailAction : "bailAction",
	// 已交保证金的Action
	bailInfoAction : "bailInfoAction",
	// 初审的Action
	infoAction : webRoot + "/manager/borrow/borrowcommon/infoAction",
	// 详细画面的Action
	infoConsumeAction : webRoot + "/manager/borrow/consume/infoAction",
	// 发标的Action
	fireAction : "fireAction"
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
	// 已交保证金按钮的单击动作事件
	bailClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"))
		}

		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 已交保证金",
			width: "30%", height: "410",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.bailInfoAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	firstClkAct : function(event) {
		Page.primaryKey.val($(this).data("borrownid"))
		$("#pageUrl").val($(this).data("pageurl"));
		if($(this).data("projecttype") == "8") {
			Page.submit(Action.infoConsumeAction);
		} else {
			Page.submit(Action.infoAction);
		}
	},
	fireClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 发标",
			width: "30%", height: "480",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.fireAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
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
		//$("#pageUrl").val(window.location.href);
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 已交保证金按钮的单击事件绑定
		$(".fn-Bail").click(Events.bailClkAct),
		// 初审按钮的单击事件绑定
		$(".fn-first").click(Events.firstClkAct),
		// 发标按钮的单击事件绑定
		$(".fn-fire").click(Events.fireClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});
