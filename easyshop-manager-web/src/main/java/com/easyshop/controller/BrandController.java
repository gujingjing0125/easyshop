package com.easyshop.controller;


import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.Brand;
import com.easyshop.service.BrandService;
import com.easyshop.utils.PageResult;
import com.easyshop.utils.ResultMsg;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-19
 */
@Controller
@RequestMapping("/brand")
public class BrandController {
	
	@Reference
	BrandService brandService;
	
	@RequestMapping("/test")
	public String showBrandList(ModelMap map) {
		List<Brand> list = brandService.selectList(null);
		map.put("brands", list);
		return "brand";
	}
	
	
	/*01-分页查询*/
	
	@RequestMapping("/list/{pageIndex}")
	public String list(Brand brand,@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,Model model){
		Page<Brand> page = new Page<Brand>(pageIndex,pageSize);
		EntityWrapper<Brand> entityWrapper = new EntityWrapper<Brand>();
		
		if(brand!=null && brand.getName()!=null){
			entityWrapper.like("name", brand.getName());//模糊查条件
		}
		entityWrapper.eq("del", 0);
		entityWrapper.orderBy("id").last("desc");
		
		Page<Brand> results = brandService.selectPage(page, entityWrapper);
		
		// 获取总数
		int totalCount = ((Long) results.getTotal()).intValue();
		// 查询是否有上一页
		boolean hasPrevious = results.hasPrevious();
		// 查询是否有下一页
		boolean hasNext = results.hasNext();
		// 查询到每页数据
		List<Brand> brands = results.getRecords();
		// 查询出所有品牌
		PageResult<Brand> pageResult = new PageResult<Brand>(totalCount, pageIndex, pageSize, brands, brand);

		model.addAttribute("pageResult", pageResult);
		model.addAttribute("hasPrevious", hasPrevious);
		model.addAttribute("hasNext", hasNext);
		return "brand";
	}
	
	
	
	/*02-新增*/
	
	@ResponseBody
	@RequestMapping("/add")
	public ResultMsg add(Brand brand){
		brand.setDel(0);
		Boolean b=null;
		ResultMsg result = null;
		try {
			b = brandService.insert(brand);
			if(b){
				result = new ResultMsg(true, "新增成功");
			}else {
				result = new ResultMsg(false, "新增失败");
			}
		} catch (Exception e) {
			result = new ResultMsg(false, "新增失败");
		}
		
		return result;
	}
	
	
	/*03-修改前先查询数据回显*/
	
	@ResponseBody
	@RequestMapping("/getBrandById")
	public Brand getBrandById(Integer id){
		Brand result = brandService.selectById(id);
		return result;
	}
	
	/*03-修改品牌*/
	
	@ResponseBody
	@RequestMapping("/update")
	public ResultMsg update(Brand brand){
		Boolean b = null;
		ResultMsg result = null;
		try {
			b = brandService.updateById(brand);
			if(b){
				result = new ResultMsg(true, "修改成功");
			}else {
				result = new ResultMsg(false, "修改失败");
			}
		} catch (Exception e) {
			result = new ResultMsg(false, "修改失败");
		}
		return result;
	}
	
	
	/*04-删除品牌*/
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public ResultMsg deleteById(@PathVariable("id") Integer id){
		Boolean b = null;
		ResultMsg result = null;
		try {
			Brand brand = brandService.selectById(id);
			brand.setDel(1);
			b = brandService.updateById(brand);
			if(b){
				result = new ResultMsg(true, "删除成功");
			}else{
				result = new ResultMsg(false, "删除失败");
			}
		} catch (Exception e) {
			result = new ResultMsg(false, "删除失败");
		}
		return result;
	}
	
	
	/*04-批量删除*/
	
	@RequestMapping("/deleteSome")
	@ResponseBody
	public ResultMsg deleteSome(Integer[] ids){
		Boolean b = null;
		ResultMsg result = null;
		List<Integer> list = Arrays.asList(ids);
		List<Brand> brands = brandService.selectBatchIds(list);
		for (Brand brand : brands) {
			brand.setDel(1);
		}
		try {
			 b = brandService.updateBatchById(brands);
			if(b){
				result = new ResultMsg(true, "删除成功");
			}else{
				result = new ResultMsg(false, "删除失败");
			}
		} catch (Exception e) {
			result = new ResultMsg(false, "删除失败");
		}
		return result;
	}
	
	

}

