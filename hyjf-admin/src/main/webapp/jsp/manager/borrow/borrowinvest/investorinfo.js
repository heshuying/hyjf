var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
		
	// 查询出借人债权明细按钮的Action
	checkAction : "debtCheckAction",
	
	// 查找的Action
	searchAction : "searchAction",
	// 导出的Action
	exportAction : "exportAction",
	// 重发邮件的Action
	resendMessageAction : "resendMessageAction",
	// 导出协议的Action
	exportAgreementAction : "toExportAgreementAction",
},
/* 事件动作处理 */
Events = {
		
	// ‘查询出借人债权明细’按钮的单击事件
	accountCheckAct : function(event) {
		var userid = $(this).data("userid");
		var nid = $(this).data("nid");
		var borrownid = $(this).data("borrownid");
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 查询出借人债权明细",
			width: "50%", height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.checkAction + "?userid=" + userid + "&nid=" + nid + "&borrownid=" + borrownid).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
		
		/*Page.confirm("", "确定要执行本次对账操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.userId = Page.primaryKey.val();
				param.startTime = Page.startTimeKey.val();
				param.endTime = Page.endTimeKey.val();
				
				//console.log(param);
				//Page.coverLayer("正在对账,请稍候...");
				$.ajax({
					url : Action.bankCheckAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						Page.primaryKey.val("");
						if (data.status == "success") {
						    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.cancelClkAct()});
						} else {
							Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.cancelClkAct()});
							Page.notice(data.msg, "","error");
						}
						
					},
					error : function(err) {
						Page.coverLayer();
						Page.primaryKey.val("");
						Page.notice("对账发生错误,请重新操作!", "","error");
					}
				});
				
			};

		})*/
		/**/
	},
		
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');

		$("#borrowNidSrch").val("");
		$("#referrerNameSrch").val("");
		$("#usernameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");

	},
	// 导出按钮的单击动作事件
	exportClkAct : function(event) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 重发按钮的单击动作事件
	updateClkAct : function(event) {
		var param = {};
		param.userid = $(this).data("userid");
		param.nid = $(this).data("nid");
		param.borrownid = $(this).data("borrownid");
		Page.coverLayer("正在操作数据，请稍候...");
		// alert("updateClkAct");
		$.ajax({
			url : Action.resendMessageAction,
			type : "post",
			data : {
				userid : param.userid,
				nid : param.nid,
				borrownid : param.borrownid
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
	// 导出协议
	exportAgreementClkAct : function(event) {
		var userid = $(this).data("userid");
		var nid = $(this).data("nid");
		var borrownid = $(this).data("borrownid");
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-plus\"></i> 发送协议",
			width : "30%",
			height : "200",
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.exportAgreementAction + "?userid=" + userid + "&nid=" + nid + "&borrownid=" + borrownid).submit();
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
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
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
		var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		
		//'查询出借人债权明细'按钮的单击事件
		$(".fn-check").click(Events.accountCheckAct),
		
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 重发邮件按钮的单击事件绑定
		$(".fn-Modify").click(Events.updateClkAct);
		// 导出Excel的单击事件绑定
		$(".fn-ExportAgreement").click(Events.exportAgreementClkAct);
	}
});
