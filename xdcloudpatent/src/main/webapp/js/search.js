/*
 * 提交查询请求
 */
function submit_search(filter,data){
	var s_url=get_search_url();
	if(check()){
		var ss = $("#ss").val();
		if(ss=="0"){/*如果不是二次检索，清除上次检索条件*/
			$("#pkwd").val(); 
			$("#path").val();
		}
		var $form_data = data||$("#search_form").serialize();
		
		if(filter){
			window.location=s_url+"&"+$form_data+"&"+filter;
		}else{
			window.location=s_url+"&"+$form_data;
		}
	}
}

function analysis(filter){
	var s_url=get_analysis_url();
	if(check()){
		var $form_data = $("#search_form").serialize();
		if(filter){
			window.location=s_url+"&"+$form_data+filter;
		}else{
			window.location=s_url+"&"+$form_data;
		}
	}
	
}

/**
 * 下一页
 */
function next_page(pn){
	page(pn+1);
}
/**
 * 上一页
 */
function previous_page(pn){
	page(pn-1);
}

/**
 * 页码跳转
 */
function page(tag_or_num){
	var pn;
	if(isNaN(tag_or_num)){
		pn=$(tag_or_num).text();
	}else{
		pn=tag_or_num;
	}
	if(pn>0){
		$("#pageNo").val(pn);
		//$("form").submit();
		submit_search(get_filter())
	}else{
		//TODO:增加框架提示 “已经是第一页了”
		alert("已经是第一页了!");
	}
}

/**
 * 重置分页大小
 */
function reset_page_size(ps){
	if(ps){
		$("#pageSize").val(ps);
		//$("#search_from").submit();
		submit_search();
	}
}

/**
 * 初始化分页页签
 */
function init_page_tag(cur_pn,page_size,total){
	//初始化 每页多少条页签
	$("#size"+page_size).addClass("cur");
	
	//隐藏所有页码
	$("a[id*=pager]").hide();
	
	//计算总共多少页
	var max_pn = Math.ceil(total/page_size); 
	$("#pager_x").click(function(){page(max_pn)});
	//页码小于5时，默认显示
	if(cur_pn<5){
		if(max_pn<5){
			for(var i=0;i<=max_pn;i++){
				$("#pager"+i).show();
			}
		}else{
			$("#pager1,#pager2,#pager3,#pager4,#pager5").show();
		}
		$("#pager"+cur_pn).addClass("cur"); //当前页码添加样式
	}else{ 
			/*
			 *当前页码>5：重新计算页码
			 */
			$("#page_a").show()
			$("#pager1").text(cur_pn-2).show();
			$("#pager2").text(cur_pn-1).show();
			$("#pager3").text(cur_pn).addClass("cur").show();
			$("#pager4,#pager5").hide();
			if((cur_pn+1)<=max_pn){
				$("#pager4").text(cur_pn+1).show();
			}
			if((cur_pn+2)<=max_pn){
				$("#pager5").text(cur_pn+2).show();
			}
	}
	if(cur_pn>5){
		$("#pager_a,#pager_p").show()
	}else{
		if(cur_pn<max_pn){
			$("#pager_n,#pager_x").show();
		}		
		if(max_pn>1000){
			$("#pager_x").hide();
		}
	}
	
	if(cur_pn >= max_pn){
		$('.next_page').removeAttr('onclick');
	}
	
}

function set_list(ths){
	var $this = $(ths);
	var chk = $this.prop("checked");
	var id = $this.attr("id");
	var f = id.substring(2);
	if(chk){
		//alert("*:[id*=td_f_"+f+"]");
		$("*[id*=td_f_"+f+"]").show();
	}else{
		$("*[id*=td_f_"+f+"]").hide();
	}
	
	//写入cookie
	var list_id="";
	$(":checked[id*=f_]").each(function(i){
		list_id+=" "+$(this).attr("id");
	});
	//alert(list_id);
	$.cookie('list_id',list_id.substring(1));
}


function check(){
	/*if($("#kwd").length > 0){//对象是否存在
		if($("#kwd").val()=="" && $('#cur').val() != 'admin_center'){
			return false;
		}
	}*/
	return true;
}

function before_submit(){
	try{
		$("#pageNo").val(1);
		$("#facet").val(0);
	}catch(e){
		
	}
}

/*
 * 按路径搜索
 */
function path_search(cls_name,cls_val,t_name){
		$("#domain").val(cls_name);
		$('#path').val(cls_val);
		$('#tName').val(t_name);
		$('#ss').val(0);
		before_submit();
		submit_search();
}



/**
 * 二次检索
 */
function second_search(){
	$("#ss").val(1);
}

/**
 * 过滤搜索
 */
function filter_search(filter){
	before_submit();
	$("#facet").val(1);
	submit_search(filter);
}


function field_search(field,val){
	before_submit();
	$("#kwd").val(field+"="+val);
	submit_search();
}

function detail(docNo,hlKwd,url,type){
	$.ajax({  
        type: 'POST',   
        dataType : "html",   
        data: {'docNo':docNo,'hlKwd':hlKwd,'type':type},
        url: url,  
        success:function(data){
			$('.items_list').html(data);
        }
    }); 
}

var downLayerIndex = 0;
function download(docNo,kwd,batch,path,tName){
	var path = encodeURIComponent(path);
	var tName = encodeURIComponent(tName);
	var kwd = encodeURIComponent(kwd);
	downLayerIndex = layer.open({
		  type: 2,
		  title: ['下载专利','padding-right:646px;color: #FFF;background-color: #962925;'],
		  area:['766px','600px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: 'patentDown/downpage.html?docNo='+docNo+'&kwd='+kwd+'&batch='+batch+'&path='+path+'&tName='+tName
		});
};

function closeLayer(){
	layer.close(downLayerIndex);
}

function add2Compare(docNo,serial){
	ti = $('#ti_'+serial).text();
	$('.comparepop-content').show();
	var liObj = '<li style="text-align:left;margin:10px 10px"><input type="checkbox" checked="checked" value="'+docNo+'" style="vertical-align: middle;"/><a style="cursor:pointer" target="_blank">'+ti+'</a><i class="icon16 icon16-close"></i></li>';
	if($('.comparepop-list li input[value="'+docNo+'"]').length <= 0){
		$('.comparepop-list').append(liObj);
		$.cookie('compareList',$('.comparepop-list').html(),{path:'/'});
	}else{
			alert('不能重复添加');
	}
}

function collect(docNo,serial){
	ti = $('#ti_'+serial).text();
	favorites(docNo,ti);
}
function favorites(docNo,ti){
	var strFullPath=window.document.location.href; 
	var strPath=window.document.location.pathname; 
	var pos=strFullPath.indexOf(strPath); 
	var prePath=strFullPath.substring(0,pos); 
	var postPath=strPath.substring(0,strPath.substr(1).indexOf('/')+1);
	
	layer.open({
		  type: 2,
		  title: ['收藏夹','padding-right:430px;color: #FFF;background-color: #962925;'],
		  area:['500px','400px'],
		  closeBtn: 0,
		  closeBtn :1,
		  shadeClose: true,
		  content: 'favorite/tofavorites.html?docNo='+docNo+"&ti="+ti
		});
}

$(function(){
	var liArray = $.cookie('compareList');
	if(liArray){
		$('.comparepop-list').append(liArray);
	}
	
	$('.comparepop-btn').on('click',function(){
		$('.comparepop-content').toggle();
	})
	
	$('.j-compare-begin').on('click',function(){
		var curSel = $('.comparepop-list input[type="checkbox"]:checked').map(function(){
			  return $(this).val();
			});
		var count = curSel.length;
		if(count < 1 ){
			alert('请点击专利条目右侧的对比按钮添加进对比框后再进行比对');
			return;
		}
		if(count < 2 && count > 0){
			alert('对比项不能少于2条');
			return;
		}
		if(count > 3){
			alert('对比项不能超过3条');
			return;
		}
		$('#compare_form')[0].docNo.value = curSel.get().join(',');
		$('#compare_form')[0].word.value = '${params.kwd}';
		$('#compare_form').submit();
	})
	
	$('.j-compare-clear').on('click',function(){
		$('.comparepop-list li').remove();
		$.cookie('compareList','',{path:'/'});
	})
	
})

function checkallbox(obj){
	var checkFlag = $(obj).prop("checked"); 
    $(".lists input[type=checkbox]").each(function() { 
        $(this).prop("checked", checkFlag); 
    }); 
	
}

function batchdown(kwd,path,tName){
	if(selectionSize() > 0){
		download(selection(),kwd,'0',path,tName);
	}else{
		alert('请选择专利');
		return;
	}
}
function batchCollect(){
	
	if(selectionSize() > 0){
		ti='';
		var sels = selection();
		$('.lists input[type=checkbox]:checked').each(function(){
			ti+="<p>"+$(this).next('span').text()+"</p>";
		})
		favorites(selection(),ti);
	}else{
		alert('请选择专利');
		return;
	}
}

	var selectionSize =  function (){
		return $('.lists input[type=checkbox]:checked').size();
	}
	var selection =  function (){
		return $('.lists input[type=checkbox]:checked').map(function(){return $(this).val()}).get().join(',');
	}
	
