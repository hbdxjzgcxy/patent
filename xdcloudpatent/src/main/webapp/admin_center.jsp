<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.format.DateTimeFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/mytags.jsp"%>
<style>
.block7 .Lsider {
	width: 100%;
}

body {
	text-align: left;
}
</style>
<div class="right_sider">
<div class="Rwrap">
  <div class="container">
<%--     <div class="nav" style="margin-bottom:15px;"><img src="<%=theme%>/images/search3.png"  alt=""/> <a href="#">专利检索</a> &gt; <span>智能检索</span> </div> --%>
    <!--Begin block7-->
    <div class="block7"> 
      <!--Begin Lsider-->
      <div class="Lsider">
        <div class="Lwrap"> 
          <!--Begin intro-->
          <div class="intro"> 
            <!--Begin left-->
            <div class="left">
              <div class="wrap">
                <div class="img"><img src="resources/photo.html" alt="" onerror="errorImg(this)"/></div>
                <div class="txts"> <span class="T1">你好<i>&nbsp;&nbsp;${user.username }</i></span> <span class="T2">用户状态：<i>正常</i></span> <span class="T3">系统时间<i>&nbsp;&nbsp;<%=DateTime.now().toString(DateTimeFormat.forPattern("yyyy-MM-dd hh:mm:ss")) %></i></span> <span class="T4">
                  <!-- <input type="button" class="btns btn1" value="用户充值">
                  <input type="button" class="btns btn2" value="用户认证"> -->
                  </span> </div>
              </div>
            </div>
            <!--End left--> 
            <!--Begin right-->
            <div class="right">
              <div class="wrap">
                <div class="txts">
                  <table>
                    <tbody>
<!--                       <tr> -->
<!--                         <td class="TD1">你的权限：</td> -->
<!--                         <td class="TD2">&nbsp;</td> -->
<!--                       </tr> -->
                      <tr>
                        <td class="TD1">空间容量：</td>
                        <td class="TD2"><span class="T1">${user.role.space }</span>M，已用<span class="T2">${space }</span>M</td>
                      </tr>
                      <tr>
                        <td class="TD1">可建专题库数量：</td>
                        <td class="TD2"><span class="T1">${user.role.dbCount }</span>个，已建<span class="T2">${dbCount }</span>个</td>
                      </tr>
<!--                       <tr> -->
<!--                         <td class="TD1">可创建子账号：</td> -->
<!--                         <td class="TD2"><span class="T1">10</span>个，已建<span class="T2">3</span>个</td> -->
<!--                       </tr> -->
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            <!--End right-->
            <div class="clear"></div>
          </div>
          <!--End intro--> 
          <!--Begin lists-->
          <div class="lists">
            <div class="title">
              <h3><img src="<%=theme%>/images/database2.png"  alt=""/> 我的专题数据库</h3>
            </div>
            <ul>
            
            <c:forEach items="${voList}" var="view">
              <li>
                <div class="txts1">
                  <dl>
                    <dt>${view.db.alias }</dt>
                    <dd>
                      <span class="T1">磁盘占用：<i>${view.space}</i><b>M</b></span> <span class="T1">专利条目：<i>${view.patentCount }</i><b>条</b></span> </dd>
                  </dl>
                </div>
                <div class="txts2">
                  <dl>
                    <dt>专题下用户</dt>
                    <dd>
                      <h4><a href="">${view.db.user.username }</a></h4>
                      <span class="T1">登录次数：<i>${view.loginCount }</i> <b>次</b></span> <span class="T1">最近登录时间：<i><fmt:formatDate value="${view.loginEndTime}" type="both"/></i></span> </dd>
                  </dl>
                </div>
                <div class="clear"></div>
              </li>
              </c:forEach>
            </ul>
          </div>
          <!--End lists--> 
        </div>
      </div>
      <!--End Lsider--> 
      <!--Begin Rsider-->
      <div class="Rsider"> 
        <!--Begin lists1
        <div class="lists1">
          <ul>
            <li> <a href="">
              <div class="txts"><span class="T1">轻松搞定自建数据库3大顽疾</span><span class="T2">服务器/网络安全，一键搞定，显5折</span></div>
              <div class="img"><img src="<%=theme%>/images/temp/7.jpg"  alt=""/></div>
              <div class="clear"></div>
              </a> </li>
            <li> <a href="">
              <div class="txts"><span class="T1">轻松搞定自建数据库3大顽疾</span><span class="T2">服务器/网络安全，一键搞定，显示5折</span></div>
              <div class="img"><img src="<%=theme%>/images/temp/7.jpg"  alt=""/></div>
              <div class="clear"></div>
              </a> </li>
          </ul>
        </div>-->
        <!--End lists1--> 
        <!--Begin lists2
        <div class="lists2">
          <div class="title">
            <h3><img src="<%=theme%>/images/product.png"  alt=""/> 新产品发布会</h3>
          </div>
          <ul>
            <li class="i-1"> <a href="">
              <div class="txts"><span class="T1">数据传输</span><span class="T2">支持不停服迁移，提供数据实时同步分发能力，让数据传输无边界</span></div>
              </a> </li>
            <li class="i-2"> <a href="">
              <div class="txts"><span class="T1">容器服务</span><span class="T2">应用全生命周期管理的Docker服务</span></div>
              </a> </li>
            <li class="i-3"> <a href="">
              <div class="txts"><span class="T1">先知计划</span><span class="T2">私密的安全众测服务，按效果付费</span></div>
              </a> </li>
            <li class="i-4"> <a href="">
              <div class="txts"><span class="T1">安骑士</span><span class="T2">企业级安全运维管理平台，0day漏洞修复，日程安全巡检</span></div>
              </a> </li>
            <li class="i-5"> <a href="">
              <div class="txts"><span class="T1">姿态感知</span><span class="T2">全球唯一可定位黑客姓名的安全大数据分析平台</span></div>
              </a> </li>
            <li class="i-6"> <a href="">
              <div class="txts"><span class="T1">物联网套件</span><span class="T2">助您快速搭建稳定可靠的物联网套件</span></div>
              </a> </li>
            <div class="clear"></div>
          </ul>
        </div>-->
        <!--End lists2--> 
      </div>
      <!--End Rsider-->
      <div class="clear"></div>
    </div>
    <!--End block7--> 
    </div>
  </div>
</div>
<script type="text/javascript">

//图像加载出错时的处理
    function errorImg(img) {
        img.src = "<%=theme%>/images/default.jpg";
        img.onerror = null;
    }

</script>