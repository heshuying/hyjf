subMenu("planinfo");
var today = new Date();
$(document).ready(
		function() {
		var planStatus = $("#planStatus").val();
		$(".project-tab").click(function(e) {
		    var _self = $(e.target);
		    if (_self.is("li")) {
		        var idx = _self.attr("panel");
		        var panel = _self.parent().siblings(".project-tab-panel");
		        _self.siblings("li.active").removeClass("active");
		        _self.addClass("active");
		        panel.children("li.active").removeClass("active");
		        panel.children("li[panel="+idx+"]").addClass("active");
		        $("#planStatus").val(_self.data("status"));
		        getMyTenderListPage();
		    }
		})
		//我的投资页面form表单查询条件添加
		/**获取我的投资*/
		/*getMyTenderListPage();*/
		$(".project-tab").children("li.active").click();
		/**
		 * 为分页按钮绑定事件
		 */
		$(document).on("click", ".fenye1", function() {
			flip($(this).data("page"));
		});
		/**
		 * 为分页按钮绑定事件
		 */
		$(document).on("click", ".fenye0", function() {
			flip($(this).data("page"));
		});
		/**
		 * 为分页按钮绑定事件
		 */
		$(document).on("click", ".fenye2", function() {
			flip($(this).data("page"));
		});
});
function popupWin(msg) {
	$('.settlement_mask').fadeIn();
	$('.js_zr').fadeIn();
	$("#popmsg").text(msg);
}
//关闭弹窗
function popoutWin() {
	$('.settlement_mask').fadeOut();
	$('.settlement').fadeOut();
}
/*
 *  @func 打开弹窗
 *  @param ele string 选择器名
 *  @param msg string 填充文字信息或者dom元素
 */
 function assignPopupWin(ele,msg){
     if(!ele){
         ele = $(".settlement");
     }else{
         ele = $(ele);
     }
     $('.settlement_mask').fadeIn();
     ele.fadeIn();
     if(msg){
         ele.find(".poptxt").html(msg);
     }
 }
 
 function assignPopoutWin(){
     $('.settlement_mask').fadeOut();
     $('.settlement').fadeOut();
 }


/**
 * 获取我的投资信息
 */
function getMyTenderListPage() {
	
	planStatus=$("#planStatus").val();
	var data="paginatorPage="+$("#paginatorPage").val()+
		"&pageSize="+$("#pageSize").val()+
		"&planStatus="+planStatus;
		
	var pannelid="fenye0";//申购中
	if(planStatus=='5'){
		pannelid="fenye1";//锁定中
	}else if(planStatus=='11'){
		pannelid="fenye2";//已还款
	} 
	doRequest(
			getMyTenderListPageSuccessCallback,
			getMyTenderListPageErrorCallback, 
			webPath+ "/user/planinfo/planlist.do",
			data, true,pannelid,pannelid);
}
/**
 * 获取我的投资信息成功回调
 */
function getMyTenderListPageSuccessCallback(data) {
	var tradeListStr ="";
	
	// 挂载数据
	if(data.planStatus!=null){
		var tradeList = data.projectlist;
		if(data.planStatus=="4"){//申购中
			tradeListStr = "<tr class='invite-table-header'> " +
			"<td>智投编号</td>" +
			"<td>锁定期</td> " +
			"<td>历史年回报率</td>" +
			"<td>加入金额</td> " +
			"<td>加入时间</td>" +
			"<td width='160'>操作</td>" +
			"</tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.debtPlanNid+'"'+(i%2 == 0 ? "" : " class='odd'")+'>'
				+ 	'<td><a href="'+webPath+'/plan/getPlanDetail.do?planNid='+project.debtPlanNid+'" class="title" target="_blank"><span class="id">'+project.debtPlanNid+'</span></a></td>' 
				+ 	'<td>'+project.debtLockPeriod+'个月</td>'
				+ 	'<td>'+project.expectApr+'%</td>'
				+ 	'<td class="newFormatNum">'+project.accedeAccount+'</td>'
				+ 	'<td>'+project.createTime+'</td>'
				+ 	'<td>'+FormatCouponTxt(project, 0)+'</td>' 
				+ 	'</tr>';
			}
			$("#pant0").html(tradeListStr);
			$(".progress-cur").each(function() {
		        var perc = $(this).data("percent");
		        if (perc) {
		            $(this).animate({ "width": perc });
		        }
		    });
			
		}else if(data.planStatus=="11"){//已退出
			tradeListStr = "<tr class='invite-table-header'> " +
			"<td width='94'>智投编号</td>" +
			"<td width='64'>锁定期</td> " +
			"<td width='114'>历史年回报率</td>" +
			"<td>加入金额</td> " +
			"<td>实际收益</td> " +
			"<td width='88'>回款总额</td> " +
			/*"<td width='88'>退出日期</td>" +*/
			"<td width='98'>实际到账日期</td>" +
			"<td width='160'>操作</td>" +
			"</tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.debtPlanNid+'"'+(i%2 == 0 ? "" : " class='odd'")+'>'
				+ 	'<td><a href="'+webPath+'/plan/getPlanDetail.do?planNid='+project.debtPlanNid+'" class="title" target="_blank"><span class="id">'+project.debtPlanNid+'</span></a></td>' 
				+ 	'<td>'+project.debtLockPeriod+'个月</td>'
				+ 	'<td>'+project.expectApr+'%</td>'
				+ 	'<td class="newFormatNum">'+project.accedeAccount+'</td>'
				+ 	'<td class="newFormatNum">'+project.repayInterestYes+'</td>'
				+ 	'<td class="newFormatNum">'+project.repayAccountYes+'</td>'
				+ 	'<td>'+project.repayTime+'</td>'
				+ 	'<td>'+ FormatCouponTxt(project, 2)+'</td>' 
				+ 	'</tr>';
			}
			$("#pant2").html(tradeListStr);
			
		}else {//锁定中
			tradeListStr = "<tr class='invite-table-header'> " +
			
			"<td>智投编号</td>" +
			"<td>锁定期</td> " +
			"<td>历史年回报率</td>" +
			"<td>加入金额</td> " +
			"<td>待收金额</td> " +
			"<td>退出日期</td>" +
			"<td>最晚到账日期</td>" +
			"<td width='160'>操作</td>" +
			"</tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.debtPlanNid+'"'+(i%2 == 0 ? "" : " class='odd'")+'>'
				+ 	'<td><a href="'+webPath+'/plan/getPlanDetail.do?planNid='+project.debtPlanNid+'" class="title" target="_blank"><span class="id">'+project.debtPlanNid+'</span><br/><span class="date">加入时间：'+project.createTimeDay+'</span></a></td>' 
				+ 	'<td>'+project.debtLockPeriod+'个月</td>'
				+ 	'<td>'+project.expectApr+'%</td>'
				+ 	'<td class="newFormatNum">'+project.accedeAccount+'</td>'
				+ 	'<td class="newFormatNum">'+project.repayAccountWait+'</td>'
				+ 	'<td>'+project.liquidateShouldTime+'</td>'
				+ 	'<td>'+project.lastRepayTime+'</td>'
				+ 	'<td>'+FormatCouponTxt(project, 1) +'</td>' 
				+ 	'</tr>';
			}
			$("#pant1").html(tradeListStr);
		}
	}
	$(".newFormatNum").html($.fmtThousand($(this).html()));
	function FormatCouponTxt(project, currentType){
		/*
		 * 格式化优惠券信息
		 * 
		 * */
		var couponTxt = "";
		if(project.tenderType == 1){
			couponTxt = '<a href="'+webPath+'/user/planinfo/getPlanInfoDetail.do?type=' + currentType +'&debtPlanNid='+project.debtPlanNid+'&accedeOrderId='+project.accedeOrderId+'" class="show-term"><span class="highlight" style="font-size:16px;">查看详情 &gt;&gt;</span></a>';
		}else{
			couponTxt = '<span class="highlight show-term">'+project.couponType+'<br/>编号：'+project.couponUserCode+'</span>';
		}
		return couponTxt;
	}
	
	//格式化数字
	var num = $(".newFormatNum");
    for(var i=0;i<num.length;i++){
    	var format = num.eq(i).html();
    	num.eq(i).html($.fmtThousand(format)+"元");
    }
}

/**
 * 获取我的投资信息失败回调
 */
function getMyTenderListPageErrorCallback(data) {

}
/**
 * 分页按钮发起请求
 */
function flip(paginatorPage) {
	planStatus=$("#planStatus").val();
	var data="paginatorPage="+paginatorPage+
	"&pageSize="+$("#pageSize").val()+
	"&planStatus="+planStatus;
	
	var pannelid="fenye0";//申购中
	if(planStatus=='5'){
		pannelid="fenye1";//锁定中
	}else if(planStatus=='11'){
		pannelid="fenye2";//已还款
	} 
	doRequest(
			getMyTenderListPageSuccessCallback,
			getMyTenderListPageErrorCallback, 
			webPath+ "/user/planinfo/planlist.do",
			data, true,pannelid,pannelid);
}

		
		