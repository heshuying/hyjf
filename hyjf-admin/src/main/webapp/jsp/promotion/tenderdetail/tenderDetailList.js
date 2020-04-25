var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	searchAction : "search",
	// 导出的Action
	exportAction : "exportUserTenderDetail"
},
/* 事件动作处理 */
Events = {
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		if(!$('#regTimeStartSrch').val()){
			Page.alert("", "注册时间查询条件不能为空");
			return;
		}
		if(!$('#regTimeEndSrch').val()){
			Page.alert("", "注册时间查询条件不能为空");
			return;
		}
		
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	exportClkAct : function() {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function() {
			Page.coverLayer();
		}, 1);
		$("#paginator-page").val(1);
		Page.submit(Action.exportAction);
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#primaryKey"),
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
		$('#start-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#end-date-time').datepicker("getDate"), d
							&& $('#start-date-time')
									.datepicker("setEndDate", d);
				}), 
		$('#end-date-time').on(
				"change",
				function(evnet, d) {
					d = $('#start-date-time').datepicker("getDate"), 
					d && $('#end-date-time').datepicker("setStartDate", d);
		}),
		//限定注册时间结束条件的取值范围
		setRegTimeEndSrchScope();
		
		$('#regTimeStartSrch').on("change",function(evnet, d) {
			d = $('#regTimeStartSrch').datepicker("getDate");
			
			//等于1代表是2月份
			var days;
			if(d.getMonth()==0 || d.getMonth()==2 || d.getMonth()==4 || d.getMonth()==6
					|| d.getMonth()==7 || d.getMonth()==9 || d.getMonth()==11){
				//一月,三月,五月,七月,八月,十月,腊月
				days=31;
			}else if(d.getMonth()==1){
				days=28;
			}else{
				days=30;
			}
			
			var day30 = 1000*60*60*24*days;
			var afterDay = new Date(day30+d.getTime());
			$('#regTimeEndSrch').datepicker('setStartDate',d)
			$('#regTimeEndSrch').datepicker('setEndDate',afterDay)
			$('#regTimeEndSrch').datepicker('setDate',afterDay)
		})
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
	}
});

//限定注册时间结束条件的取值范围
function setRegTimeEndSrchScope(){
	var d = $('#regTimeStartSrch').datepicker("getDate");
	//表中没有记录时 return
	if(d==null){
		return;
	}
	
	//等于1代表是2月份
	var days;
	if(d.getMonth()==0 || d.getMonth()==2 || d.getMonth()==4 || d.getMonth()==6
			|| d.getMonth()==7 || d.getMonth()==9 || d.getMonth()==11){
		//一月,三月,五月,七月,八月,十月,腊月
		days=31;
	}else if(d.getMonth()==1){
		days=28;
	}else{
		days=30;
	}
	
	var day30 = 1000*60*60*24*days;
	var afterDay = new Date(day30+d.getTime());
	$('#regTimeEndSrch').datepicker('setStartDate',d)
	$('#regTimeEndSrch').datepicker('setEndDate',afterDay)
	$('#regTimeEndSrch').datepicker('setDate',afterDay)
}

