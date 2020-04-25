<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<jsp:include page="/head.jsp"></jsp:include>
<title>提现结果 - 汇盈金服官网</title></head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<section class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${cdn}/dist/images/result/chulizhong@2x.png" width="125" alt="" />
            	</div>
				<div class="result-mid">
					<h3>银行处理中！</h3>
				</div>
            	<div class="result-btn">
            		<div class="result-left">
            			<a href="${ctx}/user/pandect/pandect.do" class="import" itemid="lt1">返回个人中心</a>
            		</div>
            		<div class="result-right">
            			<a target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7" itemid="lt2">联系在线客服</a>
            		</div>
            		 <div class="clearboth"></div>
            	</div>
            </div>
        </div>
    </section>
	<jsp:include page="/footer.jsp"></jsp:include>
</body>
</html>