package com.jpa.example.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.activation.CommandMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jpa.example.entity.BoardEntity;
import com.jpa.example.entity.SelectBoardVo;
import com.jpa.example.entity.UploadEntity;
import com.jpa.example.repository.BoardRepository;
import com.jpa.example.repository.UploadRepository;

@Service
public class BoardService {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private UploadRepository uploadRepository;
	

	//게시판 페이징 목록 수
	public int getBoardListCnt(SelectBoardVo selectBoardVo) {
		
		return boardRepository.getBoardListCnt();
	}

	//게시판 페이징 목록
	public List<BoardEntity> getBoardList(SelectBoardVo selectBoardVo) {
		
		return boardRepository.getBoardList(selectBoardVo.getStartList(), selectBoardVo.getListSize());
	}
	
	public void create(BoardEntity boardEntity, MultipartFile[] file) {
		
		Timestamp date = null;
		
		boardEntity.setCreatedate(date.valueOf(LocalDateTime.now()));
		
		boardRepository.save(boardEntity);
		
		boardEntity.setBid(boardRepository.getBid(boardEntity.getUserid()));
		
		for(int i=0;i<file.length; i++) {
			if(file[i].getOriginalFilename() == null || file[i].getOriginalFilename() == "" || file[i].getSize() <= 0) {
				return;
			}
			else {
				try {
					UploadEntity uploadEntity = parseFileInfo(boardEntity, file[i]);
					uploadRepository.save(uploadEntity);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public BoardEntity read(int bid) {
		return boardRepository.read(bid);
	}
	
	public void delete(Long bid) {
		boardRepository.deleteById(bid);
		uploadRepository.deletebid(bid);
	}
	
	public void update(BoardEntity boardEntity) {
		
		Timestamp date = null;
		
		boardEntity.setCreatedate(date.valueOf(LocalDateTime.now()));
		
		boardRepository.save(boardEntity);
	}
	
	public List<UploadEntity> fileread(int bid) {
		return uploadRepository.fileread(bid);
	}
	
	public UploadEntity fileinfo(int fid) {
		return uploadRepository.fileinfo(fid);
	}
	
	public void downloadfile(UploadEntity uploadEntity, HttpServletResponse response) throws Exception {
		
		String saveFileName = uploadEntity.getSavename();
		String originalFileName = uploadEntity.getFilename();
		
		File downloadFile = new File(uploadEntity.getPath() + "\\" + uploadEntity.getSavename());
		
		byte fileByte[] = FileUtils.readFileToByteArray(downloadFile);
		
		response.setContentType("application/octet-stream");
		response.setContentLength(fileByte.length);
		
		response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		
		response.getOutputStream().write(fileByte);
		response.getOutputStream().flush();
		response.getOutputStream().close();
	}
	
	public void update(BoardEntity boardEntity, MultipartFile[] file, int[] delFiles) {
		
		Timestamp date = null;
		
		boardEntity.setCreatedate(date.valueOf(LocalDateTime.now()));
		
		boardRepository.save(boardEntity);
		
		for(int i = 0;i < delFiles.length; i++) {
			uploadRepository.deleteById((long) delFiles[i]);
		}
		
		for(int i=0;i<file.length; i++) {
			if(file[i].getOriginalFilename() == null || file[i].getOriginalFilename() == "" || file[i].getSize() <= 0) {
				return;
			}
			else {
				try {
					UploadEntity uploadEntity = parseFileInfo(boardEntity, file[i]);
					uploadRepository.save(uploadEntity);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	

	@Resource(name = "uploadPath")
	private String uploadPath;
	
	private UploadEntity parseFileInfo(BoardEntity boardEntity, MultipartFile file) throws Exception {
		
		UploadEntity uploadEntity = new UploadEntity();
		
		uploadEntity.setBid(boardEntity.getBid());
		
		File target = new File(uploadPath);
		if(!target.exists()) {
			target.mkdirs();
		}
		
		String orgFileName = file.getOriginalFilename();
		String orgFileExtension = orgFileName.substring(orgFileName.lastIndexOf("."));
		String saveFileName = UUID.randomUUID().toString().replaceAll("-", "")+orgFileExtension;
		
		target = new File(uploadPath, saveFileName);
		file.transferTo(target);
		
		uploadEntity.setFilename(orgFileName);
		uploadEntity.setPath(uploadPath);
		uploadEntity.setSavename(saveFileName);
		
		
		return uploadEntity;
		
	}
}
