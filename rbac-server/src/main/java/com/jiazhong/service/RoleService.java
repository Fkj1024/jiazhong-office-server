package com.jiazhong.service;

import com.jiazhong.model.Role;

import java.util.List;

public interface RoleService {
    /**
     * 获得所有角色列表
     * @return
     */
    public List<Role> getRoles();
}
