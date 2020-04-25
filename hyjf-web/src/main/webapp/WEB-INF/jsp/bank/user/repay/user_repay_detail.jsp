<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html lang="zh-cmn-Hans">

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
	<jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>

    <article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
                <div class="loan-borrower">
                    <div class="loan-borrower-top">
                    	<form id="RepayForm" action="${ctx}/bank/web/user/repay/repay.do" method="POST">
                    		<input type="hidden" name="tokenCheck" id="tokenCheck" value="" />
							<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" /> 
							<input type="hidden" name="borrowNid" id="borrowNid" value="${repayProject.borrowNid}" />
							<input type="hidden" name="roleId" id="roleId" value="${roleId}"><!-- 仅限出借人进行投资业务 -->
							<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" /> 
                    		<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" />
                    		<input type="hidden" name="repayAuthOn" id="repayAuthOn" value="${repayAuthOn}"/> 
                    		<input type="hidden" name="repayAuthStatus" id="repayAuthStatus" value="${repayAuthStatus}" />  
                    		<input type="hidden" name="isAllRepay" id="isAllRepay" value="${isAllRepay}" /> 
	                        <div class="borrower-title">
	                           ${repayProject.borrowName}&nbsp;${repayProject.borrowNid}                         
	                        </div>
					<div class="borrower-title-main">

						<c:if
							test="${repayProject.borrowStyle eq 'endmonth' or repayProject.borrowStyle eq 'principal' or repayProject.borrowStyle eq 'month'}">
							<div class="borrower-item">
								<span><img src="${cdn}/dist/images/loan-icon2.png"></span>
								<span>请选择还款期数：</span>
								<div class="borrower-tab">

									<c:if test="${repayProject.repayStatus == 0}">
									
									    <c:if test="${repayProject.onlyAllRepay ne 1}">
										<div
											class="borrower-tab-item <c:if test='${isAllRepay ne 1 }'>active</c:if>"
											data-borrow-tab='0'>
											<span class="borrower-tab-icon"></span> 当期还款
										</div>
										</c:if>
										
										<c:if
											test="${repayProject.advanceStatus ne 2 and repayProject.advanceStatus ne 3}">
											<div
												class="borrower-tab-item <c:if test='${isAllRepay eq 1 }'>active</c:if>"
												data-borrow-tab='1'>
												<span class="borrower-tab-icon"></span> 全部还款
											</div>
										</c:if>
									</c:if>

									<c:if test="${repayProject.repayStatus == 1}">
										<c:if test='${repayProject.allRepay ne 1 }'>
											<div class="borrower-tab-item active" data-borrow-tab='0'>
												<span class="borrower-tab-icon"></span> 当期还款
											</div>
										</c:if>
										<c:if test='${repayProject.allRepay eq 1 }'>
											<div class="borrower-tab-item active" data-borrow-tab='1'>
												<span class="borrower-tab-icon"></span> 全部还款
											</div>
										</c:if>
									</c:if>
								</div>
							</div>
						</c:if>

						<div class="borrower-item" data-borrow-panel="0">
							<c:if test='${repayProject.allRepay eq 1 }'>
								<span class="first">(当前：全部结清) </span>
							</c:if>

							<c:if test='${repayProject.allRepay ne 1 }'>
								<span class="first"> <c:if test='${isAllRepay ne 1 }'>
										<c:if test="${repayProject.repayPeriod > 0}">(当前：第${repayProject.repayPeriod}期)</c:if>
										<c:if test="${repayProject.repayPeriod == 0}">(当前：第1期)</c:if>
									</c:if> <c:if test='${isAllRepay eq 1 }'>
	                                         (当前：全部结清)
	                                    </c:if>

								</span>
							</c:if>
							<span class="grey">（应还 </span> <span class="orange">${repayProject.repayTotal}</span>
							<span class="grey">元，其中本金</span> <span class="blank">${repayProject.repayCapital}</span>
							<span class="grey">元，利息</span> <span class="blank">${repayProject.shouldInterest}</span>
							<span class="grey">元，还款服务费</span> <span class="blank">${repayProject.manageFee}</span>

							<c:if
								test="${repayProject.borrowStyle eq 'endday' or repayProject.borrowStyle eq 'end'}">
								<c:if test="${repayProject.advanceStatus == 1}">
									<span class="grey">元, 提前还款减息</span>
									<span class="blank"> ${repayProject.chargeInterest}</span>
								</c:if>
							</c:if>
							<c:if
							     test="${(repayProject.borrowStyle eq 'endmonth' or repayProject.borrowStyle eq 'principal' or repayProject.borrowStyle eq 'month') and (isAllRepay eq 1 or repayProject.allRepay eq 1)}">
								<c:if test="${repayProject.advanceStatus == 1}">
									<span class="grey">元, 提前还款减息</span>
									<span class="blank"> ${repayProject.chargeInterest}</span>
								</c:if>
							</c:if>

							<c:if test="${repayProject.advanceStatus == 2}">
								<span class="grey">元, 延期利息</span>
								<span class="blank"> ${repayProject.delayInterest}</span>
							</c:if>
							<c:if test="${repayProject.advanceStatus == 3}">
								<c:if test="${repayProject.delayDays > 0}">
									<span class="grey">元, 延期利息</span>
									<span class="blank"> ${repayProject.delayInterest}</span>
								</c:if>
								<span class="grey">元, 逾期利息</span>
								<span class="blank"> ${repayProject.lateInterest}</span>
							</c:if>
							<span class="grey"> 元）</span>
						</div>


						<!-- 平台登录模块 -->
						<c:choose>
							<c:when test="${isFreeze}">
								<div class="borrower-arr">还款申请已提交，正在处理中，请勿重复操作。</div>
							</c:when>
							<c:when test="${repayProject.repayStatus == 0}">
								<div class="borrower-arr">
									<label>平台登录密码</label> <input type="text" name="text"
										class="text" id="passwordTemp" onfocus="this.type='password'"
										autocomplete="off"> <input type="hidden"
										name="password" class="password" id="password"
										onfocus="this.type='password'" autocomplete="off">
									<!-- <span class="btn">确认</span> -->
									<a href="javascript:void(0)" class="btn" id="submit">确认</a>
									<c:if test='${isAllRepay ne 1 }'>
										<span data-borrow-panel="0"> <c:if
												test="${repayProject.advanceStatus == 1}">


											<c:choose>
												<c:when
														test="${(repayProject.borrowStyle eq 'endmonth' or repayProject.borrowStyle eq 'principal' or repayProject.borrowStyle eq 'month') and repayProject.chargeDays gt 0}">
													<span class="icon iconfont icon-zhu"></span>
													<span class="small">当前为当期提前还款，请谨慎操作。</span>
												</c:when>
												<c:otherwise>
													<span class="icon iconfont icon-zhu"></span>
													<span class="small">当前为提前还款${repayProject.chargeDays}天，请谨慎操作。</span>

												</c:otherwise>
											</c:choose>
										</c:if> <c:if test="${repayProject.advanceStatus == 2}">
											<span class="icon iconfont icon-zhu"></span>
											<span class="small">当前为延期${repayProject.delayDays}天还款，请谨慎操作。</span>
										</c:if> <c:if test="${repayProject.advanceStatus == 3}">
											<span class="icon iconfont icon-zhu"></span>
											<span class="small">当前为 <c:if
													test="${repayProject.delayDays > 0}">
												延期还款${repayProject.delayDays}天+
											</c:if> 逾期${repayProject.lateDays}天还款，请谨慎操作。
												</span>
										</c:if>
										</span>
									</c:if>

									<c:if test='${isAllRepay eq 1 }'>
										<span data-borrow-panel="1"> <span
												class="icon iconfont icon-zhu"></span> <span class="small">
												当前为全部结清，请谨慎操作。 </span>
										</span>
									</c:if>

								</div>
							</c:when>
							<c:when test="${repayProject.repayStatus == 1}">
								<div class="borrower-arr">还款中...</div>
							</c:when>
						</c:choose>
					</div>
				</form>
                    </div>

                    <div class="loan-borrower-bottom"   data-borrow-panel="0">
                        <div class="loan-thead">
                            <ul>
                                <li class="th1">期数</li>
                                <li>还款时间</li>
                                <li>本期应还本金</li>
                                <li>本期应还利息</li>
                                <li>还款服务费</li>
                                <li class="thlg">本期实际还款本息</li>
                                <li class="thlg">状态</li>
                                <li class="fn-left th1">详情</li>
                            </ul>
                        </div>
                        <ul class="loan-list-main">

	                        <c:if test="${!empty repayProject.userRepayList}">
	                        	<c:forEach items="${repayProject.userRepayList}" var="userRepay" begin="0" step="1" varStatus="status">
			                            <li>
			                                <div class="list-div">
		                                        <p class="th1">${userRepay.repayPeriod}</p>
		                                        <p>${userRepay.repayTime}</p>
		                                        <p class="">${userRepay.repayCapital}</p>
		                                        <p class="">${userRepay.repayInterest}</p>
		                                        <p class="">${userRepay.manageFee}</p>
		                                        <p class="thlg">${userRepay.repayAccount}</p>
		                                        <!-- 状态 -->
		                                        <c:choose>
		                                        	<c:when test="${userRepay.status == 0}"> <p class="thlg">未还款</p> </c:when>
		                                        	<c:when test="${userRepay.status == 1}"> <p class="thlg">已还款</p> </c:when>
		                                        </c:choose>

		                                        <p class="details"><a href="javascript:void(0)"><span>展开</span><i class="icon iconfont icon-addition"></i></a></p>
		                                        <div class="list-in">
		                                        	<table>
			                                            <tr>
			                                                <td></td>
			                                                <td class="grey">实际还款=</td>
			                                                <td class="grey">应还本金</td>
			                                                <td class="grey">+应还利息</td>
			                                                <td class="grey">+还款服务费</td>
			                                                <c:if test="${userRepay.advanceStatus == 1 and userRepay.chargeDays gt 0}">
			                                                	<td class="grey">-提前减息</td>
			                                                </c:if>
			                                                <c:if test="${userRepay.advanceStatus == 2}">
			                                                	<td class="grey">+延期利息</td>
			                                                </c:if>
			                                                <c:if test="${userRepay.advanceStatus == 3}">
			                                                	<c:if test="${repayProject.delayDays > 0}">
			                                                		<td class="grey">+延期利息</td>
			                                                	</c:if>
			                                                	<td class="grey">+逾期利息</td>
			                                                </c:if>
			                                            </tr>
		                                     			<!-- 以下是展开详情 -->
		                                    			<c:if test="${!empty userRepay.userRepayDetailList}">
		                                     				<c:forEach items="${userRepay.userRepayDetailList}" var="userRepayDetail" begin="0" step="1" varStatus="detailStatus">
				                                            	<tr>
					                                                <td>${userRepayDetail.userName}</td>
					                                                <td class="font-color">${userRepayDetail.repayTotal}</td>
					                                                <td>${userRepayDetail.repayCapital}</td>
					                                                <td>${userRepayDetail.repayInterest}</td>
					                                                <td>${userRepayDetail.manageFee}</td>
					                                                <c:if test="${userRepay.advanceStatus == 1 and userRepay.chargeDays gt 0}">
					                                                	<td>${userRepayDetail.chargeInterest}</td>
					                                                </c:if>
					                                                <c:if test="${userRepay.advanceStatus == 2}">
					                                                	<td>${userRepayDetail.delayInterest}</td>
					                                                </c:if>
					                                                <c:if test="${userRepay.advanceStatus == 3}">
						                                                <c:if test="${repayProject.delayDays > 0}">
				                                                			<td>${userRepayDetail.delayInterest}</td>
				                                                		</c:if>
					                                                	<td>${userRepayDetail.lateInterest}</td>
					                                                </c:if>
					                                            </tr>
		                                             		</c:forEach>
		                                    	 		</c:if>
		                                        	</table>
		                                        </div>
			                                </div>
			                            </li>
	                            </c:forEach>
	                        </c:if>
                        </ul>
                    </div>

                </div>
            <!-- end 内容区域 -->
        </div>
        	    <!-- 缴费授权返回地址 -->
    <script type="text/javascript">
		document.cookie = 'authPayUrl='+window.location.href+';path=/'
    </script>
    <!-- 缴费授权返回地址 -->
    <div class="alert" id="authInvesPop" style="margin-top: -154.5px;width:350px;display: none;">
    	<div onclick="utils.alertClose('authInvesPop')" class="close">
    		<span class="iconfont icon-cha"></span>
    	</div>
    	<div class="icon tip"></div>
    	<div class='content prodect-sq'>

    	</div>
    	<div class="btn-group">
    		<div class="btn btn-primary single" id="authInvesPopConfirm">立即开通</div>
    	</div>
    </div>


    </article>

	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="${cdn}/dist/js/lib/bootstrap-datepicker.min.js"></script>
    <script src="${cdn}/dist/js/loan/repay-detail.js?version=${version}"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.validate.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/messages_cn.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.metadata.js" charset="utf-8"></script>
	<script type="text/javascript" src="${cdn}/js/jquery.md5.js" charset="utf-8"></script>
	<script>setActById('loanManage');</script>
	<script>
	    var validator = $("#RepayForm").validate({
	        "rules":{
	            "password":{
	                "required":true,
	                "remote":"${ctx}/bank/web/user/repay/checkPassword.do"
	            }
	        },
	        "messages":{
	            "password":{
	                "required":"请输入登录密码",
	                "remote":"密码输入错误"
	            }
	        },
	        "ignore": ".ignore",
	        "onkeyup":false,
	        errorPlacement: function(error, element) {
	            element.parent().append(error);
	        },
	        submitHandler: function(form) {
	            //表单提交
	            utils.alert({
		            id: "confirmDialog",
					type:"confirm",
					content:"您确认要还款吗?",
		            fnconfirm: function(){
						//点确定执行
						utils.alertClose("confirmDialog");
						if (checkToken() == true) {
		           			form.submit();
		           		}else{
		           			utils.alert({
		    		            id: "errorDialog" ,
		    		            content:"请勿重复提交"
		           			});
		           		}
		            }
		        });
	        }

	    });
	    $("#passwordTemp").change(function(){
	    	var passwordTmp = $("#passwordTemp").val();
			$("#password").val($.md5(passwordTmp));
	    });
		$("#submit").click(function(){
			//缴费授权提示
			$("#RepayForm").submit();
			/*
			if($("#paymentAuthOn").val() == "1" && $("#paymentAuthStatus").val() != "1"){
				utils.alert({
		            id: "authInvesPop",
		            type:"confirm",
		            content:"<div>交易过程中，会收取相应费用<br>请进行授权。</div><div class='status-box'><div class='off'>如：提现手续费，债转服务费等。</div></div>",
		            alertImg:"msg",
		            fnconfirm: function(){
		                window.location.href = webPath+ "/bank/user/auth/paymentauthpageplus/page.do";
		            }
		        });
			}else if($("#roleId").val() == "2" &&$("#repayAuthOn").val() == "1" && $("#repayAuthStatus").val() != "1"){
				utils.alert({
		            id: "authInvesPop",
		            type:"confirm",
		            content:"<div>根据监管要求，请先进行还款授权。</div>",
		            alertImg:"msg",
		            fnconfirm: function(){
		                window.location.href = webPath+ "/bank/user/auth/repayauthpageplus/page.do";
		            }
		        });
			}else{
				
			}
			*/
		});

		function showTr(ele,id) {
		    $(ele).hide().siblings(".do-close").show();
		    $(ele).parent("td").parent("tr").siblings(".opentr[data-id="+id+"]").show();
		}
		function hideTr(ele,id) {
		    $(ele).hide().siblings(".do-open").show();
		    $(ele).parent("td").parent("tr").siblings(".opentr[data-id="+id+"]").hide();
		}

		$(function(){
			isIE();//如果是IE则改变输入框的属性
			function isIE(){
				if ((navigator.userAgent.indexOf('MSIE') >= 0)
					    && (navigator.userAgent.indexOf('Opera') < 0)){
						$("input[type='text']").prop("password");
					}
			}
		})


		// 提前还款tab控制
		$(".borrower-tab").bind("click",".borrower-tab-item",function(event){
			var tab = $(this);
			if(tab.hasClass("locked")){
				return false;
			}
			var current = $(event.target).hasClass('borrower-tab-icon') ? $(event.target).parent() : $(event.target);
			var idx = current.data('borrow-tab')
			if(!current.hasClass('borrower-tab-item') || current.hasClass("active")){
			  return false;
			}
			tab.children('.borrower-tab-item').removeClass('active');
			current.addClass("active");
			tab.addClass("locked");
			// 控制tab跳转对应页面
			window.location.href = location.pathname+"?borrowNid=${repayProject.borrowNid}&isAllRepay="+current.data('borrow-tab');
	    })
   </script>

</body>

</html>