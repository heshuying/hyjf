<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta charset="utf-8" />
<title>债权转让 - 汇盈金服官网</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<input type="hidden" id="userData" data-times="3">
	<div class="user-credit-banner">
		<jsp:include page="/subMenu.jsp"></jsp:include>
		<h2>债权转让</h2>
	</div>
	<div class="project-tabbing user-credit-tabbing">
		<div class="container-1200">
			<ul class="project-tab">
				<li panel="0" class="active" tab="tendertocredit">可转让</li>
				<li panel="1" tab="creditInProgress">转让中</li>
				<li panel="2" tab="creditStop">已转让</li>
				<li panel="3" tab="creditAssign">已承接</li>
			</ul>
			<div class="clearfix"></div>
			<ul class="project-tab-panel">
				<li panel="0" class="active" id="tendertocredit">
					<table>
						<thead>
							<tr>
								<th width="320">项目编号</th>
								<th>历史年回报率</th>
								<th>剩余期限</th>
								<th>债权本金（元）</th>
								<th width="120">操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 可转让数据列表 -->
						</tbody>
					</table>
					<div class="new-pagination" id="tendertocreditPage">
						<!-- 分页栏模板 -->
					</div>
				</li>
				<li panel="1" id="creditInProgress">
					<table>
						<thead>
							<tr>
								<th width="320">项目编号</th>
								<th>折让比例</th>
								<th>历史年回报率</th>
								<th>剩余期限</th>
								<th>债权本金（元）</th>
								<th>转让进度</th>
								<th width="120">操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 转让中数据列表 -->
						</tbody>
					</table>
					<div class="new-pagination" id="creditInProgressPage">
						<!-- 分页栏模板 -->
					</div>
				</li>
				<li panel="2" id="creditStop">
					<table>
						<thead>
							<tr>
								<th width="320">项目编号</th>
								<th>折让比例</th>
								<th>历史年回报率</th>
								<th>剩余期限</th>
								<th>债权本金（元）</th>
								<th>最近交易时间</th>
								<th width="120">操作</th>
							</tr>
						</thead>
						<tbody>
							<!-- 已转让数据列表 -->
						</tbody>
					</table>
					<div class="new-pagination" id="creditStopPage">
						<!-- 分页栏模板 -->
					</div>
				</li>
				<li panel="3" id="creditAssign">
					<table>
						<thead>
							<tr>
								<th width="320">项目编号</th>
								<th>折让比例</th>
								<th>历史年回报率</th>
								<th>期限</th>
								<th>出借金额（元）</th>
								<th>待收金额（元）</th>
								<th width="120">到期时间</th>
							</tr>
						</thead>
						<tbody>
							<!-- 已承接数据列表 -->
						</tbody>
					</table>
					<div class="new-pagination" id="creditAssignPage">
						<!-- 分页栏模板 -->
					</div>
				</li>
			</ul>
		</div>
	</div>
	<form action="${ctx}/bank/user/credit/tendertocreditdetail.do" id="tenderToCreditForm" name="tenderToCreditForm" method="post">
		<input type="hidden" id="borrowNid" name="borrowNid" value="" />
		<input type="hidden" id="tenderNid" name="tenderNid" value="" />
	</form>
	<div class="settlement_mask"></div>
	<div class="settlement js_zr js_zra">
		<a class="zr_close js_close" href="javascript:void(0)"
			onclick="popoutWin()">×</a>
		<div class="qr_main">
			<h3>提示信息</h3>
			<h4 class="zr_pay" id="popmsg">提交成功</h4>
			<div class="btns">
				<button type="button" onclick="popoutWin()">确定</button>
			</div>
		</div>
	</div>
	<!-- 交易明细 Start -->
	<div class="settlement js_detail css_detail assignDetail">
        <div class="qr_main">
            <a class="zr_close js_close" href="javascript:void(0)" onclick="assignPopoutWin()">×</a>
            <h3>转让明细</h3>
            <div id="detailmsg" class="popmsg poptxt">
                <table border="0" cellspacing="0" cellpadding="0">
                <thead>
                    <tr>
                        <th>用户</th>
                        <th>购买本金</th>
                        <th>实付金额</th>
                        <th>服务费</th>
                        <th>垫付利息</th>
                        <th>交易时间</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
					<!-- 投资债转数据列表 -->
                </tbody>
            </table>
            <div class="clearfix"></div>
            </div>    
        </div>
    </div>
    
    <!-- 交易明细 End -->
    <!-- 已承接还款计划 Start -->
    <div class="settlement css_detail creditRepayPlan">
        <div class="qr_main">
            <a class="zr_close js_close" href="javascript:void(0)" onclick="assignPopoutWin()">×</a>
            <h3>还款计划</h3>
            <div id="detailmsg" class="popmsg poptxt">
                <table border="0" cellspacing="0" cellpadding="0">
                <thead>
                    <tr>
                        <th>期数</th>
                        <th>待收本息</th>
                        <th>待收本金</th>
                        <th>待收利息</th>
                        <th>应还时间</th>
                        <th>少还利息</th>
                        <th>实际还款</th>
                        <th>状态</th>
                    </tr>
                </thead>
                <tbody>
					<!-- 还款计划列表 -->
                </tbody>
            </table>
            <div class="clearfix"></div>
            </div>    
        </div>
    </div>
    <!-- 已承接还款计划 End -->
    <script>setActById('userCredit');</script>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>  
		var tabActive = "${tab}";
		$(".project-tab").click(function(e) {
			var _self = $(e.target);
			if (_self.is("li")) {
				var idx = _self.attr("panel");
				var panel = _self.parent().siblings(".project-tab-panel");
				_self.siblings("li.active").removeClass("active");
				_self.addClass("active");
				panel.children("li.active").removeClass("active");
				panel.children("li[panel=" + idx + "]").addClass("active");
			}
			if(_self.attr("tab")!=tabActive){
				tabActive = _self.attr("tab");
				if(tabActive=="tendertocredit"){
					//可转让数据查询
					tenderToCreditAjax(1, 8);
				}else if(tabActive=="creditInProgress"){
					//转让中数据查询
					creditInprogressAjax(1, 8);
				}else if(tabActive=="creditStop"){
					//已债转数据查询
					creditStopAjax(1, 8);
				}else if(tabActive=="creditAssign"){
					//已承接数据查询
					creditAssignAjax(1, 8);
				}
			}else{
				return;
			}
		})

		function popupWin(msg) {
			$('.settlement_mask').fadeIn();
			$('.js_zr').fadeIn();
			$("#popmsg").text(msg);
		}
		function popoutWin() {
			$('.settlement_mask').fadeOut();
			$('.js_zr').fadeOut();
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
	</script>
</body>
<script type="text/javascript">
$(document).ready(function(){
	var tab = "${tab}";
	//如果指定标签页,则显示该标签下的数据
	if(tab!=null && tab!=""){
		jQuery(".project-tab li").removeClass("active");
		jQuery(".project-tab-panel li").removeClass("active");
		jQuery(".project-tab-panel #${tab}").addClass("active");
		jQuery(".project-tab li").each(function(){
			if(jQuery(this).attr("tab")==tab){
				jQuery(this).addClass("active");
				creditInprogressAjax(1, 8);
			}
		})
	}else{
		//页面初始化时默认加载可转让数据
		tenderToCreditAjax(1, 8);
	}
})

//可转让分页点击查询
function tenderToCreditPage(currPage, limitPage, param){
	tenderToCreditAjax(currPage, limitPage);
}
//转让中分页点击查询
function creditInprogressPage(currPage, limitPage, param){
	creditInprogressAjax(currPage, limitPage);
}
//已转让分页点击查询
function creditStopPage(currPage, limitPage, param){
	creditStopAjax(currPage, limitPage);
}
//已承接分页点击查询
function creditAssignPage(currPage, limitPage, param){
	creditAssignAjax(currPage, limitPage);
}
//可转让点击转让提交
function tendertocredit(borrowNid, tenderNid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/tenderabletocredit.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"borrowNid":borrowNid,
			"tenderNid":tenderNid
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var creditedNum = data.data.creditedNum;
				if(0<=creditedNum && creditedNum<3){
					//复合提交条件提交债转信息
					if(data.data.tenderToCredit!=null){
						jQuery("#tenderToCreditForm #borrowNid").val(borrowNid);
						jQuery("#tenderToCreditForm #tenderNid").val(tenderNid);
						jQuery("#tenderToCreditForm").submit();
					}else{
						popupWin("转让时出错,请刷新页面后重试");
					}
				}else if(creditedNum>=3){
					popupWin("今天的转让次数已满3次,请明天再试");
				}
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//可转让数据查询AJAX函数
function tenderToCreditAjax(currPage, limitPage){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/tendertocreditlist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"currPage":currPage,
			"limitPage":limitPage
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordList = data.data.recordList;
				var recordHtml = "";
				if(recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td><a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid='+record.borrowNid+'" class="title"> <span class="id">'+record.borrowNid+'</span><br><span class="date">投资时间 ：'+record.tenderTime.substring(0,10)+'</span></a></td>';
						recordHtml += '<td><span class="highlight">'+record.borrowApr+'<em> %</em></span></td>';
						recordHtml += '<td>'+record.lastDays+'天</td>';
						recordHtml += '<td>'+record.creditAccount+'</td>';
						recordHtml += '<td><a href="javascript:;" onclick="tendertocredit(\''+record.borrowNid+'\',\''+record.tenderNid+'\')" class="btn credit-btn">转 让</a></td>';
						recordHtml += '</tr>';
					}
					jQuery("#tendertocredit .new-pagination").html(data.data.paginator.webPaginator);
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="8">';
					recordHtml += '<div class="data-empty">';
					recordHtml += '<div class="empty-icon"></div>';
					recordHtml += '<p class="align-center">咦，您所找的页面暂无数据～</p>';
					recordHtml += '</div>';
					recordHtml += '</td>';
					recordHtml += '</tr>';
				}
				jQuery("#tendertocredit tbody").html(recordHtml);
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//转让中数据查询AJAX函数
function creditInprogressAjax(currPage, limitPage){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/creditinprogresslist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"currPage":currPage,
			"limitPage":limitPage
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordList = data.data.recordList;
				var recordHtml = "";
				if(recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td><a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid='+record.bidNid+'" class="title"><span class="id">'+record.bidNid+'</span><br> <span class="date">转让时间 ：'+record.addTime.substring(0,10)+'</span></a></td>';
						recordHtml += '<td><span class="highlight">'+record.creditDiscount+'<em> %</em></td>';
						recordHtml += '<td><span class="highlight">'+record.bidApr+'<em> %</em></span></span></td>';
						recordHtml += '<td>'+record.creditTerm+'天</td>';
						recordHtml += '<td>'+record.creditCapital+'</td>';
						recordHtml += '<td><span class="highlight">'+record.creditInProgress+'<em> %</em></span></td>';
						recordHtml += '<td><a href="javascript:;" onclick="creditAssignTenderList(\''+record.creditNid+'\')" class="btn credit-btn">详 情</a></td>';
						recordHtml += '</tr>';
					}
					jQuery("#creditInProgress .new-pagination").html(data.data.paginator.webPaginator);
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="8">';
					recordHtml += '<div class="data-empty">';
					recordHtml += '<div class="empty-icon"></div>';
					recordHtml += '<p class="align-center">咦，您所找的页面暂无数据～</p>';
					recordHtml += '</div>';
					recordHtml += '</td>';
					recordHtml += '</tr>';
				}
				jQuery("#creditInProgress tbody").html(recordHtml);
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//已转让数据查询AJAX函数
function creditStopAjax(currPage, limitPage){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/creditstoplist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"currPage":currPage,
			"limitPage":limitPage
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordList = data.data.recordList;
				var recordHtml = "";
				if(recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td><a href="${ctx}/bank/web/borrow/getBorrowDetail.do?borrowNid='+record.bidNid+'" class="title"><span class="id">'+record.bidNid+'</span><br> <span class="date">转让时间 ：'+record.addTime.substring(0,10)+'</span></a></td>';
						recordHtml += '<td><span class="highlight">'+record.creditDiscount+'<em> %</em></span></td>';
						recordHtml += '<td><span class="highlight">'+record.bidApr+'<em> %</em></span></span></td>';
						recordHtml += '<td>'+record.creditTerm+'天</td>';
						recordHtml += '<td>'+record.creditCapital+'</td>';
						recordHtml += '<td><span class="end-date">'+record.assignTime.substring(0,10)+' <br>'+record.assignTime.substring(10,18)+'</span></td>';
						recordHtml += '<td><a href="javascript:;" onclick="creditAssignTenderList(\''+record.creditNid+'\')" class="btn credit-btn">详 情</a></td>';
						recordHtml += '</tr>';
					}
					jQuery("#creditStop .new-pagination").html(data.data.paginator.webPaginator);
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="5">无已转让的债转数据</td>';
					recordHtml += '</tr>';
				}
				jQuery("#creditStop tbody").html(recordHtml);
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//已承接数据查询AJAX函数
function creditAssignAjax(currPage, limitPage){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/creditassignlist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"currPage":currPage,
			"limitPage":limitPage
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordList = data.data.recordList;
				var recordHtml = "";
				if(recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td><a href="${ctx}/project/getProjectDetail.do?borrowNid='+record.bidNid+'" class="title"><span class="id">'+record.bidNid+'</span><br> <span class="date">承接时间 ：'+record.assignCreateDate.substring(0,10)+'</span></a></td>';
						recordHtml += '<td><span class="highlight">'+record.creditDiscount+'<em> %</em></span></td>';
						recordHtml += '<td><span class="highlight">'+record.bidApr+'<em> %</em></span></span></td>';
						recordHtml += '<td>'+record.creditTerm+'天</td>';
						recordHtml += '<td>'+record.assignCapital+'</td>';
						recordHtml += '<td>'+(parseFloat(record.assignAccount-record.assignRepayAccount)>=0.10?parseFloat(record.assignAccount-record.assignRepayAccount).toFixed(2):0.00)+'</td>';
						recordHtml += '<td><span class="end-date">'+record.assignRepayEndTime+' <br><a href="${ctx}/bank/user/credit/usercreditcontract.do?bidNid='+record.bidNid+'&creditNid='+record.creditNid+'&creditTenderNid='+record.creditTenderNid+'&assignNid='+record.assignNid+'" class="show-term" target="_blank">查看协议</a><br><a href="javascript:;" onclick="creditRepayPlanList(\''+record.creditNid+'\',\''+record.assignNid+'\');">还款计划</a></span></td>';
						//recordHtml += '<td><span class="end-date">'+record.assignRepayEndTime+' <br><a href="'+data.data.phpWebHost+'/agreement/contract_hzr.html?assign_nid='+record.assignNidMD5+'&uid='+record.userId+'&timestamp='+data.data.timestamp+'" class="show-term" target="_blank">查看协议</a><br><a href="javascript:;" onclick="creditRepayPlanList(\''+record.creditNid+'\',\''+record.assignNid+'\');">还款计划</a></span></td>';
						recordHtml += '</tr>';
					}
					jQuery("#creditAssign .new-pagination").html(data.data.paginator.webPaginator);
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="6">无已承接的债转数据</td>';
					recordHtml += '</tr>';
				}
				jQuery("#creditAssign tbody").html(recordHtml);
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//已承接债转的投资详情	查询AJAX函数
function creditAssignTenderList(creditNid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/creditassigntenderlist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"creditNid":creditNid
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordHtml = "";
				var recordList = data.data.recordList;
				if(recordList!=null && recordList!="" && recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td>'+record.creditAssignUserTrueName+'</td>';
						recordHtml += '<td>'+record.assignCapital+'</td>';
						recordHtml += '<td>'+record.assignPay+'</td>';
						recordHtml += '<td>'+record.creditFee+'</td>';
						recordHtml += '<td>'+record.assignInterestAdvance+'</td>';
						recordHtml += '<td>'+record.addTime+'</td>';
						recordHtml += '<td><a href="${ctx}/bank/user/credit/usercreditcontract.do?bidNid='+record.bidNid+'&creditNid='+record.creditNid+'&creditTenderNid='+record.creditTenderNid+'&assignNid='+record.assignNid+'" target="_blank">协议</a></td>';
						//recordHtml += '<td><a href="'+data.data.phpWebHost+'/agreement/contract_hzr.html?assign_nid='+record.assignNidMD5+'&uid='+record.userId+'&timestamp='+data.data.timestamp+'" target="_blank">协议</a></td>';
						recordHtml += '</tr>';
					}
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="7">无承接债转的投资数据</td>';
					recordHtml += '</tr>';
				}
				jQuery(".assignDetail table tbody").html(recordHtml);
				assignPopupWin('.assignDetail');
			}else{
				popupWin(data.msg);
			}
		}
	});
}

//已承接还款计划	查询AJAX函数
function creditRepayPlanList(creditNid,assignNid){
	jQuery.ajax({
		type: "POST",
		async: "async",
		url: "${ctx}/bank/user/credit/creditrepayplanlist.do",
		contentType:"application/x-www-form-urlencoded;charset=utf-8",
		dataType: "json",
		data: {
			"creditNid":creditNid,
			"assignNid":assignNid
		},
		error: function(request) {
	        popupWin("连接服务器出错,请稍后重试");
	    },
		success: function(data){
			if(data.resultFlag==0){
				var recordHtml = "";
				var recordList = data.data.recordList;
				if(recordList!=null && recordList!="" && recordList.length>0){
					for(i=0;i<recordList.length;i++){
						var record = recordList[i];
						recordHtml += '<tr>';
						recordHtml += '<td>'+record.recoverPeriod+'</td>';
						recordHtml += '<td>'+record.assignAccount+'</td>';
						recordHtml += '<td>'+record.assignCapital+'</td>';
						recordHtml += '<td>'+record.assignInterest+'</td>';
						recordHtml += '<td>'+record.assignRepayNextTime+'</td>';
						recordHtml += '<td>0.00</td>';
						recordHtml += '<td>'+record.assignAccount+'</td>';
						if(record.status=="1"){
							recordHtml += '<td>已还款</td>';
						}else{
							recordHtml += '<td>未还款</td>';
						}
						recordHtml += '</tr>';
					}
				}else{
					recordHtml += '<tr>';
					recordHtml += '<td colspan="9">无还款计划数据</td>';
					recordHtml += '</tr>';
				}
				jQuery(".creditRepayPlan table tbody").html(recordHtml);
				assignPopupWin('.creditRepayPlan');
			}else{
				popupWin(data.msg);
			}
		}
	});
}
</script>
</html>