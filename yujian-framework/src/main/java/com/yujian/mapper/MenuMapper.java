package com.yujian.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yujian.domain.entity.Menu;
import com.yujian.domain.vo.MenuVo;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-10-10 14:54:17
 */
public interface MenuMapper extends BaseMapper<Menu> {

    List<String> selectPermsByUserId(Long userId);

    List<MenuVo> selectAllRouterMenu();

    List<MenuVo> selectRouterMenuTreeByUserId(Long userId);
}

