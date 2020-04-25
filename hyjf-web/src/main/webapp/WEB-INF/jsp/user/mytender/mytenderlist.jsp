<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<title>我的投资 - 汇盈金服官网</title>
<link rel="stylesheet" type="text/css" href="${cdn}/css/jquery-ui.css" />
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>

    <div class="user-credit-banner">
        <jsp:include page="/subMenu.jsp"></jsp:include>
        <div class="container-1200">
            <div class="touzi-con">
                <div class="item">
                    <div class="tit">冻结金额（元）</div>
                    <div class="num"><fmt:formatNumber value="${account.bankFrost }" pattern="#,##0.00#" /></div>
                </div>
                <div class="item">
                    <div class="tit">累计收益（元）</div>
                    <div class="num highlight"><fmt:formatNumber value="${account.bankInterestSum }" pattern="#,##0.00#" maxFractionDigits="2"/></div>
                </div>
                <div class="item">
                    <div class="tit">累计投资（元）</div>
                    <div class="num highlight"><fmt:formatNumber value="${account.bankInvestSum }" pattern="#,##0.00#" maxFractionDigits="2"/></div>
                </div>
            </div>
        </div>
    </div>
    
    <div class="project-tabbing user-credit-tabbing">
        <div class="container-1200">
         	<input type="hidden" id="projectStatus" name="projectStatus">
            <ul class="project-tab">
                <li panel="0" data-status = "12">冻结中</li>
                <li panel="1" data-status = "13" class="active">还款中</li>
                <li panel="2" data-status = "14">已回款</li>
            </ul>
            <div class="date-range">
                <input type="text" id="startDate" name="startDate" class="datepicker">
               	 至
                <input type="text" id="endDate" name="endDate" class="datepicker">
                <button type="submit" id="searchByDate" class="btn">搜索</button>
            </div>
            <div class="clearfix"></div>
            <ul class="project-tab-panel">
                  <li panel="0">
                    <table id="pant0" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye0">
						<!-- 分页栏模板 -->
					</div>
                  </li>
                  <li panel="1" class="active">
					<table id="pant1" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye1">
						<!-- 分页栏模板 -->
					</div>
                  </li>
                  <li panel="2">
					<table id="pant2" border="0" cellspacing="0" cellpadding="0">
                    </table>
					<div class="new-pagination" id="fenye2">
						<!-- 分页栏模板 -->
					</div>
                  </li>
            </ul>
        </div>
    </div>
    <div class="settlement_mask"></div>
    <div class="settlement js_touzi repayPlanDetail">
        <div class="qr_main">
            <a class="zr_close js_close" href="javascript:void(0)" onclick="popoutWin()">×</a>
            <h3>还款计划</h3>
            <div class="popmsg">
                <table border="0" cellspacing="0" cellpadding="0">
                <thead>
                    <tr>
                        <th>期数</th>
                        <th>待收本息</th>
                        <th>待收本金</th>
                        <th>待收利息</th>
                        <th>待收时间</th>
                        <th>应收本息</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
                </table>
            </div>    
        </div>
    </div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${cdn}/js/jquery-ui.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/datepicker-zh-CN.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/fill.js?version=${version}" ></script>
    <script type="text/javascript" src="${cdn}/js/user/mytender/mytenderlist.js?version=${version}" ></script>
</body>
</html>