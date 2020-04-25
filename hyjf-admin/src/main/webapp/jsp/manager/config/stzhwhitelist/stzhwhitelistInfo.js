var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Action = {
	// 查询的Action
		infoAction : "loadNadmeConfig",
}
var
// --------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)&&Page.form.find(".has-error").length == 0) {
			Page.confirm("", "确定要保存当前的信息吗？", function(isConfirm) {
				if (isConfirm) {
					Page.submit();
				}
			})
		}else{
			//跳转到第一个错误报错
			var iframe = window.frames["dialogIfm"] == undefined ? $(document) : $(window.frames["dialogIfm"].document);
			var t = iframe.find('.Validform_error').eq(0).offset().top;
			$(iframe).scrollTop(t);
		}
	},
	userNameSelectChangeAct : function() {
		var that=this;
		var param = {};
		param.userName = $("#userName").val();
		console.info(JSON.stringify(param));
		$.ajax({
			url : Action.infoAction,
			type : "POST",
			async : true,
			data : "userName=" + param.userName,
			success : function(result) {
				result = eval('(' + result + ')');
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				/*result = eval('(' + result + ')');*/
				if(result.status=='y'){
					if(result.accountid!=undefined || result.accountid!="" || result.accountid!=null){
						$("#accountid").val(result.accountid + "");
						$("#accountid").attr("readOnly",true);
						valid.check(false,"#accountid");
					}else{
						$("#accountid").val("");
					}
					if(result.mobile.length!=0){
						$("#mobile").val(result.mobile + "").change();
						$("#mobile").attr("readOnly",true);
						valid.check(false,"#mobile");
					}
					if(result.customerName.length!=0){
						$("#customerName").val(result.customerName + "").change();
						$("#customerName").attr("readOnly",true);
						valid.check(false,"#customerName");
					}
					if(result.userId.length!=0){
						$("#userId").val(result.userId + "").change();
						$("#userId").attr("readOnly",true);
						valid.check(false,"#userId");
					}
				}else{
					$("#accountid").val("");
					$("#mobile").val("");
					$("#customerName").val("");
					$("#userId").val("");
					valid.check(false,"#accountid");
					valid.check(false,"#mobile");
					valid.check(false,"#customerName");
					/*$(that).parent().append("<span class='Validform_checktip Validform_wrong'>"+result.info+"</span>")*/

				}
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});
	},
	stUserNameSelectChangeAct : function() {
		var that=this;
		var param = {};
		param.stUserName = $("#stUserName").val();
		console.info(JSON.stringify(param));
		$.ajax({
			url : Action.infoAction,
			type : "POST",
			async : true,
			data : "stUserName=" + param.stUserName,
			success : function(result) {
				result = eval('(' + result + ')');
				var valid =window.frames['dialogIfm'] == undefined ? Page.validate : window.frames['dialogIfm'].Page.validate;
				if(result.status=='y'){
					if(result.accountid!=undefined || result.accountid!="" || result.accountid!=null){
						$("#stAccountid").val(result.accountid + "");
						$("#stAccountid").attr("readOnly",true);
						valid.check(false,"#stAccountid");
					}else{
						$("#accountid").val("");
					}
					if(result.mobile.length!=0){
						$("#stMobile").val(result.mobile + "");	
						$("#stMobile").attr("readOnly",true);
						valid.check(false,"#stMobile");
					}
					if(result.customerName.length!=0){
						$("#stCustomerName").val(result.customerName + "");
						$("#stCustomerName").attr("readOnly",true);
						valid.check(false,"#stCustomerName");
					}
					if(result.userId.length!=0){
						$("#stUserId").val(result.userId + "");
						$("#stUserId").attr("readOnly",true);
						valid.check(false,"#stUserId");
					}
				}else{
					$("#stAccountid").val("");
					$("#stMobile").val("");	
					$("#stCustomerName").val("");
					$("#stUserId").val("");
					valid.check(false,"#stAccountid");
					valid.check(false,"#stMobile");
					valid.check(false,"#stCustomerName");
					/*$(that).parent().append("<span class='Validform_checktip Validform_wrong'>"+result.info+"</span>")*/

				}
				
				
				
				/*if(result.couponType == 1){
					$("#couponQuota").text(result.couponQuota + "元");
				}else if(result.couponType == 2){
					$("#couponQuota").text(result.couponQuota + "%");
				}*/
			},
			error : function(err) {
				Page.alert("","数据取得失败!");
			}
		});
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		parent.$.colorbox.close();
	}
};

// --------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */
$.extend(Page, {
	// 画面的主键
	primaryKey : $("#id"),
	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		$(".fn-Cancel").click(Events.cancelClkAct);
		$("#userName").change(Events.userNameSelectChangeAct);
		$("#stUserName").change(Events.stUserNameSelectChangeAct);
	},
	 
	doLayout: function() {
		// 日历选择器
		$('#approvalTime').datepicker({
			autoclose: true,
			todayHighlight: true,
			endDate:new Date()
		})
	},
	// 画面初始化
	initialize : function() {

		// 执行成功后刷新画面
		($("#success").val() && parent.Events.refreshClkAct())
				|| Page.coverLayer();
		
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
