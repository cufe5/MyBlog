package com.yujian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Role;
import com.yujian.domain.entity.RoleMenu;
import com.yujian.domain.vo.MenuTreeVo;
import com.yujian.domain.vo.PageVo;
import com.yujian.domain.vo.RoleMenuTreeVo;
import com.yujian.domain.vo.RoleVo;
import com.yujian.mapper.RoleMapper;
import com.yujian.service.MenuService;
import com.yujian.service.RoleMenuService;
import com.yujian.service.RoleService;
import com.yujian.utils.BeanCopyUtils;
import com.yujian.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-10-10 15:06:49
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员，如果是返回集合中只需要admin
        if(id.equals(1L)){
            ArrayList<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult selectRolePage(Integer pageNum, Integer pageSize, String roleName,String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName,roleName);
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>();
        page.setSize(pageSize);
        page.setCurrent(pageNum);
        page(page,queryWrapper);

        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo roleVoPage = new PageVo();
        roleVoPage.setRows(roleVos);
        roleVoPage.setTotal(page.getTotal());



        return ResponseResult.okResult(roleVoPage);
    }

    @Override
    public ResponseResult changeStatus(Role role) {

        LambdaUpdateWrapper<Role> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.eq(Role::getId,role.getId());
        updateWrapper.set(Role::getStatus,role.getStatus());
        update(updateWrapper);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(Role role) {
        Long[] menuIds = role.getMenuIds();
        save(role);
        if (menuIds != null && menuIds.length > 0 ){
            insertRoleMenu(role);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectRoleById(long id) {


        Role role = getById(id);
        RoleVo roleVo = BeanCopyUtils.copyBean(role, RoleVo.class);
        return ResponseResult.okResult(roleVo);
    }

    @Override
    @Transactional
    public ResponseResult updateRole(RoleVo roleVo) {
        Role role = BeanCopyUtils.copyBean(roleVo, Role.class);
        updateById(role);
        List<RoleMenu> roleMenus = new ArrayList<>();

        for (Long menuId:roleVo.getMenuIds()) {
            roleMenus.add(new RoleMenu(roleVo.getId(), menuId));
        }
        //roleMenuService.saveOrUpdateBatch(roleMenus);

        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,roleVo.getId());

        roleMenuService.remove(queryWrapper);
        roleMenuService.saveBatch(roleMenus);


        /*System.out.println(roleMenus);
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");*/

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRole(long id) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Role::getId,id);
        remove(queryWrapper);
        LambdaQueryWrapper<RoleMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(RoleMenu::getRoleId,id);
        roleMenuService.remove(lambdaQueryWrapper);
        return ResponseResult.okResult();
    }


    public void insertRoleMenu(Role role){

        List<RoleMenu> roleMenus = Arrays.stream(role.getMenuIds())
                .map(menuId -> new RoleMenu(role.getId(),menuId))
                .collect(Collectors.toList());

        roleMenuService.saveBatch(roleMenus);

    }


}
