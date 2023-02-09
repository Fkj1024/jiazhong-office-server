package com.jiazhong.service.impl;

import com.jiazhong.mapper.RoleMapper;
import com.jiazhong.model.Role;
import com.jiazhong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public List<Role> getRoles() {
        return roleMapper.selectList(null);
    }
}
