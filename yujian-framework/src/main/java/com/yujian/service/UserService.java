package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.User;
import com.yujian.domain.vo.UserRoleVo;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2023-06-21 16:23:18
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult saveUser(UserRoleVo userRoleVo);

    ResponseResult removeUser(Long id);

    ResponseResult getUserById(Long id);

    ResponseResult updateUser(UserRoleVo userRoleVo);
}

