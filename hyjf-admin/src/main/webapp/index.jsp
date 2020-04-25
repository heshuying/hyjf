<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/font-awesome/4.2.0/css/font-awesome.min.css" rel="stylesheet">
<link href="http://cdn.bootcss.com/bootstrap/3.3.4/css/bootstrap.min.css" rel="stylesheet">
<style>
.panel-body a {
	height: 22px;
	line-height: 22px;
}

.panel-body span {
	display: block;
}
</style>
<script src="http://test.chinapnr.com/statics//muser/scripts/jquery.min.js"></script>
<script src="http://localhost:8080/hyjf-pay/js/hyjf.pay.chinapnr.js"></script>

<script>
	
</script>
</head>
<body>
	<h1 class="text-center col-sm-12" style="margin-bottom: 30px;">后台测试画面</h1>
	<div class="col-sm-1">&nbsp;</div>
	<div class="col-sm-10" style="margin-left: 100px;">
		<div class="panel panel-default">
			<div class="panel-body">
				<div style="border-bottom: 1px solid #ccc; font-size: 16px;" class="font16">画面列表</div>
				<span class="glyphicon glyphicon-triangle-right" aria-hidden="true"> 
					<a href="http://localhost:8080/hyjf-admin/maintenance/permissions/init" id="permissions" target="_permissions" style="margin-bottom: 10px;">权限管理</a>
				</span>
			</div>
		</div>
	</div>

	<div class="col-sm-1">
		&nbsp;
		<form id="form" action="" method="post"></form>
	</div>


</body>
</html>

