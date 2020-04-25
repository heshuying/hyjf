<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="开户掉单查询" />
	
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">	
		<form id="mainForm" method="post" action="openaccountenquiryupdateAction">
			<hyjf:message key="message"></hyjf:message>
			<input type="hidden" name=id id="id" value="${preRegist.id}"/>			
			<input type="hidden" name=mobile id="mobile" value="${preRegist.mobile}"/>
			<input type="hidden" id="success" value="${success}" />
			<input type="hidden" id="roleId" name="roleId" value="${roleId}">
			<div class="row bg-white">
				<div class="col-sm-12">
					<div class="panel panel-white">
						<div class="panel-body panel-table">
							<table class="center" style="width:95%">
								<c:if test="${accountStatus!=null&&accountStatus!=''}">
									<tr>
										<td align="right" class="tlabel"></td>
										<td align="left"><font size="5" color=""><c:out value="${accountStatus}" /></font></td>
									</tr>
								</c:if>
								<c:if test="${msg!=null&&msg!=''}">
									<tr>
										<td align="right" class="tlabel"></td>
										<td align="left"> <font size="5" color=""><c:out value="${msg}" /></font></td>
									</tr>
								</c:if>
								<c:if test="${username!=null&&username!=''}">
									<tr>
										<td align="right" class="tlabel">用户名：</td>
										<td align="left"> <c:out value="${username}" /></td>
									</tr>
								</c:if>
								<c:if test="${phone!=null&&phone!=''}">
									<tr>
										<td align="right" class="tlabel">手机号：</td>
										<td align="left"><c:out value="${phone}"/></td>
									</tr>
								</c:if>
								
								<c:if test="${accountString!=null&&accountString!=''}">
									<tr>
										<td align="right" class="tlabel">电子账号：</td>
										<td align="left"><c:out value="${accountString}"/></td>
									</tr>
								</c:if>
								<c:if test="${regTimeEnd!=null&&regTimeEnd!=''}">
									<tr>
										<td align="right" class="tlabel">开户时间：</td>
										<td align="left"><c:out value="${regTimeEnd}"/></td>
									</tr>
								</c:if>
								<%-- <c:if test="${accountString!=null&&accountString!=''}">
								</c:if>
								<c:if test="${accountString!=null&&accountString!=''}">
									
								</c:if> --%>
								<c:if test="${platform!=null&&platform!=''}">
									<tr>
										<td align="right" class="tlabel">开户平台：</td>
										<td align="left"><c:out value="${platform}"/></td>
									</tr>
								</c:if>

								<c:if test="${roleId!=null && roleId!=''}">
									<tr>
										<td align="right" class="tlabel">银行角色：</td>
										<td align="left">
											<c:if test="${roleId=='1'}">出借角色</c:if>
											<c:if test="${roleId=='2'}">借款角色</c:if>
											<c:if test="${roleId=='3'}">代偿角色</c:if>
										</td>
									</tr>
								</c:if>

							</table>
							<hr>
							<a type="text"  style="display:none" name="ordeidString" class="form-control input-sm" id="ordeidString"  value="${ordeidString}"/>
							<a type="text"  style="display:none" name="userid" class="form-control input-sm" id="userid" value="${userid}"/>
							<a type="text"  style="display:none" name="channel" class="form-control input-sm" id="channel" value="${channel}"/>
							<a type="text"  style="display:none" name="accountId" class="form-control input-sm" id="accountId" value="${accountId}"/>
							<div>  
						        <input type="text" placeholder="用户名" required="" id="ordeidString" name="ordeidString" value="${ordeidString}"/>  
						    </div>
						    <div>  
						        <input type="text" placeholder="用户名" required="" id="userid" name="userid" value="${userid}"/>  
						    </div>
						    <div>  
						        <input type="text" placeholder="用户名" required="" id="channel" name="channel" value="${channel}"/>  
						    </div>
						    <div>  
						        <input type="text" placeholder="用户名" required="" id="accountId" name="accountId" value="${accountId}"/>  
						    </div>
							<div class="form-group margin-bottom-0" align="center">
								<div class="col-sm-offset-2 col-sm-10">
									<c:if test="${accountStatus!='已开户'}">
										<a class="btn btn-o btn-primary fn-Confirms"><i class="fa fa-check"></i> 确定</a>
									</c:if>
									<c:if test="${accountStatus=='已开户'}">
										<a type="submit" class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确定并同步</a>
									
									</c:if>
									
									<!-- <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消</a> -->
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-table .tlabel {
		height: 30px;
		font-weight: 700;
		width: 35%;
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/exception/openaccountenquiryexception/openaccountenquiryinfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
