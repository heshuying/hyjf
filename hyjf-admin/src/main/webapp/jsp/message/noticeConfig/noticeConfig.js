var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 查询的Action
	searchAction : "init",
	// 详细画面的Action
	infoAction : "infoAction",
	// 修改画面的Action
	updateAction : "updateAction",
	// 关闭的Action
	closeAction : "closeAction",
	// 启用Action
	openAction : "openAction",
},
/* 事件动作处理 */
Events = {
	// 全选checkbox的change动作事件
	selectAllAct : function() {
		$(".listCheck").prop("checked", this.checked);
	},
	// 添加按钮的单击动作事件
	addClkAct : function() {
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 添加配置",
			width: 650, height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
	        // Open事件回调
	        onOpen: function() {
	        	setTimeout(function() {
	        		Page.pkIds.val(""),
	        		Page.pkCode.val(""),
	        		Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
	        	}, 0)
	        },
	        // Close事件回调
	        onClosed: function() {
	        	Page.form.attr("target", "");
	        }
		})
	},
	// 编辑按钮的单击动作事件
	modifyClkAct: function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id")),
			Page.pkCode.val($(this).data("name"))
		}
		$.colorbox({
			overlayClose: false,
			href: "#urlDialogPanel",
			title: "<i class=\"fa fa-plus\"></i> 修改配置",
			width: 650, height: 430,
			inline: true,  fixed: true, returnFocus: false, open: true,
			// Open事件回调
			onOpen: function() {
				setTimeout(function() {
					Page.form.attr("target", "dialogIfm").attr("action", Action.infoAction).submit();
				}, 0)
			},
			// Close事件回调
			onClosed: function() {
				Page.form.attr("target", "");
			}
		})
	},
	openClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id")),
			Page.pkCode.val($(this).data("name"))
		}
		Page.confirm("", "确定要执行本次启用操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.openAction);
		})
	},
	closeClkAct : function(event) {
		if(event) {
			console.info($(this).data());
			Page.pkIds.val($(this).data("id")),
			Page.pkCode.val($(this).data("name"))
		}
		Page.confirm("", "确定要执行本次关闭操作吗？", function(isConfirm) {
			isConfirm && Page.submit(Action.closeAction);
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
		
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	pkIds : $("#id"),
	pkCode : $("#name"),
	
	// 初始化画面事件处理
	initEvents : function() {
		// SelectAll		
		$("#checkall").change(Events.selectAllAct),
		// 添加按钮的单击事件绑定
		$(".fn-Add").click(Events.addClkAct),
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct),
		//关闭按钮的单击事件绑定
		$(".fn-Close").click(Events.closeClkAct),
		$(".fn-Open").click(Events.openClkAct),
		// 刷新按钮的单击事件绑定
		$(".fn-Refresh").click(Events.refreshClkAct);
	}
});
