<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>账户充值 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <style>
    	.irech>label{
    		min-width:150px;
    	}
    	table.bank-recharge-table{
    		border:0;
    	}
    	table.bank-recharge-table td{
    		border:0;
    		text-align:left;
    	}
    	.buttonRecharge{
    		margin-left:150px;
    	}
    </style>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>
<div class="banner-111" style="background-image:url(${cdn}/img/banner_recharge.jpg); ">
        <div class="container-1084">
            <h4>账户充值</h4>
        </div>
    </div>
    <div class="section icash">
        <div class="container-1084">
            <form novalidate="novalidate" id="loginForm" action="${ctx}/bank/web/user/recharge/recharge.do" method="post">
                <input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
                <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
                <input type="hidden" name="rechargeType" id="rechargeType" value="0" />
                <div class="irech">
                    <label for="tpBanks">充值方式</label>
                    <div class="controls">
                        <ul id="tpBanks" class="controls select-bank">
                            <li title="快捷支付充值" class="" id="onlinebank">快捷支付充值<i class="iconfont iconfont-check"></i></li>
                            <li title="线下充值" class="on" id="netBank">线下充值<i class="iconfont iconfont-check"></i></li>
                        </ul>
                    </div>
                </div>
                
                <div class="irech" id="online-bank">
                	<label>提现银行卡</label>
                    <div class="controls">
                        ${cardNo}  <a href="Javascript:;" class="highlight" id="unbundling">解绑&gt;&gt;</a>
                    </div>
                    <div class="irech">
	                    <label for="amount">充值金额</label>
	                    <div class="controls recharge-controls">
	                        <input class="input-xlarge" id="chargeSum" autocomplete="off" name="money" value="" type="text" maxlength="9"><span class="unit">元</span>
	                    </div>
	                </div>
	                <div class="irech">
	                    <div class="controls control-btn">
	                        <button class="buttonRecharge" type="submit">充 值</button>
	                        <input type="hidden" name="bankCode" id="bankCode" value="">
	                        <a href="javascript:history.go(-1)" class="ib-btn">返回 &gt;</a> </div>
	                </div>
                </div>
                <div class="irech controls bankLimit" id="bank-desc"  style="display: block;">
                	<label>支付宝/网银转账</label>
                	<div class="controls">
                		<table class="bank-recharge-table">
                            <tbody>
			                <tr>
			                  <td>收款方户名：</td>
			                  <td>陈松超</td>
			                </tr>
			                <tr>
			                  <td>收款方帐号：</td>
			                  <td>6212461100002677107</td>
			                </tr>
			                <tr>
			                  <td>收款方开户行：</td>
			                  <td>江西银行股份有限公司总行营业部</td>
			                </tr>
			                <tr>
			                  <td colspan="2"><small>注：网银转账时，银行请选择（城市商业银行）江西银行或者南昌银行</small></td>
			                </tr>
			              </tbody>
                        </table>
                	 </div>   
					 <div class="irech">
	                    <div class="controls control-btn">
	                        <a href="javascript:history.go(-1)" class="ib-btn"  style="margin-left:150px;">返回 &gt;</a> 
	                    </div>
	                </div>
                </div>
                
            </form>
        </div>
    </div>
    
    <div class="tipsRecharge">
        <div class="container-1084">
            <ol>
                <li>投资人充值投资所有项目均不收取充值费用；</li>
                <li>最低充值金额应大于等于 1 元；</li>
                <li>快捷充值当日充值资金不可当日操作取现；</li>
                <li>使用快捷充值的资金，取现时原卡返回；</li>
                <li>充值/提现必须为银行借记卡，不支持存折、信用卡充值；</li>
                <li>汇付天下严禁利用充值功能进行信用卡套现、转账、洗钱等行为，一经发现，将封停账号30天；</li>
                <li>充值期间，请勿关闭浏览器，待充值成功并返回首页后，所充资金才能入账，如有疑问，请联系客服；</li>
                <li>充值需开通银行卡网上支付功能，如有疑问请咨询开户行客服；</li>
                <li>支付限额请参照 <a target="_blank" href="http://www.chinapnr.com/helpcenter_zfsm.html#contentPoint">支付说明</a>。</li>
            </ol>
        </div>
    </div>
    <jsp:include page="/footer.jsp"></jsp:include>
    <script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/user/recharge/recharge.js?version=${version}" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/jquery.cookie.min.js" charset="utf-8"></script>
</body>
</html>