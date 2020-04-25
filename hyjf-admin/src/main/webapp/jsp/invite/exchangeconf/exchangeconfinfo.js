var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if(Page.validate.check(false)) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if(isConfirm) {
					Page.submit();
				}
			})
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	
	selectChangeClkAct : function() {
		var prizeType =$('input:radio[name="prizeType"]:checked').val();
		if(prizeType == 1){
			$("#couponConfig").hide();
			$("#couponConfig").find("input").removeAttr("datatype");
			$("#picUpload").show();
		}
		
		if(prizeType == 2){
			$("#couponConfig").show();
			$("#couponConfig").find("input").attr("datatype", "*2-100");
			//$("#picUpload").hide();
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
		$(":input[name='prizeType']").change(Events.selectChangeClkAct);
		
		// 图片上传
		$('.fileupload').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$(this).parents("div.picClass").find("div.purMargin input").val(file.imagePath);
				/*$("#image").val(file.imagePath);*/
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
		
		//初始化
		var prizeType =$('input:radio[name="prizeType"]:checked').val();
		if(prizeType == 1){
			$("#couponConfig").hide();
			$("#couponConfig").find("input").removeAttr("datatype");
			$("#picUpload").show();
		}
		
		if(prizeType == 2 ){
			$("#couponConfig").show();
			$("#couponConfig").find("input").attr("datatype", "*2-100");
//			$("#picUpload").hide();
		}
	}
}),

Page.initialize();
