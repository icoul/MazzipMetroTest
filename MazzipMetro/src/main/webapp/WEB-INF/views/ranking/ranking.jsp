<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<jsp:include page="../library.jsp" />
<jsp:include page="../top.jsp" />

<style>
	.subleftCon {float:left; width:200px; height:500px; border-left:1px solid #dbdbdb; border-right:1px solid #dbdbdb; padding:0; margin:0;}
	.subrightCon {float:left; width:70%; height:auto; border-right:1px solid #dbdbdb; padding-bottom:40px;}
</style>

<script type = "text/javascript">
	$(document).ready(function(){
		restRanking();
	});
	
	function restRanking(){
		
		$.ajax({
     		url:"<%=request.getContextPath()%>/restRanking.eat",
     		type :"GET",
     		dataType:"html",
     		success: function(data){
				$("#rankingView").html(data);
     		} //end of success: function(data)
     		
     	}); //end of $.ajax()
	}
	
</script>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
<div class="subleftCon" style="height:auto; font-size:25px;">
	<h2>My Zone</h2>
</div>
<div class="subrightCon" style="height:2500px;">
	<nav style="padding-left:30px;" class="navbar navbar-default">
	  <div class="container-fluid">
	    <ul align="center" class="nav navbar-nav">
	      <li class="dropdown">
	        <a href="#" onClick = "restRanking();">음식점 랭킹</a>
	      </li>
	      <li class="dropdown">
	        <a href="#">리뷰 작성 랭킹</a>
	      </li>
	      <li class="dropdown">
	        <a href="#">사용자 랭킹</a>
	      </li>
	    </ul>
	  </div>
	</nav>
	
	<div id = "rankingView">
	
	</div>
</div>
</body>
</html>

<jsp:include page="../footer.jsp" />