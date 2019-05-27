<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<br><br><br><br>
<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			</div>
					<div class="block2">
						<input id="jsessionid" type="hidden" value="${pageContext.session.id}" /> 
						<div class="lists">
							<div class="title">
								<h3>PDF文件路径处理</h3>
							</div>
							<div class="wrap">
								<table style="border-collapse:separate; border-spacing:0px 10px;">
									<tbody>
										
							             <tr>
							                <td><br/>PDF文件路径</td>
							                 </tr>
							             <tr>
							                <td>
							                    <input title="请将PDF文件拷贝到此处，点击处理。" id="filePath" name="filePath" value="${filePath}" type="text" readonly="readonly" disabled="disabled"/>
							                </td>
							             </tr>
									</tbody>
								</table>
							</div>
						</div>
					<div class="submit1 submitBG1">
					<input type="button" value="处理" class="btn1" onclick="proc_pdf_path()">
				</div>
			</div>
	</div>
</div>
<script type="text/javascript">
	function proc_pdf_path(){
		$.get("pdf/handle.html",function(){
			alert("处理成功!")
		})
	}
</script>
