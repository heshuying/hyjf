<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇添金 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/animate.css" />
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
    <div class="htl-section1">
    	<div id="subMenu" class="hd-innernav">
            <ul class="subnav-inner">
                <li><a href="${ctx}/plan/initPlanList.do">汇添金</a></li>
                <li class="active"><a href="${ctx}/htl/getHtlInfo.do">关于汇添金</a></li>
            </ul>
        </div>
        <h2>什么是汇添金</h2>
        <div class="htl-s1-con">
            “汇添金”产品是一款特定优质标的、简化用户
            <br>挑选、并拥有相应保障措施的智能投标产品。
        </div>
        <%-- <div class="htl-s1-data">
            <div class="container-1200">
            	<div class="item">
	                <span class="num highlight">${htlForm.htlRate }<span class="unit">%</span></span>
	                <span class="txt">年化收益率</span>
	            </div>
	            <div class="item">
	                <span class="num">${htlForm.avaPurchase }</span>
	                <span class="txt">可申购金额（元）</span>
	            </div>
	            <div class="item">
	                <span class="num">${htlForm.userPupper }</span>
	                <span class="txt">单户投资上限（元）</span>
	            </div>
	            <div class="item">
	                <div class="htl-s1-btn">
					                    立即投资
	                </div>
	                <a href="${ctx}/htl/moveToInvestPage.do" class="htl-s1-btn">
					                    立即投资
	                </a>
	            </div>
            </div>
            <div class="clearfix"></div>
        </div> --%>
    </div>
    <div class="htl-section2">
        <h2>产品说明</h2>
        <div class="cut-line"></div>
        <ul class="htl-s2-list">
            <li>
                <div class="htl-s2-icon icon1"></div>
                <div class="title">产品原理</div>
                <div class="txt">平台向客户提供债权组合投资的信息<br/>服务，包括债权投资和债权转出的信<br/>息服务</div>
            </li>
            <li>
                <div class="htl-s2-icon icon2"></div>
                <div class="title">产品特色</div>
                <div class="txt">投资标的风险分散化,用户投资体验人<br/>性化,期限收益选择多样化</div>
            </li>
            <li>
                <div class="htl-s2-icon icon3"></div>
                <div class="title">产品保障</div>
                <div class="txt">由合作机构提供安全保障措施，资金<br/>由汇付天下全程托管，投资标的分散<br/>化，让您安心投资</div>
            </li>
        </ul>
        <div class="clearfix"></div>
    </div>
   <!--  <div class="htl-section3">
        <h2>问题解答</h2>
        <div class="cut-line"></div>
        <ul class="new-tabbing">
            <li class="active" panel="0">委托计划</li>
            <li panel="1">委托到期</li>
            <li panel="2">委托收益</li>
            <li panel="3">安全保障</li>
        </ul>
       <ul class="new-tabbing-panel">
            <li class="active" panel="0">
                1.汇添金的委托计划有金额限制吗？<br>
                汇添金的加入起点为1000元整，100元的幅度递增，单笔投资不超过500，000.00。<br>
                <br>
                2.计划委托认购需要什么费用？<br>
                 目前计划委托认购不需要任何费用。	
            </li>
            <li panel="1">
                 1.汇添金计划的委托到期有投资金额退出限制吗？ <br>
                汇添金计划退出没有限制，具体详情请参照汇添金服务协议的相关条款。<br>
                <br>
                2.汇添金到期后资金什么时候能到账？<br>
                汇添金计划到期后，投资资金经过系统清算后直接打入客户账户，提现按相关规则。<br>
                <br>
                3.汇添金计划到期需要什么费用？<br>
               汇添金产品赎回时平台按相关标准，在保障本金和历史回报的前提下，从获得的投资<br />
               收益中收取相应委托服务费用，相关提现手续费请参考支付机构规则。<br />
            </li>
            <li panel="2">
            1.汇添金如何计算收益？<br>
              汇添金是系列产品统称，红色经典系列收益计算公式：历史回报率*本金/12*投资周期。<br>
              例如：红色经典3，预期利率为6%的时候，万元收益=6%*10000/12*3=150（元）。<br>
              	<br>
            2.汇添金什么时候可以购买？<br>
              只要产品额度开放，就可以申请。
            </li>
            <li panel="3">
            汇添金产品是由合作机构推荐的优秀债权组合投资，<br>
            汇付天下资金托管，资金进出全程封闭，<br>
            合作机构提供安全保障措施。<br>
             </li>
        </ul>
    </div> -->
    <div class="htl-section4">
        <span>更多精彩，敬请期待...</span>
    </div>
   	<script>/* setActById('hdCFH'); */setActById('subHTJ');</script>    
    <jsp:include page="/footer.jsp"></jsp:include>
    <script>
        $(".new-tabbing").click(function(e){
            var _self = $(e.target);
            if(_self.is("li")){
                var idx = _self.attr("panel");
                var panel = _self.parent().next(".new-tabbing-panel");
                _self.siblings("li.active").removeClass("active");
                _self.addClass("active");
                panel.children("li.active").removeClass("active");
                panel.children("li").eq(idx).addClass("active");
            }

        })
    </script>
</body>

</html>