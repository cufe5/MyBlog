package com.yujian.service;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.User;


public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}
