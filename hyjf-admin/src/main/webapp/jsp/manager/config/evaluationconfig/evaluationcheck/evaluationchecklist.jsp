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

<shiro:hasPermission name="evaluationcheck:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="测评配置" />

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
						<shiro:hasPermission name="evaluationcheck:VIEW">
							<li class="active"><a href="${webRoot}/manager/config/evaluationcheck/init">开关</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="evaluationmoney:VIEW">
							<li><a href="${webRoot}/manager/config/evaluationmoney/init">限额配置</a></li>
						</shiro:hasPermission>
						<shiro:hasPermission name="borrowLevelConfig:VIEW">
							<li><a href="${webRoot}/manager/config/borrowlevelconfig/init">信用等级配置</a></li>
						</shiro:hasPermission>
					</ul>
						<div class="tab-content">
							<div class="tab-pane fade in active">
									<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${borrowflowForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${borrowflowForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
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
										<th class="center">散标债转出借者测评类型校验</th>
										<th class="center">智投出借者测评类型校验</th>
										<th class="center">散标债转单笔出借金额校验</th>
										<th class="center">智投单笔出借金额校验</th>
										<th class="center">散标债转待收本金校验</th>
										<th class="center">智投待收本金校验</th>
										<th class="center">投标时校验二期</th>
										<th class="center">操作</th>
									</tr>
									</thead>
									<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty evaluationcheckForm.recordList}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${evaluationcheckForm.recordList }" var="check" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((evaluationcheckForm.paginatorPage - 1) * evaluationcheckForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${check.debtEvaluationTypeCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.intellectualEveluationTypeCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.deptEvaluationMoneyCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.intellectualEvaluationMoneyCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.deptCollectionEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.intellectualCollectionEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center"><c:out value="${check.investmentEvaluationCheck == '1' ? '开启' : '关闭'  }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="borrowflow:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${check.id}"
																   data-toggle="tooltip" tooltip-placement="top" data-original-title="编辑"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="borrowflow:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
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
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${evaluationcheckForm.paginator}"></hyjf:paginator>
								<br /> <br />
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
		<%-- 边界面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationcheck/evaluationchecklist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
