<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<!DOCTYPE html >
<html>
<head>
<meta charset="UTF-8">
<title>나의 문의내역</title>
<style type="text/css">
	.title-label{font-weight:400;font-size:24px;}
	.navbar-barnd{vertical-align : middle;line-height:45px;}
	.btn-primary.create {margin-top:10px;}
</style>
<script type="text/javascript" src="<%= request.getContextPath() %>/resources/js/jquery-2.0.0.js"></script>  
  <link rel="stylesheet" href="<%= request.getContextPath() %>/resources/BootStrapStudy/css/bootstrap.min.css">
  <script src="<%= request.getContextPath() %>/resources/BootStrapStudy/js/bootstrap.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		<c:if test="${not empty qnaSearch}">
			$("#qnaColName").val("${qnaColName}");
			$("#qnaSearch").val("${qnaSearch}");
		</c:if>		
		
		<c:if test="${not empty qnaInquiry}">
		$("#qnaInquiry").val("${qnaInquiry}");
		</c:if>
		
		$("#qnaRegYearStart").val("${qnaRegYearStart}");
		$("#qnaRegMonthStart").val("${qnaRegMonthStart}");
		$("#qnaRegDayStart").val("${qnaRegDayStart}");
		
		$("#qnaRegYearEnd").val("${qnaRegYearEnd}");
		$("#qnaRegMonthEnd").val("${qnaRegMonthEnd}");
		$("#qnaRegDayEnd").val("${qnaRegDayEnd}");
		
		$("#qnaProgress").val("${qnaProgress}");
	});



	function goSearchFrm(){
		var qnaSearchFrm = document.qnaSearchFrm;
		qnaSearchFrm.submit();
	}
	
	function openWin(src){
		window.open(src,"팝업창이름(의미없음)", "width=" + 650 + ", height=" + 550 + ", left=100px, top=100px, menubar=no, status=no, scrollbars=no");
		
	}
</script>
</head>
<body>
<div class="container">
	<div class="row">
		<h2> <span class="title-label">나의 문의내역</span>  </h2>
		<span>총 문의건 : ${totalCount}건 | 접수완료 : ${registerQnaCount}건 | 답변완료 : ${answerQnaCount}건</span>
        <nav class="navbar navbar-default query" role="query">
            <div class="container-fluid">
          	 <form name="qnaSearchFrm" style="display:inline;" action="<%=request.getContextPath()%>/myQnaList.eat" method="get">
                <table class="table table-bordered">
				      <tr>
				        <th>문의접수일</th>
				        <td colspan="5">
							<select name="qnaRegYearStart" id="qnaRegYearStart">
								${strRegDateYearSelect }
						    </select>
						    년
						    
						    <select name="qnaRegMonthStart" id="qnaRegMonthStart">
						    	${strRegDateMonthSelect}
						    </select>
						    월
						    
						    <select name="qnaRegDayStart" id="qnaRegDayStart">
							${strRegDateDaySelect }
						    </select>
						    일 ~ 
						    
						    <select name="qnaRegYearEnd" id="qnaRegYearEnd">
								${strRegDateYearSelect }
						    </select>
						    년
						    
						    <select name="qnaRegMonthEnd" id="qnaRegMonthEnd">
							${strRegDateMonthSelect}
						    </select>
						    월
						    
						    <select name="qnaRegDayEnd" id="qnaRegDayEnd">
							${strRegDateDaySelect }
						    </select>
						    일
						</td>

				      </tr>
				 
				      <tr>
				      	 
				        <th>문의 유형</th>
				        <td>
				         <select name="qnaInquiry" id="qnaInquiry">
				         	<option value="전체">전체</option>
							<option value="회원">회원관련문의</option>
							<option value="사업주">사업주관련문의</option>
							<option value="음식점">음식점문의</option>
							<option value="기타">기타문의</option>
						  </select>
						</td>
				        
					<th>문의 상세검색</th>
						<td> 
						<select name="qnaColName" id="qnaColName">
							<option value="userName">이름</option>
							<option value="qnaSubject">제목</option>
						 </select> 
						  <input name="qnaSearch" id="qnaSearch" type="text"/> &nbsp; <button type="button" onClick="javascript:goSearchFrm();">조회</button>
						  </td>
						  
						  <th>처리상태</th>
						  <td>
						  <select name="qnaProgress" id="qnaProgress">
				         	<option value="전체">전체</option>
							<option value="접수완료">접수완료</option>
							<option value="답변완료">답변완료</option>
						  </select>
						  </td>
						  
				      </tr>
				   
				  </table>
                </form>
            </div>
        </nav>
        
        <div class="table-responsive col-md-12">
            <table class="table table-striped table-hover">
                <thead>
                    <tr>
                        <th>NO</th>
                        <th>글쓴이</th>
                        <th>문의종류</th>
                        <th>문의제목</th>
                        <th>문의일</th>
                        <th>답변일</th>
                        <th>처리상태</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty myQnaList }">
                    	<c:forEach var="map" items="${myQnaList}" varStatus="status">
	                    	<tr>
		                        <td>${map.rno }</td>
		                        <td>${map.userName }</td>
		                        <td>${map.qnaInquiry } 문의</td>
		                        <td><a class="btn btn-link" href="#" onClick="openWin('<%=request.getContextPath() %>/userSeeUserQuestion.eat?qnaSeq=${map.qnaSeq}' );">${map.qnaSubject }</a></td>
		                        <td>${map.qnaRegDate }</td>
		                        <td>${map.qnaAnswerDate }</td>
		   						<td>
		   						 <c:if test="${map.qnaProgress eq '답변완료'}">
		   						 <a class="btn btn-link" href="#" onClick="openWin('<%=request.getContextPath() %>/userSeeAdminAnswer.eat?qnaSeq=${map.qnaSeq}' );">${map.qnaProgress }</a>
		   						</c:if>
		   						
		   						<c:if test="${map.qnaProgress eq '접수완료' }">
		   						  <a class="btn btn-link" href="#">${map.qnaProgress }</a>
		   						</c:if>
		   						</td>
		                    </tr>
                    	</c:forEach>
                    </c:if>
                    
                </tbody>
            </table>
        </div>
        
        <div>${pageBar}</div>
	</div>
</div>
</body>
</html>