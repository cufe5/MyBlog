package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-10-10 15:06:49
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);
}

