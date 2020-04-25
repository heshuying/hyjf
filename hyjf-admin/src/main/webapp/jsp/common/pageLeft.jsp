<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="hyjf" uri="/hyjf-tags"%>

<!-- sidebar -->
<div class="sidebar app-aside" id="sidebar">
	<c:if test="${hasMenu ne 'false' }">
	<div class="sidebar-container perfect-scrollbar">
		<nav>
			<c:if test="${!empty searchAction }">
			<!-- start: SEARCH FORM -->
			<div class="search-form no-display">
				<a class="s-open" href="#"><i class="ti-search"></i></a>
				<form class="navbar-form" role="search">
					<a class="s-remove" href="#" target=".navbar-form"><i class="ti-close"></i></a>
					<div class="form-group">
						<input type="text" class="form-control" placeholder="Search...">
						<button class="btn search-button" type="submit">
							<i class="ti-search"></i>
						</button>
					</div>
				</form>
			</div>
			<!-- end: SEARCH FORM -->
			</c:if>
			<!-- start: MAIN NAVIGATION MENU -->
			<div class="navbar-title no-display">
				<span>Main Navigation</span>
			</div>
			<hyjf:main-menu urlPrefix="${webRoot}"></hyjf:main-menu>
			<!-- end: MAIN NAVIGATION MENU -->
		</nav>
	</div>
	</c:if>
</div>
<!-- / sidebar -->
