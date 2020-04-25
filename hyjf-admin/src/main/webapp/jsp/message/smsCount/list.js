var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查找的Action
	searchAction : "init",
    // 导出的Action
    exportAction : "exportSms"
},
/* 事件动作处理 */
Events = {

	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#paginator-page").val(1);
		Page.submit(Action.searchAction);
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 边界面板按钮的单击动作事件
	searchPanelClkAct : function() {
		$("#searchPanel").modal()
	},
	// 重置表单
	resetFromClkAct: function() {
		$(".form-select2").val("").change();
	},
    //导出Excel
    exportClkAct : function() {
        var startTime = $("#post_time_begin").val();
        var endTime = $("#post_time_end").val();
        if(startTime == "" || endTime == ""){
            Page.confirm("","请选择导出数据的起止时间","error",{showCancelButton: false}, function(){});
            return false;
        }
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function() {
            Page.coverLayer()
        }, 1);
        $("#paginator-page").val(1);
        Page.submit(Action.exportAction);
    },
    // 刷新部门树
    refreshTreeAct : function() {
        var param = {};
        param.ids = $("#combotree_field_hidden").val() || "";
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
                        $.each(data.selected, function(item, parent) {
                            item = nodes[this];
                            //parent = nodes[item.parent];
                            //parent && (parent = parent.text);
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
	primaryKey : $("#borrowNid"),
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
        // 刷新树
        Events.refreshTreeAct();
        // 清空部门选择
        Events.clearDepClkAct();
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	},	
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct);
        // 导出按钮的单击事件绑定
        $(".fn-Export").click(Events.exportClkAct),
		// 重置按钮的单击事件绑定
		$(".fn-Reset").click(Events.resetFromClkAct);
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 清空部门按钮的单击事件绑定
		$(".fn-ClearDep").click(Events.clearDepClkAct),
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct);

	}
}),


Page.initialize();
