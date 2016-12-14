package com.go.mazzipmetro.controller;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.go.mazzipmetro.common.FileManager;
import com.go.mazzipmetro.service.UserService;
import com.go.mazzipmetro.vo.UserAliasVO;
import com.go.mazzipmetro.vo.UserAttendVO;
import com.go.mazzipmetro.vo.UserVO;


@Controller
public class UserController {

	@Autowired
	private UserService service;
	
	@Autowired
	private FileManager fileManager;
	
//	회원동의
	@RequestMapping(value="/userRegisterAgree.eat", method={RequestMethod.GET})
	public String userRegisterAgree(HttpServletRequest req){
		String type =  req.getParameter("type");
		req.setAttribute("type", type);
		return "user/userRegister_Agree";
	}
	
//	회원가입 중 이메일 중복체크
	@RequestMapping(value="/emailDuplicatecheck.eat", method={RequestMethod.POST})
	public String emailDuplicatecheck(HttpServletRequest req){
		String userEmail =  req.getParameter("userEmail");
		System.out.println(userEmail);
		int result = service.emailDuplicatecheck(userEmail);
		
		req.setAttribute("result", result);
		System.out.println(userEmail);
		return "user/emailDuplicatecheck";
	}
	
//	회원가입
	@RequestMapping(value="/userRegisterForm.eat", method={RequestMethod.POST})
	public String userRegister2(HttpServletRequest req){
		String userSort =  req.getParameter("userSort");
		
		List<String> list = service.alignTest();
		String[] array = list.toArray(new String[list.size()]);
		
		List<String> gaList = new ArrayList<String>();
		List<String> naList = new ArrayList<String>();
		List<String> daList = new ArrayList<String>();
		List<String> raList = new ArrayList<String>();
		List<String> maList = new ArrayList<String>();
		List<String> baList = new ArrayList<String>();
		List<String> saList = new ArrayList<String>();
		List<String> aaList = new ArrayList<String>();
		List<String> jaList = new ArrayList<String>();
		List<String> chaList = new ArrayList<String>();
		List<String> kaList = new ArrayList<String>();
		List<String> taList = new ArrayList<String>();
		List<String> paList = new ArrayList<String>();
		List<String> haList = new ArrayList<String>();
		
		Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);
		
		for (int i = 0; i < array.length; i++) {
			int metroInt = (int)array[i].charAt(0);
			
			if (metroInt < 45208) {
				gaList.add(array[i]);
			}
			
			if (45208 < metroInt && metroInt < 45796) {
				naList.add(array[i]);
			}
			
			if (45796 <= metroInt && metroInt < 46972) {
				daList.add(array[i]);
			}
			
			if (46972 <= metroInt && metroInt < 47560) {
				raList.add(array[i]);
			}
			
			if (47560 <= metroInt && metroInt < 48148) {
				maList.add(array[i]);
			}
			
			if (48148 <= metroInt && metroInt < 49324) {
				baList.add(array[i]);
			}
			
			if (49324 <= metroInt && metroInt < 50500) {
				saList.add(array[i]);
			}
			
			if (50500 <= metroInt && metroInt < 51088) {
				aaList.add(array[i]);
			}
			
			if (51088 <= metroInt && metroInt < 52264) {
				jaList.add(array[i]);
			}
			
			if (52264 <= metroInt && metroInt < 52852) {
				chaList.add(array[i]);
			}
			
			if (52852 <= metroInt && metroInt < 53440) {
				kaList.add(array[i]);
			}
			
			if (53440 <= metroInt && metroInt < 54028) {
				taList.add(array[i]);
			}
			
			if (54028 <= metroInt && metroInt < 54616) {
				paList.add(array[i]);
			}
			
			if (54616 <= metroInt) {
				haList.add(array[i]);
			}
		}
		
		req.setAttribute("gaList", gaList);
		req.setAttribute("naList", naList);
		req.setAttribute("daList", daList);
		req.setAttribute("raList", raList);
		req.setAttribute("maList", maList);
		req.setAttribute("baList", baList);
		req.setAttribute("saList", saList);
		req.setAttribute("aaList", aaList);
		req.setAttribute("jaList", jaList);
		req.setAttribute("chaList", chaList);
		req.setAttribute("kaList", kaList);
		req.setAttribute("taList", taList);
		req.setAttribute("paList", paList);
		req.setAttribute("haList", haList);
		
		req.setAttribute("userSort", userSort);
		
		return "user/userRegister_Form";
	}
	
//	회원가입
	@RequestMapping(value="/userRegisterEnd.eat", method={RequestMethod.POST})
	public String userRegisterEnd(UserVO vo, MultipartHttpServletRequest req, HttpSession session){
		
		//System.out.println(">>>>>>> 확인용 <<<<<<<");
		//System.out.println("vo.getUserName() ==> " + vo.getUserName());
		
		if(!vo.getAttach().isEmpty()) {
			String root = session.getServletContext().getRealPath("/");
			String path = root + File.separator +"files" + File.separator + "user";
			
			
			byte[] bytes = null;
			
			try {
				bytes = vo.getAttach().getBytes();
				String newFileName =  fileManager.doFileUpload(bytes, vo.getAttach().getOriginalFilename(), path);
				vo.setUserProfile(newFileName);
		} catch (Exception e) {
			
		}
		
	}

		String userGender = "";
		if("F".equals(vo.getUserGender())) {
			userGender = "여";
		} else if ("M".equals(vo.getUserGender())) {
			userGender = "남";
		}
	
		
		vo.setUserBirthDay(vo.getUserYear(), vo.getUserMonth(), vo.getUserDate());
		vo.setUserGender(userGender);
		
		System.out.println(vo.getUserBirthDay());
		System.out.println(vo.getUserGender());
		
		int n = 0;
//		service.userRegister(vo);
		
//		if(vo.getAttach().isEmpty()) {
			 n = service.userRegister(vo);
//		} /*else if(!vo.getAttach().isEmpty()) { // 파일첨부가 있는 경우
//			 n = service.userRegister(vo);
//		}*/
		req.setAttribute("n", n);
		
		System.out.println("====== 확인용 =======");
		System.out.println("n : " + n);
		
		//req.setAttribute("n", n);
		return "userRegisterEnd";
	} // end : userRegisterEnd 회원가입처리
	
//	로그인시 개인회원수정
	@RequestMapping(value="/userEdit.eat", method=RequestMethod.GET)
	public String userEdit(UserVO vo, HttpServletRequest req) {
		
		List<String> list = service.alignTest();
		String[] array = list.toArray(new String[list.size()]);
		
		List<String> gaList = new ArrayList<String>();
		List<String> naList = new ArrayList<String>();
		List<String> daList = new ArrayList<String>();
		List<String> raList = new ArrayList<String>();
		List<String> maList = new ArrayList<String>();
		List<String> baList = new ArrayList<String>();
		List<String> saList = new ArrayList<String>();
		List<String> aaList = new ArrayList<String>();
		List<String> jaList = new ArrayList<String>();
		List<String> chaList = new ArrayList<String>();
		List<String> kaList = new ArrayList<String>();
		List<String> taList = new ArrayList<String>();
		List<String> paList = new ArrayList<String>();
		List<String> haList = new ArrayList<String>();
		
		Arrays.sort(array, String.CASE_INSENSITIVE_ORDER);
		
		for (int i = 0; i < array.length; i++) {
			int metroInt = (int)array[i].charAt(0);
			
			if (metroInt < 45208) {
				gaList.add(array[i]);
			}
			
			if (45208 < metroInt && metroInt < 45796) {
				naList.add(array[i]);
			}
			
			if (45796 <= metroInt && metroInt < 46972) {
				daList.add(array[i]);
			}
			
			if (46972 <= metroInt && metroInt < 47560) {
				raList.add(array[i]);
			}
			
			if (47560 <= metroInt && metroInt < 48148) {
				maList.add(array[i]);
			}
			
			if (48148 <= metroInt && metroInt < 49324) {
				baList.add(array[i]);
			}
			
			if (49324 <= metroInt && metroInt < 50500) {
				saList.add(array[i]);
			}
			
			if (50500 <= metroInt && metroInt < 51088) {
				aaList.add(array[i]);
			}
			
			if (51088 <= metroInt && metroInt < 52264) {
				jaList.add(array[i]);
			}
			
			if (52264 <= metroInt && metroInt < 52852) {
				chaList.add(array[i]);
			}
			
			if (52852 <= metroInt && metroInt < 53440) {
				kaList.add(array[i]);
			}
			
			if (53440 <= metroInt && metroInt < 54028) {
				taList.add(array[i]);
			}
			
			if (54028 <= metroInt && metroInt < 54616) {
				paList.add(array[i]);
			}
			
			if (54616 <= metroInt) {
				haList.add(array[i]);
			}
		}
		
		req.setAttribute("gaList", gaList);
		req.setAttribute("naList", naList);
		req.setAttribute("daList", daList);
		req.setAttribute("raList", raList);
		req.setAttribute("maList", maList);
		req.setAttribute("baList", baList);
		req.setAttribute("saList", saList);
		req.setAttribute("aaList", aaList);
		req.setAttribute("jaList", jaList);
		req.setAttribute("chaList", chaList);
		req.setAttribute("kaList", kaList);
		req.setAttribute("taList", taList);
		req.setAttribute("paList", paList);
		req.setAttribute("haList", haList);
		
		return "user/userEdit";
	}
	
//	로그인시 개인회원 수정
	@RequestMapping(value="/userEditEnd.eat", method=RequestMethod.POST)
	public String userEditEnd(UserVO vo, MultipartHttpServletRequest req, HttpSession session) {
		
		if(!vo.getAttach().isEmpty()) {
			String root = session.getServletContext().getRealPath("/");
			String path = root + "resources"+ File.separator +"files";
			
			
			byte[] bytes = null;
			
			try {
				bytes = vo.getAttach().getBytes();
				String newFileName =  fileManager.doFileUpload(bytes, vo.getAttach().getOriginalFilename(), path);
				vo.setUserProfile(newFileName);
		} catch (Exception e) {
			
		}
		
	}
		
		
		int n = service.userEdit(vo);
		
		req.setAttribute("n", n);
		
		return "user/userEditEnd";
		
		
	}

//	로그인 시 개인회원 마이페이지
	@RequestMapping(value="/userMyPage.eat", method={RequestMethod.GET})
	public String userMyPage(UserVO vo, HttpServletRequest req, HttpServletResponse res, HttpSession session){
		
		UserVO loginUser =  (UserVO)session.getAttribute("loginUser");
		String userSeq = loginUser.getUserSeq();
		
		int userPoint = service.userPoint(userSeq);
		
		if(loginUser.getUserSort().equals("1")) {
			int restCount = service.restCount(userSeq);
			int effectContent = service.userContent(userSeq);
			int coupon = service.userCoupon(userSeq);
			
			req.setAttribute("restCount", restCount);
			req.setAttribute("coupon", coupon);
			req.setAttribute("effectContent", effectContent);
		} else if (loginUser.getUserSort().equals("0")) {
			int reviewCount = service.userReviewCount(userSeq);
			int qnaCount = service.userQnaCount(userSeq);
			
			req.setAttribute("reviewCount", reviewCount);
			req.setAttribute("qnaCount", qnaCount);
		}
		
		req.setAttribute("userPoint", userPoint);
		
		return "user/userMyPage";
	} // end : userMyPage

	// 로그아웃 처리
	@RequestMapping(value="/logOut.eat", method={RequestMethod.GET})
	public String UserLogOut(HttpServletRequest req, HttpServletResponse res){
		
		HttpSession session = req.getSession();
		session.invalidate();
		
		String msg = "로그아웃되었습니다";
		String loc = "index.eat";
		
		Cookie cookieAutoLogin = new Cookie("autoLogin", "yes");
		cookieAutoLogin.setMaxAge(0);
		cookieAutoLogin.setPath("/");
		res.addCookie(cookieAutoLogin);
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "msg";
	}
	
	// 로그인 처리
	@RequestMapping(value="/login.eat", method={RequestMethod.POST})
	public String UserLogin(UserVO vo, HttpServletRequest req, HttpServletResponse res){
		
		String userEmail = req.getParameter("dx_userId");
		String userPw = req.getParameter("dx_password");
		String rememberId = req.getParameter("dx_rememberId");
		String rememberPwd = req.getParameter("dx_rememberPwd");
		String autoLogin = req.getParameter("dx_autoLogin");
		
		HttpSession ses = req.getSession();
		
		HashMap<String, String> map = new HashMap<String, String>();
	    map.put("userEmail", userEmail);
	    map.put("userPw", userPw);
	    
		int result = service.UserLogin(map);
		
		/*System.out.println("확인용 loginusercheck");*/
		
		String msg = "";
		String loc = "";
		
		if (result == -1) {//아이디가 존재하지 않는 경우
			msg = "존재하지 않는 아이디입니다.";
			loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			/*System.out.println("확인용 loginusercheck");*/
			
			return"msg";
			
		} else if (result == 0) {//비밀번호가 틀린 경우
			msg = "비밀번호가 틀렸습니다.";
			loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			return"msg";
			
		} else if (result == 1){// 아이디와 비번이 모두 맞은 경우
			
			UserVO loginUser = service.getLoginUser(userEmail);
						
			//System.out.println("확인용 loginusercheck");
			
			msg = loginUser.getUserName() + "님, 환영합니다.";
			loc = "index.eat";			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			//로그인할 사용자의 정보를 세션에 저장하도록 한다.
			
			ses.setAttribute("loginUser", loginUser);
			//로그인한 시간을 세션에 저장하도록 한다.
			Date now = new Date();
			String date = String.format("%tF %tT %tZ", now, now, now);
			ses.setAttribute("loginTime", date);
			
			//사용자 로그인시 아이디저장 체크박스에 체크를 했을 경우와, 체크를 하지 않았을 경우
			// * checked : 쿠키 저장
			// * unchecked : 쿠키 삭제
			Cookie cookieId=null, cookiePwd=null
					, cookieAutoLogin = new Cookie("autoLogin", autoLogin);
			
			// 아이디,비번 암호화하기
			try {
				cookieId = new Cookie("rememberId", URLEncoder.encode(loginUser.getUserEmail(), "UTF-8"));
				cookiePwd = new Cookie("rememberPwd", loginUser.getUserPw());//비보안
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//로그인 하는 사용자의 아이디값을 "rememberId" 문자열을 키값으로 하는 쿠키객체를 생성한다. 
			//파라미터 타입은 Cookie(String name, String value)이다.
			
			//System.out.println("확인용 cooooooooooooooooooookie" + cookieId + cookiePwd);
			
			if (rememberId != null) {//아이디저장 체크박스에 체크한 경우. 즉, "on"이다.
				cookieId.setMaxAge(7*24*60*60);
				// 쿠키의 생존기간을 7일(단위는 초이다.)
				//매로그인마다 이시간이 연장된다.
			} else {//아이디저장 체크박스에 체크해제한 경우
				cookieId.setMaxAge(0);//쿠키 지속시간이 0초, 즉 삭제를 의미한다.
			}
			
			if (rememberPwd != null) {
				cookiePwd.setMaxAge(7*24*60*60);
			} else {
				cookiePwd.setMaxAge(0);
			}
			
			if (autoLogin != null) {
				cookieAutoLogin.setMaxAge(7*24*60*60);
			} else {
				cookieAutoLogin.setMaxAge(0);
			}
			
			cookieId.setPath("/");
			cookiePwd.setPath("/");
			cookieAutoLogin.setPath("/");
			//쿠키가 사용되어질 디렉토리 정보 설정.
			// cookie.setPath("디렉토리");
			// "/" : 해당 도메인의 모든 페이지에서 사용가능하다.
			
			res.addCookie(cookieId);
			res.addCookie(cookiePwd);
			res.addCookie(cookieAutoLogin);
			//사용자의 웹브라우져로 쿠키를 전송시킨다.(response 객체에 담는다.)
			//이로서, 우리는 7일간 사용가능한 쿠키를 전송할 수도 있고,
			//0초짜리 쿠키(쿠키삭제)를 사용자 웹브라우져로 전송한다.
			//이 후의 일은 모두 웹브라우져가 알아서 해준다.
			
			msg = loginUser.getUserName() + "님, 환영합니다.";
			
			if(loginUser.getUserSort().equals("0")){ //userSort가 0인 일반사용자일때만 출석체크를 한다.
				loc = "attendCheck.eat";
			}else{
				loc = "index.eat";
			}
			
			/*
			 	로그인 하지 않은 상태에서 장바구니 담기를 하면, 로그인메세지를 출력한다.
			 	로그인 후에는 기존에 장바구니에 담고자 했던 제품의 상세페이지로 간다.
			 	
			 	제품의 상세페이지는 CartAddAction에서 세션에 "returnPage"로 저장해 두었다.
			 */
			//session에 저장된 returnPage가 있다면, 그곳으로 이동한다.
			if(ses.getAttribute("returnPage") != null){
				String viewPage = (String)ses.getAttribute("returnPage"); 
				
				req.setAttribute("msg", msg);
				req.setAttribute("loc", viewPage);
				
				return "msg";
			}
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);//loc를 req에 담았지만, msg.jsp로 가지 않으면 휘발된다.
			
			
			
		}// end of if~else
		return "msg";

	}// end of logIn
	
	
	@RequestMapping(value="/login_End.eat", method={RequestMethod.GET})
	public String loginEnd(HttpServletRequest req, HttpServletResponse res){
		
		HttpSession session = req.getSession();
		session.invalidate();
		
		String msg = "로그아웃되었습니다";
		String loc = "index.eat";
		
		
		req.setAttribute("msg", msg);
		req.setAttribute("loc", loc);
		
		return "user/userLogin";
	}
	
	//출석체크
	@RequestMapping(value="/attendCheck.eat", method={RequestMethod.GET})
	public String userAttendCheck(HttpServletRequest req, HttpSession session){
		UserVO loginUser = (UserVO)session.getAttribute("loginUser");
		
		int isUserExist = service.userExist(loginUser.getUserSeq()); //userAttend테이블에  유저가 있는지 체크
		
		int result = 0;
		if(isUserExist == 0){
			//로그인한 유저가 가입후 처음으로 접속했으면 userAttend테이블에 insert를 시킨다. 그리고 유저에게 3마일리지와 경험치15를 업데이트시킨다..
			result =  service.insertAttend(loginUser.getUserSeq());
			
			UserVO userVO = service.getLoginUser(loginUser.getUserEmail());
			loginUser.setUserPoint(userVO.getUserPoint());
			loginUser.setUserExp(userVO.getUserExp());
			
			session.setAttribute("loginUser", loginUser);
			
			
			if(result == 2){
				req.setAttribute("script", " alert('처음 출석체크가 되었습니다. 마일리지 : 3점이 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
				
			}else{
				req.setAttribute("script", "alert('처음 출석체크가 실패되었습니다.'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
			}
			
		}else{//tbl_userAttend에 있는 유저가 접속했으면 tbl_userAttend에 update를 시킨다.
			
			//로그인한 사람이 오늘 다시 로그인 했는지 체크 
			int isLoginToday = service.userLoginToday(loginUser.getUserSeq()); 
			
			if(isLoginToday == 1){//isLoginToday가 1이면 오늘 로그인 했었다.
				/*req.setAttribute("msg", "오늘 이미 출석체크를 하셨습니다.");
				req.setAttribute("loc", "index.eat");*/
				req.setAttribute("script", "location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
			}else{//오늘 처음 로그인
				/*로그인      -> 출석        ->3mileage
	            -> 연속출석    ->3일출석    ->10mileage
	                    ->7일출석    ->30mileage
	                    ->15일출석    ->50mileage
	                    ->20일 출석    ->70mileage*/
				UserAttendVO vo2 = service.getUserAttend(loginUser.getUserSeq());
				String alertRandomBoxStatus = vo2.getAlertRandomBoxStatus(); //한달에 한번 랜덤박스를 지급했을때 알려주기위한 변수
				
				System.out.println("-----------------------------------------" + alertRandomBoxStatus);
				
				HashMap<String, String> hashMap2 = new HashMap<String, String>();
				hashMap2.put("attendLastDay", vo2.getAttendLastDay());
				hashMap2.put("userSeq", loginUser.getUserSeq());
				
				int isLoginContinue = service.userLoginContinueCheck(hashMap2); //어제 출석했는지 체크해서 어제 출석을 안했으면 연속출석일수를 0으로 만든다.
				
				int  m = 0;
				
			/*	HashMap<String, String> hashMap3 = new HashMap<String, String>();
				hashMap3.put("userSeq", loginUser.getUserSeq());*/
				
				if(isLoginContinue == 0){ //0이면 어제 출석을 안한것
					hashMap2.put("continueCheck", "0");
					m = service.updateUserAttend(hashMap2);
					/*hashMap3.put("continueCheck", "0");
					m = service.updateUserAttend(hashMap3);*/ //로그인한 유저의 
				}else{ // 1이면 어제 출석을 한것
					hashMap2.put("continueCheck", "1");
					m = service.updateUserAttend(hashMap2);
					/*hashMap3.put("continueCheck", "1");
					m = service.updateUserAttend(hashMap3);*/ //로그인한 유저의 출석정보를 업데이트한다. allAttendDay = allAttendDay + 1, continueAttendDay = continueAttendDay + 1, attendLastDay = sysdate + 9/24 + 4/24/60
				}
				
				if(m == 1){ //업데이트를 성공했을 때
					UserAttendVO vo = service.getUserAttend(loginUser.getUserSeq());
					
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("contineuAttendDay", vo.getContinueAttendDay());
					hashMap.put("userSeq", loginUser.getUserSeq());
					
					//로그인한 유저의 포인트와 경험치, 랜덤박스 업데이트
					int result2 = service.updateUserPoint_RandomBox(hashMap);
					
					
					/*디비에서 변경된 값을  세션에 있는 로그인유저에게 변경*/
					UserVO userVO = service.getLoginUser(loginUser.getUserEmail());
					loginUser.setUserPoint(userVO.getUserPoint());
					loginUser.setUserExp(userVO.getUserExp());
					
					session.setAttribute("loginUser", loginUser);
					
					String point = "";
					if(Integer.parseInt(vo.getContinueAttendDay()) == 3){
						point = "10";
					}else if(Integer.parseInt(vo.getContinueAttendDay()) == 7){
						point = "30";
					}else if(Integer.parseInt(vo.getContinueAttendDay()) == 15){
						point = "50";
					}else if(Integer.parseInt(vo.getContinueAttendDay()) == 20){
						point = "70";
					}else{
						point = "3";
					}
						
					if(result2 == 2){
						
						if(Integer.parseInt(vo.getContinueAttendDay()) == 1){

							if(Integer.parseInt(alertRandomBoxStatus) == 1){
								int n = service.updateAlertRandomBoxStatus(loginUser.getUserSeq());
								
								if(n == 1){
									req.setAttribute("script", " alert('프리미엄 랜덤박스 " + (loginUser.getGradeSeq().equals("UG6") ?  "1" : "3")  +"개 가 지급되었습니다.'); alert('출석체크 되었습니다." + "마일리지 :" + point + "점이 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
								}else{
									req.setAttribute("script", " alert('userAttendCheck.eat 682줄 에러'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
								}
								
								
							}else{
								req.setAttribute("script", " alert('출석체크 되었습니다." + "마일리지 :" + point + "점이 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
							}
							
						}else{
							
							if("14".equals(vo.getContinueAttendDay())){
								
								if(Integer.parseInt(alertRandomBoxStatus) == 1){
									
									int n = service.updateAlertRandomBoxStatus(loginUser.getUserSeq());
									
									if(n == 1){	
										req.setAttribute("script", " alert('프리미엄 랜덤박스 " + (loginUser.getGradeSeq().equals("UG6") ?  "1" : "3")  +"개 가 지급되었습니다.'); alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); alert('랜덤 박스가 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}else{
										req.setAttribute("script", " alert('userAttendCheck.eat 682줄 에러'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}
									
								}else{
									req.setAttribute("script", "alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); alert('랜덤 박스가 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
								}
								
							}else if("30".equals(vo.getContinueAttendDay())){
								
								if(Integer.parseInt(alertRandomBoxStatus) == 1){
									int n = service.updateAlertRandomBoxStatus(loginUser.getUserSeq());
									
									if(n == 1){	
										req.setAttribute("script", " alert('프리미엄 랜덤박스 " + (loginUser.getGradeSeq().equals("UG6") ?  "1" : "3")  +"개 가 지급되었습니다.'); alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); alert('프리미엄 박스가 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}else{
										req.setAttribute("script", " alert('userAttendCheck.eat 715줄 에러'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}
									
								}else{
									req.setAttribute("script", "alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); alert('프리미엄 박스가 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
								}
								
							}else{
								
								if(Integer.parseInt(alertRandomBoxStatus) == 1){
									int n = service.updateAlertRandomBoxStatus(loginUser.getUserSeq());
									
									if(n == 1){	
										req.setAttribute("script", " alert('프리미엄 랜덤박스 " + (loginUser.getGradeSeq().equals("UG6") ?  "1" : "3")  +"개 가 지급되었습니다.'); alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}else{
										req.setAttribute("script", " alert('userAttendCheck.eat 730줄 에러'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
									}
								}else{
									req.setAttribute("script", "alert('" + vo.getContinueAttendDay() + " 일 연속 출석입니다. 마일리지 " + point + "점이 지급되었습니다.'); location.href='userGradeCheck.eat'; self.close(); opener.location.reload(true);  ");
								}
							}
						}
						
						
					}else{
						req.setAttribute("script", " alert('m==1일때  result2 == 2가 아닐떄 출석체크 오류입니다.'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
					}
					
					
				}else{
					req.setAttribute("script", " alert('m==1일이 아닐떄 출석체크 오류입니다.'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
				}
				
			}
		}
		
		return "/user/msgEnd";
		//return "msg";
	}
	
	//유저등급체크
	@RequestMapping(value="/userGradeCheck.eat", method={RequestMethod.GET})
	public String userGradeCheck(HttpServletRequest req, HttpServletResponse res, HttpSession session){
		System.out.println("********************************************userGradeCheck");
		UserVO loginUser = (UserVO)session.getAttribute("loginUser");
		HashMap<String, String> resultHashMap = service.userGradeCheck(loginUser.getUserEmail()); //유저의 경험치를 가져온후에 그 경험치가 등급기준에 맞는지 확인해서 등급이름을 업데이트 시킨다.
		
		String alertUpgradeStatus = (service.getUserAttend(loginUser.getUserSeq())).getAlertUpgradeStatus();
		
		if(resultHashMap.get("result").equals("1")){ //등급업이 됬을때
			
			if(((resultHashMap.get("gradeSeq").equals("UG6") || resultHashMap.get("gradeSeq").equals("UG7")) )){
				String userGradeName = resultHashMap.get("userGradeName");
								
				if(resultHashMap.get("gradeSeq").equals("UG6") && Integer.parseInt(alertUpgradeStatus) == 0 ){//달인으로 등급업이 가능하고 알림횟수가 0일떄만 실행
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("userSeq", loginUser.getUserSeq());
					hashMap.put("alertUpgradeStatus", "1");
					int n = service.updateAlertUpgradeStatus(hashMap);
					
					if(n == 1){
						req.setAttribute("script", " alert('" + userGradeName + "으로 등급업 하실수 있습니다!!!!'); alert('동,역 마스터 칭호 각각 5개와 1500마일리지로 마이페이지에서 등급업을 해주세요'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
					}else{
						req.setAttribute("script", " alert('userGradeCheck.eat 719줄 에러'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
					}
					
				}else if(resultHashMap.get("gradeSeq").equals("UG7") && Integer.parseInt(alertUpgradeStatus) == 0){//신으로 등급업이 가능하고 알림횟수가 0일떄만 실행
					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap.put("userSeq", loginUser.getUserSeq());
					hashMap.put("alertUpgradeStatus", "1");
					int n = service.updateAlertUpgradeStatus(hashMap);
					
					if(n == 1){
						req.setAttribute("script", "alert('" + userGradeName + "으로 등급업 하실수 있습니다!!!!'); alert('구 마스터 칭호 1개와 3000마일리지로 마이페이지에서 등급업을 해주세요'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
					}else{
						req.setAttribute("script", " alert('userGradeCheck.eat 732줄 에러'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
					}
				}else{
					req.setAttribute("script", " location.href='index.eat'; self.close(); opener.location.reload(true);  ");
				}

			}else{ //달인, 신을 제외한 나머지 계급들이 등급업이 되었을때 알려주는 것
				String userGradeName = resultHashMap.get("userGradeName");
				
				loginUser.setGradeName(userGradeName);
				session.setAttribute("loginUser", loginUser);
				
				req.setAttribute("msg", userGradeName + "로 등급업 하셨습니다!!!!");
				req.setAttribute("script", " alert('"+ userGradeName + "로 등급업 하셨습니다!!!!'); location.href='index.eat'; self.close(); opener.location.reload(true);  ");
			}
			
		}else{ //등급업을 안하고 기존 등급 유지
			req.setAttribute("script", "location.href='index.eat'; self.close(); opener.location.reload(true);  ");
		}
		
		return "/user/msgEnd";
	}
	
	@RequestMapping(value="/myReviewList.eat", method={RequestMethod.GET})
	public String login_myReviewList(HttpServletRequest req, HttpServletResponse res, HttpSession session) {
		UserVO loginUser = (UserVO)session.getAttribute("loginUser");
		String userSeq = loginUser.getUserSeq();
		HashMap<String,String> map = new HashMap<String,String>();
		
		
		
		String pageNo = req.getParameter("pageNo");
		int totalCount = 0; // 총게시물 건수
		int sizePerPage = 5; // 한 페이지당 게시물 수
		int currentShowPageNo = 1; // 현재 보여주는 페이지 번호로서, 초기치로는 1페이지로 설정한다.

		int totalPage = 0; // 총 페이지수 (웹브라우저상에 보여줄 총 페이지 갯수)

		int start = 0; // 시작 행 번호
		int end = 0; // 끝 행 번호
		int startPageNo = 0; // 페이지바에서 시작될 페이지 번호
		/*
		 * 페이지바란 이전5페이지 [1][2][3][4][5] 다음5페이지 이전5페이지 [6][7][8][9][10] 다음5페이지
		 * 와같이 페이지바라고 부른다. statrtPageNo는 1또는 6이 된다.
		 */
		int loop = 0; // statrtPageNo이 값이 증가할때마다 1씩 증가하는 용도
		int blockSize = 5; // 페이지바에 보여줄 페이지 갯수

		if (pageNo == null) {
			currentShowPageNo = 1;
		} else {
			currentShowPageNo = Integer.parseInt(pageNo); // GET방식으로 파라미터
															// pageNo에 넘어온 값을 현재
															// 보여주고자 하는 페이지로 한다.
		}

		// 가져올 게시글의 범위를 구한다.(공식)
		/*
		 * currentShowPageNo start end 1page 1 5 2page 6 10 3page 11 15 4page 16
		 * 20 5page 21 25 6page 26 30 7page 31 35
		 */
		start = ((currentShowPageNo - 1) * sizePerPage) + 1;
		end = start + sizePerPage - 1;

		
		
		
		// 페이징처리를 위해 start end를 map에 추가하여 파라미터로 넘겨서 select되도록 한다.
		map.put("start", String.valueOf(start));
		map.put("end", String.valueOf(end));
		map.put("userSeq", userSeq);
		
		List<HashMap<String,String>> myReviewList = service.myReviewList(map);
		// List<String> myReviewList = service.myReviewList(userSeq);

		
		// 총 게시물건수를 구한다.
		totalCount = service.userReviewCount(map);
		/*System.out.println("게시물 총갯수 : " + totalCount);*/
		totalPage = (int) Math.ceil((double) totalCount / sizePerPage);

		// 이제부터 페이지바 작업을 한다.
		String pageBar = "";
		pageBar += "<ul class='pagination'>";

		/*
		 * 우리는 위에서 blocksize를 5로 설정했으므로 이전5페이지 [1][2][3][4][5] 다음5페이지 로 나와야 한다.
		 * 페이지 번호는 1씩 증가하므로 페이지번호를 증가시켜주는 반복변수가 필요하다. 위에서 선언한 loop를 사용한다. 이때
		 * loop는 blocksize의 크기보다 크면 안된다.
		 */
		loop = 1;
		/*************************
		 * 페이지바의 시작 페이지 번호(startPageNo)값 만들기
		 **************************/
		startPageNo = ((currentShowPageNo - 1) / blockSize) * blockSize + 1;
		/*
		 * 현재 우리는 blockSize를 위에서 5로 설정했다. 만약에 조회하고자 하는 currentShowPageNo가
		 * 3페이지이라면((3 - 1)/5) * 5 + 1 ==>1 만약에 조회하고자 하는 currentShowPageNo가
		 * 7페이지이라면((7 - 1)/5) * 5 + 1 ==>6
		 */

		// ***** 이전 5페이지 만들기 *****
	
		if(startPageNo == 1) {
			if(((startPageNo - blockSize) > 0) ) {
				pageBar += String.format("<li><a href='/mazzipmetro/myReviewList.eat?pageNo=%d'>[이전%d페이지]</a></li>", startPageNo-1, blockSize ) + "&nbsp;";
			}
		} else {
				pageBar += String.format("<li><a href='/mazzipmetro/myReviewList.eat?pageNo=%d'>[이전%d페이지]</a></li>", startPageNo-1, blockSize ) + "&nbsp;";
			
		}
		
		while (!(loop > blockSize || startPageNo > totalPage)) {

			if (currentShowPageNo == startPageNo) {
				pageBar += String.format("<li><span style='color:red; font-weight:bold; text-decoration:underline;'>%d</span></li>", startPageNo) + "&nbsp;";
			} else {
				pageBar += String.format("<li><a href='/mazzipmetro/myReviewList.eat?pageNo=%d'>%d</a></li>", startPageNo, startPageNo ) + "&nbsp;";
			}
			loop ++;
			startPageNo++;
		} // end of while

		// ***** 다음 5페이지 만들기 *****

		if(totalPage > startPageNo) {
			pageBar += String.format("<li><a href='/mazzipmetro/myReviewList.eat?pageNo=%d'>[다음%d페이지]</a></li>", startPageNo, blockSize ) + "&nbsp;";
		}

		pageBar += "</ul>";

		

		req.setAttribute("myReviewList", myReviewList);
		req.setAttribute("pageBar", pageBar);
		
		return "user/myReviewList";
	}
	

	@RequestMapping(value="/userAliasList.eat", method={RequestMethod.GET})
	public String login_userAliasList(HttpServletRequest req,HttpServletResponse res, HttpSession session) {
		UserVO loginUser =  (UserVO)session.getAttribute("loginUser");
		
	
		List<UserAliasVO> userGuAliasList = service.getUserGuAliasList(loginUser.getUserSeq());
		

		List<UserAliasVO> userDongAliasList = service.getUserDongAliasList(loginUser.getUserSeq());
		
	
		List<UserAliasVO> userMetroAliasList = service.getUserMetroAliasList(loginUser.getUserSeq());
		
	
		List<UserAliasVO> userRestTagAliasList = service.getUserRestTagAliasList(loginUser.getUserSeq());
		
		
		
		req.setAttribute("userGuAliasList", userGuAliasList);
		req.setAttribute("userDongAliasList", userDongAliasList);
		req.setAttribute("userMetroAliasList", userMetroAliasList);
		req.setAttribute("userRestTagAliasList", userRestTagAliasList);
		
		
		return "user/userAliasList";
	}
	
	
	
	@RequestMapping(value="/updateUserGrade.eat", method={RequestMethod.GET})
	public String updateUserGrade(HttpServletRequest req,HttpServletResponse res, HttpSession session) {
		UserVO loginUser =  (UserVO)session.getAttribute("loginUser");
		String gradeSeq = req.getParameter("gradeSeq");
		
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", loginUser.getUserSeq());
		
		HashMap<String,Object> resultMap = new HashMap<String,Object>();
		
		if("UG6".equals(gradeSeq)){
			resultMap = service.updateUserGrade(loginUser.getUserEmail(), gradeSeq);

			if((int)resultMap.get("result") >= 3){
				UserVO userVO = service.getLoginUser(loginUser.getUserEmail());
				loginUser.setGradeName("달인");
				loginUser.setUserPoint(userVO.getUserPoint());	
				session.setAttribute("loginUser", loginUser);
				
				req.setAttribute("msg", "달인으로 등급업 성공!!");
				req.setAttribute("script", "location.href='userMyPage.eat'; self.close(); opener.location.reload(true);");
			}else{
				req.setAttribute("msg", (String)resultMap.get("failReason"));
				req.setAttribute("script", "location.href='userMyPage.eat'; self.close(); opener.location.reload(true);");
			}
			
		}else if("UG7".equals(gradeSeq)){
			resultMap = service.updateUserGrade(loginUser.getUserEmail(), gradeSeq);

			if((int)resultMap.get("result") >= 3){
				UserVO userVO = service.getLoginUser(loginUser.getUserEmail());
				loginUser.setGradeName("신");
				loginUser.setUserPoint(userVO.getUserPoint());	
				session.setAttribute("loginUser", loginUser);
				
				req.setAttribute("msg", "신으로 등급업 성공!!");
				req.setAttribute("script", "location.href='userMyPage.eat'; self.close(); opener.location.reload(true);");
			}else{
				req.setAttribute("msg", (String)resultMap.get("failReason"));
				req.setAttribute("script", "location.href='userMyPage.eat'; self.close(); opener.location.reload(true);");
			}
		}
		
		return "admin/msgEnd";
	}

	@RequestMapping(value="/reviewDelete.eat", method={RequestMethod.POST})
	public String reviewDelete(UserVO vo, HttpServletRequest req, HttpSession session) {
		String reviewSeq = req.getParameter("reviewSeq");
		
		int del = service.reviewDelete(reviewSeq);
		
		req.setAttribute("del", del);
		
		return "user/reviewDelete";
	}
	
	//리뷰쓰기 후 포인트와 exp를 준다. 그리고 등급체크를 한다. userReviewAddAfter
	@RequestMapping(value="/userReviewAddAfter.eat", method={RequestMethod.GET})
	public String userReviewAddAfter(HttpServletRequest req, HttpSession session){
		UserVO loginUser = (UserVO)session.getAttribute("loginUser");
		String restSeq = req.getParameter("restSeq");
		String userSeq = req.getParameter("userSeq");
		
		System.out.println("**********************************"+restSeq);
		System.out.println("**********************************"+userSeq);
		
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", loginUser.getUserSeq());
		hashMap.put("type", "리뷰쓰기");
		hashMap.put("restSeq", restSeq);
		hashMap.put("userSeq", userSeq);
		
		int result1 = service.isFirstReview(hashMap);
		int result2 = 0;
		
		if(result1 == 1){ //사용자가 해당 업장의 리뷰에 처음 쓸 경우 
			result2 = service.updateUserPointAndExp(hashMap);
			
			if(result2 == 1){
				/*디비에서 변경된 값을  세션에 있는 로그인유저에게 변경*/
				UserVO userVO = service.getLoginUser(loginUser.getUserEmail());
				loginUser.setUserPoint(userVO.getUserPoint());
				loginUser.setUserExp(userVO.getUserExp());
				
				session.setAttribute("loginUser", loginUser);
				
				req.setAttribute("script", " alert('15포인트와 15경험치가 지급되었습니다.'); location.href='userGradeCheck.eat';  ");
			}else{
				req.setAttribute("script", "alert('리뷰쓰기 후 포인트,EXP 업데이트 실패');  self.close(); opener.location.reload(true);  ");
			}
		}else{
			req.setAttribute("script", "alert('이미 리뷰를 쓰셨습니다.');  self.close(); opener.location.reload(true);  ");
		}
		
		
		return "/user/msgEnd";
	}

}
