<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
<style>
    	.activity-2018-02{
    		width:100%;
    		float:left;
    	}
    	.section-1{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-1.jpg') center 0 no-repeat;
    		height: 809px;
    		width:100%;
    	}
    	.section-2{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-2.jpg') center 0 no-repeat;
    		height: 612px;
    		width:100%;
    		padding-top: 496px;
    	}
    	.section-2.logged{
    		height:481px;
    	}
    	.section-3{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-3.jpg') center 0 no-repeat;
    		height: 956px;
    		width:100%;
    	}
    	.section-3-2{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-3-2.jpg') center 0 no-repeat;
    		height: 956px;
    		width:100%;
    	}
    	.section-4{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-4.jpg') center 0 no-repeat;
    		height: 918px;
    		width:100%;
    	}
    	.section-5{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-5.jpg') center 0 no-repeat;
    		height: 689px;
    		width:100%;
    	}
    	.section-6{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-6.jpg') center 0 no-repeat;
    		height: 858px;
    		width:100%;
    	}
    	.section-7{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-7.jpg') center 0 no-repeat;
    		height: 710px;
    		width:100%;
    	}
    	.section-8{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-8.jpg') center 0 no-repeat;
    		height: 804px;
    		width:100%;
    	}
    	.section-9{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-9.jpg') center 0 no-repeat;
    		height: 914px;
    		width:100%;
    	}
    	.section-10{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-10.jpg') center 0 no-repeat;
    		height: 178px;
    		width:100%;
    	}
    	.section-11{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-11.jpg') center 0 no-repeat;
    		height: 571px;
    		width:100%;
    		padding-top: 121px;
    	}
    	.section-12{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-201802-12.jpg') center 0 no-repeat;
    		height: 310px;
    		width:100%;
    	}
    	.itembg{
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/itembg.png') center 0 no-repeat;
    		height: 321px;
    		width:1002px;
    		margin-left: auto;
    		margin-right: auto;
    	}
    	.active-btn1{
			background: url('${ctx}/dist/images/active/activity2018/activity201802/activity-btn-1.png') center 0 no-repeat;
    		height: 85px;
    		width: 526px;
    		margin-left: auto;
    		margin-right: auto;
    		display: block;
    		text-decoration: none;
    		border:0;
    	}
    	.go-invest{
			background: url('${ctx}/dist/images/active/activity2018/activity201802/go-invest.png') center 0 no-repeat;
    		height: 73px;
    		width: 252px;
    		margin-left: auto;
    		margin-right: auto;
    		display: block;
    		text-decoration: none;
    		border:0;
    	}
    	.itembg .total{
    		width:275px;
    		height: 150px;
    		display: inline-block;
    		text-align: center;
    		font-size: 30px;
    		color: #eb122c;
    		line-height: 90px;
    		padding-top: 76px;
    		padding-left: 72px;
    	}
    	.itembg .total span{
    		font-size: 90px;
    	}
    	.itembg .sum{
    		width:700px;
    		height: 150px;
    		display: inline-block;
    		text-align: center;
    		font-size: 36px;
    		color: #333333;
    		padding-top: 76px;
    		padding-left: 42px;
    	}
    	.itembg .sum span{
    		font-size: 48px;
    		color: #eb122c;
    	}
    	.calc-wrap{
    		width: 800px;
    		margin-left: auto;
    		margin-right: auto;
    	}
    	.calc-bg{
    		width: 506px;
    		height: 74px;
    		display: inline-block;
    		padding-left: 70px;
    		padding-top: 2px;
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/calc.png') center 0 no-repeat;
    		vertical-align: top;
    	}
    	.calc-result{
    		width: 286px;
    		height: 74px;
    		line-height: 60px;
    		display: inline-block;
    		padding-left: 10px;
    		padding-top: 2px;
    		vertical-align: top;
    		font-size: 24px;
    		white-space: nowrap;
    	}
    	.calc-result span{
    		font-size: 48px;
    		color: #eb122c;
    	}
    	.calc-result b{
    		font-size: 36px;
    	}
    	.calc-bg input{
    		display: inline-block;
    		width: 250px;
    		height: 74px;
    		line-height: 70px;
    		background: none;
    		border:none;
    		outline: none;
    		font-size: 24px;
    	}
    	.calc-btn{
    		display: inline-block;
    		width: 120px;
    		margin-left: 58px;
    		height: 72px;
    		background: url('${ctx}/dist/images/active/activity2018/activity201802/calc.png') right 2px no-repeat;
    	}
    	.info{
    		color: #333333;
    		font-size: 18px;
    		line-height: 24px;
    		width:800px;
    		margin-left: auto;
    		margin-right: auto;
    		padding:20px 0 40px;
    	}
    	.info span{
    		color: #eb122c;
    		font-size: 24px;
    	}
    	.info span.imp{
    		font-size: 18px;
    		margin-left: -13px;
    	}
    </style>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	<article class="activity-2018-02">
			<section class="section-1"></section>

			<section class="section-2 notLog" style="display:none">
				<a href="${ctx}/user/login/init.do" class="active-btn1"></a>
			</section>
			
			<section class="section-2 logged" style="display:none">
			</section>
			<section class="section-11" style="display:none">
				<div class="itembg">
					<div class="total"><span id="useable">0</span>张</div>
					<div class="sum">已使用<span id="used">0</span>张  累计获得<span id="total">0</span>张</div>
				</div>
			</section>
			<section class="section-12" style="display:none">
				<div class="calc-wrap">
					<div class="calc-bg">
						<input type="text" placeholder="输入预期投资金额" id="price" maxlength="9" onkeyup="value=value.replace(/[^\d]/g,'')" />
						<a href="javascript:;" id="btn" class="calc-btn"> </a>
					</div>
					<div class="calc-result">
						<b> = </b> 
						可获得金彩片数<span id="count">0</span>张
					</div>
					
				</div>
				<div class="info">
					<span class="imp">* </span>1. 因满足“单笔投资每满10000元”的条件  获得<span id="part1">0</span>张；  <br/>
         			2. 因满足“新增充值且投资金额每满10000元”的条件 获得<span id="part2">0</span>张。
				</div>
				<a href="${ctx}/bank/web/borrow/initBorrowList.do" class="go-invest"></a>
			</section>
			<section class="section-3" style="display:none"></section>
			<section class="section-3-2" style="display:none"></section>
			<section class="section-4"></section>
			<section class="section-5"></section>
			<section class="section-6"></section>
			<section class="section-7"></section>
			<section class="section-8"></section>
			<section class="section-9"></section>
			<section class="section-10"></section>
		</article>
		<!-- 用户账户余额为X、 -->
		<input type="hidden" id="blance" value="">

		<!-- 新增充值金额为Y -->
		<input type="hidden" id="recharge" value="">

		<!-- 新增投资余额为Z -->
		<input type="hidden" id="investRest" value="">
		<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
		<script>
		
		//用户输入的投资金额为A、
		// 碎片数为B1
		// 用户账户余额为X、
		// 新增充值金额为Y（活动期内 +充值、- 提现、- 投资金额）、
		// 新增投资余额为Z（新增充值投资不满万的余值）

		// 计算金彩碎片
		// 账户余额
		var X = parseInt(document.getElementById('blance').value) || 0; 
		// 新增充值金额
		var Y = parseInt(document.getElementById('recharge').value) || 0; 
		// 新增充值金额
		var Z = parseInt(document.getElementById('investRest').value) || 0; 
		$.ajax({
			url:"${ctx}/act2018/select.do",
			type:'GET',
			dataType : 'json',
			success:function(res){
				if(res.status=='true'){
					$('#useable').html(res.availableNumber);
					$('#used').html(res.totalNumber-res.availableNumber);
					$('#total').html(res.totalNumber);
					$('.section-3-2').show();
					$('.section-11').show();
					$('.section-12').show();
					$('.section-2.logged').show();
					X = parseFloat(res.balance);
					Y = parseFloat(res.newRecharge);
					Z = parseFloat(res.newInvestment);
				}else{
					$('.section-3').show();
					$('.section-2.notLog').show();
				}
			}
		})
		var btn = document.getElementById('btn'); 
		var part1DOM = document.getElementById('part1'); 
		var part2DOM = document.getElementById('part2'); 
		var countDOM = document.getElementById('count'); 
		
		btn.onclick = function(){
			// 投资金额
			var A = parseInt(document.getElementById('price').value) || 0
			var part1Val = part1(A)
			var part2Val = part2(A,X,Y,Z)
			
			// 计算part1
			part1DOM.innerHTML = part1Val;
			// 计算part2
			part2DOM.innerHTML = part2Val;
			// 计算总张数
			countDOM.innerHTML = part1Val+part2Val
		}
		// 单笔投资每满10000元 获得张数
		function part1(a){
			return parseInt(a/10000);
		}
		// 新增充值且投资金额每满10000元 获得张数
		function part2(a,x,y,z){
			if(a<y+z){
				return parseInt((a+z)/10000);
			}else if(a==y+z){
				return parseInt(a/10000);
			}else if(a>y+z&&(a<x||a==x)){
				if(y+z<0){
					return 0
				}else{
					return parseInt((y+z)/10000);
				}
			}else if(a>y+z&&a>x){
				if(a-(x-y)+z<0){
					return 0
				}else{
					return parseInt((a-(x-y)+z)/10000);
					
				}
				
			}
		}
		</script>
	
</body>
</html>