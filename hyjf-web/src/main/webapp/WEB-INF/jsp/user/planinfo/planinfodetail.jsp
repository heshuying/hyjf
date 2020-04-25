<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇添金 - 账户总览 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
    <div class="user-credit-banner">
        <div class="hd-innernav">
            <jsp:include page="/subMenu.jsp"></jsp:include>
        </div>
        <div class="container-1200">
            <div class="htj-detail-title">${planinfo.debtPlanNid }  <a href="javascript:window.history.go(-1);" class="btn htj-detail-btn">返回</a></div>
            <div class="touzi-con htj htj-detail">
                <div class="item">
                    <div class="tit">加入金额：${planinfo.accedeAccount }元</div>
                    <div class="tit">加入时间：${planinfo.createTime }</div>
                </div>
                <div class="item">
                    <div class="tit">历史年回报率：${planinfo.expectApr }%</div>
                    <div class="tit">退出日期：待确认</div>
                </div>
                <div class="item">
                    <div class="tit">锁定期：${planinfo.debtLockPeriod }个月</div>
                    <div class="tit">最晚到账日期：待确认</div>
                </div>
                <div class="clearfix"></div>
            </div>
        </div>
    </div>
    <div class="project-tabbing htj-detail-tabbing" style="padding-top:30px;">
        <div class="container-1200">
            <div class="htj-detail-summary sgz">  <!-- sgz:申购中,sdz:锁定中,ytc:已退出 -->
                <div class="title">资产统计</div>
                <div class="clearfix"></div>
               <div class="content" style="text-align:center;">项目匹配中，请您耐心等待</div> 
            </div>
            <div class="panel-head">持有项目列表</div>
            <ul class="project-tab-panel">
                <li class="active">
                <table>
                    <tr>
                        <td>项目匹配中，请您耐心等待</td>
                    </tr>
                </table>
                </li>
            </ul>
        </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <script>
    	subMenu("planinfo");
        $(".project-tab").click(function(e) {
            var _self = $(e.target);
            if (_self.is("li")) {
                var idx = _self.attr("panel");
                var panel = _self.parent().siblings(".project-tab-panel");
                _self.siblings("li.active").removeClass("active");
                _self.addClass("active");
                panel.children("li.active").removeClass("active");
                panel.children("li[panel="+idx+"]").addClass("active");
            }
        })
    </script>
</body>

</html>
