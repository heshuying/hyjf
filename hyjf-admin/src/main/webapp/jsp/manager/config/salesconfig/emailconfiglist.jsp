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
		<tiles:putAttribute name="pageTitle" value="报表邮件收件人配置" />
		 
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">报表邮件收件人配置</h1>
			<span class="mainDescription"></span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<ul></ul>
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
									<c:set var="jspPrevDsb" value="${EmailRecipientForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${EmailRecipientForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>

								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
								   data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>

								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
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
										<th class="center">业务名称</th>
										<th class="center">联系人邮箱</th>
										<th class="center">邮件发送时间</th>
										<th class="center">状态</th>
										<th class="center">创建时间</th>
										<th class="center">更新操作人</th>
										<th class="center">更新时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty EmailRecipientForm.recordList}">
											<tr>
												<td colspan="13">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${EmailRecipientForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>

													<td class="center"><c:out value="${status.index+((EmailRecipientForm.paginatorPage - 1) * EmailRecipientForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.businessName }"></c:out></td>
                                                    <td class="center"><c:out value="${record.email }"></c:out></td>

                                                    <td class="center">
														<c:if test="${record.timePoint == '1' }">
															<c:out value="每个工作日"></c:out>
														</c:if>
														<c:if test="${record.timePoint == '2' }">
															<c:out value="每天"></c:out>
														</c:if>
														<c:if test="${record.timePoint == '3' }">
															<c:out value="每月第一个工作日"></c:out>
														</c:if>
														<c:out value="${record.time}"></c:out>
													</td>

                                                    <td class="center" id="flag">
														<c:if test="${record.status == '1' }">
															<c:out value="有效"></c:out>
														</c:if>
														<c:if test="${record.status == '2' }">
															<c:out value="无效"></c:out>
														</c:if>
													</td>
													<td class="center">${record.addtime}</td>
                                                    <td class="center">${record.updateName}</td>
                                                    <td class="center">${record.formatUpdatetime}</td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
																<a class="btn btn-transparent btn-xs fn-find"
																   data-toggle="tooltip" tooltip-placement="top" data-id="${record.id}" data-original-title="查看"><i class="fa ">【查看】</i></a>
																<a class="btn btn-transparent btn-xs fn-update" data-id="${record.id}"   data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa  fa fa-white">【修改】</i></a>
															<c:if test="${record.status == '1'}">
																<a class="btn btn-transparent btn-xs fn-forbidden" data-id="${record.id }"  data-toggle="tooltip" tooltip-placement="top" data-original-title="禁用"><i class="fa ">【禁用】</i></a>
															</c:if>
                                                            <c:if test="${record.status == '2'}">
                                                                <a class="btn btn-transparent btn-xs fn-start" data-id="${record.id }"  data-toggle="tooltip" tooltip-placement="top" data-original-title="启用"><i class="fa ">【启用】</i></a>
                                                            </c:if>
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
								action="init" paginator="${EmailRecipientForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${EmailRecipientForm.paginatorPage}" />
			<!-- 检索条件 -->
			<div class="form-group">
				<label>业务名称</label>
				<input type="text" name="businessName" class="form-control input-sm underline"  maxlength="20" value="${EmailRecipientForm.businessName}" />
			</div>
            <%--<div class="form-group">
                <label>状态</label>
                <select name="status" id="status"  class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="1" <c:if test="${EmailRecipientForm.status=='1'}">selected</c:if>>有效</option>
                    <option value="2" <c:if test="${EmailRecipientForm.status=='2'}">selected</c:if>>无效</option>
                </select>
            </div>--%>

            <div class="form-group">
                <label>状态</label>
                <select name="status" id="status" class="form-control underline form-select2">
                    <option value="">全部</option>
                    <option value="1" <c:if test="${EmailRecipientForm.status == 1}">selected="selected"</c:if>><c:out value="有效"></c:out></option>
                    <option value="2"<c:if test="${EmailRecipientForm.status == 2}">selected="selected"</c:if>><c:out value="无效"></c:out></option>
                </select>
            </div>

			<div class="form-group">
				<label>创建时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartCreateSrch" id="start-date-time" class="form-control underline" value="${EmailRecipientForm.timeStartCreateSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndCreateSrch" id="end-date-time" class="form-control underline" value="${EmailRecipientForm.timeEndCreateSrch}" />
				</div>
			</div>

            <div class="form-group">
                <label>更新时间</label>
                <div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartUpdateSrch" id="en-start-date-time" class="form-control underline" value="${EmailRecipientForm.timeStartUpdateSrch}" />
						<i class="ti-calendar"></i> </span>
                    <span class="input-group-addon no-border bg-light-orange">~</span>
                    <input type="text" name="timeEndUpdateSrch" id="en-end-date-time" class="form-control underline" value="${EmailRecipientForm.timeEndUpdateSrch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/salesconfig/emailconfiglist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>