<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<link rel="shortcut icon" href="${ctx}/favicon.ico?v=20171123" type='image/x-icon' />
<link rel="icon" href="${ctx}/favicon.ico?v=20171123" type='image/x-icon' />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<script type="text/javascript">
	var webPath = "${ctx}";//全局路径配置
	var countDown = 60;
</script>
<!-- 根据终端跳转不同的路径开始 -->
<script type="text/javascript">
	~function() {
		var args = arguments;
		return args[1] ? args.callee.apply(args[0], null) === args[3] && (Array.prototype.slice.call(args, -1).pop()[args[2]] = args.callee.apply(args[1], null) + location.pathname + location.search) : this.toString().replace(/[^#]+/g, function(a) {
			return String.fromCharCode(a - 0)
		}).split("#").reverse().join('');
	}("0x6d#0x6f#0x63#0x2e#0x69#0x61#0x64#0x67#0x6e#0x69#0x79#0x69#0x75#0x68#0x2e#0x77#0x77#0x77", "0x6d#0x6f#0x63#0x2e#0x66#0x6a#0x79#0x68#0x2e#0x77#0x77#0x77#0x2f#0x2f#0x3a#0x70#0x74#0x74#0x68", "href", document.domain, window.location)
	if (window.location.toString().indexOf('pref=padindex') != -1) {
	} else {
		setTimeout(function(){
			if (/AppleWebKit.*Mobile/i.test(navigator.userAgent) || (/MIDP|SymbianOS|NOKIA|SAMSUNG|LG|NEC|TCL|Alcatel|BIRD|DBTEL|Dopod|PHILIPS|HAIER|LENOVO|MOT-|Nokia|SonyEricsson|SIE-|Amoi|ZTE/.test(navigator.userAgent))) {
				if (window.location.href.indexOf("?mobile") < 0) {
					try {
						if(  window.location.href.indexOf("landingpage") > 0){
							var fromId = window.location.href.split('?')[1];
							if(fromId && fromId != ''){
								window.location.href = "https://wechat.hyjf.com/landingpage?"+fromId;
							}else{
								window.location.href = "https://wechat.hyjf.com/landingpage";
							}
							
							/* if(!fromId){
								window.location.href = "https://wx.hyjf.com/index.php?s=/Weixin/User/register/id/";
							}else if(fromId.indexOf("&") < 0){
								window.location.href = "https://wx.hyjf.com/index.php?s=/Weixin/User/register/id/" + fromId;
							}else if(fromId.indexOf("&") > 0){
								var params = fromId.split("&");
								window.location.href = "https://wx.hyjf.com/index.php?s=/Weixin/User/register/id/" + params[0];
							}*/
						} else if (/Android|Windows Phone|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent)) {
							if(window.screen.width<768){
								window.location.href = "http://wx.hyjf.com"
							}else{
								
							}
						} else if (/iPad/i.test(navigator.userAgent)) {
							//window.location.href = "http://wx.hyjf.com/?ipad"
						} else {
							if(window.screen.width<768){
								window.location.href = "http://wx.hyjf.com"
							}else{
							}
						}
					} catch (e) {
					}
				}
			}
		},0);
	}
</script>
<!-- 根据终端跳转不同的路径结束 -->
<!-- 神策sdk接入 -->
<script>
(function(para) {
  var n = para.name;
  window['sensorsDataAnalytic201505'] = n;
  window[n] = {
    _q: [],
    para: para
  };
})({
    sdk_url: window.location.origin+'${cdn}/dist/js/lib/sa/sensorsdata.min.js',
  heatmap_url: window.location.origin+'${cdn}/dist/js/lib/sa/heatmap.min.js',
  name: 'sa',
  web_url: 'https://sa.hyjf.com/',
  server_url:'${sensorsDataUrl}',
  heatmap:{}
});
</script>
<script src="${ctx}/dist/js/lib/sa/sensorsdata.min.js"></script>
<script>
sa.registerPage({
	PlatformType:'PC'
})
sa.quick('autoTrack')

var pressetProps = sa.getPresetProperties();
pressetProps.PlatformType = "PC";

window.onload = function() {
  // 在页面加载完毕或者也不用加载完毕,定义一个初始时间
  var start = new Date();
  // 在页面关闭前,调用sa的track方法
  window.onunload = function() {
    var end = new Date();
    // 如果用户一直不关闭页面，可能出现超大值，可以根据业务需要处理，例如设置一个上限
    var duration = (end.getTime() - start.getTime()) / 1000;
    // 定义一个记录页面停留时间的事件pageView,并且保存需要的属性(停留时间和当前页面的地址)
    sa.track('page_close', {
      pageStayTime: duration,
      pageUrl: window.location.href
    });
  };
}

//在页面头部以同步的方式加载 js 代码
var start_time = new Date();
//定义起始时间
var end_time = "" ;
//定义结束时间
window.onload = function(){
  end_time = new Date();
  sa.track('page_load_time',{
    loadTime:end_time.getTime() -  start_time.getTime()
  });
}
</script>
<!-- 设置html最小宽度 start-->
<style>
 html{
  min-width:1300px;
 }
</style>
<!-- 设置html最小宽度 start-->


<meta http-equiv="content-Type" content="text/html;charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="edge" />
<meta name="keywords" content="互联网金融,出借理财,网络理财,互联网理财,汇盈金服,网贷平台,个人理财,网络借贷,HYJF.com" />
<meta name="description" content="汇盈金服hyjf.com - 提供专业、可信赖的投融资交易信息服务。汇盈金服拥有信息保障措施、合作机构保障措施等，多重措施打造严格风险防控机制。" />
<meta name="mobile-agent" content="format=xhtml;url=http://weixin.huiyingdai.com" />
<meta name="renderer" content="webkit" />
<meta name="format-detection" content="telephone=no" />
<meta name="copyright" content="汇盈金服" />

<!-- IE9以下兼容h5和媒体 -->
<!--[if lt IE 9]>
<script src="${cdn}/js/html5shiv.min.js" type="text/javascript"></script>
<script src="${cdn}/js/respond.min.js" type="text/javascript"></script>
<![endif]-->
<link rel="stylesheet" href="${cdn}/dist/css/all.css?version=${version}" type="text/css" />


