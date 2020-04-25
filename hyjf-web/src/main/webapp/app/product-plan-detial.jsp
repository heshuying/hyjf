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
	<section class="breadcrumbs">
        <div class="container">
            <div class="left-side">您所在的位置： <a href="#">首页</a> &gt; <a href="#">债权</a> &gt; <a href="#">债权投资</a> &gt; 项目详情</div>
            <div class="right-side"><span>开标时间：</span>2017-02-08 18:50:00</div>
        </div>
    </section>
    <article class="main-content product">
        <div class="container">
            <!-- start 内容区域 -->
            <div class="product-intr">
                <div class="title">
                    <span>天天赢/D-30</span>
                    <!--<div class="title-tag">新手专享</div>-->
                    <div class="contract-box">
                        <!--TODO:产品协议、平台协议相关修改start-->
                        <a href="#">《投资服务协议》</a>
                        <a href="#">《投资风险确认书》</a>
                        <!--TODO:产品协议、平台协议相关修改end-->
                    </div>
                </div>
                <div class="attr">
                    <div class="attr-item attr1">
                            <span class="val highlight">8.00<span class="unit"> %</span>
                                <!--<div class="poptag-pos">-->
                                <!--<div class="poptag">+0.5%</div>-->
                                <!--</div>-->
                            </span>
                        <span class="key">历史年回报率</span>
                    </div>
                    <div class="attr-item attr2">
                        <span class="val">30<span class="unit"> 天</span> </span>
                        <span class="key">锁定期限</span>
                    </div>
                    <!--TODO:项目金额改成加入人次start-->
                    <div class="attr-item attr3">
                        <span class="val">100,000<span class="unit"> 次</span></span>
                        <span class="key">加入人次</span>
                    </div>
                    <!--TODO:项目金额改成加入人次end-->
                </div>
                <div class="list">
                    <!--TODO:安全保障计划等文案修改start-->
                    <div class="list-item"><i class="safe"></i>保障方式：安全保障计划</div>
                    <div class="list-item">计息时间：计划进入锁定期后开始计息</div>
                    <div class="list-item">还款方式：按天计息，到期还本还息</div>
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                    <!--TODO:安全保障计划等文案修改end-->
                </div>
            </div>
            <!--<div class="product-form status">
            <div class="status-content">
                    <div class="title">
                        开标倒计时
                    </div>
                    <div class="timer">
                        <div id="counterTimer" class="counter" data-date="2017/6/24 15:31:00">
                            <div class="countdown"></div>
                        </div>
                    </div>
                </div> -->
            <!-- <div class="status-content yhk"></div> -->
            <!-- 已还款 -->
            <!-- <div class="status-content hkz"></div> -->
            <!-- 还款中 -->
            <!-- <div class="status-content fsz"></div> -->
            <!-- 复审中 -->
            <!-- <div class="status-content qsz"></div> -->
            <!-- 清算中 -->
            <!-- <div class="status-content qswc"></div> -->
            <!-- 清算完成 -->
            <!--<div class="start-time">开标时间：2017-05-12 14：04：24</div>
            </div> -->
            <form class="product-form" action="post" id="productForm">
                <div class="field">
                    <!--TODO：项目金额改成计划可投start-->
                    <div class="key">开放额度：</div>
                    <div class="val"><span class="highlight">216,5305.35</span> 元</div>
                    <!--TODO：项目金额改成计划可投end-->
                </div>
                <div class="field">
                    <div class="key">可用金额：</div>
                    <div class="val">12,211,400.00 元</div>
                    <a href="#" class="link-recharge">充值</a>
                </div>
                <div class="field allow-invest">
                    <div class="input">
                        <input type="text" name="money" id="money" placeholder="100元起投，100元递增" autocomplete="off"/>
                        <div class="btn sm" id="fullyBtn">全投</div>
                    </div>
                </div>
                <div class="field sub allow-invest">
                    <div class="key dark">优惠券：</div>
                    <div class="val fl-r"><a href="#" class="link-coupon" id="goCoupon"><span>请选择优惠券</span> &gt;</a></div>
                    <input type="hidden" name="coupon" value="" data-type="0" data-count="4" data-id="1" data-txt="请选择优惠券"
                           id="couponInput">
                </div>
                <div class="field sub allow-invest">
                    <div class="key dark">历史回报：</div>
                    <div class="val fl-r" id="income">0.00 元</div>
                </div>
                <div class="field allow-invest">
                    <div class="btn submit" id="goSubmit">立即投资</div>
                </div>
                <div class="field not-allow-invest">
                    <div class="later-open">
                        <span class="text">稍后开启</span>
                    </div>
                </div>

                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">加入确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">计划名称：</span></td>
                                <td><span id="prodNum">天天盈/D-30</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">预期年化收益率：</span></td>
                                <td><span id="">8.5%</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">锁定期限：</span></td>
                                <td>30天</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">还款方式：</span></td>
                                <td>按天计息，到期还本还息</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">加入金额：</span></td>
                                <td><span class="red" id="">100</span> 元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">优惠券：</span></td>
                                <td>1.00%加息券</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">历史回报：</span></td>
                                <td>123.54 元</td>
                            </tr>
                        </table>
                        <div class="cutline"></div>
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">实际支付：</span></td>
                                <td><span class="total" id="">49,234</span> 元</td>
                            </tr>
                        </table>
                        <div class="product-term">
                            <div class="term-checkbox checked"></div>
                            <span>我已同意并阅读  <a href="#">《投资服务协议》</a><a href="#">《投资风险确认书》</a></span>
                        </div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="confirmDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="confirmDialogConfirm">确 认</div>
                    </div>
                </div>
                <div class="dialog dialog-alert dialog-coupon" id="couponDialog">
                    <div class="title">请选择优惠券</div>
                    <div class="content">
                        <div class="alert-coupon-content">
                            <div class="coupon-item jxq" data-type="0" data-id="1" data-txt="1%加息券">
                                <div class="main">
                                    <span class="num">1</span>
                                    <span class="unit">%</span>
                                    <span class="type">加息券</span>
                                </div>
                                <div class="attr">
                                    <table>
                                        <tr>
                                            <td width="72" align="right">投资金额：</td>
                                            <td>1万~100万元</td>
                                        </tr>
                                        <tr>
                                            <td align="right">操作平台：</td>
                                            <td>不限</td>
                                        </tr>
                                        <tr>
                                            <td align="right">投资期限：</td>
                                            <td>大于等于6个月</td>
                                        </tr>
                                        <tr>
                                            <td align="right">项目类型：</td>
                                            <td>不限</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="date">2016/6/12~2016/6/30</div>
                                <div class="checked">已选中</div>
                            </div>
                            <div class="coupon-item djq" data-type="1" data-id="2" data-txt="10000元代金券">
                                <div class="main">
                                    <span class="num">10000</span>
                                    <span class="unit">元</span>
                                    <div class="type">代金券</div>
                                </div>
                                <div class="attr">
                                    <table>
                                        <tr>
                                            <td width="72" align="right">投资金额：</td>
                                            <td>1万~100万元</td>
                                        </tr>
                                        <tr>
                                            <td align="right">操作平台：</td>
                                            <td>APP</td>
                                        </tr>
                                        <tr>
                                            <td align="right">投资期限：</td>
                                            <td>大于等于6个月</td>
                                        </tr>
                                        <tr>
                                            <td align="right">项目类型：</td>
                                            <td>汇直投 汇消费 尊享汇</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="date">2016/6/12~2016/6/30</div>
                                <div class="checked">已选中</div>
                            </div>
                            <div class="coupon-item tyj" data-type="2" data-id="3" data-txt="10000元体验金">
                                <div class="main">
                                    <span class="num">10000</span>
                                    <span class="unit">元</span>
                                    <div class="type">体验金</div>
                                </div>
                                <div class="attr">
                                    <table>
                                        <tr>
                                            <td width="72" align="right">投资金额：</td>
                                            <td>1万~100万元</td>
                                        </tr>
                                        <tr>
                                            <td align="right">操作平台：</td>
                                            <td>不限</td>
                                        </tr>
                                        <tr>
                                            <td align="right">投资期限：</td>
                                            <td>大于等于6个月</td>
                                        </tr>
                                        <tr>
                                            <td align="right">项目类型：</td>
                                            <td>汇直投 汇消费 尊享汇</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="date">2016/6/12~2016/6/30</div>
                                <div class="checked">已选中</div>
                            </div>
                            <div class="cutline"></div>
                            <div class="coupon-item unava">
                                <div class="main">
                                    <span class="num">10000</span>
                                    <span class="unit">元</span>
                                    <div class="type">体验金</div>
                                </div>
                                <div class="attr">
                                    <table>
                                        <tr>
                                            <td width="72" align="right">投资金额：</td>
                                            <td>1万~100万元</td>
                                        </tr>
                                        <tr>
                                            <td align="right">操作平台：</td>
                                            <td>不限</td>
                                        </tr>
                                        <tr>
                                            <td align="right">投资期限：</td>
                                            <td>大于等于6个月</td>
                                        </tr>
                                        <tr>
                                            <td align="right">项目类型：</td>
                                            <td>汇直投 汇消费 尊享汇</td>
                                        </tr>
                                    </table>
                                </div>
                                <div class="date">2016/6/12~2016/6/30</div>
                            </div>
                        </div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="couponDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="couponDialogConfirm">确 认</div>
                    </div>
                </div>
            </form>
            <!-- end 内容区域 -->
        </div>
        <div class="container">
            <!--TODO:项目流程图改成计划流程图start-->
            <section class="content">
                <div class="main-title">
                    计划流程图
                </div>
                <div class="flow-content">
                    <div class="item done items">
                        <div class="icons icons-jrjh"></div>
                        加入计划
                    </div>
                    <div class="line lines done">
                        <span class="upper">智能投标</span>
                        <span class="lower">出借成功</span>
                    </div>
                    <div class="item done items">
                        <div class="icons icons-jrsdq"></div>
                        进入锁定期
                    </div>
                    <div class="line lines">
                        <span class="upper">开始计息</span>
                        <span class="lower">30天</span>
                    </div>
                    <div class="item done items">
                        <div class="icons icons-sdqjs"></div>
                        锁定期结束
                    </div>
                    <div class="line lines">
                        <span class="upper">退出计划</span>
                        <span class="lower">1-3个工作日</span>
                    </div>
                    <div class="item done items">
                        <div class="icons icons-bxhk"></div>
                        本息回款
                    </div>
                </div>
            </section>
            <!--TODO:项目流程图改成计划流程图end-->
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">计划介绍</a></li>
                        <li panel="1"><a href="javascript:;">标的组成</a></li>
                        <li panel="2"><a href="javascript:;">加入记录</a></li>
                        <li panel="3"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <!--TODO:项目详情修改为计划介绍start-->
                        <li class="active" panel="0">
                            <!-- 未登录状态 -->
                            <div class="unlogin">
                                <div class="icon"></div>
                                <p>请先 <a href="#">登录</a> 或 <a href="#">注册</a> 后可查看</p>
                            </div>
                            <!-- 计划介绍 -->
                            <!--<div class="attr-title"><span>基础信息</span></div>-->
                            <div class="attr-table" id="planIntro">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                    <tr>
                                        <td width="100%">
                                            <span class="key">计划名称</span>
                                            <span class="val">天天盈/D-30</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">计划介绍</span>
                                            <span class="val">天天盈计划是汇盈金服方便用户投资推出的标准化投资工具，用户加入计划后，平台立即启动优先匹配投标，按用户认可的标的范围内，系统对符合要求的标的进行优先自动投标，提高了用户投资的速度和灵活度，及时盘活用户资金，更好的满足用户多样化的理财需求。</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">历史年回报率</span>
                                            <span class="val">6.5%</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">锁定期限</span>
                                            <span class="val">30天</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">还款方式</span>
                                            <span class="val">按天计息，到期还本还息</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">投资范围</span>
                                            <span class="val">请参考标的组成</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">加入条件</span>
                                            <span class="val">100.00元起，以100.00元的倍数递增</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">计息方式</span>
                                            <span class="val">计划进入锁定期后开始计息</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">退出方式</span>
                                            <span class="val">系统将通过债权转让自动完成退出，您所持债权转让完成的具体时间，视债权转让市场交易情况而定</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">到账时间</span>
                                            <span class="val">计划退出后，预计1~3个工作日内到账，具体到账时间，以实际运营情况为准。</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">费用</span>
                                            <span class="val">无相关费用</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">计划服务协议</span>
                                            <span class="val">点击查看<a href="#" class="agree">《投资服务协议》</a></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="key">投资标的协议</span>
                                            <span class="val">点击查看<a href="#" class="agree">《居间服务协议》</a></span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                        <!--TODO:项目详情修改为计划介绍end-->
                        <!--TODO:投资记录修改为债权列表start-->
                        <li panel="1" class="">
                            <!-- 债权列表 -->
                            <p><span class="dark" style="padding-left:70px;">本计划投资的项目限汇添金专属资产和转让资产；债权列表动态变化，具体以实际投资为准。</span></p>
                            <div class="attr-table">
                                <div class="data-empty">
                                    <div class="empty-icon"></div>
                                    <p class="align-center">暂无可投标的</p>
                                    <!-- 有跳转添加 -->
                                    <!--<p class="align-center"><a href="javascript:;" class="btn sm btn-primary">去投资</a></p>-->
                                </div>
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr>
                                        <th width="25%">项目编号</th>
                                        <th width="12%">历史年回报率</th>
                                        <th width="12%">项目期限</th>
                                        <th width="13%">借款金额</th>
                                        <th width="13%">借款用途</th>
                                        <th width="25%">还款方式</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            <span>HDD16080202</span>
                                        </td>
                                        <td class="orange"><span>16.00</span>%</td>
                                        <td>6个月</td>
                                        <td>1,000,000元</td>
                                        <td>个人长期借款</td>
                                        <td><span class="dark">按月计息，到期还本还息</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>HDD16080202</span>
                                        </td>
                                        <td class="orange"><span>16.00</span>%</td>
                                        <td>6个月</td>
                                        <td>1,000,000元</td>
                                        <td>个人长期借款</td>
                                        <td><span class="dark">按月计息，到期还本还息</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>HDD16080202</span>
                                        </td>
                                        <td class="orange"><span>16.00</span>%</td>
                                        <td>6个月</td>
                                        <td>1,000,000元</td>
                                        <td>个人长期借款</td>
                                        <td><span class="dark">按月计息，到期还本还息</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>HDD16080202</span>
                                        </td>
                                        <td class="orange"><span>16.00</span>%</td>
                                        <td>6个月</td>
                                        <td>1,000,000元</td>
                                        <td>个人长期借款</td>
                                        <td><span class="dark">按月计息，到期还本还息</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>HDD16080202</span>
                                        </td>
                                        <td class="orange"><span>16.00</span>%</td>
                                        <td>6个月</td>
                                        <td>1,000,000元</td>
                                        <td>个人长期借款</td>
                                        <td><span class="dark">按月计息，到期还本还息</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="pages-nav">
                                    <div class="prev">上一页</div>
                                    <a href="">1</a>
                                    <a href="">2</a>
                                    <a href="">3</a>
                                    <a href="">4</a>
                                    <a href="">...</a>
                                    <a href="">50</a>
                                    <a href="">51</a>
                                    <div class="next">下一页</div>
                                </div>
                            </div>
                        </li>
                        <!--TODO:投资记录修改为债权列表end-->
                        <!--TODO:还款计划修改为加入记录start-->
                        <li panel="2" class="">
                            <!-- 还款计划 -->
                            <!-- 投资记录 -->
                            <p>
                                <span>加入总人次： 358</span>&nbsp;&nbsp;
                                <span>加入金额：5737000元</span>
                            </p>
                            <div class="attr-table">
                                <div class="data-empty">
                                    <div class="empty-icon"></div>
                                    <p class="align-center">咦，您所找的页面暂无数据～</p>
                                    <!-- 有跳转添加 -->
                                    <p class="align-center"><a href="javascript:;" class="btn sm btn-primary">去干啥</a></p>
                                </div>
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                    <tr>
                                        <th width="35%">投资人</th>
                                        <th width="25%">加入金额（元）</th>
                                        <th width="15%">来源</th>
                                        <th width="25%">加入时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            <span>fm**********</span>
                                            <div class="tag">VIP</div>
                                        </td>
                                        <td>20000.00 </td>
                                        <td>PC</td>
                                        <td><span class="dark">2016.08.18  12:00</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>fm**********</span>
                                            <div class="tag">VIP</div>
                                        </td>
                                        <td>20000.00 </td>
                                        <td>IOS</td>
                                        <td><span class="dark">2016.08.18  12:00</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>fm**********</span>
                                            <div class="tag">VIP</div>
                                        </td>
                                        <td>20000.00 </td>
                                        <td>Android</td>
                                        <td><span class="dark">2016.08.18  12:00</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>fm**********</span>
                                            <div class="tag">VIP</div>
                                        </td>
                                        <td>20000.00 </td>
                                        <td>微信</td>
                                        <td><span class="dark">2016.08.18  12:00</span></td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span>fm**********</span>
                                            <div class="tag">VIP</div>
                                        </td>
                                        <td>20000.00 </td>
                                        <td>PC</td>
                                        <td><span class="dark">2016.08.18  12:00</span></td>
                                    </tr>
                                    </tbody>
                                </table>
                                <div class="pages-nav">
                                    <div class="prev">上一页</div>
                                    <a href="">1</a>
                                    <a href="">2</a>
                                    <a href="">3</a>
                                    <a href="">4</a>
                                    <a href="">...</a>
                                    <a href="">50</a>
                                    <a href="">51</a>
                                    <div class="next">下一页</div>
                                </div>
                            </div>
                        </li>
                        <!--TODO:还款计划修改为加入记录end-->
                        <!--TODO:常见问题进行修改start-->
                        <li panel="3">
                            <!-- 常见问题 -->
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                    <tr>
                                        <td width="100%">1、"计划"安全吗?</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="dark">汇盈金服以严谨负责的态度对每笔借款进行严格筛选，同时，"计划"所对应借款均适用汇盈金服用户利益保障机制。</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">2、"计划"的"锁定期"是什么？</td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="dark">
                                               "计划"出借计划具有收益期锁定限制，锁定期内，用户不可以提前退出。
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                            3、加入"计划"的用户所获收益处理方式有几种？
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="dark">
                                                本计划提供两种收益处理方式：循环复投或用户自由支配。计划退出后，用户的本金和收益将返回至其汇盈金服账户中，供用户自由支配。
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">
                                            4、"计划"通过何种方式实现自动投标？</span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="dark">
                                            "计划"不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属计划账户，所有资金通过该专属计划账户流动。
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="100%">5、"计划"到期后，我如何退出并实现收益？
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <span class="dark">
                                                "计划"到期后，系统将自动进行资金结算，结算完成后的本金及利益将将从用户"计划"账户自动划分至用户普通账户中，用户在T+3个工作日内收到资金。
                                            </span>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                        <!--TODO:常见问题进行修改end-->
                    </ul>
                </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
    <script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
    <script src="../dist/js/product/product-plan-detail.js"></script>
</body>
</html>