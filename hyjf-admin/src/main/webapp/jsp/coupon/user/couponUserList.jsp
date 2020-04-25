<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>

<shiro:hasPermission name="couponuser:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="优惠券列表" />
		 
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">优惠券列表</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<ul class="nav nav-tabs" id="myTab">
						<li ><a href="${webRoot}/coupon/checklist/init">手动批量发放列表</a></li>
						<li class="active"><a href="javascript:void(0);">优惠券批量发放明细</a></li>
					</ul>
                    <ul></ul>
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="couponuser:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${couponUserForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${couponUserForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								    <shiro:hasPermission name="couponuser:ADD">
										<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
											data-toggle="tooltip" data-placement="bottom" data-original-title="添加">手动发放<i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<%--<shiro:hasPermission name="couponuser:IMPORT">
									    <a class="btn btn-o btn-primary btn-sm hidden-xs fn-Import"
											data-toggle="tooltip" data-placement="bottom" data-original-title="手动批量发放">手动批量发放<i class="fa fa-plus"></i></a>
									</shiro:hasPermission>--%>
									<shiro:hasPermission name="couponuser:EXPORT">
										<a class="btn btn-o btn-primary btn-sm fn-Export"
												data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							</shiro:hasPermission>
							<%-- 列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">优惠券ID</th>
										<th class="center">优惠券类别编号</th>
										<th class="center">用户名</th>
										<th class="center">发券时属性</th>
										<th class="center">注册渠道</th>
										<th class="center">优惠券类型</th>
										<th class="center">面值</th>
										<th class="center">出借限额</th>
										
										<th class="center">有效期</th>
										<th class="center">来源</th>
										<th class="center">内容</th>
										<th class="center">使用状态</th>
										<th class="center">获得时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty couponUserForm.recordList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${couponUserForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((couponUserForm.paginatorPage - 1) * couponUserForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.couponUserCode }"></c:out></td>
													<td class="center"><c:out value="${record.couponCode }"></c:out></td>
													<td class="center"><c:out value="${record.username }"></c:out></td>
													<td class="center"><c:out value="${record.attribute==0?'无主单': record.attribute==1? '有主单' : record.attribute==2 ? '线下员工' : record.attribute==3 ? '线上员工' : ''}"></c:out></td>
													<td class="center"><c:out value="${record.channel}"></c:out></td>
													<td class="center"><c:out value="${record.couponType}"></c:out></td>
													<td class="center"><c:out value="${record.couponQuota }"></c:out></td>
													<td class="center"><c:out value="${record.tenderQuota }"></c:out></td>
													<td class="center"><c:out value="${record.endTime}"></c:out></td>
													<td class="center"><c:out value="${record.couponSource}"></c:out></td>
													<td class="center"><c:out value="${record.couponContent}"></c:out></td>
													<td class="center"><c:out value="${record.usedFlagView}"></c:out></td>
													<td class="center"><c:out value="${record.addTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<a class="btn btn-transparent btn-xs fn-Info" 
																	data-toggle="tooltip" tooltip-placement="top" data-id="${record.id }" data-original-title="详情"><i class="fa fa-list"></i></a>
															<c:if test="${record.usedFlag == 0||record.usedFlag == 2}">
															<shiro:hasPermission name="couponuser:DELETE">
																<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
															</c:if>
															<c:if test="${record.usedFlag == 3}">
															<shiro:hasPermission name="couponuser:AUDIT">
																<a class="btn btn-transparent btn-xs fn-Fix" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="审核"><i class="fa fa-gavel"></i></a>
															</shiro:hasPermission>
															</c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<li>
																		<a href= "#" class="fn-Info" >详情</a>
																	</li>
																	<c:if test="${record.usedFlag == 0||record.usedFlag == 2}">
																	<shiro:hasPermission name="couponuser:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
																	</shiro:hasPermission>
																	</c:if>
																	<c:if test="${record.usedFlag == 3}">
																	<shiro:hasPermission name="couponuser:AUDIT">
																		<li><a class="fn-Fix" data-id="${record.id }">审核</a></li>
																	</shiro:hasPermission>
																	</c:if>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="searchAction" paginator="${couponUserForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${couponUserForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>优惠券ID</label> <input type="text" name="couponUserCode"
					class="form-control input-sm underline"
					value="${couponUserForm.couponUserCode}" />
			</div>
			<div class="form-group">
				<label>优惠券类别编号</label> <input type="text" name="couponCode"
					class="form-control input-sm underline"
					value="${couponUserForm.couponCode}" />
			</div>
			
			<div class="form-group">
				<label>用户名</label> <input type="text" name="username"
					class="form-control input-sm underline"
					value="${couponUserForm.username}" />
			</div>
			<div class="form-group">
				<label>来源</label>
				<select name="couponSource" class="form-control underline form-select2">
					<option value=""></option>
					<option value="1" <c:if test="${couponUserForm.couponSource=='1'}">selected</c:if>>手动发放</option>
					<option value="2" <c:if test="${couponUserForm.couponSource=='2'}">selected</c:if>>活动发放</option>
					<option value="3" <c:if test="${couponUserForm.couponSource=='3'}">selected</c:if>>会员礼包</option>
				</select>
			</div>
			<div class="form-group">
					<label>优惠券类型</label>
					<select name="couponType" class="form-control underline form-select2">
						<option value=""></option>
						<option value="1" <c:if test="${couponUserForm.couponType=='1'}">selected</c:if>>体验金</option>
						<option value="2" <c:if test="${couponUserForm.couponType=='2'}">selected</c:if>>加息券</option>
						<option value="3" <c:if test="${couponUserForm.couponType=='3'}">selected</c:if>>代金券</option>
					</select>
			</div>
			<div class="form-group">
					<label>状态</label>
					<select name="usedFlag" class="form-control underline form-select2">
						<option value=""></option>
						<option value="3" <c:if test="${couponUserForm.usedFlag=='3'}">selected</c:if>>待审核</option>
						<option value="0" <c:if test="${couponUserForm.usedFlag=='0'}">selected</c:if>>未使用</option>
						<option value="2" <c:if test="${couponUserForm.usedFlag=='2'}">selected</c:if>>审核未通过</option>
						<option value="1" <c:if test="${couponUserForm.usedFlag=='1'}">selected</c:if>>已使用</option>
						<option value="4" <c:if test="${couponUserForm.usedFlag=='4'}">selected</c:if>>已失效</option>
					</select>
			</div>
			<div class="form-group">
				<label>有效期</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="en-start-date-time" class="form-control underline" value="${couponUserForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="en-end-date-time" class="form-control underline" value="${couponUserForm.timeEndSrch}" />
				</div>
			</div>
			<div class="form-group">
				<label>获得时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartAddSrch" id="start-date-time" class="form-control underline" value="${couponUserForm.timeStartAddSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndAddSrch" id="end-date-time" class="form-control underline" value="${couponUserForm.timeEndAddSrch}" />
				</div>
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

		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/coupon/user/couponUserList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
