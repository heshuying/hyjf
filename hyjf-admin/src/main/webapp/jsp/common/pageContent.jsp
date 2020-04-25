<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>

<div class="main-content" >
	<div class="wrap-content container" id="container">
		<!-- start: PAGE TITLE -->
		<section id="page-title" class="padding-top-10 padding-bottom-10 no-display">
			<div class="row">
				<tiles:insertAttribute name="pageHeader" ignore="true" />
				<c:if test="${empty pageHeader }">
				<div class="col-sm-8">
					<tiles:insertAttribute name="pageFunCaption" ignore="true" />
				</div>
				</c:if>
			</div>
		</section>
		<!-- end: PAGE TITLE -->
		
		<!-- start: YOUR CONTENT HERE -->
		<tiles:insertAttribute name="mainContentinner" />
		<!-- end: YOUR CONTENT HERE -->
	</div>
</div>