package com.easyshop.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.easyshop.pojo.Brand;
import com.easyshop.pojo.CustomAttributeItemObject;
import com.easyshop.pojo.Json2Object;
import com.easyshop.pojo.Specification;
import com.easyshop.pojo.TypeTemplate;
import com.easyshop.service.BrandService;
import com.easyshop.service.SpecificationService;
import com.easyshop.service.TypeTemplateService;
import com.easyshop.utils.Json2ObjectUtils;
import com.easyshop.utils.JsonUtils;
import com.easyshop.utils.PageResult;
import com.easyshop.utils.ResultMsg;

import java.util.ArrayList;
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
 * @since 2019-02-22
 */
@Controller
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
	
	@Reference
	TypeTemplateService typeTemplateService;
	
	@Reference
	BrandService brandService;
	
	@Reference
	SpecificationService specificationService;
	
	
	
	/*01-分页查询*/
	
	@RequestMapping("/list/{pageIndex}")
	public String list(TypeTemplate typeTemplate,@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(name = "pageSize", defaultValue = "8") Integer pageSize,Model model){
		Page<TypeTemplate> page = new Page<TypeTemplate>(pageIndex,pageSize);
		EntityWrapper<TypeTemplate> entityWrapper = new EntityWrapper<TypeTemplate>();
		
		if(typeTemplate!=null && typeTemplate.getName()!=null){
			entityWrapper.like("name", typeTemplate.getName());//模糊查条件
		}
		entityWrapper.eq("del", 0);
		entityWrapper.orderBy("id").last("desc");
		
		Page<TypeTemplate> results = typeTemplateService.selectPage(page, entityWrapper);
		
		// 获取总数
		int totalCount = ((Long) results.getTotal()).intValue();
		// 查询是否有上一页
		boolean hasPrevious = results.hasPrevious();
		// 查询是否有下一页
		boolean hasNext = results.hasNext();
		// 查询到每页数据
		List<TypeTemplate> typeTemplates = results.getRecords();
		
		for (TypeTemplate t : typeTemplates) {
			String brandIds = Json2ObjectUtils.json2Object(t.getBrandIds());
			String specIds=Json2ObjectUtils.json2Object(t.getSpecIds());
			String customAttributeItems = Json2ObjectUtils.json2Object(t.getCustomAttributeItems());
			t.setBrandIds(brandIds);
			t.setSpecIds(specIds);
			t.setCustomAttributeItems(customAttributeItems);
		}
		
		
		// 查询出所有模板
		PageResult<TypeTemplate> pageResult = new PageResult<TypeTemplate>(totalCount, pageIndex, pageSize, typeTemplates, typeTemplate);

		model.addAttribute("pageResult", pageResult);
		model.addAttribute("hasPrevious", hasPrevious);
		model.addAttribute("hasNext", hasNext);
		return "type_template";
	}
	
	
	/*02-新增查询所有品牌和规格*/
	
	@RequestMapping("/getBrandAndSpecs")
	@ResponseBody
	public HashMap<String, Object> getBrandAndSpecs(){
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<Brand> brands = brandService.selectList(new EntityWrapper<Brand>().eq("del", 0));
		List<Specification> specifications = specificationService.selectList(new EntityWrapper<Specification>().eq("del", 0));
		
		map.put("brands", brands);
		map.put("specifications", specifications);
		
		return map;
	}
	
	
	/*03-新增*/
	
	@ResponseBody
	@RequestMapping("/add")
	public ResultMsg add(TypeTemplate typeTemplate,Integer[] brandIds,Integer[] specIds,String[] customAttributeItems){
		typeTemplate.setDel(0);
		
		ArrayList<Json2Object> brandslist = new ArrayList<Json2Object>();
		for (Integer bid : brandIds) {
			Json2Object object = new Json2Object();
			object.setId(bid);
			object.setText(brandService.selectById(bid).getName());
			brandslist.add(object);
		}
		String brand_json = JSON.toJSONString(brandslist);
		
		
		List<Json2Object> specs=new ArrayList<Json2Object>();
		for (Integer id : specIds) {
			Json2Object o=new Json2Object();
			o.setId(id);
			o.setText(specificationService.selectById(id).getSpecName());
			specs.add(o);
		}
		String spec_json = JSON.toJSONString(specs);
		
		List<CustomAttributeItemObject> custs=new ArrayList<CustomAttributeItemObject>();
		for (String cus : customAttributeItems) {
			CustomAttributeItemObject o=new CustomAttributeItemObject();
			o.setText(cus);
			custs.add(o);
		}
		String customAttributeItems_json = JSON.toJSONString(custs);
		
		typeTemplate.setBrandIds(brand_json);
		typeTemplate.setSpecIds(spec_json);
		typeTemplate.setCustomAttributeItems(customAttributeItems_json);
		
		Boolean b=null;
		ResultMsg result = null;
		try {
			b = typeTemplateService.insert(typeTemplate);
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
	
	
	//根据ID查询，修改前数据回显
	@RequestMapping("/getTemplateById/{id}")
	@ResponseBody
	public HashMap<String, Object> getTemplateById(@PathVariable("id")Integer id){
		HashMap<String, Object> map=new HashMap<String, Object>();
		
		TypeTemplate typeTemplate = typeTemplateService.selectById(id);
		
		
		String brandIds = typeTemplate.getBrandIds();
		List<Json2Object> brands = JsonUtils.jsonToList(brandIds,Json2Object.class);
		
		//List<Json2Object> brands = JSON.parseArray(typeTemplate.getBrandIds(),Json2Object.class);
		System.out.println("brands = JSON.parseArray(typeTemplate.getBrandIds()"+brands);
		List<Brand> brandsAll = brandService.selectList(new EntityWrapper<Brand>().eq("del", 0));
		for (Json2Object ckbrand : brands) {
			for (Brand allbrand : brandsAll) {
				if(allbrand.getId().intValue()==(ckbrand.getId().intValue())){
					allbrand.setFlag(true);
					break;
				}
			}
		}
		
		List<Json2Object> specs = JSON.parseArray(typeTemplate.getSpecIds(),Json2Object.class);
		List<Specification> specAll = specificationService.selectList(new EntityWrapper<Specification>().eq("del", 0));
		for (Json2Object ckspec : specs) {
			for (Specification allspec : specAll) {
				if(allspec.getId().intValue()==(ckspec.getId().intValue())){
					allspec.setFlag(true);
					break;
				}
			}
		}
		
		
		List<CustomAttributeItemObject> customAttributeItemObjects = JSON.parseArray(typeTemplate.getCustomAttributeItems(),CustomAttributeItemObject.class);
		System.out.println("List<CustomAttributeItemObject> parseArray"+customAttributeItemObjects);
		
		map.put("typeTemplate", typeTemplate);
		map.put("brandsAll", brandsAll);
		map.put("specAll", specAll);
		map.put("customAttributeItemObjects", customAttributeItemObjects);
		
		return map;
	}
	
	
	/**
	 * 04-更新
	 */
	@RequestMapping("/update")
	@ResponseBody
	public ResultMsg update(TypeTemplate typeTemplate,Integer[] brandIds,Integer[] specIds,String[] customAttributeItems){
		//把数组转为数据库中JSON那种格式？
		
		List<Json2Object> brands=new ArrayList<Json2Object>();
		for (Integer id : brandIds) {
			Json2Object o=new Json2Object();
			o.setId(id);
			o.setText(brandService.selectById(id).getName());
			brands.add(o);
		}
		String brand_json = JSON.toJSONString(brands); //转换为数据库需要的格式
		
		List<Json2Object> specs=new ArrayList<Json2Object>();
		for (Integer id : specIds) {
			Json2Object o=new Json2Object();
			o.setId(id);
			o.setText(specificationService.selectById(id).getSpecName());
			specs.add(o);
		}
		String spec_json = JSON.toJSONString(specs);
		
		
		List<CustomAttributeItemObject> customAttributeItemObjects=new ArrayList<CustomAttributeItemObject>();
		for (String name : customAttributeItems) {
			CustomAttributeItemObject o=new CustomAttributeItemObject();
			o.setText(name);
			customAttributeItemObjects.add(o);
		}
		String customAttributeItems_json = JSON.toJSONString(customAttributeItemObjects);
		
		typeTemplate.setBrandIds(brand_json);
		typeTemplate.setSpecIds(spec_json);
		typeTemplate.setCustomAttributeItems(customAttributeItems_json);
		
		Boolean b;
		ResultMsg result = null;
		try {
			b = typeTemplateService.updateById(typeTemplate);
			if (b) {
				result = new ResultMsg(true, "更新成功");
			} else {
				result = new ResultMsg(false, "更新失败");
			}
		} catch (Exception e) {
			result = new ResultMsg(false, "更新失败");
		}
		return result;
		
	} 
	
	
	

}

