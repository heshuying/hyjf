<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${ctx}/css/active.css"/>
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
<title>感恩回馈 - 汇盈金服官网</title>
</head>
<body>
<div class="telInput">
	<div class="telInputImg">
		<img src="${cdn}/img/input_tel.jpg"/>
	</div>
	<form action="/user/preregist/preregistSubmit" method="post">
		<div class="telInputCont">
			<input type="number" class="inputTelNum" id="phone" oninput="if(value.length>11)value=value.slice(0,11)" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入您的手机号码" />
			<input type="hidden" name="from" id="from" value="${from}" />
			<input type="hidden" name="utmId" id="utmId" value="${utmId}" />
			<input type="hidden" name="platform" id="platform" value="微官网" />
			<a class="inputTel-btn" href="#" id="downloadBtn">点击下载领取</a>
			<p class="inputTel-txt">* 提示：请与注册投资使用的手机号保持一致。</p>
		</div>
	</form>
</div>
<div class="errorTip">
	<div class="inputError"></div>
</div>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script>
function verIsMobile(value) {
    //验证是否是手机号
    var length = value.toString().length;
    return /^[1][3,4,5,6,7,8,9][0-9]{9}$/.test(value);
}
	$(function(){
		/* 验证手机号 */
		$("#downloadBtn").click(function(){
			
			  var myreg = /^(((13[0-9]{1})|(14[0-9]{1})|(15[0-3,5-9]{1})|(17[0,1,3,5-8]{1})|(18[0-9]{1}))+\d{8})$/; 
			  var phone = $('#phone').val().trim();
		
			  if (phone==""){
				  $('.errorTip').show();
			  	  $('.inputError').empty().append("请输入手机号！");
			  	  setTimeout(function(){$('.errorTip').fadeOut();},3000);
		          return;
			  }else if(!verIsMobile(phone)){
				  $('.errorTip').show();
			      $('.inputError').empty().append("手机号格式错误,请输入正确的手机号！");
			      setTimeout(function(){$('.errorTip').fadeOut();},3000);
		          return;
			  } else{main();
			     $('.inputError').empty();
	        	 $('.errorTip').hide();
			  
			  }
			});
		/* 点击下载领取 */
		    function main(){
			    var _url = "${ctx}/user/preregist/preregistSubmit.do";//后台
				var _mobile = $.trim($(".inputTelNum").val());
				var _from = $("#from").val();
				var _utmId = $("#utmId").val();
				var _platform = $("#platform").val();
				$.ajax({
					type:"post",
					url:_url,
					data:{"mobile":_mobile,"from":_from,"utmId":_utmId,"platform":_platform},
					async:true,
					dataType:"json",
					success:function(data){
						if("0" == data.resultFlag){
							window.location.href = 'http://a.app.qq.com/o/simple.jsp?pkgname=com.huiyingdai.apptest';
						}
					}			
				});
			  }
	   });
</script>
<div style="display:none">
	<script type="text/javascript">
 	var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
 	document.write(unescape("%3Cspan id='cnzz_stat_icon_1259483746'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1259483746%26online%3D1%26show%3Dline' type='text/javascript'%3E%3C/script%3E"));
	</script>
</div>
</body>
</html>