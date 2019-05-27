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
	.block2 .lists td input {
	    height: 28px;
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

.grid{
	margin:20px;
}
.grid-item {
	width: 170px;
    height: 120px;
    overflow: hidden;
    float: left;
    margin-bottom: 10px;
}
.grid-item:hover{
	border-style:solid;
	border-width:1px;
	border-color:#efeff4;
    height: 118px;
 }

.grid-item .close {
	float: right;
	margin: -85px 4px;
	cursor:pointer;
	color:#666;
	display: none;
}
</style>
<div class="right_sider">
<div class="Rwrap">
<%-- <div class="container">
<div class="nav"  style="margin-bottom: 15px;padding-left:10px;"><a href="#">用户信息管理</a> &gt; <span>收藏夹</span> </div>
    <div class="search" style="margin-top:5%">
      <div class="wrap">
        <div class="search_input">
          <input type="text" placeholder="Search...">
          <div class="btns">
            <input type="button" class="btn1" value="查询">
          </div>
        </div>
      </div>
    </div>
    <div class="block1">
      <div class="lists">
        <div class="title">
          <h3><img src="<%=theme %>/images/user4.png" alt=""/> 收藏夹管理</h3>
        </div>
        <table>
          <tbody>
            <tr>
              <th align="center">收藏夹名称</th>
              <th align="center">操作</th>
            </tr>
            <c:forEach items="${favorites}" var="fav">
	            <tr>
	              <td align="center"><a href="<%=path %>/detail/detail.html?docNo=${fav.docNo }&hlKwd=''">${fav.folder }</a></td>
	              <td align="center"><input type="button" class="btns btn1" onclick="deleteUser('${fav.id}')" value="删除"></td>
	            </tr>
            </c:forEach>
          </tbody>
        </table>
      </div>
    </div>
    </div> --%>
    <div class="nav" style="margin-bottom: 15px;padding-left:10px;">
					<a href="#">用户信息管理</a> &gt; <span>收藏夹</span>
				</div>
    <div class="grid">
	<%-- 	    <c:forEach items="${favorites}" var="fav" varStatus="no">
				<div class="grid-item" id="${fav.id }">
				    <img src="<%=theme%>/images/folder.png" style="width:45px;height:45px;"/>
				    <div style="left:0;top:0;">
				        	${fn:substring(fav.folder,0,6)}
				    </div>
				    <img src="<%=theme%>/images/close.png" class="close" />
				</div>
		</c:forEach>
		 --%>
		<c:forEach items="${favorites}" var="entry">
			<div class="grid-item" id="${entry.key}">
				<c:set value="${entry.value }" var="folders"></c:set>
				<a href="favorite/fav_search.html?folder=${ entry.key}">
				    <img src="<%=theme%>/images/folder.png" style="width:45px;height:45px;"/></a>
				    <div style="left:0;top:0;">
				        	${fn:substring(entry.key,0,6)}
				        	<br/>
				        	共收藏${fn:length(folders)}条专利
				    </div>
<%-- 			    <img src="<%=theme%>/images/close.png" class="close" /> --%>
				<span class="close">×</span>
			</div> 
		</c:forEach>  
	</div>
  </div>
</div>

<script src="<%=path %>/js/jquery/jquery.form.js"></script>
<script src="js/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript">

$(function(){
	$(".grid-item").mouseenter(function() {
		$(this).children(".close").show();
	});

	$(".grid-item").mouseleave(function() {
		$(this).children(".close").hide();
	});
	
	$('.close').on('click',function(obj){
		id = $(this).parent('.grid-item').attr('id');
		layer.confirm('确定要删除收藏夹吗?',{icon: 3, title:'提示',skin: 'layui-layer-molv'},
				  function(index){
					$(obj).parent('div').hide();
					$.ajax({
					       url: '<%=path %>/favorite/delfavorites.html',
						       type: "post",
						       data: {'id':id},
						       success: function(resp) {
						    	   if(resp){
						    		   alert('收藏夹删除成功');
						    		   window.location.reload();
						    	   }else{
						    		   alert('收藏夹删除失败');
						    	   }
						       }
						});
					
					layer.close(index);
				}, function(index){
					layer.close(index);
				});
	})
});


</script>