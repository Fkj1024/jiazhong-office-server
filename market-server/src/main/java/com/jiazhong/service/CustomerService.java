package com.jiazhong.service;

import com.jiazhong.model.Customer;

import java.util.List;

public interface CustomerService {
    public List<Customer> getCustomer(int empId);
}
