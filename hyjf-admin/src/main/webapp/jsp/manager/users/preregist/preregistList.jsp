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
<c:set value="${fn:split('汇盈金服,注册记录', ',')}" var="functionPaths" scope="request"></c:set>

<shiro:hasPermission name="preregist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="注册记录" />
		
		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">预注册用户</h1>
			<span class="mainDescription">本功能可以修改查询相应的预注册记录信息。</span>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="preregist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${preregistListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${preregistListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="preregist:EXPORT">
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
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">手机号</th>
										<th class="center">推荐人</th>
										<th class="center">渠道</th>
										<th class="center">预注册时间</th>
										<th class="center">是否已注册</th>
										<th class="center">操作终端</th>
										<th class="center">备注</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty preregistListForm.recordList}">
											<tr><td colspan="9">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${preregistListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(preregistListForm.paginatorPage-1)*preregistListForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><c:out value="${record.referrer }"></c:out></td>
													<td class="center"><c:out value="${record.source }"></c:out></td>
													<td class="center"><c:out value="${record.preRegistTime }"></c:out>
													<td class="center">
														<c:if test="${record.registFlag eq 1}">
														是
														</c:if>
														<c:if test="${record.registFlag eq 0}">
														否
														</c:if>
													</td>
													<td class="center"><c:out value="${record.platformName }"></c:out></td>
													<td><c:out value="${record.remark }"></c:out></td>
													<td class="center">
														<c:if test="${record.registFlag eq '0'}">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="preregist:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																			data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<shiro:hasPermission name="preregist:MODIFY">
																		<li>
																			<a class="fn-Modify" data-id="${record.id }">修改</a>
																		</li>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</div>
														</c:if>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="preregistlist" paginator="${preregistListForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" value= ""/>
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${preregistListForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>手机号</label> 
				<input type="text" name="mobile" class="form-control input-sm underline"  maxlength="11" value="${preregistListForm.mobile}" />
			</div>
			<div class="form-group">
				<label>推荐人</label> 
				<input type="text" name="referrer" class="form-control input-sm underline"  maxlength="20" value="${preregistListForm.referrer}" />
			</div>
			<div class="form-group">
				<label>渠道</label> 
				<input type="text" name="source" class="form-control input-sm underline"  maxlength="20" value="${preregistListForm.source}" />
			</div>
			<div class="form-group">
				<label>是否注册</label> 
				<select name="registFlag" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1" <c:if test="${'1' eq preregistListForm.registFlag}">selected="selected"</c:if>>是</option>
					<option value="0" <c:if test="${'0' eq preregistListForm.registFlag}">selected="selected"</c:if>>否</option>
				</select>
			</div>
			<%-- <div class="form-group">
				<label>注册渠道</label>
				<select name="sourceId" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${utmPlat }" var="plat" begin="0" step="1">
						<option value="${plat.sourceId }"
							<c:if test="${plat.sourceId eq preregistListForm.sourceId}">selected="selected"</c:if>>
							<c:out value="${plat.sourceName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div> --%>
		</tiles:putAttribute>
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript"> var webRoot = "${webRoot}";</script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/preregist/preregistList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
