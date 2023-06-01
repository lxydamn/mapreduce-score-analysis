package com.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.backend.mapper.UserMapper;
import com.example.backend.pojo.User;
import com.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> login(String username, String password) {
        Map<String,String> resp = new HashMap<>();

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("username", username);

        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            resp.put("error_info", "用户名不存在");
            return resp;
        }

        if (! password.equals(user.getPassword())) {
            resp.put("error_info", "用户名或密码错误");
            return resp;
        }

        resp.put("error_info", "success");
        resp.put("id", String.valueOf(user.getId()));
        return resp;
    }

    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String, String> resp = new HashMap<>();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        User user = userMapper.selectOne(queryWrapper.eq("username", username));

        if (user != null) {
            resp.put("error_info", "用户名重复");
            return resp;
        }

        if (username.length() == 0 || username.length() > 20) {
            resp.put("error_info", "用户名过长");
            return resp;
        }

        if (password.length() == 0 || password.length() > 20) {
            resp.put("error_info", "密码过长");
            return resp;
        }

        if (!password.equals(confirmedPassword)) {
            resp.put("error_info", "两次输入密码不一致");
            return resp;
        }

        userMapper.insert(new User(null, username, password));

        resp.put("error_info", "success");
        return resp;
    }
}
