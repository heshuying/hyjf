<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="手动发送消息" />

	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty msgPushNoticesForm.id ? '添加' : '修改'}"></c:set>
		<div class="container-fluid container-fullw bg-white">
			<div class="tabbable">
				<ul id="mainTabs" class="nav nav-tabs nav-justified">
					<li class="s-ellipsis active"><a href="#tabs_1" data-toggle="tab"><i class="fa fa-edit"></i> 编辑内容</a></li>
					<li><a href="#tabs_2" data-toggle="tab"><i class="fa fa-edit"></i> 定制发送</a></li>
				</ul>
			</div>
			<form id="mainForm" action="${empty msgPushNoticesForm.id ? 'insertAction' : 'updateAction'}" method="post" role="form" class="form-horizontal">
				<div class="tab-content">
					<!-- Start:基本信息 -->
					<div class="tab-pane fade in active" id="tabs_1">
						<div class="row">
							<!-- 表单左侧 -->
							<div class="col-xs-12 col-md-12">
								<input type="hidden" name="id" id="id" value="${msgPushNoticesForm.id }" /> <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" /> <input type="hidden" id="success" value="${success}" />
								<div class="form-group">
									<label class="col-xs-2 control-label" for="tagCode"> <span class="symbol required"></span>消息标签
									</label>
									<div class="col-xs-8">
										<select id="tagId" name="tagId" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择项消息标签...">
											<option value=""></option>
											<c:forEach items="${noticesPushTags }" var="tags" begin="0" step="1" varStatus="status">
												<option value="${tags.id }" tid="${tags.tagCode }" <c:if test="${tags.id eq msgPushNoticesForm.tagId}">selected="selected"</c:if>>
													<c:out value="${tags.tagName }"></c:out></option>
											</c:forEach>
										</select>
										<hyjf:validmessage key="tagId" label="消息标签"></hyjf:validmessage>
										<input type="hidden" id="tagCode" name="tagCode" value="${msgPushNoticesForm.tagCode}" />
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="msgTitle"> <span class="symbol required"></span>消息标题
									</label>
									<div class="col-xs-8">
										<input type="text" placeholder="消息标题" id="msgTitle" name="msgTitle" value="${msgPushNoticesForm.msgTitle}" class="form-control" maxlength="20" datatype="*1-20" errormsg="请输入消息标题！">
										<hyjf:validmessage key="msgTitle" label="消息标题"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label padding-top-5" for="msgImageUrl">消息图片</label>
									<div class="col-xs-8">
										<div class="fileinput fileinput-new picClass" data-provides="fileinput">
											<!-- 缺省图片 -->
											<div class="fileinput-new thumbnail">
												<img width="160" height="120" src="${fileDomainUrl}${msgPushNoticesForm.msgImageUrl}" alt="">
											</div>
											<!-- 显示图片 -->
											<div class="fileinput-preview fileinput-exists thumbnail"></div>
											<!-- 图片路径 -->
											<div class="purMargin">
												<input type="text" readonly="readonly" name="msgImageUrl" id="msgImageUrl" value="${msgPushNoticesForm.msgImageUrl}" placeholder="上传图片路径" />
												<hyjf:validmessage key="msgImageUrl" label="上传图片路径"></hyjf:validmessage>
											</div>
											<!-- 按钮管理 -->
											<div class="user-edit-image-buttons">
												<span class="btn btn-azure btn-file"><span class="fileinput-new"><i class="fa fa-picture"></i> 上传图片</span><span class="fileinput-exists"><i class="fa fa-picture"></i> 重新上传</span> <input type="file" name="file" id="fileupload" class="fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff"> </span> <a href="#" id="delfile" class="btn fileinput-exists btn-red" data-dismiss="fileinput"> <i class="fa fa-times"></i> 删除
												</a>
											</div>
											<hyjf:validmessage key="msgImageUrl" label="消息图片"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label" for="author"> <span class="symbol required"></span>通知内容:
									</label>
									<div class="col-xs-8">
										<textarea name="msgContent" id="msgContent" placeholder="文章内容" maxlength="4000" class="form-control tinymce" cols="10" rows="5" datatype="*" errormsg="通知内容必填！" errormsg="未填写通知内容">${msgPushNoticesForm.msgContent }</textarea>
										<hyjf:validmessage key="msgContent" label="文章内容"></hyjf:validmessage>
									</div>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-2 col-xs-8">
										<c:if test="${msgPushNoticesForm.updateOrReSend != 1}">
											<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 提交保存</a>
										</c:if>
										<a href="#tabs_2" class="btn btn-primary btn-o btn-wide pull-right fn-Next"> 下一步 <i class="fa fa-arrow-circle-right"></i>
										</a>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="tab-pane fade" id="tabs_2">
						<div class="row">
							<!-- 表单左侧 -->
							<div class="col-xs-12 col-md-12">

								<div class="form-group">
								<label class="col-xs-2 control-label" for="projectType"><span class="symbol required"></span>推送终端：</label>
									<div class="col-xs-8 checkbox clip-check check-primary inline" style="padding-left: 15px;">
										<c:set var="tempStrs" value="${fn:split(msgPushNoticesForm.msgTerminal, ',')}" />
										<c:forEach items="${plats }" var="plat" begin="0" step="1" varStatus="status">
											<input type="checkbox" id="ct-${status.index }" datatype="*" name="msgTerminal" value="${plat.nameCd}" 
											<c:forEach items="${tempStrs }" var="tempStr" begin="0" step="1"> <c:if test="${plat.nameCd eq tempStr}"> checked="checked"</c:if> </c:forEach>>
											<label for="ct-${status.index }">${plat.name }</label>
										</c:forEach>
										<hyjf:validmessage key="msgTerminal" label="推送终端"></hyjf:validmessage>
									</div>
								</div>
								
								<div class="form-group">
									<label class="col-xs-2 control-label" for="msgAction"> <span class="symbol required" aria-required="true"></span>后续动作：
									</label>
									<div class="col-xs-8">
										<select id="msgAction" name="msgAction" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择后续动作...">
											<c:forEach items="${noticesActions }" var="actions" begin="0" step="1" varStatus="status">
												<option value="${actions.nameCd }" <c:if test="${actions.nameCd eq msgPushNoticesForm.msgAction}">selected="selected"</c:if>>
													<c:out value="${actions.name }"></c:out></option>
											</c:forEach>
										</select>
										<hyjf:validmessage key="msgAction" label="后续动作"></hyjf:validmessage>
										<div id="actionPanel0" ${(msgPushNoticesForm.msgAction eq '0') ? '' : 'style="display:none;"'}>
											<!-- 打开APP -->
										</div>
										<c:choose>
										<c:when test="${msgPushNoticesForm.id == null}">
											<div id="actionPanel1" ${(msgPushNoticesForm.msgAction eq '1') ? '' : 'style="display:none;"'}>
												<!-- 打开H5页面 -->
												<input type="text" placeholder="请输入http地址（全路径）" id="noticesActionUrl1" name="noticesActionUrl1" value="${msgPushNoticesForm.noticesActionUrl1}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
												<hyjf:validmessage key="noticesActionUrl1" label="http地址"></hyjf:validmessage>
											</div>
										</c:when>
										<c:otherwise>
											<div id="actionPanel1" ${(msgPushNoticesForm.msgAction eq '1') ? '' : 'style="display:none;"'}>
												<!-- 打开H5页面 -->
												<input type="text" placeholder="请输入http地址（全路径）" id="noticesActionUrl1" name="noticesActionUrl1" value="${msgPushNoticesForm.noticesActionUrl1}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
												<hyjf:validmessage key="noticesActionUrl1" label="http地址"></hyjf:validmessage>
											</div>
											<div id="actionPanel3" ${(msgPushNoticesForm.msgAction eq '3') ? '' : 'style="display:none;"'}>
												<!-- 打开H5页面 -->
												<input type="text" placeholder="请输入http地址（全路径）" id="noticesActionUrl3" name="noticesActionUrl3" value="${msgPushNoticesForm.noticesActionUrl3}" class="form-control" datatype="*" maxlength="100" ajaxurl="checkUrlAction">
												<hyjf:validmessage key="noticesActionUrl3" label="http地址"></hyjf:validmessage>
											</div>
										</c:otherwise>
										</c:choose>
										<div id="actionPanel2" ${(msgPushNoticesForm.msgAction eq '2') ? '' : 'style="display:none;"'}>
											<!-- 指定原生页面 -->
											<span class="symbol"></span><select id="noticesActionUrl2" name="noticesActionUrl2" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择项原生页面...">
												<option value=""></option>
												<option value="hyjf://jumpInvest" <c:if test="${'hyjf://jumpInvest' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>财富汇</option>
												<option value="hyjf://jumpXSH" <c:if test="${'hyjf://jumpXSH' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>新手汇</option>
												<option value="hyjf://jumpZXH" <c:if test="${'hyjf://jumpZXH' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>尊享汇</option>
												<option value="hyjf://jumpMine" <c:if test="${'hyjf://jumpMine' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>我的账户</option>
												<option value="hyjf://jumpCouponsList" <c:if test="${'hyjf://jumpCouponsList' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>优惠券列表</option>
												<option value="hyjf://jumpTransactionDetail" <c:if test="${'hyjf://jumpTransactionDetail' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>交易记录</option>
												<option value="hyjf://jumpTransfer" <c:if test="${'hyjf://jumpTransfer' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>债券转让-已承接</option>
												<option value="hyjf://jumpTransferRecord" <c:if test="${'hyjf://jumpTransferRecord' eq msgPushNoticesForm.noticesActionUrl2}">selected="selected"</c:if>>债权转让-转让记录</option>
											</select>
											<hyjf:validmessage key="noticesActionUrl2" label="原生页面"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label"> <span class="symbol required"></span>发送时间：
									</label>
									<div class="col-xs-8">
										<select id="msgSendType" name="msgSendType" class="form-select2" style="width: 70%" datatype="*" data-placeholder="请选择发送时间...">
											<option value="0" <c:if test="${msgPushNoticesForm.msgSendType == 0}">selected="selected"</c:if>>立即发送</option>
											<option value="1" <c:if test="${msgPushNoticesForm.msgSendType == 1}">selected="selected"</c:if>>定时发送</option>
										</select>
										<hyjf:validmessage key="msgSendType" label="发送时间"></hyjf:validmessage>
										<div id="msgSendTypePanel0" ${(msgPushNoticesForm.msgSendType ==1) ? '' : 'style="display:none;"'}>
											<!-- 立即发送 -->
										</div>
										<div id="msgSendTypePanel1" ${(msgPushNoticesForm.msgSendType==0) ? '' : 'style="display:none;"'}>
											<!-- 定时发送 -->
											<span class="symbol"></span><input type="text" name="noticesPreSendTimeStr" id="noticesPreSendTimeStr" class="form-control underline" value="${msgPushNoticesForm.noticesPreSendTimeStr}" maxlength="19" datatype="*1-19" nullmsg="请填写定时发送时间" onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})" />
											<hyjf:validmessage key="noticesPreSendTimeStr" label="定时时间"></hyjf:validmessage>
										</div>
									</div>
								</div>
								<div class="form-group">
									<label class="col-xs-2 control-label"> <span class="symbol"></span>推送用户：
									</label>
									<div class="col-xs-8">所有人</div>
								</div>
								<div class="form-group margin-bottom-0">
									<div class="col-xs-offset-2 col-xs-8">
										<c:if test="${msgPushNoticesForm.updateOrReSend != 1}">
											<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 提交保存</a>
										</c:if>
										<a href="#tabs_1" class="btn btn-primary btn-o btn-wide pull-right fn-Next"><i class="fa fa-arrow-circle-left"></i> 上一步 </a>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</form>
		</div>
	</tiles:putAttribute>

	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.min.css" rel="stylesheet" media="screen">
		<style>
.purMargin {
	margin: 8px 0;
}

.purMargin input {
	width: 200px;
}
</style>
	</tiles:putAttribute>

	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<!-- Form表单插件 -->
		<%@include file="/jsp/common/pluginBaseForm.jsp"%>
		<script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script>
		<script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>

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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/msgpush/notices/noticesInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
