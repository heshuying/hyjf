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

<shiro:hasPermission name="evaluationchecklog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="互金系统配置" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">测评配置</h1>
			<span class="mainDescription">本功能可以修改测评配置信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
						<shiro:hasPermission name="evaluationchecklog:VIEW">
							<li class="active"><a href="${webRoot}/manager/config/evaluationchecklog/init">开关</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="evaluationmoneylog:VIEW">
							<li><a href="${webRoot}/manager/config/evaluationmoneylog/init">限额配置</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="borrowLevelConfigLog:VIEW">
							<li><a href="${webRoot}/manager/config/evaluationconfig/borrowlevelconfiglog/init">信用等级配置</a></li>
						</shiro:hasPermission>
					</ul>
						<div class="tab-content">
							<div class="tab-pane fade in active">
									<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${evaluationchecklogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${evaluationchecklogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
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
										<th class="center">散标测评类型</th>
										<th class="center">智投测评类型</th>
										<th class="center">散标单笔测评金额</th>
										<th class="center">智投单笔测评金额</th>
										<th class="center">散标待收本金</th>
										<th class="center">智投待收本金</th>
										<th class="center">投标时校验</th>
										<th class="center">修改人</th>
										<th class="center">IP</th>
										<th class="center">操作时间</th>
									</tr>
									</thead>
									<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty evaluationchecklogForm.recordList}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${evaluationchecklogForm.recordList }" var="log" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((evaluationchecklogForm.paginatorPage - 1) * evaluationchecklogForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${log.debtEvaluationTypeCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.intellectualEveluationTypeCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.deptEvaluationMoneyCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.intellectualEvaluationMoneyCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.deptCollectionEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.intellectualCollectionEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.investmentEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${log.updateUser}"></c:out></td>
													<td class="center"><c:out value="${log.ip}"></c:out></td>
													<td><fmt:formatDate value="${log.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
									</tbody>
								</table>
									<%-- 分页栏 --%>
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${evaluationchecklogForm.paginator}"></hyjf:paginator>
								<br /> <br />
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="evaluationchecklog:SEARCH">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${evaluationchecklogForm.paginatorPage}" />
			<input type="hidden" name="id" id="id" />
			<!-- 查询条件 -->
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
				<span class="input-icon"> <input type="text"
												 name="startTime" id="startTime" class="form-control underline"
												 value="${evaluationchecklogForm.startTime}" /> <i class="ti-calendar"></i>
				</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endTime" id="endTime"
						   class="form-control underline"
						   value="${evaluationchecklogForm.endTime}" />
				</div>
			</div>
			<div class="form-group">
				<label>修改人</label>
				<input type="text" name="updateUser" id="updateUser" class="form-control input-sm underline" value="${evaluationchecklogForm.updateUser}" />
			</div>
			</shiro:hasPermission>
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
					name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
				.thumbnails-wrap {
					border: 1px solid #ccc;
					padding: 3px;
					display: inline-block;
				}
				.thumbnails-wrap img {

					min-width: 35px;
					height: 22px;
				}
				.popover {
					max-width: 500px;
				}
				.popover img {
					max-width: 460px;
				}
				.nav-tabs>li.active>a, .nav-tabs>li.active>a:hover, .nav-tabs>li.active>a:focus{
					color: #555;
					cursor: default;
					background-color: 	#66b1ff;
					border: 1px solid #ddd;
					border-bottom-color: transparent;
				}
			</style>

		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationcheck/evaluationcheckloglist.js"></script>
		</tiles:putAttribute>

	</tiles:insertTemplate>
</shiro:hasPermission>
