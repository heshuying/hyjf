var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 跳转到详情页面
	toRecoverAction : "toRecoverAction",
	// 重新放款
	restartRepayAction:"restartRepayAction",
	// 重新还款（单笔）
	initRepayAction:"initRepayAction",
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 跳转到详情页面
	toRecoverClkAct : function(event) {
		if(event) {
			$("#borrowNidHidden").val($(this).data("id"));
			$("#periodNowHidden").val($(this).data("period"));
			$("#monthType").val($(this).data("monthtype"));
		}
		Page.submit(Action.toRecoverAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
	// 重新放款按钮的单击事件绑定
	restartRepayAction : function(event) {
		if(event) {
			$("#borrowNidHidden").val($(this).data("id"));
			$("#periodNowHidden").val($(this).data("period"));
		}

		Page.confirm("确认重新还款", "操作不可逆转，是否确定重新还款？",  function(isConfirm) {
			if (isConfirm) {
				Page.submit(Action.restartRepayAction);
			}
		});
	},
	addClkAct:function(event){
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fn-fix'></i>重新还款",
			width : 650,
			height : 450,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.initRepayAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 详情
		$(".fn-toRecover").click(Events.toRecoverClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 重新放款按钮的单击事件绑定
		$(".fn-restartRepay").click(Events.restartRepayAction);
		// 单笔重新还款
		$(".fn-Add").click(Events.addClkAct);
	}
});
