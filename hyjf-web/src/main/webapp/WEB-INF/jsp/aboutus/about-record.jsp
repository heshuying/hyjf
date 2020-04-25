<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu-aboutUS.jsp"></jsp:include>
	<article class="main-content">
        <div class="container">
            <section class="about-detial content">
                <div class="main-title">
                   <span class="title-head">备案信息</span>
                </div>
                <div class="main-tab jion-us">
                	<div class="event">
                    	<p>地方金融监管部门的备案登记信息： 等待验收中</p>
                    	<p>电信业务经营许可信息：等待验收中</p>
                    	<p>资金存管信息：</p>
                    	<p>- 资金存管银行：江西银行</p>
                    	<p>- 资金存管上线时间：2017-07-05</p>
                    	<p>公安机关核发的网站备案图标及编号：等待验收中</p>
                    	<p>信息安全测评认证信息：</p>
                    	<img src="${cdn}/dist/images/aboutUs/record-info@2x.png" width="710" alt="" />
                    </div>
			    </div>
            </section>
        </div>
        <div class="container" style="margin-top: 25px;">
            <section class="about-detial content">
                <div class="main-tab jion-us" style="line-height: 2;">
                	<div class="about-government">
						<p class="title"  style="margin-top: 0;">风险管理信息</p>
					</div>
					<p>一、风险管理组织架构</p>
                	<div style="text-align: center;margin-top: 20px;">
                		<img src="${cdn}/dist/images/aboutUs/record-info-2@2x.png" width="890" alt="" />
                	</div>
                	<p style="margin-top: 45px;">二、风险评估流程</p>
					<div>
						经过对借款人信息进行多步骤、综合性的判断和分析，完成对借款人多重风险评估后，由汇盈金服方出具审批意见，并决定借款项目发布与否和期限金额等相关信息。对于存在欺诈风险的借款申请，将及时进行反欺诈等调查，并根据调查结果决定是否加入平台黑名单。
					</div>
					<div style="text-align: center;margin-top: 50px;">
                		<img src="${cdn}/dist/images/aboutUs/record-info-3@2x.png" width="890" alt="" />
                	</div>
                	<p style="margin-top: 45px;">三、风险预警管理情况</p>
					<div>
						针对已经发布的借款项目，汇盈金服将做好贷中管理工作，在借款人授权的前提下通过第三方等多渠道监管借款人的相关信息，做好贷中风险控制工作，一旦发现有还款风险时，将及时跟进借款人，并采取提前还款等措施。
					</div>
					<p >四、贷后管理流程</p>
					<div>
						贷后管理是指从借贷行为发生后直到本息收回或借贷行为结束的全过程中对于借款金额实际用途的调查，借款项目风险预警，本息还款跟踪等多项管理、服务活动，只有配合完善的贷后管理制度，才能最终实现借款项目的风险控制。
					</div>
					<div style="text-align: center;margin-top: 50px;">
                		<img src="${cdn}/dist/images/aboutUs/record-info-4@2x.png" width="890" alt="" />
                	</div>
                	<div style="padding-bottom: 70px;margin-top: 45px;">
                		汇盈金服会综合多方情况统一调配催收资源，妥善执行催收工作，避免形成不良资产。还款到期日之前对客户进行适当的提醒，如果用户逾期未按时还款，还将第一时间通过短信、电话、信函等方式提醒用户进行还款。如果该借款人仍未还款，会进一步进行包括上门等一系列的催收工作。对于通过常规催收流程仍然不能实现有效回款的情况，我们将采取诉讼等法律方式。
                	</div>
			    </div>
			    <div class="detial-btn"> 
                    <a href="${ctx}/aboutus/getInformation.do">返回列表<i class="xiayibu"></i></a>
			    </div>
            </section>
        </div>
   </article>

	<jsp:include page="/footer.jsp"></jsp:include>
	<!-- 设置定位  -->
	<script>setActById("aboutInformation");</script>
	<!-- 导航栏定位  -->
	<script>setActById("indexMessage");</script>
</body>
</html>