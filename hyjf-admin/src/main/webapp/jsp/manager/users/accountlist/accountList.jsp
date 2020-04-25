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

<shiro:hasPermission name="accountlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="开户记录" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">开户记录</h1>
			<span class="mainDescription">本功能可以修改查询相应的会员开户记录信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="accountlist:SEARCH">
				      		<li  class="active"><a href="${webRoot}/manager/account/accountlist">汇付银行开户记录</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="accountlist:SEARCH">
				      		<li><a href="${webRoot}/manager/bankaccount/accountlist">江西银行开户记录</a></li>
				      	</shiro:hasPermission>
				    </ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="accountlist:SEARCH">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${accountListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${accountListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<shiro:hasPermission name="accountlist:EXPORT">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
															data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
												</shiro:hasPermission>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
														data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
												<%-- <col style="width:;" /> --%>
											</colgroup>
											<thead>
												<tr>
													<th class="center">序号</th>
													<th class="center">用户名</th>
													<th class="center">姓名</th>
													<th class="center">身份证号码</th>
													<!-- <th class="center">用户属性</th> -->
													<th class="center">开户状态</th>
													<th class="center">汇付账号</th>
													<th class="center">汇付客户号</th>
													<th class="center">开户平台</th>
													<th class="center">开户时间</th>
													<!-- <th class="center">操作</th> -->
												</tr>
											</thead>
											<tbody id="accountTbody">
												<c:choose>
													<c:when test="${empty accountListForm.recordList}">
														<tr><td colspan="9">暂时没有数据记录</td></tr>
													</c:when>
													<c:otherwise>
														<c:forEach items="${accountListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
															<tr>
																<td class="center">${(accountListForm.paginatorPage-1)*accountListForm.paginator.limit+status.index+1 }</td>
																<td><c:out value="${record.userName }"></c:out></td>
																<td><c:out value="${record.realName }"></c:out></td>
																<td class="center"><hyjf:asterisk value="${record.idCard }" permission="accountlist:HIDDEN_SHOW"></hyjf:asterisk></td>
																<%-- <td class="center"><c:out value="${record.userProperty }"></c:out></td> --%>
																<td class="center"><c:out value="${record.accountStatusName }"></c:out></td>
																<td><c:out value="${record.account }"></c:out></td>
																<td class="center"><c:out value="${record.customerAccount }"></c:out></td>
																<td><c:out value="${record.openAccountPlat }"></c:out></td>
																<td class="center"><c:out value="${record.openTime }"></c:out></td>
																<%-- <td class="center">
																	<div class="visible-md visible-lg hidden-sm hidden-xs">
																		<shiro:hasPermission name="accountlist:CONFIRM_ACCOUNT">
																			<c:if test="${not empty record.accountStatus}">
																				<c:if test="${record.accountStatus==0}">
																					<a class="btn btn-transparent btn-xs fn-ConfirmAccount" data-userid="${record.userId }"
																						data-toggle="tooltip" data-placement="top" data-original-title="开户确认"><i class="fa fa-refresh"></i></a>
																				</c:if>
																			</c:if>
																		</shiro:hasPermission>
																	</div>
																</td> --%>
															</tr>
														</c:forEach>					
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<%-- 分页栏 --%>
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="accountlist" paginator="${accountListForm.paginator}"></hyjf:paginator>
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
			<input type="hidden" name="userId" id="userId" value= "${accountListForm.userId}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${accountListForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${accountListForm.userName}" />
			</div>
			<div class="form-group">
				<label>姓名</label> 
				<input type="text" name="realName" class="form-control input-sm underline"  maxlength="20" value="${accountListForm.realName}" />
			</div>
			<div class="form-group">
					<label>用户属性</label>
					<select name="userProperty" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
							<option value="${property.nameCd }"
								<c:if test="${property.nameCd eq accountListForm.userProperty}">selected="selected"</c:if>>
								<c:out value="${property.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
			<div class="form-group">
				<label>身份证号码</label> 
				<input type="text" name="idCard" class="form-control input-sm underline"  maxlength="20" value="${accountListForm.idCard}" />
			</div>
			<%-- <div class="form-group">
				<label>开户状态</label>
				<select name="accountStatus" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${accountStatus }" var="acstatus" begin="0" step="1">
						<option value="${acstatus.nameCd }"
							<c:if test="${acstatus.nameCd eq accountListForm.accountStatus}">selected="selected"</c:if>>
							<c:out value="${acstatus.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div> --%>
			<div class="form-group">
				<label>开户平台</label>
				<select name="openAccountPlat" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${openAccPlat }" var="plat" begin="0" step="1">
						<option value="${plat.nameCd }"
							<c:if test="${plat.nameCd eq accountListForm.openAccountPlat}">selected="selected"</c:if>>
							<c:out value="${plat.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>汇付帐号</label> 
				<input type="text" name="account" class="form-control input-sm underline" maxlength="50" value="${accountListForm.account}"/>
			</div>
			<div class="form-group">
				<label>开户时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="openTimeStart" id="start-date-time" class="form-control underline" value="${accountListForm.openTimeStart}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="openTimeEnd" id="end-date-time" class="form-control underline" value="${accountListForm.openTimeEnd}" />
					</span>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/accountlist/accountList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
