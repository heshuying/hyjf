var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {

	// 查询的Action
	searchAction : "helpInit",
	// 详细画面的Action
	infoAction : "helpInfoAction",
	// 详细画面的Action
	updateAction : "helpUpdateAction",
	// 关闭的Action
	closeAction : "helpCloseAction",
	// 启用Action
	openAction : "helpOpenAction",
	//删除Action
	deleteAction:"helpDelAction",
	//添加到常见问题
	moveOftenAction:"moveOftenAction",
    //添加到常见问题
    moveZhiChiAction:"moveZhiChiAction",
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
	addClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 添加问题",
			width: "50%", height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.pkIds.val(""),
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	// 编辑按钮的单击动作事件
	modifyClkAct: function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 修改问题",
			width: "50%", height: 430,
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
	openClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行本次启用操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.openAction);
		})
	},
	closeClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行本次关闭操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.closeAction);
		})
	},
	delClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.deleteAction);
		})
	},
    oftenClkAct : function(event) {
        if(event) {
            console.info($(this).data());
            Page.pkIds.val($(this).data("id"))
        }
        Page.confirm("", "确定要执行添加到常见问题吗？", function(isConfirm) {
            isConfirm && Page.submit(Action.moveOftenAction);
        })
    },
	zhiChiClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id"))
		}
		Page.confirm("", "确定要执行添加到智齿客服常见问题吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.moveZhiChiAction);
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
		}else{
			$("#cateId").empty();
			$("#cateId").append("<option value=\"\">--全部--</option>");
		}
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	pkIds : $("#id"),
	
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
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		$("#checkall").change(Events.selectAllAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Close").click(Events.closeClkAct),
		// 启用按钮的单击事件绑定
		$(".fn-Open").click(Events.openClkAct),
		//删除按钮的点击事件绑定
		$(".fn-Delete").click(Events.delClkAct),
		//添加到常见问题按钮的点击事件绑定
		$(".fn-Often").click(Events.oftenClkAct),
		$(".fn-zhichi").click(Events.zhiChiClkAct),
		// 问题分类改变时,联动改变子分类
		$("#pcateId").change(Events.changeSubType)
	}
});
