<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<tiles:insertTemplate template="/jsp/layout/dialogLayout.jsp" flush="true">
	<%-- 画面的标题 --%>
	<tiles:putAttribute name="pageTitle" value="会员详情" />
	<%-- 画面主面板 --%>
	<tiles:putAttribute name="mainContentinner" type="string">
	<div class="panel panel-white no-margin">
		<div class="panel-body">
			<div class="row">
				
				<div class="col-xs-12">
					<fieldset class="margin-top-15 margin-bottom-0">
						<legend>
							基本信息
						</legend>
						<table class="table table-condensed margin-bottom-0">
							<colgroup>
								<col width="90px"></col>
								<col width="140px"></col>
								<col width="90px"></col>
								<col></col>
							</colgroup>
							<tbody>
								<tr>
									<td>用户名</td>
									<td>${detail.username}</td>
									<td>优惠券编号</td>
									<td>${detail.couponUserCode}</td>
									<td>优惠券名称</td>
									<td>${detail.couponName}</td>
								</tr>
								<tr>
									<td>优惠券类型</td>
									<td>${detail.couponTypeStr}</td>
									<td>面值</td>
									<td>${detail.couponQuota}</td>
									<td>有效期</td>
									<td>${detail.endTime}</td>
								</tr>
								<tr>
									<td>适用平台</td>
									<td>${detail.couponSystem}</td>
									<td>适用项目类型</td>
									<td>${detail.projectType}</td>
									<td>适用项目期限</td>
									<td>${detail.projectExpirationType}</td>
									
								</tr>
								<tr>
									<td>适用出借金额</td>
									<td>${detail.tenderQuota}</td>
									<td>状态</td>
									<c:if test="${detail.couponType!=1 }">
										<td colspan="3">${detail.usedFlag}</td>
									</c:if>
									<c:if test="${detail.couponType==1 }">
										<td>${detail.usedFlag}</td>
										<td>收益期限</td>
										<td>${!empty detail.couponProfitTime?detail.couponProfitTime:"--"}</td>
									</c:if>
								</tr>
								<c:if test="${detail.couponType==1 }">
									<tr>
										<td>是否与本金共用</td>
										<td colspan="5">${detail.addFlg == 0?"是":"否"}</td>
									</tr>
								</c:if>
							</tbody>
						</table>
					</fieldset>
					<fieldset class="margin-top-25 margin-bottom-0">
						<legend>
							优惠券来源
						</legend>
						<table class="table table-condensed margin-bottom-0">
							<colgroup>
								<col width="90px"></col>
								<col width="140px"></col>
								<col width="90px"></col>
								<col></col>
							</colgroup>
							<tbody>
								<tr>
									<td>来源</td>
									<td>${detail.couponFrom}</td>
									<td>操作人</td>
									<td>${detail.grantWay}</td>
									<td>获得时间</td>
									<td>${detail.addTime}</td>
								</tr>
								<tr>
									<td>内容</td>
									<td colspan="5">${detail.couponContent}</td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset class="margin-top-25 margin-bottom-0">
						<legend>
							优惠券使用
						</legend>
						<table class="table table-condensed margin-bottom-0">
							<tbody>
								<tr>
									<td>出借订单号</td>
									<td>${detail.nid}</td>
									<td>项目编号</td>
									<td>${detail.borrowNid}</td>
									<td>项目期限</td>
									<td>${detail.borrowPeriod}</td>
								</tr>
								<tr>
									<td>出借利率</td>
									<td>${detail.borrowApr}</td>
									<td>还款方式</td>
									<td>${detail.borrowStyleName}</td>
									<td>使用时间</td>
									<td>${detail.orderDate}</td>
								</tr>
								<tr>
									<td>出借金额</td>
									<td colspan="5">￥<fmt:formatNumber value="${detail.realAccount}" type="number" pattern="#,##0.00#" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
					<fieldset class="margin-top-25 margin-bottom-0">
						<legend>
							优惠券回款
						</legend>
						<c:choose>
						<c:when test="${detail.couponType eq 1 }">
						<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">项目编号</th>
										<th class="center">回款期数</th>
										<th class="center">应回款（元）</th>
										<th class="center">转账订单号</th>
										<th class="center">转账时间</th>
										<th class="center">状态</th>
										<th class="center">应回款日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty couponRecoverlist}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${couponRecoverlist }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.recoverPeriod}"></c:out></td>
													<td class="center"><c:out value="${record.recoverInterest }"></c:out></td>
													<td class="center"><c:out value="${record.transferId}"></c:out></td>
													<td class="center"><c:out value="${record.transferTime }"></c:out></td>
													<td class="center"><c:out value="${record.receivedFlg }"></c:out></td>
													<td class="center"><c:out value="${record.recoverTime }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</c:when>
						<c:otherwise>
						<table id="equiList" class="table table-striped table-bordered table-hover">
								<colgroup>
									<col style="width: 55px;" />
								</colgroup>
								<thead>
									<tr>
										<th class="center">项目编号</th>
										<th class="center">回款期数</th>
										<th class="center">应回款（元）</th>
										<th class="center">转账订单号</th>
										<th class="center">状态</th>
										<th class="center">实际回款时间</th>
										<th class="center">应回款日期</th>
									</tr>
								</thead>
								<tbody id="roleTbody">
									<c:choose>
										<c:when test="${empty couponRecoverlist}">
											<tr>
												<td colspan="12">暂时没有数据记录</td>
											</tr>
										</c:when>
										<c:otherwise>
											<c:forEach items="${couponRecoverlist }" var="record" begin="0" step="1" varStatus="status">
												<tr>
													<td class="center"><c:out value="${record.borrowNid }"></c:out></td>
													<td class="center"><c:out value="${record.recoverPeriod}"></c:out></td>
													<td class="center"><c:out value="${record.recoverInterest }"></c:out></td>
													<td class="center"><c:out value="${record.transferId}"></c:out></td>
													<td class="center"><c:out value="${record.recoverStatus }"></c:out></td>
													<td class="center"><c:out value="${record.recoverYestime }"></c:out></td>
													<td class="center"><c:out value="${record.recoverTime }"></c:out></td>
												</tr>
											</c:forEach>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
						</c:otherwise>
						</c:choose>
						
							
					</fieldset>
				</div>
			</div>
		</div>
	</div>
	</tiles:putAttribute>
	
	<%-- 画面的CSS (ignore) --%>
	<tiles:putAttribute name="pageCss" type="string">
	<style>
	.rz-icons {
		list-style: none;
		margin: 0 0 -1px 0;
		padding: 0;
	}
	.rz-icons li {
		display: inline-block;
		margin: 0;
		padding: 0;
		width: 40px;
		height: 40px;
		opacity: 0.6;
		line-height: 44px;
		top: 0;
		position: relative;
		color:#999;
	}
	.rz-icons .certified {
		background: #3B5998;
		color: #fff;
	}
	.table tbody tr td:nth-child(2n-1) {
		color:#999 !important;
	}
	fieldset {
		padding: 16px;
	}
	fieldset legend {
		font-family: "微软雅黑";
		font-size: 14px;
	}
	.qrcode {
		border: 1px solid #ccc;
		display: inline-block;
		padding: 6px;
	}
	#qrcode {
		width: 128px;
		height: 128px;
	}
	</style>
	</tiles:putAttribute>
	
	<%-- JS全局变量定义、插件 (ignore) --%>
	<tiles:putAttribute name="pageGlobalImport" type="string">
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/qrcode/jquery.qrcode.js"></script>
		<script type='text/javascript' src="${themeRoot}/vendor/plug-in/qrcode/qrcode.js"></script>
	</tiles:putAttribute>
	
	<%-- Javascripts required for this page only (ignore) --%>
	<tiles:putAttribute name="pageJavaScript" type="string">
		<script type='text/javascript' src="${webRoot}/jsp/coupon/tender/hzt/tenderDetail.js"></script>
	</tiles:putAttribute>
	
</tiles:insertTemplate>