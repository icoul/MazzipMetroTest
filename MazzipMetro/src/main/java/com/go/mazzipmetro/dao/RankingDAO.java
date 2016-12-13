package com.go.mazzipmetro.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RankingDAO {
	
	@Autowired
	private SqlSessionTemplate sqlSession;

	// 지하철 데이터를 받아오는 메서드
	public List<HashMap<String, String>> getMetro() {
		List<HashMap<String, String>> metro = sqlSession.selectList("ranking.getMetro");
		return metro;
	}

	// 동 데이터를 받아오는 메서드
	public List<HashMap<String, String>> getDong() {
		List<HashMap<String, String>> dong = sqlSession.selectList("ranking.getDong");
		return dong;
	}

	// 지하철 검색시 자동글완성을 해주는 메서드
	public List<HashMap<String, String>> searchMetroName(String metroName) {
		
		List<HashMap<String, String>> metro = sqlSession.selectList("ranking.searchMetroName", metroName);
		
		return metro;
	}
	
	// 동 검색시 자동글완성을 해주는 메서드
	public List<HashMap<String, String>> searchDongName(String dongName) {
		
		List<HashMap<String, String>> dong = sqlSession.selectList("ranking.searchDongName", dongName);
		
		return dong;
	}

	public List<HashMap<String, String>> getRestRanking(HashMap<String, Object> optionMap, String regDate) {
		List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		
		if (regDate.equals("0")) { // 기간 상관없이 랭킹
			mapList = sqlSession.selectList("ranking.getNotDateRestRanking", optionMap);
		}
		
		else if (regDate.equals("7")) {// 최근 일주일 랭킹
			mapList = sqlSession.selectList("ranking.getOneWeekRestRanking", optionMap);
		}
		
		else if (regDate.equals("30")) {// 최근 한 달 랭킹
			mapList = sqlSession.selectList("ranking.getOneMonthRestRanking", optionMap);
		}
		
		else if (regDate.equals("90")) {// 최근 3달 랭킹
			mapList = sqlSession.selectList("ranking.getThreeMonthRestRanking", optionMap);
		}
		
		else if (regDate.equals("365")) {// 최근 1년 랭킹
			mapList = sqlSession.selectList("ranking.getOneYearRestRanking", optionMap);
		}
		
		return mapList;
	}

	public List<HashMap<String, String>> getReviewRanking(HashMap<String, Object> optionMap, String regDate) {
		List<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>();
		
		if (regDate.equals("0")) { // 기간 상관없이 랭킹
			mapList = sqlSession.selectList("ranking.getNotDateReviewRanking", optionMap);
		}
		
		else if (regDate.equals("7")) {// 최근 일주일 랭킹
			mapList = sqlSession.selectList("ranking.getOneWeekReviewRanking", optionMap);
		}
		
		else if (regDate.equals("30")) {// 최근 한 달 랭킹
			mapList = sqlSession.selectList("ranking.getOneMonthReviewRanking", optionMap);
		}
		
		else if (regDate.equals("90")) {// 최근 3달 랭킹
			mapList = sqlSession.selectList("ranking.getThreeMonthReviewRanking", optionMap);
		}
		
		else if (regDate.equals("365")) {// 최근 1년 랭킹
			mapList = sqlSession.selectList("ranking.getOneYearReviewRanking", optionMap);
		}
		
		return mapList;
	}
	
}
