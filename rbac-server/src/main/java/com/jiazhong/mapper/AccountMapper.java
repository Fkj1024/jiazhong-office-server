package com.jiazhong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiazhong.commons.Result;
import com.jiazhong.model.Account;
import com.jiazhong.model.AccountView;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    public List<AccountView> getAccountInfo();

    @Select("select role_id from tbl_account_role where account_id=#{account_id}")
    public List<Integer> queryRoleIdByAccountId(int account_id);

    /**
     * 根据账户编号从tbl_account_role表中删除该账户的所有角色
     * @param account_id
     */
    @Delete("delete from tbl_account_role where account_id=#{account_id}")
    public void delRoleByAccountId(int account_id);

    public void addAccountRole(@Param("account_id") int account_id, @Param("role_ids") int[] role_ids);
}
