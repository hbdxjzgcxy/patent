<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp" %>
	<div class="right_sider">
		<div class="Rwrap">
			<div class="container">
				<div class="nav" style="margin-bottom: 15px;padding-left:10px;">
					<a href="#">用户信息管理</a> &gt; <span>用户信息设置</span>
				</div>
						<form id="settingInfoForm" method="post" action="<%=path %>/user/settingInfo.html" class="form-horizontal" enctype="multipart/form-data">
				<!--Begin block2-->
				<div class="block2">
					<div class="lists">
						<div class="wrap">
				        	<input name="id" value="${user.id }" type="hidden" />
				        	<input id="sessionId" type="hidden" value="${pageContext.session.id}"/> 
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						                <td  style="width:60px;">用户名</td>
						                <td><input name="username" value="${user.username }" disabled="disabled" type="text"/></td>
						              </tr>
						             <tr>
						                <td>邮箱</td>
						                <td><input name="email"  value="${user.email }" type="text"/></td>
						              </tr><tr>
						                <td>手机</td>
						                <td><input name="phone"  value="${user.phone }" type="text"/></td>
						              </tr>
						              <tr>
						                <td>头像</td>
						                <td>
<!-- 						                	>= IE10 -->
						                <input type="file" name="inFile" value="浏览" accept="image/*"/></td>
						              </tr>
						              <tr>
						                <td>角色</td>
						                <td>
						                	<select name="user.role.id">
						                	<c:forEach items="${roles }" var="role">
						                		<c:if test="${role.id eq user.role.id }">
						                			<option value="${role.id }">${role.name }</option>
						                		</c:if>
						                	</c:forEach>
						                	</select>
						                </td>
						              </tr>
						              <tr>
						                <td>公司</td>
						                <td><input name="company" value="${user.company }" type="text"/></td>
						              </tr>
								</tbody>
							</table>
								
						</div>
					</div>
				</div>
				<!--End block2-->
				<!--Begin submit1-->
				<div class="submit1 submitBG1">
					<input type="submit" value="修改 " class="btn1">
				</div>
				<!--End submit1-->
	<input type="hidden" name="searchType" value="table" />
				</form>
			</div>
		</div>
	</div>

<script src="js/jquery/jquery.form.js"></script>
<!-- <script src="js/uploadify/jquery.uploadify.js"></script> -->
<script type="text/javascript">

$(document).ready(function() {
	
    $('#settingInfoForm').ajaxForm(function(data) {
  	   	 alert('用户信息修改成功，重新登录之后生效。');
    	 $('#settingInfoForm').clearForm();
    	 window.location.href="adminCenter/index.html";
       });
             
   });
   
</script>

