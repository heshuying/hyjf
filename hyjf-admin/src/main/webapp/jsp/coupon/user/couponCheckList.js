var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "init",
	// 详细画面的Action
    deleteAction : "deleteAction",
	// 手动发放
	distributeViewAction : "distributeViewAction",
	// 手动批量发放
	impDistributeViewAction : "impDistributeViewAction",
	// 审核的Action
	auditAction : "auditInitAction",
	//下载的action
    downloadAction: "downloadAction",


},

/* 事件动作处理 */
Events = {
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

    //全选按钮
    selectAllAct : function(){
        $(".listCheck").prop("checked", this.checked);

    },
	// 删除按钮的单击动作事件
    deleteClkAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("id"));
        }
        Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
            isConfirm && Page.submit(Action.deleteAction);
        })
	},
	//批量删除数据
    batchDeleteClkAct : function(){
        var id="";
        if($('input:checkbox[name=id]:checked').get(0)!=null){
            $('input:checkbox[name=id]:checked').each(function(i){
                id+=$(this).val()+",";
            });
            Page.primaryKey.val(id);
            Page.confirm("", "确定要执行本次删除操作吗？", function(isConfirm) {
                isConfirm && Page.submit(Action.deleteAction);
            })
        }else{
            Page.alert("","请选择一条数据!");
        }
    },
    downLoadClkAct: function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("idx"));
		}
        var a=$(this).data("idx");
		Page.confirm("", "确定下载吗？", function(isConfirm) {
			isConfirm &&Page.submit(Action.downloadAction);
            setTimeout("window.location.reload()", 200);
		})

	},

	// 手动批量发放的单击动作事件
	importDistributeClkAct : function(event) {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 批量发放优惠券",
			width : 450,
			height : 620,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.impDistributeViewAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
                window.location.reload();
			}
		})
	},
	// 审核按钮的单击动作事件
	auditClkAct: function(event) {
		if(event) {
            var split=$(this).data("provides").split(',');
			Page.primaryKey2.val(split[0]);
            Page.primaryKey.val(split[1]);
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 优惠券审核",
			width : "30%",
			height : "65%",
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.auditAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
                window.location.reload();
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
		if(startTime == "" && esTime == ""){
			Page.confirm("","时间检索条件起始时间不能为空","error",{showCancelButton: false}, function(){});
			return false;
		}
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
	    });
	    
	    
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
        $(".fn-Import").click(Events.importDistributeClkAct),
        //批量删除
        $(".fn-batchDelete").click(Events.batchDeleteClkAct),
		// 审核按钮的单击事件绑定
		$(".fn-check").click(Events.auditClkAct),
        // 删除按钮的单击事件绑定
        $(".fn-Delete").click(Events.deleteClkAct),
        // 下载按钮的单击事件绑定
        $(".fn-Download").click(Events.downLoadClkAct),
		//全选按钮
        $("#checkall").click(Events.selectAllAct);
	}
});
