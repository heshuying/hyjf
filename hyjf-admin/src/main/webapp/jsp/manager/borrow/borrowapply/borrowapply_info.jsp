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
	<tiles:putAttribute name="pageTitle" value="审核" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="row panel panel-white" style="padding-right:5px;" >
			<div class="col-md-12">
				<fieldset>
					<legend>
						个人资料
					</legend>
					<div class="row">
						<div class="col-md-12">
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>姓名：</strong><c:out value="${borrowApplyForm.loan.name }" />
									</label>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>性别：</strong><c:if test="${borrowApplyForm.loan.sex eq '0' }">男</c:if><c:if test="${borrowApplyForm.loan.sex eq '1' }">女</c:if>
									</label>
								</div>
							</div>
						</div>
						<div class="col-md-12">
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>联系电话：</strong><c:out value="${borrowApplyForm.loan.tel }" />
									</label>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>所在地：</strong><c:out value="${borrowApplyForm.loan.province }" /><c:out value="${borrowApplyForm.loan.city }" /><c:out value="${borrowApplyForm.loan.area }" />
									</label>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						借款需求
					</legend>
					<div class="row">
						<div class="col-md-12">
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>借款金额：</strong><c:out value="${borrowApplyForm.loan.money }" />万元
									</label>
								</div>
							</div>
							<div class="col-xs-6">
								<div class="form-group">
									<label>
										<strong>借款期限：</strong><c:out value="${borrowApplyForm.loan.day }" />
									</label>
								</div>
							</div>
						</div>
						<div class="col-md-12">
							<div class="col-xs-12">
								<div class="form-group">
									<label>
										<strong>借款用途：</strong><c:out value="${borrowApplyForm.loan.use }" />
									</label>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						其他内容
					</legend>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>企业名称：</strong><c:out value="${borrowApplyForm.loan.gname }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>成立时间：</strong><c:out value="${borrowApplyForm.loan.gyear }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>经营地址：</strong><c:out value="${borrowApplyForm.loan.gdress }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>主营业务：</strong><c:out value="${borrowApplyForm.loan.gplay }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>年营业额：</strong><c:out value="${borrowApplyForm.loan.gmoney }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>年利润额：</strong><c:out value="${borrowApplyForm.loan.gget }" />
								</label>
							</div>
						</div>
						<div class="col-md-12">
							<div class="form-group">
								<label>
									<strong>银行贷款：</strong><c:out value="${borrowApplyForm.loan.gpay }" />
								</label>
							</div>
						</div>
					</div>
				</fieldset>
				<fieldset>
					<legend>
						审核内容
					</legend>
					<div class="row">
						<form id="mainForm" role="form"  method="post">
							<input type="hidden" name=id id="id" value="${ borrowApplyForm.loan.id}"/>
							<input type="hidden" id="success" value="${success}" />
							<c:if test="${(status eq '1' or status eq '2')}">
								<div class="col-md-12">
									<div class="form-group">
										<label>
											<strong>审核状态：</strong>
											<c:forEach items="${applyStatusList }" var="record" begin="0" step="1" varStatus="status">
												<c:if test="${record.nameCd eq borrowApplyForm.loan.state}"><c:out value="${record.name }"></c:out></c:if>
											</c:forEach>
										</label>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-group">
										<label>
											<strong>审核时间：</strong><c:out value="${borrowApplyForm.addtime }" />
										</label>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-group">
										<label>
											<strong>审核备注：</strong><c:out value="${borrowApplyForm.loan.remark }" />
										</label>
									</div>
								</div>
							</c:if>
							<c:if test="${status eq '0' }">
								<div class="col-md-12">
									<div class="form-group">
										<label>
											<strong><span class="symbol required" aria-required="true"></span>审核状态：</strong>
										</label>
										<select name="state" class="form-control underline form-select2" datatype="*">
											<c:forEach items="${applyStatusList }" var="record" begin="0" step="1" varStatus="status">
												<option value="${record.nameCd }"
													<c:if test="${record.nameCd eq borrowApplyForm.loan.state}">selected="selected"</c:if>>
													<c:out value="${record.name }"></c:out></option>
											</c:forEach>
										</select>
										<hyjf:validmessage key="state" label="审核状态"></hyjf:validmessage>
									</div>
								</div>
								<div class="col-md-12">
									<div class="form-group">
										<label>
											<strong><span class="symbol required" aria-required="true"></span>审核备注：</strong>
										</label>
										<textarea maxlength="255" name="remark" class="form-control limited" datatype="*1-255" errormsg="审核备注 长度1~255个字符！"><c:out value="${borrowApplyForm.loan.remark }" /></textarea>
										<hyjf:validmessage key="remark" label="审核备注"></hyjf:validmessage>
									</div>
								</div>
							</c:if>
						</form>
					</div>
				</fieldset>
				
				<div class="form-group margin-bottom-0">
					<div class="col-sm-offset-2 col-sm-10">
						<c:if test="${status eq '0' }">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 提交</a>
						</c:if>
						<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 关闭</a>
					</div>
				</div>
				<br/>
				<br/>
				<br/>
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
		<script type='text/javascript' src="${webRoot}/jsp/manager/borrow/borrowapply/borrowapply_info.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
