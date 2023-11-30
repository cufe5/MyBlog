package com.yujian.controller;

import com.yujian.domain.ResponseResult;
import com.yujian.domain.entity.Menu;
import com.yujian.domain.vo.MenuTreeVo;
import com.yujian.service.MenuService;
import com.yujian.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping("/list")
    public ResponseResult listAllMenu(String status,String menuName){
        List<Menu> menus = menuService.listAllMenu(status, menuName);
        return ResponseResult.okResult(menus);
    }

    @PostMapping()
    public ResponseResult addMenu(@RequestBody Menu menu){
        return menuService.addMenu(menu);
    }

    @GetMapping("/{id}")
    public ResponseResult selectMenuById(@PathVariable("id") Long id){
        return menuService.selectMenuById(id);
    }
    @PutMapping()
    public ResponseResult updateMenu(@RequestBody Menu menu){
        return menuService.updateMenu(menu);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteMenu(@PathVariable("id") Long id){
        return menuService.deleteMenu(id);
    }
    @GetMapping("/treeselect")
    public ResponseResult treeSelect(){
        //复用之前的查询全部菜单的方法，该方法的参数是用来模糊查询，获取全部则不需要参数
        List<Menu> menus = menuService.listAllMenu(null, null);

        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        List<MenuTreeVo> collect = menuTreeVos.stream().filter(m -> (m.getMenuType().equals("C") || m.getMenuType().equals("M"))).collect(Collectors.toList());
        return ResponseResult.okResult(collect);
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id") Long id){
        return menuService.selectRoleMenuTree(id);
    }
}
