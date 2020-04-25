var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 插入的Action
	infoAction : "infoAction",
	// 插入的Action
	insertAction : "insertAction",
	// 更新的Action
	updateAction : "updateAction",
	// 删除的Action
	deleteAction : "deleteAction",
	// 设置权限的Action
	settingAction : "settingAction"
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		if (Page.currNode != null && Page.currNode.li_attr != null) {
			$("#menuPuuid").val(Page.currNode.li_attr.menuUuid || 0);
			$("#menuPNameSpan").text(Page.currNode.li_attr.menuName);
		} else {
			$("#menuPuuid").val(0);
			$("#menuPNameSpan").text("");
		}
		$("#menuName").val("");
        $("#menuCtrl").val("");
        $("#menuIcon").val("");
        $("#menuSort").val("");
        $("#menuUrl").val("");
        $("#menuHide").val("");
        $("#menuTip").val("");
        $("#mainForm input").attr("disabled", false);
        $("#mainForm .btn").show();
        $("#mainForm").prop("action", Action.insertAction);
        $("#title").text("添加菜单");
        $("#right-panel").show();
	},
	// 编辑按钮的单击动作事件
	modifysClkAct : function(selection) {
		if (Page.currNode != null && Page.currNode.li_attr != null) {
			console.info(Page.currNode.li_attr);
			$("#menuPuuid").val(Page.currNode.li_attr.menuPuuid);
			$("#menuUuid").val(Page.currNode.li_attr.menuUuid);
			$("#menuPNameSpan").text(Page.currNode.li_attr.menuPName);
			$("#menuName").val(Page.currNode.li_attr.menuName);
	        $("#menuCtrl").val(Page.currNode.li_attr.menuCtrl);
	        $("#menuIcon").val(Page.currNode.li_attr.menuIcon);
	        $("#menuSort").val(Page.currNode.li_attr.menuSort);
	        $("#menuUrl").val(Page.currNode.li_attr.menuUrl);
	        $("#menuHide").val(Page.currNode.li_attr.menuHide);
	        $("#menuTip").val(Page.currNode.li_attr.menuTip);
		} else {
			Page.alert("", "请选择要修改的菜单！", "warning");
			return;
		}

        $("#mainForm input").attr("disabled", false);
        $("#mainForm .btn").show();
        $("#mainForm").prop("action", Action.updateAction);
        $("#title").text("修改菜单");
        $("#right-panel").show();
	},
	// 删除按钮的单击动作事件
	deletesClkAct : function(selection, cds) {
		// 取得选择行
		if (Page.currNode != null && Page.currNode.id != "") {
			Page.confirm("", "确定要删除当前的菜单及其子菜单吗？", {closeOnConfirm: false}, function(isConfirm) {
				if(isConfirm) {
					var ids = [];
					ids.push(Page.currNode.id);
					for (var i = 0;i< Page.currNode.children_d.length;i++) {
					    ids.push(Page.currNode.children_d[i]);
					}
					$("#menuPuuid").val(Page.currNode.li_attr.menuPuuid);
					$("#ids").val(JSON.stringify(ids));
					$("#mainForm").prop("action", Action.deleteAction);
					Page.submit();
				}
			})
		} else {
			Page.alert("", "请选择要删除的菜单！", "warning");
		}// Endif
	},
	// 刷新按钮的单击动作事件
	refreshClkAct : function() {
		window.location.reload();
	},
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的菜单信息吗？", {closeOnConfirm: false}, function(isConfirm) {
				if(isConfirm) {
				    Page.submit();
				}
			})
		}
	},
	// 刷新菜单树
	refreshTreeAct : function() {
		var param = {};
		param.selectedNode = $("#selectedNode").val() || "";
		console.info(JSON.stringify(param));
		$.ajax({
			url : Action.infoAction,
			type : "POST",
			async : true,
			data : JSON.stringify(param),
			dataType: "json",
			contentType : "application/json",
			success : function(result) {
				$('#tree_menu').jstree({
					'plugins' : ["wholerow", "types"],
					'core' : {
						"themes" : {
							"responsive" : false
						},
						"check_callback" : true,
						'data' : result
					},
					"types" : {
						"default" : {
							"icon" : "fa fa-folder text-primary fa-lg"
						},
						"file" : {
							"icon" : "fa fa-file text-primary fa-lg"
						},
						"lock" : {
							"icon" : "fa fa-lock text-primary fa-lg"
						},
						"unlock" : {
							"icon" : "fa fa-unlock text-primary fa-lg"
						}
					},

				}).bind("changed.jstree", function (node, data) {
					// 保存选中的子节点
					var thisNode = data.instance.get_node(data.selected);
	                Page.currNode = thisNode;
	                $("#menuPNameSpan").text(thisNode.li_attr.menuPName);
		            // 没有错误时,显示选中信息
		     		if ($("#formHasError").val() == "") {
		                $("#menuName").val(thisNode.li_attr.menuName);
		                $("#menuCtrl").val(thisNode.li_attr.menuCtrl);
		                $("#menuIcon").val(thisNode.li_attr.menuIcon);
		                $("#menuSort").val(thisNode.li_attr.menuSort);
		                $("#menuUrl").val(thisNode.li_attr.menuUrl);
		                thisNode.li_attr.menuHide == "0" ? $("#menuHideOff").prop("checked", true) : $("#menuHideOn").prop("checked", true);
		                $("#menuTip").val(thisNode.li_attr.menuTip);
		                $("#mainForm input:not([type='hidden'])").attr("disabled", "disabled");
		                $("#mainForm .btn").hide();
		                $("#title").text("查看菜单");
		            }

	                $("#right-panel").show();
	            });
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});
	},
	// 设置权限按钮的单击动作事件
	settingClkAct : function(selection) {
		if (Page.currNode != null && Page.currNode.li_attr != null) {
			$("#menuUuid").val(Page.currNode.li_attr.menuUuid);
		} else {
			Page.alert("", "请选择要设置权限的菜单！", "warning");
			return;
		}

        $.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 设置<strong class='text-blue'>"+Page.currNode.li_attr.menuName+"</strong>的权限",
			width: "50%", height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.settingAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 当前结点
	currNode: {},
	// 初始化画面
	doLayout: function() {
		// 有错误时打开右侧面板
		if ($("#formHasError").val() == "") {
			$("#right-panel").hide();
		}
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modifys").click(Events.modifysClkAct),
		// 删除按钮的单击事件绑定
		$(".fn-Deletes").click(Events.deletesClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct),
		// 设置权限按钮单击事件绑定
		$(".fn-Setting").click(Events.settingClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 刷新树
		Events.refreshTreeAct();
	}

});
Page.initialize();