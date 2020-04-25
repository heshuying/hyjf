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
	// 联动下拉事件
	assetTypeAction : "assetTypeAction"
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		Page.submit(Action.infoAction);
	},
	// 编辑按钮的单击动作事件
	modifyClkAct: function(event) {
		Page.primaryKey.val($(this).data("id"));
		Page.submit(Action.infoAction);
	},
	//启用statusAction
	statusAction : function(event) {
			if(event) {
				console.info($(this).data());
				Page.primaryKey.val($(this).data("id"));
			}
		Page.confirm("", "确定要执行本次操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.statusAction);
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
	},
	instCodeSrchOnchangeAct : function() {
		var instCode = $("#instCodeSrch").val();
		$("#assetTypeSrch").empty();
		if (instCode == "") {
			return;
		}
		$.ajax({
			url : Action.assetTypeAction,
			type : "POST",
			async : true,
			dataType : "json",
			data :  {
				instCode : instCode
			},
			success : function(data) {
				$("#assetTypeSrch").select2({
					data: data,
				  	width : 268,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
				$("#assetTypeSrch").val("").change();
				Events.assetTypeChange();
			},
			error : function(err) {
				Page.alert("","没有对应的产品类型!");
			}
		});
		
	},
	//项目类型
	projectTypeOnchangeAct : function() {
		//产品类型联动控制处理
		Events.assetTypeChange();
	},
	//产品类型联动控制处理
	assetTypeChange : function() {
		if ($("#instCodeSrch").val() == INST_CODE_HYJF){
			$("#assetTypeSrch").attr("readonly",true);
			var cd = $("#projectTypeSrch").find("option:selected").data("cd");
			$("#assetTypeSrch").val(cd).trigger("change");
		}else{
			$("#assetTypeSrch").removeAttr("readonly");
			/*$("#assetType").val("").change();*/
		}
	},
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

        $('#start-date-time1').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#end-date-time1').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });

        $("#start-date-time1").on("changeDate", function(ev) {
            var now = new Date();
            var selectedDate = new Date(ev.date.valueOf());
            var endDate = $('#end-date-time1').datepicker("getDate");
            var finalEndDate = +selectedDate + day30 >= +now ? now : new Date(+selectedDate + day30);
            $('#end-date-time1').datepicker("setStartDate", selectedDate);
            $('#end-date-time1').datepicker("setEndDate", finalEndDate);
            //如果end值范围超过30天，设置end最大结束时间
            if (endDate != null && (+endDate - selectedDate >= day30)) {
                $('#end-date-time1').datepicker("setDate", finalEndDate);
            }
        });
        if($("#start-date-time1").val() != ''){
            $('#start-date-time1').datepicker("setDate", $("#start-date-time1").val());
        }


	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Deletes").click(Events.deletesClkAct),
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		//更改状态
		$(".fn-UpdateBy").click(Events.statusAction );
		// 资产来源选择事件绑定
		$("#instCodeSrch").change(Events.instCodeSrchOnchangeAct);
		$("#projectTypeSrch").change(Events.projectTypeOnchangeAct);
	}
});
