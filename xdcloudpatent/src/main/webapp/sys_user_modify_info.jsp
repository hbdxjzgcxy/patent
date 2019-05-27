<%@ page language="java" import="com.xdtech.patent.entity.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/mytags.jsp" %>

<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">

<style>

.block2 {
    padding: 0 12%;
    background: #fff;
}
.lists td {
	font-size:14px;
}
</style>
<div class="">
	<div class="container">
	    <div class="block2">
	      <div class="lists">
	        <form id="settingInfoForm" method="post" action="<%=path %>/user/settingInfo.html" class="form-horizontal" >
	        	<input name="id" value="${mUser.id }" type="hidden" />
	          <table>
	            <tbody>
	              <tr>
	                <td  style="width:60px;">用户名</td>
	                <td><input style="background:#e3e3e3;" name="username" value="${mUser.username }" disabled="disabled" type="text"/></td>
	              </tr>
	             <tr>
	                <td>邮箱</td>
	                <td><input name="email" value="${mUser.email }" type="text"/></td>
	              </tr><tr>
	                <td>手机</td>
	                <td><input name="phone"  value="${mUser.phone }" type="text"/></td>
	              </tr>
	               <tr>
	                <td>头像</td>
	                <td><input id="fileupload" type="file" name="photo" /></td>
	              </tr>
	              <tr>
	                <td>角色</td>
	                <td>
	                	<select name="user.role.id" <c:if test="${user.role.code eq 'company_child' }"> disabled="disabled"</c:if>>
	                	<c:forEach items="${roles }" var="role">
	                			<option value="${role.id }" <c:if test="${role.id eq mUser.role.id }">selected="selected"</c:if>>${role.name }</option>
	                	</c:forEach>
	                	</select>
	                </td>
	              </tr>
	              <tr>
	                <td>公司</td>
	                <td><input name="company" value="${mUser.company }" type="text"/></td>
	              </tr>
	              </tbody>
	          </table>
			    <div class="submit1 submitBG1">
			      <input type="submit" value="修改" class="btn1">
			    </div>
	          </form>
	      </div>
	    </div>
</div>
</div>
<script src="<%=path%>/js/jquery/jquery-1.12.4.min.js"></script>
<script src="<%=path%>/js/uploadify/jquery.uploadify.js"></script>
<script src="<%=path%>/js/jquery/jquery.form.js"></script>
<script type="text/javascript">

$(document).ready(function() {
    $('#settingInfoForm').ajaxForm(function(data) {
  	   	 alert('用户信息修改成功');
    	 $('#settingInfoForm').clearForm();
    	 window.parent.location.reload();
       });
   });

</script>
