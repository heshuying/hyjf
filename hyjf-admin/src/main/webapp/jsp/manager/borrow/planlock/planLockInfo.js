var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "infoSearchAction",
	// 导出的Action
	exportAction : "exportAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 预览的Action
	previewAction : "previewAction",
	// 更新的Action
	updateAction : "updateAction",
	// 出借的Action
	InvestAction : "planInvest",
	//债转的出借Action
	creditTenderInfoAction : "creditTenderInfoAction",
	
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.primaryKey.val($(this).data("debtPlanNid"));
		Page.submit(Action.searchAction);
	},
	// 重发按钮的单击动作事件
	updateClkAct : function(event) {
		var param = {};
		param.accedeorderid = $(this).data("accedeorderid");
		Page.coverLayer("正在操作数据，请稍候...");
		// alert("updateClkAct");
		$.ajax({
			url : Action.updateAction,
			type : "post",
			data : {
				accedeorderid : param.accedeorderid
			},
			dataType : "json",
			success : function(data) {
				Page.coverLayer();
				if (data.status == "success") {
					Page.confirm("", data.result, "success", {
						showCancelButton : false
					}, function() {
						Page.coverLayer("正在处理,请稍候...");
						Events.refreshClkAct()
					});
				} else {
					Page.confirm("", data.result, "error", {
						showCancelButton : false
					}, function() {
						Page.coverLayer("正在处理,请稍候...");
						Events.refreshClkAct()
					});
				}
			},
			error : function(err) {
				Page.coverLayer();
				Page.notice("系统异常,请重新操作!", "", "error");
			}
		});
	},
	// 编辑按钮的单击动作事件
	investClkAct: function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"))
			$("#accedeOrderId").val($(this).data("accedeorderid"));
			$("#borrowNid").val($(this).data("borrownid"));
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 出借（直投类产品）",
			width: 650, height: 480,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.InvestAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
				window.location.reload();
			}
		})
	},
	
	// 债转出借按钮的点击动作事件
	creditTenderClkAct: function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"));
			Page.primaryKey2.val($(this).data("creditnid"));
			// 计划订单号
			$("#accedeOrderId").val($(this).data("accedeorderid"));
			// 债转编号
			$("#creditNid").val($(this).data("creditnid"));
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 出借（转让类产品）",
			width: 650, height: 480,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.creditTenderInfoAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
				window.location.reload();
			}
		})
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
		$("#borrowNidSrch").val("");
		$("#fullExpireTime").val("");
		$("#liquidateShouldTime").val("");
	},
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 修改按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("accedeorderid"));
		$("#accedeOrderId").val($(this).data("accedeorderid"));
			Page.submit(Action.infoAction);
	},
	
	// 预览按钮的单击动作事件
	previewClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("debtPlanNid"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 预览地址",
			width: "50%", height: 300,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.previewAction).submit();
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
	},
	sortClkAct : function() {
		$("#col").val($(this).data("col"));
		$("#sort").val($(this).data("sort") == "asc" ? 'asc' : 'desc');
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#debtPlanNid"),
	primaryKey1 : $("#debtPlanNidSrch"),
	primaryKey2 : $("#creditNid"),
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
		$('#recover-start-date-time').on("change", function(evnet, d) {
			d = $('#recover-end-date-time').datepicker("getDate"),
			d && $('#recover-start-date-time').datepicker("setEndDate", d)
		}),
		$('#recover-end-date-time').on("change", function(evnet, d) {
			d = $('#recover-start-date-time').datepicker("getDate"),
			d && $('#recover-end-date-time').datepicker("setStartDate", d)
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
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-preview").click(Events.previewClkAct),
		// 次数清零单击事件绑定
		$(".fn-update").click(Events.updateClkAct),
		// 出借单击事件绑定
		$(".fn-invest").click(Events.investClkAct),
		// 出借明细按钮的单击事件绑定
		$(".fn-TenderInfo").click(Events.tenderInfoClkAct);
		// 债转出借按钮单击事件
		$(".fn-CreditTender").click(Events.creditTenderClkAct);
	}
});
