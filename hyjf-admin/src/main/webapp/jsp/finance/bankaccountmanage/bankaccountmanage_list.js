var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 导出execlAction
	exportExcelAction : "exportAccountsExcel",

    enhanceExportAction:"enhanceExportAction",

	// 查询的Action
	searchAction : "accountmanage_list",
	// 对账按钮的Action
	checkAction : "accountCheckAction",
	// 详细画面的Action
	infoAction : "accountdetail_list?fid=1112",
	// 详细画面的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 更新的Action
	updateBalanceAction : "updateBalanceAction"
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
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
	// ‘查看’按钮的单击事件
	selectDetailAct : function(event) {
		$("#mainForm").attr("target", "");
		// alert($(this).data("userId")+"==="+$(this).data("userid"));
		// alert($(this).data("userName")+"==="+$(this).data("username"));
		Page.primaryKey.val($(this).data("userid"));
		Page.usernameKey.val($(this).data("username"));
		Page.submit(Action.infoAction);
	},
	// ‘对账’按钮的单击事件
	accountCheckAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("userid"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 银行对账",
			width: "50%", height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.checkAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 导出按钮的单击事件绑定
	exportClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "_blank");
		Page.notice("正在处理下载数据,请稍候...");
		setTimeout(function(){Page.coverLayer()},1);
		Page.submit(Action.exportExcelAction);
	},
    enhanceExportAction : function(selection, cds) {
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function(){Page.coverLayer()},1);
        Page.submit(Action.enhanceExportAction);
    },
	// 更新按钮的单击事件
	updateBalanceClkAct : function(event) {
		if(event) {
			Page.primaryKey.val($(this).data("userid"))
		}
		Page.confirm("", "确定要执行本次更新操作吗？", function(isConfirm) {
			if (isConfirm) {
				var param = {};
				param.userId = Page.primaryKey.val();
				Page.coverLayer("正在处理,请稍候...");
				$.ajax({
					url : Action.updateBalanceAction,
					type : "POST",
					async : true,
					data : JSON.stringify(param),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						Page.primaryKey.val("");
						if (data.status == "success") {
						    Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.refreshClkAct()});
						} else {
							Page.confirm("",data.result,"error",{showCancelButton: false}, function(){Events.refreshClkAct()});
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.primaryKey.val("");
						Page.notice("更新发生错误,请重新操作!", "","error");
					}
				});
			};

		})
	},
	// 刷新部门树
	refreshTreeAct : function() {
		var param = {};
		param.userId = $("#combotree_field_hidden").val() || "";
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
	primaryKey : $("#userId"),
	usernameKey: $("#username"),
	
	// 画面布局
	doLayout: function() {
		// 刷新树
		Events.refreshTreeAct();
		// 清空部门选择
		Events.clearDepClkAct();
	},
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll
		$("#checkall").change(Events.selectAllAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 边界面板查询按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),

		$(".fn-EnhanceExport").click(Events.enhanceExportAction),

		// ‘查看’按钮的单击事件
		$(".fn-info").click(Events.selectDetailAct),
		//'对账'按钮的单击事件
		$(".fn-check").click(Events.accountCheckAct),
		// 更新按钮的单击事件
		$(".fn-UpdateBalance").click(Events.updateBalanceClkAct),
		// 清空按钮的单击事件绑定
		$(".fn-ClearForm").click(Events.clearClkAct),
		// 清空部门按钮的单击事件绑定
		$(".fn-ClearDep").click(Events.clearDepClkAct);;
	}
});
