var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	//searchAction : webRoot + "/manager/borrow/hjhcredit/hjhcredittender/init?isAccedelist=1",
    searchAction : "searchAction",
	// 已交保证金的Action
	exportAction : "exportAction",
    // PDF签署
    pdfSignAction : "pdfSignAction",
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
	},

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 导出按钮
	exportClkAct : function() {
		var startTime = $("#assignTimeStart").val();
		var endTime = $("#assignTimeEnd").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
    // pdf签署
    pdfSignClkAct : function(event) {
        Page.primaryKey1.val($(this).data("userid"));
        Page.primaryKey2.val($(this).data("borrownid"));
        Page.primaryKey3.val($(this).data("assignorderid"));
        Page.primaryKey4.val($(this).data("contractstatus"));
        Page.primaryKey5.val($(this).data("creditnid"));
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
                $.post(Action.pdfSignAction, $("#mainForm").serialize(),
                    function(data) {
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
                            Page.alert("签署MQ发送失败", data.result);
                        }
                    });
            }
        });
    },
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
    primaryKey1 : $("#userIdHidden"),
    primaryKey2 : $("#borrowNidHidden"),
    primaryKey3 : $("#assignNidHidden"),
    primaryKey4 : $("#contractStatusHidden"),
    primaryKey5 : $("#creditNidHidden"),
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "全部",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('#assignTimeStart').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		$('#assignTimeEnd').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
	    $("#assignTimeStart").on("changeDate", function(ev) {
	        var now = new Date();
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#assignTimeEnd').datepicker("getDate");
	        var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
	        $('#assignTimeEnd').datepicker("setStartDate", selectedDate);
	        $('#assignTimeEnd').datepicker("setEndDate", finalEndDate);
	        //如果end值范围超过30天，设置end最大结束时间
	        if (endDate != null && (+endDate - selectedDate >= day30)) {
	            $('#assignTimeEnd').datepicker("setDate", finalEndDate);
	        }
	    });
	    if($("#assignTimeStart").val() != ''){
	    	 $('#assignTimeStart').datepicker("setDate", $("#assignTimeStart").val());
	    }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
        // PDF签署
        $(".fn-PdfSign").click(Events.pdfSignClkAct);
	}
});
