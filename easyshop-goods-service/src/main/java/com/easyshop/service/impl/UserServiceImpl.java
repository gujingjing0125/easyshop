package com.easyshop.service.impl;

import com.easyshop.pojo.User;
import com.easyshop.mapper.UserMapper;
import com.easyshop.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author gujingjing
 * @since 2019-02-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
