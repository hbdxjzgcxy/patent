<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>

<div class="left_sider">
  <div class="container1">
    <div class="dis"></div>
<!--     <div class="title"> -->
<!--       <h3>管理中心</h3> -->
<!--     </div> -->
    <dl>
      <dt class="dt_cur">
        <h3 class="dt-1"><a href="javascript:void(0)">用户信息管理</a></h3>
        <h4><img src="<%=theme %>/images/arrow_down4.png"  alt="" /></h4>
        <div class="clear"></div>
      </dt>
      <dd>
        <ul>
          <li id="s_setting"><a href="<%=path %>/user/toSettingInfo.html">- 用户信息设置</a> </li>
          <li id="s_modify"><a href="<%=path %>/user/toModifypass.html">- 修改密码</a> </li>
          <li id="s_favorites"><a href="favorite/userfavorites.html">- 收藏夹</a> </li>
          <li id="s_history"><a href="<%=path %>/adminCenter/history.html">- 检索历史</a> </li>
        </ul>
      </dd>
      <c:if test="${user.role.code ne 'company_child' && user.role.code ne 'admin' }">
      <dt>
        <h3 class="dt-2"><a href="">内容管理</a></h3>
        <h4><img src="<%=theme %>/images/arrow_down4.png"  alt=""/></h4>
        <div class="clear"></div>
      </dt>
      <dd>
        <ul>
          <li id="s_topic"><a href="<%=path %>/topic/home.html">- 专题库管理</a> </li>
          <c:if test="${user.role.code ne 'company_child' }">
          	<li id="s_sub"><a href="<%=path %>/adminCenter/child.html">- 子帐号管理</a> </li>
          </c:if>
          <li id="s_model"><a href="<%=path %>/model/show.html">- 模板管理</a></li>
          <li id="s_pdf"><a href="<%=path %>/pdf/show.html">- PDF文件处理</a></li>
        </ul>
      </dd>
      </c:if>
      <c:if test="${user.role.code eq 'admin' }">
	      <dt>
	        <h3 class="dt-3"><a href="">系统管理</a></h3>
	        <h4><img src="<%=theme %>/images/arrow_down4.png" alt=""/></h4>
	        <div class="clear"></div>
	      </dt>
	      <dd>
	        <ul>
	          <li id="s_account"><a href="<%=path %>/adminCenter/account.html">- 账号管理</a> </li>
<!-- 	          <li><a href="">- 认证申请</a> <span>(3)</span> </li> -->
	          <li id="s_role"><a href="<%=path %>/adminCenter/role.html">- 角色与权限</a> </li>
<!-- 	          <li><a href="">- 专题管理</a> </li> -->
	          <li id="s_log" ><a href="<%=path %>/adminCenter/logs.html">- 用户日志</a> </li>
	          <li id="s_param" ><a href="<%=path %>/adminCenter/param.html">- 参数配置</a> </li>
<!-- 	          <li><a href="">- 在线用户</a> </li> -->
	        </ul>
	      </dd>
      </c:if>
    </dl>
  </div>
</div>
<script type="text/javascript">
$(function(){
	$("#s_${sel}").addClass("dd_cur");
})


$("dt").click(function(){
	  $(this).siblings('.dt_cur').removeClass('dt_cur');
	  
	  $(this).addClass('dt_cur');
	  var lists = $(this).next('dd');
	  lists.toggle('slow');
	  if($(this).find('img').attr('src') == '<%=theme %>/images/arrow_down4.png')
		  $(this).find('img').attr('src','<%=theme %>/images/arrow_up4.png');
	  else 
		  $(this).find('img').attr('src','<%=theme %>/images/arrow_down4.png');
});

$("li").click(function(){
	/* alert($(sel)); */
	$("#s_${sel}").addClass("dd_cur");
});

</script>