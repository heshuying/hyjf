<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>

<script>
    var baseTableData = [{
        id: "rzzt",
        key: "融资主体",
        val: "青岛市某机械生产公司"
    }, {
        id: "zcdq",
        key: "注册地区",
        val: "山东省烟台市"
    }, {
        id: "zczb",
        key: "注册资本",
        val: "600000"
    }, {
        id: "zcsj",
        key: "注册时间",
        val: "2005年"
    }, {
        id: "fddb",
        key: "法定代表人",
        val: "刘**"
    }, {
        id: "sshy",
        key: "所属行业",
        val: "生产销售"
    }, {
        id: "zyyw",
        key: "主营业务",
        val: "环保机械的制造及销售"
    }, {
        id: "xypj",
        key: "信用评级",
        val: "AA"
    }, {
        id: "zch",
        key: "统一社会信用代码/注册号 ",
        val: "12345********"
    }];
    var assetsTableData = [
        [{
            id: "zclx",
            key: "资产类型",
            val: "住宅"
        }, {
            id: "zcwz",
            key: "资产位置",
            val: "山东省青岛市"
        }, {
            id: "zcmj",
            key: "资产面积",
            val: "345㎡"
        }, {
            id: "zcsl",
            key: "资产数量",
            val: "2"
        }, {
            id: "pgjz",
            key: "评估价值",
            val: "720万"
        }, {
            id: "zcss",
            key: "资产所属",
            val: "融资人名下/第三方名下"
        }],
        [{
            id: "zclx",
            key: "资产类型",
            val: "车辆"
        }, {
            id: "zcxh",
            key: "型号",
            val: "CR-V"
        }, {
            id: "zcgmjg",
            key: "购买价格",
            val: "193800元"
        }, {
            id: "zccph",
            key: "车牌号",
            val: "鲁F****"
        }, {
            id: "zccjh",
            key: "车架号",
            val: "L123*****"
        }, {
            id: "zcpp",
            key: "品牌",
            val: "本田"
        }, {
            id: "zcpp",
            key: "产地",
            val: "国产"
        }, {
            id: "pgjz",
            key: "评估价值",
            val: "58140元"
        }, {
            id: "zcdjd",
            key: "车辆登记地",
            val: "山东省青岛市"
        }]
    ];

    var intrTableData = [{
        id: "rzyt",
        key: "融资用途",
        val: "采购机械配件等"
    }, {
        id: "dzyw",
        key: "抵/质押物",
        val: "房产"
    }, {
        id: "cwzk",
        key: "财务状况",
        val: "融资企业2016年主营业务收入3350.01万元，净利润378.82万元；本年度截止至2017年2月底，主营业务收入681.53万元，净利润91.50万元。"
    }, {
        id: "dily",
        key: "第一还款来源",
        val: "经营收入"
    }, {
        id: "dely",
        key: "第二还款来源",
        val: "合作机构履行无限连带责任"
    }, {
        id: "fysm",
        key: "费用说明",
        val: "加入费用0元"
    }];

    var credTableData = [{
        id: "ptyqcs",
        key: "在平台逾期次数",
        val: "0"
    }, {
        id: "ptyqje",
        key: "在平台逾期金额",
        val: "0 "
    }, {
        id: "xypj",
        key: "信用评级",
        val: "AA"
    }, {
        id: "sdqk",
        key: "涉诉情况",
        val: "无/已处理"
    }];
    var reviewTableData = [{
        id: "ptyqcs",
        key: "企业证件",
        val: "已审核"
    }, {
        id: "ptyqje",
        key: "经营状况",
        val: "已审核 "
    }, {
        id: "xypj",
        key: "财务状况",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "企业信用",
        val: "无/已审核"
    }, {
        id: "sdqk",
        key: "法人信用",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "资产状况",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "购销合同",
        val: "已审核"
    }, {
        id: "sdqk",
        key: "供销合同",
        val: "已审核"
    }];
    </script>   
</head>


<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<section class="breadcrumbs">
        <div class="container">
            <div class="left-side">您所在的位置： 
            <a href="${ctx}/">首页</a> &gt; <a href="${ctx}/hjhplan/initPlanList.do">智投服务</a> &gt;服务详情</div>
        </div>
    </section>
    
    
    <article class="main-content product-zhitou">
    <div class="container">
        <!-- start 内容区域 -->
        <div class="product-intr">
            <div class="title">
                <span>${planDetail.planName}</span>
                <span class="title-tag-gray">
                	出借人适当性管理告知
               	<a class="risk-alt alt1">
               		<span class="risk-tips">
               		作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，拥有一定的金融产品出借经验并熟悉互联网金融。请您在出借前，确保了解借款项目的主要风险，同时确认具有相应的风险认知和承受能力，并自行承担出借可能产生的相关损失。
                		</span>
                		<i class="icon iconfont icon-zhu "></i>
                	</a>
                </span>
                <div class="contract-box">
                    <!--TODO:产品协议、平台协议相关修改start-->
                    <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfwxy" target="_blank" >${tzfwxy }</a>
                    <a href="${ctx}/agreement/goAgreementPdf.do?aliasName=ztfxjss" target="_blank">${ztfxjss }</a>
                    <!--TODO:产品协议、平台协议相关修改end-->
                </div>
            </div>
            <div class="attr">
                <div class="attr-item attr1">
                    <span class="val highlight">${planDetail.planApr}<span class="unit"> %<c:if test="${planDetail.isIncrease == 'true'}">+${planDetail.borrowExtraYield}%</c:if></span></span>
                    <span class="key">参考年回报率<a href="#" class="alt-el" data-alt="参考年回报率不代表实际收益"><i class="icon iconfont icon-zhu"></i></a></span>
                </div>
                <div class="attr-item attr2">
                
                <span class="val">${planDetail.planPeriod}<span class="unit"> 
                	<c:if test="${planDetail.isMonth eq 0}">
						天
					</c:if>
					<c:if test="${planDetail.isMonth eq 1}">
						个月
					</c:if>
                </span> </span>
                    <span class="key">服务回报期限</span>
                </div>
                <div class="attr-item attr3"><!-- 需要查询 ????? -->
                    <span class="val">${joinPeopleNum}<span class="unit"> 次</span></span>
                    <span class="key">服务人次</span>
                </div> 
            </div>
            <div class="list">
            	<div class="attr1"><img src="../dist/images/zhitou/icon-1@2x.png" height="18px">银行资金存管 </div>
            	<div class="attr2"><img src="../dist/images/zhitou/icon-2@2x.png" height="18px">甄选优质债权</div>
            	<div class="attr3"><img src="../dist/images/zhitou/icon-3@2x.png"  height="18px">智能分散投标</div>
            </div>
        </div>
        
        <input id="nid1" name="nid1" type="hidden" value="${planDetail.planNid }">

        <form class="product-form" action="${ctx}/hjhdetail/planInvest.do" id="productForm" autocomplete="off">
        	<input type="hidden" name="paymentAuthStatus" id="paymentAuthStatus" value="${paymentAuthStatus}" /><!-- 自动缴费状态 -->
        	<input type="hidden" name="paymentAuthOn" id="paymentAuthOn" value="${paymentAuthOn}" />
        	<input type="hidden" name="isCheckUserRole" id="isCheckUserRole" value="${isCheckUserRole}" /> <!-- 是否判断出借人 -->
        	<input type="hidden" name="roleId" id="roleId" value="${roleId}"><!-- 仅限出借人进行投资业务 -->
   		    <input type="hidden" name="tokenCheck" id="tokenCheck" value="${tokenGrant}">
            <input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}">
            <input type="hidden" name="loginFlag" id="loginFlag" value="${loginFlag }" /> <!-- 登录状态 0未登陆 1已登录 -->
           	<input type="hidden" name="openFlag" id="openFlag" value="${openFlag }" /> <!-- 开户状态 0未开户 1已开户 -->
           	<input type="hidden" name="riskFlag" id="riskFlag" value="${riskFlag }" /> <!-- 是否进行过风险测评 0未测评 1已测评 -->
           	<input type="hidden" name="setPwdFlag" id="setPwdFlag" value="${setPwdFlag }" /> <!-- 是否设置过交易密码 0未设置 1已设置 -->
           	<input type="hidden" name="autoInvesFlag" id="autoInvesFlag" value="${autoInvesFlag }" /> <!-- 自动投标授权状态 0:未授权  1:已授权 -->
           	<input type="hidden" name="forbiddenFlag" id=forbiddenFlag value="${forbiddenFlag }" /> <!-- 是否被禁用  0:未禁用  1:已禁用 -->
            <input id="nid" name="nid" type="hidden" value="${planDetail.planNid }">
            <input type="text" name="username" class="ignore fix-auto-fill"/>
            <input id="threshold" name="threshold" type="hidden" value="${threshold }">
			<!-- 神策预置属性 -->
            <input id="presetProps" name="presetProps" type="hidden" value="">
            <div class="field">
                <!--TODO：项目金额改成计划可投start-->
                <div class="key">开放额度：</div>
                <%-- <div class="val"><span class="highlight"><fmt:formatNumber value="${planDetail.availableInvestAccount}" pattern="#,###" /></span> 元</div> --%>
                <c:if test="${planDetail.planStatus eq 2}">
                	<div class="val"><span class="highlight">0.00</span> 元</div>
                </c:if>
                <c:if test="${planDetail.planStatus eq 1}">
                	<div class="val"><span class="highlight"><fmt:formatNumber value="${planDetail.availableInvestAccount}" pattern="#,##0.00" /></span> 元</div>
                </c:if>
                <!--TODO：项目金额改成计划可投end-->
            </div>
            <div class="field">
             	<div class="key">可用金额：</div>
                <c:if test="${loginFlag eq '1' }">
	                <div class="val"><fmt:formatNumber value="${userBalance }" pattern="#,##0.00" />元</div>
	                <a href="${ctx}/bank/web/user/recharge/rechargePage.do" class="link-recharge">充值</a>
                </c:if>
                <c:if test="${loginFlag eq '0' }">
	                <div class="val">登录后可见</div>
	                <a href="${ctx}/user/login/init.do" class="link-recharge" onclick="setCookie()">立即登录</a>
                </c:if>
            </div>
            <c:if test="${planDetail.planStatus eq 1}">
	   			<c:if test="${planDetail.availableInvestAccount ne '0.00'}">
		            <div class="field ">
		                <div class="input">
		                    <input type="text" name="money" id="money" 
		                    placeholder='<fmt:formatNumber value="${planDetail.debtMinInvestment }" pattern="#,###" />元起投，<fmt:formatNumber value="${planDetail.debtInvestmentIncrement }" pattern="#,###" />元递增' 
		                    oncopy="return false" onpaste="return false" oncut="return false" oncontextmenu="return false" autocomplete="off" maxlength="9" <c:if test="${debtMinInvestment ge availableInvestAccount}"> value="${availableInvestAccount}" readonly="readonly"</c:if> <c:if test="${threshold gt availableInvestAccount && availableInvestAccount ge debtMinInvestment }"> value="${availableInvestAccount}"</c:if> />
		                    <div class="btn sm <c:if test="${debtMinInvestment ge availableInvestAccount}"> btn-disabled</c:if>" <c:if test="${debtMinInvestment lt availableInvestAccount}"> id="fullyBtn"</c:if>>全投</div>
		                    <input type="hidden" id="debtMinInvestment" name="debtMinInvestment" value="${planDetail.debtMinInvestment }">
		                    <input type="hidden" id="availableInvestAccount" name="availableInvestAccount" value="${planDetail.availableInvestAccount}">
		                    <input type="hidden" id="debtInvestmentIncrement" name="debtInvestmentIncrement" value="${planDetail.debtInvestmentIncrement }">
		                    <input type="hidden" id="debtMaxInvestment" name="debtMaxInvestment" value="${planDetail.debtMaxInvestment }">
		                    <input type="hidden" id="userBalance" name="userBalance" value="${userBalance}">
		                    <input type="hidden" id="planAccountWait" name="availableInvestAccount" value="${planDetail.availableInvestAccount }">
		            		<input type="hidden" id="increase" name="increase" value="${planDetail.debtInvestmentIncrement }"><!-- 递增金额 -->
		 					<input type="hidden" id="isLast" name="isLast" value="${debtMinInvestment ge availableInvestAccount}" /><!-- 是否最后一笔投资 -->
		 					<input type="hidden" id="projectData" name="projectData" data-total="${planDetail.availableInvestAccount}" data-tendermax="${planDetail.debtMaxInvestment}" data-tendermin="${planDetail.debtMinInvestment}"/>
		                </div>
		            </div>
					<div class="field sub">
	                    <div class="key dark">优惠券：</div>
	                    <div class="val fl-r">
	                    <c:if test="${isThereCoupon==1}">
							<a href="javascript:;" class="link-coupon" id="goCoupon"><span>
								${couponConfig.couponQuotaStr }
								<c:if test="${couponConfig.couponType == 1}"> 元  </c:if><c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
								<c:if test="${couponConfig.couponType == 2}"> % </c:if><c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
							    <c:if test="${couponConfig.couponType == 3}"> 元  </c:if><c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if>
							</span>
							</a>
						</c:if>
						<c:if test="${isThereCoupon==0}">
							<!-- 是vip -->
								<a href="javascript:;" class="link-coupon" id="goCoupon"><span> 您有 <span class="num">${couponAvailableCount}</span> 张优惠券可用 </span>
								</a>
						</c:if>
	                    
	                    </div>
	                    
	                    <input type="hidden" name="couponAvailableCount" id="couponAvailableCount" value="${couponAvailableCount}"/>
	                    <input type="hidden" name="couponGrantId" id="couponGrantId" <c:if test="${isThereCoupon==1}"> value="${couponConfig.userCouponId}"</c:if>/>
	                    <input type="hidden" name="coupon" id="couponInput" value="" 
	 						<c:if test="${isThereCoupon == 1}">
		 						data-type="${couponConfig.couponType}" data-count="${couponAvailableCount}" data-val="${couponConfig.couponQuota}"
		 						data-id="${couponConfig.userCouponId}" data-txt="${couponConfig.couponName}" data-interest="${couponConfig.couponInterest}"
	 						</c:if>
	                     	<c:if test="${isThereCoupon == 0}">
		 						data-type="" data-count="0" data-id="" data-txt="请选择优惠券" data-val="" data-interest=""
	 						</c:if> />
	                </div>           
		            <div class="field sub ">
		                <div class="key dark">参考回报：</div>
		                <div class="val fl-r" id="income">${interest} 元</div>
		            </div>
		            <div class="field ">
		                <div class="btn submit" id="goSubmit">授权服务</div>
		                <input id="nid" name="nid" type="hidden" value="${planDetail.planNid }">
		            </div>
	            </c:if>
            </c:if>
             <!-- 计划开启 -->
             <c:if test="${planDetail.planStatus eq 1}">
             	<c:if test="${planDetail.availableInvestAccount eq '0.00'}">
		            <div class="field not-allow-invest">
		                <div class="later-open">
		                    <span class="text">稍后开启</span>
		                </div>
		            </div>
	             </c:if>
            </c:if>
            <!-- 计划关闭 -->
            <c:if test="${planDetail.planStatus eq 2}">
	            <div class="field not-allow-invest">
	                <div class="later-open">
	                    <span class="text">稍后开启</span>
	                </div>
	            </div>
            </c:if>
            <!-- 投资对话框 -->
            <input type="checkbox" name="termcheck" class="form-term-checkbox" id="productTerm" >
            <div class="dialog dialog-alert" id="confirmDialog">
                <div class="title">授权确认</div>
                <div class="content">
                    <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="140" align="right"><span class="dark">智投名称：</span></td>
                            <td><span id="prodNum">${planDetail.planName}</span></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="dark">参考年回报率：</span></td>
                            <td><span id="">${planDetail.planApr}%</span></td>
                        </tr>
                        <tr>
                            <td align="right"><span class="dark">服务回报期限：</span></td>
                            <td>${planDetail.planPeriod}
	                            <c:if test="${planDetail.isMonth eq 0}">
									天
								</c:if>
								<c:if test="${planDetail.isMonth eq 1}">
									个月
								</c:if>	
                            </td>
                        </tr>
                        <tr>
                            <td align="right"><span class="dark">回款方式：</span></td>
                            <td>退出完成后，一次性还本付息至出借人的银行存管账户。
                            <%-- <c:choose>
				                <c:when test="${planDetail.borrowStyle == 'endday'}">
				                	按日计息，到期还本还息
				                </c:when>
				                <c:otherwise>
				                	按月计息，到期还本还息
			                	</c:otherwise>
			                </c:choose>
			                --%>
			                </td>
                            <%-- ${planDetail.borrowStyleName} --%>
                        </tr>
                   	    <tr>
                            <td align="right"><span class="dark">授权服务金额：</span></td>
                            <td><span class="red" id="confirmmoney"></span> 元</td>
                        </tr>
                        <tr>
                            <td align="right"><span class="dark">优惠券：</span></td>
                            <td id="confirmcoupon">
                            	<c:if test="${isThereCoupon==1}">
									<span>${couponConfig.couponQuotaStr }
									<c:if test="${couponConfig.couponType == 1}"> 元  </c:if> 
									<c:if test="${couponConfig.couponType == 2}"> % </c:if>
									<c:if test="${couponConfig.couponType == 3}"> 元  </c:if>
									<c:if test="${couponConfig.couponType == 1}"> 体验金 </c:if> 
									<c:if test="${couponConfig.couponType == 2}"> 加息券 </c:if> 
									<c:if test="${couponConfig.couponType == 3}"> 代金券 </c:if></span>
								</c:if>
							</td>
                        </tr>
                        <tr>
                            <td align="right"><span class="dark">参考回报：</span></td>
                            <td ><span id='confirmincome'>${interest}</span> 元</td>
                        </tr>
                    </table>
                    <div class="cutline"></div>
                    <table class="product-confirm-table" cellspacing="0" cellpadding="0">
                        <tr>
                            <td width="140" align="right"><span class="dark">实际支付：</span></td>
                            <td><span class="total" id="confirmpaymentmoney"></span> 元</td>
                        </tr>
                    </table>
                    <div class="product-term">
                        <div class="term-checkbox"></div>
                        <span>我已阅读并同意 
                        	<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfwxy" target="_blank" >${tzfwxy }</a>
                    		<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=ztfxjss" target="_blank">${ztfxjss }</a>
                        </span>
                    </div>
                    <div style="padding-left: 50px; color: #999;font-size: 12px;margin-top: 2px;"><img src="${cdn}/dist/images/icon-star.png"  alt="" style="width:10px;margin-left:2px;height: 10px;margin-right:8px;position: relative;top: -13px;"/><span style="display:inline-block">您应在中国大陆操作，因在境外操作导致的后果将由您<br>独自承担。</span></div>
                </div>
                <div class="btn-group">
                    <div class="btn btn-default md" id="confirmDialogCancel">取 消</div>
                    <div class="btn btn-primary md" id="confirmDialogConfirm">确 认</div>
                </div>
            </div>
            <div class="dialog dialog-alert dialog-coupon" id="couponDialog">
				<div class="title">请选择优惠券</div>
				<div class="content">
					<div class="alert-coupon-content">
						<div id="available"></div>
						<div class="cutline"></div>
						<div id="unavailable"></div>
					</div>
				</div>
				<div class="btn-group">
					<div class="btn btn-default md" id="couponDialogCancel">取 消</div>
					<div class="btn btn-primary md" id="couponDialogConfirm">确 认</div>
				</div>
			</div>
        </form>
        <!-- end 内容区域 -->
    </div>
    <div class="container">
        <!--TODO:项目流程图改成计划流程图start-->
        <section class="content">
            <div class="main-title">
                        服务流程
                <span class="title-tag-gray">
                	平台不承诺退出时效
                	<a class="risk-alt">
                		<span class="risk-tips" style="">
			               	 汇盈金服展示的参考回报不代表对实际回报的承诺；您的出借本金及对应回报可能无法按时收回。服务回报期限届满，系统对尚未结清标的自动发起债权转让。退出完成需债权标的全部结清，并且债权转让全部完成。您所持债权转让完成的具体时间，视债权转让市场交易情况而定。
                		</span>
                		<i class="icon iconfont icon-zhu "></i>
                	</a>
                </span>
            </div>
            <div class="flow-content plan">
				<div class="process-box process1">
					<span class="process-img"></span>
					<em style="width: 140px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
					<div class="text" style="">授权服务</div>
					<div class="process-text">智能投标,<br/>待匹配标的全部放款。</div>
				</div>
				<div class="process-box process2">
					<span class="process-img"></span>
					<em style="width: 356px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
					<div class="text" style="">开始计息</div>
					<div class="process-text">进入服务回报期，<br>资金循环出借，并持续计息。</div>
				</div>
				<div class="process-box process3">
					<span class="process-img"></span>
					<em style="width: 169px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
					<div class="text" style="">开始退出</div>
					<div class="process-text">服务回报期结束，<br />发起债转，待转让全部完成。</div>
				</div>
				<div class="process-box process4">
					<span class="process-img"></span>
					<em style="width: 101px;"><img src="/dist/images/zhitou/forward@2x.png"/></em>
					<div class="text" style="">退出完成</div>
					<div class="process-text"><br/>系统清算</div>
				</div>
				<div class="process-box process5">
					<div class="text" style="">本息回款</div>
					<span class="process-img"></span>
				</div>
            </div>
        </section>
        <!--TODO:项目流程图改成计划流程图end-->
    </div>
    <div class="container">
        <section class="content">
            <div class="main-tab">
                <ul class="tab-tags">
                    <li class="active" panel="0"><a href="javascript:;">智投介绍</a></li>
                    <li panel="1"><a href="javascript:;" class="consumeClass" data-page="1">标的组成</a></li><!-- 原债权列表 -->
                    <li panel="2"><a href="javascript:;" class="investClass" data-page="1">服务记录</a></li>
                    <li panel="3"><a href="javascript:;">常见问题</a></li>  
                </ul>
                <input type="hidden">
                <ul class="tab-panels">
                    <!--TODO:项目详情修改为计划介绍start-->
                    <li class="active" panel="0">
                        <div class="attr-table" id="planIntro">
                            <table cellpadding="0" cellspacing="0">
                                <tbody>
                                <tr>
                                    <td width="100%">
                                        <span class="key">服务介绍</span>
                                        <span class="val">${planIntroduce}</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td width="100%">
                                        <span class="key">授权服务期限</span>
                                        <span class="val">自授权出借之日起至退出完成之日</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">服务回报期限</span>
                                        <span class="val">${planDetail.planPeriod}
                                        	<c:if test="${planDetail.isMonth eq 0}">
												天
											</c:if>
											<c:if test="${planDetail.isMonth eq 1}">
												个月
											</c:if>
											（自授权出借资金全部放款成功之日开始算起）
                                        </span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">参考年回报率</span>
                                        <span class="val">${planDetail.planApr}% （仅代表当前服务回报期限的参考年回报率，并不代表实际匹配标的的借款利率；参考回报不代表对实际回报的承诺，请以实际到账的回报为准。）</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">授权出借条件</span>
                                        <span class="val">出借金额${planDetail.debtMinInvestment}元起，且以${planDetail.debtInvestmentIncrement}元的倍数递增。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">授权出借范围</span>
                                        <span class="val">服务的标的组成及被转让债权</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">建议出借者类型</span>
                                        <span class="val">${planDetail.investLevel}及以上</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">计息时间</span>
                                        <span class="val">智投服务回报期：从授权出借本金全部放款成功之日开始，至服务回报期限届满。<br/>
匹配标的计息期：鉴于智投服务仅是自动投标授权服务，出借资金将分散匹配债权标的，故而分散出借的资金并无统一的计息时间，而是以每笔出借资金所匹配标的的实际放款时间为准。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">服务结束方式</span>
                                        <span class="val">服务回报期限届满，授权服务开始退出。退出完成后，授权服务结束。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">开始退出</span>
                                        <span class="val">服务回报期限届满，系统对尚未结清标的自动发起债权转让。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">退出完成</span>
                                        <span class="val">债权标的全部结清，并且债权转让全部完成。您所持债权转让完成的具体时间，视债权转让市场交易情况而定。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">回款方式</span>
                                        <span class="val">退出完成后，一次性还本付息至出借人的银行存管账户。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">提现说明</span>
                                        <span class="val">本息回款后，用户自行操作账户余额提现至平台绑定银行卡中。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">费用说明</span>
                                        <span class="val">加入费用：0%；<br/>服务费用：超出参考回报部分作为服务费用，参见<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfwxy" target="_blank" class="agree">${tzfwxy }</a>。</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td>
                                        <span class="key">服务协议</span>
                                        <span class="val"><a href="${ctx}/agreement/goAgreementPdf.do?aliasName=tzfwxy" target="_blank" class="agree">${tzfwxy }</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="${ctx}/agreement/goAgreementPdf.do?aliasName=jjfwjkxy" target="_blank" class="agree">${jjfwjkxy }</a></span>
                                    </td>
                                </tr>
                                 <tr>
                                    <td>
                                        <span class="key">风险提示</span>
                                        <span class="val">政策风险：因国家宏观政策和相关法律法规发生变化，将有可能影响投标及信息服务的正常提供。<br/>信用风险：当借款标的的借款人短期或者长期丧失还款能力，或者借款人的还款意愿发生变化时，借出人的本金和收益资金可能无法部分或全部回收。<br/>退出风险：服务退出时，出借人持有的债权将转让平台的其他出借人，债权转让完成的具体时间，视债权转让市场交易情况而定，不排除无法成功转让的可能，如转让不成功，出借人需继续持有相应的债权。平台不对债权转让完成的时间以及债权转让能否全部成功实现做出任何承诺，用户因债权转让申请未成功完成，将面临资金不能变现、丧失其他出借机会的风险。<br/>操作风险：不可预测或无法控制的系统故障、设备故障、通讯故障、停电等突发事故将有可能给借出人造成一定损失。由于通信故障、系统故障以及其他不可抗力等因素的影响，可能导致借出人无法及时作出合理决策，造成借出人损失。<br/>特别提醒：前述风险提示不能穷尽全部风险及市场的全部情形。</span>
                                    </td>
                                </tr>
                                 <tr>
                                    <td>
                                        <span class="key">出借人适当性管理告知</span>
                                        <span class="val">作为网络借贷的出借人，应当具备出借风险意识，风险识别能力，拥有一定的金融产品出借经验并熟悉互联网金融。请您在出借前，确保了解借款项目的主要风险，同时确认具有相应的风险认知和承受能力，并自行承担出借可能产生的相关损失。</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </li>
                    <!--TODO:项目详情修改为计划介绍end-->
                    <!--TODO:投资记录修改为债权列表start-->
                    <li panel="1" class="">
                        <!-- 标的组成 -->
                        <p><span class="dark" style="padding-left:70px;">标的组成仅限于平台发布的借款标的或服务中被转让债权；标的组成动态变化，具体以实际出借为准。</span></p>
                        	<c:if test="${loginFlag eq '1'}"> <!-- 登录-->
                        		<%-- <c:if test="${openFlag eq '1'}"> <!-- 开户--> --%>
		                        	<div class="attr-table">
		                        		<div class="ul-tit">
	                                        <div>项目编号</div>
		                                    <div>借款人</div>
		                                    <div>项目期限</div>
		                                    <!-- <th width="13%">借款金额</th> -->
		                                    <div>借款用途</div>
		                                    <div>还款方式</div>
                                        </div>
		                                <ul id="projectConsumeList">
		                                </ul>
		                                <div id="projectConsumeListNone"></div>
										<div class="pages-nav" id="consume-pagination"></div>
		                            </div>
		                        <%-- </c:if> --%>
		                    </c:if>
		                    <!-- 未登录状态 -->
                            <c:if test="${loginFlag eq '0' }">                           
	                            <div class="unlogin">
	                                <div class="icon"></div>
	                                <p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
	                            </div>
                            </c:if>          
                    </li>
                    <!--TODO:投资记录修改为债权列表end-->
                    <!--TODO:还款计划修改为加入记录start-->
                    <li panel="2" class="">
                    <c:if test="${loginFlag eq '1'}"> <!-- 登录-->
                    	<%-- <c:if test="${openFlag eq '1'}"> --%> <!-- 开户-->
	                        <p>
	                            <span>服务人次 : <span  id="investTimes"></span></span>&nbsp;&nbsp;
	                            <span>授权金额 : <span  id="investTotal"></span>元</span>
	                        </p>
	                        <div class="attr-table">
                                <table cellpadding="0" cellspacing="0">
                                    <thead>
                                        <tr>
                                            <th width="35%">授权人</th>
                                            <th width="25%">授权服务金额（元）</th>
                                            <th width="15%">来源</th>
                                            <th width="25%">授权服务时间</th>
                                        </tr>
                                    </thead>
                                    <tbody id="projectInvestList">
                            		</tbody>
                                </table>
                                <div class="pages-nav" id="invest-pagination"></div>
                            </div>
                    	<%-- </c:if> --%>  
                    </c:if>     
            		<!-- 未登录状态 -->
                    <c:if test="${loginFlag eq '0' }">                           
                     	<div class="unlogin">
                         	<div class="icon"></div>
                         	<p>请先 <a href="${ctx}/user/login/init.do">登录</a> 或 <a href="${ctx}/user/regist/init.do">注册</a> 后可查看</p>
                     	</div>
                    </c:if>      
                    </li>
                    <!--TODO:还款计划修改为加入记录end-->
                    <!--TODO:常见问题进行修改start-->
                    <li panel="3">
                        <!-- 常见问题 -->
                        <div class="attr-table">
                            <table cellpadding="0" cellspacing="0">
                                	<tbody>
	                                    <tr>
	                                        <td width="100%">1、"智投服务"安全吗？</td>
	                                    </tr>
	                                    <tr>
	                                        <td>
	                                            <span class="dark">汇盈金服以严谨负责的态度对每笔借款进行严格筛选，同时，"智投服务"所对应借款均适用汇盈金服用户利益保障机制。</span>
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td width="100%">2、"智投服务"的"服务回报期限"是什么？</td>
	                                    </tr>
	                                    <tr>
	                                        <td>
	                                            <span class="dark">
	                                               	出借人通过“智投服务”授权出借，待授权出借资⾦全部放款成功之后进入服务回报期，并开始计息。服务回报期内，用户不可以提前退出。
	                                            </span>
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td width="100%">
												3、授权出借"智投服务"的用户所获收益处理方式有几种？
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td>
	                                            <span class="dark">
	                                                本服务提供循环复投的收益处理方式。服务退出后，用户的本金和收益将返回至其汇盈金服账户中。
	                                            </span>
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td width="100%">
	                                            4、"智投服务"通过何种方式实现自动投标？
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td>
	                                            <span class="dark">
	                                            "智投服务"不设立平台级别的中间账户，不归集出借人的资金，而是为出借人开启专属智投账户，所有资金通过该专属智投账户流动。
	                                            </span>
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td width="100%">
												5、"智投服务"到期后，我如何退出并实现收回本息？
	                                        </td>
	                                    </tr>
	                                    <tr>
	                                        <td>
	                                            <span class="dark">
	                                                "智投服务"到期后，平台会根据出借人的授权办理其所持有债权的转让，届时退出所需的时间取决于平台的债权转让市场交易的情况，汇盈金服对债权的转让时效性不做保证。
	                                            </span>
	                                        </td>
	                                    </tr>
                                    </tbody>
                                </table>
                            </table>
                        </div>
                    </li>
                    <!--TODO:常见问题进行修改end-->
                </ul>
            </div>
        </section>
    </div>
</article>
	<jsp:include page="/footer.jsp"></jsp:include>
    <div class="alert" id="authInvesPop" style="margin-top: -154.5px;width:350px;display: none;">
        <div onclick="utils.alertClose('authInvesPop')" class="close">
            <span class="iconfont icon-cha"></span>
        </div>
        <div class="icon tip"></div>
        <div class='content prodect-sq'>

        </div>
        <div class="btn-group">
            <a class="btn btn-primary single" id="authInvesPopConfirm">立即授权</a>
        </div>
    </div>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.metadata.js"></script>
    <script src="${cdn}/dist/js/product/data-format.js"></script>
    <script src="${cdn}/dist/js/lib/jquery.jcountdown.min.js"></script>
    <script type="text/javascript">
        utils.alt('.attr1 .key .alt-el');
        document.cookie = 'authUrl='+window.location.href+';path=/'
    </script>
    <script>setActById("indexPlan");</script>
    <script type="text/javascript">
        document.cookie = 'authPayUrl='+window.location.href+';path=/'
    </script>
    <script>
        /*
            详情页浏览
            detail_view_screen
            product_type  产品类型  String [标的详情，计划详情，债转详情]
            project_id  项目编号  String 项目id，计划id，债转id
            project_name  项目名称  String
          */
        var projectType = "智投";
        sa && sa.track('detail_view_screen',{
            product_type: "智投详情",
            project_id: "${planDetail.planNid }"
        })
        /*
        详情页投资
        before_tender
        entrance  入口  String 是从首页还是列表页来
        project_tag 项目标签  String 新手专享，普通标，智投，债转
        project_id  项目编号  String 1001，1003，1004
        project_name  项目名称  String A，B，C
        project_duration  项目期限  Number  1，3，6，12
        duration_unit 期限单位  String 天，月
        project_apr 历史年回报率  Number  0.04，0.12
        discount_apr  折让率 Number  债转有，没有就为空
        project_repayment_type  还款方式  String 按月付息到期还本，等额本息，一次性还本付息

      */
        var isMonth = "${planDetail.isMonth}";
        var borrowStyle = "${planDetail.borrowStyle}";
        var duration_unit = "月"
        var project_repayment_type = "按月计息，到期还本还息";
        if(isMonth == "0") {
            duration_unit = "天"
        }
        if(borrowStyle == 'endday'){
            project_repayment_type = "按日计息，到期还本还息";
        }
        var customProps = {
            entrance: document.referrer,
            project_tag:projectType,
            project_id:"${planDetail.planNid}",
            project_duration: Number("${planDetail.planPeriod}"),
            duration_unit:duration_unit,
            project_apr: Number("${planDetail.planApr}")/100,
            project_repayment_type:project_repayment_type
        }
        document.cookie = 'beforeUrl='+window.location.href+';path=/'
    </script>
    <script src="${cdn}/dist/js/product/hjh-product-detail.js?version=201806051419"></script>
	<script type="text/javascript">
    	$('.risk-alt').hover(function(){
    		var alt=$(this).find('.risk-tips');
			$(this).find('.risk-tips').stop().fadeIn(150);
    	},function(){
    		$(this).find('.risk-tips').stop().fadeOut(150);
    	})
    </script>

</body>
</html>