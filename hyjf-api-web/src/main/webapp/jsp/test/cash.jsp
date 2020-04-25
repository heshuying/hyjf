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
	<h1 class="text-center col-sm-12" style="margin-bottom:30px;">免密提现</h1>
	<br/><br/><br/>
	
	<form id="form1" action="${ctx}/server/user/nonCashWithdraw/cashtest.do" method="post" enctype="application/json">
	<input type="hidden" value="test" name="chkValue" id="chkValue"/>
	
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
						<td>当前时间戳</td>
						<td id="retMsg"><input id="timestamp" name="timestamp" value=""/></td>
					</tr>
					<tr>
						<td>第三方操作平台</td>
						<td>
							<select id="channel" name="channel">
								<option value="000001">APP</option>
								<option value="000002">PC</option>
								<option value="000003">Wechat</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>用户银行电子账号</td>
						<td id="retMsg"><input id="accountId" name="accountId" value=""/></td>
					</tr>
					<tr>
						<td>银行卡卡号</td>
						<td id="retMsg"><input id="cardNo" name="cardNo" value=""/></td>
					</tr>
					<tr>
						<td>提现金额</td>
						<td ><input id="account" name="account" value=""/></td>
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
	$("#timestamp").val(Date.parse( new Date() ).toString().substr(0,10));
	function getChkValue(){
		$.ajax({
	        type: "POST",
	        url: "${ctx}/server/ChkValue/getChkValue.do",
	        data : {
	        	"timestamp":$("#timestamp").val(),
	        	"channel":$("#channel").val(),
	        	"accountId":$("#accountId").val(),
	        	"cardNo":$("#cardNo").val(),
	        	"account":$("#account").val(),
	        	"type":"cash"
	        }, 
	        dataType: "json",
	        success: function (data) {
	    		$("#chkValue").val(data.chkValue);
	    		$("#form1").submit();
	        }
	    });//ajax end
	}
	</script>
	
</body>
</html>
