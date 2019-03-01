package com.easyshop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

	// 跳转到模板index
	@RequestMapping("/index")
	public String getindex(){
		return "index";
	}
	
	// 跳转到模板main
	@RequestMapping("/main")
	public String main() {
		return "main"; 
	}
	
	// 跳转到模板login
		@RequestMapping("/loginFailure")
		public String failure(Model model) {
			model.addAttribute("msg","账号或密码错误");
			return "login"; 
		}
	
	/*登录成功 返回当前登录的用户 用户名 页面显示*/
	@RequestMapping("/showLoginName")
	@ResponseBody
	public Map<String, String> showLoginName(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String,String> map=new HashMap<String,String>();
		map.put("loginName", name);
		return map ;

	}

}
