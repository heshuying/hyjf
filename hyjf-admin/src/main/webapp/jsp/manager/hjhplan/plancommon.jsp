<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="hyjf" uri="/hyjf-tags" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>
<%-- 画面功能路径 (ignore) --%>
<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="智投发起"/>
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
                    <li class='s-ellipsis active'><a href="#tab_jbxx_1"
                                                     data-toggle="tab"><i class="fa fa-edit"></i> 基本信息</a></li>
                    <li><a href="#tab_jhjs_2" data-toggle="tab"><i
                            class="fa fa-cubes"></i> 智投介绍</a></li>
                    <li><a href="#tab_aqbz_3" data-toggle="tab"><i
                            class="fa fa-folder-open"></i> 常见问题</a></li>
                </ul>
                <form id="mainForm" action="insertAction" method="post" role="form">
                    <div class="tab-content">
                        <!-- Start:基本信息 -->
                        <div
                                <c:if test="${planForm.tabName == null or planForm.tabName =='' }"> class='tab-pane fade in active'</c:if>
                                <c:if test="${planForm.tabName != null and planForm.tabName !='' }"> class='tab-pane fade'</c:if>
                                id="tab_jbxx_1">
                            <p>
                                <i class="ti-info-alt text-primary"></i>
                                请在这里填写您本次发起智投的基本信息，以做存档.
                            </p>
                            <hr/>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="panel">
                                        <div style="overflow: hidden">
                                            <div class="form-group" style="float: left; width: 33%">
                                                <label class="control-label"> 智投编号 <span
                                                        class="symbol required"></span>
                                                </label>
                                                <div class="input-group" style="width: 52%">
                                                    <input type="text" id="debtPlanNid" name="debtPlanNid"
                                                           class="form-control input-sm"
                                                           value='<c:out value="${planInfoForm.debtPlanNid}"></c:out>'
                                                           <c:if test="${!empty planInfoForm.debtPlanNid }">readonly='readonly'</c:if>
                                                           onkeyup="value=value.replace(/[\W]/g,'') "
                                                           onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
                                                           maxlength="20" datatype="*1-20" nullmsg="未填写智投编号"
                                                           errormsg="智投编号长度不能超过20位字符"
                                                           <c:if test="${empty planInfoForm.debtPlanNid}">ajaxurl="isDebtPlanNidExist"</c:if> />

                                                </div>
                                                <hyjf:validmessage key="debtPlanNid" label="智投编号"></hyjf:validmessage>
                                            </div>
                                            <div class="form-group" style="float: left; width: 33%">
                                                <label class="control-label"> 智投名称 <span
                                                        class="symbol required"></span>
                                                </label>
                                                <div class="input-group" style="width: 52%">
                                                    <input type="text" name="debtPlanName" id="debtPlanName"
                                                           class="form-control input-sm"
                                                           value='<c:out value="${planInfoForm.debtPlanName}"></c:out>'
                                                            <c:if test="${!empty planInfoForm.debtPlanName }"></c:if>
                                                           maxlength="20" datatype="*1-20" nullmsg="未填写智投名称"
                                                           errormsg="智投名称长度不能超过20位字符"
                                                           ajaxurl="isDebtPlanNameExist?debtPlanNid=${planInfoForm.debtPlanNid}"/>
                                                </div>
                                                <hyjf:validmessage key="debtPlanName"></hyjf:validmessage>
                                            </div>
                                            <div class="form-group" style="float: left; width: 34%"></div>
                                        </div>
                                        <div style="overflow: hidden">
                                            <div class="form-group" style="float: left; width: 33%">
                                                <label class="control-label" for="borrowStyle"> 还款方式<span
                                                        class="symbol required"></span>
                                                </label>
                                                <div class="input-group input-of">
                                                    <style>
                                                        .form-group .input-group.input-of .select2-selection {
                                                            overflow: hidden;
                                                        }
                                                    </style>
                                                    <select name="borrowStyle" id="borrowStyle"
                                                            class="form-control underline form-select2"
                                                            　style="width: 200%" data-placeholder="请选还款方式..."
                                                            data-allow-clear="true" datatype="*" nullmsg="未选择还款方式">
                                                        <option value=""></option>

                                                        <option value="endday"
                                                                <c:if test="${planInfoForm.borrowStyle eq 'endday'}">selected="selected"</c:if>>
                                                            按天计息，到期还本还息
                                                        </option>
                                                        <option value="end"
                                                                <c:if test="${planInfoForm.borrowStyle eq 'end'}">selected="selected"</c:if>>
                                                            按月计息，到期还本还息
                                                        </option>
                                                    </select>
                                                </div>
                                            </div>
                                            <div class="form-group" id="expType-1" style="float: left; width: 33%">
                                                <label class="control-label">
                                                    服务回报期限 <span class="symbol required"></span>
                                                </label>
                                                <div class="input-group" style="width:52%">
                                                    <input type="text" id="lockPeriod" maxlength="3"
                                                           datatype="n1-10,range" data-minvalue="1" data-maxvalue="999"
                                                           nullmsg="未填写服务回报期限" name="lockPeriod"
                                                           class="form-control input-sm"
                                                           value='<c:out value="${planInfoForm.lockPeriod}"></c:out>'
                                                            <c:if test="${!empty planInfoForm.lockPeriod }"></c:if>/>
                                                    <!-- 服务回报期限时间类型 -->
                                                    <span class="input-group-addon"
                                                          style="padding: 4px 12px">
											<input  type="radio" value="0" name="isMonth" datatype="*"
                                                   <c:if test="${planInfoForm.isMonth eq '0' }">checked</c:if>
                                                   disabled
                                                   id="isMonthD">
											<label for="isMonthD" style="color:#fff">天</label>
											<input  type="radio" value="1" name="isMonth" datatype="*"
                                                   <c:if test="${ planInfoForm.isMonth eq '1' }">checked</c:if>
                                                   disabled
                                                   id="isMonthM">
											<label for="isMonthM" style="color:#fff">月</label>
										</span>
                                                </div>
                                                <hyjf:validmessage key="lockPeriod"></hyjf:validmessage>

                                            </div>
                                            <div class="form-group" style="float: left; width: 34%"></div>
                                        </div>

                                        <div style="overflow: hidden">
                                            <div class="form-group" style="float: left; width: 33%">
                                                <label class="control-label">
                                                    参考年回报率<span class="symbol required"></span>
                                                </label>
                                                <div class="input-group" style="width:52%">
                                                    <c:if test="${ ( planInfoForm.debtPlanStatus eq '2' ) }">
                                                        <input type="text" id="expectApr" name="expectApr"
                                                               class="form-control input-sm"
                                                               value='<c:out value="${planInfoForm.expectApr}"></c:out>'/>
                                                    </c:if>
                                                    <c:if test="${ ( planInfoForm.debtPlanStatus ne '2' ) }">
                                                        <input type="text" id="expectApr" name="expectApr"
                                                               class="form-control input-sm"
                                                               datatype="/^\d{1,2}(\.\d{1,2})?$/" maxlength="5"
                                                               errormsg="参考年回报率必须为数字，整数部分不能超过2位，小数部分不能超过2位！"
                                                               nullmsg="未填写参考年回报率"
                                                               value='<c:out value="${planInfoForm.expectApr}"></c:out>'/>
                                                    </c:if>
                                                    <span class="input-group-addon">%</span>
                                                </div>
                                            </div>
                                            <div class="form-group" style="float: left; width: 33%">
                                                <label class="control-label">
                                                    最低授权服务金额 <span class="symbol required"></span>
                                                </label>
                                                <div class="input-group" style="width:52%">
                                                    <c:set value="${ fn:split(planInfoForm.debtMinInvestment, '.') }"
                                                           var="debtMinInvest"/>
                                                    <input type="text" id="debtMinInvestment" name="debtMinInvestment"
                                                           class="form-control input-sm"
                                                           value='<c:out value="${debtMinInvest[0]}"></c:out>'
                                                           maxlength="10" datatype="n1-10,range" data-minvalue="1"
                                                           data-maxvalue="9999999999" nullmsg="未填写最低授权服务金额"
                                                           errormsg="授权服务金额至少为1元，最大不能超过9999999999元，且必须为整数"
                                                    />
                                                    <span class="input-group-addon">元</span>
                                                </div>
                                                <hyjf:validmessage key="debtMinInvestment"
                                                                   label="最低授权服务金额 "></hyjf:validmessage>
                                                <hyjf:validmessage key="debtMinInvestment-error"></hyjf:validmessage><!-- 原debtPlanMoney-error -->
                                            </div>
                                            <div class="form-group" style="float: left; width: 34%"></div>
                                        </div>
                                        <div style="overflow: hidden">
                                            <div class="form-group" style="float:left; width:33%">
                                                <label class="control-label">
                                                    最高授权服务金额 <span class="control-label"></span>
                                                </label>
                                                <div class="input-group" style="width:52%">
                                                    <c:set value="${ fn:split(planInfoForm.debtMaxInvestment, '.') }"
                                                           var="debtMaxInvest"/>
                                                    <input type="text" id="debtMaxInvestment" name="debtMaxInvestment"
                                                           class="form-control input-sm"
                                                           value='<c:out value="${debtMaxInvest[0]}"></c:out>'
                                                           datatype="compareMinInvest"
                                                           errormsg="最高授权服务金额至少为1元，且必须为整数"
                                                    />
                                                    <span class="input-group-addon">元</span>
                                                </div>
                                                <!--<hyjf:validmessage key="debtMaxInvestment" label="最高服务授权金额"></hyjf:validmessage>-->
                                                <!--<hyjf:validmessage key="debtMaxInvestment-error"></hyjf:validmessage>-->
                                                <!-- 原debtPlanMoney-error -->
                                            </div>
                                            <div class="form-group" style="float: left; width: 33%">
									<label class=" control-label
                                            ">
                                            递增金额 <span class="symbol required"></span>
                                            </label>
                                            <div class="input-group" style="width:136%">
                                                <c:set value="${ fn:split(planInfoForm.debtInvestmentIncrement, '.') }"
                                                       var="debtInvestmentIncre"/>
                                                <input type="text" id="debtInvestmentIncrement"
                                                       name="debtInvestmentIncrement" class="form-control input-sm"
                                                       value='<c:out value="${debtInvestmentIncre[0]}"></c:out>'
                                                       maxlength="10" datatype="n1-10,range,compareMaxInvest" data-minvalue="1"
                                                       data-maxvalue="9999999999" nullmsg="未填写递增金额"
                                                       errormsg="计划金额至少为1元，最大不能超过9999999999元，且必须为整数"/>
                                                <span class="input-group-addon">元</span>
                                            </div>
                                            <hyjf:validmessage key="debtInvestmentIncrement"
                                                               label="递增金额"></hyjf:validmessage>
                                            <hyjf:validmessage key="debtInvestmentIncrement-error"></hyjf:validmessage><!-- 原debtPlanMoney-error -->
                                        </div>
                                        <div class="form-group" style="float: left; width: 34%"></div>
                                    </div>
                                    <div style="overflow: hidden">
                                        <div class="form-group" style="float: left; width: 33%">
                                            <label class="control-label">
                                                最小自动投标笔数<span class="symbol required"></span>
                                            </label>
                                            <div class="input-group" style="width:52%">
                                                <c:set value="${ fn:split(planInfoForm.minInvestCounts, '.') }"
                                                       var="minInvestCounts"/>
                                                <input type="text" id="minInvestCounts" name="minInvestCounts"
                                                       class="form-control input-sm"
                                                       value='<c:out value="${planInfoForm.minInvestCounts}"></c:out>'
                                                       maxlength="2" datatype="/^([1-9]\d*)(\.\d*[1-9])?$/"
                                                       data-minvalue="1" data-maxvalue="99" nullmsg="请填写最小自动投标笔数"
                                                       errormsg="最小自动投标笔数应为正整数，请重新填写"
                                                />
                                            </div>
                                            <hyjf:validmessage key="minInvestCounts"
                                                               label="最小自动投标笔数"></hyjf:validmessage>
                                            <hyjf:validmessage key="minInvestCounts-error"></hyjf:validmessage><!-- 原debtPlanMoney-error -->
                                        </div>
                                        <div class="form-group" style="float: left; width: 33%">
                                            <label class="control-label">
                                                是否可用券
                                            </label>
                                            <div class="input-group" style="width:80%">
                                                <div class="checkbox clip-check check-primary checkbox-inline">
                                                    <input type="checkbox" id="checkAllUse" value="all"
                                                           <c:if test="${ fn:contains(planInfoForm.couponConfig, '1') and fn:contains(planInfoForm.couponConfig, '2') and fn:contains(planInfoForm.couponConfig, '3') }">checked</c:if> />
                                                    <label for="checkAllUse">全部</label>
                                                </div>
                                                <div class="checkbox clip-check check-info checkbox-inline">
                                                    <input type="checkbox" name="couponConfig" id="couponConfigTYJ"
                                                           value="1" class="checkAllUsebox"
                                                           <c:if test="${fn:contains(planInfoForm.couponConfig, '1')}">checked</c:if> />
                                                    <label for="couponConfigTYJ">体验金</label>
                                                </div>
                                                <div class="checkbox clip-check check-info checkbox-inline">
                                                    <input type="checkbox" name="couponConfig" id="couponConfigJXQ"
                                                           value="2" class="checkAllUsebox"
                                                           <c:if test="${fn:contains(planInfoForm.couponConfig, '2')}">checked</c:if> />
                                                    <label for="couponConfigJXQ">加息券</label>
                                                </div>
                                                <div class="checkbox clip-check check-info checkbox-inline">
                                                    <input type="checkbox" name="couponConfig" id="couponConfigDJQ"
                                                           value="3" class="checkAllUsebox"
                                                           <c:if test="${fn:contains(planInfoForm.couponConfig, '3')}">checked</c:if> />
                                                    <label for="couponConfigDJQ">代金券</label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div style="overflow: hidden">
                                        <div class="form-group" style="float: left; width: 33%">
                                            <label class="control-label">
                                                显示状态 <span class="symbol required"></span>
                                            </label>
                                            <div class="clip-radio radio-primary">
                                                <input type="radio" value="1" name="planDisplayStatusSrch" datatype="*"
                                                       <c:if test="${ planInfoForm.planDisplayStatusSrch eq '1' }">checked</c:if>
                                                       id="planDisplayStatusSrch_yes">
                                                <label for="planDisplayStatusSrch_yes">显示</label>
                                                <input type="radio" value="2" name="planDisplayStatusSrch" datatype="*"
                                                       <c:if test="${ planInfoForm.planDisplayStatusSrch ne '1' }">checked</c:if>
                                                       id="planDisplayStatusSrch_no">
                                                <label for="planDisplayStatusSrch_no">隐藏</label>
                                            </div>
                                            <hyjf:validmessage key="planDisplayStatusSrch"
                                                               label="显示状态"></hyjf:validmessage>
                                        </div>

                                        <div class="form-group" style="float: left; width: 33%">
                                            <label class="control-label">
                                                智投状态 <span class="symbol required"></span>
                                            </label>
                                            <div class="clip-radio radio-primary">
                                                <input type="radio" value="1" name="debtPlanStatus" datatype="*"
                                                       <c:if test="${ planInfoForm.debtPlanStatus eq '1' }">checked</c:if>
                                                       id="debtPlanStatus_yes">
                                                <label for="debtPlanStatus_yes">启用</label>
                                                <input type="radio" value="2" name="debtPlanStatus" datatype="*"
                                                       <c:if test="${ planInfoForm.debtPlanStatus ne '1' }">checked</c:if>
                                                       id="debtPlanStatus_no">
                                                <label for="debtPlanStatus_no">禁用</label>
                                            </div>
                                            <hyjf:validmessage key="debtPlanStatus" label="智投状态"></hyjf:validmessage>
                                        </div>
                                    </div>

                                    <div style="overflow: hidden">
                                        <div class="form-group" style="float: left; width: 33%">

                                        </div>
                                        <div class="form-group" style="float:left; width: 33%">
                                            <label class="control-label">
                                                建议出借者类型 <span class="symbol required"></span>
                                            </label>
                                            <div class="clip-radio radio-primary">
                                                <select id="investLevel" name="investLevel" class="form-select2" data-allow-clear="true" data-placeholder="请选择" style="width:20%" >
                                                    <c:forEach items="${investLevelList }" var="record" begin="0" step="1" varStatus="status">
                                                        <option value="${record.nameCd }" <c:if test="${record.nameCd eq planInfoForm.investLevel}">selected="selected"</c:if>
                                                                <c:if test="${record.nameCd eq planInfoForm.investLevel}">selected="selected"</c:if>> <c:out value="${record.name  }"></c:out>
                                                        </option>
                                                    </c:forEach>
                                                </select>
                                            </div>
                                            <hyjf:validmessage key="investLevel" label="建议出借者类型"></hyjf:validmessage>
                                        </div>
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
                                        <i class="fa fa-check"></i> 提交保存</i>
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
                                        智投介绍<span class="symbol required"></span>
                                    </label>
                                    <div class="form-group">
								<textarea name="planConcept" datatype="*" nullmsg="未填写智投介绍" class="tinymce" cols="10"
                                          rows="12">
									<c:set value="<p style='text-align: justify;'></p>" var="str1"></c:set>
									<c:if test="${planInfoForm.planConcept !=null && planInfoForm.planConcept !='' }"><c:out
                                            value="${planInfoForm.planConcept }"></c:out></c:if>
									<c:if test="${planInfoForm.planConcept == null || planInfoForm.planConcept == ''}"><c:out
                                            value="${str1 }"></c:out></c:if>
								</textarea>
                                    </div>
                                    <hyjf:validmessage key="planConcept" label="智投介绍"></hyjf:validmessage>
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
                                        <i class="fa fa-check"></i> 提交保存</i>
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
                                        常见问题<span class="symbol required"></span>
                                    </label>
                                    <div class="form-group">
								<textarea name="normalQuestion" datatype="*" nullmsg="请填写常见问题" class="tinymce" cols="10"
                                          rows="12">
										<c:set value="<p>1、汇速贷出借计划安全吗？</p>" var="str1"></c:set>
										<c:set value="<p>汇盈金服平台以严谨、负责的态度对速贷出借计划内每笔融资项目进行严格筛选并挑选出优质资产，计划内项目均适应于汇盈金服用户利益保障机制。</p>"
                                               var="str2"></c:set>
										<c:set value="<p>2、汇速贷出借计划的“锁定期”是什么？</p>" var="str3"></c:set>
										<c:set value="<p>汇速贷出借计划具有收益期锁定限制，锁定期内，用户不可以提前退出。</p>" var="str4"></c:set>
										<c:set value="<p>3、汇速贷出借计划收益有多少？</p>" var="str5"></c:set>
                                        <c:set value="<p>汇速贷出借计划预期出借利率与同期限“汇直投”项目的预期出借利率相同。</p>" var="str6"></c:set>
										<c:set value="<p>4、加入的汇速贷出借计划的用户所获收益处理方式有几种？</p>" var="str7"></c:set>
										<c:set value="<p>本计划提供两种收益处理方式：循环复投或用户自由支配。计划退出后，用户的本金和收益将返回至其汇盈金服账户中，供用户自行支配。</p>"
                                               var="str8"></c:set>
										<c:set value="<p>5、汇速贷出借计划通过何种方式为用户实现自动投标？</p>" var="str9"></c:set>
										<c:set value="<p>汇速贷出借计划不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属计划账户，所有资金通过该专属计划账户流动。</p>"
                                               var="str10"></c:set>
										<c:set value="<p>6、汇速贷出借计划到期后，如何退出并实现收益?</p>" var="str11"></c:set>
										<c:set value="<p>汇速贷出借计划到期后，系统将自动进行资金结算，结算完成后的本金及收益将从用户汇速贷计划账户自动划转至用户普通账户中，用户在T+3个工作日内收到资金。</p>"
                                               var="str12"></c:set>
										<c:set value="<p>汇盈金服仅为信息发布平台，未以任何明示或暗示的方式对出借人提供担保或承诺保本保息，出借人应根据自身的出借偏好和风险承受能力进行独立判断和作出决策，并自行承担资金出借的风险与责任。</p>"
                                               var="str13"></c:set>
										<c:set value="<p>市场有风险，出借需谨慎。</p>" var="str14"></c:set>
 										<c:if test="${planInfoForm.normalQuestion != null && planInfoForm.normalQuestion != '' }"><c:out
                                                value="${planInfoForm.normalQuestion }"></c:out></c:if>
										<c:if test="${planInfoForm.normalQuestion == null || planInfoForm.normalQuestion == '' }"><c:out
                                                value="${str1 }"></c:out></c:if>
								</textarea>
                                    </div>
                                    <hyjf:validmessage key="normalQuestion" label="常见问题"></hyjf:validmessage>
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
                                    </div>
                                    <a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
                                        <i class="fa fa-check"></i> 提交保存</i>
                                    </a>
                                </div>
                            </div>
                        </div>
                        <!-- End:安全保障 -->
                    </div>

                </form>
            </div>
        </div>
    </tiles:putAttribute>
    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <link href="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda-themeless.min.css" rel="stylesheet"
              media="screen">
    </tiles:putAttribute>
    <%-- 对话框面板 (ignore) --%>
    <tiles:putAttribute name="dialogPanels" type="string">
        <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm"
                style="border:none;width:100%;height:100%"></iframe>
    </tiles:putAttribute>

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
        <link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
    </tiles:putAttribute>
    <%-- JS全局变量定义、插件 (ignore) --%>
    <tiles:putAttribute name="pageGlobalImport" type="string">
        <!-- Form表单插件 -->
        <%@include file="/jsp/common/pluginBaseForm.jsp" %>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
        <%-- <script type="text/javascript" src="${themeRoot}/vendor/plug-in/region-select.js"></script> --%>
        <script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
    </tiles:putAttribute>
    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type='text/javascript' src="${webRoot}/jsp/manager/hjhplan/plancommon.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
