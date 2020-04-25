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
<script src="http://test.chinapnr.com/statics//muser/scripts/jquery.min.js"></script>

<script>
</script>
</head>
<body>
	<h1 class="text-center col-sm-12" style="margin-bottom:30px;">免密提现授权</h1>
	<br/><br/><br/>
	
	<form id="form1" action="${ctx}/server/userCashauth/cashWithdrawalTest.do" method="post" enctype="application/json">
	<input type="hidden" value="Dm5oJMwSp47k2om7s1g3LNMJwpB97FF8XLaEU2xgEzHZ3GC0uSDH7OPyvEutU5vevQ8NxVh5+3kNlQJWgKzn3R+czSRsMcDq3QWR3YczEMoBgiGXmM963hA/GmzoUWwG7fDUwyYTOZnA9LvVFku7p1gJOKondscjd8g2CMRkjDWcGmptg6HBdaNSCPtKw/wvA6SAInJmgsOUT6N3KlacjfzJ7IJStRj412e1FotdHmyc5NFzrVhRStd4bsReM9M7oAxh3hC3NcC3hXRGfA7Y4lJPe7PnfZq9M/RW+8zLduL5uXmo/Z0KKrkUitqwImchFAvVGwR39e/I9Gq3ciFY5w==" name="chkValue" id="chkValue"/>
	<input type="hidden" value="" name="autoBid" id="autoBid"/>
	<input type="hidden" value="" name="autoTransfer" id="autoTransfer"/>
	<input type="hidden" value="" name="directConsume" id="directConsume"/>
	<input type="hidden" value="1" name="agreeWithdraw" id="agreeWithdraw"/>
	
	<div class="col-sm-10 ">
		<div id="result" class=" panel panel-default ">
			<table class="table">
				<thead style="background-color: #5bc0de;">
					<tr>
						<td>参数名</td>
						<td>参数值${ctx}</td>
					</tr>
				</thead>
				<tbody id="resultBody">
					<tr>
						<td>第三方操作平台</td>
						<td>
							<select id="channel" name="channel">
								<option value="000002">PC</option>
								<option value="000001">APP</option>
								<option value="000003">Wechat</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>机构编号</td>
						<td id="instCode"><input id="instCode" name="instCode" value="10000014"/></td>
					</tr>
					<tr>
						<td>用户银行电子账号</td>
						<td id="retMsg"><input id="accountId" name="accountId" value=""/></td>
					</tr>
					<tr>
						<td>维护标识</td>
						<td id="retMsg"><input id="bitMap" name="bitMap" value="1"/></td>
					</tr>
					<tr>
						<td>同步回调路径</td>
						<td ><input id="retUrl" name="retUrl" value=""/></td>
					</tr>
					<tr>
						<td>异步回调路径</td>
						<td ><input id="notifyUrl" name="notifyUrl" value=""/></td>
					</tr>
					<tr>
						<td>返回交易页面地址</td>
						<td ><input id="transactionUrl" name="transactionUrl" value=""/></td>
					</tr>
					<tr>
						<td>忘记密码地址</td>
						<td ><input id="forgotPwdUrl" name="forgotPwdUrl" value=""/></td>
					</tr>
					<tr>
						<td><input type="button" value="提交" onclick="getChkValue()"/></td>
					</tr>
				</tbody>
			</table>
		
		</div>
	</div>
	</form>
	<script>
	function getChkValue(){
		$("#form1").submit();
	}
	</script>
	
</body>
</html>
