/*
window.onload=function(){
	var $askNo = $(".findPos");
	var time = 500;
	//点击下一步上一步
	$(".askFor").on("click",function(){
		var $parDiv1 = $(this).parent().parent();
		var $index1 = $parDiv1.index();
		$parDiv1.hide(time).prev().show(time).siblings().removeClass("show").addClass("hidden");
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
				$parDiv.hide(time).next().show(time).siblings().removeClass("show").addClass("hidden");
				$askNo.eq($index+1).addClass("askCurPos");//当前位置显示
			})			
		}
		//ajax 统计用户选择了哪一个题目
		var answer = "";
		var answerIndex = $(this).index();
		var quesIndex = $(this).prev().val();
		if(answerIndex==0){
			answer = '';
		}
		
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
	//关闭当前位置
	 $(".askFin").on("click",function(){
		 $(".eva-questions").hide();
		 $(".eva-outcome").show()
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
*/