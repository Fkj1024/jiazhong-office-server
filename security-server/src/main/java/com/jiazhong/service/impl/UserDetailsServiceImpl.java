package com.jiazhong.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiazhong.mapper.SecurityAccountMapper;
import com.jiazhong.model.Account;
import com.jiazhong.model.CurAccount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SecurityAccountMapper securityAccountMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据账户名获得账户对象
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account_name",username);
        Account account = securityAccountMapper.selectOne(queryWrapper);

        //检测账户名是否正确
        if(account==null){
           /* log.info("账户名不存在");
            return null;*/
            throw new UsernameNotFoundException("账户名不存在");
        }
        /*if(account.getAccount_status()==-1){
            throw new DisabledException("账户不可用!");
        }
        if(account.getAccount_status() == 1){
            throw new LockedException("账户被冻结!");
        }*/

        //将Account对象中的数据转存到CurAccount对象
        CurAccount curAccount = new CurAccount(account.getAccount_id(),account.getAccount_name(),account.getEmp_id());
        //创建当前账户所拥有的权限集合
        List<SimpleGrantedAuthority> authoritys = new ArrayList<>();
        authoritys.add(new SimpleGrantedAuthority("USER"));
        //创建User对象
        User user = new User(
                JSONObject.toJSONString(curAccount),//存放认证成功后要使用的数据
                account.getAccount_password(),
                account.getAccount_status()==-1?false:true,
                true,
                true,
                account.getAccount_status()==1?false:true,
                authoritys
        );



        return user;
    }
}
