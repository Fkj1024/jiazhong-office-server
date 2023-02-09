package com.jiazhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiazhong.model.Module;
import com.jiazhong.model.ModuleDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface ModuleMapper extends BaseMapper<Module> {
    /**
     * 根据用户编号获得用户所对应的权限信息
     * @param account_id
     * @return
     */
    public List<ModuleDto> getModuleByAccountId(int account_id);
}
