package com.easyshop.service;

import com.easyshop.pojo.Specification;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-21
 */
public interface SpecificationService extends IService<Specification> {
	
	
	//1.新增规格和规格选项 想处理事务
	public int addSpecificationAndOption(Specification specification,String[] optionName,Integer[] orders);

	//2.更新规格和规格选项 想处理事务
		public Integer updateSpecificationAndOption(Specification specification, String[] optionName, Integer[] orders);
}
