<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<style>
body{
	text-align:left;
}

.modal-body {
	max-height: 400px;
	padding: 15px;
	overflow-y: auto;
}

.sortList, .sortList02 {
	list-style: none;
	margin: 0;
	padding: 0;
	letter-spacing: -3px;
}

.modal-body .sortList li:nth-child(odd) {
	background-color: transparent;
}

.modal-body .sortList li {
	width: 160px;
	border: none;
	padding: 5px;
	margin: 0;
}
.sortList li, .sortList02 li {
    display: inline-block;
    border: 1px solid #ddd;
    padding: 5px 10px;
    width: 223px;
    margin: -1px 0 0 -1px;
    vertical-align: top;
    letter-spacing: normal;
}
</style>

	<div class="right_sider">
		<div class="Rwrap">
			<div class="container">
			<div class="nav"><img src="<%=theme%>/images/search3.png"  alt=""/> <a href="#">专利检索</a> &gt; <span>IPC检索</span></div>
				<div style="margin-top: 2%">
					<div class="search">
						<div class="wrap">
						<form id="search_form" action="ipc/search.html" method="get">
							<input type="hidden" name="searchType" value="ipc" />
							<!-- <input type="hidden" id="domain" name="domain" value="" />  -->
							<input type="hidden" id="path" name="path" value="" />
							<input type="hidden" id="ss" name="ss" value="2" />
							<input type="hidden" name="tName" id="tName" />
							<div class="search_input">
								<input type="text" name="kwd" placeholder="请输入IPC分类号或分类描述" />
								<div class="btns">
									<input type="submit" onclick="return check_submit()" class="btn1" value="搜索">
								</div>
							</div>
						</form>
						</div>
					</div>
					<div class="block1">
			      <div class="lists">
			      	<c:if test="${s_list!=null}" var="ipc">
						<c:forEach items="${s_list}" var ="ipc">
						<ul >
							<li style="line-height: 30px;"><span style="min-width: 100px;"> <a  href="search.html?kwd=IPC=${ipc.code}*">${ipc.code}</a>&nbsp; <a name="ipc_fl"></a></span>
							 <label>${ipc.desc}</label></li>
						</ul>
						</c:forEach>
					</c:if>
					<c:if test="${s_list==null}" var="ipc">
				        <div class="title">
				          <h3 style="cursor: pointer;"><img src="<%=theme%>/images/ipc_c.png" alt=""/> IPC分类 </h3>
				        </div>
				        <div style="display: block;border: 1px solid #ddd">

							<form name="form1" method="post">
								<div id="interClass" class="modal-body">
									<ul class="sortList">
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">A</a>&nbsp; <a name="ipc_fl" title="子节点">&gt;&gt;</a>
											</label> <br /> <label>人类生活必须</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">B</a> &nbsp;<a name="ipc_fl" title="子节点">&gt;&gt;</a>
											</label> <br /> <label>作业；运输</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">C</a> &nbsp;<a name="ipc_fl" title="子节点">&gt;&gt;</a>
											</label> <br /> <label>化学；冶金</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">D</a> &nbsp;<a name="ipc_fl" title="子节点"
													href="javascript:void(0)">&gt;&gt;</a>
											</label> <br /> <label>纺织；造纸</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">E</a> &nbsp;<a name="ipc_fl" title="子节点"
													href="javascript:void(0)" flag="2">&gt;&gt;</a>
											</label> <br /> <label>固定建筑物</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">F</a> &nbsp;<a name="ipc_fl" title="子节点"
													href="javascript:void(0)">&gt;&gt;</a>
											</label> <br /> <label>机械工程；照明；加热；武器；爆破</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">G</a>&nbsp; <a name="ipc_fl" title="子节点"
													href="javascript:void(0)">&gt;&gt;</a>
											</label> <br /> <label>物理</label></li>
	
											<li><label> <a name="ipc_b"
													href="javascript:void(0)">H</a>&nbsp; <a name="ipc_fl" title="子节点"
													href="javascript:void(0)">&gt;&gt;</a>
											</label> <br /> <label>电学</label></li>
										</ul>
								
								</div>
							</form>
						</div>
						</c:if>
				      </div>
				    </div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	
		$(function(){
			$('a[name=ipc_b]').on('click',function(){
				window.location.href="<%=basePath%>/search.html?&kwd=IPC="+$(this).text()+"*";
				//$('#search_form')[0].kwd.value += " "+$(this).text();
			})
			
			$('a[name=ipc_fl]').on('click',function(){
				var code = $(this).prev('a[name=ipc_b]').text();
				$.ajax({  
			        type: 'GET',   
			        dataType : "html",   
			        data: {'code':code},
			        url: 'ipc/category.html',  
			        success:function(data){
						$('#interClass').html(data);
			        }
			    }); 
			})
			
		})
function check_submit() {
	var kwd = $('#search_form')[0].kwd.value;
	if (!$.trim(kwd)) {
		alert("请输入检索条件!");
		return false;
	}
	$('#ss').val('0');
}
</script>
