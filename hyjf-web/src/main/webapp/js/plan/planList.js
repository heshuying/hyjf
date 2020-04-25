$(document).ready(
		//.ready 在文档加载后激活函数
	function() {
        //倒计时
        //#htjList 取值 planlist.jsp 里面的  htjList的值
        $("#htjList").delegate(".icon-info","mouseover",function(){ //当点击鼠标时，显示xxx元素：
            showInfo($(this));
        });
        $("#htjList").delegate(".icon-info","mouseleave",function(){//当点击鼠标时，隐藏xxx元素：
            hideInfo($(this));
        });
        function showInfo(obj){
            obj.children("em").fadeIn();
        }
        function hideInfo(obj){
            obj.children("em").hide();
        }
        
        
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
				webPath+ "/plan/getPlanList.do",
				$("#listForm").serialize(), true,"flipClass","new-pagination");
		}
		
		
		//获取计划列表成功回调
		function planPageSuccessCallback(data) {
			var planList = data.debtPlanList;//就是 PlanListAJaxBean 的 变量list
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
					planListStr = planListStr 
					+ '<ul>'
					+ '<a href="'+webPath+'/plan/getPlanDetail.do?planNid='+plan.planNid+'">'
					+ '<li class="pt1">'
					+ '	<div>'
					+plan.planName
					+ '	</div>'
					+ '</li>'
					
					+ '<li class="pt2">'
					+ '	<div class="yield">'
					+plan.planApr+'%'
					+ '	<span class="bg-add">'
					+ '	</span>'
					+ '	</div>'
					+ '</li>'
					
					+ '<li class="pt3">'
					+ '	<div>'
					+plan.planAccount+'元'
					+ '	</div>'
					+ '</li>'
					
					+ '<li class="pt4">'
					+ '	<div>'
					+ plan.planPeriod+'个月'
					+ '	</div>'
					+ '</li>'
					
					
					+ '<li class="pt5">'
					+ '	<div class="bond-num">'
					+ ' <div class="progress-all">'
					+ ' <div class="progress-cur" style="width:'+plan.planSchedule+'%"></div>'
					+ '	</div>'
					+ '	<div class="percent">'
					+ '<span>'
					+plan.planSchedule+'%'
					+ '</span>'
					+ '	</div>'
					+ '	</div>'
					+ '</li>'
					+ '<li class="pt6">'
					if(plan.status==0){				
						planListStr = planListStr
						+ '	<span class="timeout" id="list'+i+'"  data-start="' + data.nowTime + '" data-end="' + plan.buyBeginTime + '"></span>'
						
					}else if(plan.status==1){
						planListStr = planListStr
						+ '	<span class="btn sm">'
						+ '立即加入'
						+ '	</span>'
					}else if(plan.status==2){
						planListStr = planListStr
						+ '	<span class="">'
						+ '锁定中'
						+ '	</span>'
					}else if(plan.status==3){
						planListStr = planListStr
						+ '	<span class="">'
						+ '已退出'
						+ '	</span>'
					}
					planListStr = planListStr
					
					+ '</li>'
					+ '</a>'
					+ '</ul>';
				
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
				webPath+ "/plan/getPlanList.do", 
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


