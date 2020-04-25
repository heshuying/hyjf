<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include  file="/common.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>最新活动 - 汇盈金服官网</title>
    <jsp:include page="/head.jsp"></jsp:include>
</head>

<body>
<jsp:include page="/header.jsp"></jsp:include>
    <div class="active-banner" style="background-image:url(${cdn}/img/activebanner.jpg);">
        <h4>最新活动</h4>
    </div>
    <div class="active-list">
        <ul id ="strActivityPages">
           <li>
               <a href="#" class="act-li-img">
                   <img src="${fileDomainUrl}${record.activiti_image}" alt="">
               </a>
               <div class="act-li-content">
                   <div class="act-li-title"></div>
                   <div class="act-li-date"></div>
                   <div class="act-li-txt"></div>
                   <!-- <div class="act-li-joined iconfont iconfont iconfont-team">1254人参与</div> -->
                   <a href="" class="act-li-more ">查看详情</a>
               </div>
           </li>
        </ul>
        <div class="clearfix"></div>
        <div class="new-pagination" id="new-pagination"></div>
   </div>
  <jsp:include page="/footer.jsp"></jsp:include>
  <script src="${cdn}/js/activity/newactivitylist.js" type="text/javascript"></script>
  <script src="${cdn}/js/common/common.js" type="text/javascript"></script>
</body>

</html>