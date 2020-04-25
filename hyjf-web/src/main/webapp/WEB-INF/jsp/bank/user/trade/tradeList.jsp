<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="zh-cmn-Hans">

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
		<jsp:include page="/head.jsp"></jsp:include>
		<link rel="stylesheet" href="${cdn}/dist/css/lib/bootstrap-datepicker3.standalone.css" />
	</head>
	
	<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
	
	<input id="tabStatus" name="tabStatus" type="hidden" value=""></input><!-- 哪个tab -->
	<input id="trade" name="trade" type="hidden" value=""></input><!-- 下来菜单选中的类型 -->
	<input id="startDate" name="startDate" type="hidden" value=""></input><!-- 开始日期 -->
	<input id="endDate" name="endDate" type="hidden" value=""></input><!-- 结束日期 -->
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="loan-tradedetails">
                <div class="trade-main">
                    <div class="top">
                    	
                    	<!-- 交易类型下拉列表-->
	                    <div class="top-fl" id="type">
	                   	 	<label class="fl">交易类型 </label>
			                    <div id="divselect">
								 	  <cite class="cite">所有<i class="icon iconfont icon-ananzuiconv265"></i></cite>
								 	  <input name="" type="hidden" value="" id="inputselect"/>
								 	  <ul class="ui-ul">
								 	  	  <li><a href="javascript:void(0)" data-val="">所有</a></li>
									 	  <c:forEach items="${trades }" var="tradetype" begin="0" step="1" varStatus="status">
										 	 <li><a href="javascript:void(0)" data-val="${tradetype.value }"> <c:out value="${tradetype.name }"></c:out></a></li>
									      </c:forEach> 
								      </ul>
								</div>
						</div>
						
                    	<!-- 时间查询 -->
                        <div class="top-fr">
                            <div class="loan-divright">
                                <label>时间</label>
                                <input type="text" class="date start" id="start"  value=''>
                                <label>至</label>
                                <input type="text" class="date end" id="end"  value=''>
                                <a href="javascript:;"  onclick="getListByType()" class="find-btn">查 询</a>
                            </div>
                        </div>
                        
                    </div>
                    <div class="list">
                    	<!-- 以下 ul 不用动 -->
                        <ul class="tab-tags">
                            <li class="active" panel="0"><a href="#">交易明细</a></li>
                            <li panel="1"><a href="#">充值记录</a></li>
                            <li panel="2"><a href="#">提现记录</a></li>
                        </ul>
                        
                        <ul class="tab-panels">
                        	<!-- 交易明细tab -->
                            <li class="active" panel="0">
                                <table class="loan-div">
                                    <thead id="tradeDetailHead">
                                        <tr>
                                            <th class="ui-list-title pl1">时间</th>
                                            <th class="ui-list-title pl2">收支类型</th>
                                            <th class="ui-list-title pl3">交易类型</th>
                                            <th class="ui-list-title pl4">交易金额</th>
                                            <th class="ui-list-title pl5">可用余额 <a href="#" class="alt-el" data-alt="订单操作账户余额"><i class="icon iconfont icon-zhu"></i></a></th><!-- 叹号链接 -->
                                            <th class="ui-list-title pl6">状态</th>
                                            
                                            <!-- 用户角色1投资人2借款人3担保机构 -->
                                            <c:choose>
                                               	<c:when test="${roleId == '1'}">
                                            		<th class="ui-list-title pl7">操作平台</th>
                                            	</c:when>
                                            	<c:when test="${roleId == '2'}">
                                            		<th class="ui-list-title pl7">备注</th>
                                            	</c:when>
                                            	<c:when test="${roleId == '3'}">
                                            		<th class="ui-list-title pl7">操作平台</th>
                                            	</c:when>
                                            </c:choose>  
                                        </tr>
                                    </thead>
                                    <tbody id="tradeDetailList">
                                    	<tr><td colspan="7"><div class="loading"><div class="icon"><div class="text">Loading...</div></div></div></td></tr>
									</tbody><!--AJAX列表-->
                                </table>
                                <div class="pages-nav" id="trade-pagination"></div><!--  分页 -->
                            </li>
                            
                            <!-- 充值记录tab -->
                            <li panel="1">
                                <table class="loan-div">
                                    <thead>
                                        <tr>
                                            <th class="ui-list-title pl1">充值时间</th>
                                            <th class="ui-list-title pl2">充值金额</th>
                                            <th class="ui-list-title pl3">充值手续费</th>
                                            <th class="ui-list-title pl4">到账金额</th>
                                            <th class="ui-list-title pl5">状态</th>
                                            <th class="ui-list-title pl6">操作平台</th>
                                        </tr>
                                    </thead>
 									<tbody id="rechargeList">  </tbody>
                                </table>
                                <div class="pages-nav" id="recharge-pagination">
                                	<tr><td colspan="6"><div class="loading"><div class="icon"><div class="text">Loading...</div></div></div></td></tr>
                                </div><!--  分页 -->
                            </li>
                            
                            
                            <!-- 提现记录tab -->
                            <li panel="2">
                                <table class="loan-div">
                                    <thead>
                                        <tr>
                                            <th class="ui-list-title pl1">提现时间</th>
                                            <th class="ui-list-title pl2">提现金额</th>
                                            <th class="ui-list-title pl3">提现手续费</th>
                                            <th class="ui-list-title pl4">到账金额</th>
                                            <th class="ui-list-title pl5">状态</th>
                                            <th class="ui-list-title pl6">操作平台</th>
                                        </tr>
                                    </thead>
                                    <tbody id="withDrawalList">
                                    	<tr><td colspan="6"><div class="loading"><div class="icon"><div class="text">Loading...</div></div></div></td></tr>
                                    </tbody>
                                </table>
                                <div class="pages-nav" id="withDrawalList-pagination"></div><!--  分页 -->
                            </li>
                        </ul>

                    </div>
                </div>
            </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/bootstrap-datepicker.min.js"></script>
    <script src="${cdn}/dist/js/loan/loan-tradedetails.js?version=${version}"></script>
    <!-- 设置定位  -->
	<script>setActById("userTrade");</script>
	</body>
</html>