<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="/mytags.jsp"%>
<style>
.block2 .lists {
    padding: 1px 0;
}
.block2 .lists td input {
	    height: 28px;
	}
	.submit1{
		    padding: 12px 0 14px 0;
	}
		.block1 {
    padding: 0 12%;
    background: #fff;
}
.block1 .lists table {
    border-collapse: collapse;
    width: 100%;
}
table{white-space:nowrap;overflow:hidden;table-layout:fixed;} 
td{white-space:nowrap;overflow:hidden;text-overflow: ellipsis;}
</style>
<form id="search_form" method="get" >
<div class="right_sider">
<div class="Rwrap">
  <div class="container">
    <div class="nav"  style="margin-bottom: 15px;padding-left:10px;"><a href="#">用户信息管理</a> &gt; <span>检索历史</span> </div>
    <div class="search" >
      <div class="wrap">
        <div class="search_input">
          <input type="text" placeholder="Search..." name="kwd" id="kwd" value="${params.kwd}">
          <div class="btns">
            <input type="button" class="btn1" value="查询"  onclick="submit_search()"/>
          </div>
        </div>
      </div>
    </div>
    <!--Begin block5-->
    <div class="block1">
      <div class="lists">
        <table >
         <tbody>
            <tr>
              <th align="center" width="30%">检索表达式</th>
              <th align="center" width="*%">检索词</th>
               <th align="center">命中记录（条）</th>
               <th align="center">用时（秒）</th>
               <th align="center" width="25%">检索日期</th>
            </tr>
            <c:forEach items="${history}" var="log">
            	<tr align="center">
            		<td align="center"><a href="search.html?kwd=${fn:escapeXml(log.query)}">${log.query}</a></td>
            		<td align="center" >${log.word}</td>
            		<td>${log.hitcount}</td>
            		<td>${log.takeTime}</td>
            		<td align="center" ><fmt:formatDate value="${log.time}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
              	</tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="pages">
					<a id="pager_a" href="javascript:page(1)">首页</a><a id="pager_p"
						href="javascript:previous_page(${params.pageNo})">&lt;</a><a
						href="javascript:;" onclick="page(this)" id="pager1" class="num">1</a><a
						id="pager2" href="javascript:;" onclick="page(this)" class="num">2</a><a
						id="pager3" href="javascript:;" onclick="page(this)" class="num">3</a><a
						id="pager4" href="javascript:;" onclick="page(this)" class="num">4</a><a
						id="pager5" href="javascript:;" onclick="javascript:page(this)"
						class="num">5</a> <a id="pager_n"
						href="javascript:next_page(${params.pageNo})">&gt;</a><a id="pager_x"
						href="javascript:;">最后</a>
				</div>
      </div>
    </div>
    <!--End block5--> 
    </div>
  </div>
</div>

<!-- 分页form -->

	
	<input type="hidden" id="pageNo" name="pageNo" value="${params.pageNo}" /> 
	<input type="hidden" id="pageSize"name="pageSize" value="${params.pageSize}" />
	</form>

<script type="text/javascript" src="js/jquery/jquery.cookie.js"></script>
<script src="js/search.js"></script>
<script type="text/javascript">

function get_search_url(){
	return "<%=path%>/adminCenter/history.html?a=1";
}

function get_filter(){}

/*
 * 提交查询请求
 */
function submit_search(filter){
	var s_url=get_search_url();
		var $form_data = $("#search_form").serialize();
		if(filter){
			window.location=s_url+"&"+$form_data+filter;
		}else{
			window.location=s_url+"&"+$form_data;
		}
}

$(function(){
// 	//初始化分页
	init_page_tag('${params.pageNo}','${params.pageSize}','${s_total}');
// 	$(".pages").show();
	
// 	var list_statue=$.cookie("list_id");
// 	if(list_statue){
// 		 $.each(list_statue.split(" "),function(){
// 			$(".lists [id*=td_"+this+"]").show();
// 			$("#"+$(this)[0]).attr("checked","true");
// 		}) 
// 	}
	
// 	/*
// 	 *监听键盘按键，如果回车按下提交表单
// 	 */
// 	$(document).keydown(function(event){
// 		if(event.keyCode==13){
// 			before_submit();
// 		}
// 	});
})
</script>
