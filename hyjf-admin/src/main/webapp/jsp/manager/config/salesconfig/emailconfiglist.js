var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "init",
	//添加转跳
    addAction:"addAction",
	//查看转跳
    toInfoAction:"infoAction",
	//修改转跳
	toUpdateAction:"jumpUpdateAction",
	//禁用
    forbiddenAction:"forbiddenAction",
	//启用
    startAction:"startAction"
},

/* 事件动作处理 */
Events = {
	//添加
    addClkAct:function(event) {
        Page.submit(Action.addAction);
	},

    findClkAct:function(){
        Page.primaryKey.val($(this).data("id"));
        Page.submit(Action.toInfoAction);
	},
    updateClkAct: function () {
        Page.primaryKey.val($(this).data("id"));
        Page.submit(Action.toUpdateAction);
    },

    forbiddenClkAct: function () {
        Page.primaryKey.val($(this).data("id"));
        Page.confirm("", "确定要禁用本条记录吗？", function(isConfirm) {
            isConfirm && Page.submit(Action.forbiddenAction);
        })

    },
    startClkAct: function () {
        Page.primaryKey.val($(this).data("id"));
        Page.confirm("", "确定要启用本条记录吗？", function(isConfirm) {
            isConfirm && Page.submit(Action.startAction);
        })

    },

	// 刷新按钮的单击动作事件
	infoClkAct : function(event) {
		Page.primaryKey.val($(this).data("id"));
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 优惠券详情",
			width : "50%",
			height : "75%",
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.infoAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},

	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
		var startTime = $("#start-date-time").val();
		var esTime = $("#en-start-date-time").val();

		/*if(startTime == "" && esTime == ""){
			Page.confirm("","时间检索条件起始时间不能为空","error",{showCancelButton: false}, function(){});
			return false;
		}*/
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
	primaryKey2 :  $("#filePath"),
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
        $('#en-start-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
        $('#en-end-date-time').datepicker({
            autoclose: true,
            todayHighlight: true,
            endDate:new Date()
        });
/*
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
		// 日历选择器
		$('#en-start-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
		$('#en-end-date-time').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	    
		$("#en-start-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var endDate = $('#en-end-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate + day30);
	        if(endDate==null){
	        	 $('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }else if (+selectedDate > +endDate) {
	        	 $('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }else if(+endDate - selectedDate > day30){
	        	$('#en-end-date-time').datepicker("setDate", finalEndDate);
	        }
	    });
	    $("#en-end-date-time").on("changeDate", function(ev) {
	        var selectedDate = new Date(ev.date.valueOf());
	        var startDate = $('#en-start-date-time').datepicker("getDate");
	        var finalEndDate = new Date(+selectedDate -day30);
	        if(startDate==null){
	        	$('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }else if (+startDate > + selectedDate) {
	            $('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }else if(+selectedDate - startDate > day30){
	        	$('#en-start-date-time').datepicker("setDate", finalEndDate);
	        }
	    });*/
	    
	    
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
        // 手动批量发放的单击事件绑定
        $(".fn-Add").click(Events.addClkAct),
		//查看
        $(".fn-find").click(Events.findClkAct),
		//点击修改
		$(".fn-update").click(Events.updateClkAct),
        // 删除按钮的单击事件绑定
        $(".fn-forbidden").click(Events.forbiddenClkAct),

        $(".fn-start").click(Events.startClkAct)



	}
});
