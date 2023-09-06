package com.yujian.service;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.User;


public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
