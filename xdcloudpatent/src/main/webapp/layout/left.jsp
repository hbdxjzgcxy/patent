<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
<link rel="stylesheet"
	href="js/zTree_v3-master/css/zTreeStyle/zTreeStyle.css">
<style>
	.left_sider dt {
	    height: 26px;
	    line-height: 25px;
	    background: #fff;
	    border-bottom: 1px #ebebeb solid;
	    text-align: left;
	}
	
	.left_sider dt h4 {
	    float: right;
	    margin-right: 20px;
	    margin-top: 3px;
	}
	
	.topic_db{
		cursor:hand;
	}

	
	.contrast dd {
	    padding: 0 10px;
	    background: #fafafa;
	    text-align: left;
	    border-bottom: none;
	    font-size: 12px;
	}
</style>
<!---------------Begin left_sider--------------->
<div class="left_sider">
	<div class="container" style="max-height: 1024px; overflow: auto;">
		<div class="dis"></div>
			<!--Begin title-->
			<div class="topic_db title">
				<h3>
					<img src="<%=theme%>/images/database.png" alt="" /> 专题数据库
				</h3>
				<h4>
					<img src="<%=theme%>/images/arrow_down.png" alt="" />
				</h4>
				<div class="clear"></div>
			</div>
			<!--End title-->
			<!--Begin lists-->
			<div class="tree">
				<div class="zTreeDemoBackground left">
					<ul id="topicTree" class="ztree"></ul>
				</div>
			</div>
<c:if test="${s_list != null && s_list.size() > 0 }">
		<div class="contrast">
		<div class="title">
				<h3>
					<img src="<%=theme%>/images/filter.png" alt="" /> 筛选
				</h3>
				<div style="float:right;margin-right:10px;">
					<input type="button" class="btns btn6" id="filterBtn" value="确定">
					<input type="button" class="btns btn6" id="clearBtn" value="清除">
				</div>
				<div class="clear"></div>
			</div>
<!-- 			<div class="tt"> -->
<!-- 				<h5>对比阅读</h5> -->
<!-- 				<h6 style="float:none;"> -->
<!-- 					            <input type="button" class="btns btn6" value="专利对比"> -->
<!-- 					<input type="button" class="btns btn6" id="filterBtn" value="确定"> -->
<!-- 					<input type="button" class="btns btn6" id="clearBtn" value="清除"> -->
<!-- 				</h6> -->
<!-- 			</div> -->
			<div class="Clists">
				<dl>
				<%-- 
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">专利类型</a></h3>
						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> 
					</dt>
					<dd id="cnlx_dt" style='display:<c:if test="${cnlx_dt  ne 'cnlx_dt'}">none</c:if>'>
						<ul>
							<c:forEach items="${cnlx_facet_result}" var="fc">
								<c:if test="${fc.count>0}">
								<span> <input type="checkbox" name="CNLX" <c:if test="${cnlx_dt eq 'cnlx_dt' }">checked</c:if>
									value="${fc.value }"> ${fc.value }<b>(${fc.count})</b>
								</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">法律状态</a></h3>
 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4>
					</dt>
					<dd id="ls_dt" style='display:<c:if test="${ls_dt  ne 'ls_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${ls_facet_result}" var="fc">
								<c:if test="${fc.count>0}">
								<span> <input type="checkbox" name="LS" <c:if test="${ls_dt eq 'ls_dt' }">checked</c:if>
									value="${fc.value }"> ${fc.value }<b>(${fc.count})</b>
								</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd> --%>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">申请人</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="pa_dt" style='display:<c:if test="${pa_dt  ne 'pa_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${pa_facet_result}" var="pa">
								<c:if test="${pa.count>0}">
									<span> <input type="checkbox" name="PA_S" <c:if test="${pa_dt eq 'pa_dt' }">checked</c:if> value="${pa.value }"> ${pa.value }<b>(${pa.count})</b>
									</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">发明人</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="au_dt" style='display:<c:if test="${au_dt  ne 'au_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${au_facet_result}" var="au">
								<c:if test="${au.count>0}">
									<span> <input type="checkbox" name="AU_S" <c:if test="${au_dt eq 'au_dt' }">checked</c:if> value="${au.value }"> ${au.value }<b>(${au.count})</b>
									</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">主分类号(大类)</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="ipcdl_dt" style='display:<c:if test="${ipcdl_dt  ne 'ipcdl_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${ipc_dl_facet_result}" var="ipc">
								<c:if test="${ipc.count>0}">
									<span> <input type="checkbox" name="MIPC_DL_S" <c:if test="${ipcdl_dt eq 'ipcdl_dt' }">checked</c:if>
										value="${ipc.value }"> ${ipc.value }<b>(${ipc.count})</b>
									</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">主分类号(小类)</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="ipcxl_dt" style='display:<c:if test="${ipcdl_dt  ne 'ipcdl_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${ipc_xl_facet_result}" var="ipc">
								<c:if test="${ipc.count>0}">
									<span> <input type="checkbox" name="MIPC_XL_S" <c:if test="${ipcxl_dt eq 'ipcxl_dt' }">checked</c:if>
										value="${ipc.value }"> ${ipc.value }<b>(${ipc.count})</b>
									</span>
								</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">申请年份</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="adys_dt" style='display:<c:if test="${adys_dt  ne 'adys_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${ady_facet_result}" var="ady">
							<c:if test="${ady.count>0}">
								<span> <input type="checkbox" name="AD_Y_S" <c:if test="${adys_dt eq 'adys_dt' }">checked</c:if>
									value="${ady.value }"> ${ady.value }<b>(${ady.count})</b>
								</span>
							</c:if>
							</c:forEach>
						</ul>
					</dd>
					<dt class="dt_cur">
						<h3><a href="javascript:void(0)">公开年份</a></h3>
<%-- 						<h4><img src="<%=theme %>/images/arrow_down.png"  alt=""/></h4> --%>
					</dt>
					<dd id="pdys_dt" style='display:<c:if test="${pdys_dt  ne 'pdys_dt' }">none</c:if>'>
						<ul>
							<c:forEach items="${pdy_facet_result}" var="pdy">
							<c:if test="${pdy.count>0}">
								<span> <input type="checkbox" name="PD_Y_S" <c:if test="${pdys_dt eq 'pdys_dt' }">checked</c:if>
									value="${pdy.value }"> ${pdy.value }<b>(${pdy.count})</b>
								</span>
							</c:if>
							</c:forEach>
						</ul>
					</dd>
				</dl>
			</div>
		</div>
</c:if>
	</div>
</div>
<!---------------End left_sider--------------->


<script type="text/javascript"
	src="js/zTree_v3-master/js/jquery.ztree.core.min.js"></script>
	
<script type="text/javascript"
	src="js/zTree_v3-master/js/jquery.ztree.exedit.min.js"></script>
<SCRIPT type="text/javascript">
	<!--
	
	function restore(){
		//$("#domain").val('');
		$("#path").val('');
		$('#tName').val('');
		
		$.cookie('treeLog',null);
	}
	
	function rootNode(treeNode){
	   if(treeNode.getParentNode()!=null){    
	      var parentNode = treeNode.getParentNode();
	      if(parentNode.level ==1)return parentNode;
	      return rootNode(parentNode);    
	   }else{    
	      return treeNode;   
	   }  
	}
	
	var setting = {
			edit: {
				drag: {
					autoExpandTrigger: true,
					prev: dropPrev,
					inner: dropInner,
					next: dropNext
				},
				enable: true,
				showRemoveBtn: false,
				showRenameBtn: true
			},
			view: {
				showLine: true,
				selectedMulti: false,
				dblClickExpand: false,
				nameIsHTML: true,
				showIcon: true,
				showTitle :false
			},
			data: {
				simpleData: {
					enable: true,
					idKey: "id"
				}
			},
			callback: {
				beforeCollapse: zTreeBeforeCollapse,
				onCollapse: zTreeOnCollapse,
				onExpand: zTreeOnExpand,
				onClick: zTreeOnClick,
				beforeDrag: beforeDrag,
				onRename: onRename,
				beforeEditName:beforeEditName,
				onDrop:zTreeOnDrop
			}
		};
	
		var treeNodes;
		var treeLog='';//tree status log
		function zTreeOnClick(event, treeId, treeNode) {
			var root = rootNode(treeNode);
			$("#kwd").val("");
			treeLog = $.cookie('treeLog');
			if(treeLog){
			    treeLog = treeLog.replace(new RegExp("_true","g"),'');
			    if(treeLog.indexOf(treeNode.id) > 0){
				    var reg=new RegExp(","+treeNode.id,"g");
					treeLog = treeLog.replace(reg,','+treeNode.id+'_true');
			    }else{
			    	treeLog += ","+treeNode.id+"_true";
			    }
				$.cookie('treeLog',treeLog);
			}
			var path=zTreeObj.getSelectedNodes()[0].getPath();
			var class_val = "";
			for(var i=1;i<path.length;i++){
				if(i>1)
					class_val+="#"
				class_val+=path[i].rname;
			}
			//alert(class_val)
			if(treeNode.level > 1){
				if(treeNode.level == 1)
					root=treeNode;
			    var clazz = "CLASS_"+(treeNode.level-1);
			    rootName = root.rname;
				rootName = getNodeText(rootName);
				
				if("${cur}"==="analysis"){
					//$("#domain").val(clazz);
			    	$("#path").val(class_val);
			    	$('#tName').val(rootName);
			    	submit_analysis();
			    }else if("${cur}"==="complete_analysis" || "${cur}"==="custom_analysis"){
			    	$("#path").val(class_val);
			    	$('#tName').val(rootName);
			    	submit_analysis();
			    }else if("${cur}"==="ipc"){
			    }else{
			    	path_search(clazz,class_val,rootName);
			    }
			}else if(treeNode.level == 1){
				if("${cur}"==="analysis"){
				}else if("${cur}"==="complete_analysis" || "${cur}"==="custom_analysis" ){
			    	submit_analysis();
				}else if("${cur}"==="ipc"){
				}else{
					path_search('','',class_val);
				}
			}else{
			    $('#tName').val('all');
				if("${cur}"==="analysis"){
			    	submit_analysis();
				}else if("${cur}"==="complete_analysis" || "${cur}"==="custom_analysis"){
					submit_analysis();
				}else if("${cur}"==="ipc"){
				}else{
					path_search('','','all');
				}
			}
		};
		function zTreeBeforeCollapse(treeId, treeNode) {
		    return true;
		};
		function zTreeOnExpand(event, treeId, treeNode) {
			treeLog = $.cookie('treeLog');
		    treeLog += ","+treeNode.id;
		    $.cookie('treeLog',treeLog);
		};
		function zTreeOnCollapse(event, treeId, treeNode) {
			treeLog = $.cookie('treeLog');
			if(treeLog.indexOf(treeNode.id)){
				var reg=new RegExp(","+treeNode.id,"g");
				treeLog = treeLog.replace(reg,'');
		    	$.cookie('treeLog',treeLog);
			}
		};
		
		function dropPrev(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropInner(treeId, nodes, targetNode) {
			if (targetNode && targetNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					if (!targetNode && curDragNodes[i].dropRoot === false) {
						return false;
					} else if (curDragNodes[i].parentTId && curDragNodes[i].getParentNode() !== targetNode && curDragNodes[i].getParentNode().childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}
		function dropNext(treeId, nodes, targetNode) {
			var pNode = targetNode.getParentNode();
			if (pNode && pNode.dropInner === false) {
				return false;
			} else {
				for (var i=0,l=curDragNodes.length; i<l; i++) {
					var curPNode = curDragNodes[i].getParentNode();
					if (curPNode && curPNode !== targetNode.getParentNode() && curPNode.childOuter === false) {
						return false;
					}
				}
			}
			return true;
		}

		var log, className = "dark", curDragNodes, autoExpandNode;
		function beforeDrag(treeId, treeNodes) {
			className = (className === "dark" ? "":"dark");
			for (var i=0,l=treeNodes.length; i<l; i++) {
				if (treeNodes[i].drag === false) {
					curDragNodes = null;
					return false;
				} else if (treeNodes[i].parentTId && treeNodes[i].getParentNode().childDrag === false) {
					curDragNodes = null;
					return false;
				}
			}
			curDragNodes = treeNodes;
			return true;
		}
		
		function onRename(e, treeId, treeNode, isCancel) {
			var newName = treeNode.name;
			newName = getNodeText(newName);
			
			$.ajax({
		        type: 'POST',   
		        url: "<%=basePath%>/topic/updateName.html",
		        data:{"id":treeNode.id,"node":newName},
		        error: function (error) {
		        },   
		        success:function(data){
					if(data == "true"){
						
					}
		        }
		    });  
		}
		
		function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
			var tId = treeNodes[0].getParentNode().id;
			var nodes = zTreeObj.getNodesByParam('pId',tId);
			var array = zTreeObj.transformToArray(nodes);
			var params = '';
			for(var i = 0;i<array.length;i++){
				params+=array[i].id+'_'+array[i].getIndex()+',';
			}
		    $.ajax({
		        type: 'POST',   
		        url: "<%=basePath%>/topic/sort.html",
		        data:{'ids':params},
		        error: function (error) {
		        },   
		        success:function(data){
					if(data == "true"){
					}
		        }
		    });
		    
		};
		
		function beforeEditName(treeId, treeNode) {
			var oldName = treeNode.name; 
			oldName = getNodeText(oldName);
			treeNode.name=oldName;
		}
		
		
		
		 var param="";
		$(function(){
			$(".topic_db").click(function(){
				  var topicTree = $(this).next('.tree');
				  topicTree.toggle('slow');
				  if($(this).find('img:eq(1)').attr('src') == '<%=theme %>/images/arrow_down.png')
					  $(this).find('img:eq(1)').attr('src','<%=theme %>/images/arrow_up_1.png');
				  else 
					  $(this).find('img:eq(1)').attr('src','<%=theme %>/images/arrow_down.png');
			});
			
			$("dt").click(function(){
				if('${params.facet}' != 1){
					if('${s_total}' > 10000){
						alert('当前结果集超过10000条，无法使用此功能。');
						return;
					}else{
						$('#facet').val(1);
						var id=$(this).next("dd").attr("id");
						$('#facetTarget').val(id);
						submit_search();
					}
				}
				
				 // $(this).siblings('.dt_cur').removeClass('dt_cur');
				 // $(this).addClass('dt_cur');
				  var lists = $(this).next('dd');
				  lists.toggle();
				  
				  if($(this).find('img').attr('src') == '<%=theme %>/images/arrow_down.png')
					  $(this).find('img').attr('src','<%=theme %>/images/arrow_up.png');
				  else 
					  $(this).find('img').attr('src','<%=theme %>/images/arrow_down.png');
			});
			
			$.ajax({
		        async : false,   
		        type: 'POST',   
// 		        dataType : "json",   
		        url: "<%=basePath%>/topic/topicTree.html",//请求的action路径   
		        error: function (error) {//请求失败处理函数   
		            //alert('请求失败');
		        },   
		        success:function(data){ //请求成功后处理函数.
		        	if(data)
		        		treeNodes = eval(data);
		        
// 		        console.log(treeNodes);
		        }
		    });   
			
			//treeNodes[0].name+='(${tn_facet_result.get(0).count})';
			zTreeObj = $.fn.zTree.init($("#topicTree"), setting, treeNodes);
										
				var cookie = $.cookie("treeLog");
	            if(cookie){
	            	treeNode_c = cookie.split(',');
	                for(var i=0; i< treeNode_c.length; i++){
	                	node = treeNode_c[i];
	                	if(!node)continue;
	                	if(node.indexOf("_") != -1)
	                		id = node.substring(0,node.indexOf('_'));
	                	else id=node;
	                	highlight = node.substring(node.indexOf('_')+1,node.length);
	                	
	                    var nodeObj = zTreeObj.getNodeByParam('id', id);
	                    if(highlight && highlight == 'true'){
// 	                    	zTreeObj.setting.view.fontCss = {'background-color':'rgb(255, 230, 176)','border':'1px solid rgb(255, 185, 81)'};
							if(nodeObj){
		                    	nodeObj.name='<a style="padding-top: 0px;background-color: #FFE6B0;color: black;height: 15px;border: 1px #FFB951 solid;opacity: 0.8;">'+nodeObj.name+'</a>';
		                    	zTreeObj.updateNode(nodeObj);
							}
	                    }
	                    zTreeObj.expandNode(nodeObj, true);
	                } 
			}
	    	            $('#filterBtn').on('click',function(){
	    	            	param =get_filter();
	    	            	if(param){
	    	            		param = param.substring(0, param.length-1);
	    	            		//alert(param);
		    	            	filter_search(param);
	    	            	}
	    	        	});
	    	            
	    	            $('#clearBtn').on('click',function(){
	    	            	$('.contrast :input[type=checkbox]').each(function(){
	    	            		 $(this).attr('checked',false);         		
	    	            	});
	    	            	param = '';
	    	            });
	            
			
		});
		
		function get_filter(){
			param = "";//"&";
        	
          	$("input[name='LS']:checked").each(function(){
          		param +='LS='+$(this).val()+'&';
  			});
        	

			$("input[name='PA_S']:checked,input[name='PA_S']:checked").each(function(){
        		param +="PA_S="+$(this).val()+"&";
			});
			
			$("input[name='AU_S']:checked,input[name='AU_S']:checked").each(function(){
        		param +="AU_S="+$(this).val()+"&";
			});
			
			$("input[name='CNLX']:checked,input[name='CNLX']:checked").each(function(){
        		param +="CNLX="+$(this).val()+"&";
			});
			
			$("input[name='AD_Y_S']:checked,input[name='AD_Y_S']:checked").each(function(){
        		param +="AD_Y_S="+$(this).val()+"&";
			});
			$("input[name='PD_Y_S']:checked,input[name='PD_Y_S']:checked").each(function(){
        		param +="PD_Y_S="+$(this).val()+"&";
			});
			$("input[name='MIPC_DL_S']:checked,input[name='MIPC_DL_S']:checked").each(function(){
        		param +="MIPC_DL_S="+$(this).val()+"&";
			});
			$("input[name='MIPC_XL_S']:checked,input[name='MIPC_XL_S']:checked").each(function(){
        		param +="MIPC_XL_S="+$(this).val()+"&";
			});
			return param;
		}
		
		$("#${f_t}").show();
		
		
		function getNodeText(str){
		   try {
		    	if(str.substring(0,1)=="<")
		    		str=$(str).text();
			} catch (e) {
				
			} 
			return str;
		}
		
		//-->
	</SCRIPT>