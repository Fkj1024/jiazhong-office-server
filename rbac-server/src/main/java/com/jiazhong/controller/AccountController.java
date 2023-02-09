package com.jiazhong.controller;

import com.jiazhong.commons.Result;
import com.jiazhong.exctions.SysException;
import com.jiazhong.model.AccountView;
import com.jiazhong.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/rbac/users")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/getAccountInfo")
    public List<AccountView> getAccountInfo(){
        return accountService.getAccountInfo();
    }

    @PutMapping("/updateStatus")
    public Result updateStatus(int status_code,int account_id){

        try {
            accountService.updateStatus(status_code,account_id);
            return Result.success("状态更新成功!");
        }catch (Exception e){
            return Result.fail(500);
        }
    }
    @GetMapping("/queryRoleIdByAccountId")
    public List<Integer> queryRoleIdByAccountId(int account_id){
        return accountService.queryRoleIdByAccountId(account_id);

    }
    @PutMapping("/updateAccountRole")
    public Result updateAccountRole(int account_id,int[] role_ids){
        try {
            accountService.updateAccountRole(account_id,role_ids);
            return Result.success("账户授权成功...");
        } catch (SysException e) {
            e.printStackTrace();
            return Result.fail(500, e.getMessage());
        }

    }
}
