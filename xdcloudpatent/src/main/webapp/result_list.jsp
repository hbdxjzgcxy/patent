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
 <style>
 	body{
 	 text-align:left;
 	 }
</style>
<link rel="stylesheet" href="<%=path %>/js/jquery-ui/themes/jquery-ui.css">
 <script src="<%=path %>/js/jquery-ui/jquery-ui.js"></script>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>
<script type="text/javascript">

$(function(){
	//初始化分页
	init_page_tag(${params.pageNo},${params.pageSize},${s_total});
	$(".lists").show();
	
	var list_statue=$.cookie("list_id");
	if(list_statue){
		 $.each(list_statue.split(" "),function(){
			$(".lists [id*=td_"+this+"]").show();
			$("#"+$(this)[0]).attr("checked","true");
		}) 
	} 
	$("._simg").hover(function(){
		var cancel=false;
		var ths = $(this).get();
		var width=$(this).width();
		var height=$(this).height();
		var url = $(this).attr("src");
		var d = setTimeout(function(){
			if(!cancel){
				relayAction(ths,width,height);
			}
		},618);
		$(this).unbind("mouseout").mouseout(function(){
			 cancel=true;
			 clearTimeout(d);
		});
	},function(){
		
	})
	/*
	 *监听键盘按键，如果回车按下提交表单
	 */
	 $(document).keydown(function(e){
			var e = e || event;
		    if(e.keyCode == 13) {
		    	setTimeout(function(){
			    	check_submit();
		        },0);
		        e.preventDefault ? e.preventDefault() : (e.returnValue = false);
		    }
		});
	
	$( "#kwd" ).autocomplete({
	    source: '<%=path%>/complete.html'
	})
	
	if("${params.ss}" == 1){
		$("#second").text("在上次搜索结果中命中");
	}
});

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

function more_search(){
	var s=$("#more_search").css("display");
	if(s=="none"){
		$("#more_search").show();
	}else{
		$("#more_search").hide();
	}
}

function check_submit() {
	restore();
	var kwd = $('#search_form')[0].kwd.value;
	if (!$.trim(kwd)) {
		alert("请输入检索条件!");
		return false;
	}
	$("#ss").val(0);
	before_submit();
	submit_search();
}

function relayAction(ths,width,height){
	var rate= width/height;
	if(width!=280){
		width=280;
		height = Math.round(280/rate);
	}
	var src=$(ths).attr("src");
	$("#dImgBox").attr("src",src).css({"width":width+"px","height":height+"px"});
	var top =parseInt($(ths).offset().top)-14;
	var left = parseInt($(ths).offset().left)-14;
	var st=$(document).scrollTop();
	var sl=$(document).scrollLeft();
	var rmt=$(window).height()+st-top;
	var rml=$(window).width()+sl-left;
	if(rmt<260){
		top = top-(260-rmt);
	}
	if(rml<280){
		left = left-(280-rml);
	}
	$("#dbox").css({"top":top,"left":left}).css({"border":"1px solid #DDDDDD"}).show(200);
	$("#dbox").unbind("hover").hover(function(){
	},function(){
		$("#dbox").hide(220);
	});
	$("#dImgBox").unbind("click").click(function(){
		$("#dbox").hide("fast");
		$(ths).click();
	 }
	);
	
}

</script>

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
/*  .img img:HOVER{
	 transform-origin:70px,-50px;
	 transform: scale(2);
}   */
</style>

<form id="search_form" method="get">
<div style="z-index: 500; position:absolute; display:none; border:1px solid #DDDDDD;  background-color:white; filter: progid:DXImageTransform.Microsoft.Alpha(opacity=100);opacity:1.0;" id="dbox">
	<img src="" id="dImgBox"  style="margin: 8px; "/></br/>
	<span id="img_summary" style="cursor:pointer;" ></span></br/>
	<span id="domain" style="color: gray"></span>
</div>
	<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav">
				<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a>
				&gt; <span>搜索结果</span>
			</div>
			<div class="search">
				<div class="wrap">
					<div class="search_input"  >
						<input type="text" name="kwd"  id="kwd" value="${fn:escapeXml(params.qstr)}" placeholder="Search..."> 
						
						<div class="btns">
								<ul style="list-style: none;">
								<li id="options" >
				                  <a  href="javascript:;" style="padding-left: 35px; background:url(<%=theme%>/images/search2.png) 10px 10px no-repeat;" onclick="return check_submit();" name="goSearch">搜索&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a><a style="padding-right: 6px;display: none;" href="javascript:;" onclick="more_search()">▾&nbsp;&nbsp;</a>
				                   
				                  <br/>
				              </li>
				            
				              <li id="more_search" style="display: none;">
				              	 <a style="padding-left: 35px;" href="javascript:;" onclick="second_search();before_submit();submit_search();change_text()">在结果中检索</a>
				              </li>
							</ul>
						
							<!-- <input type="button" onclick="before_submit();submit_search()" class="btn1"  value="搜索所有专利"/><input type="button" class="btn1" style="border-radius:0px; border: 0px; background-image: none; line-height: normal important;" onclick="more_search()"   value="▾" />
							<div id="more_search" style="background-color: #d23631;color: white; padding: 0px 0px 4px 4px; cursor: default; display:none;">在结果中检索</div> -->
						
						</div>
						 <div class="btns0" style="left: 102%;">
							<input type="button" class="btn1" onclick="second_search();before_submit();submit_search();" value="结果中搜索">
						</div> 
					</div>
					
				</div>
			</div>
			<!--Begin block5-->
			<div class="block5">
				<div class="L1" style="border:0px;">
					<h3>
						<span id="second" style="color: black;">命中结果</span>共 <span>${s_total}</span> 条 &nbsp;&nbsp;&nbsp;
						用时<span>${s_time}</span>秒
					</h3>
					<div class="clear"></div>
				</div>
				<div class="L2" style="display: none;">
					<ul>
						<li class="i-1">
							<h3>
								专利类型：<span> <input type="checkbox" name="cnlx" value="发明" onchange="submit()" <c:forEach items="${cnlxList}" var="cnlx"><c:if test="${cnlx eq '发明'}">checked</c:if></c:forEach> /> 发明专利
								</span><span> <input type="checkbox" name="cnlx" value="外观设计" onchange="submit()" <c:forEach items="${cnlxList}" var="cnlx"><c:if test="${cnlx eq '外观设计'}">checked</c:if></c:forEach> /> 外观专利
								</span><span> <input type="checkbox" name="cnlx" value="实用新型" onchange="submit()" <c:forEach items="${cnlxList}" var="cnlx"><c:if test="${cnlx eq '实用新型'}">checked</c:if></c:forEach> /> 实用新型
								</span>
							</h3>
						</li>
						<li class="i-2">
							<h3>
								法律状态：
								<span> <input type="checkbox" name="ls" value="授权" onchange="submit()" <c:forEach items="${lsList}" var="ls"><c:if test="${ls eq '授权'}">checked</c:if></c:forEach> />授权</span>
								<span> <input type="checkbox" name="ls" value="失效" onchange="submit()" <c:forEach items="${lsList}" var="ls"><c:if test="${ls eq '失效'}">checked</c:if></c:forEach> />失效</span>
								<span> <input type="checkbox" name="ls" value="公开" onchange="submit()" <c:forEach items="${lsList}" var="ls"><c:if test="${ls eq '公开'}">checked</c:if></c:forEach> />公开</span>
							</h3>
						</li>
						<li class="i-3">
							<h3>
								数据库：<span> <input type="checkbox" name="ac" value="-CN" onchange="submit()" <c:forEach items="${acList}" var="ac"><c:if test="${ac eq '-CN'}">checked</c:if></c:forEach> />外国</span>
									   <span> <input type="checkbox" name="ac" value="CN" onchange="submit()" <c:forEach items="${acList}" var="ac"><c:if test="${ac eq 'CN'}">checked</c:if></c:forEach> />中国</span>
							</h3>
						</li>
					</ul>
					<div class="clear"></div>
				</div>
				<div class="L3">
					<div class="left">
							 <input type="checkbox" class="checkall" id="checkall" onclick="checkallbox(this)"  /> 全选 
							 <input type="button" class="btns btn1" onclick="batchdown('${params.kwd}','${params.path}','${params.tName}')" title="下载" />
							 <input type="button" class="btns btn2" onclick="batchCollect()" title="收藏"> 
							 <input type="button" class="btns btn3" title="分析" onclick="analysis()"/>
							<select name="sorts" onchange="submit_search()">
								<option value="score_1" <c:if test="${params.curSort eq  'score_1'}">selected</c:if>>相关度排序</option>
								<option value="AD_0" <c:if test="${params.curSort eq  'AD_0'}">selected</c:if>>申请日↑</option>
								<option value="AD_1" <c:if test="${params.curSort eq  'AD_1'}">selected</c:if>>申请日↓</option>
								<option value="PD_0" <c:if test="${params.curSort eq  'PD_0'}">selected</c:if>>公开日↑</option>
								<option value="PD_1" <c:if test="${params.curSort eq  'PD_1'}">selected</c:if>>公开日↓</option>
							</select>
							<select name="style" onchange="submit_search()">
								<option value="list" <c:if test="${params.style eq 'list' }">selected</c:if>>列表式</option>
								<option value="col" <c:if test="${params.style eq 'col' }">selected</c:if>>分栏式</option>
							</select>
					</div>
					<div class="right">
						每页条数显示 <i class="Pnums"><a
							href="javascript:reset_page_size(10)" id="size10">10</a><a
							href="javascript:reset_page_size(20)" id="size20">20</a><a
							href="javascript:reset_page_size(30)" id="size30">30</a></i> <input
							type="button" class="btns btn4" onclick="previous_page(${params.pageNo})"
							value="上一页"> <input type="button" class="btns btn4 next_page"
							onclick="next_page(${params.pageNo})" value="下一页">
					</div>
					<div class="clear"></div>
				</div>
				<div class="lists" style="display: none;">
					<ul>
						<c:forEach items="${s_list}" var="doc" varStatus="idx">
							<li>
								<div class="title">
									<h3>
										<input type="checkbox" value="${doc.docId }">
										<span style="display:none">${doc.TI }</span>
										 <span><b>${(params.pageNo-1)*params.pageSize+idx.index+1 }</b></span>
										<span class="T1"> <a id="ti_${idx.index }" target="_blank" href="detail/detail.html?docNo=${doc.docId}&hlKwd='${params.kwd }'">${doc.TI}</a></span>
										<c:choose> 
											<c:when test="${doc.LS eq '无权' }"><span style="cursor:pointer" onclick="field_search('LS','${doc.LS}')" class="T2b">无权</span> <!-- <span class="T3">授权</span> --></c:when>
											<c:when test="${doc.LS eq '有权' }"><span style="cursor:pointer" onclick="field_search('LS','${doc.LS}')" class="T2c">有权</span> <!-- <span class="T3">公开</span> --></c:when>
											<c:when test="${doc.LS eq '审中' }"><span style="cursor:pointer" onclick="field_search('LS','${doc.LS}')" class="T2a">审中</span> <!-- <span class="T3">失效</span> --></c:when>
										</c:choose> 
											<font style="font-size:10px;"><c:if test="${!empty doc.LS && doc.LS ne 'null'}">${doc.LS}</c:if></font>
									</h3>
									<h4>
										<input type="button" class="btns btn1" onclick="download('${doc.docId}','${params.kwd}','1','${params.path}','${params.path }')" title="下载"> <input
											type="button" class="btns btn2" onclick="collect('${doc.docId}','${idx.index }')" title="收藏"> <input
											type="button" class="btns btn6" onclick="add2Compare('${doc.docId}','${idx.index }');" title="加入对比">
									</h4>
									<div class="clear"></div>
								</div>
								<div class="contents">
									<div class="img" id="td_f_zyft_${idx.index}" style="display:block;">
										<img class="_simg" src="resources/patent/img.html?pn=${doc.PN}" alt="" onerror="this.style.display='none'" />
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
				<div class="lists2">
					<div class="title" id="btn_toggle" onclick="list_toggle(this)">
						<h3>
							<img src="<%=theme %>/images/arrow_b.png" alt="" />列表内容设置
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
						</span><span style="display:none"> <input type="checkbox" id="f_ipc8"
								onclick="set_list(this)" /> 国际专利分类号(8)
						</span> <span> <input type="checkbox" id="f_pr"
								onclick="set_list(this)" /> 优先权
						</span><span> <input type="checkbox" id="f_addr"
								onclick="set_list(this)" /> 地址
						</span><span> <input type="checkbox" id="f_pc"
								onclick="set_list(this)" /> 国省代码
						</span> <span> <input type="checkbox" id="f_agc"
								onclick="set_list(this)" /> 代理机构
						</span><span> <input type="checkbox" id="f_agt"
								onclick="set_list(this)" /> 代理人
						</span></li>
					</ul>

				</div>
				<br/>
				<br/>
				<br/>
			</div>
			<!--End block5-->
		</div>
		</div>
	</div>
<div id="rel_div" style="width:400px; z-index:100px; position:absolute; right:20px; float:right; background:#eea; ">
	<%-- <input type="hidden" id="domain" name="domain" value="${params.domain}" />  --%>
	<input type="hidden" id="path" name="path" value="${params.path}" />
	<input type="hidden" name="tName" id="tName" value="${params.tName}"/>
	<input type="hidden" id="facet" name="facet" value="${params.facet}"/>
	<input type="hidden" name="searchType" value="${params.searchType }" /> 
	<input type="hidden" id="pageNo" name="pageNo" value="${params.pageNo}" /> 
	<input type="hidden" id="pageSize"name="pageSize" value="${params.pageSize}" />
	<input type="hidden" id="pkwd" name="pkwd" value="${params.qstr}" />
	<input type="hidden" id="ss" name="ss" value="${params.ss}" />
	<input type="hidden" id="facetTarget" name="facetTarget" value="0" />
</form>
 <div style="right: 0px; position: fixed; bottom: 17px; width: 350px;"
	class="comparepop">
	<div class="comparepop-content" style="display:none">
		<ul class="comparepop-list" style="height: auto;min-height:55px;">
<!-- 			<li data-key="24355"><input type="checkbox" checked="checked" value="CN200610136353.X_CN1953204B_20100714" style="vertical-align: middle"/><a style="cursor:pointer" target="_blank">用于电容性负载的沟道绝缘栅双极晶体管</a><i class="icon16 icon16-close"></i></li> -->
		</ul>
		<div id="divCompare" class="comparepop-select">
			<div class="area comparepop-select-btn" style="display: block;text-align:center;padding-bottom:4px;">
				<a target="_self"
					class="btn btn-small btn-orange fn-left j-compare-begin"
					href="javascript:void(0);">开始对比</a>
					<a  style="float: right;padding-right:10px;" class="j-compare-clear" href="javascript:void(0);">清空</a>
			</div>
		</div>
	</div>
	<a target="_self" href="javascript:void(0);"  class="comparepop-btn">专利对比<i
		class="icon10 icon10-left"></i></a>
</div>

<form id="compare_form" action="<%=path %>/patent/compare.html"
	method="get" target="_blank">
	<input type="hidden" name="docNo" />
	<input type="hidden" name="word"/>
</form>
