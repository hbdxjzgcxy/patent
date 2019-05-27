<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<link rel="stylesheet" type="text/css" href="theme/default/css/graph.css"/>
<script src="js/echarts/echarts-all.js"></script>
<script src="js/echarts/zh_CN.word.js"></script>
<script src="js/highcharts-4.2.6/js/highcharts.js"></script>
<script src="js/highcharts-4.2.6/js/highcharts-3d.js"></script>
<script src="js/highcharts-4.2.6/js/modules/exporting.js"></script>
<script src="js/analysis.js"></script>
<style>
body {
	text-align: left;
}
</style>
<script type="text/javascript">

var _PageHeight = document.documentElement.clientHeight,
_PageWidth = document.documentElement.clientWidth;
var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
_LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
var _LoadingHtml = '<div id="loadingDiv" style="position:absolute;left:0px;width:100%;height:' + _PageHeight + 'px;top:0; background:while;opacity:0.8;filter:alpha(opacity=80);z-index:10000;"><div style="position: absolute; cursor1: wait; left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; width: auto; height: 57px; line-height: 57px; padding-left: 50px; padding-right: 5px; background: #fff url(<%=theme%>/images/loading.gif) no-repeat scroll 5px 10px; border: 2px solid #95B8E7; color: #555555; font-family:\'Microsoft YaHei\';">页面加载中，请等待...</div></div>';
document.write(_LoadingHtml);
document.onreadystatechange = completeLoading;    
function completeLoading() {
if (document.readyState == "complete") {
    var loadingMask = document.getElementById('loadingDiv');
    loadingMask.parentNode.removeChild(loadingMask);
}
}

function setHighchart(target, json){
	try{
		if(json.chart.type == 'line' || json.chart.type == 'column'){//json.chart.type == 'column' || 
			json.plotOptions.series.events = {click: function(event){gosearchchart(json, event.point.category)}};
		}else if(json.plotOptions.pie != 'undefined'){
			json.plotOptions.pie.events = {click: function(event){gosearchchart(json, event.point.name)}};
		}
		$('#'+target).highcharts(json);
	}catch(e){
	}
}

$(function(){
	draw_ad_y_s();
	draw_pie("ls_graph",function(point){
		submit_search("LS="+point.name,form_data());
	});
	draw_pie("cnlx_graph",function(point){
		submit_search("CNLX="+point.name,form_data());
	});
	draw_horiz_bar("ipc_dl_graph",function(point){
		submit_search("MIPC_DL_S="+point.name,form_data());
	});
	draw_horiz_bar("pa_s_graph",function(point){
		submit_search("PA_S="+point.name,form_data());
	}
	);
	draw_world_map(); 
	//draw_chain_map();
	$(".highcharts-container").css({"top":0,"left":0}); //修正边距
	$(".contrast").hide();
	
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
});
</script>
	<div class="right_sider">
		<div class="Rwrap">
			<div class="container">
				<div class="nav">
					<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a> &gt; <span>专利分析</span>
				</div>
				<div class="search">
					<div class="wrap">
						<div class="search_input">
							<input type="text" placeholder="Search..." id="kwd" name="kwd" value="${analysiForm.kwd }" />
							<!-- <div class="btns0">
								<input type="button" class="btn1" value="保存搜索">
							</div> -->
							<div class="btns">
								<input type="button" class="btn2" value="专利分析" onclick="check_submit()">
							</div>
						</div>
					</div>
				</div>
				<!--Begin block4-->
				<div class="block4">
					<!--Begin items-->
					<div class="items">
						<ul id="tab_panel">
							<li class="cur"><a  href="<%= basePath%>/analysis.html">概览</a></li>
							<li><a  href="<%= basePath%>/complete_analysis.html">完整分析报告</a></li>
							<li><a  href="<%=basePath%>/custom_analysis.html">自定义分析报告</a></li>
						</ul>
						<h4>
							分析专利 <span>${s_total}</span> 个 <input type="button" class="btn1" value="查看" onclick="to_search()">
						</h4>
						<div class="clear"></div>
					</div>
					<!--End items-->
					<!--Begin items_list-->
					<div class="items_list">
						<div class="L1">
							<h3>
								<b>${analysiForm.kwd}</b>分析报告
							</h3>
							<!-- <h4>
								<input type="button" class="btn1" value="导出分析报告" onclick="exportReport()">
							</h4> -->
							<div class="clear"></div>
							
						</div>
						<!--Begin lists-->
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>日期分布</h3>
								<h4>
									<ul id="title_panel">
										<li class="i-1 cur" onclick="change_graph(this,'ad')"><a href="javascript:;">申请年</a></li>
										<li class="i-2"   onclick="change_graph(this,'pd')"><a href="javascript:;">公开年</a></li>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="ad_y_graph" style="display:none">
								<input type="hidden" id="xaxis" name="xaxis" value="AD_Y_S"/>
								<input type="hidden" id="start" name="start" value="-10"/>
								<input type="hidden" id="dot" name="dot" value="10"/>
								<input type="hidden" disabled="disabled" id="ad_y_graph_title" value="近十年专利申请趋势分析" />
							</div>
							<div class="contents" id="ad_y_graph_t" style="display:block"></div>
							<div class="contents" id="pd_y_graph" style="display:none">
								<input type="hidden" id="xaxis" name="xaxis" value="PD_Y_S"/>
								<input type="hidden" id="start" name="start" value="-10"/>
								<input type="hidden" id="dot" name="dot" value="10"/>
								<input type="hidden" disabled="disabled" id="pd_y_graph_title" value="近十年专利公开趋势分析" />
							</div>
							<div class="contents" id="pd_y_graph_t" style="display:none"></div>
						</div>
						<!--End lists-->
						<!--Begin lists-->
						
							<div class="lists" style="display:block">
								<div class="title" >
									<h3><i class="icon16 ico_baselabel"></i>申请人地域分布</h3>
									<h4>
										<ul id="title_panel1" >
										<li class="i-1 cur"  onclick="change_div(this,'world')">
											<a href="javascript:;">世界国家分布</a>
										</li>
										<li class="i-2" onclick="change_div(this,'china')" >
											<a href="javascript:;">中国省份分布</a>
											</li>
											
										</ul>
										<div class="clear"></div>
									</h4>
								</div>
								<div class="contents" >
									<table border="0">
										<tr>
											<td style="border:0; width: 55%;">
												<div id="chinaMap" style="padding-top: 10px;width:99%;height: 405px;position: relative;overflow: hidden;background-color: white; display:none"></div>
												<div id="worldMap" style="padding-top: 10px;width:99%;height: 405px;position: relative;overflow: hidden;background-color: white; display:block"></div></td>
											<td style="border:0;width: *%;vertical-align:top; ">
												<div class="contents" id="chinaItems" style="padding-top: 10px; position: relative;overflow: hidden;background-color: white; display:none" >
												</div>
												<div class="contents" id="worldItems" style="padding-top: 10px;position: relative;overflow: hidden;background-color: white;display:block" >
												</div>
											</td>
										</tr>
									</table>
								</div>
						</div>
						<div class="lists" style="display:none">
							<div class="title">
								<h3>申请人</h3>
								<h4>
									<ul>
										<%-- <a href="#">
											<li class="i-3"><img src="<%=theme%>/images/arrow_down2.png" alt="" /></li>
										</a> --%>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="pas_graph"></div>
						</div>
						<div class="lists" style="display:none">
							<div class="title">
								<h3>发明人</h3>
								<h4>
									<ul>
										<%-- <a href="#">
											<li class="i-3"><img src="<%=theme%>/images/arrow_down2.png" alt="" /></li>
										</a> --%>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="aus_graph"></div>
						</div>
						
						<div class="lists lists_left">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>中国专利类型</h3>
								<h4>
									<ul>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="cnlx_graph" style="display: none">
								<input type="hidden" name="field" value="CNLX">
								<input type="hidden" id="cnlx_graph_title" value="中国专利类型" disabled="disabled">
								<%-- <img src="<%=theme%>/images/temp/3.jpg" alt="" /> --%>
							</div>
							<div class="contents" id="cnlx_graph_t"></div>
						</div>
						<!--End lists-->
						<!--Begin lists-->
						<div class="lists lists_right">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>中国当前法律状态</h3>
								<h4>
									<ul>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="ls_graph" style="display: none">
								<input type="hidden" name="field" value="LS">
								<input type="hidden" id="ls_graph_title" value="中国当前法律状态" disabled="disabled">
							</div>
							<div class="contents" id="ls_graph_t"></div>
						</div>
						<div class="lists lists_left">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>国际专利IPC分类排行</h3>
								<h4>
									<ul>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="ipc_dl_graph" style="padding-top: 10px;height: 380px;position: relative;overflow: hidden;background-color: white;">
								<input type="hidden" name="field" value="MIPC_DL_S" >
								<input type="hidden" name="sort" value="count">
								<input type="hidden" name="ctype" value="0">
								<input type="hidden" name="limit" value="10">
							</div>
						</div>
						
						<div class="lists lists_right">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>申请（专利权）人排行</h3>
								<h4>
									<ul>
									</ul>
									<div class="clear"></div>
								</h4>
							</div>
							<div class="contents" id="pa_s_graph" style="padding-top: 10px;height: 380px;position: relative;overflow: hidden;background-color: white;" >
								<input type="hidden" name="field" value="PA_S" >
								<input type="hidden" name="sort" value="count">
								<input type="hidden" name="ctype" value="0">
								<input type="hidden" name="limit" value="10">
							</div>
						</div>
						<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="analysis_form">
		<input type="hidden" id="path" name="path" value="${analysiForm.path}" />
		<input type="hidden" name="tName" id="tName" value="${analysiForm.tName}" />
	</form>