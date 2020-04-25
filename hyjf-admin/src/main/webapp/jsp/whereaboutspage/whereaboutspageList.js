var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateInfoAction : "updateInfoAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 启用Action
	statusAction : "statusAction",
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 刷新按钮的单击动作事件
	infoClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 优惠券管理",
			width: "50%", height: "75%",
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
	// 删除按钮的单击动作事件
	deleteClkAct : function(selection, cds) {
		// 取得选择行
		Page.primaryKey.val($(this).data("id"));
		if (!Page.primaryKey.val()) {
			Page.alert("请选择要删除的数据！", "");
		} else {
			Page.confirm("", "确定要删除此信息吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit(Action.deleteAction);
				}
			});
		}// Endif
	},
	//启用action
	statusAction : function(event) {
		alert("status");
			if(event) {
				console.info($(this).data());
				Page.primaryKey.val($(this).data("id"))
			}
		Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.statusAction);
		})
	},

	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 修改按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("id"));
		Page.submit(Action.updateInfoAction);
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
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
		$('#start-date-time').on("change", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("change", function(evnet, d) {
			d = $('#start-date-time').datepicker("getDate"),
			d && $('#end-date-time').datepicker("setStartDate", d)
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		
	}
});
