<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="推荐人修改申请" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<div class="panel panel-white no-margin">
			<div class="panel-body">
				<form id="mainForm" action="addOrSaveAdminStockInfoAction"
						method="post"  role="form" class="form-horizontal" >
					<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
					<input type="hidden" id="success" value="${success}" />
					<%-- 角色列表一览 --%>
					<div class="panel-scroll margin-bottom-15">
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="id"> 
								<span class="symbol required"></span>id
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="id" class="form-control" id="id" name="id"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.id}">
								<hyjf:validmessage key="id" label="id"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="nowPrice"> 
								<span class="symbol required"></span>当前价格
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="当前价格" class="form-control" id="nowPrice" name="nowPrice"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.nowPrice}">
								<hyjf:validmessage key="nowPrice" label="当前价格"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="increase"> 
								<span class="symbol required"></span>涨幅 百分数
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="涨幅 百分数" class="form-control" id="increase" name="increase"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.increase}">
								<hyjf:validmessage key="increase" label="涨幅 百分数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="decline"> 
								<span class="symbol required"></span>跌幅 百分数
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="跌幅 百分数" class="form-control" id="decline" name="decline"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.decline}">
								<hyjf:validmessage key="decline" label="跌幅 百分数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="volume"> 
								<span class="symbol required"></span>成交量
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="成交量" class="form-control" id="volume" name="volume"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.volume}">
								<hyjf:validmessage key="volume" label="成交量"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="date"> 
								<span class="symbol required"></span>date
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="date" class="form-control" id="date" name="date"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.date}">
								<hyjf:validmessage key="date" label="date"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="previousClosePrice"> 
								<span class="symbol required"></span>昨日收盘
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="昨日收盘" class="form-control" id="previousClosePrice" name="previousClosePrice"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.previousClosePrice}">
								<hyjf:validmessage key="previousClosePrice" label="昨日收盘"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="dayLow"> 
								<span class="symbol required"></span>最低
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="最低" class="form-control" id="dayLow" name="dayLow"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.dayLow}">
								<hyjf:validmessage key="dayLow" label="最低"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="dayHigh"> 
								<span class="symbol required"></span>最高
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="最高" class="form-control" id="dayHigh" name="dayHigh"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.dayHigh}">
								<hyjf:validmessage key="dayHigh" label="最高"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="openPrice"> 
								<span class="symbol required"></span>今日开盘
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="今日开盘" class="form-control" id="openPrice" name="openPrice"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.openPrice}">
								<hyjf:validmessage key="openPrice" label="今日开盘"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="marketCap"> 
								<span class="symbol required"></span>总市值
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="总市值" class="form-control" id="marketCap" name="marketCap"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.marketCap}">
								<hyjf:validmessage key="marketCap" label="总市值"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="peRatio"> 
								<span class="symbol required"></span>市盈率
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="市盈率" class="form-control" id="peRatio" name="peRatio"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.peRatio}">
								<hyjf:validmessage key="peRatio" label="市盈率"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-xs-2 control-label text-right padding-right-0" for="eps"> 
								<span class="symbol required"></span>每股收益
							</label>
							<div class="col-xs-5">
								<input type="text" placeholder="每股收益" class="form-control" id="eps" name="eps"
									datatype="s1-30" maxlength="30" nullmsg="请输入" errormsg="不正确" flag="true" value="${AdminStockInfoForm.eps}">
								<hyjf:validmessage key="eps" label="每股收益"></hyjf:validmessage>
							</div>
						</div>
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
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
		<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
		<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/adminstockinfo/adminStockInfoInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
