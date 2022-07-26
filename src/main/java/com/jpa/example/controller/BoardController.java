package com.jpa.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.CommandMap;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.jpa.example.config.MailSendService;
import com.jpa.example.entity.BoardEntity;
import com.jpa.example.entity.SelectBoardVo;
import com.jpa.example.entity.UploadEntity;
import com.jpa.example.entity.UserDetailsVO;
import com.jpa.example.entity.UserEntity;
import com.jpa.example.service.BoardService;
import com.jpa.example.service.UserService;

@Controller
public class BoardController {

	@Autowired
	UserService userservice = new UserService();

	@Autowired
	BoardService boardService = new BoardService();
	
	@Autowired
	MailSendService mailservice = new MailSendService();
	
	@RequestMapping(value = "/board", method = RequestMethod.GET)
	public String board(Model model, @RequestParam(required = false, defaultValue = "1") int page,
			@RequestParam(required = false, defaultValue = "1") int range) throws Exception {
		
		SelectBoardVo selectBoardVo = new SelectBoardVo();
		

		// 전체 게시글 수
		int listCnt = boardService.getBoardListCnt(selectBoardVo);

		selectBoardVo.pageInfo(page, range, listCnt);

		model.addAttribute("pagination", selectBoardVo);
		model.addAttribute("boardList", boardService.getBoardList(selectBoardVo));
		
		
		return "board/board";
	}
	
	@RequestMapping("/board/write")
	public String boardwrite(Model model, Authentication authentication) {
		
		UserDetailsVO userVo = (UserDetailsVO) authentication.getPrincipal();
		
		UserEntity userEntity = userservice.infoCheck(userVo.getUsername());
		
		model.addAttribute("username", userEntity.getId());
		
		return "board/write";
	}
	
	@RequestMapping(value = "/board/insert", method = RequestMethod.POST)
	public String boardinsert(@ModelAttribute BoardEntity boardEntity, MultipartFile[] file) throws Exception {
		
		boardService.create(boardEntity, file);
		
		return "redirect:/board";
	}
	

	@RequestMapping(value="/board/view", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView view(@RequestParam int bid) throws Exception
	{
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("board/view");
		mav.addObject("dto", boardService.read(bid));
		mav.addObject("file", boardService.fileread(bid));
		
		return mav;
	}
	

	
	@RequestMapping(value="/board/update_write", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView update_write(@RequestParam int bid) throws Exception
	{
		ModelAndView mav = new ModelAndView();
		
		mav.setViewName("board/update_write");
		mav.addObject("dto", boardService.read(bid));
		mav.addObject("file", boardService.fileread(bid));
		
		return mav;
	}

	
	@RequestMapping(value = "/board/delete", method = RequestMethod.POST)
	public String delete(@ModelAttribute BoardEntity boardEntity) throws Exception
	{
		boardService.delete(boardEntity.getBid());
		
		Map<Object, Object> map = new HashMap<Object, Object>();
		map.put("status", 1);
		
		return "redirect:/board";
	}
	
	@RequestMapping(value = "/board/update", method = RequestMethod.POST)
	public String update(@ModelAttribute BoardEntity boardEntity, MultipartFile[] file, @RequestParam(value="fileNoDel[]", required=false) int[] delFiles) throws Exception
	{
		
		boardService.update(boardEntity, file, delFiles);
		
		return "redirect:/board";
	}
	
	@RequestMapping(value="/filedownload", method=RequestMethod.GET)
	@ResponseBody
	public void downloadFile(int fid, HttpServletResponse response) throws Exception{
		
		UploadEntity uploadEntity = boardService.fileinfo(fid);
		
		boardService.downloadfile(uploadEntity, response);
	}
}
