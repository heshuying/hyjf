var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 启用Action
	statusAction : "statusAction",
	// 启用Action
	validPackageFormAction : "validPackageFormAction",
	// 下载XML
	downLoadDataAction:"downLoadDataAction",
	// 跳转到黑名单页面
	toheimingdan:"toheimingdanAction"
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
	// 查看,编辑
	infoAction : function(event) {
		var id = this.id.substring(10);
		$("#id").val(id);
		Page.submit(Action.infoAction);
	},
	// 打包
	topackage : function(event) {
		var id = this.id.substring(10);
		$("#id").val(id);
		$.post(Action.validPackageFormAction,
				{'id':id}, function(data) {
					if (data.success == false) {
						Page.alert("", data.msg);
					} else {
						$("#consumeId").val(data.consumeId);
//						Page.submit(Action.searchAction);
						Page.submit(webRoot+"/manager/borrow/consume/infoAction");
					}
				});
	},
	//下载达飞数据
	downloadData:function(event){
		$(".fn-DownloadData").attr("disabled","disabled");
		$.post(Action.downLoadDataAction,
				{}, function(data) {
					if (data.success == false) {
						Page.alert("", data.msg);
					} else {
						Page.confirm("下载成功", "下载成功", "success", {
							showCancelButton : false
						}, function() {
							Events.refreshClkAct();
						});
//						location.href = webRoot+"/manager/borrow/consume/infoAction?consumeId='"+id+"'&moveFlag='BORROW_HXF'";
					}
				});
	},
	//跳转到黑名单Action
	toheimingdan:function(event){
		Page.submit(Action.toheimingdan);
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
	doLayout : function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "请选择条件",
			allowClear: true,
			language: "zh-CN"
		}),
		// 日历选择器
		$('.datepicker').datepicker({
			autoclose : true,
			todayHighlight : true
		});
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		$(".fn-infoAction").click(Events.infoAction);
		$(".fn-topackage").click(Events.topackage);
		$(".fn-DownloadData").click(Events.downloadData);
		$(".fn-Heimingdan").click(Events.toheimingdan);
	}

});
