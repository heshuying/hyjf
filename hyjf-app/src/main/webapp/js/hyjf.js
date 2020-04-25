	/*添加H5页面调用原生代码规则*/
	//获取URL中version的值
	function GetQueryString(name){
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	//某些页面需要userID
	var userId = $("#userId").val() || '';
	var versionCode;//url 中version的值或者 后台传的version值
	if(typeof versionStr != 'undefined'){
		versionCode = versionStr;
	}
	if(versionCode == undefined || versionCode == null){
		versionCode = GetQueryString("version")+'';
	}
	var hyjfArr = {
			flag:+versionCode.substring(versionCode.lastIndexOf(".")+1),
			locationOrigin:location.origin,
			hyjf:jumpcommond,
			//定义弹层
			popShow:function(element,text){
				var pop = '<div class="activeOutdatePop">'+text+'</div>';
				$('body').append(pop);
				element.click(function(){
					$(".activeOutdatePop").fadeIn().delay(1500).fadeOut();
				});
			}
	}
	//判断渠道号
	if(!hyjfArr.hyjf){
		switch(hyjfArr.flag){
		case 79:
			hyjfArr.hyjf = "hyjf";
			break;
		case 39:
			hyjfArr.hyjf = "hyjf";
			break;
		case 150:
			hyjfArr.hyjf = "hyjfZYB";
			break;
		case 151:
			hyjfArr.hyjf = "hyjfZZB";
			break;
		case 152:
			hyjfArr.hyjf = "hyjfZNB";
			break;
		case 153:
			hyjfArr.hyjf = "hyjfYXB";
			break;
		case 149:
			hyjfArr.hyjf = "hyjfTEST";
			break;
		default:
			hyjfArr.hyjf = "hyjf";
		break;
		}
	}
	//跳转首页
	$(".hy-jumpIndexPage").prop("href",hyjfArr.hyjf+'://jumpIndexPage/?');
	//跳转银行卡页面
	$(".hy-jumpBankCard").prop("href",hyjfArr.hyjf+'://jumpBankCard/?');
	//关闭当前H5页面用此操作指令
	$(".hy-closeView").prop("href",hyjfArr.hyjf+'://closeView/?');
	//跳转登录界面用此操作指令
	$(".hy-jumpLogin").prop("href",hyjfArr.hyjf+'://jumpLogin/?');
	//跳转注册
	$(".hy-jumpRegister").prop("href",hyjfArr.hyjf+'://jumpRegister/?');
	//跳转到我的投资-投资中tab
	$(".hy-jumpInTheInvestment").prop("href",hyjfArr.hyjf+'://jumpInTheInvestment/?');
	//跳转到债券转让-已承接tab
	$(".hy-jumpTransfer").prop("href",hyjfArr.hyjf+'://jumpTransfer/?');
	//跳转到投资
	$(".hy-jumpInvest").prop("href",hyjfArr.hyjf+'://jumpInvest/?');
	//返回账户
	$(".hy-jumpMine").prop("href",hyjfArr.hyjf+'://jumpMine/?');
	//跳转到个人中心我的计划
	$(".hy-jumpMyPlan").prop("href",hyjfArr.hyjf+'://jumpMyPlan/?');
	// 交易明细
	$(".hy-jumpTransactionDetail").prop("href",hyjfArr.hyjf+'://jumpTransactionDetail/?');
	//设置手势密码
	$(".hy-jumpSetPassword").prop("href",hyjfArr.hyjf+'://jumpSetPassword/?');
	//充值
	$(".hy-jumpRecharge").prop("href",hyjfArr.hyjf+'://jumpRecharge/?');
	//新手汇
	$(".hy-jumpXSH").prop("href",hyjfArr.hyjf+'://jumpXSH/?');
	//反馈
	$(".hy-jumpFeedback").prop("href",hyjfArr.hyjf+'://jumpFeedback/?');
	//继续债转
	$(".hy-jumpCanTransferList").prop("href",hyjfArr.hyjf+'://jumpCanTransferList/?');
	//查看转让列表
	$(".hy-jumpHZRList").prop("href",hyjfArr.hyjf+'://jumpHZRList/?');
	//跳转到产品列表
	$(".hy-jumpProductList").prop("href",hyjfArr.hyjf+'://jumpProductList/?');
	//二维码赋值val
	$("#qrcodeValue").val(hyjfArr.locationOrigin+'/bandao-app/wap/registerPage?referrer='+$("#userId").val())
	//jumpMyPartner 1代表我的合伙人   2代表奖励记录
	$(".hy-myPartner").each(function(){$(this).prop("href",hyjfArr.hyjf+'://jumpMyPartner/?{"type":1}');})
	$(".hy-myReward").each(function(){$(this).prop("href",hyjfArr.hyjf+'://jumpMyPartner/?{"type":2}');})
	/*跳转到原生分享*/
	$(".hy-partnerShare").each(function(){$(this).prop("href",hyjfArr.hyjf+'://activityToShare/?{"title":"汇盈金服高收益专业金融服务平台","content":"灵活投资，稳健安全，尽享投资乐趣，快来体验专属于你的指尖财富","image":"https://www.hyjf.com/data/upfiles/image/20140617/1402991818340.png","url":"https://wx.hyjf.com/index.php?s=/Weixin/Activites/traffic/page_id/2119/id/'+userId+'.html"}');})
	//h5跳h5
	var jumpH5 = function(url,index,encode,needPop){
		if(!index){
			var index = 0;
		}
		if(!needPop){
			var needPop = 0;
		}
		//encode ：1对URL编码， 0不需要,needPop:是否需要pop  1需要 0不需要
		if(!encode || encode===0){
			$(".hy-jumpH5").eq(index).prop("href",hyjfArr.hyjf+'://jumpH5/?{"url": "'+hyjfArr.locationOrigin+url+'"}');
		}else if(encode===1){
			$(".hy-jumpH5").eq(index).prop("href",hyjfArr.hyjf+'://jumpH5Encode/?{"url": "'+hyjfArr.locationOrigin+url+'"}');
		}
		
	}
	//设置电话
	var callCenter = function(number){
		$(".hy-callCener").prop("href",hyjfArr.hyjf+'://callCenter/?{"number": "'+number+'"}');
	}
	var jumpProductDetail = function(productId){
		$(".hy-jumpProductDetail").prop("href",hyjfArr.hyjf+'://jumpProductDetail/?{"url": "'+productId+'"}');
	}
	/*添加H5页面调用原生代码规则结束*/

//// 获取是否刷新flg
//function getRefreshFlg(){
//	return window.localStorage.refreshFlg != undefined ? window.localStorage.refreshFlg : false;
//}
//// 设置是否刷新flg
//function setRefreshFlg(flg){
//	window.localStorage.refreshFlg = flg;
//}	
///* 
// * 检测是否需要刷新页面标识	根据refreshflg刷新页面
// */
//function refreshByFlg(){
//	var refreshFlg = getRefreshFlg();
//	if(refreshFlg == true){
//		setRefreshFlg(false);
//		window.location.href = window.location.href;
//	}
//}
	
/*
 *   与原生搭桥 
 *   调用方法
 * setupWebViewJavascriptBridge(function(bridge) {
 *			bridge.callHandler('reloadUserData', function responseCallback(responseData) {
 *				console.log("JS received response:", responseData)
 *			})
 *		})
 * 
 */
function setupWebViewJavascriptBridge(callback) {
	if (window.WebViewJavascriptBridge) { return callback(WebViewJavascriptBridge); }
	if (window.WVJBCallbacks) { return window.WVJBCallbacks.push(callback); }
	window.WVJBCallbacks = [callback];
	var WVJBIframe = document.createElement('iframe');
	WVJBIframe.style.display = 'none';
	WVJBIframe.src = 'https://__bridge_loaded__';
	document.documentElement.appendChild(WVJBIframe);
	setTimeout(function() { document.documentElement.removeChild(WVJBIframe) }, 0)
}