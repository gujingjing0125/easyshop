package com.easyshop.service.impl;

import com.easyshop.pojo.Seller;
import com.easyshop.mapper.SellerMapper;
import com.easyshop.service.SellerService;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-25
 */
@Service
public class SellerServiceImpl extends ServiceImpl<SellerMapper, Seller> implements SellerService {

}
