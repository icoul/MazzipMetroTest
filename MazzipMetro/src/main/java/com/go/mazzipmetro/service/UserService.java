package com.go.mazzipmetro.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.go.mazzipmetro.dao.ReviewDAO;
import com.go.mazzipmetro.dao.UserDAO;
import com.go.mazzipmetro.vo.GradeVO;
import com.go.mazzipmetro.vo.RestaurantVO;
import com.go.mazzipmetro.vo.UserAliasVO;
import com.go.mazzipmetro.vo.UserAttendVO;
import com.go.mazzipmetro.vo.UserVO;

@Service
public class UserService implements IService {

	@Autowired
	private UserDAO dao;

	@Autowired
	private ReviewDAO reviewDao;
	
	public int userRegister(UserVO vo) {
		int n = dao.userRegister(vo);
		return n;
	}

	
	public List<String> alignTest() {
		List<String> list = dao.alignTest();
		return list;
	}
	

	public int UserLogin(HashMap<String, String> map) {
		int n = dao.UserLogin(map);
		return n;
	}

	public UserVO getLoginUser(String userEmail) {
		UserVO loginuser = dao.getLoginUser(userEmail);
		return loginuser;
	}

	public int userEdit(UserVO vo) {
		int n = dao.userEdit(vo);
		return n;
	}


	public int userPoint(String userSeq) {
		int userPoint = dao.userPoint(userSeq);
		return userPoint;
	}


	public int restCount(String userSeq) {
		int restCount = dao.restCount(userSeq);
		return restCount;
	}


	public int userContent(String userSeq) {
		int userContent = dao.userContent(userSeq);
		return userContent;
	}


	public int userCoupon(String userSeq) {
		int userCoupon = dao.userCoupon(userSeq);
		return userCoupon;
	}


	public int userReviewCount(String userSeq) {
		int reviewCount = dao.reviewCount(userSeq);
		return reviewCount;
	}


	public int userQnaCount(String userSeq) {
		int qnaCount = dao.qnaCount(userSeq);
		return qnaCount;
	}

	public int userExist(String userSeq) {
		int isUserExist = dao.userExist(userSeq);
		return isUserExist;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int insertAttend(String userSeq) {
		int n =  dao.insertAttend(userSeq);
		
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", userSeq);
		hashMap.put("type", "처음출석체크");
		
		int m = dao.updateUserPointandExp(hashMap);
		
		return (n+m);
	}


	public int userLoginToday(String userSeq) {
		int n = dao.userLoginToday(userSeq);
		return n;
	}


	public UserAttendVO getUserAttend(String userSeq) {
		UserAttendVO vo = dao.getUserAttend(userSeq);
		return vo;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int updateUserPoint_RandomBox(HashMap<String, String> hashMap) {
		int n = dao.updateUserPoint2(hashMap);
		int m = dao.updateRandomBox(hashMap);
		return n+m;
	}


	public int updateUserAttend(HashMap<String, String> hashMap) {
		int n = dao.updateUserAttend(hashMap);
		return n;
	}


	public int userLoginContinueCheck(HashMap<String, String> hashMap) {
		int isLoginContinue = dao.userLoginContinueCheck(hashMap);
		return isLoginContinue;
	}


	public List<HashMap<String,String>> myReviewList(HashMap<String, String> map) {
		List<HashMap<String,String>> myReviewList = dao.myReviewList(map);
		return myReviewList;
	}


	public int reviewDelete(String reviewSeq) {
		int del = dao.reviewDelete(reviewSeq);
		return del;
	}


	public int userReviewCount(HashMap<String, String> map) {
		int count = dao.userReviewCount(map);
		return count;
	}


	public int emailDuplicatecheck(String userEmail) {
		int result = dao.emailDuplicatecheck(userEmail);
		return result;
	}


	

	public HashMap<String, String> userGradeCheck(String email) {
		
		UserVO uservo = dao.getLoginUser(email);
		
		HashMap<String, String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", uservo.getUserSeq());
		
		List<GradeVO> userGradeList = dao.getUserGradeList();
		
		int n = 0;
		String gradeSeq = "";
		
		for(int i = 0; i < userGradeList.size(); ++i){
			
			if("UG2".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG1")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG2";
				hashMap.put("gradeSeq", gradeSeq);
				n = dao.updateUserGrade(hashMap);
			}else if("UG3".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG2")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG3";
				hashMap.put("gradeSeq", gradeSeq);
				n = dao.updateUserGrade(hashMap);
			}else if("UG4".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG3")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG4";
				hashMap.put("gradeSeq", gradeSeq);
				n = dao.updateUserGrade(hashMap);
			}else if("UG5".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG4")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG5";
				hashMap.put("gradeSeq", gradeSeq);
				n = dao.updateUserGrade(hashMap);
			}else if("UG6".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG5")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG6";
				n = 1;
			}else if("UG7".equals(userGradeList.get(i).getGradeSeq()) 
				&& uservo.getGradeSeq().equals("UG6")
				&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())){
				
				gradeSeq = "UG7";
				n = 1;
			}
		}
		
		
		String userGradeName = dao.getUserGradeName(gradeSeq); //등급이름 가져오기
		
		HashMap<String, String> resultHashMap = new HashMap<String, String>();
		resultHashMap.put("result", String.valueOf(n)); //등급업이 되면 반환되는 결과값 1이 등급업, 0이 실패
		resultHashMap.put("userGradeName", userGradeName);       //알림창에 어떤 등급으로 업했는지 알려주기
		resultHashMap.put("gradeSeq", gradeSeq);
		return resultHashMap;
	}


	public List<UserAliasVO> getUserGuAliasList(String userSeq) {
		List<UserAliasVO> userGuAliasList = dao.getUserGuAliasList(userSeq);
		return userGuAliasList;
	}


	public List<UserAliasVO> getUserDongAliasList(String userSeq) {
		List<UserAliasVO> userDongAliasList = dao.getUserDongAliasList(userSeq);
		return userDongAliasList;
	}


	public List<UserAliasVO> getUserMetroAliasList(String userSeq) {
		List<UserAliasVO> userMetroAliasList = dao.getUserMetroAliasList(userSeq);
		return userMetroAliasList;
	}


	public List<UserAliasVO> getUserRestTagAliasList(String userSeq) {
		List<UserAliasVO> userRestTagAliasList = dao.getUserRestTagAliasList(userSeq);
		return userRestTagAliasList;
	}


	public int getUserAliasCount(HashMap<String,String> hashMap) {
		int userAliasCount = dao.getUserAliasCount(hashMap);
		return userAliasCount;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int updateUserGrade(String email) {
		UserVO uservo = dao.getLoginUser(email);

		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("userSeq", uservo.getUserSeq());
		
		List<GradeVO> userGradeList = dao.getUserGradeList();

		int n = 0;
		int m = 0;

		for (int i = 0; i < userGradeList.size(); ++i) {

			if ("UG6".equals(userGradeList.get(i).getGradeSeq()) && uservo.getGradeSeq().equals("UG5")
					&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())) {
				hashMap.put("gradeSeq", "UG6");
				
				n = dao.updateUserPoint(hashMap);
				m = dao.updateUserGrade(hashMap);
			} else if ("UG7".equals(userGradeList.get(i).getGradeSeq()) && uservo.getGradeSeq().equals("UG6")
					&& Integer.parseInt(userGradeList.get(i).getGradeExp()) <= Integer.parseInt(uservo.getUserExp())) {
				hashMap.put("gradeSeq", "UG7");
				
				n = dao.updateUserPoint(hashMap);
				m = dao.updateUserGrade(hashMap);
			
			}

		}
		return (n+m);
	}
	
	public int userWithdrawal(String userSeq) {
		int result = dao.userWithdrawal(userSeq);
		return result;
	}

	public String getUserEmail(HashMap<String, String> map) {
		String userEmail = dao.getUserEmail(map);
		return userEmail;
	}


	public int getUserExists(HashMap<String, String> map) {
		int pwCount = dao.getUserExists(map);
		return pwCount;
	}


	public int updatePwdUser(HashMap<String, String> map) {
		int pwNewCount = dao.updatePwdUser(map);
		return pwNewCount;
	}


	public int updateUserPointAndExp(HashMap<String, String> hashMap) { //리뷰쓰기 후 유저에게 15포인트와 15EXP를 주는 함수
		int n = dao.updateUserPointandExp(hashMap);
		return n;
	}


	public int isFirstReview(HashMap<String, String> hashMap) {
		int n = reviewDao.isFirstReview(hashMap);
		return n;
	}


	public HashMap<String, String> grantCoupon(HashMap<String, String> hashMap) {
		
		HashMap<String, String> resultMap = new HashMap<String, String>();
		
		int boxCount = 0;
		if(hashMap.get("boxType").equals("random")){
			 boxCount = Integer.parseInt(dao.getUserAttend(hashMap.get("userSeq")).getUserRandomBox());
		}else if(hashMap.get("boxType").equals("premium")){
			boxCount = Integer.parseInt(dao.getUserAttend(hashMap.get("userSeq")).getUserPremiumRandomBox());
		}
		
		if(boxCount > 0){
			int n = dao.minusRandomBox(hashMap);
			resultMap.put("result", String.valueOf(n+1));
		}else{
			resultMap.put("result", String.valueOf(0));
			resultMap.put("failReason", (hashMap.get("boxType").equals("random") ? "랜덤박스": "프리미엄 랜덤박스") + "가 없습니다.");
		}
		
		
		//int m = dao.insertCoupon();
		return resultMap;
	}
	

}


	


