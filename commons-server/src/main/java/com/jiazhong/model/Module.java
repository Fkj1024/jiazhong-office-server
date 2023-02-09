package com.jiazhong.model;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Module implements Serializable {
    @TableId
    private Integer module_id;
    private Integer parent_module_id;
    private String module_name;
    private String module_url;
    private Integer module_is_menu;

}
