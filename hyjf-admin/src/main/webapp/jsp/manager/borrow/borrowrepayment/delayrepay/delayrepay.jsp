<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="延期" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<form id="mainForm" method="post" role="form">
			<input type="hidden" name=borrowNid id="borrowNid" value="${ borrowRepayInfo.borrowNid}" /> <input type="hidden" id="success" value="${success}" />
			<div class="row panel panel-white" style="padding-right: 5px;">
				<div class="col-md-12">
					<fieldset>
						<legend> 借款信息 </legend>
						<div class="row">
							<div class="col-md-12">
								<div class="col-xs-12">
									<div class="form-group">
										<label> <strong>借款人：</strong> <c:out value="${borrowRepayInfo.borrowUserName }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>项目编号：</strong> <c:out value="${borrowRepayInfo.borrowNid }" />
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>项目名称：</strong> <c:out value="${borrowRepayInfo.borrowName }" />
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>借款期限：</strong> <c:out value="${borrowRepayInfo.borrowPeriod }" />
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>出借利率：</strong> <c:out value="${borrowRepayInfo.borrowApr }" />%
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>还款方式：</strong> <c:out value="${borrowRepayInfo.borrowStyleName }" />
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>借款金额：</strong> <c:out value="${borrowRepayInfo.borrowAccount }" />元
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>借到金额：</strong> <c:out value="${borrowRepayInfo.borrowAccountYes }" />元
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>最后还款日：</strong> <c:out value="${borrowRepayInfo.repayLastTime }" />
										</label>
									</div>
								</div>
							</div>
						</div>
					</fieldset>
					<fieldset>
						<legend> 还款信息 </legend>
						<div class="row">
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>本期应还本息：</strong> <fmt:formatNumber value="${repayInfo.repayAccount }" pattern="#,##0.0#" />元
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> 
											<c:if test="${ borrowRepayInfo.borrowStyle eq 'endday' or borrowRepayInfo.borrowStyle eq 'end'}" >
												<strong>当前期次：</strong>第1期
											</c:if>
											<c:if test="${ borrowRepayInfo.borrowStyle ne 'endday' and borrowRepayInfo.borrowStyle ne 'end'}" >
												<strong>当前期次：</strong>第<c:out value="${repayInfo.repayPeriod }" />期
											</c:if>
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>应还日期：</strong> <c:out value="${repayTime }" /> <input type="hidden" name=repayTime id="repayTime" value="${ repayInfo.repayTime}" />
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>管理费：</strong> <fmt:formatNumber value="${repayInfo.repayFee }" pattern="#,##0.0#" />元
										</label>
									</div>
								</div>
							</div>
							<div class="col-md-12">
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>应还本金：</strong> <fmt:formatNumber value="${repayInfo.repayCapital }" pattern="#,##0.0#" />元
										</label>
									</div>
								</div>
								<div class="col-xs-6">
									<div class="form-group">
										<label> <strong>应还利息：</strong> <fmt:formatNumber value="${repayInfo.repayInterest }" pattern="#,##0.0#" />元
										</label>
									</div>
								</div>
							</div>
						</div>
					</fieldset>

					<fieldset>
						<legend> 延期天数 </legend>
						<div class="col-md-12">
							<div class="col-xs-6">
								<div class="form-group">
									<div class="input-group">
										<input type="text" maxlength="1" name="delayDays" id="delayDays" class="form-control input-sm" value="<fmt:formatNumber value="${delayDays }" pattern="#" />" datatype="n1-10,range" nullmsg="未填写延期天数" data-minvalue="1" data-maxvalue="8"
											errormsg="延期天数只能数字，并且大于0，小于等于8。" /> <span class="input-group-addon">天</span>
									</div>
									<hyjf:validmessage key="delayDays" params="${repayTimeForMsg }"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="col-md-12">
							<div class="col-xs-12">
								<div class="form-group">
									<label> <strong>延期后日期：</strong><span id="afterTime"></span>
									</label>
								</div>
							</div>
						</div>
					</fieldset>

					<div class="form-group margin-bottom-0">
						<div class="col-sm-offset-4 col-sm-6">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确定延期</a> <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取消</a>
						</div>
					</div>
					<br /> <br /> <br />
				</div>
			</div>
		</form>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowrepayment/delayrepay/delayrepay.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
