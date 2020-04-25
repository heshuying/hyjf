subMenu("mytender");
var today = new Date();
$(document).ready(
		function() {
		var projectStatus = $("#projectStatus").val();
		$(".project-tab").click(function(e) {
		    var _self = $(e.target);
		    if (_self.is("li")) {
		        var idx = _self.attr("panel");
		        var panel = _self.parent().siblings(".project-tab-panel");
		        _self.siblings("li.active").removeClass("active");
		        _self.addClass("active");
		        panel.children("li.active").removeClass("active");
		        panel.children("li[panel="+idx+"]").addClass("active");
		        $("#projectStatus").val(_self.data("status"));
				//设置默认选择日期
				$( "#startDate" ).datepicker( "option","maxDate",today );
				$( "#endDate" ).datepicker( "option","maxDate",today );
				$( ".date-range" ).addClass("date-default-c");
				$("#startDate").val("");
				$("#endDate").val("");
		        getMyTenderListPage();
		    }
		})
		//日期插件绑定
		$( "#startDate" ).datepicker({
		  defaultDate: "-1w",
		  numberOfMonths: 1,
		  dateFormat: "yy-mm-dd",
		  maxDate: today,
		  onClose: function( selectedDate ) {
		    $( "#endDate" ).datepicker( "option", "minDate", selectedDate );
		    $( ".date-range" ).removeClass("date-default-c");
		  }
		});
		$( "#endDate" ).datepicker({
		  defaultDate: today,
		  dateFormat: "yy-mm-dd",
		  numberOfMonths: 1,
		  maxDate: today,
		  onClose: function( selectedDate ) {
		    $( "#startDate" ).datepicker( "option", "maxDate", selectedDate );
		    $( ".date-range" ).removeClass("date-default-c");
		  }
		});
		//设置默认选择日期
		$( "#startDate" ).datepicker( "setDate", "-1w" );
		$( "#endDate" ).datepicker( "setDate", today );
		$( ".date-range" ).addClass("date-default-c");
		
		//日期搜索
		$("#searchByDate").click(function(){
			/**获取我的投资*/
			getMyTenderListPage();
		})

		//我的投资页面form表单查询条件添加
		$("#startDate").val("");
		$("#endDate").val("");
		/**获取我的投资*/
		getMyTenderListPage();
		
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

//查询还款计划	查询AJAX函数
function selectRepayPlan(borrowNid,nid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: webPath+ "/user/mytender/repaylist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"borrowNid":borrowNid,
			"nid":nid
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordHtml = "";
				var recordList = data.data.userrepaylist;
				if(recordList!=null && recordList!="" && recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						var zt= "未还款";
						if(record.status=='1'){
							zt="已还款";
						}
						recordHtml += '<tr>';
						recordHtml += '<td>'+record.projectPeriod+'</td>';
						recordHtml += '<td>'+record.projectTotal+'</td>';
						recordHtml += '<td>'+record.projectCapital+'</td>';
						recordHtml += '<td>'+record.projectInterest+'</td>';
						recordHtml += '<td>'+record.repayTime.substring(0,10)+'</td>';
						recordHtml += '<td>'
								+ ((Number(parseFloat(record.projectTotal)*100)
										+ Number(parseFloat(record.chargeInterest)*100)
										+ Number(parseFloat(record.lateInterest)*100)
										+ Number(parseFloat(record.dalayInterest)*100) )/100).toFixed(2)
								+ '</td>';
						recordHtml += '<td>'+zt+'</td>';
						recordHtml += '</tr>';
					}
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="7">无还款计划</td>';
					recordHtml += '</tr>';
				}
				jQuery(".repayPlanDetail table tbody").html(recordHtml);
				assignPopupWin('.repayPlanDetail');
			}else{
				showTip(null,data.msg,"tip","tips");
			}
		}
	});
}
//查询优惠券还款计划	查询AJAX函数
function selectCouponRepayPlan(borrowNid,nid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: webPath+ "/user/mytender/couponrepaylist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"borrowNid":borrowNid,
			"nid":nid
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordHtml = "";
				var recordList = data.data.userrepaylist;
				if(recordList!=null && recordList!="" && recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						var zt= "未还款";
						if(record.status=='1'){
							zt="已领取";
						}
						recordHtml += '<tr>';
						recordHtml += '<td>'+record.projectPeriod+'</td>';
						recordHtml += '<td>'+record.projectTotal+'</td>';
						recordHtml += '<td>'+record.projectCapital+'</td>';
						recordHtml += '<td>'+record.projectInterest+'</td>';
						recordHtml += '<td>'+record.repayTime.substring(0,10)+'</td>';
						recordHtml += '<td>'
								+ ((Number(parseFloat(record.projectTotal)*100)
										+ Number(parseFloat(record.chargeInterest)*100)
										+ Number(parseFloat(record.lateInterest)*100)
										+ Number(parseFloat(record.dalayInterest)*100) )/100).toFixed(2)
								+ '</td>';
						recordHtml += '<td>'+zt+'</td>';
						recordHtml += '</tr>';
					}
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="7">无还款计划</td>';
					recordHtml += '</tr>';
				}
				jQuery(".repayPlanDetail table tbody").html(recordHtml);
				assignPopupWin('.repayPlanDetail');
			}else{
				showTip(null,data.msg,"tip","tips");
			}
		}
	});
}
/**
 * 获取我的投资信息
 */
function getMyTenderListPage() {
	
	projectStatus=$("#projectStatus").val();
	var data="paginatorPage="+$("#paginatorPage").val()+
		"&pageSize="+$("#pageSize").val()+
		"&startDate="+$("#startDate").val()+
		"&endDate="+$("#endDate").val()+
		"&projectStatus="+projectStatus;
		
	var pannelid="fenye1";//还款中
	if(projectStatus=='12'){
		pannelid="fenye0";//冻结中
	}else if(projectStatus=='14'){
		pannelid="fenye2";//已还款
	} 
	doRequest(
			getMyTenderListPageSuccessCallback,
			getMyTenderListPageErrorCallback, 
			webPath+ "/user/mytender/projectlist.do",
			data, true,pannelid,pannelid);
}
/**
 * 获取我的投资信息成功回调
 */
function getMyTenderListPageSuccessCallback(data) {
	var tradeListStr ="";
	// 挂载数据
	if(data.projectStatus!=null){
		var tradeList = data.projectlist;

		if(data.projectStatus=="12"){
			tradeListStr = "<tr class='invite-table-header'> " +
			"<td>项目编号</td>" +
			"<td>历史年回报率</td> " +
			"<td>期限</td>" +
			"<td>投资金额（元）</td> " +
			"<td>进度</td>" +
			"<td>投资时间</td>" +
			"<td>备注</td></tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.borrowNid+'">'
				+ 	'<td><a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="title"><span class="id">'+(project.projectType == 13 ? project.borrowAssetNumber: project.borrowNid) +'</span><br> <span class="date">投资时间 ：'+project.investTime.substring(0,10)+'</span></a></td>' 
			
				+ 	'<td>'+project.borrowInterest+'%</td>'
				+ 	'<td>'+project.borrowPeriod+'</td>'
				+ 	'<td class="newFormatNum">'+project.accountInvest+'</td>'
				+ 	'<td><div class="progress-con num"><div class="progress-all"><div class="progress-cur" data-percent="'+project.borrowSchedule+'"></div></div><div class="percent"><span>'+project.borrowSchedule+'</span>%</div></div></td>'
				+ 	'<td>'+project.investTime+'</td>'
				+ 	'<td>'+ project.remark +'</td>'
				+ 	'</tr>';
			}
			$("#pant0").html(tradeListStr);
			$(".progress-cur").each(function() {
		        var perc = $(this).data("percent");
		        if (perc) {
		            $(this).animate({ "width": perc });
		        }
		    });
			
		}else if(data.projectStatus=="13"){
			tradeListStr = "<tr class='invite-table-header'> " +
			"<td>项目编号</td>" +
			"<td>历史年回报率</td> " +
			"<td>期限</td>" +
			"<td>投资金额（元）</td> " +
			"<td>待收金额（元）</td>" +
			"<td>还款时间</td>" +
			"<td>备注</td></tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				var jihua = "";
				var xieyi = "";
				if (project.investType!='3') {
					if(project.borrowStyle=='month' || project.borrowStyle=='principal' || project.borrowStyle=='endmonth'){ 
						if(project.investType == 1){
							jihua = '<br/> <a href="javascript:;" onclick="selectRepayPlan(\''+project.borrowNid+'\',\''+project.nid+'\')" class="show-term">还款计划</a>'
						}else{
							jihua = '<br/> <a href="javascript:;" onclick="selectCouponRepayPlan(\''+project.borrowNid+'\',\''+project.nid+'\')" class="show-term">还款计划</a>'
						}
					}
				} 
				
				if(project.investType == 1){
					xieyi = '<br><a href="'+webPath+'/user/mytender/userinvestlist.do?borrowNid='+project.borrowNid+'&nid='+project.nid+'&projectType='+project.projectType+'&assetNumber='+project.borrowAssetNumber+'" class="show-term" target="_blank">查看协议</a>';
				}
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.borrowNid+'">'
				+ 	'<td><a href="'+webPath+'/bank/web/borrow/getBorrowDetail.do?borrowNid='+project.borrowNid+'" class="title"><span class="id">'+(project.projectType == 13 ? project.borrowAssetNumber: project.borrowNid) +'</span><br> <span class="date">投资时间 ：'+project.investTime.substring(0,10)+'</span></a></td>'   
				+ 	'<td>'+project.borrowInterest+'%</td>'
				+ 	'<td>'+project.borrowPeriod+'</td>'
				+ 	'<td class="newFormatNum">'+project.accountInvest+'</td>'
				+ 	'<td class="newFormatNum">'+ project.accountWait+'</td>'
				+	'<td><span class="end-date">'+project.repayTime+ xieyi   
				+	jihua +'</span></td>'
				+ 	'<td>'+project.remark+'</td>'
				+ 	'</tr>';
			}
			$("#pant1").html(tradeListStr);
			
		}else if(data.projectStatus=="14"){
			tradeListStr = "<tr class='invite-table-header'> " +
			"<td>项目编号</td>" +
			"<td>历史年回报率</td> " +
			"<td>期限</td>" +
			"<td>投资金额（元）</td> " +
			"<td>已收金额（元）</td>" +
			"<td>详情</td>" +
			"<td>备注</td></tr>";
			for (var i = 0; i < tradeList.length; i++) {
				var project =tradeList[i];
				
				tradeListStr =tradeListStr 
				+ '<tr id="'+project.borrowNid+'">'
				+ 	'<td><span class="id">'+(project.projectType == 13 ? project.borrowAssetNumber: project.borrowNid) +'</span><br> <span class="date">回款时间 ：'+project.repayTime.substring(0,10)+'</span></td>'  
				+ 	'<td>'+project.borrowInterest+'%</td>'
				+ 	'<td>'+project.borrowPeriod+'</td>'
				+ 	'<td class="newFormatNum">'+project.accountInvest+'</td>'
				+ 	'<td class="newFormatNum">'+project.accountYes+'</td>'
				+ 	'<td>'+'已回款'+'</td>'
				+ 	'<td>'+project.remark+'</td>'
				+ 	'</tr>';
			}
			$("#pant2").html(tradeListStr);
		}
	}
	$(".newFormatNum").html($.fmtThousand($(this).html()))
	//格式化数字
	var num = $(".newFormatNum");
    for(var i=0;i<num.length;i++){
    	var format = num.eq(i).html();
    	num.eq(i).html($.fmtThousand(format));
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
	projectStatus=$("#projectStatus").val();
	var data="paginatorPage="+paginatorPage+
	"&pageSize="+$("#pageSize").val()+
	"&startDate="+$("#startDate").val()+
	"&endDate="+$("#endDate").val()+
	"&projectStatus="+projectStatus;
	
	var pannelid="fenye1";//还款中
	if(projectStatus=='12'){
		pannelid="fenye0";//冻结中
	}else if(projectStatus=='14'){
		pannelid="fenye2";//已还款
	} 
	doRequest(
			getMyTenderListPageSuccessCallback,
			getMyTenderListPageErrorCallback, 
			webPath+ "/user/mytender/projectlist.do",
			data, true,pannelid,pannelid);
}

		
		