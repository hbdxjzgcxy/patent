
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
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
	.hint {
	position: absolute;
	display: none;
	/*  left: 175px; */
	/*  top: 32px; */
	width: 100%;
	border: 1px solid #fadaaf;
	border-top: 0px;
	background-color: #fff9ec;
	color: #eb934c;
	line-height: 20px;
	padding: 6px 0px 6px;
	z-index: 10;
	background-color: #fff9ec;
}

.hint .selected {
	background-color: #1d75bd;
	color: #fff;
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
							<img src="<%=theme %>/images/database2.png" alt="" /> 我的模板库
						</h3>
						<h4>
							<input type="button" class="btns btn1" id="import-model-btn"
								value="添加模板">
						</h4>
						<div class="clear"></div>
					</div>
					<ul>
						<li>
							<div class="txts1">
								<dl>
									<dt>默认模板</dt>
										<!-- <dd></dd> -->
								</dl>
							</div>
							<div class="txts2">
								<dl>
									<dd>系统默认的数据导入模板</dd>
								</dl>
							</div>
							<div class="buttons">
								<input type="button" class="btns btn2" onclick="downloadModel(0)" value="下载" /> 
							</div>
							<div class="clear"></div>
						</li>
						<c:forEach items="${modelList}" var="view">
							<li>
								<div class="txts1">
									<dl>
										<dt>${view.profileName}</dt>
										<%-- <c:if test="${view.activity==true }">
											<dd>当前使用模板</dd>
										</c:if> --%>
									</dl>
								</div>
								<div class="txts2">
									<dl>
										<dd>${view.profileDesc}</dd>
									</dl>
								</div>
								<div class="buttons">
									<%-- <c:if test="${view.activity==false }">
						                	   <input type="button" class="btns" onclick="setDefault('${view.id }','${view.profileName}')" value="设置为默认" />
						             </c:if>  --%>
									<input type="button" class="btns"
										onclick="modifyModel('${view.id}')" value="修改" /> <input
										type="button" class="btns"
										onclick="delModel('${view.id }','${view.profileName}')"
										value="删除" /> <input type="button" class="btns"
										onclick="downloadModel('${view.id}')" value="下载" /> <input
										type="button" id="upload" class="btns btn2"
										onclick="uploadModel('${view.id}')" value="上传" />

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
		<div class="container" id="createModelDiv" style="display: none">
			<div class="block8">
				<div class="lists">
					<div class="title">
						<h3>
							<img src="<%=theme %>/images/database2.png" alt="" /> 模板管理
						</h3>
						<h4>
							<input type="button" class="btns btn1" id="backToModelBtn"
								value="返回" />
						</h4>
						<div class="clear"></div>
					</div>
				</div>
			</div>
			<form id="create-model-form" method="post"
					action="<%=path %>/model/add.html" class="form-horizontal" onsubmit="return test();"  enctype="multipart/form-data">
				<div class="block2">
					<input id="jsessionid" type="hidden" value="${pageContext.session.id}" /> 
					<div class="lists">
						<div class="title">
							<h3>模板信息</h3>
						</div>
						<div class="wrap">
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						                <td  >模板名称</td>
						                </tr>
						             <tr>
						                <td><input id="modelName" name="modelName"  type="text"  required="required"/></td>
						             </tr>
						             <tr>
						                <td><br/>模板信息描述</td>
						                 </tr>
						             <tr>
						                <td>
						                    <input id="modelDesc" name="modelDesc" type="text"/>
						                </td>
						             </tr>
								</tbody>
							</table>
						</div>
					</div>
					<div class="lists">
						<div class="title">
							<h3>映射配置</h3>
						</div>
						<div class="wrap">
							<table style="border-collapse:separate; border-spacing:0px 10px;" id="configTab">
								<thead>
									<tr>
										<td>索引字段名称</td>
										<td>EXCEL标题</td>
										<td>多值分隔符</td>
									</tr>
								</thead>
								<tbody>
									<tr>
						                <td ><select name="indexName" >
						                		<option value="AN" selected="selected">AN</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" placeholder="多个标题间用“；”隔开"/>
						                </td>
						                <td><input type="text" name="separator" placeholder="例如：|;" />
						                </td>
						             </tr>
									 <tr>
						                <td><select name="indexName" >
						                		<option value="IPC" selected="selected">IPC</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" placeholder="例如：|;" />
										</td>
									</tr>
									<tr>
						                <td><select name="indexName" >
						                		<option value="PA" selected="selected">PA</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" placeholder="例如：|;" />
										</td>
									</tr>
						             <tr>
						                <td><select name="indexName" >
						                		<option value="PN" selected="selected">PN</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" placeholder="例如：|;" />
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
					<div>
						<input type="button" value="增加映射" class="btn1" onclick="addtr()">
					</div>
					<!-- <div class="lists">
						<div class="title">
							<h3>默认设置</h3>
						</div>
						<div class="wrap">
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						               <td>
						               	 <input type="checkbox" style="height: 18px" name="activity" value="true"/>
						               </td>
						               <td>是否设置为默认模板</td>
						            </tr>
								</tbody>
							</table>
						</div>
					</div> -->
				</div>
			<div class="submit1 submitBG1">
				<input type="submit" value="创建" class="btn1">
			</div>
			</form>
		</div>
	</div>
</div>

<div class="Rwrap" style="display: none" id="file-upload">
	<div class="container">
		<div class="block2">
			<div class="lists">
				<form id="file-upload-form" method="post" action="<%=path%>/model/upload.html" class="form-horizontal" enctype="multipart/form-data" >
				<input  name="tid" id="tid" type="hidden"/>
					<table>
						<tbody>
							<tr>
								<td width="40px">文件</td>
								<td><input name="uploadfile" id="uploadfile" type="file" accept="application/xml" style="text-indent: 0px;" /></td>
							</tr>
						</tbody>
					</table>
					<div class="submit1">
						<input type="submit" value="上传" class="btn1">
					</div>
				</form>
			</div>
		</div>
	</div>
</div>
<!-- Button trigger modal -->
<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<%-- <script src="<%=path %>/js/uploadify/jquery.uploadify.js"></script> --%>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>


<script type="text/javascript">

$(function() {
	$('#import-model-btn').on('click',function(){
		$('.container').hide();
		$('#create-model-form').find('input[name=modelName]').attr('style',"");
		$('#create-model-form')[0].reset();
		$('#createModelDiv').show();
		
	});
	$('#backToModelBtn').on('click',function(){
		$('.container').show();
		$('#createModelDiv').hide();
	});
	
	$("object").css({"width":"68%"});//设置上传按钮宽度  CF
});

var index1=0;
function uploadModel(id){
	$("#tid").val(id);
	index1 = layer.open({
		  type: 1,
		  title: ['文件上传','padding-right:333px;color: #FFF;background-color: #962925;'],
		  area:'400px',
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: $('#file-upload')
		});
}

/**
 * 设置为默认模板
 */
function setDefault(tId,tName){
	layer.confirm('确定要设置该模板为默认模板吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
			  function(index){
				layer.close(index);
				var loadIndex = layer.load(0,{shade: [0.4, '#393D49']});
				$.ajax({
				       url: 'model/default.html',
					       type: "post",
					       data: {'id':tId},
					       success: function(resp) {
					     	   layer.close(loadIndex);
					    	   if('success' == resp){
					    		   alert('默认模板设置成功');
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

/**
 * 修改模板
 */
var index1 = 0;
function modifyModel(tId){
	index1 = layer.open({
		  type: 2,
		  title: ['修改模板','padding-right:0px;color: #FFF;background-color: #962925;'],
		  area:['800px','600px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: 'model/tomodify.html?id='+tId
		});
};
/**
 * 删除模板
 */
function delModel(tId,tName){
	layer.confirm('确定要删除该模板吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
			  function(index){
				layer.close(index);
				var loadIndex = layer.load(0,{shade: [0.4, '#393D49']});
				$.ajax({
				       url: 'model/delete.html',
					       type: "post",
					       data: {'id':tId},
					       success: function(resp) {
					     	   layer.close(loadIndex);
					    	   if('success' == resp){
					    		   alert('模板删除成功');
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

/**
 * 下载模板
 */
function downloadModel(id){
	var url = "<%=path %>/model/download.html?id="+id;
	window.location.href=url;
}

/**
 * 创建前验证
 */
function test(){
	var modelName = $('#create-model-form').find('#modelName').val();
	if (!modelName) {
		alert("请输专题名称!");
		return false;
	}
	var flag = true;
	var lag = true;
	 $("#configTab select[name='indexName']").each(function(){
         var value = $(this).val();
         if( $("#configTab select[name='indexName'] option[value='"+value+"']:selected").size()>1){
             flag= false;
         } 
     });
     $("#configTab input[name='EXCName']").each(function(){
    	 var val = $(this).val();
    	 if(!val){
    		lag= false;
    	 }
     })
     if(!flag){
	 	alert("索引字段重复，请合并后创建！");
	 	return false;
     }else if(!lag){
     	alert("请输入EXCEL标题！");
     	return false;
     }else {
    	 return true;
     }
}
/**
 * 动态添加映射
 */
function addtr(){
	var table = document.getElementById("configTab");
	var newRow = table.insertRow();
	newRow.id = new Date().getTime();
	var newCell1 = newRow.insertCell(0);
	var select = "<select name='indexName' ><option value='AB'>AB</option><option value='AD'>AD</option><option value='ADDR'>ADDR</option><option value='AGC'>AGC</option><option value='AGT'>AGT</option><option value='AU'>AU</option><option value='CDN'>CDN</option>"
	+"<option value='CLM'>CLM</option><option value='CPC'>CPC</option><option value='CTN'>CTN</option><option value='LS'>LS</option><option value='LSE'>LSE</option><option value='PC'>PC</option><option value='PD'>PD</option>"
	+"<option value='PFN'>PFN</option><option value='PR'>PR</option><option value='TI'>TI</option><option value='FT'>FT</option><option value='PRC'>PRC</option><option value='PRN'>PRN</option><option value='PRD'>PRD</option></select>";
	newCell1.innerHTML  = "<td>"+select+"</td>";
	var newCell2 = newRow.insertCell(1);
	newCell2.innerHTML  = "<td><input type='text' name='EXCName' placeholder='多个标题间用“；”隔开'/></td>";
	var newCell3 = newRow.insertCell(2);
	newCell3.innerHTML  = "<td><input type='text' name='separator' placeholder='例如：|;' /></td>";
	var newCell4 = newRow.insertCell(3);
	newCell4.innerHTML  = "<td><input type='button' style='width:60px; text-align: center;background-color: buttonface;' value='删除' class='btn1' onclick='deltr("+newRow.id+")'></td>"
}
/**
 * 动态删除映射
 */
function deltr(rowId){
	var table = document.getElementById("configTab");
	var row = document.getElementById(rowId);
	table.deleteRow(row.rowIndex);
}
</script>
