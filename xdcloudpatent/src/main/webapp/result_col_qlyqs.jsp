<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<% 
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
String theme=path+"/"+"theme/default";
%>
<style type="text/css">
</style>
<script type="text/javascript">

</script>
<!--Begin Llist-->
<div class="Llist">
<!-- 	权利要求书CLM -->
	<div class="subtt"><a href="javascript:;">权利要求</a></div>
	<input type="hidden" id="pdf" value="${doc.PN }"/>
              <div class="contents" style="text-align: left">
                ${doc.CLM }
                <table class="table2">
<!--                   <thead> -->
<!--                     <tr> -->
<!--                       <th>NO.</th> -->
<!--                       <th>权利要求</th> -->
<!--                     </tr> -->
                  </thead>
                  <!-- <tbody>
                    <tr>
                      <td>1</td>
                      <td>     一种固相化去 SUMO 化系统，其特征在于，所述固相化去 SUMO 化系统包括固相化的SUMO 水解酶。</td>
                    </tr>
                    <tr>
                      <td>2</td>
                      <td>     如权利要求 1 所述的固相化去 SUMO 化系统，其特征在于，所述 SUMO 水解酶的序列选自：(1)SEQ ID NO ： 5 ；或 SEQ ID NO ： 6 ；或(2) 在 (1) 限定的氨基酸序列中经过取代、缺失或添加一个或几个氨基酸且具有 SUMO水解酶活性的由 (1) 衍生的蛋白质或多肽。</td>
                    </tr>
                    <tr>
                      <td>3</td>
                      <td>     如权利要求 1 所述的固相化去 SUMO 化系统，其特征在于，所述 SUMO 水解酶固相化为通过亲和标签的连接将所述 SUMO 水解酶吸附在与亲和标签有亲和结合作用的介质上。</td>
                    </tr>
                    <tr>
                      <td>4</td>
                      <td>     如权利要求 3 所述的固相化 SUMO 化系统，其特征在于，所述亲和标签选自但不仅限于以下标签：Chitin binding domain(ChBD)，AviTag，Glutathione-S-Transferase(GST)，His6hexapeptide，calmodulin binding protein(CBP)，protein C tag，hemagglutinin(HA)tag，aFLAGtag 和 Myctag。</td>
                    </tr>
                  </tbody> -->
                </table>
              </div>
            </div>
	
</div>
<!--End Llist-->
