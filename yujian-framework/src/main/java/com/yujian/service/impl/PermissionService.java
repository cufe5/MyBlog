package com.yujian.service.impl;

import com.yujian.domain.entity.LoginUser;
import com.yujian.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ps")
public class PermissionService {
    /**
     * 判断当前用户是否具有permission
     * @param permission
     * @return
     */

    @Autowired
    private UserDetailsService userDetailsService;

    public boolean hasPermission(String permission){
        //如果是超级管理员，直接返回true
        if(SecurityUtils.isAdmin()){
            return true;
        }

        //否则获取当前登录用户所具有的权限列表，判断是否具有permission
        LoginUser userDetails = (LoginUser) userDetailsService.loadUserByUsername(SecurityUtils.getLoginUser().getUsername());
        List<String> permissions = userDetails.getPermissions();
        return permissions.contains(permission);


    }
}
