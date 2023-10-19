package com.yujian.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yujian.domain.entity.Menu;
import com.yujian.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-10-10 14:55:53
 */
public interface MenuService extends IService<Menu> {

    List<String> selectPermsKeyByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}

