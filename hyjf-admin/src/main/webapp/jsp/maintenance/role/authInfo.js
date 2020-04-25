var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		Page.confirm("", "确定要保存当前的权限信息吗？", {closeOnConfirm: false}, function(isConfirm) {
			if(isConfirm) {
				var role = {};
			    var perms = [];
			    if (Page.selectedNode && Page.selectedNode.selected) {
			    	for(i = 0, j = Page.selectedNode.selected.length; i < j; i++) {
	                    var node =  Page.selectedNode.instance.get_node(Page.selectedNode.selected[i]);
	                    var perm={};
				    	var menuUuid = node.li_attr.menuUuid;
				    	var permissionUuid = node.li_attr.permissionUuid;
				    	if(permissionUuid != null && permissionUuid != "") {
				    		perm.roleId = Page.roleId.val();
				    		perm.menuUuid = menuUuid;
				    		perm.permissionUuid = permissionUuid;
				    		perms.push(perm);
				    	}
	                }
			    }
			    role.roleId = Page.roleId.val();
			    role.permList = perms;

			    Page.coverLayer("正在操作数据，请稍候...");
			    $.ajax({
					url : "modifyPermissionAction" ,
					type : "POST",
					async : true,
					data : JSON.stringify(role),
					dataType: "json",
					contentType : "application/json",
					success : function(data) {
						Page.coverLayer();
						if (data.status == "success") {
						    setTimeout(function(){Page.confirm("",data.result, "success",{showCancelButton: false}, function(){parent.Events.refreshClkAct()})},100);
						} else {
							setTimeout(function(){Page.confirm("",data.result,"error",{showCancelButton: false}, function(){parent.Events.refreshClkAct()})},100);
						}
					},
					error : function(err) {
						Page.coverLayer();
						Page.confirm("","操作失败!","error",{showCancelButton: false}, function(){parent.Events.refreshClkAct()});
					}
				});
			}
		})
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 刷新菜单树
	refreshTreeAct : function() {
		$.ajax({
			url : "menuInfoAction/" + Page.roleId.val(),
			type : "POST",
			async : true,
			data : "",
			dataType: "json",
			contentType : "application/json",
			success : function(result) {
				$('#tree_menu').jstree({
					'plugins' : ["wholerow", "checkbox",  "types"],
					"checkbox" : {
				      "keep_selected_style" : false,
				    },
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
	                Page.selectedNode = data;
	            });
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});
	}

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	roleId : $("#roleId"),
	// 选中的权限
	selectedNode:{},
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
	},
	// 画面初始化
	initialize : function() {
		// 刷新树
		Events.refreshTreeAct();
	}
}),

Page.initialize();
