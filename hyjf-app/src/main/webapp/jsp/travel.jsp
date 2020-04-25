<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>春季畅游世界</title>
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" type="text/css" href="${ctx}/css/travel.css"/>
	</head>
	<body>
		<div class="travel-active">
			<img src="${ctx}/img/travel01.jpg" alt="" />
			<img src="${ctx}/img/travel02.jpg" alt="" />
			<img src="${ctx}/img/travel03.jpg" alt="" />
			<img src="${ctx}/img/travel04.jpg" alt="" />
			<img src="${ctx}/img/travel05.jpg" alt="" />
			<img src="${ctx}/img/travel06.jpg" alt="" />
			<img src="${ctx}/img/travel07.jpg" alt="" />
			<img src="${ctx}/img/travel08.jpg" alt="" />
			<img src="${ctx}/img/travel09.jpg" alt="" />
			<div class="travel-active-annonce">
				<dl>
					<dt>活动送好礼，春季享不停</dt>
					<dd>所有达到奖励标准的客户，均可根据自己需要从旅游和奖品中进行选择，等价值香奈儿皮包、卡地亚戒指、BOSS经典男装、高端智能手机等活动好礼等你来拿！拿到手软！</dd>
				</dl>
				<dl>
					<dt>活动规则</dt>
					<dd>活动时间：2016.12.01 00:00:00—2017.01.31 23:59:59 <br />
					          奖励时间：2017.04.01（客户另有特殊需求的可以提前进行协商）<br />
					          参与人群：全网所有注册会员
					</dd>
				</dl>
				<dl>
					<dt>参与规则</dt>
					<dd>
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td class="travel-table-title" colspan=4>汇盈金服“2017春季畅游世界”方案</td>
							</tr>
							<tr>
								<td colspan=2>折合总量</td>
								<td colspan=2>奖励方案（同价值二选一）</td>
							</tr>
							<tr>
								<td colspan=2>总量（投资金额×对应期限系数）</td>
								<td>全球游（以实际出游为准）</td>
								<td>全球购</td>
							</tr>
							<tr>
								<td class="table-width">红钻</td>
								<td>≥1700万</td>
								<td>价值11288元的欧洲自然风光风情全球双人游</td>
								<td>LV,NEVERFULL经典女包手袋M41177/阿玛尼经典男士西服</td>
							</tr>
							<tr>
								<td>白钻</td>
								<td>≥570万</td>
								<td>价值6128元的韩国济州岛4日3晚风情体验，双人游</td>
								<td>LV新款女士时尚拉链单肩包（M95567）/BOSS男士经典皮鞋</td>
							</tr>
							<tr>
								<td>白金</td>
								<td>≥460万</td>
								<td>价值5128元的泰国普吉岛+皮皮岛双人游</td>
								<td>LV新款时尚女士印花单肩包（M40712）/阿玛尼精品男士钱包</td>
							</tr>
							<tr>
								<td>黄金</td>
								<td>≥430万</td>
								<td>价值1888元的上海“迪士尼”主题，老上海文化体验双人游</td>
								<td>华为G9青春版，全网通4G智能手机</td>
							</tr>
							<tr>
								<td>白银</td>
								<td>≥308万</td>
								<td>价值1288元的上海“迪士尼”主题双人游</td>
								<td>魅族新一代魅蓝E，32G全网通高端智能手机</td>
							</tr>
							<tr>
								<td>青铜</td>
								<td>≥276万</td>
								<td>价值888元的天津风情街，自由行，春季漫步双人游</td>
								<td>高端智能家居空气复原机</td>
							</tr>
							<tr>
								<td>玄铁</td>
								<td>≥246万</td>
								<td>价值688元的天津意大利风情街自由行，双人游</td>
								<td>高端生活家居用品奖励（空气清新剂、德国稀有金属厨具）</td>
							</tr>
						</table>
					</dd>
				</dl>
			    <dl>
					<dt>活动说明</dt>
					<dd>1.所有注册用户只能拥有一次获得奖励的机会。（以身份证号加以区分）</dd>
					<dd>2.活动期内，客户累计投资等于活动期内所有标的投资乘以投资系数累加所得进行评级。</dd>
					<dd>
						3.投资系数
						<table border="1" cellspacing="0" cellpadding="0">
							<tr>
								<td>标的期限</td>
								<td>一个月</td>
								<td>两个月</td>
								<td>三个月</td>
								<td>四个月</td>
								<td>五个月</td>
								<td>六个月</td>
							</tr>
							<tr>
								<td>投资系数</td>
								<td>1</td>
								<td>1.9</td>
								<td>2.3</td>
								<td>2.7</td>
								<td>3.1</td>
								<td>4.2</td>
							</tr>
						</table>
					</dd>
					<dd>例：XXX客户，在活动期内，一月标投资100万，二月标投资100万，三月标投资100万，四月标投资100万，六月标投资100万，那么客户累计投资
						=100×1+100×1.9+100×2.3+100×2.7+100×3.1+100×4.2=1520万</dd>
				</dl>
				<dl>
					<dd>详细活动规则请咨询投资顾问或官方客服 。</dd>
					<dd>客服电话：<span class="travel-phone"><a href="" class="travel-phone getContactNumber" style="text-decoration: none;  color: #541a08;"></a></span></dd>
				</dl>
			</div>
		    <div class="travel-go-invest">
		    	<a href="hyjf://jumpInvest/?">
		    		<img src="${ctx}/img/travel-invest-btn.png" alt="" />
		    	</a>
		    </div>
		    <div class="bottom-message">本活动最终解释权归汇盈金服所有</div>
		</div>
		<script src="${ctx}/js/zepto.min.js" type="text/javascript" charset="utf-8"></script>
		<script>
			$.ajax({
				type:"get",
				data:null,
				dataType:"json",
				url:"${ctx}/homepage/getServicePhoneNumber",
				success:function(data){
					var dataNumber = data.servicePhoneNumber;
					var num1 = dataNumber.substr(0,3);
					var num2 = dataNumber.substr(3,3);
					var num3 = dataNumber.substr(6);
					$(".getContactNumber").prop("href",'hyjf://callCenter/?{"number":"'+data.servicePhoneNumber+'"}')
					.text(num1+"-"+num2+"-"+num3)
				}
			})
		</script>
	</body>
</html>