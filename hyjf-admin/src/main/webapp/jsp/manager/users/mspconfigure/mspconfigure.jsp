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


<shiro:hasPermission name="msp:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="安融反欺诈查询配置" />
		
		<%-- 画面主面板的标题块 --%>	
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">安融反欺诈查询配置</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<shiro:hasPermission name="msp:SEARCH">
							<div class="well">
									<c:set var="jspPrevDsb" value="${mspForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${mspForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<shiro:hasPermission name="msp:EXPORT">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Export"
										data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
								</shiro:hasPermission>
								<shiro:hasPermission name="msp:ADD">
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Add"
										data-toggle="tooltip" data-placement="bottom" data-original-title="添加">添加<i class="fa fa-plus"></i></a>
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
									<col style="width: 55px;" align="right"/>
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<th class="center">标的名称</th>
										<th class="center">业务类型</th>
										<th class="center">借款类型(借款用途)</th>
										<th class="center">审批结果</th>
										<th class="center">借款金额（合同金额）（元）</th>
										<th class="center">借款/还款期数（月）</th>
										<th class="center">借款城市(借款地点)</th>
										<th class="center">担保类型</th>
										<th class="center">未偿还本金</th>
										<th class="center">当前还款状态</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td align="right"><c:out value="${status.index+((mspForm.paginatorPage - 1) * mspForm.paginator.limit) + 1 }"></c:out></td>
													<td align="right"><c:out value="${record.configureName }"></c:out></td>
													<td align="right"><c:out value="${record.serviceTypeName }"></c:out></td>
													<td align="right"><c:out value="${record.loanTypeName }"></c:out></td>
													<td align="right"><c:out value="${record.approvalResultName }"></c:out></td>
													<td align="right"><c:out value="${record.loanMoney }"></c:out></td>
													<td align="right"><c:out value="${record.loanTimeLimit }"></c:out></td>
													<td align="right"><c:out value="${record.creditAddress }"></c:out></td>
													<td align="right"><c:out value="${record.guaranteeTypeName }"></c:out></td>
													<td align="right"><c:out value="${record.unredeemedMoney }"></c:out></td>
													<td align="right"><c:out value="${record.repaymentStatusName }"></c:out></td>
													<%-- <td class="center">
														<c:if test="${record.serviceType=='02' }">  
														    <c:out value='一般借贷' />  
														</c:if>  
														<c:if test="${record.serviceType=='03' }">  
														    <c:out value='消费信贷' />  
														</c:if>
														<c:if test="${record.serviceType=='04' }">  
														    <c:out value='循环贷' />  
														</c:if>
														<c:if test="${record.serviceType=='05' }">  
														    <c:out value='其他' />  
														</c:if>
													</td> --%>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="msp:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="msp:DELETE">
																<a class="btn btn-transparent btn-xs fn-Delete" data-id="${record.id }" data-toggle="tooltip" tooltip-placement="top" data-original-title="删除"><i class="fa fa-times fa fa-white"></i></a>
															</shiro:hasPermission>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group" dropdown="">
																<button type="button"
																	class="btn btn-primary btn-o btn-sm"
																	data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="msp:MODIFY">
																		<li><a class="fn-Modify" data-id="${record.id }">修改</a></li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="msp:DELETE">
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page"
								action="init" paginator="${mspForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<input type="hidden" name="id" id="id" /> 
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${mspForm.paginatorPage}" />
			<div class="form-group">
					<label>业务类型</label>
					<select name="serviceTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<%-- <option value="01" <c:if test="${utmForm.sourceTypeSrch=='01'}">selected</c:if>> 信用额度(不设置该选项)</option> --%>
						<option value="02" <c:if test="${mspForm.serviceTypeSrch=='02'}">selected</c:if>>一般借贷</option>
						<option value="03" <c:if test="${mspForm.serviceTypeSrch=='03'}">selected</c:if>>消费信贷</option>
						<option value="04" <c:if test="${mspForm.serviceTypeSrch=='04'}">selected</c:if>>循环贷</option>
						<option value="05" <c:if test="${mspForm.serviceTypeSrch=='05'}">selected</c:if>>其他</option>
					</select>
			</div>
			<div class="form-group">
					<label>借款类型</label>
					<select name="loanTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="01" <c:if test="${mspForm.loanTypeSrch=='01'}">selected</c:if>>经营</option>
						<option value="02" <c:if test="${mspForm.loanTypeSrch=='02'}">selected</c:if>>消费</option>
						<option value="99" <c:if test="${mspForm.loanTypeSrch=='99'}">selected</c:if>>其他</option>
					</select>
			</div>
			<div class="form-group">
					<label>借款金额</label>
					<input type="text" name="loanMoneys" class="form-control input-sm underline" maxlength="20" value="${usersListForm.recommendNames}" />
			</div>
			<div class="form-group">
					<label>借款城市</label>
					<input type="text" name="creditAddress" class="form-control input-sm underline" maxlength="20" value="${usersListForm.recommendName}" />
			</div>	
			<div class="form-group">
					<label>担保类型</label>
					<select name="sourceTypeSrch" class="form-control underline form-select2">
						<option value=""></option>
						<option value="A" <c:if test="${mspForm.sourceTypeSrch=='A'}">selected</c:if>>抵押</option>
						<option value="B" <c:if test="${mspForm.sourceTypeSrch=='B'}">selected</c:if>>质押</option>
						<option value="C" <c:if test="${mspForm.sourceTypeSrch=='C'}">selected</c:if>>担保</option>
						<option value="D" <c:if test="${mspForm.sourceTypeSrch=='D'}">selected</c:if>>信用</option>
						<option value="E" <c:if test="${mspForm.sourceTypeSrch=='E'}">selected</c:if>>保证</option>
						<option value="Y" <c:if test="${mspForm.sourceTypeSrch=='Y'}">selected</c:if>>其他</option>
					</select>
			</div>
			<%-- <div class="form-group">
				<label>添加时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon">
						<input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${utmForm.timeStartSrch}" />
						<i class="ti-calendar"></i> </span>
					<span class="input-group-addon no-border bg-light-orange">~</span>
					<input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${utmForm.timeEndSrch}" />
				</div>
			</div> --%>
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
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/mspconfigure/mspconfigure.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
