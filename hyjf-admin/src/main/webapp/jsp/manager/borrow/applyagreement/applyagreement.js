var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
	// PDF签署
    pdfSignAction : "pdfSignAction",
    // 删除的Action
	deleteAction : "deleteAction",
	// 查找的Action
	searchAction : "searchAction",
},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct: function() {
		$(".form-control").val("");
		$(".form-select2").val("").trigger('change');
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 详细的单击动作事件
	infoClkAct : function(event) {
		Page.primaryKey.val($(this).data("assignnid"))
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 申请协议",
			width: "80%", height: "60%",
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
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	deleteClkAct : function(event) {
		if(event) {
			Page.primaryKey.val(JSON.stringify([$(this).data("id")]))
		}
		Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.deleteAction);
		})
	},
	 // pdf签署
    pdfSignClkAct : function(event) {
        var borrownid = $(this).data("borrownid");
        var repayperiod = $(this).data("repayperiod");
        Page.confirm("", "确定要重新签署PDF吗?", "info", {
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
            			$.post("pdfSignAction", { "borrowNid": borrownid, "repayPeriod":repayperiod }, function(data) {
                        clearTimeout(tid);
                        Page.coverLayer();
                        if (data.status == "success") {
                            Page.coverLayer();
                            Page.confirm("签署MQ发送成功", "", "info", {
                                closeOnConfirm : true,
                                showCancelButton : false
                            }, function() {
                                window.location.reload();
                            });
                        } else {
                            Page.alert("签署MQ发送失败", data.info);
                        }
                    });
            }
        });
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	primaryKey1 : $("#borrowNidHidden"),
	primaryKey2 : $("#repayperiodHidden"),
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
			todayHighlight: true
		});
		$('#end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	    // 日历选择器
		$('#add-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#add-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
	    var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#end-date-time').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#end-date-time').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#end-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#start-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#start-date-time').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#start-date-time').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#start-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#add-start-date-time").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#add-end-date-time').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#add-end-date-time').datepicker("setStartDate", selectedDate);
	        $('#add-end-date-time').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#add-end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#add-start-date-time").val() != ''){
	    	 $('#add-start-date-time').datepicker("setDate", $("#add-start-date-time").val());
	    }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 详细按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 取消的单击事件绑定
		$(".fn-BeCalcel").click(Events.beCalcelClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出的单击事件绑定
		$(".fn-Add").click(Events.infoClkAct);
		// 下载脱敏协议事件绑定
		$(".fn-Dow").click(Events.DowClkAct);
		// 下载原始协议事件绑定
		$(".fn-DowY").click(Events.DowYClkAct);
		 // PDF签署
        $(".fn-PdfSign").click(Events.pdfSignClkAct);
        //删除
        $(".fn-Delete").click(Events.deleteClkAct);
	},
	// 画面初始化
	initialize : function() {
		//alert($("#success").val());
		// 执行成功后刷新画面
		if($("#success").val() == "success") {
			//alert(2222);
			 parent.$.colorbox.close();
			 parent.Events.reSeacheClkAct();
		} else {
			//alert(2211);
			Page.coverLayer();
		}

	}
});
Page.initialize();
