package com.jiazhong.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emp implements Serializable {
    @TableId
    private Integer emp_id;
    private String emp_name;
    private Integer emp_sex;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date emp_birthday;
    private String emp_phone;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date emp_entry_time;
    private Integer emp_status;
    private Integer post_id;

}
