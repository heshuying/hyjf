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
							<div class="well">
								<c:set var="jspPrevDsb" value="${rechargeForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
								<c:set var="jspNextDsb" value="${rechargeForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
								<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a> <a class="btn btn-o btn-primary btn-sm margin-right-15 hidden-xs fn-Next${jspNextDsb}" data-toggle="tooltip" tooltip-placement="top" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
								<a class="btn btn-o btn-primary btn-sm fn-Refresh" data-toggle="tooltip" tooltip-placement="top" data-original-title="刷新列表">刷新 <i class="fa fa-refresh"></i>
								</a>
								<shiro:hasPermission name="recharge:EXPORT">
									<shiro:hasPermission name="recharge:ORGANIZATION_VIEW">
										<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport" data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i>
										</a>
									</shiro:hasPermission>
									<shiro:lacksPermission name="recharge:ORGANIZATION_VIEW">
										<a class="btn btn-o btn-primary btn-sm fn-Export" data-toggle="tooltip" tooltip-placement="top" data-original-title="导出Excel">导出Excel <i class="fa fa-Export"></i>
										</a>
									</shiro:lacksPermission>
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
										<th class="center">用户名</th>
										<th class="center">电子账号</th>
										<th class="center">手机号</th>
										<th class="center">流水号</th>
										<th class="center">资金托管平台</th>
										<th class="center">订单号</th>
										<!-- <th class="center">用户属性</th> -->
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
										<th class="center">发送日期</th>
										<th class="center">发送时间</th>
										<th class="center">系统跟踪号</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty rechargeForm.recordList}">
											<tr>
												<td colspan="22">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${rechargeForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${status.index+((rechargeForm.paginatorPage - 1) * rechargeForm.paginator.limit) + 1 }"></c:out></td>
													<td><c:out value="${record.username }"></c:out></td>
													<td><c:out value="${record.accountId }"></c:out></td>
													<td><hyjf:asterisk value="${record.mobile }" permission="recharge:HIDDEN_SHOW"></hyjf:asterisk></td>
													<td><c:out value="${record.seqNo }"></c:out></td>
													<td><c:out value="${record.isBank }"></c:out></td>
													<td class="center"><c:out value="${record.nid }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.userProperty }"></c:out></td> --%>
													<td class="center"><c:if test="${'B2C' eq record.gateType }">个人网银充值</c:if> 
																		<c:if test="${'B2B' eq record.gateType }">企业网银充值</c:if> 
																		<c:if test="${'QP' eq record.gateType }">快捷充值</c:if>
																		<c:if test="${'OFFLINE' eq record.gateType }">线下充值</c:if></td>
																		
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
														<shiro:hasPermission name="recharge:CONFIRM">
															<div class="visible-md visible-lg hidden-sm hidden-xs">
																<c:if test="${record.status eq '处理中'}">
																	<a class="btn btn-transparent btn-xs tooltips fn-fix" data-userId="${record.userId }" data-nid="${record.nid }" data-status="0" data-toggle="tooltip" tooltip-placement="top" data-original-title="充值失败"><i class="fa fa-frown-o"></i></a>
																	<a class="btn btn-transparent btn-xs tooltips fn-fix" data-userId="${record.userId }" data-nid="${record.nid }" data-status="1" data-toggle="tooltip" tooltip-placement="top" data-original-title="充值成功"><i class="fa fa-smile-o"></i></a>
																</c:if>
																<c:if test="${record.status eq '失败' }">
																	<a class="btn btn-transparent btn-xs tooltips fn-Modify" data-nid="${record.nid }" data-userId="${record.userId }" data-toggle="tooltip" tooltip-placement="top" data-original-title="修改充值状态">修改充值状态</a>
																</c:if>
															</div>
															<div class="visible-xs visible-sm hidden-md hidden-lg">
																<div class="btn-group" dropdown="">
																	<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																		<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																	</button>
																	<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																		<c:if test="${record.status  eq '处理中'}">
																			<li><a class="fn-fix" data-nid="${record.nid }" data-status="0" data-userId="${record.userId }">充值失败</a></li>
																			<li><a class="fn-fix" data-nid="${record.nid }" data-status="1" data-userId="${record.userId }">充值成功</a></li>
																		</c:if>
																		<c:if test="${record.status  eq '失败'}">
																			<li>
																				<a class="fn-Modify" data-nid="${record.nid }" data-userId="${record.userId }" >修改充值状态</a>
																			</li>
																		</c:if>
																	</ul>
																</div>
															</div>
														</shiro:hasPermission>
													</td>
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
							<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="recharge_list" paginator="${rechargeForm.paginator}"></hyjf:paginator>
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
			<input type="hidden" name="status" id="status" />
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${rechargeForm.paginatorPage}" />

			<!-- 查询条件 -->
			<div class="form-group">
				<label>用户名</label> <input type="text" name="usernameSearch" class="form-control input-sm underline" value="${rechargeForm.usernameSearch}" />
			</div>
			<div class="form-group">
				<label>订单号</label> <input type="text" name="nidSearch" class="form-control input-sm underline" value="${rechargeForm.nidSearch}" />
			</div>
			<div class="form-group">
				<label>电子账号</label> <input type="text" name="accountIdSearch" class="form-control input-sm underline" value="${rechargeForm.accountIdSearch}" />
			</div>
			<div class="form-group">
				<label>充值银行</label>
				<select name="bankCodeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${rechargeForm.banksList }" var="bank" begin="0" step="1" varStatus="status">
						<option value="${bank.bankName }" <c:if test="${bank.bankName eq rechargeForm.bankCodeSearch}">selected="selected"</c:if>>
							<c:out value="${bank.bankName }"></c:out></option>
					</c:forEach>
				</select>
			</div>
<%-- 			<div class="form-group">
					<label>用户属性</label>
					<select name="userProperty" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
					<option value="${property.nameCd }"
						<c:if test="${property.nameCd eq rechargeForm.userProperty}">selected="selected"</c:if>>
						<c:out value="${property.name }"></c:out></option>
				</c:forEach>
			</select>
		    </div> --%>
			<div class="form-group">
				<label>充值类型</label> <select name="typeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="B2C" <c:if test="${'B2C' eq rechargeForm.typeSearch}">selected="selected"</c:if>>
						<c:out value="个人网银充值"></c:out>
					</option>
					<option value="B2B" <c:if test="${'B2B' eq rechargeForm.typeSearch}">selected="selected"</c:if>>
						<c:out value="企业网银充值"></c:out>
					</option>
					<option value="QP" <c:if test="${'QP' eq rechargeForm.typeSearch}">selected="selected"</c:if>>
						<c:out value="快捷充值"></c:out>
					</option>
					<option value="OFFLINE" <c:if test="${'OFFLINE' eq rechargeForm.typeSearch}">selected="selected"</c:if>>
						<c:out value="线下充值"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>充值状态</label>
				<select name="statusSearch" class="form-control underline form-select2">
					<option value=""></option>
					<c:forEach items="${rechargeStatus }" var="recharge" begin="0" step="1" >
						<option value="${recharge.nameCd }"
							<c:if test="${recharge.nameCd eq rechargeForm.statusSearch}">selected="selected"</c:if>>
							<c:out value="${recharge.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>充值平台</label> <select name="clientTypeSearch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq rechargeForm.clientTypeSearch}">selected="selected"</c:if>>
						<c:out value="PC"></c:out>
					</option>
					<option value="1" <c:if test="${'1' eq rechargeForm.clientTypeSearch}">selected="selected"</c:if>>
						<c:out value="微信"></c:out>
					</option>
					<option value="2" <c:if test="${'2' eq rechargeForm.clientTypeSearch}">selected="selected"</c:if>>
						<c:out value="Android"></c:out>
					</option>
					<option value="3" <c:if test="${'3' eq rechargeForm.clientTypeSearch}">selected="selected"</c:if>>
						<c:out value="iOS"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>资金托管平台</label> <select name="isBankSearch" class="form-control underline form-select2">
					<option value="">全部</option>
					<c:forEach items="${bankTypeList }" var="bankType" begin="0" step="1" >
						<option value="${bankType.nameCd }"
							<c:if test="${bankType.nameCd eq rechargeForm.isBankSearch}">selected="selected"</c:if>>
							<c:out value="${bankType.name }"></c:out>
						</option>
					</c:forEach>
				</select>
			</div>
			<div class="form-group">
				<label>充值手续费收取方式</label> <select name="getfeefromSearch" class="form-control underline form-select2">
					<option value=""></option>
					<option value="0" <c:if test="${'0' eq rechargeForm.getfeefromSearch}">selected="selected"</c:if>>
						<c:out value="向用户收取"></c:out>
					</option>
					<option value="1" <c:if test="${'1' eq rechargeForm.getfeefromSearch}">selected="selected"</c:if>>
						<c:out value="向商户收取"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>用户角色</label> <select name="roleIdSearch" class="form-control underline form-select2">
				<option value=""></option>
					<option value=" " <c:if test="${' ' eq rechargeForm.roleIdSearch}">selected="selected"</c:if>>
						<c:out value="全部"></c:out>
					</option>
					<option value="1" <c:if test="${'1' eq rechargeForm.roleIdSearch}">selected="selected"</c:if>>
						<c:out value="出借人"></c:out>
					</option>
					<option value="2" <c:if test="${'2' eq rechargeForm.roleIdSearch}">selected="selected"</c:if>>
						<c:out value="借款人"></c:out>
					</option>
					<option value="3" <c:if test="${'3' eq rechargeForm.roleIdSearch}">selected="selected"</c:if>>
						<c:out value="担保机构"></c:out>
					</option>
				</select>
			</div>
			<div class="form-group">
				<label>充值时间</label>
				<div class="input-group input-daterange datepicker">
					<span class="input-icon"> <input type="text" name="startDate" id="start-date-time" class="form-control underline" value="${rechargeForm.startDate}" /> <i class="ti-calendar"></i>
					</span> <span class="input-group-addon no-border bg-light-orange">~</span> <input type="text" name="endDate" id="end-date-time" class="form-control underline" value="${rechargeForm.endDate}" />
				</div>
			</div>
			<div class="form-group">
				<label>流水号</label> <input type="text" name="seqNoSearch" class="form-control input-sm underline" value="${rechargeForm.seqNoSearch}" />
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
			<script type='text/javascript' src="${webRoot}/jsp/finance/recharge/recharge_list.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
