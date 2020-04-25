var disable = true;
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "init",
	// 审核
	verifyAction : "verify"
},
/* 事件动作处理 */
Events = {
//	// 添加按钮的单击动作事件
//	addClkAct : function() {
//		Page.submit(Action.infoAction);
//	},
	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		//window.location.reload();
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	//查看详情并审核
	detailClkAct : function(){
		Page.primaryKey.val($(this).data("id")),
		$("#mainForm").attr("target", "");
		Page.submit(Action.detailAction);
	},
	//提交审核
	verifyClkAct : function(){
		if(!disable){
			Page.alert("正在保存信息,请勿重复提交");
			return;
		}
		disable = false;
		var yijian = document.getElementById("yijian").value;
		if(yijian == null || yijian.trim()==""){
			Page.alert("请填写审批意见");
			return;
		}
		if(yijian.length>500){
			Page.alert("意见最多500字");
			return;
		}
		$("#form").attr("action", Action.verifyAction);
		$("#form").submit();
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	// 清空按钮的单击动作事件
	clearClkAct : function() {
		Events.clearDepClkAct();
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
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 详情按钮的单击事件绑定
		$(".fn-Info").click(Events.infoClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Submit").click(Events.verifyClkAct)
	}
});

document.onkeydown = function(event) {
	//e = event ? event : (window.event ? window.event : null);
	if( $("#searchable-panel").hasClass("active") && event.keyCode == 13){
		// 执行的方法
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	}
}

function changeTab(type){
	$("#i_detail").attr("src","gotoWkcdPage?wkcdId="+$("#i_wkcdId").val()+"&type="+type);
}


