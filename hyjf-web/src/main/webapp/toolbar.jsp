<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<div class="toolbar">
    <ul>
        <%--  <-- shares-chart.js -->
        <li class="item" id="gupiaoItem" style="display:none;">
            <div class="icon iconfont icon-gupiao" itemid="tlbar1"></div>
            <a class="hover-box">股票走势</a>
            <div class="shares-panel" id="gupiaoPanel">
                <div class="shares-panel-title">
                    <div class="main-title red">
                        <span id="sharesNow"></span> <span class="status iconfont iconfont-up" id="sharesStatus"></span> <span class="info" id="sharesStatusInfo">+2.71 (+4.17%)</span>
                    </div>
                    <div class="sub-title" id="sharesDate"></div>
                </div>
                <div class="shares-info">
                	<img src="${cdn}/dist/images/sharesdata.jpg" />
                    <table width="100%" cellpadding="0" cellspacing="0" style="display:none;">
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
                    <div class="item active" data-panel="0" itemid="tls1">分时</div>
                    <div class="item" data-panel="1" itemid="tls2">5日</div>
                    <div class="item" data-panel="2" itemid="tls3">1月</div>
                    <div class="item" data-panel="3" itemid="tls4">1年</div>
                </div>
            </div>
        </li>--%>
        <li class="item">
            <a href="${ctx}/homepage/calc.do" target="_blank" class="icon iconfont icon-jisuanqi" itemid="tlbar2"></a>
            <a href="${ctx}/homepage/calc.do" target="_blank" class="hover-box">收益计算</a>
        </li>
        <c:choose>
         <c:when test="${cookie['hyjfUsername'].value == null || cookie['hyjfUsername'].value == ''}">
    	        <li class="item">
		            <a href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376&moduleType=1" target="_blank" class="icon iconfont icon-kefu" itemid="tlbar3"></a>
		            <a href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376&moduleType=1"  target="_blank" class="hover-box">在线客服</a>
		        </li> 
	   	 </c:when>
	     <c:otherwise>
		     <li class="item">
				 <a href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376" target="_blank" class="icon iconfont icon-kefu" itemid="tlbar3"></a>
	            <a href="https://www.sobot.com/chat/pc/index.html?sysNum=1625b67dbd104a8f8d6a9b4ef8d77d5b&version=1527648376"  target="_blank" class="hover-box">在线客服</a>
		     </li> 
	     </c:otherwise>
	     </c:choose>
        <li class="item" id="saomaItem">
            <div class="icon iconfont icon-scancode" itemid="tlbar4"></div>
            <a class="hover-box">App下载</a>
            <div id="saoma" class="clearfloat">
            	<!--第一个为IOS二维码，第二个为Android验证码，一开始默认显示IOS验证码-->
            	<img src="${cdn}/dist/images/app-android.png?version=${version}" class="show" />
            	<div class="edition">
            		<p>扫描下载APP</p>
            		<a href="https://itunes.apple.com/cn/app/hui-ying-jin-fu/id1044961717?mt=8" target="_blank" class="show">IOS 版</a>
            		<!-- <a href="http://img.hyjf.com/data/download/com.huiyingdai.apptest_wangye.apk" target="_blank">Android 版</a> -->
            		<a href="${ctx}/homepage/lastesturl.do" target="_blank">Android 版</a>
            	</div>
            </div>
        </li>
        <%-- <li class="item">
      		<a href="javascript:;" class="iconfont iconfont-qr"></a>
            <div class="icon iconfont icon-scancode" itemid="tlbar4">
            	 <div class="tool-pop-title">
		            	扫描下载客户端
		        </div>
		        <img src="${cdn}/dist/images/qr_app.png" alt="" width="150" height="150">
            </div>
        </li> --%>   
        <li class="item">
            <div class="icon iconfont icon-top-copy" itemid="tlbar5" onclick="utils.scrollTo();"></div>
        </li>
    </ul>
</div>