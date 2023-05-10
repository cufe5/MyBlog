package com.yujian.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yujian.domain.entity.User;
import com.yujian.mapper.UserMapper;
import com.yujian.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-05-10 15:56:26
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
