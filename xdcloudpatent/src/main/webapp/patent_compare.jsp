<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>专利对比</title>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/common.css">
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/css.css">
</head>
<style>
.content {
/* 	width: 990px; */
	margin: 0 auto;
}

.content [class=row] {
	margin-right: 0;
}

.content .row {
	margin-right: -10px;
	padding-bottom: 10px;
	clear: both;
	zoom: 1;
}

.content .row [class*=column] {
	margin: 0 0 0 10px;
	float: left;
}

.content .overflow-v {
/* 	overflow: visible; */
}

.content .column {
	float: left;
	margin: 0 10px 0 0;
	min-height: 1px;
/* 	overflow: hidden; */
}

.grid-20 {
/* 	width: 990px; */
}

.title-nav {
	height: 27px;
	line-height: 27px;
	position: relative;
	border-top: solid 3px #3b5998;
	background-color: #fff;
}

.title h2 {
	height: 24px;
	line-height: 24px;
	position: relative;
	z-index: 100;
	float: left;
	padding: 0 20px 3px;
	background-color: #3b5998;
	color: #fff;
	float: left;
	font-size: 14px;
	font-weight: 700;
}

.title .title-subcnt {
	height: 27px;
	position: relative;
	z-index: 50;
	font-size: 12px;
	border-right: solid 1px #ccd3e5;
}

.title-content {
	height: auto;
	padding-top: 15px;
}

.title-content table.tableinfo {
	line-height: 29px;
/* 	width:100%; */
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}

user agent stylesheet
table {
	display: table;
	border-collapse: separate;
	border-spacing: 2px;
	border-color: grey;
}

Inherited from div.row
.content .row {
	margin-right: -10px;
	padding-bottom: 10px;
	clear: both;
	zoom: 1;
}

.genre-title {
	height: 36px;
	line-height: 36px;
	font-weight: bold;
	padding-left: 14px;
	cursor: pointer;
	border-top: 1px solid #ccd3e5;
	background-color: #fafbfc;
	color: #3b5998;
}

.title-content table.tableinfo .redtitle {
	height: 52px;
	font-size: 14px;
	font-weight: bold;
	text-align: center;
	color: #d60000;
}

.title-content table.tableinfo th {
	border-top: 1px solid #ccd3e4;
	text-align: center;
	background-color: #fafbfc;
}

.title-content table .title {
	width: 134px;
}

.title-content table.tableinfo tr:hover{  
	background-color:#ffc;  
} 

.title-content table.tableinfo .name {
	text-align: center;
	background-color: #fafbfc;
	line-height: 20px;
	font-weight: bold;
}

.title-content table.tableinfo td {
	border-left: 1px solid #ccd3e4;
	border-top: 1px solid #ccd3e4;
	text-align: center;
}
</style>
<body>
	<div id="J_content" class="content">
		<div class="row">
			<div class="column grid-20 overflow-v">
				<!-- start参数对比 -->
				<div class="title">
					<!-- <div class="title-nav">
                        <h2>专利对比</h2>
                        <div class="title-subcnt">
                            <div class="title-subcnt-tab">
                                <ul>
                                </ul>
                            </div>
                        </div>
                    </div> -->
					<div class="title-content">
						<table id="tbSpecInfo" class="js-hitems tableinfo">
								<tr>
									<th class="title redtitle"></th>
									<c:forEach items="${map}"  var="doc">
										<c:if test="${doc.key == '名称' }">
											<c:forEach items="${doc.value}" var="val">
												<td class="text name"><a
													href="detail/detail.html?docNo=''&hlKwd=${word}"
													target="_blank">${val }</a></td>
												</c:forEach>
										</c:if>
									</c:forEach>
								</tr>
<!-- 						<div class="js-title genre-title" data-title="基本项"> -->
<!-- 							<i class="icon10 icon10-pack"></i> -->
<!-- 							<h3>基本项</h3> -->
<!-- 						</div> -->
							<c:forEach  items="${map}"  var="doc"  > 
							    <tr style="">
										<th class="title">${doc.key}</th>
											<c:forEach items="${doc.value}" var="val">
												<td class="text">${val }</td>
											</c:forEach>
									</tr>
							</c:forEach> 
						</table>
					</div>
				</div>
				<!-- end参数对比start注释
                <div class="prompt">注：以上参数配置信息仅供参考。</div> -->
				<!-- end注释start左侧跟随 -->

				<!-- end左侧跟随 -->
			</div>
		</div>
		<!-- start返回顶部 -->
		<div class="gotop02" data-toggle="gotop" style="bottom: 10px;">
		</div>
		<!-- end返回顶部 -->

	</div>
</body>
</html>