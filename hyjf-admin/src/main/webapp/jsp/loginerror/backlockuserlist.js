var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "backinit",
	// 解锁Action
	unLockAction : "backunlock",
	// 登录失败锁定配置
	loginErrorCfgAction : "backLoginErrorCfg",

},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
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

	// 更新按钮的单击动作事件
    // 自动债转解约按钮的单击动作事件
    unlockAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("userid"));
        }
        Page.confirm("解除锁定", "确认执行此操作吗？",  function(isConfirm) {
            if (isConfirm) {
                var param = {};
                param.id = Page.primaryKey.val();
                Page.coverLayer("正在操作数据，请稍候...");
                $.ajax({
                    url : Action.unLockAction,
                    type : "GET",
                    async : true,
                    data : param,
                    success : function(data) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        if (data.success == "0") {
                            setTimeout(function(){
                                Page.confirm("",data.msg,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
                            },100);

                        } else {
                            setTimeout(function(){
                                Page.confirm("",data.msg,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
                            },100);
                        }
                    },
                    error : function(err) {
                        Page.coverLayer();
                        Page.primaryKey.val("");
                        Page.notice("解除锁定发生错误,请重新操作!", "","error");
                    }
                });
            }
        });
    },

	configAct:function (event) {
        $.colorbox({
            overlayClose: false,
            href: "#urlDialogPanel",
            title: "<i class=\"fa fa-plus\"></i>后台登录失败锁定",
            width: "60%", height: "40%",
            maxWidth: 1000,
            inline: true, fixed: true, returnFocus: false, open: true,
            // Open事件回调
            onOpen: function () {
                setTimeout(function () {
                    //Page.primaryKey.val($(this).data("id")),
                    Page.form.attr("target", "dialogIfm").attr("action", Action.loginErrorCfgAction).submit();
                }, 0)
            },
            // Close事件回调
            onClosed: function () {
                Page.form.attr("target", "");
            }
        })
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userid"),
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
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),

		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 查找按钮的单击事件绑定
		$(".fn-unlock").click(Events.unlockAct);
		//
		$(".fn-config").click(Events.configAct);
	}
});