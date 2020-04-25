//设置HTML font-size 可以使用rem
(function(){
	var width = $(window).width()/12.42;
	$("html").css("font-size",width+"px");
	$(window).resize(function(){
		var width = $(window).width()/12.42;
		$("html").css("font-size",width+"px");
	})
})()
var rdf = {
	locationOrigin : location.origin,
	locationHost : location.host,
	flag : null,// flag 0生产环境 
    rdf :'rdf',
    //h5跳转
    jumpH5 : function(url,index,encode){
				//encode ：1对URL编码， 0不需要
				if(!index){
					index = 0;
				}
				if(!encode){
					encode = 0;
				}
				$(".rdf-jumpH5").eq(index).prop("href",rdf.rdf+'://jumpH5/?{"url": "'+locationOrigin+url+'","encode":'+encode+'}');
			},
	//获取URL参数			
	getQueryString : function(name) {
					    var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
					    var r = window.location.search.substr(1).match(reg);
					    if (r != null) {
					        return unescape(r[2]);
					    }
					    return null;
					},
	//格式化为万元					
	formatNum10000 : function(){
						var formatNum10000 = $(".formatNum10000")
						var length = $(".formatNum10000").length;
						for(var i=0;i<length;i++){
							var textNum = (+formatNum10000.eq(i).text()/10000)+"万元";
							formatNum10000.eq(i).text(textNum)
						}
					}
}

/*locationOrigin.indexOf("app.zhugedai.com")!==-1?flag = 0:flag = 1
if(flag === 0){
	rdf = "rdf"
}else{
	rdf = "bdgwdev"
}*/
//各种跳转
$(".rdf-regist").prop("href",rdf.rdf+'://jumpRegist/?');//跳注册
$(".rdf-callCener").prop("href",rdf.rdf+'://callCenter/?{"number": "4008067877"}');//拨打客服电话用此操作指令
$(".rdf-closeView").prop("href",rdf.rdf+'://closeView/?');//关闭当前H5页面用此操作指令
$(".rdf-jumpLogin").prop("href",rdf.rdf+'://jumpLogin/?');//跳转登录界面用此操作指令
$(".rdf-jumpMine").prop("href",rdf.rdf+'://jumpMine/?');//跳转我的
$(".rdf-jumpIndexPage").prop("href",rdf.rdf+'://jumpIndexPage/?');//跳转首页
$(".rdf-jumpProductList").prop("href",rdf.rdf+'://jumpProductList/?');//跳转到产品列表
$("#qrcodeValue").val(locationOrigin+'/bandao-app/wap/registerPage?referrer='+$("#userId").val())//二维码赋值val
$(".rdf-myPartner").each(function(){$(this).prop("href",rdf.rdf+'://jumpMyPartner/?{"type":1}');})//jumpMyPartner 1代表我的合伙人   2代表奖励记录
$(".rdf-myReward").each(function(){$(this).prop("href",rdf.rdf+'://jumpMyPartner/?{"type":2}');})/*跳转到原生分享*/
$(".rdf-partnerShare").each(function(){$(this).prop("href",rdf.rdf+'://jumpShared/?{"title":"'+shareTitle+'","content":"'+shareContent+'","imageUrl":"https://'+locationHost+'/bandao-app/upfiles'+shareImgUrl+'","url":"'+locationOrigin+shareUrl+'?referrer='+$("#userId").val()+'"}');})


