var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 页面查询
	doDataSyn : "dodatasyn?fid="+$("#fid").val()
},
/* 事件动作处理 */
Events = {
    // 按钮事件
    dataSyn : function(event) {
        if(event) {
            Page.primaryKey.val($(this).attr("data-type"));
        }
        var dataType=$(this).attr("data-type");
        Page.confirm("", "确定同步吗？", function(isConfirm) {
            if(isConfirm){
                $.ajax({
                    url : Action.doDataSyn,
                    async : true,
                    type : "POST",
                    data : {
                        "dataType" : dataType
                    },
                    // 成功后开启模态框
                    success : function (data){
                        if(data.status=="success"){
                            Page.confirm("操作成功", "", "success", {
                                closeOnConfirm : true,
                                showCancelButton : false
                            }, function() {
                                this.close;
                            });
                        }else {
                            Page.confirm("操作失败", "", "error", {
                                closeOnConfirm : true,
                                showCancelButton : false
                            }, function() {
                                this.close;
                            });
                        }

                    },
                    error : function() {
                        alert("请求失败");
                    },
                    dataType : "json"
                });// ajax end
			}
        })
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
	// 画面布局
	doLayout : function() {


	},
	// 初始化画面事件处理
	initEvents : function() {
		// 刷新按钮的单击事件绑定
		$(".data-syn").click(Events.dataSyn)
	}
});



