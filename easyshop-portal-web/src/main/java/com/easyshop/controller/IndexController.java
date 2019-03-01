package com.easyshop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.easyshop.pojo.Content;
import com.easyshop.service.ContentService;
@Controller
@Scope("prototype")
public class IndexController {
	
	@Autowired
	RedisTemplate redisTemplate;
	
	@Reference
	ContentService contentService;// 微服务
	
	
	@RequestMapping({"index","index.html","/"})
	public String index(Model model){
		
		List<Content> ads = getLunBoAds();//轮播图
		model.addAttribute("lunBoAds", ads);
		
		List<Content> shengxianAds = getShengxianAds();//生鲜图
		model.addAttribute("shengxianAds", shengxianAds);
		
		//1.加载首页轮播图
		return "Index";
	}
	
	
	
	/**
	 * 查询数据库中所有的轮播图
	 * @return
	 */
	public List<Content> getLunBoAds(){
		//后期加redis缓存
		List<Content> selectList = (List<Content>) redisTemplate.boundHashOps("guanggao").get("lunbos");
		
		if(selectList==null){
			selectList = contentService.selectList(new EntityWrapper<Content>().eq("del", 0).eq("status", 1).eq("category_id", 1));
			// 往缓存存一份，下次再查询缓存中就有数据 不会查询数据库了
			redisTemplate.boundHashOps("guanggao").put("lunbos", selectList);// 存入缓存
		}
		return selectList;
	}
	/**
	 * 查询数据库中所有的生鲜广告
	 * @return
	 */
	public List<Content> getShengxianAds() {
		// 读取Redis数据
		List<Content> selectList = (List<Content>) redisTemplate.boundHashOps("guanggao").get("shengxians");
		if (selectList != null) {
			System.err.println("从缓存中获取首页焦点图数据......生鲜");
		} else {
			System.err.println("从数据库中获取首页焦点图数据......生鲜");
			selectList = contentService
					.selectList(new EntityWrapper<Content>().eq("del", 0).eq("status", 1).eq("category_id", 10));
			// 往缓存存一份，下次再查询缓存中就有数据 不会查询数据库了
			redisTemplate.boundHashOps("guanggao").put("shengxians", selectList);// 存入缓存
		}
		return selectList;

	}
	
	
}
