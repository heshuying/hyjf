<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>产品说明书</title>
	</head>
	<body class="bg_grey">
		<div class="specialFont response">
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">温州金融资产交易中心股份有限公司融通宝系列第【  】期说明书</p>
					<div class="contract_item_content">
						<p>声明与提示</p>
						<p>本期产品说明书依据《中华人民共和国证券法》、《温州金融资产交易中心资产支持收益权产品交易规则（试行）》及其他现行法律、法规以及温州金融资产交易中心的有关规定，并结合发行人的实际情况编制。</p>
						<p>发行人股东、高级管理人员承诺本次产品资金用途合法合规、发行程序合规，本期产品说明书不存在虚假记载、误导性陈述或重大遗漏，并保证所披露信息的真实、准确、完整。</p>
						<p>凡欲购买本次资产支持受益权产品的投资者，请认真阅读本期产品说明书及其有关信息披露文件，并进行独立投资判断。本次资产支持受益权产品经温州金融资产交易中心股份有限公司（简称“温金中心”）受理备案，本次资产支持受益权产品的发行通过温金中心的交易平台进行。温金中心对本次资产支持受益权产品的发行所作的任何决定，均不表明温金中心对本期产品的投资价值或者投资人的收益作出实质性判断或保证。任何与之相反的声明或提示均属虚假陈述。</p>
						<p>凡认购、受让并持有本次资产支持受益权产品的投资者，均视同自愿接受本期产品说明书对本次资产支持受益权产品各项权利义务的约定。本次资产支持受益权产品依法发行后，投资者自行承担投资风险。投资者在评价、认购、受让本次资产支持受益权产品时，应特别审慎地考虑本期产品说明书中所述的各项风险因素。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<p style="text-align:center">第一部分 产品简介 </p>
				<div>
					<p class="contract_item_title">一、本期产品要素表</p>
					<div class="contract_item_content">
						<table border="1px" cellspacing="0" cellpadding="0" class="borderNone">
							<tr>
								<td style="width:40%">本期产品</td>
								<td>温金融通宝系列第【 】期</td>
							</tr>
							<tr>
								<td>本期产品说明书</td>
								<td>温金融通宝系列第【 】期说明书</td>
							</tr>
							<tr>
								<td>本期发行人/回购人</td>
								<td>深圳润银商业保理有限公司</td>
							</tr>
							<tr>
								<td>本期产品类型</td>
								<td>资产支持收益权产品</td>
							</tr>
							<tr>
								<td>本期基础资产</td>
								<td>本产品基础资产为发行人持有的“一系列银行承兑汇票”（本期基础资产明细信息见附件一）所对应份额的收益权（以下简称“银票收益权”）</td>
							</tr>
							<tr>
								<td>风控措施</td>
								<td>1、经发行备案机构同意由发行人指定的第三方银行作为该系列产品的托收行；2、托收行对银行承兑汇票进行查询查复并托管；3、托收行对银行承兑汇票进行到期托收，并对托收资金进行监管，定向用以产品兑付。
								</td>
							</tr>
							<tr>
								<td>本期资金监管银行</td>
								<td>建设银行温州市分行营业部</td>
							</tr>
							<tr>
								<td>备案登记机构</td>
								<td>温州金融资产交易中心股份有限公司（以下简称“温金中心”）</td>
							</tr>
							<tr>
								<td>本期发行规模</td>
								<td>本期产品发行总额不超过人民币【  】万元</td>
							</tr>
							<tr>
								<td>本期产品的认购期</td>
								<td>本期产品的认购期不超过3个自然日，募集满额之日终止认购。</td>
							</tr>
							<tr>
								<td>本期产品的存续期</td>
								<td>本期产品存续期【  】天，计息日自{{计息开始日期}}起至{{计息结束日期}}止。本产品实际成立日以温金中心官方网站公告之日为准。</td>
							</tr>
							<tr>
								<td>本期产品年化历史回报率</td>
								<td>【  】%</td>
							</tr>
							<tr>
								<td>认购额</td>
								<td>当产品规模不超过20万元时，产品认购起点为【1000】元，按1元递增；当产品规模不超过50万元时，产品认购起点为【2500】元，按1元递增；
当产品规模超过50万时，产品认购起点为【5000】元，按1元递增。
								</td>
							</tr>
							<tr>
								<td>产品投资收益计算方式</td>
								<td>本期产品存续期内，预期投资收益为投资者认购金额×【 】%×当期天数÷365。
“当期天数”是指该期份额投资起息日至当期投资收益分配计算截止日（不含）的期间天数。
本期产品将于存续期届满后，向投资者返还本金及预期投资收益，兑付完成后，投资者对本期产品不再享有任何收益权。
本期产品投资收益精确到小数点后 2 位。所得收益采用舍尾法计算。
								</td>
							</tr>
							<tr>
								<td>投资收益分配方式</td>
								<td>发行人于本期产品到期时还本付息。</td>
							</tr>
							<tr>
								<td>赎回机制</td>
								<td>本期产品期间不可提前赎回</td>
							</tr>
							<tr>
								<td>本期产品资金募集账户</td>
								<td>账户名：温州金融资产交易中心股份有限公司;账号：33001623535053027726-0001;开户行：建设银行温州市分行营业部
								</td>
							</tr>
							<tr>
								<td>交易结算规则</td>
								<td>《温州金融资产交易中心交易与结算细则》</td>
							</tr>
							<tr>
								<td>日、交易日</td>
								<td>指温金中心公布的进行资产支持收益权计划产品认购、交易的期间内除法定节假日或周六、周日或温金中心临时公告的休市日以外的日期</td>
							</tr>
							<tr>
								<td>起息日</td>
								<td>产品开始计算收益的日期，即产品成立日。本期产品的起息日为募集满额之日的下一个工作日。</td>
							</tr>
							<tr>
								<td>止息日</td>
								<td>即计算投资者收益的日期。针对任一投资者的收益分配而言，止息日是本金结算日（不含）。</td>
							</tr>
							<tr>
								<td>投资收益到账日</td>
								<td>即投资者实际收到投资收益之日。本期产品投资收益到账日为收益分配计算截止日之后的3个工作日中的任一日，自收益分配计算截止日（含）起至投资收益到账日（含）止的期限内不对该投资收益另行计算收益。</td>
							</tr>
							<tr>
								<td>本金结算日</td>
								<td>即向投资者结算产品本金的日期。本期产品的本金结算日为存续期届满之日的当日。</td>
							</tr>
							<tr>
								<td>本金到账日</td>
								<td>即投资者实际收到认购本金之日。本期产品本金到账日为本金结算日之后的3个工作日内。</td>
							</tr>
							<tr>
								<td>募集期限</td>
								<td>投资者可参与认购产品的时间区间</td>
							</tr>
							<tr>
								<td>元</td>
								<td>人民币元</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、认购对象</p>
					<div class="contract_item_content">
						<p>本期产品认购对象为温金中心认定的合格投资者会员，不得超过200人，包括：</p>
						<p>1、经相关金融监管部门批准设立的金融机构，包括商业银行、证券公司、基金管理公司、信托公司和保险公司等；还包括上述金融机构面向投资者发行的理财产品，包括但不限于银行理财产品、信托产品、投连险产品、基金产品、证券公司资产管理产品等；</p>
						<p>2、温金中心认可的企事业单位；</p>
						<p>3、在温金中心或温金中心指定的其他渠道进行风险承受能力测评达到温金中心认可标准的个人会员可认购；</p>
						<p>4、其他被温金中心认可的合格投资者。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">三、产品类型</p>
					<div class="contract_item_content">
						<p>由其他金融机构或温州金融交易中心认可的资产标的。增信措施如：包含但不限于流动性支持、母公司提供偿还义务、担保等。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<p style="text-align:center">第二部分 本期产品发行概况</p>
				<div>
					<p class="contract_item_title">一、发行人基本情况</p>
					<div class="contract_item_content">
						<p>（一）发行人基本信息</p>
						<p>中文名称：深圳润银商业保理有限公司</p>
						<p>法定代表人：王唯东</p>
						<p>设立日期：2015年10月28日</p>
						<p>注册资本：1000万人民币</p>
						<p>企业类型：有限责任公司</p>
						<p>注册地址：深圳市前海深港合作区前湾一路1号A栋201室</p>
						<p>联系地址：上海市龙华东路647号电科滨江中心17层</p>
						<p>联系电话： 021-64850988-226</p>
						<p>企业法人统一社会信用代码：9144030035915855X1 </p>
						<p>经营范围：保付代理（非银行融资类）；从事担保业务（不含融资性担保业务）；受托管理股权投资基金（不得以任何方式公开募集及发行基金、不得从事公开募集及发行基金管理业务）；股权投资、投资管理、投资咨询、经济信息咨询、网上从事商贸活动（以上均不含限制项目）；受托资产管理（不得从事信托、金融资产管理、证券资产管理等业务）；投资兴办实业（具体项目另行申报）；企业管理咨询；市场营销策划；国内贸易（不含专营、专控、专卖商品）；经营进出口业务（法律、行政法规、国务院决定禁止的项目除外，限制的项目须取得许可后方可经营）。</p>
						<p>（二）发行人简介</p>
						<p>深圳润银商业保理有限公司是北京巨品力能销售有限公司的全资子公司，成立于2015年10月28日，注册资本1000万元，公司法定代表人为王唯东。公司法定住所为深圳市前海深港合作区前湾一路1号A栋201室。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、本期产品发行批准及备案情况</p>
					<div class="contract_item_content">
						<p>2016年5月23日发行人股东会做出决议，同意了发行以银行承兑汇票收益权为一揽子标的的资产支持收益权类系列化产品的议案。</p>
						<p>2016年5月26日，发行人取得温金中心《受理通知书》，本期产品募集规模为不超过人民币【  】万元。本期产品由发行人择机发行，并在取得温金中心出具的《受理通知书》后【6】个月内完成发行。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">三、资金用途或投资用途介绍</p>
					<div class="contract_item_content">
						<p>(一)本期产品募集资金总额</p>
						<p>根据发行人的财务状况及资金需求情况，经发行人股东会决议，发行人向温金中心申请备案发行本产品，本期产品募集不超过人民币【  】万元。</p>
						<p>（二）资金投向</p>
						<p>经本期资产支持收益权产品发行所募集资金扣除相关发行费用后，发行人将募集资金用于补充发行人经营所需流动资金。</p>
						<p>（三）本产品投资的资产收益权对应之基础资产介绍</p>
						<p>发行人持有的“一系列银行承兑汇票”收益权（以下简称“银票收益权”）在温金中心挂牌转让，注册发行本产品。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">四、本期产品的风险控制措施</p>
					<div class="contract_item_content">
						<p> 1、经发行备案机构同意由发行人指定的第三方银行作为该系列产品的托收行；</p>
						<p> 2、托收行对银行承兑汇票进行查询查复并托管；</p>
						<p> 3、托收行对银行承兑汇票进行到期托收，并对托收资金进行监管，定向用以产品兑付</p>
						
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">五、与本次发行有关的机构</p>
					<div class="contract_item_content">
						<p>（一）登记备案机构介绍</p>
						<p>温州金融资产交易中心股份有限公司为本期产品的登记备案机构。</p>
						<p>1、基本信息</p>
						<p>名称：温州金融资产交易中心股份有限公司</p>
						<p>住所：浙江省温州市高新园区高一路158号一层</p>
						<p>法定代表人：林皓</p>
						<p>经办人：刘轲</p>
						<p>电话：0577-8550 2370</p>
						<p>2、公司简介</p>
						<p>温州金融资产交易中心（以下简称“温金中心”）经浙江省人民政府批准，于2014年9月在国家级金融改革试验区浙江省温州市注册成立，从事各类金融资产交易及相关服务。温金中心根据国家金融改革的基本思路，设立竞争有序的金融生态，拓宽民间资本的投融资空间，按照国有控股，民营参股，以会员制的模式进行市场化的运作。金融资产交易中心的设立为包括金融国有资产、金融不良资产、信贷资产、债权、信托产品、私募股权、标准化金融产品及金融衍生品等在内的各类金融资产流动提供了一个有效渠道，为温州金融综合改革背景下设立金融资产交易中心具有重要的现实意义。</p>
						<p>（二）资金监管机构介绍</p>
						<p>中国建设银行受聘作为本期产品的资金监管机构。</p>
						<p>1、基本信息</p>
						<p>名称：中国建设银行温州分行</p>
						<p>住所：温州市车站大道701号建行大厦</p>
						<p>法定代表人：杨锡舟</p>
						<p>经办人：朱涤墨</p>
						<p>电话：0577-8808 0681</p>
						<p>2、公司简介</p>
						<p>中国建设银行股份有限公司是一家在中国市场处于领先地位的股份制商业银行，为客户提供全面的商业银行产品与服务。主要经营领域包括公司银行业务、个人银行业务和资金业务，多种产品和服务（如基本建设贷款、住房按揭贷款和银行卡业务等）在中国银行业居于市场领先地位。</p>
						<p>建行拥有广泛的客户基础，与多个大型企业集团及中国经济战略性行业的主导企业保持银行业务联系，营销网络覆盖全国的主要地区。于2013年6月末，本行市值为1,767 亿美元，居全球上市银行第五位。建行在中国内地设有分支机构14,295 家，在香港、新加坡、法兰克福、约翰内斯堡、东京、首尔、纽约、胡志明市、悉尼、台北设有10家一级海外分行，拥有建信基金、建信租赁、建信信托、建信人寿、中德住房储蓄银行、建行亚洲、建行伦敦、建行俄罗斯、建行迪拜、建银国际等多家子公司，为客户提供全面的金融服务。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<p style="text-align:center">第三部分 投资者权益保护</p>
				<div>
					<p class="contract_item_title">一、偿债计划</p>
					<div class="contract_item_content">
						<p>(一)本金及收益兑付</p>
						<p>本产品到期后一次性兑付本金及投资收益。</p>
						<p>本期产品本金及收益支付的具体事项将按照国家法律法规有关规定及温金中心相关规则，可由发行人在温金中心网站或温金中心认可的其他方式发布的兑付公告中加以说明，上述公告说明与本产品说明书不一致的，以公告说明为准。</p>
						<p>根据国家税收法律、法规，投资者投资本期产品应缴纳的有关税金由投资者自行缴纳，发行人及温金中心不进行代扣代缴。</p>
						<p>(二)兑付资金来源</p>
						<p>托收行对银票收益权进行到期兑付，兑付资金来源为发行人获得的基础产品到期的本金及收益。</p>
						
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、偿债保障措施</p>
					<div class="contract_item_content">
						<p>(一)保障措施</p>
						<p>为维护本期产品持有人的合法利益，发行人建立了一系列保障措施，包括健全风险监管和预警机制及加强信息披露等，形成一套保证本期产品按时还本支付收益的保障措施。具体如下：</p>
						<p>1、承兑行到期兑付本期产品投资人全部认购金额，托收行对银票进行查验、接收、保管及托收，并在回购到期日将共管账户中收到的回购到期日对应票据的托收资金足额划付到交易所指定账户。</p>
						<p>2、严格履行信息披露业务。发行人将遵循真实、准确、完整、及时的信息披露原则，按温金中心的有关规定进行重大事项信息披露，使托收行兑付能力等情况受到本期产品持有人的监督，防范偿债风险。</p>
						<p>(二)违约责任及解决措施</p>
						<p>托收行保证按照本次产品说明书约定的还本及支付收益。若托收行未按时还本支付收益，或发生其他违约情况时，本期产品持有人可向发行人进行追索。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<p style="text-align:center">第四部分 风险因素</p>
				<div>
					<div class="contract_item_content">
						<p>投资者购买本期产品，应当认真阅读本期产品说明书及有关的信息披露文件，进行独立的投资判断。如托收行未能兑付或者未能及时、足额兑付，温金中心不承担兑付义务及任何连带责任。投资者在评价和投资本期产品时，除本期产品说明书提供的其他相关材料外，投资者应特别认真地考虑以下各项风险因素：</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">一、本期产品的投资风险</p>
					<div class="contract_item_content">
						<p>(一)利率风险</p>
						<p>受国民经济总体运行状况、国家宏观经济、国家货币政策、财政政策等因素的影响，市场利率水平和利率结构存在波动的可能性。由于本产品采用固定利率的形式，在产品存续期间内，若市场利率上升，本产品的收益率不随市场利率上升而提高。</p>
						<p>（二）政策风险</p>
						<p>如果产品在实际运作过程中，如遇到国家宏观政策和相关法律法规发生变化，影响产品的发行、投资和兑付等，可能影响产品的投资运作和到期收益。</p>
						<p>（三）偿付风险</p>
						<p>产品存续期内，国内外宏观经济形势、产业金融政策、资本市场状况、市场环境等众多不可控因素可能发生变化，从而对发行人的经营、财务状况造成不利影响，使得发行人面临不能按期、足额支付本期产品的本金和利息，以致对投资人的利益造成一定影响的风险。</p>
						<p>（四）保障措施的履行风险</p>
						<p>尽管在本期产品发行时，发行人与托收行已根据现实情况安排了偿债保障措施来控制和降低本期产品的兑付风险，但是在本期产品存续期内，可能由于不可控的市场、政策、法律法规变化等因素导致目前拟定的偿债保障措施不能完全履行，进而影响投资者的利益。</p>
						<p>（五）资信风险</p>
						<p>托收行与银行、主要客户发生的重要业务往来中，未曾发生任何严重违约。在未来的业务经营中，托收行亦将秉承诚信经营的原则，严格履行所签订的合同、协议或其他承诺。但是，在本期产品存续期内，如果由托收行自身的相关风险或不可控因素导致托收行的财务状况发生重大不利变化，则托收行可能无法按期偿还贷款或无法履行与客户签订的业务合同，从而导致托收行资信状况恶化，进而影响本期产品的兑付。</p>
						<p>（六）不可抗力及意外事件风险</p>
						<p>自然灾害、战争等不能预见、不能避免、不能克服的不可抗力事件或系统故障、通讯故障、投资市场停止交易等意外事件的出现，可能对本期产品的成立、投资、兑付、信息披露、公告通知等造成影响，投资人将面临收益遭受损失的风险。对于由不可抗力及意外事件风险导致的任何损失，投资人须自行承担。</p>
					
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、发行人的相关风险</p>
					<div class="contract_item_content">
						<p>（一）信用风险</p>
						<p>若发行人与托收行的信用状况或者履约能力发生变化时，可能引起流动性风险。</p>
							<p>（二）政策风险</p>
						<p>与资产支持受益权产品发行相适应的法律法规体系仍有待建立和完善。因此，行业管理体制与监管政策的变化将可能给企业未来的业务经营带来一定的不确定性。税收政策的变化也可能会影响发行人的兑付。</p>
						<p>（三）管理风险</p>
						<p>随着企业资产及规模的扩大，为更好地适应市场需求，将对企业组织结构、管理体系、内部控制机制及人力资源管理等提出更高的要求。如果企业不能适应管理半径扩大而导致的相应管理难度加大，可能对未来的生产经营能力、盈利能力产生一定的影响。</p>
						<p>（四）行业风险</p>
						<p>发行人与托收行所处行业发展呈现一定的周期性，这种周期性将会造成公司主营业务增长速度的不稳定性。如果受经济周期影响，中国国民经济增长速度放缓或宏观经济出现周期性波动而公司未能对其有合理的预期并相应调整公司的经营行为，则将对公司的经营状况产生不利的影响，公司收入增长速度可能放缓，甚至受市场环境影响可能出现收入下降。公司主营业务未来一段时期内依然将面临激烈的市场竞争。若公司不能有效强化自身竞争优势，巩固现有优势地位，可能造成公司所占市场份额下滑，从而对公司经营业绩造成一定的不利影响。</p>
						
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<p style="text-align:center">第五部分 信息披露</p>
				<div>
					<p class="contract_item_title">一、信息披露方式</p>
					<div class="contract_item_content">
						<p>(一)信息披露义务人</p>
						<p>发行人及其全体股东及高级管理人员将严格按照《交易规则》等相关规定，指定专人负责信息披露相关事务，并保证所披露的信息真实、准确、完整、及时，不得虚假记载、误导性陈述或重大遗漏。全体股东若对所披露信息的真实性、准确性、完整性、及时性存在异议的，将按照温金中心的规定单独发表意见并陈述理由。</p>
						<p>(二)信息披露渠道</p>
						<p>发行人将在温金中心网站或温金中心指定的其他渠道进行信息披露。</p>
						
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、信息披露主要内容</p>
					<div class="contract_item_content">
						<p> (一)本期产品发行情况</p>
						<p>发行人应在本期产品在温金中心受理同意挂牌后通过温金中心网站或温金中心指定的其他渠道及时披露本期产品的名称、代码、期限、发行金额、收益率及发行人的联系方式等内容。主要披露包括但不限于以下文件：</p>
						<p>1、本期产品说明书、风险申明书；</p>
						<p>2、温金中心规定的其他文件。</p>
						<p>(二)本期产品的兑付</p>
						<p>1、发行人的经营方针和经营范围的重大变化；</p>
						<p>2、发行人的重大投资行为和重大购置财产决定；</p>
						<p>3、发行人发生重大债务和未能清偿到期债务的违约情况；</p>
						<p>4、发行人资产、负债及对外担保发生重大变化的情况；</p>
						<p>5、发行人做出股东转变、解散及清算的决定；</p>
						<p>6、发行人涉及重大诉讼、仲裁事项或受到重大行政处罚；</p>
						<p>7、发行人涉嫌犯罪被司法机关立案调查，股东、高级管理人员、负责人涉及重大民事或刑事诉讼，或已就重大经济事件接受有关部门调查；</p>
						<p>8、温金中心规定的其他重大事项。</p>
						<p></p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
			<p style="text-align:center">第六部分 法律适用及争议解决机制</p>
				<div>
					<div class="contract_item_content">
						<p>本期产品说明书及其他相关文件受中国法律管辖，并按中国法律解释。</p>
						<p>发行人未按约定偿付本期产品本金及收益，或发生其他违约情况时。发行人应承担违约责任，其承担的违约责任范围包括持有人依法享有的本期产品本金及收益、违约金、损害赔偿金和实现债权的费用和其他应支付的费用。持有人有权直接依法向发行人进行追索。</p>
						<p>凡因本期产品的发行、认购、兑付等事项引起的或与本期产品有关的任何争议，应首先通过协商解决。如果在接到要求解决争议的书面通知之日起第30日内仍不能通过协商解决争议，任何一方可向被告所在地人民法院提起诉讼。</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
			<p style="text-align:center">第七部分 备查文件</p>
				<div>
					<p class="contract_item_title">一、备查文件</p>
					<div class="contract_item_content">
						<p>发行人挂牌登记申请书</p>
						<p>温金中心接受申请通知书</p>
						<p>温金中心挂牌登记及服务协议</p>
						<p>产品法律意见书</p>
						<p>发行人声明</p>
						<p>发行人股东会决议</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
				<div>
					<p class="contract_item_title">二、查阅地点</p>
					<div class="contract_item_content">
						<p>在本期产品存续期内，投资者可以至登记备案机构处查阅本期产品说明书全文及上述备查文件。</p>
						<p>名称：温州金融资产交易中心股份有限公司</p>
						<p>联系地址：温州市鹿城区小南路205号</p>
						<p>联系人：刘轲</p>
						<p>电话号码：0577-8550 2370</p>
					</div>
				</div>
			</div>
			<!--item end-->
			<!--item begin-->			
			<div class="contract_item bg_white">
			<p>附件一：</p>
				<div>
					<p class="contract_item_title">本期基础资产明细信息表</p>
					<div class="contract_item_content">
						<p>票号</p>
						<p>票面金额</p>
						<p>出票银行</p>
						<p>到期日</p>
					</div>
				</div>
			</div>
			<!--item end-->
		</div>
	</body>
</html>