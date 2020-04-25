var
// --------------------------------------------------------------------------------------------------------------------------------
/* 对应JAVA的Controllor的@RequestMapping */
Action = {
	//补录企业用户信息
	companySerchAction:"serchCompanyInfo",
	companySaveAction :"saveCompanyInfo"
}

var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	companyClkAct :function(){
		var result = checkAccountId();
		if (result) {
			var param = {};
			param.accountId = $("#accountId").val();
			param.userId = $("#userId").val();
			$.ajax({
				url : Action.companySerchAction,
				type : "POST",
				async : true,
				data : JSON.stringify(param),
				dataType: "json",
				contentType : "application/json",
				success : function(data) {
					if (data.status == "success") {
						Page.confirm("",data.result,"success",{showCancelButton: false}, null);
						showCompanyInfo(data);
						$("#hasError").val(0);
						$("#success").val(1);
					} else {
						Page.confirm("",data.result,"error",{showCancelButton: false}, null);//function(){Events.cancelClkAct()}
						$("#success").val(0);
						$("#hasError").val(1);
					}
				},
				error : function(err) {
					Page.coverLayer();
					Page.primaryKey.val("");
					Page.notice("企业用户信息查询发生错误,请重新操作!", "","error");
				}
			});
		}
	},
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		var result = checkCompanyInfo();
		if (result) {
			var param = {};
			param.accountId = $("#accountId").val();
			param.userId = $("#userId").val();
			param.name = $("#companyName").text();
			param.idType = $("#idType").val();
			param.idNo = $("#idNo").text();
			param.account = $("#account").text();
			param.busId = $("#buseId").val();
			param.taxId = $("#taxId").val();
			param.remark = $("#remark").val();
			$.ajax({
				url : Action.companySaveAction,
				type : "POST",
				async : true,
				data : JSON.stringify(param),
				dataType: "json",
				contentType : "application/json",
				success : function(data) {
					if (data.status == "success") {
						Page.confirm("",data.result,"success",{showCancelButton: false}, function(){Events.cancelClkAct()});
					} else {
						Page.confirm("",data.result,"error",{showCancelButton: false}, null);
					}
				},
				error : function(err) {
					Page.coverLayer();
					Page.primaryKey.val("");
					Page.notice("企业用户信息补录发生错误,请重新操作!", "","error");
				}
			});
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
	
	
};
//校验合法电子账号
function checkAccountId(){
	var accountId = $("#accountId").val();
	if (accountId == "") {
		Page.notice("请填入电子账号!", "","error");
		return false;
	}else{
		 var reg = new RegExp("^[0-9]*$");
		 if(!reg.test(accountId)){
			 Page.notice("电子账号不能包含除数字外的其他字符!", "","error");
			 return false;
		 }
		 if(accountId.length > 20){
			 Page.notice("电子账号不能超过20个字符!", "","error");
			 return false;
		 }
		 return true;
	}
}
//校验企业用户信息合法性
function checkCompanyInfo(){
	var result = checkAccountId();
	var remark = $("#remark").val();
	var busId = $("#buseId").val();
	var taxId = $("#taxId").val();
	if (result) {
		var isOpen = $("#isOpenAccount").val();
		var userType = $("#userType").val();
		
		if (isOpen == 1 && userType != 1) {//用户已开户
			Page.notice("该用户已开户!", "","error");
			 return false;
		}
		var errFlag = $("#hasError").val();
		if (errFlag == 1) {//接口返回错误
			Page.notice("该用户未在江西银行开立电子账户，请检查!", "","error");
			 return false;
		}
		if (remark == "") {
			Page.notice("请填写修改说明!", "","error");
			 return false;
		}
		
		if (busId != "") {
			 if(/[\u4e00-\u9fa5]/.test(busId)){
				 Page.notice("营业执照编号不能包含除数字和字母外的其他字符!", "","error");
				 return false;
			 }
		}
		
		if (taxId != "") {
			 if(/[\u4e00-\u9fa5]/.test(taxId)){
				 Page.notice("税务登记号不能包含除数字和字母外的其他字符!", "","error");
				 return false;
			 }
		}
		
		if (remark.length > 50) {
			Page.notice("修改说明不能超过50个字符!", "","error");
			 return false;
		}
		
		return true;
	}else{
		return false;
	}
}

function showCompanyInfo(data){
	var company = data.company;
	$("#isOpenAccount").val(data.isOpenAccount);
	$("#companyName").text(company.name);
	var idType = company.idType;
	$("#idType").val(idType);
	if (idType != null && idType != "") {
		if (idType == 20) {
			$("#cardType").text("组织机构代码");
		}
		if (idType == 25) {
			$("#cardType").text("社会信用号");
		}
	}
	$("#mobile").text(company.mobile);
	$("#account").text(company.account);
	$("#buseId").text(company.busId);
	$("#taxId").text(company.taxId);
	$("#idNo").text(company.idNo);
}
// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Company").click(Events.companyClkAct);
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
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
