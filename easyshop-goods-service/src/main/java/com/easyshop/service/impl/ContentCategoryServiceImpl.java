package com.easyshop.service.impl;

import com.easyshop.pojo.ContentCategory;
import com.easyshop.mapper.ContentCategoryMapper;
import com.easyshop.service.ContentCategoryService;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 内容分类 服务实现类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-27
 */
@Service
public class ContentCategoryServiceImpl extends ServiceImpl<ContentCategoryMapper, ContentCategory> implements ContentCategoryService {

}
