	<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	<%@include file="/mytags.jsp" %>
<!--Begin Llist-->
            <div class="Llist">
            <input type="hidden" id="pdf" value="${doc.PN }"/>
              <div class="tt">${doc.TI } <span>${doc.PN }</span></div>
              <div class="subtt"><a href="javascript:;">摘要</a></div>
              <div class="contents">
                <div class="txts">${doc.AB }</div>
              </div>
              <div class="subtt"><a href="javascript:;">著录项目</a></div>
              <div class="contents">
                <table class="table1">
                  <tbody>
	                  <c:if test="${!empty doc.AN && doc.AN ne 'null' }">
		                    <tr>
		                      <th style="min-width:150px;">申请号</th>
		                      <td>${doc.AN }</td>
		                    </tr>
	                  </c:if>
	                <c:if test="${!empty doc.AD && doc.AD ne 'null' }">
	                    <tr>
	                      <th>申请日</th>
	                      <td>${fn:substring(doc.AD,0,8)}</td>
	                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.PN && doc.PN ne 'null' }">
	                    <tr>
	                      <th>公开（公告）号</th>
	                      <td>${doc.PN }</td>
	                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.PD && doc.PD ne 'null' }">
                    <tr>
                      <th>公开（公告）日</th>
                      <td>${fn:substring(doc.PD,0,8)}</td>
                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.PA && doc.PA ne 'null' }">
                    <tr>
                      <th>申请（专利权）人</th>
                      <td>${doc.PA}</td>
                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.PR && doc.PR ne 'null'}">
	                    <tr>
	                      <th>优先权</th>
	                      <td>${doc.PR}</td>
	                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.AU && doc.AU ne 'null' }">
                    <tr>
                      <th><!-- <img src="images/arrow_right.png" alt=""/> --> 发明（设计）人</th>
                      <td><div>${doc.AU}</div>
                       </td>
                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.IPC && doc.IPC ne 'null' }">
                    <tr>
                      <th><!-- <img src="images/arrow_right.png" alt=""/> --> 国际专利分类号</th>
                      <td><div>${doc.IPC}</div>
                      </td>
                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.CPC && doc.CPC ne 'null' }">
                     <tr>
                      <th><!-- <img src="images/arrow_right.png" alt=""/> --> CPC分类号</th>
                      <td><div>${doc.CPC}</div>
                      </td>
                    </tr>
                    </c:if>
<!--                     <tr> -->
<!--                       <th><img src="images/arrow_right.png" alt=""/> 国际专利分类号(8)</th> -->
<!--                       <td><div><a title="" href="javascript:void(0);" onclick="docsearchfromsub('IPCR=(C12N11/12)');"></a></div> -->
<!--                        </td> -->
<!--                     </tr> -->
                   <c:if test="${!empty doc.ADDR && doc.ADDR ne 'null'}">
	                   	 <tr>
	                      <th>地址</th>
	                      <td>${doc.ADDR }</td>
	                    </tr>
                   </c:if>
                   <c:if test="${!empty doc.PC && doc.PC ne 'null' }"> 
	                    <tr>
	                      <th>国省代码</th>
	                      <td>${doc.PC }</td>
	                    </tr>
                    </c:if> 
                    <c:if test="${!empty doc.AGC && doc.AGC ne 'null'}">
	                    <tr>
	                      <th>专利代理机构</th>
	                      <td>${doc.AGC }</td>
	                    </tr>
                    </c:if>
                    <c:if test="${!empty doc.AGT && doc.AGT ne 'null' }">
	                    <tr>
	                      <th>代理人</th>
	                      <td>${doc.AGT }</td>
	                    </tr>
                    </c:if>
                   <%--  <c:if test="${!empty doc.OAN && doc.OAN ne 'null' }">
	                    <tr>
	                      <th>分案申请</th>
	                      <td></td>
	                    </tr>
                    </c:if> --%>
                    <c:if test="${doc.extKey1 ne '' and doc.extValue1 ne ''  }">
	                    <tr>
	                      <th>${doc.extKey1}</th>
	                      <td>${doc.extValue1}</td>
	                    </tr>
                    </c:if>
                    <c:if test="${doc.extKey2 ne '' and doc.extValue2 ne ''  }">
	                    <tr>
	                      <th>${doc.extKey2}</th>
	                      <td>${doc.extValue2}</td>
	                    </tr>
                    </c:if>
                    <c:if test="${doc.extKey3 ne '' and doc.extValue3 ne ''  }">
	                    <tr>
	                      <th>${doc.extKey3}</th>
	                      <td>${doc.extValue3}</td>
	                    </tr>
                    </c:if>
                  </tbody>
                </table>
              </div>
            <!--End Llist--> 