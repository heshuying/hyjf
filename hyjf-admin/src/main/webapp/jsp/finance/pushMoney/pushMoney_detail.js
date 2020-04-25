var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 发提成Action
	confirmPushMoneyAction : "confirmPushMoneyAction",
	// 导出execlAction
	exportExcelAction : "exportPushMoneyDetailExcelAction",
	// 查询的Action
	searchAction : "searchDetailAction",
},
/* 事件动作处理 */
Events = {
	// 发提成按钮的单击事件
	confirmPushMoneyClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("id"))
			//alert($(this).data("id"));
		}
		Page.confirm("", "确定要执行本次发提成操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.ids = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.confirmPushMoneyAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
						    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						} else {
							Page.notice(data.result, "","error");
//							Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});						
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.notice("发提成出现错误,请重新操作!", "","error");
					}
				});
			};

		})
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		$("#mainForm").attr("target", "");
		Page.submit(Action.searchAction);
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
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
	// 返回按钮的单击事件绑定
	backClkAct : function(selection, cds) {
		window.location.href="pushMoney_list";
	},
	// 刷新部门树
	refreshTreeAct : function() {
		var param = {};
		param.depIds = $("#combotree_field_hidden").val() || "";
		$.ajax({
			url : "getCrmDepartmentList",
			type : "POST",
			async : true,
			data : JSON.stringify(param),
			dataType: "json",
			contentType : "application/json",
			success : function(result) {
				$('#combotree').jstree({
					"core" : {
						"themes" : {
							"responsive" : false
						},
						'data' : result
					},
					"plugins" : ["search", "checkbox", "types", "changed"],
					"checkbox" : {
						"keep_selected_style" : false
					},
					"types" : {
						"default" : {
							"icon" : "fa fa-folder text-primary fa-lg"
						},
						"file" : {
							"icon" : "fa fa-file text-primary fa-lg"
						}
					}
				}).on("changed.jstree", function (e, data) {
					if(data.action !== "model") {
						var nodes = data.instance._model.data,
							txt = [], val = [];
						$.each(data.selected, function(item) {
							item = nodes[this];
							txt.unshift(item.text.replace(/&amp;/g, "&"));
							val.push(item.id);
						});
						$("#combotree-field").val(txt.join());
						$("#combotree_field_hidden").val(val.join());
						$(".fn-ClearDep").show();
					}
				}).parent().perfectScrollbar().mousemove(function() {
					$(this).perfectScrollbar('update')
				});
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});

		var to = false;
		$('#combotree_search').keyup(function() {
			if (to) {
				clearTimeout(to);
			}
			to = setTimeout(function() {
				var v = $('#combotree_search').val();
				$('#combotree').jstree(true).search(v);
			}, 250);
		}).parent().click(false);
	},
	// 清空按钮的单击动作事件
	clearClkAct : function() {
		Events.clearDepClkAct();
	},
	// 清空部门按钮的单击动作事件
	clearDepClkAct : function() {
		$('#combotree').jstree("uncheck_all").jstree("close_all");
		$(".fn-ClearDep").hide();
		return false;
	}

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout: function() {
		// 条件下拉框
		$(".form-select2").select2({
			width: 268,
			placeholder: "全部",
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

		// 刷新树
		Events.refreshTreeAct(),
		// 清空部门选择
		Events.clearDepClkAct();
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 发提成按钮的单击事件
		$(".fn-Confirm").click(Events.confirmPushMoneyClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		// 返回事件
		$(".fn-Back").click(Events.backClkAct),
		// 清空按钮的单击事件绑定
		$(".fn-ClearForm").click(Events.clearClkAct),
		// 清空部门按钮的单击事件绑定
		$(".fn-ClearDep").click(Events.clearDepClkAct);
	}
});
