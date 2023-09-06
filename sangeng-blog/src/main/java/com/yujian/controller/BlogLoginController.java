package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.User;
import com.yujian.enums.AppHttpCodeEnum;
import com.yujian.exception.SystemException;
import com.yujian.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogLoginController {
    @Autowired
    private BlogLoginService blogLoginService;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName())){
            //必须要传用户名
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return blogLoginService.login(user);

    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        return blogLoginService.logout();

    }
}
