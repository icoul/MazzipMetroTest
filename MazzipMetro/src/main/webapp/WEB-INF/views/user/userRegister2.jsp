<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<style>
.subleftCon {float:left; width:200px; height:500px; border-left:1px solid #dbdbdb; border-right:1px solid #dbdbdb; padding:0; margin:0;}
.subleftCon h2 {width:187px; border-bottom:2px solid #000; padding-top:30px;  padding-bottom:5px; text-align:right;}
.subrightCon {float:left; width:1200px; border-right:1px solid #dbdbdb; height:500px;}
</style>
    <meta charset="utf-8">
	<title>회원가입</title>
	
	<jsp:include page="../top.jsp" />
	<script type="text/javascript" >
	
	 function goSubmit(){
		
		var registerFrm = document.registerFrm;
		
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
			registerFrm.submit();
		}
		
	} 
	
	
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
	
	  $(document).ready(function() {
		
		  
		    $('#contact_form').bootstrapValidator({
		        // To use feedback icons, ensure that you use Bootstrap v3.1.0 or later
		        feedbackIcons: {
		            valid: 'glyphicon glyphicon-ok',
		            invalid: 'glyphicon glyphicon-remove',
		            validating: 'glyphicon glyphicon-refresh'
		        },
		        fields: {
		            userName: {
		                validators: {
		                        stringLength: {
		                        min: 2,
		                        message: '한글자 이상 입력해주세요'
		                    	},
		                        notEmpty: {
		                        message: '이름은 필수입력사항이니 입력해주세요.'
		                    	}
		                }
		            },
		            userEmail: {
		                validators: {
		                    notEmpty: {
		                        message: '이메일은 필수입력사항이니 입력해주세요.'
		                    },
		                    emailAddress: {
		                        message: '이메일형식에 맞지 않습니다.'
		                    }
		                }
		            },
		            userStation: {
		                validators: {
		                    choice: {
		                        max: 3,
		                        message: '최대 3개까지 선택가능 합니다.'
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
		            userYear: {
		                validators: {
		                    notEmpty: {
		                        message: '생년월일은 필수사항이니 선택해주세요'
		                    }
		                }
		            },
		            userMonth: {
		                validators: {
		                    notEmpty: {
		                        message: '&nbsp;'
		                    }
		                }
		            },
		            userDate: {
		                validators: {
		                    notEmpty: {
		                        message: '&nbsp;'
		                    }
		                }
		            },
		            
		            userGender: {
		                validators: {
		                    notEmpty: {
		                        message: '성별은 필수선택사항이니 선택해주세요'
		                    }
		                }
		            }
		            
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
		    
		    
		    
		    $('#userPw').on('propertychange change keyup paste input', function() {
		        var password = $(this).val();
		        var passwordScore = zxcvbn(password)['score'];
		        
		        var updateMeter = function(width, background, text) {
		          $('.strength').text('안전성: ' + text).css('color', background);
		        }
		        
		        if (passwordScore === 0) {
		          if (password.length === 0) {
		            updateMeter("0%", "#ffa0a0", "none");
		          } else {
		            updateMeter("20%", "#ffa0a0", "매우 약함");
		          }
		        }
		        
		        if (passwordScore == 1) updateMeter("40%", "#ffb78c", "약함");
		        if (passwordScore == 2) updateMeter("60%", "#ffec8b", "중간");
		        if (passwordScore == 3) updateMeter("80%", "#c3ff88", "강함");
		        if (passwordScore == 4) updateMeter("100%", "#ACE872", "매우 강함"); // Color needs changing
		        
		      });
		      
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
		      });
		      
	
		
	
	</script>
</head>
<body>
<div class="subleftCon" style="height:1450px;">
	<h2>맛집메트로계정 정보입력</h2>
</div>
<%-- subleftCon --%>
<div class="subrightCon" style="height:auto;">
	<div align="center" style="height: 100px; width:604px; margin:0 auto; padding-top:20px;" >
		<div align="center" style="padding: 1em; float:left; border: 2px solid; border-top-left-radius: 1em; border-bottom-left-radius: 1em; height: 50px; width:150px; border-right:none;"><span class="glyphicon glyphicon-pencil"></span>약관동의</div>
		<div align="center" style="padding: 1em; float:left; border: 2px solid; background-color:blue;  height: 50px; width: 150px;"><span class="glyphicon glyphicon-pencil"></span>정보입력</div>
		<div align="center" style="padding: 1em; float:left; border: 2px solid; height: 50px; width: 150px; border-left:none;"><span class="glyphicon glyphicon-sort"></span>가입인증</div>
		<div align="center" style="padding: 1em; float:left; border: 2px solid; border-top-right-radius: 1em; border-bottom-right-radius: 1em; height: 50px; width: 150px; border-left:none;"><span class="glyphicon glyphicon-home"></span>가입완료</div>
	</div>
    
<div class="container">
    <form name="registerFrm" class="well form-horizontal" action="<%= request.getContextPath() %>/userRegisterEnd.eat" method="post"  id="contact_form" enctype="multipart/form-data" style="background:none;">
		<fieldset>
		<!-- Form Name -->
		<legend>::: 필수입력사항</legend>
		
		<!-- Text input-->
	<div style="margin-top:20px; padding:10px;">
		<div class="form-group">
		  <label class="col-md-4 control-label" >이름</label> 
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
		  <input  name="userName" placeholder="사용자이름" class="form-control"  type="text">
		    </div>
		  </div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
		  <label class="col-md-4 control-label">맛집메트로계정(이메일)</label>  
		  <div class="col-md-7 inputGroupContainer">
		  <div class="input-group">
		  <span class="input-group-addon"><i class="glyphicon glyphicon-envelope"></i></span>
		  <input name="userEmail" placeholder="사용자 이메일" class="form-control"  type="text">
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
		<span class="strength"></span>
		</div>
		</div>
		
		<!-- Text input-->
		
		<div class="form-group">
		  <label class="col-md-4 control-label">비밀번호확인</label>  
		    <div class="col-md-7 inputGroupContainer">
		    <div class="input-group">
		        <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
		        <input type="password" class="form-control" name="password2" id="password2" placeholder="Repeat Password" autocomplete="off">
		        
		  <!-- <input name="userPw_confirm" id="userPw_confirm" placeholder="정확하게 입력해주세요" class="form-control"  type="text"> -->
		    </div>
		    <span id="pwmatch" class="glyphicon glyphicon-remove" style="color:#FF0004;"></span> 비밀번호 일치 확인
		</div>
		</div>
		
		<!-- Text input-->
		       
		<div class="form-group" style="overflow:hidden;">
		  <label class="col-md-4 control-label" style="float:left">생년월일</label>  
		    <div class="col-md-7 inputGroupContainer" style=" float:left;">
		    <div class="input-group" style="width:100%;">
		   		 
		        <span class="input-group-addon" ><i class="glyphicon glyphicon-calendar"></i></span>
		  		<div style="overflow:hidden;">
			  		<select id="userYear" name="userYear" style="width:40%;" class="form-control">
			  			<option value="">년도</option>
						  <script type="text/javascript">
							  var myDate = new Date();
							  var year = myDate.getFullYear();
							  for(var i = year; i > 1900; i--){
								  document.write('<option value="'+i+'">'+i+'</option>');
							  }
						  </script>
					</select>
			  		<select id="userMonth" name="userMonth" style="width:30%;" class="form-control">
			  			<option value="">월</option>
			  			<script type="text/javascript">
			  				for (var j=1; j<=12; j++) {
			  					if (j < 10) {
									document.write('<option value="0'+j+'">0'+j+'</option>');
			  					} else {
			  						document.write('<option value="'+j+'">'+j+'</option>');
			  					}
						  	}
						</script>
			  		</select>
			  		<select id="userDate" name="userDate" style=" width:30%;" class="form-control">
			  			<option value="">일</option>
			  			<script type="text/javascript">
			  				for (k=1; k<=31; k++) {
			  					if (k < 10) {
									document.write('<option value="0'+k+'">0'+k+'</option>');
			  					} else {
			  						document.write("<option>"+k+'</option>'); 
			  					}
			  				}
			  			</script>
			  		</select> 
		  		</div>
		    </div>
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
	
		<!-- radio checks -->
		 <div class="form-group">
           <label class="col-md-4 control-label">성별</label>
           <div class="col-md-5 btn-group" data-toggle="buttons">
               <div class="btn btn-default">
                   <label>
                       <input type="radio" name="userGender" value="M" /> 남
                   </label>
               </div>
               <div class="btn btn-default">
                   <label>
                       <input type="radio" name="userGender" value="F" /> 여
                   </label>
               </div>
           </div>
       </div>
	</div>	
		<legend>::: 선택입력사항</legend>
		<div style="margin-top:20px; padding:10px;">
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
		
</div>
		
		<!-- Button -->
		<div class="form-group">
		  <div align = "center" class="col-md-12">
		    <button type="button" class="btn btn-default" onClick="goSubmit();">회원가입<span class="glyphicon glyphicon-send"></span></button>
		  	<button type="button" class="btn btn-default" onClick="javascript:location.href='index.eat'">취소</button>
		  </div>
		</div>
		
		</fieldset>
		</form>
		</div>
		    </div><!-- /.container -->
		      <jsp:include page="../footer.jsp" />
</div>

<%--subrightCon --%>
</body>
</html>