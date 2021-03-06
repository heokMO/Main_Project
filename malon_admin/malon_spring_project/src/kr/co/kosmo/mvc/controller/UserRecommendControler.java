package kr.co.kosmo.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.kosmo.mvc.dto.RecommendListVO;
import kr.co.kosmo.mvc.dto.SongVO;
import kr.co.kosmo.mvc.dto.UserRecommendVO;
import kr.co.kosmo.mvc.service.LikeItService;
import kr.co.kosmo.mvc.service.SongService;
import kr.co.kosmo.mvc.service.UserRecommendService;


@Controller
@RequestMapping(value="userrecommend/*")
public class UserRecommendControler {
	@Autowired
	private UserRecommendService urservice;
	
	@Autowired
	private SongService songService;
	
	@Autowired
	private LikeItService likeItService;
	
    @RequestMapping(value="list")
    public String listUp(Model m) {
    	List<UserRecommendVO> recmdList = urservice.get_list_limit();
    	List<String> imgs = new ArrayList<>();
    	
    	for (UserRecommendVO list: recmdList) {
    		String img = urservice.getImg(list.getId());
    		imgs.add(img);
    	}
    	
    	UserRecommendVO lastRecmd =  recmdList.get(recmdList.size() - 1);
    	int lastId = lastRecmd.getId();
    	m.addAttribute("imgs", imgs);
    	m.addAttribute("list", recmdList);
    	m.addAttribute("lastId", lastId);
    	return "userrecommend/list";
    }
    
    @GetMapping(value="detail")
    public String detail(Model m, @RequestParam(name="userRcmId") int userRcmId) {
    	List<SongVO> songList = urservice.getPlayListDetail(userRcmId);
    	
    	m.addAttribute("info", urservice.getInfo(userRcmId));
    	m.addAttribute("play_list", songList);
    	
    	List<Integer> likeCntList = new ArrayList<>();
    	
    	for (SongVO song : songList) {	
    		likeCntList.add(likeItService.getSongLikeCnt(song.getSong_id()));
    	}
    	
    	m.addAttribute("likeCnt", likeCntList);
    	return "userrecommend/detail";
    }
    
	@ResponseBody
	@RequestMapping(value="likeOrNot", produces="application/json;charset=utf8", method=RequestMethod.GET)
	public String getLikeIt(Model m, @RequestParam("playlist_id") int id, HttpSession session) {
    	RecommendListVO recommendListVO = new RecommendListVO();
    	recommendListVO.setMem_acc_id((String) session.getAttribute("sessionId"));
    	recommendListVO.setId(id);
    	String likeIt = Integer.toString(urservice.getlikeit(recommendListVO));
		
    	return likeIt;
	}
    
    @ResponseBody
    @RequestMapping(value="playlistLikeUpdate", produces="application/json;charset=utf8", method=RequestMethod.GET)
    public Map<String, Object> playlistLikeUpdate(Model m, @RequestParam("likeIt") String likeIt
    		, @RequestParam("playlist_id") int id, HttpSession session) {
    	RecommendListVO recommendListVO = new RecommendListVO();
    	recommendListVO.setMem_acc_id((String) session.getAttribute("sessionId"));
    	recommendListVO.setId(id);
    	
    	Map<String, Object> map = new HashMap<>();
    	
    	if (likeIt.equals("0")) {
    		urservice.insertLike(recommendListVO);
    		map.put("listLikeCnt", urservice.getInfo(id).getThumbsUp());
    		map.put("likeIt", 1);
    		return map;
    	} else {
    		map.put("listLikeCnt", urservice.getInfo(id).getThumbsUp());
    		return map;
    	}
    }
    
    @RequestMapping(value="create")
    public String create(Model m, HttpSession session) {
    	if(session.getAttribute("sessionId") == null) {
    		return "main/main";
    	}
    		
    	return "userrecommend/create";
    }
    
    @ResponseBody
	@RequestMapping(value="wordSearchShow.action", method=RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String wordSearchShow(HttpServletRequest request) {			
		String searchWord = request.getParameter("searchWord");
		List<SongVO> songList = urservice.wordSearchShow(searchWord);
		
		String result = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.writeValueAsString(songList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
    
    @ResponseBody
	@RequestMapping(value="songadd", method=RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String songAdd(HttpServletRequest request) {			
		String songId = request.getParameter("song_id");
		SongVO song = songService.getSongDetail(Integer.parseInt(songId));
		
		String result = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.writeValueAsString(song);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
    
    @ResponseBody
	@RequestMapping(value="addRecomendList", method=RequestMethod.GET, produces="text/plain;charset=UTF-8")
	public String addRecomendList(HttpServletRequest request, HttpSession session) {			
		String lastSongId = request.getParameter("last_song_id");
		List<UserRecommendVO> recmdList = urservice.get_list_limit(Integer.parseInt(lastSongId));

    	for (UserRecommendVO list: recmdList) {
    		String img = urservice.getImg(list.getId());
    		list.setImg(img);
    	}
    	
		String result = "";
		try {
			ObjectMapper mapper = new ObjectMapper();
			result = mapper.writeValueAsString(recmdList);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="addRecomend")
	public String addRecomend(HttpServletRequest request, HttpSession session) {			
		String id = (String) session.getAttribute("sessionId");
		String listTitle = request.getParameter("title");
		String listContent = request.getParameter("content");
		
		String songs = request.getParameter("songArr");
				
		List<Integer> realSongs = new ArrayList<>();
		for (String song : songs.split(",")) {
			realSongs.add(Integer.parseInt(song));
		}
		
		UserRecommendVO userRecommendVO = new UserRecommendVO();
		
		userRecommendVO.setTitle(listTitle);
		userRecommendVO.setContent(listContent);
		userRecommendVO.setUserID(id);
		
		urservice.playListInsert(userRecommendVO,realSongs);

		return "redirect:/userrecommend/list";
	}
	
    @RequestMapping(value="playlistDelete")
    public String playlistDelete(Model m, @RequestParam(name="userRcmId") int userRcmId) {
    	urservice.deletelist(userRcmId);
    	return "redirect:/userrecommend/list";
    }
	
    @RequestMapping(value="playlistModify")
    public String playlistModify(Model m, @RequestParam(name="userRcmId") int userRcmId) {
    	
    	// ?????? ???????? 
    	List<SongVO> songList = urservice.getPlayListDetail(userRcmId);
    	m.addAttribute("play_list", songList);
    	
    	// ???????????? ???? ????????
    	m.addAttribute("info", urservice.getInfo(userRcmId));
    	
    	return "userrecommend/modify";
    }
    
    @RequestMapping(value="updateRecomendList")
	public String updateRecomendList(HttpServletRequest request) {
		String listTitle = request.getParameter("title");
		String listContent = request.getParameter("content");
		int userRcmId = Integer.parseInt(request.getParameter("userRcmId"));
		
		//?????? ???????????? ????
		String songs = request.getParameter("songArr");
		Map<Integer,Integer> songlist = new HashMap<>();
		for (String song : songs.split(",")) {
			songlist.put(Integer.parseInt(song),userRcmId);
		}
			
		UserRecommendVO userRecommendVO = new UserRecommendVO();
		
		userRecommendVO.setTitle(listTitle);
		userRecommendVO.setContent(listContent);
		userRecommendVO.setId(userRcmId);

		urservice.playListUpdate(userRecommendVO, userRcmId, songlist);
		
    	return "redirect:/userrecommend/detail?userRcmId="+userRcmId;
    }
    
    
}
