package com.easyshop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.easyshop.service.UserService;

/**
 * Spring security 实现用户名和密码的数据库验证类
 * 
 * @author bruceliu
 *
 */
public class MyUserDetailsService implements UserDetailsService {

	UserService userService;
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/*
	 * 验证数据库的账号和密码
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("开始准备登录验证："+username);
		//构建角色列表
		List<GrantedAuthority> grantAuths = new ArrayList();
		grantAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));  //设置一个权限  身份 ROLE_SELLER 模拟的 正常应该查询数据库
		
		// username表单中用户登录的用户名
		com.easyshop.pojo.User u = userService.selectOne(new EntityWrapper<com.easyshop.pojo.User>().eq("username", username)); // 根据用户名查询一个对象    数据库中用户名不会重复
		if (u != null) {
			// 账号有
			if (u.getRole().toString().equals("1")) { // 如果是管理员
				System.out.println("运营商登录用户:" + u);
				User user = new User(username, u.getPassword(), grantAuths);
				System.out.println("user" + user);
				return  user;// 比对Spring security的密码和账号是否匹配
			} else {
				// 未审核 也不登录
				return null;
			}
		} else {
			// 账号不存在
			return null;
		}
	}

}
