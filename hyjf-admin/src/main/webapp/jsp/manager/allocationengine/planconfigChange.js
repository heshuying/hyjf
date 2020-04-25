var
// --------------------------------------------------------------------------------------------------------------------------------
Action = {
		// 启用Action
		checkInputlabelname : "checkInputlabelname",
		checkInputlabelSort:"checkInputlabelSort"
	},
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
//		console.log($('#labelSort').val());
		if ($('#labelSort').val() === '') {
			return;
		}
		if(isSort()==true){
			$('.sort-error').hide()
		}else{
			$('.sort-error').show()
		}
		if(Page.validate.check(false) && $(".form-group.has-error").length==0 && isSort()==true) {
				Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
					if(isConfirm) {
						Page.submit();
						console.log(parent);
						parent.$.colorbox.close();
						Page.coverLayer();
					}
				})
		}
		
	},
	
	// 查询按键单击事件绑定
	selectClkAct : function() {
		var labelName = $("#labelName").val();
		var planNid = $("#planNid").val();
		
		//标签名称未入力
		if (labelName == "") {
			return;
		}
		
		$.ajax({
			url : Action.checkInputlabelname,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				labelName : labelName,
				planNid : planNid
			},
			success : function(data) {
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				var box = $("#labelName").parent();
				var group = box.parent('.form-group');

				if(data.status == 'n'){
					$("#labelName").addClass('Validform_error');
					box.find('.Validform_checktip').addClass('Validform_wrong').removeClass("Validform_right").text(data.info)
					group.removeClass("has-success").addClass("has-error")
					
				}else{
					$("#labelName").removeClass('Validform_error');
					box.find('.Validform_checktip').removeClass('Validform_wrong').addClass("Validform_right");
					group.removeClass("has-error").addClass("has-success")
				}
			},
			error : function(err) {
				Page.alert("","系统校验接口请求异常!");
			}
		});
	},
	
	
	// 查询按键单击事件绑定
	labelSortClkAct : function() {
		var labelSort = $("#labelSort").val();
		var planNid = $("#planNid").val();
		//标签名称未入力
		if (labelSort == "") {
			return;
		}
		$.ajax({
			url : Action.checkInputlabelSort,
			type : "POST",
			text : "",
			async : true,
			dataType : "json",
			data :  {
				labelSort : labelSort,
				planNid : planNid
			},
			success : function(data) {
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				var box = $("#labelSort").parent();
				var group = box.parent('.form-group');

				if(data.status == 'n'){
					$("#labelSort").addClass('Validform_error');
					box.find('.Validform_checktip').addClass('Validform_wrong').removeClass("Validform_right").text(data.info)
					group.removeClass("has-success").addClass("has-error")
					
				}else{
					$("#labelSort").removeClass('Validform_error');
					box.find('.Validform_checktip').removeClass('Validform_wrong').addClass("Validform_right");
					group.removeClass("has-error").addClass("has-success")
				}
			},
			error : function(err) {
				Page.alert("","系统校验接口请求异常!");
			}
		});
	},
	
	
	
	
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	},
	
	// 单选按钮改变
	radioChanged : function(){
		var id = $(this).parents(".form-group").find("input.form-control").attr("id");
		Page.validate.unignore("#"+id);
		Page.validate.check(false,"#"+id);
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
		// 标签校验
		$(".fn-Select").click(Events.selectClkAct);
		// 标签名称入力各种校验
		$('#labelName').blur(Events.selectClkAct);
		
		// 标签排序入力各种校验(传入的该标签的排序不能存在于该计划下所有标签的排序中，如果存在就报错，不能插入)
		$('#labelSort').blur(Events.labelSortClkAct);

		// 图片上传
		$('#fileupload').fileupload({
			url : "uploadFile",
			autoUpload : true,
			done : function(e, data) {
				var file = data.result[0];
				$("#logo").val(file.imagePath);
			}
		});
		$("[name=transferTimeSort],[name=aprSort],[name=actulPaySort],[name=investProgressSort]").change(Events.radioChanged)
	},
	validate:null,
	// 画面初始化
	initialize : function() {
		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct()) || Page.coverLayer();

		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype: 3
		});
		// 忽略参数验证
		Page.validate.ignore("#transferTimeSortPriority,#aprSortPriority,#actulPaySortPriority,#investProgressSortPriority");
	}
}),
Page.initialize();
function isSort(){
	var el = $('.sort')
	var arr = []
	for(var i=0;i<el.length;i++){
		if(el.eq(i).val()!=''){
			if($.inArray(el.eq(i).val(),arr)==-1){
				if(el.eq(i).val()>0&&el.eq(i).val()<5){
					arr.push(el.eq(i).val())
				}else{
					console.log(2)
					return false
				}
			}else{
				console.log(1)
				return false
			}
		}
	}
	return true;
}