<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <jsp:include page="/mytags.jsp"></jsp:include>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<HTML>
<HEAD>
	<TITLE> 专题目录树</TITLE>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="../js/zTree_v3-master/css/zTreeStyle/zTreeStyle.css">
	<script type="text/javascript" src="../js/zTree_v3-master/js/jquery-1.4.4.min.js"></script>
	<script type="text/javascript" src="../js/zTree_v3-master/js/jquery.ztree.core.min.js"></script>
	<!--  <script type="text/javascript" src="../../../js/jquery.ztree.excheck.js"></script>
	  <script type="text/javascript" src="../../../js/jquery.ztree.exedit.js"></script>-->
	<SCRIPT type="text/javascript">
	<!--
	var setting = {
			view: {
				showLine: true,
				selectedMulti: false,
				dblClickExpand: false
			},
			data: {
				simpleData: {
					enable: true
				}
			},
			callback: {
				onRightClick: OnRightClick,
				onClick: this.onClick
			}
		};
		  
		var treeNodes;
		
		
		function OnRightClick(event, treeId, treeNode) {
			alert("禁止右键!");
			
		}
		function onClick(e, treeId, node) {
			alert("Do what you want to do!");
		}
		  
		$(function(){   
		    $.ajax({   
		        async : false,   
		        type: 'POST',   
		        dataType : "json",   
		        url: "${webRoot}/topic/topicTree.html",//请求的action路径   
		        error: function (error) {//请求失败处理函数   
		            //alert('请求失败');
		        },   
		        success:function(data){ //请求成功后处理函数。     
		        	treeNodes = eval(data);
		        }
		    });   
		  
			
		    $.fn.zTree.init($("#treeDemo"), setting, treeNodes);
		}); 
		
		//-->
	</SCRIPT>
 </HEAD>

<BODY>
<div class="content_wrap">
	<div class="zTreeDemoBackground left">
		<ul id="treeDemo" class="ztree"></ul>
	</div>
</div>
</BODY>
</HTML>