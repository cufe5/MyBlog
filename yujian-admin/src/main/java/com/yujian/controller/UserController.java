package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.vo.UserRoleVo;
import com.yujian.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/list")
    public ResponseResult listUser(Integer pageNum,Integer pageSize,String userName,String phonenumber,String status){
        return userService.listUser(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserRoleVo userRoleVo){
        return userService.saveUser(userRoleVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Long id){
        return userService.removeUser(id);
    }
    @GetMapping("/{id}")
    public ResponseResult queryUser(@PathVariable("id") Long id){
        return userService.getUserById(id);
    }

    @PutMapping
    public ResponseResult modifyUser(@RequestBody UserRoleVo userRoleVo){
        return userService.updateUser(userRoleVo);
    }
}
