<%@page import="com.xdtech.patent.action.ParamContext"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="systemParam" value="<%=ParamContext.getParamInfo()%>" />
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<script type="text/javascript" lang="javascript">
	$(function(){
		$("#m_${cur}").addClass("cur");
	})
	
	function logout(){
		$.removeCookie("user_ssl_key",{"path":"/"});
		$.removeCookie("user_ssl_pk",{"path":"/"});
		window.location.href='<%=path%>/logout.html';
	}
</script>
<!---------------Begin header--------------->
<div class="header">
	<div>
		<div class="top">
			<div class="logo" style="margin: 10px 30px;">
				<img src="<%=path%>/${systemParam.logo}" alt=""  style="height:41px; width:123px"/>
			</div>
			<div class="search" style="display: none">
				<span><img src="<%=theme%>/images/dots.png" alt="" /></span> <!--<img
					src="<%=theme%>/images/search1.png" alt="" />  <input type="text"
					placeholder="search..."> -->
			</div>
			<div class="right">
				<ul>
					<%-- <li><img src="<%=theme%>/images/inquire.png" alt="" /> <a
						href="#">在线客服</a></li>
					<li><img src="<%=theme%>/images/diag.png" alt="" /> <a
						href="#">意见反馈</a></li> --%> 
					<li><img src="<%=theme%>/images/tel.png" alt="" />免费客服电话 <span>${systemParam.phone}</span></li>
				</ul>
				<div class="clear"></div>
			</div>
		</div>
		<div class="clear"></div>
		<div class="bottom">
			<div class="user">
				<div class="img">
				<c:if test="${user.photo ne null}">
					<img src="resources/photo.html" alt="" onerror="this.style.display='none'" />
				</c:if>
				</div>
				<div class="txts">
					<span class="T1">${user.username }</span> <br /> <span class="T2">${user.email }</span>
				</div>
				<div class="btns">
					<input type="button" onclick="logout()" value="退出">
				</div>
			</div>
			<div class="menus">
				<ul>
					 <c:if test="${user.role.code eq 'admin' }">
					 	<li class="i-6"><a href="adminCenter/index.html" id="m_admin_center">管理中心</a></li>
					 </c:if>
					 <c:if test="${user.role.code ne 'admin' }">
						<li class="i-1"><a href="smart_search.html" id="m_home"></a></li>
						<li class="i-2"><a href="smart_search.html" id="m_smart">智能检索</a></li>
						<li class="i-3"><a href="table_search.html" id="m_table">表格检索</a></li>
						<li class="i-4"><a href="advance_search.html" id="m_advance">高级检索</a></li>
						<li class="i-7"><a href="ipc/index.html" id="m_ipc">IPC检索</a></li>
						<li class="i-5"><a href="analysis.html" id="m_analysis">专利分析</a></li>
						<li class="i-6"><a href="adminCenter/index.html" id="m_admin_center">管理中心</a></li>
					</c:if>
				</ul>
				<div class="clear"></div>
			</div>
			<div class="clear"></div>
		</div>
	</div>
	<input type="hidden" value="${cur}" id="cur"/>
</div>
<!---------------End header--------------->
