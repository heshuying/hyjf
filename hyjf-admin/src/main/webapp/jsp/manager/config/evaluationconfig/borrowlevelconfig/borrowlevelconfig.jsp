<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="添加信用等级配置" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<div class="panel panel-white" style="margin: 0">
					<div class="panel-body" style="margin: 0 auto">
						<div class="panel-scroll height-430">
							<form id="mainForm" action="updateAction" method="post" role="form" class="form-horizontal">
									<%-- 列表一览 --%>
								<input type="hidden" name="id" id="id" value="${borrowLevelCofigForm.id }" />
								<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
								<input type="hidden" id="success" value="${success}" />

								<div class="form-group">
									<label class="col-xs-5 control-label">
                                        BBB信用等级对应的建议出借者类型:
									</label>
									<div class="clip-radio radio-primary">
										<select id="bbbEvaluationProposal" name="bbbEvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.bbbEvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.bbbEvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										A信用等级对应的建议出借者类型:
									</label>
									<div class="clip-radio radio-primary">
										<select id="aEvaluationProposal" name="aEvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.aEvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.aEvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										AA-信用等级对应的建议出借者类型 ：
									</label>
									<div class="clip-radio radio-primary">
										<select id="aa0EvaluationProposal" name="aa0EvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.aa0EvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.aa0EvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										AA信用等级对应的建议出借者类型 ：
									</label>
									<div class="clip-radio radio-primary">
										<select id="aa1EvaluationProposal" name="aa1EvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.aa1EvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.aa1EvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										AA+信用等级对应的建议出借者类型:
									</label>
									<div class="clip-radio radio-primary">
										<select id="aa2EvaluationProposal" name="aa2EvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.aa2EvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.aa2EvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-5 control-label">
										AAA信用等级对应的建议出借者类型:
									</label>
									<div class="clip-radio radio-primary">
										<select id="aaaEvaluationProposal" name="aaaEvaluationProposal" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
											<c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }" <c:if test="${record.nameCd eq borrowLevelCofigForm.aaaEvaluationProposal}">selected="selected"</c:if>
														<c:if test="${record.nameCd eq borrowLevelCofigForm.aaaEvaluationProposal}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-5 col-xs-6">
										<a class="btn btn-o btn-primary fn-Confirm"><i
												class="fa fa-check"></i> 确 认</a> <a
											class="btn btn-o btn-primary fn-Cancel"><i
											class="fa fa-close"></i> 取 消</a>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/evaluationconfig/evaluationcheck/evaluationcheck.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
