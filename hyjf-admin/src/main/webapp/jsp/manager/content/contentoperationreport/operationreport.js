var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "searchAction",
    // 导出的Action
    exportExcelAction : "exportExcelAction",
	// 预览画面的Action
	infoAction : "infoAction",
	// 修改画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 发布的Action
	publishAction : "publishAction"
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
    // 导出按钮的单击事件绑定
    exportClkAct : function(selection, cds) {
        var startTime = $("#start-date-time").val();
        var endTime = $("#end-date-time").val();
        if( startTime == undefined || startTime == ""  || endTime == undefined || endTime == ""){
            Page.confirm("","请选择导出数据的起止时间","error",{showCancelButton: false}, function(){});
            return false;
        }
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
	// 编辑按钮的单击动作事件
	modifyClkAct: function(event) {
		var id=$(this).data("id");
		Page.primaryKey.val(id);
		Page.submit(Action.infoAction);
	},
	// 删除按钮的单击动作事件
	deletesClkAct : function(selection, cds) {
		// 取得选择行
		selection = $(".listCheck:checked");
		if (!selection[0]) {
			Page.alert("", "请选择要删除的记录！", "warning");
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
	// 发布按钮的单击动作事件
	publishClkAct: function(event) {
		var id ="";
		var isRelease ="";
		if(event) {
            id = $(this).data("id");
            isRelease = $(this).data("release");
		}
        var param = {};
        param.id = id;
        param.isRelease = isRelease;
        Page.coverLayer("正在处理,请稍候...");
        $.ajax({
            url : Action.publishAction,
            type : "POST",
            async : true,
            data : JSON.stringify(param),
            dataType: "json",
            contentType : "application/json",
            success : function(data) {
                Page.coverLayer();
                if (data.status == "success") {
                    Page.confirm("操作成功",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
                } else {
                    Page.notice(data.result, "","error");
                }

            },
            error : function(err) {
                Page.coverLayer();
                Page.notice("提交出现错误,请重新操作!", "","error");
            }
        });
	},
    // 再次发布按钮的单击动作事件
    publishAgainClkAct: function(event) {
        var id ="";
        var isRelease ="";
        if(event) {
            id = $(this).data("id");
            isRelease = $(this).data("release");
        }
        var param = {};
        param.id = id;
        param.isRelease = isRelease;
        Page.coverLayer("正在处理,请稍候...");
        Page.primaryKey.val(id);
        Page.pageStatus.val(isRelease);
        Page.submit(Action.infoAction);

    },
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板查询按钮的单击事件
	searchClkAct : function(event) {
        var id=$(this).data("id");
        Page.primaryKey.val(id);
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
	primaryKey : $("#ids"),
    pageStatus:$("#pageStatus"),
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
		$('.datepicker').datepicker({
			autoclose: true,
			todayHighlight: true
		});
	},
	// 初始化画面事件处理   previewClkAct
	initEvents : function() {
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 预览按钮的单击事件绑定
		//$(".fn-View").click(Events.previewClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		// 删除按钮的单击事件绑定
	//	$(".fn-Deletes").click(Events.deletesClkAct),
		$(".fn-Delete").click(Events.deleteClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
        //发布
        $(".fn-Publish").click(Events.publishClkAct );

        $(".fn-again-Publish").click(Events.publishAgainClkAct );
        $(".fn-Export").click(Events.exportClkAct);


	}
});

