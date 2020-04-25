var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 页面查询
	searchAction : "userauthlist",
	// 导出的Action
	exportAction : "exportuserauth",
	// 自动出借解约
	invesCancelAction : "userinvescancel",
    // 自动债转解约
    creditCancelAction : "usercreditcancel",
    // 自动授权查询
    queryAuth : "userauthquery"

},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$.uniform.update($(".listCheck").attr("checked", this.checked));
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").trigger('change');
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 编辑按钮的单击动作事件
	modifyClkAct : function(selection) {
		// 取得选择行
		selection = $(".listCheck:checked").last();
		console.info(!selection[0]);
		if (!selection[0]) {
			Page.alert("请指定要修改的权限维护！", "alert-warning");
		} else {
			var primaryKey = selection.val();
			Page.primaryKey.val(primaryKey), Page.submit(Action.infoAction);
		}// Endif
	},
	// 删除按钮的单击动作事件
	deleteClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("请选择要删除的权限维护！", "alert-warning");
		} else {
			cds = [], selection.each(function() {
				cds.push(this.value);
			}), Page.primaryKey.val(JSON.stringify(cds)), Page.submit(Action.deleteAction);
		}// Endif
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},

    // 自动出借解约按钮的单击动作事件
    invesCancelAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("userid"));
        }
        Page.confirm("自动出借解约", "确认执行此操作吗？",  function(isConfirm) {
            if (isConfirm) {
                var param = {};
                param.userId = Page.primaryKey.val();
                Page.coverLayer("正在操作数据，请稍候...");
                $.ajax({
                    url : Action.invesCancelAction,
                    type : "GET",
                    async : true,
                    data : param,
                    success : function(data) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        if (data.success == "0") {
                            Page.confirm("",data.msg,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        } else {
                            Page.confirm("",data.msg,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        }
                    },
                    error : function(err) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        Page.notice("自动出借解约发生错误,请重新操作!", "","error");
                    }
                });
            }
        });
    },

    // 自动债转解约按钮的单击动作事件
    creditCancelAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("userid"));
        }
        Page.confirm("自动债转解约", "确认执行此操作吗？",  function(isConfirm) {
            if (isConfirm) {
                var param = {};
                param.userId = Page.primaryKey.val();
                Page.coverLayer("正在操作数据，请稍候...");
                $.ajax({
                    url : Action.creditCancelAction,
                    type : "GET",
                    async : true,
                    data : param,
                    success : function(data) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        if (data.success == "0") {
                            Page.confirm("",data.msg,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        } else {
                            Page.confirm("",data.msg,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        }
                    },
                    error : function(err) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        Page.notice("自动债转解约发生错误,请重新操作!", "","error");
                    }
                });
            }
        });
    },

    // 自动授权查询按钮的单击动作事件
    queryAuthAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("userid"));
        }
        Page.confirm("自动授权查询", "确认执行此操作吗？",  function(isConfirm) {
            if (isConfirm) {
                var param = {};
                param.userId = Page.primaryKey.val();
                Page.coverLayer("正在操作数据，请稍候...");
                $.ajax({
                    url : Action.queryAuth,
                    type : "GET",
                    async : true,
                    data : param,
                    success : function(data) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        if (data.success == "0") {
                            Page.confirm("",data.msg,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        } else {
                            Page.confirm("",data.msg,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
                        }
                    },
                    error : function(err) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        Page.notice("自动授权查询发生错误,请重新操作!", "","error");
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
	primaryKey : $("#userId"),
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
        $('#inves-start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#inves-end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        var day30 = 30 * 24 * 60 * 60 * 1000;
        $("#inves-start-date-time").on("changeDate", function(ev) {
            var now = new Date();
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#inves-end-date-time').datepicker("getDate");
            var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
            $('#inves-end-date-time').datepicker("setStartDate", selectedDate);
            $('#inves-end-date-time').datepicker("setEndDate", finalEndDate);
            //如果end值范围超过30天，设置end最大结束时间
            if (endDate != null && (+endDate - selectedDate >= day30)) {
                $('#inves-end-date-time').datepicker("setDate", finalEndDate);
            }
        });
        if($("#inves-start-date-time").val() != ''){
            $('#inves-start-date-time').datepicker("setDate", $("#inves-start-date-time").val());
        };
        // 日历选择器
        $('#credit-start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#credit-end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        var day30 = 30 * 24 * 60 * 60 * 1000;
        $("#credit-start-date-time").on("changeDate", function(ev) {
            var now = new Date();
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#credit-end-date-time').datepicker("getDate");
            var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
            $('#credit-end-date-time').datepicker("setStartDate", selectedDate);
            $('#credit-end-date-time').datepicker("setEndDate", finalEndDate);
            //如果end值范围超过30天，设置end最大结束时间
            if (endDate != null && (+endDate - selectedDate >= day30)) {
                $('#credit-end-date-time').datepicker("setDate", finalEndDate);
            }
        });
        if($("#credit-start-date-time").val() != ''){
            $('#credit-start-date-time').datepicker("setDate", $("#credit-start-date-time").val());
        }
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$(".checkall").change(Events.selectAllAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
        // 自动出借解约按钮的单击事件绑定
        $(".fn-invesCancel").click(Events.invesCancelAct),
        // 自动出借解约按钮的单击事件绑定
        $(".fn-creditCancel").click(Events.creditCancelAct),
        // 自动授权查询
        $(".fn-queryAuth").click(Events.queryAuthAct)
		// 操作记录单击事件绑定
//		$(".fn-Operation").click(Events.operationClkAct)
	}
});