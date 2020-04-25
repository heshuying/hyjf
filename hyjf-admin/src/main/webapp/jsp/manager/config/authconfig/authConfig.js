// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
var Action = {
        goUpdateAuthConfigAction : "goUpdateAuthConfigAction"
},
/* 事件动作处理 */
Events = {
	//修改授权配置
    modifyClkAct : function(event) {
        if(event) {
            Page.primaryKey.val($(this).data("id"))
        }
        $.colorbox({
            overlayClose: false,
            href: "#urlDialogPanel",
            title: "<i class=\"fa fa-plus\"></i> 修改授权配置",
            width: 550, height: 450,
            inline: true,  fixed: true, returnFocus: false, open: true,
            // Open事件回调
            onOpen: function() {
                setTimeout(function() {
                    Page.form.attr("target", "dialogIfm").attr("action", Action.goUpdateAuthConfigAction).submit();
                }, 0);
            },
            // Close事件回调
            onClosed: function() {
                Page.form.attr("target", "");
            }
        })
	},
    // 刷新按钮的单击动作事件
    refreshClkAct : function() {
        window.location.reload();
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#ids"),
	// 画面布局
	doLayout : function() {

	},
	// 初始化画面事件处理
	initEvents : function() {
		// 修改按钮的单击事件绑定
		$(".fn-Modify").click(Events.modifyClkAct)
	}
});