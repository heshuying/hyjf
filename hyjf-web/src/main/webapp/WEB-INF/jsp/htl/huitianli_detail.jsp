<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>汇天利 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
    <link rel="stylesheet" type="text/css" href="${ctx}/css/jquery-ui.css" />
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
    <div class="project-detail">
        <div class="hd-innernav">
            <ul class="subnav-inner">
                <li><a href="${ctx}/user/pandect/pandect.do">账户总览</a></li>
                <li class="active"><a href="${ctx}/htl/getUserProdcutInfo.do">汇天利</a></li>
                <li><a href="#">我的投资</a></li>
                <li><a href="#">债权转让</a></li>
                <li><a href="#">优惠券</a></li>
                <li><a href="#">交易明细</a></li>
                <li><a href="${ctx}/financialAdvisor/financialAdvisorInit.do">理财顾问</a></li>
                <li><a href="#">安全中心</a></li>
                <li><a href="#">邀请好友</a></li>
                <li><a href="#">借款管理</a></li>
            </ul>
        </div>
        <div class="container-1200">
            <div class="left-side">
                <div class="con1">
                 	   当前收益（元） <span class="iconfont iconfont-eye"></span>
                </div>
                <div class="con2">
                    <div class="income">${htlForm.notExtractInterest }</div>
                    <div class="btn btn-mid btn-default"><a href="${ctx}/htl/moveToInvestPage.do">转入</a></div>
                    <div class="btn btn-link">
     				<a href="${ctx}/htl/moveToRedeemPage.do"> 转出</a>    
                    </div>
                </div>
                <div class="con3">
                    <div class="item item1">
                        <div class="title">已存本金（元）</div>
                        <div class="content">${htlForm.userPrincipal }</div>
                    </div>
                    <div class="item item2">
                        <div class="title"><span class="fl-l">累计收益（元）</span></div>
                        <div class="content">${htlForm.extractInterest }</div>
                    </div>
                </div>
            </div>
            <div class="right-side">
                <div class="item item1">
                    <div class="title">历史年回报率</div>
                    <div class="content">${htlForm.htlRate }<span>%</span></div>
                </div>

                <div class="item item1">
                    <div class="title">可转出金额（元）</div>
                    <div class="content">${htlForm.userPrincipal }</div>
                </div>

            </div>
        </div>
    </div>
    <div class="project-tabbing">
        <div class="container-1200">
            <ul class="project-tab">
                <li panel="0" class="active">转入</li>
                <li panel="1">转出</li>
                <li panel="2">收益</li>
            </ul>
            <div class="date-range">
                <input type="text" id="from" class="datepicker">至<input type="text" id="to" class="datepicker">
                <div class="btn" id="searchByDate">搜索</div>
            </div>
            <div class="clearfix"></div>
            <ul class="project-tab-panel">
                <li panel="0" class="active" id="productlist">
                    <table>
                        <tbody>
                        </tbody>
                    </table>
                     <div class="new-pagination" id="productlistPage"> </div>
                </li>
                <li panel="1"  id="productredeem">
                    <table>
                        <tbody>
                        </tbody>
                    </table>
                    <div class="new-pagination" id="productredeemPage"> </div>
                </li>
                <li panel="2" id="productinterest">
                    <table>
                        <tbody>
                        </tbody>
                    </table>
                    <div class="new-pagination" id="productinterestPage"> </div>
                </li>
            </ul>
        </div>
    </div>
    
<jsp:include page="/footer.jsp"></jsp:include>
<script type="text/javascript" src="${ctx}/js/jquery-ui.js" charset="utf-8"></script>
    <script>
    $(function() {
        var today = new Date();
        $(".project-tab").click(function(e) {
            var _self = $(e.target);
            if (_self.is("li")) {
                var idx = _self.attr("panel");
                var panel = _self.parent().siblings(".project-tab-panel");
                _self.siblings("li.active").removeClass("active");
                _self.addClass("active");
                panel.children("li.active").removeClass("active");
                panel.children("li[panel="+idx+"]").addClass("active");
            }
        })

        //日期插件绑定
        $( "#from" ).datepicker({
          defaultDate: "-1w",
          numberOfMonths: 1,
          dateFormat: "yy-mm-dd",
          maxDate: today,
          onClose: function( selectedDate ) {
            $( "#to" ).datepicker( "option", "minDate", selectedDate );
          }
        });
        $( "#to" ).datepicker({
          defaultDate: today,
          dateFormat: "yy-mm-dd",
          numberOfMonths: 1,
          maxDate: today,
          onClose: function( selectedDate ) {
            $( "#from" ).datepicker( "option", "maxDate", selectedDate );
          }
        });
        //设置默认选择日期
        $( "#from" ).datepicker( "setDate", "-1w" );
        $( "#to" ).datepicker( "setDate", today );
        //日期搜索
        $("#searchByDate").click(function(){
            var fromdate = $("#from").datepicker("getDate");
            var todate = $("#to").datepicker("getDate");
 
            var param = {
        		   "dateStartStr" :$("#from").val(),
        		   "dateEndStr" :$("#to").val()
            }
            var type = $(".project-tab").children("li.active").attr("panel");
            if(type == "0"){
            	//转入
            	getProductListRecords(1,10,param);
            }else if(type == "1"){
            	//转出
            	getProductRedeemRecords(1,10,param);
            }else if(type == "2"){
            	//收益
            	getProductInterestRecords(1,10,param);
            }
        })
  });
    
    //页面初始化调用
    var initparam = {
 		   "dateStartStr" :"",
 		   "dateEndStr" :""
     }
    getProductListRecords(1,10,initparam);
    getProductRedeemRecords(1,10,initparam);
    getProductInterestRecords(1,10,initparam);
	//转入记录
	function getProductListRecords(page,limit,param){
		if(page!=null && page!="" && page!=0){
			jQuery.ajax({
				type: "POST",
				url: "${ctx}/htl/getProductListRecords.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"pageIndex":page,
					"limitPage":limit,
					"dateStartStr" : param['dateStartStr'],
	        		"dateEndStr" : param['dateEndStr']				
				},
				success: function(data){
					var pagStr = "";
						pagStr += "<tr>";
						pagStr += "<th width='40%'>交易日期</th>";
						pagStr += "<th width='40%'>金额（元）</th>";
						pagStr += "<th>状态</th>";
						pagStr += "</tr>";
					for(var i=0;i<data.lists.length;i++){
						pagStr += "<tr>";
						pagStr += "<td>"+data.lists[i].time+"</td>";
						pagStr += "<td>"+data.lists[i].amountStr+"</td>";
						pagStr += "<td>"+data.lists[i].status+"</td>";
						pagStr += "</tr>";
					}
					jQuery("#productlist tbody").html(pagStr);
					jQuery("#productlistPage").html(data.webPaginator.webPaginator);
				}
			});
		}
	};

	//转出记录
	function getProductRedeemRecords(page,limit,param){
		if(page!=null && page!="" && page!=0){
			jQuery.ajax({
				type: "POST",
				url: "${ctx}/htl/getProductRedeemRecords.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"pageIndex":page,
					"limitPage":limit,
					"dateStartStr" : param['dateStartStr'],
	        		"dateEndStr" : param['dateEndStr']
				},
				success: function(data){
					var pagStr = "";
						pagStr += "<tr>";
						pagStr += "<th width='40%'>交易日期</th>";
						pagStr += "<th width='40%'>金额（元）</th>";
						pagStr += "<th>状态</th>";
						pagStr += "</tr>";
					for(var i=0;i<data.lists.length;i++){
						pagStr += "<tr>";
						pagStr += "<td>"+data.lists[i].time+"</td>";
						pagStr += "<td>"+data.lists[i].amountStr+"</td>";
						pagStr += "<td>"+data.lists[i].status+"</td>";
						pagStr += "</tr>";
					}
					jQuery("#productredeem tbody").html(pagStr);
					jQuery("#productredeemPage").html(data.webPaginator.webPaginator);
				}
			});
		}
	};

	//收益记录
	function getProductInterestRecords(page,limit,param){
		if(page!=null && page!="" && page!=0){
			jQuery.ajax({
				type: "POST",
				url: "${ctx}/htl/getProductInterestRecords.do",
				contentType:"application/x-www-form-urlencoded;charset=utf-8",
				dataType: "json",
				data: {
					"pageIndex":page,
					"limitPage":limit,
					"dateStartStr" : param['dateStartStr'],
	        		"dateEndStr" : param['dateEndStr']
				},
				success: function(data){
					var pagStr = "";
						pagStr += "<tr>";
						pagStr += "<th width='40%'>交易日期</th>";
						pagStr += "<th width='40%'>金额（元）</th>";
						pagStr += "<th>利息（元）</th>";
						pagStr += "</tr>";
					for(var i=0;i<data.lists.length;i++){
						pagStr += "<tr>";
						pagStr += "<td>"+data.lists[i].time+"</td>";
						pagStr += "<td>"+data.lists[i].amountStr+"</td>";
						pagStr += "<td>"+data.lists[i].interestStr+"</td>";
						pagStr += "</tr>";
					}
					jQuery("#productinterest tbody").html(pagStr);
					jQuery("#productinterestPage").html(data.webPaginator.webPaginator);
				}
			});
		}
	};

    </script> 
</body>

</html>