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
<script src="<%=path%>/js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/js/search.js"></script>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/js/jvmap/jquery.vector-map.css"/>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-ui/themes/jquery-ui.css"/>
<script>
	function get_search_url(){
		return "<%=path%>/search.html?a=1";
	}
	function get_analysis_url(){
		return "<%=path%>/analysis.html?a=1";
	}
	function no_patent_img(ths){
		//var img=event.srcElement; 
		ths.src="<%=theme%>/images/user.png"; 
		ths.onerror=null; //控制不要一直跳动 
	}
	$(function(){
		$(".nav a").attr({"href":"javascript:;"})
	})
</script>
<base href="<%=basePath%>/" />
</head>
<body>
	<tiles:insertAttribute name="header" />
	<tiles:insertAttribute name="left" />
	<div class="LsiderBG"></div>
	<tiles:insertAttribute name="right" />
	<tiles:insertAttribute name="footer" />
</body>
</html>