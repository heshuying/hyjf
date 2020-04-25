var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "searchAction",
	// 手动冲正的Action
	infoAction : "infoAction"
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
		
		$("#seqNoSrch").val("");
		$("#userNameSrch").val("");
		$("#accountIdSrch").val("");
		$("#txTimeStartSrch").val("");
		$("#txTimeEndSrch").val("");
	},
	// 添加按钮的单击动作事件
//	addClkAct : function() {
//		Page.submit(Action.infoAction);
//	},	
	// 添加按钮的单击动作事件
	addClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 申请调账",
			width: "50%", height: 531,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		//Page.primaryKey.val(""),
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	autoMClkAct : function() {
		$.ajax({
			url : "autoM",
			type : "POST",
			async : true
		})
	},
//	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面布局
	doLayout: function() {
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
		var day31 = 31 * 24 * 60 * 60 * 1000;
		$("#start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#end-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day31);
	        if(endDate==null){
	        	 $('#end-date-time').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#end-date-time').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day31){
	        	 $('#end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#end-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#start-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day31);
	        if(startDate==null){
	        	$('#start-date-time').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#start-date-time').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day31){
	        	$('#start-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		$(".fn-AutoM").click(Events.autoMClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct)
	}
});
