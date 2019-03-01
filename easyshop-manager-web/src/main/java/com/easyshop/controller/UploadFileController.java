package com.easyshop.controller;

import org.junit.runner.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.easyshop.utils.FastDFSClient;
import com.easyshop.utils.ResultMsg;

/**
 * 文件上传
 * 
 * @author bruceliu
 *
 */
@Controller
public class UploadFileController {
	
	@Value("${FILE_SERVER_URL}")
	private String FILE_SERVER_URL;// 图片文件服务器地址

	/**
	 * 文件上传到的方法
	 * @param file
	 * @return
	 */
	@RequestMapping("/uploadImg")
	@ResponseBody
	public ResultMsg upload(MultipartFile file) {
		
		// 1、取文件的扩展名
		String originalFilename = file.getOriginalFilename();
		String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
		try {
			// 2、创建一个 FastDFS 的客户端
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:config/fdfs_client.conf");
			// 3、执行上传处理
			String path = fastDFSClient.uploadFile(file.getBytes(), extName);
			// 4、拼接返回的 url 和 ip 地址，拼装成完整的 url
			String url = FILE_SERVER_URL + path;
			return new ResultMsg(true, url); // url上传成功之后 图片的地址
			
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "上传失败");
		}
	}
	
	
}
