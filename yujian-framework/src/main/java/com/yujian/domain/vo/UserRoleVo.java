package com.yujian.domain.vo;

import com.yujian.domain.entity.Role;
import com.yujian.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleVo {
    private Long id;

    //用户名
    private String userName;
    //昵称
    private String nickName;
    //密码
    private String password;

    //账号状态（0正常 1停用）
    private String status;
    //邮箱
    private String email;
    //手机号
    private String phonenumber;
    //用户性别（0男，1女，2未知）
    private String sex;

    //所关联的角色id
    private List<Long> roleIds;

    //所关联角色列表
    private List<Role> roles;

    private User user;


}
