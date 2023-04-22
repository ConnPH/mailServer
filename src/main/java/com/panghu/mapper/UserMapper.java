package com.panghu.mapper;

import com.panghu.mode.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    User selectUserList();

    User selectUserById(@Param("id") Integer id);

    int saveUser(User user);
}
