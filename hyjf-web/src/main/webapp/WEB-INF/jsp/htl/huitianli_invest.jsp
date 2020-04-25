<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>汇天利介绍 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <link rel="stylesheet" href="${ctx}/css/baguetteBox.min.css" type="text/css"/>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<div class="htl-banner" style="background-image:url(${ctx}/img/htlbanner1.jpg);">
        <h4><span>历史年回报率${htlForm.htlRate}%</span><span>一元即可加入</span><span>期限：活期，随时进出</span><span>保障方式：全额兑付</span></h4>
    </div>
    <div class="new-detail-con">
        <div class="new-detail-inner htl">
            <div class="new-detail-hd">
                <div class="hd2">
                    <input type="hidden" id="projectData" data-total="${htlForm.avaPurchaseNumber }" data-type="4" data-month-count="6" data-day-count="180" data-annual-rate="0.15">
                    <!-- 
                         id="userData" 用户数据
                         balance 可用余额
                         user-avapurchase 用户可申购余额
                     -->
                    <input type="hidden" id="userData" data-balance="${htlForm.userBalanceNumber }" data-user-avapurchase="${htlForm.userAvaPurchaseNumber}">
                    <form action="" id="detialForm">
                        <div class="hd2-item con1">
                            <div class="col-title">
                              	  申购金额：
                            </div>
                            <div class="col-content">
                                <div class="money">
                                	<input type="hidden" name="flag" value="1">
                                    <!-- 投标开始 -->
                                    <input type="number" name="amount" id="moneyIpt" class="money-input" placeholder="投标金额应大于 1 元" oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off">
                                    <a href="javascript:;" class="money-btn available">全投</a>
                                </div>
                                <span class="helper">建议持有金额在100元以上</span>
                            </div>
                            <div class="col-info">剩余可申购金额：${htlForm.avaPurchase} 元</div>
                        </div>
                        <div class="hd2-item con2">
                            <div class="col-title">
                         		       账户余额：
                            </div>
                            <div class="col-content">${htlForm.userBalance} 元<a href="${ctx}/recharge/rechargePage.do" class="recharge">去充值 ></a></div>
                        </div>
                        <div class="hd2-item con3">
                            <div class="col-title">
                       			        收益发放日期：
                            </div>
                            <div class="col-content">
                                <span class="highlight">${htlForm.interestDate}</span>
                            </div>
                        </div>
                        <div class="hd2-item con4">
                            <div class="col-title"></div>
                            <div class="col-content">
                                <div class="terms-check checked">
                                    <div class="checkicon"></div>
                                    您同意汇盈金服<a href="#" target="_blank" class="terms">《投资咨询与管理服务协议》</a>
                                    <input type="checkbox" name="termcheck" checked="checked">
                                </div>
                            </div>
                        </div>
                        <div class="hd2-item con5">
                            <div class="col-title"></div>
                            <div class="col-content">
                                <!-- 投标开始 -->
                                <a href="#" class="confirm-btn avaliable">确认投资</a>
                            </div>
                        </div>
                    </form>
                    <div class="clearfix"></div>
                </div>
            </div>
            <div class="new-detail-main">
                <ul class="new-detail-tab">
                    <li panel="0" class="active">产品介绍</li>
                    <li panel="1">产品详情</li>
                    <li panel="2">客户答疑</li>
                </ul>
                <ul class="new-detail-tab-panel">
                    <li panel="0" class="active">
                        <dl class="new-detail-dl">
                            <dt>安全性</dt>
                            <dd>
                                <ol>
                                    <li>本产品由平台的合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划</li>
                                    <li>由汇付天下资金托管，投资资金进出全程封闭</li>
                                </ol>
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>流动性</dt>
                            <dd>
                                <ol>
                                    <li>资金灵活，贴心为您着想</li>
                                    <li>投资金额可7*24小时购买赎回汇天利产品</li>
                                </ol>
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>收益性</dt>
                            <dd>
                                <ol>
                                    <li>历史年回报率符合市场规律，合理，规范</li>
                                    <li>汇天利产品暂不收取平台服务费用</li>
                                </ol>
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>温馨提示</dt>
                            <dd>
                                <ol>
                                    <li>最低充值金额应大于等于 1 元；</li>
                                    <li>请投资人根据发标计划合理充值，因汇盈金服无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现；</li>
                                </ol>
                            </dd>
                        </dl>
                    </li>
                    <li panel="1">
                    	<dl class="new-detail-dl">
                            <dt>产品名称</dt>
                            <dd>汇天利</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>产品介绍</dt>
                            <dd>
                                汇天利是由合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划。投资方的加入资金及加入资金所产
                                <br> 生的收益，在满足汇天利产品协议相关规则的前提下，可在随时申请部分或全部赎回。
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>投资期限</dt>
                            <dd>活期</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>历史年回报率</dt>
                            <dd>${htlForm.htlRate}%</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>起投金额</dt>
                            <dd>一元</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>累计上限</dt>
                            <dd>单户累计投资上限为${htlForm.userPupper}元</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>保证措施</dt>
                            <dd>合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划，产品全额兑付</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>首次计息</dt>
                            <dd>次日0时</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>温馨提示</dt>
                            <dd>
                                <ol>
                                    <li>最低充值金额应大于等于 1 元；</li>
                                    <li>请投资人根据发标计划合理充值，因汇盈金服无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现</li>
                                </ol>
                            </dd>
                        </dl>
                        
                    </li>
                    <li panel="2">
                        <dl class="new-detail-dl">
                            <dt>1、汇天利怎么计算收益？</dt>
                            <dd>
                                汇天利每日每万元收益为1.11元。历史年回报率为${htlForm.htlRate}%，计算公式：（1.11x360）÷10000=4.0%。例如：投资客户在汇天利中计息的资金为9000元，代入计算公式，当日收益=9000元 x1.11÷10000=0.99元。
                            </dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>2、汇天利什么时候可以购买？</dt>
                            <dd>随时购买，次日0点计息</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>3、汇天利的赎回是否收费？</dt>
                            <dd>免费赎回，不限次数，赎回金额再提现由支付机构收取一元提现费用</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>4、汇天利的赎回金额有限制吗？</dt>
                            <dd>在投资汇天利产品金额内的资金，可部分或全部赎回</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>5、赎回汇天利后资金什么时候能到账？</dt>
                            <dd>到账时间与平台其他产品完全一致，赎回一经发起，即刻到达投资方在支付机构的专属账户，如需提现，参考支付机构规则。</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>6、汇天利的加入金额有限制吗？</dt>
                            <dd>汇天利的加入金额1元起，投资方在汇天利单项产品中的总金额不得超过${htlForm.userPupper}元。</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt> 7、汇天利收取服务费或其他费用吗？</dt>
                            <dd>汇天利产品暂不收取任何费用，如有变动请以汇盈金服平台公告为准</dd>
                        </dl>
                        <dl class="new-detail-dl">
                            <dt>温馨提示</dt>
                            <dd>
                                <ol>
                                    <li>最低充值金额应大于等于 1 元；</li>
                                    <li>请投资人根据发标计划合理充值，因汇盈金服无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现；</li>
                                </ol>
                            </dd>
                        </dl>
                    </li>
                </ul>
            </div>
            <div class="clearfix"></div>
        </div>
    </div>

<jsp:include page="/footer.jsp"></jsp:include>
    <script type="text/javascript" src="../js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/jquery.metadata.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/baguetteBox.min.js" charset="utf-8"></script>
    <script type="text/javascript" src="../js/htl/huitianliInvest.js" charset="utf-8"></script>
    <script>
    $(".terms-check").click(function(e) {
        var _self = $(e.target),checkbox;
        if(_self.hasClass("checkicon")){
            checkbox = _self.siblings("input[type=checkbox]");
            _self=_self.parent();
        }else{
            checkbox = _self.find("input[type=checkbox]");
        }
        if (!_self.hasClass("terms")) {
            if (_self.hasClass("checked")) {
                _self.removeClass("checked");
                checkbox.prop("checked", false);
                $(".confirm-btn").removeClass("avaliable").addClass("disabled");
            } else {
                _self.addClass("checked");
                checkbox.prop("checked", true);
                 $(".confirm-btn").addClass("avaliable").removeClass("disabled");
            }
        }
    })
    </script>
</body>
</html>