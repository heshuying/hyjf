/* 初始化加载 */
$(document).ready(function(){ 
	$("#pageUrlDiv").css("display", "none");
//	$("#borrowImageListDiv").css("display", "none"); 
	$("#versionmin").css("display", "none");
	$("#versionmax").css("display", "block");
	optionChange();
	optionChangeVersion();
}); 

var Action = {
	validateBefore : "validateBeforeAction"
};
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			if ($('#sort').val()=="undifind"||$('#sort').val()=="") {
				alert("请输入序号");
				return
			}
			if ($("#borrowImageUrl").val()=="undifind"||$("#borrowImageUrl").val()=="") {
				alert("请上传图片");
				return
			}
			
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			});
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
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
		// 图片上传
		$('.fileupload').fileupload({
			url: "uploadFile",
			autoUpload: true,
			done: function (e, data) {
				var file = data.result[0];
				console.info(file);
				$("#borrowImageName").val(file.imageName);
				$("#borrowImageRealname").val(file.imageRealName);
				$("#borrowImageUrl").val(file.imagePath);
			}
		}).find("input:file").removeAttr('disabled');
	},
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		if($("#success").val() == "success") {
			 parent.$.colorbox.close();
			 parent.Events.reSeacheClkAct();
		} else {
			Page.coverLayer();
		}

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();

//页面类型变化调用
function optionChange(){
	var checkValue=$("#pageType").val();
	if(checkValue=="1"){
		$("#pageUrlDiv").css("display", "block");
		$("#pageUrlSelect").css("display", "none");
		/*$("#pageUrl").attr("name","pageUrl");
		$("#pageUrlSelect").attr("name","");
		$("#borrowImageListDiv").css("display", "none");
		$("#pageUrlSelect").val("");*/
	}
	if(checkValue=="0"){
		$("#pageUrlDiv").css("display", "none");
		$("#pageUrlSelect").css("display", "block");
		/*$("#pageUrlSelect").attr("name","pageUrl");
		$("#pageUrl").attr("name","");
		$("#borrowImageListDiv").css("display", "block");*/
		$("#pageUrl").val("");
	}
	if(checkValue=="2"){
		$("#pageUrlDiv").css("display", "none");
		$("#pageUrlSelect").css("display", "none");
		/*$("#pageUrlSelect").attr("name","pageUrl");
		$("#pageUrl").attr("name","");
		$("#borrowImageListDiv").css("display", "block");*/
		$("#pageUrl").val("");
	}
}
	//页面类型变化调用
function optionChangeVersion(){
		var checkValue=$("#versionSelect").val();
		if(checkValue=="1"){
			$("#versionmin").css("display", "block");
			$("#versionmax").css("display", "block");
			/*$("#pageUrl").attr("name","pageUrl");
			$("#pageUrlSelect").attr("name","");
			$("#borrowImageListDiv").css("display", "none");
			$("#pageUrlSelect").val("");*/
		}
		if(checkValue=="0"){
			$("#versionmin").css("display", "block");
			$("#versionmax").css("display", "none");
			/*$("#pageUrlSelect").attr("name","pageUrl");
			$("#pageUrl").attr("name","");
			$("#borrowImageListDiv").css("display", "block");*/
			$("#versionMax").val("");
		}
		if(checkValue=="2"){
			$("#versionmin").css("display", "none");
			$("#versionmax").css("display", "block");
			$("#version").val("");
			/*$("#pageUrlSelect").attr("name","pageUrl");
			$("#pageUrl").attr("name","");
			$("#borrowImageListDiv").css("display", "block");*/
		}
	}
		


