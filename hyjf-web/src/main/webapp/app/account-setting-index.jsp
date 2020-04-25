<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
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
            <!-- start 内容区域 --> 
            <div class="acc-set">
            	<ul class="acc-set-tab clear hidden">
            		<li class="tab-active">账户信息</li>
            		<li>我的银行卡</li>
            	</ul>
            	<ul class="acc-set-item">
            		<li class="acc-set-1 acc-set-item-li acc-set-active">
            			<div class="acc-set-1-top clear">
            				<div class="fl">
            					<img src="http://img.hyjf.com/dist/images/acc-set/account-profile.png" alt="" class="acc-set-1-top-img"/>
            					<p>hyjf200000</p>
            				</div>
            				<div class="fr">
            					<div class="friend-code-div partner-how-code">
									<input type="hidden" id="qrcodeValue" value="" />
									<div id="qrcode" class="friend-code-img-1"></div>
								</div>
            					<p>我的二维码</p>
            				</div>
            			</div>
            			<ul class="acc-set-1-list">
            				<li>
            					<span class="acc-set-1-list-1">银行存管账户</span>
            					<span class="acc-set-1-list-2">江西银行：13132442334423322332</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">托管账户</span>
            					<span class="acc-set-1-list-2">汇付天下：13132442334423322332</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<span><i class="icon iconfont icon-tanhao"></i>未开通</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">实名信息</span>
            					<span class="acc-set-1-list-2">黄**丨37**************2414</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">绑定手机</span>
            					<span class="acc-set-1-list-2">绑定手机号，账户更安全</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">绑定邮箱</span>
            					<span class="acc-set-1-list-2">及时获取相关协议和资讯</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">登录密码</span>
            					<span class="acc-set-1-list-2">上次登录时间：2017-02-17 14:43:36</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">交易密码</span>
            					<span class="acc-set-1-list-2">设置交易密码，为资金安全保驾护航</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">消息通知</span>
            					<span class="acc-set-1-list-2">及时获取账户资金变动通知和投资资讯</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				
            				<!--新增自动投标-->
            				<li>
            					<span class="acc-set-1-list-1">自动投标</span>
            					<span class="acc-set-1-list-2">授权时间：2017-02-17 14:43:36</span><!--未授权文案：使用自动投标功能，体验更多产品-->
            					<span class="acc-set-1-list-3 act-set-setted">
            						<a><i class="icon iconfont icon-duihao"></i>已授权</a>
            					</span>
            				</li>
            				
            				
            				<!--未授权样式-->
            				<!--<li>
            					<span class="acc-set-1-list-1">自动投标</span>
            					<span class="acc-set-1-list-2">使用自动投标功能，体验更多产品</span>
            					<span class="acc-set-1-list-3 act-set-not-setted">
            						<a><i class="icon iconfont icon-tanhao"></i>未授权</a>
            						<span class=""><a href="">授权</a></span>
            					</span>
            				</li>-->
            				
            				
            				<li>
            					<span class="acc-set-1-list-1">风险测评</span>
            					<span class="acc-set-1-list-2"><i class="acc-set-risk-type">稳健型</i> <!--了解自己风险承受能力，降低投资风险--></span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">紧急联系人</span>
            					<span class="acc-set-1-list-2">紧急情况无法联系您的时候，优先联系您的亲友</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已设置</span>
            						<span class=""><a href="">修改</a></span>
            					</span>
            				</li>
            				<li>
            					<span class="acc-set-1-list-1">汇付托管账户</span>
            					<span class="acc-set-1-list-2">汇付天下：zsc_14714007904579182702</span>
            					<span class="acc-set-1-list-3 act-set-setted">
            						<span><i class="icon iconfont icon-duihao"></i>已开通</span>
            						<span class=""><a href="">查看</a></span>
            					</span>
            				</li>
            				<li></li>
            			</ul>
            		</li>
            		<li class="acc-set-2 acc-set-item-li">
            			<ul>
            				<!--未绑卡-->
            				<li class="not-bind-card">
            					<img src="http://img.hyjf.com/dist/images/acc-set/acc-add-cards.png"/>
            					<p>添加银行卡</p>
            				</li>
            				<!--已绑卡-->
            				<li class="binded-card">
            					
            				</li>
            			</ul>
            		</li>
            	</ul>
            </div>
                      
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.qrcode.js"></script>
	<script src="../dist/js/lib/qrcode.js"></script>
	<script src="../dist/js/acc-set/code.js"></script>
	<script src="../dist/js/acc-set/acc-set.js"></script>
</body>
</html>