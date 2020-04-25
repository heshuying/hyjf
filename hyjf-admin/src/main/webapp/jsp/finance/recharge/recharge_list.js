var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {	
	// 查询的Action
	searchAction : "searchAction",
	// 平台转账画面的Action
	tohandrechargeAction : "tohandrechargeAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	//fix
	fixAction : "rechargeFixAction",
	// 导出execlAction
	exportExcelAction : "exportAction",
	// 具有组织机构查看权限的增强导出
    enhanceExportAction:"enhanceExportAction",
	// 修改充值状态
	modifyAction : "modifyAction"
},
/* 事件动作处理 */
Events = {

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 平台转账按钮的单击动作事件
	handRechargeClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 平台转账",
			width: "50%", height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.tohandrechargeAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	// fix单击事件
	fixClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("userid")),
			Page.nidKey.val($(this).data("nid")),
			Page.statusKey.val($(this).data("status"))
		}
		
		Page.confirm("", "您确定要执行该操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.userId = Page.primaryKey.val();
				param.nid = Page.nidKey.val();
				param.status=Page.statusKey.val();
				
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.fixAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
						    setTimeout(function(){
						    	Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						    },0);
						} else {
							setTimeout(function(){
								Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
							 },0);
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("充值确认操作发生错误,请重新操作!", "","error");
					}
				});
			};

		})
	},
	
	// 更新充值状态
	modifyClkAct : function(event){
		if(event) {
			Page.nidKey.val($(this).data("nid")),
			Page.primaryKey.val($(this).data("userid"))
		}
		Page.confirm("", "您确定要执行该操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.nid = Page.nidKey.val();
				param.userId = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.modifyAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
						    setTimeout(function(){
						    	Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						    },0);
						} else {
							setTimeout(function(){
								Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
							 },0);
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("充值状态修改发生错误,请重新操作!", "","error");
					}
				});
			};
		})
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
    enhanceExportAction : function(selection, cds) {
        var startTime = $("#start-date-time").val();
        var endTime = $("#end-date-time").val();
        if(startTime == "" || endTime == ""){
            Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function(){Page.coverLayer()},1);
        Page.submit(Action.enhanceExportAction);
    }
	
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
	nidKey:$("#nid"),
	statusKey:$("#status"),
	
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
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modifys").click(Events.modifysClkAct),
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Deletes").click(Events.deletesClkAct),
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// FIX单击事件绑定
		$(".fn-fix").click(Events.fixClkAct),
		// 打开平台转账页面
		$(".fn-handRecharge").click(Events.handRechargeClkAct), 
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);

        $(".fn-EnhanceExport").click(Events.enhanceExportAction);
	}
});
