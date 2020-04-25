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


<shiro:hasPermission name="msgpushnotices:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="手动发送消息" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">手动发送消息</h1>
			<span class="mainDescription">本功能可以增加修改删除自动触发消息模版。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${msgPushNoticesForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${msgPushNoticesForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}" data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="msgpushnotices:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add" data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a> <a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel" data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 列表 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
									<col style="width: 80px;" />
									<col style="width: 80px;" />
									<col style="width: 80px;" />
									<col style="width: 100px;" />
									<col style="width: 180px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
									<col style="width: 100px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">标签类型</th>
										<th class="center">标题</th>
										<th class="center">消息编码</th>
										<th class="center">发送时间</th>
										<th class="center">内容</th>
										<th class="center">跳转地址</th>
										<th class="center">作者</th>
										<th class="center">推送平台</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty msgPushNoticesForm.recordList}">
											<tr>
												<td colspan="11">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${msgPushNoticesForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((msgPushNoticesForm.paginatorPage - 1) * msgPushNoticesForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center">
													<c:forEach items="${noticesPushTags }" var="tag" begin="0" step="1">
															<c:if test="${tag.id eq record.tagId}"> <c:out value="${tag.tagName }"></c:out> </c:if>
													</c:forEach>	</td>
													<td class="center"><c:out value="${record.msgTitle }"></c:out></td>
													<td class="center"><c:out value="${record.msgCode }"></c:out></td>
													<td class="center"><hyjf:datetime value="${record.sendTime }"></hyjf:datetime></td>
													<td class="center"><c:if test="${fn:length(record.msgContent)<=20 }"><c:out value="${record.msgContent }"></c:out></c:if><c:if test="${fn:length(record.msgContent)>20 }"><c:out value="${fn:substring(record.msgContent,0,20) }"></c:out>...</c:if></td>
													<td class="center"><c:out value="${record.msgActionUrl }"></c:out></td>
													<td class="center"><c:out value="${record.createUserName }"></c:out></td>
													<td class="center"><c:set var="tempStrs" value="${fn:split(record.msgTerminal, ',')}" /> <c:forEach items="${plats }" var="plat" begin="0" step="1">
															<c:forEach items="${tempStrs }" var="tempStr" begin="0" step="1">
																<c:if test="${plat.nameCd eq tempStr}">
																	<c:out value="${plat.name }"></c:out>,
																</c:if>
															</c:forEach>
														</c:forEach></td>
													<td class="center"><c:forEach items="${noticesSendStatus }" var="status" begin="0" step="1">
															<c:if test="${status.nameCd eq record.msgSendStatus}">
																<c:out value="${status.name }"></c:out>
															</c:if>
														</c:forEach></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="msgpushnotices:RESEND">
																<c:if test="${record.msgSendStatus eq 1}">
																	<a class="btn btn-transparent btn-xs fn-ReSend" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="转发"><i class="fa fa-mail-reply "></i></a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="msgpushnotices:MODIFY">
																<c:if test="${record.msgSendStatus eq 0}">
																	<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="编辑"><i class="fa fa-pencil"></i></a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="msgpushnotices:INFO">
																<c:if test="${record.msgSendStatus eq 1}">
																	<a class="btn btn-transparent btn-xs fn-Info" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="msgpushnotices:DELETE">
																<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="msgpushnotices:RESEND">
																		<c:if test="${record.msgSendStatus eq 1}">
																			<li><a class="fn-ReSend" data-id="${record.id }">转发</a></li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="msgpushnotices:MODIFY">
																		<c:if test="${record.msgSendStatus eq 0}">
																			<li><a class="fn-Modify" data-id="${record.id }">编辑</a></li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="msgpushnotices:INFO">
																		<c:if test="${record.msgSendStatus eq 1}">
																			<li><a class="fn-Info" data-id="${record.id }">查看</a></li>
																		</c:if>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="msgpushnotices:DELETE">
																		<li><a class="fn-Delete" data-id="${record.id }">删除</a></li>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${msgPushNoticesForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="ids" id="ids" />
			<!-- 0为编辑1为查看2为转发 -->
			<input type="hidden" name="updateOrReSend" id="updateOrReSend" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${msgPushNoticesForm.paginatorPage}" />
			<div class="form-group">
				<label>标签类型</label> <select name="noticesTagIdSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${noticesPushTags }" var="tags" begin="0" step="1">
						<option value="${tags.id }" <c:if test="${tags.id eq msgPushNoticesForm.noticesTagIdSrch}">selected="selected"</c:if>>
							<c:out value="${tags.tagName }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>标题</label> <input type="text" name="noticesTitleSrch" class="form-control input-sm underline" value="${msgPushNoticesForm.noticesTitleSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>消息编码</label> <input type="text" name="noticesCodeSrch" class="form-control input-sm underline" value="${msgPushNoticesForm.noticesCodeSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>作者</label> <input type="text" name="noticesCreateUserNameSrch" class="form-control input-sm underline" value="${msgPushNoticesForm.noticesCreateUserNameSrch}" maxlength="20" />
			</div>
			<div class="form-group">
				<label>推送平台</label> <select name="noticesTerminalSrch" class="form-control underline form-select2">
					<option value="">全部</option>
					<c:forEach items="${plats }" var="plat" begin="0" step="1">
						<option value="${plat.nameCd }" <c:if test="${plat.nameCd eq msgPushNoticesForm.noticesTerminalSrch}">selected="selected"</c:if>>
							<c:out value="${plat.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>状态</label> <select name="noticesStatusSrch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${noticesSendStatus }" var="status" begin="0" step="1">
						<option value="${status.nameCd }" <c:if test="${status.nameCd eq msgPushNoticesForm.noticesSendStatusSrch}">selected="selected"</c:if>>
							<c:out value="${status.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>发送时间</label>
				<div class="input-group ">
					<span class="input-icon"> <input type="text" name="startSendTimeSrch" id="startSendTimeSrch" class="form-control underline" value="${msgPushNoticesForm.startSendTimeSrch}" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endSendTimeSrch" id="endSendTimeSrch" class="form-control underline" value="${msgPushNoticesForm.endSendTimeSrch}" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})" />
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
</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/msgpush/notices/notices_list.js"></script>
		</tiles:putAttribute>

	</tiles:insertTemplate>
</shiro:hasPermission>
