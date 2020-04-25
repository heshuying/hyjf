<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page session="false"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no"/>
		<link rel="stylesheet" type="text/css" href="${ctx}/css/page.css"/>
		<title>风险测评</title>
	</head>
	<body class="eavBody">
	<input type="hidden" name="jumpcommend" id="jumpcommend" value="${jumpcommend}" />
	<input type="hidden" name="platform" id="platform" value= "${platform}"/>
	<input type="hidden" name="sign" id="sign" value= "${sign}"/>
	<input type="hidden" name="behaviorId" id="behaviorId" value= ""/>
		<div class="evaluationPopMain">
			<div class="evaluationPopBg"></div>
			<div class="evaluationPop">
				<p></p>
				<div>我知道了</div>
			</div>
		</div>
	<input type="hidden" name="sendResult" id="sendResult"/>
		<div class="container specialFont response">
			<div class="eva-index">
				<div class="eva-index-pic">
					<img src="${ctx}/img/evaPic.png"/>
				</div>
				<p class="eva-index-text">通过问题的调研，我们会给您提供最专业的金融服务</p>
				<p class="eva-index-text">${couponResult}</p>
				<div class="process">
					<div class="process_line btn_bg_color askStartIndex"><a href="#" >开始测评</a></div>
				</div>
			</div>
			<div class="eva-questions hide" >
				<!--当前位置 圆-->
				<div class="askPos" >
					<div class="findPos askCurPos" id="askPos" name="askPos">
						<div class="askPosNo ">1</div>
						<span></span>
					</div>
					
					<div class="findPos">
						<div class="askPosNo">2</div>
					</div>
				</div>
				<!--问题开始-->
				<div class="eva-question">
					<div class="queryDiv" >
						<form action="" class="eva-form">
							<!--问题列表1开始-->
							<div class="queryItem queryItemFirst" >
								
								<div class="askBotDiv">
										<div class="askNext askFirstNext"><a href="#askPos">下一页</a></div>
									</div>
							</div>
							
							<!--问题列表2开始-->
							<div class="queryItem hide">
								
								<div class="askJump askBotDiv">
									<div class="askFor fl"><a href="#askPos">上一页</a></div><div class="askNext fr askFin">完成</div>
								</div>
							</div>
						</form>
					</div>
				</div>
			</div>
			<div class="eva-outcome hide">
				<p class="eva-outcome-text">你的测评结果为:</p>
				<div class="eva-outcome-type">
				</div>
				<p class="eva-index-text"></p>
				<p class="eva-coupon-text"></p>
				<div class="process">
					<div class="process_line btn_bg_color askStartIndex eva-question-reset"><a href="#" >重新测评</a></div>
					<div class="process_line btn_bg_color askStartIndex"><a href="#" class="hy-jumpInvest">马上赚钱</a></div>
				</div>
			</div>
		</div>
		<script type="text/javascript" src="${ctx}/js/jquery.min.js" ></script>
		<script src="${ctx}/js/common.js" type="text/javascript" charset="utf-8"></script>
		<script src="${ctx}/js/hyjf.js" type="text/javascript" charset="utf-8"></script>
		<script>
		window.onload=function(){
	 		$.ajax({
					type:"post",
			 		url:"/hyjf-app/financialAdvisor/questionnaireInit",
					data:"sign=${sign}&platform=${platform}",
					async:false,
					datatype:"json",
					success:function(data){
						var data=JSON.parse(data);
						//开始测评或者再测一次
						/* $("#behaviorId").val(data.behaviorIds); */
						if(data.ifEvaluation==0){
							$(".eva-index").show();
							$(".eva-outcome").hide();
						}else{
							$(".eva-index").hide();
							$(".eva-outcome").show();
							$(".eva-outcome-type").text(data.userEvalationResult.type);	
							$(".eva-index-text").text(data.userEvalationResult.summary);	
						}
						var queryItems = $(".queryItem");
						for(var j=0;j<$(".queryItem").length;j++){//循环 queryItem 
							var obj;
							for(var i=j*4;i<j*4+4;i++){//循环问题,每页4个问题
									var str="";
									var strCell="";//问题内容+标题
									var strInput = "";//问题内容
									var answersStr= ["D", "C", "B","A"];
									for(var k=0;k<data.questionList[i].answers.length;k++){
										var quesId_Id = data.questionList[i].answers[k].questionId+"_"+data.questionList[i].answers[k].id;
										var quesId_Index = data.questionList[i].answers[k].questionId+"_"+answersStr[k];
										var quesId = data.questionList[i].answers[k].questionId;
										var quesAnswer = data.questionList[i].answers[k].answer;
										strInput = '<input type="radio" name="'+'ask'+quesId+'" id="'+quesId_Id+'" value="'+quesId_Id+'" data-answer="'+quesId_Index+'"/><label for="'+quesId_Id+'">'+quesAnswer+'</label><br />'+strInput;										
									}
									strCell = '<div class="queryCell"><p>'+data.questionList[i].question+"</p>"+strInput+'</div>'+strCell;
									var j=parseInt(j);
									$(".queryItem").eq(j).find(".askBotDiv").before(strCell);//添加queryCell
									obj=data.questionList[i];
							}
						}
					} 		
			}) 	

	//发送答案			
	$(".askFin").on("click",function(){
				var $items = $(".queryCell");
				var userAnswer="";
				var $cellSelected = $(".cellSelected");
				for(var i=0;i<$items.length;i++){
					var userAnswer = $items.eq(i).find("input:checked").val()+","+userAnswer;
				}
				//如果打完 发送答案
				if($cellSelected.length === $items.length){
					 $.ajax({
							type:"post",
					 		url:"/hyjf-app/financialAdvisor/evaluationResult",
							data:{"userAnswer":userAnswer,"sign":"${sign}","behaviorId":$("#behaviorId").val(),"platform":$("#platform").val()},
							datatype:"json",
							success:function(data){
								data=JSON.parse(data);
								$(".eva-outcome-type").text(data.userEvalationResult.typeString);	
								$(".eva-index-text").text(data.userEvalationResult.summary);
								/* $("#sendResult").val(data.sendResult);  */
								/* $(".eva-form").children("label").removeClass("cellSelected");
								$("input[type='radio']").attr('checked','false'); */
								if(data.sendResult!=null){
									var html = "<p>"+data.sendResult+"</p>"
									$(".evaluationPopMain").show();
									$(".evaluationPop div").before(html);
									$(".eva-outcome").show();
									
									setupWebViewJavascriptBridge(function(bridge) {
										/* 直接调用更新用户信息原生方法 */
										bridge.callHandler('reloadUserData', function responseCallback(responseData) {
											console.log("JS received response:", responseData)
										})
									})
									
									$(".evaluationPop div").on("click",function(){
										location.reload();
									})
								}else{
									location.reload();
								}
							} 		
					}) 
				}
			})
		
			var $askNo = $(".findPos");
			var time = 300;
			//点击下一步上一步
			$(".askFor").on("click",function(){
				var $parDiv1 = $(this).parent().parent();
				var $index1 = $parDiv1.index();
				$parDiv1.hide(time).prev().show(time).siblings().removeClass("show").addClass("hide");
				//当前位置显示
				$askNo.eq($index1-1).addClass("askCurPos");
				$askNo.eq($index1).removeClass("askCurPos");
			})
			//增加选中颜色
			$(".queryItem label").on("click",function(){
				$(this).addClass("cellSelected").siblings().removeClass("cellSelected");
				var $parDiv = $(this).parent().parent();//queryItem
				var $parCell = $(this).parent();
				var $next = $parDiv.find(".askNext");
				var checkSelectedLength = $parDiv.find(".cellSelected").length;
				var cellLength = $parDiv.find(".queryCell").length;
				//判断如果题目全选 可以点击下一页
				if(checkSelectedLength == cellLength){
					$next.addClass("askNextAct");
					$next.on("click",function(){
						$index = $parDiv.index();
						$parDiv.hide(time).next().show(time).siblings().removeClass("show").addClass("hide");
						$askNo.eq($index+1).addClass("askCurPos");//当前位置显示
						window.scrollTo(0,0)
						if($index===2){
							$(".eva-questions").hide();
							$(".eva-outcome").show();
						}
					})			
				}
				//ajax 统计用户选择了哪一个题目
		/*		var answer = "";
				var answerIndex = $(this).index();
				var quesIndex = $(this).prev().val();
				if(answerIndex==0){
					answer = '';
				}*/
				
				var behavior = $(this).prev().data("answer");
				$.ajax({
					type:"post",
				 		url:"/hyjf-app/financialAdvisor/userEvaluationBehavior",
						data:{"behavior":behavior,"behaviorId":$("#behaviorId").val()},
						async:true,
						datatype:"json"
				})
				
			})
			$(".askStartIndex").on("click",function(){
				$(".eva-index").hide(time);
				$(".eva-questions").show(time)
			})
			//关闭引导页
			$(".askStartClose").on("click",function(){
				$(".queryPop").remove();
			})
			//重新测评
			 $(".eva-question-reset").on("click",function(){
				 $(".eva-outcome").hide(time);
				 $(".askPos,.queryItemFirst,.eva-questions").show(time);
			 })
			 //点击开始测评 重新测评获取behaviorId
			 $(".askStartIndex,eva-question-reset").on("click",function(){
				 $.ajax({
					type:"post",
				 		url:"/hyjf-app/financialAdvisor/startUserEvaluationBehavior",
						data:{"sign":$("#sign").val(),"platform":$("#platform").val()},
						async:true,
						datatype:"json",
						success:function(data){
							var data=JSON.parse(data);
							var $behaviorId = data.behaviorId;
							$("#behaviorId").val($behaviorId);
						} 
				})
			 })
		}

		</script>
	</body>
	<script src="${ctx}/js/evaluation.js" type="text/javascript" charset="utf-8"></script>
</html>
