package com.go.mazzipmetro.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.go.mazzipmetro.dao.ReviewDAO;
import com.go.mazzipmetro.vo.AttachFileVO;
import com.go.mazzipmetro.vo.RestaurantVO;
import com.go.mazzipmetro.vo.ReviewCommentVO;
import com.go.mazzipmetro.vo.ReviewVO;
import com.go.mazzipmetro.vo.UserAliasVO;


@Service
public class ReviewService implements IService{

	@Autowired
	private ReviewDAO dao;
	
	/*public List<HashMap<String,String>> getReviewList(String restseq) {
		List<HashMap<String,String>> reviewList = dao.getReviewList(restseq);
		return reviewList;
	}*/

	// 리뷰 이미지배열을 담은 배열 가져오기
	public List<List<String>> getReviewImageList(List<String> reviewSeq) {
		List<List<String>> reviewImageList = dao.getReviewImageList(reviewSeq);
		return reviewImageList;
	}

	public List<HashMap<String, String>> getReviewImageList(String reviewseq) {
		List<HashMap<String,String>> reviewImageList = dao.getReviewImageList(reviewseq);
		return reviewImageList;
	}

	public String getLargeReviewImageName(String revimgseq) {
		String reviewImageName = dao.getLargeReviewImageName(revimgseq);
		return reviewImageName;
	}

	public List<HashMap<String, String>> getAgeLineChartList(String restseq) {
		List<HashMap<String,String>> agelineChartList = dao.getAgeLineChartList(restseq);
		return agelineChartList;
	}

	public List<HashMap<String, String>> getGenderChartList(String restseq) {
		List<HashMap<String,String>> genderChartList = dao.getGenderChartList(restseq);
		return genderChartList;
	}
	
	public int add_file(AttachFileVO avo) {
		
		
		//int result = dao.add_file(avo);
		int result = 0;
		return result;
	}

	// 리뷰 쓰기 및 칭호 업데이트
		@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
		public List<String> addReview(ReviewVO rvo, ArrayList<String> imageList, String[] themeArr, HashMap<String, String> visitor) {
			
			HashMap<String, String> map = new HashMap<String, String>(); 
			String restSeq = rvo.getRestSeq();
			
			int result = dao.addReview(rvo);
			if(result == 1)
			{
				// 리뷰 시퀀스 얻어오기
				String reviewSeq = dao.getReviewSeq(rvo);
							
				if(reviewSeq != null)
				{
					visitor.put("reviewSeq", reviewSeq);
					// 방문자 테이블에 추가하기
					dao.insetVisitor(visitor);
					if(themeArr != null)
					{
						// 리뷰 시퀀스로 테마 넣기
						dao.addTheme(themeArr, reviewSeq, restSeq);
					}
					map.put("reviewSeq", reviewSeq);
					for(int i =0; i<imageList.size(); i++)
					{
						System.out.println(imageList.get(i));
						map.put("reviewImg", imageList.get(i));
						
						//이미지 넣기
						result = dao.addReviewImg(map); 
					}
				}
				else
				{
					System.out.println("reviewSeq를 받아오지 못했습니다.");
				}
				
			}
			
			// 동현_사용자 칭호 추가 및 업데이트
			// 사용자 업데이트 된 칭호이름을 담을 list 생성
			List<String> msgList = new ArrayList<>();
			
			//중복 업장 체크
			map.put("userSeq", rvo.getUserSeq());
			map.put("restSeq", rvo.getRestSeq());
			
			// tbl_review에 사용자가 썼던 리뷰가 있는지 확인한다.
			int n = dao.isFirstReview(map);
			System.out.println(">>>>>>>>>>>>>>>>> 현재까지 결과값 = "+result);
			System.out.println(">>>>>>>>>>>>>>>>> 사용자가 쓴 이 업장에 쓴 리뷰수 = "+n);// commit이 안된 상태라 0이 찍힐 줄 예상했지만, 현재 n = 1이다.
			
			// 이 리뷰가 첫리뷰인 경우
			if (n == 1) {
				result = 0;
				
				// 해당 업장 정보를 가져온다.
				RestaurantVO restVO = dao.getRestaurant(rvo.getRestSeq());
				
				//HashMap<String, String>map = new HashMap<String, String>();
				map.put("userSeq", rvo.getUserSeq());
				
				// 칭호테이블 존재여부 검사 : 지하철, 동, 구
				//지하철 칭호
				map.put("aliasType", "metroId");
				map.put("aliasId", restVO.getMetroId());
				if (dao.isAliasExist(map)) {
					UserAliasVO uavo = dao.getUserAlias(map);
					
					if (uavo.getAliasNum() == 4) {				
						// aliasNum이 4인경우(숙련자로 레벨업)
						map.put("aliasName", "숙련자");
						
					} else if (uavo.getAliasNum() == 9) {
						// aliasNum이 9인경우(마스터로 레벨업)
						map.put("aliasName", "마스터");
						
					} 
					// update
					result += dao.userAliasUpdate(map);
					
					// 업데이트 된 내용이 있을 경우에 msgList 에 담는다.
					if(map.containsKey("aliasName")){
						msgList.add(restVO.getMetroName()+" "+map.get("aliasName"));
					}
					
				} else {
					// 입문자칭호 부여
					result += dao.userAliasInsert(map);
					
					// insert 된 경우에 해당 alias Name을 담는다.
					msgList.add(restVO.getMetroName()+" 입문자");
				
				}//지하철 칭호 
				
				// 동 칭호
				boolean expert_dongUpdateFlag= false;
				boolean master_dongUpdateFlag= false;
				map.put("aliasType", "dongId");
				map.put("aliasId", restVO.getDongId());
				map.remove("aliasName");
				if (dao.isAliasExist(map)) {
					UserAliasVO uavo = dao.getUserAlias(map);
					
					if (uavo.getAliasNum() == 4) {				
						// aliasNum이 4인경우(숙련자로 레벨업)
						map.put("aliasName", "숙련자");
						expert_dongUpdateFlag = true;
						
					} else if (uavo.getAliasNum() == 9) {
						// aliasNum이 9인경우(마스터로 레벨업)
						map.put("aliasName", "마스터");
						master_dongUpdateFlag = true;
						
					} 
					// update
					result += dao.userAliasUpdate(map);
					

					if(map.containsKey("aliasName")){
						msgList.add(restVO.getDongName()+" "+map.get("aliasName"));
					}
					
				} else {
					// 입문자칭호 부여
					result += dao.userAliasInsert(map);
					// insert 된 경우에 해당 alias Name을 담는다.
					msgList.add(restVO.getDongName()+" 입문자");
				
				}// 동 칭호 끝
				
				// 구 칭호
				map.put("aliasType", "guId");
				map.put("aliasId", restVO.getGuId());
				map.remove("aliasName");
				if (dao.isAliasExist(map)) {
					
					// 동 칭호가 업데이트내역이 있다면 구 업데이트 여부를 체크한다.
					if (master_dongUpdateFlag || expert_dongUpdateFlag) {
						// 해당 구에 동 리스트 구하기
						List<String> dongList = dao.getDongList(restVO.getGuId());
						List<Integer> dongAliasNumList = new ArrayList<>();
						
						for (String dongId : dongList) {
							HashMap<String, String> dongMap = new HashMap<>();
							dongMap.put("userSeq", rvo.getUserSeq());
							dongMap.put("dongId", dongId);
							
							// 동칭호의 aliasNum을 구한다.
							// 해당 칭호가 없다면 null 이 대입되고, nullPointer 에러가 난다. 
							int dongAliasNum = 0;
							if (dao.checkDongAliasNum(dongMap) > 0) {
								dongAliasNum = dao.getDongAliasNum(dongMap);								
								System.out.println(">>>>>>>>>>>>>>>>>"+dongAliasNum); 
								
								// 기준이 될 숫자를 선언한다. 숙련자는 5, 마스터는 10이다.
								int barNum = (expert_dongUpdateFlag)?5:10;
								
								// 모든 동의 aliasNum 이 barNum 이상인 경우만 리스트에 담는다.
								if (dongAliasNum >= barNum) {
									dongAliasNumList.add(dongAliasNum);
								}
							}
						}
						
						// 구의 모든 동의 칭호를 가지고 있는 경우(방금 얻은 칭호를 제외한 개수와 같다면)
						if (dongAliasNumList.size() == dongList.size()-1) {
							if (master_dongUpdateFlag) {
								map.put("aliasName", "마스터");
							} else {
								map.put("aliasName", "숙련자");
							}
						}
						
					} // end of  if (master_dongUpdateFlag || expert_dongUpdateFlag)
					
					// update
					result += dao.userAliasUpdate(map);
					
					if(map.containsKey("aliasName")){
						msgList.add(restVO.getDongName()+" "+map.get("aliasName"));
					}
					
				} else {
					// 입문자칭호 부여
					result += dao.userAliasInsert(map);
					// insert 된 경우에 해당 alias Name을 담는다.
					msgList.add(restVO.getGuName()+" 입문자");
				
				}	// 구 칭호 끝
				
				// 칭호테이블 존재여부 검사 : 대분류, 중분류
				// 대분류 칭호 
				String[] bgTagArr = rvo.getReviewBgTag();
				
				if (bgTagArr != null) {
					
					for (String bgTag : bgTagArr) {
						map.put("aliasType", "restBgTag");
						map.put("aliasId", bgTag);
						map.remove("aliasName");
						
						if (dao.isAliasExist(map)) {
							UserAliasVO uavo = dao.getUserAlias(map);
							
							if (uavo.getAliasNum() == 4) {				
								// aliasNum이 4인경우(숙련자로 레벨업)
								map.put("aliasName", "숙련자");
								
							} else if (uavo.getAliasNum() == 9) {
								// aliasNum이 9인경우(마스터로 레벨업)
								map.put("aliasName", "마스터");
								
							} 
							// update
							result += dao.userAliasUpdate(map);
							
							// 업데이트 된 내용이 있을 경우에 msgList 에 담는다.
							if(map.containsKey("aliasName")){
								msgList.add(bgTag+" "+map.get("aliasName"));
							}
							
						} else {
							// 입문자칭호 부여
							result += dao.userAliasInsert(map);
							// insert 된 경우에 해당 alias Name을 담는다.
							msgList.add(bgTag+" 입문자");
						}
						
					}// end of for (String bgTag : bgTagArr)
				}
				
				// 중분류 칭호
				String[] mdTagArr = rvo.getReviewMdTag();
				
				if (mdTagArr != null) {
					
					for (String mdTag : mdTagArr) {
						map.put("aliasType", "restMdTag");
						map.put("aliasId", mdTag);
						map.remove("aliasName");
						
						if (dao.isAliasExist(map)) {
							UserAliasVO uavo = dao.getUserAlias(map);
							
							if (uavo.getAliasNum() == 4) {				
								// aliasNum이 4인경우(숙련자로 레벨업)
								map.put("aliasName", "숙련자");
								
							} else if (uavo.getAliasNum() == 9) {
								// aliasNum이 9인경우(마스터로 레벨업)
								map.put("aliasName", "마스터");
								
							} 
							// update
							result += dao.userAliasUpdate(map);
							
							// 업데이트 된 내용이 있을 경우에 msgList 에 담는다.
							if(map.containsKey("aliasName")){
								msgList.add(mdTag+" "+map.get("aliasName"));
							}
							
						} else {
							// 입문자칭호 부여
							result += dao.userAliasInsert(map);
							// insert 된 경우에 해당 alias Name을 담는다.
							msgList.add(mdTag+" 입문자");
						}
					} //end of  for (String mdTag : mdTagArr) 
				}
				
				// 총 결과값의 합을 구해서 성공여부를 피드백한다.
				int totalNum = 3 + ((bgTagArr == null)?0:bgTagArr.length)+((mdTagArr == null)?0:mdTagArr.length);
				
				if(result == totalNum){
					result = 1;// 업무 실행 성공!
				} else {
					result = 0;// 업무 실패!
				}
				
				// msgList에 업무 성공여부를 문자변환 후 마지막 인자로 담는다.
				msgList.add(String.valueOf(result));
				
				// 확인용 msgList 출력!!
				System.out.println(">>>>>>>>>>>>>>> 확인용 msgList 출력!!"); 
				for (String str : msgList) {
					System.out.println(">>>>>>>>>>"+str); 
				}
				
				return msgList;
				
			} else{ // 해당 업장에 첫번째 리뷰가 아닌 경우
				
				msgList.add(String.valueOf(result));
				return msgList;
			}
			
			//return result;
		}// end of addReview
	
	// 총리뷰수 알아오기 
	public int getTotalCount(String restSeq) {

		int result = dao.getTotalCount(restSeq);
		
		return result;
	}

	public int plusHit(String reviewSeq) {
		int result = dao.upReviewHit(reviewSeq);
		return result;
	}

	public int getHitScore(String reviewSeq) {
		int HitScore = dao.getReviewHit(reviewSeq);
		return HitScore;
	}

	public int insertLiker(HashMap<String, String> map) {
		int insertLiker =dao.insertLiker(map);
		return insertLiker;
	}

	public List<String> getLikers(String UserSeq) {
		List<String> likers = dao.getLikers(UserSeq);
		return likers;
	}

	public List<HashMap<String, String>> getRealReview(HashMap<String, String> map) {
		List<HashMap<String, String>> list = dao.getRealReview(map);
		return list;
	}

	public int getMyReviewCount(String restSeq, String userSeq) {
		int reviewCount = dao.getMyReviewCount(restSeq, userSeq);
		return reviewCount;
	}
	@Transactional
	public int delLiker(String reviewSeq, String userSeq) {
		
		int n = dao.getReviewDownHit(reviewSeq);
		int m = dao.delLiker(reviewSeq, userSeq);
		return (n+m);
	}
	
	// 해당 회원의 리뷰 1개를 가져온다(조건 : 지하철 Id)
	public List<HashMap<String, String>> getBestReview(List<String> userSeqList, String metroId) {
		
		List<HashMap<String, String>> bestReview = dao.getBestReview(userSeqList, metroId);
		
		return bestReview;
		
	}
	
	// 리뷰쓰기 폼에 해당 업체의 정보를 담아주기 위한 데이터
	public HashMap<String, String> getRestaurant(String restSeq) {
		HashMap<String, String> getRest = dao.getRest(restSeq);
		return getRest;
	}

	//한 업장의 분위기, 가격, 서비스, 맛 , 총 평점의 평점을 가져온다.
	public HashMap<String, String> getReviewAvgScore(String restSeq) {
		HashMap<String, String> reviewAvgScore = dao.getReviewAvgScore(restSeq);
		return reviewAvgScore;
	}

	public int insertReviewComment(String userSeq, String reviewSeq, String comment, String groupNo, String commentSeq, String depthNo) {
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", userSeq);
		hashMap.put("reviewSeq", reviewSeq);
		hashMap.put("comment", comment);
		hashMap.put("groupNo", groupNo);
		hashMap.put("commentSeq", commentSeq);
		hashMap.put("depthNo", depthNo);

		
		int result = dao.insertReviewComment(hashMap);
		return result;
	}

	public String getReviewCommentMaxGroupNo() {
		String groupNo = dao.getReviewCommentMaxGroupNo();
		return groupNo;
	}

	public List<ReviewCommentVO> getReviewCommentList(HashMap<String,String> hashMap) {
		List<ReviewCommentVO> reviewCommentList = dao.getReviewCommentList(hashMap);
		return reviewCommentList;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int insertCommmentComment(String userSeq, String reviewSeq, String comment, String commentSeq,
			String groupNo, int depthNo) {
		HashMap<String,String> hashMap = new HashMap<String,String>();
		hashMap.put("userSeq", userSeq);
		hashMap.put("reviewSeq", reviewSeq);
		hashMap.put("comment", comment);
		hashMap.put("commentSeq", commentSeq);
		hashMap.put("groupNo", groupNo);
		hashMap.put("depthNo", String.valueOf(depthNo));
		
		int result = dao.insertReviewComment(hashMap);
		
		int result2 = dao.updateReviewComment(hashMap);
		
		return (result+result2);
	}

	public int getReviewCommentTotalCount(String reviewSeq) {
		int reviewCommentTotalCount = dao.getReviewCommentTotalCount(reviewSeq);
		return reviewCommentTotalCount;
	}

	public List<ReviewCommentVO> getCommentCommentList(String commentSeq) {
		List<ReviewCommentVO> commentCommentList = dao.getCommentCommentList(commentSeq);
		return commentCommentList;
	}

	public int getCommentCount(String commentSeq) {
		int commentCount = dao.getCommentCount(commentSeq);
		return commentCount;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int deleteReviewCommentWithComment(String commentSeq) {
		int result1 = dao.deleteCommentComment(commentSeq);
		int result2 = dao.deleteReviewComment(commentSeq);
		return (result1+result2);
	}

	public int deleteReviewComment(String commentSeq) {
		int result = dao.deleteReviewComment(commentSeq);
		return result;
	}

	@Transactional(propagation=Propagation.REQUIRED, isolation= Isolation.READ_COMMITTED, rollbackFor={Throwable.class})
	public int deleteCommentComment(String commentSeq,String fk_seq) {
		int result1 = dao.deleteReviewComment(commentSeq);
		int result2 = dao.updateCommentCount(fk_seq);
		return (result1+result2);
	}


}
