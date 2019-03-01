package com.easyshop.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.Brand;
import com.easyshop.pojo.Specification;
import com.easyshop.pojo.SpecificationOption;
import com.easyshop.service.SpecificationOptionService;
import com.easyshop.service.SpecificationService;
import com.easyshop.utils.PageResult;
import com.easyshop.utils.ResultMsg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-21
 */
@Controller
@RequestMapping("/specification")
public class SpecificationController {
	
	@Reference
	SpecificationService specificationService;
	
	@Reference
	SpecificationOptionService specificationOptionService;
	
	
	
/*01-分页查询*/
	
	@RequestMapping("/list/{pageIndex}")
	public String list(Specification specification,@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,Model model){
		
		//分页工具
		Page<Specification> page = new Page<Specification>(pageIndex,pageSize);
		EntityWrapper<Specification> entityWrapper = new EntityWrapper<Specification>();
		
		if(specification!=null && specification.getSpecName()!=null){
			entityWrapper.like("spec_name", specification.getSpecName());//模糊查条件
		}
		entityWrapper.eq("del", 0);
		entityWrapper.orderBy("id").last("desc");
		
		Page<Specification> results = specificationService.selectPage(page, entityWrapper);
		
		// 获取总数
		int totalCount = ((Long) results.getTotal()).intValue();
		// 查询是否有上一页
		boolean hasPrevious = results.hasPrevious();
		// 查询是否有下一页
		boolean hasNext = results.hasNext();
		// 查询到每页数据
		List<Specification> specifications = results.getRecords();
		// 查询出所有品牌
		PageResult<Specification> pageResult = new PageResult<Specification>(totalCount, pageIndex, pageSize, specifications, specification);

		model.addAttribute("pageResult", pageResult);
		model.addAttribute("hasPrevious", hasPrevious);
		model.addAttribute("hasNext", hasNext);
		return "specification";
	}
	
	
/*02-新增*/
	
	@ResponseBody
	@RequestMapping("/add")
	public ResultMsg add(Specification specification,String[] optionName,Integer[] orders){
		Integer i;
		ResultMsg result=null;
		try {
			i = specificationService.addSpecificationAndOption(specification, optionName, orders);
			if (i > 0) {
				result = new ResultMsg(true, "新增成功");
			} else {
				result = new ResultMsg(false, "新增失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new ResultMsg(false, "新增失败");
		}
		return result;
	}
	
	
	/*03-修改前先查询数据回显*/
	@RequestMapping("/getSpecificationById")
	@ResponseBody
	public HashMap<String, Object> getSpecificationById(Integer id){
		Specification specification = specificationService.selectById(id); //规格
		List<SpecificationOption> options = specificationOptionService.selectList(new EntityWrapper<SpecificationOption>().eq("spec_id", specification.getId()).eq("del", 0));
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("specification", specification);
		map.put("options", options);
		return map;
	}
	
	/*04-更新*/
	@ResponseBody
	@RequestMapping("/update")
	public ResultMsg update(Specification specification,String[] optionName,Integer[] orders){
		Integer b;
		ResultMsg result = null;
		try {
			b = specificationService.updateSpecificationAndOption(specification, optionName, orders);
			if (b > 0) {
				result = new ResultMsg(true, "更新成功");
			} else {
				result = new ResultMsg(false, "更新失败");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			result = new ResultMsg(false, "更新失败");
		}
		return result;
	}
	
	
/*05-删除品牌*/
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public ResultMsg deleteById(@PathVariable("id") Integer id){
		Boolean b = null;
		ResultMsg result = null;
		try {
			Specification specification = specificationService.selectById(id);
			specification.setDel(1);
			b = specificationService.updateById(specification);
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
	
	
/*06-批量删除*/
	
	@RequestMapping("/deleteSome")
	@ResponseBody
	public ResultMsg deleteSome(Integer[] ids){
		Boolean b = null;
		ResultMsg result = null;
		List<Integer> list = Arrays.asList(ids);
		List<Specification> specifications = specificationService.selectBatchIds(list);
		for (Specification specification : specifications) {
			specification.setDel(1);
		}
		try {
			 b = specificationService.updateBatchById(specifications);
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

