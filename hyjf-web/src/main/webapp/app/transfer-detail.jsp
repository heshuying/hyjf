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
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<jsp:include page="/bread.jsp"></jsp:include>
	<article class="main-content" style="padding-top: 0;">
        <div class="container">
            <!-- start 内容区域 -->   
            <div class="transfer-details">
                <div class="transfer-top">
                    <div class="title">项目详情</div>
                    <ul class="list">
                        <li>
                            <span class="basic-label">项目编号：</span>
                            <span class="basic-value">DHH123256452</span>                         
                        </li>
                        <li>
                            <span class="basic-label">预期年收益：</span>
                            <span class="basic-value value">15.00%</span>                         
                        </li>
                        <li>
                            <span class="basic-label">项目期限：</span>
                            <span class="basic-value">3个月</span>                         
                        </li>
                        <li>
                            <span class="basic-label">当前持有：</span>
                            <span class="basic-value">57天</span>                         
                        </li>
                        <li>
                            <span class="basic-label">当前剩余：</span>
                            <span class="basic-value">33天</span>                         
                        </li>
                        <li>
                            <span class="basic-label">还款方式：</span>
                            <span class="basic-value">按月计息，到期还本付息</span>                        
                        </li>
                        <li>
                            <span class="basic-label">持有本金：</span>
                            <span class="basic-value">10,000.00元</span>                     
                        </li>
                        <li>
                            <span class="basic-label">当前期次：</span>
                            <span class="basic-value">第1期（共1期）</span>                         
                        </li>
                        <li>
                            <span class="basic-label">投资时间：</span>
                            <span class="basic-value">2016-12-12 12:15:16</span>                         
                        </li>
                        
                    </ul>
                </div>
                <div class="transfer-mid">
                    <div class="title">转让债权</div>
                    <div class="main">                   
                        <ul>
                            <li>
                                <span class="basic-label fn-right">转让本金 </span>
                                <span class="basic-main">15,200.00元</span>
                            </li>
                            <li>
                                <span class="basic-label fn-right">本金折让率</span>
                                <span class="basic-main">
                                    <div class="gw_num">
                                        <em class="jian">-</em>
                                        <input type="text" value="0.3" class="num"/>
                                        <em class="add">+</em>
                                    </div>
                                    <div class="text">%</div>
                                    <div class="text sm">（0.2%~2.0%）</div>
                                </span>
                            </li>
                            <li>
                                <span class="basic-label fn-right">预计本金折让金额 </span>
                                <span class="basic-main">45.60元</span>
                            </li>
                            <li>
                                <span class="basic-label fn-right">预计持有期收益</span>
                                <span class="basic-main">123.16元</span>
                            </li>
                            <li>
                                <span class="basic-label fn-right">预计服务费</span>
                                <span class="basic-main">
                                    <div class="text" style="margin-left: 0;">123.16元</div>
                                    <div class="text">（当前费率为0.8%）</div>
                                </span>
                            </li>
                            <li>
                                <span class="basic-label fn-right">预计到账金额</span>
                                <span class="basic-main">
                                    <div class="value lg">15,271.91</div>
                                    <div class="text bom">元</div>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
                <div class="transfer-bom">
                    <div class="title">验证信息</div>
                    <div class="main">
                        <form action="" id="registerForm">
                        <div class="tel">
                            <span class="tel-label">手机号</span>
                            <span class="tel-main">15194268110</span>
                        </div>
                        <!-- <div id="slideUnlock">
                            <input type="hidden" value="0" name="lockable" id="lockable">
                            <div id="slider">
                                <span id="label" style="left: 0px;"></span>
                                <span class="slideunlock-lable-tip">移动滑块验证</span>
                                <span id="slide-process" style="width: 18px;"></span>
                            </div>        
                        </div> -->
                        <div id="slide-box">
                            <div id="slideUnlock">
                                <input type="hidden" value="" name="lockable" id="lockable"/>
                                <div id="slider">
                                    <span id="label"></span>
                                    <span class="slideunlock-lable-tip">移动滑块验证</span>
                                    <span id="slide-process"></span>
                                </div> 
                            </div>
                        </div>
                        <label class="validate">
                            <span class="tit">手机验证</span>
                            <input type="text" class="code" name="code" maxlength="8" placeholder="输入验证码">
                            <span class="get-code disable"><em class="rule"></em>获取验证码</span>
                        </label>
                        <div class="transfer-btn">
                            <a href="javascript:void(0)" class="sub">确认转让</a>
                            <a href="javascript:void(0)" class="cancel">取消转让 >></a>
                        </div>
                        </form>
                    </div>
                </div>
                <div class="arr">
                    <p class="title-sm">转让说明</p>
                    <ul>
                        <li class="ui-title-top-sm">1、本金折让率？</li>
                        <li class="ui-title-bom">折让率是指本金折让的百分比，折让率越高，成交完成的速度越快；
                        </li>
                        <li class="ui-title-top">2、预计本金折让金额</li>
                        <li class="ui-title-bom">本金折让金额=转让本金*本金折让率；投资者在承接这部分本金时，少支付的金额；
                        </li>
                        <li class="ui-title-top">3、预计持有收益</li>
                        <li class="ui-title-bom">预计持有收益=本期历史回报-本期出让本金*预期年化*剩余天数/360；为当期本金在持有期内预计所得的收益；
                        </li>
                        <li class="ui-title-top">4、预计服务费</li>
                        <li class="ui-title-bom">服务费=[ 转让本金+预计持有收益 -本金折让金额] * 服务费率
                        </li>
                        <li class="ui-title-top">5、预计到账金额</li>
                        <li class="ui-title-bom">预计到账金额=转让本金+预计持有收益-预计本金折让金额-预计服务费
                        </li>
                    </ul>
                </div>
            </div>
            <!-- end 内容区域 -->            
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script src="../dist/js/lib/jquery.validate.js"></script>
    <script src="../dist/js/lib/jquery.slideunlock.min.js"></script>
	<script src="../dist/js/rights-manage/transfer-details.js"></script>
</body>
</html>