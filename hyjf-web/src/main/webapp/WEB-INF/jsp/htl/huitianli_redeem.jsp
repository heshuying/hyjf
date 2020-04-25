<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>汇天利转出 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="banner-111"
		style="background-image:url(${ctx}/img/banner_recharge.jpg); ">
		<h4>
			<center>汇天利转出</center>
		</h4>
	</div>
	<div class="rasom-section">
		<div class="container-1200">
			<div class="tips">
				<label>历史年回报率${htlForm.htlRate }% </label> <label>一元即可加入</label> <label>期限：活期，随时进出</label>
				<label>保障方式：全额兑付</label>
			</div>
			<form action="" id="detailForm">
				<input type="hidden" name="" id="userData"
					data-balance="${htlForm.userPrincipalNumber }">
				<div class="form-group">
					<div class="col-title">可提现本金</div>
					<div class="col-content total">
						<span class="highlight">${htlForm.userPrincipal }</span> 元
					</div>
				</div>
				<div class="form-group">
					<div class="col-title">提现金额</div>
					<div class="col-content">
						<div class="money">
							<input type="number" name="amount" id="amount"
								class="money-input" oncopy="return false" onpaste="return false"
								oncut="return false" oncontextmenu="return false"
								autocomplete="off">
						</div>
						<div class="info">
							转出后可获得利息 <span class="highlight" id="income">0.00</span> 元
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-content push-title">
						<div class="terms-check checked">
							<div class="checkicon"></div>
							您同意汇盈金服<a href="#" target="_blank" class="terms">《投资咨询与管理服务协议》</a>
							<input type="checkbox" name="termcheck" checked="checked">
						</div>
					</div>
				</div>
				<div class="form-group btn-group">
					<div class="col-content push-title">
						<a href="#" class="btn confirm-btn" id="confirmBtn">确认转出</a> <a href="javascript:history.go(-1)" class="back">返回</a>
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="rasom-tab">
		<div class="container-1200">
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
								<li>本产品由平台的合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划，本息安全有保障</li>
								<li>由汇付天下资金托管，投资资金进出全程封闭</li>
								<li>合作机构（如基金公司）提供全额兑付，本金更安全、收益稳定</li>
							</ol>
						</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>流动性</dt>
						<dd>
							<ol>
								<li>资金随进随出，贴心为您着想</li>
								<li>投资金额可7*24小时购买转出汇天利产品</li>
								<li>随时转出，不限时间、不限金额，随取随用更灵活</li>
							</ol>
						</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>收益性</dt>
						<dd>
							<ol>
								<li>收益近17倍高于银行活期存款，近1.5倍宝类产品，一样灵活，更高收益</li>
								<li>汇天利产品暂不收取平台服务费用，收益100%回馈投资客户</li>
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
					<table class="bill_tabl" border="0" cellspacing="0">
						<tbody>
							<tr>
								<th width="110">产品名称：</th>
								<td>汇天利</td>
							</tr>
							<tr>
								<th>产品介绍：</th>
								<td>汇天利是由合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划。投资方的加入资金及加入资金所产生的收益，在满足汇天利产品协议相关规则的前提下，可在随时申请部分或全部转出。</td>
							</tr>
							<tr>
								<th>投资期限：</th>
								<td>活期</td>
							</tr>
							<tr>
								<th>历史年回报率：</th>
								<td>${htlForm.htlRate }%</td>
							</tr>
							<tr>
								<th>起投金额：</th>
								<td>一元</td>
							</tr>
							<tr>
								<th>累计上限：</th>
								<td>单户累计投资上限为${htlForm.userPupper }元</td>
							</tr>
							<tr>
								<th>保证措施：</th>
								<td>合作机构（如基金公司）定向购买优质债权，银行、证券低风险理财产品等，并出让投资方的灵活期限投资计划，产品全额兑付</td>
							</tr>
							<tr>
								<th>首次计息：</th>
								<td>次日0时</td>
							</tr>
						</tbody>
					</table>
					<dl class="new-detail-dl rasom-dl">
						<dt>温馨提示</dt>
						<dd>
							<ol>
								<li>最低充值金额应大于等于 1 元；</li>
								<li>请投资人根据发标计划合理充值，因汇盈金服无法触及用户资金账户，无法收取用户任何费用，为防止套现，所充资金必须经投标回款后才能提现；</li>
							</ol>
						</dd>
					</dl>
				</li>

				<li panel="2">
					<dl class="new-detail-dl">
						<dt>1、汇天利怎么计算收益？</dt>
						<dd>汇天利每日每万元收益为1.11元。历史年回报率为${htlForm.htlRate }%，计算公式：（1.11x360）÷10000=4.0%。例如：投资客户在汇天利中计息的资金为9000元，代入计算公式，当日收益=9000元
							x 1.11÷10000=0.99元。</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>2、汇天利什么时候可以购买？</dt>
						<dd>随时购买，次日0点计息</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>3、汇天利的转出是否收费？</dt>
						<dd>免费转出，不限次数，转出金额再提现由支付机构收取一元提现费用</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>4、汇天利的转出金额有限制吗？</dt>
						<dd>在投资汇天利产品金额内的资金，可部分或全部转出</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>5、转出汇天利后资金什么时候能到账？</dt>
						<dd>到账时间与平台其他产品完全一致，转出一经发起，即刻到达投资方在支付机构的专属账户，如需提现，参考支付机构规则。</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>6、汇天利的加入金额有限制吗？</dt>
						<dd>汇天利的加入金额1元起，投资方在汇天利单项产品中的总金额不得超过${htlForm.userPupper }元。</dd>
					</dl>
					<dl class="new-detail-dl">
						<dt>7、汇天利收取服务费或其他费用吗？</dt>
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
	</div>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script type="text/javascript" src="${ctx}/js/jquery.validate.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/messages_cn.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.metadata.js" charset="utf-8"></script>
	<script>
		var timmer; //延时
		var validate = $("#detailForm").validate({
			"rules" : {
				"amount" : {
					"required" : true,
					"number" : true,
					"min" : 1,
					"max" : getMaxMoney()
				},
				"termcheck" : {
					required : true
				}
			},
			"messages" : {
				"amount" : {
					"required" : "亲，您还没有填写投资金额",
					"number" : "亲，只能填写数字",
					"min" : "提现金额应大于 1 元",
					"max" : "提现金额应小于 " + getMaxMoney() + " 元"
				},
				"termcheck" : "请先阅读并同意汇盈金服投资协议"
			},
			"ignore" : ".ignore",
			errorPlacement : function(error, element) {
				error.insertAfter(element.parent());
				// if(element.parents(".new-detail-inner.htl").length){
				//     error.insertAfter(element.parent());
				// }
				// else{
				//     error.insertAfter(element.parent().parent());
				// }
			},
			submitHandler : function(form) {

				//在这里做ajax验证  （可以传入form作为参数，成功执行下面的提交事件）
				//form.submit();
				htlRedeem();
			}
		});
		//选项卡
		$(".rasom-tab").click(function(e) {
			var _self = $(e.target);
			if (_self.is("li")) {
				var idx = _self.attr("panel");
				var panel = _self.parent().next(".new-detail-tab-panel");
				_self.siblings("li.active").removeClass("active");
				_self.addClass("active");
				panel.children("li.active").removeClass("active");
				panel.children("li[panel=" + idx + "]").addClass("active");
			}
		})
		//条款
		$(".terms-check").click(
				function(e) {
					var _self = $(e.target), checkbox;
					if (_self.hasClass("checkicon")) {
						checkbox = _self.siblings("input[type=checkbox]");
						_self = _self.parent();
					} else {
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

		//表单提交
		$("#confirmBtn").click(function() {
			$("#detailForm").submit();
		})
		//金额变动
		$("#amount").keyup(function() {
			if (timmer) {
				clearTimeout(timmer);
			}
			timmer = setTimeout(function() {
				priceChanged($("#amount"));
			}, 500);

		}).keydown(function() {
			$("#ajaxErr").remove();
		});

		//ajax请求获取历史回报
		function priceChanged(ipt) {
			var money = isNaN(parseInt($('#amount').val())) ? 0 : parseInt($('#amount').val());
			$.ajax({
				type : "POST",
				async : false,
				url : webPath + "/htl/getUserRedeemInterest.do",
				dataType : 'json',
				data : {
					"amount" : money
				},
				success : function(data) {
					if (data.error) {
						var ipt = $("#amount");
						ipt.parent().append("<span id='ajaxErr' class='error'>"+data.error+"</span>");
					} else {
						$("#income").text(data.RedeemInterest);
					}
				},
				error : function() {
					var ipt = $("#amount");
					ipt.parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
				}
			});
		}
		function htlRedeem() {
			var money = isNaN(parseInt($('#amount').val())) ? 0 : parseInt($('#amount').val());
			alert(money);
			$.ajax({
				type : "POST",
				//async : false,
				url :  "/hyjf-web/htl/check.do",
				dataType : 'json',
				data : {
					"flag":2,
					"amount" : money
				},
				success : function(data) {
					if (data.error == "1") {
						var ipt = $("#amount");
						ipt.parent().append("<span id='ajaxErr' class='error'>"+data.data+"</span>");
					} else if(data.url) {
						window.location.href=data.url;
					}
				},
				error : function() {
					var ipt = $("#amount");
					ipt.parent().append("<span id='ajaxErr' class='error'>网络异常，请检查您的网络</span>");
				}
			});
		}
		function getMaxMoney() {
			var balance = parseFloat($("#userData").data("balance"));//用户余额
			return parseInt(balance);
		}
	</script>

</body>

</html>