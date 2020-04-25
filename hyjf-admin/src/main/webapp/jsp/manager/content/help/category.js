var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {

	// 查询的Action
	searchAction : "init",
	// 详细画面的Action
	infoTypeAction : "infoTypeAction",
	// 详细画面的Action
	infoSubTypeAction : "infoSubTypeAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 关闭的Action
	closeAction : "closeAction",
	// 启用Action
	openAction : "openAction",
	// 删除前的验证Action
	beforeDelInfoAction : "beforeDelInfoAction",
	// 删除画面的Action
	delInfoAction : "delInfoAction",
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
	// 添加按钮的单击动作事件
	addTypeClkAct : function() {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 添加分类",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.pkIds.val(""), Page.form.attr("target", "dialogIfm")
							.attr("action", Action.infoTypeAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 添加按钮的单击动作事件
	addSubTypeClkAct : function() {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 添加分类",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.pkIds.val(""), Page.form.attr("target", "dialogIfm")
							.attr("action", Action.infoSubTypeAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 编辑按钮的单击动作事件
	modifyTypeClkAct : function(event) {
		if (event) {
			$("#id").append(
					"<option value=\"" + $(this).data("id") + "\"></option>");
			$("#id").val($(this).data("id"));
		}
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 修改分类",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action",
							Action.infoTypeAction).submit();
					$("#id option:last").remove();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 编辑按钮的单击动作事件
	modifySubTypeClkAct : function(event) {
		if (event) {
			$("#id").append("<option value=\"" + $(this).data("id") + "\"></option>");
			$("#id").val($(this).data("id"));
		}
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 修改分类",
			width : "50%",
			height : 430,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action",
							Action.infoSubTypeAction).submit();
					$("#id option:last").remove();
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	openClkAct : function(event) {
		if (event) {
	//		$("#id").append("<option value=\"" + $(this).data("id") + "\"></option>");
	//		$("#id").val($(this).data("id"));
			$("#ids").val($(this).data("id"));
		}
		Page.confirm("", "确定要执行本次启用操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.openAction);
	//		$("#id option:last").remove();
		})
	},
	closeClkAct : function(event) {
		if (event) {
			//$("#id").append("<option value=\"" + $(this).data("id") + "\"></option>");
			//$("#id").val($(this).data("id"));
			$("#ids").val($(this).data("id"));
		}
		Page.confirm("", "确定要执行本次关闭操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.closeAction);
		//	$("#id option:last").remove();
		})
	},
	// 删除按钮的单击动作事件
	delClkAct : function(event) {
		if (event) {
			$("#id").append(
					"<option value=\"" + $(this).data("id") + "\"></option>");
			$("#id").val($(this).data("id"));
		}
		// 在删除之前先进行验证
		$.post(Action.beforeDelInfoAction, {
			"id" : $("#id").val()}, function(data) {
			if (data.success == true) {
				$.colorbox({
					overlayClose : false,
					href : "#urlDialogPanel",
					title : "<i class=\"fa fa-plus\"></i> 删除分类",
					width : "50%",
					height : 430,
					inline : true,
					fixed : true,
					returnFocus : false,
					open : true,
					// Open事件回调
					onOpen : function() {
						setTimeout(function() {
							Page.form.attr("target", "dialogIfm").attr(
									"action", Action.delInfoAction).submit();
							$("#id option:last").remove();
						}, 0)
					},
					// Close事件回调
					onClosed : function() {
						Page.form.attr("target", "");
					}
				});
			} else {
				$("#id option:last").remove();
				Page.alert("", data.msg);
			}
		});
	},
	// 编辑按钮的单击动作事件
	changeSubType : function(event) {
		if ($("#pid").val() != "") {
			$.post(Action.changeSubTypeAction, {
				"pid" : $("#pid").val()
			}, function(data) {
				if (data.success == false) {
					Page.alert("", data.msg);
				} else {
					$("#id").empty();
					$("#id").append("<option value=\"\">--全部--</option>");
					$.each(data.childCategorys, function(i, val) {
						$("#id").append(
								"<option value=\"" + val.id + "\">" + val.title
										+ "</option>");
					});
				}
			});
		}else{
			$("#id").empty();
			$("#id").append("<option value=\"\">--全部--</option>");
		}
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	}
};
// 现增加然后删除元素,解决select不能取值问题
// $("#id").append(
// "<option value=\"" + $(this).data("id") + "\"></option>");
// $("#id").val($(this).data("id"));
// $("#id option:last").remove();
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	pkIds : $("#id"),

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
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 增加分类
		$(".fn-AddType").click(Events.addTypeClkAct),
		// 增加子分类
		$(".fn-AddSubType").click(Events.addSubTypeClkAct),
		// 全选
		$("#checkall").change(Events.selectAllAct),
		// 修改按钮的单击事件绑定
		$(".fn-ModifyType").click(Events.modifyTypeClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-ModifySubType").click(Events.modifySubTypeClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Close").click(Events.closeClkAct),
		// 启用按钮的单击事件绑定
		$(".fn-Open").click(Events.openClkAct),
		// 启用按钮的单击事件绑定
		$(".fn-Delete").click(Events.delClkAct),
		// 问题分类改变时,联动改变子分类
		$("#pid").change(Events.changeSubType)
	}
});
