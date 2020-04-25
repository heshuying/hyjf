<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="会员信息修改" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white no-margin">
			<div class="panel-body">
				<form id="mainForm" action="updateuser"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${usersUpdateForm.userId }" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-340 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>用户名 
							</label>
							<div class="col-xs-10">
								<span class="badge badge-inverse"> ${usersUpdateForm.userName} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="mobile"> 
								<span class="symbol required"></span>手机号码
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="手机号码" class="form-control input-sm" id="mobile" name="mobile" value="${usersUpdateForm.mobile}"
										datatype="m" errormsg="必须是有效的手机号码！" ajaxurl="checkAction?id=${usersUpdateForm.userId}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="userRole"> 
								<span class="symbol"></span>用户角色
							</label>
							<div class="col-xs-10 inline-block admin-select">
								<select id="userRole" name="userRole" onchange="selectCheck()" class="form-control input-sm" datatype="*0-100" errormsg="请选择用户角色！" style="width:100%">
									<option value="" disabled>选择用户角色</option>
									<c:if test="${!empty userRoles}">
										<c:forEach items="${userRoles }" var="role" begin="0" step="1" varStatus="status">
											<c:choose>
												<c:when test="${role.nameCd == usersUpdateForm.userRole }">
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
						<div id="borrowerTypeDiv" class="form-group">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="borrowerType"> 
								<span class="symbol required"></span>借款人类型
							</label>
							<div class="col-xs-10 inline-block admin-select">
								<select id="borrowerType" name="borrowerType" class="form-control input-sm" errormsg="请选择借款人类型！" style="width:100%">
									<option value="" disabled>选择借款人类型</option>
									<c:if test="${!empty borrowTypes}">
										<c:forEach items="${borrowTypes }" var="borrowType" begin="0" step="1" varStatus="status">
											<c:choose>
												<c:when test="${borrowType.nameCd == usersUpdateForm.borrowerType }">
													<option id="op${borrowType.nameCd }" value="${borrowType.nameCd }" data-class="fa" selected="selected">${borrowType.name }</option>
												</c:when>
												<c:otherwise>
													<option id="op${borrowType.nameCd }" value="${borrowType.nameCd }" data-class="fa">${borrowType.name }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:if>
								</select>
								<hyjf:validmessage key="borrowerType" label="借款人类型"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group margin-bottom-5">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0" for="remark"> 
								<span class="symbol required"></span>说明
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" id="remark" name="remark" class="form-control"
										maxlength="60" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为60字符" >${usersUpdateForm.remark}</textarea>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-top-5 padding-right-0"> 
								<span class="symbol required"></span>用户状态
							</label>
							<div class="col-xs-10">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="0"  datatype="*" ${usersUpdateForm.status == '0' ? 'checked' : ''}> <label for="stateOn">启用 </label> 
									<input type="radio" id="stateOff" name="status" value="1" datatype="*" ${usersUpdateForm.status == '1' ? 'checked' : ''}> <label for="stateOff">禁用 </label>
								</div>
								<hyjf:validmessage key="accountStatus" label="用户状态"></hyjf:validmessage>
							</div>
						</div>
					</div>
					
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/updateUser.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
