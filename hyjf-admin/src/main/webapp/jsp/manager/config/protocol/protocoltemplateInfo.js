var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
    //图片上传点击事件
    clickUpload :  function() {
        $(".checkedVersionId").val("");
    },
    //单选框change事件
	changeRadio : function() {
		// var id= $("input[type='radio']:checked").attr("id");hiddenRemarks
		//被选中的版本的协议路径
		var versionId = $(this).parent().find("input[name='hiddenProtocolVersionId']").val();
		var checkUrl = $(this).parent().find("input[name='hiddenProtocolUrl']").val();
        var checkVersionNumber = $(this).parent().find("input[name='hiddenVersionNumber']").val();
        var checkRemarks = $(this).parent().find("input[name='hiddenRemarks']").val();
        var checkVersionId = $(this).parent().find("input[name='hiddenProtocolVersionId']").val();
        var checkDisplayName = $(this).parent().find("input[name='hiddenDisplayName']").val();
        $("#protocolUrl").val(checkUrl);
        $("#versionNumber").val(checkVersionNumber);
        $("#remarks").val(checkRemarks);
        $(".checkedVersionId").val(checkVersionId);
        $("#displayName").val(checkDisplayName);
        $.ajax({
            type:"POST",
            async:false,
            url:"updateExistProtocol",
            data:{"id":versionId, "versionNumber":checkVersionNumber, "remarks":checkRemarks},
            success:function(data){
                Page.alert("", "修改成功!");
            }
        });
	},
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前协议吗？", function(isConfirm) {
				if(isConfirm) {
                    var protocolName = $("#protocolName").val();
                    var versionNumber = $("#versionNumber").val();
                    var displayName = $("#displayName").val();
                    var protocolType = $("#protocolType").val();
                    var protocolUrls = $("#protocolUrl").val();
                    var oldDisplayName = $("#oldDisplayName").val();
                    //获取协议模板的id
                    var protocolTemplateId = $("#protocolTemplateId").val();
					if(protocolTemplateId == null||protocolTemplateId == ""){//新增保存
                        $.ajax({
                            type:"POST",
                            async:false,
                            url:"validationProtocolNameAction",
                            data:{"protocolName":protocolName,"versionNumber":versionNumber,"displayName":displayName,"protocolType":protocolType,"protocolUrl":protocolUrls,"flag":0},
                            success:function(data){
                                var data = eval(data);
                                if(data.n_error||data.v_error||data.d_error||data.p_error||data.e_error){
                                    $("#n_error_message").html (data.n_error);
                                    $("#v_error_message").html (data.v_error);
                                    $("#d_error_message").html (data.d_error);
                                    $("#e_error_message").html (data.e_error);
                                    $("#v_error_pdf").html (data.p_error);
                                }else{
                                    if(flag){
                                        Page.submit();
                                    }
                                }
                            }
                        });
					}else{//修改保存
						var url= $("#protocolUrl").val();
                        var checkedVersionId= $(".checkedVersionId").val();
						//新增版本
                        $.ajax({
                            type:"POST",
                            async:false,
                            url:"validationProtocolNameAction",
                            data:{"protocolName":protocolName,"versionNumber":versionNumber,"displayName":displayName,"protocolType":protocolType,"protocolUrl":url,"oldDisplayName":oldDisplayName,"flag":1},
                            success:function(data){
                                var data = eval(data);
                                if(data.v_error==""){
                                    $("#v_error_message").html ("");
                                }else if(data.p_error==""){
                                    $("#p_error_message").html ("");
                                }else if(data.d_error==""){//前台展示名称
                                    $("#d_error_message").html ("");
                                }

                                if(data.v_error){
                                    $("#v_error_message").html ("请修改信息后保存！");
                                }
                                else if(data.p_error){
                                    $("#p_error_message").html (data.p_error);
                                }else if(data.d_error){//前台展示名称
                                    $("#d_error_message").html (data.d_error);
                                }else{
                                    if(flag){
                                        Page.submit();
                                    }
                                }
                            }
                        });
					}
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};
var flag=true;
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
    // 画面布局
    doLayout: function() {
        // 条件下拉框
        $(".form-select2").select2({
            width: 580,
            placeholder: "请选择条件",
            allowClear: true,
            language: "zh-CN"
        });
    },
	// 初始化画面事件处理
	initEvents : function() {

		//获取图片路径----
		var protocolUrl = $("#protocolUrlHidden").val();
        // var checkedVersion= $(".checkedVersionId").val();
        // alert(protocolUrl);
        // //获取协议模板的id
        // var protocolTemplateId = $("#protocolTemplateId").val();
        $("#protocolUrl").click(Events.clickUrl);
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		//单选框单机事件绑定
		$(".displayFlag").click(Events.changeRadio);
		//图片上传点击事件
		$("#fileupload").click(Events.clickUpload);
		//校验字段
		// $("#protocolName").blur(Events.validationField);
		// $("#versionNumber").blur(Events.validationField);
		// 图片上传
		$('#fileupload').fileupload({

			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
                if (file.errorMessage != "上传文件成功！"){
                    flag=false;
                    $("#v_error_pdf").html (file.errorMessage);
				}else{
                    $("#protocolUrl").val(file.imagePath);
                    // $("#v_error_pdf").html (file.errorMessage);
                    $("#v_error_pdf").html (file.errorMessage).css("color","black");
                    $("#protocolUrl").blur();
                }
			}
		});
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
	}
}),

Page.initialize();

