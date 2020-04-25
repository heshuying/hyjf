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


<shiro:hasPermission name="rechargeexception:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="充值管理" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">充值管理</h1>
			<span class="mainDescription">这里添加充值管理描述。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="rechargeexception:SEARCH">
								<div class="well">
									<c:set var="jspPrevDsb" value="${form.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${form.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a> <a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" data-placement="bottom"
										data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"> <i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i>
									</a>
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
										<th class="center">用户名</th>
										<th class="center">订单号</th>
										<th class="center">充值渠道</th>
										<th class="center">充值类型</th>
										<th class="center">充值银行</th>
										<th class="center">充值金额</th>
										<th class="center">手续费</th>
										<th class="center">垫付手续费</th>
										<th class="center">到账金额</th>
										<th class="center">充值状态</th>
										<th class="center">充值平台</th>
										<th class="center">充值时间</th>
										<th class="center">失败原因</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty form.recordList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${form.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((form.paginatorPage - 1) * form.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out value="${record.nid }"></c:out></td>
													<td class="center"><c:out value="${record.type }"></c:out></td>
													<td class="center"><c:if test="${'B2C' eq record.gateType }">个人网银充值</c:if> <c:if test="${'B2B' eq record.gateType }">企业网银充值</c:if> <c:if test="${'QP' eq record.gateType }">快捷充值</c:if></td>
													<td><c:out value="${record.bankName }"></c:out></td>
													<td align="right"><fmt:formatNumber value="${record.money}" type="number" pattern="#,##0.00#" /></td>
													<td align="right"><c:if test="${record.fee!=null }">
															<fmt:formatNumber value="${record.fee}" type="number" pattern="#,##0.00#" />
														</c:if> <c:if test="${record.fee==null }">0.00</c:if></td>
													<td align="right"><c:if test="${record.dianfuFee!=null }">
															<fmt:formatNumber value="${record.dianfuFee}" type="number" pattern="#,##0.00#" />
														</c:if> <c:if test="${record.dianfuFee==null }">0.00</c:if></td>
													<td align="right"><fmt:formatNumber value="${record.balance}" type="number" pattern="#,##0.00#" /></td>
													<td class="center"><c:out value="${record.status }"></c:out></td>
													<td class="center"><c:out value="${record.client  }"></c:out></td>
													<td class="center"><c:out value="${record.createTime  }"></c:out></td>
													<td class="center"><c:out value="${record.message  }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="rechargeexception:RECHARGE_EXCEPTION">
																<a class="btn btn-transparent btn-xs tooltips fn-fix" data-userId="${record.userId }" data-nid="${record.nid }" data-id="${record.id }" data-status="0" data-toggle="tooltip" tooltip-placement="top" data-original-title="充值掉单修复"><i class="fa fa-gavel"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="rechargeexception:RECHARGE_EXCEPTION">
																		<li><a class="fn-fix" data-nid="${record.nid }" data-status="0" data-userId="${record.userId }">充值掉单修复</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${form.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="rechargeexception:SEARCH">
				<input type="hidden" name="userId" id="userId" />
				<input type="hidden" name="nid" id="nid" />
				<input type="hidden" name="status" id="status" />
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${form.paginatorPage}" />

				<!-- 查询条件 -->
				<div class="form-group">
					<label>用户名</label> <input type="text" name="usernameSearch" class="form-control input-sm underline" value="${form.usernameSearch}" />
				</div>
				<div class="form-group">
					<label>订单号</label> <input type="text" name="nidSearch" class="form-control input-sm underline" value="${form.nidSearch}" />
				</div>
				<div class="form-group">
					<label>充值银行</label> <select name="bankCodeSearch" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${form.bankList }" var="bank" begin="0" step="1" varStatus="status">
							<option value="${bank.code }" <c:if test="${bank.code eq form.bankCodeSearch}">selected="selected"</c:if>>
								<c:out value="${bank.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>充值类型</label> <select name="typeSearch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="B2C" <c:if test="${'B2C' eq form.typeSearch}">selected="selected"</c:if>>
							<c:out value="个人网银充值"></c:out>
						</option>
						<option value="B2B" <c:if test="${'B2B' eq form.typeSearch}">selected="selected"</c:if>>
							<c:out value="企业网银充值"></c:out>
						</option>
						<option value="QP" <c:if test="${'QP' eq form.typeSearch}">selected="selected"</c:if>>
							<c:out value="快捷充值"></c:out>
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>充值平台</label> <select name="clientTypeSearch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${'0' eq form.clientTypeSearch}">selected="selected"</c:if>>
							<c:out value="PC"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq form.clientTypeSearch}">selected="selected"</c:if>>
							<c:out value="微信"></c:out>
						</option>
						<option value="2" <c:if test="${'2' eq form.clientTypeSearch}">selected="selected"</c:if>>
							<c:out value="Android"></c:out>
						</option>
						<option value="3" <c:if test="${'3' eq form.clientTypeSearch}">selected="selected"</c:if>>
							<c:out value="IOS"></c:out>
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>充值手续费收取方式</label> <select name="getfeefromSearch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="0" <c:if test="${'0' eq form.getfeefromSearch}">selected="selected"</c:if>>
							<c:out value="向用户收取"></c:out>
						</option>
						<option value="1" <c:if test="${'1' eq form.getfeefromSearch}">selected="selected"</c:if>>
							<c:out value="向商户收取"></c:out>
						</option>
					</select>
				</div>
				<div class="form-group">
					<label>添加时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${form.startDate}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${form.endDate}" />
					</div>
				</div>
			</shiro:hasPermission>
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
			<script type='text/javascript' src="${webRoot}/jsp/exception/rechargeexception/rechargeexception_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
