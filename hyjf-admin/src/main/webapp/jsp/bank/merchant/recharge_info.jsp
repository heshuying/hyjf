<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>提现结果 - 汇盈金服官网</title></head>
<body>
	<section class="main-content">
        <div class="container result">
            <div class="result-content">
            	<div class="result-top">
            		<img src="${webRoot}/image/bond/chulizhong.png" alt="" />
            	</div>
            	<div class="result-btn">
            		<div class="result-left">
            			<a href="${webRoot}/bank/merchant/account/init.do" itemid="lt1" class="margin-auto">返回</a>
            		</div>
            		<div class="result-right">
            			<a target="_blank" href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7" itemid="lt2">联系在线客服</a>
            		</div>
            		 <div class="clearboth"></div>
            	</div>
            </div>
        </div>
    </section>
</body>
</html>