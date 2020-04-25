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
<c:set value="${fn:split('汇盈金服,消息中心,群发送短信', ',')}" var="functionPaths" scope="request"></c:set>
<shiro:hasPermission name="smsLog:VIEW">
	<tiles:insertTemplate template="/jsp/layout/mainLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="群发送短信" />
		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/sweetalert/ie9.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/toastr/toastr.min.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<style>
			.table-striped .checkbox { width:20px;margin-right:0!important;overflow:hidden }
			</style>
		</tiles:putAttribute>
		<%-- 画面主面板的标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">群发送短信</h1>
			<span class="mainDescription">发送消息发送消息发送消息发送消息</span>
		</tiles:putAttribute>
		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
		<form id="mainForm" action="${pageContext.request.contextPath }/message/message/queryUserAction" method="post">
			<table>
				<tr>
					<th>
					<div class="modal-body">
								<div class="form-group">
									<label>筛选会员:</label>
										<input type="radio" name="open_account" id="open_account_tmp" value="3" <c:if test="${smsCode.open_account ==3}">checked="true"</c:if>/>所有用户
										<input type="radio" name="open_account" id="open_account_tmp" value="1" <c:if test="${smsCode.open_account ==1}">checked="true"</c:if>/>所有已开户用户
										<input type="radio" name="open_account" id="open_account_tmp" value="0" <c:if test="${smsCode.open_account ==0}">checked="true"</c:if>/>所有未开户用户
								</div>
								
								<div class="form-group">
									<label>累计出借金额大于</label>
									<input type="text" id="add_money_count" name="add_money_count" maxlength="9"  value="${smsCode.add_money_count}"  class="form-control input-sm underline">
									<hyjf:validmessage key="add_money_count" label="累计出借金额"></hyjf:validmessage>
									
								</div>
								<div class="form-group">
									<label>用户出借日期段</label>
									<div class="input-group input-daterange datepicker">
												<span class="input-icon">
													<input type="text" name="add_time_begin" id="add_time_begin" class="form-control underline" value="${smsCode.add_time_begin}"/>
													<i class="ti-calendar"></i> </span>
												<span class="input-group-addon no-border bg-light-orange">~</span>
												<input type="text" name="add_time_end" id="add_time_end" class="form-control underline"  value="${smsCode.add_time_end}" />
									</div>	
								</div>
								<div class="form-group">
									<label>用户注册日期段</label> 
									<div class="input-group input-daterange datepicker" width="10" height="10">
												<span class="input-icon">
													<input type="text" name="re_time_begin" id="re_time_begin" class="form-control underline"  value="${smsCode.re_time_begin}"/>
													<i class="ti-calendar"></i> </span>
												<span class="input-group-addon no-border bg-light-orange">~</span>
												<input type="text" name="re_time_end" id="re_time_end" class="form-control underline" value="${smsCode.re_time_end}"/>
									</div>	
								</div>
							<input class="btn btn-sm btn-primary btn-o" type="submit" value="确定"/>
							<input class="btn btn-sm btn-primary btn-o" style="width:50px" onclick="resetQuery()"  value="重置"/>
						</div>
						</th>
					</tr>
				</table>
			</form>
			<br/>
			<div id="div_updateMessage">
					此条件下共${user_number}个用户,剩余短信：${remain_number},短信余额：${remain_money},上个月发送数量：${lastMonth_number}
			</div>
			
			<div>
				<form id="sendMessageForm" action="${pageContext.request.contextPath }/message/message/sendMessageAction" method="post">
					<div class="modal-body">
					<input type="hidden" name="open_account" id="open_account" value ="${smsCode.open_account}">
					<input type="hidden" name="add_money_count" value ="${smsCode.add_money_count}">
					<input type="hidden" name="add_time_begin" value ="${smsCode.add_time_begin}">
					<input type="hidden" name="add_time_end" value ="${smsCode.add_time_end}">
					<input type="hidden" name="re_time_begin" value ="${smsCode.re_time_begin}">
					<input type="hidden" name="re_time_end" value ="${smsCode.re_time_end}">
					<input type="hidden" id="user_number" name="user_number" value ="${user_number}">
					<input type="hidden" id="remain_number" name="remain_number" value ="${remain_number}">
					<input type="hidden" id="remain_money" name="remain_money" value ="${remain_money}">
					<input type="hidden" id="pagecontext" name="pagecontext" value ="${pageContext.request.contextPath }">
					
					
					
					手机号码：<textarea rows="10" cols="30" name="user_phones" id="user_phones"></textarea>多个号码请用英文半角逗号 “,” 隔开
					<br />
					短信内容：<textarea rows="10" cols="30" name="message" id="message" maxlength="494"></textarea><font color="red">发送的内容无需添加短信签名，系统会自动加上,70个字符以内短信0.04分一条,之后每67个字符算一条计费</font>
					<br />
					短信渠道：<input type="radio" id="channelTypeNormal" name="channelType" value="normal" checked="checked"> <label for="channelTypeNormal"> 普通渠道 </label> 
						    <input type="radio" id="channelTypeMaketing" name="channelType" value="marketing" > <label for="channelTypeMaketing"> 营销渠道 </label>
					<br />
					发送类型：<input type="radio" id="sendTypeNow" name="sendType" value="now" checked="checked" onclick="javascript:$('#timetime').css('display','none');"> <label for="sendTypeNow"> 立即发送 </label> 
						    <input type="radio" id="sendTypeOntime" name="sendType" value="ontime" onclick="javascript:$('#timetime').css('display','block');" > <label for="sendTypeOntime"> 定时发送 </label>
						<br />
						是否脱敏：<input type="radio" id="noDisplay" name="isDisplay" value=1> <label
							for="noDisplay">是</label>
						<input type="radio" id="display" name="isDisplay" value=0 checked="checked"> <label
							for="display">否</label>
			    	<div class="form-group" id="timetime" style="display:none">
						<label>定时发送时间</label>
						<div class="input-group" style="width:30%">
											<span class="input-icon input-icon-right">
												<input type="text" name="on_time" id="on_time" class="form-control input-sm"
														value="<c:out value="${smsCode.on_time}"></c:out>" maxlength="19" ignore="ignore"  datatype="*" 
														onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd HH:mm:ss', errDealMode: 1})"/>
												<i class="fa fa-calendar"></i>
											</span>
						</div>
					</div>
						
					</div>
					<div class="modal-body">		
					<input class="btn btn-sm btn-primary btn-o" type="button" value="提交" onclick="tijiao();"/>
						<input class="btn btn-sm btn-primary btn-o" type="reset" value="重置"/>
					</div>
				</form>
						
				
			</div>
			
		</tiles:putAttribute>
		<%-- 对话框面板 (ignore) --%>
		<tiles:putAttribute name="dialogPanels" type="string">
			<iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border:none;width:100%;height:100%"></iframe>
		</tiles:putAttribute>
		<tiles:putAttribute name="pageJavaScript" type="string">
		    <script type="text/javascript"> var webRoot = "${webRoot}";</script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/moment.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		    <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/i18n/zh-CN.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/sweetalert/sweet-alert.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/toastr/toastr.min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/message/sendMessage/sendMessage.js"></script> 
			<script type='text/javascript'>
				function resetQuery(){
					$("input[name=open_account]").prop("checked", false);
					$("#add_money_count").val("");
					$("#add_time_begin").val("");
					$("#add_time_end").val("");
					$("#re_time_begin").val("");
					$("#re_time_end").val("");
				}
			
			</script> 
			<script type='text/javascript'>
			function tijiao() {
				var mobile = $('#user_phones').val();  //手机号码
				var ontime = $('#on_time').val() == "" ? "未选择" : $('#on_time').val(); //定时发送时间
				var usernumber = $('#user_number').val(); //发送人数
				var msg = ""; //提示信息
				var method = $('#sendTypeNow').prop("checked") ? "立即发送" : ontime; //发送方式 
				if(mobile == ""){
					//手机号码未填
					msg = "发送用户数为："+usernumber+"人，请确认发送。 发送时间："+method;
				}else{
					//手机号码已填
					msg = "此信息为手写群发，请确认发送。 发送时间："+method;
				}
				
				if(confirm(msg)) {
					$('#sendMessageForm').submit();
				}	
					
			}
			</script> 
		</tiles:putAttribute>
	</tiles:insertTemplate>
 </shiro:hasPermission>
