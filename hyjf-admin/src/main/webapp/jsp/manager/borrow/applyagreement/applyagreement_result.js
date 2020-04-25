var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "infoAction",
	// 详细画面的Action
	updateAction : "updateAction",
	// 查找的Action
	searchAction : "searchAction",
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {	
		$(".listCheck").prop("checked", this.checked);
		if($("input[name='listcheckbox']").is(':checked')){//是否有选中
			$("#add").css("display", "block");//显示
			$("#searchable-panel-clone").css("display", "none");//隐藏
		}else{
			$("#add").css("display", "none");//显示
			$("#searchable-panel-clone").css("display", "block");//隐藏
		}
	},
	// checkbox的change动作事件
	selectListCheck : function() {
		if($("input[name='listcheckbox']").is(':checked')){//是否有选中
			$("#add").css("display", "block");//显示
			$("#searchable-panel-clone").css("display", "none");//隐藏
		}else{
			$("#add").css("display", "none");//显示
			$("#searchable-panel-clone").css("display", "block");//隐藏
		}
		
	},
	addClkAct: function() {
		obj = document.getElementsByName("listcheckbox");
	    check_val = [];
	    for(k in obj){
	        if(obj[k].checked)
	            check_val.push(obj[k].value);
	    }
	    Page.primaryKey.val(JSON.stringify(check_val));
	    Page.submit(Action.updateAction);
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.infoAction);
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
		Page.submit(Action.searchAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout : function() {
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
		$("#checkall").change(Events.selectAllAct),
		$(".listCheck").change(Events.selectListCheck),
		// 未交保证金按钮的单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct),
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		
		$(".fn-Add").click(Events.addClkAct);
		
		

	},
	ready : function() {

	},
	// 画面初始化
	initialize : function() {

	}
}),

Page.initialize();
