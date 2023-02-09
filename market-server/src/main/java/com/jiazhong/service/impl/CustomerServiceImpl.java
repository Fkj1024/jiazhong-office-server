package com.jiazhong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiazhong.mapper.CustomerMapper;
import com.jiazhong.model.Customer;
import com.jiazhong.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<Customer> getCustomer(int empId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("emp_id",empId);
        queryWrapper.ne("customer_state",1);
        queryWrapper.orderByDesc("input_time");
        return customerMapper.selectList(queryWrapper);

    }
}
