<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="会员详情" />
		<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<form id="mainForm" action="updateAccountAction" method="post"  role="form" class="form-horizontal" >
					<%-- 角色列表一览 --%>
					<input type="hidden" name="userId" id="userId" value="${updateAccountForm.userId }" />
					<input type="hidden" id="success" value="${success}" />
					<input type="hidden" id="hasError" value="${hasError}" />
					<div class="panel-scroll height-230 margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="realName">
								<span class="symbol required"></span>查询方式：
							</label>
							<div class="col-xs-10">
								<input value="1" name="selecttype" type="radio" checked onclick="selectbymobile()" >手机号码查询
								<input value="2" name="selecttype" type="radio" onclick="selectbyidcard()">按身份证查询
							</div>
						</div>
						<div class="form-group" id="selectid">
                            <label class="col-xs-2 control-label text-right padding-right-0" for="realName">
                                <span class="symbol required"></span>查询号码：
                            </label>
                            <div class="col-xs-10">
                                <input type="text" onclick="deletespan()" placeholder="查询号码" class="form-control input-sm" id="selectno" name="selectno" value="" maxlength="11" datatype="n11-11" errormsg="手机号码 只能是11位数字!">
                            </div>
                           <%-- <c:choose>
                                <c:when test="${1==1}">
                                    <label class="col-xs-2 control-label text-right padding-right-0" for="realName">
                                        <span class="symbol required"></span>查询号码
                                    </label>
                                    <div class="col-xs-10">
                                        <input type="text" placeholder="查询号码" class="form-control input-sm" id="selectno" name="selectno" value="" maxlength="11" datatype="n11-11" errormsg="手机号码 只能是11位数字!">
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <label class="col-xs-2 control-label text-right padding-right-0" for="realName">
                                        <span class="symbol required"></span>查询身份证
                                    </label>
                                    <div class="col-xs-10">
                                        <input type="text" placeholder="查询身份证" class="form-control input-sm" id="selectno" name="selectno" value="" maxlength="18" datatype="s18-18" errormsg="请输入正确的身份证号码!">
                                    </div>
                                </c:otherwise>
                            </c:choose>--%>
                                    <span class="Validform_wrong" id="deletespan">
                                    <c:if test="${errormsg != null}">
                                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;${errormsg}
                                    </c:if>
                                </span>
						</div>

						<%-- <div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="userName">
								<span class="symbol required"></span>用户名
							</label>
							<div class="col-xs-10">
								<span class="badge badge-inverse"> ${updateAccountForm.username} </span>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="realName">
								<span class="symbol required"></span>姓名
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="姓名" class="form-control input-sm" id="realName" name=realName value="${realName}" datatype="s2-18">
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="idCard"> 
								<span class="symbol  required"></span>身份证号
							</label>
							<div class="col-xs-10">
								<input type="text" placeholder="身份证号" class="form-control input-sm" id="idCard" name=idCard value="${idCard}" datatype="s18-18">
								<hyjf:validmessage key="idCard" label="身份证号"></hyjf:validmessage>
							</div>
						</div> --%>

					</div>
					<div class="form-group margin-bottom-0">
						<div class="col-xs-offset-2 col-xs-10">
							<a class="btn btn-o btn-primary fn-Confirm"><i class="fa fa-check"></i> 确 认</a>
							<a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
						</div>
					</div>
				</form>
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
		<script type='text/javascript' src="${webRoot}/jsp/exception/openaccountexception/updateAccount.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>