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

<shiro:hasPermission name="bankcardlist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="银行卡管理" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">银行卡管理</h1>
			<span class="mainDescription">本功能可以查询相应的会员银行卡信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab"> 
						<shiro:hasPermission name="accountlist:SEARCH">
				      		<li  class="active"><a href="${webRoot}/manager/bankcard/bankcardlist">汇付银行卡管理</a></li>
				      	</shiro:hasPermission>
				      	<shiro:hasPermission name="accountlist:SEARCH">
				      		<li><a href="${webRoot}/manager/newbankcard/bankcardlist">江西银行卡管理</a></li>
				      	</shiro:hasPermission>
				    </ul>
				    <div class="tab-content">
					    <div class="tab-pane fade in active">
							<div class="row">
								<div class="col-md-12">
									<div class="search-classic">
										<shiro:hasPermission name="bankcardlist:SEARCH">
											<%-- 功能栏 --%>
											<div class="well">
												<c:set var="jspPrevDsb" value="${bankcardListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
												<c:set var="jspNextDsb" value="${bankcardListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
												<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
														data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
												<shiro:hasPermission name="bankcardlist:ADD">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
															data-toggle="tooltip" data-placement="top" data-original-title="添加新用户"><i class="fa fa-plus"></i> 添加</a>
												</shiro:hasPermission>
												<shiro:hasPermission name="bankcardlist:EXPORT">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
															data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
												</shiro:hasPermission>
												<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
														data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
												<shiro:hasPermission name="bankaccountlog:VIEW">
													<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Operation" href= "${webRoot}/manager/user/bankaccountlog/init"
															data-toggle="tooltip" data-placement="bottom" data-original-title="操作记录 ">操作记录 </a>
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
													<th class="center">银行账号</th>
													<th class="center">所属银行</th>
													<th class="center">是否默认</th>
													<th class="center">银行卡属性</th>
													<th class="center">添加时间</th>
												</tr>
											</thead>
											<tbody id="bankcardTbody">
												<c:choose>
													<c:when test="${empty bankcardListForm.recordList}">
														<tr><td colspan="7">暂时没有数据记录</td></tr>
													</c:when>
													<c:otherwise>
														<c:forEach items="${bankcardListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
															<tr>
																<td class="center">${(bankcardListForm.paginatorPage-1)*bankcardListForm.paginator.limit+status.index+1 }</td>
																<td><c:out value="${record.userName }"></c:out></td>
																<td><hyjf:asterisk value="${record.account }" permission="bankcardlist:HIDDEN_SHOW"></hyjf:asterisk></td>
																<td><c:out value="${record.bank }"></c:out></td>
																<td class="center"><c:out value="${record.cardType }"></c:out></td>
																<td class="center"><c:out value="${record.cardProperty }"></c:out></td>
																<td class="center"><c:out value="${record.addTime }"></c:out></td>
															</tr>
														</c:forEach>					
													</c:otherwise>
												</c:choose>
											</tbody>
										</table>
										<%-- 分页栏 --%>
										<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="bankcardlist" paginator="${bankcardListForm.paginator}"></hyjf:paginator>
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
			<input type="hidden" name="userId" id="userId" value= "${bankcardListForm.userId}"/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${bankcardListForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>用户名</label> 
				<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${bankcardListForm.userName}" />
			</div>
			<div class="form-group">
				<label>所属银行</label> 
				<select name="bank" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${banks}" var="bank" begin="0" step="1">
						<option value="${bank.code }"
							<c:if test="${bank.code eq bankcardListForm.bank}">selected="selected"</c:if>>
							<c:out value="${bank.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>银行帐号</label> 
				<input type="text" name="account" class="form-control input-sm underline"  maxlength="20" value="${bankcardListForm.account}" />
			</div>
			<div class="form-group">
				<label>银行卡属性</label>
				<select name="cardProperty" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${bankcardProperty }" var="cardpro" begin="0" step="1">
						<option value="${cardpro.nameCd }"
							<c:if test="${cardpro.nameCd eq bankcardListForm.cardProperty}">selected="selected"</c:if>>
							<c:out value="${cardpro.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>是否默认</label>
				<select name="cardType" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${bankcardType }" var="cardtype" begin="0" step="1">
						<option value="${cardtype.nameCd }"
							<c:if test="${cardtype.nameCd eq bankcardListForm.cardType}">selected="selected"</c:if>>
							<c:out value="${cardtype.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="addTimeStart" id="start-date-time" class="form-control underline" value="${bankcardListForm.addTimeStart}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="addTimeEnd" id="end-date-time" class="form-control underline" value="${bankcardListForm.addTimeEnd}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/bankcardlist/bankcardList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
