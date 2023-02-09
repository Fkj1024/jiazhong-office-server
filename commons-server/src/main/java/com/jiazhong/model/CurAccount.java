package com.jiazhong.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurAccount implements Serializable {
    private Integer account_id;
    private String account_name;
    private Integer emp_id;
}
