<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
<style type="text/css">
</style>
<script type="text/javascript">

</script>
<!--Begin Llist-->
<div class="Llist">
<!-- 	法律状态 -->
<div class="subtt"><a href="javascript:;">法律状态</a></div>
	<div class="contents" style="text-align: left">
		<input type="hidden" id="pdf" value="${doc.PN }" />
		<table class="table2" style="width: 50%">
			<tbody>
				<c:if test="${!empty doc.LSE && doc.LSE ne 'null'}">
					<c:forEach items="${ fn:split(doc.LSE, ';') }" var="d">
						<tr>
							<td style="min-width: 150px;"> ${d }</td>
						</tr>
					</c:forEach>
				</c:if>
			</tbody>
		</table>
	</div>
</div>
<!--End Llist-->
