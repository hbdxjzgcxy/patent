<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<style>
.block2 .lists {
    padding: 1px 0;
}
	.submit1{
		    padding: 12px 0 14px 0;
	}
	.layui-layer-molv .layui-layer-title {
		padding-right:230px;
		color: #FFF;
		background-color: #962925;
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
<div class="right_sider">
<div class="Rwrap">
  <div class="container">
<div class="nav"  style="margin-bottom: 15px;padding-left:10px;"><a href="#">内容管理</a> &gt; <span>子账号管理</span> </div>
    <div class="search" style="margin-top:2%">
      <div class="wrap">
        <div class="search_input">
          <input type="text" id="position" placeholder="Search...">
          <div class="btns0">
            <input type="button" class="btn1" id="create-account-btn" value="新建子账号">
          </div>
          <div class="btns">
            <input type="button" id="position-btn"  class="btn1" value="查询">
          </div>
        </div>
      </div>
    </div>
    <!--Begin block5-->
    <div class="block1">
      <div class="lists" style="margin-top: 2%">
        <div class="title">
          <h3><img src="<%=theme %>/images/user4.png" alt=""/> 子账号管理</h3>
        </div>
        <table id="jtable" class="display" cellspacing="0" width="100%">
        <thead>
            <tr>
              <th align="center">登陆用户名</th>
              <th align="center">公司名称 </th>
              <th align="center">账号类型</th>
              <th align="center">状态</th>
              <th align="center">最近登录</th>
              <th align="center">登录次数</th>
              <th align="center">操作</th>
            </tr>
        </thead>
          <tbody>
            <c:forEach items="${subUsers}" var="user">
	            <tr>
	              <td align="center">${user.username }</td>
	              <td align="center">${user.company}</td>
	              <td align="center">企业子账号</td>
	              <td align="center"><span class="T1">正常</span></td>
	              <td align="center">${user.lifeTime }</td>
	              <td align="center">${user.loginCount }</td>
	              <td align="center"><input type="button" class="btns" onclick="modifyUser('${user.id}')" value="修改">
	                <input type="button" class="btns btn1" onclick="deleteUser('${user.id}')" value="删除"></td>
	            </tr>
            </c:forEach>
          </tbody>
        </table>
<!--         <div class="pages"> <a href="">首页</a> <a href="">&lt;</a><a href="#" class="num cur">1</a><a href="#" class="num">2</a><a href="">&gt;</a><a href="">最后</a> </div> -->
      </div>
    </div>
    <!--End block5--> 
    </div>
  </div>
</div>

<!-- add user layer -->
<div class="Rwrap" style="display:none" id="add-user-layer">
	<div class="container">
	    <div class="block2">
	      <div class="lists">
	        <div>
	        <form id="add-user-form" method="post" action="<%=path %>/user/add.html" class="form-horizontal" >
	        <input name="pid"  type="hidden" value="${user.id }"/>
	        <input id="sessionId" type="hidden" value="${pageContext.session.id}"/> 
	          <table>
	            <tbody>
	              <tr>
	                <td  style="width:60px;">用户名</td>
	                <td><input name="username"  type="text"/></td>
	              </tr>
	              <tr>
	                <td  style="width:60px;">密码</td>
	                <td><input name="password"  type="password"/></td>
	              </tr>
	             <tr>
	                <td>EMAIL</td>
	                <td><input name="email"  type="text"/></td>
	              </tr><tr>
	                <td>手机</td>
	                <td><input name="phone"  type="text"/></td>
	              </tr>
	               <tr>
	                <td>头像</td>
	                <td><div style="border: 1px solid #C8C8CA;width:98%;" ><input name="inFile" type="file" accept="image/*"/></div></td>
	              </tr>
	              <tr>
	                <td>角色</td>
	                <td>
	                	<select name="role.id">
	                	<c:forEach items="${roles }" var="role">
	                		<c:if test="${role.code eq 'company_child' }">
	                			<option value="${role.id }">${role.name }</option>
	                		</c:if>
	                	</c:forEach>
	                	</select>
	                </td>
	              </tr>
	              <tr>
	                <td>公司</td>
	                <td><input name="company" type="text"/></td>
	              </tr>
	              </tbody>
	          </table>
			    <div class="submit1 submitBG1">
			      <input type="submit" value="创建" class="btn1">
			    </div>
	          </form>
	        </div>
	      </div>
	    </div>
    </div>
</div>


<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>
<script src="<%=path %>/js/uploadify/jquery.uploadify.js"></script>
<script src="<%=path %>/js/jquery-datatables/jquery.dataTables.min.js"></script>
<script type="text/javascript">

$(function(){
	
	var jtable = $('#jtable').DataTable({
		"paging":   false,
        "ordering": false,
        "info":     false,
        "searching":true,
        "language": {
            "zeroRecords": "没有符合当前检索条件的数据集"
        }
	});
	$('#jtable_filter').hide(); 
	$('#position-btn').on('click',function(){
		  var sWord = $('#position').val();
		  jtable.search(sWord).draw();
	  })
	
	
	<%-- var jsessionid = $('#sessionId').val(); 
	$("#fileupload").uploadify({
		'swf':'<%=path %>/js/uploadify/uploadify.swf',
		'fileTypeDesc' : '图片文件',
	    'fileTypeExts' : '*.png; *.jpg;',
	    'buttonText':'选择文件',
	    'removeCompleted':false,
		'uploader': '/user/photo.html;jsessionid='+jsessionid,
		'onUploadComplete' : function(file) {
//             alert('文件上传成功');
			//return false;
        }
	}); --%>
	
	
	index1=0;
	
	$('#create-account-btn').on('click',function(){
		index1 = layer.open({
			  type: 1,
			  title: ['创建子账号','padding-right:310px;color: #FFF;background-color: #962925;'],
			  area:'400px',
			  content: $('#add-user-layer')
			});
	});
	
//添加子账号  
var options = { 
    beforeSubmit:  validateUser,
    success:       addUserCallback
}; 
$('#add-user-form').ajaxForm(options);
  
$('#positionBtn').on('click',function(){
	  var sWord = $('#position').val();
	  if(!sWord){
		  alert('请输入检索词');
		  return;
	  }
	  jtable.search(sWord).draw();
})


});


function modifyUser(identity){
	index1 = layer.open({
		  type: 2,
		  title: ['修改子账号','padding-right:310px;color: #FFF;background-color: #962925;'],
		  area:['400px','315px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: ['<%=path %>/adminCenter/modify.html?id='+identity,'no']
		});
	
}


function deleteUser(identity){
	
	layer.confirm('确定要删除该子账号吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
	  function(index){
		$.ajax({
		       url: '<%=path %>/user/delete.html',
		       type: "post",
		       data: {'id':identity},
		       success: function(resp) {
		    	   if('success' == resp){
		    		   alert('子账号删除成功');
		    		   window.location.reload();
		    	   }else{
		    		   alert('子账号删除失败');
		    	   }
		       }
		});
		
		layer.close(index);
		
	}, function(index){
		layer.close(index);
	});
}

function validateUser(formData, jqForm, options) { 
    var formElement = jqForm[0];
    
    var name = formElement.username.value;
	var pwd = formElement.password.value;
	
	if(!name){
			alert('请输入用户名');
		return false;
	}
	
	if(!pwd){
			alert('请输入密码');
		return false;
	}
	
    return true; 
} 
 
function addUserCallback(responseText, statusText, xhr, $form)  { 
	if(responseText && responseText != 'success'){
 	   alert(responseText);
    }else{
 	   $('#addRoleForm').clearForm();
 	   alert('用户添加成功');
 	  window.location.reload(true); 
 	  layer.close(index1);
    }
}

</script>