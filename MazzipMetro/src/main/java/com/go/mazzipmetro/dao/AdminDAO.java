package com.go.mazzipmetro.dao;

import java.util.HashMap;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.go.mazzipmetro.vo.ContentVO;
import com.go.mazzipmetro.vo.UserVO;

@Repository
public class AdminDAO implements IDAO{

	@Autowired
	private SqlSessionTemplate sqlSession;

	public List<UserVO> list(HashMap<String, String> map) {
		List<UserVO> list = sqlSession.selectList("admin.list", map); 
		
		return list;
	}

	public int getTotalCount(HashMap<String, String> map) {
		int count = sqlSession.selectOne("admin.getTotalCount", map); 
		return count;
	}// end of getTotalCount(HashMap<String, String> map)-------------

	public int userDel(HashMap<String, String> map) {
		int n = sqlSession.update("admin.userDel", map);
		return n;
	}

	public List<HashMap<String, String>> conTentList(HashMap<String, String> map) {
		List<HashMap<String, String>> list = sqlSession.selectList("admin.conTentList", map); 
		return list;
	}

	public int getConTotalCount(HashMap<String, String> map) {
		int count = sqlSession.selectOne("admin.getConTotalCount", map); 
		return count;
	}

	
	
}
