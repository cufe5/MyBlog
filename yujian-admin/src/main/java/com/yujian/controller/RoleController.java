package com.yujian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yujian.constants.SystemConstants;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Role;
import com.yujian.domain.vo.RoleVo;
import com.yujian.enums.AppHttpCodeEnum;
import com.yujian.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult listRole(Integer pageNum, Integer pageSize, String roleName, String status){
        return roleService.selectRolePage(pageNum,pageSize,roleName,status);
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody Role role){
        return roleService.changeStatus(role);
    }

    @PostMapping
    public ResponseResult addRole(@RequestBody Role role){
        return roleService.addRole(role);
    }
    @GetMapping("/{id}")
    public ResponseResult selectRoleById(@PathVariable("id") long id){
        return roleService.selectRoleById(id);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleVo roleVo){
        return roleService.updateRole(roleVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRole(@PathVariable("id") long id){
        return roleService.deleteRole(id);
    }
    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        return ResponseResult.okResult(roleService.list(queryWrapper));
    }
}
