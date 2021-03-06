<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>

<c:if test = "${sessionScope.loginUser.userSort == 1}">
<jsp:include page="../library.jsp" />
<jsp:include page="../top.jsp" />
</c:if>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.15.0/jquery.validate.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.bootstrapvalidator/0.5.3/js/bootstrapValidator.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/zxcvbn/1.0/zxcvbn-async.min.js"></script>
<link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.min.css" rel="stylesheet" type="text/css" />
<script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.6.2/js/bootstrap-select.min.js"></script>


	<script type="text/javascript" >
	$(function() {
        $("#userUpload").on('change', function(){
            readURL(this);
        });
    });

    function readURL(input) {
        if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
                $('#userProfile').attr('src', e.target.result);
            }

          reader.readAsDataURL(input.files[0]);
        }
    }
	
	
	 function goSubmit(){
		
		var contact_form = document.contact_form;
		
		var chkboxArr = document.getElementsByName("userStation");
		
		var bool = false;
		var count = 0;
		for(var i=0; i<chkboxArr.length; i++) {
			bool = chkboxArr[i].checked;
			if(bool == true) {
				count++;
			}
		}
		
		if (count > 3) {
			alert("최대3개만 선택가능합니다.");
		}
		else {
			contact_form.submit();
		}
	} 
	
	 
	 $(document).ready(function() {
			
		  
		    $('#contact_form').bootstrapValidator({
		        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
		        feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
		        fields: {
		            userStation: {
		                validators: {
		                    choice: {
		                        max: 3,
		                        message: '3개까지 선택가능합니다.'
		                    }
		                }
		            },
		            userPw: {
		                validators: {
		                    notEmpty: {
		                        message: '사용하실 비밀번호를 입력해주세요.'
		                    }
		                }
		            },
		            userPhone: {
		                validators: {
		                    notEmpty: {
		                        message: '전화번호는 필수입력사항이니 입력해주세요.'
		                    },
		                    phone: {
		                        country: 'CN',
		                        message: '전화번호 형식에 맞지 않습니다. 정확한 전화번호를 입력해주세요.'
		                    }
		                }
		            },
		          }
		        })
		        .on('success.form.bv', function(e) {
		            $('#success_message').slideDown({ opacity: "show" }, "slow") // Do something ...
		                $('#contact_form').data('bootstrapValidator').resetForm();

		            e.preventDefault();
		            var $form = $(e.target);
		            var bv = $form.data('bootstrapValidator');
		            $.post($form.attr('action'), $form.serialize(), function(result) {
		                console.log(result);
		            }, 'json');
		        });
		    
		    $("#userPw").blur(function(){
	    		
	    		var pwd = $("#userPw").val(); 
	    		
	    		var pattern = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g);
	    		// 숫자/문자/특수문자/ 포함 형태의 8~15자리 이내의 암호 정규식 
	    		
	    		var bool = pattern.test(pwd);
	    		
	    		if(pwd.length > 0 && !bool) {
	    			$(this).next().show();
	    			$(":input").attr('disabled', true).addClass('bgcol'); 
				    $(this).attr('disabled', false).removeClass('bgcol');
				    
				    $("#btnEdit").attr('disabled', true);
	    		}
	    		else if(bool) {
	    			$(this).next().hide();
	    			$(":input").attr('disabled', false).removeClass('bgcol'); 
				    
				    $("#btnEdit").attr('disabled', false);
	    		}
	    		
	    	});// end of $("#passwd").blur();
	    	
	    	
	    	$("#userPw2").blur(function(){ 
	    		
	    		var pwd = $("#userPw").val();
	    		var userPw2 = $("#userPw2").val();
	    		
	    		if(pwd != userPw2) {
	    			$("#pwmatch").removeClass("glyphicon-ok");
	    			$("#pwmatch").addClass("glyphicon-remove");
	    			$("#pwmatch").css("color","#FF0004");
	    			$(this).next().show();
	    			$(":input").attr('disabled', true).addClass('bgcol'); 
				    $(this).attr('disabled', false).removeClass('bgcol');
				    $("#userPw").attr('disabled', false).removeClass('bgcol');
				    
				    $("#btnEdit").attr('disabled', true);
	    		}
	    		else{
	    			$(this).next().hide(); 
	                $(":input").attr('disabled', false).removeClass('bgcol'); 
				    $("#btnEdit").attr('disabled', false);
				    $("#pwmatch").removeClass("glyphicon-remove");
	    			$("#pwmatch").addClass("glyphicon-ok");
	    			$("#pwmatch").css("color","#00A41E");
	    		}
	    		
	    	});// end of $("#passwdCheck").blur();
	    	
	    	$('.show-password').click(function(event) {
		        event.preventDefault();
		        if ($('#userPw').attr('type') === 'password') {
		          $('#userPw').attr('type', 'text');
		          $('.show-password').text('비밀번호 감추기');
		        } else {
		          $('#userPw').attr('type', 'password');
		          $('.show-password').text('비밀번호 보기');
		        }
		      });
	    	
			 });
	</script>

</head>
<body>
<c:if test = "${sessionScope.loginUser.userSort == 1}">
<div class="subleftCon"  style="height:1300px;">
		<h2>${sessionScope.loginUser.userName}님의<br> 정보수정</h2>
</div>
<div class="subrightCon" style="height:1300px;">
	<jsp:include page="../userMyPage_Menubar.jsp" />
<form name="contact_form" id="contact_form" class="well form-horizontal" action="<%= request.getContextPath() %>/userEditEnd.eat" method="post" enctype="multipart/form-data" style="background:none;">
		<fieldset>
		
		
		<div class="form-group">
		  <label class="col-md-4 control-label">맛집메트로계정(이메일)</label>  
		  <div class="col-md-7 inputGroupContainer">
		  <div class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
		  <input name="userEmail" class="form-control" value="${sessionScope.loginUser.userEmail}" type="text" readonly="readonly">
		</div>
		  </div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
			<label class="col-md-4 control-label">비밀번호입력</label>
				<div class="col-md-7 inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						<input type="password" class="form-control" name="userPw" id="userPw" placeholder="비밀번호 입력" autocomplete="off">
					</div>
					<a class="show-password" href="">비밀번호 보기</a>
				</div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
		  <label class="col-md-4 control-label">비밀번호확인</label>  
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
		        <input type="password" class="form-control" name="userPw2" id="userPw2" placeholder="비밀번호 재입력" autocomplete="off">
		        
		  <!-- <input name="userPw_confirm" id="userPw_confirm" placeholder="정확하게 입력해주세요" class="form-control"  type="text"> -->
		    </div>
		    <span id="pwmatch" class="glyphicon glyphicon-remove" style="color:#FF0004;"></span> 비밀번호 일치 확인
		</div>
		</div>
		
		<input type="hidden" name="userSort" value="${userSort}" />
		
		<!-- Text input-->
		       
		<div class="form-group">
		  <label class="col-md-4 control-label">휴대폰번호</label>  
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        <span class="input-group-addon"><i class="glyphicon glyphicon-earphone"></i></span>
		  <input name="userPhone" placeholder="-없이 입력바랍니다" class="form-control" type="text">
		    </div>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-md-4 control-label">프로필사진</label>
		  <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		    <span class="input-group-addon"><i class="glyphicon glyphicon-upload"></i></span>  
			<input type='file' id="userUpload" name="attach" />
	        <img id="userProfile" name="userProfile" src="<%= request.getContextPath() %>/resources/images/userNoImg.png" alt="your image" width="150px;" height="150px;"/>
			</div>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-md-4 control-label">선호역(지역)<br>
		  최대 3개까지 선택가능합니다
		  </label>
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        	<table class ="table table_Station" style="width:730px;">
		        		<tr>
		        			<th> <span class="tablethline">가 </span></th>
		        			<th> <span class="tablethline">나 </span></th>
		        			<th> <span class="tablethline">다 </span></th>
		        			<th> <span class="tablethline">마 </span></th>
		        			<th> <span class="tablethline">바 </span></th>
		        		</tr>
		        		<tr>
		        			<td class="tabletdblock">
					        	<c:forEach var="list" items="${gaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${naList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${daList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${maList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${baList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
			   			</tr>
			   			<tr>
			   				<th>사</th>
			   				<th>아</th>
			   				<th>자</th>
			   				<th>차</th>
			   				<th>하</th>
			   			</tr>
			   			<tr>
			   				<td>
			   					<c:forEach var="list" items="${saList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td style="width:16%;">
			   					<c:forEach var="list" items="${aaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
				   				<c:forEach var="list" items="${jaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
			   					<c:forEach var="list" items="${chaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
			   					<c:forEach var="list" items="${haList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   			</tr>
					</table>
		  	</div> 
		  </div> 
		</div> 
		
		
		
		<!-- Button -->
		<div class="form-group">
		  <label class="col-md-4 control-label"></label>
		  <div class="col-md-4">
		    <button type="button" id="btnEdit" class="btn btn-warning" onClick="goSubmit();">정보수정<span class="glyphicon glyphicon-send"></span></button>
		  </div>
		</div>
		
		</fieldset>
		</form>
</div>
</c:if>
<c:if test = "${sessionScope.loginUser.userSort == 0}">
		<h2>${sessionScope.loginUser.userName}님의 정보수정</h2>
<form name="contact_form" id="contact_form" class="well form-horizontal" action="<%= request.getContextPath() %>/userEditEnd.eat" method="post" enctype="multipart/form-data" style="background:none;">
		<fieldset>
		
		
		<div class="form-group">
		  <label class="col-md-4 control-label">맛집메트로계정(이메일)</label>  
		  <div class="col-md-7 inputGroupContainer">
		  <div class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
		  <input name="userEmail" class="form-control" value="${sessionScope.loginUser.userEmail}" type="text" readonly="readonly">
		</div>
		  </div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
			<label class="col-md-4 control-label">비밀번호입력</label>
				<div class="col-md-7 inputGroupContainer">
					<div class="input-group">
						<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
						<input type="password" class="form-control" name="userPw" id="userPw" placeholder="비밀번호 입력" autocomplete="off">
					</div>
					<a class="show-password" href="">비밀번호 보기</a>
				</div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
		  <label class="col-md-4 control-label">비밀번호확인</label>  
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
		        <input type="password" class="form-control" name="userPw2" id="userPw2" placeholder="Repeat Password" autocomplete="off">
		        
		  <!-- <input name="userPw_confirm" id="userPw_confirm" placeholder="정확하게 입력해주세요" class="form-control"  type="text"> -->
		    </div>
		    <span id="pwmatch" class="glyphicon glyphicon-remove" style="color:#FF0004;"></span> 비밀번호 일치 확인
		</div>
		</div>
		
		<input type="hidden" name="userSort" value="${userSort}" />
		
		<!-- Text input-->
		       
		<div class="form-group">
		  <label class="col-md-4 control-label">휴대폰번호</label>  
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        <span class="input-group-addon"><i class="glyphicon glyphicon-earphone"></i></span>
		  <input name="userPhone" placeholder="-없이 입력바랍니다" class="form-control" type="text">
		    </div>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-md-4 control-label">프로필사진</label>
		  <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		    <span class="input-group-addon"><i class="glyphicon glyphicon-upload"></i></span>  
			<input type='file' id="userUpload" name="attach" />
	        <img id="userProfile" name="userProfile" src="<%= request.getContextPath() %>/resources/images/userNoImg.png" alt="your image" width="150px;" height="150px;"/>
			</div>
		  </div>
		</div>
		
		<div class="form-group">
		  <label class="col-md-4 control-label">선호역(지역)<br>
		  최대 3개까지 선택가능합니다
		  </label>
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        	<table class ="table table_Station" style="width:730px;">
		        		<tr>
		        			<th> <span class="tablethline">가 </span></th>
		        			<th> <span class="tablethline">나 </span></th>
		        			<th> <span class="tablethline">다 </span></th>
		        			<th> <span class="tablethline">마 </span></th>
		        			<th> <span class="tablethline">바 </span></th>
		        		</tr>
		        		<tr>
		        			<td class="tabletdblock">
					        	<c:forEach var="list" items="${gaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${naList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${daList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${maList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
							<td class="tabletdblock">
								<c:forEach var="list" items="${baList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
							</td>
			   			</tr>
			   			<tr>
			   				<th>사</th>
			   				<th>아</th>
			   				<th>자</th>
			   				<th>차</th>
			   				<th>하</th>
			   			</tr>
			   			<tr>
			   				<td>
			   					<c:forEach var="list" items="${saList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td style="width:16%;">
			   					<c:forEach var="list" items="${aaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
				   				<c:forEach var="list" items="${jaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
			   					<c:forEach var="list" items="${chaList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   				<td>
			   					<c:forEach var="list" items="${haList}">
									<input type="checkbox" id="${list}" value="${list}" name="userStation"><label for = "${list}">${list} </label><br>
								</c:forEach>
			   				</td>
			   			</tr>
					</table>
		  	</div> 
		  </div> 
		</div> 
		
		
		
		<!-- Button -->
		<div class="form-group">
		  <label class="col-md-4 control-label"></label>
		  <div class="col-md-4">
		    <button type="button" id="btnEdit" class="btn btn-warning" onClick="goSubmit();">정보수정<span class="glyphicon glyphicon-send"></span></button>
		  </div>
		</div>
		
		</fieldset>
		</form>
</div>
</c:if>

</body>
</html>