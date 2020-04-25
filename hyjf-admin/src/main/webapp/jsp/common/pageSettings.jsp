<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- start: SETTINGS -->
<c:if test="${hasSettings ne 'false' }">
<div class="settings panel panel-default hidden-xs hidden-sm" id="settings">
	<button ct-toggle="toggle" data-toggle-class="active" data-toggle-target="#settings" class="btn btn-default">
		<i class="fa fa-spin fa-gear"></i>
	</button>
	<div class="panel-heading">
		布局设置
	</div>
	<div class="panel-body">
		
		<!-- start: FIXED HEADER -->
		<div class="setting-box clearfix">
			<span class="setting-title pull-left">锁定导航栏</span>
			<span class="setting-switch pull-right">
				<input type="checkbox" class="js-switch" id="navbar-fixed" />
			</span>
		</div>
		<!-- end: FIXED HEADER -->
		
		<!-- start: FIXED FOOTER -->
		<div class="setting-box clearfix">
			<span class="setting-title pull-left">锁定页脚栏</span>
			<span class="setting-switch pull-right">
				<input type="checkbox" class="js-switch" id="footer-fixed" />
			</span>
		</div>
		<!-- end: FIXED FOOTER -->
		
		<c:if test="${hasMenu ne 'false' }">
		<!-- start: FIXED SIDEBAR -->
		<div class="setting-box clearfix">
			<span class="setting-title pull-left">锁定功能菜单栏</span>
			<span class="setting-switch pull-right">
				<input type="checkbox" class="js-switch" id="sidebar-fixed" />
			</span>
		</div>
		<!-- end: FIXED SIDEBAR -->
		
		<!-- start: CLOSED SIDEBAR -->
		<div class="setting-box clearfix">
			<span class="setting-title pull-left">仅图标显示</span>
			<span class="setting-switch pull-right">
				<input type="checkbox" class="js-switch" id="sidebar-closed" />
			</span>
		</div>
		<!-- end: CLOSED SIDEBAR -->
		</c:if>
		
	</div>
</div>
</c:if>
<!-- end: SETTINGS -->
