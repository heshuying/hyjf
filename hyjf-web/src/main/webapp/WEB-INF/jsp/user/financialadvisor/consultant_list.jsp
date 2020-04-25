<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-cmn-Hans">
<head>
<meta charset=UTF-8">
<title>汇盈金服 - 真诚透明自律的互联网金融服务平台</title>
<jsp:include page="/head.jsp"></jsp:include>
</head>
<body>
	<jsp:include page="/header.jsp"></jsp:include>
	<jsp:include page="/subMenu.jsp"></jsp:include>
	<article class="main-content">
        <div class="container result">
            <div class="fxcp-title">风险测评 </div>
            <div class="fxcp-cont">
            <div class="fxcp-tip">
                                                我们将对您进行风险承受能力评估，根据评估结果，给您提供更合适的资产选择，请您认真作答，感谢您的配合。
            </div>
            <h5></h5>
			<form action="" id="consultForm" name="consultform">
				 <!--问题列表开始-->
				<c:forEach items="${questionList}" var="question" varStatus="status">
				        <div class="fxcp-list">
						<div class="fxcp-list-title">
						<c:set value="${ fn:split(question.question, '.') }" var="questionStr" />
						<span class="titles">${questionStr[0]}</span>${questionStr[1]}
						</div>
						<ul class="fxcp-list-item">
							<c:forEach items="${question.answers}" var="answer">
								<li class="checkedStyle">
									<a href="javascript:;">
										<i class="iconfont icon-xuanze"></i>
										<input type="radio" name="ask${question.sort}"  value="${answer.questionId}_${answer.id}" />${answer.answer}
									</a>
								</li>
							</c:forEach>
						</ul>
						</div>
				</c:forEach>
				<div class="fxcp-standard">
                        <p>评分标准</p>
                        <c:forEach items="${evalationList}" var="evalation" varStatus="status">
                        	<c:if test="${status.index != 0}"><br /> <br /></c:if>
                        	<c:if test="${status.index+1 == fn:length(evalationList)}">${evalation.scoreUp}分以上&nbsp;&nbsp;${evalation.type} : ${evalation.summary} ${evalation.type}单笔最高出借金额为<span class='highlight'>${evalation.revaluationMoney }</span></span></c:if>
                        	<c:if test="${status.index+1 != fn:length(evalationList)}">${evalation.scoreUp}~${evalation.scoreDown}分&nbsp;&nbsp;${evalation.type} : ${evalation.summary} ${evalation.type}单笔最高出借金额为<span class='highlight'>${evalation.revaluationMoney }</span></c:if>
                        </c:forEach>
                </div>
				<div class="fxcp-sub">
                       <a href="javascript:;" class="submit">提交</a>
				</div>  
			</form>
			<form action="${ctx}/financialAdvisor/evaluationResult.do" id="submitForm" name="submitform" method="post">
				<input type="hidden" id="userAnswer" name="userAnswer" >
			</form>
			</div>
		</div>
    </article>
	<jsp:include page="/footer.jsp"></jsp:include>
	<script>setActById('accountSet');</script>
	<script src="${cdn}/dist/js/lib/jquery.validate.js"></script>
	<script src="${cdn }/dist/js/about/fxcp.js?version=${version}"></script>
</body>
</html>