<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@include file="/jsp/base/pageBase.jsp" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>

<script type="text/javascript">
    var ctx = "${webRoot}";
</script>

<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
    <%-- 画面的标题 --%>
    <tiles:putAttribute name="pageTitle" value="后台登录失败"/>
    <%-- 画面主面板 --%>
    <tiles:putAttribute name="mainContentinner" type="string">
        <form id="mainForm" action="" method="post" role="form" class="form-horizontal">

            <div class="form-group">
                <label class="col-sm-2 control-label"> <span class="symbol required"></span>时间内
                </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <input type="text" id="error-lockLong" name="lockLong" class="form-control input-sm" value='<c:out value="${config.lockLong}"></c:out>' maxlength="3" datatype="n1-3" nullmsg="未填写登录失败次数" errormsg="必须为数字" ajaxurl="isBorrowPeriodCheck" />
                        <span class="input-group-addon">小时</span>
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label class="col-sm-2 control-label" > <span class="symbol required"></span>登录失败  </label>
                <div class="col-sm-10">
                    <div class="input-group">
                        <input type="text" id="error-max" name="maxLoginErrorNum" class="form-control input-sm" value='<c:out value="${config.maxLoginErrorNum}"></c:out>' maxlength="3" datatype="n1-3" nullmsg="未填写登录失败次数" errormsg="必须为数字" ajaxurl="isBorrowPeriodCheck" />
                        <span class="input-group-addon">次</span>
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
    </tiles:putAttribute>

    <%-- JS全局变量定义、插件 (ignore) --%>
    <tiles:putAttribute name="pageGlobalImport" type="string">
        <!-- Form表单插件 -->
        <%@include file="/jsp/common/pluginBaseForm.jsp" %>
    </tiles:putAttribute>


    <%-- Javascripts required for this page only (ignore) --%>
    <tiles:putAttribute name="pageJavaScript" type="string">
        <script type='text/javascript' src="${webRoot}/jsp/loginerror/backlockuserconfig.js"></script>
    </tiles:putAttribute>

</tiles:insertTemplate>