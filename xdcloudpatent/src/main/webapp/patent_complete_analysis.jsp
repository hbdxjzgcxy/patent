<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<link rel="stylesheet" type="text/css" href="theme/default/css/graph.css"/>
<script language="javascript" type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<script src="js/echarts/echarts-all.js"></script>
<script src="js/echarts/zh_CN.word.js"></script>
<script src="js/highcharts-4.2.6/js/highcharts.js"></script>
<script src="js/highcharts-4.2.6/js/highcharts-3d.js"></script>
<script src="js/highcharts-4.2.6/js/modules/exporting.js"></script>
<script src="<%=path %>/js/layer-v1.9.3/layer/layer.js"></script>
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
/**
 * 设置默认初始展示
 */
function set_default(){
	$('.ico_minus').click(function(){
		$(this).parent().parent().next().hide();
		$(this).hide();
		$(this).parent().find('.ico_plus').show();
	});
	$('.ico_plus').click(function(){
		$(this).parent().parent().next().show();
		$(this).hide();
		$(this).parent().find('.ico_minus').show();
	});
	
	$('.sub_t h4').hover(function(){
		$(this).css('cursor', 'pointer');
	});
	$('.sub_t h4').click(function(){
		$(this).parent().next().toggle();
		if(!$(this).parent().next().is(':visible')){
			$(this).prev().css('background-image', 'url(<%=theme%>/images/a02.png)');
		}else{
			$(this).prev().css('background-image', 'url(<%=theme%>/images/a01.png)');
		}
	});
	//设置导出文件显示监听
	$('.dropdown').hover(function(){
		$(this).children('ul').show();
		
	},function(){
		$(this).children('ul').hide();
	});
	
	$('.dropdown').find("ul li:eq(0)").click(function(){
		var w = $(this).find("a").attr("id");
		if(w.search("adpd")>=0){
			$("#svg_text2").val(getSvgByDiv("ad_y_graph_t"));
			export_word("pd_y_graph");
		}else if(w.search("paau")>=0){
			if(w.length<=6){
				$("#svg_text2").val(getSvgByDiv("pa_s_graph_t"));
				export_word("au_s_graph");
			}else if(w.search("trend")>=0){
				$("#svg_text2").val(getSvgByDiv("pa_s_trend_graph_t"));
				export_word("au_s_trend_graph");
			}else if(w.search("tech_field")>=0){
				$("#svg_text2").val(getSvgByDiv("pa_tech_field_graph_t"));
				export_word("au_tech_field_graph");
			}else if(w.search("cooperation")>=0){
				export_word("pa_cooperation_table");
			}else if(w.search("tech_const")>=0){
				export_word("pa_tech_const_table");
			}else if(w.search("region_patent")>=0){
				export_word("pa_region_patent_table");
			}
		}else{
			var divId = null; 
			if(w.search("tech_const_region")>=0){
				divId = w.replace("_w","_table");
				export_word(divId);
			}else if(w.search("tech_life_cycle")>=0){
				var svgg = $("#tech_life_cycle_graph_t").highcharts().getSVG();  
				$("#svg_text2").val(svgg);
				divId = w.replace("_w","_table");
				export_word(divId);
			}else{
				divId = w.replace("_w","_graph");
				export_word(divId);
			}
		}
		
	})
	$('.dropdown').find("ul li:eq(1)").click(function(){
		var e = $(this).find("a").attr("id");
		if(e.search("adpd")>=0){
			export_excel("pd_y_graph");
		}else if(e.search("paau")>=0){
			if(e.length<=6){
				export_excel("au_s_graph");
			}else if(e.search("trend")>=0){
				export_excel("au_s_trend_graph");
			}else if(e.search("tech_field")>=0){
				export_excel("au_tech_field_graph");
			}else if(e.search("cooperation")>=0){
				export_excel("pa_cooperation_table");
			}else if(e.search("tech_const")>=0){
				export_excel("pa_tech_const_table");
			}else if(e.search("region_patent")>=0){
				export_excel("pa_region_patent_table");
			}
		}else{
			var divId = null; 
			if(e.search("tech_const_region")>=0 ||e.search("tech_life_cycle")>=0){
				divId = e.replace("_e","_table");
			}else {
				divId = e.replace("_e","_graph");
			}
			export_excel(divId);
		}
		
	})
	
	//根据日期范围查询的单机事件
	$('.set_rang').click(function(){
		var startDate = $("#sqlx").next().children("#d421").val().substr(0,4);
		var endDate = $("#sqlx").next().children("#d422").val().substr(0,4);
		var start = $("#sqrgkr").next().children("#d430").val().substr(0,4);
		var end = $("#sqrgkr").next().children("#d440").val().substr(0,4);
		if(startDate !="" && endDate !=""){
			$("#cnlx_graph").find("input").remove();
			$("#cnlx_graph").append("<input type='hidden' name='field' value='CNLX'/>");
			$("#cnlx_graph").append("<input type='hidden' name='rs[0].field' value='AD_Y_S'/>");
			$("#cnlx_graph").append("<input type='hidden' name='rs[0].start' value='"+startDate+"'/>");
			$("#cnlx_graph").append("<input type='hidden' name='rs[0].end' value='"+endDate+"'/>");
			$("#cnlx_table").find("input").remove();
			$("#cnlx_table").append("<input type='hidden' name='field' value='CNLX'/>");
			$("#cnlx_table").append("<input type='hidden' name='rs[0].field' value='AD_Y_S'/>");
			$("#cnlx_table").append("<input type='hidden' name='rs[0].start' value='"+startDate+"'/>");
			$("#cnlx_table").append("<input type='hidden' name='rs[0].end' value='"+endDate+"'/>");
			$("#cnlx_table").append("<input type='hidden' name='ctype' value='0'/>");
			draw_pie("cnlx_graph",function(point){
				submit_search("CNLX="+point.name,form_data());
			});
			draw_table("cnlx_table",function(name){
				submit_search("CNLX="+name,form_data());
			});
		}
		if(start !="" && end !=""){
			var dot = end-start;
			//获取申请日图表及表格
		    $("#ad_y_graph").find("input[name='end']").remove();
		    $("#ad_y_table").find("input[name='end']").remove();
			$("#ad_y_graph").find("input[name=start]").val(start);
			$("#ad_y_graph").find("input[name=dot]").val(dot);
			$("#ad_y_graph").append("<input type='hidden' name='end' value='"+end+"'/>");
			$("#ad_y_table").find("input[name=start]").val(start);
			$("#ad_y_table").find("input[name=dot]").val(dot);
			$("#ad_y_table").append("<input type='hidden' name='end' value='"+end+"'/>");
			draw_ad_y_s();
			draw_table("ad_y_table",function(name){
			  	submit_search("AD_Y_S="+name,form_data());
			  });
			$("#pd_y_graph").find("input[name='end']").remove();
			$("#pd_y_table").find("input[name='end']").remove();
		    $("#pd_y_graph").find("input[name=start]").val(start);
			$("#pd_y_graph").find("input[name=dot]").val(dot);
			$("#pd_y_graph").append("<input type='hidden' name='end' value='"+end+"'/>");
			$("#pd_y_table").find("input[name=start]").val(start);
			$("#pd_y_table").find("input[name=dot]").val(dot);
			$("#pd_y_table").append("<input type='hidden' name='end' value='"+end+"'/>");
		    //获取公开日图表及表格
		 	draw_pd_y_s();
		    draw_table("pd_y_table",function(name){
		    	submit_search("PD_Y_S="+name,form_data());
		    });
		    draw_adpd("adpd_graph");
		    draw_table("adpd_table",function(name){
		    	submit_search("AD_Y_S="+name,form_data());
		    });
		}
	})
}
function getSvgByDiv(divId){
	return $("#"+divId).highcharts().getSVG();
}
function export_excel(divId){
	$("#export_form_excel").append($("#"+divId+" input").clone());
	$("#export_form_excel").find("input[name='ctype']").remove();
	var path = "${pageContext.request.contextPath}/export.html?"+"&"+form_data();
	$("#export_form_excel").attr("action",path)
	$("#export_form_excel").submit();
	$("#export_form_excel").find("input").remove();
}
function export_word(divId){
	$("#svg_text1").val();
	$("#svg_text2").val();
	$("#export_form_word").append($("#"+divId+" input").clone());
	$("#export_form_word").find("input[name='ctype']").remove();
	try{
		var svg = $("#"+divId+"_t").highcharts().getSVG();  
		$("#svg_text1").val(svg);
	}catch(e){
		$("#svg_text1").val("empty");
	}
	var path = "${pageContext.request.contextPath}/export.html?"+"&"+form_data();
	$("#export_form_word").attr("action",path)
	$("#export_form_word").submit();
	$("#export_form_word").find("input").remove();
}

/**
 * 根据条件动态获取表格(标准简单表格)
 */
function draw_table(divId,search_detail){
	$("#"+divId+"_t").find("tbody").empty();
	var i = 0;
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		$.each(data.data,function(key,value){
			$tr=$("<tr>");
			$td1=$("<td>").text(++i);
			$td2=$("<td>").text(value.name);
			$a = $("<a>").text(value.value).addClass("intro").attr("style","color:#3368b8").attr("href","javascript:;").click(function(){
				search_detail(value.name);
			});
			$td3=$("<td>").append($a);
			 if(divId.search("life")>=0 || divId.search("adpd")>=0){
				$td4 = $("<td>").text(value.count);
				$("#"+divId+"_t").find("tbody").append($tr).append($td1).append($td2).append($td3).append($td4);
			}else{
			 	$("#"+divId+"_t").find("tbody").append($tr).append($td1).append($td2).append($td3);
			}
		})
	})
}
/**
 * 创建多列的表格（折线图表格）
 */
function draw_broken_table(divId,text,data){
	var divId = divId.replace("graph","table")+"_t";
	var cates = data.categories;
	var names = data.name;
	var datas = data.data;
	$th=$("<th>").text(text);
	$("#"+divId).find("thead").append($th);
	$.each(cates,function(key,value){
		$th1 = $("<th>").text(value);
		$("#"+divId).find("thead").append($th1);
	})
	for(var i=0;i<names.length;i++){
		$tr = $("<tr>");
		$td1 = $("<td>").text(names[i]);
		$("#"+divId).find("tbody").append($tr).append($td1);
		$.each(datas[i],function(key,value){
			$td = $("<td>").text(value);
			$("#"+divId).find("tbody").append($td)
		})
	}
}
/**
 * 绘制多列表格（柱状图表格）
 */
function draw_bar_table(divId,text,data){
	var divId = divId.replace("graph","table")+"_t";
	var cates = data.categories;
	var names = data.name;
	var datas = data.data;
	$th=$("<th>").text(text);
	$("#"+divId).find("thead").append($th);
	$.each(names,function(key,value){
		$th1 = $("<th>").text(value);
		$("#"+divId).find("thead").append($th1);
	})
	for(var i=0;i<cates.length;i++){
		$tr = $("<tr>");
		$td1 = $("<td>").text(cates[i]);
		$("#"+divId).find("tbody").append($tr).append($td1);
		for(var j =0;j<datas.length;j++){
			$td = $("<td>").text(datas[j][i]);
			$("#"+divId).find("tbody").append($td);
		}
	}
}

/**
 * 绘制多条纯表格（包括合并单元格）
 */
function draw_multitr_table(divId,data){
	var cates = data.categories;
	var datas = data.data;
	var divId = divId+"_t";
	for(var i = 0;i<cates.length;i++){
		var k = datas[i].length;
		for(var j = 0;j<datas[i].length;j++){
		$tr = $("<tr>");
		$td = $("<td>").text(i+1);
		$td1 = $("<td>").text(cates[i]);
		if(j==0){
			$td = $td.attr("rowspan",k*2);
			$td1 = $td1.attr("rowspan",k*2)
		}else{
			$td = $td.hide();
			$td1 = $td1.hide();
		}
		$("#"+divId).find("tbody").append($tr).append($td).append($td1);
			$.each(datas[i][j],function(key,value){
				$td2 = $("<td>").text(key);
				$td3 = $("<td>").text(value);
				$("#"+divId).find("tbody").append($td2).append($td3);
			})
		}
		$("#"+divId).find("tbody").append("<tr>");
	}
}
function form_data(){
	return "kwd="+$("#kwd").val()+"&"+$("#analysis_form").serialize();
}
function completeLoading() {
    if (document.readyState == "complete") {
        var loadingMask = document.getElementById('loadingDiv');
        loadingMask.parentNode.removeChild(loadingMask);
    }
}

$(function(){
	
	//设置初始展示情况
	set_default();
	//监听切换事件
	change_select();
	//生成专利类型饼状图
    draw_pie("cnlx_graph",function(point){
		submit_search("CNLX="+point.name,form_data());
	});
	draw_table("cnlx_table",function(name){
		submit_search("CNLX="+name,form_data());
	});
	//获取申请日图表及表格
    draw_ad_y_s();
    draw_table("ad_y_table",function(name){
    	submit_search("AD_Y_S="+name,form_data());
    });
    //获取公开日图表及表格
  	draw_pd_y_s();
    draw_table("pd_y_table",function(name){
    	submit_search("PD_Y_S="+name,form_data());
    });
    //获取申请人图表及表格
	draw_bar_graph("pa_s_graph");
    draw_table("pa_s_table",function(name){
    	submit_search("PA_S="+name,form_data());
    });
    //获取发明人图表及表格
	draw_bar_graph("au_s_graph");
    draw_table("au_s_table",function(name){
    	submit_search("AU_S="+name,form_data());
    });
    //加载区域排名分析图表及表格
    draw_bar_graph("ac_graph");
    draw_table("ac_table",function(name){
    	submit_search("AC="+name,form_data());
    });
    draw_table("lc_table",function(name){
    	submit_search("LS="+name,form_data());
    });
    
    //加载技术生命周期图表和表格
    draw_tech_cycle("tech_life_cycle_graph");
    draw_table("tech_life_cycle_table",function(name){
    	submit_search("AD_Y_S="+name,form_data());
    })
    //加载申请人及发明人趋势图表
    draw_paau_trend("pa_s_trend_graph");
    draw_paau_trend("au_s_trend_graph");
    //加载申请人及发明人技术构成图表
    //draw_paau_tech_field("pa_tech_field_graph");
    //draw_paau_tech_field("au_tech_field_graph");
    //加载申请人/发明人合作分析表格
    //draw_cooperation("pa_cooperation_table");
    //draw_cooperation("au_cooperation_table");
    //加载技术趋势分析
    draw_tech_trend("tech_trend_graph");
    //加载技术构成表格(申请人/发明人)
    //draw_tech_const("pa_tech_const_table");
   // draw_tech_const("au_tech_const_table");
    //加载技术构成区域分析表格
    draw_tech_const_region("tech_const_region_table");
    //加载技术组合分析
    draw_tech_compose("tech_compose_graph")
    //区域趋势分析
    draw_range_trand("range_trand_graph");
    //区域技术构成分析
  //  draw_range_tech_const("range_tech_const_graph");
	//加载区域专利权人分析表格(申请人/发明人)    
	//draw_region_patent("pa_region_patent_table");
	//draw_region_patent("au_region_patent_table");
    //加载法律状态分析图表及表格
    draw_pie("lc_graph",function(point){
    	submit_search("LS="+point.name,form_data());
    })
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
/**
 * 绘制技术生命周期双线图表
 */
function draw_tech_cycle(divId){
	$.post(url,$("#tech_life_cycle_graph_t input").serialize(),function(data){
		broken_line(divId,"技术生命周期双线图",data.categories,["专利数量（件）","申请人数量（人）"],data.data);
	})
}
/**
 * 绘制申请人/发明人趋势分析图表
 */
function draw_paau_trend(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		broken_line(divId,title,data.categories,data.name,data.data);
		draw_broken_table(divId,"名称",data);
	})
}
/**
 * 绘制申请人/发明人技术构成分析图表
 */
function draw_paau_tech_field(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		bar_graph(divId,title,data.categories,data.name,data.data)
		draw_bar_table(divId,"名称",data);
	})
}
/**
 * 绘制申请人/发明人合作分析表格
 */
function draw_cooperation(divId){
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		draw_multitr_table(divId,data);
	})
}
/**
 * 绘制技术趋势分析图表
 */
function draw_tech_trend(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		broken_line(divId,title,data.categories,data.name,data.data)
		draw_broken_table(divId,"分类",data);
	})
}
/**
 * 绘制技术构成（申请人/发明人）表格
 */
function draw_tech_const(divId){
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		draw_multitr_table(divId,data);
	})
}
/**
 * 绘制技术构成区域分析表格
 */
function draw_tech_const_region(divId){
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		draw_multitr_table(divId,data);
	})
}
/**
 * 绘制技术组合分析柱状图
 */
function draw_tech_compose(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		bar_graph(divId,title,data.categories,data.name,data.data)
		draw_bar_table(divId,"分类",data);
	})
}
/**
 * 绘制区域趋势分析折线图
 */
function draw_range_trand(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		broken_line(divId,title,data.categories,data.name,data.data)
		draw_broken_table(divId,"区域",data);
	})
}
/**
 * 绘制区域技术构成分析柱状图
 */
function draw_range_tech_const(divId){
	var title = $("#"+divId).find("input[name=title]").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		bar_graph(divId,title,data.categories,data.name,data.data)
		draw_bar_table(divId,"区域",data);
	})
}
function draw_region_patent(divId){
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		draw_multitr_table(divId,data);
	})
}

function check_submit() {
	var kwd = $('#kwd').val();
	if (!$.trim(kwd)) {
		alert("请输入检索条件!");
		return false;
	}
	before_submit();
	submit_analysis();
}
/**
 * 显示与隐藏的切换
 */
function toggleA(obj, a, b){
	$(obj).parent().parent().children('li').removeAttr('class');
	$(obj).parent().attr('class', 'cur');	
	$(a).show();
	$(b).hide();
	$(a).parent().find('div[name="manual"]').hide();
}
function toggleB(obj, a, b){
	$(obj).parent().parent().children('li').removeAttr('class');
	$(obj).parent().attr('class', 'cur');	
	$(a).hide();
	draw_pd_y_s();
	$(b).show();
	try{
		$(b).highcharts().reflow();
	}catch(e){
		// skip error
	}
	
	$(a).parent().find('div[name="manual"]').hide();
}
function toggleC(obj, showT){
	$(obj).parent().parent().children('li').removeAttr('class');
	$(obj).parent().attr('class', 'cur');
	$(showT).prev().hide();
	$(showT).prev().prev().hide();
	if(showT.search("graph")>=0){
		draw_adpd("adpd_graph");
	}else{
		draw_adpd_table();
	}
	$(showT).show();
	$(showT).highcharts().reflow();
}
/**
 * 切换多个选项的公用方法
 */
 function change_select(){
	$('.filter li a').click(function(){
		var objs = $(this).parent().parent().find('a');
		var i = objs.index(this);
		var id = objs.eq(0).attr('id').replace('show', '');
		var id2 = objs.eq(1).attr('id').replace('show', '');
		try{
			var id3 = objs.eq(2).attr('id').replace('show', '');
		}catch(e){}
		if(i == 0){
			toggleA(this, '#'+id+'_graph_t', '#'+id2+'_graph_t');
			toggleA(this, '#'+id+'_table', '#'+id2+'_table');
		}else if(i == 1){
			toggleB(this, '#'+id+'_graph_t', '#'+id2+'_graph_t');
			toggleB(this, '#'+id+'_table', '#'+id2+'_table');
		}else if(i == 2){
			toggleC(this, '#'+id3+'_graph_t');
			toggleC(this, '#'+id3+'_table');
		}
	});
}
function get_analysis_url(){
		return "<%=path%>/complete_analysis.html?a=1";
	}
function submit_analysis(){
	var url = get_analysis_url()+"&"+form_data();
	window.location=url;
} 
function form_data(){
	return "kwd="+$("#kwd").val()+"&"+$("#analysis_form").serialize();
}
function toSearch(){
	var url = get_search_url()+"&"+$("#analysis_form").serialize();
	window.location=url;
}

/**
 * 导出分析报告
 */
var exportLayerIndex = 0;
function exportReport(){
	exportLayerIndex = layer.open({
		  type: 1,
		  title: ['导出设置','color: #FFF;background-color: #962925;'],
		  area:['400px','425px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: $("#export_popdiv")
		});
}
function emptyAll(){
	$(".vdoing_list").find("input[name='analysis_export_vdoing']").prop("checked",false);
}
function selectAll(){
	$(".vdoing_list").find("input[name='analysis_export_vdoing']").prop("checked",true);
}
function closeThs(){
	layer.close(exportLayerIndex);
}
function exportAll(){
	var arr = new Array();    
	var type = $("input:radio[name='analysis_export_format']:checked").val();
	$("input:checkbox[name='analysis_export_vdoing']:checked").each(function(i,n){
		arr[i]= n.value;
	})
	var path = "<%=path%>/export_all.html?fields="+arr+"&type="+type;
	if("w" == type){
		$("#svg_zllx").val(getSvgByDiv("cnlx_graph_t"));
		$("#svg_sqr").val(getSvgByDiv("ad_y_graph_t"));
		$("#svg_gkr").val(getSvgByDiv("pd_y_graph_t"));
		$("#svg_sxt").val(getSvgByDiv("tech_life_cycle_graph_t"));
		$("#svg_sqren").val(getSvgByDiv("pa_s_graph_t"));
		$("#svg_sqrqs").val(getSvgByDiv("pa_s_trend_graph_t"));
		$("#svg_sqrgc").val(getSvgByDiv("pa_tech_field_graph_t"));
		$("#svg_fmr").val(getSvgByDiv("au_s_graph_t"));
		$("#svg_fmrqs").val(getSvgByDiv("au_s_trend_graph_t"));
		$("#svg_fmrgc").val(getSvgByDiv("au_tech_field_graph_t"));
		$("#svg_jsqs").val(getSvgByDiv("tech_trend_graph_t"));
		$("#svg_jszh").val(getSvgByDiv("tech_compose_graph_t"));
		$("#svg_qypm").val(getSvgByDiv("ac_graph_t"));
		$("#svg_qyqs").val(getSvgByDiv("range_trand_graph_t"));
		$("#svg_qyjs").val(getSvgByDiv("range_tech_const_graph_t"));
		$("#svg_flzt").val(getSvgByDiv("lc_graph_t"));
	}
	if(arr.length < 1){
		return false;
	}
	$("#export_all_form").attr("action",path);
	$("#export_all_form").submit();
}
</script>
<form id="export_all_form" method="post" >
	<div style="display: none">
		<textarea id="svg_zllx"  name="svg_zllx"></textarea>
		<textarea id="svg_sqr" name="svg_sqr"></textarea>
		<textarea id="svg_gkr" name="svg_gkr"></textarea>
		<textarea id="svg_sxt" name="svg_sxt"></textarea>
		<textarea id="svg_sqren" name="svg_sqren"></textarea>
		<textarea id="svg_sqrqs" name="svg_sqrqs"></textarea>
		<textarea id="svg_sqrgc" name="svg_sqrgc"></textarea>
		<textarea id="svg_fmr" name="svg_fmr"></textarea>
		<textarea id="svg_fmrqs" name="svg_fmrqs"></textarea>
		<textarea id="svg_fmrgc" name="svg_fmrgc"></textarea>
		<textarea id="svg_jsqs" name="svg_jsqs"></textarea>
		<textarea id="svg_jszh" name="svg_jszh"></textarea>
		<textarea id="svg_qypm" name="svg_qypm"></textarea>
		<textarea id="svg_qyqs" name="svg_qyqs"></textarea>
		<textarea id="svg_qyjs" name="svg_qyjs"></textarea>
		<textarea id="svg_flzt" name="svg_flzt"></textarea> 
		</div>
	</form>
	<div id="export_popdiv" style="display: none;width: 400px" class="popup_window">
		<div class="popup_sub_title">
			<h3>选择统计维度</h3>
		</div>
		<div class="popup_content">
			<div class="tabs">
				<div id="analysis_export_all" class="tab_cont">
					<div class="analysis_export_vdoing">
						<div class="analysis_export_vdoing_actions">
							<button onclick="emptyAll()">清空</button>
							<button onclick="selectAll()">全选</button>
						</div>
						<div class="vdoing_list">
							<h4>专利类型分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="zllx">专利类型分析</label>
				  			</p>
				  			<h4>趋势分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sqr">申请日分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="gkr">公开日分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sxt">生命周期分析</label>
				  			</p>
				  			<h4>申请人分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sqren">申请人分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sqrqs">申请人趋势分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sqrgc">申请人技术分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="sqrhz">申请人合作分析</label>
				  			</p>
				  			<h4>发明人分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="fmr">发明人分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="fmrqs">发明人趋势分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="fmrgc">发明人技术分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="fmrhz">发明人合作分析</label>
				  			</p>
				  			<h4>技术趋势分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="jsqs">技术趋势分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="jsgcsqr">技术构成分布分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="jsgcfmr">技术发明人分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="jsgcqy">技术构成区域分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="jszh">技术组合分析</label>
				  			</p>
				  			<h4>区域分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="qypm">区域排名分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="qyqs">区域趋势分析</label>
				  				<!-- <label><input type="checkbox" name="analysis_export_vdoing" value="qyjs">区域技术构成分析</label>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="qysqr">区域专利权人分析</label> -->
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="qyfmr">区域发明人分析</label>
				  			</p>
				  			<h4>法律状态分析</h4>
				  			<p>
				  				<label><input type="checkbox" name="analysis_export_vdoing" value="flzt">法律状态分析</label>
				  			</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="popup_sub_title">
			<h3>选择统计方式</h3>
		</div>
		<div class="popup_content">
			<p class="field"><label><input type="radio" name="analysis_export_format" value="w">Word文档</label><small class="note">支持导出图表和表格</small></p>
			<p class="field"><label><input type="radio" name="analysis_export_format" value="e" checked='checked'>Excel文档</label><small class="note">仅支持导出表格</small></p>
		</div>
		<div class="popup_actions">
			<button id='exportallana' onclick="exportAll()">导出</button>
			<button class="popup_cancel" onclick="closeThs()">取消</button>
		</div>
	</div>
	
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
							<li><a href="<%= basePath%>/analysis.html">概览</a></li>
							<li class="cur"><a href="<%= basePath%>/complete_analysis.html" >完整分析报告</a></li>
							<li><a href="<%=basePath%>/custom_analysis.html" >自定义分析报告</a></li>
						</ul>
						<h4>
							分析专利 <span>${s_total}</span> 个 <input type="button" class="btn1" value="查看" onclick="toSearch()">
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
								<h3><i class="icon16 ico_baselabel"></i>专利申请类型分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline-block;"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display: none"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 >专利申请类型分析</h4>
									<a class="set_rang" id="sqlx"><i class="icon16 ico_gear"></i>设置范围</a>
									<div class="set_rang_inline">
										<input type="text" id="d421" class="datepicker" placeholder="开始年份"
										 onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>-
										<input type="text" class="datepicker" placeholder="结束年份" id="d422"
										onclick="WdatePicker({minDate:'#F{$dp.$D(\'d421\')}',maxDate:'%y-%M-%d'})"/>
									</div>
									<div class="dropdown">
										<span class="selected">导出文件</span>
										<i class="ico_triangle"></i>
										<ul class="subnav" style="display: none">
											<li><a href="javascript:void(0);" id="cnlx_w">导出Word</a></li>
											<li><a href="javascript:void(0);" id="cnlx_e">导出Excel</a></li>
										</ul>
									</div>
								</div>
								<div style="display: block" class="c">
									<div class="contents" id="cnlx_graph">
										<input type="hidden" name="field" value="CNLX">
									</div>
									<div class="contents" id="cnlx_graph_t" style="display:block"></div>
									<div class="contents" id="cnlx_table">
										<input type="hidden" name="field" value="CNLX"/>
										<input type="hidden" name="ctype" value="0" />
										<table class="analysis_table" id="cnlx_table_t" >
											<thead>
												<th>序号</th>
												<th>专利类型</th>
												<th>申请量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						<!--End lists-->
						<!--Begin lists-->
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>趋势分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline-block"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display:none"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">申请日/公开日分析</h4>
									<a class="set_rang" id="sqrgkr"><i class="icon16 ico_gear"></i>设置范围</a>
									<div class="set_rang_inline">
										<input type="text" class="datepicker" placeholder="开始年份" id="d430"
										onclick="WdatePicker({maxDate:'%y-%M-%d'})"/>-
										<input type="text" class="datepicker" placeholder="结束年份" id="d440"
										onclick="WdatePicker({minDate:'#F{$dp.$D(\'d430\')}',maxDate:'%y-%M-%d'})"/>
									</div>
									<div class="dropdown">
										<span class="selected">导出文件</span>
										<i class="ico_triangle"></i>
										<ul class="subnav" style="display: none">
											<li><a href="javascript:void(0);" id="adpd_w">导出Word</a></li>
											<li><a href="javascript:void(0);" id="adpd_e">导出Excel</a></li>
										</ul>
									</div>
								</div>
								<div class="c" style="display:block" >
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showad_y">申请年</a></li>
										<li ><a href="javascript:;" id="showpd_y">公开年</a></li>
										<li ><a href="javascript:;" id="showadpd">同时显示</a></li>
									</ul>
									<div class="contents" id="ad_y_graph" style="display: none" >
										<input type="hidden" id="xaxis" name="xaxis" value="AD_Y_S"/>
										<input type="hidden" id="start" name="start" value="-30"/>
										<input type="hidden" id="dot" name="dot" value="30"/>
										<input type="hidden" disabled="disabled" id="ad_y_graph_title" value="申请年" />
									</div>
									<div class="contents" id="pd_y_graph" style="display: none">
										<input type="hidden" id="xaxis" name="xaxis" value="PD_Y_S"/>
										<input type="hidden" id="start" name="start" value="-30"/>
										<input type="hidden" id="dot" name="dot" value="30"/>
										<input type="hidden" disabled="disabled" id="pd_y_graph_title" value="公开年" />
									</div>
									<div class="contents" id="ad_y_graph_t" style="display: block"></div>
									<div class="contents" id="pd_y_graph_t" style="display: none"></div>
									<div class="contents" name="manual" id="adpd_graph_t" style="display: none"></div>
									<div class="contents" id="ad_y_table" style="display: block" >
										<input type="hidden"  name="field" value="AD_Y_S"/>
										<input type="hidden"  name="start" value="-30"/>
										<input type="hidden"  name="dot" value="30"/>
										<input type="hidden"  name="ctype" value="0"/>
										<table class="analysis_table" id="ad_y_table_t">
											<thead>
												<th>序号</th>
												<th>申请年</th>
												<th>申请量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="pd_y_table" style="display: none">
										<input type="hidden"  name="field" value="PD_Y_S"/>
										<input type="hidden"  name="start" value="-30"/>
										<input type="hidden"  name="dot" value="30"/>
										<input type="hidden"  name="ctype" value="0"/>
										<table class="analysis_table" id="pd_y_table_t" >
											<thead>
												<th>序号</th>
												<th>公开年</th>
												<th>申请量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>	
									<div class="contents" name="manual" id="adpd_table" style="display: none">
										<!-- <input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="field" value="PD_Y_S">
										<input type="hidden" name="sort" value="index">
										<input type="hidden" name="rs[0].field" value="AD_Y_S">
										<input type="hidden" name="rs[0].start" value="1987"> -->
										<table class="analysis_table" id="adpd_table_t" >
											<thead>
												<th>序号</th>
												<th>年份</th>
												<th>申请数量</th>
												<th>公开数量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">技术生命周期分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="tech_life_cycle_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="tech_life_cycle_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="tech_life_cycle_graph_t">
										<input type="hidden" name="xaxis" value="AD_Y_S">
										<input type="hidden" name="field" value="PA_S">
										<input type="hidden" name="sort" value="index">
										<input type="hidden" name="rs[0].field" value="AD_Y_S">
										<input type="hidden" name="rs[0].start" value="1987">
									</div>
									<div class="contents" id="tech_life_cycle_table">
										<input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="field" value="PA_S">
										<input type="hidden" name="sort" value="index">
										<input type="hidden" name="rs[0].field" value="AD_Y_S">
										<input type="hidden" name="rs[0].start" value="1987">
										<table class="analysis_table" id="tech_life_cycle_table_t" >
											<thead>
											<th>序号</th>
											<th>申请日</th>
											<th>专利申请数量</th>
											<th>申请人数量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>申请人分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline_block"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display:none"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">申请人/发明人分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_s">申请人</a></li>
										<li ><a href="javascript:;" id="showau_s">发明人</a></li>
									</ul>
									<div class="contents" id="pa_s_graph" style="display:none">
										<input type="hidden" name="xaxis" value="PA_S">
										<input type="hidden" name="limit" value="20">
										<input type="hidden" disabled="disabled" id="pa_s_graph_title" value="申请人数量" />
									</div>
									
									<div class="contents" id="pa_s_graph_t" ></div>
									<div class="contents" id="au_s_graph" style="display: none">
										<input type="hidden" name="xaxis" value="AU_S">
										<input type="hidden" name="limit" value="20">
										<input type="hidden" disabled="disabled" id="au_s_graph_title" value="发明人数量" />
									</div>
									<div class="contents" id="au_s_graph_t" style="display:none"></div>
									<div class="contents" id="pa_s_table">
										<input type="hidden" name="field" value="PA_S" >
										<input type="hidden" name="limit" value="20" >
										<input type="hidden" name="ctype" value="0" >
										<table class="analysis_table" id="pa_s_table_t" >
											<thead>
												<th>序号</th>
												<th>申请人</th>
												<th>专利数量（件）</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="au_s_table" style="display: none">
										<input type="hidden" name="field" value="AU_S">
										<input type="hidden" name="limit" value="20">
										<input type="hidden" name="ctype" value="0">
										<table class="analysis_table" id="au_s_table_t" >
											<thead>
												<th>序号</th>
												<th>发明人</th>
												<th>专利数量（件）</th>
											</thead>						
											<tbody></tbody>
										</table>
									</div>
								</div>
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">申请人/发明人趋势分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_trend_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_trend_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_s_trend">申请人</a></li>
										<li ><a href="javascript:;" id="showau_s_trend">发明人</a></li>
									</ul>
									<div class="contents" id="pa_s_trend_graph" style="display: none">
										<input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="xaxis" value="PA_S">
										<input type="hidden" name="title" value="申请人趋势分析" disabled="disabled">
									</div>
									<div class="contents" id="pa_s_trend_graph_t" style="display:block"></div>
									<div class="contents" id="au_s_trend_graph" style="display: none">
										<input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="xaxis" value="AU_S">
										<input type="hidden" name="title" value="发明人趋势分析" disabled="disabled">
									</div>
									<div class="contents" id="au_s_trend_graph_t" style="display:none"></div>
									<div class="contents" id="pa_s_trend_table" >
										<table class="analysis_table" id="pa_s_trend_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="au_s_trend_table" style="display: none" >
										<table class="analysis_table" id="au_s_trend_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								<!-- <div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">申请人/发明人技术构成分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_tech_field_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_tech_field_e">导出Excel</a></li>
									</ul>
									</div>
								</div> -->
								<!-- <div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_tech_field">申请人</a></li>
										<li ><a href="javascript:;" id="showau_tech_field">发明人</a></li>
									</ul>
									<div class="contents" id="pa_tech_field_graph" style="display: none">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="xaxis" value="PA_S">
										<input type="hidden" name="title" value="申请人技术领域分析" disabled="disabled">
									</div>
									<div class="contents" id="pa_tech_field_graph_t"></div>
									<div class="contents" id="au_tech_field_graph" style="display: none">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="xaxis" value="AU_S">
										<input type="hidden" name="title" value="发明人技术领域分析" disabled="disabled">
									</div>
									<div class="contents" id="au_tech_field_graph_t" style="display: none"></div>
									<div class="contents" id="pa_tech_field_table">
										<table class="analysis_table" id="pa_tech_field_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="au_tech_field_table" style="display: none">
										<table class="analysis_table" id="au_tech_field_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div> -->
								
								<!-- <div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">申请人/发明人合作分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_cooperation_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_cooperation_e">导出Excel</a></li>
									</ul>
									</div>
								</div> -->
								<!-- <div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_cooperation">申请人</a></li>
										<li ><a href="javascript:;" id="showau_cooperation">发明人</a></li>
									</ul>
									<div class="contents" id="">
									</div>
									<div class="contents" id="pa_cooperation_table">
										<input type="hidden" name="field" value="PA_S">
										<input type="hidden" name="field" value="PA_S">
										<table class="analysis_table" id="pa_cooperation_table_t" >
											<thead>
												<th>序号</th>
												<th>申请人</th>
												<th>申请人</th>
												<th>合作次数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									
									<div class="contents" id="au_cooperation_table" style="display: none">
										<input type="hidden" name="field" value="AU_S">
										<input type="hidden" name="field" value="AU_S">
										<table class="analysis_table" id="au_cooperation_table_t" >
											<thead>
												<th>序号</th>
												<th>发明人</th>
												<th>发明人</th>
												<th>合作次数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div> -->
							</div>
						</div>
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>技术领域分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline-block"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display:none"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">技术趋势分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="tech_trend_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="tech_trend_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="tech_trend_graph" style="display: none">
										<input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="xaxis" value="IPC_B_S">
										<input type="hidden" id="title" value="技术趋势分析" disabled="disabled">
									</div>
									<div class="contents" id="tech_trend_graph_t"></div>
									<div class="contents" id="tech_trend_table">
										<table class="analysis_table" id="tech_trend_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								
								<!-- <div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">技术构成分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_tech_const_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_tech_const_e">导出Excel</a></li>
									</ul>
									</div>
								</div> -->
								<!-- <div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_tech_const">申请人</a></li>
										<li ><a href="javascript:;" id="showau_tech_const">发明人</a></li>
									</ul>
									<div class="contents" >
									</div>
									<div class="contents" id="pa_tech_const_table">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="field" value="PA_S">
										<table class="analysis_table" id="pa_tech_const_table_t" >
											<thead>
												<th>序号</th>
												<th>分类</th>
												<th>申请人</th>
												<th>计数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="au_tech_const_table" style="display: none">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="field" value="AU_S">
										<table class="analysis_table" id="au_tech_const_table_t" >
											<thead>
												<th>序号</th>
												<th>分类</th>
												<th>发明人</th>
												<th>计数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div> -->
								
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">技术构成区域分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="tech_const_region_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="tech_const_region_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="tech_const_region_table">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="field" value="AC">
										<table class="analysis_table" id="tech_const_region_table_t" >
											<thead>
												<th>序号</th>
												<th>分类</th>
												<th>省份/国家/地区</th>
												<th>计数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">技术组合分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="tech_compose_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="tech_compose_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="tech_compose_graph" style="display: none">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="xaxis" value="MIPC_B_S">
										<input type="hidden" id="title" value="技术构成分析" disabled="disabled">
									</div>
									<div class="contents" id="tech_compose_graph_t"></div>
									<div class="contents" id="tech_compose_table">
										<table class="analysis_table" id="tech_compose_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>区域分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline-block;"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display:none;"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">区域排名</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="ac_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="ac_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="ac_graph" style="display:none">
										<input type="hidden" name="xaxis" value="AC">
										<input type="hidden" name="limit" value="20">
										<input type="hidden" id="ac_graph_title" value="区域专利申请数量" disabled="disabled">
									</div>
									<div class="contents" id="ac_graph_t"></div>
									<div class="contents" id="ac_table">
										<input type="hidden" name="field" value="AC">
										<input type="hidden" name="limit" value="20">
										<input type="hidden" name="ctype" value="0">
										<table class="analysis_table" id="ac_table_t" >
											<thead>
												<th>序号</th>
												<th>省份/国家/地区</th>
												<th>申请量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">区域趋势分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="range_trand_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="range_trand_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="range_trand_graph" style="display: none">
										<input type="hidden" name="field" value="AD_Y_S">
										<input type="hidden" name="xaxis" value="AC">
										<input type="hidden" id="title" value="区域趋势分析" disabled="disabled">
									</div>
									<div class="contents" id="range_trand_graph_t"></div>
									<div class="contents" id="range_trand_table">
										<table class="analysis_table" id="range_trand_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								<!-- <div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">区域技术构成分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="range_tech_const_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="range_tech_const_e">导出Excel</a></li>
									</ul>
									</div>
								</div> -->
								<!-- <div class="c" style="display: block">
									<div class="contents" id="range_tech_const_graph" style="display: none">
										<input type="hidden" name="field" value="IPC_B_S">
										<input type="hidden" name="xaxis" value="AC">
										<input type="hidden" id="title" value="区域技术构成分析" disabled="disabled">
									</div>
									<div class="contents" id="range_tech_const_graph_t"></div>
									<div class="contents" id="range_tech_const_table">
										<table class="analysis_table" id="range_tech_const_table_t" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div> -->
								<!-- <div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">区域专利权人分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="paau_region_patent_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="paau_region_patent_e">导出Excel</a></li>
									</ul>
									</div>
								</div> -->
								<!-- <div class="c" style="display: block">
									<ul class="filter">
										<li class="cur" ><a href="javascript:;" id="showpa_region_patent">申请人</a></li>
										<li ><a href="javascript:;" id="showau_region_patent">发明人</a></li>
									</ul>
									<div class="contents" id="">
									</div>
									<div class="contents" id="pa_region_patent_table">
										<input type="hidden" name="field" value="AC" >
										<input type="hidden" name="field" value="PA_S" >
										<table class="analysis_table" id="pa_region_patent_table_t" >
											<thead>
												<th>序号</th>
												<th>区域</th>
												<th>申请人</th>
												<th>计数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
									<div class="contents" id="au_region_patent_table" style="display: none">
										<input type="hidden" name="field" value="AC" >
										<input type="hidden" name="field" value="AU_S" >
										<table class="analysis_table" id="au_region_patent_table_t" >
											<thead>
												<th>序号</th>
												<th>区域</th>
												<th>发明人</th>
												<th>计数</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div> -->
							</div>
						</div>
						
							
						<div class="lists">
							<div class="title">
								<h3><i class="icon16 ico_baselabel"></i>法律状态分析</h3>
								<div class="action" >
									<a href="javascript:;" class="icon16 ico_minus" style="display:inline-block;"></a>
									<a href="javascript:;" class="icon16 ico_plus" style="display:none;"></a>
								</div>
							</div>
							<div style="display:block ">
								<div class="sub_t" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">法律状态分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);" id="lc_w">导出Word</a></li>
										<li><a href="javascript:void(0);" id="lc_e">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: block">
									<div class="contents" id="lc_graph">
										<input type="hidden" name="field" value="LS">
									</div>
									<div class="contents" id="lc_graph_t" style="display:block"></div>
									<div class="contents" id="lc_table">
										<input type="hidden" name="field" value="LS">
										<input type="hidden" name="ctype" value="0">
										<table class="analysis_table" id="lc_table_t" >
											<thead>
												<th>序号</th>
												<th>法律状态</th>
												<th>申请量</th>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
								
								<div class="sub_t" style="display: none" >
									<i class="ico_triangle" ></i>	
									<h4 style="cursor: pointer;">存活期分析</h4>
									<div class="dropdown">
									<span class="selected">导出文件</span>
									<i class="ico_triangle"></i>
									<ul class="subnav" style="display: none">
										<li><a href="javascript:void(0);">导出Word</a></li>
										<li><a href="javascript:void(0);">导出Excel</a></li>
									</ul>
									</div>
								</div>
								<div class="c" style="display: none">
									<div class="contents" id="">
										<input type="hidden" name="field" value="LS">
										<input type="hidden" id="cnlx" value="专利类型" disabled="disabled">
									</div>
									<div class="contents">
										<table class="analysis_table" id="" >
											<thead>
											</thead>
											<tbody></tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
						
						<!--End lists-->
						<div class="clear"></div>
					</div>
					<!--End items_list-->
				</div>
				<!--End block4-->
			</div>
		</div>
	</div>
<form id="analysis_form">
	<input type="hidden" id="path" name="path" value="${analysiForm.path}" />
	<input type="hidden" name="tName" id="tName" value="${analysiForm.tName}" />
</form>
<form id="export_form_word" method="post" >
	<textarea id="svg_text1" name="svg1" ></textarea>
	<textarea id="svg_text2" name="svg2" ></textarea>
</form>
<form id="export_form_excel" method="post">
</form>