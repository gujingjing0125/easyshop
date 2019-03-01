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
import com.easyshop.pojo.Seller;
import com.easyshop.service.SellerService;

/**
 * Spring security 实现用户名和密码的数据库验证类
 
 */
public class MyUserDetailsService implements UserDetailsService {

	SellerService sellerService;

	// set方法 set注入
	public void setSellerService(SellerService sellerService) {
		this.sellerService = sellerService;
	}

	/*
	 * 验证数据库的账号和密码
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		//构建角色列表
		List<GrantedAuthority> grantAuths = new ArrayList<GrantedAuthority>();
		grantAuths.add(new SimpleGrantedAuthority("ROLE_SELLER"));  //设置一个权限  身份 ROLE_SELLER 模拟的 正常应该查询数据库
		
		// username表单中用户登录的用户名
		Seller seller = sellerService.selectOne(new EntityWrapper<Seller>().eq("seller_id", username)); // 根据用户名查询一个对象    数据库中用户名不会重复
		if (seller != null) {
			System.out.println("******************");
			// 账号有
			if (seller.getStatus().toString().equals("1")) { // 如果是已审核
				System.out.println("登录用户:" + seller);
				return new User(username, seller.getPassword(), grantAuths); // 比对Spring security的密码和账号是否匹配
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
