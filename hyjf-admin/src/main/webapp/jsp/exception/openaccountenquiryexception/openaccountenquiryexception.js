var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	// 详细画面的Action
	updateAction : "openaccountenquiryAction",
},
/* 事件动作处理 */
Events = {
	
	// 编辑按钮的单击动作事件
	modifyClkAct : function(event) {
		if(Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			if(document.getElementById('lastname').value.length==0){
			}else{
				 $.ajax({
					url : "openAccountEnquiryError",
					async : true,
					type : "POST",
					data : {
						"param" : $("#lastname").val(),
						"userName" : $("#username").val(),
						"requestType":$(".ipt-change:checked").val()
					},
					// 成功后开启模态框
					success : function (data){
						$(".td-cont").html("");
						if(data.info && data.info!=""){
                            $("#t_accountStatus").html(data.info);
						}else if(data.isOpen&&data.isOpen==1){
							// 已开户  本地已经开了  直接显示就行
							$("#t_accountStatus").html("平台已开户");
							$("#t_username").html(data.username);
							$("#t_name").html(data.name);
                            $("#t_idCard").html(data.idNo);
                            $("#t_accountId").html(data.accountId);
							$("#t_regTimeEnd").html(data.regTimeEnd);
							$("#t_mobile").html(data.mobile);
							var user_role = "";
							if(data.roleId){
								if(data.roleId=="1"){
                                    user_role = "出借人";
								}
                                if(data.roleId=="2"){
                                    user_role = "借款人";
                                }
                                if(data.roleId=="3"){
                                    user_role = "担保机构";
                                }
                                $("#t_roleId").html(user_role);
							}
							$(".btn-qd-tb").hide();
						}else{
							// 本地未开户
							if(data.status && data.status=="y"){
								// 查询成功
                                $("#t_accountStatus").html("银行已开户");
                                $(".btn-qd-tb").show();
                                $("#t_username").html(data.username);
                                $("#t_name").html(data.name);
                                $("#t_idCard").html(data.idNo);
                                $("#t_accountId").html(data.accountId);
                                $("#t_regTimeEnd").html(data.regTimeEnd);
                                $("#userId").val(data.userId);
                                $("#roleId").val(data.roleId);
                                $("#t_mobile").html(data.mobile);
                                var user_role = "";
                                if(data.roleId){
                                    if(data.roleId=="1"){
                                        user_role = "出借人";
                                    }
                                    if(data.roleId=="2"){
                                        user_role = "借款人";
                                    }
                                    if(data.roleId=="3"){
                                        user_role = "担保机构";
                                    }
                                    $("#t_roleId").html(user_role);
                                }
							}else{
                                $("#t_accountStatus").html("银行未开户");
                                $(".btn-qd-tb").hide();
							}
						}
                        $.colorbox({overlayClose: false,href: "#queryInfo",title: "<i class=\"fa fa-plus\"></i> 开户掉单查询",width: "40%", height: "600",inline: true,  fixed: true, returnFocus: false, open: true,onOpen: function() {},onClosed: function() {}});
					},
					error : function() {
						alert("请求失败");
					},
					dataType : "json"
				});
			}
		}
	},
	changeClkAct:function(event) {
		var chkval = $(".ipt-change:checked").val();
    	if(chkval==2){
    		// 身份证
    		$("#username-div").show();
    	}else{
    		$("#username-div").hide();
    	}
	},
	// 保存结果
    saveAccount:function(event) {
        $.ajax({
            url : "openaccountenquiryupdateAction",
            async : true,
            type : "POST",
            data : {
                "userid" : $("#userId").val(),
                "accountId" : $("#t_accountId").html(),
                "roleId":$("#roleId").val(),
                "idNo":$("#t_idCard").html(),
                "name":$("#t_name").html(),
                "regTimeEnd":$("#t_regTimeEnd").html()
            },
            // 成功后开启模态框
            success : function (data){
            	if(data.status=="success"){
                    alert("操作成功");
				}else {
                    alert("操作失败");
				}
                $.colorbox.close();
			},
            error : function() {
                alert("请求失败");
            },
            dataType : "json"
        });
    }
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		$(".fn-Refresh").click(Events.modifyClkAct);
		$(".ipt-change").change(Events.changeClkAct);
        $(".btn-qd").click(function(){$.colorbox.close();});
        $(".btn-qd-tb").click(Events.saveAccount);
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
        $("#lastname").val("");
        $("#username").val("");
        $("#siteStatusOn").click();
	}
});
Page.initialize();