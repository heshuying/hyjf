<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="bailConfigLog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="合作额度配置日志记录" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">合作额度配置日志记录</h1>
			<span class="mainDescription">这里添加合作额度配置日志记录描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="bailConfigLog:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${bailConfigLogForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${bailConfigLogForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									   data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
									   data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br />
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">机构名称</th>
										<th class="center">修改字段</th>
										<th class="center">前值</th>
										<th class="center">后值</th>
										<th class="center">修改人</th>
										<th class="center">修改日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty bailConfigLogForm.recordList}">
											<tr>
												<td colspan="7">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${bailConfigLogForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((bailConfigLogForm.paginatorPage - 1) * bailConfigLogForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.instName }"></c:out></td>
													<td class="center"><c:out value="${record.modifyColumn }"></c:out></td>
													<td class="center"><c:out value="${record.beforeValue  }"></c:out></td>
													<td class="center"><c:out value="${record.afterValue  }"></c:out></td>
													<td class="center"><c:out value="${record.createUserName  }"></c:out></td>
													<td class="center"><c:out value="${record.createTimeStr  }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchAction" paginator="${bailConfigLogForm.paginator}"></hyjf:paginator>
								<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${bailConfigLogForm.paginatorPage}" />

			<!-- 查询条件 -->
			<div class="form-group">
				<label>机构名称</label>
				<select name="instCodeSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1" varStatus="status">
						<option value="${inst.instCode }"
								<c:if test="${inst.instCode eq bailConfigLogForm.instCodeSrch}">selected="selected"</c:if>>
							<c:out value="${inst.instName}"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>修改字段</label> <select name="modifyColumnSrch" class="form-control underline form-select2">
				<option value=""></option>
				<%--<option value="保证金金额" <c:if test="${'保证金金额' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="保证金金额"></c:out>
				</option>
				<option value="保证金比例" <c:if test="${'保证金比例' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="保证金比例"></c:out>
				</option>--%>
				<option value="日推标额度" <c:if test="${'日推标额度' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="日推标额度"></c:out>
				</option>
				<option value="月推标额度" <c:if test="${'月推标额度' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="月推标额度"></c:out>
				</option>
				<option value="合作额度" <c:if test="${'合作额度' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="合作额度"></c:out>
				</option>
				<option value="合作周期" <c:if test="${'合作周期' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="合作周期"></c:out>
				</option>
				<%--<option value="在贷余额授信额度" <c:if test="${'在贷余额授信额度' eq bailConfigLogForm.modifyColumnSrch}">selected="selected"</c:if>>
					<c:out value="在贷余额授信额度"></c:out>
				</option>--%>
			</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDate" id="startDate" class="form-control underline" value="${bailConfigLogForm.startDate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDate" id="endDate" class="form-control underline" value="${bailConfigLogForm.endDate}" />
				</div>
			</div>
			<div class="form-group">
				<label>修改人</label> <input type="text" name="createUserNameSrch" class="form-control input-sm underline" value="${bailConfigLogForm.createUserNameSrch}" />
			</div>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/bailconfiglog/bailconfiglog.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
