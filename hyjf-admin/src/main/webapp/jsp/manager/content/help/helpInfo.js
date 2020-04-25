var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 改变二级菜单呢绒
	changeSubTypeAction2 : "changeSubTypeAction2"
},
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	// 编辑按钮的单击动作事件
	changeSubType : function(event) {
		if ($("#pcateId").val() != "") {
			$.post(Action.changeSubTypeAction2, {
				"pid" : $("#pcateId").val()
			}, function(data) {
				if (data.success == false) {
					Page.alert("", data.msg);
				} else {
					$("#cateId").empty();
					$("#cateId").append("<option value=\"\">--全部--</option>");
					$.each(data.childCategorys, function(i, val) {
							$("#cateId").append(
									"<option value=\"" + val.id + "\">"
											+ val.title + "</option>");
					});
				}
			});
		}else{
			$("#cateId").empty();
			$("#cateId").append("<option value=\"\">--全部--</option>");
		}
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		// 问题分类改变时,联动改变子分类
		$("#pcateId").change(Events.changeSubType);
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3,
			beforeSubmit : function(curform) {
			},
		});
	}
}),

Page.initialize();
