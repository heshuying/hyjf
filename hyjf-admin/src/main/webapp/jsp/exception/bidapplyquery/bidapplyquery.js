var
//--------------------------------------------------------------------------------------------------------------------------------
/* 事件动作处理 */
Events = {
	// 确认按键单击事件绑定
	confirmClkAct : function() {
		if (Page.validate.check(false)) {
			Page.submit();
		}
	},
	// 取消按键单击事件绑定
	cancelClkAct : function() {
		$("#accountId").val("");
		$("#orgOrderId").val("");
		$("#result").html("");
	},
	formatResJSON : function(){
		var resdata= $("#result").data('res');
		var str = '<table class="table table-striped table-bordered table-hover">';
		if(resdata != ''){
			str+='<tr>'
	        +'    <th class="center">retCode</td>'
	        +'    <td>响应代码</td>'
	        +'    <td>'+resdata.retCode+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">retMsg</td>'
	        +'    <td>响应信息</td>'
	        +'    <td>'+resdata.retMsg+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">txCode</td>'
	        +'    <td>交易代码</td>'
	        +'    <td>'+resdata.txCode+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">accountId</td>'
	        +'    <td>电子账号</td>'
	        +'    <td>'+resdata.accountId+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">name</td>'
	        +'    <td>姓名</td>'
	        +'    <td>'+resdata.name+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">productId</td>'
	        +'    <td>标的号</td>'
	        +'    <td>'+resdata.productId+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">txAmount</td>'
	        +'    <td>投标金额</td>'
	        +'    <td>'+resdata.txAmount+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">forIncome</td>'
	        +'    <td>预期收益</td>'
	        +'    <td>'+resdata.forIncome+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">buyDate</td>'
	        +'    <td>投标日期</td>'
	        +'    <td>'+resdata.buyDate+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">state</td>'
	        +'    <td>状态</td>'
	        +'    <td>'+resdata.state+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">authCode</td>'
	        +'    <td>授权码</td>'
	        +'    <td>'+resdata.authCode+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">bonusAmount</td>'
	        +'    <td>抵扣红包金额</td>'
	        +'    <td>'+resdata.bonusAmount+'</td>'
	        +'</tr>'
			str+='</table>';
		    $("#result").html(str+"<code><pre>" + JSON.stringify(resdata,null,4) + "</pre></code>");
		}
	}
};

//--------------------------------------------------------------------------------------------------------------------------------
/* 画面对象 */

$.extend(Page, {

	// 初始化画面事件处理
	initEvents : function() {
		// 确认按键单击事件绑定
		$(".fn-Confirm").click(Events.confirmClkAct);
		// 取消按键单击事件绑定
		$(".fn-Cancel").click(Events.cancelClkAct);
		Events.formatResJSON();
	},
	// 画面初始化
	initialize : function() {
		// 初始化表单验证
		Page.validate = Page.form.Validform({
			tiptype : 3
		});
	}
}),

Page.initialize();
