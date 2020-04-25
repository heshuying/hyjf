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

<shiro:hasPermission name="accedelist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="投标明细原始" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">投标明细原始</h1>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
				<shiro:hasPermission name="finmanchargenew:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/hjhplan/accedelist/tenderInfoAction?debtPlanNidSrch=${tenderForm.debtPlanNidSrch }&accedeOrderIdSrch=${tenderForm.accedeOrderIdSrch }">投标明细-原始</a></li>
		      	</shiro:hasPermission>
				<shiro:hasPermission name="projecttype:VIEW">
					<li><a href="${webRoot}/manager/borrow/hjhcredit/hjhcredittender/init?isAccedelist=1&assignPlanNid=${tenderForm.debtPlanNidSrch}&assignPlanOrderId=${tenderForm.accedeOrderIdSrch}">出借明细-债转</a></li>
				</shiro:hasPermission>
			</ul>
				<div class="tab-content">
				<div class="tab-pane fade in active">
					<%-- 功能栏 --%>
					<div class="well">
						<c:set var="jspPrevDsb" value="${tenderForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
						<c:set var="jspNextDsb" value="${tenderForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
					<br/>
					<%-- 列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 55px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">用户名</th>
								<!-- <th class="center">用户属性</th> -->
								<th class="center">推荐人</th>
								<th class="center">项目编号</th>
								<th class="center">智投编号</th>
								<th class="center">出借利率</th>
								<th class="center">借款期限</th>
								<th class="center">还款方式</th>
								<th class="center">授权服务金额(元)</th>
								<th class="center">资产来源</th>
								<th class="center">产品类型</th>
								<th class="center">复投投标(是/否)</th>
								<th class="center">复审通过时间</th>
								<th class="center">出借时间</th>
								<th class="center">合同状态</th>
								<th class="center">合同编号</th>
								<th class="center">操作</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty recordList}">
									<tr>
										<td colspan="18">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${recordList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center"><c:out value="${status.index+((tenderForm.paginatorPage - 1) * tenderForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.username }"></c:out></td>
											<%-- <td class="center"><c:out value="${record.tenderUserAttribute }"></c:out></td> --%>
											<td class="center"><c:out value="${record.referrerName }"></c:out></td>
											<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
											<td class="center"><c:out value="${record.planNid }"></c:out></td>
											<td class="center"><c:out value="${record.borrowApr }"></c:out></td>
											<td class="center"><c:out value="${record.borrowPeriod }"></c:out></td>
											<td class="center"><c:out value="${record.borrowStyleName }"></c:out></td>
											<td class="center"><c:out value="${record.account }"></c:out></td>
											<td class="center"><c:out value="${record.instName }"></c:out></td>
											<td class="center"><c:out value="${record.productType }"></c:out></td>
											<td class="center"><c:out value="${record.tenderType}"/></td>
											<td class="center"><c:out value="${record.reAuthPassTime }"></c:out></td>
											<td class="center"><c:out value="${record.addtime }"></c:out></td>
											<td class="center"><c:if test="${record.contractStatus eq '' || record.contractStatus eq '0'}"><c:out value="初始"/></c:if>
												<c:if test="${record.contractStatus eq '1'}"><c:out value="生成成功"/></c:if>
												<c:if test="${record.contractStatus eq '2'}"><c:out value="签署成功"/></c:if>
												<c:if test="${record.contractStatus eq '3'}"><c:out value="下载成功"/></c:if>
											</td>
											<td class="center"><c:out value="${record.contractNumber }"></c:out></td>

											<!-- 操作 -->
											<td class="center">
												<div class="visible-md visible-lg hidden-sm hidden-xs">
													<!-- 合同状态 为成功-->
													<c:if test="${record.contractStatus == '3'}">
														<shiro:hasPermission name="accedelist:PDFDOWNLOAD">
															<a class="btn btn-transparent btn-xs tooltips fn-PdfDownload" data-toggle="tooltip" target="_blank" href="${record.downloadUrl}"  data-placement="top" data-original-title="下载"><i class="fa fa-file-text"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="accedelist:PDFVIEW">
															<a class="btn btn-transparent btn-xs tooltips fn-Pdfview" data-toggle="tooltip" target="_blank" href="${record.viewpdfUrl}"  data-placement="top" data-original-title="查看"><i class="fa fa-file-text"></i></a>
														</shiro:hasPermission>
														<shiro:hasPermission name="accedelist:PDFPREVIEW">
															<a class="btn btn-transparent btn-xs tooltips" data-toggle="tooltip" target="_blank" href="${webRoot}/manager/borrow/borrowinvest/pdfPreviewAction.do?nid=${record.nid }"  data-placement="top" data-original-title="预览"><i class="fa fa-file-text"></i></a>
														</shiro:hasPermission>
													</c:if>
													<c:if test="${record.contractStatus != '3'}">
													</c:if>
												</div>
												<div class="visible-xs visible-sm hidden-md hidden-lg">
													<div class="btn-group">
														<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
															<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
														</button>
														<ul class="dropdown-menu pull-right dropdown-light" role="menu">

															<c:if test="${record.contractStatus == '1'}" >
																<shiro:hasPermission name="accedelist:PDFDOWNLOAD">
																	<li>
																		<a target="_blank" href="${record.downloadUrl}" >下载</a>
																	</li>
																</shiro:hasPermission>
																<shiro:hasPermission name="accedelist:PDFVIEW">
																	<li>
																		<a target="_blank" href="${record.viewpdfUrl}" >查看</a>
																	</li>
																</shiro:hasPermission>
																<shiro:hasPermission name="accedelist:PDFPREVIEW">
																	<li>
																		<a target="_blank" href="#" >预览</a>
																	</li>
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
							<%-- add by LSY START --%>
							<tr>
								<th class="center">总计</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<td align="center"><fmt:formatNumber value="${sumBorrowInvest }" pattern="#,##0.00#"/></td>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
								<th>&nbsp;</th>
							</tr>
							<%-- add by LSY END --%>
						</tbody>
					</table>
					<%-- 分页栏 --%>
					<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="tenderInfoAction" paginator="${tenderForm.paginator}"></hyjf:paginator>
					<br /> <br />
				</div>
			</div>
			</div>
		</div>
		</tiles:putAttribute>

					<%-- 原始标的检索面板 (ignore) --%>
				<tiles:putAttribute name="searchPanels" type="string">
					<shiro:hasPermission name="borrowinvest:SEARCH">
						<input type="hidden" name=borrowNid id="borrowNid" />
						<input type="hidden" name="userId" id="userId" />
						<input type="hidden" name="nid" id="nid" />
						<input type="hidden" name="paginatorPage" id="paginator-page" value="${tenderForm.paginatorPage}" />
						<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" value="${tenderForm.debtPlanNidSrch}" />
						<input type="hidden" name="accedeOrderIdSrch" value="${tenderForm.accedeOrderIdSrch}"/>
						<!-- 检索条件 -->
						<div class="form-group">
							<label>项目编号</label> <input type="text" name="borrowNidSrch" id="borrowNidSrch" class="form-control input-sm underline" value="${tenderForm.borrowNidSrch}" />
						</div>
						<div class="form-group">
							<label>推荐人</label> <input type="text" name="refereeNameSrch" id="refereeNameSrch" class="form-control input-sm underline" value="${tenderForm.refereeNameSrch}" />
						</div>
						<div class="form-group">
							<label>资产来源</label>
							<select name="instCodeSrch" class="form-control underline form-select2">
								<option value=""></option>
								<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1" varStatus="status">
									<option value="${inst.instCode }"
											<c:if test="${inst.instCode eq tenderForm.instCodeSrch}">selected="selected"</c:if>>
										<c:out value="${inst.instName}"></c:out>
									</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group">
							<label>产品类型</label>
							<select name="projectTypeSrch" class="form-control underline form-select2">
								<option value=""></option>
								<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
									<option value="${borrowProjectType.borrowCd }"
											<c:if test="${borrowProjectType.borrowCd eq tenderForm.projectTypeSrch}">selected="selected"</c:if>>
										<c:out value="${borrowProjectType.borrowName }"></c:out></option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group">
							<label>借款期限</label>
							<input type="text" name="borrowPeriod"  class="form-control input-sm underline" value="${tenderForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')"  onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2"  />
						</div>

						<div class="form-group">
							<label>还款方式</label>
							<select name="borrowStyleSrch" class="form-control underline form-select2">
								<option value=""></option>
								<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
									<option value="${borrowStyle.nid }"
											<c:if test="${borrowStyle.nid eq tenderForm.borrowStyleSrch}">selected="selected"</c:if>>
										<c:out value="${borrowStyle.name}"></c:out>
									</option>
								</c:forEach>
							</select>
						</div>

						<div class="form-group">
							<label>复投投标</label>
							<select name="tenderType"  class="form-control underline form-select2">
								<option value="">全部</option>
								<option value="1" <c:if test="${tenderForm.tenderType eq '1'}">selected="selected"</c:if>>是</option>
								<option value="0" <c:if test="${tenderForm.tenderType eq '0'}">selected="selected"</c:if>>否</option>
							</select>
						</div>

						<div class="form-group">
							<label>复审通过时间</label>
							<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="reAuthPassStartSrch" id="reAuthPassStartSrch" class="form-control underline" value="${tenderForm.reAuthPassStartSrch}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="reAuthPassEndSrch" id="reAuthPassEndSrch" class="form-control underline" value="${tenderForm.reAuthPassEndSrch}" />
							</div>
						</div>

						<div class="form-group">
							<label>出借时间</label>
							<div class="input-group input-daterange datepicker">
						<span class="input-icon"> <input type="text" name="timeStartSrch" id="start-date-time" class="form-control underline" value="${tenderForm.timeStartSrch}" /> <i class="ti-calendar"></i>
						</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="timeEndSrch" id="end-date-time" class="form-control underline" value="${tenderForm.timeEndSrch}" />
							</div>
						</div>

						<%--<div class="form-group">
							<label>用户名</label> <input type="text" name="usernameSrch" id="usernameSrch" class="form-control input-sm underline" value="${borrowInvestForm.usernameSrch}" />
						</div>
						<div class="form-group">
							<label>计划编号</label>
							<input type="text" name="planNidSrch" id="planNidSrch" class="form-control input-sm underline" value="${borrowInvestForm.planNidSrch}" />
						</div>
						<div class="form-group">
							<label>借款期限</label> <input type="text" name="borrowPeriod" id="borrowPeriod" class="form-control input-sm underline" value="${borrowInvestForm.borrowPeriod}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="2" size="2" />
						</div>--%>
						<!-- 				<div class="form-group"> -->
						<!-- 					<label>渠道</label> -->
						<!-- 					<select name="utmIdSrch" class="form-control underline form-select2"> -->
						<!-- 						<option value=""></option> -->
						<%-- 						<c:forEach items="${utmList }" var="utm" begin="0" step="1" varStatus="status"> --%>
						<%-- 							<option value="${utm.sourceId }" --%>
						<%-- 								<c:if test="${utm.sourceId eq borrowInvestForm.utmIdSrch}">selected="selected"</c:if>> --%>
						<%-- 								<c:out value="${utm.sourceName }"></c:out></option> --%>
						<%-- 						</c:forEach> --%>
						<!-- 					</select> -->
						<!-- 				</div> -->




					</shiro:hasPermission>
				</tiles:putAttribute>

			<%--</div>--%>
		<%--</div>--%>
		<%--</tiles:putAttribute>--%>

		<%--&lt;%&ndash; 检索面板 (ignore) &ndash;%&gt;--%>
		<%--<tiles:putAttribute name="searchPanels" type="string">--%>
			<%--<shiro:hasPermission name="accedelist:SEARCH">--%>
				<%--<input type="hidden" name="paginatorPage" id="paginator-page" value="${tenderForm.paginatorPage}" />--%>
				<%--<input type="hidden" name="accedeOrderIdSrch" id="accedeOrderIdSrch" value="${tenderForm.accedeOrderIdSrch}" />--%>
				<%--<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" value="${tenderForm.debtPlanNidSrch}" />--%>
				<%--<!-- 查询条件 -->--%>
			<%--</shiro:hasPermission>--%>
		<%--</tiles:putAttribute>--%>

		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>

		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type='text/javascript' src="${webRoot}/jsp/manager/hjhaccede/tenderdetailList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
