<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
	flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="管理" />
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
		<c:set var="jspEditType"
			value="${empty instConfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-430">
					<form id="mainForm"
						action="${empty instConfigForm.id ? 'insertAction' : 'updateAction'}"
						method="post" role="form" class="form-horizontal">
						<%-- 列表一览 --%>
						<input type="hidden" name="id" id="id" value="${instConfigForm.id }" />
						<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						<div class="form-group">
							<label class="col-sm-2 control-label" for="instName"> 
								<span class="symbol required"></span>资产来源 :
							</label>
							<div class="col-sm-10">
								<input type="text" placeholder="资产来源" id="instName" name="instName" value="<c:out value="${instConfigForm.instName}" />"  class="form-control"
									datatype="*1-20" errormsg="资产来源不能超过20个字符！" maxlength="20" />
								<hyjf:validmessage key="instName" label="资产来源"></hyjf:validmessage>
							</div>
						</div>
						<c:if test="${not empty instConfigForm.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="instCode"> 
									资产来源编号 :
								</label>
								<div class="col-sm-10">
									<input type="text" disabled="disabled" placeholder="资产来源编号" id="instCode" name="instCode" value="<c:out value="${instConfigForm.instCode}" />"  class="form-control" />
								</div>
							</div>	
						</c:if>
						<c:if test="${empty instConfigForm.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="instCode">
									<span class="symbol required"></span>资产来源编号 :
								</label>
								<div class="col-sm-10">
									<input type="text" placeholder="资产来源编号" id="instCode" name="instCode" value="<c:out value="${instConfigForm.instCode}" />"  class="form-control"
										   datatype="*1-20" errormsg="资产来源编号不能超过20个字符！" maxlength="20" ajaxurl="isExists?capitalUsed=<c:out value="${instConfigForm.instCode}"/>" />
									<hyjf:validmessage key="instCode" label="资产来源编号"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<%--
						<c:if test="${empty instConfigForm.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="capitalToplimit"> <span class="symbol required"></span>发标额度上限</label>
								<div class="col-sm-10">
									<div class="input-group">
										<input type="text" placeholder="发标额度上限" id="capitalToplimit" name="capitalToplimit" value="<c:out value="${instConfigForm.capitalToplimit}" />"  class="form-control"
											datatype="/^\d{1,10}(\.\d{1,2})?$/" errormsg="发标额度上限必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="capitalToplimit" label="发标额度上限"></hyjf:validmessage>
								</div>
							</div>	
						</c:if>
						<c:if test="${not empty instConfigForm.id}">	
							<div class="form-group">
								<label class="col-sm-2 control-label" for="capitalToplimit"> <span class="symbol required"></span>发标额度上限</label>
								<div class="col-sm-10">
									<div class="input-group">
										<input type="text" placeholder="发标额度上限" id="capitalToplimit" name="capitalToplimit" value="<c:out value="${instConfigForm.capitalToplimit}" />"  class="form-control input-sm"
											datatype="/^\d{1,10}(\.\d{1,2})?$/" errormsg="发标额度上限必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13" ajaxurl="topLimitCheckAction?capitalUsed=<c:out value="${instConfigForm.capitalToplimit - instConfigForm.capitalAvailable}" />">
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="capitalToplimit" label="发标额度上限"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${not empty instConfigForm.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="capitalUsed"> 
									发标已发额度 :
								</label>
								<div class="col-sm-10">
									<div class="input-group">
										<input type="text" disabled="disabled" placeholder="发标已发额度" id="capitalUsed" name="capitalUsed" value="<c:out value="${instConfigForm.capitalToplimit - instConfigForm.capitalAvailable}" />"  class="form-control" />
										<span class="input-group-addon">元</span>
									</div>
								</div>
							</div>	
						</c:if>
						<c:if test="${not empty instConfigForm.id}">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="capitalAvailable"> 
									发标额度余额 :
								</label>
								<div class="col-sm-10">
									<div class="input-group">
										<input type="text" disabled="disabled" placeholder="发标额度余额" id="capitalAvailable" name="capitalAvailable" value="<c:out value="${instConfigForm.capitalAvailable}" />"  class="form-control" />
										<span class="input-group-addon">元</span>
									</div>
								</div>
							</div>	
						</c:if>--%>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="commissionFee"> <span class="symbol required"></span>提现手续费</label>
							<div class="col-sm-10">
								<div class="input-group">
									<input type="text" placeholder="提现手续费" id="commissionFee" name="commissionFee" value="<c:out value="${instConfigForm.commissionFee}" />"  class="form-control"
										datatype="/^\d{1,10}(\.\d{1,2})?$/" errormsg="提现手续费必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
									<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="commissionFee" label="提现手续费"></hyjf:validmessage>
							</div>
						</div>		<%--
						<c:if test="${not empty instConfigForm.id}">	
							<div class="form-group">
								<label class="col-sm-2 control-label" for="repayCapitalType"> 
									等额本息保证金回滚方式 :
								</label>
								<div class="col-sm-10 ">
									<div class="radio clip-radio radio-primary ">
										<input type="radio" id="stateOn" name="repayCapitalType" value="0"  datatype="*" <c:if test="${ ( instConfigForm.repayCapitalType eq '0' ) or ( empty instConfigForm.repayCapitalType ) }" >checked="checked"</c:if> ><label for="stateOn"> 到期回滚 </label> 
										<input type="radio" id="stateOff" name="repayCapitalType" value="1" datatype="*" <c:if test="${ ( instConfigForm.repayCapitalType eq '1' ) }" >checked="checked"</c:if> > <label for="stateOff"> 分期回滚 </label>
										<input type="radio" id="stateOther" name="repayCapitalType" value="2" datatype="*" <c:if test="${ ( instConfigForm.repayCapitalType eq '2' ) }" >checked="checked"</c:if> > <label for="stateOther"> 不回滚 </label>
										<hyjf:validmessage key="status" label="等额本息保证金回滚方式"></hyjf:validmessage>
									</div>
								</div>
							</div>	
						</c:if>
						<div class="form-group">
							<label class="col-sm-2 control-label" for="remark"> <span
								class="symbol required"></span>说明
							</label>
							<div class="col-sm-10">
								<textarea datatype="*" errormsg="必填选项" placeholder="说明"
									id="remark" name="remark" class="form-control limited">${instConfigForm.remark}</textarea>
							</div>
						</div>--%>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i
									class="fa fa-check"></i> 确 认</a> <a
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
			src="${webRoot}/jsp/manager/config/instconfig/instconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
