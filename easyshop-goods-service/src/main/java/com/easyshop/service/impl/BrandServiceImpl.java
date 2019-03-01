package com.easyshop.service.impl;

import com.easyshop.pojo.Brand;
import com.easyshop.mapper.BrandMapper;
import com.easyshop.service.BrandService;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-19
 */
@Service
public class BrandServiceImpl extends ServiceImpl<BrandMapper, Brand> implements BrandService {

}
