package com.easyshop.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.Content;
import com.easyshop.pojo.ContentCategory;
import com.easyshop.service.ContentCategoryService;
import com.easyshop.service.ContentService;
import com.easyshop.utils.PageResult;
import com.easyshop.utils.ResultMsg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-27
 */
@Controller
@RequestMapping("/content")
public class ContentController {

	@Reference
	ContentService contentService;
	
	@Reference
	ContentCategoryService contentCategoryService;
	
	@Autowired
	RedisTemplate redisTemplate; //Redis缓存相关

	/**
	 * 查询首页
	 * 
	 * @param brand
	 * @param pageIndex
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping("/list/{pageIndex}")
	public String list(Content content, @PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(name = "pageSize", defaultValue = "5") Integer pageSize, Model model) {
		Page<Content> results = null;
		// 分页工具
		Page<Content> page = new Page<Content>(pageIndex, pageSize);
		EntityWrapper<Content> entityWrapper = new EntityWrapper<Content>();
		// 查询的数据结果集对象 查询条件2个
		if (content != null && content.getTitle() != null) {
			entityWrapper.like("title", content.getTitle());
		}
		entityWrapper.eq("del", 0);
		results = contentService.selectPage(page, entityWrapper);
		// 获取总数
		int totalCount = ((Long) results.getTotal()).intValue();
		// 查询是否有上一页
		boolean hasPrevious = results.hasPrevious();
		// 查询是否有下一页
		boolean hasNext = results.hasNext();
		// 查询到每页数据
		List<Content> contents = results.getRecords();
		
		for (Content c : contents) {
			Long categoryId = c.getCategoryId(); // 分类ID
			ContentCategory contentCategory = contentCategoryService.selectById(categoryId);
			String name = contentCategory.getName(); // 分类名称
			c.setCategoryName(name);
		}

		// 查询出所有广告
		PageResult<Content> pageResult = new PageResult<Content>(totalCount, pageIndex, pageSize, contents, content);
		model.addAttribute("pageResult", pageResult);
		model.addAttribute("hasPrevious", hasPrevious);
		model.addAttribute("hasNext", hasNext);
		return "content"; // 跳转模板上
	}
	
	
	/**
	 * 02-查询下拉框 绑定到页面上 选择的时候用
	 * @return
	 */
	@RequestMapping("/getContentCategorys")
	@ResponseBody
	public List<ContentCategory> getContentCategorys() {
		return contentCategoryService.selectList(new EntityWrapper<ContentCategory>().eq("del", 0));
	}
	
	/**
	 * 04-增加
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("/add")
	@ResponseBody
	public ResultMsg add(Content content) {
		System.out.println(content);
		content.setDel(0);
		try {
			contentService.insert(content);
			return new ResultMsg(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "增加失败");
		}
	}
	
	/**
	 * 05-根据ID查询
	 * @return
	 */
	@RequestMapping("/findOne/{id}")
	@ResponseBody
	public Content getById(@PathVariable("id") Integer id){
		return contentService.selectById(id);
	}
	
	/**
	 * 05-更新
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResultMsg update(Content content) {
		System.out.println(content);
		content.setDel(0);
		try {
			contentService.updateById(content);
			return new ResultMsg(true, "更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResultMsg(false, "更新失败");
		}
	}
	
	
}

