package com.yujian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Role;
import com.yujian.domain.entity.User;
import com.yujian.domain.entity.UserRole;
import com.yujian.domain.vo.PageVo;
import com.yujian.domain.vo.UserInfoVo;
import com.yujian.domain.vo.UserRoleVo;
import com.yujian.enums.AppHttpCodeEnum;
import com.yujian.exception.SystemException;
import com.yujian.mapper.UserMapper;
import com.yujian.service.RoleService;
import com.yujian.service.UserRoleService;
import com.yujian.service.UserService;
import com.yujian.utils.BeanCopyUtils;
import com.yujian.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-06-21 16:23:19
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Override
    public ResponseResult userInfo() {
        //获取用户当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;



    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listUser(Integer pageNum, Integer pageSize, String userName, String phoneumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(phoneumber),User::getPhonenumber,phoneumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> userPage = new Page<>();
        userPage.setSize(pageSize);
        userPage.setCurrent(pageNum);
        Page<User> page = page(userPage, queryWrapper);

        PageVo pageVo = new PageVo();
        pageVo.setRows(page.getRecords());
        pageVo.setTotal(page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult saveUser(UserRoleVo userRoleVo) {
        User user = BeanCopyUtils.copyBean(userRoleVo, User.class);

        if (!StringUtils.hasText(user.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        } else if (userNameExist(user.getUserName())) {
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (phoneExist(user.getPhonenumber())){
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (emailExist(user.getEmail())){
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }

        String passwordEncode = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncode);

        save(user);
        List<Long> roleIds = userRoleVo.getRoleIds();
        List<UserRole> userRoles = new ArrayList<>();
        roleIds.forEach(r -> userRoles.add(new UserRole(user.getId(),r)));

        userRoleService.saveBatch(userRoles);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult removeUser(Long id) {
        removeById(id);
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId,id));
        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public ResponseResult updateUser(UserRoleVo userRoleVo) {
        User user = BeanCopyUtils.copyBean(userRoleVo, User.class);
        saveOrUpdate(user);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRole::getUserId,userRoleVo.getId());
        userRoleService.remove(queryWrapper);

        List<UserRole> userRoles = new ArrayList<>();
        userRoleVo.getRoleIds().forEach(r -> userRoles.add(new UserRole(userRoleVo.getId(),r)));
        userRoleService.saveBatch(userRoles);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUserById(Long id) {
        List<Role> roleList = roleService.list();
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(UserRole::getRoleId).eq(UserRole::getUserId,id);
        List<UserRole> userRoles = userRoleService.getBaseMapper().selectList(queryWrapper);
        User user = getById(id);
        List<Long> roIds = new ArrayList<>();

        userRoles.forEach(u -> roIds.add(u.getRoleId()));


        UserRoleVo userRoleVo = new UserRoleVo();
        userRoleVo.setUser(user);
        userRoleVo.setRoleIds(roIds);
        userRoleVo.setRoles(roleList);

        return ResponseResult.okResult(userRoleVo);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        return count(queryWrapper) > 0;
    }
    private boolean nickNameExist(String nickName){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        return count(queryWrapper) > 0;
    }
    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email);
        return count(queryWrapper) > 0;
    }
    private boolean phoneExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        return count(queryWrapper) > 0;
    }
}
