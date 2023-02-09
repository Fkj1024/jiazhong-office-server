package com.jiazhong.service.impl;

import com.jiazhong.exctions.SysException;
import com.jiazhong.mapper.AccountMapper;
import com.jiazhong.model.Account;
import com.jiazhong.model.AccountView;
import com.jiazhong.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
@Transactional//事务注解，加上该注解后，service中的所有方法都是一个独立的事务
public class AccountServiceImpl implements AccountService {
    @Resource
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public Map<String, Object> getAccountByTokenForRedis(String token) {
        Map<String, Object> curAccountMap = (Map<String, Object>) redisTemplate.opsForValue().get(token);
        return curAccountMap;
    }

    @Override
    public List<AccountView> getAccountInfo() {
        return accountMapper.getAccountInfo();
    }

    @Override
    public void updateStatus(int status_code, int account_id) {
        Account account = new Account();
        account.setAccount_status(status_code);
        account.setAccount_id(account_id);
        accountMapper.updateById(account);
    }

    @Override
    public List<Integer> queryRoleIdByAccountId(int account_id) {
        List<Integer> roleIds = accountMapper.queryRoleIdByAccountId(account_id);
        System.out.println("((((((((((((((((((");
        System.out.println(roleIds);
        System.out.println("))))))))))))))))))");
        return roleIds;
    }

    @Override
    public void updateAccountRole(int account_id, int[] role_ids) throws SysException {
        accountMapper.delRoleByAccountId(account_id);
        accountMapper.addAccountRole(account_id,role_ids);

        //将当前账户的菜单从redis中移除
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.delete("moduleMenu","accountId:"+account_id);
    }
}
