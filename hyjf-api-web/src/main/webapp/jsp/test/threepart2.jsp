<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<html>
<head>
<link href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<style>
.panel-body a {
	height:22px;
	line-height:22px;
}
.panel-body span {
	display:block;
}
</style>
</script>
</head>
<body>
	<h1 class="text-center col-sm-12" style="margin-bottom:30px;">汇晶社网站模拟页</h1>
	<br/><br/><br/>
	
	<form name="form" method="post">
	<div class="col-sm-10 ">
		<div id="result" class=" panel panel-default ">
			<table class="table">
				<thead style="background-color: #5bc0de;">
					<tr>
						<td>参数名</td>
						<td>参数值</td>
					</tr>
				</thead>
				<tbody id="resultBody">
					<tr>
						<td>汇晶社用户ID</td>
						<td><input id="bindUniqueIdScy" name="bindUniqueIdScy" value=""/></td>
					</tr>
					<tr>
						<td>汇晶社ID</td>
						<td><input id="pid" name="pid" value=""/></td>
					</tr>
					<tr>
						<td>通知url</td>
						<td><input id="retUrl" name="retUrl" value=""/></td>
					</tr>
					<tr>
						<td>当前时间戳</td>
						<td><input id="timestamp" name="timestamp" value=""/></td>
					</tr>
					<tr>
						<td>签名</td>
						<td><input id="chkValue" name="chkValue" value=""/></td>
					</tr>
					
- 					<tr> 
 						<td>调用接口Url</td> 
 						<td><input id="url" name="url" value=""/></td> 
 					</tr>  

<!-- 					<tr> -->
<!-- 						<td>同步回调路径</td> -->
<!-- 						<td ><input id="retUrl" name="retUrl" value=""/></td> -->
<!-- 					</tr> -->
<!-- 					<tr> -->
<!-- 						<td>异步回调路径</td> -->
<!-- 						<td ><input id="bgRetUrl" name="bgRetUrl" value=""/></td> -->
<!-- 					</tr>  -->
<!-- 					<tr> -->
 						<td><input type="button" value="提交" onClick=send()></td>
				</tbody>
			</table>
		
		</div>
	</div>
	</form>
	<script language=javascript>
		function send()
		  {
		    document.form.action=document.getElementById("url").value
		    document.form.submit()
		   }
	</script>
</body>
</html>
