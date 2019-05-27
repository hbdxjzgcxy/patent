/*
 * 配置hightcharts中文显示
 */
Highcharts.setOptions({
	    lang:{
	       contextButtonTitle:"图表导出菜单",
	       decimalPoint:".",
	       downloadJPEG:"下载JPEG图片",
	       downloadPDF:"下载PDF文件",
	       downloadPNG:"下载PNG文件",
	       downloadSVG:"下载SVG文件",
	       drillUpText:"返回 {series.name}",
	       loading:"加载中",
	       months:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
	       noData:"没有数据",
	       numericSymbols: [ "千" , "兆" , "G" , "T" , "P" , "E"],
	       printChart:"打印图表",
	       resetZoom:"恢复缩放",
	       resetZoomTitle:"恢复图表",
	       shortMonths: [ "Jan" , "Feb" , "Mar" , "Apr" , "May" , "Jun" , "Jul" , "Aug" , "Sep" , "Oct" , "Nov" , "Dec"],
	       thousandsSep:",",
	       weekdays: ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六","星期天"]
	    }
}); 

/*
 *分析模块的表单数据 
 * @returns
 */
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

/*
 *分析跳转到检索
 */
function to_search(){
	var url = get_search_url()+"&"+form_data();
	window.location=url;
}


/*
 * 获取分析数据url
 */
function graph_data_url(){
	return "graph.html?"+form_data();
}

/*
 * 绘制水平条形图（自定义）
 */
  function draw_horiz_bar(divId,callback){
	$.post(url,$("#"+divId+" input").serialize(),function(grpah_model){
		var data = grpah_model.data;
		draw_bar(divId,data,callback);
	})
}
$(function(){
	url = graph_data_url();
})
/*
 * 绘制水平条形图
 */
function draw_bar(divId,data,callback){
	var $div = $("#"+divId);
	$div.empty();
	base = data[0].value;
	$(data).each(function(i){	
		if(i>=10){
			return false;
		}else{
			$div.append(new_bar(base,data[i],i,callback));
		}
	})
}

/*
 *封装 Desc 和Code 
 * @param d
 * @returns
 */
function  wrap_text(d){
	var desc = d.desc? d.desc.substring(0,Math.min(d.desc.length,6))+"..." : "";
	return d.desc? d.name+"("+desc+")": d.name;
}

/*
 * 创建一个条形图
 */
function new_bar(base,d,i,callback){
	var bar_w=Math.round(d.value/base*100);//计算条形图的宽度
	$an_bar =$("<li>").addClass("bar_graph");
	$grade= $("<i>").addClass("icon"+(i+1)); //等级
	$region= $("<span>").text(wrap_text(d)).addClass("region") //MIPC_DL_S(描述)
	$a= $("<a>").text(d.value).attr("href","javascript:;").click(function(){
		callback(d);
	})
	$amount= $("<span>").append($a).addClass("amount");//对应的专利统计数量  
	$wrap=$("<span>").addClass("bar_wrap"); //条形图warp
	$bar= $("<span>").css({"width":bar_w+"%"}).addClass("bar"); //条形图
	$wrap.append($bar);
	$an_bar.append($grade).append($region).append($amount).append($wrap);
	return $an_bar;
}

/*
 * 中国地图/世界地图 切换显示
 */
function change_div(ths,mark){
	$("#title_panel1 li").removeClass("cur");
	$(ths).addClass("cur");
	if(mark=="world"){
		$("#chinaMap").hide();
		$("#chinaItems").hide();
		draw_world_map();
		$("#worldMap").show();
		$("#worldItems").show();
	}else{
		$("#chinaMap").show();
		$("#chinaItems").show();
		draw_china_map();
		$("#worldMap").hide();
		$("#worldItems").hide();
	}
}

/*
 * 申请日（年）/公开日（年）切换
 */
function change_graph(ths,mark){
	$("#title_panel li").removeClass("cur");
	$(ths).addClass("cur");
	if(mark=="pd"){
		//draw_line("pd_y_graph");
		draw_pd_y_s();
		$("#ad_y_graph_t").hide();
		$("#pd_y_graph_t").show();
	}else{
		//draw_line("ad_y_graph");
		draw_ad_y_s();
		$("#pd_y_graph_t").hide();
		$("#ad_y_graph_t").show();
	}
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
 * 绘制申请日
 * @returns
 */
function draw_ad_y_s(){
	draw_line("ad_y_graph",function(point){
		submit_search("AD_Y_S="+point.category,form_data());
	});
}

/**
 * 绘制公开日
 * @returns
 */
function draw_pd_y_s(){
	draw_line("pd_y_graph",function(point){
		submit_search("PD_Y_S="+point.category,form_data());
		$("#ad_y_graph_t").highcharts().reflow();
	});
}

/**
 * 绘制申请日和公开日的二维图
 * @param divId
 * @returns
 */
function draw_adpd(divId){
	var category = null;
	var adData = null;
	var pdData = null;
	var adName = $("#ad_y_graph_title").val();
	var pdName = $("#pd_y_graph_title").val();
	$.ajax({
		async : false,
		type : "POST",
		url : url,
		data: $("#ad_y_graph"+" input").serialize(),
		success : function(data){
			adData = data.data,
			category = data.categories
		}
	});
	$.ajax({
		async: false,
		type : "POST",
		url : url,
		data : $("#pd_y_graph"+" input").serialize(),
		success : function(data){
			pdData = data.data
		}
	})
	var names = [adName,pdName];
	var datas = [adData,pdData];
	broken_line(divId,"申请日公开日",category,names,datas);
}
/**
 * 绘制申请年/公开年同时显示的表格
 * @returns
 */
function draw_adpd_table(){
	$("#adpd_table_t").find("tbody").empty();
	var addata = null;
	var pddata = null;
	var i = 0;
	$.ajax({
		async:false,
		type:"POST",
		url:url,
		data:$("#ad_y_table input").serialize(),
		success : function(data){
			addata = data.data;
		}
	})
	$.ajax({
		async:false,
		type:"POST",
		url:url,
		data:$("#pd_y_table input").serialize(),
		success : function(data){
			pddata = data.data;
			for(var j =0;j<pddata.length;j++){
				$tr=$("<tr>");
				$td1=$("<td>").text(++i);
				$td2=$("<td>").text(addata[j].name);
				$a = $("<a>").text(addata[j].value).addClass("intro").attr("href","javascript:;").click(function(){
					search_detail(addata[j].name);
				});
				$td3=$("<td>").append($a);
				$td4 = $("<td>").text(pddata[j].value);
				$("#adpd_table_t").find("tbody").append($tr).append($td1).append($td2).append($td3).append($td4);
			}
			
		}
	})
	
}
/**
 * 绘制折线图的方法（多条数据）
 * @param title
 * @param category
 * @param names
 * @param datas
 * @returns
 */
function broken_line(divId,title,category,names,datas){
	 $("#"+divId+"_t").highcharts({
	        title: {
	            text: title
	        },
	        xAxis: {
	            categories: category
	        },
	        credits: {
	            enabled: false
	       },
	        yAxis: {
	            title: {
	                text: '专利数量(件)'
	            },
	            plotLines: [{
	                value: 0,
	                width: 1,
	                color: '#808080'
	            }]
	        },
	        legend: {
	            align: 'right',
	            verticalAlign: 'middle',
	            layout: 'vertical'
	        },
	        plotOptions: {
		            series: {
		                cursor: 'pointer'
		            },
		        line: {
	                dataLabels: {
	                    enabled: true
	                },
	                enableMouseTracking: false
	            }
	        },
	        series: create_series(names,datas)
	    });
}
/**
 * 创建series动态数据
 * @param names
 * @param datas
 * @returns
 */
function create_series(names,datas){
	var series = new Array();
	for(var i =0;i<datas.length;i++){
		switch(names[i]){
		case "A":names[i]="A(人类生活必须)";
		break;
		case "B":names[i]="B(作业；运输)";
		break;
		case "C":names[i]="C(化学；冶金)";
		break;
		case "D":names[i]="D(纺织；造纸)";
		break;
		case "E":names[i]="E(固定建筑物)";
		break;
		case "F":names[i]="F(机械工程)";
		break;
		case "G":names[i]="G(物理)";
		break;
		case "H":names[i]="H(电学)";
		break;
		}
		series.push({"name":names[i],"data":datas[i]});
	}
	return series;
}

/**
 * 绘制折线图(单条数据)
 * @param divId
 * @returns
 */
function draw_line(divId,callback){
	var title = $("#"+divId+"_title").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		 $("#"+divId+"_t").highcharts({
		        title: {
		            text: title
		        },
		        xAxis: {
		            categories: data.categories
		        },
		        credits: {
		            enabled: false
		       },
		        yAxis: {
		            title: {
		                text: '专利数量(件)'
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        legend: {
		            align: 'right',
		            verticalAlign: 'middle',
		            layout: 'vertical'
		        },
		        plotOptions: {
		            series: {
		                cursor: 'pointer',
		                events: {
		                    click: function (event) {
		                    		callback(event.point);
		                    }
		                }
		            },
		            line: {
		                dataLabels: {
		                    enabled: true
		                },
		                enableMouseTracking: false
		            }
		        },
		        series: [{
		        	type: 'line',
		        	name: '专利数量',          
		        	data: data.data
		        }]
		    });
	})
}

/**
 * 绘制多条数据的柱状图（多条数据）
 * @param divId
 * @param title
 * @param category
 * @param names
 * @param datas
 * @returns
 */
function bar_graph(divId,title,category,names,datas){
	 $("#"+divId+"_t").highcharts({
	        title: {
	            text: title
	        },
	        chart:{
	        	type:'column'
	        },
	        xAxis: {
	            categories: category
	        },
	        credits: {
	            enabled: false
	       },
	       legend: {
	            align: 'right',
	            verticalAlign: 'middle',
	            layout: 'vertical'
	        },
	        yAxis: {
	            min:0,
	            title:{
	            	text:'专利数量（件）'
	            }
	        },
	        plotOptions: {
	        	 column: {
	                 dataLabels: {
	                     enabled: true
	                 }
	             }
	        },
	        series: create_series(names,datas)
	    });
}

/**
 * 绘制柱状图（单条数据）
 * @param divId
 * @param callback
 * @returns
 */
function draw_bar_graph(divId){
	var title = $("#"+divId+"_title").val();
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		 $("#"+divId+"_t").highcharts({
		        title: {
		            text: title
		        },
		        chart:{
		        	type:'column'
		        },
		        xAxis: {
		            categories: data.categories
		        },
		        credits: {
		            enabled: false
		       },
		       legend: {
		            align: 'right',
		            verticalAlign: 'middle',
		            layout: 'vertical'
		        },
		        yAxis: {
		            min:0,
		            title:{
		            	text:'专利数量（件）'
		            }
		        },
		        plotOptions: {
		        	 column: {
		                 dataLabels: {
		                     enabled: true
		                 }
		             }
		        },
		        series: [{
		        	name: '专利数量',          
		        	data: data.data
		        }]
		    });
	})
}

/**
 * 绘制饼状图
 * @param divId
 * @param inputId
 * @returns
 */
function draw_pie(divId,callback){
	//var url ="graph.html?"+"kwd=PN=CN*"+"&"+$("#analysis_form").serialize()
	$.post(url,$("#"+divId+" input").serialize(),function(data){
		 $("#"+divId+"_t").highcharts({
		        credits: {
		            enabled: false
		       },
		        title: {
		            text: $("#"+divId+"_title").val()
		        },
		        tooltip: {
		            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
		        },
		        plotOptions: {
		            pie: {
		                allowPointSelect: true,
		                cursor: 'pointer',
		                dataLabels: {
		                    enabled: true,
		                    format: '<b>{point.name}</b>: {point.percentage:.1f} %'
		                },
		                events:{
		                	click: function(event){
		                		callback(event.point)
		                	}
		                }
		            }
		        },
		        series: [{
		            type: 'pie',
		            name:"百分比",
		            data: data.data
		        }]
		    });
	})
}

/*
 *绘制世界地图 
 * @returns
 */
function draw_world_map(){
	$.post(url,"field=AC&ctype=0&limit=300",function(map_model){
		   var myChart = echarts.init(document.getElementById('worldMap'));
			if(map_model.data.length >0){
				  var option = {
							 title: {
								subtext : '-。-'
							}, 
							tooltip : {
								trigger: 'item',
								formatter: function(a){
									return a[2];
								}
							},
							dataRange: {   
								min: 0,
								max: map_model.data[0].value>=500?map_model.data[0].value:500,
								color:['#0E5CB3','#ADD7FD'],
								text:['高','低'],           // 文本，默认为数值文本
								calculable : true
							},
							series : [
								{
									name: '数据名称',
									type: 'map',
									mapType: 'world',
									scaleLimit:{min:1,max:1},
									itemStyle:{
										normal:{
											label:{show:false},
											areaStyle:{
												color:"#F9C156"
											}
										},
										emphasis:{
											label:{show:true},
											areaStyle:{
												color:"orange"
											}
										}
									},
									data:map_model.data,
									  nameMap : zh_CN_word
							    }
							]
						};
						myChart.setOption(option);	
						draw_bar("worldItems",map_model.data,function(point){
							submit_search("AC="+point.code,form_data());
						});
			}
	   });
}

/*
 * 绘制中国地图
 */
function draw_china_map(){
	$.post(url,"field=PC&limit=50&ctype=0",function(map_model){
		var myChart = echarts.init(document.getElementById('chinaMap'));
		if(map_model.data.length>0){
			var option = {
					 title: {
						subtext : '-。-'
					},
					tooltip : {
						trigger: 'item',
						formatter: function(a){
							return a[2];
						}
					},
					/*legend: {
						orient: 'vertical',
						x:'right',
						data:['数据名称']
					},*/
					dataRange: {
						min: 0,
						max:map_model.data[0].value>=500?map_model.data[0].value:500,
						color:['#0E5CB3','#ADD7FD'],
						text:['高','低'],           // 文本，默认为数值文本
						calculable : true
					},
					series : [
						{
							name: '数据名称',
							type: 'map',
							mapType: 'china',
							scaleLimit:{min:1,max:1},
							itemStyle:{
								normal:{
									label:{show:true},
									areaStyle:{
										color:"#F9C156"
									}
								},
								emphasis:{
									label:{show:true},
									areaStyle:{
										color:"orange"
									}
								}
							},
							data:map_model.data
					    }
						]
					};
					myChart.setOption(option);
					draw_bar("chinaItems",map_model.data,function(point){
						submit_search("PC="+point.name,form_data());
					});
		}
	})
}