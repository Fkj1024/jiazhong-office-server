package com.jiazhong.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDto implements Serializable {
    private Integer P_id;
    private String p_name;
    private Integer m_id;
    private String m_name;
    private String m_url;
}
