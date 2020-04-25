var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 删除的Action
	deleteViewAction : "deleteViewAction",
	// 导出的Action
	exportAction : "exportAction",
	// 手动发放
	distributeViewAction : "distributeViewAction",
	// 手动批量发放
	impDistributeViewAction : "impDistributeViewAction",
	// 审核的Action
	auditAction : "auditInitAction",
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	infoClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 优惠券详情",
			width : "50%",
			height : "75%",
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.infoAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 删除按钮的单击动作事件
	deleteClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 删除优惠券",
			width : 450,
			height : 320,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.deleteViewAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 手动发布按钮的单击动作事件
	manualDistributeClkAct : function(event) {
		 $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 发放优惠券",
			width : 450,
			height : 720,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.distributeViewAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 手动批量发放的单击动作事件
	importDistributeClkAct : function(event) {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 批量发放优惠券",
			width : 350,
			height : 420,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.impDistributeViewAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 审核按钮的单击动作事件
	auditClkAct: function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("id"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 优惠券审核",
			width : "60%",
			height : "80%",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.auditAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	exportClkAct : function() {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		var esTime = $("#en-start-date-time").val();
		var eeTime = $("#en-end-date-time").val();
		var flag1 = false;
		var flag2 = false;
		if(startTime == "" || endTime == ""){
			flag1 = true;
		}
		if(esTime == "" || eeTime == ""){
			flag2 = true;
		}
		if(flag1 && flag2){
            Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		var startTime = $("#start-date-time").val();
		var esTime = $("#en-start-date-time").val();
		/*if(startTime == "" && esTime == ""){
			Page.confirm("","时间检索条件起始时间不能为空","error",{showCancelButton: false}, function(){});
			return false;
		}*/
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
	    if($("#start-date-time").val() != ''){
	    	 $('#start-date-time').datepicker("setDate", $("#start-date-time").val());
	    }
		// 日历选择器
		$('#en-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#en-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	    
		$("#en-start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#en-end-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#en-end-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#en-start-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    
	    
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 手动发布按钮的单击事件绑定
		$(".fn-Add").click(Events.manualDistributeClkAct),
		// 手动批量发放的单击事件绑定
		$(".fn-Import").click(Events.importDistributeClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 审核按钮的单击事件绑定
		$(".fn-Fix").click(Events.auditClkAct);
	}
});
