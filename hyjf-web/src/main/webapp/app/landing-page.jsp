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
  <section class="top-bar">
        <div class="container">
            <div class="top-left"><span class="icon iconfont icon-dianhua"></span> 客服电话：400-900-7878（服务时间 9:00-18:00）</div>
            <nav class="top-right">
                <a href="#">关于我们</a>
                <a href="#">帮助中心</a>
                <a href="#"> <span class="icon iconfont icon-phone"></span>手机端下载</a>
            </nav>
        </div>
    </section>
	<header id="header">
        <div class="nav-main">
            <div class="container">
                <a href="/" class="logo"><img src="http://img.hyjf.com/dist/images/logo.png" alt="汇盈金服" /></a>
                <nav class="nav-right">
                    <div class="user-nav">
                        <a href="#">注册</a> | <a href="#">返回首页</a>
                    </div>
                </nav>
            </div>
        </div>
    </header>
	 <section class="main-content" style="padding-top: 0;">
        <div class="landing-page">
            <div class="landing_section landing_1 register">
                <div class="landing-banner swiper-container">
                    <div class="prev"></div>
                    <dir class="next"></dir>
                    <div class="swiper-wrapper">
                        <a href="javascript:;" style="background-image: url('../images/landing/banner.jpg');" class="bg swiper-slide">&nbsp;</a>
                    </div>
                </div>
                <div class="top-main re-content">
                    <form action="" method="get" id="registerForm">
                    	<input type="hidden" name="tokenCheck" id="tokenCheck" value="" /> 
						<input type="hidden" name="tokenGrant" id="tokenGrant" value="${tokenGrant}" />
                        <div class="form-main">
                            <label>
                                <span class="tit">手机号</span>
                                <input type="text" onkeyup="value=value.replace(/[^\d]/g,'')" name="telnum" id="telnum" placeholder="输入手机号"/>
                            </label>
                            <label>
                                <span class="tit">密码</span>
                                <input type="password" name="password" placeholder="6-16位字母和数字组成"/>
                                <span class="iconfont icon-yanjing1 yanjing"></span>
                            </label>
                            <div id="slide-box">
                                <div id="slideUnlock">
                                    <input type="hidden" value="" name="lockable" id="lockable"/>
                                    <div id="slider">
                                        <span id="label"><i></i></span>
                                        <span class="slideunlock-lable-tip">移动滑块验证</span>
                                        <span id="slide-process"></span>
                                    </div>
                                    
                                </div>
                            </div>
                            <label>
                                <span class="tit">手机验证</span>
                                <input type="text" name="code" maxlength="8" placeholder="输入验证码"/>
                                <span class="get-code disable"><em class="rule"></em>获取验证码</span>
                            </label>
                            <label>
                                <span class="tit">推荐人</span>
                                <input type="text" placeholder="输入推荐人手机号或推荐码"/>
                            </label>                           
                            <div class="read-protocol">
		    					<input type="checkbox" value="1" id="protocol"/>
			    					<span>我已阅读并同意 </span>
			    					<span class="relevant">相关注册协议</span>
			    					<div class="agreement">
			    						<a href="${ctx}/agreement/registrationProtocol.do" target="_blank">《网站注册协议》</a><br/>
		    							<a href="${ctx}/agreement/privacyClause.do" target="_blank">《隐私保护规则》</a><br/>
		    							<a href="${ctx}/agreement/investmentAdvisoryAndManagementServices.do" target="_blank">《投资咨询与管理服务协议》</a>
		    						</div>
		    				</div>
                            <a class="sub disable">注册</a>
                        </div>
                    </form>
                </div>
            </div>
            <div class="red-packet">
            </div>
            <div class="control-box">
                <div class="div1 box-div">
                    <div class="div1-main box-div-main">
                        <a href=""></a>
                    </div>
                </div>
                <div class="div2 box-div">
                    <div class="div2-main box-div-main">
                        <a href=""></a>
                    </div>
                </div>
                <div class="div3 box-div">
                    <div class="div3-main box-div-main">
                        <a href=""></a>
                    </div>
                </div>
                <div class="div4 box-div">
                    <div class="div4-main box-div-main">
                        <a href=""></a>
                    </div>
                </div>
            </div>
            <div class="icon-box">
                <div class="icon-boxmain">
                    <div class="img">
                        <img src="http://img.hyjf.com/dist/images/landing/icon.png">
                    </div>
                    <div class="bom">
                        <ul class="bom-ul">
                            <li class="ui-1">
                                <div class="bom-item">
                                    <span class="item1">品牌优势</span>
                                    <span class="item2">稳健运营1276天<br>
                                    用户累计投资13亿</span>
                                </div>
                            </li>
                            <li class="ui-2">
                                <div class="bom-item">
                                    <span class="item1">美国上市</span>
                                    <span class="item2">美国上市公司汇盈金服<br>
                                    股票代码SFHD</span>
                                </div>
                            </li>
                            <li class="ui-3">
                                <div class="bom-item">
                                    <span class="item1">资金存管</span>
                                    <span class="item2">江西银行资金存管<br>
                                    保障用户资金安全</span>
                                </div>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="land-bottom">
                <div class="add-main">
                    <div class="add-title">平台统计</div>
                    <div class="add-money">
                        <div class="add-money-num">
                            <span class="add-money-title">平台用户累计投资（元）</span>
                            <span class="add-money-big">11,073,154,090</span>
                        </div>
                        <div class="add-money-num">
                            <span class="add-money-title">已为用户创造收益（元）</span>
                            <span class="add-money-big">371,411,295</span>
                        </div>
                    </div>
                </div>
                <div class="add-backbg"></div>
            </div>
        </div>
    </section>
	    <footer id="footer">
        <div class="nav-footer">
            <div class="container">
                <div class="footer-left">
                    <div class="footer-hotline">
                        <div class="hotline-num">
                            服务热线：<span>400-065-5000</span>
                        </div>
                        <div class="hotline-time">（ 服务时间 9:00-18:00 ）</div>
                    </div>
                    <div class="footer-tips">历史回报不等于实际收益</div>
                </div>
                <nav class="footer-nav">
                    <dl>
                        <dt><a href="#">安全保障</a></dt>
                        <dd>
                            <a href="#">业务流程</a>
                            <a href="#">风险措施</a>
                            <a href="#">风险保证金</a>
                        </dd>
                    </dl>
                    <dl>
                        <dt><a href="#">关于我们</a></dt>
                        <dd>
                            <a href="#">公司记事</a>
                            <a href="#">合作伙伴</a>
                            <a href="#">联系我们</a>
                        </dd>
                    </dl>
                    <dl>
                        <dt><a href="#">帮助中心</a></dt>
                        <dd>
                            <a href="#">注册登录</a>
                            <a href="#">投资问题</a>
                            <a href="#">投资问题</a>
                        </dd>
                    </dl>
                </nav>
                <div class="footer-right">
                    <div class="footer-qr">
                        <div class="qr-img"><img src="http://img.hyjf.com/dist/images/qr_app.png" alt="下载客户端" /></div>
                        <p>客户端下载</p>
                    </div>
                    <div class="footer-qr">
                        <div class="qr-img">
                            <img src="http://img.hyjf.com/dist/images/qr_weixin.png" alt="微信关注我们" />
                        </div>
                        <p>关注我们</p>
                    </div>
                </div>
            </div>
        </div>
        <div class="footer-approve">
            <div class="container">
                <ul>
                    <li>
                        <a href="https://ss.knet.cn/verifyseal.dll?sn=e13121111010044010zk2c000000&amp;ct=df&amp;a=1&amp;pa=0.4153385634999722" target="_blank"></a>
                    </li>
                </ul>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                <span>&copy; 汇盈金服 All rights reserved | 惠众商务顾问（北京）有限公司 京ICP备13050958号 | 公安安全备案证：37021313127</span>
                <span>市场有风险  投资需谨慎</span>
            </div>
        </div>
    </footer>
    <div class="toolbar" style="height: 65px;">
        <ul>
            <!-- <li class="item" id="gupiaoItem">
                <div class="icon iconfont icon-gupiao"></div>
                <div class="shares-panel" id="gupiaoPanel">
                    <div class="shares-panel-title">
                        <div class="main-title red">
                            <span id="sharesNow"></span> <span class="status iconfont iconfont-up" id="sharesStatus"></span> <span class="info" id="sharesStatusInfo">+2.71 (+4.17%)</span>
                        </div>
                        <div class="sub-title" id="sharesDate"></div>
                    </div>
                    <div class="shares-info">
                        <table width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="40%">今开</td>
                                <td align="right" class="red" id="dataOpen"></td>
                            </tr>
                            <tr>
                                <td>昨收</td>
                                <td align="right" id="dataPreviousClose"></td>
                            </tr>
                            <tr>
                                <td>最高</td>
                                <td align="right" class="red" id="dataHigh"></td>
                            </tr>
                            <tr>
                                <td>最低</td>
                                <td align="right" class="red" id="dataLow"></td>
                            </tr>
                            <tr>
                                <td>成交量</td>
                                <td align="right" id="dataVolume"></td>
                            </tr>
                            <tr>
                                <td>市盈率</td>
                                <td align="right" id="dataPERatio"></td>
                            </tr>
                            <tr>
                                <td>每股收益</td>
                                <td align="right" id="dataEPS"></td>
                            </tr>
                            <tr>
                                <td>总市值</td>
                                <td align="right" id="dataMarketCap"></td>
                            </tr>
                        </table>
                    </div>
                    <div class="shares-panel-body" id="sharesTabBody">
                        <div class="item active" data-panel="0">
                            <div class="shares-char" id="chart0">
                            </div>
                        </div>
                        <div class="item" data-panel="1">
                            <div class="shares-char" id="chart1">
                            </div>
                        </div>
                        <div class="item" data-panel="2">
                            <div class="shares-char" id="chart2">
                            </div>
                        </div>
                        <div class="item" data-panel="3">
                            <div class="shares-char" id="chart3">
                            </div>
                        </div>
                    </div>
                    <div class="shares-panel-tab" id="sharesTabTag">
                        <div class="item active" data-panel="0">分时</div>
                        <div class="item" data-panel="1">5日</div>
                        <div class="item" data-panel="2">1月</div>
                        <div class="item" data-panel="3">1年</div>
                    </div>
                </div>
            </li>
            <li class="item">
                <a href="#" target="_blank" class="icon iconfont icon-jisuanqi"></a>
            </li>
            <li class="item">
                <a href="http://b.qq.com/webc.htm?new=0&sid=4000655000&eid=218808P8z8p8p8z8K8p80&o=www.huiyingdai.com&q=7" target="_blank" class="icon iconfont icon-kefu"></a>
            </li>
            <li class="item">
                <div class="icon iconfont icon-scancode"></div>
            </li> -->
            <li class="item">
                <div class="icon iconfont icon-top-copy" onclick="utils.scrollTo();"></div>
            </li>
        </ul>
    </div>
	<script src="../dist/js/lib/jquery.min.js"></script>
    <script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.metadata.js"></script>
    <!--<script src="../dist/js/lib/echarts.common.min.js"></script>
    <script src="../dist/js/lib/nprogress.js"></script> -->
    <script src="../dist/js/lib/baguetteBox.min.js"></script>
    <script src="../dist/js/utils.js"></script>
    <script src="../dist/js/utils.shareschart.js"></script>
    <script src="../dist/js/lib/jquery.slideunlock.min.js"></script>
    <script src="../dist/js/login/register.js"></script>
    <script src="../dist/js/idangerous.swiper.min.js"></script>
    <script src="../dist/js/landing-page.js"></script>
</body>
</html>