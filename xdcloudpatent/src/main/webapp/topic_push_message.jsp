<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<div class="progress-panel">
	<ul id="ul-bar" onclick="switch_view()" style="display: none;">
		<li>数据文件:${sessionScope.progressInfo.fileCount}个,已处理${sessionScope.progressInfo.okCount}个</li>
		<li class="li-bar"><div style="width:${sessionScope.progressInfo.progress}">${sessionScope.progressInfo.progress}</div></li>
	</ul>
	<ul id="ul-progress" style="display: none;">
		<c:forEach items="${sessionScope.progressInfo.files}" var="f" varStatus="s">
			<c:if test="${s.index eq sessionScope.progressInfo.cursor}">
				<li class='li-cur' onclick="switch_view()">${f}[${sessionScope.progressInfo.currentOK}/${sessionScope.progressInfo.currentSize}]</li>
			</c:if>
			<c:if test="${s.index lt sessionScope.progressInfo.cursor}">
				<li class="li-ok" onclick="switch_view()">${f}[完成]</li>
			</c:if>
			<c:if test="${s.index gt sessionScope.progressInfo.cursor}">
				<li class="la-wait" onclick="switch_view()">${f}</li>
			</c:if>
		</c:forEach>
	</ul>
</div>
<script>
	$("#${id}").show(); //显示用户模式

	/**
	 *如果推送完成清除定时器
	 */
	var _progress_ = 'sessionScope.progressInfo.progress';
	if (_progress_ == "100%" && _timer_) {
		clearInterval(clearInterval);
	}
</script>