package com.jiazhong.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountView implements Serializable {
    private Integer account_id;
    private String account_name;
    private String emp_name;
    private String dept_name;
    private String post_name;
    private Integer account_status;

}
