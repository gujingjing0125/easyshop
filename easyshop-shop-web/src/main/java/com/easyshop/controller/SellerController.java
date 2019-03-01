package com.easyshop.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.easyshop.pojo.Seller;
import com.easyshop.service.SellerService;
import com.easyshop.utils.ResultMsg;

@Controller
@RequestMapping("/seller")
public class SellerController {
	
	@Reference
	SellerService sellerService;
	
	
	/*检查验证码*/
	@RequestMapping("/ckcode")
	@ResponseBody
	public ResultMsg ckcode(String usercode, HttpSession session){
		ResultMsg result=null;
		String code = (String) session.getAttribute("CODE"); //系统生成验证码
		if(code.equalsIgnoreCase(usercode)){
			result = new ResultMsg(true, "验证成功");
		}else{
			result = new ResultMsg(false, "验证失败");
		}
		return result;
	}
	
	/**
	 * 02-商家入驻
	 */
	@RequestMapping("/addseller")
	public String addseller(Seller seller,HttpSession session,String usercode){
		String code = (String) session.getAttribute("CODE");
		System.err.println(seller);
		
		if(code.equalsIgnoreCase(usercode)){
			seller.setCreateTime(new Date()); //创建时间
			seller.setDel(0); //是否删除
			seller.setStatus("0"); //表示0未审核  最新入驻状态未审核
			
			//密码加密 商家入驻的密码  需要在数据库加密
		    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
		    String password = passwordEncoder.encode(seller.getPassword());
		   
		    System.out.println( passwordEncoder.encode("1")+"1**********");
		    seller.setPassword(password);
		    
			boolean b = sellerService.insert(seller); //新增到数据库
			
			if(b){
				return "ruzhu_success"; //入驻成功页面
			}else{
				return "ruzhu_fail"; //入驻失败提示页面
			}
		}else{
			return "ruzhu_fail";//入驻失败提示页面
		}
		
	}
	
	public static void main(String[] args) {
		//密码加密 商家入驻的密码  需要在数据库加密
	    BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
	    String password = passwordEncoder.encode("root");
	    System.out.println(password);
	}

}
