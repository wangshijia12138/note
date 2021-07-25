package com.wangshijia.dao;

import com.wangshijia.pojo.User;

import java.util.List;

public interface UserMapper {
    // 获取全部用户
    List<User> getUserList();

    //根据id查询用户
    User getUserById(int id);

    //根据id查询用户
    int addUser(User user);

    //根据id查询用户
    int updateUser(User user);

    //根据id查询用户
    int deleteUserById(int id);
}
