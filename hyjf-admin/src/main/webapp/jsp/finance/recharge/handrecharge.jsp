<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<shiro:hasPermission name="recharge:ADD">
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="充值" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
.panel-title {
	font-family: "微软雅黑"
}
</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="平台转账"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<h4>平台转账</h4>
				<hr />
				<div class="panel-scroll height-430">
					<form id="mainForm" action="handrechargeAction" method="post"
						role="form" class="form-horizontal">
						<%--  --%>
						<input type="hidden" id="success" value="${success}" />
						<input type="hidden" id="status" value="${status}" />
						<input type="hidden" id="result" value="${result}" />
						
						<div class="form-group">
							<label class="col-sm-2 control-label" for="username"> <span
								class="symbol required"></span>用户账号（会员用户名）
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="用户账号" id="username" name="username"
									class="form-control" datatype="*1-20"
									errormsg="用户账号的长度不能大于20个字符！" ajaxurl="checkTransfer">
								<hyjf:validmessage key="username" label="用户账号 "></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="money"> <span
								class="symbol required"></span> 操作金额（输入转账金额, 资金从商户账户中转账给会员）当前商户余额：${avlBal}   
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="操作金额" id="money" name="money"
									class="form-control" datatype="/^[1-9]+(\.\d+)?(%)?$|^-?0(\.\d+)?$|^[1-9]+[0-9]*(\.\d+)?(%)?$/"
									errormsg="金额只能是数字，长度1-10位数字！" maxlength="10">
								<hyjf:validmessage key="money" label="操作金额"></hyjf:validmessage>
							</div>
						</div>

						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span
								class="symbol required"></span> 备注说明（请输入操作理由）      
							</label>
							<div class="col-sm-10">
								<textarea id="remark" name="remark" placeholder="备注说明"
									class="ckeditor form-control" cols="10" rows="12"
									maxlength="200" datatype="*1-200" errormsg="备注说明的长度不能大于200个字符！"></textarea>
								<hyjf:validmessage key="remark" label="备注说明"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="title"> <span
								class="symbol required"></span> 交易密码
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="交易密码" id="password" name="password"
									class="form-control" datatype="*1-20"
									errormsg="交易密码的长度不能大于20个字符！">
								<hyjf:validmessage key="password" label="交易密码 "></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 提交</a> <a
									class="btn btn-o btn-primary fn-Cancel"><i
									class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"
			src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript'
			src="${webRoot}/jsp/finance/recharge/handrecharge.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
</shiro:hasPermission>
