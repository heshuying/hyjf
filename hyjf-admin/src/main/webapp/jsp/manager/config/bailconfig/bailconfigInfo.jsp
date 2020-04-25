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
    <tiles:putAttribute name="pageTitle" value="添加合作额度配置" />

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
        <link href="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker3.standalone.css" rel="stylesheet" media="screen">

    </tiles:putAttribute>

    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <c:set var="jspEditType"
               value="${empty bailConfigForm.id ? '添加' : '修改'}"></c:set>
        <div class="panel panel-white" style="margin: 0">
            <div class="panel-body">
                <div class="panel-scroll height-830">
                    <form id="mainForm"
                          action="${empty bailConfigForm.id ? 'insertAction' : 'updateAction'}"
                          method="post" role="form" class="form-horizontal">
                            <%-- 列表一览 --%>
                        <input type="hidden" name="id" id="id" value="${bailConfigForm.id }"/>
                        <input type="hidden" name="monthDEL" id="monthDEL" value="${bailConfigForm.monthDEL }"/>
                        <input type="hidden" name="endDEL" id="endDEL" value="${bailConfigForm.endDEL }"/>
                        <input type="hidden" name="endmonthDEL" id="endmonthDEL" value="${bailConfigForm.endmonthDEL }"/>
                        <input type="hidden" name="enddayDEL" id="enddayDEL" value="${bailConfigForm.enddayDEL }"/>
                        <input type="hidden" name="principalDEL" id="principalDEL" value="${bailConfigForm.principalDEL }"/>
                        <input type="hidden" name="seasonDEL" id="seasonDEL" value="${bailConfigForm.seasonDEL }"/>
                        <input type="hidden" name="endmonthsDEL" id="endmonthsDEL" value="${bailConfigForm.endmonthsDEL }"/>
                        <input type="hidden" name="pageToken" value="${sessionScope.RESUBMIT_TOKEN}"/>
                        <input type="hidden" id="success" value="${success}"/>
                        <%--<div class="form-group">
                            <label class="col-sm-12"> <h4>保证金设置</h4><hr style="margin-top:0;"/></label>
                        </div>--%>


                        <%--<div class="form-group">
                            <label class="col-sm-2 control-label" for="bailTatol"> <span class="symbol required"></span>保证金金额</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="保证金金额" id="bailTatol" name="bailTatol"
                                           value="<c:out value="${bailConfigForm.bailTatol}" />" class="form-control"
                                           datatype="/^\d{1,10}(\.\d{1,2})?$/"
                                           errormsg="保证金金额必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="monthMarkLine" label="月推标额度"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="bailRate"> <span class="symbol required"></span>保证金比例</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="保证金比例" id="bailRate" name="bailRate"
                                           value="<c:out value="${bailConfigForm.bailRate}" />" class="form-control"
                                           datatype="n1-3" errormsg="保证金比例必须为整数！" maxlength="3">
                                    <span class="input-group-addon">%</span>
                                </div>
                                <hyjf:validmessage key="monthMarkLine" label="月推标额度"></hyjf:validmessage>
                            </div>
                        </div>--%>

                        <div class="form-group">
                            <label class="col-sm-12"> <h4>推标额度设置</h4><hr style="margin-top:0;"/></label>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="instCodeType"><span
                                    class="symbol required"></span>资产来源 </label>
                            <c:if test="${ empty bailConfigForm.id }">
                                <div class="col-sm-8">
                                    <select id="instCode" name="instCode" class="form-select2" datatype="*"
                                            nullmsg="未指定资产来源！" style="width:100%" data-placeholder="请选择资产来源...">
                                        <option value="">请选择资产来源...</option>
                                        <c:forEach items="${hjhInstConfigList }" var="inst" begin="0" step="1">
                                            <option value="${inst.instCode }"
                                                    <c:if test="${inst.instCode eq bailConfigForm.instCode}">selected="selected"</c:if>>
                                                <c:out value="${inst.instName }"></c:out>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>
                            <c:if test="${ !empty bailConfigForm.id }">
                                <div class="col-sm-8">
                                    <select id="instName" name="instName" class="form-select2" datatype="*" style="width:100%" data-placeholder="资产来源" disabled="disabled" >
                                        <option value="${bailConfigForm.instName}"selected="selected">${bailConfigForm.instName}</option>
                                    </select>
                                    <input type="hidden" name="instCode" value="${bailConfigForm.instCode}"/>
                                </div>
                            </c:if>
                            <hyjf:validmessage key="instCode" label="资产来源"></hyjf:validmessage>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="dayMarkLine"> <span
                                    class="symbol required"></span>日推标额度</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="日推标额度" id="dayMarkLine" name="dayMarkLine"
                                           value="<c:out value="${bailConfigForm.dayMarkLine}" />" class="form-control"
                                           datatype="/^\d{1,10}(\.\d{1,2})?$/"
                                           errormsg="日推标额度必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="dayMarkLine" label="日推标额度"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="monthMarkLine"> <span
                                    class="symbol required"></span>月推标额度</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="月推标额度" id="monthMarkLine" name="monthMarkLine"
                                           value="<c:out value="${bailConfigForm.monthMarkLine}" />"
                                           class="form-control"
                                           datatype="/^\d{1,10}(\.\d{1,2})?$/"
                                           errormsg="月推标额度必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="monthMarkLine" label="月推标额度"></hyjf:validmessage>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="isAccumulate">
                                <span class="symbol required"></span>未用额度累计
                            </label>
                            <div class="col-sm-8 ">
                                <div class="radio clip-radio radio-primary ">
                                    <input type="radio" id="stateOn" name="isAccumulate" value="1"  datatype="*" ${ bailConfigForm.isAccumulate eq 1 ? 'checked' : ''}> <label for="stateOn"> 开启 </label>
                                    <input type="radio" id="stateOff" name="isAccumulate" value="0" datatype="*" ${ ( empty bailConfigForm.isAccumulate || bailConfigForm.isAccumulate eq 0 ) ? 'checked' : ''}> <label for="stateOff"> 关闭 </label>
                                    <hyjf:validmessage key="isAccumulate" label="未用额度累计"></hyjf:validmessage>
                                </div>
                            </div>
                        </div>

                        <br/>
                        <div class="form-group"><label class="col-sm-12"> <h4>合作额度设置</h4><hr style="margin-top:0;"/></label></div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label padding-top-5" for="time"> <span
                                    class="symbol required"></span>合作周期
                            </label><%--
                            <div class="input-group input-daterange datepicker">
                                <input type="text" name="timestart" id="timestart"
                                       maxlength="10" style="margin-left: 15px;"
                                       onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd', errDealMode: 1})"onFocus="WdatePicker({maxDate: '#F{$dp.$D(\'timeend\')}' })"
                                       value="${bailConfigForm.timestart}" datatype="*" errormsg="授信周期开始时间必填！" />~<input
                                    type="text" name="timeend" id="timeend" maxlength="10"
                                    onclick="WdatePicker({skin:'twoer', dateFmt:'yyyy-MM-dd', errDealMode: 1})" onFocus="WdatePicker({minDate: '#F{$dp.$D(\'timestart\')}' })"
                                    value="${bailConfigForm.timeend}" datatype="*" errormsg="授信周期结束时间必填！" /></span>
                            </div>--%>
                            <div class="col-sm-8">
                                <div class="input-group input-daterange datepicker ">
                                    <span class="input-icon">
                                        <input type="text"
                                         name="timestart" id="timestart" class="form-control underline"
                                         value="${bailConfigForm.timestart}" /> <i class="ti-calendar"></i>
                                    </span>
                                    <span class="input-group-addon no-border bg-light-orange">~</span>
                                    <input type="text" name="timeend" id="timeend"
                                           class="form-control underline"
                                           value="${bailConfigForm.timeend}" />
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="newCreditLine"> <span
                                    class="symbol required"></span>合作额度</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="合作额度" id="newCreditLine" name="newCreditLine"
                                           value="<c:out value="${bailConfigForm.newCreditLine}" />"
                                           class="form-control"
                                           datatype="/^\d{1,10}(\.\d{1,2})?$/"
                                           errormsg="合作额度必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="newCreditLine" label="合作额度"></hyjf:validmessage>
                            </div>
                        </div>

                        <%--<div class="form-group">
                            <label class="col-sm-2 control-label" for="loanCreditLine"> <span
                                    class="symbol required"></span>在贷余额额度</label>
                            <div class="col-sm-8">
                                <div class="input-group">
                                    <input type="text" placeholder="在贷余额额度" id="loanCreditLine" name="loanCreditLine"
                                           value="<c:out value="${bailConfigForm.loanCreditLine}" />"
                                           class="form-control"
                                           datatype="/^\d{1,10}(\.\d{1,2})?$/"
                                           errormsg="在贷余额额度必须为数字，整数部分不能超过10位，小数部分不能超过2位！" maxlength="13">
                                    <span class="input-group-addon">元</span>
                                </div>
                                <hyjf:validmessage key="monthMarkLine" label="月推标额度"></hyjf:validmessage>
                            </div>
                        </div>--%>

                        <div class="form-group">
                            <label class="col-sm-2 control-label" for="remark"> <span
                                    class="symbol required"></span>备注
                            </label>
                            <div class="col-sm-8">
                        <textarea datatype="*" errormsg="必填选项，长度不超过40!" placeholder="备注" maxlength="40"
                                  id="remark" name="remark"
                                  class="form-control limited">${bailConfigForm.remark}</textarea>
                            </div>
                        </div>
                        <br/>
                        <%--<div class="form-group"><label class="col-sm-12"> <h4>还款方式授信方式设置</h4><hr style="margin-top:0;"/></label></div>

                        <c:if test="${ empty bailConfigForm.monthDEL or bailConfigForm.monthDEL eq 0 }">
                            <div class="form-group" id="month">
                                <label class="col-sm-2 control-label" for="month">
                                    <span></span>等额本息:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="monthNCL" id="monthNCL" value="1" class="monthCheckbox"
                                               <c:if test="${( bailConfigForm.monthNCL eq '1' ) }">checked</c:if> />
                                        <label for="monthNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="monthLCL" id="monthLCL" value="1" class="monthCheckbox"
                                               <c:if test="${( bailConfigForm.monthLCL eq '1' ) }">checked</c:if> />
                                        <label for="monthLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="monthRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.monthRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.monthRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.monthRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.endDEL or bailConfigForm.endDEL eq 0 }">
                            <div class="form-group" id="end">
                                <label class="col-sm-2 control-label" for="end">
                                    <span></span>按月计息，到期还本还息:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endNCL" id="endNCL" value="1" class="endCheckbox"
                                               <c:if test="${( bailConfigForm.endNCL eq '1' ) }">checked</c:if> />
                                        <label for="endNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endLCL" id="endLCL" value="1" class="endCheckbox"
                                               <c:if test="${( bailConfigForm.endLCL eq '1' ) }">checked</c:if> />
                                        <label for="endLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="endRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.endRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>&lt;%&ndash;
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.endRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>&ndash;%&gt;
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.endRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.endmonthDEL or bailConfigForm.endmonthDEL eq 0 }">
                            <div class="form-group" id="endmonth">
                                <label class="col-sm-2 control-label" for="endmonth">
                                    <span></span>先息后本:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endmonthNCL" id="endmonthNCL" value="1"
                                               class="endmonthCheckbox"
                                               <c:if test="${( bailConfigForm.endmonthNCL eq '1' ) }">checked</c:if> />
                                        <label for="endmonthNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endmonthLCL" id="endmonthLCL" value="1"
                                               class="endmonthCheckbox"
                                               <c:if test="${( bailConfigForm.endmonthLCL eq '1' ) }">checked</c:if> />
                                        <label for="endmonthLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="endmonthRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.endmonthRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.endmonthRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.endmonthRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.enddayDEL or bailConfigForm.enddayDEL eq 0 }">
                            <div class="form-group" id = "endday">
                                <label class="col-sm-2 control-label" for="endday">
                                    <span></span>按天计息，到期还本息:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="enddayNCL" id="enddayNCL" value="1"
                                               class="enddayCheckbox"
                                               <c:if test="${( bailConfigForm.enddayNCL eq '1' ) }">checked</c:if> />
                                        <label for="enddayNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="enddayLCL" id="enddayLCL" value="1"
                                               class="enddayCheckbox"
                                               <c:if test="${( bailConfigForm.enddayLCL eq '1' ) }">checked</c:if> />
                                        <label for="enddayLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="enddayRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.enddayRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>&lt;%&ndash;
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.enddayRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>&ndash;%&gt;
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.enddayRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.principalDEL or bailConfigForm.principalDEL eq 0 }">
                            <div class="form-group" id = "principal">
                                <label class="col-sm-2 control-label" for="principal">
                                    <span></span>等额本金:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="principalNCL" id="principalNCL" value="1"
                                               class="principalCheckbox"
                                               <c:if test="${( bailConfigForm.principalNCL eq '1' ) }">checked</c:if> />
                                        <label for="principalNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="principalLCL" id="principalLCL" value="1"
                                               class="principalCheckbox"
                                               <c:if test="${( bailConfigForm.principalLCL eq '1' ) }">checked</c:if> />
                                        <label for="principalLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="principalRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.principalRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.principalRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.principalRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.seasonDEL or bailConfigForm.seasonDEL eq 0 }">
                            <div class="form-group" id = "season">
                                <label class="col-sm-2 control-label" for="season">
                                    <span></span>按季还款:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="seasonNCL" id="seasonNCL" value="1"
                                               class="seasonCheckbox"
                                               <c:if test="${( bailConfigForm.seasonNCL eq '1' ) }">checked</c:if> />
                                        <label for="seasonNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="seasonLCL" id="seasonLCL" value="1"
                                               class="seasonCheckbox"
                                               <c:if test="${( bailConfigForm.seasonLCL eq '1' ) }">checked</c:if> />
                                        <label for="seasonLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="seasonRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.seasonRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.seasonRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.seasonRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>

                        <c:if test="${ empty bailConfigForm.endmonthsDEL or bailConfigForm.endmonthsDEL eq 0 }">
                            <div class="form-group" id = "endmonths">
                                <label class="col-sm-2 control-label" for="endmonths">
                                    <span></span>按月付息到期还本:
                                </label>
                                <div class="col-sm-10">
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endmonthsNCL" id="endmonthsNCL" value="1"
                                               class="endmonthsCheckbox"
                                               <c:if test="${( bailConfigForm.endmonthsNCL eq '1' ) }">checked</c:if> />
                                        <label for="endmonthsNCL">新增授信</label>
                                    </div>
                                    <div class="checkbox clip-check check-info checkbox-inline">
                                        <input type="checkbox" name="endmonthsLCL" id="endmonthsLCL" value="1"
                                               class="endmonthsCheckbox"
                                               <c:if test="${( bailConfigForm.endmonthsLCL eq '1' ) }">checked</c:if> />
                                        <label for="endmonthsLCL">在贷余额授信</label>
                                    </div>
                                    <div class="clip-check check-info checkbox-inline">
                                        <select name="endmonthsRCT"
                                                class="form-control underline form-select2">
                                            <option value="0"
                                                    <c:if test="${bailConfigForm.endmonthsRCT eq '0'}">selected="selected"</c:if>>
                                                到期回滚
                                            </option>
                                            <option value="1"
                                                    <c:if test="${bailConfigForm.endmonthsRCT eq '1'}">selected="selected"</c:if>>
                                                分期回滚
                                            </option>
                                            <option value="2"
                                                    <c:if test="${bailConfigForm.endmonthsRCT eq '2'}">selected="selected"</c:if>>
                                                不回滚
                                            </option>
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </c:if>--%>

                                <div class="form-group margin-bottom-0">
                            <div class="col-sm-offset-2 col-sm-10">
                                <a class="btn btn-o btn-primary fn-Confirm" id="fn-Confirm"><i
                                        class="fa fa-check"></i> 确 认</a> <a
                                    class="btn btn-o btn-primary fn-Cancel"><i
                                    class="fa fa-close"></i> 取 消</a>
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
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/bootstrap-datepicker/bootstrap-datepicker.js"></script>
        <script type='text/javascript' src="${webRoot}/jsp/manager/config/bailconfig/bailconfigInfo.js"></script>
    </tiles:putAttribute>
</tiles:insertTemplate>
