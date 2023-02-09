package com.jiazhong.service;

import com.jiazhong.model.ModuleMenuView;

import java.util.List;

public interface ModuleService {
    /**
     * 根据用户编号获得用户所对应的权限列表信息
     * @param account_id
     * @return
     */
    public List<ModuleMenuView> getModuleMenuByAccountId(int account_id);

}
