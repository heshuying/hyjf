<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>会员管理 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<div class="user-detail">
        <jsp:include page="/subMenu.jsp"></jsp:include>
        <div class="vip-level-box">
            <div class="vip-title">我的会员等级
            <label style="font-size:16px;">&nbsp;&nbsp;有效期至：<c:out value="${vipExpDate}"></c:out></label>
            </div>
            
            <div class="level-bg">
                <div class="level-mark"></div>
                <div class="level-bar"></div>
                <ul>
                    <c:forEach items="${levelList}" var="level">
                    	<li>${level.vipName} <br>${level.vipValue}</li>
                    </c:forEach>
                </ul>
            </div>

        </div>
    </div>
    <div class="vip-privilege">
        <div class="title"></div>
        <div class="container-1200">
            <ul>
                <li style="background-image: url(${cdn }/img/vip_pri_1.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_2.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_3.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_4.png);"></li>
            </ul>
        </div>
    </div>
    <div class="vip-privilege-disabled">
        <div class="title">更多精彩, 敬请期待...</div>
        <div class="container-1200">
            <ul>
                <li style="background-image: url(${cdn }/img/vip_pri_disable_1.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_disable_2.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_disable_3.png);"></li>
                <li style="background-image: url(${cdn }/img/vip_pri_disable_4.png);"></li>
            </ul>
        </div>
    </div>
    <div class="vip-rules">
        <div class="inner">
            <div class="title">成长值计算规则</div>
           	
            <div class="table">
            	成长值=投资金额×成长系数 
            	<table width="100%" cellpadding="0" cellspacing="0" border="0">
            		<tr>
            			<th>标期</th>
            			<td>1个月</td>
            			<td>2个月</td>
            			<td>3个月</td>
            			<td>4个月</td>
            			<td>5个月</td>
            			<td>6个月</td>
            			<td>12个月</td>
            		</tr>
            		<tr>
            			<th>成长系数</th>
            			<td>0.10</td>
            			<td>0.30</td>
            			<td>0.50</td>
            			<td>0.70</td>
            			<td>0.90</td>
            			<td>1.10</td>
            			<td>2.30</td>
            		</tr>
            	</table>
            	仅汇直投项目投资金额参与成长值计算 <br/>
            </div>
			
		
        </div>
    </div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>
		setActById('vipManage');
		function getLevel(num){
	        var l = __getL(num);//获取等级
	        return __getW(num,l);
	        //计算宽度
	        function __getW(num,level){
	            if(level === "max"){
	                return "100%";
	            }
	            var levelw = 176;//每个等级宽度
	            var lv = level[0];
	            var min = level[1];
	            var max = level[2];
	            return levelw*(lv-1)+((num-min)/max)*levelw;
	        }
	
	        //获取等级
	        function __getL(num){
	            var level = {};//记录等级
	            if(num >= parseInt('${level1.vipValue}') && num <parseInt('${level2.vipValue}')){
	                level = [1,parseInt('${level1.vipValue}'),parseInt('${level2.vipValue}')];
	            }else if(num >=parseInt('${level2.vipValue}') && num<parseInt('${level3.vipValue}')){
	                level = [2,parseInt('${level2.vipValue}'),parseInt('${level3.vipValue}')];
	            }else if(num >=parseInt('${level3.vipValue}') && num<parseInt('${level4.vipValue}')){
	                level = [3,parseInt('${level3.vipValue}'),parseInt('${level4.vipValue}')];
	            }else if(num >=parseInt('${level4.vipValue}') && num<parseInt('${level5.vipValue}')){
	                level = [4,parseInt('${level4.vipValue}'),parseInt('${level5.vipValue}')];
	            }else if(num >=parseInt('${level5.vipValue}') && num<parseInt('${level6.vipValue}')){
	                level = [5,parseInt('${level5.vipValue}'),parseInt('${level6.vipValue}')];
	            }else if(num >=parseInt('${level6.vipValue}') && num<parseInt('${level6.vipValue}')*10){
	                level = [6,parseInt('${level6.vipValue}'),parseInt('${level6.vipValue}')*10];
	            }else{
	                level = "max";
	            }
	            return level;
	        }
	    }
	    function playLevel(num){
	        $(".level-bar").animate({"width":getLevel(num)},200);
	        $(".level-mark").animate({"left":getLevel(num)},200).html("当前成长值 <br> "+num);
	    }



        //渲染会员等级    !!!!!!!!!!!!! java只需执行下面方法，传入会员积分 !!!!!!!!!!!!
        playLevel('${sumValue}');
    </script>
	</body>
</html>