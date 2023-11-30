package com.yujian.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Accessors(chain = true)
public class RoleMenuTreeVo {
    private List<MenuTreeVo> menus;
    private Long[] checkedKeys;
}
