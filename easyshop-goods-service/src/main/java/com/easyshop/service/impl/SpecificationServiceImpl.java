package com.easyshop.service.impl;

import com.easyshop.pojo.Specification;
import com.easyshop.pojo.SpecificationOption;
import com.easyshop.mapper.SpecificationMapper;
import com.easyshop.mapper.SpecificationOptionMapper;
import com.easyshop.service.SpecificationOptionService;
import com.easyshop.service.SpecificationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-21
 */
@Service
public class SpecificationServiceImpl extends ServiceImpl<SpecificationMapper, Specification> implements SpecificationService {
	
	@Autowired
	SpecificationMapper specificationMapper;
	@Autowired
	SpecificationOptionMapper specificationOptionMapper;

	@Override
	@Transactional(isolation=Isolation.DEFAULT,propagation=Propagation.REQUIRED)
	public int addSpecificationAndOption(Specification specification, String[] optionName, Integer[] orders) {
		specification.setDel(0);
		Integer count = specificationMapper.insert(specification);
		Integer sum=0;
		if(count>0){
			for(int i=0;i<optionName.length;i++){
				SpecificationOption specificationOption = new SpecificationOption();
				specificationOption.setDel(0);
				specificationOption.setOptionName(optionName[i]);
				specificationOption.setOrders(orders[i]);
				specificationOption.setSpecId(specification.getId());//外键
				Integer i2 = specificationOptionMapper.insert(specificationOption);
				if(i2>0){
					sum++;
				}
			}
		}
		if(count>0 && sum==optionName.length){
			return 1;
		}else {
			return 0;
		}
		
	}

	@Override
	public Integer updateSpecificationAndOption(Specification specification, String[] optionName, Integer[] orders) {
		
		Integer count = specificationMapper.updateById(specification);
		Integer sum = 0;
		if(count>0){
			Integer c = specificationOptionMapper.delete(new EntityWrapper<SpecificationOption>().eq("spec_id", specification.getId()));
			for (int i = 0; i < optionName.length; i++) {
				SpecificationOption option = new SpecificationOption();
				option.setOptionName(optionName[i]);
				option.setOrders(orders[i]);
				option.setSpecId(specification.getId());//外键
				option.setDel(0);
				int a = specificationOptionMapper.insert(option);
				if (a > 0) {
					sum++;
				}
			}
		}
		if(count>0&&optionName.length==sum){
			return 1;
		}else{
			return 0;
		}
	}

}
