<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
<!---------------Begin right_sider--------------->
<div class="right_sider">
  <div class="container">
    <div class="nav"><img src="<%=theme%>/images/search3.png"  alt=""/> <a href="#">专利检索</a> &gt; <span>智能检索</span></div>
    <div class="search">
      <div class="wrap">
        <div class="search_input">
          <input type="text" placeholder="Search...">
          <div class="btns">
            <input type="button" class="btn1" value="Search">
          </div>
        </div>
      </div>
    </div>
    <div class="block1">
      <div class="lists">
        <div class="title">
          <h3><img src="<%=theme%>/images/history.png" alt=""/> 检索历史</h3>
        </div>
        <table>
          <tbody>
            <tr>
              <th>检索表达式</th>
              <th>概念检索 </th>
              <th>同义词</th>
            </tr>
            <tr>
              <td>a&gt;b</td>
              <td>jade</td>
              <td>jaden</td>
            </tr>
            <tr>
              <td>c=d</td>
              <td>mile</td>
              <td>miles</td>
            </tr>
          </tbody>
        </table>
        <div class="pages"> <a href="">首页</a> <a href="">&lt;</a><a href="#" class="num cur">1</a><a href="#" class="num">2</a><a href="">&gt;</a><a href="">最后</a> </div>
      </div>
    </div>
  </div>
</div>
<!---------------End right_sider--------------->