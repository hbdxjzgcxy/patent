<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>

<style type="text/css">
.more {
	border: none;
	padding: 0;
	color: #fff;
	padding-left: 5px;
	padding-right: 5px;
	display: inline-block;
	padding-top: 8px;
}

.btns li{
	height: 34px;
	background-color: #d23631;
	text-align: left;
}

.btns li a {
	border: none;
	padding: 0;
	color: #fff;
	display: inline-block;
	padding-top: 8px;
}
</style>
<script type="text/javascript" src="<%=path%>/js/search.js"></script>
<script type="text/javascript">
var _list_set_panel_hide=true;
function list_toggle(ths){
	//分页列表控制
	$("#list_set_panel").fadeToggle("fast", "swing",function(){
		if(_list_set_panel_hide){
			$(ths).html('<h3><img src="<%=theme%>/images/arrow_b.png"  alt=""/>列表内容设置</h3>');
		}else{
			$(ths).html('<h3><img src="<%=theme%>/images/arrow_a.png"  alt=""/>收起列表内容设置</h3>');
		}
	});
	_list_set_panel_hide=!_list_set_panel_hide;
}
$(function(){
	var list_statue=$.cookie("list_id");
	if(list_statue){
		 $.each(list_statue.split(" "),function(){
			$(".lists [id*=td_"+this+"]").show();
			$("#"+$(this)[0]).attr("checked","true");
		}) 
	} 
})
</script>
<form id="search_form" action="#"
	method="get" onsubmit="return check()">
	<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav">
				<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a>
				&gt; <span>专利收藏</span>
			</div>
			<!--Begin block5-->
			<div class="block5">
				<div class="lists">
					<ul>
						<c:forEach items="${s_list}" var="doc" varStatus="idx">
							<li>
								<div class="title">
									<h3>
										<span style="display:none">${doc.TI }</span>
										 <span><b>${(params.pageNo-1)*params.pageSize+idx.index+1 }</b></span>
										<span class="T1"> <a id="ti_${idx.index }" target="_blank" href="detail/detail.html?docNo=${doc.docId}&hlKwd='${params.kwd }'">${doc.TI}</a></span>
										<c:choose> 
											<c:when test="${doc.LS eq '授权' }"><span class="T2a">有权</span> <span class="T3">授权</span></c:when>
											<c:when test="${doc.LS eq '公开' }"><span class="T2c">公开</span> <span class="T3">公开</span></c:when>
											<c:when test="${doc.LS eq '失效' }"><span class="T2b">失效</span> <span class="T3">失效</span></c:when>
										</c:choose> 
									</h3>
									<div class="clear"></div>
								</div>
								<div class="contents">
									<div class="img" id="td_f_zyft_${idx.index}" style="display:none;">
										<img   src="http://books.daweisoft.com/abstphoto/FM/20110323/200910089989.7/200910089989.gif" alt="" />
									</div>
									<div class="txts">
										<span class="T1" id="td_f_pa_${doc.AN}">申请(专利权人): <a href="javascript:;">${doc.PA}</a></span> 
										<span class="T1" style="display: none;" id="td_f_au_${doc.AN}">发明设计人: <a href="#">${doc.AU}</a></span>
										<span class="T2"><b>申请号：</b><i class="i1">${doc.AN }</i> <b>申请日：</b><i class="i2" onclick="field_search('AD','${fn:substring(doc.AD,0,8)}')">${fn:substring(doc.AD,0,8)}</i><b>公开(公告)号：</b><i class="i1">${doc.PN }</i> <b>公开(公告)日：</b><i class="i2" onclick="field_search('PD','${fn:substring(doc.PD,0,8)}')">${fn:substring(doc.PD,0,8)}</i></span>
										<span class="T1" style="display: none;" id="td_f_ipc_${doc.AN}">国际专利号: <a href="javascript:;">${doc.IPC}</a></span>
										<span class="T2" id="td_f_addr_${doc.AN}" style="display: none;">地址: <a href="javascript:;">${doc.ADDR}</a></span>
										<span class="T2" style="display: none;" id="td_f_pc_${doc.AN}">国省代码: <a href="javascript:;">${doc.PC}</a></span> 
										<span class="T1" style="display: none;" id="td_f_pr_${doc.AN}">优先权: <a href="javascript:;">${doc.PR}</a></span>
										<span class="T2" id="td_f_agc_${doc.AN}" style="display: none;">专利代理机构: <a href="javascript:;">${doc.AGC}</a></span>
										<span class="T2" style="display: none;" id="td_f_agt_${doc.AN}">代理人:<a href="javascript:;">${doc.AGT}</a>
										</span> <span class="T3">
											<table>
												<tbody>
													<tr id="td_f_ab_${doc.AN}" style="display: none;">
														<td class="TD1">摘要：</td>
														<td class="TD2">${doc.AB}</td>
													</tr>
												</tbody>
											</table>
										</span>
									</div>
									<div class="clear"></div>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="lists2">
					<div class="title" id="btn_toggle" onclick="list_toggle(this)">
						<h3>
							<img src="<%=theme%>/images/arrow_b.png" alt="" />列表内容设置
						</h3>
					</div>
					<ul style="display: none;" id="list_set_panel">
						<li><span> <input type="checkbox" id="f_ti" checked
								disabled="disabled" /> 标题
						</span><span> <input type="checkbox" id="f_ls" checked
								disabled="disabled" /> 法律状态
						</span><span> <input type="checkbox" id="f_pa" checked
								disabled="disabled" /> 申请人
						</span> <span> <input type="checkbox" id="f_an" checked
								disabled="disabled" /> 申请号
						</span><span> <input type="checkbox" id="f_pn" checked
								disabled="disabled" /> 公开(公告)号
						</span><span> <input type="checkbox" id="f_pd" checked
								disabled="disabled" /> 公开(公告)日
						</span> <span> <input type="checkbox" id="f_zyft" checked
								onclick="set_list(this)" /> 摘要附图
						</span><span> <input type="checkbox" id="f_ab"
								onclick="set_list(this)" /> 摘要
						</span></li>
						<li><span> <input type="checkbox" id="f_au"
								onclick="set_list(this)" /> 发明设计人
						</span><span> <input type="checkbox" id="f_ipc"
								onclick="set_list(this)" /> 国际专利分类号
						</span><span> <input type="checkbox" id="f_ipc8"
								onclick="set_list(this)" /> 国际专利分类号(8)
						</span> <span> <input type="checkbox" id="f_pr"
								onclick="set_list(this)" /> 优先权
						</span><span> <input type="checkbox" id="f_addr"
								onclick="set_list(this)" /> 地址
						</span><span> <input type="checkbox" id="f_pc"
								onclick="set_list(this)" /> 国省代码
						</span> <span> <input type="checkbox" id="f_agc"
								onclick="set_list(this)" /> 专利机构代理
						</span><span> <input type="checkbox" id="f_agt"
								onclick="set_list(this)" /> 代理人
						</span></li>
					</ul>

				</div>
				</br>
				</br>
				</br>
			</div>
			<!--End block5-->
		</div>
		</div>
	</div>
<div id="rel_div" style="width:400px; z-index:100px; position:absolute; right:20px; float:right; background:#eea; ">
	<input type="hidden" id="CLASS_1" name="class1"/>
	<input type="hidden" id="CLASS_2" name="class2"/>
	<input type="hidden" id="CLASS_3" name="class3"/>
	<input type="hidden" id="CLASS_4" name="class4"/>
	<input type="hidden" id="facet" name="facet" value="${params.facet}"/>
	<input type="hidden" name="searchType" value="smart" /> 
	<input type="hidden" id="pageNo" name="pageNo" value="${params.pageNo}" /> 
	<input type="hidden" id="pageSize"name="pageSize" value="${params.pageSize}" />
	<input type="hidden" id="pkwd" name="pkwd" value="${params.qstr}" />
	<input type="hidden" id="ss" name="ss" value="0" />
</form>

<script type="text/javascript" src="js/search.js"></script>
