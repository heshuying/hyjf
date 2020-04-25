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


<shiro:hasPermission name="recharge:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="平台转账" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">平台转账</h1>
			<span class="mainDescription">这里添加平台转账描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${rechargeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${rechargeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<%-- <shiro:hasPermission name="permissions:ADD">
									<a class="btn btn-o btn-primary btn-sm fn-Add"
											data-toggle="tooltip" tooltip-placement="top" data-original-title="添加新的权限类别"><i class="fa fa-plus"></i> 添加</a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm fn-Modifys"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="修改权限信息"><i class="fa fa-pencil"></i> 修改</a>
								<a class="btn btn-o btn-primary btn-sm fn-Deletes"
										data-toggle="tooltip" tooltip-placement="top" data-original-title="删除权限"><i class="fa fa-trash-o"></i> 删除</a> --%>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i>
								</a>
								<shiro:hasPermission name="recharge:ADD">
									<a class="btn btn-o btn-primary btn-sm fn-handRecharge" data-toggle="tooltip" tooltip-placement="top" data-original-title="平台转账">平台转账 <i class="fa fa-download"></i>
									</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="recharge:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i>
									</a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">转账订单号</th>
										<th class="center">用户名</th>
										<th class="center">手机号</th>
										<th class="center">转账金额</th>
										<th class="center">可用余额</th>
										<th class="center">转账状态</th>
										<th class="center">转账时间</th>
										<th class="center">备注</th>
										<th class="center">发送日期</th>
										<th class="center">发送时间</th>
										<th class="center">系统跟踪号</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty rechargeForm.recordList}">
											<tr>
												<td colspan="17">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${rechargeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((rechargeForm.paginatorPage - 1) * rechargeForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.nid }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.mobile }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.money}" type="number" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.balance}" type="number" pattern="#,##0.00#" /></td>
													<td class="center"><c:out value="${record.statusName }"></c:out></td>
													<td class="center"><c:out value="${record.createTime  }"></c:out></td>
													<td class="center"><c:out value="${record.remark  }"></c:out></td>
													<td class="center"><c:out value="${record.txDate  }"></c:out></td>
													<td class="center"><c:out value="${record.txTime  }"></c:out></td>
													<td class="center"><c:out value="${record.bankSeqNo  }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="rechargePT_list" paginator="${rechargeForm.paginator}"></hyjf:paginator>
								<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="nid" id="nid" />
			<input type="hidden" name="statusName" id="statusName" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${rechargeForm.paginatorPage}" />

			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label> <input type="text" name="usernameSearch" class="form-control input-sm underline" value="${rechargeForm.usernameSearch}" />
			</div>
			<div class="form-group">
				<label>订单号</label> <input type="text" name="nidSearch" class="form-control input-sm underline" value="${rechargeForm.nidSearch}" />
			</div>
			<div class="form-group">
				<label>转账状态</label> <select name="statusSearch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="2" <c:if test="${'2' eq rechargeForm.statusSearch}">selected="selected"</c:if>>
						<c:out value="成功"></c:out>
					</option>
					<!-- 充值中应该是0， -->
					<option value="1" <c:if test="${'1' eq rechargeForm.statusSearch}">selected="selected"</c:if>>
						<c:out value="转账中"></c:out>
					</option>
					<option value="0" <c:if test="${'0' eq rechargeForm.statusSearch}">selected="selected"</c:if>>
						<c:out value="失败"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${rechargeForm.startDate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${rechargeForm.endDate}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
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
			<script type='text/javascript' src="${webRoot}/jsp/finance/recharge/rechargePT_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
