var Action = {
	// 查找的Action
	searchAction : "searchAction",
	processAction : "processAction",
    checkAction : "checkAction"
};

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct : function() {

	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		Page.submit(Action.searchAction);
	},
    createHtml: function (data) {
        var html =
            '<div class="form-group text-left margin-left-30">' +
            '<span class="margin-right-15 margin-left-15" >标的编号:</span>' +
            '<span>' + data.borrowNid + '</span>' +
            '</div><div class="form-group text-left margin-left-30">' +
            '<span class="margin-right-15" >冻结订单号:</span>' +
            '<span>' + data.orderId + '</span>' +
            '</div><div class="form-group text-left margin-left-30">' +
            '<span class="margin-right-15" >银行返回码:</span>' +
            '<span>' + data.retCode + '</span>' +
            '</div><div class="form-group text-left margin-left-30">' +
            '<span class="margin-right-15 margin-left-15" >冻结状态:</span>' +
            '<span>' + (data.retCode == '00000000' && data.state == '0' ? "成功" : (data.retCode == '00000000' ? "已撤销" : "失败")) + '</span>' +
            '</div>';
        return html;
    },
    getButtonText: function (data) {
        if (data && data.retCode == '00000000') {
            return data.state == '0' ? "还款" : "还款解冻";
        } else if (data && data.createTime) {
            var nowTimestamp = new Date().getTime() / 1000;
            if (nowTimestamp >= data.createTime + 60 * 20) {
                return "还款解冻";
            }
        }
        return "确定";
    },
    confirmClkAct: function (event) {
        if (event) {
            Page.primaryKey.val($(this).data("orderId"))
        }
        var param = {};
        param.orderId = Page.primaryKey.val();
        param.borrowNid = $(this).data("borrowNid");
        $.ajax({
            url: Action.checkAction,
            type: "POST",
            async: true,
            data: JSON.stringify(param),
            dataType: "json",
            contentType: "application/json",
            success: function (data) {
                var html = Events.createHtml(data);
                var buttonText = Events.getButtonText(data);
                Page.confirm("", html, "warning", {
                    html: true,
                    imageUrl: null,
                    confirmButtonText: buttonText,
                    cancelButtonText: "取消",
                    showCancelButton: true
                }, function (isConfirm) {
                    if (isConfirm && buttonText != "确定") {
                        Page.coverLayer("正在处理,请稍候...");
                        $.ajax({
                            url : Action.processAction,
                            type : "POST",
                            async : true,
                            data : JSON.stringify(data),
                            dataType : "json",
                            contentType : "application/json",
                            success : function(data) {
                                Page.coverLayer();
                                Page.primaryKey.val("");
                                if (data.status == "success") {
                                    setTimeout(function () {
                                        Page.confirm("", data.result, "success", {showCancelButton: false}, function () {
                                            Events.refreshClkAct()
                                        });
                                    }, 0)
                                } else {
                                    setTimeout(function () {
                                        Page.confirm("", data.result, "error", {showCancelButton: false}, function () {
                                            Events.refreshClkAct()
                                        });
                                    }, 0)
                                }
                            },
                            error : function(err) {
                                Page.coverLayer();
                                Page.primaryKey.val("");
                                Page.notice("发生错误,请重新操作!", "", "error");
                            }
                        });
                    }
                })
            },
            error: function (err) {
                Page.coverLayer();
                Page.primaryKey.val("");
                Page.notice("发生错误,请重新操作!", "", "error");
            }
        });
    }
};
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	primaryKey : $("#orderId"),
	// 画面布局
	doLayout: function() {
        // 日历选择器
        $('#freeze-start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#freeze-end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        // 日历选择器
        $('#submit-start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#submit-end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });

        $("#freeze-start-date-time").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#freeze-end-date-time').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate + day30);
            if(endDate==null){
                $('#freeze-end-date-time').datepicker("setDate", finalEndDate);
            }else if (+selectedDate > +endDate) {
                $('#freeze-end-date-time').datepicker("setDate", finalEndDate);
            }else if(+endDate - selectedDate > day30){
                $('#freeze-end-date-time').datepicker("setDate", finalEndDate);
            }
        });
        $("#freeze-end-date-time").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var startDate = $('#freeze-start-date-time').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate -day30);
            if(startDate==null){
                $('#freeze-start-date-time').datepicker("setDate", finalEndDate);
            }else if (+startDate > + selectedDate) {
                $('#freeze-start-date-time').datepicker("setDate", finalEndDate);
            }else if(+selectedDate - startDate > day30){
                $('#freeze-start-date-time').datepicker("setDate", finalEndDate);
            }
        });

        $("#submit-start-date-time").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#submit-end-date-time').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate + day30);
            if(endDate==null){
                $('#submit-end-date-time').datepicker("setDate", finalEndDate);
            }else if (+selectedDate > +endDate) {
                $('#submit-end-date-time').datepicker("setDate", finalEndDate);
            }else if(+endDate - selectedDate > day30){
                $('#submit-end-date-time').datepicker("setDate", finalEndDate);
            }
        });
        $("#submit-end-date-time").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var startDate = $('#submit-start-date-time').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate -day30);
            if(startDate==null){
                $('#submit-start-date-time').datepicker("setDate", finalEndDate);
            }else if (+startDate > + selectedDate) {
                $('#submit-start-date-time').datepicker("setDate", finalEndDate);
            }else if(+selectedDate - startDate > day30){
                $('#submit-start-date-time').datepicker("setDate", finalEndDate);
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
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),		
		// 解冻资金的单击事件绑定
		$(".fn-UpdateBalance").click(Events.confirmClkAct);
	}
});
