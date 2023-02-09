package com.jiazhong.controller;

import com.jiazhong.model.CurAccount;
import com.jiazhong.model.ModuleMenuView;
import com.jiazhong.service.AccountService;
import com.jiazhong.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/module")
public class ModuleController {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AccountService accountService;
    @GetMapping("/getMenu")
    public List<ModuleMenuView> getMenuByAccount(HttpServletRequest request){
        //获得客户端传入的token
        String token = request.getHeader("Authorization");
        //从redis中获取token对应的数据
        Map<String, Object> curAccountMap = accountService.getAccountByTokenForRedis(token);
        int account_id = ((CurAccount)curAccountMap.get("curAccount")).getAccount_id();
        List<ModuleMenuView> menuViewList = moduleService.getModuleMenuByAccountId(account_id);
//        menuViewList.forEach(System.out::println);
        return menuViewList;

    }
}
