
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

<%-- 画面功能菜单设置开关  --%>
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
					<a href="#tab_zcxx_9" data-toggle="tab"><i class="fa fa-briefcase"></i> 项目信息</a>
				</li>
				<li>
					<a href="#tab_czya_10" data-toggle="tab"><i class="fa fa-legal"></i> 处置预案</a>
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
				<input type="hidden" name="moveFlag" id="moveFlag" value="${borrowForm.moveFlag}" />
				<input type="hidden" name="status" value="${borrowForm.status}" />
				<input type="hidden" name="pageUrl" value="${borrowForm.pageUrl}" />
				
				<%-- 检索条件保持 --%>
				<!-- 项目编号 -->
				<input type="hidden" name="borrowNidSrch" value="${borrowForm.borrowNidSrch}" />
				<!-- 项目名称 -->
				<input type="hidden" name="borrowNameSrch" value="${borrowForm.borrowNameSrch}" />
				<!-- 借  款 人 -->
				<input type="hidden" name="usernameSrch" value="${borrowForm.usernameSrch}" />
				<!-- 项目状态 -->
				<input type="hidden" name="statusSrch" value="${borrowForm.statusSrch}" />
				<!-- 项目类型 -->
				<input type="hidden" name="projectTypeSrch" value="${borrowForm.projectTypeSrch}" />
				<!-- 还款方式 -->
				<input type="hidden" name="borrowStyleSrch" value="${borrowForm.borrowStyleSrch}" />
				<!-- 时间 -->
				<input type="hidden" name="timeStartSrch" value="${borrowForm.timeStartSrch}" />
				<input type="hidden" name="timeEndSrch" value="${borrowForm.timeEndSrch}" />
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
				<c:if test="${ ( ( borrowForm.status ne '10' ) and ( borrowForm.status ne '0' )) and ( !empty borrowForm.status )}">
					<c:set var="isEdit" value="1"></c:set><!-- isEdit=1 disabled 入力不可修改 -->
				</c:if>
				
				
			<div class="tab-content">
				<!-- Start:基本信息 -->
				<div class="tab-pane fade in active" id="tab_jbxx_1">
					<div style="line-height:30px;">
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次借款的基本信息，以做存档.
						<div style="float:right;">
							<span class="btn btn-success fileinput-button" data-original-title="借款填充" data-toggle="tooltip" data-placement="top"> 
								<i class="fa fa-file-o"></i> <span></span>
								<input name="file" class=" btn-contentfill" type="file">
							</span>
							<a class="btn btn-success btn-DownloadContentFill" href="#" data-original-title="下载模板" data-toggle="tooltip" data-placement="top"><i class="fa fa-download"></i></a>
						</div>
					</div>
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
														<select id="" name="" class="form-select2" style="width:100%" data-allow-clear="true" disabled="disabled" data-placeholder="请选择项目类型...">
															<option value=""></option>
															<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
																<option value="${borrowProjectType.borrowCd }" data-borrowclass="${borrowProjectType.borrowClass }" data-increasemoney="${borrowProjectType.increaseMoney}" data-interestcoupon="${borrowProjectType.interestCoupon }"
																		data-tastemoney="${borrowProjectType.tasteMoney }"   data-min="${ borrowProjectType.investStart}" data-max="${ borrowProjectType.investEnd}" data-increaseinterestflag="${borrowProjectType.increaseInterestFlag}"
																		<c:if test="${borrowProjectType.borrowCd eq borrowForm.projectType}">selected="selected"</c:if>>
																	<c:out value="${borrowProjectType.borrowName }"></c:out></option>
															</c:forEach>
														</select>
														<input type="hidden" id="projectType" name="projectType" value="${borrowForm.projectType}" />
													</div>
												</c:if>
									<c:if test="${ ( isEdit ne '1' ) }">
										<div>
											<select id="projectType" name="projectType" class="form-select2" data-allow-clear="true" datatype="*" nullmsg="未指定项目类型！"  style="width:100%" data-placeholder="请选择项目类型...">
												<option value=""></option>
												<c:forEach items="${borrowProjectTypeList }" var="borrowProjectType" begin="0" step="1" varStatus="status">
													<option value="${borrowProjectType.borrowCd }" data-borrowclass="${borrowProjectType.borrowClass }"  data-increasemoney="${borrowProjectType.increaseMoney}" data-interestcoupon="${borrowProjectType.interestCoupon }"
															data-tastemoney="${borrowProjectType.tasteMoney }"  data-min="${ borrowProjectType.investStart}" data-max="${ borrowProjectType.investEnd}" data-increaseinterestflag="${borrowProjectType.increaseInterestFlag}"
														<c:if test="${borrowProjectType.borrowCd eq borrowForm.projectType}">selected="selected"</c:if>>
														<c:out value="${borrowProjectType.borrowName }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</c:if>
									<hyjf:validmessage key="projectType" label="项目类型"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										资产来源 <span class="symbol required"></span>
									</label>
									<c:if test="${ ( isEdit eq '1' ) }">
										<div>
											<select id="" name="" class="form-select2" style="width:100%" data-allow-clear="true" disabled="disabled" data-placeholder="请选择资产来源...">
												<option value=""></option>
												<c:forEach items="${instList }" var="instConfig" begin="0" step="1" varStatus="status">
													<option value="${instConfig.instCode }" data-instcode="${instConfig.instCode }"
															<c:if test="${instConfig.instCode eq borrowForm.instCode}">selected="selected"</c:if>>
														<c:out value="${instConfig.instName }"></c:out></option>
												</c:forEach>
											</select>
											<input type="hidden" id="instCode" name="instCode" value="${borrowForm.instCode}" />
										</div>
									</c:if>
									<c:if test="${ ( isEdit ne '1' ) }">
										<div>
											<select id="instCode" name="instCode" class="form-select2" data-allow-clear="true" disabled="disabled" datatype="*" nullmsg="未指定资产来源！"  style="width:100%" data-placeholder="请选择资产来源...">
												<option value=""></option>
												<c:forEach items="${instList }" var="instConfig" begin="0" step="1" varStatus="status">
													<option value="${instConfig.instCode }"
															<c:if test="${instConfig.instCode eq borrowForm.instCode}">selected="selected"</c:if>>
														<c:out value="${instConfig.instName }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</c:if>
									<hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										借款人用户名 <span class="symbol required"></span>
									</label>
									<input type="text" name="username" id="username" class="form-control input-sm" value='<c:out value="${borrowForm.username}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
										maxlength="30" datatype="*1-30" nullmsg="未填写借款人用户名"  errormsg="借款人用户名长度不能超过30位字符" ajaxurl="isExistsUser"/>
									<hyjf:validmessage key="username"></hyjf:validmessage>
								</div>
								<%--<div class="form-group">
									<label class="control-label">
										项目申请人
									</label>
									<input type="text" name="applicant" id="applicant" class="form-control input-sm" value='<c:out value="${borrowForm.applicant}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
										maxlength="20" datatype="*0-20" errormsg="项目申请人长度不能超过20位字符"/>
									<hyjf:validmessage key="applicant"></hyjf:validmessage>
								</div>--%>
								<div class="form-group">
									<label class="control-label">
										担保机构用户名
									</label>
									<input type="text" name="repayOrgName" id="repayOrgName" class="form-control input-sm" value='<c:out value="${borrowForm.repayOrgName}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
										maxlength="30" datatype="*0-30"  errormsg="担保机构用户名长度不能超过30位字符" ajaxurl="isRepayOrgUser"/>
									<hyjf:validmessage key="repayOrgName"></hyjf:validmessage>
								</div>
								<c:if test="${empty borrowForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目编号 <span class="symbol required"></span>
										</label>
										<div class="input-group">
											<input type="text" id="borrowPreNid" name="borrowPreNid" class="form-control input-sm" value='<c:out value="${borrowForm.borrowPreNid}"></c:out>'
													maxlength="12" datatype="n1-12" nullmsg="未填写项目编号" errormsg="项目编号长度不能超过12位，且必须为数字" ajaxurl="isExistsBorrowPreNidRecord" />
											<span class="input-group-btn">
												<a class="btn btn-primary btn-sm" id="getBorrowPreNid"> 重新获取</a>
											</span>
										</div>
										<span class="help-block"><i class="ti-info-alt text-primary"></i> 14110001意为2014年11月份第1单借款标。</span>
										<hyjf:validmessage key="borrowPreNid" label="项目编号"></hyjf:validmessage>
									</div>
								</c:if>
								<c:if test="${!empty borrowForm.borrowNid }">
									<div class="form-group">
										<label class="control-label">
											项目编号 <span class="symbol required"></span>
										</label>
										<input type="text" id="borrowNid" name="borrowNid" class="form-control input-sm" value='<c:out value="${borrowForm.borrowNid}"></c:out>' readonly="readonly" />
										<hyjf:validmessage key="borrowNid" label="项目编号"></hyjf:validmessage>
									</div>
								</c:if>
								<div class="form-group">
									<label class="control-label">
										项目名称 <span class="symbol required"></span>
									</label>
									<input type="text" id="projectName" name="projectName" class="form-control input-sm" value='<c:out value="${borrowForm.projectName}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
											maxlength="15" datatype="*1-15" nullmsg="未填写项目名称" errormsg="项目名称长度不能超过15位字符" />
									<hyjf:validmessage key="projectName" label="项目名称"></hyjf:validmessage>
								</div>

								<div class="form-group">
									<label class="control-label">
										借款金额 <span class="symbol required"></span>
									</label>
									<div class="input-group">
										<span class="input-group-addon">￥</span>
										<input type="text" id="account" name="account" class="form-control input-sm" value='<c:out value="${borrowForm.account}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
												maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" 
												ajaxurl="isAccountLegal" />
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="account" label="借款金额"></hyjf:validmessage>
									<hyjf:validmessage key="account-error"></hyjf:validmessage>
								</div>
								
								<!-- 如果是新增画面 -->
								<c:if test="${ ( isEdit ne '1' ) }">
									<div class="form-group">
										<label class="control-label">
											受托支付标志
										</label>
										<div class="clip-radio radio-primary">
											<input type="radio" value="0" name="entrustedFlg" id="entrustedFlgNo" <c:if test="${ ( borrowForm.entrustedFlg eq '0' ) or ( empty borrowForm.entrustedFlg) }">checked</c:if>>
											<label for="entrustedFlgNo">否</label>
											<input type="radio" value="1" name="entrustedFlg" id="entrustedFlgYes" <c:if test="${ ( borrowForm.entrustedFlg eq '1' )  }">checked</c:if>>
											<label for="entrustedFlgYes">是</label>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											受托支付用户名 <span class="symbol"></span>
										</label>
										<input type="text" name="entrustedUsername" id="entrustedUsername" class="form-control input-sm" value='<c:out value="${borrowForm.entrustedUsername}"></c:out>'
											<c:if test="${ ( borrowForm.entrustedFlg eq '0' ) or ( empty borrowForm.entrustedFlg) }">disabled ignore="ignore"</c:if> 
											maxlength="30" datatype="*1-30" nullmsg="受托支付用户名"  errormsg="受托支付用户名长度不能超过30位字符" ajaxurl="isExistEntrustedUser" />
										<hyjf:validmessage key="entrustedUsername"></hyjf:validmessage>
									</div>
								</c:if>
								
								<!-- 如果是修改画面 此处不用再单独判断是否已备案，isEdit已经判断：status在(1.2.3.4.5.6.7)范围内不允许修改-->
								<c:if test="${ ( isEdit eq '1' ) }">
									<div class="form-group">
										<label class="control-label">
											受托支付标志 
										</label>
										<div class="clip-radio radio-primary">
											<input type="radio" value="0" name="entrustedFlg" id="entrustedFlgNo" disabled <c:if test="${ ( borrowForm.entrustedFlg eq '0' ) or ( empty borrowForm.entrustedFlg) }">checked</c:if>>
											<label for="entrustedFlgNo">否</label>
											<input type="radio" value="1" name="entrustedFlg" id="entrustedFlgYes" disabled <c:if test="${ ( borrowForm.entrustedFlg eq '1' )  }">checked</c:if>>
											<label for="entrustedFlgYes">是</label>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											受托支付用户名 <span class="symbol <c:if test="${ ( borrowForm.entrustedFlg eq '1' ) }">required</c:if> "></span>
										</label>
										<input type="text" name="entrustedUsername" id="entrustedUsername" class="form-control input-sm" 
											value='<c:out value="${borrowForm.entrustedUsername}"></c:out>'  
											disabled ignore='ignore'
											maxlength="30" datatype="*1-30" nullmsg="受托支付用户名"  errormsg="受托支付用户名长度不能超过30位字符" ajaxurl="isExistEntrustedUser"/>
										<hyjf:validmessage key="entrustedUsername"></hyjf:validmessage>
									</div>
								</c:if>
								
								
								

								<div class="form-group">
									<label class="control-label">
										还款方式 <span class="symbol required"></span>
									</label>
									<c:if test="${ ( isEdit eq '1' ) }">
										<select name="" class="form-select2" style="width:100%" id="sel-hkfs" disabled="disabled"
												data-placeholder="请选还款方式..." data-allow-clear="true" datatype="*"  nullmsg="未指定还款方式">
											<option value=""></option>
											<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
												<option value="${borrowStyle.repayMethod }" ${ borrowStyle.borrowClass} 
													<c:if test="${borrowStyle.repayMethod eq borrowForm.borrowStyle}">selected="selected"</c:if>>
													<c:out value="${borrowStyle.methodName}"></c:out>
												</option>
											</c:forEach>
										</select>
										<input type="hidden" name="borrowStyle" value="${borrowForm.borrowStyle}" />
									</c:if>
									<c:if test="${ ( isEdit ne '1' ) }">
										<select name="borrowStyle" class="form-select2" style="width:100%" id="sel-hkfs" 
												data-placeholder="请选还款方式..." data-allow-clear="true" datatype="*"  nullmsg="未指定还款方式">
											<option value=""></option>
											<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
												<option value="${borrowStyle.repayMethod }" ${ borrowStyle.borrowClass} 
													<c:if test="${borrowStyle.repayMethod eq borrowForm.borrowStyle}">selected="selected"</c:if>>
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
										<input type="text" name="borrowApr" id="borrowApr" class="form-control input-sm" maxlength="5"
												value='<c:out value="${borrowForm.borrowApr}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
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
										<input type="text" id="borrowPeriod" name="borrowPeriod" class="form-control input-sm" value='<c:out value="${borrowForm.borrowPeriod}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
												maxlength="3" datatype="n1-3" nullmsg="未填写借款期限" errormsg="借款期限长度不能超过3位，且必须为数字" ajaxurl="isBorrowPeriodCheck" />
										<span class="input-group-addon">
											<c:choose>
												<c:when test="${borrowForm.borrowStyle eq 'endday'}">天</c:when>
												<c:otherwise>月</c:otherwise>
											</c:choose>
										</span>
									</div>
									<hyjf:validmessage key="borrowPeriod" label="借款期限"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<c:if test="${empty borrowForm.borrowNid }">
							<!-- 表单右侧 -->
							<div class="col-xs-12 col-md-7">
								<div class="form-group" style="display: none">
									<label class="control-label">
										是否预约
									</label>
									<div class="clip-radio radio-primary shifuchaibiao">
										<input type="radio" value="no" name="isChaibiao" id="chai_no" <c:if test="${ ( borrowForm.isChaibiao eq 'no' ) }">checked</c:if>>
										<label for="chai_no">不预约</label>
										<input type="radio" value="yes" name="isChaibiao" id="chai_yes" <c:if test="${ ( borrowForm.isChaibiao eq 'yes' ) or ( empty borrowForm.isChaibiao) }">checked</c:if>>
										<label for="chai_yes">预约</label>
									</div>
								</div>
								
								<!-- new added start-->
								<div class="form-group">
									<label class="control-label">
										是否调用标的分配规则引擎 
									</label>
									<div class="clip-radio radio-primary">
										<input type="radio" value="0" name="isEngineUsed" id="engine_no" <c:if test="${ ( borrowForm.isEngineUsed eq '0' ) or ( empty borrowForm.isEngineUsed) }">checked</c:if>>
										<label for="engine_no">否</label>
										<input type="radio" value="1" name="isEngineUsed" id="engine_yes" <c:if test="${ ( borrowForm.isEngineUsed eq '1' )}">checked</c:if>>
										<label for="engine_yes">是</label>
									</div>
									<!-- add by liushouyi HJH3 -->
									<hyjf:validmessage key="isEngineUsedErr" label="是否使用引擎"></hyjf:validmessage>
								</div>
								<!-- new added end-->
								
								<div class="panel panel-white" style="display: none">
									<div class="panel-heading border-light">
										<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 预约配置</h4>
										<ul class="panel-heading-tabs border-light">
											<li>
												<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
													<i class="fa fa-indent text-primary margin-right-10"></i>
													<span id="cbRowCounts" class="value">
														<c:if test="${empty borrowForm.borrowCommonNameAccountList}">2</c:if>
														<c:if test="${!empty borrowForm.borrowCommonNameAccountList}"><c:out value="${fn:length ( borrowForm.borrowCommonNameAccountList ) }"></c:out></c:if>
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
															<th>预约金额（元） <span class="symbol required"></span></th>
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
													<c:if test="${empty borrowForm.borrowCommonNameAccountList}">
														<tr>
															<td class="center">1</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="names" class="form-control input-sm" value="" maxlength="60" datatype="*1-60" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts" class="form-control input-sm" value=""  maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" ajaxurl="isAccountLegal"  />
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
																	<input type="text" name="names" class="form-control input-sm" value="" maxlength="60" datatype="*1-60" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts" class="form-control input-sm" value=""  maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" ajaxurl="isAccountLegal" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-removeRow"
																		data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${borrowForm.borrowCommonNameAccountList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="center"><c:out value="${status.index + 1 }"></c:out></td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="names" class="form-control input-sm" value="<c:out value="${record.names }"></c:out>"
																			maxlength="60" datatype="*1-60" />
																	<hyjf:validmessage key="names${status.index }" ></hyjf:validmessage>
																</div>
																
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="accounts"
																			class="form-control input-sm" value="<c:out value="${record.accounts }"></c:out>" maxlength="10" datatype="n1-10" />
																	<hyjf:validmessage key="accounts${status.index }" ></hyjf:validmessage>
																</div>
															</td>
															<td class="center">
																<c:if test="${ status.index eq 0}">
																	<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
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
							</div>
						</c:if>
						<c:if test="${borrowForm.moveFlag eq 'BORROW_FIRST' }">
							<!-- 表单右侧 -->
							<div class="col-xs-12 col-md-7">
								<div class="form-group">
									<label class="control-label">
										初审意见：<span class="symbol required"></span>
									</label>
									<select name="verify" class="form-select2" style="width:100%"
											data-placeholder="请选初审意见..." data-allow-clear="true" datatype="*"  nullmsg="未指定初审意见">
										<option value="1" selected="selected">初审通过</option>
									</select>
								</div>
								<div class="form-group">
									<label class="control-label">
										发标方式：<span class="symbol required"></span>
									</label>
									<input type="hidden" id = "engineFlag" name="engineFlag" value="${engineFlag }" />
									<div class="clip-radio radio-primary"> 
										<input class="verifyradio" type="radio" value="2" name="verifyStatus" id="verifyStatus1" <c:if test="${ borrowForm.verifyStatus eq '2' }">checked</c:if>>
										<label for="verifyStatus1">暂不发标</label>
										<%-- 0:未使用引擎 1：使用 --%>
										<%-- <c:if test="${engineFlag eq '0' }"> --%>
											<input class="verifyradio" type="radio" value="3" name="verifyStatus" id="verifyStatus2" <c:if test="${ borrowForm.verifyStatus eq '3' }">checked</c:if>>
											<label for="verifyStatus2">定时发标</label>
										<%-- </c:if> --%>
										<input class="verifyradio" type="radio" value="4" name="verifyStatus" id="verifyStatus3" <c:if test="${ borrowForm.verifyStatus eq '4' }">checked</c:if>>
										<label for="verifyStatus3">立即发标</label>
									</div>
									
									
									<hyjf:validmessage key="verify"></hyjf:validmessage>
								</div>
								
								<!-- new added start-->
								<div class="form-group">
									<label class="control-label">
										是否调用标的分配规则引擎：<span class="symbol required"></span>
									</label>
									<div class="clip-radio radio-primary">
										<input type="radio" value="0" name="isEngineUsed" id="engine_no" <c:if test="${ ( borrowForm.isEngineUsed eq '0' ) or ( empty borrowForm.isEngineUsed) }">checked</c:if>>
										<label for="engine_no">否</label>
										<input type="radio" value="1" name="isEngineUsed" id="engine_yes" <c:if test="${ ( borrowForm.isEngineUsed eq '1' )}">checked</c:if>>
										<label for="engine_yes">是</label>
									</div>
									<!-- add by liushouyi HJH3 -->
									<hyjf:validmessage key="isEngineUsedError" label="是否使用引擎"></hyjf:validmessage>
								</div>

								<div class="form-group" id="ontimeDiv" style='display:none;'>
									<label class="control-label">
										发标时间：<span class="symbol required"></span>
									</label>
									<span class="input-icon input-icon-right">
										<input type="text" name="ontime" id="ontime" class="form-control input-sm"
												value="<c:out value="${borrowForm.ontime }"></c:out>" maxlength="16" datatype="*1-16" nullmsg="未填写发标时间"
												onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm', errDealMode: 1})"/>
										<i class="fa fa-calendar"></i>
									</span>
									<hyjf:validmessage key="ontime"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										初审备注：<span class="symbol required"></span>
									</label>
									<textarea placeholder="请填写初审备注..." maxlength="255" datatype="*1-255" nullmsg="未填写初审备注" errormsg="初审备注长度不能超过255位" id="verifyRemark" name="verifyRemark" class="form-control" rows="5"><c:out value="${borrowForm.verifyRemark }"></c:out></textarea>
									<hyjf:validmessage key="verifyRemark"></hyjf:validmessage>
								</div>
							</div>
						</c:if>

						<c:if test="${borrowForm.isRegistFlg eq 'BORROW_REGIST' }">
							<!-- 表单右侧 -->
							<div class="col-xs-12 col-md-7">
								<!-- new added start-->
								<div class="form-group">
									<label class="control-label">
										是否调用标的分配规则引擎：<span class="symbol required"></span>
									</label>
									<div class="clip-radio radio-primary">
										<input type="radio" value="0" name="isEngineUsed" id="engine_no" <c:if test="${ ( borrowForm.isEngineUsed eq '0' ) or ( empty borrowForm.isEngineUsed) }">checked</c:if>>
										<label for="engine_no">否</label>
										<input type="radio" value="1" name="isEngineUsed" id="engine_yes" <c:if test="${ ( borrowForm.isEngineUsed eq '1' )}">checked</c:if>>
										<label for="engine_yes">是</label>
									</div>
									<!-- add by liushouyi HJH3 -->
									<hyjf:validmessage key="isEngineUsedErrors" label="是否使用引擎"></hyjf:validmessage>
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
				
				<!-- 新加字段 -->
					<div class="row">
						<div class="col-md-12">
							<div class="row">
								<div class="col-md-12 col-lg-8">
									<div class="form-group">
										<label class="control-label">
											资产编号
										</label>
										<input type="text" name="borrowAssetNumber" id="borrowAssetNumber" class="form-control input-sm" value="<c:out value="${borrowForm.borrowAssetNumber }"></c:out>" maxlength="50"  />
									</div>
									<div class="form-group">
										<label class="control-label">
											项目来源
										</label>
										<input type="text" name="borrowProjectSource" id="borrowProjectSource" class="form-control input-sm" value="<c:out value="${borrowForm.borrowProjectSource }"></c:out>" maxlength="10"  />
									</div>
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													起息时间
												</label>
												<input type="text" name="borrowInterestTime" id="borrowInterestTime" class="form-control input-sm" value="<c:out value="${borrowForm.borrowInterestTime }"></c:out>" maxlength="50" />
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													到期时间
												</label>
												<input type="text" name="borrowDueTime" id="borrowDueTime" class="form-control input-sm" value="<c:out value="${borrowForm.borrowDueTime }"></c:out>" maxlength="50" />
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											发行人
										</label> 
										<select class="form-control input-sm" name="borrowPublisher" id="borrowPublisher" data-ignore="true">
																		<option value="" 
																		<c:if test="${empty borrowForm.borrowPublisher or borrowForm.borrowPublisher eq ''}">
																		selected="selected"</c:if>>
																		</option>
																		<option value="嘉诺" 
																		<c:if test="${not empty borrowForm.borrowPublisher and borrowForm.borrowPublisher eq '嘉诺'}">
																		selected="selected"</c:if>>嘉诺</option>
																		<option value="中商储"
																		<c:if test="${not empty borrowForm.borrowPublisher and borrowForm.borrowPublisher eq '中商储'}">
																		selected="selected"</c:if>>中商储</option>
																	</select>
									</div>									
									<div class="form-group">
										<label class="control-label">
											协议期数
										</label>
										<c:if test="${empty borrowForm.contractPeriod or borrowForm.contractPeriod eq 'null'}">
											<input type="text" name="contractPeriod" id="contractPeriod" class="form-control input-sm" value="" maxlength="10" datatype="n1-10" ignore="ignore" />
										</c:if>
										<c:if test="${not empty borrowForm.contractPeriod and borrowForm.contractPeriod ne 'null'}">
											<input type="text" name="contractPeriod" id="contractPeriod" class="form-control input-sm" value="${borrowForm.contractPeriod }" maxlength="10" datatype="n1-10" ignore="ignore" />
										</c:if>
									</div>
									<div class="form-group">
										<label class="control-label">
											借款用途<span class="symbol required"></span>
										</label>
										<%--20180724 借款用途必填--%>
										<%--<input type="text" name="financePurpose" id="financePurpose" class="form-control input-sm" value='<c:out value="${borrowForm.financePurpose}"></c:out>'--%>
											   <%--maxlength="30" datatype="*1-30" nullmsg="请填写借款用途"  errormsg="请填写借款用途长度不能超过30位字符" />--%>
										<select id="financePurpose" name="financePurpose" class="form-select2" style="width:100%" id="sel-hkfs"
												data-placeholder="请选借款用途..." data-allow-clear="true" datatype="*"  nullmsg="未指定借款用途">
											<option value=""></option>
											<c:forEach items="${financePurposeList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowForm.financePurpose}">selected="selected"</c:if>> <c:out value="${record.name }"></c:out></option>
											</c:forEach>
										</select>
										<hyjf:validmessage key="financePurpose"></hyjf:validmessage>
									</div>
									<div class="form-group">
										<label class="control-label">
											月薪收入
										</label>
										<input type="text" name="monthlyIncome" id="monthlyIncome" class="form-control input-sm" value="<c:out value="${borrowForm.monthlyIncome }"></c:out>" maxlength="30"  />
									</div>
									<div class="form-group">
										<label class="control-label">
											还款来源
										</label>
										<input type="text" name="payment" id="payment" class="form-control input-sm" value="<c:out value="${borrowForm.payment }"></c:out>" maxlength="30"  />
									</div>									
									<div class="form-group">
										<label class="control-label">
											第一还款来源
										</label>
										<input type="text" name="firstPayment" id="firstPayment" class="form-control input-sm" value="<c:out value="${borrowForm.firstPayment }"></c:out>" maxlength="30"  />
									</div>									

									<div class="form-group">
										<label class="control-label">
											第二还款来源
										</label>
										<input type="text" name="secondPayment" id="secondPayment" class="form-control input-sm" value="<c:out value="${borrowForm.secondPayment }"></c:out>" maxlength="30"  />
									</div>									
									<div class="form-group">
										<label class="control-label">
											费用说明
										</label>
										<input type="text" name="costIntrodution" id="costIntrodution" class="form-control input-sm" value="<c:out value="${borrowForm.costIntrodution }"></c:out>" maxlength="30"  />
									</div>									

									<div class="form-group">
										<label class="control-label">
											保障方式
										</label>
										<textarea placeholder="请填写保障方式..."  maxlength="500" id="borrowSafeguardWay" name="borrowSafeguardWay" class="form-control" rows="5"><c:out value="${borrowForm.borrowSafeguardWay }"></c:out></textarea>
									</div>
									<div class="form-group">
										<label class="control-label">
											收益说明
										</label>
										<textarea placeholder="请填写收益说明..."  maxlength="500" id="borrowIncomeDescription" name="borrowIncomeDescription" class="form-control" rows="5"><c:out value="${borrowForm.borrowIncomeDescription }"></c:out></textarea>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label">
									项目信息
								</label>
								<textarea  placeholder="请填写项目信息..." name="borrowContents" id="borrowContents" class="form-control" cols="10" rows="12" ><c:out value="${borrowForm.borrowContents }"></c:out></textarea>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label">
									财务状况
								</label>
								<textarea  placeholder="请填写财务状况..." name="fianceCondition" id="fianceCondition" class="form-control" cols="10" rows="12" ><c:out value="${borrowForm.fianceCondition }"></c:out></textarea>
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
				
				<!-- Start:抵/质押信息 -->
				<div class="tab-pane fade" id="tab_dyxx_4">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label class="control-label margin-right-20">
									资产属性
								</label>
								<div class="radio clip-radio radio-primary radio-inline">
									<input type="radio" id="diya" name="assetAttributes" value="1" <c:if test="${borrowForm.assetAttributes eq '1'or empty borrowForm.assetAttributes or borrowForm.assetAttributes eq '0' }">checked='checked'</c:if>>
									<label for="diya">抵押标</label>
									<input type="radio" id="zhiya" name="assetAttributes" value="2" <c:if test="${borrowForm.assetAttributes eq '2'}">checked='checked'</c:if>>
									<label for="zhiya">质押标</label>
									<input type="radio" id="xinyong" name="assetAttributes" value="3" <c:if test="${borrowForm.assetAttributes eq '3'}">checked='checked'</c:if>>
									<label for="xinyong">信用标</label>
								</div>
							</div>
							<div id="dywlx" class="form-group">
								<label class="control-label margin-right-20">
									抵/质押物类型
								</label>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" name="typeCar" id="checkboxCLDY" value="2" class="dyxxCheckbox"
												data-panel="#cldyListPanel" <c:if test="${borrowForm.type eq '2' or borrowForm.type eq '3' or empty borrowForm.type }">checked</c:if> />
										<label for="checkboxCLDY">车辆抵押</label>
									</div>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" name="typeHouse" id="checkboxFCDY" value="1" class="dyxxCheckbox"
												data-panel="#fcdyListPanel" <c:if test="${borrowForm.type eq '1' or borrowForm.type eq '3' or empty borrowForm.type  }">checked</c:if> />
										<label for="checkboxFCDY">房产抵押</label>
									</div>
							</div>
							<!-- Start:Panel -->
							<div id="cldyListPanel" class="panel panel-white" ${(borrowForm.type eq '2' or borrowForm.type eq '3' or empty borrowForm.type) ? '' : 'style="display:none;"'}>
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 车辆抵/质押详情</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="cldyxxRowCounts" class="value">
													<c:if test="${empty borrowForm.borrowCarinfoList}">1</c:if>
													<c:if test="${!empty borrowForm.borrowCarinfoList}"><c:out value="${fn:length ( borrowForm.borrowCarinfoList ) }"></c:out></c:if>
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
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
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
														<th class="center">是否有保险</th>
														<th class="center">评估价（元）</th>
														<th class="center">车牌号</th>
														<th class="center">车辆登记地</th>
														<th class="center">车架号</th>
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
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="7%"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty borrowForm.borrowCarinfoList}">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="brand" id="brand" class="form-control input-sm" value="" maxlength="40" datatype="*1-40" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="model" id="model" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="carseries" id="carseries" class="form-control input-sm" value="" maxlength="50" datatype="*1-50" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="color" id="color" class="form-control input-sm" value="" maxlength="16" datatype="*1-16" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="year" id="year" class="form-control input-sm" value="" maxlength="12" datatype="*1-12" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="place" id="place" class="form-control input-sm" value="" maxlength="60" datatype="*1-60" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="input-group">
																	<input type="text" name="buytime" id="buytime" class="form-control input-sm datepicker" value="" maxlength="10" datatype="d" ignore="ignore" />
																	<span class="input-group-addon"><i class="fa fa-calendar"></i></span>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="price" id="price" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore" />
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
																	<input type="text" name="toprice" id="toprice" class="form-control input-sm" value="" maxlength="13" datatype="n1-13" ignore="ignore" />
																</div>
															</td>

															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="number" id="number" class="form-control input-sm" value="" maxlength="10" datatype="*1-10" ignore="ignore" />
																</div>
															</td>
															
														    <td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="registration" class="form-control input-sm" value="" maxlength="15" datatype="*1-15" ignore="ignore" />
																</div>
															</td>
															
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="vin" class="form-control input-sm" value="" maxlength="30" datatype="*1-30" ignore="ignore" />
																</div>
															</td>
															<td class="center">
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${borrowForm.borrowCarinfoList }" var="record" begin="0" step="1" varStatus="status">
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
															
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="number" class="form-control input-sm" value="<c:out value="${record.number }"></c:out>" maxlength="10" datatype="*1-10" ignore="ignore" />
																	<hyjf:validmessage key="toprice${status.index}"></hyjf:validmessage>
																</div>
															</td>

															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="registration" class="form-control input-sm" value="<c:out value="${record.registration }"></c:out>" maxlength="15" datatype="*1-15" ignore="ignore" />
																	<hyjf:validmessage key="registration${status.index}"></hyjf:validmessage>
																</div>
															</td>
															
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="vin" class="form-control input-sm" value="<c:out value="${record.vin }"></c:out>" maxlength="30" datatype="*1-30" ignore="ignore" />
																	<hyjf:validmessage key="vin${status.index}"></hyjf:validmessage>
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
							<div id="fcdyListPanel" class="panel panel-white" ${(borrowForm.type eq '1' or borrowForm.type eq '3' or empty borrowForm.type) ? '' : 'style="display:none"' }>
								<div class="panel-heading border-light">
									<h4 class="panel-title text-primary"><i class="fa fa-list-ul"></i> 房产抵押详情</h4>
									<ul class="panel-heading-tabs border-light">
										<li>
											<div class="rate" data-original-title="当前的记录数" data-toggle="tooltip" data-placement="top">
												<i class="fa fa-indent text-primary margin-right-10"></i>
												<span id="fcdyxxRowCounts" class="value">
													<c:if test="${empty borrowForm.borrowHousesList}">1</c:if>
													<c:if test="${!empty borrowForm.borrowHousesList}"><c:out value="${fn:length ( borrowForm.borrowHousesList ) }"></c:out></c:if>
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
													<col width="10%"></col>
													<col width="10%"></col>
													<col width="10%"></col>
													<col width="5%"></col>
												</colgroup>
												<thead>
													<tr>
														<th class="center">房产类型 <span class="symbol required"></span></th>
														<th class="center">房产位置</th>
														<th class="center">建筑面积 <span class="symbol required"></span></th>
														<th class="center">市值 </th>
														<th class="center">资产数量</th>
														<th class="center">评估价值（元） </th>
														<th class="center">资产所属</th>
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
													<col width="10%"></col>
													<col width="10%"></col>
													<col width="10%"></col>
													<col width="5%"></col>
												</colgroup>
												<tbody>
													<c:if test="${empty borrowForm.borrowHousesList}">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="housesType" id="housesType">
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
																	<input type="text" name="housesArea" id="housesArea" class="form-control input-sm" value="" maxlength="255"
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
																	<input type="text" name="housesCnt" id="housesCnt" class="form-control input-sm" value="" maxlength="3" datatype="n1-3" ignore="ignore" />
																</div>
															</td>
															
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesToprice" id="housesToprice" class="form-control input-sm" value="" maxlength="12" datatype="n1-12" ignore="ignore" />
																</div>
															</td>

															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesBelong" id="housesBelong" class="form-control input-sm" value="" maxlength="20" datatype="*1-20" ignore="ignore" />
																</div>
															</td>
															
															<td class="center">	
																<a class="btn btn-transparent btn-xs fn-addRow" data-toggle="tooltip" data-placement="left" data-original-title="添加"><i class="fa fa-plus"></i></a>
																<a class="btn btn-transparent btn-xs fn-removeRowTop" data-toggle="tooltip" data-placement="left" data-original-title="删除"><i class="fa fa-trash-o"></i></a>
															</td>
														</tr>
													</c:if>
													<c:forEach items="${borrowForm.borrowHousesList }" var="record" begin="0" step="1" varStatus="status">
														<tr>
															<td class="vertical-align-top">
																<div class="form-group">
																	<select class="form-control input-sm" name="housesType" id="housesType">
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
																	<input type="text" name="housesCnt" class="form-control input-sm" value="<c:out value="${record.housesCnt }"></c:out>" maxlength="5" datatype="n1-5" ignore="ignore" />
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesToprice" class="form-control input-sm" value="<c:out value="${record.housesToprice }"></c:out>" maxlength="20" datatype="n1-20" ignore="ignore"/>
																	<hyjf:validmessage key="housesToprice${status.index}"></hyjf:validmessage>
																</div>
															</td>
															<td class="vertical-align-top">
																<div class="form-group">
																	<input type="text" name="housesBelong" class="form-control input-sm" value="<c:out value="${record.housesBelong }"></c:out>" maxlength="20" datatype="*1-20" ignore="ignore"/>
																	<hyjf:validmessage key="housesBelong${status.index}"></hyjf:validmessage>
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
								<textarea placeholder="请填写相关内容..." id="borrowMeasuresMort" name="borrowMeasuresMort" class="form-control" rows="5"><c:out value="${borrowForm.borrowMeasuresMort }"></c:out></textarea>
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
											<select id="borrowMeasuresInstit" name="borrowMeasuresInstit" class="form-select2" data-allow-clear="true" style="width:70% " data-placeholder="请选择项合作机构...">
												<option value="" ></option>
												<c:forEach items="${links }" var="record" begin="0" step="1" varStatus="status">
													<option data-summary="${record.summary2 }" data-operatingprocess="${record.operatingProcess }" data-measures="${record.controlMeasures }" value="${record.webname }" <c:if test="${record.webname eq borrowForm.borrowMeasuresInstit}">selected="selected"</c:if>> <c:out value="${record.webname }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											机构介绍
										</label>
										<textarea placeholder="请填写相关内容..." id="borrowCompanyInstruction" name="borrowCompanyInstruction" class="form-control" rows="5"><c:out value="${borrowForm.borrowCompanyInstruction }"></c:out></textarea>
									</div>
									<div class="form-group">
										<label class="control-label">
											操作流程
										</label>
										<textarea placeholder="请填写相关内容..." id="borrowOperatingProcess" name="borrowOperatingProcess" class="form-control" rows="5"><c:out value="${borrowForm.borrowOperatingProcess }"></c:out></textarea>
									</div>
									<div class="form-group">
										<label class="control-label">
											风控措施
										</label>
										<textarea  id="borrowMeasuresMea" name="borrowMeasuresMea" class="form-control" rows="5">
										<c:out value="${borrowForm.borrowMeasuresMea}"></c:out></textarea>
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
								<a href="#tab_zcxx_9" class="btn btn-primary btn-o btn-wide fn-Next">
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
								<span class="label label-inverse" id="txtJieKuanren"><c:out value="${borrowForm.username}" default=" -- "></c:out></span>
									<label class="control-label">
													信用评级:
												</label>
												<select id="borrowLevel" name="borrowLevel" class="form-select2" data-allow-clear="true" data-placeholder="暂无评分" style="width:20%" >
													<option value="">暂无评分</option>
													<c:forEach items="${levelList }" var="record" begin="0" step="1" varStatus="status">
														<option value="${record.nameCd }" <c:if test="${record.nameCd eq 'AA' and borrowForm.borrowLevel ne ''
														 and  borrowForm.borrowLevel ne 'AA' and  borrowForm.borrowLevel ne 'A'
														 and  borrowForm.borrowLevel ne 'AA+' and  borrowForm.borrowLevel ne 'AA-'
														  and  borrowForm.borrowLevel ne 'AAA' and borrowForm.borrowLevel ne 'BBB' }">selected="selected"</c:if>
														  <c:if test="${record.nameCd eq borrowForm.borrowLevel}">selected="selected"</c:if>> <c:out value="${record.nameCd  }"></c:out></option>
													</c:forEach>
												</select>
							</div>
							<div class="form-group">
								<label class="control-label margin-right-20">
									借款类型
								</label>
								<div class="radio clip-radio radio-primary radio-inline jklx">
									<input type="radio" id="jklxQYJK" name="companyOrPersonal" value="1" <c:if test="${ ( borrowForm.companyOrPersonal eq '1' ) or ( empty borrowForm.companyOrPersonal )}">checked="checked"</c:if>>
									<label for="jklxQYJK">法人</label>
									<input type="radio" id="jklxGRJK" name="companyOrPersonal" value="2" <c:if test="${borrowForm.companyOrPersonal eq '2'}">checked="checked"</c:if>>
									<label for="jklxGRJK">自然人</label>
								</div>
							</div>
							<!-- 企业借款面板 -->
							<div id="qyjkPanel">
								<div class="col-md-8">
									<div class="row">
										<div class="col-md-6">
											<div class="form-group">
											<label class="control-label">
												借款主体:<span class="symbol required"></span>
											</label>
											<input type="text" name="comName" id="comName" class="form-control input-sm" value="<c:out value="${borrowForm.comName}"></c:out>"
												   maxlength="50" datatype="*1-50" nullmsg="请填写借款主体" ajaxurl="isBorrowUserCACheck" />
											<hyjf:validmessage key="comName" label="借款主体"></hyjf:validmessage>
										</div>
										<%--20180704 添加企业组织机构代码和企业注册地 add by nxl Start--%>
										<div class="form-group">
											<label class="control-label">
												企业注册地:<span class="symbol required"></span>
											</label>
											<input type="text" name="registrationAddress" id="registrationAddress" class="form-control input-sm" value="<c:out value="${borrowForm.registrationAddress}"></c:out>"
												   maxlength="99" datatype="*1-99" nullmsg="请填写企业注册地" errormsg="企业注册地长度不能超过99位" />
											<hyjf:validmessage key="registrationAddress" label="企业注册地"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												企业组织机构代码
											</label>
											<input type="text" name="corporateCode" id="corporateCode" class="form-control input-sm" value="<c:out value="${borrowForm.corporateCode}"></c:out>"
												   maxlength="99" datatype="s1-99" errormsg="企业组织机构代码长度不能超过99位" ignore="ignore" />
											<hyjf:validmessage key="corporateCode" label="企业组织机构代码"></hyjf:validmessage>
										</div>
										<%--20180704 添加企业组织机构代码和企业注册地 add by nxl End--%>
											<div class="form-group">
												<label class="control-label">
													法人<span class="symbol required"></span>
												</label>
												<input type="text" name="comLegalPerson" id="comLegalPerson" class="form-control input-sm" value="<c:out value="${borrowForm.comLegalPerson }"></c:out>"
														maxlength="20" datatype="s1-20" errormsg="法人不可超过20位" ignore="ignore" />
												<hyjf:validmessage key="comLegalPerson" label="法人"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													注册地区<span class="symbol required"></span>
												</label>
												<input type="text" name="comLocationCity" id="comLocationCity" class="form-control input-sm"
															value="<c:out value="${borrowForm.comLocationProvince}"></c:out><c:out value="${borrowForm.comLocationCity}"></c:out>" 
															maxlength="20" datatype="*1-20" ignore="ignore" />
												<hyjf:validmessage key="comLocationCity" label="注册地区"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													公司规模
												</label>
												<select name="size" class="form-select2" style="width:100%" data-placeholder="人数">
													<option value=""></option>
													<c:forEach items="${companySizeList }" var="record" begin="0" step="1" varStatus="status">
														<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowForm.size}">selected="selected"</c:if>> <c:out value="${record.name }"></c:out></option>
													</c:forEach>
												</select>
											</div>

											<div class="form-group">
												<label class="control-label">
													主营业务
												</label>
												<input type="text" name="comMainBusiness" id="comMainBusiness" class="form-control input-sm" value="<c:out value="${borrowForm.comMainBusiness}"></c:out>"
															maxlength="30" datatype="*1-30" ignore="ignore" />
												<hyjf:validmessage key="comMainBusiness" label="主营业务"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													在平台逾期次数
												</label>
												<input type="text" name="comOverdueTimes" id="comOverdueTimes" class="form-control input-sm" value="<c:out value="${borrowForm.comOverdueTimes}"></c:out>"
															maxlength="10" datatype="n1-10" errormsg="在平台逾期次数必须为1-10位数字" ignore="ignore" />
												<hyjf:validmessage key="comOverdueTimes" label="在平台逾期次数"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													在平台逾期金额
												</label>
												<input type="text" name="comOverdueAmount" id="comOverdueAmount" class="form-control input-sm" value="<c:out value="${borrowForm.comOverdueAmount}"></c:out>"
															maxlength="15" datatype="n1-15" errormsg="在平台逾期金额必须为1-15位数字" ignore="ignore" />
												<hyjf:validmessage key="comOverdueAmount" label="在平台逾期金额"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													重大负债状况
												</label>
												<input type="text" name="comDebtSituation" id="comDebtSituation" class="form-control input-sm" value="<c:out value="${borrowForm.comDebtSituation}"></c:out>"
															maxlength="50"  errormsg="重大负债状况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comDebtSituation" label="重大负债状况"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label">
													注册时间<span class="symbol required"></span>
												</label>
												<%--<input type="text" name="comRegTime" id="comRegTime" class="form-control input-sm" value="<c:out value="${borrowForm.comRegTime }"></c:out>" --%>
														<%--maxlength="30" datatype="*1-30" errormsg="注册时间长度不能超过30位文字" ignore="ignore" />--%>
												<input type="text" name="comRegTime" id="comRegTime" class="form-control input-sm datepicker" value="<c:out value="${borrowForm.comRegTime }"></c:out>" maxlength="10" datatype="*" errormsg="请选择正确的注册时间" ignore="ignore"/>
												<hyjf:validmessage key="comRegTime" label="注册时间"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													统一社会信用代码:<span class="symbol required"></span>
												</label>
												<input type="text" name="comSocialCreditCode" id="comSocialCreditCode" class="form-control input-sm" value="<c:out value="${borrowForm.comSocialCreditCode }"></c:out>"
														maxlength="30" datatype="s1-30" errormsg="统一社会信用代码必须为数字和字母且不可超过30位" nullmsg="请填写统一社会信用代码" ajaxurl="isCAIdNoCheck" />
												<hyjf:validmessage key="comSocialCreditCode" label="统一社会信用代码"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													注册号
												</label>
												<input type="text" name="comRegistCode" id="comRegistCode" class="form-control input-sm" value="<c:out value="${borrowForm.comRegistCode }"></c:out>"
														maxlength="30" datatype="n1-30" errormsg="注册号必须为数字且不可超过30位" ignore="ignore" />
												<hyjf:validmessage key="comRegistCode" label="注册号"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													注册资本<span class="symbol required"></span>
												</label>
												<div class="input-group">
													<input type="text" name="comRegCaptial" id="comRegCaptial" class="form-control input-sm" value="<c:out value="${borrowForm.comRegCaptial }"></c:out>"
														maxlength="20" datatype="n1-20" errormsg="注册资本必须为数字且长度不能超过20位" ignore="ignore" />
													<span class="input-group-addon">元</span>
													<input type="hidden" name="currencyName" value="<c:out value="元"></c:out>" />
													<%--<select id="currencyName" name="currencyName" class="form-select2" data-allow-clear="true"  style="width:30%" data-placeholder="请选择货币类型...">--%>
														<%--<option value=""></option>--%>
														<%--<c:forEach items="${currencyList }" var="currency" begin="0" step="1" varStatus="status">--%>
															<%--<option value="${currency.name }" --%>
																<%--<c:if test="${currency.name eq borrowForm.currencyName}">selected="selected"</c:if>>--%>
																<%--<c:out value="${currency.name }"></c:out></option>--%>
														<%--</c:forEach>--%>
													<%--</select>--%>
													</div>
												    <hyjf:validmessage key="comRegCaptial" label="注册资本"></hyjf:validmessage>

											</div>
											<div class="form-group">
												<label class="control-label">
													授信额度
												</label>
												<div class="input-group">
													<input type="text" name="comCredit" id="comCredit" class="form-control input-sm" value="<c:out value="${borrowForm.comCredit }"></c:out>"
														maxlength="10" datatype="n0-10" errormsg="授信额度长度不能超过10位，且必须为数字" />
													<span class="input-group-addon">元</span>
												</div>
												<hyjf:validmessage key="comCredit" label="授信额度"></hyjf:validmessage>
											</div>

											<div class="form-group">
												<label class="control-label">
													所属行业
												</label>
												<input type="text" name="comUserIndustry" id="comUserIndustry" class="form-control input-sm" value="<c:out value="${borrowForm.comUserIndustry }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="所属行业长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comUserIndustry" label="所属行业"></hyjf:validmessage>
											</div>
											<!-- 征信报告逾期情况:暂未提供；无；已处理 -->
											<div class="form-group">
												<label class="control-label">
													征信报告逾期情况
												</label>
												<input type="text" name="comOverdueReport" id="comOverdueReport" class="form-control input-sm" value="<c:out value="${borrowForm.comOverdueReport }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="征信报告逾期情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comOverdueReport" label="征信报告逾期情况"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													其他平台借款情况
												</label>
												<input type="text" name="comOtherBorrowed" id="comOtherBorrowed" class="form-control input-sm" value="<c:out value="${borrowForm.comOtherBorrowed }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="其他平台借款情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comOtherBorrowed" label="其他平台借款情况"></hyjf:validmessage>
											</div>
										</div>
									</div>
									<div class="form-group">
										<fieldset>
											<legend>
												<strong class="no-wrap">企贷审核信息</strong>
											</legend>
												<label class="control-label">企业证件： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsCertificate" id="comIsCertificate" value="1" <c:if test="${ borrowForm.comIsCertificate ne '0' }">checked</c:if> />
													<label for="comIsCertificate">已审核</label>
												</div>

												<label class="control-label">经营状况： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsOperation" id="comIsOperation" value="1" <c:if test="${ borrowForm.comIsOperation ne '0' }">checked</c:if> />
													<label for="comIsOperation">已审核</label>
												</div>

												<label class="control-label">财务状况： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsFinance" id="comIsFinance" value="1" <c:if test="${ borrowForm.comIsFinance ne '0' }">checked</c:if> />
													<label for="comIsFinance">已审核</label>
												</div>

												<label class="control-label">企业信用： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsEnterpriseCreidt" id="comIsEnterpriseCreidt" value="1" <c:if test="${ borrowForm.comIsEnterpriseCreidt ne '0' }">checked</c:if> />
													<label for="comIsEnterpriseCreidt">已审核</label>
												</div>

												<label class="control-label">法人信息： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsLegalPerson" id="comIsLegalPerson" value="1" <c:if test="${ borrowForm.comIsLegalPerson ne '0' }">checked</c:if> />
													<label for="comIsLegalPerson">已审核</label>
												</div>

												<label class="control-label">资产状况： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsAsset" id="comIsAsset" value="1" <c:if test="${ borrowForm.comIsAsset ne '0' }">checked</c:if> />
													<label for="comIsAsset">已审核</label>
												</div>

												<label class="control-label">购销合同： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsPurchaseContract" id="comIsPurchaseContract" value="1" <c:if test="${ borrowForm.comIsPurchaseContract eq '1' }">checked</c:if> />
													<label for="comIsPurchaseContract">已审核</label>
												</div>

												<label class="control-label">供销合同： </label>
												<div class="checkbox clip-check check-info checkbox-inline">
													<input type="checkbox" name="comIsSupplyContract" id="comIsSupplyContract" value="1" <c:if test="${ borrowForm.comIsSupplyContract eq '1' }">checked</c:if> />
													<label for="comIsSupplyContract">已审核</label>
												</div>
										</fieldset>
									</div>
									<div class="row">
										<div class="col-md-6">

											<div class="form-group">
												<label class="control-label">
													借款资金运用情况
												</label>
												<input type="text" name="comIsFunds" id="comIsFunds" class="form-control input-sm" value="<c:out value="${borrowForm.comIsFunds}"></c:out>"
															maxlength="50"  errormsg="借款资金运用情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsFunds" label="借款资金运用情况"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													借款方还款能力变化情况
												</label>
												<input type="text" name="comIsAbility" id="comIsAbility" class="form-control input-sm" value="<c:out value="${borrowForm.comIsAbility}"></c:out>"
															maxlength="50"  errormsg="借款人还款能力变化情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsAbility" label="借款人还款能力变化情况"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													借款方涉诉情况
												</label>
												<input type="text" name="comIsComplaint" id="comIsComplaint" class="form-control input-sm" value="<c:out value="${borrowForm.comIsComplaint}"></c:out>"
															maxlength="50"  errormsg="借款人涉诉情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsComplaint" label="借款人涉诉情况"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-md-6">
											<div class="form-group">
												<label class="control-label">
													借款方经营状况及财务状况
												</label>
												<input type="text" name="comIsManaged" id="comIsManaged" class="form-control input-sm" value="<c:out value="${borrowForm.comIsManaged }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="借款人经营状况及财务状况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsManaged" label="借款人经营状况及财务状况"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													借款方逾期情况
												</label>
												<input type="text" name="comIsOverdue" id="comIsOverdue" class="form-control input-sm" value="<c:out value="${borrowForm.comIsOverdue }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="借款人逾期情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsOverdue" label="借款人逾期情况"></hyjf:validmessage>
											</div>
											<div class="form-group">
												<label class="control-label">
													借款方受行政处罚情况
												</label>
												<input type="text" name="comIsPunished" id="comIsPunished" class="form-control input-sm" value="<c:out value="${borrowForm.comIsPunished }"></c:out>"
																maxlength="100" datatype="*1-100" errormsg="借款人受行政处罚情况长度不能超过100位" ignore="ignore" />
												<hyjf:validmessage key="comIsPunished" label="借款人受行政处罚情况"></hyjf:validmessage>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="control-label">
											涉诉情况
										</label>
										<textarea placeholder="请填写相关内容..." id="comLitigation" name="comLitigation" class="form-control" rows="4"
												maxlength="100" datatype="*1-100" errormsg="涉诉情况长度不能超过100位" ignore="ignore" ><c:out value="${borrowForm.comLitigation }"></c:out></textarea>
										<hyjf:validmessage key="comLitigation" label="请填写相关内容"></hyjf:validmessage>
									</div>
									<div class="form-group">
										<label class="control-label">
											征信记录
										</label>
										<textarea placeholder="请填写相关内容..." id="comCreReport" name="comCreReport" class="form-control" rows="4"
												maxlength="100" datatype="*1-100" errormsg="征信记录长度不能超过100位" ignore="ignore" ><c:out value="${borrowForm.comCreReport }"></c:out></textarea>
										<hyjf:validmessage key="comCreReport" label="征信记录"></hyjf:validmessage>
									</div>
								</div>
								<div class="col-md-12">
									<label class="control-label">
										财务情况
									</label>
									<div class="form-group">
										<textarea name="accountContents" class="tinymce" cols="10" rows="12"><c:out value="${borrowForm.accountContents }"></c:out></textarea>
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
												姓名:<span class="symbol required"></span>
											</label>
											<input type="text" name="manname" id="manname" class="form-control input-sm" value="<c:out value="${borrowForm.manname }"></c:out>"
													maxlength="50" datatype="*1-50" errormsg="姓名长度不能超过50位" nullmsg="请填写姓名" ajaxurl="isBorrowUserCACheck" />
											<hyjf:validmessage key="manname" label="姓名"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												身份证号:<span class="symbol required"></span>
											</label>
											<input type="text" name="cardNo" id="cardNo" class="form-control input-sm" value="<c:out value="${borrowForm.cardNo }"></c:out>"
													maxlength="19" datatype="*1-19" errormsg="身份证号长度不能超过19位" nullmsg ="请填写身份证号" ajaxurl="isCAIdNoCheck" />
											<hyjf:validmessage key="cardNo" label="身份证号"></hyjf:validmessage>
										</div>
										<%--20180703 添加借款人地址 add by nxl--%>
										<div class="form-group">
											<label class="control-label">
												借款人地址:<span class="symbol required"></span>
											</label>
											<input type="text" name="address" id="address" class="form-control input-sm" value="<c:out value="${borrowForm.address}"></c:out>"
												   maxlength="99" datatype="*1-99" errormsg="借款人地址长度不能超过99位" nullmsg ="请填写借款人地址"/>
											<hyjf:validmessage key="address" label="借款人地址"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												年龄
											</label>
											<input type="text" name="old" id="old" class="form-control input-sm" value="<c:out value="${borrowForm.old }"></c:out>"
													maxlength="3" datatype="n1-3" errormsg="年龄长度不能超过3位，且必须为数字" ignore="ignore" />
											<hyjf:validmessage key="old" label="年龄"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												职业类型<span class="symbol required"></span>
											</label>
											<%--<input type="text" name="position" id="position" class="form-control input-sm" value="<c:out value="${borrowForm.position }"></c:out>" --%>
													<%--maxlength="20" datatype="s1-20" errormsg="岗位职业长度不能超过20位" ignore="ignore" />--%>
											<select name="position" id="position" class="form-select2" style="width:100%"
													data-placeholder="请选职业类型..." data-allow-clear="true" datatype="*"  nullmsg="未指定岗位职业" ignore="ignore">
												<option value=""></option>
												<c:forEach items="${positionList }" var="record" begin="0" step="1" varStatus="status">
													<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowForm.position}">selected="selected"</c:if>> <c:out value="${record.name }"></c:out></option>
												</c:forEach>
											</select>
											<hyjf:validmessage key="position" label="岗位职业"></hyjf:validmessage>
										</div>
										<div class="row">
											<div class="col-sm-6">
												<div class="form-group">
													<label class="control-label margin-bottom-0">
														性别
													</label>
													<div class="radio clip-radio radio-primary">
														<input type="radio" id="jkgrMan" name="sex" value="1" <c:if test="${borrowForm.sex eq '1' or empty borrowForm.sex}">checked='checked'</c:if>>
														<label for="jkgrMan">男</label>
														<input type="radio" id="jkgrWon" name="sex" value="2" <c:if test="${borrowForm.sex eq '2'}">checked='checked'</c:if>>
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
														<input type="radio" id="jkgrYihun" name="merry" value="1" <c:if test="${borrowForm.merry eq '1'}">checked='checked'</c:if>>
														<label for="jkgrYihun">已婚</label>
														<input type="radio" id="jkgrWeihun" name="merry" value="2" <c:if test="${borrowForm.merry eq '2' or empty borrowForm.merry}">checked='checked'</c:if>>
														<label for="jkgrWeihun">未婚</label>
														<!-- new added by libin -->
														<input type="radio" id="jkgrLiyi" name="merry" value="3" <c:if test="${borrowForm.merry eq '3'}">checked='checked'</c:if>>
														<label for="jkgrLiyi">离异</label>
														<input type="radio" id="jkgrSangou" name="merry" value="4" <c:if test="${borrowForm.merry eq '4' }">checked='checked'</c:if>>
														<label for="jkgrSangou">丧偶</label>
														<!-- new added by libin -->
													</div>
												</div>
											</div>
										</div>
										<div class="form-group">
											<label class="control-label">
												工作城市
											</label>
											<input type="text" name="location_c" id="location_c" class="form-control input-sm"
											 value="<c:out value="${borrowForm.location_p }"></c:out><c:out value="${borrowForm.location_c }"></c:out>"
													maxlength="15" datatype="*1-20" errormsg="工作城市长度不能超过15位" ignore="ignore" />
											<hyjf:validmessage key="location_p" label="工作城市"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												户籍地
											</label>
											<input type="text" name="domicile" id="domicile" class="form-control input-sm" value="<c:out value="${borrowForm.domicile }"></c:out>"
													maxlength="15" datatype="*1-15" errormsg="户籍地长度不能超过15位" ignore="ignore" />
											<hyjf:validmessage key="domicile" label="户籍地"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												在平台逾期次数
											</label>
											<input type="text" name="overdueTimes" id="overdueTimes" class="form-control input-sm" value="<c:out value="${borrowForm.overdueTimes }"></c:out>"
													maxlength="10" datatype="n1-10" errormsg="在平台逾期次数必须为1-10位数字" ignore="ignore" />
											<hyjf:validmessage key="overdueTimes" label="在平台逾期次数"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												在平台逾期金额
											</label>
											<input type="text" name="overdueAmount" id="overdueAmount" class="form-control input-sm" value="<c:out value="${borrowForm.overdueAmount }"></c:out>"
													maxlength="15" datatype="n1-15" errormsg="在平台逾期金额必须为1-15位数字" ignore="ignore" />
											<hyjf:validmessage key="overdueAmount" label="在平台逾期金额"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												涉诉情况
											</label>
											<input type="text" name="litigation" id="litigation" class="form-control input-sm" value="<c:out value="${borrowForm.litigation }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="涉诉情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="litigation" label="涉诉情况"></hyjf:validmessage>
										</div>

										<div class="form-group">
											<label class="control-label">
												行业
											</label>
											<input type="text" name="industry" id="industry" class="form-control input-sm" value="<c:out value="${borrowForm.industry }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="行业长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="industry" label="行业"></hyjf:validmessage>
										</div>

										<div class="form-group">
											<label class="control-label">
												授信额度
											</label>
											<div class="input-group">
												<input type="text" name="userCredit" id="userCredit" class="form-control input-sm" value="<c:out value="${borrowForm.userCredit }"></c:out>"
													maxlength="10" datatype="n0-10" errormsg="授信额度长度不能超过10位，且必须为数字" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="userCredit" label="授信额度"></hyjf:validmessage>
										</div>
										<!-- 信批需求(个人)新增 start ：非必填 -->
										<!-- 个人年收入:10万以内；10万以上 -->
										<div class="form-group">
											<label class="control-label">
												年收入<span class="symbol required"></span>
											</label>
											<input type="text" name="annualIncome" id="annualIncome" class="form-control input-sm" value="<c:out value="${borrowForm.annualIncome }"></c:out>" 
													maxlength="100" datatype="n1-100" errormsg="年收入必须是数字且不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="annualIncome" label="年收入"></hyjf:validmessage>
										</div>

										<!-- 征信报告逾期情况:暂未提供；无；已处理 -->
										<div class="form-group">
											<label class="control-label">
												征信报告逾期情况
											</label>
											<input type="text" name="overdueReport" id="overdueReport" class="form-control input-sm" value="<c:out value="${borrowForm.overdueReport }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="征信报告逾期情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="overdueReport" label="征信报告逾期情况"></hyjf:validmessage>
										</div>

										<!-- 重大负债状况:无 -->
										<div class="form-group">
											<label class="control-label">
												重大负债状况
											</label>
											<input type="text" name="debtSituation" id="debtSituation" class="form-control input-sm" value="<c:out value="${borrowForm.debtSituation }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="重大负债状况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="debtSituation" label="重大负债状况"></hyjf:validmessage>
										</div>

										<!-- 其他平台借款情况:无 -->
										<div class="form-group">
											<label class="control-label">
												其他平台借款情况
											</label>
											<input type="text" name="otherBorrowed" id="otherBorrowed" class="form-control input-sm" value="<c:out value="${borrowForm.otherBorrowed }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="其他平台借款情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="otherBorrowed" label="其他平台借款情况"></hyjf:validmessage>
										</div>
										<!-- 信批需求(个人)新增 end ：非必填 -->
										<div class="form-group">
											<fieldset>
												<legend>
													<strong class="no-wrap">个贷审核信息</strong>
												</legend>
													<label class="control-label"> 身份证： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isCard" id="isCard" value="1" <c:if test="${ borrowForm.isCard ne '0' }">checked</c:if> />
														<label for="isCard">已审核</label>
													</div>
													<label class="control-label">收入状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isIncome" id="isIncome" value="1" <c:if test="${ borrowForm.isIncome ne '0' }">checked</c:if> />
														<label for="isIncome">已审核</label>
													</div>

													<label class="control-label">信用状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isCredit" id="isCredit" value="1" <c:if test="${ borrowForm.isCredit ne '0' }">checked</c:if> />
														<label for="isCredit">已审核</label>
													</div>

													<label class="control-label">资产状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isAsset" id="isAsset" value="1" <c:if test="${ borrowForm.isAsset ne '0' }">checked</c:if> />
														<label for="isAsset">已审核</label>
													</div>

													<label class="control-label">车辆状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isVehicle" id="isVehicle" value="1" <c:if test="${ borrowForm.isVehicle ne '0' }">checked</c:if> />
														<label for="isVehicle">已审核</label>
													</div>

													<label class="control-label">行驶证： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isDrivingLicense" id="isDrivingLicense" value="1" <c:if test="${ borrowForm.isDrivingLicense ne '0' }">checked</c:if> />
														<label for="isDrivingLicense">已审核</label>
													</div>

													<label class="control-label">车辆登记证： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isVehicleRegistration" id="isVehicleRegistration" value="1" <c:if test="${ borrowForm.isVehicleRegistration ne '0' }">checked</c:if> />
														<label for="isVehicleRegistration">已审核</label>
													</div>

													<label class="control-label">婚姻状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isMerry" id="isMerry" value="1" <c:if test="${ borrowForm.isMerry ne '0' }">checked</c:if> />
														<label for="isMerry">已审核</label>
													</div>

													<label class="control-label">工作状况： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isWork" id="isWork" value="1" <c:if test="${ borrowForm.isWork ne '0' }">checked</c:if> />
														<label for="isWork">已审核</label>
													</div>

													<label class="control-label">户口本： </label>
													<div class="checkbox clip-check check-info checkbox-inline">
														<input type="checkbox" name="isAccountBook" id="isAccountBook" value="1" <c:if test="${ borrowForm.isAccountBook ne '0' }">checked</c:if> />
														<label for="isAccountBook">已审核</label>
													</div>
											</fieldset>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款资金运用情况
											</label>
											<input type="text" name="isFunds" id="isFunds" class="form-control input-sm" value="<c:out value="${borrowForm.isFunds }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款资金运用情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isFunds" label="借款资金运用情况"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款方经营状况及财务状况
											</label>
											<input type="text" name="isManaged" id="isManaged" class="form-control input-sm" value="<c:out value="${borrowForm.isManaged }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款人经营状况及财务状况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isManaged" label="借款人经营状况及财务状况"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款方还款能力变化情况
											</label>
											<input type="text" name="isAbility" id="isAbility" class="form-control input-sm" value="<c:out value="${borrowForm.isAbility }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款人还款能力变化情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isAbility" label="借款人还款能力变化情况"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款方逾期情况
											</label>
											<input type="text" name="isOverdue" id="isOverdue" class="form-control input-sm" value="<c:out value="${borrowForm.isOverdue }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款人逾期情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isOverdue" label="借款人逾期情况"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款方涉诉情况
											</label>
											<input type="text" name="isComplaint" id="isComplaint" class="form-control input-sm" value="<c:out value="${borrowForm.isComplaint }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款人涉诉情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isComplaint" label="借款人涉诉情况"></hyjf:validmessage>
										</div>
										<div class="form-group">
											<label class="control-label">
												借款方受行政处罚情况
											</label>
											<input type="text" name="isPunished" id="isPunished" class="form-control input-sm" value="<c:out value="${borrowForm.isPunished }"></c:out>"
													maxlength="100" datatype="*1-100" errormsg="借款人受行政处罚情况长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="isPunished" label="借款人受行政处罚情况"></hyjf:validmessage>
										</div>
										<!-- 信批需求(个人)新增 end ：默认勾选 -->
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
								<a href="#tab_czya_10" class="btn btn-primary btn-o btn-wide fn-Next">
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
													<c:if test="${empty borrowForm.borrowCommonCompanyAuthenList}">1</c:if>
													<c:if test="${!empty borrowForm.borrowCommonCompanyAuthenList}"><c:out value="${fn:length ( borrowForm.borrowCommonCompanyAuthenList ) }"></c:out></c:if>
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
													<c:if test="${empty borrowForm.borrowCommonCompanyAuthenList}">
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
													<c:forEach items="${borrowForm.borrowCommonCompanyAuthenList }" var="record" begin="0" step="1" varStatus="status">
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
										<input type="checkbox" id="tzptCheckAll" value="all" <c:if test="${ (empty borrowForm.borrowNid) or ( borrowForm.canTransactionPc eq '1' and borrowForm.canTransactionWei eq '1' and borrowForm.canTransactionIos eq '1' and borrowForm.canTransactionAndroid eq '1' ) }">checked</c:if> />
										<label for="tzptCheckAll">全部</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionPc" id="checkboxPC" value="1" class="tzptCheckbox" <c:if test="${ (empty borrowForm.borrowNid) or ( borrowForm.canTransactionPc eq '1' ) }">checked</c:if> />
										<label for="checkboxPC">PC</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionWei" id="checkboxWGW" value="1" class="tzptCheckbox" <c:if test="${ (empty borrowForm.borrowNid) or ( borrowForm.canTransactionWei eq '1' ) }">checked</c:if> />
										<label for="checkboxWGW">微官网</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionIos" id="checkboxIOS" value="1" class="tzptCheckbox" <c:if test="${ (empty borrowForm.borrowNid) or ( borrowForm.canTransactionIos eq '1' ) }">checked</c:if> />
										<label for="checkboxIOS">IOS</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="canTransactionAndroid" id="checkboxAndroid" value="1" class="tzptCheckbox" <c:if test="${ (empty borrowForm.borrowNid) or ( borrowForm.canTransactionAndroid eq '1' ) }">checked</c:if> />
										<label for="checkboxAndroid">Android</label>
									</div>
							</fieldset>

							<fieldset style="display: none">
								<legend>
									<strong class="no-wrap">定向发标</strong>
								</legend>
								<select name="publishInstCode" class="form-select2" style="width:100%" data-allow-clear="true" data-placeholder="全部">
									<option value="0">全部</option>
									<c:forEach items="${instConfigList }" var="instConfig" begin="0" step="1" varStatus="status">
										<option value="${instConfig.instCode }" <c:if test="${instConfig.instCode eq borrowForm.publishInstCode}">selected="selected"</c:if> >
											<c:out value="${instConfig.instName }"></c:out></option>
									</c:forEach>
								</select>
							</fieldset>


							<fieldset style="display: none">
								<legend>
									<strong class="no-wrap">是否用券</strong>
								</legend>
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" id="checkAllUse" value="all"  <c:if test="${ (borrowForm.borrowInterestCoupon eq '1') and (borrowForm.borrowTasteMoney eq '1') }">checked</c:if> />
										<label for="checkAllUse">全部</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="borrowInterestCoupon" id="borrowInterestCoupon" value="1" class="checkAllUsebox" <c:if test="${ borrowForm.borrowInterestCoupon eq '1' }">checked</c:if> />
										<label for="borrowInterestCoupon">加息券</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="borrowTasteMoney" id="borrowTasteMoney" value="1" class="checkAllUsebox"  <c:if test="${ borrowForm.borrowTasteMoney eq '1' }">checked</c:if> />
										<label for="borrowTasteMoney">体验金</label>
									</div>
							</fieldset>


							<fieldset>
								<legend>
									<strong class="no-wrap">费率相关</strong>
								</legend>
								<div class="row">
									<%--<div class="col-md-6">--%>
										<%--<label class="col-sm-9 control-label">放款服务费率</label>--%>
										<%--<label class="col-sm-3 control-label">--%>
											<%--<span class="badge badge-success" id="borrowServiceScale"><c:out value="${borrowForm.borrowServiceScale }" default="--"></c:out></span>--%>
										<%--</label>--%>
										<%--<hyjf:validmessage key="borrowServiceScale" label="放款服务费率"></hyjf:validmessage>--%>
									<%--</div>
									--%>
									<div class="col-md-6">
										<label class="col-sm-9 control-label">还款服务费率</label>
										<label class="col-sm-3 control-label">
											<span class="badge badge-inverse" id="borrowManagerScale"><c:out value="${borrowForm.borrowManagerScale }" default="--"></c:out> </span>
										</label>
										<hyjf:validmessage key="borrowManagerScale" label="还款服务费率"></hyjf:validmessage>
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
												最低投标金额 <span class="symbol required"></span>
											</label>
											<div class="input-group">
												<span class="input-group-addon">￥</span>
												<input type="text" name="tenderAccountMin" id="tenderAccountMin" class="form-control input-sm" value="${borrowForm.tenderAccountMin}"
														datatype="n1-10" errormsg="最低投标金额长度不能超过10位，且必须为数字" required="required" nullmsg="未填写最低投标金额" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="tenderAccountMin" label="最低投标金额"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label>
												有效时间<span class="symbol required"></span>
											</label>
											<div class="input-group">
												<input type="text" name="borrowValidTime" class="form-control input-sm" value="<c:out value="${borrowForm.borrowValidTime }"></c:out>"
													<c:if test="${ ( isEdit eq '1' ) }">
														readonly="readonly"
													</c:if>
													datatype="n1-2" errormsg="有效时间长度不能超过2位，且必须为数字" required="required" nullmsg="未填写有效时间"  />
												<span class="input-group-addon">天</span>
											</div>
											<hyjf:validmessage key="borrowValidTime" label="有效时间"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label>
												银行备案时间
											</label>
											<div class="input-group">
												<input type="text" name="bankRegistDays" class="form-control input-sm" value="<c:out value="${borrowForm.bankRegistDays }"></c:out>"
													<c:if test="${ ( isEdit eq '1' ) }">
													readonly="readonly"
													</c:if>
													datatype="n1-2" errormsg="银行时间长度不能超过2位，且必须为数字" ignore="ignore" />
												<span class="input-group-addon">天</span>
											</div>
											<hyjf:validmessage key="bankRegistDays" label="银行备案时间"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label>
												最高投标金额<span class="symbol required"></span>
											</label>
											<div class="input-group">
												<span class="input-group-addon">￥</span>
												<input type="text" name="tenderAccountMax" id="tenderAccountMax" class="form-control input-sm" value="${borrowForm.tenderAccountMax}"
														datatype="n1-10" errormsg="最高投标金额长度不能超过10位，且必须为数字" required="required" nullmsg="未填写最高投标金额" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="tenderAccountMax" label="最高投标金额"></hyjf:validmessage>
											<hyjf:validmessage key="tenderAccount" label=""></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">

									<div class="form-group">
											<label>
												递增金额<span class="symbol required"></span>
											</label>
											<div class="input-group">
												<span class="input-group-addon">￥</span>
												<input type="text" name="borrowIncreaseMoney" id="borrowIncreaseMoney" class="form-control input-sm" value="${borrowForm.borrowIncreaseMoney}"
														datatype="n1-10" errormsg="递增金额长度不能超过10位，且必须为数字" required="required" nullmsg="未填写递增出借金额" />
												<span class="input-group-addon">元</span>
											</div>
											<hyjf:validmessage key="borrowIncreaseMoney" label="递增金额"></hyjf:validmessage>
										</div>
									</div>
								</div>

								<div class="row">
									<div class="col-md-6">
										<div class="form-group">
											<label>
												产品加息收益率
											</label>
											<div class="input-group">
												<input type="text" id="borrowExtraYield" name="borrowExtraYield" class="form-control input-sm" maxlength="5"
														value='<c:out value="${borrowForm.borrowExtraYield}"></c:out>' <c:if test="${ ( isEdit eq '1' ) or (borrowForm.increaseInterestFlag eq '0') }">readonly="readonly"</c:if>
														datatype="/^\d{1,3}(\.\d{1,2})?$/,range" data-minvalue="0" data-maxvalue="30" nullmsg="未填写产品加息收益率" errormsg="产品加息收益率必须为数字，且最多2位小数，值在0~30之间" ignore="ignore" />
												<span class="input-group-addon">%</span>
											</div>
											<hyjf:validmessage key="borrowExtraYield" label="产品加息收益率"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-md-6">
										<div class="form-group">
											<label>
												建议出借者类型
											</label>
											<div class="input-group">
												<input type="hidden" id="investLevel" name="investLevel" class="form-control input-sm" value='<c:out value="${borrowForm.investLevel}"></c:out>'readonly="readonly"/>
												<label id="investLevelLabel"><c:out value="${borrowForm.investLevel}"></c:out><c:if test="${borrowForm.investLevel ne '进取型'}">及以上</c:if></label>
											</div>
											<hyjf:validmessage key="investLevel" label="建议出借者类型"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="row" style="display: none">
									<div class="col-md-6">
										<div class="form-group">
											<label class="block">
												运营标签
											</label>
											<div class="checkbox clip-check check-primary checkbox-inline">
												<input type="checkbox" id="operationLabel" name="operationLabel" value="1" <c:if test="${borrowForm.operationLabel eq '1' }">checked</c:if> />
												<label for="operationLabel">旅游标</label>
											</div>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-md-12">
										<div class="form-group">
											<label class="block">
												备注
											</label>
											<div class="form-group">
												<textarea placeholder="请填写100字以内备注..." maxlength="100" datatype="*0-100" errormsg="备注长度不能超过100位" ignore="ignore" 
													id="remark" name="remark" class="form-control" rows="5"><c:out value="${borrowForm.remark eq 'null'? '' : borrowForm.remark}"></c:out></textarea>
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
													<c:if test="${empty borrowForm.borrowCommonImageList}">1</c:if>
													<c:if test="${!empty borrowForm.borrowCommonImageList}"><c:out value="${fn:length ( borrowForm.borrowCommonImageList ) }"></c:out></c:if>
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
													<c:if test="${empty borrowForm.borrowCommonImageList}">
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
													<c:forEach items="${borrowForm.borrowCommonImageList }" var="record" begin="0" step="1" varStatus="status">
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
				
				<!-- Start:项目信息 -->
				<div class="tab-pane fade" id="tab_zcxx_9">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12 col-lg-8">
							<fieldset>
								<legend>
									<strong class="no-wrap">基础信息</strong>
								</legend>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												项目名称
											</label>
											<input type="text" name="disposalProjectName" id="disposalProjectName" class="form-control input-sm" value="<c:out value="${borrowForm.disposalProjectName }"></c:out>" 
													maxlength="100" datatype="*1-100" errormsg="项目名称长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="disposalProjectName" label="项目名称"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												项目类型
											</label>
											<input type="text" name="disposalProjectType" id="disposalProjectType" class="form-control input-sm" value="<c:out value="${borrowForm.disposalProjectType }"></c:out>" 
													maxlength="100" datatype="*1-100" errormsg="项目类型长度不能超过100位" ignore="ignore" />
											<hyjf:validmessage key="disposalProjectType" label="项目类型"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												所在地区
											</label>
											<input type="text" name="disposalArea" id="disposalArea" class="form-control input-sm" value="<c:out value="${borrowForm.disposalArea }"></c:out>" 
													maxlength="200" datatype="*1-200" errormsg="所在地区不能超过200位" ignore="ignore" />
											<hyjf:validmessage key="disposalArea" label="所在地区"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												评估价值
											</label>
											<input type="text" name="disposalPredictiveValue" id="disposalPredictiveValue" class="form-control input-sm" value="<c:out value="${borrowForm.disposalPredictiveValue }"></c:out>" 
													maxlength="20" datatype="*1-20" errormsg="评估价值长度不能超过50位" ignore="ignore" />
											<hyjf:validmessage key="disposalPredictiveValue" label="评估价值"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												权属类别
											</label>
											<input type="text" name="disposalOwnershipCategory" id="disposalOwnershipCategory" class="form-control input-sm" value="<c:out value="${borrowForm.disposalOwnershipCategory }"></c:out>" 
													maxlength="20" datatype="*1-20" errormsg="权属类别长度不能超过50位" ignore="ignore" />
											<hyjf:validmessage key="disposalOwnershipCategory" label="权属类别"></hyjf:validmessage>
										</div>
									</div>
									<div class="col-sm-6">
										<div class="form-group">
											<label class="control-label">
												资产成因
											</label>
											<input type="text" name="disposalAssetOrigin" id="disposalAssetOrigin" class="form-control input-sm" value="<c:out value="${borrowForm.disposalAssetOrigin }"></c:out>" 
													maxlength="20" datatype="*1-20" errormsg="资产成因长度不能超过20位" ignore="ignore" />
											<hyjf:validmessage key="disposalAssetOrigin" label="资产成因"></hyjf:validmessage>
										</div>
									</div>
								</div>
							</fieldset>
							<fieldset>
								<legend>
									<strong class="no-wrap">资产信息</strong>
								</legend>
								<div class="row">
									<div class="col-md-12 col-lg-12">
										<div class="form-group">
											<textarea placeholder="请填写资产信息..." maxlength="2000" datatype="*1-2000" errormsg="资产信息长度不能超过2000位" ignore="ignore" 
												id="disposalAttachmentInfo" name="disposalAttachmentInfo" class="form-control" rows="5"><c:out value="${borrowForm.disposalAttachmentInfo }"></c:out></textarea>
										</div>
										<hyjf:validmessage key="disposalAttachmentInfo" label="资产信息"></hyjf:validmessage>
									</div>
								</div>
							</fieldset>
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
								<a href="#tab_czya_10" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>
							
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:项目信息 -->
				
				<!-- Start:处置预案 -->
				<div class="tab-pane fade" id="tab_czya_10">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="row">
								<div class="col-md-12 col-lg-8">
									<div class="row">
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													售价预估
												</label>
												<input type="text" name="disposalPriceEstimate" id="disposalPriceEstimate" class="form-control input-sm" value="<c:out value="${borrowForm.disposalPriceEstimate }"></c:out>" 
														maxlength="50" datatype="*1-50" errormsg="售价预估长度不能超过50位" ignore="ignore" />
												<hyjf:validmessage key="disposalPriceEstimate" label="售价预估"></hyjf:validmessage>
											</div>
										</div>
										<div class="col-sm-6">
											<div class="form-group">
												<label class="control-label">
													处置周期
												</label>
												<input type="text" name="disposalPeriod" id="disposalPeriod" class="form-control input-sm" value="<c:out value="${borrowForm.disposalPeriod }"></c:out>" 
														maxlength="50" datatype="*1-50" errormsg="处置周期长度不能超过50位" ignore="ignore" />
												<hyjf:validmessage key="disposalPeriod" label="处置周期"></hyjf:validmessage>
											</div>
										</div>
									</div>
									<div class="form-group">
										<label class="control-label">
											处置渠道
										</label>
										<input type="text" name="disposalChannel" id="disposalChannel" class="form-control input-sm" value="<c:out value="${borrowForm.disposalChannel }"></c:out>" 
												maxlength="50" datatype="*1-50" errormsg="处置渠道长度不能超过50位" ignore="ignore" />
										<hyjf:validmessage key="disposalChannel" label="处置渠道"></hyjf:validmessage>
									</div>
									<div class="form-group">
										<label class="control-label">
											处置结果预案
										</label>
										<textarea placeholder="请填写处置结果预案..."  maxlength="2000" datatype="*1-2000" errormsg="处置结果预案长度不能超过2000位" ignore="ignore" 
												id="disposalResult" name="disposalResult" class="form-control" rows="5"><c:out value="${borrowForm.disposalResult }"></c:out></textarea>
										<hyjf:validmessage key="disposalResult" label="处置结果预案"></hyjf:validmessage>
									</div>
<%-- 	龙海说风控要求去掉 20160218								
									<div class="form-group">
										<label class="control-label">
											备注说明
										</label>
										<textarea placeholder="请填写备注说明..."  maxlength="2000" datatype="*1-2000" errormsg="备注说明长度不能超过2000位" ignore="ignore" 
												id="disposalNote" name="disposalNote" class="form-control" rows="5"><c:out value="${borrowForm.disposalNote }"></c:out></textarea>
										<hyjf:validmessage key="disposalNote" label="备注说明"></hyjf:validmessage>
									</div> --%>
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
								<a href="#tab_zcxx_9" class="btn btn-primary btn-o btn-wide fn-Next">
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
				<!-- End:处置预案 -->
			</div>
			
			<!-- 列表的行模板 -->
			<table id="rowTemplts" type="text/x-tmpl" style="display:none">
				<!-- 拆表的行模板 -->
				<tr>
					<td class="center"></td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="names" class="form-control input-sm" maxlength="60" value="" datatype="*1-60"  ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="accounts" class="form-control input-sm" maxlength="60" value="" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写借款金额" errormsg="借款金额至少为1元，最大不能超过9999999999元，且必须为数字" ignore="ignore"/>
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
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="number" class="form-control input-sm" value="" maxlength="13" datatype="*1-10" ignore="ignore" />
						</div>
					</td>
				    <td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="registration" class="form-control input-sm" value="" maxlength="13" datatype="*1-15" ignore="ignore" />
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="vin" class="form-control input-sm" value="" maxlength="13" datatype="*1-30" ignore="ignore" />
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
							<input type="text" name="housesCnt" class="form-control input-sm" value="" maxlength="5" datatype="n1-5" ignore="ignore" />
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesToprice" class="form-control input-sm" value="" maxlength="20" datatype="n1-20" ignore="ignore"/>
						</div>
					</td>
					<td class="vertical-align-top">
						<div class="form-group">
							<input type="text" name="housesBelong" class="form-control input-sm" value="" maxlength="20" datatype="*1-20" ignore="ignore" />
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowcommon/borrowcommon.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
