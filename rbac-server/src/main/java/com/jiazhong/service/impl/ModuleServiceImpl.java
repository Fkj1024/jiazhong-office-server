package com.jiazhong.service.impl;

import com.jiazhong.mapper.ModuleMapper;
import com.jiazhong.model.ModuleDto;
import com.jiazhong.model.ModuleMenuView;
import com.jiazhong.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
@Service
public class ModuleServiceImpl implements ModuleService {
    @Autowired
    private ModuleMapper moduleMapper;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public List<ModuleMenuView> getModuleMenuByAccountId(int account_id) {
        /**
         * 1.从redis中获取权限列表
         * 2.判断redis中是否有权限列表，如果有则直接取用
         * 3.如果没有，访问数据库从数据库中获取权限列表
         * 4.获取到后存入redis中
         */
        HashOperations hashOperations = redisTemplate.opsForHash();
        //从redis中获取权限列表
        List<ModuleMenuView> menuList = (List<ModuleMenuView>) hashOperations.get("moduleMenu", "accountId:" + account_id);
        //判断redis中是否有权限列表
        if (menuList == null || menuList.size()==0){
            System.out.println("从数据库中获取权限列表.....");
            //根据账户信息获得账户所对应的权限信息
            List<ModuleDto> moduleList = moduleMapper.getModuleByAccountId(account_id);
            //将对象封装到List集合中，集合中存放的是moduleMenuView对象
            menuList = new ArrayList<>();
            //设置一个主菜单对象
            ModuleMenuView mainMenu = null;
            for (ModuleDto moduleDto :moduleList){
                if (mainMenu == null || !moduleDto.getP_id().equals(mainMenu.getMenuId())){
                    //创建主菜单
                    mainMenu = new ModuleMenuView();
                    //设置主菜单中相关属性
                    mainMenu.setMenuId(moduleDto.getP_id());
                    mainMenu.setMenuName(moduleDto.getP_name());

                    //设置当前菜单的子菜单列表
                    mainMenu.setSubMenuList(new ArrayList<>());

                    //将主菜单添加到list集合中
                    menuList.add(mainMenu);
                }
                //设置子菜单
                //创建子菜单对象
                ModuleMenuView subMenu = new ModuleMenuView();
                subMenu.setMenuId(moduleDto.getM_id());
                subMenu.setMenuName(moduleDto.getM_name());
                subMenu.setMenuUrl(moduleDto.getM_url());

                //将子菜单设置到主菜单中
                mainMenu.getSubMenuList().add(subMenu);
            }
            //将权限列表存入到redis中
            hashOperations.put("moduleMenu","accountId:"+account_id,menuList);
        }



        return menuList;
    }
}
