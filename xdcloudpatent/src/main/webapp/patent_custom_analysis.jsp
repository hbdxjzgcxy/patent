<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<link rel="stylesheet" type="text/css"
	href="theme/default/css/graph.css" />
<link rel="stylesheet" type="text/css"
	href="theme/default/css/other.css" />
<link rel="stylesheet" type="text/css"
	href="theme/default/css/other_rewrite.css" />
<link rel="stylesheet" type="text/css" href="theme/default/css/other_rewrite.css"/>
<style>
body {
	text-align: left;
}
</style>
<script src="js/echarts/echarts-all.js"></script>
<script src="js/echarts/zh_CN.word.js"></script>
<script src="js/highcharts-5.0.7/code/highcharts.src.js"></script>
<script src="js/highcharts-5.0.7/code/highcharts-3d.js"></script>
<script src="js/highcharts-4.2.6/js/modules/exporting.js"></script>
<script src="js/analysis.js"></script>
<script src="js/themes-min.js"></script>
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


	//x轴标题名称
	var x_title = "";
	//y轴标题名称
	var y_title = "专利数量（件）";
	 //标题名称
	var title_text = "申请年统计";
	//默认图表类型
	var graph_type = "column";
	//图例
	var legend = "right";
	var align = "middle";
	var table_data = null;
	var op;
	var ca;
	get_basic_options = function() {
		var options = {
		     colors: ['#058DC7', '#50B432', '#ED561B', '#DDDF00', '#24CBE5', '#64E572', '#FF9655', '#FFF263', '#6AF9C4','#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', 
		    	   '#f15c80', '#e4d354', '#2b908f', '#f45b5b', '#91e8e1','#2f7ed8', '#0d233a', '#8bbc21', '#910000', '#1aadce', 
		    	   '#492970', '#f28f43', '#77a1e5', '#c42525', '#a6c96a'], 
			chart : {
				type : graph_type,
				renderTo : 'show_graph'
			},
			title : {
				text : title_text
			},
			credits : {
				enabled : false
			},
			legend : {
				align : legend,
				verticalAlign : align,
				layout : 'vertical'
			},
			yAxis : {
				min : 0,
				title : {
					text : y_title
				}
			},
			tooltip: {
				 pointFormat: $("#amount option:selected").text()+': <b>{point.y} </b>'
	        },
			plotOptions : {
				column : {
					//depth : 25,
					dataLabels : {
						enabled : true
					}
				},
				line : {
					dataLabels : {
						enabled : true
					}
				},
				spline : {
					dataLabels : {
						enabled : true
					}
				},
				areaspline : {
					dataLabels : {
						enabled : true
					}
				},
				bar : {
					dataLabels : {
						enabled : true
					}
				},
				scatter: {
					dataLabels : {
						enabled : true
					}
				},
				area : {
					dataLabels : {
						enabled : true
					}
				},
				pie: {
	                allowPointSelect: true,
	                cursor: 'pointer',
	                depth : 35,
	                dataLabels: {
	                    enabled: true,
	                    format: '<b>{point.name}</b>: {point.percentage:.1f} %',
	                    style: {
	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
	                    }
	                }
	             
	            }
			}
		};
		return options;
	};

	var HCDefaults = $.extend(true, {}, Highcharts.getOptions(), {});
	function ResetOptions() {
		var defaultOptions = Highcharts.getOptions();
		for ( var prop in defaultOptions) {
			if (typeof defaultOptions[prop] !== 'function')
				delete defaultOptions[prop];
		}
		Highcharts.setOptions(HCDefaults);
	}
	var chart;
	function createChart() {
		var chartingOptions;
		if(graph_type == 'pie' && op.length>1){
			chartingOptions = {series: op};
		}
		
		chartingOptions = {xAxis : {title:{text:x_title},categories : ca},series: op};
		chartingOptions = $.extend({},get_basic_options(), chartingOptions);
		chart = new Highcharts.Chart(chartingOptions);
	}

	$(function() {
		draw_graph();
		change_graph_type();
		change_graph_config();
		change_graph_shape();
		//隐藏左侧的筛选
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
	function form_data(){
		return "kwd="+$("#kwd").val()+"&"+$("#analysis_form").serialize();
	}

	/*
	 *提交分析 
	 * @returns
	 */
	function submit_analysis(){
		var url = get_analysis_url()+"&"+form_data();
		window.location=url;
	}
	function get_analysis_url(){
		return "<%=path%>/custom_analysis.html?a=1";
	}
	/**
	 * 根据图表基本配置项刷新图表
	 */
	function refresh_config() {
		if($("[name='box1']:checkbox:checked").length>0){
		 	title_text = $("#title").val();
		}
		if($("[name='box2']:checkbox:checked").length>0){
			 x_title = $("#xtitle").val();
		}
		if($("[name='box3']:checkbox:checked").length>0){
			 y_title = $("#ytitle").val();
		}
		if($("[name='box4']:checkbox:checked").length>0){
			 legend = $("#legend").val();
			if ("center" == legend) {
				align = 'bottom';
			} else {
				align = 'middle';
			}
		}
		if($("[name='box5']:checkbox:checked").length>0){
			$("#show_table").show();
		}else{
			$("#show_table").hide();
		}
		ResetOptions();
		createChart();
		$("#"+graph_type+" ul li").removeClass("cur");
		$("#"+graph_type+" ul li:eq(0)").addClass("cur");
	}
	/***
	 * 切换图表配置项
	 */
	function change_graph_config() {
		$(".icon li a").click(function() {
			var objs = $(this).parent().parent().find("a");
			var index = objs.index(this);
			if (index == 0) {
				$("#config1").show();
				$("#config2").hide();
				$("#config3").hide();
			} else if (index == 1) {
				$("#config1").hide();
				$("#config2").show();
				$("#config3").hide();
			} else if (index == 2) {
				$("#config1").hide();
				$("#config2").hide();
				$("#config3").show();
			}
		});
	}
/**
 * 创建请求参数
 */
function create_str(){
	var field1 =null;
	var field2 = null;
	var val1 = $("#statistic").val();
	var val2 = $("#field1").val();
	var limit = $("#limit").val();
	var val3 = $("#dataSection").val();
	var val4 = $("#amount").val();
	var isCount = null;
	 str = null;
	if(val1 !=null){
		if(val1 == 'AD_PD_Y_S'){
			field1 = 'AD_Y_S';
			field2 = 'PD_Y_S';
			str = "field="+field1+"&field="+field2;
		}else if(val1 == 'AD_PD_M_S'){
			field1 = 'AD_M_S';
			field2 = 'PD_M_S';
			str = "field="+field1+"&field="+field2;
		}else{
			field1 = val1;
			str = "field="+field1;
		}
	}else {
		field1 = val2;
		str = "field="+field1;
	}
	if( val3 != 'none' && val1 != 'AD_PD_Y_S' && val1 != 'AD_PD_M_S' ){
		field2 = val3 ;
		str = str +"&field="+field2+"&pivot=1";
	}else if( val3 == 'none' && val4 !='none' && val1 != 'AD_PD_Y_S' && val1 != 'AD_PD_M_S'){
		field2 = val4;
		str = str +"&field="+field2+"&isCount=1"+"&pivot=1";
	}else if(val1 != 'AD_PD_Y_S' && val1 != 'AD_PD_M_S') {
		str = str +"&isCount=1";
	}
	//饼图
	if(graph_type == 'pie'){
		str = str + '&ctype=pie';
	}
	return str = str+"&limit="+limit;
}
/**
 * 绘制图表
 */
	function draw_graph() {
	 var str = create_str();
	 var url = "graph2.html?"+"&"+form_data();
	 $.post(url,str, function(data) {
		 var arr = new Array();
		 var size = data.data.length;
		 if(graph_type == 'pie' && size>1){
			var op1= {name:data.data[0].name,data:data.data[0].data, size: '70%', dataLabels: { formatter: function () { return this.y > 5 ? this.point.name : null }, color: 'white', distance: -20 }};
			var op2 = {name:data.data[1].name,data:data.data[1].data,size:'90%', innerSize: '60%'}
			arr.push(op1);
			arr.push(op2);
			op = arr;
		 }else{
			 //通用series
			 op = data.data;
			 //通用categroies
			 ca = data.categories;
		 }
			createChart();
		}) 
		draw_table()
		$("#title").val(title_text);
	 	$("#xtitle").val(x_title);
		 $("#ytitle").val(y_title);
		//将cur放置默认数据图上
		$("#"+graph_type+" ul li").removeClass("cur");
		$("#"+graph_type+" ul li:eq(0)").addClass("cur");
	}
/**
 * 绘制表格
 */
	function draw_table(){
		$("#table_t").find("thead").empty();
		$("#table_t").find("tbody").empty();
		var url = "graph2.html?"+"&"+form_data();
		var str = create_str();
		if(str.search("&ctype=pie")>=0){
			str = str.replace("&ctype=pie","");
		}
		$.post(url,str,function(data){
			table_data = data;
			var name = data.name[0];
			var categories = data.categories;
			var data = data.data;
			$th1 = $("<th>").text(name);
			$("#table_t").find("thead").append($th1);
			$.each(data,function(key,value){
				$th =$("<th>").text(value.name); 
				$("#table_t").find("thead").append($th);
			});
			$.each(categories,function(key,value){
				$tr = $("<tr>");
				$td1=$("<td>").text(value);
				$("#table_t").find("tbody").append($tr).append($td1);
				for(var j =0;j<data.length;j++){
					$td = $("<td>").text(data[j].data[key]);
					$("#table_t").find("tbody").append($td);
				}
			})
		})
	}
	//导出word
	function export_word(){
		$("#svg_text").val("");
		$("#data").val("");
		var svg = $("#show_graph").highcharts().getSVG();
		$("#svg_text").val(svg);
		$("#data").val(JSON.stringify(table_data));
		$("#export_word").submit();
	}
	/**
	 * 切换图形形状
	 */
	function change_graph_shape() {
		$(".L2 ul[name !='ul_word'] li a").click(function() {
			$(".L2 ul li").removeClass();
			$(this).parent().addClass("cur");
			var objs = $(this).parent().parent().find("a");
			var i = objs.index(this);
			var chart = $("#show_graph").highcharts();
			if(graph_type == 'pie'){
				if(i == 0){
					chart.update({chart : {
						options3d : {
							enabled : false
						}
					}
				});
					chart.series[0].update({
						innerSize : '0'
					});
				}else if(i == 1){
					chart.update({
						chart : {
							type : graph_type,
							options3d : {
								enabled : true,
								 alpha: 45,
					                beta: 0
							}
						}});
					chart.series[0].update({
						innerSize : '0'
					});
				}else if(i == 2){
					chart.update({chart : {
						options3d : {
							enabled : false
						}
					}});
					chart.series[0].update({
							innerSize : '40%'
						});
				}else if(i == 3){
					chart.update({
						chart : {
							type : graph_type,
							options3d : {
								enabled : true,
								 alpha: 45,
					                beta: 0
							}
						}});
					chart.series[0].update({
						innerSize : '40%'
					});
				}
			}else if(graph_type == 'line' || graph_type == 'spline'){
				if(i == 0){
					graph_type ="line";
					createChart();
				}else{
					graph_type ="spline";
					createChart();
				}
			}else if(graph_type == 'area' || graph_type == 'areaspline'){
				if(i == 0){
					graph_type ="area";
					createChart();
				}else{
					graph_type ="areaspline";
					createChart();
				}
			}else
			if (i == 0) {
				chart.update({
					plotOptions : {
						column : {
							stacking : ''
						},
						bar : {
							stacking : ''
						}
					},
					chart : {
						options3d : {
							enabled : false
						}
					}
				});
			} else if (i == 1) {
				chart.update({
					plotOptions : {
						column : {
							stacking : 'normal'
						},
						bar : {
							stacking : 'normal'
						}
					},
					chart : {
						options3d : {
							enabled : false
						}
					}
				});
			} else if (i == 2) {
				chart.update({
					chart : {
						type : graph_type,
						options3d : {
							enabled : true,
							alpha : 15,
							beta : 15,
							depth : 50,
							viewDistance : 25
						}
					},
					plotOptions : {
						column : {
							stacking : ''
						},
						bar : {
							stacking : ''
						}
					}
				});
				//三维
			} else if (i == 3) {
				//三维堆叠
				chart.update({
					chart : {
						type : graph_type,
						options3d : {
							enabled : true,
							alpha : 15,
							beta : 15,
							depth : 50,
							viewDistance : 25
						}
					},
					plotOptions : {
						column : {
							stacking : 'normal'
						},
						bar : {
							stacking : 'normal'
						}
					}
				});
			}
		});
	}
	/**
	 * 切换图形类型
	 */
	function change_graph_type() {
		$(".L1 ul[name!='ul_word'] li a").click(function() {
			$(".L1 ul li").removeClass();
			$(this).parent().addClass("cur");
			var objs = $(this).parent().parent().find("a");
			var i = objs.index(this);
			var chart = $("#show_graph").highcharts();
			if (i == 0) {//柱状图
				$("#column").show();
				$("#bar").hide();
				$("#area").hide();
				$("#line").hide();
				$("#pie").hide();
				$("#scatter").hide();
				graph_type = "column";
				draw_graph();
			} else if (i == 1) {
				$("#line").show();
				$("#bar").hide();
				$("#column").hide();
				$("#area").hide();
				$("#pie").hide();
				$("#scatter").hide();
				graph_type = "line";
				draw_graph();
			} else if (i == 2) {
				$("#pie").show();
				$("#bar").hide();
				$("#column").hide();
				$("#area").hide();
				$("#line").hide();
				$("#scatter").hide();
				graph_type = "pie";
				draw_graph();
			} else if (i == 3) {
				$("#bar").show();
				$("#line").hide();
				$("#column").hide();
				$("#area").hide();
				$("#pie").hide();
				$("#scatter").hide();
				graph_type = "bar";
				draw_graph();
			} else if (i == 4) {
				$("#area").show();
				$("#line").hide();
				$("#column").hide();
				$("#bar").hide();
				$("#pie").hide();
				$("#scatter").hide();
				graph_type = "area";
				draw_graph();
			} else if (i == 5) {
				$("#scatter").show();
				$("#line").hide();
				$("#column").hide();
				$("#bar").hide();
				$("#pie").hide();
				$("#area").hide();
				graph_type = "scatter";
				draw_graph();
			}
			if(i == 2){
				$("#dimension").val("onedimen");
				change_pivot();
				$("#twodimen").hide();
			}else{
				$("#twodimen").show();
			}
		});
	}
	var statis = [ [ {
		name : '按年统计',
		value : 'Y_S'
	}, {
		name : '按月统计',
		value : 'M_S'
	} ], [ {
		name : '按国家(国省代码)',
		value : 'AC'
	}, {
		name : '按省份(国省代码)',
		value : 'PC'
	} ],[{
		name : '部(主IPC/CPC分类号)',
		value : 'B_S'
	},{
		name : '大类(主IPC/CPC分类号)',
		value : 'DL_S'
	},{
		name : '大组(主IPC/CPC分类号)',
		value : 'DZ_S'
	},{
		name : '小类(主IPC/CPC分类号)',
		value : 'XL_S'
	}] ];
/**
 * 动态切换统计方式（按什么统计）
 */
	function selectStatis() {
		$("#statistic").empty();
		var sel = $("#field1").val();
		var opt = '';
		var fir;
		if(sel == 'AD_' || sel == 'PD_' || sel == 'AD_PD_'){
			fir =0;
		}else if(sel == '_'){
			fir = 1;
			sel = "";
		}else if(sel == 'MIPC_' || sel == 'CPC_'){
			fir = 2;
		}else{
			$("#statistic").hide();
			return ;
		}
		$("#statistic").show();
		for (var i = 0; i < statis[fir].length; i++) {
			var value = sel + statis[fir][i].value;
			opt = opt + '<option value="'+value+'">' + statis[fir][i].name
					+ '</option>';
		}
		$("#statistic").append(opt);
	}
	/**
	 * 切换统计维度数
	 */
	function change_pivot() {
		if ($("#dimension").val() == 'onedimen') {
			showAcount();
			$("#field1 option:last").remove();
			$("#field1").removeAttr("disabled");
			selectStatis()
			$("#ul_2").show();
		} else {
			$("#field1").append("<option value='AD_PD_' selected='selected'>申请/公开日对比</option>");
			$("#field1").attr("disabled","disabled");
			selectStatis();
			$("#amount_ul").hide();
			$("#amount_top").hide();
			$("#ul_2").hide();
		}
	}
	/**
	 * 动态显示统计数量
	 */
	function showAcount() {
		if ($("#dataSection").val() != 'none') {
			$("#amount_ul").hide();
			$("#amount_top").hide();
		} else {
			$("#amount_top").show();
			$("#amount_ul").show();
		}
	}
/**
 * 切换主题
 */
	function change_theme(type,ths) {
		$("#theme_div .btn_sel").removeClass("btn_sel").addClass("btn");
		$(ths).removeClass("btn").addClass("btn_sel");
		ResetOptions();
		if (type == 1) {
			var defaultOptions = Highcharts.setOptions(theme1);
		} else if(type == 2){
			var defaultOptions = Highcharts.setOptions(theme2);
		}else if(type == 3){
			var defaultOptions = Highcharts.setOptions(theme3);
		}else if(type ==4){
			var defaultOptions = Highcharts.setOptions(theme4);
		}else if(type == 5){
			var defaultOptions = Highcharts.setOptions(theme5);
		}else if(type == 6){
			var defaultOptions = Highcharts.setOptions(theme6);
		}else if(type == 7){
			var defaultOptions = Highcharts.setOptions(theme7);
		}else{
			var defaultOptions = Highcharts.setOptions(theme8);
			
		}
		createChart();
		//将cur放置默认数据图上
		$("#"+graph_type+" ul li").removeClass("cur");
		$("#"+graph_type+" ul li:eq(0)").addClass("cur");
	}
</script>

<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav">
				<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a>
				&gt; <span>智能检索</span>
			</div>
			<div class="search">
				<div class="wrap">
					<div class="search_input">
						<input type="text" placeholder="Search..." id="kwd" name="kwd"
							value="${analysiForm.kwd }" />
						<!-- <div class="btns0">
								<input type="button" class="btn1" value="保存搜索">
							</div> -->
						<div class="btns">
							<input type="button" class="btn2" value="专利分析"
								onclick="check_submit()">
						</div>
					</div>
				</div>
			</div>
			<!--Begin block4-->
			<div class="block4">
				<!--Begin items-->
				<div class="items">
					<ul id="tab_panel">
						<li><a href="<%=basePath%>/analysis.html">概览</a></li>
						<li><a href="<%=basePath%>/complete_analysis.html">完整分析报告</a></li>
						<li class="cur"><a href="<%=basePath%>/custom_analysis.html">自定义分析报告</a></li>
					</ul>
					<h4>
						分析专利 <span>${s_total}</span> 个 
						<input type="button" class="btn1"value="查看" onclick="toSearch()">
					</h4>
					<div class="clear"></div>
				</div>
				<!--End items-->
				<!--Begin block_analysis-->
				<div class="block_analysis">
					<!--Begin L1-->
					<div class="L1">
						<ul>
							<li class="cur"><a><img src="<%=theme%>/images/t1.png"
									alt=""> 柱形图</a></li>
							<li><a><img src="<%=theme%>/images/t2.png" alt="">
									折线图</a></li>
							<li><a><img src="<%=theme%>/images/t3.png" alt="">
									饼图</a></li>
							<li><a><img src="<%=theme%>/images/t4.png" alt="">
									条形图</a></li>
							<li><a><img src="<%=theme%>/images/t5.png" alt="">
									面积图</a></li>
							<li><a><img src="<%=theme%>/images/t6.png" alt="">
									散点图</a></li>
							<!--  <li><a href="#"><img src="####" alt=""> 股价图</a></li>
					            <li><a href="#"><img src="####" alt=""> 曲面图</a></li>
					            <li><a href="#"><img src="####" alt=""> 雷达图</a></li>
					            <li><a href="#"><img src="####" alt=""> 组合</a></li> -->
						</ul>
					</div>
					<!--end L1-->
					<!--Begin L2-->
					<div class="L2">
						<div class="top" id="column" style="display: block">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t11.png"
										alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t12.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t13.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t14.png" alt=""></a></li>
								<!-- <li><a href="#"><img src="####" alt=""></a></li>
					              <li><a href="#"><img src="####" alt=""></a></li>
					              <li><a href="#"><img src="####" alt=""></a></li> -->
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="top" id="line" style="display: none">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t21.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t22.png" alt=""></a></li>
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="top" id="pie" style="display: none">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t31.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t32.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t33.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t34.png" alt=""></a></li>
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="top" id="bar" style="display: none">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t41.png" alt=""></a></li>
								<li ><a><img src="<%=theme%>/images/t42.png" alt=""></a></li>
								<li ><a><img src="<%=theme%>/images/t43.png" alt=""></a></li>
								<li ><a><img src="<%=theme%>/images/t44.png" alt=""></a></li>
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="top" id="area" style="display: none">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t51.png" alt=""></a></li>
								<li><a><img src="<%=theme%>/images/t52.png" alt=""></a></li>
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="top" id="scatter" style="display: none">
							<ul>
								<li class="cur"><a><img src="<%=theme%>/images/t61.png" alt=""></a></li>
							</ul>
							<ul style="float: right;" name="ul_word">
								<li style="vertical-align: bottom;"><a onclick="export_word();"><img title="导出word" src="<%=theme%>/images/export_word.png"/></a></li>
							</ul>
							<div class="clear"></div>
						</div>
						<div class="show" id="show_graph">
							<%-- <img src="<%=theme%>/images/temp/zhu.png"  alt=""/> --%>
						</div>
						<div class="show" id="show_table" style="display: block">
							<table class="analysis_table" id="table_t">
								<thead>
								</thead>
								<tbody></tbody>
							</table>
						</div>
					</div>
					<!--End L2-->
					<!--Begin L3-->
					<div class="L3">
						<div class="wrap" style="display: block" id="config1">
							<!--Begin TT-->
							<div class="TT">
								<div class="top">
									<h3>图表配置项</h3>
									<input type="button" value="刷新图表" class="btn1"
										onclick="refresh_config()" />
									<div class="clear"></div>
								</div>
								<div class="icon">
									<ul>
										<li class="cur"><a><img	src="<%=theme%>/images/g11.png" alt=""></a></li>
										<li><a><img src="<%=theme%>/images/g2.png" alt=""></a></li>
										<li><a><img src="<%=theme%>/images/g3.png" alt=""></a></li>
									</ul>
									<div class="clear"></div>
								</div>
							</div>
							<!--End TT-->
							<div class="TT2">
								<!--Begin TT2-->
								<div class="top">
									<h3>图表标题</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul>
										<li><input type="checkbox" name="box1" checked="checked"/> 
											<input type="text" id="title" name="title" placeholder="图标标题" class="input1">
										</li>
									</ul>
								</div>
							</div>
								<!--End TT2-->
								<div class="TT2">
								<!--Begin TT2-->
								<div class="top">
									<h3>坐标轴标题</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul>
										<li>
											<div class="title">横坐标</div> <input type="checkbox" name="box2" checked="checked" /> <input
											type="text" id="xtitle" name="xtitle" placeholder="横坐标标题"
											class="input1">
										</li>
										<li>
											<div class="title">纵坐标</div> <input type="checkbox" name="box3" checked="checked" /> <input
											type="text" id="ytitle" name="ytitle" placeholder="纵坐标标题"
											class="input1">
										</li>
									</ul>
								</div>
								</div>
								<!-- <div class="top">
									<h3>数据标签</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul>
										<li><input type="checkbox" /> <select name="">
												<option>居中</option>
										</select></li>
									</ul>
								</div> -->
								<div class="TT2">
								<div class="top">
									<h3>图例</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul>
										<li><input type="checkbox" name="box4" checked="checked" /> <select name="legend" id="legend">
												<option value="center">底部</option>
												<option value="left">左</option>
												<option value="right" selected="selected">右</option>
										</select></li>
									</ul>
								</div>
								</div>
								<div class="TT2">
								<div class="top">
									<h3>数据表</h3>
									<input type="button" class="btn1"/>
								</div>
								<div class="lists">
									<ul>
										<li><input type="checkbox" name="box5" checked="checked"/> 显示数据表</li>
									</ul>
								</div>
								</div>
						</div>
						<div class="wrap" style="display: none" id="config2">
							<!--Begin TT-->
							<div class="TT">
								<div class="top">
									<h3>图表配置项</h3>
									<input type="button" value="刷新图表" class="btn1"
										onclick="draw_graph()" />
									<div class="clear"></div>
								</div>
								<div class="icon">
									<ul>
										<li><a><img src="<%=theme%>/images/g1.png" alt=""></a></li>
										<li class="cur"><a><img
												src="<%=theme%>/images/g22.png" alt=""></a></li>
										<li><a><img src="<%=theme%>/images/g3.png" alt=""></a></li>
									</ul>
									<div class="clear"></div>
								</div>
							</div>
							<div class="TT2">
								<div class="top">
									<h3>图表维度</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul>
										<li><select name="dimension" style="width:180px;"
											id="dimension" onchange="change_pivot()">
												<option value="onedimen">单维数据</option>
												<option value="twodimen" id="twodimen">二维比较</option>
										</select></li>
									</ul>
								</div>
							</div>
								<div class="TT2">
								<div class="top">
									<h3>选择字段</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists">
									<ul id="field_ul_1">
										<li><select name="field1" id="field1"
											style="width: 95px" onchange="selectStatis()">
												<option value="AD_">申请日</option>
												<option value="PD_">公开日</option>
												<option value="_">国省代码</option>
												<option value="MIPC_">主IPC分类号</option>
												<option value="CNLX">专利类型</option>
												<option value="CPC_">CPC专利分类号</option>
												<option value="PA_S">申请（专利权）人</option>
												<option value="AU_S">公开（设计）人</option>
												<option value="AGC_S">专利代理机构</option>
												<option value="AGT_S">代理人</option>
												<option value="CTN_COUNT">引证次数</option>
												<option value="PFN_COUNT">同族专利数量</option>
										</select> <select name="statistic" id="statistic" style="width: 85px">
												<option value="AD_Y_S">按年统计</option>
												<option value="AD_M_S">按月统计</option>
										</select></li>
									</ul>
								</div>
								<div class="lists">
									<!-- <ul id="ul_1" style="display: none">
											<li>&nbsp;&nbsp;&nbsp;&nbsp;对比字段：&nbsp; &nbsp;<select name="contraField" id="contraField" style="width:120px"></select></li>
										</ul> -->
									<ul id="ul_2">
										<li><div class="title">数据切片</div><select
											name="dataSection" id="dataSection" style="width: 180px"
											onchange="showAcount()">
												<option value ='none' selected="selected">-----无-----</option>
												<option value="CNLX">专利类型</option>
												<option value="LS">法律状态</option>
												<option value="PC">国省代码</option>
										</select></li>
									</ul>

									<ul id="amount_ul">
										<li>
										<div class="title">统计</div>
										<select name="amount"
											id="amount" style="width: 180px">
												<option value="none" selected="selected">专利数量</option>
												<option value="PA_S">申请（专利权）人数量</option>
												<option value="AU_S">发明（设计）人数量</option>
												<option value="AGC_S">专利代理机构数量</option>
												<option value="AGT_S">代理人数量</option>
										</select>
										</li>
									</ul>
								</div>
								</div>
								<div class="TT2">
								<div class="top" id="amount_top">
									<h3>选择统计量</h3>
									<input type="button" class="btn1" />
								</div>
								<div class="lists" id="amount_list">
									<ul>
										<li><div class="title">显示数量</div><select
											name="limit" id="limit" style="width: 180px">
												<option value="5">前5项（近5年）</option>
												<option value="10" selected="selected">前10项（近10年）</option>
												<option value="15">前15项（近15年）</option>
												<option value="20">前20项（近20年）</option>
										</select></li>
									</ul>
								</div>
								</div>
						</div>
						<div class="wrap" style="display: none" id="config3">
							<!--Begin TT-->
							<div class="TT">
								<div class="top">
									<h3>图表配置项</h3>
									<!-- <input type="button" value="刷新图表" class="btn1"
										onclick="refresh_theme()" /> -->
									<div class="clear"></div>
								</div>
								<div class="icon">
									<ul>
										<li><a><img src="<%=theme%>/images/g1.png" alt=""></a></li>
										<li><a><img src="<%=theme%>/images/g2.png" alt=""></a></li>
										<li class="cur"><a><img
												src="<%=theme%>/images/g33.png" alt=""></a></li>
									</ul>
									<div class="clear"></div>
								</div>
							</div>
							<div class="TT2" id="theme_div">
								<button class="btn" onclick="change_theme(1,this)">主题1</button>
								<button class="btn" onclick="change_theme(2,this)">主题2</button>
								<button class="btn" onclick="change_theme(3,this)">主题3</button>
								<button class="btn" onclick="change_theme(4,this)">主题4</button>
								<button class="btn" onclick="change_theme(5,this)">主题5</button>
								<button class="btn" onclick="change_theme(6,this)">主题6</button>
								<button class="btn" onclick="change_theme(7,this)">主题7</button>
								<button class="btn" onclick="change_theme(8,this)">主题8</button>
							</div>
						</div>
					</div>
						<!--End L3-->
					<div class="clear"></div>
					<!--End block_analysis-->

				</div>
				<!--End block4-->
			</div>
		</div>
	</div>
</div>
<form id="analysis_form">
		<input type="hidden" id="path" name="path" value="${analysiForm.path}" />
		<input type="hidden" name="tName" id="tName" value="${analysiForm.tName}" />
</form>
<form id="export_word" method="post"  action="${pageContext.request.contextPath}/export2.html">
	<textarea id="svg_text" name="svg" ></textarea>
	<textarea id="data" name="data" ></textarea>
</form>