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


<shiro:hasPermission name="huixiaofei:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="汇消费数据对接" />
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">汇消费数据对接</h1>
			<span class="mainDescription">本功能可与达飞金融进行汇消费数据对接。</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb" value="${huixiaofeiForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${huixiaofeiForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
											<shiro:hasPermission name="huixiaofei:INFO">
									<a class="btn btn-o btn-primary btn-sm fn-Heimingdan"
											data-toggle="tooltip" data-placement="bottom" data-original-title="黑名单">黑名单 <i class="fa fa-pencil"></i></a>
											</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新">刷新 <i class="fa fa-refresh"></i></a>
											<shiro:hasPermission name="huixiaofei:EXPORT">
									<a class="btn btn-o btn-primary btn-sm fn-DownloadData"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下载达飞数据">下载达飞数据 <i class="fa fa-download"></i></a>
											</shiro:hasPermission>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
												data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
												data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 公司环境列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
									<col style="width:;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">时间</th>
										<th class="center">编号</th>
										<th class="center">合作方</th>
										<th class="center">总金额</th>
										<th class="center">实际金额</th>
										<th class="center">人数</th>
										<th class="center">状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty huixiaofeiForm.recordList}">
											<tr>
												<td colspan="9">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${huixiaofeiForm.recordList }" var="record"
												begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out
															value="${status.index+((huixiaofeiForm.paginatorPage - 1) * huixiaofeiForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out
															value="${record.insertTime }"></c:out></td>
													<td class="center"><c:out value="${record.consumeId }"></c:out></td>
													<td align="left">达飞</td>
													<td align="right"><fmt:formatNumber value="${record.amount }" pattern="#,##0.00#" /></td>
													<td align="right"><fmt:formatNumber value="${record.amountReal }" pattern="#,##0.00#" /></td>
													<td class="center"><c:if
															test="${record.personNum-record.personReal!=0 }">
															<font color="red"><c:out
																	value="${record.personNum-record.personReal }"></c:out>人</font>/
													</c:if> <font color="green"><c:out
																value="${record.personReal }"></c:out>人</font></td>
													<td class="center"><c:choose>
															<c:when test="${record.release==1}">
															已打包
															</c:when>
															<c:otherwise>
																<c:choose>
																	<c:when test="${record.status==0}">
																	未审核
																	</c:when>
																	<c:when test="${record.status==1}">
																	已审核
																	</c:when>
																	<c:when test="${record.status==2}">
																	已处理
																	</c:when>
																</c:choose>
															</c:otherwise>
														</c:choose></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<c:if test="${record.release==1||(record.release==0&&record.status==2)}">
															<shiro:hasPermission name="huixiaofei:INFO">
															<a class="btn btn-transparent btn-xs fn-infoAction" data-toggle="tooltip"href="${webRoot}/manager/huixiaofei/infoAction?id=${record.id }&type=1"
																	tooltip-placement="top" data-original-title="查看"><i class="fa fa-list"></i></a>
																	</shiro:hasPermission>
															</c:if>
															<c:if test="${record.release==0&&(record.status==0||record.status==1)}">
															<shiro:hasPermission name="huixiaofei:MODIFY">
															<a class="btn btn-transparent btn-xs fn-infoAction" data-toggle="tooltip"href="${webRoot}/manager/huixiaofei/infoAction?id=${record.id }&type=0"
																	tooltip-placement="top" data-original-title="编辑"><i
																	class="fa fa-pencil"></i></a>
																	</shiro:hasPermission>
															</c:if>
															<shiro:hasPermission name="huixiaofei:MODIFY">
																<c:if test="${record.release==0&&record.status==2}">
																	<a class="btn btn-transparent btn-xs fn-topackage"id="fn-package${record.id }" data-toggle="tooltip"
																			tooltip-placement="top" data-original-title="打包"><i class="fa fa-file-zip-o"></i></a>
																</c:if>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<c:if test="${record.release==1||(record.release==0&&record.status==2)}"><shiro:hasPermission name="huixiaofei:INFO">
															<a class="btn btn-transparent btn-xs fn-infoAction"id="fn-package${record.id }" data-toggle="tooltip" href="${webRoot}/manager/huixiaofei/infoAction?id=${record.id }&type=1"
																	tooltip-placement="top" data-original-title="查看"><i
																	class="fa fa-pencil"></i></a></shiro:hasPermission>
																	</c:if>
																	<c:if test="${record.release==0&&(record.status==0||record.status==1)}"><shiro:hasPermission name="huixiaofei:MODIFY">
															<a class="btn btn-transparent btn-xs fn-infoAction"id="fn-package${record.id }" data-toggle="tooltip" href="${webRoot}/manager/huixiaofei/infoAction?id=${record.id }&type=0"
																	tooltip-placement="top" data-original-title="编辑"><i
																	class="fa fa-pencil"></i></a></shiro:hasPermission>
																	</c:if>
																	<c:if test="${record.release==0&&record.status==2}"><shiro:hasPermission name="huixiaofei:PACKAGE">
															<a class="btn btn-transparent btn-xs fn-topackage"id="fn-package${record.id }"id="fn-package${record.id } data-toggle="tooltip" href="${webRoot}/manager/huixiaofei/infoAction?id=${record.id }&type=0"
																	tooltip-placement="top" data-original-title="打包"><i
																	class="fa fa-lightbulb-o"></i></a></shiro:hasPermission>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="init" paginator="${huixiaofeiForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 查询面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" />

			<input type="hidden" name="moveFlag" id="moveFlag" value="BORROW_HXF" />
			<input type="hidden" name="consumeId" id="consumeId" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${huixiaofeiForm.paginatorPage}" />
			<div class="form-group">
				<label>状态</label> <select name="status"
					class="form-control underline form-select2">
					<option value="">全部</option>
					<option value="0"
						<c:if test="${huixiaofeiForm.status==0}">selected="selected"</c:if>>未审核</option>
					<option value="1"
						<c:if test="${huixiaofeiForm.status==1}">selected="selected"</c:if>>已审核</option>
					<option value="2"
						<c:if test="${huixiaofeiForm.status==2}">selected="selected"</c:if>>已处理</option>
					<option value="3"
						<c:if test="${huixiaofeiForm.release==1}">selected="selected"</c:if>>已打包</option>
				</select>
			</div>
			<div class="form-group">
				<label>时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text"
						name="startCreate" id="startCreate" class="form-control underline"
						value="${huixiaofeiForm.startCreate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="endCreate" id="endCreate"
						class="form-control underline" value="${huixiaofeiForm.endCreate}" />
				</div>
			</div>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/huixiaofei/huixiaofei.js"></script>
		</tiles:putAttribute>
		
	</tiles:insertTemplate>
</shiro:hasPermission>
