<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
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
	<article class="main-content">
        <div class="container result">
            <div class="fxcp-title">
                风险测评
            </div>
            <div class="fxcp-cont">
                <div class="fxcp-tip">
                    我们将对您进行风险承受能力评估，根据评估结果，给您提供更合适的资产选择，请您认真作答，感谢您的配合。
                    <p class="colorRed">首次评测送加息券！</p>
                </div>
                <form action="" id="consultForm" name="consultform">
                    <!--问题列表开始-->
                    <div class="fxcp-list">
                        <div class="fxcp-list-title">
                            <span class="titles">1</span> 你现在的年龄是？
                            <span class="tips"><i class="iconfont icon-jinzhi"></i>未回答本题</span>
                        </div>
                        <ul class="fxcp-list-item">
                            <li class="checkedStyle">
                                <a href="javascript:;">
                                    <i class="iconfont icon-xuanze"></i>
                                    <input type="radio" /> 25岁(含25)以下
                                </a>
                            </li>
                            <li>
                                <a href="javascript:;">
                                    <i class="iconfont icon-xuanze"></i>
                                    <input type="radio" /> 26-45岁
                                </a>
                            </li>
                            <li>
                                <a href="javascript:;">
                                    <i class="iconfont icon-xuanze"></i>
                                    <input type="radio" /> 46-65岁
                                </a>
                            </li>
                            <li>
                                <a href="javascript:;">
                                    <i class="iconfont icon-xuanze"></i>
                                    <input type="radio" /> 高于65岁
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!--问题列表结束-->
                    <div class="fxcp-standard">
                        <p>评分标准</p>
                        0~65分&nbsp;&nbsp;保守型：您的风险承受能力较低，建议在专业指导下投资风险较小的产品。
                        <br />
                        <br /> 66~85分&nbsp;&nbsp;稳健型：您具有一定的风险承受能力，建议选择产品时要平衡收益和风险的比例。
                        <br />
                        <br /> 86~95分&nbsp;&nbsp;成长型：您具有较好的风险承受能力，建议可投资风险适中但收益较好的产品。
                        <br />
                        <br /> 96分以上&nbsp;&nbsp;进取型：您可以承担较大风险来获取利益，建议可投资风险较高但收益较大的产品。
                    </div>
                    <div class="fxcp-sub">
                        <a href="javascript:;" class="submit">提交</a>
                    </div>
                </form>
            </div>
        </div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${ctx }/dist/js/about/fxcp.js"></script>
</body>
</html>