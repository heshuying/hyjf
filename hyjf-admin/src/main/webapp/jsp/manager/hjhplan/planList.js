var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 查找的Action
	checkStatus : "checkStatus",
	// 校验显示/隐藏状态
    checkDisplayStatus: "checkDisplayStatus",
	// 导出的Action
	exportExcelAction : "exportExcel",
	// 开启/关闭 的Action
	switchAction  : "switchAction",
	// 开启/关闭 的详情页面Action
	displayShowAction : "displayShowAction",
	// 显示/隐藏 的Action
	displayAction : "displayAction",
	// 详情画面
	detailAction : webRoot + "",/*原/manager/borrow/plancommon/detailAction.do*/
	// 添加计划点击后的详情画面的Action
	infoAction : "addPlanAction",
	// 运营记录
	optRecordAction: webRoot+ "/manager/borrow/borrow/optAction",
},
/* 事件动作处理 */
Events = {
	// 查找按钮的单击事件绑定
		searchClkAct : function(selection, cds) {
			$("#paginator-page").val(1);
			Page.submit(Action.searchAction);
		},

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},


	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的添加时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},

	// (2)重置表单
	resetFromClkAct: function() {
		//1.清空 出借状态 下拉菜单
		$(".form-select2").val("").trigger('change');
		//2.清空查询条件
		$("#planNidSrch").val("");
		$("#planNameSrch").val("");

	},

	// (3)添加智投 按钮 的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},

	// (5)修改按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("debtplannid"));//取修改按键的传值${record.planNid }
		Page.primaryKey1.val($(this).data("debtplannid"));
		Page.primaryKey2.val($(this).data("debtplanstatus"));//取修改按键的传值 ${record.planInvestStatus }
			Page.submit(Action.infoAction);
	},


	// 运营记录
	optRecordClkAct: function(event) {
		Page.primaryKey.val($(this).data("debtplannid"));//取修改按键的传值${record.planNid }
		Page.primaryKey3.val($(this).data("debtplannid"));
		Page.primaryKey2.val($(this).data("debtplanstatus"));//取修改按键的传值 ${record.planInvestStatus }
			Page.submit(Action.optRecordAction);
	},


	//(4) 排序按钮
	sortClkAct : function() {
		$("#col").val($(this).data("col"));
		$("#sort").val($(this).data("sort") == "asc" ? 'asc' : 'desc');
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},

	// (6)启用/关闭 的单击动作事件
	detailSwitch: function(event) {
/*		Page.primaryKey.val($(this).data("debtplannid"));//取修改按键的传值${record.planNid }
		Page.primaryKey1.val($(this).data("debtplannid"));
		Page.primaryKey2.val($(this).data("debtplanstatus"));//取修改按键的传值 ${record.planInvestStatus }
			Page.submit(Action.switchAction);*/
		if(event) {
			console.info($(this).data());
			Page.primaryKey.val($(this).data("debtplannid"));
		}
		Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.switchAction);
		})
	},
	// (7)显示/隐藏 的单击动作事件
	detailDisplay: function(event) {
		if(event) {
			var debtPlanNid = $(this).data("debtplannid");
			var debtPlanDisplayStatus= $(this).data("debtplanstatus");
			var addTime= $(this).data("addtime");
    	}
        var titleStr = debtPlanDisplayStatus;
        var status = titleStr == "1" ? "隐藏" : "显示" ;
        var str = status+"智投";

        //智投显示/隐藏 处理
        $.ajax({
            url : Action.checkDisplayStatus,
            type : "POST",
            async : true,
            data :  {
                debtPlanNid : debtPlanNid,
                planDisplayStatusSrch : debtPlanDisplayStatus
            },
            success:function(data){
                if(data.status == 1){
                    $.colorbox({
                        overlayClose : false,
                        href : "#urlDialogPanel",
                        title : str,
                        width : 450,
                        height : 360,
                        inline : true,
                        fixed : true,
                        returnFocus : false,
                        open : true,
                        // Open事件回调
                        onOpen : function() {
                            setTimeout(function() {
                                Page.form.attr("target", "dialogIfm").attr("action",Action.displayShowAction + "?debtPlanNid=" + debtPlanNid+"&addTime=" + addTime+"&enableOrDisplayFlag=2").submit();
                            }, 0)
                        },
                        // Close事件回调
                        onClosed : function() {
                            Page.form.attr("target", "");
                        }
                    })
                }else{
                    var message="智投状态已为"+status+",无需再次"+status;
                    Page.confirm("",message,"error",{showCancelButton: false}, function(){window.location.reload();});
                }
            }}
        );


    /*Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
			if(isConfirm){
				$.ajax({
					url: Action.displayAction,
					type: "POST",
					async: true,
					data: {
						debtPlanNid: debtPlanNid
					},
					success: function (data) {
						if (data.status ==1){
							//成功以后执行刷新方法
							window.location.reload();
						}
					}
				})
			}
		})*/


	},
	// (7)启动关闭 的单击跳转页面
	detailDisplayShow: function(event) {
		if(event) {
			var debtPlanNid = $(this).data("debtplannid");
			var debtPlanStatus= $(this).data("debtplanstatus");
			var addTime= $(this).data("addtime");
		}
		var titleStr = debtPlanStatus;
		var status = titleStr == "1" ? "禁用" : "启用" ;
		var str = status+"智投";
		$.ajax({
			url : Action.checkStatus,
			type : "POST",
			async : true,
			data :  {
				debtPlanNid : debtPlanNid,
				debtPlanStatus : debtPlanStatus,
				addTime : addTime
			},
		    success:function(data){
			  if(data.status == 1){
				  $.colorbox({
						overlayClose : false,
						href : "#urlDialogPanel",
			 			title : str,
						width : 450,
						height : 360,
						inline : true,
						fixed : true,
						returnFocus : false,
						open : true,
						// Open事件回调
						onOpen : function() {
							setTimeout(function() {
								Page.form.attr("target", "dialogIfm").attr("action",Action.displayShowAction + "?debtPlanNid="
									+ debtPlanNid + "&debtPlanStatus=" + debtPlanStatus+ "&addTime=" + addTime+"&enableOrDisplayFlag=1").submit();
							}, 0)
						},
						// Close事件回调
						onClosed : function() {
							Page.form.attr("target", "");
						}
					})
			  }else{
				  var message="智投状态已为"+status+",无需再次"+status;
				  Page.confirm("",message,"error",{showCancelButton: false}, function(){window.location.reload();});
			  }
		  }}
		);


	},

	// 修改按钮的单击动作事件
	detailClkAct: function(event) {
		Page.primaryKey.val($(this).data("debtplannid"));//取修改按键的传值${record.planNid }
		Page.primaryKey1.val($(this).data("debtplannid"));
		Page.primaryKey2.val($(this).data("debtplanstatus"));//取修改按键的传值 ${record.planInvestStatus }
			Page.submit(Action.detailAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#debtPlanNid"),
	primaryKey1 : $("#debtPlanNidSrch"),
	primaryKey2 : $("#debtPlanStatus"),
	primaryKey3 : $("#planNidSrch"),
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
		// 画面检索条件面板点击展开，再点击关闭
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 添加智投 按钮 的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 检索面板的检索按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 检索面板的修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 汇计划三期新增 运营记录
		$(".fn-OptRecord").click(Events.optRecordClkAct),
		// 排序按钮的单击事件绑定
		$(".fn-Sort").click(Events.sortClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.detailClkAct),
		// 关闭/开启 按钮 的单击事件绑定
		$(".fn-Switch").click(Events.detailSwitch),
		// 隐藏/显示 按钮 的单击事件绑定
		$(".fn-Display").click(Events.detailDisplay),
		// 隐藏/显示 按钮 的单击项目请事件绑定
		$(".fn-DisplayShow").click(Events.detailDisplayShow)
	}
});
