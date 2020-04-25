var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "init?fid="+$("#fid").val(),
	// 重新跑皮
    updateCount : "updateCount?fid="+$("#fid").val(),
    // 发送MQ
    dosendmq : "dosendmq?fid="+$("#fid").val()
},
/* 事件动作处理 */
Events = {
		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			//window.location.reload();
			$("#mainForm").attr("target", "");
			$("#paginator-page").val(1), Page.submit(Action.searchAction);
		},
		// 查找按钮的单击事件绑定
		searchClkAct : function(selection, cds) {
			$("#mainForm").attr("target", "");
			$("#paginator-page").val(1), Page.submit(Action.searchAction);
		},
		// 重置表单
		resetFromClkAct : function() {
			$(".form-select2").val("").trigger('change');
		},

    	// 查看错误原因按钮
    	errorMessClkAct : function(event) {
			var content = $(this).data("txcounts");
            Page.confirm("查询成功", content, "success", {
                closeOnConfirm : true,
                showCancelButton : false
            }, function() {
                this.close;
            });
    	},

		// 重新跑批按钮
    	updateCountClkAct : function(event) {
			var id = $(this).data("txcounts");

            $.post(Action.updateCount, {"id":id},
                function(data) {
                    if (data.status == "success") {
                        Page.confirm("操作成功", "", "info", {
                            closeOnConfirm : true,
                            showCancelButton : false
                        }, function() {
                            $(".fn-Refresh").click();
                        });
                    } else {
                        Page.alert("操作失败", "");
                    }
                });
		},
    // 按钮事件
    dataSyn : function(event) {
        if(event) {
            Page.primaryKey.val($(this).attr("data-type"));
        }
        var dataType=$(".form-select2").val();
        var mqValue=$("#mqValue").val();
        if(dataType==""||mqValue==""){
            Page.alert("请求参数和请求类型不能为空", "");
            return ;
        }
        Page.confirm("", "确定发送消息吗？", function(isConfirm) {
            if(isConfirm){
                $.ajax({
                    url : Action.dosendmq,
                    async : true,
                    type : "POST",
                    data : {
                        "dataType" : dataType,
                        "mqValue":mqValue
                    },
                    // 成功后开启模态框
                    success : function (data){
                        if(data.status=="success"){
                            Page.confirm("操作成功", "", "success", {
                                closeOnConfirm : true,
                                showCancelButton : false
                            }, function() {
                                this.close;
                            });
                        }else {
                            Page.confirm("操作失败", "", "error", {
                                closeOnConfirm : true,
                                showCancelButton : false
                            }, function() {
                                this.close;
                            });
                        }

                    },
                    error : function() {
                        alert("请求失败");
                    },
                    dataType : "json"
                });// ajax end
            }
        })
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
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
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// ‘推荐人修改申请’按钮的单击事件绑定
		$(".fn-AdminStockInfo").click(Events.AdminStockInfoClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 重置表单
        $(".fn-Reset").click(Events.resetFromClkAct),
        $(".fn-DownFile").click(Events.downLoadClkAct),
        $(".fn-Feedback").click(Events.downloadFeeBack),
		// 查看失败原因
        $(".btn-cksb").click(Events.errorMessClkAct),
		// 重新跑批
		$(".btn-cxpp").click(Events.updateCountClkAct),
		// 刷新按钮的单击事件绑定
		$(".data-syn").click(Events.dataSyn)

	}
});



