<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="subconfig:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="分账名单配置" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">分账名单配置</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<%-- <ul class="nav nav-tabs" id="myTab">
				<shiro:hasPermission name="projecttype:VIEW">
					<li><a href="${webRoot}/manager/config/projecttype/init">项目类型</a></li>
				</shiro:hasPermission>
		      	<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/finmanchargenew/init">费率配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="borrowflow:VIEW">
		      		<li><a href="${webRoot}/manager/config/borrowflow/init">流程配置</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="sendtype:VIEW">
					<li><a href="${webRoot}/manager/config/sendtype/init">发标/复审</a></li>
				</shiro:hasPermission>
			</ul> --%>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${subcofigForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${subcofigForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
						<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
								data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
						<shiro:hasPermission name="subconfig:ADD">
							<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
									data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
						</shiro:hasPermission>
						<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
								data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
						<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" 
							data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
							<i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
						</a>
					</div>
					<br />
					<%-- 列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								
								<th class="center">用户名</th>
								<th class="center">姓名</th>
								<th class="center">用户角色</th>						
								<th class="center">用户类型</th>
								<th class="center">合作机构编号</th>
								<th class="center">银行开户状态</th>
								<th class="center">江西银行电子账户</th>
								<th class="center">用户状态</th>	
								<th class="center">说明</th>
								<th class="center">添加时间</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty subconfigForm.recordList}">
									<tr>
										<td colspan="11">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${subconfigForm.recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((subconfigForm.paginatorPage - 1) * subconfigForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.username }"></c:out></td>
											<td class="center"><c:out value="${record.truename }"></c:out></td>
											<td class="center"><c:out value="${record.roleName }"></c:out></td>
											<td class="center"><c:out value="${record.userType }"></c:out></td>
											<td class="center"><c:out value="${record.cooperateNum }"></c:out></td>
											<td class="center"><c:out value="${record.bankOpenAccount }"></c:out></td>
											<%-- <td class="center"><c:out value="${record.manChargeTime }"></c:out>
												<c:if test="${record.manChargeType == '天标' }">
													天
												</c:if>
												<c:if test="${record.manChargeType == '月标' }">
													个月
												</c:if>
											</td> --%>
											<td class="center"><c:out value="${record.account }"></c:out></td>
											<td class="center"><c:out value="${record.status == '0' ? '启用' : '禁用'  }"></c:out></td>											
											<td class="center"><c:out value="${record.remark }"></c:out></td>
											<td class="center"><c:out value="${record.createTime }"></c:out></td>
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<shiro:hasPermission name="subconfig:MODIFY">
														<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
													</shiro:hasPermission>
													<shiro:hasPermission name="subconfig:DELETE">
														<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
													</shiro:hasPermission>
												</div>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${subconfigForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="subconfig:SEARCH">
				<input type="hidden" name="id" id="id" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${subconfigForm.paginatorPage}" />
				<!-- 查询条件 -->
				<div class="form-group">
					<label>用户名:</label>
					<input type="text" name="userNameSrch" class="form-control input-sm underline"  maxlength="20" value="${subconfigForm.userNameSrch}" />
				</div>
				<div class="form-group">
					<label>姓名:</label>
					<input type="text" name="trueNameSrch" class="form-control input-sm underline"  maxlength="20" value="${subconfigForm.trueNameSrch}" />
				</div>
				<div class="form-group">
					<label>角色:</label>
					<input type="text" name="roleNameSrch" class="form-control input-sm underline"  maxlength="20" value="${subconfigForm.roleNameSrch}" />
				</div>
				<div class="form-group">
					<label>用户类型:</label>
					<select name="userTypeSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="企业用户" <c:if test="${'企业用户' eq subconfigForm.userTypeSrch}">selected="selected"</c:if>>
							<c:out value="企业用户"></c:out>
						</option>
						<option value="个人用户" <c:if test="${'个人用户' eq subconfigForm.userTypeSrch}">selected="selected"</c:if>>
							<c:out value="个人用户"></c:out>
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>江西银行电子账号:</label>
					<input type="text" name="accountSrch" class="form-control input-sm underline"  maxlength="20" value="${subconfigForm.accountSrch}" />
				</div>
				<div class="form-group">
					<label>用户状态:</label>
					<select name="statusSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0" <c:if test="${'0' eq subconfigForm.statusSrch}">selected="selected"</c:if>>
							<c:out value="启用"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq subconfigForm.statusSrch}">selected="selected"</c:if>>
							<c:out value="禁用"></c:out>
						</option>
					</select>
				</div>
				<div class="form-group">
				<label>添加时间:</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="recieveTimeStartSrch" id="start-date-time" class="form-control underline" value="${subconfigForm.recieveTimeStartSrch}" />
						<i class="ti-calendar"></i> 
					</span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<span class="input-icon">
						<input type="text" name="recieveTimeEndSrch" id="end-date-time" class="form-control underline" value="${subconfigForm.recieveTimeEndSrch}" />
					</span>
				</div>
			</div>
				
			</shiro:hasPermission>
		</tiles:putAttribute>
		
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/subconfig/subconfig.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
