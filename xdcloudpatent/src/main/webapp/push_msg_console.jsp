<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<style type="text/css">
.progress-panel {
	vertical-align: middle;
	height: 100%;
}

ul {
	padding-left: 2px;
}

li {
	list-style: none;
	font-size: 12px;
	color: #555;
}

.li-ok {
	color: #555;
}

.li-bar {
	border: 1px solid gray;
	cursor: pointer;
}

.li-bar div {
	background-color: green;
	color: #fff;
	text-align: center;
	cursor: pointer;
}

.li-cur {
	background-color: green;
	color: #fff;
	cursor: pointer;
}

.li-wait {
	background-color: #d63a35;
	color: #aaa;
	cursor: pointer;
}
</style>
<script type="text/javascript" src="js/jquery/jquery-1.12.4.min.js"></script>
<script>
	var _view_ = "ul-bar"; //显示

	var _timer_ = ""; //定时器
	
	var _interval_=1000;

	//刷新消息
	function refresh_msg() {
		$("#console").load("progress/topic.html?_rd="+Math.random()+"&id=" + _view_);
	}

	/*
	 *切换显示效果
	 */
	function switch_view() {
		if ($("#ul-bar").css("display") == "block") {
			$("#ul-bar").hide();
			$("#ul-progress").show();
			_view_ = "ul-progress";
		} else {
			$("#ul-bar").show();
			$("#ul-progress").hide();
			_view_ = "ul-bar";
		}
	}

	$(function() {
		var call_code="refresh_msg()";
		_timer_ = setInterval(call_code, _interval_);
		//reloadMsg();
	})
</script>
</head>
<body>
	<div id="console" style="width: 100%"></div>
</body>
</html>