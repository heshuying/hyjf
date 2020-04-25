<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="employeeinfo:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="员工管理" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">员工列表</h1>
			<span class="mainDescription">员工列表信息。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<hyjf:message key="delete_error"></hyjf:message>
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${employeeinfoForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${employeeinfoForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
					<%-- 	<shiro:hasPermission name="employeeinfo:ADD">	--%>	
								<a class="btn btn-o btn-primary btn-sm fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加新用户"><i class="fa fa-plus"></i> 添加</a>
					<%-- 		</shiro:hasPermission>	--%>	
							
							<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"><i class="fa fa-refresh"></i> 刷 新</a> 
							<shiro:hasPermission name="accountdetail:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export"
										data-toggle="tooltip" data-placement="bottom" data-original-title="导出Excel"><i class="fa fa-Export"></i> 导出Excel</a>
								</shiro:hasPermission>		
							<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="hidden-xs center">
											<div align="left" class="checkbox clip-check check-primary checkbox-inline"
													data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
										<th class="center">员工编号</th>
										<th class="center">二级分部</th>
										<th class="center">城市经理</th>
										<th class="center">团队名称</th>
										<th class="center">姓名</th>
										<th class="center">角色</th>
										<th class="center">岗位名称</th>
										<th class="center">手机</th>
										<th class="center">入职日期</th>
										<th class="center">是否兼职</th>
										<th class="center">汇盈贷账号</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty employeeinfoForm.recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${employeeinfoForm.recordList}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<div class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
													</td>
													<td class="center"><c:out value="${record.id }"></c:out></td>
													<td><c:out value="${record.seconddepart }"></c:out></td>
													<td><c:out value="${record.cityManager }"></c:out></td>
													<td><c:out value="${record.thirddepart }"></c:out></td>
													<td><c:out value="${record.user_realname }"></c:out></td>
													<td class="center"> 
														<c:if test="${record.level == 1}"> 主管 </c:if>
														<c:if test="${record.level == 2}"> 员工 </c:if>
													</td>
													<td><c:out value="${record.pname }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><fmt:formatDate value="${record.entrydate }" pattern="yyyy-MM-dd"/></td>
										<!-- 		<td><c:out value="${record.entrydate }"></c:out></td>    -->	
													<td class="center">  
														<c:if test="${record.temporary == 1}"> 兼职 </c:if>
														<c:if test="${record.temporary == 2}"> 正式员工 </c:if>
													</td>
													<td><c:out value="${record.hyd_name  }"></c:out></td>
													<td style="display:none;"> ${record.hyd_id} </td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/employee/employeeinfo/detail?id=${record.id }" data-original-title="详细"><i class="fa fa-pencil"></i></a>
															<shiro:hasPermission name="employeeinfo:MODIFY">
															<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="employeeinfo:DELETE">
															<a class="btn btn-transparent btn-xs tooltips fn-Delete" data-id="${record.id }"
																	data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
															<a class="btn btn-transparent btn-xs fn-reset" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/employee/employeeinfo/resetPassword?id=${record.id }" data-original-title="重置密码"><i class="fa fa-pencil"></i></a>
															<a class="btn btn-transparent btn-xs fn-leave" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/employee/employeeinfo/leaveStaff?id=${record.id }" data-original-title="离职"><i class="fa fa-pencil"></i></a>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<a class="fn-detail" href= "${webRoot}/employee/employeeinfo/detail?id=${record.id }">详细</a>
																	</li>
																	<shiro:hasPermission name="employeeinfo:MODIFY">
																	<li>
																		<a class="fn-Modify" data-id="${record.id }">修改</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="employeeinfo:DELETE">
																	<li>
																		<a class="fn-Delete" data-id="${record.id }">删除</a>
																	</li>
																	</shiro:hasPermission>
																	<li>
																		<a class="fn-reset" href= "${webRoot}/employee/employeeinfo/resetPassword?id=${record.id }" >重置密码</a>
																	</li>
																	<shiro:hasPermission name="employeeinfo:LEAVE">
																	<li>
																		<a class="fn-Leave" data-id="${record.id }">离职</a>
																	</li>
																	</shiro:hasPermission>
																</ul>
															</div>
														</div>
													</td>
												</tr>
											</c:forEach>					
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="employeeinfo_list" paginator="${employeeinfoForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${employeeinfoForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>角色</label> 
				<select name="levelSrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1"
						<c:if test="${1 eq employeeinfoForm.levelSrch}">selected="selected"</c:if>>
						<c:out value="主管"></c:out>
					</option>
					<option value="2"
						<c:if test="${2 eq employeeinfoForm.levelSrch}">selected="selected"</c:if>>
						<c:out value="员工"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>姓名</label>
				<input type="text" name="truenameSrch" class="form-control input-sm underline" value="${employeeinfoForm.truenameSrch}" />
			</div>
			<div class="form-group">
				<label>手机号码</label>
				<input type="text" name="mobileSrch" class="form-control input-sm underline" value="${employeeinfoForm.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>是否兼职</label> 
				<select name="temporarySrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1"
						<c:if test="${1 eq employeeinfoForm.temporarySrch}">selected="selected"</c:if>>
						<c:out value="兼职"></c:out>
					</option>
					<option value="2"
						<c:if test="${2 eq employeeinfoForm.temporarySrch}">selected="selected"</c:if>>
						<c:out value="正式员工"></c:out>
					</option>
				</select>
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
			<script type='text/javascript' src="${webRoot}/jsp/employee/employeeinfo/employeeinfo_li.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
