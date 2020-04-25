<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="添加项目类型" />
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.panel-title { font-family: "微软雅黑" }
	.admin-select .select2-container { 
		width: 100% !important;
	}
	.admin-select .select2-container--default .select2-selection--single { 
		border-radius: 0px;
		border: 1px solid #BBBAC0 !important;
	}
	.admin-select .select2-container--default .select2-selection--single .select2-selection__rendered, .admin-select .select2-container--default .select2-selection--single { 
		height: 34px;
		line-height:34px;
	}
	.admin-select .select2-container .select2-selection--single .select2-selection__rendered {
		padding-left: 4px;
	}
	</style>
	<link href="${themeRoot}/vendor/plug-in/select2/select2.min.css" rel="stylesheet" media="screen">
	</tiles:putAttribute>
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
		<c:set var="jspEditType" value="${empty htjconfigForm.ids ? '添加' : '修改'}"></c:set>
		<div class="panel panel-white" style="margin:0">
			<div class="panel-body">
				<p class="text-small margin-bottom-20">
					在这里可以添加和修改项目类型。
				</p>
				<hr/>
				<div class="panel-scroll height-535">
					<form id="mainForm" class="form-horizontal" action="${empty htjconfigForm.ids ? 'insertAction' : 'updateAction'}" method="post"  role="form" class="form-horizontal" >
						<%-- 角色列表一览 --%>
						<input type="hidden" name="ids" id="ids" value="${htjconfigForm.ids }" />
						<input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}" />
						<input type="hidden" id="success" value="${success}" />
						
						<c:if test="${empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanTypeName">产品名称:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanTypeName" name="debtPlanTypeName" value="<c:out value="${htjconfigForm.debtPlanTypeName}"></c:out>"
											class="form-control" datatype="*1-50" errormsg="产品名称应该输入1~50个字符!" ajaxurl="checkAction" maxlength="50"/>
									</div>
									<hyjf:validmessage key="debtPlanTypeName" label="产品名称"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${!empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanTypeName">产品名称:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanTypeName" name="debtPlanTypeName" value="<c:out value="${htjconfigForm.debtPlanTypeName}"></c:out>"
											class="form-control" disabled="disabled" maxlength="50"/>
									</div>
									<hyjf:validmessage key="debtPlanTypeName" label="产品名称"></hyjf:validmessage>
									<input type="hidden" name="debtPlanTypeName" id="debtPlanTypeName" value="<c:out value="${htjconfigForm.debtPlanTypeName}"></c:out>" />
								</div>
							</div>
						</c:if>
						<c:if test="${empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanType">产品ID:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanType" name="debtPlanType" value="<c:out value="${htjconfigForm.debtPlanType}"></c:out>"
											class="form-control" datatype="n1-10" nullmsg="请填写产品ID" errormsg="产品ID应该输入1~10个数字!" ajaxurl="checkAction" maxlength="10"/>
									</div>
									<hyjf:validmessage key="debtPlanType" label="产品ID"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${!empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanType">产品ID:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanType" name="debtPlanType" value="<c:out value="${htjconfigForm.debtPlanType}"></c:out>"
											class="form-control" disabled="disabled" maxlength="10"/>
									</div>
									<hyjf:validmessage key="debtPlanType" label="产品ID"></hyjf:validmessage>
									<input type="hidden" name="debtPlanType" id="debtPlanType" value="<c:out value="${htjconfigForm.debtPlanType}"></c:out>" />
								</div>
							</div>
						</c:if>
						<c:if test="${empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanPrefix">编号前缀:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanPrefix" name="debtPlanPrefix" value="<c:out value="${htjconfigForm.debtPlanPrefix}"></c:out>"
											class="form-control" datatype="*1-20" nullmsg="请填写编号前缀" errormsg="编号前缀应该输入1~20个字符!" maxlength="20"/>
									</div>
									<hyjf:validmessage key="debtPlanType" label="产品ID"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${!empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtPlanPrefix">编号前缀:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtPlanPrefix" name="debtPlanPrefix" value="<c:out value="${htjconfigForm.debtPlanPrefix}"></c:out>"
											class="form-control" disabled="disabled" maxlength="20"/>
									</div>
									<hyjf:validmessage key="debtPlanType" label="编号前缀"></hyjf:validmessage>
									<input type="hidden" name="debtPlanPrefix" id="debtPlanPrefix" value="<c:out value="${htjconfigForm.debtPlanPrefix}"></c:out>" />
								</div>
							</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="debtMinInvestment">最低授权服务金额:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="debtMinInvestment" name="debtMinInvestment" value="<c:out value="${htjconfigForm.debtMinInvestment}"></c:out>"
										class="form-control" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/" nullmsg="请填写最低授权服务金额" errormsg="最低授权服务金额必须为数字,并且必须是1000的倍数" maxlength="10"/>
										<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="debtMinInvestment" label="最低授权服务金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="debtInvestmentIncrement">递增金额:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="debtInvestmentIncrement" name="debtInvestmentIncrement" value="<c:out value="${htjconfigForm.debtInvestmentIncrement}"></c:out>"
										class="form-control" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/"  errormsg="递增金额必须为数字,并且必须是1000的倍数" nullmsg="请填写出借增量"  maxlength="10"/>
										<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="debtInvestmentIncrement" label="递增金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="debtMaxInvestment">最高授权服务金额:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="debtMaxInvestment" name="debtMaxInvestment" value="<c:out value="${htjconfigForm.debtMaxInvestment}"></c:out>"
										class="form-control" datatype="/^[1-9]\d*000$/|/^[1-9]\d*000.00$/" nullmsg="请填写最高授权服务金额" errormsg="最高授权服务金额必须为数字,并且必须是1000的倍数" ajaxurl="checkAction?debtMinInvestment=${htjconfigForm.debtMinInvestment}" maxlength="10"/>
										<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="debtMaxInvestment" label="最高授权服务金额"></hyjf:validmessage>
							</div>
						</div>
						<c:if test="${!empty htjconfigForm.ids }">
							<div class="form-group">
								<label class="col-sm-3 control-label" for="debtLockPeriod">服务回报期限:<span class="symbol required"></span></label>
								<div class="col-sm-5">
									<div class="input-group">
										<input type="text" id="debtLockPeriod" name="debtLockPeriod" disabled="disabled" value="<c:out value="${htjconfigForm.debtLockPeriod}"></c:out>"
											class="form-control" datatype="n1-2" nullmsg="请填写服务回报期限" errormsg="服务回报期限必须为1-2位数字！" maxlength="2"/>
											<span class="input-group-addon">个月</span>
									</div>
									<hyjf:validmessage key="debtLockPeriod" label="服务回报期限"></hyjf:validmessage>
								</div>
							</div>
						</c:if>
						<c:if test="${empty htjconfigForm.ids }">
							<div class="form-group">
									<label class="col-sm-3 control-label" for="debtLockPeriod">服务回报期限:<span class="symbol required"></span></label>
									<div class="col-sm-5">
										<div class="input-group">
											<input type="text" id="debtLockPeriod" name="debtLockPeriod" value="<c:out value="${htjconfigForm.debtLockPeriod}"></c:out>"
												class="form-control" datatype="n1-2" nullmsg="请填写服务回报期限" ajaxurl="checkAction" errormsg="服务回报期限必须为1-2位数字！" maxlength="2"/>
												<span class="input-group-addon">个月</span>
										</div>
										<hyjf:validmessage key="debtLockPeriod" label="服务回报期限"></hyjf:validmessage>
									</div>
								</div>
						</c:if>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="debtQuitPeriod">退出所需天数:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="debtQuitPeriod" name="debtQuitPeriod" value="<c:out value="${htjconfigForm.debtQuitPeriod}"></c:out>"
										class="form-control" datatype="n1-2" nullmsg="请填写退出所需天数" errormsg="退出所需天数必须为1-2位数字！" maxlength="2"/>
										<span class="input-group-addon">天</span>
								</div>
								<hyjf:validmessage key="debtQuitPeriod" label="退出所需天数"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label" for="investAccountLimit">汇添金专属资产最后一笔授权服务金额:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="investAccountLimit" name="investAccountLimit" value="<c:out value="${htjconfigForm.investAccountLimit}"></c:out>"
										class="form-control" datatype="/^\d{1,10}(\.\d{1,2})?$/" nullmsg="请填写汇添金专属资产最后一笔授权服务金额" errormsg="汇添金专属资产最后一笔授权服务金额必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13"/>
										<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="investAccountLimit" label="汇添金专属资产最后一笔授权服务金额"></hyjf:validmessage>
							</div>
						</div>
						
						<div class="form-group">
							<label class="col-sm-3 control-label" for="minSurplusInvestAccount">债转成交下限:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="minSurplusInvestAccount" name="minSurplusInvestAccount" value="<c:out value="${htjconfigForm.minSurplusInvestAccount}"></c:out>"
										class="form-control" datatype="/^\d{1,10}(\.\d{1,2})?$/" nullmsg="请填写债转成交下限" errormsg="债转成交下限必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="10"/>
										<span class="input-group-addon">元</span>
								</div>
								<hyjf:validmessage key="minSurplusInvestAccount" label="债转成交下限"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="minInvestNumber">承接债转本金拆分笔数下限:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="minInvestNumber" name="minInvestNumber" value="<c:out value="${htjconfigForm.minInvestNumber}"></c:out>"
										class="form-control" datatype="n1-2" nullmsg="请填写承接债转本金拆分笔数下限" errormsg="承接债转本金拆分笔数下限必须为1-2位数字！" maxlength="2"/>
								</div>
								<hyjf:validmessage key="minInvestNumber" label="承接债转本金拆分笔数下限"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="maxInvestNumber">承接债转本金拆分笔数上限:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="maxInvestNumber" name="maxInvestNumber" value="<c:out value="${htjconfigForm.maxInvestNumber}"></c:out>"
										class="form-control" datatype="n1-2" nullmsg="请填写承接债转本金拆分笔数上限" errormsg="承接债转本金拆分笔数上限必须为1-2位数字！" maxlength="2"/>
								</div>
								<hyjf:validmessage key="maxInvestNumber" label="承接债转本金拆分笔数下限"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="cycleTimes">遍历次数:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="cycleTimes" name="cycleTimes" value="<c:out value="${htjconfigForm.cycleTimes}"></c:out>"
										class="form-control" datatype="n2-2" nullmsg="请填写遍历次数" errormsg="遍历次数必须为2位数字,且最小为10" min="10" maxlength="2"/>
								</div>
								<hyjf:validmessage key="cycleTimes" label="遍历次数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="unableCycleTimes">无视规则出借次数:<span class="symbol required"></span></label>
							<div class="col-sm-5">
								<div class="input-group">
									<input type="text" id="cycleTimes" name="unableCycleTimes" value="<c:out value="${htjconfigForm.unableCycleTimes}"></c:out>"
										class="form-control" datatype="n1-2" nullmsg="请填写无视规则出借次数" errormsg="遍历次数必须为数字,最小为0" min="0" maxlength="2"/>
								</div>
								<hyjf:validmessage key="unableCycleTimes" label="遍历次数"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="roundAmount">取整金额:<span class="symbol required"></span></label>
							<div class="col-sm-9">
								<div class="input-group">
									<select name="roundAmount" id="roundAmount" class="form-control underline form-select2" style="width: 100px;" datatype="*" errormsg="请填写取整金额!"
									data-placeholder="请选择取整金额">
										<option value="10" <c:if test="${htjconfigForm.roundAmount=='10.00'|| htjconfigForm.roundAmount=='10'}">selected="selected"</c:if>>10</option>
										<option value="100" <c:if test="${htjconfigForm.roundAmount=='100.00' || htjconfigForm.roundAmount=='100'}">selected="selected"</c:if>>100</option>
										<option value="1000" <c:if test="${htjconfigForm.roundAmount=='1000.00'}">selected="selected"</c:if>>1000</option>
										<option value="10000" <c:if test="${htjconfigForm.roundAmount=='10000.00'}">selected="selected"</c:if>>10000</option>
									</select>
								</div>
								<hyjf:validmessage key="roundAmount" label="取整金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="roundAmount">是否可用券:<span class="symbol required"></span></label>
							<div class="col-sm-9">
								<div class="input-group">
									<div class="checkbox clip-check check-primary checkbox-inline">
										<input type="checkbox" id="checkAllUse" value="all"  <c:if test="${ fn:contains(htjconfigForm.couponConfig, '1') and fn:contains(htjconfigForm.couponConfig, '2') and fn:contains(htjconfigForm.couponConfig, '3') }">checked</c:if> />
										<label for="checkAllUse">全部</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="couponConfig" id="couponConfigTYJ" value="1" class="checkAllUsebox"  <c:if test="${fn:contains(htjconfigForm.couponConfig, '1')}">checked</c:if> />
										<label for="couponConfigTYJ">体验金</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="couponConfig" id="couponConfigJXQ" value="2" class="checkAllUsebox" <c:if test="${fn:contains(htjconfigForm.couponConfig, '2')}">checked</c:if> />
										<label for="couponConfigJXQ">加息券</label>
									</div>
									<div class="checkbox clip-check check-info checkbox-inline">
										<input type="checkbox" name="couponConfig" id="couponConfigDJQ" value="3" class="checkAllUsebox" <c:if test="${fn:contains(htjconfigForm.couponConfig, '3')}">checked</c:if> />
										<label for="couponConfigDJQ">代金券</label>
									</div>
								</div>
								<hyjf:validmessage key="roundAmount" label="取整金额"></hyjf:validmessage>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="status"> 
								<span class="symbol required"></span>状态
							</label>
							<div class="col-sm-5">
								<div class="radio clip-radio radio-primary ">
									<input type="radio" id="stateOn" name="status" value="1"  datatype="*" ${ ( htjconfigForm.status eq '1' ) ? 'checked' : ''}> <label for="stateOn"> 启用 </label> 
									<input type="radio" id="stateOff" name="status" value="0" datatype="*" ${( empty htjconfigForm.status || htjconfigForm.status eq 0 ) ? 'checked' : ''}> <label for="stateOff"> 关闭 </label>
									<hyjf:validmessage key="status" label="状态"></hyjf:validmessage>
								</div>
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-3 control-label" for="remark"> <span class="symbol required"></span>常见问题 </label>
							<div class="col-sm-8">
								<textarea placeholder="" id="remark" name="remark" cols="10" rows="12" class="tinymce" 
									datatype="*" nullmsg="请填写常见问题">
									<c:set value="<p>1、汇添金出借计划安全吗？</p>" var="str1"></c:set>
									<c:set value="<p>汇盈金服平台以严谨、负责的态度对汇添金出借计划内每笔融资项目进行严格筛选并挑选出优质资产，计划内项目均适应于汇盈金服用户利益保障机制。</p>" var="str2"></c:set>
									<c:set value="<p>2、汇添金出借计划的“锁定期”是什么？</p>" var="str3"></c:set>
									<c:set value="<p>汇添金出借计划具有收益期锁定限制，锁定期内，用户不可以提前退出。</p>" var="str4"></c:set>
									<c:set value="<p>3、汇添金出借计划收益有多少？</p>" var="str5"></c:set>
									<c:set value="<p>汇添金出借计划预期出借利率与同期限“汇直投”项目的预期出借利率相同。</p>" var="str6"></c:set>
									<c:set value="<p>4、加入的汇添金出借计划的用户所获收益处理方式有几种？</p>" var="str7"></c:set>
									<c:set value="<p>本计划提供两种收益处理方式：循环复投或用户自由支配。计划退出后，用户的本金和收益将返回至其汇盈金服账户中，供用户自行支配。</p>" var="str8"></c:set>
									<c:set value="<p>5、汇添金出借计划通过何种方式为用户实现自动投标？</p>" var="str9"></c:set>
									<c:set value="<p>汇添金出借计划不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属计划账户，所有资金通过该专属计划账户流动。</p>" var="str10"></c:set>
									<c:set value="<p>6、汇添金出借计划到期后，如何退出并实现收益?</p>" var="str11"></c:set>
									<c:set value="<p>汇添金出借计划到期后，系统将自动进行资金结算，结算完成后的本金及收益将从用户汇添金计划账户自动划转至用户普通账户中，用户在T+3个工作日内收到资金。</p>" var="str12"></c:set>
									<c:set value="<p>汇盈金服仅为信息发布平台，未以任何明示或暗示的方式对出借人提供担保或承诺保本保息，出借人应根据自身的出借偏好和风险承受能力进行独立判断和作出决策，并自行承担资金出借的风险与责任。</p>" var="str13"></c:set>
									<c:set value="<p>市场有风险，出借需谨慎。</p>" var="str14"></c:set>
									<c:if test="${htjconfigForm.remark!=null and htjconfigForm.remark != ''}">
										<c:out value="${htjconfigForm.remark}"></c:out>
									</c:if>
									<c:if test="${htjconfigForm.remark ==null or htjconfigForm.remark == ''}">
										<c:out value="${str1 }"></c:out>
										<c:out value="${str2 }"></c:out>
										<c:out value="${str3 }"></c:out>
										<c:out value="${str4 }"></c:out>
										<c:out value="${str5 }"></c:out>
										<c:out value="${str6 }"></c:out>
										<c:out value="${str7 }"></c:out>
										<c:out value="${str8 }"></c:out>
										<c:out value="${str9 }"></c:out>
										<c:out value="${str10 }"></c:out>
										<c:out value="${str11 }"></c:out>
										<c:out value="${str12 }"></c:out>
										<c:out value="${str13 }"></c:out>
										<c:out value="${str14 }"></c:out>
									</c:if>
									</textarea>
								<hyjf:validmessage key="remark" label="常见问题"></hyjf:validmessage>
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
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
		<script type='text/javascript' src="${webRoot}/jsp/manager/config/htjconfig/htjconfigInfo.js"></script>
	</tiles:putAttribute>
</tiles:insertTemplate>
