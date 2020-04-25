<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
     <article class="main-content" style="padding-top: 0;">
        <div class="container-investsuccess">
            <!-- start 内容区域 -->   
                <div class="auto-tender-success">
                    <div class="top-attr">
                        <img src="../dist/images/result/success-img2@2x.png" width="126">
                        <p class="top-title value">恭喜您，自动投标授权成功！</p>
                    </div>
                    <div class="mid-attr">
                        <ul>
                            <li>
                                <div><span>10</span>秒后自动跳转</div>
                            </li>
                        </ul>
                    </div>
                    <div class="bom-attr">
                        <button type="button" class="submit-btn">完成</button>
                    </div>
                </div>
                <div class="invest-bom-bg"> </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
		var s=10;
		var el=$('.auto-tender-success .mid-attr li span')
		el.text(s);
		var time=window.setInterval(function(){
			s--;
			if(s>0||s==0){
				el.text(s);
			}else{
				location.href='https://www.baidu.com/'
			}
		},1000)
   	</script>
</body>
</html>