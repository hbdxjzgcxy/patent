<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>


<script src="js/jquery/jquery-1.12.4.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="js/jquery/jquery.cookie.js"></script>
<script type="text/javascript" src="js/search.js"></script>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<style>
body {
	text-align: left;
}

.modal-body {
	max-height: 400px;
	padding: 15px;
	overflow-y: auto;
}

.sortList, .sortList02 {
	list-style: none;
	margin: 0;
	padding-top: 37px;
	letter-spacing: -3px;
}

.modal-body .sortList li:nth-child(odd) {
	background-color: transparent;
}

.modal-body .sortList li {
	width: 160px;
	border: none;
	padding: 5px;
	margin: 0;
}

.sortList li, .sortList02 li {
	display: inline-block;
	border: 1px solid #ddd;
	padding: 5px 10px;
	width: 223px;
	margin: -1px 0 0 -1px;
	vertical-align: top;
	letter-spacing: normal;
}
</style>
</head>
<div style="float:left;">
	<a href="ipc/index.html">返回</a>
	<c:forEach items="${fn:split(codes,',')}" var="code">
	>
		<a style="display:none;" name="ipc_b">${code }</a>
		<a name="ipc_fl">${code }</a>
	</c:forEach>
</div>


<ul class="sortList">
<c:forEach items="${ipcCategory }" var="category">
	<li><label> <a name="ipc_b">${category.code }</a>
			<c:if test="${!empty category.children}">
			&nbsp;
			<a name="ipc_fl" title="子节点">&gt;&gt;</a></c:if>
	</label> <br /> <label>${category.desc }</label></li>
	</c:forEach>
</ul>
<form id="ipc_search_form">

</form>

	<script type="text/javascript">
	
		$(function(){
			$('a[name=ipc_b]').on('click',function(){
				window.location.href="search.html?&kwd=IPC="+$(this).text()+"*";
				//$('#search_form')[0].kwd.value += " "+$(this).text();
			})
			
			$('a[name=ipc_fl]').on('click',function(){
				var code = $(this).prev('a[name=ipc_b]').text();
				$.ajax({  
			        type: 'POST',   
			        dataType : "html",
			        data: {'code':code,'codes':'${codes}'},
			        url: 'ipc/category.html',  
			        success:function(data){
						$('#interClass').html(data);
			        }
			    }); 
			})
			
		})
		
	</script>