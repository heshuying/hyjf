<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>春季畅游世界</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/active/active-201612.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<div class="spring">
			<div class="bg1"></div>
			<div class="bg2"></div>
			<div class="bg3"></div>
			<div class="bg4"></div>
			<div class="bg5"></div>
			<div class="bg6"></div>
			<div class="bg7"></div>
			
			<!--显示内容开始-->
			<div class="bg8">
				<div class="content">
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
										<td>价值11288元的欧洲自然风光风情全球双人游。</td>
										<td>LV,NEVERFULL经典女包手袋M41177/阿玛尼经典男士西服</td>
									</tr>
									<tr>
										<td>白钻</td>
										<td>≥570万</td>
										<td>价值6128元的韩国济州岛4日3晚风情体验，双人游。</td>
										<td>LV新款女士时尚拉链单肩包（M95567）/BOSS男士经典皮鞋</td>
									</tr>
									<tr>
										<td>白金</td>
										<td>≥460万</td>
										<td>价值5128元的泰国普吉岛+皮皮岛双人游。</td>
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
										<td>价值688元的天津意大利风情街自由行，双人游游</td>
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
					</div>
				</div>
			</div>
			<!--显示内容结束-->
			
			<div class="bg9"></div>
			<div class="bg10">
				<a href="${ctx}/project/initProjectList.do?projectType=HZT"><img src="${ctx}/css/images/spring-button_12.png"/></a>
			</div>
			<div class="bg11"></div>
		</div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	
	
	</body>
</html>