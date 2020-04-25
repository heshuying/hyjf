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
		$("#orgTxDate").val("");
		$("#orgTxTime").val("");
		$("#orgSeqNo").val("");
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
	        +'    <td>响应描述</td>'
	        +'    <td>'+resdata.retMsg+'</td>'
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
	        +'    <th class="center">txAmount</td>'
	        +'    <td>交易金额</td>'
	        +'    <td>'+resdata.txAmount+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">orFlag</td>'
	        +'    <td>冲正撤销标志</td>'
	        +'    <td>'+resdata.orFlag+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">result</td>'
	        +'    <td>交易处理结果</td>'
	        +'    <td>'+resdata.result+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">acqRes</td>'
	        +'    <td>请求方保留</td>'
	        +'    <td>'+resdata.acqRes+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">success</td>'
	        +'    <td>返回值</td>'
	        +'    <td>'+resdata.success+'</td>'
	        +'</tr>'
	        +'<tr>'
	        +'    <th class="center">msg</td>'
	        +'    <td>返回信息</td>'
	        +'    <td>'+resdata.msg+'</td>'
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
