<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>

<%-- <link rel="stylesheet" href="<%=path %>/js/uploadify/uploadify.css"> --%>
<style>
	.block2 .lists{
		padding:1px 0;
	}
	.submit1{
		    padding: 12px 0 14px 0;
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
			<!--Begin block8-->
			<div class="block8">
				<!--Begin lists-->
				<div class="lists">
					<div class="title">
						<h3>
							<img src="<%=theme %>/images/database2.png" alt="" /> 我的专题数据库
						</h3>
						<h4>
							<input type="button" class="btns btn1" id="import-topic-btn"
								value="导入专题">
						</h4>
						<div class="clear"></div>
					</div>
					<ul>
						<c:forEach items="${voList}" var="view">
						              <li>
						                <div class="txts1">
						                  <dl>
						                    <dt>${view.db.alias }</dt>
						                    <dd>
<!-- 						                      <h4>化工信息网</h4> -->
						                      <span class="T1">磁盘占用：<i>${view.space}</i><b>M</b></span> <span class="T1">专利条目：<i>${view.patentCount }</i><b>条</b></span> </dd>
						                  </dl>
						                </div>
						                <div class="txts2">
						                  <dl>
						                    <dt>专题下用户</dt>
						                    <dd>
						                      <h4><a href="">${view.db.user.username }</a></h4>
						                      <span class="T1">登录次数：<i>${view.loginCount }</i> <b>次</b></span> <span class="T1">最近登录时间：<i><fmt:formatDate value="${view.loginEndTime}" type="both"/></i></span> </dd>
						                  </dl>
						                </div>
						                <div class="buttons">
						                					<input type="button" class="btns" onclick="add('${view.db.name }','${view.db.name}')" value="追加数据" />
															<input type="button" class="btns" onclick="modifyTopic('${view.db.id }','${view.db.alias}')" value="修改" />
															<input type="button" class="btns"  value="打开" style="display: none;" /> 
															<input type="button" class="btns btn2" onclick="delTopic('${view.db.id }','${view.db.name}')" value="删除" />
														</div>
						                <div class="clear"></div>
						              </li>
						              </c:forEach>
					</ul>
				</div>
				<!--End lists-->
			</div>
			<!--End block8-->

		</div>

		<div class="container" id="createTopicDiv" style="display: none">
			<div class="block8">
				<div class="lists">
					<div class="title">
						<h3>
							<img src="<%=theme %>/images/database2.png" alt="" /> 我的专题数据库
						</h3>
						<h4>
							<input type="button" class="btns btn1" id="backToTopicBtn"
								value="返回">
						</h4>
						<div class="clear"></div>
					</div>
				</div>
			</div>
			<div class="block2">
				<div class="lists">
					<div class="wrap">

						<form id="create-topic-form" method="post"
							action="<%=path %>/topic/create.html" class="form-horizontal" onsubmit="return check();"  enctype="multipart/form-data">
							<input id="jsessionid" type="hidden" value="${pageContext.session.id}"/> 
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						                <td >专题名称</td>
						             </tr>
						             <tr>
						                <td><input id="topicname" name="topicname"  type="text"  required="required"/></td>
						             </tr>
						             <tr>
						                <td><br/>自定义字段1</td>
						             </tr>
						             <tr>
						                <td>
						                    <label style="width: 10%;">名称&nbsp;&nbsp;</label><input name="dynaFiled" style="width: 30%;" type="text"/><label style="width: 10%;">&nbsp;&nbsp;EXCEL标题</label>
						                    <input name="dynaFiledCode" style="width: 30%;" type="text"/>
						               </td>
						              </tr>
						              <tr>
						                <td><br/>自定义字段2</td>
						              </tr>
						              <tr>
						                <td>
						                    	<label style="width: 10%;">名称&nbsp;&nbsp;</label><input name="dynaFiled" style="width: 30%;" type="text"/><label style="width: 10%;">&nbsp;&nbsp;EXCEL标题</label>
						                    <input name="dynaFiledCode" style="width: 30%;" type="text"/>
						                </td>
						              </tr>
						              <tr>
						                <td><br/>自定义字段3</td>
						              </tr>
						              <tr>
						                <td>
						                    	<label style="width: 10%;">名称&nbsp;&nbsp;</label><input name="dynaFiled" style="width: 30%;" type="text"/><label style="width: 10%;">&nbsp;&nbsp;EXCEL标题</label>
						                    <input name="dynaFiledCode" style="width: 30%;" type="text"/>
						                </td>
						              </tr>
						              <tr>
						                <td><br/>数据文件</td>
						              </tr>
						              <tr>
						                <td><input id="uploadfile" name="uploadfile" type="file" style="text-indent: 0px;" /></td>
						              </tr>
						              <!--  <tr>
										<td ><a style="color: blue;"
											href="topicTemplate.xlsx">系统定义模板下载</a></td>
									</tr> -->
								</tbody>
							 </table>
							<div>
								<table>
									<tbody>
										<tr>
											<td>导入模板<br/></td>
										</tr>
										<tr>
											<td>
												<select name="templete">
												<option value="">默认模板</option>
												<c:forEach items="${confList}" var="vo" varStatus="s">
											  		<option value="${vo.profileUrl}">${vo.profileName}</option>
											  	</c:forEach>
										  	</select>
										  	</td>
										</tr>
									</tbody>
								</table> 
							</div>
							<div class="submit1 submitBG1">
								<input type="submit" value="创建" class="btn1">
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>
<!-- Button trigger modal -->
</div>


<!-- modify topic layer -->
<div class="Rwrap" style="display: none" id="modifyTopicLayer">
	<div class="container">
		<div class="block2">
			<div class="lists">
				<div class="wrap" style="width: 86%;">
					<form id="modify-topic-form" method="post"
						action="<%=path %>/topic/modify.html" class="form-horizontal">
						<input name="id" type="hidden" />
						<table>
							<tbody>
								<tr>
									<td style="width: 60px;">专题名称</td>
									<td><input name="name" type="text" /></td>
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
</div>

<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<%-- <script src="<%=path %>/js/uploadify/jquery.uploadify.js"></script> --%>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>


<script type="text/javascript">

$(function() {
	$('#import-topic-btn').on('click',function(){
		if('${user.role.code}' == 'company_child'){
			alert('当前用户为企业子账号，无法操作此功能。');
			return;
		}
		$('.container').hide();
// 		$('#create-topic-form').find('input[name=topicname]').attr('disabled',"");
		$('#create-topic-form').find('input[name=topicname]').attr('style',"");
		$('#create-topic-form')[0].reset();
		$('#createTopicDiv').show();
		
	});
	$('#backToTopicBtn').on('click',function(){
		$('.container').show();
		$('#createTopicDiv').hide();
	});
	
	$("object").css({"width":"68%"});//设置上传按钮宽度  CF
	
    $('#modify-topic-form').ajaxForm(function(data) {
    	$('#addUserForm').clearForm();
    	   	 if('success' == data){
    	   		 layer.close(index1);
    	   		 alert('专题名称修改成功');
    	   		 window.location.reload();
    	   	 }else{
    	   		alert('专题名称修改失败');	   		 
    	   	 }
      });
    
});

var index1 = 0;
function modifyTopic(tId,tName){
	$('#modify-topic-form')[0].id.value=tId;
	$('#modify-topic-form')[0].name.value=tName;
	index1 = layer.open({
		  type: 1,
		  title: ['修改专题','padding-right:333px;color: #FFF;background-color: #962925;'],
		  area:'400px',
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: $('#modifyTopicLayer')
		});
};

/*//ajax submit 
$(function(){
	var options = { 
           beforeSubmit:  validate,
           success:  showResponse 
   };
   $('#create-topic-form').ajaxForm(options);	
})

function validate(formData, jqForm, options) { 
    var formElement = jqForm[0];
    
    var topicname = formElement.topicname.value;

    if(!topicname){
			alert('请输入专题名称');
		return false;
	}
	
	if(!is_upload){
			alert('请选择数据文件');
		return false;
	}
	
    return true; 
} 
 
function showResponse(responseText, statusText, xhr, $form)  { 
	if(responseText && responseText != 'success'){
 	   alert(responseText);
    }else{
 	   $('#modify-pass-form').clearForm();
 	   alert('专题创建成功');
    }
} */

function delTopic(tId,tName){
	
	layer.confirm('确定要删除该专题吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
			  function(index){
				layer.close(index);
				var loadIndex = layer.load(0,{shade: [0.4, '#393D49']});
				$.ajax({
				       url: 'topic/delete.html',
					       type: "post",
					       data: {'id':tId},
					       success: function(resp) {
					     	   layer.close(loadIndex);
					    	   if('success' == resp){
					    		   alert('专题删除成功');
					    		   window.location.reload();
					    	   }else{
					    		   alert(resp);
					    	   }
					       }
					});
				
			}, function(index){
				layer.close(index);
			});
}

function add(tName){
	if('${user.role.code}' == 'company_child'){
		alert('当前用户为企业子账号，无法操作此功能。');
		return;
	}
	$('.container').hide();
	$('#create-topic-form').find('input[name=topicname]').val(tName);
	$('#create-topic-form').find('input[name=topicname]').attr('readonly',"readonly");
	$('#create-topic-form').find('input[name=topicname]').attr('style',"background:#e3e3e3");
	$('#create-topic-form').find('input[type=submit]').val('追加');
	$('#createTopicDiv').show();
}

function check(){
	var topicname = $('#create-topic-form').find('#topicname').val();
	if (!topicname) {
		alert("请输专题名称!");
		return false;
	}
	
	if(legalfile()){
		return true;
	}
	
	return false;
}


function legalfile(){
	 var file =$("#uploadfile").val();
	 if(file !=''){
	   var extension=file.substring(file.lastIndexOf(".")+1);
	   
	   var arr = [ "xls","XLS","xlsx","XLSX",'zip','ZIP'];
	   if($.inArray(extension, arr) == '-1'){
		   alert('请选择正确格式的文件!');
		   return false;
		 }
	 }else{
		 alert('请选择文件!');
		 return false;
	 }
	 return true;
}

</script>
