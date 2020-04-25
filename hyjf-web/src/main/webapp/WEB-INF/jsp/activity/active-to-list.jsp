<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
<jsp:include page="/header.jsp"></jsp:include>	
<article class="active-to-list">
	<div class="acive-banner"><img src="${ctx}/dist/images/active/activity2018/activityToList/banner.png" alt="" /></div>
	<div class="active-tab">
		<ul>
			<li class="img-box box-1">
				<img src="${ctx}/dist/images/active/activity2018/activityToList/tab-img1.png" alt="" />
			</li>
			<li class="img-box box-2">
				<img src="${ctx}/dist/images/active/activity2018/activityToList/tab-img2.png" alt="" />
			</li>
			<li class="img-box box-3">
				<img src="${ctx}/dist/images/active/activity2018/activityToList/tab-img3.png" alt="" />
			</li>
		</ul>
		<ul class="tab-ul">
			<li class="tab-btn tab-btn-1 select">
				幸运礼盒<span>“放肆”</span>拆
			</li>
			<li class="tab-btn">
				加息不停<span>“放肆”</span>赚
			</li>
			<li class="tab-btn tab-btn-3">
				荣耀榜单<span>“放肆”</span>刷
			</li>
		</ul>
	</div>
	
	
	
	<!--
          	未登录 活动1
          -->
			<div class="active-content">
				<h2 class="title">活动内容</h2>
				<div class="active-introduce">
					活动期间，每个标的的第一笔和最后一笔投资用户（实际投资排序以满标时<br/>的顺序为准）将获得一个惊喜礼盒，内含随机无门槛红包奖励！
				</div>
				<c:if test="${ userId eq ''}">
						<div class="open-gift"><a href="${ctx}/user/login/init.do" class="login">点击登录，查看我的惊喜礼盒</a></div>
				</c:if>
				<c:if test="${ userId ne ''}">
					<c:if test="${num eq 0}">
						<div class="open-gift">您暂未获得任何惊喜礼盒哦！<a href="${ctx}/bank/web/borrow/initBorrowList.do" class="touzi">立即去投资</a></div>
					</c:if>
					<c:if test="${num eq 1}">
						<div class="open-gift"><a class="login receive">点击领取礼盒</a></div>
					</c:if>
					<c:if test="${not empty list}">
					<h2 class="title">中奖纪录</h2>
					<div class="table-box">
					
					<div class="table-bg">
						<table border="0" cellspacing="0" cellpadding="0">
							<thead>
								<tr>
							      <th width="16.6%">标的编号</th>
							      <th width="16.6%">投资时间</th>
							      <th width="16.6%">投资金额</th>
							      <th width="16.6%">首尾笔订单</th>
							      <th width="16.6%">获得奖励</th>
							      <th width="16.6%">领取时间</th>
							    </tr>
							</thead>
							<tbody>
							
								<c:forEach items="${list}" var="item" begin="0" step="1" varStatus="status">
									<tr>
										<td>${item.number}</td>
										<td><hyjf:datetime value="${item.createTime }" pattern="yyyy-MM-dd"></hyjf:datetime></td>
										<td><fmt:formatNumber type="number" value="${item.investment/100}" maxFractionDigits="0"/>元</td>
										<td>
											<c:if test="${item.type eq 0}">
												首
											</c:if>
											<c:if test="${item.type eq 1}">
												尾
											</c:if>
										</td>
										<td>${item.reward}元代金券</td>
										<td><hyjf:datetime value="${item.updateTime }" pattern="yyyy-MM-dd"></hyjf:datetime></td>
									</tr>
									
								</c:forEach>
							</tbody>
						</table>
					</div>
					</div>
				</c:if>
				</c:if>
				<h2 class="title">奖励发放</h2>
				<div class="normal-box ">无门槛红包奖励将于用户拆开礼盒后，由系统自动发放至用户的汇盈金<br>服账户中，用户登录后于“我的奖励”中查看。</div>
				<h2 class="title">活动须知</h2>
				<div class="normal-box">1、若某单笔投资同时是标的的第一笔和最后一笔，那么礼盒只发一个。<br>
2、若某标的的第一笔和最后一笔为同一用户，则礼盒发放两个。<br>
3、此活动仅计算新手专区和散标投资项目的投资。<br>
4、活动结束后，惊喜礼盒不保留，请及时领取哦！<br></div>
				<div class="normal-box box-margin-more">
					注：<br>
1、本活动所发优惠券仅限用于散标投资项目和计划专区（标的期限均不低于30天）。<br>
2、本活动所发优惠券均自获得之日起7日内有效，过期作废。<br>
3、本活动所发加息券的加息上限为100000元。<br>
				</div>
				<img src="${ctx}/dist/images/active/activity2018/activityToList/bg.png" class="bg-footer"></img>
			</div>

			<!--
          	未登录 活动1  end
         -->
          
	
	<!--
          	活动2
          -->
          <div class="active-content"  style="display: none;">
		<h2 class="title">活动内容</h2>
		<img src="${ctx}/dist/images/active/activity2018/activityToList/content-box-BIG.png" alt=""  style="margin: 45px auto 0; display: block;"/>
		<h2 class="title" style="margin-top: 90px;">奖励设置</h2>
		<div class="table-box">
			<div class="table-bg">
				<table border="0" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
					      <th width="28%">累计新增充值且投资金额</th>
					      <th width="17.27%">20000元</th>
					      <th width="17.27%">50000元</th>
					      <th width="17.27%">100000元</th>
					      <th width="17.27%">200000元</th>
					    </tr>
					</thead>
					<tbody>
						<tr>
							<td style="background: #fff;">奖励</td>
							<td>1%加息券（一张）</td>
							<td>2%加息券（一张）</td>
							<td>3%加息券（一张）</td>
							<td>3%加息券（两张）</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<h2 class="title" >参与方式</h2>
		<div class="normal-box">参与方式：在汇盈金服官方微信公众号（huiyingdai）中，点击“福利活动<br>”中“加息不停”菜单，或回复“加息不停”，即可进入活动页面。</div>
		<img src="${ctx}/dist/images/active/activity2018/activityToList/code.png" alt="" style="margin: 80px auto 100px;display: block;padding-left: 75px;"/>
				<h2 class="title" >奖励发放</h2>
				<div class="normal-box">
加息券奖励将于用户领取后3个工作日内，由系统自动发放至用户的汇盈金服账户中，用户登录后于“我的奖励”中查看。
				</div>
				<h2 class="title" >活动须知</h2>
				<div class="normal-box ">
此活动计算新手专区、散标投资项目和计划专区的投资。
				</div>
				<div class="normal-box box-margin-more">
					注：<br>
1、 本活动所发优惠券仅限用于散标投资项目和计划专区（标的期限均不低于30天）。<br>
2、本活动所发优惠券均自获得之日起7日内有效，过期作废。<br>
3、本活动所发加息券的加息上限为100000元。<br>
				</div>
				<img src="${ctx}/dist/images/active/activity2018/activityToList/bg.png" class="bg-footer"></img>
			</div>
            <!--
          	活动2 end
          -->
          
          
          <div class="active-content" style="display: none;">
		<h2 class="title">活动内容</h2>
		<div class="active-introduce">
			有钱任性，花样送手机！活动期间， 按指定要求刷榜即有机会获得各种手机大奖！
		</div>
		<h2 class="title" style="margin-top: 50px;">奖 励 设 置</h2>
				<div class="table-box">
					<div class="table-bg">
						<div class="table-tab">
							<div class="select" data-val="1">风云榜</div>
							<div data-val="2">龙虎榜</div>
							<div data-val="3">黑马榜</div>
							<div data-val="4">人气榜</div>
							<div data-val="5">壕友榜</div>
						</div>
						<div class="rank-content">
							<div class="introduce"><span>风云榜榜首奖励:iPhone X一部</span><br>
（考核标准：活动结束后，累计年化投资金额最多的用户将成为风云榜榜首）</div>
							<ul>
								<li>名次</li>
								<li>用户名</li>
								<li>累计年化投资金额</li>
							</ul>
							<div class="table table-1" style="position: relative;max-height: 250px;overflow: hidden;" id='t1'>
								<table border="0" cellspacing="0" cellpadding="0" style="border-radius: 0;">
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="rank-content" style="display: none;">
							<div class="introduce"><span>龙虎榜榜首奖励:iPhone X一部</span><br>
（考核标准：活动结束后，单笔年化投资金额最多的用户将成为龙虎榜榜首）</div>
							<ul>
								<li>名次</li>
								<li>用户名</li>
								<li>单笔年化投资金额</li>
							</ul>
							<div class="table table-1" style="position: relative;max-height: 250px;overflow: hidden;" id='t2'>
								<table border="0" cellspacing="0" cellpadding="0" style="border-radius: 0;">
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="rank-content" style="display: none;">
							<div class="introduce"><span>黑马榜榜首奖励:iPhone X一部</span><br>
（考核标准：活动结束后，累计年化投资金额最多的新用户将成为黑马榜榜首<br>注意：新用户的注册需要发生在活动期间哦！）</div>
							<ul>
								<li>名次</li>
								<li>用户名</li>
								<li>新用户累计年化投资金额</li>
							</ul>
							<div class="table table-1" style="position: relative;max-height: 250px;overflow: hidden;" id='t3'>
								<table border="0" cellspacing="0" cellpadding="0" style="border-radius: 0;">
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="rank-content" style="display: none;">
							<div class="introduce"><span>人气榜榜首奖励:华为mate10一部</span><br>
（考核标准：活动结束后，邀请好友数最多的用户将成为人气榜榜首<br>
邀请成功的标准：好友的注册、开户、绑卡操作都发生在活动期间<br>注：推荐人信息以用户注册时的信息为准）</div>
							<ul>
								<li>名次</li>
								<li>用户名</li>
								<li>邀请好友数量</li>
							</ul>
							<div class="table table-1" style="position: relative;max-height: 250px;overflow: hidden;" id='t4'>
								<table border="0" cellspacing="0" cellpadding="0" style="border-radius: 0;">
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
						<div class="rank-content" style="display: none;">
							<div class="introduce"><span>壕友榜榜首奖励:华为mate10一部</span><br>
（考核标准：活动结束后，被邀请的好友的累计年化投资额之和最高的用户将成为壕友榜榜首
<br>
注意：好友的注册、投资操作都需发生在活动期间，推荐人信息以用户注册时的信息为准）</div>
							<ul>
								<li>名次</li>
								<li>用户名</li>
								<li>好友累计年化投资额之和</li>
							</ul>
							<div class="table table-1" style="position: relative;max-height: 250px;overflow: hidden;" id='t5'>
								<table border="0" cellspacing="0" cellpadding="0" style="border-radius: 0;">
									<tbody>
										
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
				<h2 class="title">奖励发放</h2>
				<div class="normal-box ">手机奖励将于活动结束后的五个工作日内，由客服电话联系中奖用户<br>核实信息后，统一采购发放。<br>iPhone X为256GB版，华为mate10为64GB版，颜色皆随机。</div>
				<h2 class="title">活动须知</h2>
				<div class="normal-box">1、此活动计算新手专区、散标投资项目和计划专区的投资。<br />
2、最终排名以活动结束日23:59:59的数值为准。<br />
3、若个榜单出现并列名次，则按时间顺序排名。<br />
4、榜单只显示前100名哦！</div>
				<div class="normal-box box-margin-more">
					注：<br>
1、本活动所发优惠券仅限用于散标投资项目和计划专区（标的期限均不低于30天）。<br>
2、本活动所发优惠券均自获得之日起7日内有效，过期作废。<br>
3、本活动所发加息券的加息上限为100000元。<br>
				</div>
				<img src="${ctx}/dist/images/active/activity2018/activityToList/bg.png" class="bg-footer"></img>
			</div>
            
            
            
            
			<div class="active-to-list-footer">
				集团内部员工不得参与<br>
汇盈金服保留在法律规定范围内对上述规则进行解释的权利
			</div>
		</article>
		
		<div id="shadowBg" style='display:none'>
			<div class="gift-box">
				<div >
					<img src="${ctx}/dist/images/active/activity2018/activityToList/gift-bg.png" alt=""  class="gift-decorate"/>
				</div>
				<div >
					<img src="${ctx}/dist/images/active/activity2018/activityToList/gift.png" alt="" class="gift"/>
					<a class="close close-gift"></a>
				</div>
			</div>
			<div class="gift-detail" style="display: none;">
		<div>恭喜您<br />获得一张<span></span>元代金卷</div>
			<a class="btn" href="${ctx}/user/invite/toInvite.do" target='_blank'>去看看</a>
			<a class="close" href="javascript:void(0)" onclick="location.reload(true)"></a>
		</div>
	</div>
	<div id="notStarted" <c:if test="${msg eq '1' }"> style='display:none' </c:if>  >
		<div class="box">
			<p>
			<c:if test="${msg eq '0' }">
			活动未开始
			</c:if>
			<c:if test="${msg eq '2' }">
			活动已结束
			</c:if>	
			</p>
			<div class="confrim-btn">
				知道了
			</div>
		</div>
	</div>
<script src="${ctx}/dist/js/lib/jquery.min.js"></script>
<script src="${ctx}/dist/js/utils.js"></script>
<script type="text/javascript" src="${ctx}/dist/js/lib/my_scrollbar.js"></script>
<script src="${ctx}/dist/js/active/active-to-list.js"></script>
</body>
</html>