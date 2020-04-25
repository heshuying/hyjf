<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta charset="utf-8" />
    <title>债权转让 - 投资项目 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
    <%-- <ul class="htl-subnav">
        <li>
            <a href="${ctx}/htl/getHtlInfo.do">
                <span class="htl-icon htl-icon1"></span>
                <span class="title">汇添金</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/project/initProjectList.do?projectType=HZT">
                <span class="htl-icon htl-icon2"></span>
                <span class="title">汇直投</span>
            </a>
        </li>
        <li>
            <a href="${ctx}/project/initProjectList.do?projectType=HXF">
                <span class="htl-icon htl-icon3"></span>
                <span class="title">汇消费</span>
            </a>
        </li>
        <li class="active">
            <a href="${ctx}/credit/webcreditlist.do">
                <span class="htl-icon htl-icon4"></span>
                <span class="title">汇转让</span>
            </a>
        </li>
    </ul> --%>
    <div class="hzt-banner" style="background-image: url(${cdn}/img/hzrbg1.jpg?version=${version});">
        <h4>为您和他人实现共赢</h4>
    </div>
    <div class="new-listing">
    <br/>
    <br/>
        <ul class="zhuanrang">
        <c:forEach items="${creditResult.data.recordList}" var="record">
            <li>
                <div class="new-title">
                    <a href="${ctx}/credit/webcredittender.do?creditNid=${record.creditNid}" class="id" >HZR${record.creditNid}</a>
                    <a href="${ctx}/credit/webcredittender.do?creditNid=${record.creditNid}" class="name">原项目${record.bidNid}</a>
                    <a href="javascript:;" class="tag">${record.borrowStyleName}</a>
                </div>
                <div class="new-content">
                    <div class="con1">
                        <div class="num highlight">${record.bidApr}<span>%</span></div>
                        <div class="con-title">历史年回报率</div>
                    </div>
                    <div class="con2">
                        <div class="num">${record.creditDiscount}%</div>
                        <div class="con-title">折让比例</div>
                    </div>
                    <div class="con3">
                        <div class="num">${record.creditTerm}天</div>
                        <div class="con-title">期限</div>
                    </div>
                    <div class="con4">
                        <div class="num">${record.creditCapital}</div>
                        <div class="con-title">金额（元）</div>
                    </div>
                    <div class="con5">
                        <div class="progress-con num">
                            <div class="progress-all">
                                <div class="progress-cur" data-percent="${record.creditInProgress}"></div>
                            </div>
                            <div class="percent"><span>${record.creditInProgress}</span>%</div>
                        </div>
                        <div class="con-title">进度</div>
                    </div>
                    <div class="con6">
                        <div class="num">
                            <a href="${ctx}/credit/webcredittender.do?creditNid=${record.creditNid}" class="new-list-btn avalible">立即投资</a>
                        </div>
                        <!-- 
                        <div class="con-title time" data-time="${record.addTimeInt + 3600*24*3}">剩 0天0小0时0秒</div>
                         -->
                    </div>
                </div>
            </li>
		</c:forEach>
		<c:if test="${fn:length(creditResult.data.recordList)<=0}">  
			<li style="margin-bottom:20px;text-align: center;">
				<div class="new-title" ><span class="id">没有债权转让信息</span></div>
			</li>
        </c:if>
        </ul>
        <div class="clearfix"></div>
        <c:if test="${fn:length(creditResult.data.recordList)>0}">  
        	<div class="new-pagination" id="pagination">${creditResult.data.paginator.webPaginator}</div>
        </c:if>
        
    </div>
    <form id="creditForm" name="myform" method="post" action='${ctx}/credit/webcreditlist.do' >
    	<input type="hidden" id="totalPages" name="totalPages" value="${creditResult.data.paginator.totalPages}" />
    	<input type="hidden" id="totalCount" name="totalCount" value="${creditResult.data.paginator.totalCount}" />
    	<input type="hidden" id="currPage" name="currPage" value="${creditResult.data.paginator.page}" />
    	<input type="hidden" id="limitPage" name="limitPage" value="${creditResult.data.paginator.limit}" />
    </form>
    
    <script>setActById('subHZR');</script>
    <jsp:include page="/footer.jsp"></jsp:include>
</body>

<script type="text/javascript">
	$(document).ready(function(){
		//进度
	    $(".progress-cur").each(function() {
	        var perc = $(this).data("percent");
	        if (perc) {
	            $(this).animate({ "width": perc });
	        }
	    });
		jQuery("#pagination a").click(function(){	
			var dataPage = jQuery(this).attr("data-page");
			if(dataPage!=null && dataPage!="" && dataPage!=0){
				jQuery("#currPage").val(dataPage);
				jQuery("#limitPage").val(8);
				jQuery("#creditForm").submit();
			}
		})
	})
</script>
</html>