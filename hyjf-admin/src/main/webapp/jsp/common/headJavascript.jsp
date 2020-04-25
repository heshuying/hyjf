	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
	<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
	<!--[if lt IE 9]>
		<script type="text/javascript" src="${themeRoot}/vendor/bootstrap/support/html5shiv-printshiv.min.js"></script>
		<script type="text/javascript" src="${themeRoot}/vendor/bootstrap/support/respond.min.js"></script>
	<![endif]-->
	<%-- JavaScript plug-in patch --%>
	<tiles:insertAttribute name="pageJsPatch" ignore="true" />
	
	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script type="text/javascript" src="${themeRoot}/vendor/jquery/jquery.min.js"></script>
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/modernizr.min.js"></script>
	
	<!--[if lte IE 8]>
		<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.placeholder.min.js"></script>
	<![endif]-->
	
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script type="text/javascript" src="${themeRoot}/vendor/bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/perfect-scrollbar/perfect-scrollbar.min.js"></script>
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/switchery/switchery.min.js"></script>
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/JSON-js-master/json2.min.js"></script>
	<script type="text/javascript" src="${themeRoot}/vendor/plug-in/jquery.cookie.min.js"></script>
	<script>
		var webRoot = "${webRoot}",
			themeRoot = "${themeRoot}";
	</script>