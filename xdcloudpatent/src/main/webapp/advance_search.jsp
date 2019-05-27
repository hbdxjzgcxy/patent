<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<link rel="stylesheet" type="text/css" href="<%=theme%>/css/index.css">
<style>
body {
	text-align: left;
}
</style>
<div class="right_sider">
	<div class="Rwrap">
		<div class="container">
			<div class="nav">
				<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a>
				&gt; <span>高级检索</span>
			</div>
			<form target="_blank" class="advance_search"
				action="&lt;%=path %&gt;search.html" name="formsubmitid"
				id="search_form" method="POST">
				<div id="logic_expression_search" class="none"
					style="display: block; padding: 1% 15%; width: 70%">
					<div class="h_js">
						<div class="h_tj" id="selectfuhao">
							<a href="javascript:void(0);" val=" AND ">AND</a> <a
								href="javascript:void(0);" val=" OR ">OR</a> <a
								href="javascript:void(0);" val=" NOT ">NOT</a> <a
								href="javascript:void(0);" val="()">()</a> <a
								href="javascript:void(0);" val="&quot;&quot;">“ ”</a> <a
								href="javascript:void(0);" val="*">*</a>
							<!-- <a href="javascript:void(0);" val=" ADJ/1W ">ADJ/NW</a>
						<a href="javascript:void(0);" val=" ADJ/1S ">ADJ/NS</a>
						<a href="javascript:void(0);" val=" ADJ/1P ">ADJ/NP</a>
						<a href="javascript:void(0);" val=" PRE ">PRE</a>
						<a href="javascript:void(0);" val=" PRE/1W ">PRE/NW</a>
						<a href="javascript:void(0);" val=" PRE/1S ">PRE/NS</a>
						<a href="javascript:void(0);" val=" PRE/1P ">PRE/NP</a> -->
							<a href="javascript:void(0)" class="alt_more">展开字段列表</a>
						</div>
						<div class="h_tbox">
							<textarea name="kwd" id="kwd" class="h_area"></textarea>
						</div>
						<!-- <input type="hidden" id="domain" name="domain" /> -->
						<input type="hidden" id="path" name="path" /> <input type="hidden"
							name="tName" id="tName" /> <input type="hidden"
							name="searchType" value="advanced" />
					</div>
					<div class="h_ziduan">
						<div class="h_ziduan_in" id="hziduan_id">
							<table width="100%" cellspacing="0" cellpadding="0"
								class="h_ziduan_tab">
								<tbody>
									<tr>
										<td valign="middle"><span>地域</span></td>
										<td><p val="AC=()"
												title="输入专利所在地区，确定检索的数据范围。

示例：

AC=CN

AC=(TW OR HK)
">专利申请国/地区/组织（AC＝）</p></td>
										<td><p></p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>号码</span></td>
										<td><p val="AN=()"
												title="支持模糊检索，模糊字符为&quot;*&quot;， &quot;*&quot;可代替单个或多个字符。

模糊字符&quot;*&quot;位于末尾时可省略不写。支持or逻辑运算。

示例：AN＝CN200710146170.0、AN＝CN200710146170
">申请号（AN＝）</p></td>
										<td><p val="PN=()"
												title="支持模糊检索，模糊字符为&quot;*&quot;， &quot;*&quot;可代替单个或多个字符。

模糊字符&quot;*&quot;位于末尾时可省略不写。支持or逻辑运算。

示例：PN＝CN202872582U or CN103057390A、PN＝CN101062671

">公开（公告）号（PN＝）</p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>日期</span></td>
										<td><p val="AD=()"
												title="申请日由年、月、日三部分组成

示例：

AD=20020101

(AD&gt;=20100602) AND (AD&lt;=20140602)
">申请日（AD＝）</p></td>
										<td><p val="PD=()"
												title="公开(公告)日由年、月、日三部分组成

示例：

PD=20020101

(PD&gt;=20100602) AND (PD&lt;=20140602)

">公开（公告）日（PD＝）</p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>关键词</span></td>
										<td>
											<p val="TAC=()"
												title="示例：TAC=(电动汽车 AND 锂离子电池)、TAC=(混合电动汽车 AND 混合电动车辆)

(TAC=(计算机) NOT TAC=(服务器 OR 笔记本))

">名称＋摘要＋首项权利要求（TAC＝）</p>
											<p val="AB=()"
												title="示例：AB＝(电动汽车 AND 锂离子电池)、AB＝(混合电动汽车 AND 混合电动车辆)

(AB＝(计算机) NOT AB＝(服务器 OR 笔记本))

">摘要（AB＝）</p>
										</td>
										<td>
											<!-- <p val="TA=()" title="示例：TA=(电动汽车 AND 锂离子电池)、TA=(混合电动汽车 AND 混合电动车辆)

(TA=(计算机) NOT TA=(服务器 OR 笔记本))

">名称＋摘要（TA＝）</p> -->
											<p val="TI=()"
												title="示例：TI＝(电动汽车 AND 锂离子电池)、TI＝(混合电动汽车 AND 混合电动车辆)

(TI＝(计算机) NOT TI＝(服务器 OR 笔记本))

">名称（TI＝）</p>
											<p val="CLM=()"
												title="示例：CLM＝(电动汽车 AND 锂离子电池)、CLM＝(混合电动汽车 AND 混合电动车辆)

(CLM＝(计算机) NOT CLM＝(服务器 OR 笔记本))

">首项权利要求（CLM＝）</p>
										</td>
										<td>

											<p val="FT=()"
												title="示例：FT＝(电动汽车 AND 锂离子电池)、FT＝(混合电动汽车 AND 混合电动车辆)

(FT＝(计算机) NOT FT＝(服务器 OR 笔记本))

">说明书（FT＝）</p>
										</td>
									</tr>
									<tr>
										<td valign="middle"><span>公司名/人名</span></td>
										<td><p val="PA=()"
												title="申请（专利权）人支持模糊检索，用截词符*代替模糊部分。

示例：

PA＝北汽福田汽车股份有限公司

PA＝内蒙古*风电

PA=&quot;GM GLOBAL&quot; AND&quot;TECHNOLOGY OPERATIONS&quot;

PA＝(普罗克特 OR 普罗格特) AND (甘布尔 OR 甘保尔)

PA＝(普罗克特 NOT 施瓦茨)

">申请（专利权）人（PA＝）</p></td>
										<td><p val="AU=()"
												title="发明(设计)人支持模糊检索，用截词符*代替模糊部分。
示例：

AU＝朱小林

AU＝&quot;Brunemann George 

AU＝Brunemann

AU＝Brunemann AND George

AU＝&quot;runemann George &quot; AND &quot;Brackney Larry J &quot; 

AU＝&quot;runemann George A &quot;OR &quot;Brackney Larry J &quot;

">发明（设计）人（AU＝）</p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>分类</span></td>
										<td>
											<!-- 											<p val="MIPC=()"
												title="支持模糊检索，模糊部分使用*代替.

示例：

MIPC＝B60S5/02

MIPC＝B60S5

MIPC＝B60F*或B60F

MIPC＝H01M10/44 AND B60L11/18

MIPC＝H01M10/00 OR H01M2/10
 
">主IPC分类号（MIPC＝）</p> -->
											<p val="IPC=()"
												title="支持模糊检索，模糊部分使用*代替.

示例：

MIPC＝B60S5/02

MIPC＝B60S5

MIPC＝B60F*

MIPC＝H01M10/44 AND B60L11/18

MIPC＝H01M10/00 OR H01M2/10

">国际专利分类号（IPC＝）</p>
											<!-- 											<p val="FIC=()"
												title="FI专利分类号是日本专利局对日本专利文献给出的分类号，只适用于日本专利文献或具有日本同族的专利文献的检索。
 
支持模糊检索，模糊部分使用*代替。

 示例：
 
FIC=G02B 6/00,356@A
 FIC=G02C*

FIC=G02C7/04 AND A61K9/08

FIC=G02C7/12 OR G02C7/14

FIC=G02C7/04 NOT A61K9/08

FIC=G02C7/04 not A61K*

">FI专利分类号（FIC=）</p> -->
										</td>
										<td><p val="CPC=()"
												title="CPC专利分类号是由欧洲专利局和美国专利局共同制定的一种专利技术分类代码。

支持模糊检索，模糊部分使用*代替。

CPC＝B60S5/02


CPC＝B60S5


CPC＝B60F*


CPC＝H01M10/44 AND B60L11/18


CPC＝H01M10/00 OR H01M2/10


">CPC专利分类号（CPC＝）</p>
											<!-- <p val="FTC=()"
												title="F-TERM专利分类号是日本专利局对日本专利文献给出的不同于FI专利分类号的另一种专利分类号

支持模糊检索，模糊部分使用*代替

示例：

FTC=2H150AA01

FTC=2H150

FTC=2H150AA01 AND 2H150AB01

FTC=4G021 OR 2H150

FTC=2H150AA01 NOT 2H150AB01

">F-TERM专利分类号（FTC=）</p> --></td>
										<td>
											<!-- <p val="USPC=()"
												title="美国专利分类号是美国专利局对美国专利文献给出的分类号，只适用于美国专利文献或具有美国同族的专利文献的检索。

支持模糊检索，模糊部分使用*代替

示例：

USPC＝903/944

USPC＝903*

USPC＝903/944 OR 903/945 OR 903/946

USPC＝903/947 AND 701/22

USPC＝701/22 NOT 903/947

">美国专利分类号（USPC＝）</p> --> <!-- <p val="DC=()"
												title="国际外观设计分类号用于对外观设计专利进行分类。国际外观设计分类号仅适用于外观设计专利文献检索。

支持模糊检索，模糊部分使用*代替。

示例：

DC＝12-08

DC＝12*

DC＝12-06 OR 12-08

DC＝12* NOT (12-01 OR 12-02)

">国际外观设计分类号（DC＝）</p> -->
										</td>
									</tr>
									<tr>
										<td valign="middle"><span>法律状态</span></td>
										<td><p val="LS=()"
												title="当前法律状态包括三个：有权、审中、失效。

示例：

LS＝失效

LS＝有权

LS＝审中

LS＝有权 OR 审中

">当前法律状态（LS＝）</p></td>
										<td><p val="LSE=()"
												title="检索时需要输入完整的历史法律事件名称，如：

LSE＝专利权的无效宣告

LSE＝专利权的撤销

LSE＝专利实施的强制许可

">历史法律事件（LSE＝）</p></td>
										<!-- 	<td><p val="LSD=()" title="法律状态公告日由年、月、日三部分组成

示例：

LSD＝20020101

(LSD&gt;=20100602) AND (LSD&lt;=20140602)

">法律状态公告日（LSD＝）</p></td> -->
									</tr>
									<!-- 	<tr>
										<td valign="middle"><span>优先权</span></td>
										<td><p val="PRC=()"
												title="要求优先权，是指申请人根据专利法第二十九条规定向专利局要求以其在先提出的专利申请为基础享有优先权。优先申请国指作为优先权基础的在先申请的原受理机构名称。

示例：

PRC＝US

PRC＝US OR JP

">优先申请国（PRC＝）</p></td>
										<td><p val="PRN=()"
												title="优先申请号指作为优先权基础的在先申请的申请号。

支持模糊检索，模糊字符为“*”，支持or逻辑运算。

示例：

PRN＝CN200710146170.0

PRN＝CN200710146170

">优先申请号（PRN＝）</p></td>
										<td><p val="PRD=()" title="优先申请日由年、月、日三部分组成

示例：

PRD＝20020101

(PRD&gt;=20100602) AND (PRD&lt;=20140602)

">优先申请日（PRD＝）</p></td>
									</tr> -->
									<tr>
										<td valign="middle"><span>同族</span></td>
										<td><p val="PFN=()"
												title="同族专利号指构成专利族的同族专利的公开（公告）号（本平台的专利族是简单专利族，由具有完全相同的优先权的专利文献构成）。


注意：同族专利号中应该输入专利文献的公开（公告）号，不能输入申请号。


示例：


PFN＝US4360548A

">同族专利号（PFN＝）</p></td>
										<!-- 	<td><p val="CNT(PFN)=()"
												title="同族专利数指专利族中包含的同族专利的件数（本平台的专利族是简单专利族，由具有完全相同的优先权的专利文献构成）。

一个专利族的同族专利数越多，意味着该专利族所保护发明越重要，市场价值越大。

示例：

（1）检索同族专利数为8件的专利族：CNT(PFN)=8

（2）检索同族专利数大于或等于5件的专利族：CNT(PFN)&gt;=5

（3）检索同族专利数小于或等于5件的专利族：CNT(PFN)&lt;=5

（4）检索同族专利数为5-10件的专利族： CNT(PFN)&gt;=5 AND CNT(PFN)&lt;=10

">同族专利数（CNT(PFN)＝）</p></td> -->
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>引证</span></td>
										<td><p val="CTN=()"
												title="所谓专利引证是指一件专利文献引用其他专利文献或被其他专利文献引用的情况。

引证专利号检索出的结果是引证了被检索公开（公告）号的专利文献。

注意：引证专利号中应该输入专利文献的公开（公告）号，不能输入申请号。

示例：

（1）检索引证了US4360548A的专利文献：CTN=US4360548A

">引证专利号（CTN＝）</p></td>
										<td><p val="CDN=()"
												title="所谓专利引证是指一件专利文献引用其他专利文献或被其他专利文献引用的情况。

被引证专利号检索出的结果是被检索公开（公告）号所引证的专利文献。

注意：被引证专利号中应该输入专利文献的公开（公告）号，不能输入申请号。

示例：

（1）检索US6655579B1所引证的专利文献：CDN=US6655579B1

">被引证专利号（CDN＝）</p></td>
										<!-- 					<td><p val="CNT(CDN)=()"
												title="所谓专利引证是指一件专利文献引用其他专利文献的情况。

被引证次数是指一件专利申请被其后的专利文献引用的次数。

一件专利申请的被引证次数越多，说明该专利申请对后来的技术发展影响越大，重要度越高，处于核心位置。

示例：

（1）检索被引证次数为8次的专利文献：CNT(CDN)=8

（2）检索被引证次数大于或等于5次的专利文献：CNT(CDN)&gt;=5

（3）检索被引证次数小于或等于5次的专利文献：CNT(CDN)&lt;=5

（4）检索被引证次数为5-10次的专利文献： CNT(CDN)&gt;=5 AND CNT(CDN)&lt;=10

">被引证次数（CNT(CDN)＝）</p></td> -->
									</tr>
									<tr>
										<td valign="middle"><span>代理</span></td>
										<td><p val="AGC=()"
												title="专利代理机构字段支持多个检索词之间的AND、OR、NOT运算。

示例：

AGC＝中国国际贸易促进委员会专利商标事务所

AGC＝律诚同业

AGC＝贸易 AND 委员会

">专利代理机构（AGC＝）</p></td>
										<td><p val="AGT=()"
												title="示例：

AGT＝陈红

AGT＝陈红 AND 黄韧敏

AGT＝徐金国 OR  梁挥

">代理人（AGT＝）</p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>地址</span></td>
										<td><p val="ADDR=()"
												title="申请（专利权）人地址字段支持多个检索词之间的AND、OR、NOT运算。

示例：

ADDR＝山东省威海市

ADDR＝山东 AND 威海市

ADDR＝北京 AND 安德路

ADDR＝宣武区 OR 西城区

ADDR＝100011

">申请（专利权）人地址（ADDR＝）</p></td>
										<td><p val="PC=()"
												title="对于中国申请人的专利申请，国省代码字段可以检索申请人的省份，可以用省份名称进行检索；

对于外国申请人的专利申请，国省代码字段可以检索申请人的国籍，可以用国家名称进行检索。

示例：

PC＝浙江

PC＝美国

PC＝重庆 or 四川

">国省代码（PC＝）</p></td>
										<td><p></p></td>
									</tr>
									<!-- 					<tr>
										<td valign="middle"><span>序列</span></td>
										<td><p val="PGQ=()"
												title="蛋白质序列/基因序列字段用于检索氨基酸序列或核苷酸序列。

氨基酸为3位缩写字母代码，多个氨基酸之间用空格分隔，核苷酸为1位字母代码，多个核苷酸之间不用分隔符分隔。

一个氨基酸序列相当于一个多单词组成的短语，输入时应该用&quot;&quot;引起来。

支持多个氨基酸序列之间的AND、OR、NOT、PRE、PRE/NW、ADJ/NW位置限定。

示例：

PGQ＝&quot;Val Lys Ile Val Arg Cys His&quot;

PGQ＝&quot;Val Lys Ile Val Arg Cys His&quot;&quot;Val Lys Ile Val Arg Cys His&quot; AND &amp;quotLeu Arg TrpAla Gly Arg Tyr Gln Ser&quot;

PGQ＝&quot;Val Lys Ile Val Arg Cys His&quot; OR &quot;Leu Arg TrpAla Gly Arg Tyr Gln Ser&quot;

PGQ＝&quot;Val Lys Ile Val Arg Cys His&quot; NOT &quot;Leu Arg TrpAla Gly Arg Tyr Gln Ser&quot;

">蛋白质序列/基因序列（PGQ＝）</p></td>
										<td><p></p></td>
										<td><p></p></td>
									</tr>
									<tr> -->
									<!-- 										<td valign="middle"><span>标签</span></td>
										<td><p val="TN=()" title="示例：

TN=发明目的

TN=发明目的 OR 技术问题

">标签名称（TN=）</p></td>
										<td><p val="TV=()" title="示例：

TV=汽车

TV=计算机 OR 电脑

TV=汽车 AND 电池

">标签内容（TV=）</p></td>
										<td><p></p></td>
									</tr>
									<tr>
										<td valign="middle"><span>化学结构</span></td>
										<td><p val="MOL=()"
												title="1、利用界面中的化学结构绘制工具绘制出化学结构。

2、选择检索的类型是&quot;相似结构检索&quot;、&quot;准确结构检索&quot;、还是&quot;子结构检索&quot;。

3、当选择&quot;相似结构检索&quot;时，利用相似度滑条选择相似度。

">化学结构式（MOL＝）</p></td>
										<td><p></p></td>
										<td><p></p></td>
									</tr> -->
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<a href="javascript:;" class="more_condition" style="display: none;">
					更多检索条件<span class="arrow_down"><i></i></span>
				</a>
				<div class="actions condition submit1">
					<!-- <input type="hidden" name="searchType" value="advanced" /> <input type="checkbox" name="save_search" id="save_search" /> <label
						for="save_search">保存检索</label> -->
					<button class="btn1" title="检索" type="button"
						onclick="check_submit();">检索</button>
					<button class="btn2" title="清除" type="reset">清除</button>
				</div>
			</form>
			<!--End submit1-->
		</div>
		<!-- <div class="keywords">
        <table>
          <tbody>
            <tr>
              <td>AN/SQH/专利申请号</td>
              <td>PAM/GTK/专利公开号</td>
              <td>TI/MC/名称</td>
              <td>AB/ST/ZY摘要</td>
            </tr>
            <tr>
              <td>AN/SQH/专利申请号</td>
              <td>PAM/GTK/专利公开号</td>
              <td>TI/MC/名称</td>
              <td>AB/ST/ZY摘要</td>
            </tr>
            <tr>
              <td>AN/SQH/专利申请号</td>
              <td>PAM/GTK/专利公开号</td>
              <td>TI/MC/名称</td>
              <td>AB/ST/ZY摘要</td>
            </tr>
            <tr>
              <td>AN/SQH/专利申请号</td>
              <td>PAM/GTK/专利公开号</td>
              <td>TI/MC/名称</td>
              <td>AB/ST/ZY摘要</td>
            </tr>
          </tbody>
        </table>
      </div> -->
	</div>
	<!--End block3-->
</div>
<script type="text/javascript">
	$(function() {
		$('.alt_more').on('click', function() {
			if ($(this).text() == '展开字段列表')
				$(this).text('收起字段列表');
			else
				$(this).text('展开字段列表');

			$(".h_ziduan").toggle();
		})

		$('#selectfuhao a:not(:last)').on('click', function() {
			$('#kwd').val($('#kwd').val() + $(this).attr('val'));
			$('#kwd').focus();
		})

		$('.h_ziduan p').on('click', function() {
			$('#kwd').val($('#kwd').val() + $(this).attr('val'));
			$('#kwd').focus();
		})

	})

	function check_submit() {
		var kwd = $('#search_form')[0].kwd.value;
		if (!$.trim(kwd)) {
			alert("请输入检索表达式!");
			return false;
		} else {
			submit_search();
		}
	}
</script>