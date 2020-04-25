function checkTime(i) {
	if (i < 10) {
		i = "0" + i;
	}
	return i;
}
// 下拉菜单非默认--请选择  省市区
jQuery.validator.addMethod("emptySelect", function(value, element) {
	return $.trim(value) != "" && $.trim(value) != "请选择";
}, "请选择");

var validate = $("#hjkForm").validate({
	"rules" : {
		"realname" : "required",
		"tel" : {
			"required" : true,
			"isMobile" : true
		},
		"gender" : "required",
		"money" : {
			"number" : true,
			"required" : true
		},
		"limit" : "required",
		"purpose" : "required",
		"purpose" : "required",
		"pledge" : "required",
		"province" : "emptySelect",
		"city" : "emptySelect",
		"county" : "emptySelect",
		"profit" : "number",
		"turnover" : "number",
		"loan" : "number"
	},
	"messages" : {
		"realname" : "这是必填字段",
		"tel" : {
			"required" : "这是必填字段",
			"isMobile" : "请输入正确的电话"
		},
		"gender" : "请选择性别",
		"money" : {
			"number" : "请输入正确的金额",
			"required" : "这是必填字段"
		},
		"limit" : "这是必填字段",
		"purpose" : "这是必填字段",
		"purpose" : "这是必填字段",
		"pledge" : "这是必填字段",
		"province" : "请选择省份",
		"city" : "请选择城市",
		"county" : "请选择区县",
		"profit" : "请输入正确的金额",
		"turnover" : "请输入正确的金额",
		"loan" : "请输入正确的金额"
	},
	"ignore" : ".ignore",
	errorPlacement : function(error, element) {
		error.insertAfter(element);
	},
	highlight : function(element, errorClass) { //针对验证的表单设置高亮
		$(element).parent().addClass(errorClass);
	},
	submitHandler : function(form) {
		form.submit();
	}

});

customForm.init();
customForm.citySelection(cities);

//提交
$("#formSubmit").click(function() {
	$("#hjkForm").submit();
});

$("input.new-form-input").keyup(function() {
	if ($("#hjkForm").validate().element(this)) {
		$(this).parent().removeClass("error");
	}
})

$("input:hidden").change(function() {
	if ($("#hjkForm").validate().element(this)) {
		$(this).parent().removeClass("error");
	}
})
