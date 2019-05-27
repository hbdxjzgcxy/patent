<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@include file="/mytags.jsp" %>


<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<style>
.lists td {
	font-size:14px;
}
	.submit1{
		    padding: 2px 0 3px 0;
	}
	.block1 {
    padding: 0 12%;
    background: #fff;
}
.block2 {
    padding: 0 12%;
    background: #fff;
}
</style>
	<div class="container">
	    <div class="block2">
	      <div class="lists">
	        <form id="modifyRoleForm" method="post" action="<%=path %>/adminCenter/update.html" class="form-horizontal" >
	        <input type="hidden" name="id" value="${role.id }" />
	          <table>
	            <tbody>
	              <tr>
	                <td  style="width:60px;">角色代码</td>
	                <td><input name="code"  type="text"  value="${role.code }" disabled="disabled" style="background:#e3e3e3;"/></td>
	              </tr>
	              <tr>
	                <td  style="width:75px;">角色名称</td>
	                <td><input name="name"  type="text" value="${role.name }"  /></td>
	              </tr>
	             <tr>
	                <td>空间(MB)</td>
	                <td><input name="space"  type="text"  value="${role.space }"/></td>
	              </tr><tr>
	                <td>数据库数量</td>
	                <td><input name="dbCount"  type="text"  value="${role.dbCount }"/></td>
	              </tr><tr>
	                <td>角色描述</td>
	                <td><textarea name="desc" rows="4"  cols="24">${role.desc }</textarea>
	                </td>
	              </tr>
	              </tbody>
	          </table>
			    <div class="submit1 submitBG1" style="padding: 2px 0 3px 0;">
			      <input type="submit" value="修改" class="btn1">
			    </div>
	          </form>
	      </div>
	    </div>
    </div>
    
<script src="<%=path %>/js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<script type="text/javascript">

$(document).ready(function() {
    $('#modifyRoleForm').ajaxForm(function(data) {
    	 $('#modifyRoleForm').clearForm();
  	   	 alert('角色信息修改成功');
    	 window.parent.location.reload();
       });
   });

</script>

