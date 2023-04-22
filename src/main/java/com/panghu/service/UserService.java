package com.panghu.service;

import com.panghu.mode.User;

public interface UserService {
    int save(User user);

    User selectUserById(Integer userId);

}
