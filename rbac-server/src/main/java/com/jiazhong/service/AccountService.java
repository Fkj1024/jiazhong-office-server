package com.jiazhong.service;

import com.jiazhong.exctions.SysException;
import com.jiazhong.model.AccountView;

import java.util.List;
import java.util.Map;

public interface AccountService {
    /**
     * 从redis中获取账户信息
     */
    public Map<String,Object> getAccountByTokenForRedis(String token);

    /**
     * 获得账户信息
     * @return
     */
    public List<AccountView> getAccountInfo();

    public void updateStatus(int status_code,int account_id) throws Exception;

    public List<Integer> queryRoleIdByAccountId(int account_id);

    void updateAccountRole(int account_id, int[] role_ids) throws SysException;
}
