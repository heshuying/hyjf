var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	/*// 联动下拉事件
	assetTypeAction : "assetNameAction",*/
	updateSelectAction:"updateSelectAction",
 	assetTypeAction : "selectAction"
},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的账户信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 查询按键单击事件绑定
	selectClkAct : function() {
		var username = $("#username").val();
		if (username == "") {
            var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
            valid.check(false,"#username");
			return;
		}
		$.ajax({
			url : Action.assetTypeAction,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				username : username
			},
			success : function(data) {
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				var box = $("#username").parent();
				var group = box.parent('.form-group');
//				$("#username").val('<c:out value="${data.info}"></c:out>');
				if(data.status == 'n'){
					$("#username").addClass('Validform_error');
					box.find('.Validform_checktip').addClass('Validform_wrong').removeClass("Validform_right").text(data.info)
					group.removeClass("has-success").addClass("has-error")
					//validform
				}else{
					$("#username").removeClass('Validform_error');
					box.find('.Validform_checktip').removeClass('Validform_wrong').addClass("Validform_right");
					group.removeClass("has-error").addClass("has-success")
				}

				if(data.truename!=null){
					$("#truename").val(data.truename);
					valid.check(false,"#truename");
				}else{
                    $("#truename").val("");
				}
				// 当用户角色为出借人时，此项为空
                $("#cooperateNum").parent().parent().hide();
                $("#cooperateNum").attr("datatype","*0-20");
				if(data.roleId!=null){
					$("#roleName").val(data.roleId);
					valid.check(false,"#roleName");
					// 当用户角色为借款人时，才调取其用户的合作机构编号；出借人则不调取
                    if(data.roleId == "借款人") {
                        $("#cooperateNum").parent().parent().show();
                        $("#cooperateNum").attr("datatype","*1-20");
                        if (data.cooperateNum != null) {
                            $("#cooperateNum").val(data.cooperateNum);
                            valid.check(false,"#cooperateNum");
                        }else{
                            $("#cooperateNum").val("");
						}
                    }
				}else{
                    $("#roleName").val("");
                }
				if(data.userType!=null){
					$("#userType").val(data.userType);
					valid.check(false,"#userType");
				}else{
                    $("#userType").val("");
                }
				if(data.OPEN!=null){
					$("#bankOpenAccount").val(data.OPEN);
					valid.check(false,"#bankOpenAccount");
				}else{
                    $("#bankOpenAccount").val("");
                }
				if(data.account!=null){
					$("#account").val(data.account);
					valid.check(false,"#account");
				}else{
                    $("#account").val("");
                }
			},
			error : function(err) {
				Page.alert("","您输入的用户名无对应信息，请重新输入!");
			}
		});
	},
	//更新查询按键单击事件绑定
	updateClkAct : function() {
		var username = $("#username").val();
		if (username == "") {
            var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
            valid.check(false,"#username");
			return;
		}
		$.ajax({
			url : Action.updateSelectAction,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				username : username
			},
			success : function(data) {
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				var box = $("#username").parent();
				var group = box.parent('.form-group');
//				$("#username").val('<c:out value="${data.info}"></c:out>');
				if(data.status == 'n'){
					$("#username").addClass('Validform_error');
					box.find('.Validform_checktip').addClass('Validform_wrong').removeClass("Validform_right").text(data.info)
					group.removeClass("has-success").addClass("has-error")
					//validform
				}else{
					$("#username").removeClass('Validform_error');
					box.find('.Validform_checktip').removeClass('Validform_wrong').addClass("Validform_right");
					group.removeClass("has-error").addClass("has-success")
				}
				
				if(data.truename!=null){
					$("#truename").val(data.truename);
					valid.check(false,"#truename");
				}else{
                    $("#truename").val("");
                }
                // 当用户角色为出借人时，此项为空
                $("#cooperateNum").parent().parent().hide();
                $("#cooperateNum").attr("datatype","*0-20");
                if(data.roleId!=null){
                    $("#roleName").val(data.roleId);
                    valid.check(false,"#roleName");
                    // 当用户角色为借款人时，才调取其用户的合作机构编号；出借人则不调取
                    if(data.roleId == "借款人") {
                        $("#cooperateNum").parent().parent().show();
                        $("#cooperateNum").attr("datatype","*1-20");
                        if (data.cooperateNum != null) {
                            $("#cooperateNum").val(data.cooperateNum);
                            valid.check(false,"#cooperateNum");
                        }else{
                            $("#cooperateNum").val("");
                        }
                    }
                }else{
                    $("#roleName").val("");
				}
				if(data.userType!=null){
					$("#userType").val(data.userType);
					valid.check(false,"#userType");
				}else{
                    $("#userType").val("");
                }
				if(data.OPEN!=null){
					$("#bankOpenAccount").val(data.OPEN);
					valid.check(false,"#bankOpenAccount");				
				}else{
                    $("#bankOpenAccount").val("");
                }
				if(data.account!=null){
					$("#account").val(data.account);
					valid.check(false,"#account");
				}else{
                    $("#account").val("");
                }
			},
			error : function(err) {
				Page.alert("","您输入的用户名无对应信息，请重新输入!");
			}
		});
	},
	// 选择事件
	changeClkAct : function() {
		if ($(this).val() == "endday") {
			$("#chargeTimeDivDay").show();
			$("#chargeTimeDivMonth").hide();
			//$("#chargeTime").val("");
			//Page.validate.ignore("#chargeTime");
		} else {
			$("#chargeTimeDivMonth").show();
			$("#chargeTimeDivDay").hide();
			//Page.validate.unignore("#chargeTime");
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	
	cooperateNumSrchOnchangeAct : function() {
		//产品类型控制处理
		var cooperateNum = $("#cooperateNum").val();
		$("#assetType").empty();
		if (cooperateNum == "") {
			return;
		}
		$.ajax({
			url : Action.assetTypeAction,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				cooperateNum : cooperateNum
			},
			success : function(data) {
				$("#assetType").select2({
					data: data,
				  	width : 268,
					placeholder : "全部",
					allowClear : true,
					language : "zh-CN"
				});
				Events.assetTypeChange();
			},
			error : function(err) {
				Page.alert("","没有对应的产品类型!");
			}
		});
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		$(".fn-Select").click(Events.selectClkAct);
		$(".fn-Update").click(Events.updateClkAct);
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		//$("#manChargeTimeType").change(Events.changeClkAct).change();
		// 
		$("#cooperateNum").change(Events.cooperateNumSrchOnchangeAct);
		// 
		$("#projectType").change(Events.projectTypeOnchangeAct);
	},
	// 画面布局
	doLayout : function() {
		// 初始化下拉框
		$(".form-select2").select2({
			allowClear : true,
			language : "zh-CN"
		})
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3,
		});
	},
}),

Page.initialize();
