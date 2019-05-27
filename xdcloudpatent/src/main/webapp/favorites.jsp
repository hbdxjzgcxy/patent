<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
<script src="<%=path %>/js/jquery/jquery-1.12.4.min.js"
	type="text/javascript"></script>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>

<style>
	.submit1 {
    padding: 39px 0 30px 0;
    text-align: center;
}
</style>
<div class="right_sider" id="favorites_div">
				<div class="search">
				<div class="wrap">
					<div class="search_input" style="width:73%" >
						<input type="text"  placeholder="收藏夹名称"> 
						<div class="btns">
							<input type="button" class="btn1  create" value="创建收藏夹">
						</div>
					</div>
				</div>
			</div>
			
			<!--Begin block5-->
			<div class="block5" style="padding:0px;">
				<div class="lists2">
					<div class="title" id="btn_toggle">
						<h3>
							收藏夹
						</h3>
					</div>
					<ul id="list_set_panel">
						<li  style="float:left;">
							<c:forEach items="${favorites}" var="favorite">
									<span  style="float:left;line-height:30px;padding-right:10px;"> <input style="vertical-align:text-top;margin-right:-1px;" name="ll" type="radio" value="${favorite }"/>${favorite }</span>
							</c:forEach>
						</li>
					</ul>

				</div>
			</div>
			<div style="text-align:left;padding-top:20%;">
				<div style="border: 1px #eee solid;line-height:30px;color:#888;padding-left:21px;">
						<h6>
							选中专利
						</h6>
				</div>
				<div style="text-align:left;padding-left:15px;">
					${ti}
				</div>
			</div>
			<!--End block5-->
			<div class="submit1">
					<input type="button" value="收藏" class="btn1 collect" />
				</div>
	</div>
	<script type="text/javascript">
	$(function(){
		$('.create').on('click',function(){
			var favorite = $('.search_input input[type=text]').val();
			if(favorite){
				if($('#list_set_panel input[value="'+favorite+'"]').length > 0){
						alert('不能创建重复的收藏夹');
						return;
				}
				
				if(favorite.length > 30){
					alert('收藏夹名称过长，长度不能超过20。');
					return;
				}
				//当前用户的收藏数量限制
				
				$("#list_set_panel input[type=radio]").removeAttr("checked");
				li = '<span  style="float:left;line-height:30px;padding-right:10px;"> <input style="vertical-align:text-top;margin-right:-1px;" name="ll" type="radio" value="'+favorite+'"/>'+favorite+'</span>';
				$("#list_set_panel li").append(li);
// 				$.ajax({
//				       url: '<%=path %>/favorite/favorites.html',
// 					       type: "post",
// 					       data: {'docNo':'${docNo}','folder':favorite},
// 					       success: function(resp) {
// 					    	   if(resp){
// 					    		   //动态显示
// 					    		   $("#list_set_panel input[type=radio]").removeAttr("checked");
// 					    		   $("#list_set_panel li").append('<span>').append('<input type="radio"  checked  value="'+favorite+'"/>').append(favorite).append('<sup>x</sup>').append('</span>');
// 					    	   }else{
// 					    		   alert('收藏夹创建失败');
// 					    	   }
// 					       }
// 					});
				
			}
		})
		
		$('.collect').on('click',function(){
				var favorite = $("#list_set_panel input[name=ll]:checked").val();
				if(favorite){
					$.ajax({
					       url: '<%=path %>/favorite/favorites.html',
						       type: "post",
						       data: {'docNo':'${docNo}','folder':favorite},
						       success: function(resp) {
						    	   if(resp){
						    		   alert('收藏成功');
						    		   window.parent.location.reload();
						    	   }else{
						    		   alert('收藏夹创建失败');
						    	   }
						       }
						});
				}else{
					alert('请选择收藏夹');
					return;
				}
		})
		
	})
	
	</script>