<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<jsp:include page="library.jsp" />

<jsp:include page="top.jsp" />

<style>
	<%-- .mainBann{background-image: url("<%=request.getContextPath() %>/resources/images/IndexImage.jpg");} --%>
	#top_container {background: url(/mazzipmetro/resources/images/IndexImage.jpg) no-repeat 0 71px;
    width: 100%;
    background-size: 100% 576px;
	}
</style>

	<script>
        $(document).ready(function() {

        	// 인덱스 페이지 메인 컨텐츠 뷰
        	mainContentsView();
        	
        	// 인덱스 페이지 측면 컨텐츠 뷰
        	rightContentView();
        	
        	// 인덱스 페이지 최하단 리뷰어 추천 뷰
        	topReviewerRecommend();
        	
        	// 인덱스 페이지 업장 탑 5 랭킹
        	top5RankView();
        	
        	// 실시간 리뷰 애니메이트
            MainReview();

        	// tooltipster 중복호출 방지용
        	//var metroIdArr = [];
        	
		(function($){
			
        	// tooltipster 활성화 
            $('.tooltipster_top').tooltipster({
            	animation: 'grow',
            	animationDuration: 200,
            	distance : 3,
           	   	delay: 100,
           	   	side: 'bottom', // 'top', 'bottom', 'right', 'left'
           	   	theme: 'tooltipster-noir',
           	   	trigger: 'hover',
           	 	interactive: true,
	           	content: 'Loading...',
	           	contentAsHTML: true,// if($origin.data('loaded') !== true)) 이 조건식에 걸리지 않는다. 
	           	
	             // 'instance' is basically the tooltip. More details in the "Object-oriented Tooltipster" section.
	            functionBefore: function(instance, helper) {
	                 
		                 var $origin = $(helper.origin);
		              	
		                 // metroId 얻어오기
		                 var str = instance.elementOrigin().toString(); // localhost로 시작하는 href 값이 나오지만, 객체이다.
						 var metroId = str.substring(str.indexOf("=")+1, str.indexOf("=")+5);//시작index, 끝나는 index  : javascript: searchByMetro(1, '2005')
		         
						 //(중복호출방지를 위해 내가 작성했지만, 필요없어짐)
		                /*for (var i = 0; i < metroIdArr.length; i++) {
							if(metroIdArr[i] == metroId)
								return;
						} */
		                 
		                 // we set a variable so the data is only loaded once via Ajax, not every time the tooltip opens
		                 if ($origin.data('loaded') !== true) {
							 $.get('<%=request.getContextPath()%>/getBest5RestInMetroMap.eat', "metroId="+metroId, function(data) {

				                // call the 'content' method to update the content of our tooltip with the returned data.
				                // note: this content update will trigger an update animation (see the updateAnimation option)
				                instance.content(data);

				                // to remember that the data has been loaded
				                $origin.data('loaded', true);
					            },'html');
		                 }
		             }// end of functionBefore
            });
            
            
            $('.tooltipster_right').tooltipster({
            	animation: 'grow',
            	animationDuration: 200,
            	distance : 3,
           	   	delay: 100,
           	   	side: 'left', // 'top', 'bottom', 'right', 'left'
           	   	theme: 'tooltipster-noir',
           	   	trigger: 'hover',
           	 	interactive: true,
	           	content: 'Loading...',
	           	contentAsHTML: true,
	             // 'instance' is basically the tooltip. More details in the "Object-oriented Tooltipster" section.
	            functionBefore: function(instance, helper) {
	                 
		                 var $origin = $(helper.origin);
		                 
		             	 // metroId 얻어오기
		                 var str = instance.elementOrigin().toString(); // localhost로 시작하는 href 값이 나오지만, 객체이다.
						 var metroId = str.substring(str.indexOf("=")+1, str.indexOf("=")+5);//시작index, 끝나는 index  : javascript: searchByMetro(1, '2005')
		         
		                 
		                 // we set a variable so the data is only loaded once via Ajax, not every time the tooltip opens
		                 if ($origin.data('loaded') !== true) {
							$.ajax({
		                 		url:"<%=request.getContextPath()%>/getBest5RestInMetroMap.eat",
		                 		type :"GET",
		                 		data: "metroId="+metroId,
		                 		dataType:"html",
		                 		success: function(data,status){

					                // call the 'content' method to update the content of our tooltip with the returned data.
					                // note: this content update will trigger an update animation (see the updateAnimation option)
					                instance.content(data);

					                // to remember that the data has been loaded
					                $origin.data('loaded', true);
		                 		}, //end of success: function(data)
		                 		error: function(request, status, error){
		                 			alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		                 		} // end of error: function(request,status,error)
		                 		
		                 	}); //end of $.ajax()
		                 }
		             }// end of functionBefore
            });
            
            
            $('.tooltipster_bottom').tooltipster({
            	animation: 'grow',
            	animationDuration: 200,
            	distance : 3,
           	   	delay: 100,
           	 	side: 'top', // 'top', 'bottom', 'right', 'left'
           	   	theme: 'tooltipster-noir',
           	   	trigger: 'hover',
           		interactive: true,
	           	content: 'Loading...',
	           	contentAsHTML: true,
	             // 'instance' is basically the tooltip. More details in the "Object-oriented Tooltipster" section.
	            functionBefore: function(instance, helper) {
	                 
		                 var $origin = $(helper.origin);
		                 
		         	     // metroId 얻어오기
		                 var str = instance.elementOrigin().toString(); // localhost로 시작하는 href 값이 나오지만, 객체이다.
						 var metroId = str.substring(str.indexOf("=")+1, str.indexOf("=")+5);//시작index, 끝나는 index  : javascript: searchByMetro(1, '2005')
		         
		                 // we set a variable so the data is only loaded once via Ajax, not every time the tooltip opens
		                 if ($origin.data('loaded') !== true) {
		                	 $.ajax({
		                 		url:"<%=request.getContextPath()%>/getBest5RestInMetroMap.eat",
		                 		type :"GET",
		                 		data: "metroId="+metroId,
		                 		dataType:"html",
		                 		success: function(data,status){

					                // call the 'content' method to update the content of our tooltip with the returned data.
					                // note: this content update will trigger an update animation (see the updateAnimation option)
					                instance.content(data);

					                // to remember that the data has been loaded
					                $origin.data('loaded', true);
		                 		}, //end of success: function(data)
		                 		error: function(request, status, error){
		                 			alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		                 		} // end of error: function(request,status,error)
		                 		
		                 	}); //end of $.ajax()
		                 }
		             }// end of functionBefore
            });
            
            $('.tooltipster_left').tooltipster({
            	animation: 'grow',
            	animationDuration: 200,
            	distance : 3,
           	   	delay: 100,
           	   	side: 'right', // 'top', 'bottom', 'right', 'left'
           	   	theme: 'tooltipster-noir',
           	   	trigger: 'hover',
           		interactive: true,
	           	content: 'Loading...',
	           	contentAsHTML: true,
	             // 'instance' is basically the tooltip. More details in the "Object-oriented Tooltipster" section.
	            functionBefore: function(instance, helper) {
	                 
		                 var $origin = $(helper.origin);
		                 
		         	     // metroId 얻어오기
		                 var str = instance.elementOrigin().toString(); // localhost로 시작하는 href 값이 나오지만, 객체이다.
						 var metroId = str.substring(str.indexOf("=")+1, str.indexOf("=")+5);//시작index, 끝나는 index  : javascript: searchByMetro(1, '2005')
		         
		                 // we set a variable so the data is only loaded once via Ajax, not every time the tooltip opens
		                 if ($origin.data('loaded') !== true) {
		                	 $.ajax({
		                 		url:"<%=request.getContextPath()%>/getBest5RestInMetroMap.eat",
		                 		type :"GET",
		                 		data: "metroId="+metroId,
		                 		dataType:"html",
		                 		success: function(data,status){
		                 			
					                // call the 'content' method to update the content of our tooltip with the returned data.
					                // note: this content update will trigger an update animation (see the updateAnimation option)
					                instance.content(data);
					                
					                // to remember that the data has been loaded
					                $origin.data('loaded', true);
		                 		}, //end of success: function(data)
		                 		error: function(request, status, error){
		                 			alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
		                 		} // end of error: function(request,status,error)
		                 		
		                 	}); //end of $.ajax()
		                 }
		             }// end of functionBefore
            });
            
         // bind on start events (triggered on mouseenter)
            $.tooltipster.on('start', function(event) {

                if ($(event.instance.elementOrigin()).hasClass('tooltip_group')) {

                    var instances = $.tooltipster.instances('.tooltip_group'),
                        open = false,
                        duration;

                    $.each(instances, function (i, instance) {

                        if (instance !== event.instance) {

                            // if another instance is already open
                            if (instance.status().open){

                                open = true;

                                // get the current animationDuration
                                duration = instance.option('animationDuration');

                                // close the tooltip without animation
                                instance.option('animationDuration', 0);
                                instance.close();
                                
                                // restore the animationDuration to its normal value
                                instance.option('animationDuration', duration);
                            }
                        }
                    });

                    // if another instance was open
                    if (open) {

                        duration = event.instance.option('animationDuration');

                        // open the tooltip without animation
                        event.instance.option('animationDuration', 0);
                        event.instance.open();
                        
                        // restore the animationDuration to its normal value
                        event.instance.option('animationDuration', duration);

                        // now that we have opened the tooltip, the hover trigger must be stopped
                        event.stop();
                    }
                }
            });
         
		})(jQuery)
		  
         
    		
        });// $(document).ready()
        
        //등록된 맛집이 없는 경우(지도에러유발) 링크 폐쇄
        function goAlert(metroName){
        	alert(metroName + '에 등록된 맛집이 없습니다.');
        }
        

        function MainReview(){
        	
    	 $.ajax({ 
    			url: "<%= request.getContextPath()%>/MainReviewAjax.eat",  
    			method:"get",  	 
    			dataType: "html",
    			success: function(data) {
    				
    				$("#Scroller").html(data);
    				
    				}
    		});//end of $.ajax()
        	
        }// end of MainReview
        
              
       /* function scrolling(){
    	   var $wrapper = $("#scroller");
   		$wrapper.css({'top':0});
   		
   		var n = 1;
   		var animator  = function(imgblock){
   			imgblock.animate({'top':-84*n}, 3000,
   							function(){
   							n++;
   							if(n == 6){
   								imgblock.css({'top':0});
   								n = 1;
   							}
   							setTimeout(animator($(this)), 1000);
   				});
   			
   		}
   		
   		// 마우스 호버시 애니메이션을 멈추기
   		$wrapper.hover(function(){
   			var eventCnt = $wrapper.queue('fx').length;
   			// .queue('fx').length 를 통해서 현재 적용된 애니메이션 관련 효과가 몇개인지를 알 수 있다.
   			for(var i=0; i<eventCnt; i++) {
   				$wrapper.stop(); // 현재 수행중인 애니메이션만 멈추는 stop()메소드
   			}
   		},function(){
   			animator($wrapper); //다시 처음부터 시작되지 않고, 중단된 부분부터 다시 간다.
   		}); 
       }// end of scrolling */
       
       function goSearch(){
    	   if($("#keyword").val().trim().length == 0){
    		   return;
    	   }
    	   
    	   searchFrm.action = "<%=request.getContextPath()%>/search.eat";
    	   searchFrm.submit();
       }

	   	// 인덱스 페이지 탑 5 업장 랭킹 뷰
	   	function top5RankView(){
	   			
	 		$.ajax({
	 			url : "<%=request.getContextPath()%>/indexTop5RankView.eat",
	 			method : "GET",
	 			dataType : "html",
	 			success : function(data){
	 				$(".top5RankView").html(data);
	 			}
	 		}); // end of ajax
	   	}
	   	
		// 인덱스 페이지 메인 컨텐츠 뷰
	   	function mainContentsView(){
	   			
			$.ajax({
				url : "<%=request.getContextPath()%>/mainContentsView.eat",
				method : "GET",
				dataType : "html",
				success : function(data){
					$(".mainContentsView").html(data);
				}
			}); // end of ajax
	   	}
	 	
	    //측면에 들어가는 배너 컨텐츠 뷰
	   	function rightContentView(){
	   		$.ajax({
				url : "<%=request.getContextPath()%>/rightContentView.eat",
				method : "GET",
				dataType : "html",
				success : function(data){
					$("#rightContent").html(data);
				}
			}); // end of ajax
	   	}
	    
	  //중앙 최하단의 리뷰어 추천 뷰
	   	function topReviewerRecommend(){
	   		$.ajax({
				url : "<%=request.getContextPath()%>/topReviewerRecommend.eat",
				method : "GET",
				dataType : "html",
				success : function(data){
					$(".topReviewerRecommend").html(data);
				}
			}); // end of ajax
	   	}
       
</script>
		<div id = "top_container"></div>
		<div class="mainBann" style = "width: 100%; margin-bottom : 25px;">
			<img alt="" src="<%=request.getContextPath() %>/resources/images/metroMap.png"  usemap="#mazzipMetroImgMap" style="margin-top: 15px;margin-bottom: 15px;">
			<map id="mazzipMetroImgMap" name="mazzipMetroImgMap">
				<area shape="circle" alt="잠실역" 				  	coords="918,494,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2005" id="2005" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="잠실나루역" 			  	coords="973,490,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2006" id="2006" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="강변역" 				  	coords="1021,479,22" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2007" id="2007" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="구의역" 				  	coords="1065,453,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2008" id="2008" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="건대입구역"	 		  	coords="1104,418,22" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2009" id="2009" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="성수역" 				  	coords="1133,371,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2010" id="2010" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="뚝섬역" 				  	coords="1152,317,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2011" id="2011" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="한양대역" 				coords="1154,262,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2012" id="2012" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="왕십리역" 				coords="1137,211,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2043" id="2043" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="상왕십리역" 			  	coords="1109,165,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2014" id="2014" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="신당역" 				  	coords="1071,129,21" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2015" id="2015" name="metroMapArea" class='tooltip_group tooltipster_right'/>
				<area shape="circle" alt="동대문역사문화공원역" coords="1028,100,22" 	href="<%=request.getContextPath() %>/metroMap.eat?metroId=2016" id="2016" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="을지로4가역" 			coords="979,82,22"   		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2017" id="2017" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="을지로3가역" 			coords="922,77,22"   		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2018" id="2018" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="을지로입구역" 		  	coords="869,75,20"   		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2019" id="2019" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="시청역" 				  	coords="812,76,22"  		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2020" id="2020" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="충정로역" 				coords="754,77,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2021" id="2021" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="아현역" 			  	  	coords="700,76,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2022" id="2022" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="이대역" 				  	coords="643,76,22" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2023" id="2023" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="신촌역" 				  	coords="587,77,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2024" id="2024" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="홍대역" 				  	coords="530,78,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2025" id="2025" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="합정역" 				  	coords="475,77,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2026" id="2026" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="당산역" 			      	coords="423,82,22" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2027" id="2027" name="metroMapArea" class='tooltip_group tooltipster_top'/>
				<area shape="circle" alt="영등포구청역" 		  	coords="375,97,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2028" id="2028" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="문래역" 				  	coords="329,121,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2029" id="2029" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="신도림역" 				coords="293,155,22" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2030" id="2030" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="대림역" 				  	coords="268,197,22" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2031" id="2031" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="구로디지털단지역" 	  	coords="250,241,22" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2032" id="2032" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="신대방역" 				coords="245,291,20"  		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2033" id="2033" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="신림역" 				  	coords="253,336,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2034" id="2034" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="봉천역" 				  	coords="269,385,20" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2035" id="2035" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="서울대입구역"		  	coords="300,425,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2036" id="2036" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="낙성대역" 				coords="338,454,20" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2037" id="2037" name="metroMapArea" class='tooltip_group tooltipster_left'/>
				<area shape="circle" alt="사당역" 				  	coords="386,475,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2038" id="2038" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="방배역" 				  	coords="435,488,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2039" id="2039" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="서초역" 			 	  	coords="486,494,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2040" id="2040" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="교대역" 				  	coords="537,494,20" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2041" id="2041" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="강남역" 				  	coords="591,494,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2042" id="2042" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="역삼역" 				  	coords="647,494,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2001" id="2001" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="선릉역" 				  	coords="702,493,20" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2002" id="2002" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="삼성역" 				  	coords="756,493,20" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2003" id="2003" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="종합운동장역" 		  	coords="812,493,20" 		href="javascript:goAlert('종합운동장역');" 														id="2004" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
				<area shape="circle" alt="신천역" 				  	coords="868,494,21" 		href="<%=request.getContextPath() %>/metroMap.eat?metroId=2061" id="2061" name="metroMapArea" class='tooltip_group tooltipster_bottom'/>
			</map>
		</div>
		
		<div class="leftCon">
			
			<div class="mainContentsView">
			</div>
			<div  style="margin-top:60px; vertical-align : middle;">
				<div style = "margin-bottom : 8px;">
					<span style = "font-size: 20pt; font-weight: bold;">MazzipMetro의 추천 맛집</span>
				</div>
				<div style = "border : solid 1px black;">
				</div>
				<div class="top5RankView">
				</div>
			</div>
			
			<div class="topReviewerRecommend">
			</div>
		</div>
		<%-- end of leftCon --%>
		
		<div class="rightCon" style = "height : 582px;">
			<div id="realReview">
				<div style = "margin-bottom : 8px;">
					<span style = "font-size: 20pt; font-weight: bold;">실시간 리뷰</span>
				</div>
				<div style = "border : solid 1px black;">
				</div>
			</div>

			<div id="Scroller" class="Scroller">
			</div>

		</div>
		<div class="rightCon" id="rightContent" style = "width : 30%; height : 382px; margin-left : 115px;"></div>
		<%-- end of rightCon --%>	
<jsp:include page="footer.jsp" />

