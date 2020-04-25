var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoConfigAction",/*infoAction*/
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 计划配置
	tenderInfoAction:"planConfig",
	// 修改
	modifyAction:"modifyAction",
	// 换绑
	changeAction:"changeAction",
	// 导出Action
	exportExcelAction:"exportPlanConfigExcel",
	// 
	statusAction : "labelStatusAction",
	// 查询提示信息
	reportAction : "reportAction"
},
/* 事件动作处理 */
Events = {
		
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		/*var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的应还款起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
			return false;
		}*/
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},	
	
	// 计划配置单击事件
	tenderInfoClkAct : function(event) {
		$("#planNidSrch").val($(this).data("debtplannid"));
		Page.submit(Action.tenderInfoAction);
	},
	
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 添加配置",
			width: 550, height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.primaryKey.val(""),
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	
	// 编辑按钮的单击动作事件
	modifyClkAct: function(event) {
		if(event) {
			
			$('#labelId').val($(this).data("labelid"));
			$('#planId').val($(this).data("id"));
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 修改配置",
			width: 650, height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.modifyAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	
	// 换绑按钮的单击动作事件
	changeClkAct: function(event) {
		if(event) {
			$('#labelId').val($(this).data("labelid"));
			$('#planId').val($(this).data("id"));
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 修改配置",
			width: 650, height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.changeAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
				Events.refreshClkAct();
			}
			
		})
//		if(event){
//		 Events.refreshClkAct();
//			}
		},
	
	// 删除按钮的单击动作事件
	deletesClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("", "请选择要删除的角色！", "warning");
		} else {
			cds = [],
			selection.each(function() {
				cds.push(this.value);
			}),
			Page.primaryKey.val(JSON.stringify(cds));
			Events.deleteClkAct()
		}// Endif
	},
	deleteClkAct : function(event) {
		if(event) {
			Page.primaryKey.val(JSON.stringify([$(this).data("id")]))
		}
		Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.deleteAction);
		})
	},
	
	//启用statusAction
	statusClkAct : function(event) {
		$('#planId').val($(this).data("id"));
		var id = $(this).data("id");
		// 是否启用 0停用  1启用
		var status = $(this).data("status"); 
		var labelName = $(this).data("label-name"); 
			if(event) {
				Page.primaryKey.val(id)
			}
			if(status == '1'){
				Events.popConfirm("确定要执行本次操作吗？",true);
			}else{
				// 查询弹窗提示信息
				$.ajax({
					url: Action.reportAction,
					type:"POST",
					text : "",
					async : true,
					dataType : "json",
					data:{
						id: id,
						labelName: labelName,
						planName: $("#planName").val()
					},
					success:function(data){
						var message= data.info || "确定要执行本次操作吗？";
						var status = data.status; 
						Events.popConfirm(message,true);
					}
				})
			}
	},
	
	popConfirm: function(message,allow){
		message = message || "确定要执行本次操作吗？";
		Page.confirm("", message , function(isConfirm) {
			isConfirm && allow &&  Page.submit(Action.statusAction);
		})
	},
	
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
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
		
	    $("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        $('#end-date-time').datepicker("setStartDate", selectedDate);
	        $('#end-date-time').datepicker("setDate", selectedDate);
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		
		
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		
		// 换绑按钮的单击事件绑定
		$(".fn-Change").click(Events.changeClkAct),
		$(".fn-changeClkAct").click(Events.changeClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Deletes").click(Events.deletesClkAct),
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct);
		// 计划配置
		$(".fn-TenderInfo").click(Events.tenderInfoClkAct);
		
		
		//更改状态
		$(".fn-UpdateBy").click(Events.statusClkAct );
	}
});
