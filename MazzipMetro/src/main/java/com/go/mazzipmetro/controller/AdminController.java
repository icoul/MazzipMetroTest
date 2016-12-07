package com.go.mazzipmetro.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.go.mazzipmetro.common.FileManager;
import com.go.mazzipmetro.common.ThumbnailManager;
import com.go.mazzipmetro.service.AdminService;
import com.go.mazzipmetro.vo.ContentVO;
import com.go.mazzipmetro.vo.RestaurantVO;
import com.go.mazzipmetro.vo.UserVO;

@Controller
public class AdminController {

	@Autowired
	private AdminService service;
	
	
	// 관리자용 업장 수정 페이지 (업장 등급 리스트)
	@RequestMapping(value="/restGradeList.eat", method={RequestMethod.POST})
	public String  restGradeList(HttpServletRequest req) {
		List<HashMap<String, String>> restGradeList = service.restGradeList();
		JSONObject jObj = new JSONObject();
		jObj.put("restGradeList", restGradeList);
		
		req.setAttribute("jObj", jObj);
		return "/admin/ajax/restGradeList";
	}
	
	// 관리자용 업장 수정 페이지 
	@RequestMapping(value="/adminRestEdit.eat", method={RequestMethod.POST})
	public String adminRestEdit(HttpServletRequest req) {
		String restSeq = req.getParameter("restSeq");
		RestaurantVO vo = service.adminRestEditInfo(restSeq);
		
		req.setAttribute("vo", vo);
		return "/admin/adminRestEdit";
	}
	
	// 관리자용 업장 수정 요청 
	@RequestMapping(value="/adminRestEditEnd.eat", method={RequestMethod.POST})
	public String  adminRestEditEnd(HttpServletRequest req, RestaurantVO vo) {
		System.out.println(vo.getRestImg());
		
		vo.setRestImg("900ba417cbe9597ae3ac58a3c3458bdc.jpg");
		int result = service.adminRestEdit(vo);
		
		String msg = "";
		String script = "self.close();";
		
		if (result > 0) {
			msg ="업장 정보변경 성공!";
		}
		req.setAttribute("msg", msg);
		req.setAttribute("script", script);
		return "/admin/msgEnd";
	}
	
	// 등록된 업장관리
	@RequestMapping(value="/adminRestManager.eat", method={RequestMethod.GET})
	public String adminRestManager() {
		
		return "/admin/adminRestManager";
	}
	
	//회원리스트
	@RequestMapping(value="/adminUserList.eat", method={RequestMethod.GET})
	public String adminUserList(HttpServletRequest req, HttpSession session) {
		
		String pageNo = req.getParameter("pageNo");
		
		int totalCount = 0;         // 총게시물 건수
		int sizePerPage = 5;        // 한 페이지당 보여줄 게시물 수
		int currentShowPageNo = 1;  // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함. 
		int totalPage = 0;          // 총 페이지 수(웹브라우저상에 보여줄 총 페이지 갯수) 
		
		int start = 0;              // 시작 행 번호
		int end   = 0;              // 끝 행 번호
		int startPageNo = 0;        // 페이지바에서 시작될 페이지 번호
		
		
		int loop = 0;      // startPageNo 값이 증가할때 마다 1씩 증가하는 용도.
		int blocksize = 5; // "페이지바" 에 보여줄 페이지의 갯수
		
		if(pageNo == null) {
			// 게시판 초기화면에 보여지는 것은
			// req.getParameter("pageNo"); 값이 없으므로
			// pageNo 는 null 이 된다.
			
			currentShowPageNo = 1;
			// 즉, 초기화면은 /list.eat?pageNo=1 로 하겠다는 말이다.
		}
		else{
			currentShowPageNo = Integer.parseInt(pageNo);
			// GET 방식으로 파라미터 pageNo 에 넘어온 값을 
			// 현재 보여주고자 하는 페이지로 설정한다.
		}
		
		start = ((currentShowPageNo - 1) * sizePerPage) + 1;
		end = start + sizePerPage - 1;
		
		session.setAttribute("readCountCheck", "no");
		
		String colName = req.getParameter("colName");
	    String search = req.getParameter("search");
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    map.put("colName", colName);
	    map.put("search", search);
	    
	    
	    map.put("start", String.valueOf(start) );  // 키값 start, 해쉬맵이 String 타입인데 start 는 int 타입이어서 String 타입으로 변경함.  
	    map.put("end", String.valueOf(end) );      // 키값 end,   해쉬맵이 String 타입인데 end 는 int 타입이어서 String 타입으로 변경함.  

		
		List<UserVO> list = service.list(map); 
		
		totalCount = service.getTotalCount(map);
		
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage);  

	    String pagebar = "";
	    pagebar += "<ul>";
	    
	    
	    loop = 1;
	    
	    // **** !!! 페이지바의 시작 페이지 번호(startPageNo)값 만들기 -- 공식임!!!!
	    startPageNo = ( (currentShowPageNo - 1)/blocksize)*blocksize + 1; 
	    
	 // **** 이전5페이지 만들기 ****
	    if(startPageNo == 1) {
	    	// 첫 페이지바에 도달한 경우
	    	pagebar += String.format("&nbsp;[이전%d페이지]", blocksize);  
	    }
	    else {
	    	// 첫 페이지바가 아닌 두번째 이상 페이지바에 온 경우 
	    	
	    	if(colName == null || search == null) {
				// 검색어가 없는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d'>[이전%d페이지]</a>&nbsp;", startPageNo-1, blocksize); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 블럭크기의값 이다.	
			}
			else {
				// 검색어가 있는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d&colname=%s&search=%s'>[이전%d페이지]</a>&nbsp;", startPageNo-1, colName, search, blocksize); // 검색어 있는 경우
			}
	    }
	    
	    
	    // *** 이전5페이지 와 다음5페이지 사이에 들어가는 것을 만드는 것 
	    while( !(loop > blocksize || 
	    		 startPageNo > totalPage) ) {
	    	
	    	if(startPageNo == currentShowPageNo) {
	    		pagebar += String.format("&nbsp;<span style='color:red; font-weight:bold; text-decoration:underline;'>%d</span>&nbsp;", startPageNo);
	    	}
	    	else{
	    		if(colName == null || search == null) {
	    			// 검색어가 없는 경우
	    			pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d'>%d</a>&nbsp;", startPageNo, startPageNo); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 페이지바에 나타낼 startPageNo값 이다.	
	    		}
	    		else {
	    			// 검색어가 있는 경우
	    			pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d&colName=%s&search=%s'>%d</a>&nbsp;", startPageNo, colName, search, startPageNo); // 검색어 있는 경우
	    		}
	    		  
	    	}
	    	
	    	loop++;
	    	startPageNo++;
	    	
	    }// end of while------------------
	    
	    
	    // **** 다음5페이지 만들기 ****
	    if(startPageNo > totalPage) {
	    	// 마지막 페이지바에 도달한 경우
	    	pagebar += String.format("&nbsp;[다음%d페이지]", blocksize); 
	    }
	    else {
	    	// 마지막 페이지바가 아닌 경우 
	    	
	    	if(colName == null || search == null) {
				// 검색어가 없는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d'>[다음%d페이지]</a>&nbsp;", startPageNo, blocksize); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 블럭크기의값 이다.	
			}
			else {
				// 검색어가 있는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminUserList.eat?pageNo=%d&colName=%s&search=%s'>[다음%d페이지]</a>&nbsp;", startPageNo, colName, search, blocksize); // 검색어 있는 경우
			}
	    }
	    
	    
	    
	    pagebar += "</ul>";
	    
		
	    req.setAttribute("list", list);
		req.setAttribute("colName", colName);
		req.setAttribute("search", search);
		req.setAttribute("pagebar", pagebar);
		req.setAttribute("pageNo", pageNo);
		
		return "/admin/adminUserList";
		
		
		
	}
	
	//회원삭제
	@RequestMapping(value = "/adminUserDel.eat", method = RequestMethod.POST)
	public String userDel(HttpServletRequest req) {
		
		String userSeq = req.getParameter("userSeq");
		System.out.println(userSeq);
		String str_pageNo = req.getParameter("pageNo");
		
		int pageNo = Integer.parseInt(str_pageNo);
		HttpSession ses = req.getSession();
		
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userSeq", userSeq);
		
		int result = service.userDel(map);
		
		String msg="";
		String loc ="javascript:history.back();";
		
		if (result > 0) {
			msg ="회원이 삭제되었습니다.";
			loc = ("adminUserList.eat?pageNo="+pageNo);
		}
		
		else {
			msg ="회원이 삭제되지 않았습니다.";
			loc ="javascript:location.href='adminUserList.eat';";
		}
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "/admin/adminUserDel";
	}
	
	//회원 수정 팝업창 열기
	@RequestMapping(value = "/adminUserEdit.eat", method = RequestMethod.GET)
	public String userEdit(HttpServletRequest req, HashMap<String, String> map) {
		String userSeq = req.getParameter("userSeq");
		String userName = req.getParameter("userName");
		String gradeName = req.getParameter("gradeName");
		String userEmail = req.getParameter("userEmail");
		String userPhone = req.getParameter("userPhone");
		String userRegDate = req.getParameter("userRegDate");
		String userPoint = req.getParameter("userPoint");
		
		map.put("userSeq", userSeq);
		map.put("userName", userSeq);
		map.put("gradeName", userSeq);
		map.put("userEmail", userSeq);
		map.put("userPhone", userSeq);
		map.put("userRegDate", userSeq);
		map.put("userPoint", userSeq);
		
		//map = service.adminUserInfo(map); //여러개를 받아오면 map or vo로 받아와야 한다.
		
		
		
		
		
		return "/admin/adminUserEdit";
	}
	
	//회원 수정 완료시
	@RequestMapping(value = "/adminUserEditEnd.eat", method = RequestMethod.GET)
	public String userEditEnd(HttpServletRequest req, HttpSession ses) {
		
		return "/admin/adminUserEditEnd";
	}
	
	//컨텐츠관리
	@RequestMapping(value="/adminConList.eat", method={RequestMethod.GET})
	public String adminConList(HttpServletRequest req, HttpSession session) {
		
		String pageNo = req.getParameter("pageNo");
		
		int totalCount = 0;         // 총게시물 건수
		int sizePerPage = 5;        // 한 페이지당 보여줄 게시물 수
		int currentShowPageNo = 1;  // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정함. 
		int totalPage = 0;          // 총 페이지 수(웹브라우저상에 보여줄 총 페이지 갯수) 
		
		int start = 0;              // 시작 행 번호
		int end   = 0;              // 끝 행 번호
		int startPageNo = 0;        // 페이지바에서 시작될 페이지 번호
		
		
		int loop = 0;      // startPageNo 값이 증가할때 마다 1씩 증가하는 용도.
		int blocksize = 5; // "페이지바" 에 보여줄 페이지의 갯수
		
		if(pageNo == null) {
			// 게시판 초기화면에 보여지는 것은
			// req.getParameter("pageNo"); 값이 없으므로
			// pageNo 는 null 이 된다.
			
			currentShowPageNo = 1;
			// 즉, 초기화면은 /list.eat?pageNo=1 로 하겠다는 말이다.
		}
		else{
			currentShowPageNo = Integer.parseInt(pageNo);
			// GET 방식으로 파라미터 pageNo 에 넘어온 값을 
			// 현재 보여주고자 하는 페이지로 설정한다.
		}
		
		start = ((currentShowPageNo - 1) * sizePerPage) + 1;
		end = start + sizePerPage - 1;
		
		session.setAttribute("readCountCheck", "no");
		
		String colName = req.getParameter("colName");
	    String search = req.getParameter("search");
	    
	    HashMap<String, String> map = new HashMap<String, String>();
	    map.put("colName", colName);
	    map.put("search", search);
	    
	    
	    map.put("start", String.valueOf(start) );  // 키값 start, 해쉬맵이 String 타입인데 start 는 int 타입이어서 String 타입으로 변경함.  
	    map.put("end", String.valueOf(end) );      // 키값 end,   해쉬맵이 String 타입인데 end 는 int 타입이어서 String 타입으로 변경함.  

		
		List<HashMap<String,String>> list = service.conTentList(map); //업장리스트
		
		/*for(HashMap<String,String> map2 : list) {
			String restSeq = map2.get("restSeq");
			String restSeq = map2.get("restName");
		}*/
		
		for (int i = 0; i < list.size(); i++) {			
			ContentVO contVO = new ContentVO();
			RestaurantVO rvo = new RestaurantVO();
			rvo.setRestName(list.get(i).get("restName"));
			rvo.setGradeName(list.get(i).get("gradeName"));
			rvo.setRestPhone(list.get(i).get("restPhone"));
			rvo.setRestAddr(list.get(i).get("restAddr"));
			
			System.out.println(list.get(i).get("gradeName"));
			System.out.println(list.get(i).get("restPhone"));
			contVO.setItem(rvo);
		}
		
			
		
		
		totalCount = service.getConTotalCount(map);
		
		totalPage = (int)Math.ceil((double)totalCount/sizePerPage);  

	    String pagebar = "";
	    pagebar += "<ul>";
	    
	    
	    loop = 1;
	    
	    // **** !!! 페이지바의 시작 페이지 번호(startPageNo)값 만들기 -- 공식임!!!!
	    startPageNo = ( (currentShowPageNo - 1)/blocksize)*blocksize + 1; 
	    
	 // **** 이전5페이지 만들기 ****
	    if(startPageNo == 1) {
	    	// 첫 페이지바에 도달한 경우
	    	pagebar += String.format("&nbsp;[이전%d페이지]", blocksize);  
	    }
	    else {
	    	// 첫 페이지바가 아닌 두번째 이상 페이지바에 온 경우 
	    	
	    	if(colName == null || search == null) {
				// 검색어가 없는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d'>[이전%d페이지]</a>&nbsp;", startPageNo-1, blocksize); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 블럭크기의값 이다.	
			}
			else {
				// 검색어가 있는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d&colname=%s&search=%s'>[이전%d페이지]</a>&nbsp;", startPageNo-1, colName, search, blocksize); // 검색어 있는 경우
			}
	    }
	    
	    
	    // *** 이전5페이지 와 다음5페이지 사이에 들어가는 것을 만드는 것 
	    while( !(loop > blocksize || 
	    		 startPageNo > totalPage) ) {
	    	
	    	if(startPageNo == currentShowPageNo) {
	    		pagebar += String.format("&nbsp;<span style='color:red; font-weight:bold; text-decoration:underline;'>%d</span>&nbsp;", startPageNo);
	    	}
	    	else{
	    		if(colName == null || search == null) {
	    			// 검색어가 없는 경우
	    			pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d'>%d</a>&nbsp;", startPageNo, startPageNo); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 페이지바에 나타낼 startPageNo값 이다.	
	    		}
	    		else {
	    			// 검색어가 있는 경우
	    			pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d&colName=%s&search=%s'>%d</a>&nbsp;", startPageNo, colName, search, startPageNo); // 검색어 있는 경우
	    		}
	    		  
	    	}
	    	
	    	loop++;
	    	startPageNo++;
	    	
	    }// end of while------------------
	    
	    
	    // **** 다음5페이지 만들기 ****
	    if(startPageNo > totalPage) {
	    	// 마지막 페이지바에 도달한 경우
	    	pagebar += String.format("&nbsp;[다음%d페이지]", blocksize); 
	    }
	    else {
	    	// 마지막 페이지바가 아닌 경우 
	    	
	    	if(colName == null || search == null) {
				// 검색어가 없는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d'>[다음%d페이지]</a>&nbsp;", startPageNo, blocksize); // 처음 %d 에는 startPageNo값 , 두번째 %d 에는 블럭크기의값 이다.	
			}
			else {
				// 검색어가 있는 경우
				pagebar += String.format("&nbsp;<a href='/mazzipmetro/adminConList.eat?pageNo=%d&colName=%s&search=%s'>[다음%d페이지]</a>&nbsp;", startPageNo, colName, search, blocksize); // 검색어 있는 경우
			}
	    }
	    
	    
	    
	    pagebar += "</ul>";
	    
		
	    req.setAttribute("list", list);
		req.setAttribute("colName", colName);
		req.setAttribute("search", search);
		req.setAttribute("pagebar", pagebar);
		
		
		return "/admin/adminConList";
		
	}
	
}
