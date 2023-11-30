package com.yujian.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yujian.constants.SystemConstants;
import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Menu;
import com.yujian.domain.entity.RoleMenu;
import com.yujian.domain.vo.MenuTreeVo;
import com.yujian.domain.vo.MenuVo;
import com.yujian.domain.vo.RoleMenuTreeVo;
import com.yujian.enums.AppHttpCodeEnum;
import com.yujian.mapper.MenuMapper;
import com.yujian.service.MenuService;
import com.yujian.service.RoleMenuService;
import com.yujian.utils.SecurityUtils;
import com.yujian.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-10-10 14:55:54
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectPermsKeyByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if(id.equals(1L)){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus,SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());

            return perms;


        }

        //否则返回去所具有的权限
        return getBaseMapper().selectPermsByUserId(id);

    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<MenuVo> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是， 返回所有符合要求的Menu
             menus = menuMapper.selectAllRouterMenu();
        }else {
            //如果不是 返回当前用户所具有的权限
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
        //构建tree
        //先找出第一层的菜单，然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = builderMenuTree(menus,0L);

        return menuTree;

    }



    private List<MenuVo> builderMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());

        return menuTree;
    }

    /**
     * 获取存入参数的子Menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus) {
        List<MenuVo> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());

        return childrenList;
    }


    @Override
    //后台查看全部菜单管理
    public List<Menu> listAllMenu(String status, String menuName) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        wrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        wrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        List<Menu> menuList = list(wrapper);
        return menuList;
    }

    @Override
    //新增菜单
    public ResponseResult addMenu(Menu menu) {
        save(menu);
        return ResponseResult.okResult();
    }

    @Override
    //根据id查询菜单
    public ResponseResult selectMenuById(Long id) {
        Menu menu = getById(id);
        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenu(Menu menu) {
        if (menu.getId().equals(menu.getParentId())){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        updateById(menu);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,id);
        if (count(queryWrapper) != 0){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR,"该菜单存在子菜单不允许删除");
        }
        removeById(id);
        return ResponseResult.okResult();
    }
    @Override
    public ResponseResult selectRoleMenuTree(Long id) {
        List<MenuTreeVo> menuTreeVosTemp = SystemConverter.buildMenuSelectTree(listAllMenu(null, null));
        List<MenuTreeVo> menuTreeVos = menuTreeVosTemp.stream().filter(m -> (m.getMenuType().equals("C") || m.getMenuType().equals("M"))).collect(Collectors.toList());

        RoleMenuTreeVo roleMenuTreeVo = new RoleMenuTreeVo();
        roleMenuTreeVo.setMenus(menuTreeVos);
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenuList = roleMenuService.list(queryWrapper);



        Long[] menuIdsArray = roleMenuList.stream()
                .filter(i -> getById(i.getMenuId()).getMenuType().equals("C") || getById(i.getMenuId()).getMenuType().equals("M"))
                .map(RoleMenu::getMenuId).toArray(Long[]::new);
        roleMenuTreeVo.setCheckedKeys(menuIdsArray);


        return ResponseResult.okResult(roleMenuTreeVo);
    }



}
