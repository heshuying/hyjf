<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="userslist:INFO">
	<tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
		<%-- 画面的标题 --%>
		<tiles:putAttribute name="pageTitle" value="会员信息" />

		<%-- 画面主面板标题块 --%>
		<tiles:putAttribute name="pageFunCaption" type="string">
			<h1 class="mainTitle">会员信息</h1>
			<span class="mainDescription">本功能可以查看会员信息。</span>
		</tiles:putAttribute>

		<%-- 画面主面板 --%>
		<tiles:putAttribute name="mainContentinner" type="string">
			<div class="container-fluid container-fullw bg-white">
				<div class="row">
					<div class="col-md-12">
						<div class="search-classic">
								<%-- 功能栏 --%>
								<div class="well">
									<a class="btn btn-sm">会员详情</a>
									<a class="btn btn-sm pull-right">最后登录时间：${usersDetailForm.lastLoginTime}</a>
									<a class="btn btn-sm pull-right">最后登录IP：${usersDetailForm.lastLoginIP}</a>
								</div>
								<hr/>
							<div class="container">
								<div class="row clearfix">
									<div class="col-md-6 column">
										<div class = "title"><span>用户信息</span>&emsp;<span>用户ID:${usersDetailForm.userId}</span></div>
										<br/>
										<c:if test="${usersDetailForm.iconUrl == null}">
											<img alt="会员头像" src="${themeRoot}/images/admin/touxiang.png" width="120" height="120" class = "imgStyle"/>
										</c:if>
										<c:if test="${usersDetailForm.iconUrl != null}">
											<img alt="会员头像" src="${hostUrl}/data/upfiles/image/${usersDetailForm.iconUrl}" width="120" height="120" class = "imgStyle"/>
										</c:if>
										
										<div class = "ulStyle">
											<ul>
												<li><span>用户名</span><span class = "fontContent">${usersDetailForm.userName}</span></li>
												<li><span>用户昵称</span><span class = "fontContent">${usersDetailForm.userNickName}</span></li>
												<%-- <li><span>用户属性</span><span class = "fontContent colorBut colorOrange">${usersDetailForm.userProperty}</span></li> --%>
												<li><span>用户角色</span><span class = "fontContent colorBut colorGreen ">${usersDetailForm.role}</span></li>
												<c:if test="${usersDetailForm.role =='借款人'}">
													<li><span>借款人类型</span><span class = "fontContent colorBut colorGreen ">${usersDetailForm.borrowerType == 1 ? '内部机构':(usersDetailForm.borrowerType == 2 ? '外部机构':'') }</span></li>
												</c:if>
											</ul>
										</div>
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
											<ul>
												<li><span>手机</span><span class = "fontContent"><hyjf:asterisk value="${usersDetailForm.mobile }" permission="userslist:HIDDEN_SHOW"></hyjf:asterisk></span></li>
												<li><span>邮箱</span><span class = "fontContent">${usersDetailForm.email}</span></li>
												<li><span>真实姓名</span><span class = "fontContent">${usersDetailForm.realName}</span></li>
												<li><span>身份证</span><span class = "fontContent"><hyjf:asterisk value="${usersDetailForm.idCard}" permission="userslist:HIDDEN_SHOW"></hyjf:asterisk></span></li>
												<li><span>推荐人</span><span class = "fontContent">${usersDetailForm.recommendName}</span></li>
												<%--<li><span>所属部门</span><span class = "fontContent">${usersDetailForm.departName}</span></li>--%>
											</ul>
										</div>
										<!-- 第三方绑定信息信息 -->
										
										
										<c:if test="${bindUsers.id != null}">
										<div class = "clearFloat"></div>
											<div class = "ulStyle width100">
												<div class = "title"><span>汇晶社绑定信息</span></div>
												<ul>
													<li><span>汇晶社ID</span><span class = "fontContent">${bindUsers.bindUniqueId}</span></li>
													<li><span>汇晶社绑定时间</span><span class = "fontContent"><hyjf:datetime value="${bindUsers.createTime}" ></hyjf:datetime></span></li>
												</ul>
											</div>
										</c:if>
										<!-- 开户信息 -->
										
										
										<c:if test="${userBankOpenAccount.account != null}">
										<div class = "clearFloat"></div>
											<div class = "ulStyle width100">
												<div class = "title"><span>银行开户信息</span></div>
												<ul>
													<li><span>开户状态</span><span class = "fontContent colorBut colorGreen">${userBankOpenAccount.account == null ?'未开户':'已开户'}</span></li>
													<li><span>用户类型</span><span class = "fontContent colorBut colorOrange">${userBankOpenAccount.userType}</span></li>
													<li><span>开户银行</span><span class = "fontContent">${userBankOpenAccount.bankName}</span></li>
													<li><span>银行卡号</span><span class = "fontContent">${userBankOpenAccount.bankNo}</span></li>
													<li><span>银联号</span><span class = "fontContent">${userBankOpenAccount.payAllianceCode}</span></li>
													<li><span>电子账号</span><span class = "fontContent">${userBankOpenAccount.account}</span></li>
													<li><span>开户平台</span><span class = "fontContent <c:if test="${userBankOpenAccount.openAccountPlat != null}"> colorBut colorBlue </c:if>">${userBankOpenAccount.openAccountPlat}</span></li>
													<li><span>是否设置交易密码</span><span class = "fontContent colorBut colorGreen">${usersDetailForm.isSetPassword==0?'未设置':'已设置'}</span></li>
													<li><span>开户时间</span><span class = "fontContent">${userBankOpenAccount.openAccountTime}</span></li>
                                                    <li><span>银行卡预留手机号</span><span class = "fontContent">${userBankOpenAccount.mobile}</span></li>
												</ul>
											</div>
										</c:if>
										<c:if test="${usersDetailForm.account != null}">
										<div class = "clearFloat"></div>
											<div class = "ulStyle width100">
												<div class = "title"><span>汇付开户信息</span></div>
												<ul>
													<li><span>开户状态</span><span class = "fontContent colorBut colorGreen">${usersDetailForm.account == null ?'未开户':'已开户'}</span></li>
													<li><span>用户类型</span><span class = "fontContent colorBut colorOrange">${usersDetailForm.userType}</span></li>
													<li><span>用户号</span><span class = "fontContent">${usersDetailForm.account}</span></li>
													<li><span>客户号</span><span class = "fontContent">${usersDetailForm.customerAccount}</span></li>
													<li><span>开户平台</span><span class = "fontContent <c:if test="${usersDetailForm.openAccountPlat != null}"> colorBut colorBlue </c:if>">${usersDetailForm.openAccountPlat}</span></li>
													<li><span>开户时间</span><span class = "fontContent">${usersDetailForm.openAccountTime}</span></li>
												</ul>
											</div>
										</c:if>
										
										<c:if test="${enterpriseInformation != null}">
										<!-- 企业信息 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>企业信息</span></div>
											<ul>
												<li><span>证件类型</span><span class = "fontContent colorBut colorGreen">${enterpriseInformation.idType}</span></li>
												<li><span>证件号码</span><span class = "fontContent colorBut colorOrange">${enterpriseInformation.idNo}</span></li>
												<li><span>企业名称</span><span class = "fontContent">${enterpriseInformation.name}</span></li>
												<li><span>对公账号</span><span class = "fontContent">${enterpriseInformation.account}</span></li>
												<li><span>营业执照编号</span><span class = "fontContent>${enterpriseInformation.busId}</span></li>
												<li><span>税务登记号</span><span class = "fontContent">${enterpriseInformation.taxId}</span></li>
											</ul>
										</div>
										</c:if>
										
										<!-- 电子签章 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>电子签章</span></div>
											<ul>
												<li><span>CA平台</span><span class = "fontContent colorBut colorGreen">法大大</span></li>
												<li><span>CA申请状态</span><span class = "fontContent colorBut colorOrange">${usersDetailForm.isCaFlag == 1 ?'已认证':'未认证'}</span></li>
												<li><span>客户编号</span><span class = "fontContent">${certificateAuthority.customerId}</span></li>
											</ul>
										</div>
									</div>
									<div class="col-md-6 column">
									<!-- 注册信息 -->
										<div class = "ulStyle width100">
										<div class = "title"><span>注册信息 </span></div>
											<ul>
												<li><span>注册渠道</span> <span class = "fontContent">${usersDetailForm.registPidName}</span></li>
												<li><span>注册平台</span><span class = "fontContent colorBut colorBlue">${usersDetailForm.registPlat}</span></li>
												<li><span>注册时间</span><span class = "fontContent">${usersDetailForm.registTime}</span></li>
												<li><span>注册IP</span><span class = "fontContent">${usersDetailForm.registIP}</span></li>
											</ul>
										</div>
										<c:if test="${vipInfo != null}">
										<!-- VIP信息 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>VIP信息 </span></div>
											<ul>
												<li><span>VIP等级</span><span class = "fontContent colorBut colorGreen">${vipInfo.vipName}</span></li>
												<li><span>V值</span>${usersDetailForm.vipValue}</li>
												<li><span>到期时间</span><span class = "fontContent">${usersDetailForm.vipExpDate}</span></li>
											</ul>
										</div>
										</c:if>
										<!-- 邀请信息 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
											<div class = "title"><span>邀请信息 </span></div>
											<br/>
											<%-- <img alt="pic" src="${themeRoot}/images/main.png" width="120" height="120" class = "imgStyle"/> --%>
											<div class = "imgStyle">
												<div class="qrcode">
													<%--<input type="hidden" id="qrcodeValue" value="http://weixin.huiyingdai.com/index.php?s=/Wx/user/register/id/${usersDetailForm.userId}.html" />--%>
													<input type="hidden" id="qrcodeValue" value="${wechatQRUrl}" />
													<div id="qrcode"></div>
												</div>
											</div>
											<ul class = "ulStyle2">
												<li><span>邀请码</span><span>${usersDetailForm.userId}</span></li>
												<li><span>邀请链接</span><span>${webPageUrl}</span></li>
											</ul>
										</div>
										
										<c:if test="${userEvalationResult == null}">
										<!-- 测评信息 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>测评信息 </span></div>
											<ul>
												<li><span>测评状态</span><span class = "fontContent colorBut colorGreen">未测评</span></li>
												<li><span>测评平台</span></li>
												<li><span>风险测评分</span></li>
												<li><span>风险等级</span><span class = "fontContent"></span></li>
												<li><span>最近测评时间</span><span class = "fontContent"></span></li>
											</ul>
										</div>
										</c:if>
										<c:if test="${userEvalationResult != null}">
											<c:if test="${isEvalation == '1'}">
												<!-- 测评信息 -->
												<div class = "clearFloat"></div>
												<div class = "ulStyle width100">
												<div class = "title"><span>测评信息 </span></div>
													<ul>
														<li><span>测评状态</span><span class = "fontContent colorBut colorGreen">已测评</span></li>
														<li><span>测评平台</span>${userEvalationResult.instName}</li>
														<li><span>风险测评分</span>${userEvalationResult.scoreCount}</li>
														<li><span>风险等级</span><span class = "fontContent">${userEvalationResult.type}</span></li>
														<li><span>最近测评时间</span>
														<span class = "fontContent">
														<fmt:formatDate value="${userEvalationResult.createtime }" pattern="yyyy-MM-dd HH:mm:ss"/>
														</span>
														</li>
													</ul>
												</div>
											</c:if>
											
											<c:if test="${isEvalation == '2'}">
												<!-- 测评信息 -->
												<div class = "clearFloat"></div>
												<div class = "ulStyle width100">
												<div class = "title"><span>测评信息 </span></div>
													<ul>
														<li><span>测评状态</span><span class = "fontContent colorBut colorGreen">已过期</span></li>
														<li><span>测评平台</span>${userEvalationResult.instName}</li>
														<li><span>风险测评分</span>${userEvalationResult.scoreCount}</li>
														<li><span>风险等级</span><span class = "fontContent">${userEvalationResult.type}</span></li>
														<li><span>最近测评时间</span>
														<span class = "fontContent">
														<fmt:formatDate value="${userEvalationResult.createtime }" pattern="yyyy-MM-dd HH:mm:ss"/>
														</span>
														</li>
													</ul>
												</div>
											</c:if>
										</c:if>
										<!-- 紧急联系人 -->
										<div class = "clearFloat"></div>
										<div class = "ulStyle width100">
										<div class = "title"><span>紧急联系人 </span></div>
											<ul>
												<li><span>联系人</span><span class = "fontContent">${usersDetailForm.emName}</span></li>
												<li><span>电话</span><span class = "fontContent">${usersDetailForm.emPhone}</span></li>
												<li><span>与联系人关系</span><span class = "fontContent">${usersDetailForm.emRealtion}</span></li>
											</ul>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>


		<%-- 画面的CSS (ignore) --%>
		<tiles:putAttribute name="pageCss" type="string">
			<link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
			<link href="${themeRoot}/vendor/plug-in/jstree/themes/default/style.min.css" rel="stylesheet" media="screen">
			<style>
				hr{margin:10px 0;}
				.well a{color:#7d7d7d;}
				ul{list-style:none;margin:0;padding:0;}
				.imgStyle{display:block;float:left;margin-right:20px;}
				.width100{width:100%}
				.ulStyle{float:left;}
				.ulStyle2{border:1px solid #ddd;width:75%;margin-left:25%;}
				.ulStyle .ulStyle2 li{height:58px;line-height:40px;}
				.ulStyle2 li{width:90%;margin:0 auto;padding:0 5px;}
				.ulStyle2 li:last-child{border-style:none;}
				.ulStyle .title{font-size:18px;color:#363636;}
				.ulStyle ul li{line-height:30px;height:30px;font-size:12px;border-bottom:1px solid #ddd;}
				.ulStyle ul li span,.ulStyle li span{min-width:120px;display:inline-block;}
				.ulStyle ul li span.fontContent{color:#333}
				.clearFloat{height:1px;line-height:1px;clear:both;margin:20px 0;}
				.colorBut{border-radius:5px;height:22px;line-height:22px;text-align:center;min-width:auto !important;padding:0 3px;color:#fff !important;margin:0 3px;}
				.colorOrange{background:#ff6500;}
				.colorBlue{background:#0097d0}
				.colorGreen{background:#619109}
			</style>
		</tiles:putAttribute>

		<%-- JS全局变量定义、插件 (ignore) --%>
		<tiles:putAttribute name="pageGlobalImport" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/colorbox/jquery.colorbox-min.js"></script>
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/qrcode/jquery.qrcode.js"></script>
			<script type='text/javascript' src="${themeRoot}/vendor/plug-in/qrcode/qrcode.js"></script>
		</tiles:putAttribute>

		<%-- Javascripts required for this page only (ignore) --%>
		<tiles:putAttribute name="pageJavaScript" type="string">
			<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jstree/jstree.min.js"></script>
			<script type='text/javascript' src="${webRoot}/jsp/manager/users/userlist/userDetail.js"></script>
		</tiles:putAttribute>
	</tiles:insertTemplate>
</shiro:hasPermission>
