package com.jiazhong.controller;

import com.jiazhong.model.Role;
import com.jiazhong.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/rbac/role")
public class RoleController {
    @Resource
    private RoleService roleService;
    @GetMapping("/getRoles")
    public List<Role> getRoles() {
        return roleService.getRoles();
    }
}
