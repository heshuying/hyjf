<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="userauthlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="服务费授权" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">服务费授权</h1>
			<span class="mainDescription">本功能可以查询相应的会员服务费授权信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="userauthlist:SEARCH">
				      		<li><a href="${webRoot}/manager/userauth/userauthlist">授权状态</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="userauthlist:SEARCH">
				      		<li><a href="${webRoot}/manager/userauth/userauthloglist">授权记录</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="userauthlist:SEARCH">
				      		<li  class="active"  ><a href="${webRoot}/manager/userauth/userpayauthlist">服务费授权</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="userauthlist:SEARCH">
				      		<li><a href="${webRoot}/manager/userauth/userrepayauthlist">还款授权</a></li>
				      	</shiro:hasPermission>
				    </ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="userauthlist:SEARCH">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${obj.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${obj.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<shiro:hasPermission name="userauthlist:EXPORT">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
															data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
												</shiro:hasPermission>
												<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
														data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
														data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
											</div>
										</shiro:hasPermission>
										<br/>
										<%-- 角色列表一览 --%>
										<table id="equiList" class="table table-striped table-bordered table-hover">
											<colgroup>
												<col style="width:55px;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
												<col style="width:;" />
											</colgroup>
											<thead>
												<tr>
													<th class="center">序号</th>
													<th class="center">用户名</th>
													<th class="center">授权金额</th>
													<th class="center">签约到期日</th>
													<th class="center">授权状态</th>
													<th class="center">银行电子账号</th>
													<th class="center">授权时间</th>
													<th class="center">操作</th>
												</tr>
											</thead>
											<tbody id="userauthTbody">
												<c:choose>
													<c:when test="${empty obj.recordList}">
														<tr><td colspan="11">暂时没有数据记录</td></tr>
													</c:when>
													<c:otherwise>
														<c:forEach items="${obj.recordList }" var="record" begin="0" step="1" varStatus="status">
															<tr>
																<td class="center">${(obj.paginatorPage-1)*obj.paginator.limit+status.index+1 }</td>
																<td class="center"><c:out value="${record.userName }"></c:out></td>
																<td class="center">
																	<c:choose>
																		<c:when test="${record.authType != 2}">
																			${record.paymentMaxAmt}
																		</c:when>
																		<c:when test="${record.authType == 2}">
																		</c:when>
																	</c:choose>
																</td>
																<td class="center"><c:out value="${record.signEndDate}"></c:out></td>
																<td class="center">
																	<c:choose>
																		<c:when test="${record.authType == 0}">
																			未授权
																		</c:when>
																		<c:when test="${record.authType == 1}">
																			已授权
																		</c:when>
																		<c:when test="${record.authType == 2}">
																			已解约
																		</c:when>
																	</c:choose>
																</td>
																<td class="center"><c:out value="${record.bankid }"></c:out></td>
																<td class="center"><c:out value="${record.signDate }"></c:out></td>
																<td class="center">
<%--                                										<a href="${webRoot}/manager/userauth/userpayauthquery?id=${record.userid}" class="fn-Info">查询</a> --%>
																	<a class="fn-queryAuth" data-userid="${record.userid }">查询</a>
<%--                                										<a href="${webRoot}/manager/userauth/userpayauthdis?id=${record.userid}" class="fn-Info">解约</a> --%>
                               										<%--<a class="fn-paycancel" data-userid="${record.userid}">解约</a>--%>
																</td>
															</tr>
														</c:forEach>					
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<%-- 分页栏 --%>
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="userpayauthlist" paginator="${obj.paginator}"></hyjf:paginator>
										<br/><br/>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" value= "${obj.userid}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${obj.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${obj.userName}" />
			</div>
			<div class="form-group">
				<label>电子账号</label>
				<input type="text" name="bankid" class="form-control input-sm underline"  maxlength="20" value="${obj.bankid}" />
			</div>
			<div class="form-group">
				<label>授权状态</label>
				<select name="authType" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1"<c:if test="${'1' eq obj.authType}">selected="selected"</c:if>>已授权</option>
					<option value="0"<c:if test="${'0' eq obj.authType}">selected="selected"</c:if>>未授权</option>
					<option value="2"<c:if test="${'2' eq obj.authType}">selected="selected"</c:if>>已解约</option>
				</select>
			</div>

			<div class="form-group">
				<label>授权时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="authTimeStart" id="inves-start-date-time" class="form-control underline" value="${obj.authTimeStart}" />
						<i class="ti-calendar"></i>
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="authTimeEnd" id="inves-end-date-time" class="form-control underline" value="${obj.authTimeEnd}" />
					</span>
				</div>
			</div>

			<div class="form-group">
				<label>签约到期日</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="signTimeStart" id="sign-start-date-time" class="form-control underline" value="${obj.signTimeStart}" />
						<i class="ti-calendar"></i>
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="signTimeEnd" id="sign-end-date-time" class="form-control underline" value="${obj.signTimeEnd}" />
					</span>
				</div>
			</div>
			
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogpanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/userauthlist/userpayauthList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
