<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="userslist:VIEW">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="会员列表" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">会员列表</h1>
			<span class="mainDescription">本功能可以修改查询相应的会员信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
							<shiro:hasPermission name="userslist:SEARCH">
								<%-- 功能栏 --%>
								<div class="well">
									<c:set var="jspPrevDsb" value="${usersListForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
									<c:set var="jspNextDsb" value="${usersListForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
									<a class="btn btn-o btn-primary btn-sm hidden-xs fn-Prev${jspPrevDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="上一页"><i class="fa fa-chevron-left"></i></a>
									<a class="btn btn-o btn-primary btn-sm hidden-xs margin-right-15 fn-Next${jspNextDsb}"
											data-toggle="tooltip" data-placement="bottom" data-original-title="下一页"><i class="fa fa-chevron-right"></i></a>
									<shiro:hasPermission name="userslist:EXPORT">
										<shiro:hasPermission name="userslist:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm fn-EnhanceExport"
											   data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:hasPermission>
										<shiro:lacksPermission name="userslist:ORGANIZATION_VIEW">
											<a class="btn btn-o btn-primary btn-sm fn-Export"
													data-toggle="tooltip" data-placement="bottom" data-original-title="导出列表">导出列表 <i class="fa fa-plus"></i></a>
										</shiro:lacksPermission>
									</shiro:hasPermission>
									<shiro:hasPermission name="userslist:UPDATEINVITE">
										<a class="btn btn-o btn-primary btn-sm fn-UpdateInvite"
												data-toggle="tooltip" data-placement="bottom" data-original-title="更新出借推荐人">更新出借推荐人 <i class="fa fa-plus"></i></a>
									</shiro:hasPermission>
									<a class="btn btn-o btn-primary btn-sm fn-Refresh"
											data-toggle="tooltip" data-placement="bottom" data-original-title="刷新列表"> 刷新 <i class="fa fa-refresh"></i></a>
									<a class="btn btn-o btn-info btn-sm pull-right fn-searchPanel"
											data-toggle="tooltip" data-placement="bottom" data-original-title="检索条件"
											data-toggle-class="active" data-toggle-target="#searchable-panel"><i class="fa fa-search margin-right-10"></i> <i class="fa fa-chevron-left"></i></a>
								</div>
							</shiro:hasPermission>
							<br/>
							<%-- 角色列表一览 --%>
							<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width:55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">序号</th>
										<shiro:hasPermission name="userslist:ORGANIZATION_VIEW">
											<th class="center">分公司</th>
											<th class="center">分部</th>
											<th class="center">团队</th>
										</shiro:hasPermission>
										<th class="center">用户来源</th>
										
										<th class="center">用户名</th>
										<th class="center">姓名</th>
										<th class="center">手机</th>
										<th class="center">客户编号</th>
										<th class="center">电子账号</th>
										<!-- <th class="center">会员类型</th> -->
										<th class="center">用户角色</th>
										<!-- <th class="center">借款人类型</th> -->
										<th class="center">用户类型</th>
										<!-- <th class="center">用户属性</th> -->
										<th class="center">推荐人</th>
										<!-- <th class="center">51老用户</th> -->
										<th class="center">用户状态</th>
										<th class="center">银行开户状态</th>
										<!-- <th class="center">注册平台</th> -->
										<th class="center">注册时间</th>
										<th class="center">操作</th>
									</tr>
								</thead>
								<tbody id="userTbody">
									<c:choose>
										<c:when test="${empty usersListForm.recordList}">
											<tr><td colspan="18">暂时没有数据记录</td></tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${usersListForm.recordList }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center">${(usersListForm.paginatorPage-1)*usersListForm.paginator.limit+status.index+1 }</td>
													<shiro:hasPermission name="userslist:ORGANIZATION_VIEW">
														<td class="center"><c:out value="${record.regionName }"></c:out></td>
														<td class="center"><c:out value="${record.branchName }"></c:out></td>
														<td class="center"><c:out value="${fn:replace(record.departmentName, '&amp;', '&')  }"></c:out></td>
													</shiro:hasPermission>
													<td class="center"><c:out value="${record.instName }"></c:out></td>
													<%-- <td class="center"><c:out value="${empty record.instName ? '汇盈金服': record.instName }"></c:out></td> --%>
													<td class="left"><c:out value="${record.userName }"></c:out></td>
													<td class="left"><c:out value="${record.realName }"></c:out></td>
													<td class="center"><hyjf:asterisk value="${record.mobile }" permission="userslist:HIDDEN_SHOW"></hyjf:asterisk></td>
													<td><c:out value="${record.customerId }"></c:out></td>
													<td><c:out value="${record.account }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.vipType }"></c:out></td> --%>
													<td class="center"><c:out value="${record.userRole }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.borrowerType == 1 ? '内部机构':(record.borrowerType == 2 ? '外部机构':'') }"></c:out></td> --%>
													<td class="center"><c:out value="${record.userType }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.userProperty }"></c:out></td> --%>
													<td><c:out value="${record.recommendName }"></c:out></td>
													<%-- <td class="center"><c:out value="${record.is51 }"></c:out></td> --%>
													<td class="center"><c:out value="${record.userStatus }"></c:out></td>
													<td class="center"><c:out value="${record.bankOpenAccount == '1' ? '已开户':'未开户'}"></c:out></td>
													<%-- <td class="left"><c:out value="${record.registPlat }"></c:out></td> --%>
													<td class="center"><c:out value="${record.regTime }"></c:out></td>
													<td class="center">
														<div class="visible-md visible-lg hidden-sm hidden-xs">
															<shiro:hasPermission name="userslist:MODIFY">
																<a class="btn btn-transparent btn-xs fn-Modify" data-userid="${record.userId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="修改"><i class="fa fa-pencil"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="userslist:MODIFYIDCARD">
																<c:if test="${record.userProperty!='线上员工'&&record.userProperty!='线下员工' }">
																<a class="btn btn-transparent btn-xs fn-ModifyIC" data-userid="${record.userId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="修改身份证"><i class="fa fa-pencil"></i></a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="userslist:MODIFYRE">
																<c:if test="${record.userProperty!='线上员工'&&record.userProperty!='线下员工' }">
																<a class="btn btn-transparent btn-xs tooltips fn-ModifyRe" data-userid="${record.userId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="修改推荐人"><i class="fa fa-group"></i></a>
																</c:if>
															</shiro:hasPermission>
															<shiro:hasPermission name="userslist:INFO">
																<a class="btn btn-transparent btn-xs tooltips" href="${webRoot}/manager/users/userdetail?userId=${record.userId }"
																		data-toggle="tooltip" data-placement="top" data-original-title="详情"><i class="fa fa-file-text"></i></a>
															</shiro:hasPermission>
															<c:if test="${record.userType =='企业用户' || record.bankOpenAccount != '1'}">
																<shiro:hasPermission name="userslist:COMPANY">
																	<a class="btn btn-transparent btn-xs tooltips companyInfo" data-userid="${record.userId }"
																			data-toggle="tooltip" data-placement="top" data-original-title="企业信息补录"><i class="fa fa-pencil"></i></a>
																</shiro:hasPermission>
															</c:if>
                                                            <c:if test="${record.userType == '个人用户' && record.bankOpenAccount=='1'}">
                                                                <shiro:hasPermission name="userslist:MODIFYRE">
                                                                    <a class="btn btn-transparent btn-xs tooltips fn-SyncRole" data-userid="${record.userId }"
                                                                       data-toggle="tooltip" data-placement="top" data-original-title="同步角色"><i class="fa fa-refresh"></i></a>
                                                                </shiro:hasPermission>
                                                            </c:if>
															<shiro:hasPermission name="userslist:MODIFYPHONE">
																<a class="btn btn-transparent btn-xs fn-ModifyMobile" data-userid="${record.userId }"
																   data-toggle="tooltip" data-placement="top" data-original-title="修改手机号"><i class="fa fa-phone"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="userslist:MODIFYEMAIL">
																<a class="btn btn-transparent btn-xs fn-ModifyEmail" data-userid="${record.userId }"
																   data-toggle="tooltip" data-placement="top" data-original-title="修改邮箱"><i class="fa fa-book"></i></a>
															</shiro:hasPermission>
															<shiro:hasPermission name="userslist:MODIFYUSERROLE">
																<a class="btn btn-transparent btn-xs fn-ModifyUserRole" data-userid="${record.userId }"
																   data-toggle="tooltip" data-placement="top" data-original-title="修改用户角色"><i class="fa fa-cogs"></i></a>
															</shiro:hasPermission>
															<c:if test="${record.bankOpenAccount == '1'}">
																<shiro:hasPermission name="userslist:MODIFYBANKCARD">
																	<a class="btn btn-transparent btn-xs fn-ModifyCard" data-userid="${record.userId}"
																	   data-toggle="tooltip" data-placement="top" data-original-title="修改银行卡"><i class="fa fa-laptop"></i></a>
																</shiro:hasPermission>
															</c:if>
														</div>
														<div class="visible-xs visible-sm hidden-md hidden-lg">
															<div class="btn-group">
																<button type="button" class="btn btn-primary btn-o btn-sm" data-toggle="dropdown">
																	<i class="fa fa-cog"></i>&nbsp;<span class="caret"></span>
																</button>
																<ul class="dropdown-menu pull-right dropdown-light" role="menu">
																	<shiro:hasPermission name="userslist:MODIFY">
																	<li>
																		<a class="fn-Modify" data-userid="${record.userId }">修改</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="userslist:MODIFYIDCARD">
																	<li>
																		<a class="fn-ModifyIC" data-userid="${record.userId }">修改身份证</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="userslist:MODIFYRE">
																	<li>
																		<a class="fn-ModifyRe" data-userid="${record.userId }">修改推荐人</a>
																	</li>
																	</shiro:hasPermission>
																	<shiro:hasPermission name="userslist:INFO">
																	<li>
																		<a href="${webRoot}/manager/users/userdetail?userId=${record.userId }">详情</a>
																	</li>
																	</shiro:hasPermission>

																	<c:if test="${record.userType =='企业用户' || record.bankOpenAccount != '1'}">
																		<shiro:hasPermission name="userslist:COMPANY">
																			<li>
																				<a class="companyInfo" data-userid="${record.userId }">企业信息补录</a>
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
								</tbody>
							</table>
							<%-- 分页栏 --%>
							<shiro:hasPermission name="userslist:SEARCH">
								<hyjf:paginator id="equiPaginator" hidden="paginator-page" action="userslist" paginator="${usersListForm.paginator}"></hyjf:paginator>
							</shiro:hasPermission>
							<br/><br/>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- 检索面板 (ignore) --%>
		<tiles:putAttribute name="searchPanels" type="string">
			<shiro:hasPermission name="userslist:SEARCH">
				<input type="hidden" name="ids" id="ids" />
				<input type="hidden" name="userId" id="userId" value= "${usersListForm.userId}"/>
				<input type="hidden" name="paginatorPage" id="paginator-page" value="${usersListForm.paginatorPage}" />
				<input type="hidden" id="updType"  name="updType">
				<!-- 检索条件 -->
				<div class="form-group">
					<label>用户名</label>
					<input type="text" name="userName" class="form-control input-sm underline"  maxlength="20" value="${ usersListForm.userName}" />
				</div>
				<div class="form-group">
					<label>姓名</label>
					<input type="text" name="realName" class="form-control input-sm underline" maxlength="20" value="${usersListForm.realName}"/>
				</div>
				<div class="form-group">
					<label>手机号码</label>
					<input type="text" name="mobile" class="form-control input-sm underline" maxlength="20" value="${usersListForm.mobile}"/>
				</div>
				<div class="form-group">
					<label>推荐人</label>
					<input type="text" name="recommendName" class="form-control input-sm underline" maxlength="20" value="${usersListForm.recommendName}" />
				</div>
				<div class="form-group">
					<label>部门</label>
					<div class="dropdown-menu no-radius">
						<input type="text" class="form-control input-sm underline margin-bottom-10 " value="" id="combotree_search" placeholder="Search" >
						<input type="hidden" id="combotree_field_hidden"  name="combotreeSrch" value="${usersListForm.combotreeSrch}">
						<div id="combotree-panel" style="width:270px;height:300px;position:relative;overflow:hidden;">
							<div id="combotree" class="tree-demo" ></div>
						</div>
					</div>

					<span class="input-icon input-icon-right" data-toggle="dropdown" >
						<input id="combotree-field" type="text" class="form-control underline form " readonly="readonly">
						<i class="fa fa-remove fn-ClearDep" style="cursor:pointer;"></i>
					</span>
				</div>
				<div class="form-group">
					<label>用户类型</label>
					<select name="userType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userTypes }" var="userType" begin="0" step="1">
							<option value="${userType.nameCd }"
								<c:if test="${userType.nameCd eq usersListForm.userType}">selected="selected"</c:if>>
								<c:out value="${userType.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>用户角色</label>
					<select name="userRole" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userRoles }" var="role" begin="0" step="1">
							<option value="${role.nameCd }"
								<c:if test="${role.nameCd eq usersListForm.userRole}">selected="selected"</c:if>>
								<c:out value="${role.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>用户属性</label>
					<select name="userProperty" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userPropertys }" var="property" begin="0" step="1" >
							<option value="${property.nameCd }"
								<c:if test="${property.nameCd eq usersListForm.userProperty}">selected="selected"</c:if>>
								<c:out value="${property.name }"></c:out></option>
						</c:forEach>
					</select>
				</div>
				<%-- <div class="form-group">
						<label>用户类型</label>
						<select name="vipType" class="form-control underline form-select2">
							<option value=""></option>
							<option value="1" <c:if test="${usersListForm.vipType=='1'}">selected</c:if>>会员</option>
							<option value="2" <c:if test="${usersListForm.vipType=='2'}">selected</c:if>>VIP</option>
						</select>
				</div> --%>
				<div class="form-group">
					<label>银行开户状态</label>
					<select name="accountStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${accountStatus }" var="acstatus" begin="0" step="1">
							<option value="${acstatus.nameCd }"
								<c:if test="${acstatus.nameCd eq usersListForm.accountStatus}">selected="selected"</c:if>>
								<c:out value="${acstatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>用户状态</label>
					<select name="userStatus" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${userStatus }" var="ustatus" begin="0" step="1">
							<option value="${ustatus.nameCd }"
								<c:if test="${ustatus.nameCd eq usersListForm.userStatus}">selected="selected"</c:if>>
								<c:out value="${ustatus.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<div class="form-group">
					<label>用户来源</label>
					<select name="instCodeSrch" id="instCodeSrch" class="form-control underline form-select2">
						<option value="">全部</option>
						<c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
							<option value="${inst.instCode }"
								<c:if test="${inst.instCode eq usersListForm.instCodeSrch}">selected="selected"</c:if>>
								<c:out value="${inst.instName }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div>
				<%-- <div class="form-group">
					<label>注册平台</label>
					<select name="registPlat" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${registPlat }" var="plat" begin="0" step="1">
							<option value="${plat.nameCd }"
								<c:if test="${plat.nameCd eq usersListForm.registPlat}">selected="selected"</c:if>>
								<c:out value="${plat.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div> --%>
				<%-- <div class="form-group">
					<label>借款人类型</label>
					<select name="borrowerType" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${borrowTypes }" var="borrowType" begin="0" step="1">
							<option value="${borrowType.nameCd }"
								<c:if test="${borrowType.nameCd eq usersListForm.borrowerType}">selected="selected"</c:if>>
								<c:out value="${borrowType.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div> --%>
				<%-- <div class="form-group">
					<label>51老用户</label>
					<select name="is51" class="form-control underline form-select2">
						<option value=""></option>
						<c:forEach items="${is51 }" var="is51" begin="0" step="1">
							<option value="${is51.nameCd }"
								<c:if test="${is51.nameCd eq usersListForm.is51}">selected="selected"</c:if>>
								<c:out value="${is51.name }"></c:out>
							</option>
						</c:forEach>
					</select>
				</div> --%>
				<div class="form-group">
					<label>注册时间</label>
					<div class="input-group input-daterange datepicker">
						<span class="input-icon">
							<input type="text" name="regTimeStart" id="start-date-time" class="form-control underline" value="${usersListForm.regTimeStart}" />
							<i class="ti-calendar"></i>
						</span>
						<span class="input-group-addon no-border bg-light-orange">~</span>
						<span class="input-icon">
							<input type="text" name="regTimeEnd" id="end-date-time" class="form-control underline" value="${usersListForm.regTimeEnd}" />
						</span>
					</div>
				</div>
				<div class="form-group">
					<label>客户编号</label>
					<input type="text" name="customerId" class="form-control input-sm underline" maxlength="32" value="${usersListForm.customerId}" />
				</div>
			</shiro:hasPermission>
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
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/userList.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
