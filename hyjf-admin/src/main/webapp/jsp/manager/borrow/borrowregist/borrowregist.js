var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 已交保证金的Action
	debtRegistAction : "debtRegistAction"
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
		$("#borrowNidSrch").val("");
		$("#borrowNameSrch").val("");
		$("#usernameSrch").val("");
		$("#start-date-time").val("");
		$("#end-date-time").val("");
	},
	// 已交保证金按钮的单击动作事件
	debtRegistAct : function(event) {
		var borrowName;
		if(event) {
			Page.primaryKey.val($(this).data("borrownid"));
			borrowName = $(this).data("borrowname");
		}
		Page.confirm("银行备案", "确认进行银行备案吗？\r\n项目编号："+Page.primaryKey.val()+"",  function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.borrowNid = Page.primaryKey.val();
				param.pageToken = $('#pageToken').val();
			    Page.coverLayer("正在操作数据，请稍候...");
				$.ajax({
					url : Action.debtRegistAction,
					type : "POST",
					async : false,
					//data : JSON.stringify(param),
					data : {
                        borrowNid : param.borrowNid,
                        pageToken : param.pageToken
                    },
					dataType: "json",
					//contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						Page.primaryKey.val("");
						setTimeout(function(){
							if (data.success == "0") {
							    Page.confirm("",data.msg,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
							} else {
								Page.confirm("",data.msg,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
							}
						},500)
					},
					error : function(err) {
						Page.coverLayer();
						Page.primaryKey.val("");
						Page.notice("标的备案发生错误,请重新操作!", "","error");
					}
				});
			}
		});
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#borrowNid"),
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
		$('#start-date-time').on("show", function(evnet, d) {
			d = $('#end-date-time').datepicker("getDate"),
			d && $('#start-date-time').datepicker("setEndDate", d)
		}),
		$('#end-date-time').on("show", function(evnet, d) {
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
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 已交保证金按钮的单击事件绑定
		$(".fn-debtRegist").click(Events.debtRegistAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});
