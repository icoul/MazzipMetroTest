<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
    .map_wrap {position:relative;width:100%;height:350px;}
    .title {font-weight:bold;display:block;}
    .hAddr {position:absolute;left:10px;top:10px;border-radius: 2px;background:#fff;background:rgba(255,255,255,0.8);z-index:1;padding:5px;}
    #centerAddr {display:block;margin-top:2px;font-weight: normal;}
    .bAddr {padding:5px;text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
</style>

<c:if test="${not empty nameList}">
	<div id="map" style="width: 100%; height: 350px;"></div>
	<br/> 
</c:if>
<!--  ddd -->
<script type="text/javascript" src="//apis.daum.net/maps/maps3.js?apikey=0d211626a8ca667e54b95403a7ae692f&libraries=services"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery-2.0.0.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		
		$("#searchWord").val("${name}");
		
		$(".restName").bind("click", function(){ // 업장명을 클릭하면 지도에서 보여주는 함수
			var restName = $(this).val();
			showAddr(restName);
		});
	});
	
	function goMove() { // 업장을 선택했고 이제 데이터를 이전 페이지로 모두 넘겨주는 함수
		var name = $("#name").val();
		var addr = $("#addr").val();
		var newAddr = $("#newAddr").val();
		var phone = $("#phone").val();
		var seq = $("#seq").val();
		
		var phoneSplit = phone.split('-');
		
		var frm = opener.window.document.registerFrm; 
        frm.name.value = name;
        frm.addr.value = addr;
        frm.newAddr.value = newAddr;
		frm.phone.value = phone;
		frm.seq.value = seq;
        
		self.window.close(); // 팝업창 닫기 
	}
	
	function goSearch(){ // 팝업창 상태에서 업장명을 검색하는 함수
		var searchFrm = document.searchFrm;
		searchFrm.submit();
	}
	
	function goInput(name, addr, newAddr, phone, seq){ // 선택한 업장의 정보를 input hidden에 담아주는 함수
		$("#name").val(name);
		$("#addr").val(addr);
		$("#newAddr").val(newAddr);
		$("#phone").val(phone);
		$("#seq").val(seq);
	}
	
	function goRegister(name){ // 업장이 존재하지 않아서 직접 등록하겠다는 함수
		location.href = "<%= request.getContextPath()%>/notRestRegi.eat?name=" + name;
	}

<!-- 주소입력으로 장소 검색 -->
	
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	mapOption = {
		center : new daum.maps.LatLng(37.518517, 126.984507), // 지도의 중심좌표 : 서빙고 골프연습장
		level : 5
	// 지도의 확대 레벨
	};

	// 지도를 생성합니다    
	var map = new daum.maps.Map(mapContainer, mapOption);

	function showAddr(keyword){
		var map = new daum.maps.Map(mapContainer, mapOption);
		// 주소-좌표 변환 객체를 생성합니다
		var geocoder = new daum.maps.services.Geocoder();
		
		// 주소로 좌표를 검색합니다
		geocoder.addr2coord(keyword, function(status, result) {
							
							// 정상적으로 검색이 완료됐으면 
							if (status === daum.maps.services.Status.OK) {

								var coords = new daum.maps.LatLng(
										result.addr[0].lat, result.addr[0].lng);

								// 결과값으로 받은 위치를 마커로 표시합니다
								var marker = new daum.maps.Marker({
									map : map,
									position : coords
								});

								// 인포윈도우로 장소에 대한 설명을 표시합니다
								var infowindow = new daum.maps.InfoWindow(
										{
											content : '<div style="width:150px;text-align:center;padding:6px 0;">'+keyword+'</div>'
										});
								infowindow.open(map, marker);

								// 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
								map.setCenter(coords);
								
								searchDetailAddrFromCoords(coords.latLng, function(){
									alert(coords.latLng);
									
								});
							}
						});
	}
	
</script>

</head>

<span style="font-size: 10pt;">가게명 '${name}'의 검색결과입니다</span>

<form name = "searchFrm" action = "restCheck.eat" method = "get">
	<input type = "text" name = "name" id = "searchWord" value = "" />&nbsp;
	<button type = "button" onClick = "goSearch();">검색</button>&nbsp;
	<c:if test="${not empty nameList}">
		<button type = "button" onClick="goMove();">확인</button>
	</c:if>
</form>

<div align="center">

<table width="98%" class="outline">
	<c:if test="${empty nameList}">
	  <tr>
    	<td colspan="3" align="center">
    		<span style="color: red; font-weight: bold;">
    			검색결과가 존재하지 않습니다 <br/>
    			새 가게로 등록하시려면 등록버튼을 눌러주세요
    		</span>
    	</td>  	
      </tr>
      <button type = "button" onClick="goRegister('${name}');">등록</button>
	</c:if>
	
	<c:if test="${not empty nameList}">
		<tr>
	    	<td>찾으시는 업장을 클릭하시고 맞으면 확인을 눌러주세요</td>
	    </tr>
	  <c:forEach var="list" items="${nameList}" varStatus="status">
	    <tr>
      	   <td onClick="goInput('${list.restName}', '${list.restAddr}', '${list.restNewAddr}', '${list.restPhone}', '${list.restSeq}');">
	      	 <input type = "radio" id = "restName${status.index}" name = "restName" class = "restName" value = "${list.restAddr}" />
		       <label for = "restName${status.index}">
		      	 <span style = "cursor: pointer;">${list.restName}</span>
	      	   </label>
	      	    <br/>
      	   </td>
        </tr>
	  </c:forEach>
	</c:if>
</table>
<c:if test="${not empty nameList}">
	<div>${html}</div>
</c:if>
	<input type = "hidden" id = "name" value="" />
	<input type = "hidden" id = "addr" value="" />
	<input type = "hidden" id = "newAddr" value="" />
	<input type = "hidden" id = "phone" value="" />
	<input type = "hidden" id = "seq" value="" />
</div>

</body>
</html>