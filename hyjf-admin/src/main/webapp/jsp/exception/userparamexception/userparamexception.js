var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 更新用户属性
	updateUserParamAction : "updateUserParam",
	// 更新全部用户属性
	updateAllUserParamAction : "updateAllUserParam",
	// 更新全部用户属性
	initTenderRepairAction : "initTenderRepairAction",
	// 页面查询
	searchAction : "userslist"
},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
	// 更新按钮的单击动作事件
	updateClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid"));
		Page.confirm("", "确定要更新该用户的用户属性么？", "info", {
			closeOnConfirm : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			showCancelButton : true,
		}, function(isConfirm, tid) {
			if (isConfirm) {
				Page.coverLayer("正在操作数据，请稍候...");
				tid = setTimeout(function() {
					swal.close();
				}, 100);
				$.post(Action.updateUserParamAction,
						$("#mainForm").serialize(), function(data) {
							clearTimeout(tid);
							Page.coverLayer();
							if (data.success == true) {
								Page.confirm("操作成功", "更新成功!", "info", {
									closeOnConfirm : false,
									showCancelButton : false
								}, function() {
									Events.refreshClkAct();
								});
							} else {
								Page.alert("操作失败", data.msg);
							}
						});
			}
		})
	},
	// 更新全部用户属性按钮的单击动作事件
	updateAllClkAct : function(event) {
		Page.confirm("", "确定要更新全部的用户属性么？处理时间会很长,请谨慎操作", "info", {
			closeOnConfirm : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			showCancelButton : true,
		}, function(isConfirm, tid) {
			if (isConfirm) {
				Page.alert("处理中", "处理中");
				$.post(Action.updateAllUserParamAction, $("#mainForm")
						.serialize(), function(data) {
					// clearTimeout(tid);
					if (data.success) {
						Page.alert("操作结果", data.info);
					} else {
						Page.alert("操作结果", data.info);
					}
				});
			}
		})
	},
	//修复用户出借数据
	repairTenderClkAct : function(event) {
		 $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> 修复出借数据",
			width : 750,
			height : 400,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.initTenderRepairAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 刷新按钮的单击动作事件
	infoClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-newspaper-o\"></i> 会员详情",
			width : 870,
			height : 630,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.infoAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		});
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
	// 画面布局
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width : 268,
			placeholder : "全部",
			allowClear : true,
			language : "zh-CN"
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
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 更新按钮的单击事件绑定
		$(".fn-Update").click(Events.updateClkAct),
		// 更新按钮的单击事件绑定
		$(".fn-UpdateAll").click(Events.updateAllClkAct),
		// 更新按钮的单击事件绑定
		$(".fn-RepairTender").click(Events.repairTenderClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct)
	}
});