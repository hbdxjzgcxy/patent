<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<script src="<%=path%>/js/jquery/jquery-1.12.4.min.js"
	type="text/javascript"></script>
<style>
.btn1 {
	width: 68px;
	height: 32px;
	background-color: #006ce2;
	border: 1px solid #E2E2E2;
	color: white;
	font-family: '微软雅黑', sans-serif;
	border-radius: 4px;
	text-align: center;
	font-size: 14px;
}
</style>
</head>
<body>
	<div class="download_box popup_window" id="downloadselectresult"
		style="display: block; position: relative;">
		<div class="step">
			<span>第一步：</span>下载数据范围
		</div>
		<div class="d_con">
			<ul class="save_scopeType">
				<c:if test="${batch ==1}">
					<li class="center"><input type="radio" name="downtype" id="selRange1"
						value="cur" disabled="disabled" checked="checked"> <label for="selRange1">当前专利下载</label>
					</li>
				</c:if>
				<c:if test="${batch == 0 }">
					<li class="center"><input type="radio" name="downtype" id="selRange3" onclick="switchCheck('selRange3')"
						value="batch"  checked="checked"> <label for="selRange3">所有选中专利下载 </label></li>
					<%-- <c:if test="${kwd ne null && !empty kwd}"> --%>
					<li class="center"><input type="radio" name="downtype" onclick="switchCheck('selRange2')"
					id="selRange2" value="range" > <label for="selRange2">数据范围</label>
					<input name="from_val" id="from_val" type="text" size="3" value="1" disabled="disabled">
					~ <input name="to_val" id="to_val" type="text" size="5"  value="20" disabled="disabled">条</li>
					<%-- </c:if> --%>
				</c:if>
			</ul>
		</div>
		<div class="step">
			<span>第二步：</span>请选择模板
		</div>
		<div class="d_con">
			<select name="template" onchange="changeTemp(this)" style="width: 30%">
				<option value="">默认模板</option>
			<c:forEach items="${modelList}" var="vo">
				<option value="${vo.profileUrl}">${vo.profileName}</option>
			</c:forEach>
			</select>
		</div>
		<div class="step">
			<span>第三步：</span>请选择下载内容
		</div>
		<div class="d_con">
			<div class="down_select_area" id="option03">
				<dl class="down_select">
					<dt>可以选择的下载字段</dt>
					<dd>
						<select multiple="multiple" name="source" id="source"
							onclick="moveOption('source','target')"
							style="visibility: visible;">
							<c:forEach items="${mapFields}" var="item">
								<option value="${item.key}">${item.value}</option>
							</c:forEach>
							<option value="EXT_DISPLAY_1">自定义字段1</option>
							<option value="EXT_DISPLAY_2">自定义字段2</option>
							<option value="EXT_DISPLAY_3">自定义字段3</option>
							<!-- <option value="TI">名称</option>
							<option value="AB">摘要</option>
							<option value="AN">申请号</option>
							<option value="PN">公开(公告)号</option>
							<option value="AD">申请日</option>
							<option value="AU">发明（设计）人</option>
							<option value="PD">公开（公告）日</option>
							<option value="LS">法律状态</option>
							<option value="PA">申请（专利权）人</option>
							<option value="PR">优先权</option>
							<option value="AGC">专利代理机构</option>
							<option value="ADDR">申请（专利权）人地址</option>
							<option value="PC">中国国省代码</option>
							<option value="IPN">国际公布</option>
							<option value="PCTF">进入国家日期</option>
							<option value="OAN">分案申请</option>
							<option value="CNLX">专利类型</option>
							<option value="CLM">权利要求</option>
							<option value="IAN">国际申请</option>
							<option value="AGT">代理人</option>
							<option value="IPC">国际专利分类号</option>
							<option value="DC">国际外观设计分类号</option>
							<option value="SCY">审查员</option> -->
						</select>
						<p>
							<span class="btn_gray">
								<button type="button"
									onclick="moveAllOption('source', 'target')">全选</button>
							</span>
						</p>
					</dd>
				</dl>
				<p class="btn_zone">
					<span class="btn_add">
						<button type="button" onclick="moveOption('source', 'target');">添加</button>
					</span><br> <span class="btn_delete">
						<button type="button" onclick="moveOption('target', 'source');">删除</button>
					</span><br> <span class="btn_basic">
						<button type="button" onclick="downloadbasefileds();">基本项目</button>
					</span>
				</p>
				<dl class="down_select">
					<dt>已经选择的下载字段</dt>
					<dd>
						<select multiple="multiple" name="target" id="target"
							onclick="moveOption('target','source')"
							style="visibility: visible;">
						</select>
						<p>
							<span class="btn_gray">
								<button type="button"
									onclick="moveAllOption('target', 'source');">全删</button>
							</span>
						</p>
					</dd>
				</dl>
				<p class="clear">※ 单击字段名称可以选择或删除下载字段</p>
			</div>
		</div>
		<div class="step">
			<span>第四步：</span>请选择下载格式 <input type="radio" name="filetype"
				value="xls" checked="checked"> <label for="typeList2">Excel(.xls)
			</label> <input type="radio" name="filetype" value="text"> <label
				for="typeList1">Text(.rtf) </label>
		</div>
		<div class="d_con">
			<span><button onclick="goDownload();" type="button"
					class="btn1">下载</button></span>
		</div>
	</div>

	<form id="patentDownForm" method="post"
		action="<%=path%>/patentDown/download.html" class="form-horizontal">
		<input name="downtype" type="hidden" /> <input name="field" type="hidden" /> 
		<input name="filetype" type="hidden" /> <input name="word" type="hidden" value="${kwd}" />
		<input name="template" type="hidden"/>
		<input type="hidden" name="path" value="${path}" /> <input type="hidden" name="tName" value="${tName}" />
	</form>


	<script type="text/javascript">
	
		function downloadbasefileds() {
			var array = [ 'AB', 'TI' ];
			for (var i = 0; i < array.length; i++) {
				$("#target").append(
						$("#source").find("option[value=" + array[i] + "]"));
			}
		}
		function moveAllOption(source, target) {
			var sel = $("#" + source + " option");
			$("#" + target).append($(sel));
		}
		function moveOption(source, target) {
			var sel = $("#" + source + " option:selected");
			$("#" + target).append($(sel));
		}

		function goDownload() {
			var downtype = $('input[name=downtype]:checked').val();
			var start = 0;
			var rows = 0;
			var kwd = '${docNo}';
			var template = $("select[name='template']").val();
			if (downtype && downtype == 'range') {
				start = $('#from_val').val();
				rows = $('#to_val').val();
				kwd = '${kwd}';
			}
			if (rows > 500) {
				alert('下载数量不能超出500');
				return;
			}
			downtype = downtype + "&" + kwd + "&" + start + "-" + rows;

			var fields = $("#target option").map(function() {
				return $(this).val();
			});
			var filetype = $('input[name=filetype]:checked').val();
			if (fields.length <= 0) {
				alert('请选择下载字段');
				return;
			}

			$('#patentDownForm')[0].downtype.value = downtype;
			$('#patentDownForm')[0].field.value = fields.get().join(",");
			$('#patentDownForm')[0].filetype.value = filetype;
			$('#patentDownForm')[0].template.value = template;
			$('#patentDownForm').submit();

		}
		/**
		 * 切换模板选项
		 */
		function changeTemp(ths){
			var val = $(ths).val();
			var url = "<%=path%>/patentDown/fields.html?value="+val;
			var option = "";
			var d = $.ajax({
				type : "GET",
				url : url,
				async: false,
			}).responseText;
			var data = JSON.parse(d);
			$.each(data,function(key,value){
				option = option +"<option value="+key+">"+value+"</option>"
			})
			$("#source").empty();
			$("#target").empty();
			$("#source").append(option);
			var $option = "<option value='EXT_DISPLAY_1'>自定义字段1</option><option value='EXT_DISPLAY_2'>自定义字段2</option><option value='EXT_DISPLAY_3'>自定义字段3</option>";
			$("#source").append($option);
		}
		
		function switchCheck(id){
			if('selRange3' == id){
				$("#from_val").attr("disabled",true);
		        $("#to_val").attr("disabled",true);
			}else{
				$("#from_val").attr("disabled",false);
				$("#to_val").attr("disabled",false);
			}
		}
		
	</script>
</body>
</html>