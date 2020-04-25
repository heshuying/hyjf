<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

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
<script src="http://test.chinapnr.com/statics//muser/scripts/jquery.min.js"></script>

<script>
</script>
</head>
<body>
	<h1 class="text-center col-sm-12" style="margin-bottom:30px;">汇付天下接口测试</h1>
	${content}
	<div class="col-sm-1" >&nbsp;
	<form id="form1" action="" method="post">
	</form></div>
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
						<td>txCode</td>
						<td id="CmdId">${bean.txCode }</td>
					</tr>
					<tr>
						<td>retCode</td>
						<td id="RespCode">${bean.retCode }</td>
					</tr>
					<tr>
						<td>retMsg</td>
						<td id="retMsg"><script>document.write(decodeURI("${bean.retMsg}"));</script></td>
					</tr>
					<tr>
						<td>sign</td>
						<td id="sign" style="word-break: break-all;word-wrap: break-word;">${bean.sign }</td>
					</tr>
					<tr>
						<td>resultBody</td>
						<td id="resultTd" style="word-break: break-all;word-wrap: break-word;" ><script>document.write(decodeURI("${bean.allParams}"));</script></td>
					</tr>
				</tbody>
			</table>


		</div>
	</div>
	
	<div class="col-sm-1">&nbsp;
	<form id="form" action="" method="post">
	</form>
	</div>


</body>
</html>
