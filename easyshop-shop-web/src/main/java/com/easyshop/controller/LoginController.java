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

	@RequestMapping("/shop/index")
	public String index(){
		return "index";
	}
	
	@RequestMapping("/shop/home")
	public String home(){
		return "home";
	}
	
	@RequestMapping("/shop/loginfailure")
	public String loginfailure(Model model){
		model.addAttribute("msg","账号或密码错误");
		return "shoplogin";
	}
	
	
	@RequestMapping("/showLoginName")
	@ResponseBody
	public Map<String, String> showLoginName(){
		// 从SpringSecurity框架中获取当前认证通过的 登录人 的 username
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Map<String, String> map = new HashMap<String, String>();
		map.put("loginName", name);
		return map;
	}
	
}
