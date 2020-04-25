<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>感恩回馈 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" href="${ctx}/css/ganen.css" type="text/css" />
		<link rel="stylesheet" type="text/css" href="${ctx}/css/footer-adjust.css" />
	</head>
	<body>
          <div class="ganen_wrapper">
               <div class="section section1"></div>
               <div class="section section2">
                    <div class="btn-box">
                         <div class="qr">
                              <img src="${cdn}/img/active_ganen/download_qr_1467007677.png" width="178" height="178" alt="">
                         </div>
                         <a href="javascript:;" class="dw-btn android"></a>
                         <a href="javascript:;" class="dw-btn ios"></a>
                    </div>
               </div>
               <div class="section section3">
                    <div class="ticket_box" id="tBox1">
                         <ul class="bd">
                         </ul>
                         
                    </div>
                    <div class="rules">
                         1.单笔投资满<span>5万</span>元即可获得迪士尼门票<span>1张</span>； <br/>
                         2.投资项目仅限于投资“<span>汇直投</span>”“<span>尊享汇</span>”产品。
                    </div>
                    <div class="rules">
                         1.单笔投资<span>1万</span>元即可获得<span>50</span>元话费;  <br/>
                         2.投资项目限于“<span>汇直投</span>”“<span>尊享汇</span>”。 
                    </div>
                    <div class="ticket_box ticket_box2" id="tBox2">
                         <ul class="bd">
                         </ul>
                    </div>
                    <div class="clearfix"></div>
                    <div class="rules_btm" style="margin-top:5px;">
						1.活动时间：<span>2016.06.25 至 2016.07.10</span> <br/>
						2.活动对象：仅限活动期间在<span>“汇盈金服”APP端成功注册的用户</span>，投资不限终端；<br/>
						3.活动奖励计算：按照活动期间单笔最高投资额，发放活动奖励；两个奖励只能获取一个，且只发放一次；<br/>
						4.活动兑奖：活动结束后第三个工作日起，统计用户获得奖励并短信通知，7个工作日内工作人员联系获奖用户并确认收货信息，即送奖品；<br/>
						* 本次活动最终解释权归惠众商务顾问 (北京)有限公司所有。
                    </div>
               </div>
               <div class="section section4"></div>
               <div class="section section5">
                    <div class="btn-box">
                         <div class="qr">
                              <img src="${cdn}/img/active_ganen/download_qr_1467007677.png" width="178" height="178" alt="">
                         </div>
                         <a href="javascript:;" class="dw-btn android"></a>
                         <a href="javascript:;" class="dw-btn ios"></a>
                    </div>
               </div>
               <div class="section section6">市场有风险 投资需谨慎</div>
          </div>
	<%@ include file="/footer.jsp"%>
     <script src="${ctx}/js/doT.min.js"></script>
     <script src="${ctx}/js/fill.js"></script>
     <script src="${ctx}/js/jquery.SuperSlide.2.1.1.js"></script>
     
     <script id="tmpl-tBox" type="text/x-dot-template">
		{{? it.resultFlag == 0 }}
			{{~ it.data : v : index }}
        	    <li><em>{{=v.username}}</em> <span>{{=v.mobile}}</span></li>
        	{{~}}
        {{?}}
	</script>
	<script>
	var param1 = {"reward":"门票"};
	var param2 = {"reward":"话费"};
	/* $("#tBox1 ul").doT("#tmpl-tBox", {
		"data":[{
			"mobile":"151********",
			"username":"缝**"
		}],
		"msg":"查询完成",
		"resultFlag":"0"
	}); */
    $.fillTmplByAjax(webPath+"/user/preregistchannelexclusiveactivity/getrewardlist.do",param1, "#tBox1 ul", "#tmpl-tBox", null);
    $.fillTmplByAjax(webPath+"/user/preregistchannelexclusiveactivity/getrewardlist.do",param2, "#tBox2 ul", "#tmpl-tBox", null);
	</script>
     <script>
          $("#tBox1").slide({"mainCell":"ul.bd",vis:4,"autoPlay":true,effect:"topMarquee",interTime:80,trigger:"click"})
          $("#tBox2").slide({"mainCell":"ul.bd",vis:4,"autoPlay":true,effect:"topMarquee",interTime:80,trigger:"click"})
     </script>
     <div style="display:none">
     	<script type="text/javascript">
	     	var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
	     	document.write(unescape("%3Cspan id='cnzz_stat_icon_1259483746'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1259483746%26online%3D1%26show%3Dline' type='text/javascript'%3E%3C/script%3E"));
     	</script>
     </div>
	</body>
</html>
