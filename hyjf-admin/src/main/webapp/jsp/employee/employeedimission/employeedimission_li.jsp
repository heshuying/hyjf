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
		<tiles:putAttribute name="pageTitle" value="离职记录" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">离职列表</h1>
			<span class="mainDescription">添加描述。</span>
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
								<c:set var="jspPrevDsb" value="${employeedimissionForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${employeedimissionForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}"
										data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
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
											<div class="checkbox clip-check check-primary checkbox-inline"
													data-toggle="tooltip" tooltip-placement="top" data-original-title="选择所有行">
												<input type="checkbox" id="checkall">
												<label for="checkall"></label>
											</div>
										</th>
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
										<th class="center">离职状态</th>
										<th class="center">工薪截止日</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty employeedimissionForm.recordList}">
											<tr><td colspan="12">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${employeedimissionForm.recordList}" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="hidden-xs center">
														<div class="checkbox clip-check check-primary checkbox-inline">
															<input type="checkbox" class="listCheck" id="row${status.index }" value="${record.id }">
															<label for="row${status.index }"></label>
														</div>
													</td>
													<td><c:out value="${record.seconddepart }"></c:out></td>
													<td><c:out value="${record.cityManager }"></c:out></td>
													<td><c:out value="${record.thirddepart }"></c:out></td>
													<td><c:out value="${record.user_realname }"></c:out></td>
													<td class="center"> 
														<c:if test="${record.level == 1}"> 主管 </c:if>
														<c:if test="${record.level == 2}"> 员工 </c:if>		
									<%-- 						<c:choose>  
															<c:when test="${record.level == 1}"> 主管 </c:when>
															<c:when test="${record.level == 2}"> 员工 </c:when>
															<c:otherwise> 位置 </c:otherwise>
														</c:choose>		--%>
													</td>
													<td><c:out value="${record.pname }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><fmt:formatDate value="${record.entrydatetime }" pattern="yyyy-MM-dd"/></td>
													<td class="center">  
														<c:if test="${record.temporary == 1}"> 兼职 </c:if>
														<c:if test="${record.temporary == 2}"> 正式员工 </c:if>
													</td>
													<td><c:out value="${record.hyd_name }"></c:out></td>
													<td> 
														<c:choose>
															<c:when test="${record.oprocess == 'E1'}"> 入职一级审批   </c:when>
															<c:when test="${record.oprocess == 'E2'}"> 入职二级审批   </c:when>
															<c:when test="${record.oprocess == 'Q'}" > 已离职	    </c:when>
															<c:when test="${record.oprocess == 'Q1'}"> 待一级审批       </c:when>
															<c:when test="${record.oprocess == 'Q2'}"> 待二级审批       </c:when>
															<c:when test="${record.oprocess == 'Q3'}"> 取消离职           </c:when>
															<c:when test="${record.oprocess == 'Q11'}">一审驳回           </c:when>
															<c:when test="${record.oprocess == 'Q21'}">二审驳回           </c:when>					
														</c:choose>
													</td>
													<td class="center"><fmt:formatDate value="${record.enddatetime }" pattern="yyyy-MM-dd"/></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/employee/employeedimission/detail?id=${record.id }" data-original-title="详细"><i class="fa fa-pencil"></i></a>
															<a class="btn btn-transparent btn-xs tooltips" 
																	data-toggle="tooltip" tooltip-placement="top" href= "${webRoot}/employee/employeedimission/approval?id=${record.id }" data-original-title="审批"><i class="fa fa-pencil"></i></a>
															
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<a class="fn-Detail" href= "${webRoot}/employee/employeedimission/detail?id=${record.id }">详细</a>
																	</li>
																	<li>
																		<a class="fn-Approval" href= "${webRoot}/employee/employeedimission/approval?id=${record.id }">审批</a>
																	</li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="employeedimission_list" paginator="${employeedimissionForm.paginator}"></hyjf:paginator>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${employeedimissionForm.paginatorPage}" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>角色</label> 
				<select name="levelSrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1"
						<c:if test="${1 eq employeedimissionForm.levelSrch}">selected="selected"</c:if>>
						<c:out value="主管"></c:out>
					</option>
					<option value="2"
						<c:if test="${2 eq employeedimissionForm.levelSrch}">selected="selected"</c:if>>
						<c:out value="员工"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>姓名</label>
				<input type="text" name="truenameSrch" class="form-control input-sm underline" value="${employeedimissionForm.truenameSrch}" />
			</div>
			<div class="form-group">
				<label>手机号码</label>
				<input type="text" name="mobileSrch" class="form-control input-sm underline" value="${employeedimissionForm.mobileSrch}" />
			</div>
			<div class="form-group">
				<label>是否兼职</label> 
				<select name="temporarySrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1"
						<c:if test="${1 eq employeedimissionForm.temporarySrch}">selected="selected"</c:if>>
						<c:out value="兼职"></c:out>
					</option>
					<option value="2"
						<c:if test="${2 eq employeedimissionForm.temporarySrch}">selected="selected"</c:if>>
						<c:out value="正式员工"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>离职状态</label> 
				<select name="oprocessSrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="Q"
						<c:if test="${Q eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="已离职"></c:out>
					</option>
					<option value="Q1"
						<c:if test="${Q1 eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="待一级审批"></c:out>
					</option>
					<option value="Q2"
						<c:if test="${Q2 eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="待二级审批"></c:out>
					</option>
					<option value="Q3"
						<c:if test="${Q3 eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="取消离职"></c:out>
					</option>
					<option value="Q11"
						<c:if test="${Q11 eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="一审驳回"></c:out>
					</option>
					<option value="Q21"
						<c:if test="${Q21 eq employeedimissionForm.oprocessSrch}">selected="selected"</c:if>>
						<c:out value="二审驳回"></c:out>
					</option>
					<option selected="selected"> 
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
			<script type='text/javascript' src="${webRoot}/jsp/employee/employeedimission/employeedimission_li.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
