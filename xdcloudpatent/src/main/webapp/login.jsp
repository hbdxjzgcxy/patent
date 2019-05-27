<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录</title>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<script src="js/jquery/jquery-1.12.4.min.js"></script>
<%-- <script src="js/jquery/jquery.validate.js"></script> --%>
<%-- <script src="js/jquery/messages_zh.js"></script> --%>
<script src="js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=path%>/js/jquery/jquery.cookie.js"></script>
<script>
	$(function(){
		$("#username").focus();
	})
</script>
<style>
</style>
</head>
<body>
	<div class="land">
		<div class="wrap">
			<div class="logo" style="padding-bottom: 15px;">
				<img src="<%=theme%>/images/logo.png" alt="" style="height: 41px; width: 123px" />
			</div>
			<form id="loginForm" method="post" action="login/login.html">
				<div class="contents">
					<h3>登录到你的账户</h3>
					<div  class="input i-1">
						<input id="username"  name="username" type="text" placeholder="用户名/邮箱/手机" required>
						<div class="icon">
							<img src="<%=theme%>/images/land/user.png" alt="" /> 用户名：
						</div>
					</div>
					<div class="input i-2">
						<input id="password" name="password"  type="password"  required>
						<div class="icon">
							<img src="<%=theme%>/images/land/unlock.png" alt="" /> 密&nbsp;&nbsp;&nbsp;码：
						</div>
					</div>
					<div class="T2">
						<div class="left">
							<input type="checkbox" id="remberMe" /> 保持我的登录状态
						</div>
						<!-- <div class="right">忘记密码?</div> -->
						<div class="clear"></div>
					</div>
					<div class="T3">
						<!-- <div class="left">你没有账号吗？请注册一个新账号</div> -->
						<div class="right">
							<input type="submit" value="登 录">
						</div>
						<div class="clear"></div>
					</div>
				</div>
				<input type="hidden" name="user_ssl_key" id="user_ssl_key" value="" />
				<input type="hidden" name="user_ssl_pk" id="user_ssl_pk" value="" />
			</form>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function() {
			var user_ssl_key = $.cookie("user_ssl_key");
			var user_ssl_pk=$.cookie("user_ssl_pk");
			if(user_ssl_key&&user_ssl_pk){
				$("#user_ssl_key").val(user_ssl_key);
				$("#user_ssl_pk").val(user_ssl_pk);
				$('#loginForm').attr("action","login/cookielogin.html");
				$('#loginForm').submit();
			}
			$('#loginForm').ajaxForm({
				success:function(data) {
					if ('false' == data) {
						$('#loginForm').clearForm();
			    		$('input[name=username]').focus();
						alert("您输入的账户或密码错误！");
						return false;
					}else{
						if($("#remberMe")[0].checked){
							recordUserInfo();
						}else{
							window.location.href="smart_search.html";
						}
						
					}
				}
			})
		})
		
		function recordUserInfo(){
			$.get("login/user_pk.html",function(_pk_){
				window.location.href="smart_search.html";
				$.cookie("user_ssl_key",$("#username").val(),{expires:7,domain:'<%=request.getServerName()%>',path:'/'});
				$.cookie("user_ssl_pk",_pk_,{expires:7,domain:'<%=request.getServerName()%>',path:'/'});
			});
		}
	</script>
</body>
</html>