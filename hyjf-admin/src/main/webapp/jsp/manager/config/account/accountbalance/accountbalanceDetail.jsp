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

<shiro:hasPermission name="accountbalance:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="余额监控编辑" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>
		
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">余额监控</h1>
			<span class="mainDescription">注意：修改数据可能会影响系统的正常运行，请谨慎！</span>
		</tiles:putAttribute>
		
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
			<ul class="nav nav-tabs" id="myTab">
		      	<shiro:hasPermission name="accountconfig:VIEW">
		      		<li><a href="${webRoot}/manager/config/accountconfig/init">账户配置</a></li>
		      	</shiro:hasPermission>
		      	<shiro:hasPermission name="accountbalance:VIEW">
		      		<li class="active"><a href="${webRoot}/manager/config/accountbalance/init">余额监控</a></li>
		      	</shiro:hasPermission>
			</ul>
			<div class="tab-content">
				<div class="tab-pane fade in active">
					<form id="mainForm" class="form-horizontal" method="post"  role="form" class="form-horizontal" >
					<!-- 标题 -->
					<input type="hidden" name="balanceDataJson" id="balanceDataJson" value="" />
					
					<div id="banlanceList" class="table-body perfect-scrollbar">
					<%-- 列表一览 --%>
					<table id="equiList" class="table table-striped table-bordered table-hover">
						<colgroup>
							<col style="width: 100px;" />
						</colgroup>
						<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">子账户名称</th>
								<th class="center">子账户类型</th>
								<th class="center">子账户代号</th>
								<th class="center">余额下限（元）</th>
								<th class="center"><span class="symbol required"></span>自动转出</th>
								<th class="center"><span class="symbol required"></span>自动转入</th>
								<th class="center">转入比例（％）</th>
							</tr>
						</thead>
						<tbody id="roleTbody">
							<c:choose>
								<c:when test="${empty accountbalanceForm.accountBalanceList}">
									<tr>
										<td colspan="8">暂时没有数据记录</td>
									</tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${accountbalanceForm.accountBalanceList }" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center" style="display: none;">
												<div class="col-sm-9">
											<input type="hidden" name="ids" id="ids" value="${record.id }" />
											</div>
											</td>
											<td class="center"><c:out value="${status.index+((accountbalanceForm.paginatorPage - 1) * accountbalanceForm.paginator.limit) + 1 }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountName }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountType }"></c:out></td>
											<td class="center"><c:out value="${record.subAccountCode }"></c:out></td>
											
											<td class="center">
												<div class="col-sm-5">
													<input type="text" name="balanceLowerLimit" class="form-control input-sm" value="<c:out value="${record.balanceLowerLimit == 0 ? '' : record.balanceLowerLimit }"></c:out>"
															maxlength="10" datatype="n1-10" style="width: 100px;" ignore="ignore" />
													<hyjf:validmessage key="balanceLowerLimits${status.index }" ></hyjf:validmessage>
												</div>
											</td>
											
											<td class="center">
												<div class="col-sm-9">
													<select id="autoTransferOuts" name="autoTransferOut"
														class="form-control underline form-select2" style="width: 100%;" 
														datatype="*" data-placeholder="请选择">
														<option value="1"
															<c:if test="${record.autoTransferOut=='1'}">selected="selected"</c:if>>是</option>
														<option value="0"
															<c:if test="${record.autoTransferOut =='0'}">selected="selected"</c:if>>否</option>
													</select>
													<hyjf:validmessage key="autoTransferOuts${status.index }" label="自动转出"></hyjf:validmessage>
												</div>
											</td>
											<td class="center">
												<div class="col-sm-9">
													<select id="autoTransferIntos" name="autoTransferInto"
														class="form-control underline form-select2" style="width: 100%;" 
														datatype="*" data-placeholder="请选择">
														<option value="1"
															<c:if test="${record.autoTransferInto=='1'}">selected="selected"</c:if>>是</option>
														<option value="0"
															<c:if test="${record.autoTransferInto =='0'}">selected="selected"</c:if>>否</option>
													</select>
													<hyjf:validmessage key="autoTransferIntos${status.index }" label="自动转入"></hyjf:validmessage>
												</div>
											</td>
											<td class="center">
												<input type="text" style="width: 100px;" name="transferIntoRatio" class="form-control input-sm" value="<c:out value="${record.transferIntoRatio == 0 ? '' : record.transferIntoRatio }"></c:out>"
														maxlength="3" datatype="n1-10" ignore="ignore"/>
												<hyjf:validmessage key="transferIntoRatio${status.index }" label="转入比例"></hyjf:validmessage>
											</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
					</div>
						<%-- 功能栏 --%>
					<div class="row">
						<div class="col-md-3">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						
					</div>
					<div class="row">
						<div class="col-md-6">
								<div class="btn-group pull-right">
									<a class="btn btn-o btn-primary btn-wide pull-right fn-Back" data-toggle="tooltip" data-placement="bottom" data-original-title="返回列表">返回列表 <i class="fa fa-mail-reply"></i></a>
								</div>
								<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit" data-toggle="tooltip" data-placement="bottom">
									<i class="fa fa-check"></i>  确定</i>
								</a>
							</div>
					</div>
					<br />
					</form>
				
				</div>
			</div>
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
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
		</tiles:putAttribute>
		
		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/config/account/accountbalance/accountbalanceDetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
