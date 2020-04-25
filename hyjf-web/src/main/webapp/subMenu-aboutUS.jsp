<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<nav class="nav-sub">
    <div class="container">
        <ul>
            <li id="aboutAboutus"><a href="${ctx}/aboutus/aboutus.do" itemid="aboutAboutus">关于我们</a></li>
            <li id="aboutInformation"><a href="${ctx}/aboutus/getInformation.do" itemid="aboutInformation">信息披露</a></li>
            <li id="aboutPlatformInfo" class="has-nav">
                <a href="#">数据披露<span class="iconfont icon icon-ananzuiconv265"></span></a>
                <ul>
                    <li id="platdatastatistics"><a href="${ctx}/platdatastatistics/initPlatData.do">运营数据</a></li>
                    <li id="aboutReport"><a href="${ctx}/aboutus/report.do" >运营报告</a></li>
                </ul>
            </li>
            <li id="aboutPartners"><a href="${ctx}/aboutus/partners.do" itemid="aboutPartners">合作伙伴</a></li>
            <li id="aboutEvents"><a href="${ctx}/aboutus/events.do" itemid="aboutEvents">公司历程</a></li>
            <li id="aboutNotice"><a href="${ctx}/contentarticle/siteNotices.do" itemid="aboutNotice">网站公告</a></li>
            <li id="aboutDynamics"><a href="${ctx}/contentarticle/getCompanyDynamicsList.do" itemid="aboutDynamics">公司动态</a></li>
            <li id="aboutJobs"><a href="${ctx}/aboutus/jobs.do" itemid="aboutJobs">招贤纳士</a></li>
            <%-- <li id="aboutloan"><a href="${ctx}/aboutus/searchKnowReportList.do" itemid="aboutloan">网贷知识</a></li>
            <li id="aboutRiskEducation"><a href="${ctx}/aboutus/searchFXReportList.do" itemid="aboutRiskEducation">风险教育</a></li> --%>

            
            <li id="aboutContactus"><a href="${ctx}/aboutus/contactus.do" itemid="aboutContactus">联系我们</a></li>
        </ul>
    </div>
</nav>