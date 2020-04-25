<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp"
					  flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="管理" />
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
		<c:set var="jspEditType"
			   value="${empty nifaconfigForm.id ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin: 0">
			<div class="panel-body" style="margin: 0 auto">
				<div class="panel-scroll height-630">
					<form id="mainForm" action="${empty nifaconfigForm.id ? 'contractInsertAction' : 'contractUpdateAction'}" method="post" role="form" class="form-horizontal">
							<%-- 列表一览 --%>
						<input type="hidden" name="ids" id="ids" value="${nifaconfigForm.id }" />
						<input type="hidden"  name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
                        <div class="form-group">
                            <div class="form-group">
								<label class="col-sm-2 control-label" for="templetNid"> <span class="symbol required"></span>合同模板编号</label>
								<c:if test="${ !empty nifaconfigForm.id}">
									<div class="col-sm-4">
										<select id="templetNid" name="templetNid" datatype="*" data-placeholder="合同模板编号"  disabled="disabled" >
											<option value="${nifaconfigForm.templetNid}"selected="selected">${nifaconfigForm.templetNid}</option>
										</select>
									</div>
								</c:if>
								<c:if test="${ empty nifaconfigForm.id}">
									<div class="col-sm-4">
										<select id="templetNid" name="templetNid" class="form-select2" style="width:100%" data-placeholder="请合同模板编号..." datatype="*">
											<option value="">--请选择合同模板编号--</option>
											<c:forEach items="${tempIds}" var="tempId" begin="0" step="1">
												<option value="${tempId.templetId}" <c:if test="${tempId.templetId eq nifaconfigForm.templetId}">selected="selected"</c:if>>
													<c:out value="${tempId.templetId }"></c:out>
												</option>
											</c:forEach>
										</select>
									</div>
								</c:if>
                            </div>
                        </div>
						<div class="form-group">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="normalDefinition"> <span class="symbol required"></span>正常还款定义
								</label>
								<div class="col-sm-10">
									<textarea datatype="*" errormsg="必填选项" placeholder="正常还款定义" id="normalDefinition"
											  name="normalDefinition" class="form-control limited" maxlength="999"
											  datatype="*1-999" nullmsg="请填写正常还款定义"
											  errormsg="正常还款定义长度不能超过999位" >${nifaconfigForm.normalDefinition}</textarea>
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="form-group">
								<label class="col-sm-2 control-label" for="prepaymentDefinition"> <span class="symbol required"></span>提前还款定义
								</label>
								<div class="col-sm-10">
                                    <textarea datatype="*" errormsg="必填选项" placeholder="提前还款定义" id="prepaymentDefinition"
                                              name="prepaymentDefinition" class="form-control limited" maxlength="999"
											  datatype="*1-999" nullmsg="请填写提前还款定义"
											  errormsg="提前还款定义长度不能超过999位">${nifaconfigForm.prepaymentDefinition}</textarea>
								</div>
							</div>
						</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="borrowerPromises"> <span class="symbol required"></span>借款人承诺与保证
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="借款人承诺与保证" id="borrowerPromises"
                                                      name="borrowerPromises" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写借款人承诺与保证"
													  errormsg="借款人承诺与保证长度不能超过999位">${nifaconfigForm.borrowerPromises}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="lenderPromises"> <span class="symbol required"></span>出借人承诺与保证
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="出借人承诺与保证" id="lenderPromises"
                                                      name="lenderPromises" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写出借人承诺与保证"
													  errormsg="出借人承诺与保证长度不能超过999位">${nifaconfigForm.lenderPromises}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="borrowerObligation"> <span class="symbol required"></span>借款人还款义务
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="借款人还款义务" id="borrowerObligation"
                                                      name="borrowerObligation" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写借款人还款义务"
													  errormsg="借款人还款义务长度不能超过999位">${nifaconfigForm.borrowerObligation}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="confidentiality"> <span class="symbol required"></span>保密
										</label>
										<div class="col-sm-10">
							                <textarea datatype="*" errormsg="必填选项" placeholder="保密" id="confidentiality" name="confidentiality"
                                                      class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写保密"
													  errormsg="保密长度不能超过999位">${nifaconfigForm.confidentiality}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="breachContract"> <span class="symbol required"></span>违约
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="违约" id="breachContract"
                                                      name="breachContract" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写违约"
													  errormsg="违约长度不能超过999位">${nifaconfigForm.breachContract}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="applicableLaw"> <span class="symbol required"></span>法律适用
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="法律适用" id="applicableLaw"
                                                      name="applicableLaw" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写法律适用"
													  errormsg="法律适用长度不能超过999位">${nifaconfigForm.applicableLaw}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="normalDefinition"> <span class="symbol required"></span>争议解决
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="争议解决" id="disputeResolution"
                                                      name="disputeResolution" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写争议解决"
													  errormsg="争议解决长度不能超过999位">${nifaconfigForm.disputeResolution}</textarea>
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="form-group">
										<label class="col-sm-2 control-label" for="normalDefinition"> <span class="symbol required"></span>其他条款
										</label>
										<div class="col-sm-10">
                                            <textarea datatype="*" errormsg="必填选项" placeholder="其他条款" id="otherConditions"
                                                      name="otherConditions" class="form-control limited" maxlength="999"
													  datatype="*1-999" nullmsg="请填写其他条款"
													  errormsg="其他条款长度不能超过999位">${nifaconfigForm.otherConditions}</textarea>
										</div>
									</div>
								</div>
						<div class="form-group margin-bottom-0">
							<div class="col-sm-offset-2 col-sm-10">
								<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
								<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/nifaconfig/contracttemplateInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
