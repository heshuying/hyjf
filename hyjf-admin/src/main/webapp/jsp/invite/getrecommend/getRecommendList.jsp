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


<shiro:hasPermission name="activitylist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="邀请会员列表" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">推荐星获取明细列表</h1>
			<span class="mainDescription">本功能可以查询相应的推荐星获取明细信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul class="nav nav-tabs" id="myTab"> 
					<shiro:hasPermission name="activitylist:VIEW">
			      		<li><a href="${webRoot}/inviteUser/init">账户信息</a></li>
			      	</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li  class="active"><a href="javascript:void(0);">推荐星获取明细</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/usedRecommend/init">推荐星使用明细</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/exchangeconf/conf/init">兑换配置</a></li>
					</shiro:hasPermission>
					<shiro:hasPermission name="activitylist:VIEW">
						<li ><a href="${webRoot}/invite/drawconf/conf/init">抽奖配置</a></li>
					</shiro:hasPermission>
			    </ul>
			    
				<div class="tab-content">
					<div class="tab-pane fade in active">
							<shiro:hasPermission name="activitylist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${getRecommendForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${getRecommendForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="activitylist:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
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
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机号</th>
										<th class="center">所属部门</th>
										<th class="center">获得推荐星类型</th>
										<th class="center">获得推荐星数量</th>
										<th class="center">推荐好友</th>
										<th class="center">推荐好友姓名</th>
										<th class="center">推荐好友手机</th>
										<th class="center">获得时间</th>
										<th class="center">是否发放</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty getRecommendForm.recordList}">
											<tr><td colspan="15">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${getRecommendForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(getRecommendForm.paginatorPage-1)*getRecommendForm.paginator.limit+status.index+1 }</td>
													<td class="center"><c:out value="${record.userName }"></c:out></td>
													<td class="center"><c:out value="${record.trueName }"></c:out></td>
													<td class="center"><c:out value="${record.mobile }"></c:out></td>
													<td class="center"><c:out value="${record.departmentName }"></c:out></td>
													<td class="center"><c:out value="${record.recommendSource }"></c:out></td>
													<td class="center"><c:out value="${record.recommendCount }"></c:out></td>
													<td class="center"><c:out value="${record.inviteByUserName }"></c:out></td>
													<td class="center"><c:out value="${record.inviteByTrueName }"></c:out></td>
													<td class="center"><c:out value="${record.inviteByMobile }"></c:out></td>
													<td class="center"><c:out value="${record.sendTime }"></c:out></td>
													<td class="center"><c:out value="${record.sendFlag }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="activitylist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="search" paginator="${getRecommendForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="activitylist:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<input type="hidden" name="userId" id="userId" value= "${getRecommendForm.userId}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${getRecommendForm.paginatorPage}" />
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${ getRecommendForm.userName}" />
				</div>
				<div class="form-group">
					<label>手机号码</label>
					<input type="text" name="mobile" class="form-control input-sm underline" maxlength="20" value="${getRecommendForm.mobile}"/>
				</div>
				<div class="form-group">
					<label>所属部门</label>
					<div class="dropdown-menu no-radius">
						<input type="text" class="form-control input-sm underline margin-bottom-10 " value="" id="combotree_search" placeholder="Search" >
						<input type="hidden" id="combotree_field_hidden"  name="combotreeSrch" value="${getRecommendForm.combotreeSrch}">
						<div id="combotree-panel" style="width:270px;height:300px;position:relative;overflow:hidden;">
							<div id="combotree" class="tree-demo" ></div>
						</div>
					</div>

					<span class="input-icon input-icon-right" data-toggle="dropdown" >
						<input id="combotree-field" type="text" class="form-control underline form " readonly="readonly">
						<i class="fa fa-remove fn-ClearDep" style="cursor:pointer;"></i>
					</span>
				</div>
				<div class="form-group">
					<label>推荐好友</label>
					<input type="text" name="inviteByUserName" class="form-control input-sm underline" maxlength="20" value="${getRecommendForm.inviteByUserName}"/>
				</div>
				
				<div class="form-group">
					<label>推荐好友手机号</label>
					<input type="text" name="inviteByMobile" class="form-control input-sm underline" maxlength="20" value="${getRecommendForm.inviteByMobile}"/>
				</div>
				
				<div class="form-group">
					<label>获得推荐星类型</label>
					<select name="recommendSourceSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${recommendSourceList }" var="property" begin="0" step="1" >
							<option value="${property.nameCd }"
								<c:if test="${property.nameCd eq getRecommendForm.recommendSourceSrch}">selected="selected"</c:if>>
								<c:out value="${property.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>发放状态</label>
					<select name="sendFlagSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<option value="0"
							<c:if test="${0 eq getRecommendForm.sendFlagSrch}">selected="selected"</c:if>>
							未发放
						</option>
						<option value="1"
							<c:if test="${1 eq getRecommendForm.sendFlagSrch}">selected="selected"</c:if>>
							已发放
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>获得时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="timeStartSrch" id="timeStartSrch" class="form-control underline" value="${getRecommendForm.timeStartSrch}" />
							<i class="ti-calendar"></i> </span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<input type="text" name="timeEndSrch" id="timeEndSrch" class="form-control underline" value="${getRecommendForm.timeEndSrch}" />
					</div>
				</div>
			</shiro:hasPermission>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/invite/getrecommend/getRecommendList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
