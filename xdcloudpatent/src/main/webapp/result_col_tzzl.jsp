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
<input type="hidden" id="pdf" value="${doc.PN }"/>

<div class="subtt"><a href="javascript:;">同族专利</a></div>
              <div class="contents" >
                <table class="table2" style="width: 50%">
                  <thead>
                  <c:if test="${!empty doc.PFNS && doc.PFNS ne 'null'}">
                    <tr>
                      <th >同专利族公开(公告)号</th>
                    </tr>
                  </c:if>
                  </thead>
                  <tbody>
                     	<c:forEach items="${doc.PFNS}" var="PFN">
                     		 <tr>
	                     		<td>
	                     			${PFN}
	                     		</td>
                     		</tr>
                     	</c:forEach>
                    </tr>
                  </tbody>
 </table>
              </div>


