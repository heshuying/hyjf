<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<script>
    var baseTableData = [{
        id: "rzzt",
        key: "融资主体",
        val: "青岛市某机械生产公司"
    }, {
        id: "zcdq",
        key: "注册地区",
        val: "山东省烟台市"
    }, {
        id: "zczb",
        key: "注册资本",
        val: "600000"
    }, {
        id: "zcsj",
        key: "注册时间",
        val: "2005年"
    }, {
        id: "fddb",
        key: "法定代表人",
        val: "刘**"
    }, {
        id: "sshy",
        key: "所属行业",
        val: "生产销售"
    }, {
        id: "zyyw",
        key: "主营业务",
        val: "环保机械的制造及销售"
    }, {
        id: "xypj",
        key: "信用评级",
        val: "AA"
    }, {
        id: "zch",
        key: "统一社会信用代码/注册号 ",
        val: "12345********"
    }];
    var assetsTableData = [
        [{
            id: "zclx",
            key: "资产类型",
            val: "住宅"
        }, {
            id: "zcwz",
            key: "资产位置",
            val: "山东省青岛市"
        }, {
            id: "zcmj",
            key: "资产面积",
            val: "345㎡"
        }, {
            id: "zcsl",
            key: "资产数量",
            val: "2"
        }, {
            id: "pgjz",
            key: "评估价值",
            val: "720万"
        }, {
            id: "zcss",
            key: "资产所属",
            val: "融资人名下/第三方名下"
        }],
        [{
            id: "zclx",
            key: "资产类型",
            val: "车辆"
        }, {
            id: "zcxh",
            key: "型号",
            val: "CR-V"
        }, {
            id: "zcgmjg",
            key: "购买价格",
            val: "193800元"
        }, {
            id: "zccph",
            key: "车牌号",
            val: "鲁F****"
        }, {
            id: "zccjh",
            key: "车架号",
            val: "L123*****"
        }, {
            id: "zcpp",
            key: "品牌",
            val: "本田"
        }, {
            id: "zcpp",
            key: "产地",
            val: "国产"
        }, {
            id: "pgjz",
            key: "评估价值",
            val: "58140元"
        }, {
            id: "zcdjd",
            key: "车辆登记地",
            val: "山东省青岛市"
        }]
    ];

    var intrTableData = [{
        id: "rzyt",
        key: "融资用途",
        val: "采购机械配件等"
    }, {
        id: "dzyw",
        key: "抵/质押物",
        val: "房产"
    }, {
        id: "cwzk",
        key: "财务状况",
        val: "融资企业2016年主营业务收入3350.01万元，净利润378.82万元；本年度截止至2017年2月底，主营业务收入681.53万元，净利润91.50万元。"
    }, {
        id: "dily",
        key: "第一还款来源",
        val: "经营收入"
    }, {
        id: "dely",
        key: "第二还款来源",
        val: "合作机构履行无限连带责任"
    }, {
        id: "fysm",
        key: "费用说明",
        val: "加入费用0元"
    }];

    var credTableData = [{
        id: "ptyqcs",
        key: "在平台逾期次数",
        val: "0"
    }, {
        id: "ptyqje",
        key: "在平台逾期金额",
        val: "0 "
    }, {
        id: "xypj",
        key: "信用评级",
        val: "AA"
    }, {
        id: "sdqk",
        key: "涉诉情况",
        val: "无/已处理"
    }];
    var reviewTableData = [{
        id: "ptyqcs",
        key: "企业证件",
        val: "已审核"
    }, {
        id: "ptyqje",
        key: "经营状况",
        val: "已审核 "
    }, {
        id: "xypj",
        key: "财务状况",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "企业信用",
        val: "无/已审核"
    }, {
        id: "sdqk",
        key: "法人信用",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "资产状况",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "购销合同",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "供销合同",
        val: "已审核"
    }];
    </script>
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
                    <span>债权转让项目---HZR170202703 </span>
                    <div class="contract-box">
                        <a href="#">投资协议(范本)</a>
                        <a href="#">风险确认书(范本)</a>
                    </div>
                </div>
                <div class="attr transfer">
                    <div class="attr-item attr4">
                        <span class="val highlight">10.00<span class="unit"> %</span> </span>
                        <span class="key">预期年化收益率</span>
                    </div>
                    <div class="attr-item attr5">
                        <span class="val highlight">0.2<span class="unit"> %</span> </span>
                        <span class="key">折让率</span>
                    </div>
                    <div class="attr-item attr6">
                        <span class="val">180<span class="unit"> 天</span> </span>
                        <span class="key">剩余期限</span>
                    </div>
                    <div class="attr-item attr7">
                        <span class="val">10,865,000<span class="unit"> 元</span></span>
                        <span class="key">出让金额</span>
                    </div>

                </div>
                <div class="list">
                    <div class="list-item">项目编号：<a href="#" class="normal-a highlight">HDF12314512</a></div>
                    <div class="list-item">还款方式：按月计息，到期还本付息</div>
                    <div class="list-item">债权持有天数：80天</div>
                    <div class="list-item"><span class="dark">建议投资者类型：稳健型及以上</span></div>
                </div>
            </div>
            <form class="product-form transfer" action="post" id="productForm">
                <div class="field">
                    <div class="key">项目剩余：</div>
                    <div class="val"><span class="highlight">216,5305.35</span> 元</div>
                </div>
                <div class="field">
                    <div class="key">可用金额：</div>
                    <div class="val">12,211,400.00 元</div>
                    <a href="#" class="link-recharge">充值</a>
                </div>
                <div class="field field-input">
                    <div class="input">
                        <input type="text" name="money" id="money" placeholder="100元起投，100元递增" autocomplete="off" />
                        <div class="btn sm" id="fullyBtn">全投</div>
                    </div>
                </div>
                <div class="field sub">
                    <div class="key dark">垫付利息：</div>
                    <div class="val fl-r">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">实际支付金额：</div>
                    <div class="val fl-r">0.00 元</div>
                </div>
                <div class="field sub">
                    <div class="key dark">历史回报：</div>
                    <div class="val fl-r" id="income">0.00 元</div>
                </div>
                <div class="field">
                    <div class="btn submit" id="goSubmit">立即投资</div>
                </div>
                <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" checked="">
                <div class="dialog dialog-alert" id="confirmDialog">
                    <div class="title">投资确认</div>
                    <div class="content">
                        <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                            <tr>
                                <td width="140" align="right"><span class="dark">预期年化收益率：</span></td>
                                <td><span id="">12%</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">折扣率：</span></td>
                                <td><span id="">0.2%</span></td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">认购本金：</span></td>
                                <td><span class="red" id="">49,234.00</span> 元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">剩余期限：</span></td>
                                <td>35天</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">垫付利息</span></td>
                                <td>60元</td>
                            </tr>
                            <tr>
                                <td align="right"><span class="dark">计息方式：</span></td>
                                <td>先息后本</td>
                            </tr>
                            
                            <tr>
                                <td align="right"><span class="dark">历史回报：</span></td>
                                <td>0.00 元</td>
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
                            <p class="highlight">注：实际支付金额=认购本金*（1-折扣率）+垫付利息</p>
                            <div class="term-checkbox checked"></div>
                            <span>我已同意并阅读  <a href="#">《居间服务协议》</a><a href="#">《投资风险确认书》</a></span>
                        </div>
                    </div>
                    <div class="btn-group">
                        <div class="btn btn-default md" id="confirmDialogCancel">取 消</div>
                        <div class="btn btn-primary md" id="confirmDialogConfirm">确 认</div>
                    </div>
                </div>
            </form>
            <!-- end 内容区域 -->
        </div>
        <div class="container">
            <section class="content">
                <div class="main-title">
                    项目流程图
                </div>
                <div class="flow-content transfer">
                    <div class="item done">
                        <div class="icon icon-sh"></div>债权人发起转让</div>
                    <div class="line done"></div>
                    <div class="item done">
                        <div class="icon icon-fb"></div>投资人认购</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-tz"></div>支付资金</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-jx"></div>计息</div>
                    <div class="line"></div>
                    <div class="item">
                        <div class="icon icon-hk"></div>回款</div>
                </div>
            </section>
        </div>
        <div class="container">
            <section class="content">
                <div class="main-tab">
                    <ul class="tab-tags">
                        <li class="active" panel="0"><a href="javascript:;">项目详情</a></li>
                        <li panel="1"><a href="javascript:;">承接记录</a></li>
                        <li panel="2"><a href="javascript:;">还款计划</a></li>
                        <li panel="3"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li class="active" panel="0">
                            <!-- 项目详情 -->
                            <div class="attr-title"><span>基础信息</span></div>
                            <div class="attr-table" id="baseTable">
                            </div>
                            <div class="attr-title"><span>资产信息</span></div>
                            <div class="attr-table" id="assetsTable">
                            </div>
                            <div class="attr-title"><span>项目介绍</span></div>
                            <div class="attr-table" id="intrTable">
                            </div>
                            <div class="attr-title"><span>信用状况</span></div>
                            <div class="attr-table" id="credTable">
                            </div>
                            <div class="attr-title"><span>审核状况</span></div>
                            <div class="attr-table" id="reviewTable">
                            </div>
                            <!--单列表格样式-->
                            <div class="attr-title"><span>单列表格样式</span></div>
                            <div class="attr-table" id="reviewTable">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="100%"><span class="key">融资用途</span> <span class="val">采购机械配件等</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">财务状况</span> <span class="val">融资企业2016年主营业务收入3350.01万元，净利润378.82万元；本年度截止至2017年2月底，主营业务收入681.53万元，净利润91.50万元。</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <!--单列表格样式-->
                            <div class="attr-title"><span>双列表格样式</span></div>
                            <div class="attr-table" id="reviewTable">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="50%"><span class="key">融资用途</span> <span class="val">采购机械配件等</span></td>
                                            <td width="50%"><span class="key">抵/质押物</span> <span class="val">房产</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">第一还款来源</span> <span class="val">经营收入</span></td>
                                            <td><span class="key">第二还款来源</span> <span class="val">合作机构履行无限连带责任</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="attr-note">
                                <p style="color: #999999;">1、汇盈金服已对该项目进行了严格的审核，最大程度的确保融资方信息的真实性，但不保证审核信息完全无误。
                                    <br/> 2、汇盈金服仅为信息发布平台，未以任何明示或暗示的方式对出借人提供担保或承诺保本保息，出借人应根据自身的投资偏好和风险承受能力进行独立判断和作出决策，并自行承担资金出借的风险与责任。
                                </p>
                            </div>
                        </li>
                        <li panel="1" class="">
                            <!-- 投资记录 -->
                            <p>
                                <span>加入总人次： 358</span>&nbsp;&nbsp;
                                <span>加入金额：5737000元</span>
                            </p>
                            <div class="attr-table">
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
                        <li panel="2" class="">
                            <!-- 还款计划 -->
                            <p><span class="dark" style="padding-left:70px;">还款时间在标的放款后生成</span></p>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">还款时间</th>
                                            <th width="25%">还款期数</th>
                                            <th width="15%">类型</th>
                                            <th width="25%">还款金额</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <span class="dark">2016.08.18  12:00</span>
                                            </td>
                                            <td>第一期</td>
                                            <td>本息</td>
                                            <td>12,750.00</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <span class="dark">2016.08.18  12:00</span>
                                            </td>
                                            <td>第一期</td>
                                            <td>本息</td>
                                            <td>12,750.00</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <span class="dark">2016.08.18  12:00</span>
                                            </td>
                                            <td>第一期</td>
                                            <td>本息</td>
                                            <td>12,750.00</td>
                                        </tr>
                                        <tr>
                                            <td>
                                                <span class="dark">2016.08.18  12:00</span>
                                            </td>
                                            <td>第一期</td>
                                            <td>本息</td>
                                            <td>12,750.00</td>
                                        </tr>
                                    </tbody>
                                </table>
                                <div class="pages-nav">
                                    <a href="" class="prev">上一页</a>
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
                        <li panel="3">
                            <!-- 常见问题 -->
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">1、我可以投资吗？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>持有本人中华人民共和国居民身份证的公民，且年满十八周岁，都可在汇盈金服网站上进行注册、完成实名认证，成为投资人。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">2、怎样进行投资？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>请您按照以下步骤进行投资：
                                                <br/> 1. 在汇盈金服网站或手机客户端上进行注册、通过实名认证、成功绑定银行卡；
                                                <br/> 2. 账户充值；
                                                <br/> 3. 浏览平台融资项目，根据个人风险偏好自主选择项目投资；
                                                <br/> 4. 确认投资，投资成功。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">3、投资后是否可以提前退出？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>1. 平台产品暂不支持提前回款申请。
                                                <br/> 2. 汇直投和尊享汇融资项目支持债权转让。</td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">4、为何投标后会显示资金冻结？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>对于所有投资项目，投资人可自主选择进行投资。在项目完成放款之前，投资金额将被冻结；在项目完成放款之后，投资金额将通过第三方资金托管机构（汇付天下）转给融资方；如果在限定时间内未满标，则根据情况将已融资金放款给融资方或原路返还投资人。
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">5、在汇盈金服投资有哪些费用？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>在汇盈金服平台进行投资，平台本身不收取投资人任何费用，投资人在充值/提现时第三方资金托管机构（汇付天下）会收取相关手续费。
                                                <br/> 提现方式： 一般提现 快速取现 即时取现
                                                <br/> 手续费： 1元／笔 1元／笔 ＋ 提现金额的0.05% 1元／笔 ＋ 提现金额的0.05%
                                                <br/> 到账时间：
                                                <br/> 一般提现：提现日后下一个工作日到账（T+1）
                                                <br/> 快速提现：工作日当日14:30前发起，当日到账（T+0）
                                                <br/> 即时提现：提现当日到账（目前只有部分银行支持）
                                                <br/>
                                                <br/> 特别提示：快速提现的手续费的计算方式为1元/笔+提现金额的0.05%，只适用于提现日后的一天为工作日的情况，如果后一天是非工作日，提现的手续费计算方式为1元/笔+提现金额的0.05% x（1+非工作日的天数）。例：周五申请快速提现，手续费=1元/笔+提现金额的0.05% x（1+2）
                                            </td>
                                        </tr>
                                        <tr>
                                            <td width="100%">
                                                <span class="dark">6. 投资人风险测评目的和规则是什么？</span>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td>为完善对投资者风险承受能力的尽职评估，实现对投资者的等级管理，保障投资者购买合适的产品，根据投资者风险测评的结果，将投资者风险承受能力由低到高分为保守型、稳健型、成长型、进取型四种类型。
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </li>
                    </ul>
                </div>
            </section>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
    <script src="../dist/js/product/data-format.js"></script>
    <script src="../dist/js/product/product-transfer-detail.js"></script>
</body>
</html>