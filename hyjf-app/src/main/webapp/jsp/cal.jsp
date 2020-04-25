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
		<link rel="stylesheet" href="${ctx}/css/font-awesome.min.css">
		<title>收益计算器</title>
</head>
<body class="bg_grey">
		<div class="activeOutdatePop"></div>
		<div class="specialFont  cal-main">
                <section class="new-form-item  bg_white">
                	<div>
                		<label for="Tel" class="cal-icon-1">历史年回报率(%)</label>
                    	<input type="number" name="tel" maxlength="11" class="new-form-input" id="percent"  autocomplete="off"  onpaste="return false"/>
               		</div>
               		<div>
               			<label for="Money" class="cal-icon-2">理财期限(<span>月</span>)</label>
                    	<input type="tel" name="money" maxlength="3" class="new-form-input" id="month"  oninput="if(value.length>3)value=value.slice(0,3)" autocomplete="off" onpaste="return false"/>
               		</div>
                </section>
                <section class="new-form-item item-1 mr20 bg_white">
                	<div>
	                    <label class="jisuanqi-adjust-label cal-icon-3">还款方式</label>
                        <select for="Gender" id="ForGender">
                            <!-- value为选项的值 -->
                            <option value="0">按月计息，到期还本付息</option>
                            <option value="1">按天计息，到期还本付息</option>
                            <option value="2">先息后本</option>
                            <option value="3">等额本息</option>
                            <option value="4">等额本金</option>
                        </select>
                    </div>
                    <div>
                    	<label for="investMoney" class="jisuanqi-adjust-label cal-icon-4">投资金额(元)</label>
                    	<input type="tel" name="investmoney" maxlength="9" placeholder="请输入投资金额" class="new-form-input" id="investMoney"  autocomplete="off" oninput="if(value.length>9)value=value.slice(0,9)"/  onpaste="return false">
                    </div>
                </section>
                <div class="process_line btn_bg_color new-form-btn cal-btn" ><a href="#" id="formSubmit">开始计算</a></div>
                 <p class="calc-txt text_grey">注：计算收益仅供参考，最终收益以实际到账为准</p>
                <div class="jisuanqi-outcome bg_white">
	                <div class="jisuanqi-outcome-title hide">
			        	<div>
			        		<span>本息结果：</span>
			        		<p class="outcomeTotal"><i>0</i>元</p>
			        	</div>
			        	<div>
			        		<span>收益总计：</span>
			        		<p class="interst"><i>0</i>元</p>
			        	</div>
			        	<i class="clearfix"></i>
			        </div>
			        <div class="jisuanqi-table hide">
			        	<p>收益明细：</p>
			        	<div>
			        	<table border="0" cellspacing="0" cellpadding="0">
	                        	<!--表头-->
	                        	<tr class="invite-table-header bg-grey">
	                        		<td>还款期次</td>
	                        		<td>本息(元)</td>
	                        		<td>本金(元)</td>
	                        		<td>利息(元)</td>
	                        	</tr>
	                        	<!--表头结束-->
                        </table>
                        </div>
			        </div>
		        </div>
		</div>
	<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
	 <script>
        function popupWin(msg){
            $('.settlement_mask').fadeIn();
            $('.js_zr').fadeIn();
            $("#popmsg").text(msg);
        }
        function popoutWin(){
            $('.settlement_mask').fadeOut();
            $('.js_zr').fadeOut();
        }
    </script>
    <script>
    //小数不足两位补零
	function fixNum(f_x){
		var s_x = f_x.toString();
		var pos_decimal = s_x.indexOf('.');
		if(pos_decimal < 0){
			pos_decimal = s_x.length;
			s_x += '.';
		}
		while (s_x.length <= pos_decimal + 2){
			s_x += '0';
		}
		return formatNum(s_x);
	}
    Number.prototype.toFixed2 = function() {
		return parseFloat(this.toString().replace(/(\.\d{2})\d+$/, "$1"));
	}
	Number.prototype.toFixed4 = function() {
		return parseFloat(this.toString().replace(/(\.\d{4})\d+$/, "$1"));
	}
	$( function(){
	$("#yearRate").val( window.loadRate );
		//计算期次
		function getTerm(){
			return parseInt(years.val() || "0") * 12 + parseInt(month.val() || "0");
		}		
		// x ^ y
		function power( x, y ){
			var t = x;
			while( y-- > 1 ){
				t *= x;
			}
			return t;
		}
	})
	//计算器开始
	//设置初始数据
	function GetQueryString(name){
	     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
	     var r = window.location.search.substr(1).match(reg);
	     if(r!=null)return  unescape(r[2]); return null;
	}
	var calPercent = GetQueryString("percent");
	var calTime = GetQueryString("time");
	var calType = GetQueryString("calType");
	$("#ForGender").val(calType);
	$("#percent").val(calPercent);
	$("#month").val(calTime);
	
	//错误提示
	function showError(massage){
		$(".activeOutdatePop").text(massage)
		$(".activeOutdatePop").show().delay(3000).fadeOut();
	}
	//按日计算时改月为天
	$("#ForGender").change(function(){
		var type = $(this).prop("value");
		if(type == 1){
			$(".cal-icon-2 span").text("天")
		}else{
			$(".cal-icon-2 span").text("月")
		}
	})
	//投资金额 输入框输入控制
	$("#investMoney").keyup(function(){
		var _self = $(this);
		var val = _self.val();
		_self.val(val.replace(/[^0-9]*$/g,''));
	})
	//投资期限 输入框输入控制
	$("#month").keyup(function(){
		var _self = $(this);
		var val = _self.val();
		_self.val(val.replace(/[^0-9]*$/g,''));
	})
	//收益率 输入框输入控制
	$("#percent").keyup(function(){
		var _self = $(this);
		var val = _self.val();
		_self.val(val.replace(/[^0-9.]*$/g,''));
		if(val.indexOf(".")>-1){
		    var aNew;
		    var re = /([0-9]+\.[0-9]{2})[0-9]*/;
		    aNew = val.replace(re,"$1");
		    _self.val(aNew);
		}else{
			if(val.length>2){
				aNew=val.slice(0,2);
			_self.val(aNew);
			}
		}
	})
	
	//计算不同类型的收益
	$("#formSubmit").click(function(){
		if($("#investMoney").val().length == 0){
			showError("请填写投资金额");
			$("#investMoney").addClass("hyjf-border")
			return false;
		}else if($("#investMoney").val()==0){
			showError("请填写9位以内正整数金额");
			$("#investMoney").addClass("hyjf-border")
			return false;
		}else{
			$("#investMoney").removeClass("hyjf-border");
		}
		if($("#percent").val().length == 0){
			showError("请填写历史年回报率");
			$("#percent").addClass("hyjf-border")
			return false;
		}else{
			$("#percent").removeClass("hyjf-border");
		}
		/*if($("#percent").val().indexOf(".") == -1){
			if($("#percent").val().length >2 ){
			showError("年化收益率仅限小数点前后两位");
			$("#percent").addClass("hyjf-border")
			return false;
			}
		}*/
		if($("#month").val().length == 0){
			showError("请填写项目期限");
			$("#month").addClass("hyjf-border");
			return false;
		}else if($("#month").val() == 0){
			showError("项目期限仅限3位以内正整数");
			$("#month").addClass("hyjf-border");
			return false;
		}else{
			$("#month").removeClass("hyjf-border");
		}
		var val = parseInt($("#ForGender").val()),
			investMoney = parseInt($("#investMoney").val() || "0"),
			percent = parseFloat($("#percent").val() || "0")/100,
			month = parseInt($("#month").val() || "0");
		switch(val){
			case 0://按月计息
				var interest = parseFloat((investMoney*(percent/12)).toFixed4()*10000*month/10000);
				var outcomeTotal = parseFloat(investMoney+interest);
				$(".outcomeTotal i").text(fixNum(outcomeTotal.toFixed2()));
				$(".interst i").text(fixNum(interest.toFixed2()));//利息总计
				$(".jisuanqi-table").hide(200);
				$(".jisuanqi-table table tr:not(.invite-table-header)").remove();//删除除表头意外的tr
				$(".jisuanqi-outcome-title").slideDown(200);
				break;
			case 1://按天计息
				var interest = parseFloat((investMoney*percent/360).toFixed4()*10000*month/10000);
				var outcomeTotal = parseFloat(investMoney+interest);
				$(".outcomeTotal i").text(fixNum(outcomeTotal.toFixed2()));
				$(".interst i").text(fixNum(interest.toFixed2()));//利息总计
				$(".jisuanqi-table").hide(200);
				$(".jisuanqi-table table tr:not(.invite-table-header)").remove();
				$(".jisuanqi-outcome-title").slideDown(200);
				break;
			case 2://先息后本
				$(".jisuanqi-table table tr:not(.invite-table-header)").remove();
				var interest = parseFloat((investMoney*percent/12).toFixed4()*10000*month/10000);//利息总和
				var monthInterst = parseFloat(investMoney*percent/12);//每月利息
				var outcomeTotal = parseFloat(investMoney+interest);//本息总和
				var lastMonthTotal = parseFloat(monthInterst+investMoney);//最后一月
				$(".outcomeTotal i").text(fixNum(outcomeTotal.toFixed2()));
				$(".interst i").text(fixNum(interest.toFixed2()));//利息总计
				for(var i=1;i<month;i++){
					var str = "<tr><td>"+i+"</td><td>"+fixNum(monthInterst.toFixed2())+"</td><td>"+0+"</td><td>"+fixNum(monthInterst.toFixed2())+"</td></tr>";
					$(".jisuanqi-table table").append(str);
				}
				var str1 = "<tr><td>"+month+"</td><td>"+fixNum(lastMonthTotal.toFixed2())+"</td><td>"+fixNum(investMoney.toFixed2())+"</td><td>"+fixNum(monthInterst.toFixed2())+"</td></tr>";
				$(".jisuanqi-table table").append(str1);//最后一行
				$(".jisuanqi-outcome-title").slideDown(200);
				$(".jisuanqi-table").slideDown(200);
				break;
			case 3://等额本息
				$(".jisuanqi-table table tr:not(.invite-table-header)").remove();
				var monthPer = percent/12;//月利率
				var powM = Math.pow(1+monthPer,month); // (1+月利率)^投资月数
				//每月本息
				var monthMoneyInter = (investMoney*monthPer*Math.pow((1+monthPer),month)).toFixed4()*10000/(powM-1)/10000; 
				var total = (monthMoneyInter.toFixed2()*10000*month/10000).toFixed2();
				$(".outcomeTotal i").text(fixNum(total));
				for(var i=1;i<month+1;i++){
					//每月利息
					var monthInterst = ((investMoney*monthPer*(powM-Math.pow(1+monthPer,i-1)))/(powM-1)).toFixed2();
					//每月本金
					var monthMoney = parseFloat(investMoney*monthPer*Math.pow(1+monthPer,i-1)/(powM-1));
					var str = "<tr><td>"+i+"</td><td>"+fixNum(monthMoneyInter.toFixed2())+"</td><td>"+fixNum(monthMoney.toFixed2())+"</td><td>"+fixNum(monthInterst)+"</td></tr>";
					$(".jisuanqi-table table").append(str);
				}
				$(".interst i").text(fixNum((total-investMoney).toFixed2()));//利息总计
				$(".jisuanqi-outcome-title").slideDown(200);
				$(".jisuanqi-table").slideDown(200);
				break;
			case 4://等额本金
				$(".jisuanqi-table table tr:not(.invite-table-header)").remove();
				var monthPer = percent/12;//月利率
				//每月本金
				var monthMoney = investMoney/month;
				//利息总计
				var interest = 0;
				for(var i=1;i<month+1;i++){
					//每月本息
					var monthMoneyInter = monthMoney.toFixed2()+(investMoney-monthMoney.toFixed2()*(i-1))*monthPer;  
					//每月利息
					var monthInterst = (investMoney-monthMoney.toFixed2()*(i-1))*monthPer;
					var str = "<tr><td>"+i+"</td><td>"+fixNum(monthMoneyInter.toFixed2())+"</td><td>"+fixNum(monthMoney.toFixed2())+"</td><td>"+fixNum(monthInterst.toFixed2())+"</td></tr>";
					$(".jisuanqi-table table").append(str);
					interest+=monthInterst.toFixed2()*10000;
				}
				//利息总计
				// var interest = ((investMoney/month+investMoney*monthPer).toFixed4()*10000+investMoney.toFixed4()*10000/month*(1+monthPer))/10000/2*month-investMoney;
				interest = (interest/10000).toFixed2();


				$(".interst i").text(fixNum(interest.toFixed2()));//利息总计
				$(".outcomeTotal i").text(fixNum((interest+investMoney).toFixed2()));
				$(".jisuanqi-outcome-title").slideDown(200);
				$(".jisuanqi-table").slideDown(200);
				break;

		}
	});


	function formatNum(strNum) {
		//return strNum;
		if (strNum.length <= 3) {
			return strNum;
		}
		if (!/^(\+|-)?(\d+)(\.\d+)?$/.test(strNum)) {
			return strNum;
		}
		var a = RegExp.$1, b = RegExp.$2, c = RegExp.$3;
		var re = new RegExp();
		re.compile("(\\d)(\\d{3})(,|$)");
		while (re.test(b)) {
			b = b.replace(re, "$1,$2$3");
		}
		return a + "" + b + "" + c;
	}
    </script>
    </body>
</html>