<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<script type="text/javascript"
	src="${themeRoot}/vendor/jquery/jquery.min.js"></script>

<script type="text/javascript"
	src="${themeRoot}/vendor/plug-in/jspdf/jspdf.min.js"></script>
<script type="text/javascript"
	src="${themeRoot}/vendor/plug-in/jspdf/html2canvas.js"></script>
<title>反欺诈风险监测信息</title>
<style type="text/css">
* {
	margin: 0 auto;
	font-family: 微软雅黑;
	font-style: normal;
	font-size: 14px;
	font-weight: 400;
	color: #256a85;
}

table {
	width: 95%;
	border-collapse: collapse;
	border-spacing: 0;
	border-left: 1px solid #ddd;
	border-top: 1px solid #ddd;
	margin-top: 25px;
	margin-bottom: 10px;
	text-align: center;
	font-size: 13px;
	line-height: 36px;
}

th, td {
	border-right: 1px solid #ddd;
	border-bottom: 1px solid #ddd;
}

.bold {
	font-family: 微软雅黑 bold;
	font-weight: 700;
}

.div {
	margin: 3px auto;
	border: 1px solid #ddd;
	padding-bottom: 13px;
	width: 80%;
	background-color: white;
}

.toptitle {
	height: 25px;
	background: #66a9bf;
	padding: 6px 5px 2px 10px;
	color: #FFF;
}

.download {
	float: right;
	margin: 5px 15px 10px auto;
	color: #0066ff;
	font-size: 13px;
}

.downloadicon {
	height: 12px;
	width: 9px;
	left: 10;
	top: 0;
	margin-right: 2px;
}

.baseInfo {
	width: 95%;
	border: 0px;
	border-bottom: 1px dotted #999;
	line-height: normal;
	text-align: left;
}

.baseInfo td {
	color: #999999;
	padding-bottom: 6px;
}

.baseInfo td {
	border: 0px;
}

.tiptitle {
	width: 95%;
	border-bottom: 1px solid #66a9bf;
	margin: 30px auto 25px auto;
	color: #256a85;
	padding-bottom: 7px;
}

.bgblue {
	background: #dfeef4;
}

.bgblue td {
	font-family: 微软雅黑 bold;
	font-weight: 700;
}

.bgblue2 {
	background: #eef6f9;
}

.shiyi {
	display: inline-block;
	font-size: 12px;
	line-height: normal;
	margin: 8px 20px;
}

.bar {
	width: 137px;
	height: 137px;
	margin-top: 20px;
}

#score {
	display: inline-block;
	margin-top: 50px;
	font-size: 36px;
}

.detail {
	text-align: left;
	padding-left: 10px;
}

.green {
	color: #7ab326;
	text-align: center;
}

#gltable td {
	line-height: 28px;
	font-size: 13px;
}

#gltable th {
	line-height: 35px;
	font-size: 14px;
	font-weight: 700 !important;
}

.sifa td {
	line-height: 25px;
}

.sifaresult td {
	font-size: 13px;
}

.label {
	color: #0b5fa5 !important;
	font-weight: bold;
}

.imgPhoto {
	display: inline-block;
	background: #eee;
	height: 120px;
	width: 100px;
}

#img {
	width: 100px;
	height: 120px;
}

.imgName {
	line-height: 36px !important;
}

.res {
	color: #333;
}

.trres td {
	text-align: left;
	padding-left: 200px;
}

.trres td:not (.conimg ){
	text-align: left;
	padding-left: 226 px;
}

.trres td label {
	color: #333;
}
</style>
<script type="text/javascript">
	$(function() {
		var score = $('#score').text();
		if (score == "— —" || score >= 60 || score == "NR") {
			$('#score').css('color', '#256a85');
			if (score == "— —") {
				$('#score').css('font-size', '18px');
				$('#score').css('font-weight', '700');
			}
		} else {
			$('#score').css('color', 'red');
		}
		$('.bar').each(function(index, el) {
			var num = $(this).find('span').text();
			if (num == "— —" || num == "NR" || num == 100)
				$(this).addClass('bar100');
			if (num == 0)
				$(this).addClass('bar0');
			if (num > 0 && num <= 5)
				$(this).addClass('bar5');
			if (num > 5 && num <= 10)
				$(this).addClass('bar10');
			if (num > 10 && num <= 15)
				$(this).addClass('bar15');
			if (num > 15 && num <= 20)
				$(this).addClass('bar20');
			if (num > 20 && num <= 25)
				$(this).addClass('bar25');
			if (num > 25 && num <= 30)
				$(this).addClass('bar30');
			if (num > 30 && num <= 35)
				$(this).addClass('bar35');
			if (num > 35 && num <= 40)
				$(this).addClass('bar40');
			if (num > 40 && num <= 45)
				$(this).addClass('bar45');
			if (num > 45 && num <= 50)
				$(this).addClass('bar50');
			if (num > 50 && num <= 55)
				$(this).addClass('bar55');
			if (num > 55 && num <= 60)
				$(this).addClass('bar60');
			if (num > 60 && num <= 65)
				$(this).addClass('bar65');
			if (num > 65 && num <= 70)
				$(this).addClass('bar70');
			if (num > 70 && num <= 75)
				$(this).addClass('bar75');
			if (num > 75 && num <= 80)
				$(this).addClass('bar80');
			if (num > 85 && num <= 90)
				$(this).addClass('bar85');
			if (num > 90 && num <= 95)
				$(this).addClass('bar90');
			if (num > 95 && num <= 100)
				$(this).addClass('bar95');
		});
		//	 (01.P2P企业 02.小贷公司 03.担保公司 04.财务公司 99.其他) （01.审批通过 02.审批拒绝  04.重新审批 05.客户取消
		$('.huiyuan').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());
			if (detail.indexOf("01") >= 0) {
				$(this).text("P2P企业");
			}
			if (detail.indexOf("02") >= 0) {
				$(this).text("小贷公司");
			}
			if (detail.indexOf("03") >= 0) {
				$(this).text("担保公司");
			}
			if (detail.indexOf("04") >= 0) {
				$(this).text("财务公司");
			}
			if (detail.indexOf("05") >= 0) {
				$(this).text("消费金融");
			}
			if (detail.indexOf("06") >= 0) {
				$(this).text("典当公司");
			}
			if (detail.indexOf("07") >= 0) {
				$(this).text("民间借贷");
			}
			if (detail.indexOf("08") >= 0) {
				$(this).text("保险机构");
			}
			if (detail.indexOf("09") >= 0) {
				$(this).text("融资租赁");
			}
			if (detail.indexOf("99") >= 0) {
				$(this).text("其他");
			}
		})
		$('.shenpi').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());
			if (detail.indexOf("01") >= 0) {
				$(this).text("审批通过");
			}
			if (detail.indexOf("02") >= 0) {
				$(this).text("审批拒绝");
			}
			if (detail.indexOf("04") >= 0) {
				$(this).text("重新审批");
			}
			if (detail.indexOf("05") >= 0) {
				$(this).text("客户取消");
			}
		})
		//字典（A.抵押 B.质押 C.担保 D.信用  E.保证 Y.其它）
		$('.danbao').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());
			if (detail.indexOf("A") >= 0) {
				$(this).text("抵押");
			}
			if (detail.indexOf("B") >= 0) {
				$(this).text("质押");
			}
			if (detail.indexOf("C") >= 0) {
				$(this).text("担保");
			}
			if (detail.indexOf("D") >= 0) {
				$(this).text("信用");
			}
			if (detail.indexOf("E") >= 0) {
				$(this).text("保证");
			}
			if (detail.indexOf("Y") >= 0) {
				$(this).text("其他");
			}
		})
				$('.yuqiyuanyin').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());01.还款能力下降 02.恶意拖欠 03.身份欺诈 04.逃逸 05.犯罪入狱 06.疾病 07.死亡 99.其他
			if (detail.indexOf("01") >= 0) {
				$(this).text("还款能力下降");
			}
			if (detail.indexOf("02") >= 0) {
				$(this).text("恶意拖欠");
			}
			if (detail.indexOf("03") >= 0) {
				$(this).text("身份欺诈");
			}
			if (detail.indexOf("04") >= 0) {
				$(this).text("逃逸");
			}
			if (detail.indexOf("05") >= 0) {
				$(this).text("犯罪入狱");
			}
			if (detail.indexOf("06") >= 0) {
				$(this).text("疾病");
			}
			if (detail.indexOf("07") >= 0) {
				$(this).text("死亡");
			}
			if (detail.indexOf("99") >= 0) {
				$(this).text("其他");
			}
		})
				$('.yuqizhuangtai').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());（01.催收中 02.催收还款中 03.正常还款 04.核销 05.逾期已结清 06.正常结清 07.协议还款）
						if (detail.indexOf("01") >= 0) {
				$(this).text("催收中");
			}
			if (detail.indexOf("02") >= 0) {
				$(this).text("催收还款中");
			}
			if (detail.indexOf("03") >= 0) {
				$(this).text("正常还款");
			}
			if (detail.indexOf("04") >= 0) {
				$(this).text("核销");
			}
			if (detail.indexOf("05") >= 0) {
				$(this).text("逾期已结清");
			}
			if (detail.indexOf("06") >= 0) {
				$(this).text("正常结清");
			}
			if (detail.indexOf("07") >= 0) {
				$(this).text("协议还款");
			}
		})
		$('.detail').each(function(index, el) {
			var detail = $(this).text();
			//	alert($(this).text());
			if (detail.indexOf("未发现关联风险信息") >= 0) {
				$(this).removeClass("detail");
				$(this).addClass('green');
			}
			if (detail.indexOf("— —") >= 0) {
				$(this).removeClass("detail");
				$(this).css("text-align", "center");
			}
			if (detail.indexOf("未开通查询") >= 0) {
				$(this).removeClass("detail");
				$(this).css("text-align", "center");
				$(this).css("color", "#999");
			}
		})
		$('.result').each(function(index, el) {
			var result = $(this).text();
			if (result == "√") {
				$(this).addClass('green').css("font-weight", "700");
			}
		})
		$('.power').each(function() {
			var result = $(this).text();
			if (result == "未开通查询")
				$(this).css("color", "#999");
		});
	});

	function downPdf() {
		html2canvas(document.getElementById("pdf"), {
			dpi: 80,
			onrendered : function(canvas) {
				
				var scale = 2; //定义任意放大倍数 支持小数
				
				var contentWidth = canvas.width;
				var contentHeight = canvas.height;

				//一页pdf显示html页面生成的canvas高度;
				var pageHeight = contentWidth / 592.28 * 841.89;
				//未生成pdf的html页面高度
				var leftHeight = contentHeight;
				//pdf页面偏移
				var position = 0;
				//a4纸的尺寸[595.28,841.89]，html页面生成的canvas在pdf中图片的宽高
				var imgWidth = 595.28;
				var imgHeight = 592.28 / contentWidth * contentHeight;

				var pageData = canvas.toDataURL('image/jpeg', 1.0);

				var pdf = new jsPDF('', 'pt', 'a4');

				//有两个高度需要区分，一个是html页面的实际高度，和生成pdf的页面高度(841.89)
				//当内容未超过pdf一页显示的范围，无需分页
				if (leftHeight < pageHeight) {
					pdf.addImage(pageData, 'JPEG', 0, 0, imgWidth, imgHeight);
				} else {
					while (leftHeight > 0) {
						pdf.addImage(pageData, 'JPEG', 0, position, imgWidth,
								imgHeight)
						leftHeight -= pageHeight;
						position -= 841.89;
						//避免添加空白页
						if (leftHeight > 0) {
							pdf.addPage();
						}
					}
				}

				pdf.save('${mspapply.name}.pdf');
			}
		})
	}
</script>
</head>
<body>
	<div id="pdf" class="div">
		<div class="download">
			<button id="renderPdf" onclick="downPdf()">DOWNLOAD PDF文档</button>
		</div>
		<table class="baseInfo">
			<tbody>
				<tr>
					<td width="25%">姓名:${mspapply.name}</td>
					<td width="35%">身份证号：${mspapply.identityCard}</td>
					<td width="40%">报告编码：${mspapply.applyId}</td>
				</tr>
				<tr>
					<td>查询机构：汇盈金服</td>
					<td>查询时间：${mspapply.applyDate}</td>
					<td>操作人员：${mspapply.createUser}</td>
				</tr>
			</tbody>
		</table>
		<div class="toptitle bold">反欺诈风险监测信息</div>
		<div class="tiptitle bold">身份真实性验证</div>
		<!-- 		<table cellpadding="0" cellspacing="0"> -->
		<!-- 			<tbody> -->
		<!-- 				<tr> -->
		<!-- 					<td class="bold bgblue" width="200">身份证所属地区</td> -->
		<!-- 					<td width="262">屯留县</td> -->
		<!-- 					<td class="bold bgblue" width="200">性别</td> -->
		<!-- 					<td width="261">女</td> -->
		<!-- 				</tr> -->
		<!-- 				<tr> -->
		<!-- 					<td class="bold bgblue">年龄</td> -->
		<!-- 					<td>26</td> -->
		<!-- 					<td class="bold bgblue">手机号归属地</td> -->
		<!-- 					<td>山东</td> -->
		<!-- 				</tr> -->
		<!-- 			</tbody> -->
		<!-- 		</table> -->
		<table style="line-height: 45px !important;" cellpadding="0"
			cellspacing="0">
			<tbody>
				<tr class="trres">
					<th class="bold bgblue" width="200">姓名+身份证号验证</th>

					<td colspan="4" class="conimg"><label> 
							<c:if test="${fqz.identityauth == 0}">
								未开通查询权限
							</c:if> 
							<c:if test="${fqz.identityauth == 1}">
								身份证、姓名验证一致
							</c:if>
							<c:if test="${fqz.identityauth == 2}">
								身份证、姓名验证不一致
							</c:if>
							<c:if test="${fqz.identityauth == 3}">
								库中无此号
							</c:if>
							<c:if test="${fqz.identityauth == 4}">
								未知
							</c:if>



					</label></td>
				</tr>
				<!-- 				<tr class="trres"> -->
				<!-- 					<th class="bold bgblue" width="200">姓名+银行卡验证</th> -->





				<!-- 					<td colspan="4"><label style="color: #999;">未开通查询</label></td> -->

				<!-- 				</tr> -->
				<!-- 				<tr class="trres"> -->
				<!-- 					<th class="bold bgblue" width="200">姓名+身份证号+银行卡验证</th> -->





				<!-- 					<td colspan="4"><label style="color: #999;">未开通查询</label></td> -->

				<!-- 				</tr> -->
				<!--     	<tr class="trres"> -->
				<!--     		<th class="bold bgblue" rowspan="5" width="200">学历查询结果</th> -->

				<!--     			<td colspan="4"  class="conimg"> -->
				<!--     				<img src='../../images/chahao.png' style="width:18px;margin-right:5px;"/> -->
				<!--     				<label >查询无结果</label></td> -->





				<!--     			<td colspan="4" class="conimg" > -->
				<!--     				<img src='../../images/duigou.png' style="width:18px;margin-right:5px;"/> -->



				<!--     			<td colspan="4"  ><label>未知</label></td> -->

				<!--     		<th rowspan="5" class="tdpaichu"> -->
				<!--     			<div class="imgName label">学历照片</div> -->

				<!--     				<div class="imgPhoto"> -->

				<!-- 		    		</div> -->




				<!--     		</th> -->
				<!--     	</tr> -->
				<!--     	<tr> -->
				<!--     		<td class="label">毕业院校</td> -->

				<!--     		<td class="label">学历</td> -->

				<!--     	</tr> -->
				<!--     	<tr> -->
				<!--     		<td class="label">入学年份</td> -->

				<!--     		<td class="label">专业</td> -->

				<!--     	</tr> -->
				<!--     	<tr> -->
				<!--     		<td class="label">毕业时间</td> -->

				<!--     		<td class="label">毕业结论</td> -->

				<!--     	</tr> -->
				<!--     	<tr> -->
				<!--     		<td class="label">学历类型</td> -->

				<!--     		<td class="label">学校性质</td> -->

				<!--     	</tr> -->
			</tbody>
		</table>

		<div class="tiptitle bold">小额信贷风险信息</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td colspan="2" class="bold bgblue" width="767">风险信息明细</td>
					<td rowspan="6" width="200"><span class="bold">小额信贷交易行为评分</span>
						<div class="bar bar90">
							<!-- style="background: url(../../images/progressbar/bar.png);" -->
							<span style="color: rgb(37, 106, 133);" id="score">${fqz.mspscore}</span>
							分

						</div></td>
				</tr>
				<tr>
					<td width="276">多重申请风险信息</td>
					<td class="power">${fqz.mspapply}</td>
				</tr>
				<tr>
					<td>多重借贷风险信息(未结清)</td>
					<td class="power">${fqz.mspcontract}</td>
				</tr>
				<tr>
					<td>已结清借贷信息</td>
					<td class="power">${fqz.mspendcontract}</td>
				</tr>
				<tr>
					<td>违约风险信息</td>
					<td class="power">${fqz.mspblacklist}</td>
				</tr>
				<tr>
					<td colspan="2" class="bgblue" style="text-align: left;"><span
						class="shiyi"> <span class="bold">交易行为评分释义：</span>①对小额信贷信息主体申请、签约、违约交易行为的综合评价；<br>
							<p
								style="display: inline-block; margin-left: 126px; font-size: 12px;">
								②评分为60以下，则存在违约信息；评分为60-80，则存在借贷记录，但不存在违<br>约信息。<br>评分为80以上，则只存在申请信息；笔数越多，金额越大，越接近分数下限。<br>③评分为NR，则表明无交易行为记录；
							</p>
					</span></td>
				</tr>
			</tbody>
		</table>

		<div class="tiptitle bold">黑名单信息</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td class="bold bgblue" width="300">黑名单类型</td>
					<td class="bold bgblue" width="623">风险信息</td>
				</tr>
				<tr>
					<td>MSP行业黑名单信息</td>
					<td class="detail"><c:choose>
							<c:when test="${fqz.mspblack !=null}">${fqz.mspblack}
							</c:when>
							<c:otherwise>未发现关联风险信息
							</c:otherwise>
						</c:choose></td>
				</tr>
				<tr>
					<td>合作机构风险库信息</td>
					<td class="detail"><c:choose>
							<c:when test="${fqz.allwinblack !=null}">${fqz.allwinblack}
							</c:when>
							<c:otherwise>未发现关联风险信息
							</c:otherwise>
						</c:choose></td>
				</tr>
			</tbody>
		</table>
		<div class="tiptitle bold">关联信息风险监测</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr>
					<td colspan="8" class="bold bgblue">反欺诈关联信息风险监测(共监测10项)</td>
				</tr>
				<tr class="bgblue2">
					<td width="128">监测项</td>
					<td width="111">身份证号</td>
					<td width="112">电话</td>
					<td width="111">邮箱</td>
					<td width="111">QQ号</td>
					<td width="112">单位名称</td>
					<td width="115">申请地点</td>
					<td width="111">总计</td>
				</tr>
				<tr>
					<td class="bgblue2">存在风险数量</td>
					<td>${fqz.countidentityrisk}</td>
					<td>${fqz.countphonerisk}</td>
					<td>${fqz.countemailrisk}</td>
					<td>${fqz.countqqrisk}</td>
					<td>${fqz.countcompanyrisk}</td>
					<td>${fqz.countaddressrisk}</td>
					<td>${fqz.countidentityrisk+fqz.countaddressrisk+fqz.countphonerisk+fqz.countqqrisk+fqz.countcompanyrisk+fqz.countemailrisk}</td>
				</tr>
			</tbody>
		</table>
		<table id="gltable" cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue">
					<th colspan="2">监测项</th>
					<th width="87">风险结果</th>
					<th width="486">关联风险信息内容</th>
				</tr>
				<!-- 开通查询 -->
				<tr>
					<td rowspan="4" width="120">身份证号</td>
				</tr>

				<tr>
					<td>是否在M3逾期履约库中</td>


					<c:choose>
						<c:when test="${fqz.identityrisk1 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.identityrisk1}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>





				</tr>

				<tr>
					<td>是否在借贷申请欺诈库中</td>
					<c:choose>
						<c:when test="${fqz.identityrisk2 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.identityrisk2}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>是否和其他申请人身份证号相同</td>
					<c:choose>
						<c:when test="${fqz.identityrisk3 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.identityrisk3}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>




				<tr>
					<td rowspan="7" width="120">手机号</td>
				</tr>

				<tr>
					<td>是否在小额信贷行业共享黑名单中</td>
					<c:choose>
						<c:when test="${fqz.phonerisk1 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk1}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>是否在合作机构风险库中</td>
					<c:choose>
						<c:when test="${fqz.phonerisk7 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk7}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>是否在M3逾期履约库中</td>
					<c:choose>
						<c:when test="${fqz.phonerisk2 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk2}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>是否在借贷申请欺诈库中</td>
					<c:choose>
						<c:when test="${fqz.phonerisk3 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk3}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>是否和其他申请人手机号相同</td>
					<c:choose>
						<c:when test="${fqz.phonerisk4 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk4}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>

				<tr>
					<td>前次申请时电话与本次不同</td>
					<c:choose>
						<c:when test="${fqz.phonerisk6 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.phonerisk6}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>




				<tr>
					<td rowspan="6" width="120">邮箱</td>
				</tr>

				<tr>
					<td>是否在小额信贷行业共享黑名单中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否在合作机构风险库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否在M3逾期履约库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否在借贷申请欺诈库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否和其他申请人邮箱相同</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>




				<tr>
					<td rowspan="5" width="120">QQ号</td>
				</tr>

				<tr>
					<td>是否在合作机构风险库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否在M3逾期履约库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否在借贷申请欺诈库中</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>是否和其他申请人QQ号相同</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>




				<tr>
					<td rowspan="3" width="120">单位名称</td>
				</tr>

				<tr>
					<td>是否与其他逾期借款人相同单位</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>

				<tr>
					<td>前次申请时单位名称与本次不同</td>
					<td class="result detail">— —</td>
					<td style="text-align: center;" class="detail">— —</td>
				</tr>





				<tr>
					<td width="120">申请地点</td>
					<td>前次申请时地点与本次不同</td>
					<c:choose>
						<c:when test="${fqz.applyaddressrisk1 !=null}">
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">${fqz.applyaddressrisk1}</td>
						</c:when>
						<c:otherwise>
							<td style="font-weight: 700;" class="result detail">√</td>
							<td class="detail">未发现关联风险信息</td>
						</c:otherwise>
					</c:choose>
				</tr>



			</tbody>
		</table>
		<div class="tiptitle bold">司法信息</div>
		<table class="sifa" border="1">
			<tbody>
				<tr class="bgblue">
					<td rowspan="100" style="width: 10%;">失信信息</td>
					<td style="width: 16%;">执行法院</td>
					<td style="width: 5%;">省份</td>
					<td style="width: 17%;">案号</td>
					<td style="width: 10%;">被执行人的履行情况</td>
					<td style="width: 18%;">失信被执行人行为具体情形</td>
					<td style="width: 12%;">发布时间</td>
					<td style="width: 12%;">立案时间</td>
				</tr>
				<!-- 开通查询-->
				<c:choose>
					<c:when test="${empty shixinInfos}">
						<tr class="sifaresult">
							<td colspan="7">无</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${shixinList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.zhixingcourt }"></c:out></td>
								<td><c:out value="${record.province }"></c:out></td>
								<td><c:out value="${record.anlinum }"></c:out></td>
								<td><c:out value="${record.beizhixingrenlvxingstatus }"></c:out></td>
								<td><c:out value="${record.jutistatus }"></c:out></td>
								<td><c:out value="${record.publictime }"></c:out></td>
								<td><c:out value="${record.liantime }"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<table class="sifa">
			<tbody>
				<tr class="bgblue">
					<td rowspan="100" style="width: 10%;">执行信息</td>
					<td style="width: 25%;">执行法院</td>
					<td style="width: 25%;">案例号</td>
					<td style="width: 10%;">案件状态</td>
					<td style="width: 15%;">执行标的</td>
					<td style="width: 15%;">立案时间</td>
				</tr>
				<!-- 开通查询 -->
				<c:choose>
					<c:when test="${empty zhixingList}">
						<tr class="sifaresult">
							<td colspan="5">无</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${zhixingList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.zhixingcourt }"></c:out></td>
								<td><c:out value="${record.anlinum }"></c:out></td>
								<td><c:out value="${record.anjianstate }"></c:out></td>
								<td><c:out value="${record.zhixingtaget }"></c:out></td>
								<td><c:out value="${record.liantime }"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<table>
			<tbody>
				<tr class="bgblue">
					<td rowspan="100" style="width: 10%">案件信息</td>
					<td style="width: 10%;">当事人类型</td>
					<td style="width: 5%;">性别</td>
					<td style="width: 10%">生日</td>
					<td style="width: 26%">案件标题</td>
					<td style="width: 17%;">案件字号</td>
					<td style="width: 10%;">案件类型</td>
					<td style="width: 12%;">审结日期</td>
				</tr>
				<!-- 开通查询 -->


				<c:choose>
					<c:when test="${empty anliList}">
						<tr class="sifaresult">
							<td colspan="7">无</td>
						</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${anliList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.dangshirenType }"></c:out></td>
								<td><c:out value="${record.sex }"></c:out></td>
								<td><c:out value="${record.birthday }"></c:out></td>
								<td><c:out value="${record.anjiantitle }"></c:out></td>
								<td><c:out value="${record.anjiannum }"></c:out></td>
								<td><c:out value="${record.anjiantype }"></c:out></td>
								<td>
									<fmt:formatDate value="${record.endDate}" pattern="yyyy-MM-dd" />
								</td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>




			</tbody>
		</table>
		<!-- 	<table> -->
		<!--     	<tr class="bgblue"> -->
		<!--         	<td rowspan=2 style="width:10%;">催款公示</td> -->
		<!--         	<td style="width:60%;">标题</td> -->
		<!--             <td style="width:30%;">公布时间</td> -->
		<!--         </tr> -->


		<!-- 	        	<tr class="sifaresult"> -->
		<!-- 		        	<td colspan="2">无</td> -->
		<!-- 		        </tr> -->



		<!-- 			        <tr class="sifaresult"> -->


		<!-- 			        </tr> -->




		<!--         	<tr class="sifaresult"> -->
		<!-- 	        	<td colspan="2" class="power">未开通查询</td> -->
		<!-- 	        </tr> -->

		<!-- 	</table> -->

		<div class="tiptitle bold">非本机构反欺诈查询量统计</div>
		<table>
			<tbody>
				<tr class="bgblue">
					<td>3个月内</td>
					<td>6个月内</td>
					<td>1年内</td>
					<td>累计查询量统计</td>
				</tr>
				<!-- 开通权限 -->
				<tr>
					<td>${fqz.querytimesinthree}</td>
					<td>${fqz.querytimesinsix}</td>
					<td>${fqz.querytimesinoneyea}</td>
					<td>${fqz.querytimesintwoyear}</td>
				</tr>


			</tbody>
		</table>
		<br> <br> <br> <br> <br>
		<div class="toptitle bold">个人小额信贷交易征信报告</div>
		<div class="tiptitle bold">信贷交易统计情况</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td rowspan="2"></td>
					<td colspan="5">借款申请记录</td>
					<td colspan="3">正常还款账户</td>
					<td colspan="3">异常还款账户</td>
					<td rowspan="2" width="50">行业不良记录</td>
					<td colspan="3">查询记录</td>
				</tr>
				<tr class="bgblue2">
					<td width="">待审核</td>
					<td width="">审批通过</td>
					<td width="">审批拒绝</td>
					<td width="">客户取消</td>
					<td width="">小计</td>
					<td width="">未结清</td>
					<td width="">已结清</td>
					<td width="">小计</td>
					<td width="">未结清</td>
					<td width="">已结清</td>
					<td width="">小计</td>
					<td width="">3个月内</td>
					<td width="">6个月内</td>
					<td width="">累计查询</td>
				</tr>
				<tr>
					<td class="bgblue2">笔数</td>
					<td width="">${title.applyingcount}</td>
					<td width="">${title.applypassedcount}</td>
					<td width="">${title.applyrejectcount}</td>
					<td width="">${quxiaoshu}</td>
					<td width="">${title.applytotalcount}</td>
					<td width="">${title.wjqcount}</td>
					<td width="">${title.jqcount}</td>
					<td width="">${title.totalcount}</td>
					<td width="">${title.ewjqcount}</td>
					<td width="">${title.ejqcount}</td>
					<td width="">${title.etotalcount}</td>
					<td width="">${title.querycount}</td>
					<td width="">${sangeyue}</td>
					<td width="">${liugeyue}</td>
					<td width="">${zongji}</td>
				</tr>
				<tr>
					<td class="bgblue2">总(合同)金额</td>
					<td width=""></td>
					<td width="">${tongguo}</td>
					<td width="">${jujue}</td>
					<td width="">${quxiao}</td>
					<td width="">${zongshu}</td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>

				</tr>
			</tbody>
		</table>
		<div class="tiptitle bold">信贷交易详情</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td colspan="6">借款申请记录明细(金额：元)</td>
				</tr>
				<tr class="bgblue2">
					<td width="">申请日期</td>
					<td width="">会员类型</td>
					<td width="">申请地点</td>
					<td width="">申请金额</td>
					<td width="">审批结果</td>
					<td width="">备注</td>
				</tr>
				
				<c:choose>
					<c:when test="${empty applyList}">
				<tr>
					<td colspan="6">无</td>
				</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${applyList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.applydate }"></c:out></td>
								<td  class="huiyuan"><c:out value="${record.membertype }"></c:out></td>
								<td><c:out value="${record.creditaddress }"></c:out></td>
								<td><c:out value="${record.loanmoney }"></c:out></td>
								<td  class="shenpi"><c:out value="${record.applyresult }"></c:out></td>
								<td><c:out value="${record.remark }"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
				
			</tbody>
		</table>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td colspan="9">正常还款账户明细(金额：元)</td>
				</tr>
				<tr class="bgblue2">
					<td width="">项目编号</td>
					<td width="">借款日期</td>
					<td width="">到期日期</td>
					<td width="">借款地点</td>
					<td width="">担保方式</td>
					<td width="">合同金额</td>
					<td width="">还款期数</td>
					<td width="">备注</td>
				</tr>
				<c:choose>
					<c:when test="${empty normalCreditList}">
				<tr>
					<td colspan="8">无</td>
				</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${normalCreditList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.num}"></c:out></td>
								<td><c:out value="${record.creditstartdate}"></c:out></td>
								<td><c:out value="${record.creditenddate}"></c:out></td>
								<td><c:out value="${record.creditaddress}"></c:out></td>
								<td class="danbao"><c:out value="${record.assuretype}"></c:out></td>
								<td><c:out value="${record.loanmoney}"></c:out></td>
								<td><c:out value="${record.loanperiods}"></c:out></td>
								<td><c:out value="${record.remark}"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td colspan="13">异常还款记录明细(金额：元)</td>
				</tr>
				<tr class="bgblue2">
					<td width="">项目编号</td>
					<td width="">借款日期</td>
					<td width="">到期日期</td>
					<td width="">担保方式</td>
					<td width="">合同金额</td>
					<td width="">还款期数</td>
					<td width="">逾期期数</td>
					<td width="">逾期时长</td>
					<td width="">逾期原因</td>
					<td width="">状态</td>
					<td width="">更新日期</td>
					<td width="">备注</td>
				</tr>
				<c:choose>
					<c:when test="${empty abnormalList}">
				<tr>
					<td colspan="12">无</td>
				</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${abnormalList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${record.num}"></c:out></td>
								<td><c:out value="${record.creditstartdate}"></c:out></td>
								<td><c:out value="${record.creditenddate}"></c:out></td>
								<td class="danbao"><c:out value="${record.assuretype}"></c:out></td>
								<td><c:out value="${record.loanmoney}"></c:out></td>
								<td><c:out value="${record.loanperiods}"></c:out></td>
								<td><c:out value="${record.checkoverduedate}"></c:out></td>
								<td><c:out value="${record.overduedays}"></c:out></td>
								<td class="yuqiyuanyin"><c:out value="${record.overduereason}"></c:out></td>
								<td class="yuqizhuangtai"><c:out value="${record.overduestate}"></c:out></td>
								<td><c:out value="${record.opertime}"></c:out></td>
								<td><c:out value="${record.remark}"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td colspan="5">查询记录明细</td>
				</tr>
				<tr class="bgblue2">
					<td width="">序号</td>
					<td width="">查询日期</td>
					<td width="">会员类型</td>
					<td width="">查询类别</td>
					<td width="">备注</td>
				</tr>
				<c:choose>
					<c:when test="${empty queryList}">
				<tr>
					<td colspan="5">无</td>
				</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${queryList }" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${status.index+1}"></c:out></td>
								<td><c:out value="${record.querydate}"></c:out></td>
								<td class="huiyuan"><c:out value="${record.membertype}"></c:out></td>
								<td><c:out value="${record.querytype}"></c:out></td>
								<td ><c:out value="${record.remark}"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<div class="tiptitle bold">行业不良信息</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td width="">序号</td>
					<td width="">报送/公开日期 </td>
					<td width="">最近逾期开始日期</td>
					<td width="">借款地点</td>
					<td width="">欠款总金额</td>
					<td width="">逾期天数</td>
					<td width="">电话 </td>
					<td width="">邮箱</td>
					<td width="">户籍地址</td>
					<td width="">现居地址</td>
				</tr>
								<c:choose>
					<c:when test="${empty blackList}">
				<tr>
					<td colspan="10">无</td>
				</tr>
					</c:when>
					<c:otherwise>
						<c:forEach items="${blackList}" var="record" begin="0" step="1"
							varStatus="status">
							<tr>
								<td><c:out value="${status.index+1}"></c:out></td>
								<td><c:out value="${record.createdate}"></c:out></td>
								<td ><c:out value="${record.lastoverduedate}"></c:out></td>
								<td><c:out value="${record.creditaddress}"></c:out></td>
								<td ><c:out value="${record.arrears}"></c:out></td>
								<td ><c:out value="${record.overduedays}"></c:out></td>
								<td ><c:out value="${record.phone}"></c:out></td>
								<td ><c:out value="${record.email}"></c:out></td>
								<td ><c:out value="${record.residenceaddress}"></c:out></td>
								<td ><c:out value="${record.currentaddress}"></c:out></td>
							</tr>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</tbody>
		</table>
		<div class="tiptitle bold">本人异议申告明细</div>
		<table cellpadding="0" cellspacing="0">
			<tbody>
				<tr class="bgblue2">
					<td width="">申告日期</td>
					<td width="">申告内容</td>
					<td width="">备注</td>
				</tr>
				<tr>
					<td colspan="3">无</td>
				</tr>
				<tr>
					<td width=""></td>
					<td width=""></td>
					<td width=""></td>
				</tr>
			</tbody>
		</table>
	</div>




</body>
</html>

