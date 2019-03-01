package com.easyshop.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.ContentCategory;
import com.easyshop.service.ContentCategoryService;
import com.easyshop.service.ContentService;
import com.easyshop.utils.PageResult;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * <p>
 * 内容分类 前端控制器
 * 广告分类的增删改查
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-27
 */
@Controller
@RequestMapping("/contentCategory")
public class ContentCategoryController {

	@Reference
	ContentCategoryService contentCategoryService;
	
	/**
	 * 01-查询首页
	 * @param contentCategory
	 * @param pageIndex
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@RequestMapping("/list/{pageIndex}")
	public String list(ContentCategory contentCategory, @PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize, Model model) {
		Page<ContentCategory> results = null;
		// 分页工具
		Page<ContentCategory> page = new Page<ContentCategory>(pageIndex, pageSize);
		EntityWrapper<ContentCategory> entityWrapper = new EntityWrapper<ContentCategory>();
		// 查询的数据结果集对象 查询条件2个
		if (contentCategory != null && contentCategory.getName() != null) {
			entityWrapper.like("name", contentCategory.getName());
		}
		entityWrapper.eq("del", 0);
		results = contentCategoryService.selectPage(page, entityWrapper);
		// 获取总数
		int totalCount = ((Long) results.getTotal()).intValue();
		// 查询是否有上一页
		boolean hasPrevious = results.hasPrevious();
		// 查询是否有下一页
		boolean hasNext = results.hasNext();
		// 查询到每页数据
		List<ContentCategory> contentCategorys = results.getRecords();
		// 查询出所有班级
		PageResult<ContentCategory> pageResult = new PageResult<ContentCategory>(totalCount, pageIndex, pageSize,
				contentCategorys, contentCategory);
		model.addAttribute("pageResult", pageResult);
		model.addAttribute("hasPrevious", hasPrevious);
		model.addAttribute("hasNext", hasNext);
		return "content_category"; // 跳转模板上
	}
}

