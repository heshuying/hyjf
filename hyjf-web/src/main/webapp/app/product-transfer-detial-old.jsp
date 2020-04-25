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
                        <li class="active" panel="0"><a href="javascript:;">风控信息</a></li>
                        <li panel="1"><a href="javascript:;">相关文件</a></li>
                        <li panel="2"><a href="javascript:;">还款计划</a></li>
                        <li panel="3"><a href="javascript:;">投资记录</a></li>
                        <li panel="4"><a href="javascript:;">常见问题</a></li>
                    </ul>
                    <ul class="tab-panels">
                        <li panel="0" class="active">
                            <div class="attr-title"><span>风控信息</span></div>
                            <div class="attr-table">
                                <img alt="项目流程图" src="http://img.hyjf.com/dist/images/product/product_old_flow.png">
                            </div>
                            <div class="attr-title"><span>基础信息</span></div>
                            <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <tbody>
                                        <tr>
                                            <td width="50%"><span class="key">所在地区：</span> <span class="val"></span></td>
                                            <td width="50%"><span class="key">抵/注册资本：</span> <span class="val">元</span></td>
                                        </tr>
                                        <tr>
                                            <td><span class="key">注册时间：</span> <span class="val">经营收入</span></td>
                                            <td><span class="key">信用评级：</span> <span class="val">AA</span></td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div class="attr-title"><span>项目介绍</span></div>
                            <div class="attr-note" style="color:#7F7F80;">
                                所属行业：加工销售
                                <br> 主营业务：从事棉花收购、加工及棉纺织品的销售等
                                <br> 融资用途：采购棉花等
                                <br> 抵/质押物：融资企业提供企业名下一处工业用地为本项目融资提供相应的安全保障措施，土地使用权面积41260.00平方米，价值536.38万元，产权清晰。
                                <br> 财务状况：融资企业2016年主营业务收入30377.25万元，净利润3392.58万元；本年度截止至2017年04月底，主营业务收入13873.06万元，净利润1581.63万元。
                                <br> 在平台逾期次数：0
                                <br> 在平台逾期金额：0
                                <br> 第一还款来源：经营收入
                                <br> 第二还款来源：合作机构履行无限连带责任
                                <br> 费用说明：加入费用0元
                            </div>
                            <div class="attr-title"><span>风控措施</span></div>
                            <div class="attr-note" style="color:#7F7F80;">
                                1. 汇盈金服已对该项目进行了严格的审核并通过。
                                <br>2. 融资企业法定代表人提供无限连带责任保证。
                                <br>3. 若融资方未能按时偿还该项目款项，将由合作机构履行无限连带责任。
                                <br>4. 若合作机构未能按时履约，汇盈金服将使用风险保证金进行垫付。
                                <br>5. 若该项目无法偿还款项，将由资产管理公司协助处置该笔融资项目中涉及的资产。
                                <br>
                            </div>
                        </li>
                        <li panel="1">
                            <!-- 相关文件 -->
                            <div class="new-detail-img-c">
                                <ul>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494904059428.jpg" data-caption="承诺函">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494904059428.jpg" alt=""> </div>
                                            <span class="title">承诺函</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901827628.jpg" data-caption="营业执照">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901827628.jpg" alt=""> </div>
                                            <span class="title">营业执照</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901829003.jpg" data-caption="开户许可证">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901829003.jpg" alt=""> </div>
                                            <span class="title">开户许可证</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901830749.jpg" data-caption="机构信用代码证">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901830749.jpg" alt=""> </div>
                                            <span class="title">机构信用代码证</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901832247.jpg" data-caption="身份证">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901832247.jpg" alt=""> </div>
                                            <span class="title">身份证</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901835086.jpg" data-caption="土地证">
                                            <div> <img src="https://www.hyjf.com/data/upfiles/image/HDD17050386/1494901835086.jpg" alt=""> </div>
                                            <span class="title">土地证</span>
                                        </a>
                                    </li>
                                </ul>
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
                        <li panel="3" class="">
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
                        <li panel="4">
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