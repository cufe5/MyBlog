package com.yujian.utils;

import com.yujian.domain.entity.Menu;
import com.yujian.domain.vo.MenuTreeVo;
import com.yujian.domain.vo.MenuVo;

import java.util.List;
import java.util.stream.Collectors;

//新增角色-获取菜单下拉树列表
public class SystemConverter {

    private SystemConverter(){}

    public static List<MenuTreeVo> buildMenuSelectTree(List<Menu> menus){
        List<MenuTreeVo> menuTreeVos = menus.stream()
                .map(m -> new MenuTreeVo(m.getId(), m.getMenuName(), m.getParentId(), m.getMenuType(),null))
                .collect(Collectors.toList());

        List<MenuTreeVo> options = menuTreeVos.stream()
                .filter(o -> !(o.getParentId().equals(0L)))
                .map(o -> o.setChildren(getChildrenList(menuTreeVos, o)))
                .collect(Collectors.toList());
        return options;
    }

    //得到子节点列表
    public static List<MenuTreeVo> getChildrenList(List<MenuTreeVo> list,MenuTreeVo menuTreeVo){
        List<MenuTreeVo> menuTreeVos = list.stream()
                .filter(o -> o.getParentId().equals(menuTreeVo.getId()))
                .map(o -> o.setChildren(getChildrenList(list, o)))
                .collect(Collectors.toList());
        return menuTreeVos;
    }

}
