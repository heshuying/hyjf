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
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="智投发起" />
	<%-- 画面主面板的标题块 --%>
	<tiles:putAttribute name="pageFunCaption" type="string">
		<h1 class="mainTitle">智投发起</h1>
		<span class="mainDescription">智投发起的说明。</span>
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="container-fluid container-fullw bg-white">
		<div class="tabbable">
			<ul id="mainTabs" class="nav nav-tabs nav-justified">
				<li <c:if test="${planForm.tabName ==null or planForm.tabName =='' }"> class='s-ellipsis active'</c:if>>
					<a href="#tab_jbxx_1" data-toggle="tab"><i class="fa fa-edit"></i> 基本信息</a>
				</li>
				<li>
					<a href="#tab_jhjs_2" data-toggle="tab"><i class="fa fa-cubes"></i> 计划介绍</a>
				</li>
				<li>
					<a href="#tab_aqbz_3" data-toggle="tab"><i class="fa fa-folder-open"></i> 安全保障</a>
				</li>
				<li>
					<a href="#tab_yyxx_4" data-toggle="tab"><i class="fa fa-server"></i> 运营信息</a>
				</li>
				<li <c:if test="${planForm.tabName !=null and planForm.tabName !='' }"> class='s-ellipsis active'</c:if> >
					<a href="#tab_glzc_5" data-toggle="tab"><i class="fa fa-user-secret"></i> 关联资产</a>
				</li>
			</ul>
			<form id="downloadForm" action="" method="post" target="_blank">
			</form>
			<form id="mainForm" action="insertAction" method="post"  role="form">
			<!-- 资产配置 -->
			<input type="hidden" name="debtPlanBorrowNid" id="debtPlanBorrowNid" value="${planForm.debtPlanBorrowNid}"/>
			
			<input type="hidden" name="debtPlanNidSrch" id="debtPlanNidSrch" value="${planForm.debtPlanNid}"/>
			
			<input type="hidden" name="borrowNidSrch" id="borrowNidSrch" value="${planForm.borrowNidSrch}"/>
			
			<input type="hidden" name="borrowStyleSrch" id="borrowStyleSrch" value="${planForm.borrowStyleSrch}"/>
			
			<input type="hidden" name="paginatorPage" id="paginator-page" value="${planForm.paginatorPage}" />
				
			<%-- LIST --%>
			<c:if test="${ ( ( planForm.debtPlanStatus ne '1' ) and ( planForm.debtPlanStatus ne '2' ) and ( planForm.debtPlanStatus ne '0' ) ) and ( !empty planForm.debtPlanStatus )}">
				<c:set var="isEdit" value="1"></c:set>
			</c:if>
				
			<div class="tab-content">
				<!-- Start:基本信息 -->
				<div <c:if test="${planForm.tabName ==null or planForm.tabName =='' }"> class='tab-pane fade in active'</c:if>
				<c:if test="${planForm.tabName != null and planForm.tabName !='' }"> class='tab-pane fade'</c:if> id="tab_jbxx_1">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您本次发起智投的基本信息，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="panel">
								<div class="form-group">
									<label class="control-label">
										计划类型 <span class="symbol required"></span>
									</label>
									<c:if test="${!empty planForm.debtPlanNid }">
										<div>
											<select id="" name="" class="form-select2" style="width:30%" disabled="disabled" data-placeholder="请选择计划类型...">
												<option value=""></option>
												<c:forEach items="${debtPlanConfigList }" var="debtPlanConfig" begin="0" step="1" varStatus="status">
													<option value="${debtPlanConfig.debtPlanType }" data-debtPlanPrefix="${debtPlanConfig.debtPlanPrefix }" data-debtMinInvestment="${debtPlanConfig.debtMinInvestment}"
													data-debtPlanTypeName="${debtPlanConfig.debtPlanTypeName }" data-debtPlanType="${debtPlanConfig.debtPlanType}" data-debtInvestmentIncrement="${debtPlanConfig.debtInvestmentIncrement}" data-debtMaxInvestment ="${debtPlanConfig.debtMaxInvestment}"
														 data-debtLockPeriod="${debtPlanConfig.debtLockPeriod}" data-debtQuitPeriod="${debtPlanConfig.debtQuitPeriod }"
														<c:if test="${debtPlanConfig.debtPlanType eq planForm.debtPlanType}">selected="selected"</c:if>>
														<c:out value="${debtPlanConfig.debtPlanTypeName }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</c:if>
									<c:if test="${empty planForm.debtPlanNid }">
										<div>
											<select id="debtPlanType" name="debtPlanType" class="form-select2" datatype="*" nullmsg="未指定计划类型！" style="width:30%" data-placeholder="请选择计划类型...">
												<option value=""></option>
												<c:forEach items="${debtPlanConfigList }" var="debtPlanConfig" begin="0" step="1" varStatus="status">
													<option value="${debtPlanConfig.debtPlanType }" data-debtPlanPrefix="${debtPlanConfig.debtPlanPrefix }" data-debtMinInvestment="${debtPlanConfig.debtMinInvestment}"
													data-debtPlanTypeName="${debtPlanConfig.debtPlanTypeName }" data-debtPlanType="${debtPlanConfig.debtPlanType}" data-debtInvestmentIncrement="${debtPlanConfig.debtInvestmentIncrement}" data-debtMaxInvestment ="${debtPlanConfig.debtMaxInvestment}"
														 data-debtLockPeriod="${debtPlanConfig.debtLockPeriod}" data-debtQuitStyle="${debtPlanConfig.debtQuitStyle}" data-debtQuitPeriod="${debtPlanConfig.debtQuitPeriod }"
														<c:if test="${debtPlanConfig.debtPlanType eq planForm.debtPlanType}">selected="selected"</c:if>>
														<c:out value="${debtPlanConfig.debtPlanTypeName }"></c:out></option>
												</c:forEach>
											</select>
										</div>
									</c:if>
									<hyjf:validmessage key="planType" label="计划类型"></hyjf:validmessage>
								</div>
								<c:if test="${empty planForm.debtPlanNid }">
									<div class="form-group">
										<label class="control-label">
											智投预编号 <span class="symbol required"></span>
										</label>
										<div class="input-group" style="width:30%">
											<input type="text" id="debtPlanPreNid" name="debtPlanPreNid" class="form-control input-sm" value='<c:out value="${planForm.debtPlanPreNid}"></c:out>'
													maxlength="20" datatype="*1-20" nullmsg="未填写计划预编号" errormsg="计划预编号长度不能超过20位"/>
											<span class="input-group-btn"><a class="btn btn-primary btn-sm" id="getPlanPreNid"> 重新获取</a></span>
										</div>
										<hyjf:validmessage key="debtPlanPreNid" label="智投编号"></hyjf:validmessage>
									</div>
								</c:if>
								<c:if test="${!empty planForm.debtPlanNid }">
									<div class="form-group">
										<label class="control-label">
											智投编号 <span class="symbol required"></span>
										</label>
										<div class="input-group" style="width:30%">
											<input type="text" id="debtPlanNid" name="debtPlanNid" class="form-control input-sm" value='<c:out value="${planForm.debtPlanNid}"></c:out>' readonly="readonly" />
										</div>
										<hyjf:validmessage key="debtPlanNid" label="智投编号"></hyjf:validmessage>
									</div>
								</c:if>
								<div class="form-group">
									<label class="control-label">
										智投名称 <span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<input type="text" name="debtPlanName" id="debtPlanName" class="form-control input-sm" value='<c:out value="${planForm.debtPlanName}"></c:out>' <c:if test="${!empty planForm.debtPlanNid }">readonly="readonly"</c:if>
											maxlength="20" datatype="*1-20" nullmsg="未填写智投名称" errormsg="智投名称长度不能超过20位字符" ajaxurl="isDebtPlanNameExist?debtPlanNid=${planForm.debtPlanNid}"/>
									</div>
									<hyjf:validmessage key="debtPlanName"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										授权服务金额 <span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<input type="text" id="debtPlanMoney" name="debtPlanMoney" class="form-control input-sm" value='<c:out value="${planForm.debtPlanMoney}"></c:out>' <c:if test="${ ( isEdit eq '1' ) }">readonly="readonly"</c:if>
												maxlength="10" datatype="n1-10,range" data-minvalue="1" data-maxvalue="9999999999" nullmsg="未填写智投金额" errormsg="智投金额至少为1元，最大不能超过9999999999元，且必须为数字"  />
										<span class="input-group-addon">元</span>
									</div>
									<hyjf:validmessage key="debtPlanMoney" label="授权服务金额"></hyjf:validmessage>
									<hyjf:validmessage key="debtPlanMoney-error"></hyjf:validmessage>
								</div>
								<div class="form-group">
									<label class="control-label">
										服务回报期限<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<input type="text" id="debtLockPeriod" name="debtLockPeriod" class="form-control input-sm" value='<c:out value="${planForm.debtLockPeriod}"></c:out>' readonly="readonly"/>
										<span class="input-group-addon">个月</span>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										参考年回报率<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="expectApr" name="expectApr" class="form-control input-sm" value='<c:out value="${planForm.expectApr}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="expectApr" name="expectApr" class="form-control input-sm" datatype="/^\d{1,2}(\.\d{1,2})?$/" maxlength="5" errormsg="参考年回报率必须为数字，整数部分不能超过2位，小数部分不能超过2位！" nullmsg="未填写参考年回报率" value='<c:out value="${planForm.expectApr}"></c:out>' />
										</c:if>
										<span class="input-group-addon">%</span>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										退出方式<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ empty planForm.debtQuitStyle or planForm.debtQuitStyle eq '0'}">
											<input type="text" class="form-control input-sm" value='<c:out value="到期退出"></c:out>' readonly="readonly"/>
										</c:if>
										<input type="hidden" id="debtQuitStyle" name="debtQuitStyle" class="form-control input-sm" value='<c:out value="${planForm.debtQuitStyle}"></c:out>' readonly="readonly"/>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										退出所需天数<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="debtQuitPeriod" name="debtQuitPeriod" class="form-control input-sm" value='<c:out value="${planForm.debtQuitPeriod}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="debtQuitPeriod" name="debtQuitPeriod" class="form-control input-sm" value='<c:out value="${planForm.debtQuitPeriod}"></c:out>' />
										</c:if>
										<span class="input-group-addon">天</span>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										提交审核 <span class="symbol required"></span>
									</label>
									<div class="clip-radio radio-primary">
										<input type="radio" disabled="disabled" value="yes" name="isAudits" datatype="*" <c:if test="${ planForm.isAudits eq 'yes' }">checked</c:if> id="isAudits_yes" >
										<label for="isAudits_yes">立即提交审核</label>
										<input type="radio" disabled="disabled" value="no" name="isAudits" datatype="*" <c:if test="${ planForm.isAudits eq 'no' }">checked</c:if> id="isAudits_no">
										<label for="isAudits_no">暂不提交审核</label>
									</div>
									<hyjf:validmessage key="isAudits" label="提交审核"></hyjf:validmessage>
								</div>
							</div>
						</div>
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
							<a href="#tab_jhjs_2" class="btn btn-primary btn-o btn-wide pull-right fn-Next">
								下一步 <i class="fa fa-arrow-circle-right"></i>
							</a>
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:基本信息 -->
				<!-- Start:计划介绍-->
				<div class="tab-pane fade" id="tab_jhjs_2">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<!-- 计划介绍面板 -->
					<div id="jhjsPanel">
						<div class="col-md-12">
							<label class="control-label">
								计划概念<span class="symbol required"></span>
							</label>
							<div class="form-group">
								<textarea name="planConcept" datatype="*" nullmsg="未填写计划概念" class="tinymce" cols="10" rows="12">
									<c:set value="<p style='padding-left: 30px; text-align: justify;'>汇添金出借计划是汇盈金服平台为出借人提供的本息自动循环出借及到期</p>" var="str1"></c:set>
									<c:set value="<p style='text-align: justify;'>自动转让退出的投标工具，在出借人认可的标的范围内，系统对符合要求的标的</p>" var="str2"></c:set>
									<c:set value="<p style='text-align: justify;'>进行自动投标。汇添金出借计划为出借人实现分散投标，更好的满足出借人多样</p>" var="str3"></c:set>
									<c:set value="<p style='text-align: justify;'>化的出借需求。</p>" var="str4"></c:set>
									<c:if test="${planForm.planConcept !=null && planForm.planConcept !='' }"><c:out value="${planForm.planConcept }"></c:out></c:if>
									<c:if test="${planForm.planConcept == null || planForm.planConcept == ''}"><c:out value="${str1 }"></c:out><c:out value="${str2 }"></c:out><c:out value="${str3 }"></c:out><c:out value="${str4 }"></c:out></c:if>
								</textarea>
							</div>
							<hyjf:validmessage key="planConcept" label="计划概念"></hyjf:validmessage>
						</div>
						<div class="col-md-12">
							<label class="control-label">
								计划原理<span class="symbol required"></span>
							</label>
							<div class="form-group">
								<textarea name="planPrinciple" datatype="*" nullmsg="未填写计划原理" class="tinymce" cols="10" rows="12">
									<c:set value="<p>加入汇添金出借计划之后，系统立即开始自动投标。计划满额或到达计划加入截止时间后，计划进入锁定期，锁定期内出借人不能提前退出。锁定期结束后，系统通过投标的优先机制设计来提供转出债权的成交优先权，从而在短时间内完成自动退出。</p>" var="str1"></c:set>
									<c:set value="<p>&nbsp;</p>" var="str2"></c:set>
									<c:set value="<p><img class='example1' src='${webhost}/data/images/planprinciple00-1.png' alt='' width='1100' height='280' /></p>" var="str3"></c:set>
									<c:if test="${planForm.planPrinciple != null && planForm.planPrinciple != ''}"><c:out value="${planForm.planPrinciple }"></c:out></c:if>
									<c:if test="${planForm.planPrinciple == null || planForm.planPrinciple == ''}"><c:out value="${str1 }"></c:out><c:out value="${str2 }"></c:out><c:out value="${str3 }"></c:out></c:if>
								</textarea>
							</div>	
							<hyjf:validmessage key="planPrinciple" label="计划原理"></hyjf:validmessage>
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
								<a href="#tab_aqbz_3" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>
							
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:计划介绍 -->
				<!-- Start:安全保障-->
				<div class="tab-pane fade" id="tab_aqbz_3">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<!-- 安全保障面板 -->
					<div id="aqbzPanel">
						<div class="col-md-12">
							<label class="control-label">
								风控保障措施<span class="symbol required"></span>
							</label>
							<div class="form-group">
								<textarea name="safeguardMeasures" datatype="*" nullmsg="未填写风控保障措施" class="tinymce" cols="10" rows="12">
										<c:set value="<p style='padding-left: 30px;'>汇盈金服平台风控团队具备丰富的法律、金融服务机构风险管理控制等相关</p>" var="str1"></c:set>
										<c:set value="<p>行业经验，兼备贷前、贷中、贷后管理的综合能力，本着安全大于一切的理念，对</p>" var="str2"></c:set>
										<c:set value="<p>平台的每个标的进行严格详细地审核，为出借方建立资金安全壁垒。</p>" var="str3"></c:set>
										<c:if test ="${planForm.safeguardMeasures !=null && planForm.safeguardMeasures !='' }"><c:out value="${planForm.safeguardMeasures }"></c:out></c:if>
										<c:if test ="${planForm.safeguardMeasures ==null || planForm.safeguardMeasures =='' }"><c:out value="${str1 }"></c:out><c:out value="${str2 }"></c:out><c:out value="${str3 }"></c:out></c:if>
									</textarea>
							</div>
							<hyjf:validmessage key="safeguardMeasures" label="风控保障措施"></hyjf:validmessage>
						</div>
						<div class="col-md-12">
							<label class="control-label">
								风险保证金措施<span class="symbol required"></span>
							</label>
							<div class="form-group">
								<textarea name="marginMeasures" datatype="*" nullmsg="风险保证金措施" class="tinymce" cols="10" rows="12">
										<c:set value="<p style='padding-left: 30px;'>在汇盈金服平台发布的每一笔融资项目都由合作机构按照该笔业务授信额度的2%-5%提交保证金，该笔</p>" var="str1"></c:set>
										<c:set value="<p>风险保证金存放于中国建设银行为汇盈金服专门开设的独立账户中。若合作机构无法回购出借方债权，风</p>" var="str2"></c:set>
										<c:set value="<p>险保证金将用于对出借方进行垫付。</p>" var="str3"></c:set>
										<c:if test="${planForm.marginMeasures != null && planForm.marginMeasures != '' }"><c:out value="${planForm.marginMeasures }"></c:out></c:if>
										<c:if test="${planForm.marginMeasures == null || planForm.marginMeasures == '' }"><c:out value="${str1 }"></c:out><c:out value="${str2 }"></c:out><c:out value="${str3 }"></c:out></c:if>
									</textarea>
							</div>	
							<hyjf:validmessage key="marginMeasures" label="风险保证金措施"></hyjf:validmessage>
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
								<a href="#tab_jhjs_2" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_yyxx_4" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>
							
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:安全保障 -->
				
				<!-- Start:运营信息-->
				<div class="tab-pane fade" id="tab_yyxx_4">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="row">
						<div class="col-md-12">
							<div class="panel">
								
								<div class="form-group">
									<label class="control-label">
										申购开始时间<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="buyBeginTime" name="buyBeginTime" class="form-control input-sm" value='<c:out value="${planForm.buyBeginTime}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<span class="input-icon input-icon-right">
												<input type="text" name="buyBeginTime" id="buyBeginTime" class="form-control input-sm"
														value="<c:out value="${planForm.buyBeginTime }"></c:out>" maxlength="16" datatype="*1-16" nullmsg="未填写申购开始时间"
														onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm', errDealMode: 1})"/>
												<i class="fa fa-calendar"></i>
											</span>
										</c:if>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										申购期限<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="buyPeriodDay" name="buyPeriodDay" class="form-control input-sm" value='<c:out value="${planForm.buyPeriodDay}"></c:out>' readonly="readonly"/>
											<span class="input-group-addon">天</span>
											<c:if test="${!empty planForm.buyPeriodHour and planForm.buyPeriodHour ne '0' }">
												<input type="text" id="buyPeriodHour" name="buyPeriodHour" class="form-control input-sm" value='<c:out value="${planForm.buyPeriodHour}"></c:out>' readonly="readonly"/>
												<span class="input-group-addon">小时</span>
											</c:if>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="buyPeriodDay" name="buyPeriodDay" class="form-control input-sm" value='<c:out value="${planForm.buyPeriodDay}"></c:out>' maxlength="2" datatype="n1-2" nullmsg="未填写申购天数" errormsg="申购天数必须为数字" />
											<span class="input-group-addon">天</span>
											<input type="text" id="buyPeriodHour" name="buyPeriodHour" class="form-control input-sm" value='<c:out value="${planForm.buyPeriodHour}"></c:out>' maxlength="2" datatype="n1-2" ignore="ignore" errormsg="申购小时必须为数字" />
											<span class="input-group-addon">小时</span>
										</c:if>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										最低授权服务金额<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="debtMinInvestment" name="debtMinInvestment" class="form-control input-sm" value='<c:out value="${planForm.debtMinInvestment}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="debtMinInvestment" name="debtMinInvestment" class="form-control input-sm" value='<c:out value="${planForm.debtMinInvestment}"></c:out>' maxlength="10" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/" nullmsg="未填写最低授权服务金额" errormsg="最低授权服务金额必须为数字,并且必须是1000的倍数" />
										</c:if>
										<span class="input-group-addon">元</span>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										递增金额<span class="symbol required"></span>
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="debtInvestmentIncrement" name="debtInvestmentIncrement" class="form-control input-sm" value='<c:out value="${planForm.debtInvestmentIncrement}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="debtInvestmentIncrement" name="debtInvestmentIncrement" class="form-control input-sm" value='<c:out value="${planForm.debtInvestmentIncrement}"></c:out>' maxlength="10" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/" nullmsg="未填写递增金额" errormsg="递增金额必须为数字,并且必须是1000的倍数" />
										</c:if>
										<span class="input-group-addon">元</span>
									</div>
								</div>
								<div class="form-group">
									<label class="control-label">
										最高授权服务金额
									</label>
									<div class="input-group" style="width:30%">
										<c:if test="${ ( isEdit eq '1' ) }">
											<input type="text" id="debtMaxInvestment" name="debtMaxInvestment" class="form-control input-sm" value='<c:out value="${planForm.debtMaxInvestment}"></c:out>' readonly="readonly"/>
										</c:if>
										<c:if test="${ ( isEdit ne '1' ) }">
											<input type="text" id="debtMaxInvestment" name="debtMaxInvestment" class="form-control input-sm" value='<c:out value="${planForm.debtMaxInvestment}"></c:out>' maxlength="10" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/" ignore="ignore" errormsg="最高授权服务金额必须为数字,并且必须是1000的倍数" />
										</c:if>
										<span class="input-group-addon">元</span>
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
								<a href="#tab_aqbz_3" class="btn btn-primary btn-o btn-wide fn-Next">
									<i class="fa fa-arrow-circle-left"></i> 上一步
								</a>
								<a href="#tab_glzc_5" class="btn btn-primary btn-o btn-wide fn-Next">
									下一步 <i class="fa fa-arrow-circle-right"></i>
								</a>
							</div>
							
							<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
								<i class="fa fa-check"></i>  提交保存</i>
							</a>
						</div>
					</div>
				</div>
				<!-- End:运营信息 -->

				<!-- Start:关联资产 -->
				<div <c:if test="${planForm.tabName ==null or planForm.tabName =='' }">class="tab-pane fade"</c:if> 
				<c:if test="${planForm.tabName !=null and planForm.tabName !='' }">class='tab-pane fade in active'</c:if>
				id="tab_glzc_5">
					<p>
						<i class="ti-info-alt text-primary"></i> 请在这里填写您的项目描述信息，尽可能的说明您的项目目的，以做存档.
					</p>
					<hr>
					<div class="container-fluid container-fullw bg-white">
					<div class="row">
						<div class="col-md-12">
							<div class="search-classic">
								<shiro:hasPermission name="planmanager:SEARCH">
									<%-- 功能栏 --%>
									<div class="well">
										<c:set var="jspPrevDsb" value="${planForm.paginator.firstPage ? ' disabled' : ''}"></c:set>
										<c:set var="jspNextDsb" value="${planForm.paginator.lastPage ? ' disabled' : ''}"></c:set>
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
								</shiro:hasPermission>
								<br/>
								<%-- 列表一览 --%>
								<table id="equiList" class="table table-striped table-bordered table-hover">
									<colgroup>
										<col style="width:55px;" />
									</colgroup>
									<thead>
										<tr>
											<th class="center">序号</th>
											<th class="center">选择框</th>
											<th class="center">已关联计划编号</th>
											<th class="center">项目编号</th>
											<th class="center">项目名称</th>
											<th class="center">项目金额</th>
											<th class="center">剩余可投</th>
											<th class="center">项目期限</th>
											<th class="center">还款方式</th>
											<th class="center">参考年回报率</th>
										</tr>
									</thead>
									<tbody id="roleTbody">
										<c:choose>
											<c:when test="${empty planForm.debtPlanBorrowList}">
												<tr><td colspan="10">暂时没有数据记录</td></tr>
											</c:when>
											<c:otherwise>
												<c:forEach items="${planForm.debtPlanBorrowList }" var="record" begin="0" step="1" varStatus="status">
													<tr>
														<td align="center">${(planForm.paginatorPage -1 ) * planForm.paginator.limit + status.index + 1 }</td>
														<td align="center">
															<div align="left" class="checkbox clip-check check-primary checkbox-inline">
																<input type="checkbox" class="listCheck" disabled="disabled" <c:if test="${record.isSelected eq '1' }">checked='checked'</c:if> id="row${status.index }" value="${record.borrowNid }">
																<label for="row${status.index }"></label>
															</div>
														</td>
														<td align="center">
															<c:if test="${!empty record.debtPlanNidList}">
																<c:forEach items="${record.debtPlanNidList}" var="debtPlanNid" begin="0" step="1" varStatus="status">
																	<c:out value="${debtPlanNid}"/><br/>
																</c:forEach>
															</c:if>
														
														</td>
														<td align="center"><c:out value="${record.borrowNid }" /></td>
														<td align="center"><c:out value="${record.projectName }" /></td>
														<td align="center"><c:out value="${record.account }" /></td>
														<td class="center"><c:out value="${record.borrowAccountWait }" /></td>
														<td class="center"><c:out value="${record.borrowPeriod }" /></td>
														<td class="center"><c:out value="${record.borrowStyle }" /></td>
														<td class="center"><c:out value="${record.borrowApr }" /></td>
													</tr>
												</c:forEach>
												<tr>
													<td>总计</td>
													<td></td>
													<td></td>
													<td></td>
													<td></td>
													<td align="right">
														<c:choose>
															<c:when test="${not empty accountSum }">
																<fmt:formatNumber type="number" value="${accountSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td align="right">
														<c:choose>
															<c:when test="${not empty borrowAccountWaitSum }">
																<fmt:formatNumber type="number" value="${borrowAccountWaitSum }" /> 
															</c:when>
															<c:otherwise>
																0.00
															</c:otherwise>
														</c:choose>
													</td>
													<td></td>
													<td></td>
													<td></td>
												</tr>
											</c:otherwise>
										</c:choose>
									</tbody>
								</table>
								<br/><br/>
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
							<a href="#tab_yyxx_4" class="btn btn-primary btn-o btn-wide fn-Next">
								<i class="fa fa-arrow-circle-left"></i> 上一步
							</a>
						</div>
						<a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
							<i class="fa fa-check"></i>  提交保存
						</a>
					</div>
				</div>
				</div>
				<!-- End:关联资产 -->
				<%-- 检索面板 (ignore) --%>
				<tiles:putAttribute name="searchPanels" type="string">
					<shiro:hasPermission name="planmanager:SEARCH">
						
						<!-- 查询条件 -->
						<div class="form-group">
							<label>项目编号</label>
							<input type="text" name="borrowNid" id="borrowNid" class="form-control input-sm underline" value="${planForm.borrowNidSrch}" />
						</div>
						<div class="form-group">
							<label>还款方式</label>
							<select name="borrowStyle" id="borrowStyle" class="form-control underline form-select2">
								<option value="">全部</option>
								<c:forEach items="${borrowStyleList }" var="borrowStyle" begin="0" step="1" varStatus="status">
									<option value="${borrowStyle.nid }"
										<c:if test="${borrowStyle.nid eq planForm.borrowStyleSrch}">selected="selected"</c:if>>
										<c:out value="${borrowStyle.name}"></c:out>
									</option>
								</c:forEach>
							</select>
						</div>
					</shiro:hasPermission>
				</tiles:putAttribute>
			</div>
			</form>
		</div>
	</div>
	</tiles:putAttribute>


	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet" media="screen">
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
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
	    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/plancommon/plancommonInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
