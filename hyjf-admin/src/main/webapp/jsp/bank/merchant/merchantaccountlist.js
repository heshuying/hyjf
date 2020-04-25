var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	//查询转让列表
	transferListAction:"init",
	// 导出的Action
	exportAction : "exportAccount",
	//同步账号
	synbalanceAction:"synbalance",
	//提现初始化页面 
	withdrawalsAction:"withdrawalsInit",
	//发放红包 
	initPocketSendAction:"initPocketSendAction",
	//充值初始化页面 
	rechargeAction:"rechargeInit"
},
/* 事件动作处理 */
Events = {
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		//window.location.reload();
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.transferListAction);
	},
	// 查找按钮的单击事件绑定
	/*searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.transferListAction);
	},*/
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer()
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	},
	
	updateBalanceClkAct : function(event) {
		var accountCode=$(this).data("account-code");
		$.ajax({
			url : Action.synbalanceAction,
			type : "POST",
			async : true,
			data :  {
				accountCode : accountCode
			},
			success : function(data) {
				if(data.status == "0"){
					//Page.confirm("","请选择导出数据的起止时间\n（起止时间需小于等于31天）","custom",{showCancelButton: false}, function(){});
					Page.alert("", data.message);
					//window.location.reload();
					$("#mainForm").attr("target", "");
					$(".confirm").click(function(){
						$("#paginator-page").val(1), Page.submit(Action.transferListAction);
					})
				}else{
					//alert(result);
					Page.alert("", data.message);
				}
			},
			error : function(err) {
				Page.alert("","接口调用失败!");
			}
		});
		
		
	},
	rechargeClkAct : function() {
		// 充值
		var accountCode=$(this).data("account-code");
		var name=$(this).data("name");
		Page.primaryKey.val($(this).data("account-code")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> "+name+"充值",
			width : 500,
			height : 500,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.rechargeAction+"?accountCode="+accountCode, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	withdrawalsClkAct : function(event) {
		var name=$(this).data("name");
		var accountCode=$(this).data("account-code");
		Page.primaryKey.val($(this).data("account-code")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> "+name+"提现",
			width : 500,
			height : 400,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.withdrawalsAction+"?accountCode="+accountCode, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	initPocketSendClkAct : function(event) {
		Page.primaryKey.val(), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> 发送红包",
			width : 600,
			height : 400,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.initPocketSendAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
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
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		}),
		// 日历范围限制
		$('#opreate-start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#opreate-end-date-time').datepicker("getDate"), d
							&& $('#opreate-start-date-time')
									.datepicker("setEndDate", d)
				}), $('#opreate-end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#opreate-start-date-time').datepicker("getDate"), d
							&& $('#opreate-end-date-time')
									.datepicker("setStartDate", d)
		}),
		// 日历范围限制
		$('#transfer-start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#transfer-end-date-time').datepicker("getDate"), d
							&& $('#transfer-start-date-time')
									.datepicker("setEndDate", d)
				}), $('#transfer-end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#transfer-start-date-time').datepicker("getDate"), d
							&& $('#transfer-end-date-time')
									.datepicker("setStartDate", d)
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		/*$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),*/
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		
		// 更新账户余额以及明细
		$(".fn-UpdateBalance").click(Events.updateBalanceClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Returncash").click(Events.rechargeClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Delete").click(Events.withdrawalsClkAct),
		// 初始化发送红包
		$(".fn-initSendPocket").click(Events.initPocketSendClkAct)
	}
});

