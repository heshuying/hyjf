<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="loan-tradedetails">
                <div class="trade-main">
                    <div class="top">
                        <div class="top-fl" id="type">
                            <label class="fl">交易类型</label>
                            <form action="" method="post">
                            <div id="divselect">
                                  <cite class="cite">
                                  投资收到还款
                                  <i class="icon iconfont icon-ananzuiconv265"></i>
                                  </cite>
                                  <ul class="ui-ul">
                                     <li><a href="javascript:void(0)" data-val="1">投资收到还款</a></li>
                                     <li><a href="javascript:void(0)" data-val="2">投资收到还款2</a></li>
                                     <li><a href="javascript:void(0)" data-val="3">投资收到还款3</a></li>
                                     <li><a href="javascript:void(0)" data-val="4">444</a></li>
                                     <li><a href="javascript:void(0)" data-val="5">222</a></li>
                                   
                                  </ul>
                              </div>
                              <input name="" type="hidden" value="" id="inputselect"/>
                            </form>
                            <p style="width:600px; margin:0 auto; line-height:170%;">
                        </div>
                        <div class="top-fr">
                            <div class="loan-divright">
                                <label>时间</label>
                                    <input type="text" class="date start" onclick="layDateStart(start)" id="start" placeholder="2016-01-01">
                                    <label>至</label>
                                    <input type="text" class="date end" onclick="layDateEnd(end)" id="end" placeholder="2016-01-02">
                                    <a href="" class="find-btn">查询</a>
                            </div>
                        </div>
                    </div>
                    <div class="list">
                        <ul class="tab-tags">
                            <li class="active" panel="0"><a href="#">交易明细</a></li>
                            <li panel="1"><a href="#">充值记录</a></li>
                            <li panel="2"><a href="#">提现记录</a></li>
                        </ul>
                        <ul class="tab-panels">
                            <li class="active" panel="0">
                                <table class="loan-div">
                                    <thead>
                                        <tr>
                                            <th class="ui-list-title pl1">时间</th>
                                            <th class="ui-list-title pl2">收支类型</th>
                                            <th class="ui-list-title pl3">交易类型</th>
                                            <th class="ui-list-title pl4">交易金额</th>
                                            <th class="ui-list-title pl5">
                                            可用余额
                                            <a href="#" title="订单操作账户余额"><i class="icon iconfont icon-zhu"></i></a>
                                            </th>
                                            <th class="ui-list-title pl6">状态</th>
                                            <th class="ui-list-title pl7">操作用户</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">收入</td>
                                        <td class="ui-list-item pl3">充值</td>
                                        <td class="ui-list-item pl4">0</td>
                                        <td class="ui-list-item pl5">0</td>
                                        <td class="ui-list-item pl6">充值成功</td>
                                        <td class="ui-list-item pl7">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">收入</td>
                                        <td class="ui-list-item pl3">充值</td>
                                        <td class="ui-list-item pl4">+10000</td>
                                        <td class="ui-list-item pl5">+10000</td>
                                        <td class="ui-list-item pl6">充值成功</td>
                                        <td class="ui-list-item pl7">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">收入</td>
                                        <td class="ui-list-item pl3">充值</td>
                                        <td class="ui-list-item pl4">+10000</td>
                                        <td class="ui-list-item pl5">+10000</td>
                                        <td class="ui-list-item pl6">充值成功</td>
                                        <td class="ui-list-item pl7">江西银行</td>
                                    </tr>
                                </tbody>
                                </table>
                            </li>
                            <li panel="1">
                                <table class="loan-div">
                                    <thead>
                                        <tr>
                                            <th class="ui-list-title pl1">充值时间</th>
                                            <th class="ui-list-title pl2">充值金额</th>
                                            <th class="ui-list-title pl3">充值手续费</th>
                                            <th class="ui-list-title pl4">到账金额</th>
                                            <th class="ui-list-title pl5">状态</th>
                                            <th class="ui-list-title pl6">操作平台</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">充值成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">充值成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">充值成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                </tbody>
                                </table>
                            </li>
                            <li panel="2">
                                <table class="loan-div">
                                    <thead>
                                        <tr>
                                            <th class="ui-list-title pl1">提现时间</th>
                                            <th class="ui-list-title pl2">提现金额</th>
                                            <th class="ui-list-title pl3">提现手续费</th>
                                            <th class="ui-list-title pl4">到账金额</th>
                                            <th class="ui-list-title pl5">状态</th>
                                            <th class="ui-list-title pl6">操作平台</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">提现成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">提现成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                    <tr class="ui-list">
                                        <td class="ui-list-item pl1">2017-02-24 12:59:35</td>
                                        <td class="ui-list-item pl2">+1000</td>
                                        <td class="ui-list-item pl3">5.00</td>
                                        <td class="ui-list-item pl4">+1000</td>
                                        <td class="ui-list-item pl5">提现成功</td>
                                        <td class="ui-list-item pl6">江西银行</td>
                                    </tr>
                                </tbody>
                                </table>
                            </li>
                        </ul>
                        <div class="ui-pagination">
                             <ul>
                                 <li><a href="#" class="prev">上一页</a></li>
                                 <li><a href="#" class="active page-link">1</a></li>
                                 <li><a href="#" class="page-link">2</a></li>
                                 <li><a href="#" class="page-link">3</a></li>
                                 <li><a href="#" class="page-link">4</a></li>
                                 <li><span href="#" class="disable">...</span></li>
                                 <li><a href="#" class="page-link">50</a></li>
                                 <li><a href="#" class="page-link">51</a></li>
                                 <li><a href="#" class="next">下一页</a></li>
                             </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/laydate.dev.js"></script>
    <script src="../dist/js/loan/loan-tradedetails.js"></script>
</body>
</html>