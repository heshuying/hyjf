$(document).ready(
	function() {
        
        //获取相应的计划列表
		getPlanListPage();
		//获取计划列表函数
		function getPlanListPage() {
			$("#paginatorPage").val(1);
			$("#pageSize").val(pageSize);
			$("#planList").html(utils.loadingHTML);
			doRequest(
				planPageSuccessCallback,
				planPageErrorCallback, 
				webPath+ "/hjhplan/getPlanList.do",
				$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		
		
		//获取计划列表成功回调
		function planPageSuccessCallback(data) {
			var planList = data.planList;//就是 PlanListAJaxBean 的 变量list
			// 挂载数据
			var planListStr = "";
			if(planList.length==0){
				planListStr = planListStr 
				+'<div class="data-empty"> <div class="empty-icon"></div> <p class="align-center">暂无投资项目...</p> </div>'
				$("#planList").html(planListStr);
				$("#new-pagination").hide();
			}else{
				for (var i = 0; i < planList.length; i++) {
					var plan = planList[i];
					var periodUnit = (plan.isMonth=='0'?"天":"个月");
					var jxs=''
//					if(plan.isIncrease == true){
//						jxs='<p><em class="e1">' + plan.planApr + '</em> %+'+plan.borrowExtraYield+'%</p>'
//					}else{
					jxs='<p><em class="e1">' + plan.planApr + '</em> %</p>'
					//}
					if(plan.status == 1 && plan.statusName != '稍后开启'){
						planListStr = planListStr 
						+ '<li>'
						+	 '<a href="'+webPath+'/hjhdetail/getPlanDetail.do?planNid='+plan.planNid+'">'
						+	 '<p class="name">' + plan.planName + '</p>'
						+	 '<div class="info">'
						+		'<div class="if1">'
						+			'<p><em class="e1">' + plan.planApr + '</em> %</p>'
						+			'<span>历史年回报率</span>'
						+		'</div>'
						+		'<span class="rule"></span>'
						+		'<div class="if2">'
						+			'<p><em>' + plan.planPeriod + '</em>' + periodUnit + '</p>'
						+			'<span>锁定期限</span>'
						+		'</div>'
						+		'<span class="rule"></span>'
						+		'<div class="if3">'
						+			'<p><em>' + plan.availableInvestAccount + '</em> 元</p>'
						+			'<span>开放额度</span>'
						+		'</div>'
						+	'</div>'
						+	'<em class="join">' + plan.statusName + '</em>'
						+	'</a>'
						+ '</li>';
					}else {
						planListStr = planListStr 
						+ '<li>'
						+	 '<a href="'+webPath+'/hjhdetail/getPlanDetail.do?planNid='+plan.planNid+'">'
						+	 '<p class="name">' + plan.planName + '</p>'
						+	 '<div class="info">'
						+		'<div class="if1">'
						+			'<p><em class="e1">' + plan.planApr + '</em> %</p>'
						+			'<span>历史年回报率</span>'
						+		'</div>'
						+		'<span class="rule"></span>'
						+		'<div class="if2">'
						+			'<p><em>' + plan.planPeriod + '</em>' + periodUnit + '</p>'
						+			'<span>锁定期限</span>'
						+		'</div>'
						+		'<span class="rule"></span>'
						+		'<div class="if3">'
						+			'<p><em>' + plan.availableInvestAccount + '</em> 元</p>'
						+			'<span>开放额度</span>'
						+		'</div>'
						+	'</div>'
						+	'<span class="join">稍后开启</span>'
						+	'</a>'
						+ '</li>';
					}
				
				}
				$("#new-pagination").show();
				$("#planList").html(planListStr);
				$(".timeout").each(function() {
				    var ele = $(this),
				        s = parseInt(ele.data("end")) - parseInt(ele.data("start"));
				    utils.timer(ele, s);
				})
			}
		}
	
		//获取计划列表失败
		function planPageErrorCallback(data) {
	
		}
		$(document).on("click", ".flipClass", function() {
			flip($(this).data("page"));
		});
	
		//分页按钮
		function flip(paginatorPage) {
			$("#paginatorPage").val(paginatorPage);
			$("#pageSize").val(pageSize);
			$("#planList").html(utils.loadingHTML);
			doRequest(
				planPageSuccessCallback,
				planPageErrorCallback, 
				webPath+ "/hjhplan/getPlanList.do", 
				$("#listForm").serialize(), true,"flipClass","new-pagination");
			scrollTo();
		}
		function scrollTop(speed){
			$("html,body").animate({
	            scrollTop: 0
	        }, speed);
		}
		
        
	}
);


