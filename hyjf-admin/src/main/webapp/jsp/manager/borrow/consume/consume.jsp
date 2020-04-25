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
<%-- 画面功能路径 (ignore) --%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="添加借款" />

	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">添加借款</h1>
		<span class="mainDescription">添加借款添加借款添加借款的说明。</span>
	</tiles:putAttribute>

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul id="mainTabs" class="nav nav-tabs nav-justified">
				<li class="s-ellipsis active">
					<a href="#tab_jbxx_1" data-toggle="tab"><i class="fa fa-edit"></i> 基本信息</a>
				</li>
				<li>
					<a href="#tab_xmms_2" data-toggle="tab"><i class="fa fa-cubes"></i> 项目描述</a>
				</li>
				<li>
					<a href="#tab_xmzl_3" data-toggle="tab"><i class="fa fa-folder-open"></i> 项目资料</a>
				</li>
				<li>
					<a href="#tab_dyxx_4" data-toggle="tab"><i class="fa fa-server"></i> 抵/质押信息</a>
				</li>
				<li>
					<a href="#tab_dbxx_5" data-toggle="tab"><i class="fa fa-user-secret"></i> 担保信息</a>
				</li>
				<li>
					<a href="#tab_jkwxx_6" data-toggle="tab"><i class="fa fa-user"></i> 借款人信息</a>
				</li>
				<li>
					<a href="#tab_rzxx_7" data-toggle="tab"><i class="fa fa-certificate"></i> 认证信息</a>
				</li>
				<li>
					<a href="#tab_yyxx_8" data-toggle="tab"><i class="fa fa-share-alt-square"></i> 运营信息</a>
				</li>
			</ul>
			<form id="downloadForm" action="" method="post" target="_blank">
			</form>
			<form id="mainForm" action="insertAction" method="post"  role="form">
				<%-- 角色列表一览 --%>
				<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
				<input type="hidden" name="moveFlag" id="moveFlag" value="${consumeForm.moveFlag}" />
				<input type="hidden" name="cunsumeCount" id="cunsumeCount" value="${consumeForm.cunsumeCount}" />
				<input type="hidden" name="status" value="${consumeForm.status}" />

				<%-- 检索条件保持 --%>
				<!-- 项目编号 -->
				<input type="hidden" name="borrowNidSrch" value="${consumeForm.borrowNidSrch}" />
				<!-- 项目名称 -->
				<input type="hidden" name="borrowNameSrch" value="${consumeForm.borrowNameSrch}" />
				<!-- 借  款 人 -->
				<input type="hidden" name="usernameSrch" value="${consumeForm.usernameSrch}" />
				<!-- 项目状态 -->
				<input type="hidden" name="statusSrch" value="${consumeForm.statusSrch}" />
				<!-- 项目类型 -->
				<input type="hidden" name="projectTypeSrch" value="${consumeForm.projectTypeSrch}" />
				<!-- 还款方式 -->
				<input type="hidden" name="borrowStyleSrch" value="${consumeForm.borrowStyleSrch}" />
				<!-- 时间 -->
				<input type="hidden" name="timeStartSrch" value="${consumeForm.timeStartSrch}" />
				<input type="hidden" name="timeEndSrch" value="${consumeForm.timeEndSrch}" />
				<%-- 检索条件保持 --%>
				<%-- LIST --%>
				<!-- 标题 -->
				<input type="hidden" name="borrowNameJson" id="borrowNameJson" value="" />
				<!-- 图片 -->
				<input type="hidden" name="borrowImageJson" id="borrowImageJson" value="" />
				<!-- 车辆 -->
				<input type="hidden" name="borrowCarJson" id="borrowCarJson" value="" />
				<!-- 房产 -->
				<input type="hidden" name="borrowHousesJson" id="borrowHousesJson" value="" />
				<!-- 认证 -->
				<input type="hidden" name="borrowAuthenJson" id="borrowAuthenJson" value="" />
				<%-- LIST --%>
				<c:if test="${ ( ( consumeForm.status ne '10' ) and ( consumeForm.status ne '0' ) ) and ( !empty consumeForm.status )}">
					<c:set var="isEdit" value="1"></c:set>
				</c:if>


			<div class="tab-content">
				<!-- Start:基本信息 -->
				<div class="tab-pane fade in active" id="tab_jbxx_1">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次借款的基本信息，以做存档.
					</p>
					<hr>
					<div class="row">
						<!-- 表单左侧 -->
						<div class="col-xs-12 col-md-5">
							<div class="user-left">
								<div class="form-group">
									<label class="control-label">
										项目类型 <span class="symbol required"></span>
									</label>
									<c:if test="${ ( isEdit eq '1' ) }">
										<div>
											<select id="" name="" class="form-select2" style="width:100%" disabled="disabled" data-placeholder="请选择项目类型...">
												<option value=""></option>
												<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
													<option value="${borrowProjectType.borrowCd }" data-borrowclass="${borrowProjectType.borrowClass }" data-min="${ borrowProjectType.investStart}" data-max="${ borrowProjectType.investEnd}"
														<c:if test="${borrowProjectType.borrowCd eq consumeForm.projectType}">selected="selected"</c:if>>
														<c:out value="${borrowProjectType.borrowName }"></c:out></option>
												</c:forEach>
											</select>
											<input type="hidden" name="projectType" value="${consumeForm.projectType}" />
										</div>
									</c:if>
									<c:if test="${ ( isEdit ne '1' ) }">
										<div>
											<select id="projectType" name="projectType" class="form-select2" datatype="*" nullmsg="未指定项目类型！"  style="width:100%" data-placeholder="请选择项目类型...">
												<option value=""></option>
												<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
													<option value="${borrowProjectType.borrowCd }" data-borrowclass="${borrowProjectType.borrowClass }" data-min="${ borrowProjectType.investStart}" data-max="${ borrowProjectType.investEnd}"
														<c:if test="${borrowProjectType.borrowCd eq consumeForm.projectType}">selected="selected"</c:if>>
														<c:out value="${borrowProjectType.borrowName }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</c:if>
									<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										借款人用户名 <span class="symbol required"></span>
									</label>
									<input type="text" name="username" id="username" class="form-control input-sm" value='<c:out value="${consumeForm.username}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
										maxlength="30" datatype="*1-30" nullmsg="未填写借款人用户名"  errormsg="借款人用户名长度不能超过30位字符" ajaxurl="isExistsUser"/>
									<hyjf:validmessage key="username"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										资产包编号：<c:out value="${consumeForm.consumeId}"></c:out>
										<input type="hidden" name="consumeId" id="consumeId" value="<c:out value="${consumeForm.consumeId}"></c:out>" />
									</label>
								</div>
								<c:if test="${empty consumeForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目编号 <span class="symbol required"></span>
										</label>
										<div class="input-group">
											<input type="text" id="borrowPreNid" name="borrowPreNid" class="form-control input-sm" value='<c:out value="${consumeForm.borrowPreNid}"></c:out>'
													maxlength="10" datatype="n1-10" nullmsg="未填写项目编号" errormsg="项目编号长度不能超过10位，且必须为数字" ajaxurl="isExistsBorrowPreNidRecord" />
											<span class="input-group-btn">
												<a class="btn btn-primary btn-sm" id="getBorrowPreNid"> 重新获取</a>
											</span>
										</div>
										<span class="help-block"><i class="ti-info-alt text-primary"></i> 1411001意为2014年11月份第1单借款标，后借款标分期编号HDD141100101。</span>
										<hyjf:validmessage key="borrowPreNid" label="项目编号"></hyjf:validmessage>
									</div>
								</c:if>
								<c:if test="${!empty consumeForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目编号 <span class="symbol required"></span>
										</label>
										<input type="text" id="borrowNid" name="borrowNid" class="form-control input-sm" value='<c:out value="${consumeForm.borrowNid}"></c:out>' readonly="readonly" />
										<hyjf:validmessage key="borrowNid" label="项目编号"></hyjf:validmessage>
									</div>
								</c:if>
								<c:if test="${!empty consumeForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目名称 <span class="symbol required"></span>
										</label>
										<input type="text" id="jkName" name="name" class="form-control input-sm" value='<c:out value="${consumeForm.name}"></c:out>'
												maxlength="60" datatype="*1-60" nullmsg="未填写项目名称" errormsg="项目名称长度不能超过60位字符" />
										<hyjf:validmessage key="name" label="项目名称"></hyjf:validmessage>
									</div>
								</c:if>
								<c:if test="${empty consumeForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目名称 <span class="symbol required"></span>
										</label>
										<div class="input-group">
											<input type="text" id="jkName" name="name" class="form-control input-sm" value='<c:out value="${consumeForm.name}"></c:out>'
													maxlength="60" datatype="*1-60" nullmsg="未填写项目名称" errormsg="项目名称长度不能超过60位字符" />
											<span class="input-group-btn">
												<a class="btn btn-primary btn-sm" id="getBorrowNameAccountList"> 重新生成拆标标题</a>
											</span>
										</div>
									</div>
									<hyjf:validmessage key="name" label="项目名称"></hyjf:validmessage>
									<hyjf:validmessage key="borrowname" ></hyjf:validmessage>
								</c:if>
								<div class="form-group">
									<label class="control-label">
										借款金额 <span class="symbol required"></span>
									</label>
									<div class="input-group">
										<span class="input-group-addon">￥</span>
										<input type="text" id="account" name="account" class="form-control input-sm" value='<c:out value="${consumeForm.account}"></c:out>'
												maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" readonly="readonly" />
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="account" label="借款金额"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										还款方式 <span class="symbol required"></span>
									</label>
									<c:if test="${ ( isEdit eq '1' ) }">
										<select name="" class="form-select2" style="width:100%" id="sel-hkfs" disabled="disabled"
												data-placeholder="请选还款方式..." datatype="*"  nullmsg="未指定还款方式">
											<option value=""></option>
											<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
												<option value="${borrowStyle.repayMethod }" ${ borrowStyle.borrowClass}
													<c:if test="${borrowStyle.repayMethod eq consumeForm.borrowStyle}">selected="selected"</c:if>>
													<c:out value="${borrowStyle.methodName}"></c:out>
												</option>
											</c:forEach>
										</select>
										<input type="hidden" name="borrowStyle" value="${consumeForm.borrowStyle}" />
									</c:if>
									<c:if test="${ ( isEdit ne '1' ) }">
										<select name="borrowStyle" class="form-select2" style="width:100%" id="sel-hkfs"
												data-placeholder="请选还款方式..." datatype="*"  nullmsg="未指定还款方式">
											<option value=""></option>
											<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
												<option value="${borrowStyle.repayMethod }" ${ borrowStyle.borrowClass}
													<c:if test="${borrowStyle.repayMethod eq consumeForm.borrowStyle}">selected="selected"</c:if>>
													<c:out value="${borrowStyle.methodName}"></c:out>
												</option>
											</c:forEach>
										</select>
									</c:if>
									<hyjf:validmessage key="borrowStyle" label="还款方式"></hyjf:validmessage>
									<select id="sel-hkfs-backs" style="display:none">
										<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
											<option value="${borrowStyle.repayMethod }" ${ borrowStyle.borrowClass} >
												<c:out value="${borrowStyle.methodName}"></c:out>
											</option>
										</c:forEach>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">
										出借利率 <span class="symbol required"></span>
									</label>
									<div class="input-group">
										<input type="text" name="borrowApr" class="form-control input-sm" maxlength="5"
												value='<c:out value="${consumeForm.borrowApr}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
												datatype="/^\d{1,3}(\.\d{1,2})?$/,range" data-minvalue="0" data-maxvalue="30" nullmsg="未填写出借利率" errormsg="出借利率必须为数字，且最多2位小数，值在0~30之间" />
										<span class="input-group-addon">%</span>
									</div>
									<hyjf:validmessage key="borrowApr" label="出借利率"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										借款期限 <span class="symbol required"></span>
									</label>
									<div class="input-group">
										<input type="text" id="borrowPeriod" name="borrowPeriod" class="form-control input-sm" value='<c:out value="${consumeForm.borrowPeriod}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
												maxlength="3" datatype="n1-3" nullmsg="未填写借款期限" errormsg="借款期限长度不能超过3位，且必须为数字"  />
										<span class="input-group-addon">
											<c:choose>
												<c:when test="${consumeForm.borrowStyle eq 'endday'}">天</c:when>
												<c:otherwise>月</c:otherwise>
											</c:choose>
										</span>
									</div>
									<hyjf:validmessage key="borrowPeriod" label="借款期限"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<c:if test="${empty consumeForm.borrowNid }">
							<!-- 表单右侧 -->
							<div class="col-xs-12 col-md-7">
								<div class="form-group">
									<label class="control-label">
										是否预约
									</label>
									<div class="clip-radio radio-primary shifuchaibiao">
										<input type="radio" value="no" name="isChaibiao" id="chai_no" <c:if test="${ ( consumeForm.isChaibiao eq 'no' ) or ( empty consumeForm.isChaibiao) }">checked</c:if>>
										<label for="chai_no">不预约</label>
										<input type="radio" value="yes" name="isChaibiao" id="chai_yes" <c:if test="${ ( consumeForm.isChaibiao eq 'yes' ) }">checked</c:if>>
										<label for="chai_yes">预约</label>
									</div>
								</div>
								<div class="panel panel-white">
									<div class="panel-heading border-light">
										<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 预约配置</h4>
										<ul class="panel-heading-tabs border-light">
											<li>
												<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
													<i class="fa fa-indent text-primary margin-right-10"></i>
													<span id="cbRowCounts" class="value">
														<c:if test="${empty consumeForm.borrowCommonNameAccountList}">2</c:if>
														<c:if test="${!empty consumeForm.borrowCommonNameAccountList}"><c:out value="${fn:length ( consumeForm.borrowCommonNameAccountList ) }"></c:out></c:if>
													</span>
												</div>
											</li>
										</ul>
									</div>
									<div class="panel-wrapper">
										<div class="panel-body">
											<!-- Start:表头 -->
											<div class="table-heads">
												<table class="table table-striped table-bordered">
													<colgroup>
														<col width="10%"></col>
														<col width="40%"></col>
														<col width=""></col>
														<col width="10%"></col>
													</colgroup>
													<thead>
														<tr>
															<th class="center">序号</th>
															<th>预约名称 <span class="symbol required"></span></th>
															<th>借款金额（元） <span class="symbol required"></span></th>
															<th class="center">操作</th>
														</tr>
													</thead>
												</table>
											</div>
											<!-- End:表头 -->
											<!-- Start:列表 -->
											<div id="chaiBiaoList" class="table-body perfect-scrollbar" style="height: 380px;">
												<table class="table table-striped table-bordered table-hover">
													<colgroup>
														<col width="10%"></col>
														<col width="40%"></col>
														<col width=""></col>
														<col width="10%"></col>
													</colgroup>
													<tbody>
													<c:if test="${empty consumeForm.borrowCommonNameAccountList}">
														<tr>
															<td class="center">1</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="names" class="form-control input-sm" value="" maxlength="60" datatype="*1-60"  readonly="readonly" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts" class="form-control input-sm" value=""  maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" readonly="readonly" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
															</td>
														</tr>
														<tr>
															<td class="center">2</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="names" class="form-control input-sm" value="" maxlength="60" datatype="*1-60"  readonly="readonly" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts" class="form-control input-sm" value=""  maxlength="10" datatype="n1-10" readonly="readonly" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-removeRow"
																		data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${consumeForm.borrowCommonNameAccountList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="center"><c:out value="${status.index + 1 }"></c:out></td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="names" class="form-control input-sm" value="<c:out value="${record.names }"></c:out>"
																			maxlength="60" datatype="*1-60"  readonly="readonly" />
																	<hyjf:validmessage key="names${status.index }" ></hyjf:validmessage>
																</div>

															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts"
																			class="form-control input-sm" value="<c:out value="${record.accounts }"></c:out>" maxlength="10" datatype="n1-10" readonly="readonly" />
																	<hyjf:validmessage key="accounts${status.index }" ></hyjf:validmessage>
																</div>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow"
																			data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																</c:if>
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow"
																			data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
													</tbody>
												</table>
											</div>
											<!-- End:列表 -->
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<c:if test="${consumeForm.moveFlag eq 'BORROW_FIRST' }">
							<!-- 表单右侧 -->
							<div class="col-xs-12 col-md-7">
								<div class="form-group">
									<label class="control-label">
										初审意见：<span class="symbol required"></span>
									</label>
									<select name="verify" class="form-select2" style="width:100%"
											data-placeholder="请选初审意见..." datatype="*"  nullmsg="未指定初审意见">
										<option value="1">初审通过</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">
										发标方式：<span class="symbol required"></span>
									</label>
									<div class="clip-radio radio-primary">
										<input class="verifyradio" type="radio" value="1" name="verifyStatus" id="verifyStatus1" <c:if test="${ consumeForm.verifyStatus eq '1' }">checked</c:if>>
										<label for="verifyStatus1">定时发标</label>
										<input class="verifyradio" type="radio" value="2" name="verifyStatus" id="verifyStatus2" <c:if test="${ consumeForm.verifyStatus eq '2' }">checked</c:if>>
										<label for="verifyStatus2">立即发标</label>
										<input class="verifyradio" type="radio" value="3" name="verifyStatus" id="verifyStatus3" <c:if test="${ ( consumeForm.verifyStatus eq '3' ) or ( consumeForm.verifyStatus eq '0'  )}">checked</c:if>>
										<label for="verifyStatus3">暂不发标</label>
									</div>
									<hyjf:validmessage key="verify"></hyjf:validmessage>
								</div>

								<div class="form-group" id="ontimeDiv" style="display:none;">
									<label class="control-label">
										发标时间：<span class="symbol required"></span>
									</label>
									<span class="input-icon input-icon-right">
										<input type="text" name="ontime" id="ontime" class="form-control input-sm"
												value="<c:out value="${consumeForm.ontime }"></c:out>" maxlength="16" datatype="*1-16" nullmsg="未填写发标时间"
												onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm', errDealMode: 1})"/>
										<i class="fa fa-calendar"></i>
									</span>
									<hyjf:validmessage key="ontime"></hyjf:validmessage>
								</div>

								<div class="form-group">
									<label class="control-label">
										初审备注：<span class="symbol required"></span>
									</label>
									<textarea placeholder="请填写初审备注..." maxlength="255" datatype="*1-255" nullmsg="未填写初审备注" errormsg="初审备注长度不能超过255位" id="verifyRemark" name="verifyRemark" class="form-control" rows="5"><c:out value="${consumeForm.verifyRemark }"></c:out></textarea>
									<hyjf:validmessage key="verifyRemark"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div>
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<a href="#tab_xmms_2" class="btn btn-primary btn-o btn-wide pull-right fn-Next">
								下一步 <i class="fa fa-arrow-circle-right"></i>
							</a>
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:基本信息 -->

				<!-- Start:项目描述 -->
				<div class="tab-pane fade" id="tab_xmms_2">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<textarea name="borrowContents" class="form-control" cols="10" rows="12" ><c:out value="${consumeForm.borrowContents }"></c:out></textarea>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_jbxx_1" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_xmzl_3" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:项目描述 -->

				<!-- Start:抵押信息 -->
				<div class="tab-pane fade" id="tab_dyxx_4">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div id="dywlx" class="form-group">
								<label class="control-label margin-right-20">
									抵/质押物类型
								</label>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" name="typeCar" id="checkboxCLDY" value="2" class="dyxxCheckbox"
												data-panel="#cldyListPanel" <c:if test="${consumeForm.type eq '2' or consumeForm.type eq '3' or empty consumeForm.type }">checked</c:if> />
										<label for="checkboxCLDY">车辆抵押</label>
									</div>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" name="typeHouse" id="checkboxFCDY" value="1" class="dyxxCheckbox"
												data-panel="#fcdyListPanel" <c:if test="${consumeForm.type eq '1' or consumeForm.type eq '3' or empty consumeForm.type  }">checked</c:if> />
										<label for="checkboxFCDY">房产抵押</label>
									</div>
							</div>
							<!-- Start:Panel -->
							<div id="cldyListPanel" class="panel panel-white" ${(consumeForm.type eq '2' or consumeForm.type eq '3' or empty consumeForm.type) ? '' : 'style="display:none;"'}>
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 车辆抵/质押详情</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="cldyxxRowCounts" class="value">
													<c:if test="${empty consumeForm.borrowCarinfoList}">1</c:if>
													<c:if test="${!empty consumeForm.borrowCarinfoList}"><c:out value="${fn:length ( consumeForm.borrowCarinfoList ) }"></c:out></c:if>
												</span>
											</div>
										</li>
										<li>
											<span class="btn btn-success fileinput-button" data-original-title="导入车辆" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-file-o"></i> <span ></span>
												<input type="file" name="file" class=" btn-UploadCar">
											</span>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success btn-DownloadCar" href="#"
													data-original-title="下载模板" data-toggle="tooltip" data-placement="top"><i class="fa fa-download"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width=""></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">车辆品牌 <span class="symbol required"></span></th>
														<th class="center">型号</th>
														<th class="center">车系</th>
														<th class="center">颜色</th>
														<th class="center">出厂年份</th>
														<th class="center">产地</th>
														<th class="center">购买日期</th>
														<th class="center">购买价格（元）</th>
														<th class="center">是否有保险<span class="symbol required"></span></th>
														<th class="center">评估价（元）<span class="symbol required"></span></th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="cldyxxList" class="table-body perfect-scrollbar" style="height: 216px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width=""></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="9%"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty consumeForm.borrowCarinfoList}">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="brand" class="form-control input-sm" value="" maxlength="40" datatype="*1-40" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="model" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="carseries" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="color" class="form-control input-sm" value="" maxlength="16" datatype="*1-16" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="year" class="form-control input-sm" value="" maxlength="12" datatype="*1-12" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="place" class="form-control input-sm" value="" maxlength="60" datatype="*1-60" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="input-group">
																	<input type="text" name="buytime" class="form-control input-sm datepicker" value="" maxlength="10" datatype="d" ignore="ignore" />
																	<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="price" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="isSafe" data-ignore="true">
																		<option value=""></option>
																		<option value="1">无</option>
																		<option value="2">有</option>
																	</select>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="toprice" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${consumeForm.borrowCarinfoList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="brand" class="form-control input-sm" value="<c:out value="${record.brand }"></c:out>" maxlength="40" datatype="*1-40" ignore="ignore" />
																	<hyjf:validmessage key="brand${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="model" class="form-control input-sm" value="<c:out value="${record.model }"></c:out>" maxlength="50" datatype="*1-50" ignore="ignore" />
																	<hyjf:validmessage key="model${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="carseries" class="form-control input-sm" value="<c:out value="${record.carseries }"></c:out>" maxlength="50" datatype="*1-50" ignore="ignore" />
																	<hyjf:validmessage key="carseries${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="color" class="form-control input-sm" value="<c:out value="${record.color }"></c:out>" maxlength="16" datatype="*1-16" ignore="ignore" />
																	<hyjf:validmessage key="color${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="year" class="form-control input-sm" value="<c:out value="${record.year }"></c:out>" maxlength="12" datatype="*1-12" ignore="ignore" />
																	<hyjf:validmessage key="year${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="place" class="form-control input-sm" value="<c:out value="${record.place }"></c:out>" maxlength="60" datatype="*1-60" ignore="ignore" />
																	<hyjf:validmessage key="place${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="input-group">
																	<input type="text" name="buytime" class="form-control input-sm datepicker" value="<c:out value="${record.buytime }"></c:out>" maxlength="10" datatype="d" ignore="ignore" />
																	<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
																	<hyjf:validmessage key="buytime${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="price" class="form-control input-sm" value="<c:out value="${record.price }"></c:out>" maxlength="13" datatype="n1-13" ignore="ignore" />
																	<hyjf:validmessage key="price${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="isSafe" data-ignore="true">
																		<option value=""></option>
																		<option value="1" <c:if test="${record.isSafe eq '1'}">selected="selected"</c:if>>无</option>
																		<option value="2" <c:if test="${record.isSafe eq '2'}">selected="selected"</c:if>>有</option>
																	</select>
																	<hyjf:validmessage key="isSafe${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="toprice" class="form-control input-sm" value="<c:out value="${record.toprice }"></c:out>" maxlength="13" datatype="n1-13" ignore="ignore" />
																	<hyjf:validmessage key="toprice${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
									<div class="panel-footer" style="padding: 0 15px;">
										<table class="table no-margin" style="table-layout: fixed;">
											<tr>
												<td colspan="9" class="no-border">
													<span class="pull-right"> 总 计 ： </span>
												</td>
												<td width="9%" class="no-border center no-padding">
													<div style="padding:11px 4px;border-left:1px solid #ddd;border-right:1px solid #ddd;">
														<span class="label label-inverse block s-ellipsis toprice"
																data-toggle="tooltip" data-placement="top" data-original-title="0.00">0.00</span>
													</div>
												</td>
												<td width="5%" class="no-border"></td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<!-- End:Panel -->

							<!-- Start:Panel -->
							<div id="fcdyListPanel" class="panel panel-white" ${(consumeForm.type eq '1' or consumeForm.type eq '3' or empty consumeForm.type) ? '' : 'style="display:none"' }>
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 房产抵押详情</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="fcdyxxRowCounts" class="value">
													<c:if test="${empty consumeForm.borrowHousesList}">1</c:if>
													<c:if test="${!empty consumeForm.borrowHousesList}"><c:out value="${fn:length ( consumeForm.borrowHousesList ) }"></c:out></c:if>
												</span>
											</div>
										</li>
										<li>
											<span class="btn btn-success fileinput-button" data-original-title="导入房产" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-file-o"></i> <span ></span>
												<input type="file" name="file" class=" btn-UploadHouse">
											</span>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success btn-DownloadHouse" href="#"
													data-original-title="下载模板" data-toggle="tooltip" data-placement="top"><i class="fa fa-download"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="15%"></col>
													<col width="15%"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">房产类型 <span class="symbol required"></span></th>
														<th class="center">房产位置</th>
														<th class="center">建筑面积 <span class="symbol required"></span></th>
														<th class="center">市值 <span class="symbol required"></span></th>
														<th class="center">抵押价值（元） <span class="symbol required"></span></th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="fcdyxxList" class="table-body perfect-scrollbar" style="height: 216px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="15%"></col>
													<col width="15%"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty consumeForm.borrowHousesList}">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="housesType">
																		<option value=""></option>
																		<c:forEach items="${housesTypeList }" var="housesType" begin="0" step="1" varStatus="status">
																			<option value="${housesType.nameCd }"><c:out value="${housesType.name }"></c:out></option>
																		</c:forEach>
																	</select>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesLocation" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesArea" class="form-control input-sm" value="" maxlength="255"
																			datatype="/^\d{1,7}(\.\d{1,2})?$/" errormsg="必须为数字，且最多2位小数，7位整数" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesPrice" class="form-control input-sm" value="" maxlength="20" datatype="n1-20" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesToprice" class="form-control input-sm" value="" maxlength="20" datatype="n1-20" ignore="ignore" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${consumeForm.borrowHousesList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="housesType">
																		<option value=""></option>
																		<c:forEach items="${housesTypeList }" var="housesTypeRecord" >
																			<option value="${housesTypeRecord.nameCd }"
																				<c:if test="${housesTypeRecord.nameCd eq record.housesType}">selected="selected"</c:if>><c:out value="${housesTypeRecord.name }"></c:out>
																			</option>
																		</c:forEach>
																	</select>
																	<hyjf:validmessage key="housesType${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesLocation" class="form-control input-sm" value="<c:out value="${record.housesLocation }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore" />
																	<hyjf:validmessage key="housesLocation${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesArea" class="form-control input-sm" value="<c:out value="${record.housesArea }"></c:out>" maxlength="255"
																			datatype="/^\d{1,7}(\.\d{1,2})?$/" errormsg="必须为数字，且最多2位小数，7位整数" ignore="ignore"/>
																	<hyjf:validmessage key="housesArea${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesPrice" class="form-control input-sm" value="<c:out value="${record.housesPrice }"></c:out>" maxlength="20" datatype="n1-20" ignore="ignore"/>
																	<hyjf:validmessage key="housesPrice${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesToprice" class="form-control input-sm" value="<c:out value="${record.housesToprice }"></c:out>" maxlength="20" datatype="n1-20" ignore="ignore"/>
																	<hyjf:validmessage key="housesToprice${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" name="toprice" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
									<div class="panel-footer" style="padding: 0 15px;">
										<table class="table no-margin" style="table-layout: fixed">
											<tr>
												<td colspan="3" class="no-border">
													<span class="pull-right"> 总 计 ： </span>
												</td>
												<td width="15%" class="no-border center no-padding s-ellipsis">
													<div style="padding:11px 4px;border-left:1px solid #ddd;border-right:1px solid #ddd;">
														<span class="label label-inverse block s-ellipsis housesPrice"
																data-toggle="tooltip" data-placement="top" data-original-title="0.00">0.00</span>
													</div>
												</td>
												<td width="15%" class="no-border center no-padding s-ellipsis">
													<div style="padding:11px 4px;border-right:1px solid #ddd;">
														<span class="label label-inverse block s-ellipsis housesToprice"
																data-toggle="tooltip" data-placement="top" data-original-title="0.00">0.00</span>
													</div>
												</td>
												<td width="5%" class="no-border"></td>
											</tr>
										</table>
									</div>
								</div>
							</div>
							<!-- End:Panel -->
							<div class="form-group">
								<label class="control-label">
									抵/质押物描述
								</label>
								<textarea placeholder="请填写相关内容..." id="borrowMeasuresMort" name="borrowMeasuresMort" class="form-control" rows="5"><c:out value="${consumeForm.borrowMeasuresMort }"></c:out></textarea>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_xmzl_3" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_dbxx_5" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:抵押信息 -->

				<!-- Start:担保信息 -->
				<div class="tab-pane fade" id="tab_dbxx_5">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="row">
								<div class="col-md-12 col-lg-8">
									<div class="form-group">
										<label class="control-label">
											合作机构
										</label>
										<div>
											<select id="borrowMeasuresInstit" name="borrowMeasuresInstit" class="form-select2" style="width:70% " data-placeholder="请选择项合作机构...">
												<option value="" ></option>
												<c:forEach items="${links }" var="record" begin="0" step="1" varStatus="status">
													<option value="${record.webname }" <c:if test="${record.webname eq consumeForm.borrowMeasuresInstit}">selected="selected"</c:if>> <c:out value="${record.webname }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											机构介绍
										</label>
										<textarea placeholder="请填写相关内容..." id="borrowCompanyInstruction" name="borrowCompanyInstruction" class="form-control" rows="5"><c:out value="${consumeForm.borrowCompanyInstruction }"></c:out></textarea>
									</div>
									<div class="form-group">
										<label class="control-label">
											操作流程
										</label>
										<textarea placeholder="请填写相关内容..." id="borrowOperatingProcess" name="borrowOperatingProcess" class="form-control" rows="5"><c:out value="${consumeForm.borrowOperatingProcess }"></c:out></textarea>
									</div>
									<div class="form-group">
										<label class="control-label">
											风控措施
										</label>
										<textarea placeholder="请填写相关内容..." id="borrowMeasuresMea" name="borrowMeasuresMea" class="form-control" rows="5"><c:out value="${consumeForm.borrowMeasuresMea }"></c:out></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_dyxx_4" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_jkwxx_6" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:担保信息 -->

				<!-- Start:借款人信息 -->
				<div class="tab-pane fade" id="tab_jkwxx_6">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
														<div class="form-group">
								<label class="control-label margin-right-20">
									借款人：
								</label>
								<span class="label label-inverse" id="txtJieKuanren"><c:out value="${consumeForm.username}" default=" -- "></c:out></span>
							</div>
							<div class="form-group">
								<label class="control-label margin-right-20">
									借款类型
								</label>
								<div class="radio clip-radio radio-primary radio-inline jklx">
									<input type="radio" id="jklxQYJK" name="companyOrPersonal" value="1" <c:if test="${ ( consumeForm.companyOrPersonal eq '1' ) or ( empty consumeForm.companyOrPersonal )}">checked="checked"</c:if>>
									<label for="jklxQYJK">企业借款</label>
									<input type="radio" id="jklxGRJK" name="companyOrPersonal" value="2" <c:if test="${consumeForm.companyOrPersonal eq '2'}">checked="checked"</c:if>>
									<label for="jklxGRJK">个人借款</label>
								</div>
							</div>
							<!-- 企业借款面板 -->
							<div id="qyjkPanel">
								<div class="col-md-8">
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label">
													企业名称
												</label>
												<input type="text" name="buName" id="buName" class="form-control input-sm" value="<c:out value="${consumeForm.buName}"></c:out>"
															maxlength="50" datatype="*1-50" ignore="ignore" />
												<hyjf:validmessage key="buName" label="企业名称"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													所在地区
												</label>
												<div class="row">
													<div class="col-sm-4">
														<select name="location_pro" id="location_pro"
																class="form-select2 region-select" style="width:100%" data-placeholder="省" data-childsel="#location_cro" data-default="<c:out value="${consumeForm.location_pro}"></c:out>">
														</select>
													</div>
													<div class="col-sm-4">
														<select name="location_cro" id="location_cro"
																class="form-select2" style="width:100%" data-placeholder="市" data-childsel="#location_aro" data-default="<c:out value="${consumeForm.location_cro}">
														</c:out>"></select>
													</div>
													<div class="col-sm-4">
														<select name="location_aro" id="location_aro" class="form-select2" style="width:100%" data-placeholder="区" data-default="<c:out value="${consumeForm.location_aro}"></c:out>">
														</select>
													</div>
												</div>
											</div>
											<div class="form-group">
												<label class="control-label">
													公司规模
												</label>
												<select name="size" class="form-select2" style="width:100%" data-placeholder="人数">
													<option value=""></option>
													<c:forEach items="${companySizeList }" var="record" begin="0" step="1" varStatus="status">
														<option value="${record.nameCd }" <c:if test="${record.nameCd eq consumeForm.size}">selected="selected"</c:if>> <c:out value="${record.name }"></c:out></option>
													</c:forEach>
												</select>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label">
													注册时间
												</label>
												<input type="text" name="comRegTime" id="comRegTime" class="form-control input-sm" value="<c:out value="${consumeForm.comRegTime }"></c:out>"
														maxlength="30" datatype="*1-30" errormsg="注册时间长度不能超过30位文字" ignore="ignore" />
												<hyjf:validmessage key="comRegTime" label="注册时间"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													注册资本
												</label>
												<div class="input-group">
													<input type="text" name="regCaptial" id="regCaptial" class="form-control input-sm" value="<c:out value="${consumeForm.regCaptial }"></c:out>"
														maxlength="10" datatype="*1-10" errormsg="注册资本长度不能超过10位，且必须为数字" ignore="ignore" />
													<span class="input-group-addon">元</span>
												</div>
												<hyjf:validmessage key="regCaptial" label="注册资本"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													授信额度<span class="symbol required"></span>
												</label>
												<div class="input-group">
													<input type="text" name="credit" id="credit" class="form-control input-sm" value="<c:out value="${consumeForm.credit }"></c:out>"
														maxlength="10" datatype="n1-10" errormsg="授信额度长度不能超过10位，且必须为数字" nullmsg="请输入授信额度!" />
													<span class="input-group-addon">元</span>
												</div>
												<hyjf:validmessage key="credit" label="授信额度"></hyjf:validmessage>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											所属行业
										</label>
										<input type="text" name="userIndustry" id="userIndustry" class="form-control input-sm" value="<c:out value="${consumeForm.userIndustry }"></c:out>"
														maxlength="100" datatype="*1-100" errormsg="所属行业长度不能超过100位" ignore="ignore" />
										<hyjf:validmessage key="userIndustry" label="所属行业"></hyjf:validmessage>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="control-label">
											涉诉情况
										</label>
										<textarea placeholder="请填写相关内容..." id="litigation" name="litigation" class="form-control" rows="4"
												maxlength="100" datatype="*1-100" errormsg="涉诉情况长度不能超过100位" ignore="ignore" ><c:out value="${consumeForm.litigation }"></c:out></textarea>
										<hyjf:validmessage key="litigation" label="请填写相关内容"></hyjf:validmessage>
									</div>
									<div class="form-group">
										<label class="control-label">
											征信记录
										</label>
										<textarea placeholder="请填写相关内容..." id="creReport" name="creReport" class="form-control" rows="4"
												maxlength="100" datatype="*1-100" errormsg="征信记录长度不能超过100位" ignore="ignore" ><c:out value="${consumeForm.creReport }"></c:out></textarea>
										<hyjf:validmessage key="creReport" label="征信记录"></hyjf:validmessage>
									</div>
								</div>
								<div class="col-md-12">
									<label class="control-label">
										财务情况
									</label>
									<div class="form-group">
										<textarea name="accountContents" class="tinymce" cols="10" rows="12"><c:out value="${consumeForm.accountContents }"></c:out></textarea>
									</div>
									<hyjf:validmessage key="accountContents" label="财务情况"></hyjf:validmessage>
								</div>
							</div>
							<!-- 个人借款面板 -->
							<div id="grjkPanel" style="display:none">
								<div>
									<div class="col-md-8 col-lg-6">
										<div class="form-group">
											<label class="control-label">
												姓名
											</label>
											<input type="text" name="manname" id="manname" class="form-control input-sm" value="<c:out value="${consumeForm.manname }"></c:out>"
													maxlength="50" datatype="*1-50" errormsg="姓名长度不能超过50位" ignore="ignore" />
											<hyjf:validmessage key="manname" label="姓名"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												年龄
											</label>
											<input type="text" name="old" id="old" class="form-control input-sm" value="<c:out value="${consumeForm.old }"></c:out>"
													maxlength="3" datatype="n1-3" errormsg="年龄长度不能超过3位，且必须为数字" ignore="ignore" />
											<hyjf:validmessage key="old" label="年龄"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												岗位职业
											</label>
											<input type="text" name="position" id="position" class="form-control input-sm" value="<c:out value="${consumeForm.position }"></c:out>" 
													maxlength="20" datatype="s1-20" errormsg="岗位职业长度不能超过20位" ignore="ignore" />
											<hyjf:validmessage key="position" label="岗位职业"></hyjf:validmessage>
										</div>
										<div class="row">
											<div class="col-sm-6">
												<div class="form-group">
													<label class="control-label margin-bottom-0">
														性别
													</label>
													<div class="radio clip-radio radio-primary">
														<input type="radio" id="jkgrMan" name="sex" value="1" <c:if test="${consumeForm.sex eq '1' or empty consumeForm.sex}">checked='checked'</c:if>>
														<label for="jkgrMan">男</label>
														<input type="radio" id="jkgrWon" name="sex" value="2" <c:if test="${consumeForm.sex eq '2'}">checked='checked'</c:if>>
														<label for="jkgrWon">女</label>
													</div>
												</div>
											</div>
											<div class="col-sm-6">
												<div class="form-group">
													<label class="control-label margin-bottom-0">
														婚姻状况
													</label>
													<div class="radio clip-radio radio-primary">
														<input type="radio" id="jkgrYihun" name="merry" value="1" <c:if test="${consumeForm.merry eq '1'}">checked='checked'</c:if>>
														<label for="jkgrYihun">已婚</label>
														<input type="radio" id="jkgrWeihun" name="merry" value="2" <c:if test="${consumeForm.merry eq '2' or empty consumeForm.merry}">checked='checked'</c:if>>
														<label for="jkgrWeihun">未婚</label>
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label">
												工作城市
											</label>
											<div class="row">
												<div class="col-sm-6">
													<select name="location_p" id="location_p"
															class="form-select2 region-select" style="width:100%" data-placeholder="省" data-childsel="#location_c" data-default="<c:out value="${consumeForm.location_p}"></c:out>">
													</select>
												</div>
												<div class="col-sm-6">
													<select name="location_c" id="location_c"
															class="form-select2" style="width:100%" data-placeholder="市" data-default="<c:out value="${consumeForm.location_c}"></c:out>">
													</select>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label">
												行业
											</label>
											<input type="text" name="industry" id="industry" class="form-control input-sm" value="<c:out value="${consumeForm.industry }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="行业长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="industry" label="行业"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												授信额度<span class="symbol required"></span>
											</label>
											<div class="input-group">
												<input type="text" name="userCredit" id="userCredit" class="form-control input-sm" value="<c:out value="${consumeForm.userCredit }"></c:out>" 
													maxlength="10" datatype="n1-10" errormsg="授信额度长度不能超过10位，且必须为数字" nullmsg="请输入授信额度!" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="userCredit" label="授信额度"></hyjf:validmessage>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_dbxx_5" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_rzxx_7" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:借款人信息 -->

				<!-- Start:认证信息 -->
				<div class="tab-pane fade" id="tab_rzxx_7">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<!-- Start:Panel -->
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 认证信息一览</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="rzxxRowCounts" class="value">
													<c:if test="${empty consumeForm.borrowCommonCompanyAuthenList}">1</c:if>
													<c:if test="${!empty consumeForm.borrowCommonCompanyAuthenList}"><c:out value="${fn:length ( consumeForm.borrowCommonCompanyAuthenList ) }"></c:out></c:if>
												</span>
											</div>
										</li>
										<li>
											<span class="btn btn-success fileinput-button" data-original-title="导入认证信息" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-file-o"></i> <span ></span>
												<input type="file" name="file" class=" btn-UploadAuthen">
											</span>
										</li>
										<li class="panel-tools">
											<a class="btn btn-success btn-DownloadAuthen" href="#"
													data-original-title="下载模板" data-toggle="tooltip" data-placement="top"><i class="fa fa-download"></i></a>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="20%"></col>
													<col width="40%"></col>
													<col width=""></col>
													<col width="15%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">展示顺序 <span class="symbol required"></span></th>
														<th class="center">认证项目名称 <span class="symbol required"></span></th>
														<th class="center">认证时间 <span class="symbol required"></span></th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="rzxxList" class="table-body perfect-scrollbar" style="height: 380px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="20%"></col>
													<col width="40%"></col>
													<col width=""></col>
													<col width="15%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty consumeForm.borrowCommonCompanyAuthenList}">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="authenSortKey" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="authenName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="input-group">
																	<input type="text" name="authenTime" class="form-control input-sm datepicker" value="" maxlength="10" datatype="d" ignore="ignore"/>
																	<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${consumeForm.borrowCommonCompanyAuthenList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="authenSortKey" class="form-control input-sm" value="<c:out value="${record.authenSortKey }"></c:out>" maxlength="2" datatype="n1-2" ignore="ignore"/>
																	<hyjf:validmessage key="authenSortKey${status.index}" ></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="authenName" class="form-control input-sm" value="<c:out value="${record.authenName }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore"/>
																	<hyjf:validmessage key="authenName${status.index}" ></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="input-group">
																	<input type="text" name="authenTime" class="form-control input-sm datepicker" value="<c:out value="${record.authenTime }"></c:out>" maxlength="10" datatype="d" ignore="ignore"/>
																	<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
																	<hyjf:validmessage key="authenTime${status.index}" ></hyjf:validmessage>
																</div>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
								</div>
							</div>
							<!-- End:Panel -->
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_jkwxx_6" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_yyxx_8" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存
							</a>
						</div>
					</div>
				</div>
				<!-- End:认证信息 -->

				<!-- Start:运营信息 -->
				<div class="tab-pane fade" id="tab_yyxx_8">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12 col-lg-8">
							<fieldset>
								<legend>
									<strong class="no-wrap">可出借平台</strong>
								</legend>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" id="tzptCheckAll" value="all" <c:if test="${ (empty consumeForm.borrowNid) or ( consumeForm.canTransactionPc eq '1' and consumeForm.canTransactionWei eq '1' and consumeForm.canTransactionIos eq '1' and consumeForm.canTransactionAndroid eq '1' ) }">checked</c:if> />
										<label for="tzptCheckAll">全部</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionPc" id="checkboxPC" value="1" class="tzptCheckbox" <c:if test="${ (empty consumeForm.borrowNid) or ( consumeForm.canTransactionPc eq '1' ) }">checked</c:if> />
										<label for="checkboxPC">PC</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionWei" id="checkboxWGW" value="1" class="tzptCheckbox" <c:if test="${ (empty consumeForm.borrowNid) or ( consumeForm.canTransactionWei eq '1' ) }">checked</c:if> />
										<label for="checkboxWGW">微官网</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionIos" id="checkboxIOS" value="1" class="tzptCheckbox" <c:if test="${ (empty consumeForm.borrowNid) or ( consumeForm.canTransactionIos eq '1' ) }">checked</c:if> />
										<label for="checkboxIOS">IOS</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionAndroid" id="checkboxAndroid" value="1" class="tzptCheckbox" <c:if test="${ (empty consumeForm.borrowNid) or ( consumeForm.canTransactionAndroid eq '1' ) }">checked</c:if> />
										<label for="checkboxAndroid">Android</label>
									</div>
							</fieldset>
							<fieldset>
								<legend>
									<strong class="no-wrap">费率相关</strong>
								</legend>
								<div class="row">
									<div class="col-md-4">
										<label class="col-sm-9 control-label">放款服务费率</label>
										<label class="col-sm-3 control-label">
											<span class="badge badge-success" id="borrowServiceScale"><c:out value="${consumeForm.borrowServiceScale }" default="0.00"></c:out> </span>
										</label>
										<hyjf:validmessage key="borrowServiceScale" label="放款服务费率"></hyjf:validmessage>
									</div>

									<div class="col-md-4">
										<label class="col-sm-9 control-label">还款服务费上限</label>
										<label class="col-sm-3 control-label">
											<span class="badge badge-inverse" id="borrowManagerScale"><c:out value="${consumeForm.borrowManagerScale }" default="--"></c:out> </span>
										</label>
										<hyjf:validmessage key="borrowManagerScale" label="还款服务费上限"></hyjf:validmessage>
									</div>

									<div class="col-md-4">
										<label class="col-sm-9 control-label">还款服务费下限</label>
										<label class="col-sm-3 control-label">
											<span class="badge badge-inverse" id="borrowManagerScaleEnd"><c:out value="${consumeForm.borrowManagerScaleEnd }" default="--"></c:out> </span>
										</label>
										<hyjf:validmessage key="borrowManagerScaleEnd" label="还款服务费下限"></hyjf:validmessage>
									</div>
								</div>
							</fieldset>
							<fieldset>
								<legend>
									<strong class="no-wrap">投标相关</strong>
								</legend>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label class="control-label">
												最低投标金额
											</label>
											<div class="input-group">
												<span class="input-group-addon">￥</span>
												<input type="text" name="tenderAccountMin" id="tenderAccountMin" class="form-control input-sm" value="${consumeForm.tenderAccountMin}"
														datatype="n1-10" errormsg="最低投标金额长度不能超过10位，且必须为数字" ignore="ignore" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="tenderAccountMin" label="最低投标金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label>
												有效时间
											</label>
											<div class="input-group">
												<input type="text" name="borrowValidTime" class="form-control input-sm" value="<c:out value="${consumeForm.borrowValidTime }"></c:out>"<c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
														datatype="n1-2" errormsg="有效时间长度不能超过2位，且必须为数字" ignore="ignore" />
												<span class="input-group-addon">天</span>
											</div>
											<hyjf:validmessage key="borrowValidTime" label="有效时间"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label>
												最高投标金额
											</label>
											<div class="input-group">
												<span class="input-group-addon">￥</span>
												<input type="text" name="tenderAccountMax" id="tenderAccountMax" class="form-control input-sm" value="${consumeForm.tenderAccountMax}"
														datatype="n1-10" errormsg="最高投标金额长度不能超过10位，且必须为数字" ignore="ignore" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="tenderAccountMax" label="最高投标金额"></hyjf:validmessage>
											<hyjf:validmessage key="tenderAccount" label=""></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label class="block">
												运营标签
											</label>
											<div class="checkbox clip-check check-primary checkbox-inline">
												<input type="checkbox" id="operationLabel" name="operationLabel" value="1" <c:if test="${consumeForm.operationLabel eq '1' }">checked</c:if> />
												<label for="operationLabel">旅游标</label>
											</div>
										</div>
									</div>
								</div>
							</fieldset>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<a href="#tab_rzxx_7" class="btn btn-primary btn-o btn-wide pull-right fn-Next">
								<i class="fa fa-arrow-circle-left"></i> 上一步
							</a>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:运营信息 -->

				<!-- Start:项目资料 -->
				<div class="tab-pane fade" id="tab_xmzl_3">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<!-- Start:Panel -->
							<div class="panel panel-white">
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 项目资料一览</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="xmzxRowCounts" class="value">
													<c:if test="${empty consumeForm.borrowCommonImageList}">1</c:if>
													<c:if test="${!empty consumeForm.borrowCommonImageList}"><c:out value="${fn:length ( consumeForm.borrowCommonImageList ) }"></c:out></c:if>
												</span>
											</div>
										</li>
										<li>
											<span class="btn btn-success fileinput-button" data-original-title="批量上传" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-file-o"></i> <span ></span>
												<input type="file" name="file" class="morefileupload" multiple>
											</span>
										</li>
									</ul>
								</div>
								<div class="panel-wrapper">
									<div class="panel-body">
										<!-- Start:表头 -->
										<div class="table-heads">
											<table class="table table-striped table-bordered">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">展示顺序 <span class="symbol required"></span></th>
														<th class="center">资料名称 <span class="symbol required"></span></th>
														<th class="center">资料图片 <span class="symbol required"></span></th>
														<th class="center">图片预览</th>
														<th class="center">操作</th>
													</tr>
												</thead>
											</table>
										</div>
										<!-- End:表头 -->
										<!-- Start:列表 -->
										<div id="xmzxList" class="table-body perfect-scrollbar" style="height: 380px;">
											<table class="table table-striped table-bordered table-hover">
												<colgroup>
													<col width="15%"></col>
													<col width=""></col>
													<col width="15%"></col>
													<col width="93px"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty consumeForm.borrowCommonImageList}">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="" />
																			<input type="hidden" name="imagePath" value="" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="暂无预览...">
																	<img />
																</a>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${consumeForm.borrowCommonImageList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageSort" class="form-control input-sm" value="<c:out value="${record.imageSort }"></c:out>" maxlength="2" datatype="n1-2" ignore="ignore"/>
																	<hyjf:validmessage key="imageSort${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="">
																<div class="form-group">
																	<input type="text" name="imageName" class="form-control input-sm" value="<c:out value="${record.imageName }"></c:out>" maxlength="255" datatype="*1-255" ignore="ignore"/>
																	<hyjf:validmessage key="imageName${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class=" center">
																<div class="row fileupload-buttonbar">
																	<div class="col-lg-12">
																		<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
																			<input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
																			<input type="hidden" name="imageRealName" value="<c:out value="${record.imageRealName }"></c:out>" />
																			<input type="hidden" name="imagePath" value="<c:out value="${record.imagePath }"></c:out>" />
																		</span>
																	</div>
																</div>
															</td>
															<td class="center">
																<a class="thumbnails-wrap"
																		data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
																		data-title="图片预览" data-content="<img src='<c:out value="${record.imageSrc }"></c:out>' style='max-height:350px;'/>">
																	<img src="<c:out value="${record.imageSrc }"></c:out>" />
																</a>
																<hyjf:validmessage key="imageSrc${status.index}"></hyjf:validmessage>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																	<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
																<c:if test="${ status.index ne 0}">
																	<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
																</c:if>
															</td>
														</tr>
													</c:forEach>
												</tbody>
											</table>
										</div>
										<!-- End:列表 -->
									</div>
								</div>
							</div>
							<!-- End:Panel -->
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="margin-top-15">
								<span class="symbol required"></span>必须填写的项目
								<hr>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-5">
							<p>
								点击【提交保存】按钮，保存当前的填写的资料。
							</p>
						</div>
						<div class="col-md-7">
							<div class="btn-group pull-right">
								<a href="#tab_xmms_2" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_dyxx_4" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>

							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:项目资料 -->
			</div>

			<!-- 列表的行模板 -->
			<table id="rowTemplts" type="text/x-tmpl" style="display:none">
				<!-- 拆表的行模板 -->
				<tr>
					<td class="center"></td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="names" class="form-control input-sm" maxlength="60" value="" datatype="*1-60"  ignore="ignore" readonly="readonly" />
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="accounts" class="form-control input-sm" maxlength="60" value="" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字"  ignore="ignore" readonly="readonly" />
						</div>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>

				<!-- 车辆抵押的行模板 -->
				<tr>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="brand" class="form-control input-sm" value="" maxlength="40" datatype="*1-40" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="model" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="carseries" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="color" class="form-control input-sm" value="" maxlength="16" datatype="*1-16" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="year" class="form-control input-sm" value="" maxlength="12" datatype="*1-12" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="place" class="form-control input-sm" value="" maxlength="60" datatype="*1-60" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="input-group">
							<input type="text" name="buytime" class="form-control input-sm" value="" maxlength="10" datatype="d" ignore="ignore"/>
							<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="price" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<select class="form-control input-sm" name="isSafe" data-ignore="true">
								<option value=""></option>
								<option value="1">有</option>
								<option value="2">无</option>
							</select>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="toprice" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore"/>
						</div>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow"
								 data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>

				<!-- 房产抵押的行模板 -->
				<tr>
					<td class="vertical-align-top">
						<div class="form-group">
							<select class="form-control input-sm" name="housesType">
								<option value=""></option>
								<c:forEach items="${housesTypeList }" var="housesType" begin="0" step="1" varStatus="status">
									<option value="${housesType.nameCd }"><c:out value="${housesType.name }"></c:out></option>
								</c:forEach>
							</select>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesLocation" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesArea" class="form-control input-sm" value="" maxlength="255"
									datatype="/^\d{1,7}(\.\d{1,2})?$/" errormsg="必须为数字，且最多2位小数7位整数" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesPrice" class="form-control input-sm" value="" maxlength="20" datatype="n1-20" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesToprice" class="form-control input-sm" value="" maxlength="20" datatype="n1-20" ignore="ignore"/>
						</div>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>

				<!-- 认证信息的行模板 -->
				<tr>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="authenSortKey" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="authenName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="input-group">
							<input type="text" name="authenTime" class="form-control input-sm" value="" maxlength="10" datatype="d" ignore="ignore"/>
							<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
						</div>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>

				<!-- 图片信息的行模板 -->
				<tr>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageSort" class="form-control input-sm" value="" maxlength="2" datatype="n1-2" ignore="ignore"/>
						</div>
					</td>
					<td class="">
						<div class="form-group">
							<input type="text" name="imageName" class="form-control input-sm" value="" maxlength="255" datatype="*1-255" ignore="ignore"/>
						</div>
					</td>
					<td class=" center">
						<div class="row fileupload-buttonbar">
							<div class="col-lg-12">
								<span class="btn btn-success fileinput-button"> <i class="glyphicon glyphicon-plus"></i> <span>上传图片</span>
									<input type="file" name="file" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
									<input type="hidden" name="imageRealName" value="" />
									<input type="hidden" name="imagePath" value="" />
								</span>
							</div>
						</div>
					</td>
					<td class="center">
						<a class="thumbnails-wrap"
								data-toggle="popover" data-placement="left" data-trigger="hover" data-html="true" data-container="body"
								data-title="图片预览" data-content="暂无预览...">
							<img />
						</a>
					</td>
					<td class="center">
						<a class="btn btn-transparent btn-xs fn-removeRow" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>


	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
	<style>
	.table-heads .table-striped {
		margin-bottom: 0;
	}
	.table-body {
		position: relative;
	}
	.table-body .vertical-align-top {
		vertical-align: top!important;
	}
	.table-body .table-striped {
		border-top: none;
		margin-bottom: 0;
	}
	.table-body .table-striped tr:first-child td {
		border-top: 0;
	}
	.table-body .form-group {
		margin-bottom: 0;
	}
	.thumbnails-wrap {
		background: #f5f5f5;
		border: 1px solid #ccc;
		padding: 3px;
		display: inline-block;
	}
	.thumbnails-wrap img {
		min-width: 35px;
		max-width: 70px;
		height: 22px;
	}
	.popover {
		max-width: 500px;
	}
	.popover img {
		max-width: 460px;
	}
	</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
		<!-- The Templates plugin is included to render the upload/download listings -->
<%-- 		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
		<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
		<!-- The Canvas to Blob plugin is included for image resizing functionality -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
		<!-- blueimp Gallery script -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
		<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
		<!-- The basic File Upload plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
		<!-- The File Upload processing plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
		<!-- The File Upload image preview & resize plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
		<!-- The File Upload audio preview plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
		<!-- The File Upload video preview plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
		<!-- The File Upload validation plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
		<!-- The File Upload user interface plugin -->
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>


	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/consume/consume.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
