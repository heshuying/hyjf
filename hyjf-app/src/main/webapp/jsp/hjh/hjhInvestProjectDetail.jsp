<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session="false" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8" name="viewport"
          content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/page.css?v=1"/>
    <link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
    <title>持有详情</title>
</head>
<body class="bg_grey">
<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
<input type="hidden" name="version" id="version" value="${version}" />
<div class="specialFont  cal-main">
    <section class="new-form-item  bg_white my-invest-header">
        <h5>
            ${hjhDetail.planName}
        </h5>
        <div class="my-invest-header-div">
            <span>历史年回报率<i class="hyjf-color">${hjhDetail.planApr}</i></span>
            <span>锁定期限<i class="hyjf-color">${hjhDetail.planPeriod}</i></span>
        </div>
    </section>
    <section class="new-form-item  bg_white my-invest-item">
        <!-- 投资模块 -->
        <div>
            <span>加入金额(元)</span>
            <span class="hyjf-color format-num">${hjhDetail.accedeAccount}</span>
        </div>
        <div>
            <span>加入时间</span>
            <span>${hjhDetail.addTime}</span>
        </div>
        <!-- 收益模块 -->
    </section>
    <section class="new-form-item  bg_white my-invest-item">


        <c:choose>
            <c:when test="${type == '1'}">
                <div>
                    <span>待收本息(元)</span>
                    <span class="hyjf-color format-num">${hjhDetail.waitTotal}</span>
                </div>
                <div>
                    <span>待收收益(元)</span>
                    <span>${hjhDetail.waitInterest}</span>
                </div>
                <div>
                    <span>计划应还时间</span>
                    <span>${hjhDetail.lastPaymentTime}</span>
                </div>
            </c:when>
            <c:otherwise>
                <div>
                    <span>回款金额(元)</span>
                    <span class="hyjf-color format-num">${hjhDetail.receivedTotal}</span>
                </div>
                <div>
                    <span>实际收益(元)</span>
                    <span>${hjhDetail.receivedInterest}</span>
                </div>
                <div>
                    <span>退出时间</span>
                        <span>${hjhDetail.quitTime}</span>
                </div>
            </c:otherwise>

        </c:choose>

    </section>
    <section class="new-form-item  bg_white my-invest-item" style="padding-top:10px;padding-bottom:50px">
        <i class="icon-star"></i>
        <span>持有项目</span>
        <span class="toggle-list hyjf-color">收起</span>
        <table cellspacing="0" cellpadding="0" class="detail-car-table" id="cyxmList">
            <tr>
                <td>资产编号</td>
                <td>投资金额（元）</td>
                <td>投资时间</td>
                <td>操作</td>
            </tr>


            <c:forEach items='${hjhInvistList}' var="record" begin="0" step="1" varStatus="status">
                <tr>
                    <c:choose>
                        <c:when test="${type == '1'}">
                            <td><a href="${jumpcommend}://jumpH5/?{'url':'${assetUrl}/?borrowNid=${record.borrowNid}&order=${record.accedeOrderId}&randomString=${randomString}&token=${token}&sign=${sign}&platform=${platform}&realPlatform=${realPlatform}&version=${version}'}"
                                   class="hyjf-color"><c:out value="${record.borrowNid}"></c:out></a></td>
                        </c:when>
                        <c:otherwise>
                            <td><c:out value="${record.borrowNid}"></c:out></td>
                        </c:otherwise>
                    </c:choose>

                    <td><c:out value="${record.account}"></c:out></td>
                    <td><c:out value="${record.addTime}"></c:out></td>
                    <td>
                        <a id="goHjhListAgreement" href="${jumpcommend}://jumpH5/?{'url':'${hostAppUrl}/hyjf-app/hjhagreement/hjhListAgreement.do?borrowNid=${record.borrowNid}&accedeOrderId=${record.accedeOrderId}&userId=${hjhDetail.userId}&sign=${sign}&account=${record.account}'}"
                           class="hyjf-color"><c:out value="投资协议"></a></c:out></td>
                </tr>
            </c:forEach>


        </table>
    </section>

    <c:choose>
        <c:when test="${type == '1'}">
            <div class="foot-invest">
                <a href="javascript:;" id="goPlanContact">计划协议</a>
            </div>
        </c:when>
    </c:choose>
</div>
<script type="text/javascript" src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    $(".format-num").each(function () {
        var _self = $(this);
        _self.text(formatNum(_self.text()))
    })

    function formatNum(strNum) {
        //return strNum;
        if (strNum.length <= 3) {
            return strNum;
        }
        if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
            return strNum;
        }
        var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
        var re = new RegExp();
        re.compile("(\\d)(\\d{3})(,|$)");
        while (re.test(b)) {
            b = b.replace(re, "$1,$2$3");
        }
        return a + "" + b + "" + c;
    }
</script>
<script type="text/javascript">
    /**
     **点击收起、展开操作
     **/
    $('.toggle-list').click(function () {
        $('#cyxmList').toggle(0);
        if ($("#cyxmList").css("display") == "none") {
            $(this).text("展开");
        } else {
            $(this).text("收起");
        }
    })

    /* *
     **加载页面配置计划协议的链接
     **/
    var planContactUrl = '${planContactUrl}';
    $("#goPlanContact").prop("href", hyjfArr.hyjf + '://jumpH5/?{"url": "'+ planContactUrl + '&sign=${sign}"}');
    
    /* 跳转到协议列表 */
    /* var jumpcommend = '${jumpcommend}';
    var ctx = '${ctx}';
    var borrowNid = '${record.borrowNid}';
    var accedeOrderId = '${record.accedeOrderId}';
    var userId = '${hjhDetail.userId}';
    var sign = '${sign}';
    $("#goHjhListAgreement").prop("href", jumpcommend+"://jumpH5/?{'url':'"+location.origin+ctx+"/hjhagreement/hjhListAgreement.do?borrowNid="+borrowNid+"&accedeOrderId="+accedeOrderId+"&userId="+userId+"&sign="+sign+"'}"); */
</script>

</body>
</html>