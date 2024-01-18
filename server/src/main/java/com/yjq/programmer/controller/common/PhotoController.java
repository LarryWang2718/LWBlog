package com.yjq.programmer.controller.common;

import com.yjq.programmer.bean.CodeMsg;
import com.yjq.programmer.dto.ResponseDTO;
import com.yjq.programmer.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;


@RequestMapping("/common/photo")
@RestController
public class PhotoController {

	@Autowired
	private ResourceLoader resourceLoader;

	@Value("${yjq.upload.photo.path}")
	private String uploadPhotoPath;

	private static final Logger logger = LoggerFactory.getLogger(PhotoController.class);

	/**
	 * @param filename
	 * @return
	 */
	@RequestMapping(value="/view")
	public ResponseEntity<?> viewPhoto(@RequestParam(name="filename", required=true)String filename){
		Resource resource = resourceLoader.getResource("file:" + uploadPhotoPath + filename);
		try {
			return ResponseEntity.ok(resource);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}


	/**
	 * @param photo
	 * @param request
	 * @return
	 */
	@PostMapping(value="/upload_photo")
	public ResponseDTO<String> uploadPhoto(MultipartFile photo, HttpServletRequest request){
		if(photo == null){
			return ResponseDTO.errorByMsg(CodeMsg.PHOTO_EMPTY);
		}
		if(photo.getSize() > 1*1024*1024) {
			return ResponseDTO.errorByMsg(CodeMsg.PHOTO_SURPASS_MAX_SIZE);
		}
		String suffix = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf(".")+1,photo.getOriginalFilename().length());
		if(!CommonUtil.isPhoto(suffix)){
			return ResponseDTO.errorByMsg(CodeMsg.PHOTO_FORMAT_NOT_CORRECT);
		}
		String path = request.getContextPath();
		String savePath = uploadPhotoPath + CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "\\";
		File savePathFile = new File(savePath);
		if(!savePathFile.exists()){
			savePathFile.mkdir();
		}
		String filename = new Date().getTime()+"."+suffix;
		logger.info("Saved file path:{}",savePath + filename);
		try {
			photo.transferTo(new File(savePath + filename));
		}catch (Exception e) {
			e.printStackTrace();
			return ResponseDTO.errorByMsg(CodeMsg.SAVE_FILE_EXCEPTION);
		}
		String filepath = CommonUtil.getFormatterDate(new Date(), "yyyyMMdd") + "/" + filename;
		return ResponseDTO.successByMsg(filepath, "Successfully uploaded photo");
	}
}
