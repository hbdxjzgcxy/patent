<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title></title>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css"/>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css"/>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/index.css"/>
</head>
<body>
<div class="block6"> 
          <!--Begin items-->
          <div class="patent_detail items">
            <ul>
              <li class="cur"><a id="zlxm">著录项目</a></li>
              <c:if test="${!empty doc.CLM && doc.CLM ne 'null' }">
              <li><a id="qlyqs">首项权利要求</a></li>
              </c:if>
              <c:if test="${!empty doc.FT && doc.FT ne 'null' }">
              <li><a id="sms">说明书</a></li> 
              </c:if>
              <c:if test="${!empty doc.PFNS && doc.PFNS ne 'null' }">
              <li><a id="tzzl">同族专利</a></li>
              </c:if>
              <c:if test="${!empty doc.LS && doc.LS ne 'null' }">
              <li><a id="flzt">法律状态</a></li>
              </c:if>
              <c:if test="${(!empty doc.CTNS && doc.CTNS ne 'null') || (!empty doc.CDNS && doc.CDNS ne 'null') }">
              <li><a id="yzxx">引证信息</a></li>
              </c:if>
            </ul>
            <h4>
              <a onclick="downPDF('${doc.PN}')" class="btns btn1" style="padding: 3px 15px 0px 30px" target="hideIfrmae">下载PDF</a>
              <a href="down.html?pn=${doc.PN}" class="btns btn1b" style="padding: 3px 15px 0px 30px" target="_blank">查看PDF</a>
              <!-- <input type="button" class="btns btn2"  value="收藏"> -->
              <input type="button" class="btns btn3" onclick="print('list','div.block6')" value="打印">
            </h4>
            <div class="clear"></div>
          </div>
          <!--End items--> 
          <!--Begin items_list-->
          <div class="items_list">
            <div class="clear"></div>
          </div>
          <!--End items_list--> 
          
        </div>
        
        
<script src="<%=path %>/js/jquery/jquery-1.12.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="<%=path %>/js/search.js"></script>
<script type="text/javascript" src="<%=path %>/js/jquery/jquery.cookie.js"></script>
<script src="<%=path %>/js/jquery/jquery.PrintArea.js"></script>
	<script type="text/javascript">
$(function(){
	var docNo = '${docNo}';
	var hlKwd = ${hlKwd};
	detail(docNo,hlKwd,'<%=path %>/detail/info.html','zlxm');
	
	$('.patent_detail li a').on('click',function(){
		$(this).parent().siblings('.cur').removeClass('cur');
		$(this).parent().addClass('cur');
		var reqType = $(this).attr('id');
		url = '<%=path %>/detail/info.html';
		detail(docNo,hlKwd,url,reqType);
	})
})

function downPDF(pn){
	window.open("<%=path %>/detail/down.html?pn="+pn);
}

function print(style,area){
         var print = area;
         var keepAttr = ['style','on','class'];
         var headElements = '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>';
         var options = { mode : 'iframe', popClose : 'false', retainAttr : keepAttr, extraHead : headElements };
         $( print ).printArea( options );
	
}

</script>
<iframe name="hideIfrmae">

</iframe>
</body>
</html>