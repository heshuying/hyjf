<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
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
        <div class="panel panel-white no-margin">
            <div class="panel-body">
                <div class="row">

                    <div class="col-xs-12">
                        <fieldset class="margin-top-25 margin-bottom-0">
                            <legend>
                                审批
                            </legend>
                            <form id="mainForm" action="auditAction"
                                  method="post"  role="form" class="form-horizontal" >
                                    <%-- 角色列表一览 --%>
                                <input type="hidden" name="id" id="id" value="${couponCheckForm.id}" />
                                        <input type="hidden" name="id" id="id" value="${couponCheckForm.filePath}" />
                                <div class="form-group">
                                    <label class="col-sm-2 control-label" > <span class="symbol required"></span>审核意见 </label>
                                    <div class="col-sm-10 ">
                                        <div>
                                            <input type="radio"  name="status" checked="checked" datatype="*" value="2"}> <label> 通过 </label>
                                            <input type="radio"  name="status"  datatype="*"  value="3"}> <label> 不通过 </label>
                                            <hyjf:validmessage key="status" label="审核意见"></hyjf:validmessage>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-sm-2 control-label"> <span class="symbol required"></span>审核备注</label>
                                    <div class="col-sm-10">
                                        <textarea placeholder="审核不通过必须备注不通过原因（20字以内）" id="mark" name="mark"  class="form-control limited" datatype="*"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">&nbsp;&nbsp;&nbsp; 注：未发行审核通过的优惠券/过期优惠券/活动未开始或已结束三种情况下，优惠券不能被手动发放到用户账户。因此，审核前请仔细校验导入文件中的优惠券信息。   </div>
                                   <div class="form-group margin-bottom-0">
                                    <div class="col-sm-offset-2 col-sm-10">
                                        <a class="btn btn-o btn-primary fn-Confirm" ><i class="fa fa-check"></i> 确 认</a>
                                        <a class="btn btn-o btn-primary fn-Cancel"><i class="fa fa-close"></i> 取 消</a>
                                    </div>
                                </div>
                            </form>
                        </fieldset>
                    </div>
                </div>
            </div>
        </div>
    </tiles:putAttribute>

    <%-- 画面的CSS (ignore) --%>
    <tiles:putAttribute name="pageCss" type="string">
        <style>
            .rz-icons {
                list-style: none;
                margin: 0 0 -1px 0;
                padding: 0;
            }
            .rz-icons li {
                display: inline-block;
                margin: 0;
                padding: 0;
                width: 40px;
                height: 40px;
                opacity: 0.6;
                line-height: 44px;
                top: 0;
                position: relative;
                color:#999;
            }
            .rz-icons .certified {
                background: #3B5998;
                color: #fff;
            }
            .table tbody tr td:nth-child(2n-1) {
                color:#999 !important;
            }
            fieldset {
                padding: 16px;
            }
            fieldset legend {
                font-family: "微软雅黑";
                font-size: 14px;
            }
            .qrcode {
                border: 1px solid #ccc;
                display: inline-block;
                padding: 6px;
            }
            #qrcode {
                width: 128px;
                height: 128px;
            }
        </style>
    </tiles:putAttribute>

    <%-- JS全局变量定义、插件 (ignore) --%>
    <tiles:putAttribute name="pageGlobalImport" type="string">
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/qrcode/jquery.qrcode.js"></script>
        <script type='text/javascript' src="${themeRoot}/vendor/plug-in/qrcode/qrcode.js"></script>
    </tiles:putAttribute>

    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.validform_v5.3.2.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/classie.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/selectFx/selectFx.js"></script>
        <script type="text/javascript" src="${themeRoot}/vendor/plug-in/select2/select2.min.js"></script>

        <script type='text/javascript' src="${webRoot}/jsp/coupon/user/couponAudit.js"></script>
    </tiles:putAttribute>

</tiles:insertTemplate>