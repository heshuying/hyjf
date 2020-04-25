<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
					  flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="管理"/>
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<style>
			.panel-title {
				font-family: "微软雅黑"
			}
		</style>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
					<form id="mainForm" action="updateUserBaseInfo"
						  method="post"  role="form" class="form-horizontal" >
							<%-- 列表一览 --%>
						<input type="hidden" name="id" id="id" value="${userInfos.userId }"/>
						<input type="hidden" name="userId" id="userId" value="${userInfos.userId }"/>
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
						<input type="hidden" id="success" value="${success}"/>
						<input type="hidden" id="updFlg"  name="updFlg" value="${updType}"/>
						<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0">
								<span class="symbol"></span>用户名
							</label>
							<div class="col-xs-10">
								<span class="badge badge-inverse"> ${userInfos.userName} </span>
							</div>
						</div>
						<c:choose>
						<c:when test="${updType == 'mobile'}">
							<div class="form-group">
								<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="mobile">
									<span class="symbol required"></span>手机号码
								</label>
								<div class="col-xs-10">
									<input type="text" placeholder="手机号码" class="form-control input-sm" id="mobile" name="mobile"  value="${userInfos.mobile}"
										   datatype="m" ajaxurl="isPhone?id=${userInfos.userId}" >
								</div>
							</div>
						</c:when>
							<c:when test="${updType == 'userRole'}">
								<div class="form-group">
									<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="userRole">
										<span class="symbol"></span>用户角色
									</label>
									<div class="col-xs-10 inline-block admin-select">
										<select id="userRole" name="userRole"  class="form-control input-sm" datatype="*0-100" errormsg="请选择用户角色！" style="width:100%">
											<option value="" disabled>选择用户角色</option>
											<c:if test="${!empty userRoles}">
												<c:forEach items="${userRoles}" var="role" begin="0" step="1" varStatus="status">
													<c:choose>
														<c:when test="${role.nameCd == userInfos.userRole }">
															<option value="${role.nameCd }" data-class="fa" selected="selected">${role.name }</option>
														</c:when>
														<c:otherwise>
															<option value="${role.nameCd }" data-class="fa">${role.name }</option>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</c:if>
										</select>
										<hyjf:validmessage key="userRole" label="用户角色"></hyjf:validmessage>
									</div>
								</div>
							</c:when>
							<c:when test="${updType == 'email'}">
								<div class="form-group">
									<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="email">
										<span class="symbol required"></span>邮箱
									</label>
									<div class="col-xs-10">
										<input type="text" placeholder="邮箱" class="form-control input-sm" id="email" name="email" value="${userInfos.email}"
											   datatype="e" >
									</div>
								</div>
							</c:when>
							<c:when test="${updType == 'bankCard'}">
								<div class="form-group">
									<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="bank">
										<span class="symbol required"></span>银行
									</label>
									<div class="col-xs-10">
										<input type="text" placeholder="银行" class="form-control input-sm" id="bank" name="bank" value="${bankInfo.bank}"
											   datatype="*" >
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="bank">
										<span class="symbol required"></span>银行卡号
									</label>
									<div class="col-xs-10">
										<input type="text" placeholder="银行卡号" class="form-control input-sm" id="cardNo" name="cardNo" value="${bankInfo.cardNo}"
											   datatype="n" >
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="payAllianceCode">
										<span></span>联行号
									</label>
									<div class="col-xs-8">
										<input type="text" placeholder="联行号" class="form-control input-sm" id="payAllianceCode" name="payAllianceCode" value="${bankInfo.payAllianceCode}">
										<span id="payWarring" name ="payWarring"></span>

									</div>
									<a class="btn-sm btn-primary fn-Search"><i class="fa fa-check"></i>查询</a>
								</div>
							</c:when>
						</c:choose>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="remark">
								<span></span>说明
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" id="remark" name="remark" class="form-control"
										  maxlength="50" errormsg="说明信息最长为50字符" >${remak}</textarea>
							</div>
						</div>
						<c:if test="${updType == 'mobile'}">
							<div class="form-group">
								<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0">
									<span class="symbol required"></span>用户状态
								</label>
								<div class="col-xs-10">
									<div class="radio clip-radio radio-primary ">
										<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${userInfos.status == '0' ? 'checked' : ''}> <label for="stateOn">启用 </label>
										<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${userInfos.status == '1' ? 'checked' : ''}> <label for="stateOff">禁用 </label>
									</div>
									<hyjf:validmessage key="accountStatus" label="用户状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
					</c:if>
					<c:if test="${updType != 'bankCard'}">
						<table id="equiList" class="table table-striped table-bordered table-hover">
							<colgroup>
								<col style="width:55px;" />
								<col style="width:;" />
								<col style="width:;" />
								<col style="width:;" />
									<%-- <col style="width:;" /> --%>
								<col style="width:;" />
								<col style="width:;" />
							</colgroup>
							<thead>
							<tr>
								<th class="center">序号</th>
								<th class="center">用户名</th>
								<th class="center">手机号</th>
								<th class="center">邮箱</th>
								<th class="center">用户角色</th>
								<th class="center">借款人类型</th>
								<th class="center">说明</th>
								<th class="center">用户状态</th>
								<th class="center">修改人</th>
								<th class="center">修改时间</th>
							</tr>
							</thead>
							<tbody id="userTbody">
							<c:choose>
								<c:when test="${empty usersChangeLogForm}">
									<tr><td colspan="12">暂时没有数据记录</td></tr>
								</c:when>
								<c:otherwise>
									<c:forEach items="${usersChangeLogForm}" var="record" begin="0" step="1" varStatus="status">
										<tr>
											<td class="center">${ status.index + 1}</td>
											<td class="center"><c:out value="${record.username }"></c:out></td>
											<td class="center"><c:out value="${record.mobile }"></c:out></td>
											<td class="center"><c:out value="${record.email}"></c:out></td>
											<td class="center">
												<c:if test="${record.role == '1'}">
													<c:out value="出借人"></c:out>
												</c:if>
												<c:if test="${record.role == '2'}">
													<c:out value="借款人"></c:out>
												</c:if>
												<c:if test="${record.role == '3'}">
													<c:out value="担保机构"></c:out>
												</c:if>
                                            </td>
											<td class="center"><c:out value="${record.borrowerType == 1 ? '内部机构':(record.borrowerType == 2 ? '外部机构':'') }"></c:out></td>
											<td class="center"><c:out value="${record.remark }"></c:out></td>
											<td class="center"><c:out value="${record.status == '0' ? '启用' : '停用'}"></c:out></td>
											<td class="center"><c:out value="${record.changeUser }"></c:out></td>
											<td class="center"><c:out value="${record.changeTime }"></c:out></td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
							</tbody>
						</table>
					</c:if>


				<div class="form-group margin-bottom-0">
					<div class="col-xs-offset-2 col-xs-10">
						<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
						<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
					</div>
				</div>
		</form>
	</div>
</div>
</tiles:putAttribute>
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/updateUserInfos.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
