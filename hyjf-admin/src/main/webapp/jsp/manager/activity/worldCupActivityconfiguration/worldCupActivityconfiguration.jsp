<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/jsp/base/pageBase.jsp"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%-- 画面功能菜单设置开关 --%>
<c:set var="hasSettings" value="true" scope="request"></c:set>
<c:set var="hasMenu" value="true" scope="request"></c:set>
<c:set var="searchAction" value="#" scope="request"></c:set>


<shiro:hasPermission name="worldCupactivityConf:VIEW">
    <tiles:insertTemplate template="/jsp/layout/listLayout.jsp" flush="true">
        <%-- 画面的标题 --%>
        <tiles:putAttribute name="pageTitle" value="世界杯活动配置" />

        <%-- 画面主面板的标题块 --%>
        <tiles:putAttribute name="pageFunCaption" type="string">
            <h1 class="mainTitle">世界杯活动配置</h1>
            <span class="mainDescription">本功能可以修改。</span>
        </tiles:putAttribute>

        <%-- 画面主面板 --%>
        <tiles:putAttribute name="mainContentinner" type="string">
            <div class="container-fluid container-fullw bg-white">
                <div class="tabbable">
                    <ul class="nav nav-tabs" id="myTab">
                        <shiro:hasPermission name="worldCupactivityConf:VIEW">
                            <li class="active"><a href="${webRoot}/manager/activity/worldCupActivityconfiguration/init">决战赛球队配置</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="worldCupactivityConf:VIEW">
                            <li><a href="${webRoot}/manager/activity/worldCupActivityconfiguration/matchInit">决战赛比赛配置</a></li>
                        </shiro:hasPermission>
                    </ul>
                    <div class="tab-content">
                        <div class="tab-pane fade in active">
                            <form id="mainForm" action="submitTeamAction" method="post"  role="form">
                                <div class="tab-content">
                                    <div class="tab-pane fade in active" id="tab_jbxx_1">
                                        <div class="row">
                                            <div class="col-xs-12 col-md-6" >
                                                <div class="user-left" style="text-align:center" >
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1C</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1c" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[0].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1c" class= "fileupload" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" >
                                                                            <input type="hidden" name="team1c2d.homeLogo" datatype="*" nullmsg="请选择球队LOGO"  class="teamteamLogo1c" value="${recordSelected[0].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1c2d.homeId" datatype="*" nullmsg="请选择球队" datatype="*" nullmsg="请选择球队" class="teamId1c" value="${recordSelected[0].homeId}" />
                                                                <input type="hidden" name="team1c2d.homeName" class="teamteamName1c" value="${recordSelected[0].homeName}" />
                                                                <input type="hidden" name="team1c2d.homeTeamGroupings" value="3"/>
                                                                <select id="statusSrch1c" class=" form-control underline form-select2" onchange="clickstatusSrch(this)">
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 3}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[0].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">2D</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;line-height: 0 !important;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg2d" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[0].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload2d" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff" >
                                                                            <input type="hidden" name="team1c2d.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2d" value="${recordSelected[0].visitedLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>


                                                            <div class="form-group">
                                                                <input type="hidden" name="team1c2d.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2d" value="${recordSelected[0].visitedId}" />
                                                                <input type="hidden" name="team1c2d.visitedName" class="teamteamName2d" value="${recordSelected[0].visitedName}" />
                                                                <input type="hidden" name="team1c2d.visitedTeamGroupings" value="4"/>
                                                                <select id="statusSrch2d" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 4}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[0].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1A</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1a" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[1].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1a" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1a2b.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1a" value="${recordSelected[1].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1a2b.homeId"  datatype="*" nullmsg="请选择球队" class="teamId1a" value="${recordSelected[1].homeId}" />
                                                                <input type="hidden" name="team1a2b.homeName" class="teamteamName1a" value="${recordSelected[1].homeName}" />
                                                                <input type="hidden" name="team1a2b.homeTeamGroupings" value="1"/>
                                                                <select id="statusSrch1a" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 1}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[1].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }" ></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">2B</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg2b" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[1].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload2b" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1a2b.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2b" value="${recordSelected[1].visitedLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1a2b.visitedId"  datatype="*" nullmsg="请选择球队" class="teamId2b" value="${recordSelected[1].visitedId}" />
                                                                <input type="hidden" name="team1a2b.visitedName" class="teamteamName2b" value="${recordSelected[1].visitedName}" />
                                                                <input type="hidden" name="team1a2b.visitedTeamGroupings" value="2"/>
                                                                <select id="statusSrch2b" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 2}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[1].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1B</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1b" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[6].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1b" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1b2a.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1b" value="${recordSelected[6].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1b2a.homeId"  datatype="*" nullmsg="请选择球队" class="teamId1b" value="${recordSelected[6].homeId}" />
                                                                <input type="hidden" name="team1b2a.homeName" class="teamteamName1b" value="${recordSelected[6].homeName}" />
                                                                <input type="hidden" name="team1b2a.homeTeamGroupings" value="2"/>
                                                                <select id="statusSrch1b" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 2}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[6].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">2A</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg2a" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[6].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload2a" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1b2a.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2a" value="${recordSelected[6].visitedLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>
                                                            <div class="form-group">
                                                                <input type="hidden" name="team1b2a.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2a" value="${recordSelected[6].visitedId}" />
                                                                <input type="hidden" name="team1b2a.visitedName" class="teamteamName2a" value="${recordSelected[6].visitedName}" />
                                                                <input type="hidden" name="team1b2a.visitedTeamGroupings" value="1"/>
                                                                <select id="statusSrch2a" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 1}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[6].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1D</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1d" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[7].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1d" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1d2c.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1d" value="${recordSelected[7].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1d2c.homeId" datatype="*" nullmsg="请选择球队" class="teamId1d" value="${recordSelected[7].homeId}" />
                                                                <input type="hidden" name="team1d2c.homeName" class="teamteamName1d" value="${recordSelected[7].homeName}" />
                                                                <input type="hidden" name="team1d2c.homeTeamGroupings" value="4"/>
                                                                <select id="statusSrch1d" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 4}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[7].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">2C</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg2c" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[7].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload2c" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1d2c.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2c" value="${recordSelected[7].visitedLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1d2c.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2c" value="${recordSelected[7].visitedId}" />
                                                                <input type="hidden" name="team1d2c.visitedName" class="teamteamName2c" value="${recordSelected[7].visitedName}" />
                                                                <input type="hidden" name="team1d2c.visitedTeamGroupings" value="3"/>
                                                                <select id="statusSrch2c" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 3}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[7].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-xs-12 col-md-6">
                                                <div class="user-left" style="text-align:center" >
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1E</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1e" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[4].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1e" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1e2f.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1e" value="${recordSelected[4].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>
                                                            <div class="form-group">
                                                                <input type="hidden" name="team1e2f.homeId" datatype="*" nullmsg="请选择球队" class="teamId1e" value="${recordSelected[4].homeId}" />
                                                                <input type="hidden" name="team1e2f.homeName" class="teamteamName1e" value="${recordSelected[4].homeName}" />
                                                                <input type="hidden" name="team1e2f.homeTeamGroupings" value="5"/>
                                                                <select id="statusSrch1e" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 5}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[4].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">2F</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg2f" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[4].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload2f" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1e2f.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2f" value="${recordSelected[4].visitedLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>
                                                            <div class="form-group">
                                                                <input type="hidden" name="team1e2f.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2f" value="${recordSelected[4].visitedId}" />
                                                                <input type="hidden" name="team1e2f.visitedName" class="teamteamName2f" value="${recordSelected[4].visitedName}" />
                                                                <input type="hidden" name="team1e2f.visitedTeamGroupings" value="6"/>
                                                                <select id="statusSrch2f" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 6}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[4].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1G</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1g" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[5].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1g" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1g2h.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1g" value="${recordSelected[5].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>
                                                            <div class="form-group">
                                                                <input type="hidden" name="team1g2h.homeId" datatype="*" nullmsg="请选择球队" class="teamId1g" value="${recordSelected[5].homeId}" />
                                                                <input type="hidden" name="team1g2h.homeName" class="teamteamName1g" value="${recordSelected[5].homeName}" />
                                                                <input type="hidden" name="team1g2h.homeTeamGroupings" value="7"/>
                                                                <select id="statusSrch1g" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 7}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[5].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group col-xs-6 col-md-3">
                                                                <div class="form-group" style="width:65px;height:80px;">
                                                                    <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                        <h2 style="font-weight:bold;margin-right: 20px;">2H</h2>
                                                                        <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                            <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                                <c:if test="${recordSelected == null}">
                                                                                    <h5 style="font-weight:bold;">LOGO</h5>
                                                                                </c:if>
                                                                                <img class="activityCon_as" id="activityConimg2h" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[5].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                            </div>
                                                                            <!-- 按钮管理 -->
                                                                            <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                                <input type="file" name="file" id="fileupload2h" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                                <input type="hidden" name="team1g2h.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2h" value="${recordSelected[5].visitedLogo}" />
                                                                            </div>
                                                                        </div>
                                                                    </label>
                                                                </div>
                                                                <div class="form-group">
                                                                    <input type="hidden" name="team1g2h.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2h" value="${recordSelected[5].visitedId}" />
                                                                    <input type="hidden" name="team1g2h.visitedName" class="teamteamName2h" value="${recordSelected[5].visitedName}" />
                                                                    <input type="hidden" name="team1g2h.visitedTeamGroupings" value="8"/>
                                                                    <select id="statusSrch2h" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                        <option value="请选择球队"></option>
                                                                        <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                            <c:if test="${bean.teamGroupings == 8}">
                                                                                <option value="${bean.id }" <c:if test="${recordSelected[5].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                    <c:out value="${bean.teamName }"></c:out>
                                                                                </option>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                        </div>
                                                    </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1F</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1f" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[2].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1f" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1f2e.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1f" value="${recordSelected[2].homeLogo} " />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1f2e.homeId" datatype="*" nullmsg="请选择球队" class="teamId1f" value="${recordSelected[2].homeId}" />
                                                                <input type="hidden" name="team1f2e.homeName" class="teamteamName1f" value="${recordSelected[2].homeName}" />
                                                                <input type="hidden" name="team1f2e.homeTeamGroupings" value="6"/>
                                                                <select id="statusSrch1f" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 6}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[2].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group col-xs-6 col-md-3">
                                                                <div class="form-group" style="width:65px;height:80px;">
                                                                    <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                        <h2 style="font-weight:bold;margin-right: 20px;">2E</h2>
                                                                        <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                            <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                                <c:if test="${recordSelected == null}">
                                                                                    <h5 style="font-weight:bold;">LOGO</h5>
                                                                                </c:if>
                                                                                <img class="activityCon_as" id="activityConimg2e" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[2].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                            </div>
                                                                            <!-- 按钮管理 -->
                                                                            <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                                <input type="file" name="file" id="fileupload2e" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                                <input type="hidden" name="team1f2e.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2e" value="${recordSelected[2].visitedLogo}" />
                                                                            </div>
                                                                        </div>
                                                                    </label>
                                                                </div>
                                                                <div class="form-group">
                                                                    <input type="hidden" name="team1f2e.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2e" value="${recordSelected[2].visitedId}" />
                                                                    <input type="hidden" name="team1f2e.visitedName" class="teamteamName2e" value="${recordSelected[2].visitedName}" />
                                                                    <input type="hidden" name="team1f2e.visitedTeamGroupings" value="5"/>
                                                                    <select id="statusSrch2e" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                        <option value="请选择球队"></option>
                                                                        <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                            <c:if test="${bean.teamGroupings == 5}">
                                                                                <option value="${bean.id }" <c:if test="${recordSelected[2].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                    <c:out value="${bean.teamName }"></c:out>
                                                                                </option>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                        </select>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="row">
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group" style="width:65px;height:80px;">
                                                                <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                    <h2 style="font-weight:bold;margin-right: 20px;">1H</h2>
                                                                    <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                        <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                            <c:if test="${recordSelected == null}">
                                                                                <h5 style="font-weight:bold;">LOGO</h5>
                                                                            </c:if>
                                                                            <img class="activityCon_as" id="activityConimg1h" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[3].homeLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                        </div>
                                                                        <!-- 按钮管理 -->
                                                                        <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                            <input type="file" name="file" id="fileupload1h" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                            <input type="hidden" name="team1h2g.homeLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo1h" value="${recordSelected[3].homeLogo}" />
                                                                        </div>
                                                                    </div>
                                                                </label>
                                                            </div>

                                                            <div class="form-group">
                                                                <input type="hidden" name="team1h2g.homeId" datatype="*" nullmsg="请选择球队" class="teamId1h" value="${recordSelected[3].homeId}" />
                                                                <input type="hidden" name="team1h2g.homeName" class="teamteamName1h" value="${recordSelected[3].homeName}" />
                                                                <input type="hidden" name="team1h2g.homeTeamGroupings" value="8"/>
                                                                <select id="statusSrch1h" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                    <option value="请选择球队"></option>
                                                                    <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                        <c:if test="${bean.teamGroupings == 8}">
                                                                            <option value="${bean.id }" <c:if test="${recordSelected[3].homeId == bean.id}">selected="selected"</c:if>>
                                                                                <c:out value="${bean.teamName }"></c:out>
                                                                            </option>
                                                                        </c:if>
                                                                    </c:forEach>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group">
                                                            </div>
                                                            <div class="form-group">
                                                                <img class="activityCon_as" class="img" src="${ctx}/hyjf-admin/image/worldcup/vs.png">
                                                            </div>
                                                        </div>
                                                        <div class="form-group col-xs-6 col-md-3">
                                                            <div class="form-group col-xs-6 col-md-3">
                                                                <div class="form-group" style="width:65px;height:80px;">
                                                                    <label class="control-label" style="display: flex;width:5px;height:80px;">
                                                                        <h2 style="font-weight:bold;margin-right: 20px;">2G</h2>
                                                                        <div class="fileinput fileinput-new  picClass" data-provides="fileinput" >
                                                                            <div style="margin: 11px 0px 0px 7px;"  class="fileinput-preview fileinput-exists myimg">
                                                                                <c:if test="${recordSelected == null}">
                                                                                    <h5 style="font-weight:bold;">LOGO</h5>
                                                                                </c:if>
                                                                                <img class="activityCon_as" id="activityConimg2g" style="width: 60px;height: 60px; border-radius: 45px;" src="<c:if test="${recordSelected != null}">${recordSelected[3].visitedLogo}</c:if><c:if test="${recordSelected == null}">${ctx}/hyjf-admin/image/worldcup/activityCon_team.png</c:if>">
                                                                            </div>
                                                                            <!-- 按钮管理 -->
                                                                            <div style="margin:25px 0px 0px 0px;height: 60px;width: 60px;" class="user-edit-image-buttons">
                                                                                <input type="file" name="file" id="fileupload2g" class= "fileupload" onclick="fileUpload(this)" value="LOGO" style="opacity: 0;" accept="image/jpeg,image/png,image/gif,image/x-ms-bmp,image/tiff">
                                                                                <input type="hidden" name="team1h2g.visitedLogo" datatype="*" nullmsg="请选择球队LOGO" class="teamteamLogo2g" value="${recordSelected[3].visitedLogo}" />
                                                                            </div>
                                                                        </div>
                                                                    </label>
                                                                </div>

                                                                <div class="form-group">
                                                                    <input type="hidden" name="team1h2g.visitedId" datatype="*" nullmsg="请选择球队" class="teamId2g" value="${recordSelected[3].visitedId}" />
                                                                    <input type="hidden" name="team1h2g.visitedName" class="teamteamName2g" value="${recordSelected[3].visitedName}" />
                                                                    <input type="hidden" name="team1h2g.visitedTeamGroupings" value="7"/>
                                                                    <select id="statusSrch2g" class=" form-control underline form-select2" onchange="clickstatusSrch(this)" >
                                                                        <option value="请选择球队"></option>
                                                                        <c:forEach items="${record}" var="bean" begin="0" step="1" varStatus="status">
                                                                            <c:if test="${bean.teamGroupings == 7}">
                                                                                <option value="${bean.id }" <c:if test="${recordSelected[3].visitedId == bean.id}">selected="selected"</c:if>>
                                                                                    <c:out value="${bean.teamName }"></c:out>
                                                                                </option>
                                                                            </c:if>
                                                                        </c:forEach>
                                                                    </select>
                                                                </div>
                                                             </div>
                                                        </div>
                                                     </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-md-6"></div>
                                            <div class="col-md-6">
                                                <shiro:hasPermission name="worldCupactivityConf:ADD">
                                                    <c:if test="${mathResult == 0}">
                                                        <a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit">
                                                            <i class="fa fa-check"></i>  提交</i>
                                                        </a>
                                                    </c:if>
                                                    <c:if test="${mathResult != 0}">
                                                        <a class="btn btn-primary btn-wide pull-right margin-right-15 fn-Submit" disabled="true">
                                                            <i class="fa fa-check"></i>  提交</i>
                                                        </a>
                                                    </c:if>
                                                </shiro:hasPermission>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </tiles:putAttribute>

        <%-- 检索面板 (ignore) --%>
        <tiles:putAttribute name="searchPanels" type="string">
        </tiles:putAttribute>

        <%-- 对话框面板 (ignore) --%>
        <tiles:putAttribute name="dialogPanels" type="string">
            <iframe class="colobox-dialog-panel" id="urlDialogPanel" name="dialogIfm" style="border: none; width: 100%; height: 100%"></iframe>
        </tiles:putAttribute>

        <%-- 画面的CSS (ignore) --%>
        <tiles:putAttribute name="pageCss" type="string">
            <link href="${themeRoot}/vendor/plug-in/colorbox/colorbox.css" rel="stylesheet" media="screen">
            <style>
                /*img .myimg{*/
                /*height: 20px;*/
                /*width: 20px;*/
                /*}*/
                <%--.thumbnails-wrap {--%>
                <%--border: 1px solid #ccc;--%>
                <%--padding: 3px;--%>
                <%--display: inline-block;--%>
                <%--}--%>
                <%--/*.thumbnails-wrap img {*/--%>
                <%--/*min-width: 35px;*/--%>
                <%--/*max-width: 70px;*/--%>
                <%--/*height: 22px;*/--%>
                <%--/*}*/--%>
                <%--.popover {--%>
                <%--max-width: 500px;--%>
                <%--}--%>
                <%--.popover img {--%>
                <%--max-width: 460px;--%>
                <%--}--%>
            </style>
        </tiles:putAttribute>

        <%-- JS全局变量定义、插件 (ignore) --%>
        <tiles:putAttribute name="pageGlobalImport" type="string">
            <!-- Form表单插件 -->
            <%@include file="/jsp/common/pluginBaseForm.jsp"%>
            <script src="${themeRoot}/vendor/plug-in/bootstrap-fileinput/jasny-bootstrap.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/bootstrap-ladda/spin.min.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/bootstrap-ladda/ladda.min.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/tinymce/jquery.tinymce.min.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/My97DatePicker/WdatePicker.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/region-select.js"></script>
            <script type="text/javascript" src="${themeRoot}/assets/js/common.js"></script>
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.ui.widget.js"></script>
            <!-- The Templates plugin is included to render the upload/download listings -->
            <%--      <script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/tmpl.min.js"></script> --%>
            <!-- The Load Image plugin is included for the preview images and image resizing functionality -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/load-image.all.min.js"></script>
            <!-- The Canvas to Blob plugin is included for image resizing functionality -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/canvas-to-blob.min.js"></script>
            <!-- blueimp Gallery script -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/vendor/jquery.blueimp-gallery.min.js"></script>
            <!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.iframe-transport.js"></script>
            <!-- The basic File Upload plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload.js"></script>
            <!-- The File Upload processing plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-process.js"></script>
            <!-- The File Upload image preview & resize plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-image.js"></script>
            <!-- The File Upload audio preview plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-audio.js"></script>
            <!-- The File Upload video preview plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-video.js"></script>
            <!-- The File Upload validation plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-validate.js"></script>
            <!-- The File Upload user interface plugin -->
            <script type="text/javascript"
                    src="${themeRoot}/vendor/plug-in/jquery-file-upload/jquery.fileupload-ui.js"></script>
        </tiles:putAttribute>

        <%-- Javascripts required for this page only (ignore) --%>
        <tiles:putAttribute name="pageJavaScript" type="string">
            <script type='text/javascript' src="${webRoot}/jsp/manager/activity/worldCupActivityconfiguration/worldCupActivityconfiguration.js"></script>
        </tiles:putAttribute>

    </tiles:insertTemplate>
</shiro:hasPermission>
