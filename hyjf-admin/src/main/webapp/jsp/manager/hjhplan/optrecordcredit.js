var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
	Action = {
		// 查找的Action
		searchAction : webRoot + "/manager/borrow/hjhcredit/optCreditActionSearch",
        initAction : webRoot + "/manager/borrow/hjhcredit/optCreditActionInit",
		// 详细画面的Action
		infoAction : "infoAction",
		// 导出
		exportAction : "exportAction",
		// 原始标的Action
		optLoanAction : webRoot + "/manager/borrow/borrow/optAction",
		// 出借明细Action
		optTenderAction : webRoot + "/manager/borrow/borrowinvest/optActionInit",
		// 运营记录-tab承接明细标签Action
		optCreditTenderAction : webRoot + "/manager/borrow/hjhcredit/hjhcredittender/optActionInit",
		// 点击操作栏的承接明细跳转，带有flag
        optCreditTenderFlagAction : webRoot + "/manager/borrow/hjhcredit/hjhcredittender/optActionInit?isOptFlag=1"
	},
/* 事件动作处理 */
	Events = {
		// 查找按钮的单击事件绑定
		searchClkAct : function(selection, cds) {
			$("#paginator-page").val(1);
            $("#myPlanNid").val($("#planNidTemp").val());
            $("#isSearch").val("1");
			Page.submit(Action.searchAction);
		},
		// 重置表单
		resetFromClkAct: function() {
			$(".form-select2").val("").trigger('change');
		},
		// 详细的(承接明细)单击动作事件
		infoClkAct : function(event) {
			/*$("#paginator-page").val(1);
			Page.primaryKey3.val($("#planNidSrch").val());
			Page.submit(Action.optCreditTenderFlagAction);*/
            $("#paginator-page").val(1);
            //Page.primaryKey3.val($("#planNidSrch").val());
			$("#assignPlanNid").val("");
            $("#creditNid").val($(this).data("creditnid"));
            $("#myPlanNid").val($("#planNidTemp").val());
            Page.submit(Action.optCreditTenderFlagAction);
		},
		// 刷新按钮的单击动作事件
		refreshClkAct : function() {
			window.location.reload();
		},
		// 导出按钮
		exportClkAct : function() {
			var startTime = $("#liquidatesTimeStart").val();
			var endTime = $("#liquidatesTimeEnd").val();
			var esTime = $("#repayNextTimeStart").val();
			var eeTime = $("#repayNextTimeEnd").val();
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
			setTimeout(function() {
				Page.coverLayer()
			}, 1);
			$("#paginator-page").val(1);
			Page.submit(Action.exportAction);
		},

		// 原始标的点击动作事件
		optLoanClkAct : function(selection, cds) {
			$("#paginator-page").val(1);
			//Page.primaryKey2.val($("#planNidSrch").val());
           // Page.primaryKey2.val($("#planNidNew").val());
            var planNid = $("#planNidNew").val();
            if (planNid){
                Page.primaryKey2.val(planNid);
            }else{
                Page.primaryKey2.val($("#planNidTemp").val());
            }
			$("#myPlanNid").val($("#planNidTemp").val());
			Page.submit(Action.optLoanAction);
		},

		// 出借明细点击动作事件
		optTenderClkAct:function(selection, cds) {
			$("#paginator-page").val(1);
			//Page.primaryKey2.val($("#planNidSrch").val());
            var planNid = $("#planNidNew").val();
            if (planNid){
                Page.primaryKey2.val(planNid);
            }else{
                Page.primaryKey2.val($("#planNidTemp").val());
            }
            //Page.primaryKey2.val($("#planNidNew").val());
            $("#myPlanNid").val($("#planNidTemp").val());
			Page.submit(Action.optTenderAction);
		},

		// 承接明细点击动作事件
		optCreditTenderClkAct:function(selection, cds) {
			$("#paginator-page").val(1);
			var planNid = $("#planNidNew").val();
			if (planNid){
                Page.primaryKey3.val(planNid);
            }else{
                Page.primaryKey3.val($("#planNidTemp").val());
			}
            $("#myPlanNid").val($("#planNidTemp").val());
			Page.submit(Action.optCreditTenderAction);
		}

	};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#creditNid"),
	primaryKey2 : $("#planNidSrch"),  // 跳转原始标的和出借明细参数
	primaryKey3 : $("#assignPlanNid"), // 承接明细参数
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
			$('#liquidatesTimeStart').datepicker({
				autoclose: true,
				todayHighlight: true
			});
		$('#liquidatesTimeEnd').datepicker({
			autoclose: true,
			todayHighlight: true
		});

		//预计开始退出时间
        $('#endDateStart').datepicker({
            autoclose: true,
            todayHighlight: true
        });
        $('#endDateEnd').datepicker({
            autoclose: true,
            todayHighlight: true
        });

		// 日历选择器
		$('#repayNextTimeStart').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#repayNextTimeEnd').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		var day30 = 30 * 24 * 60 * 60 * 1000;
		$("#liquidatesTimeStart").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var endDate = $('#liquidatesTimeEnd').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate + day30);
			if(endDate==null){
				$('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
			}else if (+selectedDate > +endDate) {
				$('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
			}else if(+endDate - selectedDate > day30){
				$('#liquidatesTimeEnd').datepicker("setDate", finalEndDate);
			}
		});
		$("#liquidatesTimeEnd").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var startDate = $('#liquidatesTimeStart').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate -day30);
			if(startDate==null){
				$('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
			}else if (+startDate > + selectedDate) {
				$('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
			}else if(+selectedDate - startDate > day30){
				$('#liquidatesTimeStart').datepicker("setDate", finalEndDate);
			}
		});

        /**
		 * 预计开始退出时间
         */
        $("#endDateStart").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#endDateEnd').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate + day30);
            if(endDate==null){
                $('#endDateEnd').datepicker("setDate", finalEndDate);
            }else if (+selectedDate > +endDate) {
                $('#endDateEnd').datepicker("setDate", finalEndDate);
            }else if(+endDate - selectedDate > day30){
                $('#endDateEnd').datepicker("setDate", finalEndDate);
            }
        });
        $("#endDateEnd").on("changeDate", function(ev) {
            var selectedDate = new Date(ev.date.valueOf());
            var startDate = $('#endDateStart').datepicker("getDate");
            var finalEndDate = new Date(+selectedDate -day30);
            if(startDate==null){
                $('#endDateStart').datepicker("setDate", finalEndDate);
            }else if (+startDate > + selectedDate) {
                $('#endDateStart').datepicker("setDate", finalEndDate);
            }else if(+selectedDate - startDate > day30){
                $('#endDateStart').datepicker("setDate", finalEndDate);
            }
        });


		$("#repayNextTimeStart").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var endDate = $('#repayNextTimeEnd').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate + day30);
			if(endDate==null){
				$('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
			}else if (+selectedDate > +endDate) {
				$('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
			}else if(+endDate - selectedDate > day30){
				$('#repayNextTimeEnd').datepicker("setDate", finalEndDate);
			}
		});
		$("#repayNextTimeEnd").on("changeDate", function(ev) {
			var selectedDate = new Date(ev.date.valueOf());
			var startDate = $('#repayNextTimeStart').datepicker("getDate");
			var finalEndDate = new Date(+selectedDate -day30);
			if(startDate==null){
				$('#repayNextTimeStart').datepicker("setDate", finalEndDate);
			}else if (+startDate > + selectedDate) {
				$('#repayNextTimeStart').datepicker("setDate", finalEndDate);
			}else if(+selectedDate - startDate > day30){
				$('#repayNextTimeStart').datepicker("setDate", finalEndDate);
			}
		});

		$("#actualAprStartId").keyup(function (ev) {
            var value = $(this).val();

            if (value.indexOf("0") == 0){
                value = parseFloat(value);
                $(this).val("");
            }
	        var actualAprEndStr = $("#actualAprEndId").val();
	        if (actualAprEndStr.length == 0){
                actualAprEndStr = 0;
				return;
			}
			if (parseFloat(value) > parseFloat(actualAprEndStr)) {
	        	$(this).val(actualAprEndStr);
			}
        });

        $("#actualAprEndId").keyup(function (ev) {
            var value = $(this).val();

            if (value.indexOf("0") == 0){
            	value = parseFloat(value);
            	$(this).val("");
			}
            var actualAprStartStr = $("#actualAprStartId").val();
            if (actualAprStartStr.length == 0){
                actualAprStartStr = 0;
                return;
            }
            if (parseFloat(value) < parseFloat(actualAprStartStr)) {
                $(this).val(actualAprStartStr);
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
			// 详细按钮的单击事件绑定
			$(".fn-Info").click(Events.infoClkAct),
			// 刷新按钮的单击事件绑定
			$(".fn-Refresh").click(Events.refreshClkAct),
			// 导出的单击事件绑定
			$(".fn-Export").click(Events.exportClkAct),
			$(".ysbd").click(Events.optLoanClkAct),
			$(".zzbd").click(Events.searchClkAct);
		    $(".tzmx").click(Events.optTenderClkAct);
			$(".cjmx").click(Events.optCreditTenderClkAct);

	}
});




