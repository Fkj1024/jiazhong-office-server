package com.jiazhong.service.impl;

import com.jiazhong.mapper.SecurityEmpMapper;
import com.jiazhong.model.Emp;
import com.jiazhong.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private SecurityEmpMapper empMapper;
    @Override
    public Emp getEmpByEmpId(int empId) {
        return empMapper.selectById(empId);
    }
}
