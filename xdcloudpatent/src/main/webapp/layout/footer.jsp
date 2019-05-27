<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/layer-v1.9.3/layer/layer.js"></script>
<style type="text/css">
</style>
<script>
	<c:if test="${sessionScope.echo=='true'}">
	layer.open({
		type : 2,
		title : [ "数据处理进度 " ],
		closeBtn : 1, //不显示关闭按钮
		shade : false,
		offset : 'rb', //右下角弹出
		maxmin : true,
		anim : 2,
		content : [
				'${pageContext.request.contextPath}/push_msg_console.jsp',
				'yes' ], //iframe的url，no代表不显示滚动条
	});
	</c:if>
</script>