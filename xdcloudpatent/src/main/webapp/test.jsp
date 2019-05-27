<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="<%=path %>/js/jquery/jquery-1.12.4.min.js"
	type="text/javascript"></script>
<script type="text/javascript" src="<%=path %>/js/search.js"></script>
<script type="text/javascript"
	src="<%=path %>/js/jquery/jquery.cookie.js"></script>
	<script type="text/javascript"
	src="<%=path %>/js/jquery-ui/jquery-ui.js"></script>
<script src="<%=path %>/js/jquery/jquery.PrintArea.js"></script>
</head>
<body>
  <div style="border: solid 2px #999fff; float: left; padding: 5px; margin-bottom: 5px;">
    <div style="text-align: center;">
        <span style="font-weight: bold;">Contains content that I want to print</span>
        <br>
        This is just a sample page to demonstrate this plugin.
    </div>
    <div class="PrintArea area1 all" id="Retain">
        <div>Print Area 1</div>
        <span style="color: #000777;">print me</span>
        <span class="test">xyz</span>
        <br>
        <input name="chkTest1" value="1" type="checkbox">
        <input name="chkTest2" value="2" type="checkbox">
        <br>
        <input value="3" name="rdoTest3" type="radio">
        <input value="4" name="rdoTest3" type="radio">
        <br>
        <input value="" name="textTest4" type="">
        <input value="" name="textTest5" type="text">
        <br>
        <select name="selTest6">
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="C">C</option>
        </select>
        <select name="selTest7" multiple="">
            <option value="a">a</option>
            <option value="b">b</option>
            <option value="c">c</option>
        </select>
        <br>
        <textarea name="textareaTest8"></textarea>
        <br>
        <img src="FoesEnd.jpg" id="img2" alt="test" width="200">
    </div>

    <div style="border: solid 1px #999; margin: 20px 5px; padding: 20px;">
        I don't want this to print.
    </div>

    <div class="PrintArea area2 all" style="border-color: #999;">
        <div>Print Area 2</div>
        <span style="color: #000777;">print me</span>
        <span class="test">xyz</span>
        <br>
        <input name="chkTest3" value="1" type="checkbox">
        <input name="chkTest4" value="2" type="checkbox">
        <br>
        <input value="3" name="rdoTest4" type="radio">
        <input value="4" name="rdoTest4" type="radio">
        <br>
        <input value="" name="textTest6" type="text">
        <br>
        <select name="selTest7">
            <option value="A">A</option>
            <option value="B">B</option>
            <option value="C">C</option>
        </select>
        <select name="selTest8" multiple="">
            <option value="a">a</option>
            <option value="b">b</option>
            <option value="c">c</option>
        </select>
        <br>
        <textarea name="textareaTest9"></textarea>
        <br>
        <img src="FoesEnd.jpg" id="img2" alt="test" width="200">
    </div>
  </div>

  <div class="testDialog">
    <div class="PrintArea area3 all">
        This is Print Area 3
    </div>
  </div>

  <div style="border: solid 2px #999fff; float: left; margin-left: 20px;" class="SettingsBox">
    <div style="width: 400px; padding: 20px;">
        <div style="padding: 0 10px 10px;" class="buttonBar">
          <div class="button b1">Print</div>
          <div class="toggleDialog">open dialog</div>
        </div>

        <div style="font-weight: bold; border-top: solid 1px #999fff; padding-top: 10px;">Settings</div>
        <table>
          <tbody>
          <tr>
            <td><input value="popup" name="mode" id="popup" checked="" type="radio"> Popup</td>
          </tr>
          <tr>
            <td style="padding-left: 20px;"><input value="popup" name="popup" id="closePop" type="checkbox"> Close popup</td>
          </tr>
          <tr>
            <td><input value="iframe" name="mode" id="iFrame" type="radio"> IFrame</td>
          </tr>
          <tr>
            <td>Extra css: <input type="text" name="extraCss" size="50" /></td>
          </tr>
          <tr>
            <td><div class="settingName">Print area:</div>
              <div class="settingVals">
                <input type="checkbox" class="selPA" value="area1" checked /> Area 1<br>
                <input type="checkbox" class="selPA" value="area2" checked /> Area 2<br>
                <input type="checkbox" class="selPA" value="area3" checked /> Area 3<br>
              </div>
            </td>
          </tr>
          <tr>
            <td><div class="settingName">Retain Attributes:</div>
              <div class="settingVals">
                <input type="checkbox" checked name="retainCss"   id="retainCss" class="chkAttr" value="class" /> Class
                <br>
                <input type="checkbox" checked name="retainId"    id="retainId"  class="chkAttr" value="id" /> ID
                <br>
                <input type="checkbox" checked name="retainStyle" id="retainId"  class="chkAttr" value="style" /> Style
              </div>
            </td>
          </tr>
          <tr>
            <td><div style="padding: 3px; border: solid 1px #ddd;">Add to head :
                    <input type="checkbox" checked name="addElements" id="addElements" class="chkAttr" />
                    <pre>&lt;meta charset="utf-8" /&gt;<br>&lt;http-equiv="X-UA-Compatible" content="IE=edge"/&gt;</pre>
                </div>
            </td>
          </tr>
        </tbody></table>
    </div>
  </div>

  <script>
    $(document).ready(function(){
       /*  var dialog = $("div.testDialog").dialog({position : { my : "left top", at : "left bottom+50", of : ".SettingsBox" }, width: "600", title: "Print Dialog Box Contents"});
        $(".toggleDialog").click(function(){
            dialog.dialog("open");
        }); */
        $("div.b1").click(function(){
            var mode = $("input[name='mode']:checked").val();
            var close = mode == "popup" && $("input#closePop").is(":checked");
            var extraCss = $("input[name='extraCss']").val();
            var print = "";
            $("input.selPA:checked").each(function(){
                print += (print.length > 0 ? "," : "") + "div.PrintArea." + $(this).val();
            });
            var keepAttr = [];
            $(".chkAttr").each(function(){
                if ($(this).is(":checked") == false )
                    return;
                keepAttr.push( $(this).val() );
            });
            alert(print);
            var headElements = $("input#addElements").is(":checked") ? '<meta charset="utf-8" />,<meta http-equiv="X-UA-Compatible" content="IE=edge"/>' : '';
            var options = { mode : mode, popClose : close, extraCss : extraCss, retainAttr : keepAttr, extraHead : headElements };
            $( print ).printArea( options );
        });
        $("input[name='mode']").click(function(){
            if ( $(this).val() == "iframe" ) $("#closePop").attr( "checked", false );
        });
    });
  </script>
</body>
</html>