<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="账户分佣" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="transferAction" method="post"  role="form" class="form-horizontal" >
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="status" value="${status}" />
					<input type="hidden" id="result" value="${result}" />
					<input type="hidden" id="accountId" name="accountId" value="${subCommissionForm.accountId}" />
					<input type="hidden" id="receiveUserId" name="receiveUserId" value="${subCommissionForm.receiveUserId}" />
					<input type="hidden" id="receiveUserName" name="receiveUserName" value="${subCommissionForm.receiveUserName}" />
<%-- 					<input type="hidden" id="receiveAccountId" name="receiveAccountId" value="${subCommissionForm.receiveAccountId}" />
 --%>					
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="accountId"> 
								<span class="symbol required"></span>转出电子账户号:
							</label>
							<div class="col-xs-10">
								<span id ="accountId" class="badge badge-inverse"> ${subCommissionForm.accountId} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0">
								<span class="symbol"></span>用户余额: 
							</label>
							<div class="col-xs-10">
								<span id ="balance" class="badge badge-inverse"> ${subCommissionForm.balance} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="receiveUserList"> 
								<span class="symbol required"></span>转入用户名:
							</label>
							<div class="col-xs-5">
								<%--<span id ="receiveUserName" class="badge badge-inverse"> ${subCommissionForm.receiveUserName} </span>--%>
									<select id="receiveUserList" name="receiveUserList" class="form-select2" style="width:100%" data-allow-clear="true" data-placeholder="">
										<option value="">请选择转入用户名</option>
										<%-- <c:forEach items="${subCommissionForm.recoverListBean }" var="recover" begin="0" step="1" varStatus="status">
											<option value="${recover.userId }" data-receiveaccountid="${recover.accountId }" data-receiveusername="${recover.userName }">
												<c:out value="${recover.userName }"></c:out>
											</option>
										</c:forEach> --%>
										<c:forEach items="${userNameList }" var="recover" begin="0" step="1" varStatus="status">
											<option value="${recover.userId }" data-receiveaccountid="${recover.userId }" data-receiveusername="${recover.username }" <c:if test="${recover.userId eq subCommissionForm.receiveUserId}">selected="selected"</c:if> >
												<c:out value="${recover.username }"></c:out>
											</option>
										</c:forEach>
									</select>
									<input type="hidden" id="receiveUserId" name="receiveUserId" value="${subCommissionForm.receiveUserId}" />
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="truename"> 
								<span class="symbol required"></span>转入姓名:
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="转入姓名" id="truename" name="truename" value="<c:out value="${subCommissionForm.truename}" />"   class="form-control"
									datatype="*1-20" errormsg="转入姓名不能超过20个字符！" maxlength="20" <c:if test="${ !empty subCommissionForm.truename}" > readonly="readonly" </c:if>  >
								<hyjf:validmessage key="truename" label="转入姓名"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="receiveAccountId">
								<span class="symbol required"></span>转入电子账户号:
							</label>
							<div class="col-xs-5">
								<%-- <span id ="reAccountId" class="badge badge-inverse"> ${subCommissionForm.receiveAccountId} </span> --%>
								<input type="text" placeholder="转入电子账户号:" id="receiveAccountId" name="receiveAccountId" value="<c:out value="${subCommissionForm.receiveAccountId}" />"   class="form-control"
									datatype="*1-20" errormsg="转入电子账户号不能超过20个字符！" maxlength="20" <c:if test="${ !empty subCommissionForm.receiveAccountId}" > readonly="readonly" </c:if>  >
								<hyjf:validmessage key="receiveAccountId" label="转入电子账户号"></hyjf:validmessage>
							</div>
						</div>
						<!--  -->
						
						<!--  -->
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="txAmount"> 
								<span class="symbol required"></span>转账金额:
							</label>
							<div class="col-xs-6">
								<input type="text" placeholder="转账金额" class="form-control input-sm" id="txAmount" name="txAmount" value="${subCommissionForm.txAmount}" maxlength="12"
										datatype="/^((([1-9]{1}[0-9]{0,9}){1})|(((([1-9]{1}[0-9]{0,9}){1})\.([0-9]{1,2}){1})|([0]{1}\.[1-9]{1}[0-9]?)|([0]{1}\.[0-9]{1}[1-9]{1})))$/" errormsg="金额输入错误！">
								<hyjf:validmessage key="txAmount" label="转账金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-2 control-label text-right padding-right-0" for="remark"> 
								<span class="symbol required"></span>说明:
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" id="remark" name="remark" class="form-control input-sm"
										maxlength="50" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为50字符" >${subCommissionForm.remark}</textarea>
								<hyjf:validmessage key="remark" label="说明"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="password"> <span
								class="symbol required"></span> 交易密码:
							</label>
							<div class="col-xs-6">
								<input type="password" placeholder="交易密码" id="password" name="password" class="form-control" maxlength="20" datatype="*1-20" errormsg="交易密码的长度不能大于20个字符！">
								<hyjf:validmessage key="password" label="交易密码 "></hyjf:validmessage>
							</div>
						</div>
					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/finance/subcommission/subcommissiondetail.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
