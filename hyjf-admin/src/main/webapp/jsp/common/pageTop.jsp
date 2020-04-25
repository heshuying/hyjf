<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- start: TOP NAVBAR -->
<header class="navbar navbar-default navbar-static-top">
	<!-- start: NAVBAR HEADER -->
	<div class="navbar-header">
		<c:if test="${hasMenu ne 'false' }">
		<a href="#" class="sidebar-mobile-toggler pull-left hidden-md hidden-lg" class="btn btn-navbar sidebar-toggle"
				data-toggle-class="app-slide-off" data-toggle-target="#app" data-toggle-click-outside="#sidebar">
			<i class="ti-align-justify"></i>
		</a>
		<a class="navbar-brand" href="#">
			<!-- Logo -->
			<img src="${themeRoot}/images/logo.png" alt="HYJF" width="126px" height="34px"/>
		</a>
		<a href="#" class="sidebar-toggler pull-right visible-md visible-lg" data-toggle-class="app-sidebar-closed" data-toggle-target="#app">
			<i class="ti-align-justify"></i>
		</a>
		</c:if>
		<a class="pull-right menu-toggler visible-xs-block" id="menu-toggler" data-toggle="collapse" href=".navbar-collapse">
			<span class="sr-only">切换导航</span>
			<i class="ti-view-grid"></i>
		</a>
	</div>
	<!-- end: NAVBAR HEADER -->

	<!-- start: NAVBAR COLLAPSE -->
	<div class="navbar-collapse collapse">
		<ul class="nav navbar-right margin-right-0">

			<!-- start: MESSAGES DROPDOWN -->
			<li class="dropdown no-display">
				<a href class="dropdown-toggle" data-toggle="dropdown">
					<span class="dot-badge partition-red"></span> <i class="ti-comment"></i> <span>我的消息</span>
				</a>
				<ul class="dropdown-menu dropdown-light dropdown-messages dropdown-large">
					<li>
						<span class="dropdown-header">未处理的消息</span>
					</li><li>
						<div class="drop-down-wrapper ps-container">
							<ul>
								<li class="unread">
									<a href="javascript:;" class="unread">
										<div class="clearfix">
											<div class="thread-image">
												<img src="${themeRoot}/images/avatar/avatar-2.jpg" alt="">
											</div>
											<div class="thread-content">
												<span class="author">Nicole Bell</span>
												<span class="preview">Duis mollis, est non commodo luctus, nisi erat porttitor ligula...</span>
												<span class="time"> Just Now</span>
											</div>
										</div>
									</a>
								</li><li>
									<a href="javascript:;" class="unread">
										<div class="clearfix">
											<div class="thread-image">
												<img src="${themeRoot}/images/avatar/avatar-3.jpg" alt="">
											</div>
											<div class="thread-content">
												<span class="author">Steven Thompson</span>
												<span class="preview">Duis mollis, est non commodo luctus, nisi erat porttitor ligula...</span>
												<span class="time">8 hrs</span>
											</div>
										</div>
									</a>
								</li><li>
									<a href="javascript:;">
										<div class="clearfix">
											<div class="thread-image">
												<img src="${themeRoot}/images/avatar/avatar-5.jpg" alt="">
											</div>
											<div class="thread-content">
												<span class="author">Kenneth Ross</span>
												<span class="preview">Duis mollis, est non commodo luctus, nisi erat porttitor ligula...</span>
												<span class="time">14 hrs</span>
											</div>
										</div>
									</a>
								</li>
							</ul>
						</div>
					</li><li class="view-all">
						<a href="#">查看所有</a>
					</li>
				</ul>
			</li>
			<!-- end: MESSAGES DROPDOWN -->

			<!-- start: ACTIVITIES DROPDOWN -->
			<li class="dropdown no-display">
				<a href class="dropdown-toggle" data-toggle="dropdown">
					<i class="ti-check-box"></i> <span>相关活动</span>
				</a>
				<ul class="dropdown-menu dropdown-light dropdown-messages dropdown-large">
					<li>
						<span class="dropdown-header"> 您有新的活动通知</span>
					</li><li>
						<div class="drop-down-wrapper ps-container">
							<div class="list-group no-margin">
								<a class="media list-group-item" href="">
									<img class="img-circle" alt="..." src="${themeRoot}/images/avatar/avatar-1.jpg">
									<span class="media-body block no-margin"> Use awesome animate.css <small class="block text-grey">10 minutes ago</small> </span>
								</a>
								<a class="media list-group-item" href="">
									<span class="media-body block no-margin"> 1.0 initial released <small class="block text-grey">1 hour ago</small> </span>
								</a>
							</div>
						</div>
					</li><li class="view-all">
						<a href="#">查看所有</a>
					</li>
				</ul>
			</li>
			<!-- end: ACTIVITIES DROPDOWN -->

			<!-- start: LANGUAGE SWITCHER -->
			<li class="dropdown no-display">
				<a href class="dropdown-toggle" data-toggle="dropdown">
					<i class="ti-world"></i> English
				</a>
				<ul role="menu" class="dropdown-menu dropdown-light fadeInUpShort">
					<li>
						<a href="#" class="menu-toggler">简体中文</a>
					</li><li>
						<a href="#" class="menu-toggler">繁体中文</a>
					</li><li>
						<a href="#" class="menu-toggler">English</a>
					</li>
				</ul>
			</li>
			<!-- start: LANGUAGE SWITCHER -->

			<!-- start: USER OPTIONS DROPDOWN -->
			<li class="dropdown current-user">
				<a href class="dropdown-toggle" data-toggle="dropdown">
					<span class="username margin-top-10 margin-right-15">${sessionScope.LOGIN_USER_INFO.username}</span>
					<img src="${themeRoot}/images/avatar/avatar-1.jpg" alt="Peter">
					<span class="username margin-top-10 margin-left-5"><i class="ti-angle-down"></i></span>
				</a>
				<ul class="dropdown-menu dropdown-light">
					<li class="no-display">
						<a href="pages_user_profile.html">我的设置</a>
					</li><li class="no-display">
						<a href="pages_calendar.html">工作日程管理</a>
					</li><li class="no-display">
						<a hef="pages_messages.html">消息管理 (3)</a>
					</li><li class="no-display">
						<a href="javascript:void(0);" id="lockscreenBtn">屏幕锁定</a>
					</li>
					<li>
						<a href="${webRoot}/login/toUpdatePassword">修改密码</a>
					</li>
					<li>
						<a href="${webRoot}/login/loginOut">退出</a>
					</li>
				</ul>
			</li>
			<!-- end: USER OPTIONS DROPDOWN -->
		</ul>

		<!-- start: MENU TOGGLER FOR MOBILE DEVICES -->
		<div class="close-handle visible-xs-block menu-toggler" data-toggle="collapse" href=".navbar-collapse">
			<div class="arrow-left"></div>
			<div class="arrow-right"></div>
		</div>
		<!-- end: MENU TOGGLER FOR MOBILE DEVICES -->
	</div>
	<a class="dropdown-off-sidebar no-display"
			data-toggle-class="app-offsidebar-open1" data-toggle-target="#app" data-toggle-click-outside="#off-sidebar">&nbsp;</a>
	<!-- end: NAVBAR COLLAPSE -->
</header>
<!-- end: TOP NAVBAR -->
