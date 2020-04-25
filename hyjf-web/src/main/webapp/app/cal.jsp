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
	<section class="main-content">
			<div class="container">
				<!-- start 内容区域 -->
				<form id="hjkForm" class="cal">
					<article class="content content-2">
						<div class="main-title">
							收益计算器
						</div>
						<div class="new-form new-jisuanqi">
							<div class="new-form-item item-1 mr20">
								<label for="investMoney" class="jisuanqi-adjust-label">投资金额</label>
								<input type="text" name="investmoney" maxlength="9" class="new-form-input" id="investMoney" placeholder="请输入投资金额" autocomplete="off" oninput="if(value.length>9)value=value.slice(0,9)">
								<span class="unit">元</span>
							</div>
							<div class="new-form-item item-1">
								<label for="Tel">预期年化收益率</label>
								<input type="text" name="tel" maxlength="11" class="new-form-input" id="percent" placeholder="请输入年化收益率" autocomplete="off">
								<span class="unit">%</span>
							</div>
							<div class="new-form-item item-1 mr20">
								<label class="jisuanqi-adjust-label">还款方式</label>
								<h4 class="new-form-input  selectType">按月计息，到期还本付息<i class="iconfont icon-ananzuiconv265"></i></h4>
								<div class="new-form-hdselect select-1 select-jisuqni">
									<!-- for为需要存放值的隐藏input的id -->
									<ul for="Gender" id="ForGender">
										<!-- value为选项的值 -->
										<li value="0">按月计息，到期还本付息</li>
										<li value="1">按天计息，到期还本付息</li>
										<li value="2">先息后本</li>
										<li value="3">等额本息</li>
										<li value="4">等额本金</li>
									</ul>
								</div>
								<input type="hidden" name="gender" id="Gender" value="0">
							</div>
							<div class="new-form-item item-1">
								<label for="Money">项目期限</label>
								<input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" name="money" maxlength="3" class="new-form-input" id="month" placeholder="请输入项目期限" oninput="if(value.length>3)value=value.slice(0,3)" autocomplete="off">
								<span class="unit month-day">月</span>
							</div>
							<div class="clearfix"></div>
							<a class="new-form-btn" id="formSubmit">开始计算</a>
						</div>
					</article>
					<article class="content-outcome content-2 content hide">
						<div class="main-title">
							收益结果
						</div>
							<div class="jisuanqi-outcome-title">
								<div>
									<p class="outcomeTotal">0.00</p>
									<span>本息总计(元)</span>
								</div>
								<div class="line"></div>
								<div>
									<p class="interst">0.00</p>
									<span>利息总计(元)</span>
									
								</div>
								<i class="clearfix"></i>
							</div>
					</article>
					<!--<article class="content content-2">
						<div class="main-title">
							还款明细
						</div>
						<div class="jisuanqi-table hide">
							<table border="0" cellspacing="0" cellpadding="0">
								<tbody>
									<tr class="invite-table-header">
										<td>还款期次</td>
										<td>本息(元)</td>
										<td>本金(元)</td>
										<td>利息(元)</td>
									</tr>
								</tbody>
							</table>
						</div>
						<p class="calc-txt">计算收益仅供参考，最终收益以实际到账为准</p>
					</article>-->
				</form>
				<!-- end 内容区域 -->
			</div>
			
		</section>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
	<script src="../dist/js/lib/jquery.metadata.js"></script>
	<script src="../dist/js/lib/messages_cn.js"></script>
	<script src="../dist/js/cal/customform.js"></script>
	<script src="../dist/js/cal/cal.js"></script>
</body>
</html>