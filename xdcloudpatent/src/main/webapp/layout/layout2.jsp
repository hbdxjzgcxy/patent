<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
 <%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><tiles:insertAttribute name="title" ignore="true"/></title>
<base href="<%=basePath%>/" />
<script src="js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/js/jquery/jquery.cookie.js"></script>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<script>
$(function(){
		$(".nav a").attr({"href":"javascript:;"})
})
function get_search_url(){
		return "<%=path%>/search.html?a=1";
	}
</script>
</head>
<body>
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="left" />
	<div class="LsiderBG"></div>
	<tiles:insertAttribute name="right" />
	<tiles:insertAttribute name="footer" />
</body>
</html>