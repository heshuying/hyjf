<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta charset="UTF-8">
		<title>充值手续费对帐 - 汇盈金服官网</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" type="text/css" href="${cdn}/css/jquery-ui.css" />
	</head>
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	
	
    <div class="container">
        <div class="deal-header">
        	<jsp:include page="/subMenu.jsp"></jsp:include>
            <p>充值手续费对帐</p>
        </div>
        <div class="new-detail-inner deal-detail-main recharge-check-main">
            <form action="${ctx}/rechargeFee/rechargeFeeList.do">
                <div class="deal-detail-filter">
                    <label for="">状态:</label>
                    <select id="dstatus" name="dstatus">
					   <option value="">全部</option>
		               <option value="0">待付款</option>
		               <option value="1">已付款</option>
                    </select>
                    <label for="">账单周期:</label>
                    <div class="date-range">	
                        <input type="text" id="startTime" name="startTime" class="datepicker">
                        至
                        <input type="text" id="endTime" name="endTime" class="datepicker">
                        <div class="btn" onclick="getFeeListPage()">搜索</div>
                    </div>
                </div>
            </form>
            <div class="clearfix"></div>
            <div class="invite-tab-panel">
                <table border="0" cellspacing="0" cellpadding="0" id="feetable" >
                </table>
            </div>
            <div class="clearfix"></div>
        </div>
        <div id="new-pagination" class="new-pagination"></div>
    </div>
	
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/js/rechargefee/rechargefee.js" type="text/javascript"></script>
	<script type="text/javascript" src="${cdn}/js/jquery-ui.js" charset="utf-8"></script>
    <script type="text/javascript" src="${cdn}/js/datepicker-zh-CN.js" charset="utf-8"></script>
	<script type="text/javascript">
    var today = new Date();
    $(function() {
        //日期插件绑定
        $("#startTime").datepicker({
            defaultDate: "-1w",
            dateFormat: "yy-mm-dd",
            maxDate: today,
            onClose: function(selectedDate) {
                $("#endTime").datepicker("option", "minDate", selectedDate);
                $(".date-range").removeClass("date-default-c");
            }
        });
        $("#endTime").datepicker({
            defaultDate: today,
            dateFormat: "yy-mm-dd",
            maxDate: today,
            onClose: function(selectedDate) {
                $("#startTime").datepicker("option", "maxDate", selectedDate);
                $(".date-range").removeClass("date-default-c");
            }
        });
        var aweekago = new Date();
    //    $( "#startTime" ).datepicker( "setDate", "-1w" );
    //    $( "#endTime" ).datepicker( "setDate", today );
    });


    </script>
	
	</body>
</html>