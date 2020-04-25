<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="bankdebtend:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp"
		flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="债权结束" />

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">(新)债权结束</h1>
			<span class="mainDescription">债转结束</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="tabbable">
					<ul class="nav nav-tabs" id="myTab">
					
						<!-- 新结束债券 相同的权限 后期可以加：class="active"-->
						<shiro:hasPermission name="bankdebtend:VIEW">
							<li class="active" ><a href="${webRoot}/exception/bankdebtend/init">(新)结束债权</a></li>
						</shiro:hasPermission>

						<shiro:hasPermission name="bankdebtend:VIEW">
							<li ><a href="${webRoot}/exception/bankdebtend/oldinit">结束债权</a></li>
						</shiro:hasPermission>
						
						<shiro:hasPermission name="hjhcreditend:VIEW">
							<li><a href="${webRoot}/exception/hjhcreditendexception/init">汇计划债转结束债权</a></li>
						</shiro:hasPermission>
					</ul>
					<div class="tab-content">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<%-- 功能栏 --%>
							<div class="well">
								<c:set var="jspPrevDsb"
									value="${bankDebtEndForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb"
									value="${bankDebtEndForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
									data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Refresh" data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i></a>
								<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
									data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件" data-toggle-class="active" data-toggle-target="#searchable-panel">
									<i class="fa fa-search margin-right-10"></i> 
									<i class="fa fa-chevron-left"></i></a>
							</div>
							<br />
							<%-- 模板列表一览 --%>
							<table id="equiList"
								class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<!-- 批次号当日必须唯一 -->
										<th class="center">批次号</th>
										<th class="center">批次交易日期</th>
										<th class="center">批次交易时间</th>
										<th class="center">批次交易流水号</th>
										<th class="center">批次笔数</th>
										<th class="center">批次状态</th>
										<th class="center">债权融资人用户ID</th>
										<th class="center">债权出借人用户ID</th>
										<th class="center">债权订单号</th>
										<th class="center">债权标的号</th>
										<th class="center">债权银行授权码</th>
										<th class="center">债权结束债权类型</th>
										<th class="center">债权交易状态</th>
										<th class="center">债权失败描述</th>
										<th class="center">同步</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								

								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty recordList}">
											<tr>
												<td colspan="17">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${recordList }" var="record" begin="0"
												step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((bankDebtEndForm.paginatorPage - 1) * bankDebtEndForm.paginator.limit) + 1 }"></c:out></td>
													<td class="center"><c:out value="${record.batchNo }"></c:out></td>
													<td class="center"><c:out value="${record.txDate }"></c:out></td>
													<td class="center"><c:out value="${record.txTime }"></c:out></td>
													<td class="center"><c:out value="${record.seqNo }"></c:out></td>
													<td class="center"><c:out value="${record.txCounts }"></c:out></td>
													
													<td class="center">
														<c:if test="${record.state == 'S' }">成功</c:if> 
														<c:if test="${record.state == 'F' }">失败</c:if>
														<c:if test="${record.state == 'A' }">待处理</c:if>
														<c:if test="${record.state == 'D' }">正在处理</c:if>
														<c:if test="${record.state == 'C' }">撤销</c:if>
													</td>
													<td class="center"><c:out value="${record.userId }"></c:out></td>
													<td class="center"><c:out value="${record.tenderUserId}"></c:out></td>
													<td class="center"><c:out value="${record.orderId }"></c:out></td>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.authCode }"></c:out></td>
													
													<td class="center">
														<c:if test="${record.creditEndType == '1' }">还款</c:if> 
														<c:if test="${record.creditEndType == '2' }">散标债转</c:if>
														<c:if test="${record.creditEndType == '3' }">计划债转</c:if>
													</td>
													<td class="center">
														<c:if test="${record.status == '0' }">初始</c:if>
														<c:if test="${record.status == '1' }">待请求</c:if>
														<c:if test="${record.status == '2' }">请求成功</c:if>
														<c:if test="${record.status == '3' }">请求失败</c:if>
														<c:if test="${record.status == '4' }">校验成功</c:if>
														<c:if test="${record.status == '5' }">业务全部成功</c:if>
														<c:if test="${record.status == '10' }">校验失败</c:if>
														<c:if test="${record.status == '11' }">业务部分成功</c:if>
														<c:if test="${record.status == '12' }">业务失败</c:if>
													</td>
													<td class="center"><c:out value="${record.failmsg }"></c:out></td>
													<!-- 同步 -->

	 													<td class="center">
															<c:if test="${record.status == '10' || record.status == '11' || record.status == '12'}">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="bankdebtend:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-Modify"
																	
																		data-txcounts="${record.txCounts }" data-txdate="${record.txDate }" data-batchno="${record.batchNo }" data-status="${record.status }"
																		ss
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="批次查询处理">
																		<i class="fa fa-pencil"></i>
																	</a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<ul class="dropdown-menu pull-right dropdown-light"
																		role="menu">
																		<shiro:hasPermission name="bankdebtend:MODIFY">
																			<li><a class="fn-Modify" 
																			data-txcounts="${record.txCounts }" data-txdate="${record.txDate }" data-batchno="${record.batchNo }" data-status="${record.status }">批次查询处理</a></li>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</div>
															</c:if>
														</td> 

													
													
													<!-- 更新 -->

	 													<td class="center">
															<c:if test="${record.status == '10' || record.status == '11' || record.status == '12'}">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<shiro:hasPermission name="bankdebtend:MODIFY">
																	<a class="btn btn-transparent btn-xs fn-Change"
																	
																		data-txcounts="${record.txCounts }" data-txdate="${record.txDate }" data-batchno="${record.batchNo }" data-status="${record.status }"
																		
																		data-orderid="${record.orderId }"
																		
																		data-toggle="tooltip" tooltip-placement="top" data-original-title="单次查询处理">
																		<i class="fa fa-pencil"></i>
																	</a>
																</shiro:hasPermission>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<ul class="dropdown-menu pull-right dropdown-light"
																		role="menu">
																		<shiro:hasPermission name="bankdebtend:MODIFY">
																			<li><a class="fn-Change" 
																			data-txcounts="${record.txCounts }" data-txdate="${record.txDate }" data-batchno="${record.batchNo }" data-status="${record.status }" data-orderid="${record.orderId }">单次查询处理</a></li>
																		</shiro:hasPermission>
																	</ul>
																</div>
															</div>
															</c:if>
														</td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="searchNewAction" paginator="${bankDebtEndForm.paginator}"></hyjf:paginator>
							<br /> <br />
						</div>
					</div>
				</div>
			</div>
			</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
<!--  			<input type="hidden" name=borrowNid id="borrowNid" />
			<input type="hidden" name="userId" id="userId" />
			<input type="hidden" name="tenderNid" id="tenderNid" /> -->
			<input type="hidden" name=txCounts id="txCounts" />
			<input type="hidden" name=txDate id="txDate" />
			<input type="hidden" name=batchNo id="batchNo" />
			<input type="hidden" name=status id="status" />
			<input type="hidden" name=orderId id="orderId" />
			
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${bankDebtEndForm.paginatorPage}" />
			<div class="form-group">
				<label>融资用户ID:</label>
				<input type="text" name="userIdSrch" placeholder="请输入数字"
					id="userIdSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.userIdSrch}" />
			</div>
			<div class="form-group">
				<label>出借用户ID:
				</label> <input type="text" name="tenderUserIdSrch" placeholder="请输入数字 "
					id="tenderUserIdSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.tenderUserIdSrch}" />
			</div>
			
			<div class="form-group">
				<label>批次号:
				</label> <input type="text" name="batchNoSrch"
					id="batchNoSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.batchNoSrch}" />
			</div>
			
			<div class="form-group">
				<label>订单号:
				</label> <input type="text" name="orderIdSrch"
					id="orderIdSrch" class="form-control input-sm underline"
					value="${bankDebtEndForm.orderIdSrch}" />
			</div>
			
			<div class="form-group">
				<label>债权状态:</label> <select name="statusSrch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="初始"></c:out>
					</option>
					<option value="1" <c:if test="${'1' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="待请求"></c:out>
					</option>
					<option value="2" <c:if test="${'2' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="请求成功"></c:out>
					</option>
					<option value="3" <c:if test="${'3' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="请求失败"></c:out>
					</option>
					<option value="4" <c:if test="${'4' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="校验成功"></c:out>
					</option>
					<option value="5" <c:if test="${'5' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="业务全部成功"></c:out>
					</option>
					<option value="10" <c:if test="${'10' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="校验失败"></c:out>
					</option>
					<option value="11" <c:if test="${'11' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="业务部分成功"></c:out>
					</option>
					<option value="12" <c:if test="${'12' eq bankDebtEndForm.statusSrch}">selected="selected"</c:if>>
						<c:out value="业务失败"></c:out>
					</option>
				</select>
			</div>
		</tiles:putAttribute>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel"
				name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css"
				rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript"
				src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript'
				src="${webRoot}/jsp/exception/bankdebtend/newbankdebtendlist.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
