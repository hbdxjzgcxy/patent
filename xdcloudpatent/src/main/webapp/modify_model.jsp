
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<script src="<%=path%>/js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path%>/js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=path%>/js/search.js"></script>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<link rel="stylesheet" type="text/css" href="<%=path%>/js/jvmap/jquery.vector-map.css"/>
<link rel="stylesheet" type="text/css" href="<%=path %>/js/jquery-ui/themes/jquery-ui.css"/>

<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<%-- <script src="<%=path %>/js/uploadify/jquery.uploadify.js"></script> --%>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>
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
<!-- Button trigger modal -->
<!-- modify topic layer -->
<div class="Rwrap" style="display: block" id="modifyModelLayer">
	<div class="container">
		<div class="block2">
			<div class="lists">
				<div class="wrap" style="width: 100%;">
					<form id="modify-model-form" method="post" action="<%=path %>/model/modify.html" class="form-horizontal" onsubmit="return test();">
						<input name="id" type="hidden" value="${id}"/>
						<input id="jsessionid" type="hidden" value="${pageContext.session.id}" /> 
							<table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						                <td >模板名称</td>
						                <td><input id="modelName" name="modelName" value="${profileName }" type="text"  required="required"/></td>
						             </tr>
						             <tr>
						                <td>模板信息描述</td>
						                <td>
						                    <input id="modelDesc" name="modelDesc" value="${profileDesc }" type="text"/>
						                </td>
						             </tr>
								</tbody>
							</table>
							<table style="border-collapse:separate; border-spacing:0px 10px;" id="configMod">
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
						                <td><input type="text" name="EXCName" id="EXCName" value="${AN.excelName}" placeholder="多个标题间用“；”隔开"/>
						                </td>
						                <td><input type="text" name="separator" value="${AN.sepa }" placeholder="例如：|;" />
						                </td>
						             </tr>
						             <tr>
						                <td><select name="indexName" >
						                		<option value="PN" selected="selected">PN</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" id="EXCName" value="${PN.excelName}" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" value="${PN.sepa }" placeholder="例如：|;" />
										</td>
									</tr>
									 <tr>
						                <td><select name="indexName" >
						                		<option value="IPC" selected="selected">IPC</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" id="EXCName" value="${IPC.excelName}" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" value="${IPC.sepa }" placeholder="例如：|;" />
										</td>
									</tr>
									<tr>
						                <td><select name="indexName" >
						                		<option value="PA" selected="selected">PA</option>
						                	</select></td>
						                <td><input type="text" name="EXCName" id="EXCName" value="${PA.excelName}" placeholder="多个标题间用“；”隔开" />
										</td>
										<td><input type="text" name="separator" value="${PA.sepa }" placeholder="例如：|;" />
										</td>
									</tr>
									<c:forEach items="${mapList}" var="impo" varStatus="status">
										<tr id="${status.count }">
											<td>
												<select name='indexName' >
												<option value='AU' <c:if test='${("AU")eq(impo.indexName)}'>selected</c:if> >AU</option><option value='AB' <c:if test='${("AB")eq(impo.indexName)}'>selected</c:if>>AB</option><option value='PR'<c:if test='${("PR")eq(impo.indexName)}'>selected</c:if>>PR</option><option value='AGC'<c:if test='${("AGC")eq(impo.indexName)}'>selected</c:if>>AGC</option>
												<option value='CLM'<c:if test='${("CLM")eq(impo.indexName)}'>selected</c:if>>CLM</option><option value='PFN'<c:if test='${("PFN")eq(impo.indexName)}'>selected</c:if>>PFN</option><option value='CTN'<c:if test='${("CTN")eq(impo.indexName)}'>selected</c:if>>CTN</option><option value='CDN'<c:if test='${("CDN")eq(impo.indexName)}'>selected</c:if>>CDN</option>
												<option value='CPC'<c:if test='${("CPC")eq(impo.indexName)}'>selected</c:if>>CPC</option><option value='LSE'<c:if test='${("LSE")eq(impo.indexName)}'>selected</c:if>>LSE</option><option value='LS'<c:if test='${("LS")eq(impo.indexName)}'>selected</c:if>>LS</option><option value='TI' <c:if test='${("TI")eq(impo.indexName)}'>selected</c:if>>TI</option>
												<option value='AGT'<c:if test='${("AGT")eq(impo.indexName)}'>selected</c:if>>AGT</option><option value='AD'<c:if test='${("AD")eq(impo.indexName)}'>selected</c:if>>AD</option><option value='PD'<c:if test='${("PD")eq(impo.indexName)}'>selected</c:if>>PD</option><option value='PC'<c:if test='${("PC")eq(impo.indexName)}'>selected</c:if>>PC</option>
												<option value='ADDR'<c:if test='${("ADDR")eq(impo.indexName)}'>selected</c:if>>ADDR</option><option value='FT'<c:if test='${("FT")eq(impo.indexName)}'>selected</c:if>>FT</option><option value='PRN'<c:if test='${("PRN")eq(impo.indexName)}'>selected</c:if>>PRN</option><option value='PRC'<c:if test='${("PRC")eq(impo.indexName)}'>selected</c:if>>PRC</option>
												<option value='PRD'<c:if test='${("PRD")eq(impo.indexName)}'>selected</c:if>>PRD</option>
												</select>
											</td>
											<td>
											<input type="text" name="EXCName" id="EXCName" value="${impo.excelName}" placeholder="多个标题间用“；”隔开" />
											</td>
											<td>
												<input type="text" name="separator" value="${impo.sepa }" placeholder="例如：|;" />
											</td>
											<td>
												<input type="button" style="width:60px; text-align: center;background-color: buttonface;" value="删除" class="btn1" onclick="deltr(${status.count })">
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
							<div>
								<input type="button" value="增加映射" class="btn1" onclick="addtr()">
							</div>
							<%-- <table style="border-collapse:separate; border-spacing:0px 10px;">
								<tbody>
									<tr>
						               <td>
						               	 <input type="checkbox" style="height: 18px" name="activity" value="true" <c:if test='${("true")eq(activity)}'>checked</c:if> />
						               </td>
						               <td>是否设置为默认模板</td>
						            </tr>
								</tbody>
							</table> --%>
						<div class="submit1 submitBG1">
							<input type="submit" value="修改" class="btn1">
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>



<script type="text/javascript">
$(function() {
	 $('#modify-model-form').ajaxForm(function(data) {
	    	   	 if('success' == data){
	    	   		 parent.location.reload();
	    	   		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
	    	   		parent.layer.close(index); //再执行关闭   
	    	   		alert('模板名称修改成功');
	    	   	 }else{
	    	   		alert('模板名称修改失败');	   		 
	    	   	 }
	      });
});
/**
 * 修改保存前的校验
 */
function test(){
	var modelName = $('#modify-model-form').find('#modelName').val();
	if (!modelName) {
		alert("请输专题名称!");
		return false;
	}
	var flag = true;
	var lag = true;
	 $("#configMod select[name='indexName']").each(function(){
         var value = $(this).val();
         if( $("#configMod select[name='indexName'] option[value='"+value+"']:selected").size()>1){
             flag= false;
         } 
     });
     $("#configMod input[name='EXCName']").each(function(){
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
	var table = document.getElementById('configMod');
	var newRow = table.insertRow();
	newRow.id = new Date().getTime();
	var newCell1 = newRow.insertCell(0);
	var select = "<select name='indexName' ><option value='AB'>AB</option><option value='AD'>AD</option><option value='ADDR'>ADDR</option><option value='AGC'>AGC</option><option value='AGT'>AGT</option><option value='AU'>AU</option><option value='CDN'>CDN</option>"
		+"<option value='CLM'>CLM</option><option value='CPC'>CPC</option><option value='CTN'>CTN</option><option value='LS'>LS</option><option value='LSE'>LSE</option><option value='PC'>PC</option><option value='PD'>PD</option>"
		+"<option value='PFN'>PFN</option><option value='PR'>PR</option><option value='TI'>TI</option><option value='FT'>FT</option><option value='PRN'>PRN</option><option value='PRC'>PRC</option><option value='PRD'>PRD</option></select>";
	newCell1.innerHTML  = "<td>"+select+"</td>";
	var newCell2 = newRow.insertCell(1);
	newCell2.innerHTML  = "<td><input type='text' name='EXCName' id='EXCName' placeholder='多个标题间用“；”隔开'/></td>";
	var newCell3 = newRow.insertCell(2);
	newCell3.innerHTML  = "<td><input type='text' name='separator' placeholder='例如：|;' /></td>";
	var newCell4 = newRow.insertCell(3);
	newCell4.innerHTML  = "<td><input type='button' style='width:60px; text-align: center;background-color: buttonface;' value='删除' class='btn1' onclick='deltr("+newRow.id+")'></td>"
}
/**
 * 动态删除映射
 */
function deltr(rowId){
	var table = document.getElementById("configMod");
	var row = document.getElementById(rowId);
	table.deleteRow(row.rowIndex);
}
</script>
