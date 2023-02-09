package com.jiazhong.controller;

import com.jiazhong.model.Customer;
import com.jiazhong.model.Emp;
import com.jiazhong.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/market/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @Resource
    private RedisTemplate redisTemplate;
    @GetMapping("/getCustomer")
    public List<Customer> getCustomer(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        Map<String,Object> curAccountMap = (Map<String, Object>) redisTemplate.opsForValue().get(token);
        Emp emp = (Emp) curAccountMap.get("curEmp");
        System.out.println(emp);
        return customerService.getCustomer(emp.getEmp_id());
    }
}
