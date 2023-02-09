package com.jiazhong.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer implements Serializable {
    @TableId
    private Integer customer_id;
    private String customer_name;
    private Integer customer_sex;
    private String customer_phone;
    private String customer_weixin;
    private String customer_qq;
    private Integer customer_education;
    private String customer_school;
    private String customer_major;
    private Integer customer_grade;
    private Integer customer_graduation_time;
    private String customer_consult_course;
    private Integer customer_source;
    private Date input_time;
    private Integer customer_state;
    private Integer emp_id;

}
