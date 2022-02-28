package kr.co.kosmo.mvc.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import kr.co.kosmo.mvc.dao.LikeItDAO;
import kr.co.kosmo.mvc.dto.LikeItVO;

@Transactional
@Repository
public class LikeItService implements LikeItDAO{
	@Autowired
	private SqlSessionTemplate ss;
	
	@Override
	public int getSongLikeCnt(int song_id) {
		return ss.selectOne("like_it.getSongLikeCnt", song_id);		
	}

	@Override
	public int getLikeIt(LikeItVO likeVO) {
		return ss.selectOne("like_it.getLikeIt", likeVO);
	}
	
	@Override
	public void insertLike(LikeItVO likeVO) {
		ss.insert("like_it.insertLike", likeVO);
	}

	@Override
	public void deleteLike(LikeItVO likeVO) {
		ss.delete("like_it.deleteLike", likeVO);
	}
	
}