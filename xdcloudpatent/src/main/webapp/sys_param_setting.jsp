<%@page import="com.xdtech.patent.action.ParamContext"%>
<%@ page language="java" import="com.xdtech.patent.entity.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>

<c:set var="systemParam" value='<%=ParamContext.getParamInfo()%>' />
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">

<style>
.block2 {
	padding: 0 12%;
	background: #fff;
}

.lists td {
	font-size: 14px;
}
</style>
<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav" style="margin-bottom: 15px; padding-left: 10px;">
				<a href="#">系统管理</a> &gt; <span>参数设置</span>
			</div>
			<form id="paramSettingForm" method="post"
				action="<%=path%>/param/save.html" class="form-horizontal"	enctype="multipart/form-data">
				<!--Begin block2-->
				<div class="block2">
					<div class="lists">
						<div class="wrap">
							<input name="id" value="${systemParam.id }" type="hidden" /> 
							<table style="border-collapse: separate; border-spacing: 0px 10px;">
								<tbody>
									<tr>
										<td style="width: 100px;">系统LOGO</td>
										<td><input name="logo" value="${systemParam.logo}" type="text" /></td>
									</tr>
									<tr>
										<td>客服电话</td>
										<td><input name="phone" value="${systemParam.phone }"
											type="text" /></td>
									</tr>
								</tbody>
							</table>

						</div>
					</div>
				</div>
				<!--End block2-->
				<!--Begin submit1-->
				<div class="submit1 submitBG1">
					<input type="submit" value="修改 " class="btn1">
				</div>
			</form>
		</div>
	</div>
</div>
<%-- <script src="<%=path%>/js/jquery/jquery-1.12.4.min.js"></script> --%>
<script src="<%=path%>/js/uploadify/jquery.uploadify.js"></script>
<script src="<%=path%>/js/jquery/jquery.form.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$('#paramSettingForm').ajaxForm(function(data) {
			alert('系统参数修改成功');
			$('#paramSettingForm').clearForm();
			window.parent.location.reload();
		});
	});
</script>
