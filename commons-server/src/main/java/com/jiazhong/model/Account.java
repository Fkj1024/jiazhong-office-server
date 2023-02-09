package com.jiazhong.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data

public class Account implements Serializable {
    @TableId
    private Integer account_id;
    private String account_name;
    private String account_password;
    private Integer account_status;
    private Integer account_is_first;
    private Integer emp_id;

}
