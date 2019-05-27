<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<style>
.submit1 {
	padding: 12px 0 14px 0;
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
	padding-right: 230px;
	color: #FFF;
	background-color: #962925;
}
</style>
<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav" style="margin-bottom: 15px; padding-left: 10px;">
				<a href="#">系统管理</a> &gt; <span>账号管理</span>
			</div>
			<div class="search">
				<div class="wrap">
					<div class="search_input">
						<input type="text" id="position" placeholder="Search...">
						<div class="btns0">
							<input type="button" class="btn1" id="create-account-btn" value="新建账号">
						</div>
						<div class="btns">
							<input type="button" class="btn1" id="position-btn" value="查询">
						</div>
					</div>
				</div>
			</div>
			<!--Begin block5-->
			<div class="block1">
				<div class="lists">
					<div class="title">
						<h3>
							<img src="<%=theme%>/images/user4.png" alt="" /> 账号管理
						</h3>
					</div>
					<table id="jtable" class="display" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th align="center">登录用户名</th>
								<th align="center">公司名称</th>
								<th align="center">账号类型</th>
								<th align="center">状态</th>
								<th align="center">最近登录</th>
								<th align="center">登录次数</th>
								<th align="center">操作</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${users}" var="user">
								<tr>
									<td align="center"><a href="javascript:;" onclick="modifyUser('${user.id}')"><span class="T1">${user.username }</span></a></td>
									<td align="center">${user.company}</td>
									<td align="center">
										<c:if test="${user.role.code eq 'admin' }">管理员</c:if> 
										<c:if test="${user.role.code eq 'company' }">企业账号</c:if> 
										<c:if test="${user.role.code eq 'company_child' }">企业子账号</c:if>
									</td>
									<td align="center">
										<span class="T3">
											<c:if test="${user.disabled==true}">已禁用</c:if>
											<c:if test="${user.disabled==false}">正常</c:if> 
										</span>
									</td>
									<td align="center"><fmt:formatDate value="${user.lifeTime }" pattern="yyyy-MM-dd HH:mm"/></td>
									<td align="center">${user.loginCount }</td>
									<td align="left" >
										<c:if test="${user.username ne 'admin' }">
											<input type="button" class="btns" onclick="restPwd('${user.id}')"  value="重置密码" />
											<input type="button" class="btns btn1" onclick="deleteUser('${user.id}')" value="删除"/>
											<c:if test="${user.disabled==true}"><input type="button" class="btns btn1" onclick="enableUser('${user.id}')" value="启用"></c:if>
											<c:if test="${user.disabled==false}"><input type="button" class="btns btn1" onclick="disableUser('${user.id}')" value="禁用"></c:if>
										</c:if> 
									</td>
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
<div class="Rwrap" style="display: none" id="add-user-layer">
	<div class="container">
		<div class="block2">
			<div class="lists">
				<form id="add-user-form" method="post" action="<%=path%>/user/add.html" class="form-horizontal" onsubmit="return check()" class="form-horizontal">
					<input id="sessionId" type="hidden" value="${pageContext.session.id}" />
					<table>
						<tbody>
							<tr>
								<td style="width: 60px;">用户名</td>
								<td><input name="username" id="username" type="text" /></td>
							</tr>
							<tr>
								<td style="width: 60px;">密码</td>
								<td><input name="password" id="password" type="password" /></td>
							</tr>
							<tr>
								<td>EMAIL</td>
								<td><input name="email" type="text" /></td>
							</tr>
							<tr>
								<td>手机</td>
								<td><input name="phone" type="text" /></td>
							</tr>
							<tr>
								<td>头像</td>
								<td><input name="inFile" type="file" /></td>
							</tr>
							<tr>
								<td>角色</td>
								<td><select name="role.id">
										<c:forEach items="${roles }" var="role">
											<option value="${role.id }">${role.name }</option>
										</c:forEach>
									</select></td>
							</tr>
							<tr>
								<td>公司</td>
								<td><input name="company" type="text" /></td>
							</tr>
						</tbody>
					</table>
					<div class="submit1">
						<input type="submit" value="创建" class="btn1">
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<script src="js/jquery/jquery.form.js"></script>
<script src="js/uploadify/jquery.uploadify.js"></script>
<script src="js/layer-v1.9.3/layer/layer.js"></script>
<script src="js/jquery-datatables/jquery.dataTables.min.js"></script>
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
	  
	 
	
	var index1=0;
	
	$('#create-account-btn').on('click',function(){
		index1 = layer.open({
			  type: 1,
			  title: ['创建用户','padding-right:333px;color: #FFF;background-color: #962925;'],
			  area:'400px',
			  closeBtn: 0,
			  closeBtn :1,
			  shadeClose: true,
			  content: $('#add-user-layer')
			});
	});
	
	$('#add-user-form').ajaxForm(function(data) {
		$('#add-user-form')[0].reset();
		   	 if('success' == data){
		   		 alert('用户创建成功');
		   		 layer.close(index1);
		   		 window.location.reload();
		   	 }else{
		   		alert(data);	   		 
		   	 }
	  });
  
});

	//修改用户信息
	function modifyUser(identity){
	index1 = layer.open({
		  type: 2,
		  title: ['修改用户信息','padding-right:300px;color: #FFF;background-color: #962925;'],
		  area:['400px','315px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: ['<%=path%>/adminCenter/modify.html?id='+identity,'no']
		});
	}

	
	//删除用户
	function deleteUser(identity){
	
	layer.confirm('确定要删除该用户吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
	  function(index){
		$.ajax({
		       url: '<%=path%>/user/delete.html?rd_'+Math.random(),
				type : "post",
				data : {
					'id' : identity
				},
				success : function(resp) {
					if ('success' == resp) {
						window.location.reload();
					} else {
						alert('用户删除失败');
					}
				}
			});
			layer.close(index);
		}, function(index) {
			layer.close(index);
		});
	}
	
	//禁用用户
	function disableUser(identity){
		layer.confirm('确定要禁用该用户?禁用后该用户无法登录系统！',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
		  function(index){
			$.ajax({
			       url: '<%=path%>/user/disable.html?rd_'+Math.random(),
				type : "post",
				data : {
					'uid' : identity
				},
				success : function(resp) {
					if ('success' == resp) {
						window.location.reload();
					} else {
						alert('用户删除失败');
					}
				}
			});
			layer.close(index);
		}, function(index) {
			layer.close(index);
		});
	}
	
	//启用用户
	function enableUser(identity){
		layer.confirm('确定要启用该用户吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
		  function(index){
			$.ajax({
			       url: '<%=path%>/user/enable.html?rd_'+Math.random(),
				type : "post",
				data : {
					'uid' : identity
				},
				success : function(resp) {
					if ('success' == resp) {
						window.location.reload();
					} else {
						alert('用户删除失败');
					}
				}
			});
			layer.close(index);
		}, function(index) {
			layer.close(index);
		});
	}
	
	//启用用户
	function restPwd(identity){
		layer.confirm('确定要重置该用户的密码?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
		  function(index){
			$.ajax({
			       url: '<%=path%>/user/restPwd.html?rd_'+Math.random(),
				type : "post",
				data : {
					'uid' : identity
				},
				success : function(resp) {
					if ('success' == resp) {
						alert("密码已重置为[123456],请牢记!");
					} else {
						alert('操作失败!');
					}
				}
			});
			layer.close(index);
		}, function(index) {
			layer.close(index);
		});
	}

	//验证
	function check() {
		var username = $('#add-user-form').find('#username').val();
		var password = $('#add-user-form').find('#password').val();
		if (!username.trim()) {
			alert("请输入用户名!");
			return false;
		}
		if (!password.trim()) {
			alert("请输入密码!");
			return false;
		}
		return true;
	}
</script>