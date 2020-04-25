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
	<tiles:putAttribute name="pageTitle" value="修改推荐人" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="modifyre"
						method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${modifyReForm.userId }" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-230 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol"></span>用户名 
							</label>
							<div class="col-xs-10">
								<span class="badge badge-inverse"> ${modifyReForm.userName} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="recommendName"> 
								<span class="symbol"></span>推荐人
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="推荐人" class="form-control input-sm" id="recommendName" name="recommendName" value="${modifyReForm.recommendName}"
										datatype="s2-20" ignore="ignore" errormsg="推荐人只能是数字、字母和汉字，长度2~20个字符！！" ajaxurl="checkReAction?id=${modifyReForm.userId}">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="remark"> 
								<span class="symbol required"></span>说明
							</label>
							<div class="col-xs-10">
								<textarea rows="4" placeholder="说明" class="form-control input-sm"
										id="remark" name="remark"  maxlength="60" datatype="*" nullmsg="请填写说明信息" errormsg="说明信息最长为60字符" >${modifyReForm.remark}</textarea>
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
										<th class="center">推荐人</th>
										<th class="center">修改人</th>
										<th class="center">修改时间</th>
										<th class="center">说明</th>
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
													<td class="center"><c:out value="${record.recommendUser }"></c:out></td>
													<td class="center"><c:out value="${record.changeUser }"></c:out></td>
													<td class="center"><c:out value="${record.changeTime }"></c:out></td>
													<td class="center"><c:out value="${record.remark }"></c:out></td>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/modifyRe.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
