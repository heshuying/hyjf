var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	infoAction : "userdetail",
	// 查询用户的详情Action
	modifyAction : "inituserupdate",
	// 更新用户属性
	//updateUserParamAction : "updateUserParam",
	// 更新全部用户属性
	//updateAllUserParamAction : "updateAllUserParam",
	// 修改推荐人
	modifyReAction : "initmodifyre",
	//修改身份证
	modifyICAction : "initmodifyidcard",
	// 页面查询
	searchAction : "userslist",
	// 导出的Action
	exportAction : "exportusers",
    // 导出的Action (具有组织机构查看权限的导出)
    enhanceExportAction : "enhanceExportusers",
	// 更新出借表的推荐人信息数据
	updateInviteInfoAction : "updateInviteInfoAction",
	//补录企业用户信息
	companyClkAction:"insertCompanyInfo",
	//同步用户角色
    syncRoleAction:"syncRoleAction",

	companyClkAction:"insertCompanyInfo",

    modifyMoileAction:"modifyMoileAction",

    updateUserInfos:"initUpdateUserInfos"
},
/* 事件动作处理 */
Events = {

	// 重置表单
	resetFromClkAct : function() {
		$(".form-select2").val("").trigger('change');
	},
//	// 更新按钮的单击动作事件
//	updateClkAct : function(event) {
//		Page.primaryKey.val($(this).data("userid"));
//		Page.confirm("", "确定要更新该用户的用户属性么？", "info", {
//			closeOnConfirm : false,
//			confirmButtonColor : "#DD6B55",
//			confirmButtonText : "确定",
//			cancelButtonText : "取消",
//			showCancelButton : true,
//		}, function(isConfirm) {
//			if (isConfirm) {
//				Page.coverLayer("正在操作数据，请稍候...");
//				$.post(Action.updateUserParamAction,
//						$("#mainForm").serialize(), function(data) {
//							Page.coverLayer();
//							if (data.success == true) {
//								Page.confirm("操作成功", "更新成功!", "info", {
//									closeOnConfirm : false,
//									showCancelButton : false
//								}, function() {
//									Events.refreshClkAct();
//								});
//							} else {
//								Page.alert("操作失败", data.msg);
//							}
//						});
//			}
//		})
//	},
//	// 更新全部用户属性按钮的单击动作事件
//	updateAllClkAct : function(event) {
//		Page.confirm("", "确定要更新全部的用户属性么？处理时间会很长,请谨慎操作", "info", {
//			closeOnConfirm : false,
//			confirmButtonColor : "#DD6B55",
//			confirmButtonText : "确定",
//			cancelButtonText : "取消",
//			showCancelButton : true,
//		}, function(isConfirm) {
//			if (isConfirm) {
//				Page.coverLayer("正在操作数据，请稍候...");
//				$.post(Action.updateAllUserParamAction, $("#mainForm")
//						.serialize(), function(data) {
//					Page.coverLayer();
//					if (data.success == true) {
//						Page.confirm("操作成功", "更新成功!", "info", {
//							closeOnConfirm : false,
//							showCancelButton : false
//						}, function() {
//							Events.refreshClkAct();
//						});
//					} else {
//						Page.alert("操作失败", data.msg);
//					}
//				});
//			}
//		})
//	},
//	updAll : function(event) {
//
//	},
	companyClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> 企业用户信息补录",
			width : 650,
			height : 630,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.companyClkAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
    syncRoleClkAct:function(event){

        var userId=$(this).data("userid");

        Page.confirm("", "确定要同步该用户角色么?", "info", {
			closeOnConfirm : false,
			confirmButtonColor : "#DD6B55",
			confirmButtonText : "确定",
			cancelButtonText : "取消",
			showCancelButton : true,
		}, function(isConfirm) {
			if (isConfirm) {
				Page.coverLayer("正在同步角色，请稍候...");
				$.post(Action.syncRoleAction, {"userId":userId}, function(data) {
					Page.coverLayer();
					if (data.code != "0") {
                        Page.alert("操作失败", data.msg);
					} else {
                        Page.confirm("同步成功", "同步成功!", "info", {
                            closeOnConfirm : false,
                            showCancelButton : false
                        }, function() {
                            Events.refreshClkAct();
                        });
					}
				});
			}
		})

	},
	// 编辑按钮的单击动作事件
	modifyClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class='fa fa-pencil'></i> 会员信息修改",
			width : 650,
			height : 580,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.modifyAction, null, null, "dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},

    modifyMoileClkAct : function(event) {
    Page.primaryKey.val($(this).data("userid")), $.colorbox({
        overlayClose : false,
        href : "#urlDialogPanel",
        title : "<i class='fa fa-pencil'></i> 测试用手机号修改",
        width : 650,
        height : 580,
        inline : true,
        fixed : true,
        returnFocus : false,
        open : true,
        // Open事件回调
        onOpen : function() {
            setTimeout(function() {
                Page.submit(Action.modifyMoileAction, null, null, "dialogIfm");
            }, 0)
        },
        // Close事件回调
        onClosed : function() {
            Page.form.attr("target", "");
        }
    })
},
	// 编辑按钮的单击动作事件
	modifyReClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")), $.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 修改推荐人",
			width : 650,
			height : 483,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.modifyReAction, null, null,
									"dialogIfm");
						}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		})
	},
	// 编辑按钮的单击动作事件
	modifyICClkAct : function(event) {
		Page.primaryKey.val($(this).data("userid")),
			$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-group\"></i> 修改身份证",
			width : 650,
			height : 483,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(
						function() {
							Page.submit(Action.modifyICAction, null, null,
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
		//window.location.reload();
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	// 更新出借表的推荐人信息数据
	updateInviteClkAct : function(event) {
		$.colorbox({
			overlayClose : false,
			href : "#urlDialogPanel",
			title : "<i class=\"fa fa-newspaper-o\"></i> 更新出借表的推荐人信息数据",
			width : 870,
			height : 630,
			inline : true,
			fixed : true,
			returnFocus : false,
			open : true,
			// Open事件回调
			onOpen : function() {
				setTimeout(function() {
					Page.submit(Action.updateInviteInfoAction, null, null,
							"dialogIfm");
				}, 0)
			},
			// Close事件回调
			onClosed : function() {
				Page.form.attr("target", "");
			}
		});
	},
	// 查找按钮的单击事件绑定
	searchClkAct : function(selection, cds) {
		$("#mainForm").attr("target", "");
		$("#paginator-page").val(1), Page.submit(Action.searchAction);
	},
	exportClkAct : function() {
		var startTime = $("#start-date-time").val();
		var endTime = $("#end-date-time").val();
		if(startTime == "" || endTime == ""){
			Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
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
    enhanceExportAction : function() {
        var startTime = $("#start-date-time").val();
        var endTime = $("#end-date-time").val();
        if(startTime == "" || endTime == ""){
            Page.confirm("","请选择导出数据的起止时间（起止时间需小于等于31天）","error",{showCancelButton: false}, function(){});
            return false;
        }
        $("#mainForm").attr("target", "_blank");
        Page.notice("正在处理下载数据,请稍候...");
        setTimeout(function() {
            Page.coverLayer()
        }, 1);
        $("#paginator-page").val(1);
        Page.submit(Action.enhanceExportAction);
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
	},
	//合规四期
    // 修改用户信息(手机号,邮箱等)
    updateUserMobile : function(event) {
        $("#updType").val("mobile");
        Page.primaryKey.val($(this).data("userid")),
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 会员信息修改",
                width : 850,
                height : 583,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.updateUserInfos).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
    },
    updateUserEmail : function(event) {
        $("#updType").val("email");
        Page.primaryKey.val($(this).data("userid")),
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 会员信息修改",
                width : 850,
                height : 583,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.updateUserInfos).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
    },
    updateUserRole : function(event) {
        $("#updType").val("userRole");
        Page.primaryKey.val($(this).data("userid")),
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 会员信息修改",
                width : 850,
                height : 583,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.updateUserInfos).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
    },
    updateUserCard : function(event) {
        $("#updType").val("bankCard");
        Page.primaryKey.val($(this).data("userid")),
            $.colorbox({
                overlayClose: false,
                href: "#urlDialogPanel",
                title: "<i class=\"fa fa-plus\"></i> 会员信息修改",
                width : 750,
                height : 483,
                inline: true,
                fixed: true,
                returnFocus: false,
                open: true,
                // Open事件回调
                onOpen: function () {
                    setTimeout(function () {
                        Page.form.attr("target", "dialogIfm").attr("action",
                            Action.updateUserInfos).submit();
                    }, 0)
                },
                // Close事件回调
                onClosed: function () {
                    Page.form.attr("target", "");
                }
            })
    }

};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#userId"),
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
		Events.refreshTreeAct();
		// 清空部门选择
		Events.clearDepClkAct();
	},
	// 初始化画面事件处理
	initEvents : function() {
		// 边界面板按钮的单击事件绑定
		$(".fn-searchPanel").click(Events.searchPanelClkAct),
		// 重置表单
		$(".fn-Reset").click(Events.resetFromClkAct),
		// 导出按钮的单击事件绑定
		$(".fn-Export").click(Events.exportClkAct),
		 // 导出按钮的单击事件绑定 具有 组织机构查看权限
		$(".fn-EnhanceExport").click(Events.enhanceExportAction),
//		// 更新按钮的单击事件绑定
//		$(".fn-Update").click(Events.updateClkAct),
//		// 更新按钮的单击事件绑定
//		$(".fn-UpdateAll").click(Events.updateAllClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		//修改手机号
		$(".fn-Modify-mobile").click(Events.modifyMoileClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-ModifyRe").click(Events.modifyReClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-ModifyIC").click(Events.modifyICClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct),
		// 查找按钮的单击事件绑定
		$(".fn-Search").click(Events.searchClkAct),
		// 更新出借表的推荐人信息数据
		$(".fn-UpdateInvite").click(Events.updateInviteClkAct),
		// 清空按钮的单击事件绑定
		$(".fn-ClearForm").click(Events.clearClkAct),
		// 清空部门按钮的单击事件绑定
		$(".fn-ClearDep").click(Events.clearDepClkAct),
		//补录企业用户信息事件绑定
		$(".companyInfo").click(Events.companyClkAct),
		//同步用户角色
		$(".fn-SyncRole").click(Events.syncRoleClkAct),

		$(".fn-ModifyMobile").click(Events.updateUserMobile)
        $(".fn-ModifyEmail").click(Events.updateUserEmail)
        $(".fn-ModifyUserRole").click(Events.updateUserRole)
        $(".fn-ModifyCard").click(Events.updateUserCard)
	}
});

