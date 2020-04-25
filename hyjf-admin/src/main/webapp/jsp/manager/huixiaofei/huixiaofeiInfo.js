var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 启用Action
	statusAction : "statusAction",
	// 审核Action
	shenheInfoAction : "shenheInfoAction",
	// 导出的Action
	exportAction : "exportAction",
	// 审核
	shenheAction : "shenheAction",
	// 处理
	chuliAction : "chuliAction"
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.infoAction);
	},
	// 批量审核
	shenheInfoYessClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("", "请选择要审核的项目!", "warning");
		} else {
			cds = [], selection.each(function() {
				cds.push(this.value);
			});
			var ids = "";
			for (var i = 0; i < cds.length; i++) {
				ids += cds[i] + ",";
			}
			ids = ids.substring(0, ids.length - 1);
			$("#ids").val(ids);
			$("#updatestatus").val("0");
			Events.shenheFunction();
		}// Endif
	},
	// 批量审核
	shenheInfoNosClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("", "请选择要审核的项目!", "warning");
		} else {
			cds = [], selection.each(function() {
				cds.push(this.value);
			});
			var ids = "";
			for (var i = 0; i < cds.length; i++) {
				ids += cds[i] + ",";
			}
			ids = ids.substring(0, ids.length - 1);
			$("#ids").val(ids);
			$("#updatestatus").val("1");
			Events.shenheFunction();
		}// Endif
	},
	// 单条审核
	shenheInfoYesClkAct : function(event) {
		$("#ids").val($(this).data("id"));
		$("#updatestatus").val("0");
		Events.shenheFunction();
	},
	// 单条审核
	shenheInfoNoClkAct : function(event) {
		$("#ids").val($(this).data("id"));
		$("#updatestatus").val("1");
		Events.shenheFunction();
	},
	// 审核
	shenheFunction : function() {
		Page.confirm("", "确定操作吗？", function(isConfirm) {
			if (isConfirm) {
				$.ajax({
					url : Action.shenheAction,
					type : "POST",
					async : false,
					data : {
						'ids' : $("#ids").val(),
						'updatestatus' : $("#updatestatus").val()
					},
					success : function(data) {
						Page.coverLayer();
						setTimeout(function() {
							if (data.success) {
								Page.confirm("操作成功", data.msg, "success", {
									showCancelButton : false
								}, function() {
									parent.Events.refreshClkAct() || Page.coverLayer();
								});
							} else {
								Page.confirm("操作失败", data.msg, "error", {
									showCancelButton : false
								}, function() {
									parent.Events.refreshClkAct() || Page.coverLayer();
								});
							}
						}, 500);
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("执行错误,请重新操作!", "", "error");
					}
				});
			}
		});
	},
	// 导出按钮的单击动作事件
	exportClkAct : function(event) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 处理
	chuliClkAct : function(event) {
		$.ajax({
			url : Action.chuliAction,
			type : "POST",
			async : false,
			data : {
				'id' : $("#id").val()
			},
			success : function(data) {
				// Page.coverLayer();
				setTimeout(function() {
					if (data.success) {
						Page.confirm("处理结果", data.msg, "success", {
							showCancelButton : false
						}, function() {
							parent.Events.refreshClkAct() || Page.coverLayer();
						});
					} else {
						Page.confirm("处理失败", data.msg, "error", {
							showCancelButton : false
						}, function() {
							parent.Events.refreshClkAct() || Page.coverLayer();
						});
					}
				}, 500);
			},
			error : function(err) {
				Page.coverLayer();
				Page.notice("执行错误,请重新操作!", "", "error");
			}
		});
	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
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
		// SelectAll
		$("#checkall").change(Events.selectAllAct);
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 审核通过
		$(".fn-shenheYes").click(Events.shenheInfoYesClkAct);
		// 审核不通过
		$(".fn-shenheNo").click(Events.shenheInfoNoClkAct);
		// 批量审核通过
		$(".fn-shenheYess").click(Events.shenheInfoYessClkAct);
		// 批量审核不通过
		$(".fn-shenheNos").click(Events.shenheInfoNosClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 处理
		$(".fn-chuli").click(Events.chuliClkAct);
	}
});
