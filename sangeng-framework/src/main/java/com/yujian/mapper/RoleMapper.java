package com.yujian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yujian.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-10 15:06:48
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);
}

