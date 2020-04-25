<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>2017年度报告 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
<link rel="stylesheet" type="text/css"
	href="${ctx}/css/jquery.fullPage.css" />
<%-- <link rel="stylesheet" type="text/css" href="${ctx}/css/report-tmp.css?version=${version}" /> --%>
<link rel="stylesheet" type="text/css"
	href="${ctx}/css/newweb-report.css" />
	<style>
	.right-nav li.section9 {
	    background-position: 0 -504px;
	    padding-left:0;
    }
    .right-nav li.section9:hover, .right-nav li.section9.active {
        background-position: right -504px;
    }
    .dec .right-nav li.section11 {
         background-position: 0 -441px;
         padding-left:27px;
     }
     .dec .right-nav li.section11:hover, .dec .right-nav li.section11.active {
        background-position: right -441px;
    }
    .dec .tiyanyouhua .inner-bg {
    width: 1229px;
    height: 520px;
    background-image: url(../img/report/201712/sinner.png);
}
</style>
</head>

<body class="dec">
	<div id="fullpage">
		<div class="section top" style="background-image: url(${ctx}/img/report/201712/top.jpg);">
			<div class="scroll"></div>
		</div>
		<div class="section section1 yejizonglan repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>业绩总览
				</div>
				<dl class="data1">
					<dt>累计交易额（元）</dt>
					<dd id="s1data1">19,137,442,346</dd>
				</dl>
				<dl class="data2">
					<dt>平台注册人数（人）</dt>
					<dd id="s1data2">367,893</dd>
				</dl>
				<dl class="data3">
					<dt>累计赚取收益（元）</dt>
					<dd id="s1data3">649,088,728</dd>
				</dl>
			</div>
		</div>

		<div class="section section2 repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>全年业绩
				</div>
				<div class="echarts-box">
					<div id="echarts2_1"
						style="width: 1050px; margin-left: auto; margin-right: auto; float: none;"></div>
				</div>
				<div class="data-box">
					<p style="font-size: 18px;">
						2017年全年平台累计成交<span id="s2data1" class="data">294,134</span>笔，累计成交金额<span
							class="data"><span id="s2data2">88.896</span>亿</span>元，累计赚取收益<span
							class="data"><span id="s2data3" class="data">23,116.63</span>万</span>元
					</p>
					<p style="font-size: 18px; text-align: center;">
						成交量最高单月为<span class="data">9</span>月，共成交<span class="data"><span
							id="s2data8">9.96</span>亿</span>元
					</p>
					<p style="font-size: 18px; text-align: center;">
						全年预期平均收益率<span class="data"><span id="s2data9">8.24</span></span>%
					</p>
				</div>
			</div>
		</div>
		
		<div class="section section3 repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>借款期限
				</div>
				<div class="echarts-box">
					<div id="borrow3_1" style="height: 360px"></div>
				</div>
			</div>
		</div>
		
		<div class="section section4 qudaofenxi repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>渠道分析
				</div>
				<div class="qudaofenxi_style2_left">
					<div class="item-title">
						<img src="${ctx}/img/report/txt_app.png" alt="">
					</div>
					<div class="item">
						<div class="icon phone"></div>
						年度成交<span id="s3data1">155,186</span>笔，占比<span id="s3data2">52.76</span>%
					</div>
					<div class="item-title">
						<img src="${ctx}/img/report/txt_weixin.png" alt="">
					</div>
					<div class="item">
						<div class="icon wechat"></div>
						年度成交<span id="s3data3">11,573</span>笔，占比<span id="s3data4">3.93</span>%
					</div>
					<div class="item-title">
						<img src="${ctx}/img/report/txt_pc.png" alt="">
					</div>
					<div class="item">
						<div class="icon desktop"></div>
						年度成交<span id="s3data5">127,375</span>笔，占比<span id="s3data6">43.31</span>%
					</div>
				</div>
				<div id="echarts3_1"
					style="width: 500px; height: 520px; float: left; margin-top: 50px;"></div>
			</div>
		</div>
		
		<div class="section section4 qudaofenxi repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>渠道分析
				</div>
				<div class="qudaofenxi_style2_left">
					<div class="item-title">
						<img src="${ctx}/img/report/txt_app.png" alt="">
					</div>
					<div class="item">
						<div class="icon phone"></div>
						年度累计成交金额<span id="s4data1">38.69</span>亿元，占比<span id="s4data2">43.78</span>%
					</div>
					<div class="item-title">
						<img src="${ctx}/img/report/txt_weixin.png" alt="">
					</div>
					<div class="item">
						<div class="icon wechat"></div>
						年度累计成交金额<span id="s4data3">2.48</span>亿元，占比<span id="s4data4">2.80</span>%
					</div>
					<div class="item-title">
						<img src="${ctx}/img/report/txt_pc.png" alt="">
					</div>
					<div class="item">
						<div class="icon desktop"></div>
						年度累计成交金额<span id="s4data5">47.21</span>亿元，占比<span id="s4data6">53.42</span>%
					</div>
				</div>
				<div id="echarts4_1"
					style="width: 500px; height: 520px; float: left; margin-top: 50px;"></div>
			</div>
		</div>
		
		<div class="section section6 yonghufenxi repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>用户分析
				</div>
				<div class="echarts-box">
					<div id="yonghufenxi_1" style="width: 500px; height: 400px;"></div>
					<div id="yonghufenxi_3"
						style="width: 500px; height: 400px; margin-left: 0px;"></div>
				</div>
			</div>
		</div>
		
		<div class="section section7 dangyuezhizui repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>年度之最
				</div>
				<div class="dangyuezhizui-left-style2">
					<div class="item-title">本年度十大投资人</div>
					<div class="item-box">
						<div class="item">
							<div class="name">l*</div>
							<div class="value top3">4,258.18万</div>
						</div>
						<div class="item">
							<div class="name">l*</div>
							<div class="value top3">4,247.00万</div>
						</div>
						<div class="item">
							<div class="name">李*</div>
							<div class="value top3">3,609.92万</div>
						</div>
						<div class="item">
							<div class="name">达*</div>
							<div class="value">3,234.30万</div>
						</div>
						<div class="item">
							<div class="name">H*</div>
							<div class="value">3,011.48万</div>
						</div>
						<div class="item">
							<div class="name">H*</div>
							<div class="value">2,216.60万</div>
						</div>
						<div class="item">
							<div class="name">y*</div>
							<div class="value">2,104.30万</div>
						</div>
						<div class="item">
							<div class="name">满*</div>
							<div class="value">1,970.25万</div>
						</div>
						<div class="item">
							<div class="name">李*</div>
							<div class="value">1,831.68万</div>
						</div>
						<div class="item">
							<div class="name">h*</div>
							<div class="value">1,793.04万</div>
						</div>
					</div>
				</div>
				<div class="dangyuezhizui-right-style2">
					<div class="item">
						<div class="item-title">
							<img src="${ctx}/img/report/zuiduojin.png" alt="">
						</div>
						<p class="item-title-txt first">本年度投资金额最高</p>
						<p class="item-value">
							<span class="val1"><span id="s6data1">4,258.18</span> 万元</span> <span
								class="val2">l*</span> <span class="val3">81岁</span> <span
								class="val4">青岛市</span>
						</p>
					</div>
					<div class="item">
						<div class="item-title">
							<img src="${ctx}/img/report/dayingjia.png" alt="">
						</div>
						<p class="item-title-txt second">本年度历史回报最高</p>
						<p class="item-value">
							<span class="val1"><span id="s6data2">129.02</span> 万元</span> <span
								class="val2">达*</span> <span class="val3">70岁</span> <span
								class="val4">烟台市</span>
						</p>
					</div>
					<div class="item">
						<div class="item-title">
							<img src="${ctx}/img/report/chahuoyue.png" alt="">
						</div>
						<p class="item-title-txt third">本年度投资次数最多</p>
						<p class="item-value">
							<span class="val1"><span id="s6data3">2639</span>次</span> <span
								class="val2">x*</span> <span class="val3">33岁</span> <span
								class="val4">许昌市</span>
						</p>
					</div>
					<div id="dangyuezhizui_1" style="width: 500px; height: 320px;"></div>
				</div>

			</div>
		</div>
		
		<div class="section section8 kehufuwu repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>客户服务
				</div>

				<div class="inner-bg">
					<div class="item1">
						<span id="s8data1">56940</span> <span class="field">个</span>
					</div>
					<div class="item2">
						<span id="s8data2">70421</span> <span class="field">人</span>
					</div>
					<div class="item3">
						<span id="s8data3">11890</span> <span class="field">人</span>
					</div>
					<div class="item4">
						<span id="s8data4">124120</span> <span class="field">个</span>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section section9 tiyanyouhua repeat">
			<div class="inner" style="width:1229px">
				<div class="title">
					<span class="line"></span>体验优化
				</div>
				<div class="inner-bg"></div>
			</div>
		</div>
		
		<div class="section section10 jingcaihuodong repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>精彩活动
				</div>

				<div class="active-bg-2 inner-bg"
					style="width: 1150px; margin-left: auto; margin-right: auto;">
					<div class="item item1">
						<div class="image">
							<img src="${ctx}/img/report/201701/201701_active1.jpg" alt="">
						</div>
						<div class="con">
							汇盈金服3周年盛典<br />百亿时刻，邀您共同见证！
						</div>
						<div class="date">平台成交额达100亿起至105亿止</div>
						<div class="join"></div>
					</div>
					<div class="item item2">
						<div class="image">
							<img src="${ctx}/img/report/201701/201701_active2.jpg" alt="">
						</div>
						<div class="con">
							金鸡报晓<br /> 贺岁纳福
						</div>
						<div class="date">2017年1月20日至2017年2月11日</div>
						<div class="join"></div>
					</div>
					<div class="item item3">
						<div class="image">
							<img src="${ctx}/img/report/201703/201703_active3.jpg" alt="">
						</div>
						<div class="con">
							新手福利 <br />注册即领188元新手红包
						</div>
						<div class="date">2017年4月6日起至2017年4月18日</div>
						<div class="join"></div>
					</div>
					<div class="item item4" style="margin-top: 0;">
						<div class="image">
							<img src="${ctx}/img/report/201704/201704_active2.jpg" alt="">
						</div>
						<div class="con">
							新手限时专享<br /> 888元现金红包
						</div>
						<div class="date">2017年4月18日起</div>
						<div class="join"></div>
					</div>
					<div class="item item5" style="margin-top: 0">
						<div class="image">
							<img src="${ctx}/img/report/201706/201706_active1.png" alt="">
						</div>
						<div class="con">
							理财<br>就“邀”一起玩！
						</div>
						<div class="date">2017年6月1日至2017年6月30日</div>
						<div class="join"></div>
					</div>
				</div>
			</div>
		</div>
		
		<div class="section section10 jingcaihuodong repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>精彩活动
				</div>
				<div class="active-bg-2 inner-bg"
					style="width: 1150px; margin-left: auto; margin-right: auto;">
					<div class="item item1">
						<div class="image">
							<img src="${ctx}/img/report/201707/201707_active2.png" alt="">
						</div>
						<div class="con">礼财，就“邀”一起玩</div>
						<div class="date">2017年7月24日至2017年7月30日</div>
						<div class="join"></div>
					</div>
					<div class="item item2">
						<div class="image">
							<img src="${ctx}/img/report/201710/201710_active2.jpg?version=1"
								alt="">
						</div>
						<div class="con">
							嗨翻国庆 <br />壕礼三重
						</div>
						<div class="date">2017年10月1日至2017年10月31日</div>
						<div class="join"></div>
					</div>
					<div class="item item3">
						<div class="image">
							<img src="${ctx}/img/report/201711/201711_active2.jpg" alt="">
						</div>
						<div class="con">
							抢疯了！！！<br />iPhone X + 11%加息券
						</div>
						<div class="date">2017年11月11日至2017年11月30日</div>
						<div class="join"></div>
					</div>
					<div class="item item4" style="margin-top: 0;">
						<div class="image">
							<img src="${ctx}/img/report/201712/active-1.png" alt="">
						</div>
						<div class="con">
							当双十二遇上周年庆<br />苹果多多
						</div>
						<div class="date">2017年12月12日至2017年12月31日</div>
						<div class="join"></div>
					</div>
					<div class="item item5" style="margin-top: 0">
						<div class="image">
							<img src="${ctx}/img/report/201712/active-2.png" alt="">
						</div>
						<div class="con">
							汇盈金服庆生party<br />最高6000元红包等你拿！
						</div>
						<div class="date">2017年12月12日至2017年12月31日</div>
						<div class="join"></div>
					</div>
				</div>
			</div>
		</div>

		<div class="section section12 zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>
				<div class="inner-bg">
					<div class="item item1">
						<div class="color1 date">2017年1月6日</div>
						<div class="con">汇聚梦想  赢创未来”——汇盈金服年终总结大会隆重召开</div>
					</div>
					<div class="item item2">
						<div class="color2 date">2017年1月7日</div>
						<div class="con">“宁为凤首  春风圣意”——汇盈金服年度庆典盛大绽放</div>
					</div>
					<div class="item item3" style="margin-left: 355px !important">
						<div class="color3 date">2017年1月11日</div>
						<div class="con">汇盈金服应邀出席2017消费金融创新发展论坛  开启财富智慧双碰撞</div>
					</div>
					<div class="item item4">
						<div class="color4 date">2017年2月16日</div>
						<div class="con">汇盈金服与上海衷和投资管理有限公司达成战略合作  资源渠道又拓宽</div>
					</div>
					<div class="item item5">
						<div class="color5 date">2017年2月17日</div>
						<div class="con">投之家莅临汇盈金服洽谈深度合作  初定战略合作框架</div>
					</div>
					<div class="item item6">
						<div class="color6 date">2017年3月1日</div>
						<div class="con">汇盈金服与投之家正式达成战略合作</div>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section section12 zuji2 zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>

				<div class="inner-bg">
					<div class="item item1">
						<div class="color7 date">2017年3月2日</div>
						<div class="con"> 汇盈金服受邀出席中国网贷百强CEO论坛</div>
					</div>
					<div class="item item2">
						<div class="color8 date">2017年3月22日</div>
						<div class="con">汇盈金服与江西银行正式签订存管协议</div>
					</div>
					<div class="item item3" style="margin-left: 355px !important">
						<div class="color9 date">2017年5月6日</div>
						<div class="con">大浪淘沙，沉者为金——之家哥俱乐部济南站顺利举办</div>
					</div>
					<div class="item item4">
						<div class="color10 date">2017年5月24日</div>
						<div class="con">汇盈金服应邀出席中国网贷百强CEO论坛 一场激烈的思想盛宴</div>
					</div>
					<div class="item item5">
						<div class="color11 date">2017年6月18日</div>
						<div class="con">网贷整改，不忘初心——之家哥俱乐部青岛站活动顺利举办</div>
					</div>
					<div class="item item6">
						<div class="color12 date">2017年7月6日</div>
						<div class="con">汇盈金服正式宣布: 平台已经完成银行存管！</div>
					</div>
				</div>

			</div>
		</div>

		<div class="section section12 zuji zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>

				<div class="inner-bg">
					<div class="item item1">
						<div class="color7 date">2017年7月17日</div>
						<div class="con">华尔街投行Axiom Capital及Cuttone & Co.莅临汇盈金服考察</div>
					</div>
					<div class="item item2">
						<div class="color8 date">2017年8月23日</div>
						<div class="con">企业文化齐发展，全“心”面貌树标杆！</div>
					</div>
					<div class="item item3" style="margin-left: 355px !important">
						<div class="color9 date">2017年8月28日</div>
						<div class="con">汇盈金服携手利全科技发力普惠金融，正式签署战略合作协议</div>
					</div>
					<div class="item item4">
						<div class="color10 date">2017年9月12日</div>
						<div class="con">“千帆过尽，初心不泯”——汇盈金服高端客户赴上海总部参观交流！</div>
					</div>
					<div class="item item5">
						<div class="color11 date">2017年9月20日</div>
						<div class="con">怀揣克己敬畏之心，深耕不辍——高端客户座谈会隆重召开</div>
					</div>
					<div class="item item6">
						<div class="color12 date">2017年10月11日</div>
						<div class="con">大汇盈金服携手快牛金科达成战略合作 用科技践行普惠！</div>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section section12 zuji2 zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>

				<div class="inner-bg">
					<div class="item item1">
						<div class="color7 date">2017年10月12日</div>
						<div class="con">汇盈金服与给米金服达成战略合作 以科技的力量带动金融创新！</div>
					</div>
					<div class="item item2">
						<div class="color8 date">2017年10月12日</div>
						<div class="con">探索企业发展的更多可能性—汇盈金服高端客户座谈会隆重召开</div>
					</div>
					<div class="item item3" style="margin-left: 355px !important">
						<div class="color9 date">2017年10月14日</div>
						<div class="con"> 江西大唐汇金员工大会顺利召开！</div>
					</div>
					<div class="item item4">
						<div class="color10 date">2017年10月18日</div>
						<div class="con">中国梦实践者 | 汇盈金服常州高端客户交流座谈会成功举行！</div>
					</div>
					<div class="item item5">
						<div class="color11 date">2017年10月19日</div>
						<div class="con"> 座谈会架起沟通之桥，让客户心声凝聚成汇盈金服发展之基！</div>
					</div>
					<div class="item item6">
						<div class="color12 date">2017年10月24日</div>
						<div class="con">汇盈金服受邀出席厦门国际银行北京分行举办的科技金融座谈会</div>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section section12 zuji zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>

				<div class="inner-bg">
					<div class="item item1">
						<div class="color7 date">2017年10月24日</div>
						<div class="con">董事长刘伯党：互金时代需遵守契约精神，合规与创新并行</div>
					</div>
					<div class="item item2">
						<div class="color8 date">2017年10月25日</div>
						<div class="con">汇盈金服应邀参加金融科技·数据驱动金融商业裂变价值峰会</div>
					</div>
					<div class="item item3" style="margin-left: 355px !important">
						<div class="color9 date">2017年10月26日</div>
						<div class="con">新时代，新征程——汇盈金服烟台高端客户座谈会成功举行！</div>
					</div>
					<div class="item item4">
						<div class="color10 date">2017年11月2日</div>
						<div class="con">“引进来走出去”——潍坊高端客户交流座谈会举行！</div>
					</div>
					<div class="item item5">
						<div class="color11 date">2017年11月8日</div>
						<div class="con">2018新思路新高度——对话苏珊娜·奇斯蒂及全球金融科技论道</div>
					</div>
					<div class="item item6">
						<div class="color12 date">2017年11月27日</div>
						<div class="con">汇盈金服荣膺“互联网金融合规发展30强”</div>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section section12 zuji3 zuji repeat">
			<div class="inner">
				<div class="title">
					<span class="line"></span>足迹
				</div>

				<div class="inner-bg">
					<div class="item item1">
						<div class="color13 date">2017年12月5日</div>
						<div class="con">汇盈金服荣获“2017互联网金融年度影响力企业奖”</div>
					</div>
					<div class="item item2">
						<div class="color14 date">2017年12月6日</div>
						<div class="con">汇盈金服受邀出席21世纪新金融发展峰会暨金石奖颁奖典礼</div>
					</div>
					<div class="item item3">
						<div class="color15 date">2017年12月9日</div>
						<div class="con">之家哥俱乐部重庆站顺利举办！</div>
					</div>
					<div class="item item4">
						<div class="color16 date">2017年12月14日-15日</div>
						<div class="con">汇盈金服受邀出席第十四届中国国际金融论坛</div>
					</div>
					<div class="item item5">
						<div class="color17 date">2017年12月15日</div>
						<div class="con">汇盈金服荣获“金鼎奖“年度卓越风控能力互联网金融平台奖</div>
					</div>
				</div>

			</div>
		</div>
		
		<div class="section over">
			<div class="inner"></div>
		</div>

	</div>

	<div class="right-nav">
		<ul>
			<li class="top active" data-idx="0" authors="top"><span>top</span></li>
			<li class="section1" data-idx="1" authors="section1"><span>业绩<br />总览</span></li>
			<li class="section2" data-idx="2" authors="section2"><span>全年<br />业绩</span></li>
			<li class="section3" data-idx="3" authors="section3"><span>借款<br />期限</span></li>
			<li class="section4 section5" data-idx="4" authors="section4"><span>渠道<br />分析</span></li>
			<li class="section6" data-idx="6" authors="section6"><span>用户<br />分析</span></li>
			<li class="section7" data-idx="7" authors="section7"><span>年度<br />之最</span></li>
			<li class="section8" data-idx="8" authors="section8"><span>客户<br />服务</span></li>
			<li class="section9" data-idx="9" authors="section9"><span>体验<br />优化</span></li>
			<li class="section10 section11" data-idx="10" authors="section10"><span>精彩<br />活动</span></li>
			<li class="section12 section13 section14 section15 section16 section17" data-idx="12" authors="section12"><span>足迹</span></li>
		</ul>
	</div>
	<script src="${ctx}/js/jquery.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/jquery.fullPage.js"></script>
	<script type="text/javascript" src="${ctx}/js/echarts.common.min.js"></script>
	<script type="text/javascript" src="${ctx}/js/countUp.min.js"></script>
	<script>
		var Sys = {};
		var ua = navigator.userAgent.toLowerCase();
		var s;
		(s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] : (s = ua
				.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] : (s = ua
				.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] : (s = ua
				.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] : (s = ua
				.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;

		if (Sys.ie || Sys.firefox) {
			$("dd.copy").hide();
		}
		if (document.documentElement.clientHeight <= 900) {
			$('.section .inner').css("zoom", .8);
		}

		$(".right-nav").find("li").children("span");
	</script>

	<script>
		//echarts默认设置
		var mainColor = [ '#5ffae8', '#2da9dc', '#6675c8', '#d53535',
				'#ed4544', '#dd8c33', '#e0b370', '#b9a832', '#c73171',
				'#8832c9', '#33c888' ];
		var secondColor = [ '#ed4544', '#6474cb', '#5efbe7', '#e0b36f',
				'#ed4544', '#e0b36f', '#daae6d', '#45b067', '#066f28',
				'#06732e' ];

		var echarts2_1_color = [ '#5efbe7', '#2eace0', '#6474cb', '#da3534',
				'#ed4544', '#dd8b32', '#e0b36f', '#48bd6c', '#2baa3f',
				'#da3534', '#90c231', '#ffef00' ];
		var mainTextStyle = {
			color : "#fff",
			fontWeight : "normal"
		}
	</script>
	<script type="text/javascript">
		/* section1 当月业绩
		-----------------------------------------------------------------------------------------*/
		//交易金额 单位 （元）
		var data_2_1 = {
			title : "交易金额 单位（亿元）",
			data : [ {
				name : "2017年1月",
				value : 5.61,
				itemStyle : {
					normal : {
						color : echarts2_1_color[0]
					},
					emphasis : {
						color : echarts2_1_color[0]
					}
				}
			}, {
				name : "2017年2月",
				value : 4.66,
				itemStyle : {
					normal : {
						color : echarts2_1_color[1]
					},
					emphasis : {
						color : echarts2_1_color[1]
					}
				}
			}, {
				name : "2017年3月",
				value : 6.31,
				itemStyle : {
					normal : {
						color : echarts2_1_color[2]
					},
					emphasis : {
						color : echarts2_1_color[2]
					}
				}
			}, {
				name : "2017年4月",
				value : 6.30,
				itemStyle : {
					normal : {
						color : echarts2_1_color[3]
					},
					emphasis : {
						color : echarts2_1_color[3]
					}
				}
			}, {
				name : "2017年5月",
				value : 6.74,
				itemStyle : {
					normal : {
						color : echarts2_1_color[4]
					},
					emphasis : {
						color : echarts2_1_color[4]
					}
				}
			}, {
				name : "2017年6月",
				value : 7.88,
				itemStyle : {
					normal : {
						color : echarts2_1_color[5]
					},
					emphasis : {
						color : echarts2_1_color[5]
					}
				}
			}, {

				name : "2017年7月",
				value : 7.72,
				itemStyle : {
					normal : {
						color : echarts2_1_color[6]
					},
					emphasis : {
						color : echarts2_1_color[6]
					}
				}
			}, {

				name : "2017年8月",
				value : 8.43,
				itemStyle : {
					normal : {
						color : echarts2_1_color[7]
					},
					emphasis : {
						color : echarts2_1_color[7]
					}
				}
			}, {

				name : "2017年9月",
				value : 9.96,
				itemStyle : {
					normal : {
						color : echarts2_1_color[8]
					},
					emphasis : {
						color : echarts2_1_color[8]
					}
				}
			}, {
				name : "2017年10月",
				value : 6.63,
				itemStyle : {
					normal : {
						color : echarts2_1_color[9]
					},
					emphasis : {
						color : echarts2_1_color[9]
					}
				}
			}, {

				name : "2017年11月",
				value : 9.13,
				itemStyle : {
					normal : {
						color : echarts2_1_color[10]
					},
					emphasis : {
						color : echarts2_1_color[10]
					}
				}
			}, {

				name : "2017年12月",
				value : 9.52,
				itemStyle : {
					normal : {
						color : echarts2_1_color[11]
					},
					emphasis : {
						color : echarts2_1_color[11]
					}
				}
			} ],
			field : [ '2017年1月', '2017年2月', '2017年3月', '2017年4月', '2017年5月',
					'2017年6月', '2017年7月', '2017年8月', '2017年9月', '2017年10月',
					'2017年11月', '2017年12月' ]
		};
		/* var data_2_2 = {
		    title:"赚取收益 单位 （万元）",
		    data:[{
		            name:"2017年11月",
		            value:1434.24,
		            itemStyle:{
		                normal:{
		                    color:"#ed4544"
		                },
		                emphasis:{
		                    color:"#ed4544"
		                }
		            }
		        },{
		            name:"2015年11月",
		            value:1175.52,
		            itemStyle:{
		                normal:{
		                    color:"#00b8ec"
		                },
		                emphasis:{
		                    color:"#00b8ec"
		                }
		            }
		        }
		    ],
		    field : ['2017年11月','2015年11月']
		} */

		var echarts2_1_option = {
			color : echarts2_1_color,
			title : {
				text : data_2_1.title,
				textStyle : mainTextStyle,
				top : 300,
				left : "center"
			},
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				},
				formatter : "{b}<br/> {c}亿元"
					
			},
			grid : {
				left : '-15',
				right : '0',
				bottom : '60',
				top : '35',
				containLabel : true
			},
			legend : {
				data : data_2_1.field
			},
			xAxis : [ {
				type : 'category',
				axisLabel : {
					textStyle : mainTextStyle
				},
				data : data_2_1.field,
				axisLine : {
					lineStyle : {
						color : "#6474cb",
						width : 1
					}
				},
				splitLine : {
					show : false
				}
			} ],
			yAxis : {
				show : false
			},
			series : [ {
				name : data_2_1.title,
				type : 'bar',
				label : {
					normal : {
						show : true,
						formatter : "{c}",
						position : "top",
						textStyle : {
							fontSize : 16
						}
					}
				},
				barWidth : 40,
				data : data_2_1.data
			}, {
				name : data_2_1.title,
				type : 'line',
				data : data_2_1.data
			} ]
		};

		var echarts2_1 = echarts.init(document.getElementById('echarts2_1'));//
	</script>
	<script type="text/javascript">
		/* section3 标的分析数据
		-----------------------------------------------------------------------------------------*/
		//借款期限
		var data_3_1 = {
			title : "借款期限",
			data : [ //借款期限
            {
				value : 5421,
				name : '30天以下'
			},
			{
				value : 1787,
				name : '30天'
			}, {
				value : 1494,
				name : '1个月'
			}, {
				value : 1824,
				name : '2个月'
			}, {
				value : 3488,
				name : '3个月'
			}, {
				value : 483,
				name : '4个月'
			}, {
				value : 11,
				name : '5个月'
			}, {
				value : 3785,
				name : '6个月'
			}, {
				value : 7,
				name : '9个月'
			}, {
				value : 2,
				name : '10个月'
			}, {
				value : 638,
				name : '12个月'
			}, {
				value : 9,
				name : '15个月'
			}, {
				value : 15,
				name : '18个月'
			}, {
				value : 244,
				name : '24个月'
			} ],
			field : [ '30天以下','30天', '1个月', '2个月', '3个月', '4个月', '5个月', '6个月', '9个月' , '10个月' , '12个月' , '15个月' , '18个月' , '24个月']
		}
		var borrow3_1 = echarts.init(document.getElementById('borrow3_1'));//借款期限
		// 指定图表的配置项和数据
		var borrow3_1_option = {
			color : mainColor,
			title : {
				text : data_3_1.title,
				textStyle : mainTextStyle,
				left : 275,
				top : 167
			},
			tooltip : {
				trigger : 'item',
				//formatter : "{a} <br/>{b}: {c} ({d}%)"
					formatter : function(obj) {
						if (obj.data.value == "3785") {
							obj.percent = "19.70";
						}
						return obj.seriesName + "<br/>" + obj.data.name + " "
						+ obj.data.value + "(" + obj.percent + "%)";

					}
			},
			legend : {
				orient : 'vertical',
				x : '73%',
				y : 'middle',
				data : data_3_1.field,
				textStyle : {
					color : "#fff"
				}
			},
			series : [ {
				name : data_3_1.title,
				type : 'pie',
				label : {
					normal : {
						//formatter : "{b}\n{d}%"
							formatter : function(obj) {
								if (obj.data.value == "3785") {
									obj.percent = "19.70";
								}
								return  obj.data.name + "\n" + obj.percent + "%";

							}
					}
				},
				radius : [ '45%', '60%' ],
				data : data_3_1.data,
				center : [ '30%', '50%' ]
			} ]
		};
	</script>
	<script>
		var echarts3_1_option = {
			title : {
				text : 'APP、微信、PC成交笔数对比图',
				left : 'center',
				top : 20,
				textStyle : {
					color : '#5df8e4',
					fontSize : 30,
					fontWeight : "normal"
				}
			},

			tooltip : {
				trigger : 'item',
				formatter : "{b} : {c} ({d}%)"
			},

			visualMap : {
				show : false,
				min : 80,
				max : 600,
				inRange : {
					colorLightness : [ 0, 1 ]
				}
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '50%' ],
				data : [ {
					value : 127375,
					name : 'PC',
					itemStyle : {
						normal : {
							color : "#607ace"
						},
						emphasis : {
							color : "#607ace"
						}
					}

				}, {
					value : 155186,
					name : 'APP',
					itemStyle : {
						normal : {
							color : "#31aad4"
						},
						emphasis : {
							color : "#31aad4"
						}
					}

				}, {
					value : 11573,
					name : '微信',
					itemStyle : {
						normal : {
							color : "#90d7cc"
						},
						emphasis : {
							color : "#90d7cc"
						}
					}

				} ].sort(function(a, b) {
					return a.value - b.value
				}),
				// roseType: 'angle',
				label : {
					normal : {
						textStyle : {
							color : '#fff',
							fontSize : 18
						},
						formatter : "{b}\n{d}%",
					}
				},
				labelLine : {
					normal : {
						lineStyle : {
							color : '#fff'
						},
						smooth : 0.2,
						length : 8,
						length2 : 10
					}
				},
				itemStyle : {
					normal : {
						color : '#c23531',
						shadowBlur : 50,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};
		var echarts3_1 = echarts.init(document.getElementById('echarts3_1'));
	</script>
	<script>
		var echarts4_1_option = {
			title : {
				text : 'APP、微信、PC累计成交金额对比图',
				left : 'center',
				top : 20,
				textStyle : {
					color : '#5df8e4',
					fontSize : 30,
					fontWeight : "normal"
				}
			},

			tooltip : {
				trigger : 'item',
				//formatter : "{b} : {c} ({d}%)"
					formatter : function(obj) {
						if (obj.data.value == "247873501") { //hack 数字bug 应该显示34.09 插件显示 34.1
							obj.percent = "2.80";
						}
						return  obj.data.name + ":"+ obj.data.value + "(" + obj.percent + "%)";

					}
			},

			visualMap : {
				show : false,
				min : 80,
				max : 600,
				inRange : {
					colorLightness : [ 0, 1 ]
				}
			},
			series : [ {
				name : '访问来源',
				type : 'pie',
				radius : '55%',
				center : [ '50%', '50%' ],
				data : [ {
					value : 4720646366,
					name : 'PC',
					itemStyle : {
						normal : {
							color : "#607ace"
						},
						emphasis : {
							color : "#607ace"
						}
					}

				}, {
					value : 3869095432,
					name : 'APP',
					itemStyle : {
						normal : {
							color : "#31aad4"
						},
						emphasis : {
							color : "#31aad4"
						}
					}

				}, {
					value : 247873501,
					name : '微信',
					itemStyle : {
						normal : {
							color : "#90d7cc"
						},
						emphasis : {
							color : "#90d7cc"
						}
					}

				} ].sort(function(a, b) {
					return a.value - b.value
				}),
				// roseType: 'angle',
				label : {
					normal : {
						textStyle : {
							color : '#fff',
							fontSize : 18
						},
						//formatter : "{b}\n{d}%",
						formatter : function(obj) {
							if (obj.data.value == "247873501") {
								obj.percent = "2.80";
							}
							return  obj.data.name + "\n" + obj.percent + "%";

						}
					}
				},
				labelLine : {
					normal : {
						lineStyle : {
							color : '#fff'
						},
						smooth : 0.2,
						length : 8,
						length2 : 10
					}
				},
				itemStyle : {
					normal : {
						color : '#c23531',
						shadowBlur : 50,
						shadowColor : 'rgba(0, 0, 0, 0.5)'
					}
				}
			} ]
		};
		var echarts4_1 = echarts.init(document.getElementById('echarts4_1'));
	</script>
	<script type="text/javascript">
		/* section6 用户分析数据
		-----------------------------------------------------------------------------------------*/

		//年龄、性别分布
		var data_6_1 = {
			title : "年龄、性别分布",
			data : {
				sex : [ {
					value : 38656,
					name : '女'
				}, {
					value : 30371,
					name : '男'
				} ],
				age : [ {
					value : 12760,
					name : '18-29岁'
				}, {
					value : 15306,
					name : '30-39岁'
				}, {
					value : 15106,
					name : '40-49岁'
				}, {
					value : 11834,
					name : '50-59岁'
				}, {
					value : 14021,
					name : '60岁以上'
				} ]
			},
			field : [ '18-29岁', '30-39岁', '40-49岁', '50-59岁', '60岁以上' ]
		}

		//人均投资额
		var yonghufenxi_data_3 = {
			title : '金额分布',
			//subtitle:"101501.22",
			data : [ {
				value : 34548,
				name : '1万以下'
			}, {
				value : 16187,
				name : '1-5万'
			}, {
				value : 6045,
				name : '5-10万'
			}, {
				value : 9055,
				name : '10-50万'
			}, {
				value : 3192,
				name : '50万以上'
			} ],
			field : [ '1万以下', '1-5万', '5-10万', '10-50万', '50万以上' ]
		}
		var yonghufenxi_1 = echarts.init(document.getElementById('yonghufenxi_1'));
		var yonghufenxi_3 = echarts.init(document.getElementById('yonghufenxi_3'));
		// 指定图表的配置项和数据
		var yonghufenxi_1_option = {
			color : mainColor,
			tooltip : {

				trigger : 'item',
				//formatter: "{a} <br/>{b}: {c} ({d}%)"
				formatter : function(obj) {
					if (obj.data.value == "14021") { //hack 数字bug 应该显示34.09 插件显示 34.1
						obj.percent = "20.32";
					}
					return obj.seriesName + "<br/>" + obj.data.name + " "
							+ obj.data.value + "(" + obj.percent + "%)";

				}
			},
			legend : {
				orient : 'vertical',
				x : 'right',
				y : 'middle',
				data : data_6_1.field,
				textStyle : {
					color : [ "#fff" ]
				}
			},

			series : [ {
				name : data_6_1.title,
				type : 'pie',
				radius : [ 0, '30%' ],
				label : {
					normal : {
						position : 'inner',
						formatter : "{b}\n{d}% "
					}
				},
				data : data_6_1.data.sex,
				center : [ '40%', '50%' ],
				color : [ "#1dcded", "#6474cb" ]
			}, {
				name : data_6_1.title,
				type : 'pie',
				label : {
					normal : {
						/* formatter: "{b}\n{d}%" */
						formatter : function(obj) {

							if (obj.value == "14021") { //hack 数字bug 应该显示34.09 插件显示 34.1
								obj.percent = "20.32";
							}
							return obj.data.name + "\n" + obj.percent + "%";
						}
					}
				},
				radius : [ '40%', '55%' ],
				data : data_6_1.data.age,
				center : [ '40%', '50%' ]
			} ]
		};

		var yonghufenxi_3_option = {
			color : mainColor,
			title : {
				text : yonghufenxi_data_3.title,
				textStyle : mainTextStyle,
				left : "37%",
				top : 180
			},
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b}元: {c}人 ({d}%)"
			},
			legend : {
				orient : 'vertical',
				x : 'right',
				y : 'middle',
				data : yonghufenxi_data_3.field,
				textStyle : {
					color : [ "#fff" ]
				}
			},
			series : [ {
				name : yonghufenxi_data_3.title,
				type : 'pie',
				label : {
					normal : {
						formatter : "{b}\n{d}%"
					}
				},
				radius : [ '40%', '55%' ],
				data : yonghufenxi_data_3.data,
				center : [ '45%', '50%' ]
			} ]
		};
	</script>
	<script>
		/* section7 用户分析数据
		-----------------------------------------------------------------------------------------*/
		var data_6_5 = {
			title : '\r10大投资人\n金额之和占比',
			subtitle : "3.22%",//十大投资人占比
			data : [ {
				value : 282767500,
				name : '10大投资人金额'
			}, {
				value : 8503132928,
				name : '其他投资人金额'
			} ]
		}
		var dangyuezhizui_1 = echarts.init(document
				.getElementById('dangyuezhizui_1'));

		var dangyuezhizui_1_option = {
			color : mainColor,
			tooltip : {
				trigger : 'item',
				formatter : "{a} <br/>{b}: {c} ({d}%)"
			},
			title : {
				text : data_6_5.title,
				textStyle : mainTextStyle,
				subtext : data_6_5.subtitle,
				subtextStyle : {
					color : "#5ac0fe",
					fontSize : 20
				},
				left : "center",
				top : 120
			},
			series : [ {
				name : data_6_5.title,
				type : 'pie',
				label : {
					normal : {
						formatter : "{b}\n{d}%",
						textStyle : {
							fontSize : 16
						}
					}
				},
				radius : [ '40%', '55%' ],
				data : data_6_5.data,
				center : [ '50%', '50%' ]
			} ]
		};
	</script>

	<script>
    $(document).ready(
		function() {
			var nav = $(".right-nav");
			var countUpOptions = {
				useEasing : true,
				useGrouping : true,
				separator : ',',
				decimal : '.',
				prefix : '',
				suffix : ''
		   };
		//向下滚屏
		$(document).on('click', '.scroll', function() {
			$.fn.fullpage.moveSectionDown();
		});
		$('#fullpage').fullpage({
			anchors : [ 'top', 'section1','section2', 'section3','section4', 'section4','section6', 'section7','section8', 'section9','section10','section10','section12','section12','section12','section12','section12','section12', 'over' ],		
			onLeave : function(index,nextIndex, direction) {
			var idx = nextIndex - 1;
			var anchors = $("#fullpage").children(".section").eq(idx).data("anchor");
			if (idx == 1) {
				var s1data1 = new CountUp("s1data1", 0,19137442346, 0,1.5,countUpOptions); //累计交易额(元)
				var s1data2 = new CountUp("s1data2", 0,367893, 0, 1.5,countUpOptions);//平台注册人数(人)
				var s1data3 = new CountUp("s1data3", 0,649088728, 0,1.5,countUpOptions);//累计赚取收益(元)
				s1data1.start();
				s1data2.start();
				s1data3.start();
			} else if (idx == 2) {
				var s2data1 = new CountUp("s2data1", 0,294134, 0, 1.5,countUpOptions);
				var s2data2 = new CountUp("s2data2", 0,88.896, 3, 1.5,countUpOptions);
				var s2data3 = new CountUp("s2data3", 0,23116.63, 2,1.5,countUpOptions);
				var s2data8 = new CountUp("s2data8", 0,9.96, 2, 1.5,countUpOptions);
				var s2data9 = new CountUp("s2data9", 0,8.24, 2, 1.5,countUpOptions);
				s2data1.start();
				s2data2.start();
				s2data3.start();
				s2data8.start();
				s2data9.start();
				echarts2_1.setOption(echarts2_1_option);
			} else if (idx == 3) {
							
				borrow3_1.setOption(borrow3_1_option);
			} else if (idx == 4) {
				var s3data1 = new CountUp("s3data1", 0,155186, 0, 1.5,countUpOptions);
				var s3data2 = new CountUp("s3data2", 0,52.76, 2, 1.5,countUpOptions);
				var s3data3 = new CountUp("s3data3", 0,11573, 0, 1.5,countUpOptions);
				var s3data4 = new CountUp("s3data4", 0,3.93, 2, 1.5,countUpOptions);
				var s3data5 = new CountUp("s3data5", 0,127375, 0, 1.5,countUpOptions);
				var s3data6 = new CountUp("s3data6", 0,43.31, 2, 1.5,countUpOptions);
				s3data1.start();
				s3data2.start();
				s3data3.start();
				s3data4.start();
				s3data5.start();
				s3data6.start();
				echarts3_1.setOption(echarts3_1_option);
			} else if (idx == 5) {
				var s4data1 = new CountUp("s4data1", 0,38.69, 2, 1.5,countUpOptions);
				var s4data2 = new CountUp("s4data2", 0,43.78, 2, 1.5,countUpOptions);
				var s4data3 = new CountUp("s4data3", 0,2.48, 2, 1.5,countUpOptions);
				var s4data4 = new CountUp("s4data4", 0,2.80, 2, 1.5,countUpOptions);
				var s4data5 = new CountUp("s4data5", 0,47.21, 2, 1.5,countUpOptions);
				var s4data6 = new CountUp("s4data6", 0,53.42, 2, 1.5,countUpOptions);
				s4data1.start();
				s4data2.start();
				s4data3.start();
				s4data4.start();
				s4data5.start();
				s4data6.start();
				echarts4_1.setOption(echarts4_1_option);
			} else if (idx == 6) {
				yonghufenxi_1.setOption(yonghufenxi_1_option);
				yonghufenxi_3.setOption(yonghufenxi_3_option);
			} else if (idx == 7) {
				var s6data1 = new CountUp("s6data1", 0,4258.18, 2,1.5,countUpOptions);//当月投资金额最高(万元)
				var s6data2 = new CountUp("s6data2", 0,129.02, 2, 1.5,countUpOptions);//当月投资次数最多(次)
				var s6data3 = new CountUp("s6data3", 0,2639, 0, 1.5,countUpOptions);//当月历史回报最高(万元)
				s6data1.start();
				s6data2.start();
				s6data3.start();
				dangyuezhizui_1.setOption(dangyuezhizui_1_option);
			} else if (idx == 8) {
				var s8data1 = new CountUp("s8data1", 0,56940, 0, 1.5,countUpOptions);//400电话数量(个)
				var s8data2 = new CountUp("s8data2", 0,70421, 0, 1.5,countUpOptions);//qq客服接待人数(人)
				var s8data3 = new CountUp("s8data3", 0,11890, 0, 1.5,countUpOptions);//微信客服接待人数(人)
				var s8data4 = new CountUp("s8data4", 0,124120, 0, 1.5,countUpOptions);//解决问题个数(个)
				s8data1.start();
				s8data2.start();
				s8data3.start();
				s8data4.start();
			} else if (idx == 9) {
	
			}
			if (idx == 18) {
				$(".right-nav").fadeOut(300);
			} else if (nav.is(":hidden")) {
				$(".right-nav").fadeIn(300);
			}
			$(".right-nav ul li").removeClass("active");
			$(".right-nav ul li[authors="+ anchors+ "]").addClass("active");
		}
	});

	$(".right-nav ul li").on("click", function() {
		var idx = $(this).data("idx") + 1;
		$.fn.fullpage.moveTo(idx);
	})
});
	</script>
</body>

</html>
