var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "zhiChiInit",
	// 启用Action
	moveAction : "zhiChiMoveAction",
	// 改变二级菜单呢绒
	changeSubTypeAction : "changeSubTypeAction"
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
	moveClkAct : function(event) {
		if (event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行本次移出操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.moveAction);
		})
	},
	// 编辑按钮的单击动作事件
	changeSubType : function(event) {
		if ($("#pcateId").val() != "") {
			$.post(Action.changeSubTypeAction, {
				"pid" : $("#pcateId").val()
			}, function(data) {
				if (data.success == false) {
					Page.alert("", data.msg);
				} else {
					$("#cateId").empty();
					$("#cateId").append("<option value=\"\">--全部--</option>");
					$.each(data.childCategorys, function(i, val) {
						$("#cateId").append(
								"<option value=\"" + val.id + "\">" + val.title
										+ "</option>");
					});
				}
			});
		} else {
			$("#cateId").empty();
			$("#cateId").append("<option value=\"\">--全部--</option>");
		}
	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	pkIds : $("#id"),

	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "请选择条件",
			allowClear : true,
			language : "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 全选
		$("#checkall").change(Events.selectAllAct),
		// 启用按钮的单击事件绑定
		$(".fn-Move").click(Events.moveClkAct),
		// 问题分类改变时,联动改变子分类
		$("#pcateId").change(Events.changeSubType)
	}
});
