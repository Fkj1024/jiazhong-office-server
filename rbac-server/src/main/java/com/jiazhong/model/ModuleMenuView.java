package com.jiazhong.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 权限菜单类，对应视图层的菜单
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleMenuView implements Serializable {
    private Integer menuId;//菜单编号
    private String menuName;//菜单名
    private String menuUrl;//菜单地址

    private List<ModuleMenuView> subMenuList;//当前菜单的子菜单列表
}
