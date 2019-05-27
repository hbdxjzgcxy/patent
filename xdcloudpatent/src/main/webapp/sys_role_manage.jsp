<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<style>
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
.layui-layer-molv .layui-layer-title {
		padding-right:230px;
		color: #FFF;
		background-color: #962925;
	}
</style>
<div class="right_sider">
<div class="Rwrap">
  <div class="container">
<div class="nav"  style="margin-bottom: 15px;padding-left:10px;"><a href="#">系统管理</a> &gt; <span>角色与权限</span> </div>
    <div class="search"  >
      <div class="wrap">
        <div class="search_input">
          <input type="text" id="position" placeholder="Search...">
          <div class="btns0">
            <input type="button" class="btn1" id="create-account-btn" value="新建角色">
          </div>
          <div class="btns">
            <input type="button" id="position-btn" class="btn1" value="查询">
          </div>
        </div>
      </div>
    </div>
    <!--Begin block5-->
    <div class="block1">
      <div class="lists">
        <div class="title">
          <h3><img src="<%=theme %>/images/user4.png" alt=""/> 角色管理</h3>
        </div>
        <table id="jtable">
        	<thead>
            <tr>
              <th align="center">角色名称 </th>
              <th align="center">角色代码</th>
              <th align="center">空间(MB)</th>
              <th align="center">数据库数量</th>
              <th align="center">角色描述</th>
              <th align="center">操作</th>
            </tr>
            </thead>
          <tbody>
            <c:forEach items="${roles}" var="role">
	            <tr>
	              <td align="center">${role.name}</td>
	              <td align="center">${role.code }</td>
	              <td align="center">${role.space}</td>
	              <td align="center">${role.dbCount }</td>
	              <td align="center">${role.desc }</td>
	              <td align="center"><input type="button"  class="btns" onclick="modifyRole('${role.id}')" value="修改">
	                <input type="button"  class="btns btn1" onclick="deleteRole('${role.id}')" value="删除"></td>
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
<div class="Rwrap" style="display:none" id="add-role-layer">
	<div class="container">
	    <div class="block2">
	      <div class="lists">
	        <form id="add-role-form" method="post" action="<%=path %>/adminCenter/addRole.html" class="form-horizontal" >
	          <table>
	            <tbody>
	              <tr>
	                <td  style="width:75px;">角色名称</td>
	                <td><input name="name"  type="text"/></td>
	              </tr>
	              <tr>
	                <td  style="width:60px;">角色代码</td>
	                <td><input name="code"  type="text"/></td>
	              </tr>
	             <tr>
	                <td>空间(MB)</td>
	                <td><input name="space"  type="text" value="0"/></td>
	              </tr><tr>
	                <td>数据库数量</td>
	                <td><input name="dbCount"  type="text"  value="0"/></td>
	              </tr><tr>
	                <td>角色描述</td>
	                <td><textarea name="desc" rows="4" cols="24"></textarea>
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


<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>
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


	index1=0;
	
	$('#create-account-btn').on('click',function(){
		index1 = layer.open({
			  type: 1,
			  title: ['创建角色','padding-right:333px;color: #FFF;background-color: #962925;'],
			  area:'400px',
			  closeBtn: 0,
			  closeBtn :1,
			  shadeClose: true,
			  content: $('#add-role-layer')
			});
	});
	// 角色添加  
	var options = { 
        beforeSubmit:  validateRole,  // pre-submit callback 
        success:       addRoleCallback// post-submit callback 
    }; 
    $('#add-role-form').ajaxForm(options); 
    
});


function modifyRole(identity){
	index1 = layer.open({
		  type: 2,
		  title: ['修改角色','padding-right:334px;color: #FFF;background-color: #962925;'],
		  area:['400px','315px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: ['<%=path %>/adminCenter/modifyRole.html?id='+identity,'no']
		});
}


function deleteRole(identity){
	layer.confirm('确定要删除该角色吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
	  function(index){
		$.ajax({
		       url: '<%=path %>/adminCenter/deleteRole.html',
		       type: "post",
		       data: {'id':identity},
		       success: function(resp) {
		    	   if('success' == resp){
		    		   alert('角色删除成功');
		    		   window.location.reload(true);
		    	   }else{
		    		   alert('角色删除失败');
		    	   }
		       }
		});
		
		layer.close(index);
		
	}, function(index){
		layer.close(index);
	});
}

function validateRole(formData, jqForm, options) { 
    var formElement = jqForm[0];
    
    var name = formElement.name.value;
	var code = formElement.code.value;
	
	if(!name){
			alert('请输入角色名称');
		return false;
	}
	
	if(!code){
			alert('请输入角色代码');
		return false;
	}
	
    return true; 
} 
 
function addRoleCallback(responseText, statusText, xhr, $form)  { 
	if(responseText && responseText != 'success'){
 	   alert(responseText);
    }else{
 	   $('#add-role-form').clearForm();
 	   alert('角色添加成功');
 	  layer.close(index1);
 	  window.location.reload(true); 
    }
}

</script>