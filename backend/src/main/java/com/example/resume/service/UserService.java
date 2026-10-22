package com.example.resume.service;

import com.example.resume.common.BizException;
import com.example.resume.entity.User;
import com.example.resume.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    public String register(String username, String password, String nickname) {
        if (username == null || username.trim().length() < 3) {
            throw new BizException("用户名至少 3 个字符");
        }
        if (password == null || password.length() < 6) {
            throw new BizException("密码至少 6 位");
        }
        if (userMapper.selectByUsername(username) != null) {
            throw new BizException("用户名已存在");
        }
        User user = new User();
        user.setUsername(username);
        user.setNickname(nickname == null || nickname.isEmpty() ? username : nickname);
        user.setPassword(passwordEncoder.encode(password));
        user.setToken(UUID.randomUUID().toString());
        userMapper.insert(user);
        return user.getToken();
    }

    public String login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BizException("用户名或密码错误");
        }
        String token = UUID.randomUUID().toString();
        userMapper.updateToken(user.getId(), token);
        return token;
    }

    public void logout(Long userId) {
        userMapper.updateToken(userId, null);
    }

    public User info(Long userId) {
        User user = userMapper.selectById(userId);
        user.setPassword(null);
        user.setToken(null);
        return user;
    }
}
