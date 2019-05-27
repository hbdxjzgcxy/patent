<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
 <link rel="stylesheet" href="<%=path %>/js/jquery-ui/themes/jquery-ui.css">
 <script src="<%=path %>/js/jquery-ui/jquery-ui.js"></script>
 <style>
 	body{
 	 text-align:left;
 	 }
 </style>
 
<form id="search_form" action="search.html" method="get">
<div class="right_sider">
<div class="Rwrap">
  <div class="container">
    <div class="nav"><img src="<%=theme%>/images/search3.png"  alt=""/> <a href="#">专利检索</a> &gt; <span>智能检索</span></div>
	<div>
	<div class="search">
      <div class="wrap">
        <div class="search_input">
          		<input type="text" style="width:89.6%" name="kwd" id="kwd" placeholder="Search..." />
		          <div class="btns">
		            <input type="submit" onclick="return check_submit()"  class="btn1" value="搜索">
		            <input type="hidden" name="searchType" value="smart" />
		            <input type="hidden" name="pageNo" value="1" />
		            <input type="hidden" name="pageSize" value="10" />
		           <!--  <input type="hidden" id="domain" name="domain" />  -->
					<input type="hidden" id="path" name="path"/>
					<input type="hidden" name="tName" id="tName" value="${params.tName}"/>
		          </div>
        </div>
      </div>
    </div>
    <div class="block1">
      <div class="lists">
        <div class="title">
          <h3 onclick="location.href='adminCenter/history.html'" style="cursor: pointer;" title="点击查看更多" ><img src="<%=theme%>/images/history.png" alt=""/> 检索历史 </h3>
        </div>
        <table>
          <tbody>
            <tr>
              <th>检索表达式</th>
              <th>检索词</th>
               <th>检索日期</th>
            </tr>
            <c:forEach items="${slogs }" var="log">
            	<tr>
            		<td width="60%"><a onclick="restore()" href="search.html?kwd=${fn:escapeXml(log.query)}">${log.query}</a></td>
            		<td width="*%">${log.word}</td>
            		<td width="20%"><fmt:formatDate value="${log.time}" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
              	</tr>
            </c:forEach>
          </tbody>
        </table>
        <div class="pages" style="display: none;"> <a href="">首页</a> <a href="">&lt;</a><a href="#" class="num cur">1</a><a href="#" class="num">2</a><a href="">&gt;</a><a href="">最后</a> </div>
      </div>
    </div>
	</div>
  </div>
  </div>
</div>
</form>

<script type="text/javascript">
$( function() {
$( "#kwd" ).autocomplete({
    source: '<%=path%>/complete.html',
    select: function(event,ui) {
    }
})
});

function check_submit() {
	restore();
	var kwd = $('#search_form')[0].kwd.value;
	if (!$.trim(kwd)) {
		alert("请输入检索条件!");
		return false;
	}
}
</script>