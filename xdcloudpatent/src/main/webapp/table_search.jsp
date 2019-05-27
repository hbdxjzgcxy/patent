<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path;
	String theme = path + "/" + "theme/default";
%>
<script language="javascript" type="text/javascript" src="js/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.hint {
	position: absolute;
	display: none;
	/*  left: 175px; */
	/*  top: 32px; */
	width: 100%;
	border: 1px solid #fadaaf;
	border-top: 0px;
	background-color: #fff9ec;
	color: #eb934c;
	line-height: 20px;
	padding: 6px 0px 6px;
	z-index: 10;
	background-color: #fff9ec;
}

.hint .selected {
	background-color: #1d75bd;
	color: #fff;
}

.hle_list li {
	display: inline-block;
	margin: 2px 4px;
}

body {
	text-align: left;
}
</style>
<script type="text/javascript">
	//优先申请国 处理
	function prc_deal() {
		var prc_arr = [];
		$("#PRC_1_id a.selected").each(function() {
			prc_arr.push($(this).text());
		});
		$("#PRC_2_id a.selected").each(function() {
			prc_arr.push($(this).text());
		});
		$("#PRC").val(prc_arr.join('  OR  '));
	}

	//法律状态
	function lse_deal() {
		var prc_arr = [];
		$("#LSE_1_id a.selected").each(function() {
			var text = $(this).text();
			if (text != '全部') {
				prc_arr.push(text);
			}
		});
		$("#LE").val(prc_arr.join('  OR  '));
	}

	$(function() {
		var timer;
		$(":text").each(function() {
			var id = $(this).attr("id");
			if (id != "LE" && id != "PRC") {
				$(this).hover(function() {
					$this = $(this);
					$hint = $this.next(".hint");
					var w = $(this).css("width");
					timer = setInterval(function() {
						$hint.css("width", $this.css("width")).show();
					}, 200);
				}, function() {
					$this = $(this);
					$this.next(".hint").hide();
					clearInterval(timer);
				});
			}
		});

		$("#more_btn").click(function() {
			var btn_text = $(this).text();
			if (btn_text == "显示更多检索条件") {
				$(this).text("隐藏更多检索条件");
				$(".more").show();
			} else {
				$(this).text("显示更多检索条件");
				$(".more").hide();
			}
		});

		$('.mult_sel').each(function() {
			var $this = $(this);
			input = $this.find('input[type=text]');

			var hint = $(this).find('.hint');

			input.bind('focus', function() {
				hint.css("width", $(this).css("width"));
				hint.show();
			});

			$this.bind('mouseleave', function() {
				hint.hide();
				input.blur();
			});

			hint.find('a:not(#pa_more)').click(function() {
				$(this).toggleClass('selected');
				prc_deal();
				lse_deal();
				return false;
			});
		});
	})

	function check_submit() {
		var check = false;
		$(":text").each(function() {
			var val = $.trim($(this).val());
			if (val != '' && val.length != 0) {
				check = true;
				return false;
			}
		});
		check = check || $("select").val();
		if (check) {
			if(!checkbox()){
				alert("请选择检索位置!");
			}else{
				submit_search();
			}
		} else {
			alert("至少输入一个检索条件!");
		}
	}
	
	var checkbox = function(){
		if($('input[name="moreKwd"]').val() != '' || $('input[name="lessKwd"]').val() != '' || $('input[name="notKwd"]').val() != '' ){
			if($("input:checkbox[name='fields']:checked").length == 0){
				return false;
			}
			return true;
		}else
			return true;
	}
	
</script>
<form id="search_form">
	<div class="right_sider">
		<div class="Rwrap">
			<div class="container">
				<div class="nav" style="margin-bottom: 15px;">
					<img src="<%=theme%>/images/search3.png" alt="" /> <a href="#">专利检索</a> &gt; <span>表格检索</span>
				</div>
				<!--Begin block2-->
				<div class="block2">
					<div class="lists">
						<div class="title">
							<h3>关键字</h3>
						</div>
						<div class="wrap">
							<div class="tt">
								在以下位置中 <span> <input type="checkbox" name="fields" value="TI" /> 名称
								</span><span> <input type="checkbox" name="fields" value="AB" /> 摘要
								</span><span> <input type="checkbox" name="fields" value="CLM" /> 权利要求书
								</span><span> <input type="checkbox" name="fields" value="FT" /> 说明书
								</span>
							</div>
							<table>
								<tbody>
									<tr>
										<td>包含全部关键字</td>
									</tr>
									<tr>
										<td><input type="text" placeholder="多个关键字用逗号分离" name="moreKwd">
											<div class="hint">多个关键字之间为AND的关系。如：名称中含“电动汽车”和“锂离子电池”，则选中“名称”字段，在输入框中键入：电动汽车,锂离子电池</div></td>
									</tr>
									<tr>
										<td>包含至少一个关键字</td>
									</tr>
									<tr>
										<td><input type="text" placeholder="多个关键字用逗号分离" name="lessKwd">
											<div class="hint">多个关键字之间为OR的关系。如：名称中包含“混合电动汽车”或“混合电动车辆”，则选中“名称”字段，在输入框中键入：混合电动汽车 OR混合电动车辆</div></td>
									</tr>
									<tr>
										<td>不包含关键字</td>
									</tr>
									<tr>
										<td><input type="text" placeholder="多个关键字用逗号分离" name="notKwd">
											<div class="hint">此输入框为否定关键字，逻辑符为NOT。多个否定关键字之间为OR的关系。如：摘要中包含“计算机”但不包含“服务器”和”笔记本”时，则选中“摘要”字段，在“包含全部关键字”输入框中键入：计算机，在“不包含关键字”输入框中键入：服务器,笔记本</div>
										</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!--End block2-->
				<!--Begin block2-->
				<div class="block2">
					<div class="lists">
						<div class="title">
							<h3>专利</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>申请(专利)号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[0].field" value="AN" /> <input type="text" name="s[0].value" placeholder="">
											<div class="hint">
												<p>支持模糊检索，模糊字符为“*”， “*”可代替单个或多个字符。模糊字符“*”位于末尾时可省略不写。支持or逻辑运算。</p>
												<ol>
													<li>输入完整号码，如键入：CN200710146170.0、CN201110093282.0</li>
													<li>输入部分号码，如键入：CN200710146170、CN101100172、200710146170</li>
													<li>检索多个号码时，用or算符连接各号码，如CN200710146170、CN200710096257，应键入：CN200710146170 or CN200710096257</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>公开(专利)号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[1].field" value="PN" /> <input type="text" name="s[1].value" />
											<div class="hint">
												<p>支持模糊检索，模糊字符为“*”， “*”可代替单个或多个字符。模糊字符“*”位于末尾时可省略不写。支持or逻辑运算。</p>
												<ol>
													<li>输入完整号码，如键入：CN102206221A、CN101100172A</li>
													<li>输入部分号码，如键入：CN101062671、102897045A</li>
													<li>检索多个号码时，用or算符连接各号码，如CN202872582U、CN103057390A，应键入：CN202872582U or CN103057390A</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>申请(专利权)人</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[2].field" value="PA" /> <input type="text" name="s[2].value" placeholder="">
											<div class="hint">
												<p>申请（专利权）人为个人或组织，请输入申请人或专利权人的名字或名称进行检索，支持模糊检索，模糊部分位于名称中间时，用截词符*代替模糊部分，位于名称开始或末尾时，可省略截词符。多个检索词之间的AND、OR、NOT逻辑运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>北汽福田汽车股份有限公司</li>
													<li>“THE PROCTER & GAMBLE COMPANY”</li>
													<li>普罗克特</li>
													<li>内蒙古*风电</li>
													<li>名称中包含“普罗克特”和“甘布尔”，应键入：普罗克特 AND 甘布尔</li>
													<li>申请（专利权）人包含郭衡和朱小林，应键入：郭衡 AND 朱小林</li>
													<li>申请（专利权）人为“普罗克特和甘保尔公司”或“宝洁公司”，应键入：普罗克特和甘保尔公司 OR宝洁公司</li>
													<li>申请（专利权）人名称中包含“普罗克特”或“普罗格特”，此外还包含“甘布尔”或“甘保尔”，应键入：(普罗克特 OR 普罗格特) AND (甘布尔 OR 甘保尔)</li>
													<li>申请（专利权）人名称中包“GM GLOBAL”和“TECHNOLOGY OPERATIONS”，应键入：”GM GLOBAL” AND “TECHNOLOGY OPERATIONS”</li>
													<li>申请（专利权）人名称中包含“普罗克特”但不包含“施瓦茨”，应键入：普罗克特 NOT 施瓦茨</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>发明(设计人)人</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[3].field" value="AU" /> <input type="text" name="s[3].value" placeholder="">
											<div class="hint">
												<p>发明(设计)人为个人，请输入发明(设计)人的名字进行检索，支持模糊检索，模糊部分位于名字中间时，用截词符*代替模糊部分，位于名字开始或末尾时，可省略截词符。多个检索词之间的AND、OR、NOT逻辑运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>朱小林</li>
													<li>”Brunemann George A”</li>
													<li>发明(设计)人名字中包含Brunemann，应键入：Brunemann</li>
													<li>发明(设计)人名字中包含Brunemann和George，应键入：Brunemann AND George</li>
													<li>发明(设计)人名字中包含” runemann George A”和” Brackney Larry J”，应键入：”runemann George A” AND “Brackney Larry J”</li>
													<li>发明(设计)人为” runemann George A”或” Brackney Larry J”，应键入：”runemann George A”OR “Brackney Larry J”</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>国际专利分类号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[4].field" value="IPC" /> <input type="text" name="s[4].value" placeholder="" />
											<div class="hint">
												<p>国际专利分类号是由国际知识产权组织制定的一种专利技术分类代码，国际专利分类号仅适用于发明和实用新型专利文献检索。国际专利分类号可通过《国际专利分类表》获得。支持模糊检索，模糊部分使用*代替，当模糊部分位于分类号尾部时，*可以省略，多个国际专利分类号之间的AND、OR、NOT运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索国际专利分类号B60S5/02，应键入：B60S5/02</li>
													<li>检索国际专利分类B60S5大组，应键入：B60S5</li>
													<li>检索国际专利分类B60F小类，应键入：B60F*或B60F</li>
													<li>国际专利分类号中包含H01M10/44和B60L11/18，应键入：H01M10/44 AND B60L11/18</li>
													<li>国际专利分类号中包含H01M10/00或H01M2/10，应键入：H01M10/00 OR H01M2/10</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>当前法律状态</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[5].field" value="LS" /> <select name="s[5].value" style="text-indent: 1px; width:44%">
												<option value="">全部</option>
												<option value="公开">公开</option>
												<option value="实质审查">实质审查</option>
												<option value="撤回">撤回</option>
												<option value="驳回">驳回</option>
												<option value="审定">审定</option>
												<option value="授权">授权</option>
												<option value="放弃">放弃</option>
												<option value="部分无效">部分无效</option>
												<option value="部分撤销">部分撤销</option>
												<option value="全部撤销">全部撤销</option>
												<option value="权利终止">权利终止</option>
												<option value="权利恢复">权利恢复</option>
											</select></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<!--End block2-->
				<!--Begin block2-->
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>日期</h3>
						</div>
						<div class="wrap" style="width: 30%; margin-left: 16%">
							<table>
								<tbody>
									<tr>
										<td>申请日</td>
									</tr>
									<tr>
										<td><div class="input">
												<input type="hidden" name="rs[0].field" value="AD" /> <input type="hidden" name="rs[0].type" value="date" />
												<ul>
													<li class="i-1"><input type="text" name="rs[0].start" id="d12" onclick="WdatePicker()"  /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[0].end" id="d11" onclick="WdatePicker({maxDate:'%y-%M-%d'})"  /></li>
												</ul>
											</div></td>
									</tr>
									<tr>
										<td>公开(公告)日</td>
									</tr>
									<tr>
										<td><div class="input">
												<input type="hidden" name="rs[1].field" value="PD" /> <input type="hidden" name="rs[1].type" value="date" />
												<ul>
													<li class="i-1"><input type="text" name="rs[1].start" onclick="WdatePicker()" /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[1].end" onclick="WdatePicker({maxDate:'%y-%M-%d'})" /></li>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>代理</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>代理机构</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[6].field" value="AGC" /> <input type="text" name="s[6].value" />
											<div class="hint">
												<p>专利代理机构字段支持多个检索词之间的AND、OR、NOT运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>专利代理机构为中国国际贸易促进委员会专利商标事务所，应键入：中国国际贸易促进委员会专利商标事务所</li>
													<li>专利代理机构名称中包含律诚同业，可键入：律诚同业</li>
													<li>专利代理机构名称中包含贸易和委员会，可键入：贸易 AND 委员会</li>
													<li>专利代理机构为北京律诚同业知识产权代理有限公司或中国国际贸易促进委员会专利商标事务所，应键入：北京律诚同业知识产权代理有限公司 OR 中国国际贸易促进委员会专利商标事务所</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>代理人</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[7].field" value="AGT" /> <input type="text" name="s[7].value" />
											<div class="hint">
												<p>代理人字段支持多个检索词之间的AND、OR运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>代理人为陈红，可键入：陈红</li>
													<li>代理人中包含陈红和黄韧敏，可键入：陈红 AND 黄韧敏</li>
													<li>代理人中包含徐金国或梁挥，可键入：徐金国 OR 梁挥</li>
												</ol>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>地址</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>申请（专利权）人地址</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[8].field" value="ADDR" /> <input type="text" name="s[8].value" />
											<div class="hint">
												<p>申请（专利权）人地址字段支持多个检索词之间的AND、OR、NOT运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>地址中包含山东省威海市，可键入：山东省威海市</li>
													<li>地址中包含山东和威海，可键入：山东 AND 威海市</li>
													<li>申请人地址为北京市某区安德路，应键入：北京 AND 安德路</li>
													<li>检索宣武区或西城区的专利，可键入：宣武区 OR 西城区</li>
													<li>申请人地址邮编为100011，可键入：100011</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>国省代码</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[9].field" value="PC" /> <input type="text" name="s[9].value" />
											<div class="hint">
												<p>对于中国申请人的专利申请，国省代码字段可以检索申请人的省份，可以用省份名称进行检索；对于外国申请人的专利申请，国省代码字段可以检索申请人的国籍，可以用国家名称进行检索。支持多个检索词之间的OR运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索浙江省的申请人的专利申请，应键入：浙江</li>
													<li>检索美国申请人的专利申请，应键入：美国</li>
													<li>检索重庆或四川的申请人的专利申请，应键入：重庆 OR 四川</li>
												</ol>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>法律状态</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>历史法律事件</td>
									</tr>
									<tr>
										<td class="mult_sel"><input type="hidden" name="s[10].field" value="LE" /> <input type="text" name="s[10].value" id="LE" />
											<div class="hint">
												<ul class="hle_list" id="LSE_1_id">
													<li><a class="selected" href="">全部</a></li>
													<li><a href="">公开</a></li>
													<li><a href="">申请的撤回</a></li>
													<li><a href="">专利申请的视为撤回</a></li>
													<li><a href="">实质审查请求的生效</a></li>
													<li><a href="">专利局对发明专利申请自行进行实质审查的决定</a></li>
													<li><a href="">专利申请的驳回</a></li>
													<li><a href="">审定</a></li>
													<li><a href="">授权</a></li>
													<li><a href="">专利权的视为放弃</a></li>
													<li><a href="">专利权的终止</a></li>
													<li><a href="">权利的恢复</a></li>
													<li><a href="">专利权的无效宣告</a></li>
													<li><a href="">维持专利权有效</a></li>
													<li><a href="">专利权的撤销</a></li>
													<li><a href="">专利权有效期续展</a></li>
													<li><a href="">保密专利的解密</a></li>
													<li><a href="">专利实施的强制许可</a></li>
													<li><a href="">专利申请权、专利权的转移</a></li>
													<li><a href="">专利（申请）权的质押、保全及解除</a></li>
													<li><a href="">专利实施许可合同的备案</a></li>
													<li><a href="">文件的公告送达</a></li>
													<li><a href="">变更</a></li>
													<li><a href="">更正</a></li>
													<li><a href="">修改</a></li>
													<li><a href="">其他有关事项</a></li>
												</ul>
											</div></td>
									</tr>
									<tr>
										<td>法律状态公告日</td>
									</tr>
									<tr>
										<td><div class="input" style="width: 40%">
												<input type="hidden" name="rs[2].field" value="LSND" /><input type="hidden" name="rs[2].type" value="date" />
												<ul>
													<li class="i-1"><input type="text" name="rs[2].start" onclick="WdatePicker()" /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[2].end" onclick="WdatePicker({maxDate:'%y-%M-%d'})" /></li>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>分类</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>外观设计国际分类号（DC）</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[11].field" value="DC" /> <input type="text" name="s[11].value" />
											<div class="hint">
												<p>国际外观设计分类号用于对外观设计专利进行分类。国际外观设计分类号仅适用于外观设计专利文献检索。国际外观设计分类号可通过《国际外观设计分类表》获得。支持模糊检索，模糊部分使用*代替，当模糊部分位于分类号尾部时，*可以省略，多个国际外观设计分类号之间的AND、OR、NOT运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索国际外观设计分类号12-08，应键入：12-08</li>
													<li>检索国际外观设计分类号12大类，应键入：12*或12</li>
													<li>国际外观设计分类号为12-06或12-08，应键入：12-06 OR 12-08</li>
													<li>检索国际外观设计分类号12大类，但不包含12-01、12-02，应键入：12* NOT (12-01 OR 12-02)</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>主IPC分类号（MIPC）</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[12].field" value="MIPC" /> <input type="text" name="s[12].value" />
											<div class="hint">
												<p>国际专利分类号是由国际知识产权组织制定的一种专利技术分类代码，国际专利分类号仅适用于发明和实用新型专利文献检索。国际专利分类号可通过《国际专利分类表》获得。支持模糊检索，模糊部分使用*代替，当模糊部分位于分类号尾部时，*可以省略，多个国际专利分类号之间的AND、OR、NOT运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索国际专利分类号B60S5/02，应键入：B60S5/02</li>
													<li>检索国际专利分类B60S5大组，应键入：B60S5</li>
													<li>检索国际专利分类B60F小类，应键入：B60F*或B60F</li>
													<li>国际专利分类号中包含H01M10/44和B60L11/18，应键入：H01M10/44 AND B60L11/18</li>
													<li>国际专利分类号中包含H01M10/00或H01M2/10，应键入：H01M10/00 OR H01M2/10</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>CPC专利分类号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[13].field" value="CPC" /> <input type="text" name="s[13].value" />
											<div class="hint">
												<p>CPC专利分类号是由欧洲专利局和美国专利局共同制定的一种专利技术分类代码，仅适用于欧洲、美国的发明和实用新型专利文献检索或有欧洲、美国同族的专利文献检索。与国际专利分类号相比，CPC专利分类号的分类更细。CPC专利分类号可通过《CPC专利分类号表》获得。CPC专利分类号字段支持模糊检索，模糊部分使用*代替，当模糊部分位于分类号尾部时，*可以省略，多个国际专利分类号之间的and、or、not运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索国际专利分类号B60S5/02，应键入：B60S5/02</li>
													<li>检索国际专利分类B60S5大组，应键入：B60S5</li>
													<li>检索国际专利分类B60F小类，应键入：B60F*或B60F</li>
													<li>国际专利分类号中包含H01M10/44和B60L11/18，应键入：H01M10/44 AND B60L11/18</li>
													<li>国际专利分类号中包含H01M10/00或H01M2/10，应键入：H01M10/00 OR H01M2/10</li>
												</ol>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>优先权</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>优先申请国</td>
									</tr>
									<tr>
										<td class="mult_sel"><input type="hidden" name="s[14].field" value="PRC" /> <input type="text" name="s[14].value" id="PRC" />
											<div class="hint">
												<ul class="hle_list" id="PRC_1_id">
													<li><a title="中国大陆" href="javascript:void(0);">中国大陆</a></li>
													<li><a title="中国台湾" href="javascript:void(0);">中国台湾</a></li>
													<li><a title="中国香港" href="javascript:void(0);">中国香港</a></li>
													<li><a title="加拿大" href="javascript:void(0);">加拿大</a></li>
													<li><a title="澳大利亚" href="javascript:void(0);">澳大利亚</a></li>
													<li><a title="瑞典" href="javascript:void(0);">瑞典</a></li>
													<li><a title="西班牙" href="javascript:void(0);">西班牙</a></li>
													<li><a title="意大利" href="javascript:void(0);">意大利</a></li>
													<li><a title="美国" href="javascript:void(0);">美国</a></li>
													<li><a title="日本" href="javascript:void(0);">日本</a></li>
													<li>
												</ul>
												<ul class="hle_list pa_list_more none" id="PRC_2_id">
													<a title="英国" href="javascript:void(0);">英国</a>
													</li>
													<li><a title="德国" href="javascript:void(0);">德国</a></li>
													<li><a title="法国" href="javascript:void(0);">法国</a></li>
													<li><a title="瑞士" href="javascript:void(0);">瑞士</a></li>
													<li><a title="韩国" href="javascript:void(0);">韩国</a></li>
													<li><a title="俄罗斯" href="javascript:void(0);">俄罗斯</a></li>
													<li><a title="EPO" href="javascript:void(0);">EPO</a></li>
													<li><a title="WIPO" href="javascript:void(0);">WIPO</a></li>
													<li><a title="阿尔及利亚" href="javascript:void(0);">阿尔及利亚</a></li>
													<li><a title="阿根廷" href="javascript:void(0);">阿根廷</a></li>
													<li><a title="埃及" href="javascript:void(0);">埃及</a></li>
													<li><a title="爱尔兰" href="javascript:void(0);">爱尔兰</a></li>
													<li><a title="爱沙尼亚" href="javascript:void(0);">爱沙尼亚</a></li>
													<li><a title="奥地利" href="javascript:void(0);">奥地利</a></li>
													<li><a title="巴拿马" href="javascript:void(0);">巴拿马</a></li>
													<li><a title="巴西" href="javascript:void(0);">巴西</a></li>
													<li><a title="白俄罗斯" href="javascript:void(0);">白俄罗斯</a></li>
													<li><a title="保加利亚" href="javascript:void(0);">保加利亚</a></li>
													<li><a title="比利时" href="javascript:void(0);">比利时</a></li>
													<li><a title="冰岛" href="javascript:void(0);">冰岛</a></li>
													<li><a title="波兰" href="javascript:void(0);">波兰</a></li>
													<li><a title="波斯尼亚和黑塞哥维那" href="javascript:void(0);">波斯尼亚和黑塞哥维那</a></li>
													<li><a title="丹麦" href="javascript:void(0);">丹麦</a></li>
													<li><a title="东德" href="javascript:void(0);">东德</a></li>
													<li><a title="多米尼加共和国" href="javascript:void(0);">多米尼加共和国</a></li>
													<li><a title="厄瓜多尔" href="javascript:void(0);">厄瓜多尔</a></li>
													<li><a title="非洲地区工业产权组织" href="javascript:void(0);">非洲地区工业产权组织</a></li>
													<li><a title="非洲知识产权组织" href="javascript:void(0);">非洲知识产权组织</a></li>
													<li><a title="菲律宾" href="javascript:void(0);">菲律宾</a></li>
													<li><a title="芬兰" href="javascript:void(0);">芬兰</a></li>
													<li><a title="哥伦比亚" href="javascript:void(0);">哥伦比亚</a></li>
													<li><a title="哥斯达黎加" href="javascript:void(0);">哥斯达黎加</a></li>
													<li><a title="格鲁吉亚" href="javascript:void(0);">格鲁吉亚</a></li>
													<li><a title="古巴" href="javascript:void(0);">古巴</a></li>
													<li><a title="哈萨克斯坦" href="javascript:void(0);">哈萨克斯坦</a></li>
													<li><a title="海湾地区阿拉伯国家合作委员会专利局" href="javascript:void(0);">海湾地区阿拉伯国家合作委员会专利局</a></li>
													<li><a title="荷兰" href="javascript:void(0);">荷兰</a></li>
													<li><a title="黑山共和国" href="javascript:void(0);">黑山共和国</a></li>
													<li><a title="洪都拉斯" href="javascript:void(0);">洪都拉斯</a></li>
													<li><a title="捷克共和国" href="javascript:void(0);">捷克共和国</a></li>
													<li><a title="捷克斯洛伐克" href="javascript:void(0);">捷克斯洛伐克</a></li>
													<li><a title="津巴布韦" href="javascript:void(0);">津巴布韦</a></li>
													<li><a title="克罗地亚" href="javascript:void(0);">克罗地亚</a></li>
													<li><a title="肯尼亚" href="javascript:void(0);">肯尼亚</a></li>
													<li><a title="拉脱维亚" href="javascript:void(0);">拉脱维亚</a></li>
													<li><a title="立陶宛" href="javascript:void(0);">立陶宛</a></li>
													<li><a title="卢森堡" href="javascript:void(0);">卢森堡</a></li>
													<li><a title="罗马尼亚" href="javascript:void(0);">罗马尼亚</a></li>
													<li><a title="马耳他" href="javascript:void(0);">马耳他</a></li>
													<li><a title="马拉维墨西哥" href="javascript:void(0);">马拉维墨西哥</a></li>
													<li><a title="马来西亚" href="javascript:void(0);">马来西亚</a></li>
													<li><a title="蒙古" href="javascript:void(0);">蒙古</a></li>
													<li><a title="秘鲁" href="javascript:void(0);">秘鲁</a></li>
													<li><a title="摩尔多瓦共和国" href="javascript:void(0);">摩尔多瓦共和国</a></li>
													<li><a title="摩洛哥" href="javascript:void(0);">摩洛哥</a></li>
													<li><a title="摩纳哥" href="javascript:void(0);">摩纳哥</a></li>
													<li><a title="墨西哥" href="javascript:void(0);">墨西哥</a></li>
													<li><a title="南非" href="javascript:void(0);">南非</a></li>
													<li><a title="南斯拉夫" href="javascript:void(0);">南斯拉夫</a></li>
													<li><a title="尼加拉瓜" href="javascript:void(0);">尼加拉瓜</a></li>
													<li><a title="挪威" href="javascript:void(0);">挪威</a></li>
													<li><a title="欧亚专利局" href="javascript:void(0);">欧亚专利局</a></li>
													<li><a title="葡萄牙" href="javascript:void(0);">葡萄牙</a></li>
													<li><a title="前苏联" href="javascript:void(0);">前苏联</a></li>
													<li><a title="萨尔瓦多" href="javascript:void(0);">萨尔瓦多</a></li>
													<li><a title="塞尔维亚" href="javascript:void(0);">塞尔维亚</a></li>
													<li><a title="塞浦路斯" href="javascript:void(0);">塞浦路斯</a></li>
													<li><a title="圣马利诺" href="javascript:void(0);">圣马利诺</a></li>
													<li><a title="斯洛伐克" href="javascript:void(0);">斯洛伐克</a></li>
													<li><a title="斯洛文尼亚" href="javascript:void(0);">斯洛文尼亚</a></li>
													<li><a title="塔吉克斯坦" href="javascript:void(0);">塔吉克斯坦</a></li>
													<li><a title="泰国" href="javascript:void(0);">泰国</a></li>
													<li><a title="特立尼达和多巴哥" href="javascript:void(0);">特立尼达和多巴哥</a></li>
													<li><a title="土耳其" href="javascript:void(0);">土耳其</a></li>
													<li><a title="危地马拉" href="javascript:void(0);">危地马拉</a></li>
													<li><a title="乌克兰" href="javascript:void(0);">乌克兰</a></li>
													<li><a title="乌拉圭" href="javascript:void(0);">乌拉圭</a></li>
													<li><a title="乌兹别克斯坦" href="javascript:void(0);">乌兹别克斯坦</a></li>
													<li><a title="希腊" href="javascript:void(0);">希腊</a></li>
													<li><a title="新加坡" href="javascript:void(0);">新加坡</a></li>
													<li><a title="新西兰" href="javascript:void(0);">新西兰</a></li>
													<li><a title="匈牙利" href="javascript:void(0);">匈牙利</a></li>
													<li><a title="亚美尼亚" href="javascript:void(0);">亚美尼亚</a></li>
													<li><a title="以色列" href="javascript:void(0);">以色列</a></li>
													<li><a title="印度" href="javascript:void(0);">印度</a></li>
													<li><a title="印度尼西亚" href="javascript:void(0);">印度尼西亚</a></li>
													<li><a title="越南" href="javascript:void(0);">越南</a></li>
													<li><a title="赞比亚" href="javascript:void(0);">赞比亚</a></li>
													<li><a title="智利" href="javascript:void(0);">智利</a></li>
												</ul>
											</div></td>
									</tr>
									<tr>
										<td>优先申请号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[15].field" value="PRN" /> <input type="text" name="s[15].value" />
											<div class="hint">
												<p>优先申请号指作为优先权基础的在先申请的申请号。支持模糊检索，模糊字符为“*”， “*”可代替单个或多个字符。模糊字符“*”位于末尾时可省略不写。支持or逻辑运算。</p>
												<p>【检索示例】</p>
												<ol>
													<li>输入完整号码，如键入：CN200710146170.0、CN201110093282.0</li>
													<li>输入部分号码，如键入：CN200710146170、CN101100172、200710146170</li>
													<li>检索多个号码时，用or算符连接各号码，如CN200710146170、CN200710096257，应键入：CN200710146170 or CN200710096257</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>优先申请日</td>
									</tr>
									<tr>
										<td><div class="input" style="width: 40%">
												<input type="hidden" name="rs[3].field" value="PRD" /> <input type="hidden" name="rs[3].type" value="date" />
												<ul>
													<li class="i-1"><input type="text" name="rs[3].start" onclick="WdatePicker()" /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[3].end" onclick="WdatePicker()" /></li>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>同族</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>同族专利号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[16].field" value="PFN" /> <input type="text" name="s[16].value" />
											<div class="hint">
												<p>同族专利号指构成专利族的同族专利的公开（公告）号（本平台的专利族是简单专利族，由具有完全相同的优先权的专利文献构成）。</p>
												<p>注意：同族专利号中应该输入专利文献的公开（公告）号，不能输入申请号。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索同族专利包括US4360548A的专利文献，应在同族专利号的输入框中输入：US4360548A</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>同族专利数</td>
									</tr>
									<tr>
										<td><input type="hidden" name="rs[4].field" value="PFN_COUNT" />
											<div class="input" style="width: 40%">
												<ul>
													<li class="i-1"><input type="text" name="rs[4].start" /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[4].end" /></li>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2 more" style="display: none;">
					<div class="lists">
						<div class="title">
							<h3>引证</h3>
						</div>
						<div class="wrap">
							<table>
								<tbody>
									<tr>
										<td>引证专利号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[17].field" value="CTN" /> <input type="text" name="s[17].value" />
											<div class="hint">
												<p>所谓专利引证是指一件专利文献引用其他专利文献或被其他专利文献引用的情况。</p>
												<p>引证专利号检索出的结果是引证了被检索公开（公告）号的专利文献。</p>
												<p>注意：引证专利号中应该输入专利文献的公开（公告）号，不能输入申请号。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索引证了US4360548A的专利文献，应在引证专利号的输入框中输入：US4360548A</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>被引证专利号</td>
									</tr>
									<tr>
										<td><input type="hidden" name="s[18].field" value="CDN" /> <input type="text" name="s[18].value" />
											<div class="hint">
												<p>所谓专利引证是指一件专利文献引用其他专利文献或被其他专利文献引用的情况。</p>
												<p>引证专利号检索出的结果是引证了被检索公开（公告）号的专利文献。</p>
												<p>注意：引证专利号中应该输入专利文献的公开（公告）号，不能输入申请号。</p>
												<p>【检索示例】</p>
												<ol>
													<li>检索引证了US4360548A的专利文献，应在引证专利号的输入框中输入：US4360548A</li>
												</ol>
											</div></td>
									</tr>
									<tr>
										<td>被引证次数</td>
									</tr>
									<tr>
										<td><input type="hidden" name="rs[5].field" value="CDN_COUNT" />
											<div class="input" style="width: 40%">
												<ul>
													<li class="i-1"><input type="text" name="rs[5].start" /></li>
													<li class="i-2">至</li>
													<li class="i-3"><input type="text" name="rs[5].end" /></li>
												</ul>
											</div></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div class="block2">
					<button type="button" style="width: 68%" id="more_btn">显示更多检索条件</button>
				</div>
				<!--End block2-->
				<!--Begin submit1-->
				<div class="submit1 submitBG1">
					<input type="button" value="搜 索" onclick="check_submit();" class="btn1" /> <input type="reset" value="清 除" class="btn2" />
				</div>
				<!--End submit1-->
			</div>
		</div>
	</div>
	<input type="hidden" name="searchType" value="table" />
	<input type="hidden" id="domain" name="domain" value="" /> 
	<input type="hidden" id="path" name="path" value="" />
	<input type="hidden" name="tName" id="tName" />
</form>